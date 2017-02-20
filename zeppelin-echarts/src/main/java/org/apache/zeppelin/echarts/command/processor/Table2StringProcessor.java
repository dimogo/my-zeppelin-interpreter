package org.apache.zeppelin.echarts.command.processor;

import org.apache.commons.lang3.StringUtils;
import org.apache.zeppelin.echarts.command.Table;
import org.apache.zeppelin.echarts.utils.PropertyGetter;
import org.apache.zeppelin.interpreter.InterpreterContext;

/**
 * Created by Ethan Xiao on 2017/2/18.
 */
public class Table2StringProcessor extends Processor<Table, String> {
	@Override
	public void setParameters(String[] parameters) {

	}

	@Override
	public void addPara(String name, String[] options, String body) {

	}

	@Override
	public void setBody(String body) {

	}

	@Override
	public String execute(Table input, PropertyGetter propertyGetter, InterpreterContext interpreterContext) {
		StringBuilder tableString = new StringBuilder();
		tableString.append(StringUtils.join(input.getColumns(), "\t"));
		for (String[] row : input.getRows()) {
			tableString.append("\n").append(StringUtils.join(row, "\t"));
		}
		return tableString.toString();
	}
}
