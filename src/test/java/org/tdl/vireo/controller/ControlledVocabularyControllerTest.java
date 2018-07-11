package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.tdl.vireo.model.ControlledVocabulary;
import org.tdl.vireo.model.VocabularyWord;

import edu.tamu.weaver.response.ApiStatus;

@ActiveProfiles("test")
public class ControlledVocabularyControllerTest extends AbstractControllerTest{

	@SuppressWarnings("unchecked")
	@Test
	public void testGetAllControlledVocabulary() {
		response = controlledVocabularyController.getAllControlledVocabulary();
		List<ControlledVocabulary> controlledVocabularyList = (List<ControlledVocabulary>) response.getPayload().get("ArrayList<ControlledVocabulary>");
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		assertEquals("The first controlled vocabulary name in the list is incorrrect", TEST_CONTROLLED_VOCABULARY_1.getName(),controlledVocabularyList.get(0).getName() );
		assertEquals("The first controlled vocabulary language name in the list is incorrrect", TEST_CONTROLLED_VOCABULARY_1.getLanguage().getName(),controlledVocabularyList.get(0).getLanguage().getName() );		
	}

	@Test
	public void testGetControlledVocabularyByName() {
		response = controlledVocabularyController.getControlledVocabularyByName(TEST_CONTROLLED_VOCABULARY_1.getName());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		ControlledVocabulary controlledVocabulary = (ControlledVocabulary) response.getPayload().get("ControlledVocabulary");
		assertEquals("The controlled vocabulary name is incorrect", TEST_CONTROLLED_VOCABULARY_1.getName(), controlledVocabulary.getName());
	}

	@Test
	public void testCreateControlledVocabulary() {
		response = controlledVocabularyController.createControlledVocabulary(TEST_CONTROLLED_VOCABULARY_1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		ControlledVocabulary controlledVocabulary = (ControlledVocabulary) response.getPayload().get("ControlledVocabulary");
		assertEquals("The created controlled vocabulary name is incorrect", TEST_CONTROLLED_VOCABULARY_1.getName(), controlledVocabulary.getName());
		assertEquals("The created controlled vocabulary language name is incorrect", TEST_CONTROLLED_VOCABULARY_1.getLanguage().getName(), controlledVocabulary.getLanguage().getName());
	}

	@Test
	public void testUpdateControlledVocabulary() {
		response = controlledVocabularyController.updateControlledVocabulary(TEST_CONTROLLED_VOCABULARY_1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		ControlledVocabulary controlledVocabulary = (ControlledVocabulary) response.getPayload().get("ControlledVocabulary");
		assertEquals(" The updated controlled vocabulary name is incorrect", TEST_CONTROLLED_VOCABULARY_1.getName(), controlledVocabulary.getName());
	}

	@Test
	public void testRemoveControlledVocabulary() {
		//TODO mock controlledVocabularyRepo.remove(controlledVocabulary);
		System.out.println("\n\n\nsize = "+ mockControlledVocabularyList.size());
	}

	@Test
	public void testReorderControlledVocabulary() {
		response = controlledVocabularyController.reorderControlledVocabulary(2l, 1l);
		//TODO mock controlledVocabularyRepo.reorder(src, dest)
	}

	@Test
	public void testSortControlledVocabulary() {
		//TODO controlledVocabularyRepo.sort(column);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testExportControlledVocabulary() {
		response = controlledVocabularyController.exportControlledVocabulary(TEST_CONTROLLED_VOCABULARY_1.getName());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		List<String> cvMapHeaderList = (List<String>) ((Map<String, Object>) response.getPayload().get("HashMap")).get("headers");
		assertEquals("The size of the cv Map headers is not correct", 4, cvMapHeaderList.size() );
		List<List<Object>> cvMapRowList = (List<List<Object>>) ((Map<String, Object>) response.getPayload().get("HashMap")).get("rows");
		assertEquals("The vocabulary word name is incorrect", TEST_CONTROLLED_VOCABULARY_1.getDictionary().get(0).getName() , cvMapRowList.get(0).get(0));
		assertTrue("The contact list is inccorrect for the first controlled vocabulary", cvMapRowList.get(0).get(3).equals("Contact1,Contact2"));
	}

	@Test
	public void testImportControlledVocabularyStatus() {
		response = controlledVocabularyController.importControlledVocabularyStatus(TEST_CONTROLLED_VOCABULARY_1.getName());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Boolean doesControlledVocabularyExist = (Boolean) response.getPayload().get("Boolean");
		assertTrue("The import status controlled vocabulary is not true ", doesControlledVocabularyExist);
	}

	@Test
	public void testCancelImportControlledVocabulary() {
		response = controlledVocabularyController.cancelImportControlledVocabulary(TEST_CONTROLLED_VOCABULARY_1.getName());
		//TODO mock controlledVocabularyCachingService.removeControlledVocabularyCache(name)
		//Map<String, ControlledVocabularyCache> cvCacheMap = new HashMap<String, ControlledVocabularyCache>();
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}
	//TODO
	@Test
	public void testCompareControlledVocabulary() {

	}

	@Test
	public void testImportControlledVocabulary() {
		response = controlledVocabularyController.importControlledVocabulary(TEST_CONTROLLED_VOCABULARY_1.getName());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		ControlledVocabulary savedControlledVocabulary = (ControlledVocabulary) response.getPayload().get("ControlledVocabulary");
		assertEquals("The controlled vocabulary is not saved", TEST_CONTROLLED_VOCABULARY_1, savedControlledVocabulary);
	}

	@Test
	public void testAddVocabularyWord() {
		response = controlledVocabularyController.addVocabularyWord(TEST_CONTROLLED_VOCABULARY_1.getId(), TEST_VOCABULARY_WORD1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		VocabularyWord vocabularyWord = (VocabularyWord)response.getPayload().get("VocabularyWord");
		assertEquals("The vocabukary word was not added", TEST_VOCABULARY_WORD1, vocabularyWord);
	}
	//TODO
	@Test
	public void testRemoveVocabularyWord() {
		//response = controlledVocabularyController.removeVocabularyWord(TEST_CONTROLLED_VOCABULARY_1.getId(), TEST_VOCABULARY_WORD1.getId());
	}

	@Test
	public void testUpdateVocabularyWord() throws IOException {
		response = controlledVocabularyController.updateVocabularyWord(TEST_CONTROLLED_VOCABULARY_1.getId(), TEST_VOCABULARY_WORD1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		VocabularyWord updatedVocabularyWord = (VocabularyWord) response.getPayload().get("VocabularyWord");
		assertEquals("The controlled vocabulary is not updated", TEST_VOCABULARY_WORD1, updatedVocabularyWord);
	}
}
