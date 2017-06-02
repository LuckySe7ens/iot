package com.yonyou.socket.tcp.model;

/**
 * 变量类型
 * @author liushiquan
 *@Date 2017-5-24 15:56:11
 */
public class VariableType {
	/** 变量名 AI */
	public static final byte VARNAME_TYPE_AI = 0x01;
	/** 变量名 AO */
	public static final byte VARNAME_TYPE_AO = 0x02;
	/** 变量名 AR */
	public static final byte VARNAME_TYPE_AR = 0x03;
	/** 变量名 VA */
	public static final byte VARNAME_TYPE_VA = 0x07;
	/** 变量名 DI */
	public static final byte VARNAME_TYPE_DI = 0x04;
	/** 变量名 DO */
	public static final byte VARNAME_TYPE_DO = 0x05;
	/** 变量名 DR */
	public static final byte VARNAME_TYPE_DR = 0x06;
	/** 变量名 VD */
	public static final byte VARNAME_TYPE_VD = 0x08;
	/** 变量名 VT */
	public static final byte VARNAME_TYPE_VT = 0x09;
	/** 变量名+量程 AI */
	public static final byte VARNAME_RANGE_TYPE_AI = 0x11;
	/** 变量名+量程 AO */
	public static final byte VARNAME_RANGE_TYPE_AO = 0x12;
	/** 变量名+量程 AR */
	public static final byte VARNAME_RANGE_TYPE_AR = 0x13;
	/** 变量名+量程 VA */
	public static final byte VARNAME_RANGE_TYPE_VA = 0x17;
}
