package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.tdl.vireo.model.OrganizationCategory;

import edu.tamu.weaver.response.ApiStatus;

public class OrganizationCategoryControllerTest extends AbstractControllerTest {

	@Test
	public void testGetOrganizationCategories() {
		response = organizationCategoryController.getOrganizationCategories();
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		@SuppressWarnings("unchecked")
		List<OrganizationCategory> organizationCategoryList = (List<OrganizationCategory>) response.getPayload().get("ArrayList<OrganizationCategory>");
		assertEquals("There are no organizationCategories in the list ", mockOrganizationCategoryList.size() , organizationCategoryList.size());
	}

	@Test
	public void testCreateOrganizationCategory() {
		response = organizationCategoryController.createOrganizationCategory(TEST_ORGANIZATION_CATEGORY1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		OrganizationCategory createdOrganizationCategory = (OrganizationCategory) response.getPayload().get("OrganizationCategory");
		assertEquals("The created organizationCategory does not have correct name ", TEST_ORGANIZATION_CATEGORY1.getName() , createdOrganizationCategory.getName());
	}

	@Test
	public void testUpdateOrganizationCategory() {
		TEST_ORGANIZATION_CATEGORY1.setName("Modifying rrganizationCategory name");
		response = organizationCategoryController.updateOrganizationCategory(TEST_ORGANIZATION_CATEGORY1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		OrganizationCategory updatedOrganizationCategory = (OrganizationCategory) response.getPayload().get("OrganizationCategory");
		assertEquals("The updated organizationCategory does not have updated name ", TEST_ORGANIZATION_CATEGORY1.getName() , updatedOrganizationCategory.getName());
	}

	@Test
	public void testRemoveOrganizationCategory() {
		//TODO
		response = organizationCategoryController.removeOrganizationCategory(TEST_ORGANIZATION_CATEGORY1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}
}
