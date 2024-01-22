package com.pearson.ptb.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import com.pearson.ptb.framework.exception.BadDataException;
import com.pearson.ptb.util.ExceptionHelper;

/**
 * The <code>UserFolder</code> class is responsible to hold the user folder
 * details
 * 
 * @author nithinjain
 *
 */
@Document(collection = "userTestsFolder")
public class UserFolder extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * The sorting sequence of a folder
	 */
	private double sequence;

	/**
	 * The instructor user id
	 */
	private String userId;

	/**
	 * The parent folder id
	 */
	private String parentId;

	private List<TestBinding> testBindings;

	/** Get {@see #sequence}. @return {@link #sequence}. */
	public double getSequence() {
		return sequence;
	}

	/** Set {@see #sequence}. @param {@link #sequence}. */
	public void setSequence(double sequence) {
		this.sequence = sequence;
	}

	/** Get {@see #userId}. @return {@link #userId}. */
	public String getUserID() {
		return userId;
	}

	/** Set {@see #userId}. @param {@link #userId}. */
	public void setUserID(String userId) {
		this.userId = userId;
	}

	/** Get {@see #parentId}. @return {@link #parentId}. */
	public String getParentId() {
		return parentId;
	}

	/** Set {@see #parentId}. @param {@link #parentId}. */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/** Get {@see #testBindings}. @return {@link #testBindings}. */
	public List<TestBinding> getTestBindings() {

		if (testBindings != null) {
			for (TestBinding testBinding : new ArrayList<>(testBindings)) {
				if (testBinding == null) {
					testBindings.remove(testBinding);
				}
			}
		}

		if (testBindings != null && !testBindings.isEmpty()) {
			Collections.sort(testBindings, new TestBindingComparator());
			return testBindings;
		} else {
			return new ArrayList<TestBinding>();
		}
	}

	/** Set {@see #testBindings}. @param {@link #testBindings}. */
	public void setTestBindings(List<TestBinding> testBindingsList) {

		for (TestBinding testBinding : new ArrayList<>(testBindingsList)) {
			if (testBinding == null) {
				testBindingsList.remove(testBinding);
			}
		}

		testBindings = testBindingsList;
	}

	/**
	 * To remove TestBinding
	 * 
	 * @param testId
	 * @return TestBinding
	 */
	public TestBinding removeTestBinding(String testId) {

		TestBinding testBindingRemoved = null;

		for (TestBinding testBinding : this.testBindings) {
			if (testBinding.getTestId().equals(testId)) {
				testBindingRemoved = testBinding;
				break;
			}
		}
		this.testBindings.remove(testBindingRemoved);

		return testBindingRemoved;
	}

	/**
	 * To Validate the sequence and userId fields
	 * 
	 * @throws BadDataException
	 */
	public void validateState() {

		List<String> messages = new ArrayList<String>();
		// Validate the sequence field and its value should be grater than zero
		if (this.getSequence() <= 0) {
			messages.add("sequence should be grater than zero");
		}

		// Validate userId field
		if (StringUtils.isBlank(this.userId)) {
			messages.add("userId should not be null or Empty");
		}

		if (!messages.isEmpty()) {
			throw new BadDataException(ExceptionHelper.getMessage(messages));
		}
	}

	private class TestBindingComparator implements Comparator<TestBinding> {

		@Override
		public int compare(TestBinding o1, TestBinding o2) {

			return Double.compare(o1.getSequence(), o2.getSequence());
		}

	}
}
