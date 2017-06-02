package com.yonyou.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.springframework.transaction.annotation.Transactional;


@Named("userApplication")
@Transactional
public class UserApplicationImpl implements UserApplication {

	@Override
	public List<UserVO> sayHello(String name) {
		System.out.println("post--->");
		List<UserVO> userList = new ArrayList<UserVO>();
		UserVO userVO = new UserVO();
		userVO.setName(name);
		userVO.setAge(23);
		userList.add(userVO);
		return userList;
	}

	@Override
	public int insert(String name, int age) {
		System.out.println("insert --> Name:" + name + ",Age:" + age);
		return 1;
	}

	@Override
	public int delete(String name) {
		System.out.println("delete --> Name:" + name);
		return 1;
	}

	@Override
	public int add(String name, int age) {
		System.out.println("add---> Name:" + name +",Age:" + age);
		return 0;
	}

	@Override
	public int adds(String name, String age) {
		System.out.println("adds---> Name:" + name +",Age:" + age);
		return 0;
	}
	
}