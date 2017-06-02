package com.yonyou.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yonyou.service.IotService;
import com.yonyou.socket.tcp.model.BatchTypeVariable;
import com.yonyou.socket.tcp.model.VariableModel;

import net.sf.json.JSONObject;

@Controller
public class TestController {
	
	@Autowired
	private IotService iotService;

	private static final Logger LOG = LoggerFactory.getLogger(TestController.class);
	/**
	 * 启动的时候初始化节点数据
	 * @param req
	 * @param res
	 * @param msg
	 * @throws IOException
	 */
	@RequestMapping(value = "/init")
	public void getMsg(HttpServletRequest req, HttpServletResponse res, String msg) throws IOException {
		System.out.println("running getMsg.....");
		// System.out.println("msg---->" + msg);
		JSONObject json = JSONObject.fromObject(msg);
		Object object = json.get("variableModels");
		Map<String, Class> clazz = new HashMap<>();
		clazz.put("variableModels", VariableModel.class);
		BatchTypeVariable bean = (BatchTypeVariable) JSONObject.toBean(json, BatchTypeVariable.class, clazz);
		// System.out.println(bean);
		List<VariableModel> list = bean.getVariableModels();
		iotService.init(bean.getVariableType().toString(), list);
		for (VariableModel variableModel : list) {
			System.out.println(variableModel);
		}
		res.getWriter().print("init ok");
	}

	/**
	 * 定时更新节点的信息
	 * @param req
	 * @param res
	 * @param msg
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping(value = "/update")
	public void updateMsg(HttpServletRequest req, HttpServletResponse res, String msg)
			throws JsonParseException, JsonMappingException, IOException {

//		Map<Integer, Float> map = new HashMap<>();
//		ObjectMapper mapper = new ObjectMapper();
//		map = mapper.readValue(msg, new TypeReference<HashMap<Integer, Float>>() {});
//		System.out.println(map);
		
//		iotService.update(topic, datas);
		JSONObject json = JSONObject.fromObject(msg);
		Object object = json.get("variableModels");
		Map<String, Class> clazz = new HashMap<>();
		clazz.put("variableModels", VariableModel.class);
		BatchTypeVariable bean = (BatchTypeVariable) JSONObject.toBean(json, BatchTypeVariable.class, clazz);
		// System.out.println(bean);
		List<VariableModel> list = bean.getVariableModels();
		iotService.update(bean.getVariableType().toString(), list);
		iotService.insertBatch(bean.getVariableType().toString() + "_UPDATE", list);
		res.getWriter().print("update ok");
	}
	
	//根据传入的pageSize，offset参数决定查哪一页,根据其他参数决定查询哪些数据   
	@RequestMapping( value = "/data", method = RequestMethod.POST, produces = "application/json;charset=UTF-8" )  
	  @ResponseBody  
	  public Object channelDivideDetailsData( HttpServletRequest request, @RequestBody JSONObject jsonObj ) {
		return null;
	} 
}
