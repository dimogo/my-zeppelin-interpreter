package org.apache.zeppelin.echarts.command.processor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.zeppelin.echarts.command.Table;
import org.apache.zeppelin.echarts.utils.PropertyGetter;
import org.apache.zeppelin.interpreter.InterpreterContext;

/**
 * Created by Ethan Xiao on 2017/2/18.
 */
public class Table2JsonProcessor extends Processor<Table, JSONArray> {
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
	public JSONArray execute(Table input, PropertyGetter propertyGetter, InterpreterContext interpreterContext) {
		if (input == null) {
			throw new RuntimeException("table can not be null");
		}
		JSONArray table = new JSONArray();
		String[] columns = input.getColumns();
		for (String[] row : input.getRows()) {
			JSONObject rowObj = new JSONObject();
			for (int i = 0; i < row.length; i++) {
				rowObj.put(columns[i], row[i]);
			}
			table.add(rowObj);
		}
		return table;
	}
}
