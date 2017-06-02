package com.yonyou.socket.tcp.service;

import java.util.List;
import java.util.Map;

import com.yonyou.socket.tcp.model.VariableModel;

/**
 * Socket操作接口
 * @author liushiquan
 *
 */
public interface SocketService {
	
	/**
	 * 建立socket远程连接
	 * @param ip 
	 * @param port
	 * @return 是否连接成功
	 */
	boolean connectSocket();
	/**
	 * 根据变量类型获取所有的变量对象（varIndex:varName/varName_range）
	 * @param varType 变量类型（AI、DI）
	 * @return
	 */
	List<VariableModel> getIndexVariRelation(Byte varType);
	
	/**
	 * 根据变量类型获取初始化的变量索引与变量值关系
	 * @param varType 变量类型（AI、DI）
	 * @return
	 */
	Map<Integer,Float> initIndexValueRelation(Byte varType);
	
	/**
	 * 批量读取某时间段内发生变化的变量值
	 * @param varType 变量类型（AI、DI）
	 * @param timeRange 变化时间段（获取初始化值时该值置0）
	 * @return
	 */
	Map<Integer,Float> getIndexValueRelationByTime(Byte varType,int timeRange);
	
}
