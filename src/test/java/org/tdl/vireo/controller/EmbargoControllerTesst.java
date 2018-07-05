package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.tdl.vireo.model.Embargo;

import edu.tamu.weaver.response.ApiStatus;

public class EmbargoControllerTesst extends AbstractControllerTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testGetEmbargoes() {
		response = embargoController.getEmbargoes();
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		List<Embargo> embargoList = (List<Embargo>) response.getPayload().get("ArrayList<Embargo>");
		assertEquals("There are no degrees in the list ", mockEmbargoList.size(), embargoList.size());
	}

	@Test
	public void testCreateEmbargo() {
		response = embargoController.createEmbargo(TEST_EMBARGO1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Embargo createdEmbargo = (Embargo) response.getPayload().get("Embargo");
		assertEquals("The created embargo does not have correct name ", TEST_EMBARGO1.getName(),
				createdEmbargo.getName());
		assertEquals("The created embargo does not have correct description ", TEST_EMBARGO1.getDescription(),
				createdEmbargo.getDescription());
		assertEquals("The created embargo does not have correct duration ", TEST_EMBARGO1.getDuration(),
				createdEmbargo.getDuration());
		assertEquals("The created embargo does not have correct giurantor ", TEST_EMBARGO1.getGuarantor(),
				createdEmbargo.getGuarantor());
		assertEquals("The created embargo does not have correct active flag ", TEST_EMBARGO1.isActive(),
				createdEmbargo.isActive());
	}

	@Test
	public void testUpdateEmbargo() {
		TEST_EMBARGO1.setName("Modified the Embargo Name");
		response = embargoController.updateEmbargo(TEST_EMBARGO1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Embargo updatedEmbargo = (Embargo) response.getPayload().get("Embargo");
		assertEquals("The updated degree does not have updated name ", TEST_EMBARGO1.getName(),
				updatedEmbargo.getName());
	}

	@Test
	public void testRemoveEmbargo() {
		// TODO
		response = embargoController.removeEmbargo(TEST_EMBARGO1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testReorderEmbargoes() {
		// TODO
		response = embargoController.reorderEmbargoes(TEST_EMBARGO1.getGuarantor().toString(), 3l, 1l);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testSortEmbargoes() {
		// TODO
		response = embargoController.sortEmbargoes(TEST_EMBARGO1.getGuarantor().toString(), "description");
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}
}
