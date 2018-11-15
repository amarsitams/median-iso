package com.rumango.median.iso.socket.server;

import java.net.InetSocketAddress;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.rumango.median.iso.service.IsoConvertor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {

	private final static Logger logger = Logger.getLogger(ServerHandler.class);
	private IsoConvertor convertor;
	private String uuid;
	private Map<String, String> map = new LinkedHashMap<>();

	public ServerHandler(IsoConvertor convertor) {
		this.convertor = convertor;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info("inside channelRead of server handler : ");
		try {
			String strIP = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
			map.put("IP", strIP);
			logger.info("IP " + strIP);
			IsoMessage isoMessage = (IsoMessage) msg;
			uuid = UUID.randomUUID().toString();
			map.put("uuid", uuid);
			logger.info("input iso msg: " + isoMessage.getStr());
			String response = convertor.convertAndRespond(isoMessage.getStr(), map);
			String sendMessage = getTcpHeader(response.length()) + response;
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
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}
}