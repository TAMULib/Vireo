package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.ActiveProfiles;
import org.tdl.vireo.model.ControlledVocabulary;
import org.tdl.vireo.model.ControlledVocabularyCache;
import org.tdl.vireo.model.Language;
import org.tdl.vireo.model.VocabularyWord;
import org.tdl.vireo.model.repo.ControlledVocabularyRepo;
import org.tdl.vireo.model.repo.VocabularyWordRepo;
import org.tdl.vireo.service.ControlledVocabularyCachingService;
import org.tdl.vireo.service.EntityControlledVocabularyService;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.tamu.weaver.response.ApiStatus;

@ActiveProfiles("test")
public class ControlledVocabularyControllerTest extends AbstractControllerTest{

	@Spy
	protected  ControlledVocabularyCachingService controlledVocabularyCachingService;

	@InjectMocks
	protected EntityControlledVocabularyService entityControlledVocabularyService;
	@Mock
	protected  ControlledVocabularyRepo controlledVocabularyRepo;

	@Mock
	protected  VocabularyWordRepo vocabularyWordRepo;
	
	protected static List<String> mockVocabularyWordContactList = Arrays.asList(new String[] {"VocabWordContact1", "VocabWordContact2"});
	protected static VocabularyWord TEST_VOCABULARY_WORD1 = new VocabularyWord("Vocabulary Word Name1", "Vocabulary Word Definition1", "Vocabulary Word Identifier1", mockVocabularyWordContactList);
	protected static VocabularyWord TEST_VOCABULARY_WORD2 = new VocabularyWord("Vocabulary Word Name2", "Vocabulary Word Definition2", "Vocabulary Word Identifier2", mockVocabularyWordContactList);
	protected static List<VocabularyWord> mockVocabularyWordList = Arrays.asList(new VocabularyWord[] {TEST_VOCABULARY_WORD1, TEST_VOCABULARY_WORD2});
	protected static ControlledVocabulary TEST_CONTROLLED_VOCABULARY_1 = new ControlledVocabulary("Controlled Vocabulary Name1", new Language("Language name for Controlled Vocabulary1"),false);
	protected static ControlledVocabulary TEST_CONTROLLED_VOCABULARY_2 = new ControlledVocabulary("Controlled Vocabulary Name2", new Language("Language name for Controlled Vocabulary2"), false);

	static {
		TEST_CONTROLLED_VOCABULARY_1.setId(1l);
		TEST_CONTROLLED_VOCABULARY_1.setDictionary(mockVocabularyWordList);
		TEST_CONTROLLED_VOCABULARY_2.setId(2l);
		TEST_CONTROLLED_VOCABULARY_2.setDictionary(mockVocabularyWordList);
	}
	
	protected static ControlledVocabularyCache TEST_CONTROLLED_VOCABULARY_CACHE = new ControlledVocabularyCache(1l, TEST_CONTROLLED_VOCABULARY_1.getName());
	protected static List<ControlledVocabulary> mockControlledVocabularyList = 
			new ArrayList<ControlledVocabulary>(Arrays.asList(new ControlledVocabulary[] { TEST_CONTROLLED_VOCABULARY_1, TEST_CONTROLLED_VOCABULARY_2 }));

	public ControlledVocabulary findControlledVocabularyByName(String controlledVocabularyName) {
		for(ControlledVocabulary cvObj : mockControlledVocabularyList) {
			if(cvObj.getName().equals(controlledVocabularyName)) {
				return cvObj;
			}
		}
		return null;
	}

	public ControlledVocabulary updateControlledVocabulary(ControlledVocabulary modifiedControlledVocabulary) throws IOException {
		for(ControlledVocabulary cvObj : mockControlledVocabularyList) {
			if(cvObj.getName().equals(modifiedControlledVocabulary.getName())) {
				return modifiedControlledVocabulary;
			}
		}
		return null;
	}

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);		
		when(controlledVocabularyRepo.findAllByOrderByPositionAsc()).thenReturn(mockControlledVocabularyList);

		when(controlledVocabularyRepo.create(any(String.class), any(Language.class))).then(new Answer<ControlledVocabulary>() {
			@Override
			public ControlledVocabulary answer(InvocationOnMock invocation) throws Throwable {
				return TEST_CONTROLLED_VOCABULARY_1;
			}
		});

		when( controlledVocabularyRepo.update(any(ControlledVocabulary.class))).then(new Answer<ControlledVocabulary>() {
			@Override
			public ControlledVocabulary answer(InvocationOnMock invocation) throws Throwable {
				return updateControlledVocabulary((ControlledVocabulary) invocation.getArguments()[0]);
			}
		});

		when(controlledVocabularyRepo.findByName(any(String.class))).then(new Answer<ControlledVocabulary>() {
			@Override
			public ControlledVocabulary answer(InvocationOnMock invocation) throws Throwable {
				return findControlledVocabularyByName((String)invocation.getArguments()[0]);
			}
		});
	
		when(controlledVocabularyCachingService.doesControlledVocabularyExist(any(String.class))).then(new Answer<Boolean>() {

			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				return true;
			}
		});
		when(controlledVocabularyCachingService.getControlledVocabularyCache(any(String.class))).then(new Answer<ControlledVocabularyCache>() {
			@Override
			public ControlledVocabularyCache answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				return TEST_CONTROLLED_VOCABULARY_CACHE;
			}
			
		});
	}
	
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
	public void testReorderControlledVocabulary() throws JsonProcessingException {
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
		assertTrue("The contact list is inccorrect for the first controlled vocabulary", cvMapRowList.get(0).get(3).equals("VocabWordContact1,VocabWordContact2"));
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
	
	@Test
	public void testCompareControlledVocabulary() {
        /*MultipartFile file  = new MultipartFile() {
			
			@Override
			public void transferTo(File arg0) throws IOException, IllegalStateException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isEmpty() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public long getSize() {
				// TODO Auto-generated method stub
				return TEST_CONTROLLED_VOCABULARY_1.getPosition();
			}
			
			@Override
			public String getOriginalFilename() {
				// TODO Auto-generated method stub
				return TEST_CONTROLLED_VOCABULARY_1.getName();
			}
			
			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return TEST_CONTROLLED_VOCABULARY_1.getName();
			}
			
			@Override
			public InputStream getInputStream() throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getContentType() {
				// TODO Auto-generated method stub
				return TEST_CONTROLLED_VOCABULARY_1.getLanguage().getControlledDefinition();
			}
			
			@Override
			public byte[] getBytes() throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
		};
		Map<String, Object> wordsMap = cacheImport(TEST_CONTROLLED_VOCABULARY_1, file);
		Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader("name", "definition", "identifier", "contacts").parse(new InputStreamReader(file.getInputStream()));
		response = controlledVocabularyController.compareControlledVocabulary(TEST_CONTROLLED_VOCABULARY_1.getName(), file);
		System.out.println("\n\n\n response ="+ objectMapper.writeValueAsString(response)); */
	}

	@Test
	public void testImportControlledVocabulary() {
		response = controlledVocabularyController.importControlledVocabulary(TEST_CONTROLLED_VOCABULARY_1.getName());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		ControlledVocabulary savedControlledVocabulary = (ControlledVocabulary) response.getPayload().get("ControlledVocabulary");
		assertEquals("The controlled vocabulary is not saved", TEST_CONTROLLED_VOCABULARY_1, savedControlledVocabulary);
	}
	
	
	@After
	public void cleanUp() {
		response = null;
		controlledVocabularyRepo.deleteAll();
	}

}
