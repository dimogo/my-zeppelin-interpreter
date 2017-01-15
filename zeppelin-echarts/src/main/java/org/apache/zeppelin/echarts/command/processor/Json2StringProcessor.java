package org.apache.zeppelin.echarts.command.processor;

import org.apache.zeppelin.echarts.utils.PropertyGetter;
import org.apache.zeppelin.interpreter.InterpreterContext;

/**
 * Created by Ethan Xiao on 2017/1/16.
 */
public class Json2StringProcessor extends Processor<Object, String> {
	public void setParameters(String[] parameters) {

	}

	public void addPara(String name, String[] options, String body) {

	}

	public void setBody(String body) {

	}

	public String execute(Object input, PropertyGetter propertyGetter, InterpreterContext interpreterContext) {
		return input.toString();
	}
}
