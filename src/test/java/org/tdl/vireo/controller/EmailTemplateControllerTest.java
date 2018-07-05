package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.tdl.vireo.model.EmailTemplate;

import edu.tamu.weaver.response.ApiStatus;

@SuppressWarnings("unchecked")
@ActiveProfiles("test")
public class EmailTemplateControllerTest extends AbstractControllerTest {

	@Test
	public void testAllEmailTemplates() {
		response = emailTemplateController.allEmailTemplates();
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		List<EmailTemplate> emailTemplateList = (List<EmailTemplate>) response.getPayload().get("ArrayList<EmailTemplate>");
		assertEquals("The email template list size is incorrect " , mockEmailTemplateList.size() , emailTemplateList.size());
	}

	@Test
	public void testCreateEmailTemplate() {
		response = emailTemplateController.createEmailTemplate(TEST_EMAIL_TEMPLATE3);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		EmailTemplate createdEmailTemplate = (EmailTemplate) response.getPayload().get("EmailTemplate");
		assertEquals(" The created email template has incorrect name " , TEST_EMAIL_TEMPLATE3.getName() , createdEmailTemplate.getName());
		assertEquals(" The created email template has incorrect subject " , TEST_EMAIL_TEMPLATE3.getSubject() , createdEmailTemplate.getSubject());
		assertEquals(" The created email template has incorrect message " , TEST_EMAIL_TEMPLATE3.getMessage() , createdEmailTemplate.getMessage());
	}

	@Test
	public void testUpdateEmailTemplate() throws Exception {
		response = emailTemplateController.updateEmailTemplate(TEST_EMAIL_TEMPLATE3);
		EmailTemplate updatedEmailTemplate = (EmailTemplate) response.getPayload().get("EmailTemplate");
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		assertEquals(" The updated email template has incorrect name " , TEST_EMAIL_TEMPLATE3.getName() , updatedEmailTemplate.getName());
		assertEquals(" The updated email template has incorrect subject " , TEST_EMAIL_TEMPLATE3.getSubject() , updatedEmailTemplate.getSubject());
		assertEquals(" The updated email template has incorrect message " , TEST_EMAIL_TEMPLATE3.getMessage() , updatedEmailTemplate.getMessage());

		TEST_EMAIL_TEMPLATE3.setSystemRequired(true);
		response = emailTemplateController.updateEmailTemplate(TEST_EMAIL_TEMPLATE3);
		EmailTemplate createdEmailTemplate = (EmailTemplate) response.getPayload().get("EmailTemplate");
		assertEquals(" The created email template has incorrect name " , TEST_EMAIL_TEMPLATE3.getName() , createdEmailTemplate.getName());
		assertEquals(" The created email template has incorrect subject " , TEST_EMAIL_TEMPLATE3.getSubject() , createdEmailTemplate.getSubject());
		assertEquals(" The created email template has incorrect message " , TEST_EMAIL_TEMPLATE3.getMessage() , createdEmailTemplate.getMessage());
	}
    //TODO
	@Test
	public void testRemoveEmailTemplate() {
		response = emailTemplateController.removeEmailTemplate(TEST_EMAIL_TEMPLATE3);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}
	//TODO
	@Test
	public void testReorderEmailTemplates() {
		response = emailTemplateController.reorderEmailTemplates(2l, 3l);
	}
	//TODO
	@Test
	public void testSortEmailTemplates() {
		response = emailTemplateController.sortEmailTemplates("name");
	}
}
