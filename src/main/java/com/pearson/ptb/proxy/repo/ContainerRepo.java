/**
 * 
 */
package com.pearson.ptb.proxy.repo;

import java.net.UnknownHostException;
import java.util.List;

//import org.mongodb.morphia.query.Query;

import com.google.common.collect.ImmutableMap;
import com.pearson.ptb.bean.Container;
import com.pearson.ptb.framework.exception.ConfigException;
import com.pearson.ptb.framework.exception.NotFoundException;
import com.pearson.ptb.proxy.ContainerDelegate;

import org.springframework.stereotype.Repository;

/**
 * Implementation class which got implemented from ContainerDelegate.
 * 
 * @see com.pearson.ptb.proxy.ContainerDelegate
 *
 */
@Repository("containers")
public class ContainerRepo implements ContainerDelegate {

	@Override
	public List<Container> getRootLevelContainersByBookId(String bookID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Container getContainerById(String containerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Container> getContainerChildrenById(String containerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Container> getContainersFlatViewByBookId(String bookID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Container getContainerByContainerId(String containerid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Container> getContainerByQuestionids(String bookID, List<String> questionids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle(String bookID, String containerID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getQuestionBindings(String bookID, String containerID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(List<Container> containers) {
		// TODO Auto-generated method stub
		
	}
	/*
	 * 
	 * private DataAccessHelper<Container> accessor;
	 * 
	 *//**
		 * Constructor to access DataAccessHelper to perform Container operation.
		 * 
		 * @throws ConfigException
		 * @throws UnknownHostException
		 */
	/*
	 * public ContainerRepo(){ //accessor = new
	 * DataAccessHelper<Container>(Container.class); }
	 * 
	 *//**
		 * Get the list of Containers for the given bookid
		 * 
		 * @throws NotFoundException
		 * @throws BaseException
		 */
	/*
	 * @Override public List<Container> getRootLevelContainersByBookId(String
	 * bookID){ List<Container> containers = null;
	 * 
	 * Query<Container> query = accessor.getDataQuery(); containers =
	 * query.filter(QueryFields.BOOKID, bookID) .filter(QueryFields.PARENTID, "")
	 * .order("sequence").asList();
	 * 
	 * return containers; }
	 * 
	 *//**
		 * Get the Container for the given bookid and containerid
		 * 
		 * @throws NotFoundException
		 */
	/*
	 * @Override public Container getContainerById(String containerId){
	 * 
	 * Container container = null; Query<Container> query = accessor.getDataQuery();
	 * container = query.filter(QueryFields.GUID, containerId).get();
	 * 
	 * return container; }
	 * 
	 *//**
		 * This method will get the container by container id
		 * 
		 * @param Container id
		 * @return Container
		 */
	/*
	 * @Override public Container getContainerByContainerId(String containerid){
	 * 
	 * Container container = null; Query<Container> query = accessor.getDataQuery();
	 * container = query.filter(QueryFields.GUID, containerid).get();
	 * 
	 * return container; }
	 * 
	 *//**
		 * This method will fetch the containers for which the sent questions is binded
		 * 
		 * @param list of questionid's
		 * @return list of Container
		 */
	/*
	 * @Override public List<Container> getContainerByQuestionids(String bookID,
	 * List<String> questionids){ List<Container> containers = null;
	 * Query<Container> query = accessor.getDataQuery(); containers =
	 * query.filter(QueryFields.BOOKID, bookID).filter("questionBindings in",
	 * questionids).asList();
	 * 
	 * return containers; }
	 * 
	 *//**
		 * Get the Container children for the given book id and container id
		 * 
		 * @throws NotFoundException
		 */
	
	/*
	 * @Override public List<Container> getContainerChildrenById(String containerId)
	 * {
	 * 
	 * List<Container> containers = null; Query<Container> query =
	 * accessor.getDataQuery(); containers = query.filter(QueryFields.PARENTID,
	 * containerId) .order(QueryFields.SEQUENCE).asList();
	 * 
	 * return containers; }
	 */
	  
	 /**
		 * This method gets all the containers up to nth level for a given book
		 * 
		 * @param bookID
		 * @return list of Container
		 */
	/*
	 * @Override public List<Container> getContainersFlatViewByBookId(String bookID)
	 * {
	 * 
	 * List<Container> containers = null; Query<Container> query =
	 * accessor.getDataQuery(); containers = query.filter(QueryFields.BOOKID,
	 * bookID) .order(QueryFields.SEQUENCE).asList();
	 * 
	 * return containers; }
	 * 
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.pearson.mytest.proxy.ContainerDelegate#getTitle(String,String)
	 * 
	 * @Override public String getTitle(String bookID, String containerID) { return
	 * accessor.getBaseFieldByCriteria( ImmutableMap.of(QueryFields.BOOKID, bookID,
	 * QueryFields.GUID, containerID), QueryFields.TITLE); }
	 * 
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.pearson.mytest.proxy.ContainerDelegate#getQuestionBindings(String,String)
	 * 
	 * @Override public List<String> getQuestionBindings(String bookID, String
	 * containerID){ return accessor.getListFieldByCriteria(
	 * ImmutableMap.of(QueryFields.BOOKID, bookID, QueryFields.GUID, containerID),
	 * "questionBindings"); }
	 * 
	 *//**
		 * @see com.pearson.mytest.proxy.ContainerDelegate#save(List<Container>)
		 *//*
			 * @Override public void save(List<Container> containers) {
			 * accessor.save(containers); }
			 */

}
