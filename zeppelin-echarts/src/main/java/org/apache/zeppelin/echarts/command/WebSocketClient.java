package org.apache.zeppelin.echarts.command;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.proxy.ProxyServer;
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

		WebSocket websocket = c.prepareGet("ws://vpn.dimogo.com:8081")
				.execute(new WebSocketUpgradeHandler.Builder().addWebSocketListener(
						new WebSocketTextListener() {

							@Override
							public void onMessage(String message) {
								System.out.println("recv msg:" + message);
							}

							@Override
							public void onOpen(WebSocket websocket) {
								websocket.sendMessage("...");
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
		latch.await();
	}
}
