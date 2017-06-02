package com.yonyou.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.yonyou.socket.tcp.model.VariableModel;

@Repository(value="iotService")
public class IotServiceImpl implements IotService<VariableModel>{

	@Autowired
	private MongoTemplate mongoTemplate;
	@Override
	public void init(String topic, List<VariableModel> datas) {
		mongoTemplate.remove(new Query(), topic);
		mongoTemplate.insert(datas, topic);
	}

	@Override
	public void update(String topic, List<VariableModel> datas) {
		for (VariableModel model : datas) {
			mongoTemplate.updateFirst(new Query(Criteria.where("varIndex").is(model.getVarIndex())), new Update().set("value", model.getValue()), topic);
		}
	}
	
	@Override
	public void insertBatch(String topic, List<VariableModel> datas) {
		mongoTemplate.insert(datas, topic);
	}
	
	@Override
	public List<VariableModel> find(String topic, Integer start, Integer limit) {
		List<VariableModel> list = mongoTemplate.find(new Query().skip(start).limit(limit), VariableModel.class, topic);
		return list;
	}
	
	@Override
	public List<VariableModel> findAll(String topic) {
		List<VariableModel> list = mongoTemplate.findAll(VariableModel.class, topic);
		return list;
	}
}
