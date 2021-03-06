package org.apache.zeppelin.echarts.command.reader;

import org.apache.zeppelin.echarts.command.WebSocketClient;
import org.apache.zeppelin.echarts.utils.PropertyGetter;
import org.apache.zeppelin.interpreter.InterpreterContext;

/**
 * Created by Ethan Xiao on 2017/1/12.
 */
public class InterpreterReader extends Reader<String, String> {
	private String body;

	private String noteBookId;
	private String paragraphId;
	private boolean run;
	private String replName;

	public void setParameters(String[] parameters) {
		if (parameters == null || parameters.length == 0) {
			return;
		}
		noteBookId = parameters[0];
		if (parameters.length > 1) {
			paragraphId = parameters[1];
		}
		if (parameters.length > 2) {
			try {
				run = "run".equalsIgnoreCase(parameters[2]);
			} catch (Exception e) {}
		}
		if (parameters.length > 3) {
			replName = parameters[3];
		}
	}

	public void addPara(String name, String[] options, String body) {

	}

	public void setBody(String body) {
		this.body = body;
	}

	public String execute(String input, PropertyGetter propertyGetter, InterpreterContext interpreterContext) {
		if (noteBookId == null || paragraphId == null) {
			throw new RuntimeException("noteBookId or paragraphId can not be null");
		}
		if (run && replName == null) {
			throw new RuntimeException("replName can not be null");
		}
		try {
			WebSocketClient client = new WebSocketClient(propertyGetter.getWebSocketURL(), propertyGetter.getWebSocketMaxFrameSize(),
					propertyGetter.getWebSocketRecvMsgQueueSize());
			client.setPrincipal(interpreterContext.getAuthenticationInfo().getUser());
			client.setTicket(interpreterContext.getAuthenticationInfo().getTicket());
			if (run) {
				return WebSocketClient.ResultUtil.getParagraphAllMessages(client.runParagraph(noteBookId, paragraphId, "%" + replName + "\n" + body));
			}
			return WebSocketClient.ResultUtil.getParagraphAllMessages(client.getNote(noteBookId), paragraphId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
