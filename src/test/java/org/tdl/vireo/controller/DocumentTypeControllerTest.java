package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.tdl.vireo.model.DocumentType;

import edu.tamu.weaver.response.ApiStatus;

@ActiveProfiles("test")
public class DocumentTypeControllerTest extends AbstractControllerTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testAllDocumentTypes() {
		response = documentTypeController.allDocumentTypes();
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		List<DocumentType> documentTypeList = (List<DocumentType>) response.getPayload().get("ArrayList<DocumentType>");
        assertEquals("There are no documenttypes in the list " , mockDocumentTypeList.size() , documentTypeList.size() );
	}

	@Test
	public void testCreateDocumentType() {
		response = documentTypeController.createDocumentType(TEST_DOCUMENT_TYPE3);
		DocumentType documentType  = (DocumentType) response.getPayload().get("DocumentType");
		assertEquals("The document type is not added to the repo ", 3, mockDocumentTypeList.size());
		assertEquals(" The created document type does not have correct value " ,  TEST_DOCUMENT_TYPE3.getName() , documentType.getName());
	}

	@Test
	public void testUpdateDocumentType() {
		TEST_DOCUMENT_TYPE1.setName("Modified name for Test Document Type 1");
		response = documentTypeController.updateDocumentType(TEST_DOCUMENT_TYPE1);
		DocumentType updatedDocumentType  = (DocumentType) response.getPayload().get("DocumentType");
		assertEquals(" The updated document type has not been updated " , TEST_DOCUMENT_TYPE1.getName() , updatedDocumentType.getName());
	}
	//TODO
	@Test
	public void testRemoveDocumentType()  {
		response = documentTypeController.removeDocumentType(TEST_DOCUMENT_TYPE3);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}
	//TODO
	@Test
	public void testReorderDocumentTypes() {
		response = documentTypeController.reorderDocumentTypes(3l, 1l);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}
	//TODO
	@Test
	public void testSortDocumentTypes() {
		response = documentTypeController.sortDocumentTypes("name");
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}
}
