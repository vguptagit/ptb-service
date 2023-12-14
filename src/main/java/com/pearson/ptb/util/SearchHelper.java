package com.pearson.ptb.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.pearson.ptb.bean.ExtMetadata;
import com.pearson.ptb.bean.QuestionMetadata;

public class SearchHelper {	
	
	static Map<String, String> searchCriteriaMap=null;
    
    /*
     * Constants which has actual text for the abbreviated value  
     */
    private static final String QUIZTYPE_ESSAY = "Essay";
    private static final String QUIZTYPE_MULTIPLERESPONSE = "MultipleResponse";
    private static final String QUIZTYPE_MATCHING = "Matching";
    private static final String QUIZTYPE_MULTIPLECHOICE = "MultipleChoice";
    private static final String QUIZTYPE_TRUEFALSE = "TrueFalse";
    private static final String QUIZTYPE_FILLINBLANKS = "FillInBlanks";
    
    private static final String DIFFICULTYLEVEL_EASY = "Easy";
    private static final String DIFFICULTYLEVEL_MODERATE = "Moderate";
    private static final String DIFFICULTYLEVEL_DIFFICULT = "Difficult";
    
    /*
	 * Static method which represents the keys of the question meta data
	 */
    public static enum QuestionSearchCriteria{
        quizType,
        questionLevelOfDifficulty,
        topic,
        objective,
        pageReference,
        skill,
        questionId        
    }
    
    private SearchHelper(){
        
	}
    /*
     * Updates the map which has search key and value.It will update the value part. 
     * i.e it replaces the abbreviated value to actual text
     */
    public static void updateSearchValues(Map<String, String> filterCriteria){

        for (Map.Entry<String, String> entry : filterCriteria.entrySet()) {
            if(entry.getKey().equals(QuestionSearchCriteria.quizType.toString())){
                String value=entry.getValue();
                value=value.replace("ES", QUIZTYPE_ESSAY);
                value=value.replace("MR", QUIZTYPE_MULTIPLERESPONSE);
                value=value.replace("MT", QUIZTYPE_MATCHING);
                value=value.replace("MC", QUIZTYPE_MULTIPLECHOICE);
                value=value.replace("TF", QUIZTYPE_TRUEFALSE);
                value=value.replace("FIB", QUIZTYPE_FILLINBLANKS);
                entry.setValue(value);
            }else if(entry.getKey().equals(QuestionSearchCriteria.questionLevelOfDifficulty.toString())){
                String value=entry.getValue();
                value=value.replace("Esy", DIFFICULTYLEVEL_EASY);
                value=value.replace("Mod", DIFFICULTYLEVEL_MODERATE);
                value=value.replace("Dif", DIFFICULTYLEVEL_DIFFICULT);
                entry.setValue(value);
            }
        }
    }
    /*
     * This represents the key mapping for question search.Question will be searched based on many criteria,
     * each criteria is sent as abbreviated text.This map will have mapping of abbreviated text and actual value.  
     */

    public static Map<String, String> getQuestionFilterCriteriaMap() {
    	if(searchCriteriaMap==null){
    		searchCriteriaMap = new HashMap<String, String>();	
    		searchCriteriaMap.put("QTyp",QuestionSearchCriteria.quizType.toString());
        	searchCriteriaMap.put("Diff",QuestionSearchCriteria.questionLevelOfDifficulty.toString());
        	searchCriteriaMap.put("Topk",QuestionSearchCriteria.topic.toString());
        	searchCriteriaMap.put("Objt",QuestionSearchCriteria.objective.toString());
        	searchCriteriaMap.put("PRef",QuestionSearchCriteria.pageReference.toString());
        	searchCriteriaMap.put("Skil",QuestionSearchCriteria.skill.toString());
        	searchCriteriaMap.put("QnId",QuestionSearchCriteria.questionId.toString());
    	}
        return searchCriteriaMap;
    }
    /*
     * This method will check the question whether the question is meeting the search criteria,if it matches it will add the question to a list.
     * questionMetadata : question for which need to be validated against search criteria
     * filterCriteria : search criteria for searching question
     * filteredQuestions : List of questions. THis will be filled if question meets  search criteria.
     */
    public static void filterQuestionBySearchCriteria(QuestionMetadata questionMetadata,Map<String, String> filterCriteria,List<QuestionMetadata> filteredQuestions){
        for (Map.Entry<String, String> entry : filterCriteria.entrySet()) {
            if(entry.getKey().equals(QuestionSearchCriteria.quizType.toString())){
                List<String> quizTypeList = Arrays.asList(entry.getValue().split(","));
                if(!quizTypeList.contains(questionMetadata.getQuizType())){
                    return;
                }
            }else if(entry.getKey().equals(QuestionSearchCriteria.questionId.toString())){
                if(!questionMetadata.getGuid().equals(entry.getValue())){
                    return;
                }
            }else{
                ExtMetadata qMetadata=questionMetadata.getExtendedMetadataByName(entry.getKey());
                if(qMetadata==null){
                    return;
                }else{
                	String metadataValue=qMetadata.getValue();
                	if(entry.getKey().equals(QuestionSearchCriteria.questionLevelOfDifficulty.toString())){
                		List<String> difficultyLevel = Arrays.asList(entry.getValue().split(","));
                		if(!difficultyLevel.contains(metadataValue)){
                			return;	
                		}
                	}else if(!StringUtils.containsIgnoreCase(metadataValue, entry.getValue())){
                            return;	
                    }    
                }
                
            }
        }
        filteredQuestions.add(questionMetadata);
    }
}

