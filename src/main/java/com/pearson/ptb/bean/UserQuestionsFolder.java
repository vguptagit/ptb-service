package com.pearson.ptb.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * The <code>UserQuestionsFolder</code> class responsible to hold the user created questions 
 *
 */
public class UserQuestionsFolder extends UserFolder {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Indicates question Bindings
	 */
	private List<QuestionBinding> questionBindings;
	
	/** Get {@see #questionBindings}. @return {@link #questionBindings}. */
	public List<QuestionBinding> getQuestionBindings() {
		
		if(questionBindings != null && !questionBindings.isEmpty()) {
			Collections.sort(questionBindings, new QuestionBindingComparator());
			return questionBindings;
		} else {
			return new ArrayList<QuestionBinding>();	
		}
	}

	/** Set {@see #questionBindings}. @param {@link #questionBindings}. */
	public void setQuestionBindings(List<QuestionBinding> questionBindings) {
		this.questionBindings = questionBindings;
	}
	
	private class QuestionBindingComparator implements Comparator<QuestionBinding> {

		@Override
		public int compare(QuestionBinding o1, QuestionBinding o2) {

			return Double.compare(o1.getSequence(),
					o2.getSequence());

		}

	}
}
