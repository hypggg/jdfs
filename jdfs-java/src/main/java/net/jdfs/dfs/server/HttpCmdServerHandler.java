package net.jdfs.dfs.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.net.URI;

import net.jdfs.dfs.entity.ServerStatus;

public class HttpCmdServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	  private HttpRequest request;
	  private String status;
	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
			FullHttpRequest msg) throws Exception {
		
		  if (msg instanceof HttpRequest) {
	            HttpRequest request = this.request = (HttpRequest) msg;
	            URI uri = new URI(request.getUri());
	            if (!uri.getPath().startsWith("/upload")) {
	                // Write Menu
	                status = ServerStatus.RESOURCE_NOT_FOUND.toString();
	            	write(ctx,status);
	                return;
	            }
		  }     
		HttpHeaders header = msg.headers();
		ByteBuf inputBuf = msg.content();
		byte[] bytes = new byte[inputBuf.readableBytes()];
		inputBuf.readBytes(bytes);
		String body = new String(bytes,"UTF-8");
		System.out.println("客户端发来消息："+body);
		status = ServerStatus.OK.toString();
		write(ctx,status);
		return;
		
		
	}
	
	private void write (ChannelHandlerContext ctx,String status){
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		response.headers().set("Content-Type", "application/json;charset=UTF-8");
		String retStr = new String ("{\"status\":\""+status+"\",\"uploadPath\":\"http://xxxx\"}");
		ByteBuf buffer = Unpooled.copiedBuffer(retStr, CharsetUtil.UTF_8);
		response.content().writeBytes(buffer);
		buffer.release();
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		
	}

	
	
	
}
