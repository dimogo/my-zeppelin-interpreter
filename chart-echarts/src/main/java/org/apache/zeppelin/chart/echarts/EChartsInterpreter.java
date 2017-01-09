package org.apache.zeppelin.chart.echarts;

import org.apache.zeppelin.interpreter.Interpreter;
import org.apache.zeppelin.interpreter.InterpreterContext;
import org.apache.zeppelin.interpreter.InterpreterResult;
import org.apache.zeppelin.scheduler.Scheduler;
import org.apache.zeppelin.scheduler.SchedulerFactory;

import java.util.Properties;

/**
 * Created by Ethan Xiao on 2017/1/9.
 */
public class EChartsInterpreter extends Interpreter {
	private final String CONCURRENT_EXECUTION_KEY = "zeppelin.chart.echarts.concurrent.use";
	private final String CONCURRENT_EXECUTION_COUNT = "zeppelin.chart.echarts.concurrent.max_connection";
	private final String ECHARTS_PLUGIN_EXECUTION_KEY = "zeppelin.chart.echarts.plugin.url.echarts";
	private final String JQUERY_PLUGIN_EXECUTION_KEY = "zeppelin.chart.echarts.plugin.url.jquery";
	private final String BOOTSTRAP_STYLE_PLUGIN_EXECUTION_KEY = "zeppelin.chart.echarts.plugin.url.bootstrap.style";
	private final String BOOTSTRAP_THEME_STYLE_PLUGIN_EXECUTION_KEY = "zeppelin.chart.echarts.plugin.url.bootstrap.theme.style";
	private final String BOOTSTRAP_PLUGIN_EXECUTION_KEY = "zeppelin.chart.echarts.plugin.url.bootstrap";

	public EChartsInterpreter(Properties property) {
		super(property);
	}

	@Override
	public void open() {

	}

	@Override
	public void close() {

	}

	@Override
	public InterpreterResult interpret(String cmd, InterpreterContext interpreterContext) {
		StringBuilder content = new StringBuilder();
		content.append("<script type=\"text/javascript\" src=\"").append(getEchartsURL()).append("\"></script>");
		content.append("<script type=\"text/javascript\" src=\"").append(getJqeuryURL()).append("\"></script>");
		content.append("<link rel=\"stylesheet\" href=\"").append(getBootstrapStyleURL()).append("\" />");
		content.append("<link rel=\"stylesheet\" href=\"").append(getBootstrapThemeStyleURL()).append("\" />");
		content.append("<script type=\"text/javascript\" src=\"").append(getBootstrapURL()).append("\"></script>");

		content.append("<link rel=\"stylesheet\" href=\"/plugins/zeppelin-echarts/css/zeppelin-echarts.css\" />");
		content.append("<script type=\"text/javascript\" src=\"/plugins/zeppelin-echarts/js/zeppelin-echarts.js\"></script>");
		content.append(cmd);
		return new InterpreterResult(InterpreterResult.Code.SUCCESS, InterpreterResult.Type.HTML, content.toString());
	}

	@Override
	public void cancel(InterpreterContext interpreterContext) {

	}

	@Override
	public FormType getFormType() {
		return FormType.SIMPLE;
	}

	@Override
	public int getProgress(InterpreterContext interpreterContext) {
		return 0;
	}

	@Override
	public Scheduler getScheduler() {
		String schedulerName = EChartsInterpreter.class.getName() + this.hashCode();
		return isConcurrentExecution() ?
				SchedulerFactory.singleton().createOrGetParallelScheduler(schedulerName,
						getMaxConcurrentConnection())
				: SchedulerFactory.singleton().createOrGetFIFOScheduler(schedulerName);
	}

	boolean isConcurrentExecution() {
		return Boolean.valueOf(getProperty(CONCURRENT_EXECUTION_KEY));
	}

	int getMaxConcurrentConnection() {
		try {
			return Integer.valueOf(getProperty(CONCURRENT_EXECUTION_COUNT));
		} catch (Exception e) {
			return 10;
		}
	}

	String getEchartsURL() {
		try {
			return getProperty(ECHARTS_PLUGIN_EXECUTION_KEY);
		} catch (Exception e) {
			return "/plugins/echarts/echarts-3.3.2.min.js";
		}
	}

	String getJqeuryURL() {
		try {
			return getProperty(JQUERY_PLUGIN_EXECUTION_KEY);
		} catch (Exception e) {
			return "/plugins/jquery/jquery-3.1.1.min.js";
		}
	}

	String getBootstrapStyleURL() {
		try {
			return getProperty(BOOTSTRAP_STYLE_PLUGIN_EXECUTION_KEY);
		} catch (Exception e) {
			return "/plugins/bootstrap/3.3.0/css/bootstrap.min.css";
		}
	}

	String getBootstrapThemeStyleURL() {
		try {
			return getProperty(BOOTSTRAP_THEME_STYLE_PLUGIN_EXECUTION_KEY);
		} catch (Exception e) {
			return "/plugins/bootstrap/3.3.0/css/bootstrap-theme.min.css";
		}
	}

	String getBootstrapURL() {
		try {
			return getProperty(BOOTSTRAP_PLUGIN_EXECUTION_KEY);
		} catch (Exception e) {
			return "/plugins/bootstrap/3.3.0/js/bootstrap.min.js";
		}
	}
}
