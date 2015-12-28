package net.jdfs.dfs.server;

import org.springframework.beans.factory.InitializingBean;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpServer implements InitializingBean {
  
	public static void main(String[] args) {
		
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 100)
			.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					//向ChannelPipeline 中添加HTTP请求消息解码器
					ch.pipeline().addLast("http-decoder",new HttpRequestDecoder());
					//可以将多个消息转换为单一的FullHttpRequest 或者 FullHttpResponse
					ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
					//新增HTTP响应编码器
					ch.pipeline().addLast("http-encoder",new HttpResponseEncoder());
					//支持异步发送大的码流
					ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
					ch.pipeline().addLast("fileServerHandler",new HttpCmdServerHandler());
				}
			});
			
			ChannelFuture future = b.bind("127.0.0.1",80).sync();
			System.err.println("netty http 服务器启动:127.0.0.1");
			//等待服务端监听端口关闭
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//优雅退出，释放线程池资源
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
		
	}

	public void afterPropertiesSet() throws Exception {
		
	}
	
	
}
