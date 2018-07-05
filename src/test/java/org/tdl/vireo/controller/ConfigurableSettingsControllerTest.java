package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.tdl.vireo.model.Configuration;
import org.tdl.vireo.model.ManagedConfiguration;

import com.fasterxml.jackson.core.type.TypeReference;

import edu.tamu.weaver.response.ApiStatus;

@ActiveProfiles("test")
public class ConfigurableSettingsControllerTest extends AbstractControllerTest {

	@Test
	public void testGetSettings() throws Exception {
		response = configurableSettingsController.getSettings();		
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Map<String, List<Configuration>> currentConfigurationsMap = objectMapper.readValue(objectMapper.writeValueAsString(response.getPayload().get("HashMap")), new TypeReference<Map<String, List<ManagedConfiguration>>>(){});
		List<Configuration> currentConfigurationsList = currentConfigurationsMap.get("list");
		assertEquals("No configuration setting was found in response", 2 ,currentConfigurationsList.size());
		assertEquals("The name for test config1 is not correct", TEST_CONFIGURATION_SETTING1.getName(), currentConfigurationsList.get(0).getName());
	}

	@Test
	public void testUpdateSettings() {
		ManagedConfiguration testManagedConfiguration = new ManagedConfiguration(TEST_CONFIGURATION_SETTING1.getName(), TEST_CONFIGURATION_SETTING1.getValue(), TEST_CONFIGURATION_SETTING1.getType());
		response = configurableSettingsController.updateSetting(testManagedConfiguration);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		ManagedConfiguration testConfig = (ManagedConfiguration)response.getPayload().get("ManagedConfiguration");
		assertEquals("The configuration name is incorrect",TEST_CONFIGURATION_SETTING1.getName(), testConfig.getName());
	}

	@Test
	public void testResetSetting() {
		ManagedConfiguration testManagedConfiguration = new ManagedConfiguration(TEST_CONFIGURATION_SETTING1.getName(), TEST_CONFIGURATION_SETTING1.getValue(), TEST_CONFIGURATION_SETTING1.getType());
		response = configurableSettingsController.resetSetting(testManagedConfiguration);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Configuration defaultConfiguration = (Configuration) response.getPayload().get("ManagedConfiguration");
		assertEquals("The configuration name is incorrect","defaultConfigurationName", defaultConfiguration.getName());
		assertEquals("The configuration value is incorrect","defaultConfigurationValue", defaultConfiguration.getValue());
		assertEquals("The configuration type is incorrect","defaultConfigurationType", defaultConfiguration.getType());
	}
}
