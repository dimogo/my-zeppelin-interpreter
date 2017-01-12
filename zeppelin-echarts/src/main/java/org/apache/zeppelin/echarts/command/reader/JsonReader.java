package org.apache.zeppelin.echarts.command.reader;

import org.apache.zeppelin.echarts.utils.PropertyGetter;
import org.apache.zeppelin.interpreter.InterpreterContext;

/**
 * Created by Ethan Xiao on 2017/1/12.
 */
public class JsonReader extends Reader<String, String> {
	/**
	 * 命令后紧跟的JSON字符串并作为输出
	 * @param parameters
	 */
	private String json;

	public void setParameters(String[] parameters) {
		//没有参数
	}

	public void setBody(String body) {
		this.json = body;
	}

	public String execute(String input, PropertyGetter propertyGetter, InterpreterContext interpreterContext) {
		return this.json;
	}
}
