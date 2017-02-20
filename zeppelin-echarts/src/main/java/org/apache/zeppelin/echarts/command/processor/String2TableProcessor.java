package org.apache.zeppelin.echarts.command.processor;

import org.apache.commons.lang3.StringUtils;
import org.apache.zeppelin.echarts.command.Table;
import org.apache.zeppelin.echarts.utils.PropertyGetter;
import org.apache.zeppelin.interpreter.InterpreterContext;

/**
 * Created by Ethan Xiao on 2017/2/18.
 */
public class String2TableProcessor extends Processor<String, Table> {
	private String body;

	@Override
	public void setParameters(String[] parameters) {

	}

	@Override
	public void addPara(String name, String[] options, String body) {

	}

	@Override
	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public Table execute(String input, PropertyGetter propertyGetter, InterpreterContext interpreterContext) {
		String tableString = input != null ? input : body;
		if (StringUtils.isBlank(tableString)) {
			throw new RuntimeException("table string is empty");
		}
		String[] array = tableString.split("\\n");
		Table table = new Table();
		for (int i = 0; i < array.length; i++) {
			if (i == 0) {
				table.setColumns(array[i].split("\\t"));
				continue;
			}
			table.addRow(array[i].split("\\t"));
		}
		return table;
	}

}
