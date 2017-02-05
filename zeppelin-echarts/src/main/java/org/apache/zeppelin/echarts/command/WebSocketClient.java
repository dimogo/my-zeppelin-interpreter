package org.apache.zeppelin.echarts.command;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.ws.WebSocket;
import org.asynchttpclient.ws.WebSocketTextListener;
import org.asynchttpclient.ws.WebSocketUpgradeHandler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * Created by Ethan Xiao on 2017/2/5.
 */
public class WebSocketClient {

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		final CountDownLatch latch = new CountDownLatch(1);
		AsyncHttpClientConfig cf = new DefaultAsyncHttpClientConfig.Builder().build();
				//.setProxyServer(new ProxyServer.Builder("127.0.0.1", 38080)).build();

		AsyncHttpClient c = new DefaultAsyncHttpClient(cf);

		WebSocket websocket = c.prepareGet("ws://vpn.dimogo.com:8081/ws")
				.addHeader("Content-Type", "application/json")
				.addHeader("Sec-WebSocket-Extensions", "permessage-deflate; client_max_window_bits")
				.addHeader("Sec-WebSocket-Key", "9LmWSc0w+oHM6iTvxqYTCA==")
				.addHeader("Sec-WebSocket-Version", "13")
				.addHeader("Upgrade", "websocket")
				.addHeader("DNT", "1")
				.addHeader("Connection", "Upgrade")
				.addHeader("Accept-Encoding", "gzip, deflate, sdch")
				.execute(new WebSocketUpgradeHandler.Builder().addWebSocketListener(
						new WebSocketTextListener() {

							@Override
							public void onMessage(String message) {
								System.out.println("recv msg:" + message);
							}

							@Override
							public void onOpen(WebSocket websocket) {
								System.out.println("opend web socket connection.");
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
		String message = "{\n" +
				"  \"op\": \"RUN_PARAGRAPH\",\n" +
				"  \"data\": {\n" +
				"    \"id\": \"20170202-032801_1404276097\",\n" +
				"    \"title\": \"paragraph_1486006081844_-1048842316\",\n" +
				"    \"paragraph\": \"%sh↵echo hello\",\n" +
				"    \"config\": {\n" +
				"      \"colWidth\": 12,\n" +
				"      \"graph\": {\n" +
				"        \"mode\": \"table\",\n" +
				"        \"height\": 300,\n" +
				"        \"optionOpen\": false,\n" +
				"        \"keys\": [],\n" +
				"        \"values\": [],\n" +
				"        \"groups\": [],\n" +
				"        \"scatter\": {}\n" +
				"      },\n" +
				"      \"enabled\": true,\n" +
				"      \"editorMode\": \"ace/mode/sh\"\n" +
				"    },\n" +
				"    \"params\": {}\n" +
				"  },\n" +
				"  \"principal\": \"\",\n" +
				"  \"ticket\": \"\",\n" +
				"  \"roles\": \"\"\n" +
				"}";
		//message = "{op: 'PING', principal: '', ticket: '', roles: ''}";
		System.out.println("websocket is open:" + websocket.isOpen());
		System.out.println(message);
		websocket.sendMessage(message);
		latch.await();
	}
}
