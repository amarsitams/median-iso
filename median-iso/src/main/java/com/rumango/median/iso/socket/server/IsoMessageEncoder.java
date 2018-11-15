package com.rumango.median.iso.socket.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class IsoMessageEncoder extends MessageToByteEncoder<IsoMessage> {
	@Override
	protected void encode(ChannelHandlerContext ctx, IsoMessage msg, ByteBuf out) throws Exception {
		out.writeBytes(msg.toByteArray());
	}
}