package org.apache.zeppelin.echarts.command.processor;

import com.alibaba.fastjson.JSON;
import org.apache.zeppelin.echarts.utils.PropertyGetter;
import org.apache.zeppelin.interpreter.InterpreterContext;

/**
 * Created by Ethan Xiao on 2017/1/16.
 */
public class String2JsonProcessor extends Processor<String, Object> {

	/**
	 * 命令行的body,如果上一个执行器转入的input == null就使用body的内容进行转换
	 */
	private String body;

	public void setParameters(String[] parameters) {

	}

	public void addPara(String name, String[] options, String body) {

	}

	public void setBody(String body) {
		this.body = body;
	}

	public Object execute(String input, PropertyGetter propertyGetter, InterpreterContext interpreterContext) {
		if (body != null) {
			input = body;
		}
		if (input == null) {
			return null;
		}
		return JSON.parse(input);
	}
}
