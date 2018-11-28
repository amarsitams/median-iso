package com.rumango.median.iso.server;

import java.net.InetSocketAddress;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.rumango.median.iso.service.GetResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {

	private final static Logger logger = Logger.getLogger(ServerHandler.class);
	private GetResponse getResponse;
	private String uuid;
	private Map<String, String> map = new LinkedHashMap<>();

	public ServerHandler(GetResponse convertor) {
		this.getResponse = convertor;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info("inside channelRead of server handler : ");
		String response;
		try {
			String strIP = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
			map.put("IP", strIP);
			logger.info("IP " + strIP);
			StringIsoMessage isoMessage = (StringIsoMessage) msg;
			uuid = UUID.randomUUID().toString();
			map.put("uuid", uuid);
			logger.info("input iso msg: " + isoMessage.getStr());
			// response = getResponse.convertAndRespond(test(), map);
			response = getResponse.convertAndRespond(isoMessage.getStr(), map);
			String sendMessage = getTcpHeader(response.length()) + response;
			logger.info("Response :" + sendMessage);
			isoMessage.setStr(sendMessage);
			ctx.write(isoMessage);
			ctx.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getTcpHeader(int length) {
		String tcpHeader = "";
		tcpHeader = (length < 9 ? "000" : length < 99 ? "00" : length < 999 ? "0" : "") + length;
		logger.info("tcpHeader :" + tcpHeader);
		return tcpHeader;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}