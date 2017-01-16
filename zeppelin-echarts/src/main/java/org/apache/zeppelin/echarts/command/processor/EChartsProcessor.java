package org.apache.zeppelin.echarts.command.processor;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.zeppelin.echarts.command.writer.Writer;
import org.apache.zeppelin.echarts.utils.OutputCombiner;
import org.apache.zeppelin.echarts.utils.PropertyGetter;
import org.apache.zeppelin.interpreter.InterpreterContext;
import org.apache.zeppelin.interpreter.InterpreterResult;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Ethan Xiao on 2017/1/12.
 * Writer for ECharts
 */
public class EChartsProcessor extends Processor<String, String> {

	private VelocityEngine ve = new VelocityEngine();
	private Map<String, String> optionSettings = new HashMap<String, String>();
	private int seriesCount = 1;

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
		if (parameters.length > 0) {
			this.seriesCount = Integer.parseInt(parameters[0]);
		}
	}

	public void addPara(String name, String[] options, String body) {
		if ("option".equalsIgnoreCase(name) && options != null && options.length > 0) {
			if (options.length > 1) {
				optionSettings.put(options[0], options[1]);
			} else {
				optionSettings.put(options[0], body);
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
			StringWriter writer = new StringWriter();
			template.merge(context, writer);
			return writer.toString();
		} catch (ResourceNotFoundException e) {
			throw new RuntimeException("vm path:" + this.vmPath, e);
		}
	}

}
