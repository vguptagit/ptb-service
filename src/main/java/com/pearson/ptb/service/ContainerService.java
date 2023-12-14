package com.pearson.ptb.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.pearson.ptb.bean.Container;
import com.pearson.ptb.bean.ContainerSorter;
import com.pearson.ptb.bean.QuestionMetadata;
import com.pearson.ptb.framework.exception.NotFoundException;
import com.pearson.ptb.proxy.ContainerDelegate;
import com.pearson.ptb.util.SearchHelper;

import org.springframework.stereotype.Service;

/**
 * 
 * Serves container related details
 *
 */
@Service("containerService")
public class ContainerService {

	/**
	 * 
	 */
	@Autowired
	@Qualifier("containers")
	private ContainerDelegate containerDelegate;

	/*
	 * @Autowired
	 * 
	 * @Qualifier("questionService") private QuestionService questionService;
	 */

	/**
	 * Returns a list of all containers for the given Book
	 * 
	 * @param bookID
	 * @return list of container
	 * @throws NotFoundException
	 */
    public List<Container> getContainers(String bookID, boolean flat, Map<String, String> filterCriteria) {
		List<Container> containers;
		if(flat){
			containers = this.getContainersFlatViewByBookIds(bookID);
        } else if (!filterCriteria.isEmpty()) {
            containers = getContainersBySearchCriteria(bookID, filterCriteria);
		} else {
            containers = containerDelegate.getRootLevelContainersByBookId(bookID);            
		}
		return containers;
	}
	
	/**
	 * This method gets a list of all containers for the given Book
	 * 
	 * @param bookID
	 * @param containerid
	 * 					, id of the container which book belongs to.
	 * @param quizTypes
	 * @param includeSelf
	 * @return list of container
	 */
	public List<Container> getContainers(String bookID, String containerid,
            Map<String, String> searchCriteria, boolean includeSelf) {
		List<Container> containers = new ArrayList<Container>();
		containers = getContainerChildrenById(containerid);

		if (includeSelf) {
			containers.add(getContainerById(containerid));
		}
        if (!searchCriteria.isEmpty() && !containers.isEmpty()) {
            containers = getContainersBySearchCriteria(bookID, searchCriteria, containers);
		}

		return containers;
	}

	/**
	 * Return container for the given id
	 * @param containerid
	 * 
	 * @return Container details
	 */
	public Container getContainerById(String containerId) {

		return containerDelegate.getContainerById(containerId);
	}

	/**
	 * Return container children for the given id
	 * @param containerId
	 * 
	 * @return list of container
	 * @throws NotFoundException
	 */
	public List<Container> getContainerChildrenById(String containerId) {

		return containerDelegate.getContainerChildrenById(containerId);
	}

	/**
	 * This method will filter the containers which has questions types which
	 * has been sent
	 * 
	 * @param bookID
	 * @param quizTypes
	 * @param containers
	 * @return list of containers
	 */
    private List<Container> getContainersBySearchCriteria(String bookID,
            Map<String, String> searchCriteria, List<Container> containers) {
        List<Container> questionContainers = getQuestionContainers(bookID,searchCriteria);
		List<Container> finalContainers = new ArrayList<Container>();
		boolean isContainerHasQuizType = false;

		for (Container container : containers) {
			List<Container> childContainers = new ArrayList<Container>();
			getAllChildContainers(container, childContainers);

			if (!childContainers.isEmpty()) {
				isContainerHasQuizType = isChildContainerContainsQuizType(
						questionContainers, isContainerHasQuizType,
						childContainers);
			} else {
				isContainerHasQuizType = isContainerContainsQuizType(
						questionContainers, isContainerHasQuizType, container);
			}
			
			
			if (isContainerHasQuizType) {
				finalContainers.add(container);
				isContainerHasQuizType = false;
			}
		}
		Collections.sort(finalContainers, new ContainerSorter());
		return finalContainers;
	}



	/**
	 * This method will get containers flat view for the given book id. 
	 * @param bookids
	 * @return
	 */
	public List<Container> getContainersFlatViewByBookIds(String bookid) {
		return containerDelegate.getContainersFlatViewByBookId(bookid);
	}

	/**
	 * This method will get all questions of quiz types passed for a given
	 * course. Then it will get the containers for which the questions are
	 * binded. Questions can be in any level container, so next it will get the
	 * root container for a give container
	 * 
	 * @param bookId
	 * @param quizTypes
	 * @return list of container
	 */
    private List<Container> getContainersBySearchCriteria(String bookId,Map<String, String> filterCriteria) {
		List<Container> parentContainers = new ArrayList<Container>();
        List<Container> containers = getQuestionContainers(bookId, filterCriteria);
		for (Container container : containers) {
			getParentContainer(container, parentContainers);
		}
		Collections.sort(parentContainers, new ContainerSorter());
		return parentContainers;
	}

	private List<Container> getQuestionContainers(String bookId,Map<String, String> filterCriteria) {
		List<Container> rootContainers = new ArrayList<Container>();
        List<QuestionMetadata> questionMetadata = getQuestionsBySearchCriteria(bookId,filterCriteria);
		
		List<Container> bookContainers = getContainersFlatViewByBookIds(bookId);
		for (Container bookContainer : bookContainers) {
			for (QuestionMetadata question : questionMetadata) {
				if (bookContainer.getQuestionBindings().contains(
						question.getGuid().trim())) {
					rootContainers.add(bookContainer);
					break;
				}
			}
		}
		return rootContainers;
	}
	
    private List<QuestionMetadata> getQuestionsBySearchCriteria(String bookId,Map<String, String> filterCriteria){
        List<QuestionMetadata> filteredQuestions = new ArrayList<QuestionMetadata>();    
		//List<QuestionMetadata> bookQuestions = questionService.getQuestions(bookId);
		
//		for (QuestionMetadata bookQuestion : bookQuestions) {
//            SearchHelper.filterQuestionBySearchCriteria(bookQuestion,filterCriteria,filteredQuestions);
//		}
		return filteredQuestions;
	}

	/**
	 * This is a recursive function which get the root level container. it will
	 * get the containers until it get the container for which parent id is null
	 * 
	 * @param container
	 * @param parentContainers
	 */
	private void getParentContainer(Container container,
			List<Container> parentContainers) {
		String parentContainerid = container.getParentId();
		if (parentContainerid != null && !parentContainerid.isEmpty()) {
			try {
				getParentContainer(
						containerDelegate
								.getContainerByContainerId(parentContainerid),
						parentContainers);
			} catch (NotFoundException e) { // NOSONAR
			}
		} else {
			boolean isParentContainerExists = false;
			for (Container parentContainer : parentContainers) {
				if (parentContainer.getGuid()
						.equals(container.getGuid().trim())) {
					isParentContainerExists = true;
				}
			}
			if (!isParentContainerExists) {
				parentContainers.add(container);
			}
		}
	}

	/**
	 * This will get all the child containers for a given container
	 * @param parentContainer
	 * @param childContainers
	 */
	private void getAllChildContainers(Container parentContainer,
			List<Container> childContainers) {
		try {
			List<Container> containers = containerDelegate
					.getContainerChildrenById(parentContainer.getGuid());
			childContainers.addAll(containers);
			for (Container container : containers) {
				getAllChildContainers(container, childContainers);
			}
		} catch (NotFoundException e) { // NOSONAR
		}

	}
	
	/**
	 * Checks whether container contains the questions with the given quiz type
	 * @param questionContainers
	 * @param isContainerHasQuizType
	 * @param container
	 * @return
	 */
	private boolean isContainerContainsQuizType(
			List<Container> questionContainers, boolean isContainerHasQuizType,
			Container container) {
		for (Container questionContainer : questionContainers) {
			if (questionContainer.getGuid().equals(container.getGuid())) {
				isContainerHasQuizType = true;
				break;
			}
		}
		return isContainerHasQuizType;
	}

	/**
	 * Checks whether child container contains the questions with given quiz type
	 * @param questionContainers
	 * @param isContainerHasQuizType
	 * @param childContainers
	 * @return
	 */
	private boolean isChildContainerContainsQuizType(
			List<Container> questionContainers, boolean isContainerHasQuizType,
			List<Container> childContainers) {
		for (Container childContainer : childContainers) {
			isContainerHasQuizType = isContainerContainsQuizType(
					questionContainers, isContainerHasQuizType, childContainer);
		}
		return isContainerHasQuizType;
	}
}
