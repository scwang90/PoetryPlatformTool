package com.poetry.platformtool.domain;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;

public class AvDomain<T extends AVObject> {

	AVQuery<T> query = null;
	
	public AvDomain(Class<T> clazz) {
		// TODO Auto-generated constructor stub
		query = AVObject.getQuery(clazz);
	}
	
	public void add(T model) throws AVException{
		model.save();
	}
	
	public List<T> list(int skip,int limit) throws AVException{
		query.setSkip(skip);
		query.setLimit(limit);
		return query.find();
	}
}
