package com.rumango.median.iso.server;

import java.nio.charset.StandardCharsets;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class IsoMessageDecoder extends ByteToMessageDecoder {

	private String inputMsg;
	private StringIsoMessage isoMessage;
	private int msgLength = 0;
	private byte[] requestMsg = null;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		msgLength = in.readableBytes();
		if (msgLength != -1) {
			requestMsg = new byte[msgLength];
			in.readBytes(requestMsg);
			inputMsg = new String(new String(requestMsg, StandardCharsets.US_ASCII)).trim();
			// inputMsg = inputMsg.substring(4);// removing first 4 characters i.e length of
			isoMessage = new StringIsoMessage();
			isoMessage.setStr(inputMsg);
			out.add(isoMessage);
		}
	}
}