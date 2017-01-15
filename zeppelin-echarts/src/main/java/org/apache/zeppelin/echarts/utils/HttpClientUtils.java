package org.apache.zeppelin.echarts.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * Created by Ethan Xiao on 2017/1/14.
 */
public class HttpClientUtils {

	/**
	 * 默认的http请求配置
	 */
	public static final RequestConfig defaultRequestConfig = RequestConfig.custom()
			.setSocketTimeout(15000)
			.setConnectTimeout(15000)
			.setConnectionRequestTimeout(15000)
			.build();

	/**
	 * 创建Http(s) Get请求
	 * @param httpUrl
	 * @return
	 */
	public static HttpGet newGetRequest(String httpUrl) {
		HttpGet request = new HttpGet(httpUrl);
		return request;
	}

	/**
	 * 创建Http(s) Post请求
	 * @param httpUrl
	 * @return
	 */
	public static HttpPost newPostRequest(String httpUrl) {
		HttpPost request = new HttpPost(httpUrl);
		return request;
	}

	/**
	 * 为请求设置请求头
	 * @param request
	 * @param headers
	 */
	public static void setHeaders(HttpRequestBase request, Map<String, String> headers) {
		if (headers == null) {
			return;
		}
		for (Map.Entry<String, String> header : headers.entrySet()) {
			request.setHeader(header.getKey(), header.getValue());
		}
	}

	/**
	 * 为Post请求设置URL编码的表单内容
	 * @param request
	 * @param formPairs
	 * @return
	 */
	public static HttpPost setEncodedPostForm(HttpPost request, Map<String, String> formPairs) {
		if (formPairs == null) {
			return request;
		}
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> pair : formPairs.entrySet()) {
			nameValuePairs.add(new BasicNameValuePair(pair.getKey(), pair.getValue()));
		}
		try {
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			return request;
		} catch (Exception e) {
			throw new RuntimeException("http post entity error", e);
		}
	}

	/**
	 * 为Post请求设置表单内容
	 * @param request
	 * @param raw post form: key1=value1&key2=value2
	 * @return
	 */
	public static HttpPost setPostRaw(HttpPost request, String raw) {
		if (raw == null) {
			return request;
		}
		try {
			StringEntity stringEntity = new StringEntity(raw, "UTF-8");
			//stringEntity.setContentType("application/x-www-form-urlencoded");
			request.setEntity(stringEntity);
			return request;
		} catch (Exception e) {
			throw new RuntimeException("http post entity error", e);
		}
	}

	/**
	 * 为Post请求设置文本的表单和文件
	 * @param request
	 * @param formPairs
	 * @param fileLists
	 * @return
	 */
	public static HttpPost sendPostFormAndFiles(HttpPost request, Map<String, String> formPairs, List<File> fileLists) {
		MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
		if (formPairs != null) {
			for (Map.Entry<String, String> pair : formPairs.entrySet()) {
				meBuilder.addPart(pair.getKey(), new StringBody(pair.getValue(), ContentType.TEXT_PLAIN));
			}
		}
		if (fileLists != null) {
			for (File file : fileLists) {
				FileBody fileBody = new FileBody(file);
				meBuilder.addPart("files", fileBody);
			}
		}
		HttpEntity reqEntity = meBuilder.build();
		request.setEntity(reqEntity);
		return request;
	}

	/**
	 * 发送http(s)请求
	 * @param request
	 * @param requestConfig
	 * @return
	 */
	public static String sendRequest(HttpRequestBase request, RequestConfig requestConfig) {
		String url = request.getURI().toString();
		if (url.startsWith("https://")) {
			return sendHttps(request, requestConfig);
		}
		return sendHttp(request, requestConfig);
	}

	/**
	 * 发送Http请求
	 * @param request
	 * @param requestConfig
	 * @return
	 */
	public static String sendHttp(HttpRequestBase request, RequestConfig requestConfig) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			request.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(request);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}

	/**
	 * 发送Https请求
	 * @param request
	 * @param requestConfig
	 * @return
	 */
	public static String sendHttps(HttpRequestBase request, RequestConfig requestConfig) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.load(new URL(request.getURI().toString()));
			DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
			httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();
			request.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(request);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException("https request error:" + request.getURI(), e);
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}
}
