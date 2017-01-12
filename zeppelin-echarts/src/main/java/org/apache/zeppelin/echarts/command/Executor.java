package org.apache.zeppelin.echarts.command;

import org.apache.zeppelin.echarts.utils.PropertyGetter;
import org.apache.zeppelin.interpreter.InterpreterContext;

/**
 * Created by Ethan Xiao on 2017/1/12.
 * 命令执行器
 */
public interface Executor<I, O> {

	/**
	 * 设置执行器的参数
	 * 命令格式:%type name para1 para2 para3 ...
	 * 不是所有命令都需要参数, 所以有时会为空
	 * @param parameters
	 */
	void setParameters(String[] parameters);

	/**
	 * 设置执行器需要执行的内容
	 * 内容是从zeppelin传入的紧跟当前命令行结束符至下一条命令开始前一个字符
	 * 不是所有命令都需要传入执行内容
	 * @param body
	 */
	void setBody(String body);

	/**
	 * 执行命令的动作, 前一条命令的输出作为当前命令的输入
	 * @param input
	 * @return
	 */
	O execute(I input, PropertyGetter propertyGetter, InterpreterContext interpreterContext);

}
