/**
 * 
 */
package com.pearson.ptb.proxy.repo;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.google.common.collect.ImmutableMap;
import com.pearson.ptb.bean.Container;
import com.pearson.ptb.dataaccess.GenericMongoRepository;
import com.pearson.ptb.framework.exception.BaseException;
import com.pearson.ptb.framework.exception.NotFoundException;
import com.pearson.ptb.proxy.ContainerDelegate;

import lombok.RequiredArgsConstructor;

/**
 * Implementation class which got implemented from ContainerDelegate.
 * 
 * @see com.pearson.ptb.proxy.ContainerDelegate
 *
 */
@Repository("container")
@RequiredArgsConstructor
public class ContainerRepo implements ContainerDelegate {
	
	


	private final GenericMongoRepository<Container, String> genericMongoRepository;

	@Override
	public List<Container> getRootLevelContainersByBookId(String bookID) {
		Query query = genericMongoRepository.createDataQuery();
		query.addCriteria(Criteria.where(QueryFields.BOOKID).is(bookID))
			  .addCriteria(Criteria.where(QueryFields.PARENTID).is(StringUtils.EMPTY))
			  .with(Sort.by(QueryFields.SEQUENCE));
		List<Container> containerList = genericMongoRepository.findAll(query);
		return containerList;
	}

	/**
	 * Retrieves a container by its unique identifier.
	 *
	 * @param containerId
	 *            The unique identifier of the container to retrieve.
	 * @return The container with the specified ID.
	 */
	@Override
	public Container getContainerById(String containerId) {
		Query query = genericMongoRepository.createDataQuery();
		query.addCriteria(Criteria.where(QueryFields.GUID).is(containerId));
		Container container = genericMongoRepository.findOne(query,
				Container.class);

		return container;
	}

	/**
	 * Retrieves a list of container children based on the parent container's
	 * ID.
	 *
	 * @param containerId
	 *            The unique identifier of the parent container.
	 * @return A list of containers that are children of the specified parent
	 *         container.
	 */
	@Override
	public List<Container> getContainerChildrenById(String containerId) {
		Query query = genericMongoRepository.createDataQuery();
		query.addCriteria(Criteria.where(QueryFields.PARENTID).is(containerId));
		query.with(Sort.by(QueryFields.SEQUENCE));
		List<Container> containers = genericMongoRepository.findAll(query);

		return containers;
	}

	/**
	 * Retrieves a flat view of containers associated with a specific book.
	 *
	 * @param bookID
	 *            The unique identifier of the book for which containers are
	 *            retrieved.
	 * @return A list of containers associated with the specified book in a flat
	 *         view.
	 */
	@Override
	public List<Container> getContainersFlatViewByBookId(String bookID) {

		Query query = genericMongoRepository.createDataQuery();
		query.addCriteria(Criteria.where(QueryFields.BOOKID).is(bookID));
		query.with(Sort.by(QueryFields.SEQUENCE));
		List<Container> findAll = genericMongoRepository.findAll(query);
		System.out.println(findAll + "cccccccccccccccc");
		return findAll;

	}

	@Override
	public Container getContainerByContainerId(String containerid) {
		Query query = genericMongoRepository.createDataQuery();
		query.addCriteria(Criteria.where(QueryFields.GUID).is(containerid));		
		return genericMongoRepository.findOne(query, Container.class);
	}

	@Override
	public List<Container> getContainerByQuestionids(String bookID,
			List<String> questionids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle(String bookID, String containerID) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Retrieves a list of question bindings associated with a specific book and
	 * container.
	 *
	 * @param bookID
	 *            The unique identifier of the book.
	 * @param containerID
	 *            The unique identifier of the container.
	 * @return A list of question bindings associated with the specified book
	 *         and container.
	 */
	@Override
	public List<String> getQuestionBindings(String bookID, String containerID) {
		return genericMongoRepository
				.getListFieldByCriteria(
						ImmutableMap.of(QueryFields.BOOKID, bookID,
								QueryFields.GUID, containerID),
						"questionBindings");

	}

	@Override
	public void save(List<Container> containers) {

		genericMongoRepository.saveAll(containers);
	}
	

}
