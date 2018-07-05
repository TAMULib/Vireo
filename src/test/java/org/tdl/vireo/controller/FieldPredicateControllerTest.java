package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.tdl.vireo.model.FieldPredicate;

import edu.tamu.weaver.response.ApiStatus;

public class FieldPredicateControllerTest extends AbstractControllerTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testGetAllFieldPredicates() {
		response = fieldPredicateController.getAllFieldPredicates();
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		List<FieldPredicate> fieldPredicatesList = (List<FieldPredicate>) response.getPayload().get("ArrayList<FieldPredicate>");
		assertEquals(" The size of fieldPredicate list is incorrect ", mockFieldPredicateList.size() , fieldPredicatesList.size());
	}

	@Test
	public void testGetFieldPredicateByValue() {
		response  = fieldPredicateController.getFieldPredicateByValue(TEST_FIELD_PREDICATE1.getValue());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		FieldPredicate fieldPredicate = (FieldPredicate) response.getPayload().get("FieldPredicate");
		assertEquals(" The field predicate does not have the correct value " , TEST_FIELD_PREDICATE1.getValue() , fieldPredicate.getValue());
	}

	@Test
	public void testCreateFieldPredicate() {
		response = fieldPredicateController.createFieldPredicate(TEST_FIELD_PREDICATE1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		FieldPredicate createdFieldPredicate = (FieldPredicate) response.getPayload().get("FieldPredicate");
		assertEquals(" The field predicate does not have the correct value " , TEST_FIELD_PREDICATE1.getValue() , createdFieldPredicate.getValue());
		assertEquals(" The created field predicate does not have correct documentType Predicate flag " , TEST_FIELD_PREDICATE1.getDocumentTypePredicate() , createdFieldPredicate.getDocumentTypePredicate());
	}

	@Test
	public void testRemoveFieldPredicate() {
		response = fieldPredicateController.removeFieldPredicate(TEST_FIELD_PREDICATE1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testUpdateFieldPredicate() {
		TEST_FIELD_PREDICATE.setValue("Modified Field Predicate Value");
		response  = fieldPredicateController.updateFieldPredicate(TEST_FIELD_PREDICATE);
		FieldPredicate updatedFieldPredicate = (FieldPredicate) response.getPayload().get("FieldPredicate");
		assertEquals(" The updated field predicate does not have the modified value " , TEST_FIELD_PREDICATE.getValue() , updatedFieldPredicate.getValue());
	}
}
