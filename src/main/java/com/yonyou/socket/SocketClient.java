package com.yonyou.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created by xingguangyao on 17/5/18.
 */
public class SocketClient {

    static Socket socket;
    static InputStream receive;
    static OutputStream send;
    static byte[] buffer = new byte[65535];

    // 搭建客户端
    public static void main(String[] args) throws IOException {
        try {
            // 1、创建客户端Socket，指定服务器地址和端口
            socket = new Socket("10.11.65.51", 5002);
            socket.setKeepAlive(true);
            socket.setSoTimeout(1000 * 6);
            receive = socket.getInputStream();
            send = socket.getOutputStream();
            work();
            getData();
        } catch (Exception e) {
            System.out.println("can not listen to:" + e);// 出错，打印出错信息
        }
    }

    public static int work() {
        if (socket.isClosed())
            return -1;

        try {
            int iWantSendLen = 12;
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
            send.write(buffer, 0, iWantSendLen);
            send.flush();
            Arrays.fill(buffer, (byte) 0);
            int memLen = 0;
            int iWantRecSum = 2;
            memLen = getMemLen(memLen, iWantRecSum);
            if (memLen >= iWantRecSum) {
                int irecLen = receive.read(buffer, 0, memLen);
                ByteBuffer mBuffer = ByteBuffer.wrap(buffer);
                 byte[] head = new byte[9];
                 mBuffer.get(head);
                 int l = 0;
                 int i = 1;
                 for(;;){
                	 byte len = mBuffer.get();
                	 if(len == 0){
                		 System.out.println("over..............");
                		 break;
                	 }
                	 byte[] index = new byte[3];
                	 mBuffer.get(index);
                	 byte[] name = new byte[len - 3];
                	 mBuffer.get(name);
                	 int ind = (int)((index[0] & 0xFF << 16) | (index[1] & 0xFF << 8) | index[2] & 0xFF);
                	 String nameStr = new String(name,"UTF-8");
                	 System.out.println(ind + ","+(l+=len)+ ","+ (i++) + "," + nameStr);
                  }
            } else {
                iWantRecSum = 0;
            }
        } catch (IOException e) {

        }
        return -1;
    }

    private static int getMemLen(int memLen, int iWantRecSum) throws IOException {
    	int temp = 0;
        for (int i = 0; i < 6000; i = i + 100) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {

            }
            if(temp == receive.available() && temp > iWantRecSum)
            {
            	break;
            }else{
            	temp = receive.available();
            }
        }
        return temp;
    }

    static int ArryToShort(byte[] Array, int Pos) {
        if (Pos + 2 > Array.length)
            return 0;
        int s0 = (int) (Array[Pos + 0] & 0x000000ff);
        int s1 = (int) (Array[Pos + 1] & 0x000000ff);
        s1 <<= 8;
        int ret = (int) (s0 | s1);
        return ret;
    }

    public static int getData() {
        if (socket.isClosed()) {
            return -1;
        }
        try {
            int iWantSendLen = 13;
            buffer[0] = 0x3E;
            buffer[1] = 0x2A;
            buffer[2] = 0x27;
            buffer[3] = 0x1C;
            buffer[4] = 0x11;
            buffer[5] = 0x00;
            buffer[6] = 0x00;
            buffer[7] = 0x00;
            buffer[8] = 0x00;
            buffer[9] = 0x00;
            buffer[10] = 0x00;
            buffer[11] = 0x00;
            buffer[12] = 0x05;
            send.write(buffer, 0, iWantSendLen);
            send.flush();
            Arrays.fill(buffer, (byte) 0);
            int memLen = 0;
            int iWantRecSum = 2;
            memLen = getMemLen(memLen, iWantRecSum);
            System.out.println("length ---->" + memLen);
            if (memLen >= iWantRecSum) {
                int irecLen = receive.read(buffer, 0, memLen);
                ByteBuffer mBuffer = ByteBuffer.wrap(buffer);
                short mShort1 = mBuffer.getShort();
                short mShort2 = mBuffer.getShort();
                byte mByte1 = mBuffer.get();
                byte mByte2 = mBuffer.get();
               for(;;){
            	   //数据包长度
            	   short mShort3 = mBuffer.getShort();
            	   if(mShort3 == 0)
            	   {
            		   System.out.println("over....");
            		   break;
            	   }
            	   byte[] b = new byte[mShort3];
            	   mBuffer.get(b);
            	   for(int i=0;i<mShort3;i+=8){
            		   byte[] index = new byte[3];
            		   mBuffer.get(index);
            		   int ind = (int)(((b[i] & 0xFF) << 16) | ((b[i+1] & 0xFF) << 8) | (b[i+2] & 0xFF));
            		   byte sq = b[i+3];
            		   ByteBuffer buf = ByteBuffer.allocateDirect(4);
            		   buf.order(ByteOrder.LITTLE_ENDIAN);
            		   buf.put(new byte[]{b[i+4],b[i+5],b[i+6],b[i+7]});
            		   buf.rewind();
            		   float value = buf.getFloat();
            		   System.out.println(ind + "," + sq + "," + value);
            	   }
               }
            }
        } catch (Exception e) {

        }
        return 1;
    }
}


