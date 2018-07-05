package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.tdl.vireo.model.FieldGloss;

import edu.tamu.weaver.response.ApiStatus;

public class FieldGlossControllerTest extends AbstractControllerTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testGetAllFieldGlosses() {
		response = fieldGlossController.getAllFieldGlosses();
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		List<FieldGloss> fieldGlossList = (List<FieldGloss>) response.getPayload().get("ArrayList<FieldGloss>");
		assertEquals(" The list size is incorrect ", mockFieldGlossList.size() , fieldGlossList.size());
	}

	@Test
	public void testCreateFieldGloss() {
		response = fieldGlossController.createFieldGloss(TEST_FIELD_GLOSS1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		FieldGloss createdFieldGloss = (FieldGloss) response.getPayload().get("FieldGloss");
		assertEquals(" The created field gloss does not have the correct value ", TEST_FIELD_GLOSS1.getValue() , createdFieldGloss.getValue());
		assertEquals(" The created Field Gloss does not have the correct language ", TEST_FIELD_GLOSS1.getLanguage() , createdFieldGloss.getLanguage());
	}

	@Test
	public void testRemoveFieldGloss() {
		response = fieldGlossController.RemoveFieldGloss(TEST_FIELD_GLOSS2);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testUpdateFieldGloss() {
		TEST_FIELD_GLOSS1.setValue("Modified Value for Field Gloss 1");
		response = fieldGlossController.UpdateFieldGloss(TEST_FIELD_GLOSS1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		FieldGloss updatedFieldGloss = (FieldGloss) response.getPayload().get("FieldGloss");
		assertEquals(" The updated field gloss does not have the correct value ", TEST_FIELD_GLOSS1.getValue() , updatedFieldGloss.getValue());
	}

}
