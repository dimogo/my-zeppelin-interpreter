package org.apache.zeppelin.echarts.command;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.zeppelin.notebook.socket.Message;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.ws.WebSocket;
import org.asynchttpclient.ws.WebSocketTextListener;
import org.asynchttpclient.ws.WebSocketUpgradeHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Ethan Xiao on 2017/2/5.
 */
public class WebSocketClient {
	private String principal = "anonymous";
	private String ticket = "anonymous";
	private String roles = "";

	private WebSocket websocket;
	private AsyncHttpClient httpClient;
	private CountDownLatch latch;
	private BlockingQueue<JSONObject> recvMessages;

	public WebSocketClient(String webSocketUrl, int maxFrameSize, int recvMsgQueueSize) throws ExecutionException, InterruptedException {
		this.latch = new CountDownLatch(1);
		this.recvMessages = new ArrayBlockingQueue<>(recvMsgQueueSize);
		AsyncHttpClientConfig cf = new DefaultAsyncHttpClientConfig.Builder().setWebSocketMaxFrameSize(maxFrameSize).build();
		this.httpClient = new DefaultAsyncHttpClient(cf);
		this.websocket = httpClient.prepareGet(webSocketUrl)
				.addHeader("Content-Type", "application/json")
				.execute(new WebSocketUpgradeHandler.Builder().addWebSocketListener(
						new WebSocketTextListener() {

							@Override
							public void onMessage(String message) {
								try {
									recvMessages.put((JSONObject) JSON.parse(message));
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onOpen(WebSocket websocket) {
							}

							@Override
							public void onClose(WebSocket websocket) {
								latch.countDown();
							}

							@Override
							public void onError(Throwable t) {
								t.printStackTrace();
							}
						}).build()).get();
	}

	private Message newMessage(Message.OP op) {
		Message message = new Message(op);
		message.principal = this.principal;
		message.ticket = this.ticket;
		message.roles = this.roles;
		return message;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public Message newGetInterpreterSettings() {
		return newMessage(Message.OP.GET_INTERPRETER_SETTINGS);
	}

	public Message newListConfirations() {
		return newMessage(Message.OP.LIST_CONFIGURATIONS);
	}

	public Message newListNotes() {
		return newMessage(Message.OP.LIST_NOTES);
	}

	public Message newGetNote(String noteId) {
		Message message = newMessage(Message.OP.GET_NOTE);
		message.data.put("id", noteId);
		return message;
	}

	public Message newRunParagraph(String id, String paragraph) {
		Message message = newMessage(Message.OP.RUN_PARAGRAPH);
		message.data.put("id", id);
		message.data.put("paragraph", paragraph);
		message.data.put("params", new HashMap<String, Object>());
		message.data.put("config", new HashMap<String, Object>());
		message.data.put("title", null);
		message.data.put("date", null);
		return message;
	}

	public void sendMessage(Message message) {
		this.websocket.sendMessage(JSON.toJSONString(message));
	}

	public void waitingForClose(long timeout, TimeUnit timeUnit) throws InterruptedException {
		this.latch.await(timeout, timeUnit);
	}

	public void waitingForClose() throws InterruptedException {
		this.latch.await();
	}

	public void close() throws IOException {
		this.websocket.close();
		this.httpClient.close();
	}

	public JSONObject getMessage() throws InterruptedException {
		return this.recvMessages.take();
	}

	public JSONObject getMessage(long timeout, TimeUnit timeUnit) throws InterruptedException {
		return this.recvMessages.poll(timeout, timeUnit);
	}

	public JSONObject getNote(String noteId, long timeout, TimeUnit timeUnit) throws InterruptedException {
		sendMessage(newGetNote(noteId));
		return getMessage(timeout, timeUnit);
	}

	public JSONObject getNote(String noteId) throws InterruptedException {
		sendMessage(newGetNote(noteId));
		return getMessage();
	}

	public JSONObject runParagraph(String noteId, String paragraphId, String paragraph) throws InterruptedException {
		sendMessage(newGetNote(noteId));
		sendMessage(newRunParagraph(paragraphId, paragraph));
		while (true) {
			JSONObject msg = getMessage();
			if (!"PARAGRAPH".equals(msg.getString("op"))) {
				continue;
			}
			if ("FINISHED".equals(msg.getJSONObject("data").getJSONObject("paragraph").getString("status"))) {
				return msg;
			}
		}
	}

	public static class ResultUtil {
		public static JSONArray getParagraphs(JSONObject object) {
			return object.getJSONObject("data").getJSONObject("note").getJSONArray("paragraphs");
		}

		public static JSONObject getParagraph(JSONObject object, String paragraphId) {
			JSONArray paragraphs = getParagraphs(object);
			for (int i = 0; i < paragraphs.size(); i++) {
				if (paragraphId.equalsIgnoreCase(paragraphs.getJSONObject(i).getString("id"))) {
					return paragraphs.getJSONObject(i);
				}
			}
			throw new RuntimeException("can not found paragraph " + paragraphId);
		}

		public static JSONObject getParagraph(JSONObject object, int index) {
			return getParagraphs(object).getJSONObject(index);
		}

		public static JSONObject getParagraph(JSONObject object) {
			return object.getJSONObject("data").getJSONObject("paragraph");
		}

		public static JSONArray getMessages(JSONObject object) {
			return getParagraph(object).getJSONObject("results").getJSONArray("msg");
		}

		public static JSONArray getMessages(JSONObject object, String paragraphId) {
			return getParagraph(object, paragraphId).getJSONObject("results").getJSONArray("msg");
		}

		public static JSONArray getMessages(JSONObject object, int index) {
			return getParagraph(object, index).getJSONObject("results").getJSONArray("msg");
		}

		public static String getParagraphAllMessages(JSONObject object, int index) {
			return StringUtils.join(getParagraphMessages(object, index), "");
		}

		public static List<String> getParagraphMessages(JSONObject object, int index) {
			JSONArray msgs = getParagraph(object, index).getJSONObject("results").getJSONArray("msg");
			List<String> msgList = new LinkedList<>();
			for (int i = 0; i < msgs.size(); i++) {
				msgList.add(msgs.getJSONObject(i).getString("data"));
			}
			return msgList;
		}

		public static String getParagraphCode(JSONObject object, int index) {
			return getParagraph(object, index).getJSONObject("results").getString("code");
		}

		public static List<String> getParagraphTypes(JSONObject object, int index) {
			JSONArray msgs = getParagraph(object, index).getJSONObject("results").getJSONArray("msg");
			List<String> codeList = new LinkedList<>();
			for (int i = 0; i < msgs.size(); i++) {
				codeList.add(msgs.getJSONObject(i).getString("type"));
			}
			return codeList;
		}

		public static String getParagraphAllMessages(JSONObject object, String paragraphId) {
			return StringUtils.join(getParagraphMessages(object, paragraphId), "");
		}

		public static List<String> getParagraphMessages(JSONObject object, String paragraphId) {
			JSONArray msgs = getParagraph(object, paragraphId).getJSONObject("results").getJSONArray("msg");
			List<String> msgList = new LinkedList<>();
			for (int i = 0; i < msgs.size(); i++) {
				msgList.add(msgs.getJSONObject(i).getString("data"));
			}
			return msgList;
		}

		public static String getParagraphCode(JSONObject object, String paragraphId) {
			return getParagraph(object, paragraphId).getJSONObject("results").getString("code");
		}

		public static List<String> getParagraphTypes(JSONObject object, String paragraphId) {
			JSONArray msgs = getParagraph(object, paragraphId).getJSONObject("results").getJSONArray("msg");
			List<String> codeList = new LinkedList<>();
			for (int i = 0; i < msgs.size(); i++) {
				codeList.add(msgs.getJSONObject(i).getString("type"));
			}
			return codeList;
		}

		public static String getParagraphAllMessages(JSONObject object) {
			return StringUtils.join(getParagraphMessages(object), "");
		}

		public static List<String> getParagraphMessages(JSONObject object) {
			JSONArray msgs = getMessages(object);
			List<String> msgList = new LinkedList<>();
			for (int i = 0; i < msgs.size(); i++) {
				msgList.add(msgs.getJSONObject(i).getString("data"));
			}
			return msgList;
		}

		public static String getParagraphCode(JSONObject object) {
			return getParagraph(object).getJSONObject("results").getString("code");
		}

		public static List<String> getParagraphTypes(JSONObject object) {
			JSONArray msgs = getMessages(object);
			List<String> codeList = new LinkedList<>();
			for (int i = 0; i < msgs.size(); i++) {
				codeList.add(msgs.getJSONObject(i).getString("type"));
			}
			return codeList;
		}
	}
}
