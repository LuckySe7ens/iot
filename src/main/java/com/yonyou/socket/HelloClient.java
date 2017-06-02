package com.yonyou.socket;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;  
  
public class HelloClient {  
    public void connect(String host, int port) throws Exception {  
        EventLoopGroup workerGroup = new NioEventLoopGroup();  
  
        try {  
            Bootstrap b = new Bootstrap();  
            b.group(workerGroup);  
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_BACKLOG, 128)
            .option(ChannelOption.TCP_NODELAY, true);
//            b.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(906400));
//            b.option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(64,2048,65535));
            b.option(ChannelOption.SO_KEEPALIVE, true);  
            b.handler(new ChannelInitializer<SocketChannel>() {  
                @Override  
                public void initChannel(SocketChannel ch) throws Exception {  
                    ch.pipeline()
//                    .addLast(new DelimiterBasedFrameDecoder(65535, delimiter));
//                    .addLast(new LineBasedFrameDecoder(1024))
                  //decoder
//                .addLast("framer", new DelimiterBasedFrameDecoder(1024*100, Delimiters.lineDelimiter()))
    			.addLast(new HelloClientInHandler());  
                }  
            });  
  
            // Start the client.  
            ChannelFuture f = b.connect(host, port).sync();  
            //发送init
//            f.channel().write("");
            
//            while(true){
//            	Thread.sleep(5000);
//            	//定时请求
//            	f.channel().write("");
//            }
            // Wait until the connection is closed.  
            f.channel().closeFuture().sync();  
        } finally {  
            workerGroup.shutdownGracefully();  
        }  
  
    }  
  
    public static void main(String[] args) throws Exception {  
        HelloClient client = new HelloClient();  
        client.connect("10.11.65.51", 5002);  
    }  
}  