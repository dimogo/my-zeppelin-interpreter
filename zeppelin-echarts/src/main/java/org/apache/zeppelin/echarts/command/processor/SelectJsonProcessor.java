package org.apache.zeppelin.echarts.command.processor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.zeppelin.echarts.utils.PropertyGetter;
import org.apache.zeppelin.interpreter.InterpreterContext;

/**
 * Created by Ethan Xiao on 2017/1/16.
 */
public class SelectJsonProcessor extends Processor<Object, Object> {

	/**
	 * 已经通过空字符分割的选择路径, 这么做是因为在解析命令时已经对参数进行清洗并分割, 避免重复操作
	 */
	private String[] path;

	public void setParameters(String[] parameters) {
		this.path = parameters;
	}

	public void addPara(String name, String[] options, String body) {

	}

	public void setBody(String body) {

	}

	public Object execute(Object input, PropertyGetter propertyGetter, InterpreterContext interpreterContext) {
		if (input == null) {
			throw new RuntimeException("input object of json error");
		}
		if (path == null || path.length == 0) {
			throw new RuntimeException("json select path error");
		}
		JSONArray array;
		JSONObject object;
		Object selected = input;
		try {
			for (String node : path) {
				if (node.startsWith("[") && node.endsWith("]")) {
					array = (JSONArray)selected;
					selected = array.get(Integer.parseInt(node.replaceAll("\\[", "").replaceAll("\\]", "")));
				} else {
					object = (JSONObject)selected;
					selected = object.get(node);
				}
			}
			return selected;
		} catch (Throwable e) {
			throw new RuntimeException("json select path error", e);
		}
	}
}
