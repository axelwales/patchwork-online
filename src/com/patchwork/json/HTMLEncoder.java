package com.patchwork.json;

import org.apache.commons.lang3.StringEscapeUtils;

public class HTMLEncoder {

	public static String encode(String html) {
		return StringEscapeUtils.escapeHtml4(html);
	}
	
}
