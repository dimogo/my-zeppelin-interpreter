package org.apache.zeppelin.echarts.utils;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Ethan Xiao on 2017/1/12.
 * 合并多个内容成为一个字符串
 */
public class OutputCombiner {

	public static String combine(String... arr) {
		if (arr.length == 1) {
			//只有一个元素时直接返回
			return arr[0];
		}
		if (arr.length == 2) {
			//只有两个元素时,其中一个为空直接返回另一个
			if (StringUtils.isBlank(arr[0])) {
				return arr[1];
			}
			if (StringUtils.isBlank(arr[1])) {
				return arr[0];
			}
		}
		StringBuilder builder = new StringBuilder();
		for (String a : arr) {
			if (StringUtils.isBlank(a)) {
				continue;
			}
			builder.append(a);
		}
		return builder.toString();
	}

	public static String combine(StringBuilder builder, String... arr) {
		for (String a : arr) {
			if (StringUtils.isBlank(a)) {
				continue;
			}
			builder.append(a);
		}
		return builder.toString();
	}

}
