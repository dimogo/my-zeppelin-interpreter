package org.apache.zeppelin.echarts.command.writer;

import org.apache.zeppelin.echarts.utils.OutputCombiner;
import org.apache.zeppelin.echarts.utils.PropertyGetter;
import org.apache.zeppelin.interpreter.InterpreterContext;
import org.apache.zeppelin.interpreter.InterpreterResult;

/**
 * Created by Ethan Xiao on 2017/1/12.
 * Writer for ECharts
 */
public class EChartsWriter extends Writer<String, InterpreterResult> {

	/**
	 * 紧跟命令后的HTML代码,附加在前一条命令的结果后一起输出
	 */
	private String html;

	public void setParameters(String[] parameters) {
		//没有参数
	}

	public void setBody(String body) {
		this.html = body;
	}

	public InterpreterResult execute(String input, PropertyGetter propertyGetter, InterpreterContext interpreterContext) {
		StringBuilder content = new StringBuilder();
		content.append("<script type=\"text/javascript\" src=\"").append(propertyGetter.getEchartsURL()).append("\"></script>");
		content.append("<script type=\"text/javascript\" src=\"").append(propertyGetter.getJqeuryURL()).append("\"></script>");
		//content.append("<link rel=\"stylesheet\" href=\"").append(getBootstrapStyleURL()).append("\" />");
		//content.append("<link rel=\"stylesheet\" href=\"").append(getBootstrapThemeStyleURL()).append("\" />");
		content.append("<script type=\"text/javascript\" src=\"").append(propertyGetter.getBootstrapURL()).append("\"></script>");

		content.append("<link rel=\"stylesheet\" href=\"/plugins/zeppelin-echarts/css/zeppelin-echarts.css\" />");
		content.append("<script type=\"text/javascript\" src=\"/plugins/zeppelin-echarts/js/zeppelin-echarts.js\"></script>");
		return new InterpreterResult(InterpreterResult.Code.SUCCESS, InterpreterResult.Type.HTML, OutputCombiner.combine(content, input, this.html));
	}
}
