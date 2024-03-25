/**
 * 
 */
package com.pearson.ptb.proxy;

import java.util.List;

import com.pearson.ptb.bean.Container;
import com.pearson.ptb.framework.exception.NotFoundException;
import com.pearson.ptb.proxy.aws.bean.QuestionEnvelop;

/**
 * This interface defines the contract for accessing the Container with given
 * book
 *
 */
public interface ContainerDelegate {

	/**
	 * Get the list of Containers for the given book id <code>bookID</code> from
	 * the repository.
	 * 
	 * @param bookID
	 *            Identification number of the book
	 * @return list of Containers for the given book
	 * @throws NotFoundException
	 *             The containers not found custom exceptions
	 */
	List<Container> getRootLevelContainersByBookId(String bookID);

	/**
	 * Get the Container for the given bookid and container id
	 * 
	 * @param containerId
	 *            , identification number
	 * 
	 * @return Container
	 * @throws NotFoundException
	 *             The container not found custom exceptions
	 */
	Container getContainerById(String containerId);

	/**
	 * Get the Container children for the given bookid and container id
	 * 
	 * @param containerId
	 *            , identification number
	 * 
	 * @return Container
	 * @throws NotFoundException
	 *             The container not found custom exceptions
	 */
	List<Container> getContainerChildrenById(String containerId);

	/**
	 * This method gets all the containers up to nth level for a given book
	 * 
	 * @param bookID
	 * @return list of Container
	 */

	List<Container> getContainersFlatViewByBookId(String bookID);

	/**
	 * This method will get the container by container id
	 * 
	 * @param containerid
	 * @return single container
	 */
	Container getContainerByContainerId(String containerid);

	/**
	 * This method will fetch the containers for which the questions sent is
	 * binded
	 * 
	 * @param questionids
	 * @return list of Container
	 */
	List<Container> getContainerByQuestionids(String bookID,
			List<String> questionids);

	/**
	 * Get the title of the container
	 * 
	 * @param bookID,
	 *            bookid where container resides
	 * @param containerID,
	 *            container id
	 * @return title of the container
	 * @throws NotFoundException
	 */
	String getTitle(String bookID, String containerID);

	/**
	 * Get the question bindings for the given container id and book id.
	 * 
	 * @param bookID,
	 *            book id of the book where container resides
	 * @param containerID,
	 *            id of the container
	 * @return question bindings
	 * @throws NotFoundException
	 */
	List<String> getQuestionBindings(String bookID, String containerID);

	/**
	 * Save the nodes
	 * 
	 * @param containers,
	 *            list of nodes
	 */
	void save(List<Container> containers);
	
	
	public List<String> getQuestionBinding(String bookId , String containerID);
}
