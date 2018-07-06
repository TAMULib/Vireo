package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.tdl.vireo.model.Note;

import edu.tamu.weaver.response.ApiStatus;

public class NoteControllerTest extends AbstractControllerTest {

	@Test
	public void testGetAllNotes() {
		response = noteController.getAllNotes();
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		@SuppressWarnings("unchecked")
		List<Note> noteList = (List<Note>) response.getPayload().get("ArrayList<Note>");
		assertEquals("There are no notes in the list ", mockNoteList.size() , noteList.size());
	}
}