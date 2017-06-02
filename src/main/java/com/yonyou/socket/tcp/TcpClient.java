package com.yonyou.socket.tcp;

import org.apache.log4j.Logger;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;


public class TcpClient {
	private static final Logger logger = Logger.getLogger(TcpClient.class);
	public static String HOST = "10.11.65.51";
	public static int PORT = 5002;
	
	public static Bootstrap bootstrap = getBootstrap();
	public static Channel channel = getChannel(HOST,PORT);
	/**
	 * 初始化Bootstrap
	 * @return
	 */
	public static final Bootstrap getBootstrap(){
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class);
		b.handler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
				pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
				pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
				pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
				pipeline.addLast("handler", new TcpClientHandler());
			}
		});
		b.option(ChannelOption.SO_KEEPALIVE, true);
		return b;
	}

	public static final Channel getChannel(String host,int port){
		Channel channel = null;
		try {
			channel = bootstrap.connect(host, port).sync().channel();
		} catch (Exception e) {
			System.out.println(String.format("连接Server(IP[%s],PORT[%s])失败", host,port) + e);
			logger.error(String.format("连接Server(IP[%s],PORT[%s])失败", host,port),e);
			return null;
		}
		return channel;
	}

	public static void sendMsg(Object msg) throws Exception {
		if(channel!=null){
			channel.writeAndFlush(msg).sync();
		}else{
			logger.warn("消息发送失败,连接尚未建立!");
		}
	}

    public static void main(String[] args) throws Exception {
		try {
			TcpClient.sendMsg(Unpooled.wrappedBuffer(genInitBuf()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			channel.close();
		}
    }
    
    public static byte[] genInitBuf()
    {
    	byte[] buffer = new byte[12];
    	buffer[0] = 0x3E;
        buffer[1] = 0x2A;
        buffer[2] = 0x27;
        buffer[3] = 0x1B;
        buffer[4] = 0x11;
        buffer[5] = 0x00;
        buffer[6] = 0x00;
        buffer[7] = 0x00;
        buffer[8] = 0x00;
        buffer[9] = 0x00;
        buffer[10] = 0x00;
        buffer[11] = 0x00;
        return buffer;
    }
}