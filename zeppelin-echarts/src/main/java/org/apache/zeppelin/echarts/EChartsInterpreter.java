package org.apache.zeppelin.echarts;

import org.apache.zeppelin.echarts.command.Command;
import org.apache.zeppelin.echarts.utils.PropertyGetter;
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

	private PropertyGetter propertyGetter;

	public EChartsInterpreter(Properties property) {
		super(property);
		this.propertyGetter = new PropertyGetter(this);
	}

	@Override
	public void open() {

	}

	@Override
	public void close() {

	}

	@Override
	public InterpreterResult interpret(String cmd, InterpreterContext interpreterContext) {
		try {
			Command command = CommandParser.getInstance().parse(cmd);
			return command.execute(this.propertyGetter, interpreterContext);
		} catch (Throwable e) {
			throw new RuntimeException("execute command exception:", e);
		}
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
		return this.propertyGetter.isConcurrentExecution() ?
				SchedulerFactory.singleton().createOrGetParallelScheduler(schedulerName,
						this.propertyGetter.getMaxConcurrentConnection())
				: SchedulerFactory.singleton().createOrGetFIFOScheduler(schedulerName);
	}
}
