package com.yonyou.socket.tcp;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class TcpClientHandler extends SimpleChannelInboundHandler<Object> {
	private static final Logger logger = Logger.getLogger(TcpClientHandler.class);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {  
        logger.info("HelloClientIntHandler.channelRead");  
        ByteBuf result = (ByteBuf) msg; 
        ByteBuf b = Unpooled.copiedBuffer(result); 
        byte[] a = new byte[b.readableBytes()];
        System.out.println("报文长度：" + a.length);
        b.readBytes(a);
        int resFlag = result.readUnsignedShort(); // 0 1 
        int funcCode = result.readUnsignedShort();// 2 3
        short varType = result.readUnsignedByte();// 4
        short statusCode = result.readUnsignedByte();//5
        ByteBuf actNumBuf = result.readBytes(3);// 6 7 8 
        byte num1 = actNumBuf.readByte();
        byte num2 = actNumBuf.readByte();
        byte num3 = actNumBuf.readByte();
        int actuNum = (num1 << 16) + (num2 << 8) + num3;
        System.out.println("actNum is :"+actuNum);
//        int actuNum = result.readBytes(3).getInt(3);
        int l = 0;
        int i = 1;
        for(;;)
        {
        	byte len = result.readByte();
        	if(len == 0)
        	{
        		System.out.println("OVER.................");
        		break;
        	}
        	byte[] inde = new byte[3];
        	ByteBuf indexBuf = result.readBytes(inde);
        	int index = (inde[0] << 16) + (inde[1] << 8) + inde[2];
        	byte[] dst = new byte[len - 3];
        	result.readBytes(dst);
        	String name = new String(dst,"UTF-8");
        	System.out.println(len + "," + (l += len) + ","+(i++) +"," + index + ":" + name);
        }
//        result.release();  
    }
}
