package com.yonyou.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yonyou.service.IotService;
import com.yonyou.socket.tcp.model.VariableModel;
import com.yonyou.util.Page;

@Controller
public class ShowDataController {
	@Autowired
	private IotService iotService;
	
	private String Type_AI = "17";
	
	private String Type_AI_UPDATE = "17_UPDATE";
	//第一次点击链接  
	@RequestMapping(value = "/list", method = RequestMethod.GET)  
	public String query(HttpServletRequest request,@RequestParam(value="pn",defaultValue="1")Integer pn, Map<String, Object> map) {  
		List list = iotService.findAll(Type_AI_UPDATE);
	    // 默认为pageNow = 1  
	    Page page = new Page(list.size(), 1);  
	    List<VariableModel> lists = iotService.find(Type_AI_UPDATE, (pn-1)*10 + 1, 10);
	    map.put("page", page);  
	    map.put("list", lists);  
	    return "main";  
	}  
	  
	//获取json对象  
	@RequestMapping(value = "/list", method = RequestMethod.POST)  
	public @ResponseBody Map<String, Object> queryByFoodPrice(HttpServletRequest request) {  
		Integer pn = Integer.parseInt(request.getParameter("pn"));
	    Map<String, Object> map = new HashMap<String, Object>();  
	    List list = iotService.findAll(Type_AI_UPDATE);
	    // 默认为pageNow = 1  
	    Page page = new Page(list.size(), pn);  
	    List<VariableModel> lists = iotService.find(Type_AI_UPDATE, (pn-1)*10, 10);
	    map.put("page", page);  
	    map.put("list", lists);  
	    return map;  
	}  
}
