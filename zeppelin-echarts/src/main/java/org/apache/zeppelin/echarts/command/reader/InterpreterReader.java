package org.apache.zeppelin.echarts.command.reader;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.zeppelin.echarts.utils.PropertyGetter;
import org.apache.zeppelin.interpreter.Interpreter;
import org.apache.zeppelin.interpreter.InterpreterContext;
import org.apache.zeppelin.interpreter.InterpreterResult;
import org.apache.zeppelin.server.ZeppelinServer;

/**
 * Created by Ethan Xiao on 2017/1/12.
 */
public class InterpreterReader extends Reader<String, String> {
	private String replName;
	private String body;

	public void setParameters(String[] parameters) {
		if (parameters != null && parameters.length > 0) {
			this.replName = parameters[0];
		}
	}

	public void addPara(String name, String[] options, String body) {

	}

	public void setBody(String body) {
		this.body = body;
	}

	public String execute(String input, PropertyGetter propertyGetter, InterpreterContext interpreterContext) {
		try {
			InterpreterContext subContext = (InterpreterContext) BeanUtils.cloneBean(interpreterContext);
			BeanUtils.setProperty(subContext, "replName", this.replName);
			Interpreter interpreter = ZeppelinServer.notebook.getInterpreterFactory().getInterpreter(interpreterContext
					.getAuthenticationInfo().getUser(), interpreterContext.getNoteId(), subContext.getReplName());
			InterpreterResult rs = interpreter.interpret(this.body, subContext);
			return rs.toString();
		} catch (Exception e) {
			throw new RuntimeException("call sub interpreter error:", e);
		}
	}
}
