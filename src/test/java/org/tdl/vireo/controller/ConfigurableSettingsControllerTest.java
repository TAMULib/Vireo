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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.ActiveProfiles;
import org.tdl.vireo.model.Configuration;
import org.tdl.vireo.model.ManagedConfiguration;
import org.tdl.vireo.model.repo.ConfigurationRepo;

import com.fasterxml.jackson.core.type.TypeReference;

import edu.emory.mathcs.backport.java.util.Arrays;
import edu.tamu.weaver.response.ApiStatus;

@ActiveProfiles("test")
public class ConfigurableSettingsControllerTest extends AbstractControllerTest {

	protected Configuration TEST_CONFIGURATION_SETTING1 = new ManagedConfiguration("name1", "value1", "type1");
	protected Configuration TEST_CONFIGURATION_SETTING2 = new ManagedConfiguration("name2", "value2", "type2");
	protected static List<Configuration> mockConfigurationSettings;

	public Configuration saveConfiguration(ManagedConfiguration modifiedConfiguration) {
		ManagedConfiguration managedConfiguration = null;
		if(modifiedConfiguration.getName().equals(TEST_CONFIGURATION_SETTING1.getName())) {			
			managedConfiguration = (ManagedConfiguration) TEST_CONFIGURATION_SETTING1;
		}
		return managedConfiguration;
	}

	@Mock
	protected ConfigurationRepo configurationRepo;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		Map<String, List<Configuration>> parameters = new HashMap<String, List<Configuration>>();	
		mockConfigurationSettings = Arrays.asList(new ManagedConfiguration[] {(ManagedConfiguration) TEST_CONFIGURATION_SETTING1, (ManagedConfiguration) TEST_CONFIGURATION_SETTING2});
		parameters.put("list", mockConfigurationSettings);

		when(configurationRepo.getCurrentConfigurations()).thenReturn(parameters);
		
		when(configurationRepo.save(any (ManagedConfiguration.class))).then(new Answer<ManagedConfiguration>() {
			@Override
			public ManagedConfiguration answer(InvocationOnMock invocation) throws Throwable {
				return (ManagedConfiguration) saveConfiguration((ManagedConfiguration) invocation.getArguments()[0]);
			}
		});
		
		when(configurationRepo.reset( any(ManagedConfiguration.class))).then(new Answer<Configuration>() {
			@Override
			public ManagedConfiguration answer(InvocationOnMock invocation) throws Throwable {
				Configuration defaultConfiguration = new ManagedConfiguration("defaultConfigurationName", "defaultConfigurationValue", "defaultConfigurationType");
				return (ManagedConfiguration) defaultConfiguration;
			}			
		});
	}

	@Test
	public void testGetSettings() {
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

	@After
	public void cleanUp() {
		response = null;
		configurationRepo.deleteAll();
	}
}
