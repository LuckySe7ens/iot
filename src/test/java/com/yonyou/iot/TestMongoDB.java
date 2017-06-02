package com.yonyou.iot;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.yonyou.socket.tcp.model.VariableModel;

public class TestMongoDB {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("mongodb-context.xml");
		MongoTemplate dbTemp = (MongoTemplate) ctx.getBean("mongoTemplate");
		System.out.println(dbTemp.getCollectionName(VariableModel.class));
		
		//insert
//		dbTemp.insert(new VariableModel(1, "name1", 0F, 65535.0f), "AI");
		//save
//		dbTemp.save(new VariableModel(1, "name1", 0F, 65535.0f), "AI");
		//findOne
//		VariableModel one = dbTemp.findOne(new Query(Criteria.where("name").is("name1")), VariableModel.class,"AI");
//		System.out.println(one);
		VariableModel m1 = new VariableModel(1, "name1", 0F, 65535.0f);
		VariableModel m2 = new VariableModel(1, "name2", 0F, 65535.0f);
		VariableModel m3 = new VariableModel(1, "name3", 0F, 65535.0f);
		VariableModel m4 = new VariableModel(1, "name4", 0F, 65535.0f);
		List<VariableModel> list = new ArrayList<>();
		list.add(m1);
		list.add(m2);
		list.add(m3);
		list.add(m4);
		dbTemp.remove(new Query(), "variableModel");
		dbTemp.insert(list, "ABC");
//		dbTemp.save(list, "AI");
//		dbTemp.insertAll(list);
//		dbTemp.updateFirst(new Query(Criteria.where("name").is("name1")), new Update().set("warnHighLimit", 129F), VariableModel.class);
		
		
	}
}
