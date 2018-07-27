package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.ActiveProfiles;
import org.tdl.vireo.model.Configuration;
import org.tdl.vireo.model.ManagedConfiguration;
import org.tdl.vireo.model.repo.ConfigurationRepo;
import org.tdl.vireo.service.VireoThemeManagerService;

import com.fasterxml.jackson.core.type.TypeReference;

import edu.tamu.weaver.response.ApiStatus;

@ActiveProfiles("test")
public class ConfigurableSettingsControllerTest extends AbstractControllerTest {

	private Map<String, List<Configuration>> parameters;

	@Mock
	private VireoThemeManagerService themeManagerService;

	@InjectMocks
	private ConfigurableSettingsController configurableSettingsController;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		parameters = new HashMap<String, List<Configuration>>();

		parameters.put("list", mockConfigurationSettings);

		when(configurationRepo.getCurrentConfigurations()).thenReturn(parameters);

		when(configurationRepo.save(any(ManagedConfiguration.class))).then(new Answer<ManagedConfiguration>() {
			@Override
			public ManagedConfiguration answer(InvocationOnMock invocation) throws Throwable {
				return saveConfiguration((ManagedConfiguration) invocation.getArguments()[0]);
			}
		});

		when(configurationRepo.reset(any(ManagedConfiguration.class))).then(new Answer<Configuration>() {
			@Override
			public Configuration answer(InvocationOnMock invocation) throws Throwable {
				Configuration defaultConfiguration = new ManagedConfiguration(TEST_CONFIGURATION_SETTING1.getName(),TEST_CONFIGURATION_SETTING1.getValue(), TEST_CONFIGURATION_SETTING1.getType());
				return defaultConfiguration;
			}
		});
	}

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
		response = configurableSettingsController.updateSetting(TEST_CONFIGURATION_SETTING1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		ManagedConfiguration testConfig = (ManagedConfiguration)response.getPayload().get("ManagedConfiguration");
		assertEquals("The configuration name is incorrect",TEST_CONFIGURATION_SETTING1.getName(), testConfig.getName());
	}

	@Test
	public void testResetSetting() {
		response = configurableSettingsController.resetSetting(TEST_CONFIGURATION_SETTING1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Configuration defaultConfiguration = (Configuration) response.getPayload().get("ManagedConfiguration");
		assertEquals("The configuration name is incorrect", TEST_CONFIGURATION_SETTING1.getName(), defaultConfiguration.getName());
		assertEquals("The configuration value is incorrect",TEST_CONFIGURATION_SETTING1.getValue(), defaultConfiguration.getValue());
		assertEquals("The configuration type is incorrect", TEST_CONFIGURATION_SETTING1.getType(), defaultConfiguration.getType());
	}

	@After
	public void cleanUp() {
		configurationRepo.deleteAll();
	}
}