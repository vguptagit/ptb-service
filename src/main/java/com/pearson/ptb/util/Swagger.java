package com.pearson.ptb.util;
/**
 * This <code>Swagger</code> is utility for swagger
 * 
 */
public class Swagger {

    /**
     * Swagger constant for difficulty
     */
    public static final String DIFFICULTY = "This indicates the question/item difficulty metadata to be sent for the filter data.";

    /**
     * Swagger constant for topic
     */
    public static final String TOPIC = "This indicates the question/item topic metadata to be sent for the filter data.";

    /**
     * Swagger constant for keywords
     */
    public static final String KEYWORDS = "This indicates the question/item keywords metadata to be sent for the filter data.";

    /**
     * Swagger constant for multimediaLink
     */
    public static final String MULTIMEDIA_LINK = "This indicates the question/item multimedia link metadata to be sent for the filter data.";

    /**
     * Swagger constant for nationalStandard
     */
    public static final String NATIONAL_STANDARD = "This indicates the question/item national standard metadata to be sent for the filter data.";

    /**
     * Swagger constant for notes
     */
    public static final String NOTES = "This indicates the question/item notes metadata to be sent for the filter data.";

    /**
     * Swagger constant for pageReference
     */
    public static final String PAGE_REFERENCE = "This indicates the question/item page reference metadata to be sent for the filter data.";

    /**
     * Swagger constant for skill
     */
    public static final String SKILL = "This indicates the question/item skill metadata to be sent for the filter data.";

    /**
     * Swagger constant for localStandard
     */
    public static final String LOCAL_STANDARD = "This indicates the question/item local standard metadata to be sent for the filter data.";

    /**
     * Swagger constant for miscellaneous
     */
    public static final String MISCELLANEOUS = "This indicates the question/item miscellaneous metadata to be sent for the filter data.";

    /**
     * Swagger constant for includeInnerContainer
     */
    public static final String INCLUDE_INNER_CONTAINER = "This indicates whether to include question/item inside the inner containers";

    /**
     * Swagger constant for getallcontainers
     */
    public static final String GET_ALL_CONTAINERS = "This indicates whether to get to the containers at all levels (nth level)";
    
    /**
     * Swagger constant for getallQuestionFolders
     */
    public static final String GET_ALL_QUESTIONFOLDERS = "This indicates whether to get question folders at all levels (nth level)";

    /**
     * Swagger constant for includeSelf
     */
    public static final String INCLUDE_SELF = "This indicates whether to include self for processing";

    /**
     * Swagger constant for questionId
     */
    public static final String QUESTION_ID = "This indicates the question/item questionId metadata to be sent for the filter data.";

    /**
     * Swagger constant for book search string
     */
    public static final String BOOK_SEARCH = "This indicates the book search text, passed to search books and the text will be search in Book - Id,Title,Author,Publisher,Isbn,Disipline.";

    /**
     * Swagger constant for book guid
     */
    public static final String BOOK_GUID = "This indicates the book guid, passed to search books.";

    /**
     * Swagger constant for book title
     */
    public static final String BOOK_TITLE = "This indicates the book title, passed to search books.";

    /**
     * Swagger constant for book author
     */
    public static final String BOOK_AUTHOR = "This indicates the book author, passed to search books.";

    /**
     * Swagger constant for book publisher
     */
    public static final String BOOK_PUBLISHER = "This indicates the book publisher, passed to search books.";

    /**
     * Swagger constant for book ISBN
     */
    public static final String BOOK_ISBN = "This indicates the book ISBN number, passed to search books.";

    /**
     * Swagger constant for book discipline
     */
    public static final String BOOK_DISCIPLINE = "This indicates the book discipline, passed to search books.";

    /**
     * Swagger constant to describe get tests end point note
     */
    public static final String GET_ALL_TESTS_NOTE = "Returns the list of all tests for the given book identified through the {bookid}";

    /**
     * Swagger constant to describe get tests end point
     */
    public static final String GET_ALL_TESTS_VALUE = "This API will help user to fetch all tests details of a specific book";

    /**
     * Swagger constant to describe get test end point note
     */
    public static final String GET_TESTS_NOTE = "Return the detail of specific test identified by {id} for the given book identified through {bookid}";

    /**
     * Swagger constant to describe get test end point
     */
    public static final String GET_TESTS_VALUE = "This API will help user to fetch the details of a specific my test";

    /**
     * Swagger constant to describe create test end point
     */
    public static final String SAVE_TEST_VALUE = "Create/update the test";

    /**
     * Swagger constant to describe create test end point note
     */
    public static final String SAVE_TEST_NOTE = "Create/update the test with the given data for the given book identified through {bookid}";

    /**
     * Swagger constant to describe create test end point
     */
    public static final String UPDATE_TEST_METADATA_VALUE = "Update the test metadata";

    /**
     * Swagger constant to describe create test end point note
     */
    public static final String UPDATE_TEST_METADATA_NOTE = "Update the test metadata of a given folder";

    /**
     * Swagger constant to describe get tests end point note
     */
    public static final String GET_MYFOLDER_TESTS_VALUE = "Returns the list of all created tests for the given folder identified through the {folderid}";

    /**
     * Swagger constant to describe get tests end point
     */
    public static final String GET_MYFOLDER_TESTS_NOTE = "This API will help user to fetch all created tests details (of a specific folder)";

    /**
     * Swagger constant to describe get tests end point note
     */
    public static final String GET_ROOT_TESTS_VALUE = "Returns the list of all created tests at the root level";

    /**
     * Swagger constant to describe get tests end point
     */
    public static final String GET_ROOT_TESTS_NOTE = "This API will help user to fetch all root level created tests details (without folder)";

    /**
     * Swagger constant to describe get question by id end point
     */
    public static final String GET_QUESTION_BY_ID_VALUE = "This API will help user to fetch test for geven test id";

    /**
     * Swagger constant to describe get question by id end point note
     */
    public static final String GET_QUESTION_BY_ID_NOTE = "Return the detail of specific question identified by {id} in the xml format";

    /**
     * Swagger constant to describe get questions end point
     */
    public static final String GET_USERQUESTIONS_VALUE = "This API will help user to fetch questions created by the user";

    /**
     * Swagger constant to describe get questions end point note
     */
    public static final String GET_USERQUESTIONS_NOTE = "Return the questions";

    /**
     * Swagger constant to describe get question by container id end point note
     */
    public static final String GET_QUESTIONS_BY_CONTAINER_NOTE = "Returns list of all questions created for the book identified by {bookid} under the specific Container identified by {nodeId}";

    /**
     * Swagger constant to describe save question by container id end point note
     */
    public static final String SAVE_QUESTION = "Save question with input as JSON data for book identified by {bookid} under the specific Container identified by {nodeId}";

    /**
     * Swagger constant to describe save question by container id end point note
     */
    public static final String SAVE_QUESTIONS = "Save questions with input as JSON data for User Id from request";

    /**
     * Swagger constant to describe get root folders API method
     */
    public static final String GET_ROOT_FOLDERS_VALUE = "This API will help user to get a list of all root folders";

    /**
     * Swagger constant to describe get root folders API method note
     */
    public static final String GET_ROOT_FOLDERS_NOTE = "Returns the list of all root folders of a user";

    /**
     * Swagger constant to describe get root folders API method
     */
    public static final String GET_USERQUESTION_FOLDERS_VALUE = "This API will help user to get a list of all root level question folders";

    /**
     * Swagger constant to describe get root folders API method note
     */
    public static final String GET_USERQUESTION_FOLDERS_NOTE = "Returns the list of all root level question folders of a user";

    /**
     * Swagger constant to describe get child folders API method
     */
    public static final String GET_CHILDQUESTION_FOLDERS_VALUE = "This API will help user to get a list of all child level question folders";

    /**
     * Swagger constant to describe get child folders API method note
     */
    public static final String GET_CHILDQUESTION_FOLDERS_NOTE = "Returns the list of all child level question folders of a user";

    /**
     * Swagger constant to describe get root folders API method
     */
    public static final String GET_QUESTIONFOLDERS_ROOT_VALUE = "This API will help user to get root level question folder";

    /**
     * Swagger constant to describe get root folders API method note
     */
    public static final String GET_QUESTIONFOLDERS_ROOT_NOTE = "Returns root level question folder of a user";

    /**
     * Swagger constant to describe get root folders API method
     */
    public static final String GET_TEST_ROOT_VALUE = "This API will help user to get folder used for storing root level tests";

    /**
     * Swagger constant to describe get root folders API method note
     */
    public static final String GET_TEST_ROOT_NOTE = "Returns test root folder of a user";

    /**
     * Swagger constant to describe get folders API method
     */
    public static final String GET_FOLDERS_VALUE = "This API will help user to get a list of children folders of a give folder";

    /**
     * Swagger constant to describe get folders API method note
     */
    public static final String GET_FOLDERS_NOTE = "Returns the list of all children folders";

    /**
     * Swagger constant to describe get root folders API method
     */
    public static final String GET_ARCHIVEROOT_FOLDERS_VALUE = "This API will help user to get a list of all archive root folders";

    /**
     * Swagger constant to describe get root folders API method note
     */
    public static final String GET_ARCHIVEROOT_FOLDERS_NOTE = "Returns the list of all archive root folders of a user";

    /**
     * Swagger constant to describe get folders API method
     */
    public static final String GET_ARCHIVEFOLDERS_VALUE = "This API will help user to get a list of children folders of a given archive folder";

    /**
     * Swagger constant to describe get folders API method note
     */
    public static final String GET_ARCHIVEFOLDERS_NOTE = "Returns the list of all archive children folders";

    /**
     * Swagger constant to describe archive folder API method
     */
    public static final String ARCHIVEFOLDER_VALUE = "This API will help to archive folder";

    /**
     * Swagger constant to describe archive folder API method note
     */
    public static final String ARCHIVEFOLDER_NOTE = "Archive folder";

    /**
     * Swagger constant to describe restore folder API method
     */
    public static final String RESTOREFOLDER_VALUE = "This API will help to restore folder";

    /**
     * Swagger constant to describe restore folder API method note
     */
    public static final String RESTOREFOLDER_NOTE = "Restore folder";

    /**
     * Swagger constant to describe delete folder API method
     */
    public static final String DELETEFOLDER_VALUE = "This API will help to delete folder";

    /**
     * Swagger constant to describe delete folder API method note
     */
    public static final String DELETEFOLDER_NOTE = "Delete folder";

    /**
     * Swagger constant to describe delete test API method
     */
    public static final String DELETETEST_VALUE = "This API will help to delete test";

    /**
     * Swagger constant to describe delete test API method note
     */
    public static final String DELETETEST_NOTE = "Delete test";

    /**
     * Swagger constant to describe delete test API method
     */
    public static final String DELETEROOTTEST_VALUE = "This API will help to delete test at root level";

    /**
     * Swagger constant to describe delete test API method note
     */
    public static final String DELETEROOTTEST_NOTE = "Delete root test";

    /**
     * Swagger constant to describe get tests end point note
     */
    public static final String GET_ARCHIVEFOLDER_TESTS_VALUE = "Returns the list of all created tests for the given archive folder identified through the {folderid}";

    /**
     * Swagger constant to describe get tests end point
     */
    public static final String GET_ARCHIVEFOLDER_TESTS_NOTE = "This API will help user to fetch all created tests details of a specific archive folder";

    /**
     * Swagger constant to describe save folders API method
     */
    public static final String SAVE_FOLDERS_VALUE = "This API will help user to create a instructor custom folder";

    /**
     * Swagger constant to describe save folders API method note
     */
    public static final String SAVE_FOLDERS_NOTE = "Returns the newly created folder";

    /**
     * Swagger constant to describe update folder API method
     */
    public static final String UPDATE_FOLDERS_VALUE = "This API will help user to update a instructor custom folder";

    /**
     * Swagger constant to describe update folders API method note
     */
    public static final String UPDATE_FOLDERS_NOTE = "Returns the updated folder";

    /**
     * Swagger constant to describe get folders API method
     */
    public static final String SAVE_USERQUESTION_FOLDERS_VALUE = "This API will help user to create a instructor question folder";

    /**
     * Swagger constant to describe get folders API method note
     */
    public static final String SAVE_USERQUESTION_FOLDERS_NOTE = "Returns the newly created folder";

    /**
     * Swagger constant to describe update folders API method
     */
    public static final String UPDATE_USERQUESTION_FOLDERS_VALUE = "This API will help user to update a instructor question folder";

    /**
     * Swagger constant to describe update folders API method note
     */
    public static final String UPDATE_USERQUESTION_FOLDERS_NOTE = "Returns the updated folder";

    /**
     * Swagger constant to describe create test end point
     */
    public static final String CREATE_TEST_VERSION = "Creates the versin of activity";

    /**
     * Swagger constant to describe create test end point note
     */
    public static final String CREATE_TEST_VERSION_NOTE = "Creating the tests by given test by applying question and answer key scrambling inside the give folder";

    /**
     * Swagger constant to describe get test meta data end point note
     */
    public static final String GET_TESTS_METADATA_VALUE = "Returns the metadata of the test";

    /**
     * Swagger constant to describe get test meta data end point
     */
    public static final String GET_TESTS_METADATA_NOTE = "This API will help user to fetch meta data of the test";

    /**
     * Swagger constant to describe get test end point
     */
    public static final String GET_TESTQUESTIONS_VALUE = "This API will help user to fetch the questions details of a specific my test";

    /**
     * Swagger constant to describe get test meta data end point
     */
    public static final String GET_TESTQUESTIONS_NOTE = "Returns the questions for geven test id";

    /**
     * Making the Constructor private.
     */
    private Swagger() {

    }

}
