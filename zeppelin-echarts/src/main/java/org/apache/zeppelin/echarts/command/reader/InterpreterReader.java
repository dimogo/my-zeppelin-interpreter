package org.apache.zeppelin.echarts.command.reader;

import com.alibaba.fastjson.JSON;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.zeppelin.echarts.utils.PropertyGetter;
import org.apache.zeppelin.interpreter.Interpreter;
import org.apache.zeppelin.interpreter.InterpreterContext;
import org.apache.zeppelin.interpreter.InterpreterFactory;
import org.apache.zeppelin.interpreter.InterpreterResult;
import org.apache.zeppelin.notebook.Notebook;
import org.apache.zeppelin.server.ZeppelinServer;
import org.apache.zeppelin.user.AuthenticationInfo;

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
	//	try {
	//		InterpreterContext subContext = new InterpreterContext(
	//				interpreterContext.getNoteId(),
	//				interpreterContext.getParagraphId(),
	//				interpreterContext.getParagraphTitle(),
	//				interpreterContext.getParagraphText(),
	//				interpreterContext.getAuthenticationInfo(),
	//				interpreterContext.getConfig(),
	//				interpreterContext.getGui(),
	//				interpreterContext.getAngularObjectRegistry(),
	//				interpreterContext.getResourcePool(),
	//				interpreterContext.getRunners(),
	//				interpreterContext.out
	//		);
	//		//BeanUtils.setProperty(subContext, "replName", this.replName);
	//		Notebook notebook = ZeppelinServer.notebook;
	//		InterpreterFactory interpreterFactory = notebook.getInterpreterFactory();
	//		AuthenticationInfo authenticationInfo = interpreterContext.getAuthenticationInfo();
	//		String user = authenticationInfo.getUser();
	//		Interpreter interpreter = interpreterFactory.getInterpreter(user, interpreterContext.getNoteId(), this.replName);
	//		InterpreterResult rs = interpreter.interpret(this.body, subContext);
//			return rs.toString();
//		} catch (Exception e) {
//			throw new RuntimeException("call sub interpreter error:", e);
//		}
		return JSON.toJSONString(interpreterContext);
	}
}
