package com.rumango.median.iso.client;

import org.springframework.web.client.RestTemplate;

import com.rumango.median.iso.model.ValidateChannel;

public class RestCall {
	private static final String urlPath = "http://172.16.2.178:8080/ValidateAPI/rest/service/check?channelID=";

	public static ValidateChannel callRestApi(String channelId, String transactionId) {
		RestTemplate restTemplate = new RestTemplate();
		String url = urlPath + channelId + "&transactionID=" + transactionId;
		System.out.println("url ::" + url);
		ValidateChannel response = restTemplate.getForObject(url, ValidateChannel.class);
		return response;
	}
}
