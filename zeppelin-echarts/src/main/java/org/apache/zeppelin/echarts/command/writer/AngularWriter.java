package org.apache.zeppelin.echarts.command.writer;

import org.apache.zeppelin.echarts.utils.OutputCombiner;
import org.apache.zeppelin.echarts.utils.PropertyGetter;
import org.apache.zeppelin.interpreter.InterpreterContext;
import org.apache.zeppelin.interpreter.InterpreterResult;

/**
 * Created by Ethan Xiao on 2017/1/12.
 */
public class AngularWriter extends Writer<String, InterpreterResult> {

	/**
	 * 紧跟命令后的angular,附加在前一条命令的结果后一起输出
	 */
	private String angular;

	public void setParameters(String[] parameters) {
		//没有参数
	}

	public void addPara(String name, String[] options, String body) {

	}

	public void setBody(String body) {
		this.angular = body;
	}

	public InterpreterResult execute(String input, PropertyGetter propertyGetter, InterpreterContext interpreterContext) {
		return new InterpreterResult(InterpreterResult.Code.SUCCESS, InterpreterResult.Type.ANGULAR, OutputCombiner.combine(input, angular));
	}
}
