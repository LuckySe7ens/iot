package com.yonyou.service;

import java.util.List;

import com.yonyou.util.Page;

/**
 * 网关业务接口
 * @author liushiquan
 * @param <T>
 *
 */
public interface IotService<T> {
	
	/**
	 * 初始化数据
	 * @param <T>
	 * @param topic
	 * @param data
	 */
	void init(String topic,List<T> datas);
	
	/**
	 * 更新数据
	 * @param topic
	 * @param map
	 */
	void update(String topic,List<T> datas);
	
	/**
	 * 批量插入
	 * @param topic
	 * @param datas
	 */
	void insertBatch (String topic,List<T> datas);
	
	List<T> findAll (String topic);
	
	List<T> find(String topic, Integer start,Integer limit);
}
