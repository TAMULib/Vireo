package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.ActiveProfiles;
import org.tdl.vireo.model.Degree;
import org.tdl.vireo.model.DegreeLevel;

import edu.tamu.weaver.response.ApiStatus;

@SuppressWarnings("unchecked")
@ActiveProfiles("test")
public class DegreeControllerTest extends AbstractControllerTest {

	protected static DegreeLevel TEST_DEGREE_LEVEL = new DegreeLevel("Degree Level name");
	protected static Degree TEST_DEGREE1 = new Degree("A Degree Name", TEST_DEGREE_LEVEL , "ProquestCode for Degree1 ");
	protected static Degree TEST_DEGREE2 = new Degree("Second Degree Name", TEST_DEGREE_LEVEL , "ProquestCode for Second Degree ");
	static {
		TEST_DEGREE1.setId(1l);
		TEST_DEGREE1.setPosition(1l);
		TEST_DEGREE2.setId(2l);
		TEST_DEGREE2.setPosition(2l);
	}
	protected static List<Degree> mockDegreeList = new ArrayList<>(Arrays.asList(new Degree[] {TEST_DEGREE1 , TEST_DEGREE2}));

	public Degree createDegree(String name, DegreeLevel level) {
		return new Degree(name, level);
	}

	public Degree updateDegree(Degree modifiedDegree) {
		Degree updatedDegree = null;
		for(Degree degree: mockDegreeList) {
			if(modifiedDegree.getId().equals(degree.getId())) {
				degree.setLevel(modifiedDegree.getLevel());
				degree.setName(modifiedDegree.getName());
				degree.setDegreeCode(modifiedDegree.getDegreeCode());
				updatedDegree = degree;
				break;
			}
		}
		return updatedDegree;
	}

	public Map<String, String> getProquestDegreeCodes(String degreeCode) {
		Map<String,String> degrees = new HashMap<String,String>();
		degrees.put("name", "This is a name");
		degrees.put("level", "This is a degree level");
		degrees.put("code", "This is a degree code");
		return degrees;
	}

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);		
		when(degreeRepo.findAllByOrderByPositionAsc()).thenReturn(mockDegreeList);
		
		when(degreeRepo.create( any(String.class), any(DegreeLevel.class) )).then(new Answer<Degree>() {
			@Override
			public Degree answer(InvocationOnMock invocation) throws Throwable {
				return createDegree( (String)invocation.getArguments()[0], (DegreeLevel) invocation.getArguments()[1]);
			}
		});
		
		when(degreeRepo.update( any(Degree.class) )).then(new Answer<Degree>() {
			@Override
			public Degree answer(InvocationOnMock invocation) throws Throwable {
				return updateDegree((Degree) invocation.getArguments()[0] );
			}
		});
		when(proquestCodesService.getCodes(any(String.class) )).then(new Answer<Map<String,String>>() {
			@Override
			public Map<String, String> answer(InvocationOnMock invocation) throws Throwable {
				return getProquestDegreeCodes( (String) invocation.getArguments()[0] );
			}
		});
	}

	@Test
	public void testAllDegrees() {
		response = degreeController.allDegrees();
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		List<Degree> degreeList = (List<Degree>) response.getPayload().get("ArrayList<Degree>");
		assertEquals("There are no degrees in the list ", mockDegreeList.size() , degreeList.size());
	}

	@Test
	public void testCreateDegree() {
		response = degreeController.createDegree(TEST_DEGREE1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Degree createdDegree = (Degree) response.getPayload().get("Degree");
		assertEquals("The created degree does not have correct name ", TEST_DEGREE1.getName() , createdDegree.getName());
		assertEquals(" The created degree does not have correct degree level", TEST_DEGREE1.getLevel().getName(), createdDegree.getLevel().getName());
	}

	@Test
	public void testUpdateDegree() {
		TEST_DEGREE1.setName("Modified the Degree Name");
		TEST_DEGREE1.setDegreeCode("Modified Degree Code");
		response = degreeController.updateDegree(TEST_DEGREE1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Degree updatedDegree = (Degree) response.getPayload().get("Degree");
		assertEquals("The updated degree does not have updated name ", TEST_DEGREE1.getName() , updatedDegree.getName());
		assertEquals("The updated degree does not have updated degree code ", TEST_DEGREE1.getDegreeCode() , updatedDegree.getDegreeCode());
	}
   // TODO - mock remove()
	@Test
	public void testRemoveDegree() {
		response = degreeController.removeDegree(TEST_DEGREE2);
	}
	// TODO - mock reorderDegrees()
	@Test
	public void testReorderDegree() {
		response = degreeController.reorderDegrees(TEST_DEGREE2.getId(), TEST_DEGREE1.getId());
	}
	// TODO - mock sortDegrees()
	@Test
	public void testSortDegrees() {
		response = degreeController.sortDegrees("name");
	}

	@Test
	public void testGetProquestLanguageCodes() throws Exception{
		response = degreeController.getProquestLanguageCodes();
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Map<String, String> degreeCodes = (Map<String, String>) response.getPayload().get("HashMap");
		assertEquals("The code in the degreeCodeMap is incorrect ", degreeCodes.get("code"), getProquestDegreeCodes("degrees").get("code"));
	}

	@After
	public void cleanUp() {
		degreeRepo.deleteAll();
	}
}
