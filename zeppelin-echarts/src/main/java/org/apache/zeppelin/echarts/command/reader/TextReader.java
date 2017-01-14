package org.apache.zeppelin.echarts.command.reader;

import org.apache.zeppelin.echarts.utils.PropertyGetter;
import org.apache.zeppelin.interpreter.InterpreterContext;

/**
 * Created by Ethan Xiao on 2017/1/12.
 */
public class TextReader extends Reader<String, String> {
	/**
	 * 命令后紧跟的文本,并作为输出
	 * @param parameters
	 */
	private String text;

	public void setParameters(String[] parameters) {
		//没有输出
	}

	public void addPara(String name, String[] options, String body) {

	}

	public void setBody(String body) {
		this.text = body;
	}

	public String execute(String input, PropertyGetter propertyGetter, InterpreterContext interpreterContext) {
		return this.text;
	}
}
