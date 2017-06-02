package com.yonyou.socket.tcp.model;

import java.util.List;

/**
 * 某种类型的变量
 * @author liushiquan
 * @date 2017-5-24 15:42:04
 *
 */
public class BatchTypeVariable {
	
	/**
	 * 变量类型
	 */
	private Integer variableType;
	
	/**
	 * 变量名
	 */
	List<VariableModel> variableModels;

	public BatchTypeVariable() {
	}

	public BatchTypeVariable(Integer variableType, List<VariableModel> variableModels) {
		this.variableType = variableType;
		this.variableModels = variableModels;
	}

	public Integer getVariableType() {
		return variableType;
	}

	public void setVariableType(Integer variableType) {
		this.variableType = variableType;
	}

	public List<VariableModel> getVariableModels() {
		return variableModels;
	}

	public void setVariableModels(List<VariableModel> variableModels) {
		this.variableModels = variableModels;
	}

	@Override
	public String toString() {
		return "BatchTypeVariable [variableType=" + variableType + ", variableModels="
				+ variableModels + "]";
	}

}
