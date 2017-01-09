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
	public InterpreterResult interpret(String s, InterpreterContext interpreterContext) {
		return new InterpreterResult(InterpreterResult.Code.SUCCESS, InterpreterResult.Type.HTML, "<h1>Hello, ECharts!</h1>");
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
		return SchedulerFactory.singleton().createOrGetParallelScheduler(
				JDBCEChartsInterpreter.class.getName() + this.hashCode(), 10);
	}
}
