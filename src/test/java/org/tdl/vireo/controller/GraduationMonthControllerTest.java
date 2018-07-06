package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.tdl.vireo.model.GraduationMonth;

import edu.tamu.weaver.response.ApiStatus;

public class GraduationMonthControllerTest extends AbstractControllerTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testAllGraduationMonths() {
		response = graduationMonthController.allGraduationMonths();
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		List<GraduationMonth> graduationMonthList = (List<GraduationMonth>) response.getPayload().get("ArrayList<GraduationMonth>");
		assertEquals("There are no degrees in the list ", mockGraduationMonthList.size() , graduationMonthList.size());
	}

	@Test
	public void testCreateGraduationMonth() {
		response = graduationMonthController.createGraduationMonth(TEST_GRADUATION_MONTH1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}
	@Test
	public void testUpdateGraduateMonth() {
		response = graduationMonthController.updateGraduationMonth(TEST_GRADUATION_MONTH1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		GraduationMonth createdGraduationMonth = (GraduationMonth) response.getPayload().get("GraduationMonth");
		assertEquals("The created GraduationMonth does not have correct month ", TEST_GRADUATION_MONTH1.getMonth() , createdGraduationMonth.getMonth());
	}

	@Test
	public void testUpdateGraduationMonth() {
		TEST_GRADUATION_MONTH1.setMonth(7);
		response = graduationMonthController.updateGraduationMonth(TEST_GRADUATION_MONTH1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		GraduationMonth updatedGraduationMonth = (GraduationMonth) response.getPayload().get("GraduationMonth");
		assertEquals("The updated graduationMonth does not have correct month ", TEST_GRADUATION_MONTH1.getMonth() , updatedGraduationMonth.getMonth());
	}

	@Test
	public void testRemoveGraduationMonth() {
		//TODO
		response = graduationMonthController.removeGraduationMonth(TEST_GRADUATION_MONTH2);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testReorderGraduationMonth() {
		//TODO
		response = graduationMonthController.reorderGraduationMonths(1l, 2l);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testSortGraduationMonth() {
		//TODO
		response = graduationMonthController.sortGraduationMonths("month");
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}
}
