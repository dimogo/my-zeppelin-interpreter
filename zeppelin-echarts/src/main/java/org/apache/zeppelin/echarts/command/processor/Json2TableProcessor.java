package org.apache.zeppelin.echarts.command.processor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.zeppelin.echarts.command.Table;
import org.apache.zeppelin.echarts.utils.PropertyGetter;
import org.apache.zeppelin.interpreter.InterpreterContext;

/**
 * Created by Ethan Xiao on 2017/2/18.
 */
public class Json2TableProcessor extends Processor<JSONArray, Table> {
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
	public Table execute(JSONArray input, PropertyGetter propertyGetter, InterpreterContext interpreterContext) {
		Table table = new Table();
		for (int i = 0; i < input.size(); i++) {
			JSONObject rowObj = input.getJSONObject(i);
			String[] columns = table.getColumns();
			if (ArrayUtils.isEmpty(columns)) {
				columns = rowObj.keySet().toArray(new String[rowObj.keySet().size()]);
				table.setColumns(columns);
			}
			String[] values = new String[table.getColumns().length];
			for (int j = 0; j < columns.length; j++) {
				values[j] = rowObj.getString(columns[j]);
			}
			table.addRow(values);
		}
		return table;
	}
}
