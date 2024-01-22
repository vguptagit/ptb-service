package com.pearson.ptb.proxy.aws.util;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.pearson.ptb.bean.Metadata;
import com.pearson.ptb.bean.Test;
import com.pearson.ptb.bean.TestEnvelop;
import com.pearson.ptb.bean.TestResult;
import com.pearson.ptb.framework.exception.BadDataException;
import com.pearson.ptb.proxy.aws.bean.Activity;
import com.pearson.ptb.proxy.aws.bean.Assignment;
import com.pearson.ptb.proxy.aws.bean.AssignmentEnvelop;

public final class ActivityUtil {
	
	

	/**
	 * Making the Constructor private to avoid this class instantiation
	 */
	private ActivityUtil() {

	}


	
	/**
	 * Gets the test JSON payload from the TestEnvelop been
	 * 
	 * @param test
	 *            the TestEnvelop bean
	 * @param folder
	 *            the folder info to add activity metadata like Book and chapter
	 *            title
	 * @return the JSON payload
	 */
	public static String getTestPayload(TestEnvelop test) {

		AssignmentEnvelop pafTest =ActivityUtil.getPAFAssignmentEnvelop(test);

		Activity metadata = Converter.getDestinationBean(test.getmetadata(),
				Activity.class, Metadata.class);

		pafTest.setMetadata(metadata);
	
		pafTest.getMetadata()
				.setContentTypeTier2(new String[] { "Assignment" });

		Gson gson = new Gson();

		return gson.toJson(pafTest);
	}

	/**
	 * Gets test result from the paf response
	 * 
	 * @param result
	 *            the response of paf activity create or update end point
	 * @return the test results
	 * @throws BadDataException
	 */
	public static TestResult getTestResultBean(String result){
		List<TestResult> testResults = null;
		int firstTest = 0;
		Gson gson = new Gson();
		if (result != null && !result.isEmpty()) {
			// Get the type of test result bean list
			Type type = new TypeToken<List<TestResult>>() {
				private static final long serialVersionUID = 1L;
			}.getType();
			testResults = gson.fromJson(result, type);

		} else {
			throw new BadDataException("Result not found ");
		}
		return testResults.get(firstTest);
	}

	/**
	 * Get the paf assignment envelop after converting evalu8 test envelop into paf assignment envelop
	 * @param testEnvelop the evalu8 test envelop
	 * @return the paf assignment envelop
	 */
	public static AssignmentEnvelop getPAFAssignmentEnvelop(TestEnvelop testEnvelop) {
		Collections.reverse(testEnvelop.getBody().getAssignmentContents().getBinding());
		return Converter.getDestinationBean(testEnvelop, AssignmentEnvelop.class,
				TestEnvelop.class);
	}
	
	/**
	 * Get the evalu8 test after converting paf assignment into evalu8 test
	 * @param test the paf assignment 
	 * @return the evalu8 
	 */
	public static Test getMyTest(Assignment test) {
		//
		Collections.reverse(test.getAssignmentContents().getBinding());
		return Converter.getDestinationBean(test, Test.class,
				Assignment.class);
	}

}
