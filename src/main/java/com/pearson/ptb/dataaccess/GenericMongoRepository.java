package com.pearson.ptb.dataaccess;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.pearson.ptb.framework.exception.NotFoundException;

/**
 * GenericMongoRepository provides generic CRUD operations for MongoDB.
 * 
 * @param <T>
 *            The type of the entity.
 * @param <ID>
 *            The type of the entity identifier.
 * @author manojkumar.ns
 */
@Repository
public class GenericMongoRepository<T, ID> {

	private final MongoOperations mongoOperations;
	private final Class<T> clazz;

	/**
	 * Default constructor initializing the MongoDB operations and entity class.
	 */
	public GenericMongoRepository() {
		this(null, null);
	}

	/**
	 * Parameterized constructor initializing the MongoDB operations and entity
	 * class.
	 * 
	 * @param mongoOperations
	 *            The MongoDB operations.
	 * @param clazz
	 *            The entity class.
	 */
	public GenericMongoRepository(MongoOperations mongoOperations,
			Class<T> clazz) {
		this.mongoOperations = mongoOperations;
		this.clazz = clazz;
	}

	/**
	 * Saves the entity to the MongoDB database.
	 * 
	 * @param entity
	 *            The entity to be saved.
	 * @return The saved entity.
	 */
	public T save(T entity) {
		mongoOperations.save(entity);
		return entity;
	}

	/**
	 * Retrieves an entity by its identifier.
	 * 
	 * @param id
	 *            The identifier of the entity.
	 * @return The entity with the given identifier.
	 */
	public T findById(ID id) {
		return mongoOperations.findById(id, clazz);
	}

	/**
	 * Retrieves all entities of the specified type.
	 * 
	 * @return List of all entities.
	 */
	public List<T> findAll() {
		return mongoOperations.findAll(clazz);
	}

	/**
	 * Deletes an entity by its identifier.
	 * 
	 * @param id
	 *            The identifier of the entity to be deleted.
	 */
	public void deleteById(ID id) {
		mongoOperations.remove(Query.query(Criteria.where("_id").is(id)),
				clazz);
	}

	/**
	 * Deletes entities by their identifiers.
	 * 
	 * @param ids
	 *            List of identifiers for entities to be deleted.
	 */
	public void delete(List<String> ids) {
		Query query = new Query(Criteria.where("_id").in(ids));
		mongoOperations.remove(query, clazz);
	}

	/**
	 * Retrieves distinct values of a specified field from the MongoDB
	 * collection.
	 * 
	 * @param fieldName
	 *            The name of the field.
	 * @return List of distinct values.
	 */
	public List<String> getDistinctValuesByField(String fieldName) {
		return mongoOperations.query(clazz).distinct(fieldName).as(String.class)
				.all().stream().filter(value -> value != null)
				.collect(Collectors.toList());
	}

	/**
	 * Finds entities based on the provided query.
	 * 
	 * @param query
	 *            The MongoDB query.
	 * @return List of entities matching the query.
	 */
	public List<T> findAll(Query query) {
		return mongoOperations.find(query, clazz);
	}

	/**
	 * Finds entities based on the provided filter criteria.
	 * 
	 * @param criteria
	 *            The filter criteria.
	 * @return List of entities matching the filter criteria.
	 */
	public List<T> getByFilter(Map<String, String> criteria) {
		Query query = getDataQuery(criteria);
		return mongoOperations.find(query, clazz);
	}

	/**
	 * Creates a new MongoDB query based on the provided filter criteria.
	 * 
	 * @return The MongoDB query.
	 */
	public Query createDataQuery() {
		return new Query().addCriteria(new Criteria());
	}

	/**
	 * Saves a list of entities to the MongoDB database.
	 * 
	 * @param entities
	 *            The list of entities to be saved.
	 * @return The list of saved entities.
	 */
	public List<T> saveAll(List<T> entities) {
		mongoOperations.insertAll(entities);
		return entities;
	}

	/**
	 * Finds a single entity based on the provided query.
	 * 
	 * @param query
	 *            The MongoDB query.
	 * @param clazz
	 *            The entity class.
	 * @return The single entity matching the query.
	 */
	public T findOne(Query query, Class<T> clazz) {
		return mongoOperations.findOne(query, clazz);
	}

	/**
	 * Finds entities based on the provided query and entity class.
	 * 
	 * @param query
	 *            The MongoDB query.
	 * @param clazz
	 *            The entity class.
	 * @return List of entities matching the query.
	 */
	public List<T> findAll(Query query, Class<T> clazz) {
		return mongoOperations.find(query, clazz);
	}

	/**
	 * Retrieves a list of values for a specified field based on the provided
	 * filter criteria.
	 * 
	 * @param criteria
	 *            The filter criteria.
	 * @param fieldName
	 *            The name of the field.
	 * @return List of values for the specified field.
	 * @throws NotFoundException
	 *             if the field is not found in the bean.
	 */
	@SuppressWarnings("unchecked")
	public List<String> getListFieldByCriteria(Map<String, String> criteria,
			String fieldName) {
		List<String> returnValue;
		try {
			Query query = getDataQuery(criteria);
			query.fields().include(fieldName);
			T object = mongoOperations.findOne(query, clazz);

			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			returnValue = (List<String>) field.get(object);

			return returnValue;
		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException | IllegalAccessException
				| NullPointerException e) {
			throw new NotFoundException(
					"Field " + fieldName + " is not found in the bean", e);
		}
	}

	/**
	 * Creates a MongoDB query based on the provided filter criteria.
	 * 
	 * @param criteria
	 *            The filter criteria.
	 * @return The MongoDB query.
	 */
	private Query getDataQuery(Map<String, String> criteria) {
		Criteria criteriaObject = new Criteria();
		criteria.forEach((key, value) -> criteriaObject.and(key).is(value));
		return new Query(criteriaObject);
	}
	
	public T findOneByUserIdAndParentId(String userId, String parentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId).and("parentId").is(parentId));
        return mongoOperations.findOne(query, clazz);
    }
	
	  public List<T> findByParentId(String parentId) {
	        Query query = new Query(Criteria.where("parentId").is(parentId));
	        return mongoOperations.find(query, clazz);
	    }

	    public void delete(T entity) {
	        mongoOperations.remove(entity);
	    }
}
