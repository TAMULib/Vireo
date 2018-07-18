package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;
import java.util.List;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.tdl.vireo.model.DegreeLevel;
import edu.tamu.weaver.response.ApiStatus;

public class DegreeLevelControllerTest extends AbstractControllerTest {

	@InjectMocks
	private DegreeLevelController degreeLevelController;

	@SuppressWarnings("unchecked")
	@Test
	public void testAllDegreeLevels() {
		response = degreeLevelController.allDegreeLevels();
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		List<DegreeLevel> degreeLevelList = (List<DegreeLevel>) response.getPayload().get("ArrayList<DegreeLevel>");
		assertEquals(" There are no degree levels in the list ", mockDegreeLevelList.size() , degreeLevelList.size());
	}
}
