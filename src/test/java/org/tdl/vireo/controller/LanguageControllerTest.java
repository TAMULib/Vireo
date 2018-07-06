package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.tdl.vireo.model.Language;

import edu.tamu.weaver.response.ApiStatus;

public class LanguageControllerTest extends AbstractControllerTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testGetAllLanguages() {
		response = languageController.getAllLanguages();
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		List<Language> languageList = (List<Language>) response.getPayload().get("ArrayList<Language>");
		assertEquals("There are no languages in the list ", mockLanguageList.size() , languageList.size());
	}

	@Test
	public void testCreateLanguage() {
		response = languageController.createLanguage(TEST_LANGUAGE1);
		Language createdLanguage = (Language) response.getPayload().get("Language");
		assertEquals("The created Language does not have correct name ", TEST_LANGUAGE1.getName() , createdLanguage.getName());
	}

	@Test
	public void testUpdateLanguage() {
		TEST_LANGUAGE1.setName("Modified the language name");
		response = languageController.updateLanguage(TEST_LANGUAGE1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Language updatedLanguage = (Language) response.getPayload().get("Language");
		assertEquals("The updatedLanguage does not have correct name ", TEST_LANGUAGE1.getName() , updatedLanguage.getName());
	}

	@Test
	public void testRemoveLanguage() {
		//TODO
		response = languageController.removeLanguage(TEST_LANGUAGE1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testReorderLanguage() {
		//TODO
		response = languageController.reorderLanguage(1l, 2l);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testSortLanguage() {
		//TODO
		response = languageController.sortLanguage("name");
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testGetProquestLanguageCodes() throws Exception {
		response = languageController.getProquestLanguageCodes();
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		@SuppressWarnings("unchecked")
		Map<String, String> proquestCodeMap = (Map<String, String>) response.getPayload().get("HashMap");
		assertEquals(" The language code is incorrect ", proquestCodeMap.get("languages"), getProquestLanguageCodes().get("languages"));
	}
}
