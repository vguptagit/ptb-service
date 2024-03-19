package com.pearson.ptb.proxy.repo;

import org.springframework.stereotype.Repository;


import com.pearson.ptb.bean.Test;
import com.pearson.ptb.proxy.TestDelegate;

import org.springframework.beans.factory.annotation.Autowired;



import com.pearson.ptb.dataaccess.GenericMongoRepository;

import com.pearson.ptb.proxy.aws.util.ActivityUtil;
import com.pearson.ptb.proxy.aws.bean.Assignment;

@Repository("tests")
public class TestRepo implements TestDelegate {
	
	private static final String TEST_NOT_FOUND = "Test not found";
    private static final String TEST_ID_NULL = "Test id is set to null";



    @Autowired
    private GenericMongoRepository<Assignment, String> assignmentRepository;


	@Override
	public Test getTestByID(String testId) {
		
		if (testId == null || testId.trim().isEmpty()) {
            throw new IllegalArgumentException(TEST_ID_NULL);
        }

		 Assignment assignment = assignmentRepository.findById(testId);

	        if (assignment == null) {
	            throw new IllegalArgumentException(TEST_NOT_FOUND);
	        }

        
        Test test = ActivityUtil.getMyTest(assignment);
        test.setGuid(testId);
        return test;
	}

}
