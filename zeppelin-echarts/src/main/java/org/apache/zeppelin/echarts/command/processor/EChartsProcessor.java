package org.apache.zeppelin.echarts.command.processor;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.zeppelin.echarts.utils.PropertyGetter;
import org.apache.zeppelin.interpreter.InterpreterContext;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * Created by Ethan Xiao on 2017/1/12.
 * Writer for ECharts
 */
public class EChartsProcessor extends Processor<String, String> {

	private VelocityEngine ve = new VelocityEngine();
	private Map<String, String> optionSettings = new HashMap<String, String>();
	private int seriesCount = 1;
	private int xAxisCount = 1;
	private int yAxisCount = 1;
	private String width = null;
	private String height = null;
	private boolean adminConsole = true;
	private boolean userConsole = false;
	private List<Serie> serieList = new LinkedList<Serie>();

	/**
	 * 紧跟命令后的HTML代码,附加在前一条命令的结果后一起输出
	 */
	private String html;

	private String vmPath = this.getClass().getResource("/").getPath();

	public EChartsProcessor() {
		Properties p = new Properties();
		try {
			p.load(this.getClass().getResourceAsStream("/velocity.properties"));
			if (!p.containsKey("file.resource.loader.path")) {
				p.put("file.resource.loader.path", vmPath);
			} else {
				vmPath = (String)p.get("file.resource.loader.path");
			}
			ve.init(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setParameters(String[] parameters) {
		if (parameters == null) {
			return;
		}
		if (parameters.length > 0) {
			this.seriesCount = Integer.parseInt(parameters[0]);
		}
		if (parameters.length > 1) {
			//xAxis
			this.xAxisCount = Integer.parseInt(parameters[1]);
		}
		if (parameters.length > 2) {
			//yAxis
			this.yAxisCount = Integer.parseInt(parameters[2]);
		}
	}

	public void addPara(String name, String[] options, String body) {
		if ("option".equalsIgnoreCase(name) && options != null && options.length > 0) {
			if (options.length > 1) {
				optionSettings.put(options[0], options[1]);
			} else {
				optionSettings.put(options[0], body);
			}
		} else if ("serie".equalsIgnoreCase(name) && options != null && options.length >= 4) {
			Serie serie = new Serie();
			serie.index = Integer.parseInt(options[0]);
			serie.name = options[1];
			serie.type = options[2];
			serie.fields = options[3];
			if (options.length >= 5) {
				serie.xAxisIndex = Integer.parseInt(options[4]);
			}
			if (options.length >= 6) {
				serie.yAxisIndex = Integer.parseInt(options[5]);
			}
			this.serieList.add(serie);

			int max = 0;
			for (Serie s : serieList) {
				if (max < s.index) {
					max = s.index;
				}
			}
			if (max > seriesCount) {
				seriesCount = max;
			}
		} else if ("width".equalsIgnoreCase(name) && options != null && options.length > 0) {
			this.width = options[0];
		} else if ("height".equalsIgnoreCase(name) && options != null && options.length > 0) {
			this.height = options[0];
		} else if ("console".equalsIgnoreCase(name) && options != null && options.length >= 2) {
			if ("admin".equalsIgnoreCase(options[0])) {
				this.adminConsole = "enable".equalsIgnoreCase(options[1]);
			}
			if ("user".equalsIgnoreCase(options[0])) {
				this.userConsole = "enable".equalsIgnoreCase(options[1]);
			}
		}
	}

	public void setBody(String body) {
		this.html = body;
	}

	/**
	 * ECharts图表代码生成
	 * @param input 输入的JSON格式原始数据
	 * @param propertyGetter 解释器配置参数
	 * @param interpreterContext
	 * @return ECharts图表HTML代码
	 */
	public String execute(String input, PropertyGetter propertyGetter, InterpreterContext interpreterContext) {
		try {
			Template template = ve.getTemplate("/vm/zeppelin-echarts-body.vm");
			VelocityContext context = new VelocityContext();
			context.put("ZeppelinEChartsJSUrl", propertyGetter.getEchartsURL());
			context.put("ZeppelinEChartsJQueryUrl", propertyGetter.getJqeuryURL());
			context.put("ZeppelinEChartsBootstrapURL", propertyGetter.getBootstrapURL());
			context.put("ZeppelinEChartsOriginJsonData", input);
			context.put("ZeppelinEChartsBodyFoot", this.html);
			context.put("ZeppelinEchartsOptionSettings", optionSettings);
			context.put("ZeppelinEchartsSeriesCount", this.seriesCount);
			context.put("ZeppelinECahrtsXAxisCount", this.xAxisCount);
			context.put("ZeppelinECahrtsYAxisCount", this.yAxisCount);
			context.put("ZeppelinEchartsSerieList", this.serieList);
			context.put("ZeppelinECahrtsWidth", this.width);
			context.put("ZeppelinECahrtsHeight", this.height);
			context.put("ZeppelinEChartsAdminConsole", this.adminConsole);
			context.put("ZeppelinEChartsUserConsole", this.userConsole);
			StringWriter writer = new StringWriter();
			template.merge(context, writer);
			return writer.toString();
		} catch (ResourceNotFoundException e) {
			throw new RuntimeException("vm path:" + this.vmPath, e);
		}
	}

	private static class Serie {
		private int index = 0;
		private String name = "";
		private String type = "line";
		private String fields = "";
		private int xAxisIndex = 0;
		private int yAxisIndex = 0;

		public String toString() {
			return "{index:" + index + ",name:\"" + name + "\",type:\"" + type + "\",fields:\"" + fields
					+ "\",xAxisIndex:" + xAxisIndex
					+ ",yAxisIndex:" + yAxisIndex
					+ "}";
		}
	}

}
