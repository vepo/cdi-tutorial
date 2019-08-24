package io.vepo.access.infra;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.mongodb.client.MongoCollection;

@Dependent
public class Repository<T> {

	@Inject
	private MongoCollection<T> collection;
	
	public List<T> findAll() {
		return collection.find().into(new ArrayList<>());
	}
}