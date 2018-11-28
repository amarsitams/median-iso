package com.rumango.median.iso.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class IsoMessageEncoder extends MessageToByteEncoder<StringIsoMessage> {
	@Override
	protected void encode(ChannelHandlerContext ctx, StringIsoMessage msg, ByteBuf out) throws Exception {
		out.writeBytes(msg.toByteArray());
	}
}