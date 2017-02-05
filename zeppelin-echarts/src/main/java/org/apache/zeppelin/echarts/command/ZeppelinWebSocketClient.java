package org.apache.zeppelin.echarts.command;

import org.apache.zeppelin.notebook.repo.zeppelinhub.websocket.Client;
import org.apache.zeppelin.notebook.repo.zeppelinhub.websocket.ZeppelinhubClient;

/**
 * Created by Ethan Xiao on 2017/2/5.
 */
public class ZeppelinWebSocketClient {
	public static void main(String[] args) {
		String wsurl = "ws://vpn.dimogo.com:8081/ws";
		String token = "TOKEN";
		Client.initialize(wsurl, wsurl, token, null).start();
		ZeppelinhubClient client = ZeppelinhubClient.getInstance();
		if (client == null) {
			client = ZeppelinhubClient.initialize(wsurl, token);
		}
		client.start();

		String runNotebookMsg = "{\n" +
				"  \"op\": \"RUN_NOTEBOOK\",\n" +
				"  \"data\": [\n" +
				"    {\n" +
				"      \"id\": \"20170202-032801_1404276097\",\n" +
				"      \"title\": null,\n" +
				"      \"config\": {},\n" +
				"      \"params\": {},\n" +
				"      \"date\": null,\n" +
				"      \"paragraph\": \"%sh\\necho 2\"\n" +
				"    }\n" +
				"  ],\n" +
				"  \"meta\": {\n" +
				"    \"owner\": \"author\",\n" +
				"    \"instance\": \"my-zepp\",\n" +
				"    \"noteId\": \"2C78F5XN6\"\n" +
				"  }\n" +
				"}";
		System.out.println(runNotebookMsg);
		client.handleMsgFromZeppelinHub(runNotebookMsg);
	}
}
