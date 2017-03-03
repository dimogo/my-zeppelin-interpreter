package org.apache.zeppelin;

import com.qiniu.util.Auth;

/**
 * Created by Ethan Xiao on 2017/3/3.
 */
public class QiniuTokenGenerator {
	private static String accessKey = "rMcjTbc3rGIqJo0eIhB_Y90aH5oGl2rFfx-vTZaS";
	private static String secretKey = "-ZAZyOfAXuflW74uwsT4C3TVV8nnVP6l-oibDhe9";
	private static String bucket = "fcphoto";

	private static class QiniuTokenGeneratorHolder {
		private static QiniuTokenGenerator instance = new QiniuTokenGenerator();
	}

	private QiniuTokenGenerator() {

	}

	public static QiniuTokenGenerator getInstance() {
		return QiniuTokenGeneratorHolder.instance;
	}

	public static String genUpToken() {
		Auth auth = Auth.create(accessKey, secretKey);
		return auth.uploadToken(bucket);
	}

}
