package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.tdl.vireo.model.Degree;

import edu.tamu.weaver.response.ApiStatus;

@SuppressWarnings("unchecked")
@ActiveProfiles("test")
public class DegreeControllerTest extends AbstractControllerTest {

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
}
