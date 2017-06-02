package com.yonyou.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;  
  
public class HelloClientInHandler extends ChannelInboundHandlerAdapter  {  
	
	boolean flag = false;
    private static Logger logger = LoggerFactory.getLogger(HelloClientInHandler.class);  
    private ByteBuf result;
    // 接收server端的消息，并打印出来  
    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
        logger.info("HelloClientIntHandler.channelRead"); 
        	
        if(result.getByte(result.readableBytes() -1) != 0){
        	System.out.println("get byte len: --->" + result.readableBytes());
        	return;
        }
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
//  
    // 连接成功后，向server发送消息  
    @Override  
    public void channelActive(ChannelHandlerContext ctx) throws Exception {  
        logger.info("HelloClientIntHandler.channelActive");  
        String msg = "Are you ok?";  
//        ByteBuf encoded = ctx.alloc().buffer(4 * msg.length());  
//        encoded.writeBytes(msg.getBytes());  
//        ctx.write(encoded);  
//        ctx.flush();  
        ByteBuf encode = Unpooled.copiedBuffer(genInitBuf());
        ctx.writeAndFlush(encode);
        
        
    }  
    
    private byte[] genInitBuf()
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

	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {  
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
        for(;;)
        {
        	byte len = result.readByte();
        	if(len == 0)
        	{
        		break;
        	}
        	byte[] inde = new byte[3];
        	ByteBuf indexBuf = result.readBytes(inde);
        	int index = (inde[0] << 16) + (inde[1] << 8) + inde[2];
        	byte[] dst = new byte[len - 3];
        	result.readBytes(dst);
        	String name = new String(dst,"UTF-8");
        	System.out.println(len + "," + (l += len) + "," + index + ":" + name);
        }
        int len = 0;
        while((len = result.readByte()) != 0) {
        	 ByteBuf indexBuf = result.readBytes(3);
        	 byte index1 = indexBuf.readByte();
             byte index2 = indexBuf.readByte();
             byte index3 = indexBuf.readByte();
        	 int index = (index1 << 16) + (index2 << 8) + index3;
        	 byte[] varNameByte = new byte[len - 3];
        	 result.readBytes(varNameByte);
        	 String varName = new String(varNameByte, "UTF-8");
        }
        result.release();  
    }
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("run channelReadComplete......");
		super.channelReadComplete(ctx);
	}
    
	
    
}  