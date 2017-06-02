package com.yonyou.socket.tcp.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yonyou.http.HTTPClientUtils;
import com.yonyou.socket.tcp.model.BatchTypeVariable;
import com.yonyou.socket.tcp.model.VariableModel;
import com.yonyou.socket.tcp.model.VariableType;

import net.sf.json.JSONObject;

public class SocketServiceImpl implements SocketService {

	static Socket socket;

	static InputStream receive;

	static OutputStream send;

	static byte[] buffer = new byte[65535];

	private String host = "10.11.65.51";

	private int port = 5002;

	/**
	 * 模拟（R8）值长
	 */
	public static final int VALUE_LENGTH_TYPE_R8 = 8;

	/**
	 * 模拟（R4）值长
	 */
	public static final int VALUE_LENGTH_TYPE_R4 = 4;

	/**
	 * 开关（I1）值长
	 */
	public static final int VALUE_LENGTH_TYPE_I1 = 0;

	private static final Logger LOG = LoggerFactory.getLogger(SocketServiceImpl.class);

	@Override
	public boolean connectSocket() {
		try {
			LOG.info("starting connect socket host:{},port:{}", host, port);
			socket = new Socket(host, port);
			socket.setKeepAlive(true);
			socket.setSoTimeout(1000 * 6);
			receive = socket.getInputStream();
			send = socket.getOutputStream();
			LOG.info("connect socket success.");
			return true;
		} catch (IOException e) {
			LOG.error("connect socket failed.", e);
			return false;
		}
	}

	@Override
	public List<VariableModel> getIndexVariRelation(Byte varType) {
		if (socket.isClosed()) {
			LOG.error("socket is closed.");
			return null;
		}

		try {
			byte[] reqBytes = genRequestNameBytes(varType);
			send.write(reqBytes, 0, reqBytes.length);
			send.flush();
			Arrays.fill(buffer, (byte) 0);
			int receiveLen = getMemLen(10);
			if (0 == receiveLen) {
				LOG.error("receive sokcet failed.");
				return null;
			}
			receive.read(buffer, 0, receiveLen);
			ByteBuffer mBuffer = ByteBuffer.wrap(buffer);
			byte[] head = new byte[9];
			mBuffer.get(head);

			byte resType = head[4];
			byte statuCode = head[5];
			// 校验响应的类型、状态码是否合法
			if (resType != varType || statuCode != 0) {
				LOG.error("the socket receive is wrong,cause by response type:{} isn't match expect:{},"
						+ "or statuCode:{} isn't match 0", resType, varType, statuCode);
				return null;
			}
			
			List<VariableModel> list = new ArrayList<>();
			for (;;) {
				byte len = mBuffer.get();
				if (len == 0) {
					// 报文解析完毕
					break;
				}
				// 索引
				byte[] index = new byte[3];
				mBuffer.get(index);
				int ind = (int) (((index[0] & 0xFF) << 16) | ((index[1] & 0xFF) << 8) | index[2] & 0xFF);
				// 变量名或变量名+量程
				byte[] name = new byte[len - 3];
				mBuffer.get(name);
				String nameStr;
				try {
					nameStr = new String(name, "UTF-8");
					list.add(genVarByType(varType, ind, nameStr));
//					System.out.println(ind + "," + nameStr);
				} catch (UnsupportedEncodingException e) {
					LOG.error("encoding failed.", e);
				}
			}
			return list;
		} catch (IOException e) {
			LOG.error("parse socket failed.", e);
		}
		return null;
	}

	@Override
	public Map<Integer,Float> initIndexValueRelation(Byte varType) {
		return getIndexValueRelationByTime(varType, 0);
	}

	@Override
	public Map<Integer,Float> getIndexValueRelationByTime(Byte varType, int timeRange) {
		if (socket.isClosed()) {
			LOG.error("socket is closed.");
			return null;
		}
		try {
			byte[] reqBytes = genRequestValueBytes(varType, timeRange);
			send.write(reqBytes, 0, reqBytes.length);
			send.flush();
			Arrays.fill(buffer, (byte) 0);
			int receiveLen = getMemLen(8);
			if (0 == receiveLen) {
				LOG.error("receive sokcet failed.");
				return null;
			}
			receive.read(buffer, 0, receiveLen);
			ByteBuffer mBuffer = ByteBuffer.wrap(buffer);
			byte[] head = new byte[6];
			mBuffer.get(head);

			byte resType = head[4];
			byte statuCode = head[5];
			// 校验响应的类型、状态码是否合法
			if (resType != varType || statuCode != 0) {
				LOG.error("the socket receive is wrong,cause by response type:{} isn't match expect:{},"
						+ "or statuCode:{} isn't match 0", resType, varType, statuCode);
				return null;
			}
			
			Map<Integer,Float> map = new HashMap<Integer, Float>();
			// 返回的值长
			int valueLen = getLenByType(varType);
			for (;;) {
				// 数据包长度
				short packLen = mBuffer.getShort();
				if (packLen == 0) {
					break;
				}
				byte[] b = new byte[packLen];
				mBuffer.get(b);

				for (int i = 0; i < packLen; i += (4 + valueLen)) {
					
					//索引获取
					byte[] index = new byte[3];
					mBuffer.get(index);
					int ind = (int) (((b[i] & 0xFF) << 16) | ((b[i + 1] & 0xFF) << 8) | b[i + 2] & 0xFF);
					//质量状态码、（开关类型的值）
					byte sq = b[i + 3];

					if (VALUE_LENGTH_TYPE_I1 == valueLen) {
						map.put(ind, (float)sq);
					} else if (VALUE_LENGTH_TYPE_R4 == valueLen || VALUE_LENGTH_TYPE_R8 == valueLen) {
						ByteBuffer buf = ByteBuffer.allocateDirect(valueLen);
						buf.order(ByteOrder.LITTLE_ENDIAN);
						byte[] range = Arrays.copyOfRange(b, i + 4, i + 4 + valueLen);
						buf.put(range);
						buf.rewind();
						float value = buf.getFloat();
						map.put(ind, value);
					} 
				}
			}
			return map;
		} catch (IOException e) {
			LOG.error("parse socket failed.", e);
		}
		return null;
	}

	/**
	 * 构造查询 索引：变量名 的请求报文
	 * 
	 * @param varType
	 * @return
	 */
	private byte[] genRequestNameBytes(byte varType) {
		byte[] requestByte = new byte[12];
		requestByte[0] = 0x3E;
		requestByte[1] = 0x2A;
		requestByte[2] = 0x27;
		requestByte[3] = 0x1B;
		requestByte[4] = varType;
		requestByte[5] = 0x00;
		requestByte[6] = 0x00;
		requestByte[7] = 0x00;
		requestByte[8] = 0x00;
		requestByte[9] = 0x00;
		requestByte[10] = 0x00;
		requestByte[11] = 0x00;

		return requestByte;
	}

	/**
	 * 构造查询 索引：变量值 的请求报文
	 * 
	 * @param varType
	 *            变量类型
	 * @param timeRange
	 *            时间段
	 * @return
	 */
	private byte[] genRequestValueBytes(byte varType, int timeRange) {

		byte[] requestByte = new byte[13];
		requestByte[0] = 0x3E;
		requestByte[1] = 0x2A;
		requestByte[2] = 0x27;
		requestByte[3] = 0x1C;
		requestByte[4] = varType;
		requestByte[5] = 0x00;
		requestByte[6] = 0x00;
		requestByte[7] = 0x00;
		requestByte[8] = 0x00;
		requestByte[9] = 0x00;
		requestByte[10] = 0x00;
		requestByte[11] = (byte) ((timeRange >> 8) & 0xFF);
		requestByte[12] = (byte) (timeRange & 0xFF);
		return requestByte;
	}

	/**
	 * 根据变量类型获取变量值长度（只支持R8/R4/I1类型，ST类型暂不支持）
	 * 
	 * @param varType
	 *            变量类型
	 * @return
	 */
	private int getLenByType(byte varType) {
		if (varType == VariableType.VARNAME_RANGE_TYPE_AI | varType == VariableType.VARNAME_RANGE_TYPE_AO
				| varType == VariableType.VARNAME_RANGE_TYPE_AR | varType == VariableType.VARNAME_RANGE_TYPE_VA) {
			return VALUE_LENGTH_TYPE_R4;
		} else if (varType == VariableType.VARNAME_TYPE_AI | varType == VariableType.VARNAME_TYPE_AO
				| varType == VariableType.VARNAME_TYPE_AR | varType == VariableType.VARNAME_TYPE_VA) {
			return VALUE_LENGTH_TYPE_R8;
		} else if (varType == VariableType.VARNAME_TYPE_DI | varType == VariableType.VARNAME_TYPE_DO
				| varType == VariableType.VARNAME_TYPE_DR | varType == VariableType.VARNAME_TYPE_VD) {
			return VALUE_LENGTH_TYPE_I1;
		}
		LOG.error("unsupport this variableType:{}", varType);
		return -1;
	}

	/**
	 * 获取接收报文长度
	 * 
	 * @param iWantRecSum
	 *            期望获取的最小长度
	 * @return
	 * @throws IOException
	 */
	private int getMemLen(int iWantRecSum) {
		int temp = 0;
		for (int i = 0; i < 60; i++) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				LOG.error("Thread sleep error.", e);
			}
			try {
				if (temp == receive.available() && temp >= iWantRecSum) {
					break;
				} else {
					temp = receive.available();
				}
			} catch (Exception e) {
				LOG.error("get received num failed.", e);
			}
		}
		return temp;
	}
	
	/**
	 * 根据变量类型将变量名或变量名_量程字符串转对象
	 * @param varType
	 * @param oriStr
	 * @return
	 */
	private VariableModel genVarByType(byte varType,int index, String oriStr) {
		if(VALUE_LENGTH_TYPE_R4 == getLenByType(varType)){
			String name = StringUtils.substringBefore(oriStr, "{");
			float start = Float.parseFloat(StringUtils.substringBetween(oriStr, "{", ","));
			float end = Float.parseFloat(StringUtils.substringBetween(oriStr, ",", "}"));
			return new VariableModel(index, name, start, end);
		} else {
			return new VariableModel(index, oriStr);
		}
	}

	public static void main(String[] args) throws InterruptedException, JsonProcessingException {
		SocketServiceImpl i = new SocketServiceImpl();
		i.connectSocket();
		System.out.println("-------------------------");
		List<VariableModel> list = i.getIndexVariRelation((byte) 0x11);
		System.out.println("-------------------------");
		Map<Integer, Float> map = i.initIndexValueRelation((byte) 0x11);
		System.out.println("map is :---------->" + map);
		
		
		for (VariableModel model : list) {
			model.setValue(map.get(model.getVarIndex()));
//			System.out.println(model);
		}
		BatchTypeVariable batch = new BatchTypeVariable(0x11,list);
		//TODO 通过HTTP接口发送到IOT
		JSONObject json = JSONObject.fromObject(batch);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("msg", json.toString());
		String url = "https://127.0.0.1/TestWeb/init";
		
		String get = HTTPClientUtils.doPost(url, params, "UTF-8");
		System.out.println(get);
		System.out.println(map);

		 while(true) {
			 Thread.sleep(3000);
			 System.out.println("-------------------------");
			 Map<Integer, Float> m = i.getIndexValueRelationByTime((byte)0x11,5);
			 
			 List<VariableModel> lists = new ArrayList<>();
			 for (Entry<Integer, Float> entry : m.entrySet()) {
				lists.add(new VariableModel(entry.getKey(),entry.getValue()));
			}
			 BatchTypeVariable updateBatch = new BatchTypeVariable(0x11, lists);
//			 //通过HTTP接口发送到IOT
//			 ObjectMapper mapper = new ObjectMapper();
//			 String string = mapper.writeValueAsString(m);
			 
			 JSONObject updateJson = JSONObject.fromObject(updateBatch);
			 Map<String, String> param = new HashMap<String, String>();
				param.put("msg", updateJson.toString());
				String ur = "https://127.0.0.1/TestWeb/update";
				
				String gets = HTTPClientUtils.doPost(ur, param, "UTF-8");
				System.out.println(gets);
			 
			 System.out.println(m);
			 System.out.println("---------------------------");
		 }
	}
}
