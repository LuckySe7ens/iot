package com.yonyou.socket.tcp.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 变量
 * @author liushiquan 
 * @date 2017-5-24 15:35:34
 *
 */

@Document(collection="AI")
public class VariableModel {
	
	/**
	 * 变量索引
	 */
	private Integer varIndex;
	
	/**
	 * 变量名
	 */
	private String name;
	
	/**
	 * 当前值
	 */
	private Float value;
	
	/**
	 * 范围最小值
	 */
	private Float minValue;
	
	/**
	 * 范围最大值
	 */
	private Float maxValue;
	
	/**
	 * 下限告警临界值
	 */
	private Float warnLowLimit;
	
	/**
	 * 上限告警临界值
	 */
	private Float warnHighLimit;
	
	public VariableModel() {
	}
	
	
	public VariableModel(Integer varIndex, Float value) {
		this.varIndex = varIndex;
		this.value = value;
	}


	public VariableModel(Integer varIndex, String name) {
		this.varIndex = varIndex;
		this.name = name;
	}
	
	public VariableModel(Integer varIndex, String name, Float minValue, Float maxValue) {
		this.varIndex = varIndex;
		this.name = name;
		this.maxValue = maxValue;
		this.minValue = minValue;
	}

	public VariableModel(Integer varIndex, String name, Float value,Float minValue, Float maxValue,  Float warnLowLimit,
			Float warnHighLimit) {
		this.varIndex = varIndex;
		this.name = name;
		this.value = value;
		this.maxValue = maxValue;
		this.minValue = minValue;
		this.warnLowLimit = warnLowLimit;
		this.warnHighLimit = warnHighLimit;
	}
	
	

	public Integer getVarIndex() {
		return varIndex;
	}

	public void setVarIndex(Integer varIndex) {
		this.varIndex = varIndex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public Float getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Float maxValue) {
		this.maxValue = maxValue;
	}

	public Float getMinValue() {
		return minValue;
	}

	public void setMinValue(Float minValue) {
		this.minValue = minValue;
	}

	public Float getWarnLowLimit() {
		return warnLowLimit;
	}

	public void setWarnLowLimit(Float warnLowLimit) {
		this.warnLowLimit = warnLowLimit;
	}

	public Float getWarnHighLimit() {
		return warnHighLimit;
	}

	public void setWarnHighLimit(Float warnHighLimit) {
		this.warnHighLimit = warnHighLimit;
	}

	@Override
	public String toString() {
		return "VariableModel [varIndex=" + varIndex + ", name=" + name + ", value=" + value + ", minValue=" + minValue
				+ ", maxValue=" + maxValue + ", warnLowLimit=" + warnLowLimit + ", warnHighLimit=" + warnHighLimit
				+ "]";
	}
}
