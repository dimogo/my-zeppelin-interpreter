package org.apache.zeppelin.echarts.utils;

import org.apache.zeppelin.interpreter.Interpreter;

/**
 * Created by Ethan Xiao on 2017/1/12.
 * 配置参数读取器,为便于在其他类中取得解析器的参数
 */
public class PropertyGetter {
	private final String CONCURRENT_EXECUTION_KEY = "zeppelin.echarts.concurrent.use";
	private final String CONCURRENT_EXECUTION_COUNT = "zeppelin.echarts.concurrent.max_connection";
	private final String ECHARTS_PLUGIN_EXECUTION_KEY = "zeppelin.echarts.plugin.url.echarts";
	private final String JQUERY_PLUGIN_EXECUTION_KEY = "zeppelin.echarts.plugin.url.jquery";
	private final String BOOTSTRAP_STYLE_PLUGIN_EXECUTION_KEY = "zeppelin.echarts.plugin.url.bootstrap.style";
	private final String BOOTSTRAP_THEME_STYLE_PLUGIN_EXECUTION_KEY = "zeppelin.echarts.plugin.url.bootstrap.theme.style";
	private final String BOOTSTRAP_PLUGIN_EXECUTION_KEY = "zeppelin.echarts.plugin.url.bootstrap";

	private Interpreter interpreter;

	public PropertyGetter(Interpreter interpreter) {
		this.interpreter = interpreter;
	}

	public boolean isConcurrentExecution() {
		return Boolean.valueOf(this.interpreter.getProperty(CONCURRENT_EXECUTION_KEY));
	}

	public int getMaxConcurrentConnection() {
		try {
			return Integer.valueOf(this.interpreter.getProperty(CONCURRENT_EXECUTION_COUNT));
		} catch (Exception e) {
			return 10;
		}
	}

	public String getEchartsURL() {
		try {
			return this.interpreter.getProperty(ECHARTS_PLUGIN_EXECUTION_KEY);
		} catch (Exception e) {
			return "/plugins/echarts/echarts-3.3.2.min.js";
		}
	}

	public String getJqeuryURL() {
		try {
			return this.interpreter.getProperty(JQUERY_PLUGIN_EXECUTION_KEY);
		} catch (Exception e) {
			return "/plugins/jquery/jquery-3.1.1.min.js";
		}
	}

	public String getBootstrapStyleURL() {
		try {
			return this.interpreter.getProperty(BOOTSTRAP_STYLE_PLUGIN_EXECUTION_KEY);
		} catch (Exception e) {
			return "/plugins/bootstrap/3.3.0/css/bootstrap.min.css";
		}
	}

	public String getBootstrapThemeStyleURL() {
		try {
			return this.interpreter.getProperty(BOOTSTRAP_THEME_STYLE_PLUGIN_EXECUTION_KEY);
		} catch (Exception e) {
			return "/plugins/bootstrap/3.3.0/css/bootstrap-theme.min.css";
		}
	}

	public String getBootstrapURL() {
		try {
			return this.interpreter.getProperty(BOOTSTRAP_PLUGIN_EXECUTION_KEY);
		} catch (Exception e) {
			return "/plugins/bootstrap/3.3.0/js/bootstrap.min.js";
		}
	}
}
