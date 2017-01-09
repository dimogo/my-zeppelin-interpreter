package org.apache.zeppelin.jdbc;

import org.apache.zeppelin.interpreter.Interpreter;
import org.apache.zeppelin.interpreter.InterpreterContext;
import org.apache.zeppelin.interpreter.InterpreterResult;
import org.apache.zeppelin.scheduler.Scheduler;
import org.apache.zeppelin.scheduler.SchedulerFactory;

import java.util.Properties;

/**
 * Created by Ethan Xiao on 2017/1/9.
 */
public class JDBCEChartsInterpreter extends Interpreter {
	private final String CONCURRENT_EXECUTION_KEY = "zeppelin.jdbc.echarts.concurrent.use";
	private final String CONCURRENT_EXECUTION_COUNT = "zeppelin.jdbc.echarts.concurrent.max_connection";
	private final String ECHARTS_PLUGIN_EXECUTION_KEY = "zeppelin.jdbc.echarts.plugin.url";

	public JDBCEChartsInterpreter(Properties property) {
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
		return new InterpreterResult(InterpreterResult.Code.SUCCESS, InterpreterResult.Type.HTML, cmd);
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
		String schedulerName = JDBCEChartsInterpreter.class.getName() + this.hashCode();
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
}
