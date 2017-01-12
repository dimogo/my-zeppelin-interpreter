package org.apache.zeppelin.echarts.command.writer;

import org.apache.zeppelin.echarts.utils.OutputCombiner;
import org.apache.zeppelin.echarts.utils.PropertyGetter;
import org.apache.zeppelin.interpreter.InterpreterContext;
import org.apache.zeppelin.interpreter.InterpreterResult;

/**
 * Created by Ethan Xiao on 2017/1/12.
 */
public class SVGWriter extends Writer<String, InterpreterResult> {

	private String svg;

	public void setParameters(String[] parameters) {
		//没有参数
	}

	public void setBody(String body) {
		this.svg = body;
	}

	public InterpreterResult execute(String input, PropertyGetter propertyGetter, InterpreterContext interpreterContext) {
		return new InterpreterResult(InterpreterResult.Code.SUCCESS, InterpreterResult.Type.SVG, OutputCombiner.combine(input, svg));
	}
}
