package com.pearson.ptb.dataaccess;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class GenericMongoRepository<T, ID> {

	private final MongoOperations mongoOperations;
	private final Class<T> clazz;

	public GenericMongoRepository(MongoOperations mongoOperations, Class<T> clazz) {
		this.mongoOperations = mongoOperations;
		this.clazz = clazz;
	}

	/**
	 * Saving the entity to the database
	 * 
	 * @param entity , Entity annotated class
	 */
	public T save(T entity) {
		mongoOperations.save(entity);
		return entity;
	}

	/**
	 * Getting the entity value for the given id
	 * 
	 * @param clazz , entity annotated class reference
	 * @param id    , id of the entity
	 * @return entity value
	 */
	public T findById(ID id) {
		return mongoOperations.findById(id, clazz);
	}

	public List<T> findAll() {
		return mongoOperations.findAll(clazz);
	}

	public void deleteById(ID id) {
		mongoOperations.remove(Query.query(Criteria.where("_id").is(id)), clazz);
	}

	public List<String> getDistinctValuesByField(String fieldName) {
		return mongoOperations.query(clazz).distinct(fieldName).as(String.class).all().stream()
				.filter(value -> value != null).collect(Collectors.toList());
	}

	public List<T> findAll(Query query) {
		return mongoOperations.find(query, clazz);
	}

	public List<T> getByFilter(Map<String, String> criteria) {
		Query query = getDataQuery(criteria);
		return mongoOperations.find(query, clazz);
	}

	private Query getDataQuery(Map<String, String> criteria) {
		Criteria criteriaObject = new Criteria();
		criteria.forEach((key, value) -> criteriaObject.and(key).is(value));
		return new Query(criteriaObject);
	}
	
	
	
}
