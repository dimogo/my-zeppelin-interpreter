package org.apache.zeppelin.echarts.command.reader;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.zeppelin.echarts.utils.HttpClientUtils;
import org.apache.zeppelin.echarts.utils.PropertyGetter;
import org.apache.zeppelin.interpreter.InterpreterContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ethan Xiao on 2017/1/12.
 */
public class HttpReader extends Reader<String, String> {

	private HttpMethodType method;
	private String url;
	private RequestConfig.Builder requestConfigBuilder;
	private Map<String, String> header;
	private Map<String, String> postForm;
	private String postRawData;

	/**
	 * Http请求的方法
	 */
	enum HttpMethodType {
		get,
		post;
	}

	public void setParameters(String[] parameters) {
		this.method = HttpMethodType.valueOf(parameters[0]);
		this.url = parameters[1];
	}

	public void addPara(String name, String[] options, String body) {
		if ("header".equalsIgnoreCase(name)) {
			if (options.length > 1) {
				body = options[1];
			}
			if (header == null) {
				header = new HashMap<String, String>();
			}
			header.put(options[0], body);
		} else if ("form".equalsIgnoreCase(name)) {
			if (options.length > 1) {
				body = options[1];
			}
			if (postForm == null) {
				postForm = new HashMap<String, String>();
			}
			postForm.put(options[0], body);
		} else if ("raw".equalsIgnoreCase(name)) {
			if (options != null && options.length > 0) {
				postRawData = options[0];
			} else if (body != null) {
				postRawData = body;
			}
		} else if ("conf".equalsIgnoreCase(name)) {
			if (requestConfigBuilder == null) {
				requestConfigBuilder = RequestConfig.custom();
				requestConfigBuilder.setSocketTimeout(15000)
						.setConnectTimeout(15000)
						.setConnectionRequestTimeout(15000);
			}
			if ("SocketTimeout".equalsIgnoreCase(options[0])) {
				try {
					requestConfigBuilder.setSocketTimeout(Integer.parseInt(options[1]));
				} catch (Exception e) {
					throw new RuntimeException("socket timeout error", e);
				}
			} else if ("ConnectTimeout".equalsIgnoreCase(options[0])) {
				try {
					requestConfigBuilder.setConnectTimeout(Integer.parseInt(options[1]));
				} catch (Exception e) {
					throw new RuntimeException("connect timeout error", e);
				}
			} else if ("ConnectionRequestTimeout".equalsIgnoreCase(options[0])) {
				try {
					requestConfigBuilder.setConnectionRequestTimeout(Integer.parseInt(options[1]));
				} catch (Exception e) {
					throw new RuntimeException("connection timeout error", e);
				}
			}
		}
	}

	public void setBody(String body) {

	}

	public String execute(String input, PropertyGetter propertyGetter, InterpreterContext interpreterContext) {
		if (this.method == HttpMethodType.get) {
			return HttpClientUtils.sendHttp(HttpClientUtils.newGetRequest(this.url),
					requestConfigBuilder == null ? HttpClientUtils.defaultRequestConfig : requestConfigBuilder.build());
		}
		HttpPost httpPost = HttpClientUtils.newPostRequest(this.url);
		HttpClientUtils.setHeaders(httpPost, this.header);
		if (this.postRawData != null) {
			HttpClientUtils.setPostRaw(httpPost, this.postRawData);
		} else {
			HttpClientUtils.setEncodedPostForm(httpPost, postForm);
		}
		return HttpClientUtils.sendHttp(httpPost,
				requestConfigBuilder == null ? HttpClientUtils.defaultRequestConfig : requestConfigBuilder.build());
	}
}
