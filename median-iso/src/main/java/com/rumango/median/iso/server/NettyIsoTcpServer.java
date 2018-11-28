package com.rumango.median.iso.server;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rumango.median.iso.dao.ServerDetailsRepository;
import com.rumango.median.iso.entity.ServerDetails;
import com.rumango.median.iso.service.GetResponse;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

@Component
public class NettyIsoTcpServer implements InitializingBean {

	private final static Logger logger = Logger.getLogger(NettyIsoTcpServer.class);

	@Value("${netty.server.port}")
	private int port;

	private ServerDetails serverDetails;

	@Autowired
	private GetResponse getResponse;

	@Autowired
	private ServerDetailsRepository serverDetailsRepository;

	public void setPort(int port) {
		this.port = port;
	}

	public void start() throws IOException, InterruptedException {
		NioEventLoopGroup boosGroup = new NioEventLoopGroup();
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(boosGroup, workerGroup);
		bootstrap.channel(NioServerSocketChannel.class);

		final EventExecutorGroup group = new DefaultEventExecutorGroup(100); // thread pool of 100

		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast("idleStateHandler", new IdleStateHandler(0, 0, 0));
				pipeline.addLast(new IsoMessageEncoder());
				pipeline.addLast(new IsoMessageDecoder());
				pipeline.addLast(group, "serverHandler", new ServerHandler(getResponse));
			}
		});

		bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

		try {
			port = serverDetails.getServerPort();
		} catch (Exception e) {
		}
		bootstrap.bind(port).sync();
		logger.info(" Server running on port:" + this.port);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			this.serverDetails = serverDetailsRepository.findById(1);
		} catch (Exception e) {
		}
		start();

	}
}