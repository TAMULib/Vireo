package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import org.tdl.vireo.model.ControlledVocabulary;
import org.tdl.vireo.model.ControlledVocabularyCache;
import org.tdl.vireo.model.VocabularyWord;

import edu.tamu.weaver.response.ApiStatus;

@ActiveProfiles("test")
public class ControlledVocabularyControllerTest extends AbstractControllerTest {

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
	    Mockito.doAnswer((Answer<Void>) invocation -> {
	        Object finalControlledVocabularyListSize = mockControlledVocabularyList.size()-1;
	        assertEquals(" The controlled vocabulary was not removed from the list", 1, finalControlledVocabularyListSize);
	        return null;
	    }).when(controlledVocabularyRepo).remove(any(ControlledVocabulary.class));
	    controlledVocabularyRepo.remove(TEST_CONTROLLED_VOCABULARY_2);
		response = controlledVocabularyController.removeControlledVocabulary(TEST_CONTROLLED_VOCABULARY_1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testReorderControlledVocabulary() throws Exception {
		response = controlledVocabularyController.reorderControlledVocabulary(TEST_CONTROLLED_VOCABULARY_2.getId(), TEST_CONTROLLED_VOCABULARY_1.getId());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		//TODO mock controlledVocabularyRepo.reorder(src, dest)
	}

	@Test
	public void testSortControlledVocabulary() {
		response = controlledVocabularyController.sortControlledVocabulary("name");
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
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
	public void testImportControlledVocabularyStatus() throws Exception {
		response = controlledVocabularyController.importControlledVocabularyStatus(TEST_CONTROLLED_VOCABULARY_1.getName());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Boolean doesControlledVocabularyExist = (Boolean) response.getPayload().get("Boolean");
		assertTrue("The import status controlled vocabulary is not true ", doesControlledVocabularyExist);
	}

	@Test
	public void testCancelImportControlledVocabulary() {
		//TODO mock controlledVocabularyCachingService.removeControlledVocabularyCache(name)
		response = controlledVocabularyController.cancelImportControlledVocabulary(TEST_CONTROLLED_VOCABULARY_1.getName());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testCompareControlledVocabulary() throws Exception {
		//TODO mock byte []
		byte[] randomByteVar = TEST_CONTROLLED_VOCABULARY_1.getName().getBytes();
		MultipartFile file  = new MockMultipartFile("Test name for MockMultiPart file", randomByteVar);
		response = controlledVocabularyController.compareControlledVocabulary(TEST_CONTROLLED_VOCABULARY_1.getName(), file);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		@SuppressWarnings("unchecked")
		Map<String, Object> wordsMapResponse = (Map<String, Object>) response.getPayload().get("HashMap");
		assertEquals(" The  response wordMap does not contain the correct new key ", true, wordsMapResponse.containsKey("new"));
		assertEquals(" The  response wordMap does not contain the correct repeats key ", true, wordsMapResponse.containsKey("repeats"));
		assertEquals(" The  response wordMap does not contain the correct new key ", true, wordsMapResponse.containsKey("updating"));
	}

	@Test
	public void testImportControlledVocabulary() throws Exception {
		VocabularyWord[] updatingVocabularyWord = (VocabularyWord[]) mockVocabularyWordList.toArray();
		updatingVocabularyWord[1].setName(TEST_VOCABULARY_WORD1.getName());
		updatingVocabularyWord[1].setControlledVocabulary(TEST_CONTROLLED_VOCABULARY_1);
		updatingVocabularyWord[1].setDefinition(TEST_VOCABULARY_WORD1.getDefinition());
		updatingVocabularyWord[1].setIdentifier(TEST_VOCABULARY_WORD1.getIdentifier());
		updatingVocabularyWord[1].setContacts(TEST_VOCABULARY_WORD1.getContacts());

		List<VocabularyWord[]> updatingVocabularyWords = new ArrayList<VocabularyWord[]>();
		updatingVocabularyWords.add(updatingVocabularyWord);

		ControlledVocabularyCache cvCache = new ControlledVocabularyCache();
		cvCache.setUpdatingVocabularyWords(updatingVocabularyWords);

		response = controlledVocabularyController.importControlledVocabulary(TEST_CONTROLLED_VOCABULARY_1.getName());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		ControlledVocabulary savedControlledVocabulary = (ControlledVocabulary) response.getPayload().get("ControlledVocabulary");
		assertEquals("The controlled vocabulary is not saved", TEST_CONTROLLED_VOCABULARY_1, savedControlledVocabulary);
	}

	@Test
	public void testAddVocabularyWord() {
		TEST_CONTROLLED_VOCABULARY_1 = new ControlledVocabulary("A Controlled Vocabulary Name", TEST_LANGUAGE1, false);
		TEST_CONTROLLED_VOCABULARY_1.setId(1l);
		TEST_VOCABULARY_WORD1 = new VocabularyWord("Vocabulary Word Name1",	"Vocabulary Word Definition1", "Vocabulary Word Identifier1", mockContactList);
		response = controlledVocabularyController.addVocabularyWord(1l, TEST_VOCABULARY_WORD1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		VocabularyWord vocabularyWord = (VocabularyWord)response.getPayload().get("VocabularyWord");
		assertEquals("The vocabulary word was not added", TEST_VOCABULARY_WORD1.getName(), vocabularyWord.getName());
		assertEquals("The vocabulary word with wrong definition was added", TEST_VOCABULARY_WORD1.getDefinition(), vocabularyWord.getDefinition());
	}

	@Test
	public void testRemoveVocabularyWord() throws Exception {
		//TODO
		//response = controlledVocabularyController.removeVocabularyWord(TEST_CONTROLLED_VOCABULARY_1.getId(), TEST_VOCABULARY_WORD1.getId());
		//assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		//ControlledVocabulary responseControlledVocabulary = (ControlledVocabulary) response.getPayload().get("ControlledVocabulary");
	}

	@Test
	public void testUpdateVocabularyWord() throws IOException, Exception {
		/*response = controlledVocabularyController.updateVocabularyWord(TEST_CONTROLLED_VOCABULARY_1.getId(), TEST_VOCABULARY_WORD1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		VocabularyWord updatedVocabularyWord = (VocabularyWord) response.getPayload().get("VocabularyWord");
		assertEquals("The controlled vocabulary is not updated", TEST_VOCABULARY_WORD1, updatedVocabularyWord);*/
	}

	@After
	public void cleanUp() {
		controlledVocabularyRepo.deleteAll();
	}
}
