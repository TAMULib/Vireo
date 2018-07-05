package org.tdl.vireo.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.tdl.vireo.auth.controller.AuthController;
import org.tdl.vireo.auth.service.VireoUserCredentialsService;
import org.tdl.vireo.mock.MockData;
import org.tdl.vireo.model.Configuration;
import org.tdl.vireo.model.ControlledVocabulary;
import org.tdl.vireo.model.ControlledVocabularyCache;
import org.tdl.vireo.model.CustomActionDefinition;
import org.tdl.vireo.model.DocumentType;
import org.tdl.vireo.model.EmailTemplate;
import org.tdl.vireo.model.Embargo;
import org.tdl.vireo.model.EmbargoGuarantor;
import org.tdl.vireo.model.FieldGloss;
import org.tdl.vireo.model.Language;
import org.tdl.vireo.model.ManagedConfiguration;
import org.tdl.vireo.model.User;
import org.tdl.vireo.model.VocabularyWord;
import org.tdl.vireo.model.repo.ConfigurationRepo;
import org.tdl.vireo.model.repo.ControlledVocabularyRepo;
import org.tdl.vireo.model.repo.CustomActionDefinitionRepo;
import org.tdl.vireo.model.repo.DegreeRepo;
import org.tdl.vireo.model.repo.DepositLocationRepo;
import org.tdl.vireo.model.repo.DocumentTypeRepo;
import org.tdl.vireo.model.repo.EmailTemplateRepo;
import org.tdl.vireo.model.repo.EmbargoRepo;
import org.tdl.vireo.model.repo.FieldGlossRepo;
import org.tdl.vireo.model.repo.LanguageRepo;
import org.tdl.vireo.model.repo.UserRepo;
import org.tdl.vireo.model.repo.VocabularyWordRepo;
import org.tdl.vireo.service.ControlledVocabularyCachingService;
import org.tdl.vireo.service.DepositorService;
import org.tdl.vireo.service.EntityControlledVocabularyService;
import org.tdl.vireo.service.ProquestCodesService;
import org.tdl.vireo.utility.TemplateUtility;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.weaver.auth.model.Credentials;
import edu.tamu.weaver.auth.service.CryptoService;
import edu.tamu.weaver.email.service.MockEmailService;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.token.service.TokenService;
import edu.tamu.weaver.utility.HttpUtility;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public abstract class AbstractControllerTest extends MockData {

	protected static ApiResponse response;
    
    @Spy
    protected ObjectMapper objectMapper;

    @Spy
    protected BCryptPasswordEncoder passwordEncoder;

    @Mock
    protected SimpMessagingTemplate simpMessagingTemplate;

    @Mock
    protected Environment env;

    @Mock
    protected MockEmailService mockEmailService;

    @Spy
    @InjectMocks
    protected HttpUtility httpUtility;

    @Spy
    @InjectMocks
    protected CryptoService cryptoService;

    @Spy
    @InjectMocks
    protected TokenService tokenService;

    @Spy
    @InjectMocks
    protected TemplateUtility templateUtility;

    @Spy
	protected  ControlledVocabularyCachingService controlledVocabularyCachingService;

	@InjectMocks
	protected EntityControlledVocabularyService entityControlledVocabularyService;

	@Mock
	protected  ControlledVocabularyRepo controlledVocabularyRepo;

	@Mock
	protected EmailTemplate emailTemplate;

	@Spy
    @InjectMocks
    protected ProquestCodesService proquestCodesService;

    @Spy
    @InjectMocks
    protected DepositorService depositorService;

    @InjectMocks
    protected AuthController authController;

    @InjectMocks
    protected ConfigurableSettingsController configurableSettingsController;

    @InjectMocks
	protected ControlledVocabularyController controlledVocabularyController;

    @InjectMocks
	protected CustomActionSettingsController customActionSettingsController;

    @InjectMocks
	protected DegreeController degreeController;

    @InjectMocks
	protected DepositLocationController depositLocationController;

    @InjectMocks
    protected DocumentTypeController documentTypeController;

    @InjectMocks
    protected EmailTemplateController emailTemplateController;

    @InjectMocks
    protected EmbargoController embargoController;

    @InjectMocks
    protected FieldGlossController fieldGlossController;

    @Mock
	protected ConfigurationRepo configurationRepo;

    @Mock
    protected DepositLocationRepo depositLocationRepo;

    @Mock
    protected DocumentTypeRepo documentTypeRepo;

    @Mock
    protected CustomActionDefinitionRepo customActionDefinitionRepo;

    @Mock
    protected DegreeRepo degreeRepo;

    @Mock
    protected EmailTemplateRepo emailTemplateRepo;

    @Mock
    protected  EmbargoRepo embargoRepo;

    @Mock
    protected  FieldGlossRepo fieldGlossRepo;

    @Mock
    protected  LanguageRepo languageRepo;

    @Mock
    protected UserRepo userRepo;

    @Mock
	protected  VocabularyWordRepo vocabularyWordRepo;

    protected Credentials TEST_CREDENTIALS = new Credentials();

    @Mock
    protected VireoUserCredentialsService vireoUserCredentialsService;

    @SuppressWarnings("unchecked")
	@Before
    public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		ReflectionTestUtils.setField(httpUtility, HTTP_DEFAULT_TIMEOUT_NAME, HTTP_DEFAULT_TIMEOUT_VALUE);
		
		ReflectionTestUtils.setField(cryptoService, SECRET_PROPERTY_NAME, SECRET_VALUE);
		
		ReflectionTestUtils.setField(tokenService, AUTH_SECRET_KEY_PROPERTY_NAME, AUTH_SECRET_KEY_VALUE);
		
		ReflectionTestUtils.setField(tokenService, AUTH_ISSUER_KEY_PROPERTY_NAME, AUTH_ISSUER_KEY_VALUE);
		
		ReflectionTestUtils.setField(tokenService, AUTH_DURATION_PROPERTY_NAME, AUTH_DURATION_VALUE);
		
		ReflectionTestUtils.setField(tokenService, AUTH_KEY_PROPERTY_NAME, AUTH_KEY_VALUE);
		
		ReflectionTestUtils.setField(authController, "url", "localhost:9000");
		
		TEST_CREDENTIALS.setFirstName(TEST_USER_FIRST_NAME);
		TEST_CREDENTIALS.setLastName(TEST_USER_LAST_NAME);
		TEST_CREDENTIALS.setEmail(TEST_USER_EMAIL);
		TEST_CREDENTIALS.setRole(TEST_USER_ROLE.toString());
		
		//Configurable Settings 
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
		//ControlledVocabulary
		when(controlledVocabularyRepo.findAllByOrderByPositionAsc()).thenReturn(mockControlledVocabularyList);
		
		when(controlledVocabularyRepo.create(any(String.class), any(Language.class))).then(new Answer<ControlledVocabulary>() {
			@Override
			public ControlledVocabulary answer(InvocationOnMock invocation) throws Throwable {
				return TEST_CONTROLLED_VOCABULARY_1;
			}
		});
		when( controlledVocabularyRepo.update(any(ControlledVocabulary.class))).then(new Answer<ControlledVocabulary>() {
			@Override
			public ControlledVocabulary answer(InvocationOnMock invocation) throws Throwable {
				return updateControlledVocabulary((ControlledVocabulary) invocation.getArguments()[0]);
			}
		});
		when(controlledVocabularyRepo.findByName(any(String.class))).then(new Answer<ControlledVocabulary>() {
			@Override
			public ControlledVocabulary answer(InvocationOnMock invocation) throws Throwable {
				return findControlledVocabularyByName((String)invocation.getArguments()[0]);
			}
		});
		when(controlledVocabularyCachingService.doesControlledVocabularyExist(any(String.class))).then(new Answer<Boolean>() {
		
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				return true;
			}
		});
		when(controlledVocabularyCachingService.getControlledVocabularyCache(any(String.class))).then(new Answer<ControlledVocabularyCache>() {
			@Override
			public ControlledVocabularyCache answer(InvocationOnMock invocation) throws Throwable {
				return TEST_CONTROLLED_VOCABULARY_CACHE;
			}			
		});
		when(controlledVocabularyRepo.findOne(any(Long.class))).then(new Answer<ControlledVocabulary>() {
			@Override
			public ControlledVocabulary answer(InvocationOnMock invocation) throws Throwable {
				return findControlledVocabularyById((Long)invocation.getArguments()[0]);
			}
		});
		when(vocabularyWordRepo.create(any(ControlledVocabulary.class), any(String.class), any(String.class), any(String.class), any(List.class))).then(new Answer<VocabularyWord>() {
			@Override
			public VocabularyWord answer(InvocationOnMock invocation) throws Throwable {
				return TEST_VOCABULARY_WORD1;
			}
		});
		when(vocabularyWordRepo.findOne(any(Long.class))).then(new Answer<VocabularyWord>() {
			@Override
			public VocabularyWord answer(InvocationOnMock invocation) throws Throwable {
				return findVocabularyWordById((Long)invocation.getArguments()[0]);
			}
		});
		when(vocabularyWordRepo.update(any(VocabularyWord.class))).then(new Answer<VocabularyWord>() {
			@Override
			public VocabularyWord answer(InvocationOnMock invocation) throws Throwable {
				return updateVocabularyWord((VocabularyWord)invocation.getArguments()[0]);
			}
		});
		
		//Custom Settings
		
		when(customActionDefinitionRepo.findAllByOrderByPositionAsc()).thenReturn(mockCustomActionDefList);
		when(customActionDefinitionRepo.create( any(String.class), any(Boolean.class) )).then(new Answer<CustomActionDefinition>() {
			@Override
			public CustomActionDefinition answer(InvocationOnMock invocation) throws Throwable {
				return createCustomActionDefinition( (String)invocation.getArguments()[0], (Boolean)invocation.getArguments()[1]);
			}
		});
		
		when(customActionDefinitionRepo.update(any(CustomActionDefinition.class) )).then(new Answer<CustomActionDefinition>() {
			@Override
			public CustomActionDefinition answer(InvocationOnMock invocation) throws Throwable {
				return updateCustomActionDefinition( (CustomActionDefinition)invocation.getArguments()[0]);
			}			
		});
		
		//DocumentType
		when(documentTypeRepo.findAllByOrderByPositionAsc()).thenReturn(mockDocumentTypeList);
		
		when(documentTypeRepo.create( any(String.class) )).then( new Answer<DocumentType>() {
			@Override
			public DocumentType answer(InvocationOnMock invocation) throws Throwable {
				mockDocumentTypeList.add(TEST_DOCUMENT_TYPE3);
				return createDocumentType((String) invocation.getArguments()[0]);
			}
		});
		
		when(documentTypeRepo.update( any(DocumentType.class) )).then( new Answer<DocumentType>() {
			@Override
			public DocumentType answer(InvocationOnMock invocation) throws Throwable {
				return updateDocumentType( (DocumentType) invocation.getArguments()[0]);
			}
		});
		
		//Email Template
		when(emailTemplateRepo.findAllByOrderByPositionAsc()).thenReturn(mockEmailTemplateList);
		
		/*when(emailTemplateRepo.findByNameOverride( any(String.class))).then(new Answer<EmailTemplate>() {
			@Override
			public EmailTemplate answer(InvocationOnMock invocation) throws Throwable {
				return findEmailTemplateByNameOverride((String) invocation.getArguments()[0] );
			}
		});*/
		
		when(emailTemplateRepo.findByNameOverride(REGISTRATION_TEMPLATE)).then(new Answer<Object>() {
		    @Override
		    public Object answer(InvocationOnMock invocation) throws Throwable {
		        EmailTemplate emailTemplate = new EmailTemplate(TEST_EMAIL_TEMPLATE_NAME, TEST_EMAIL_TEMPLATE_SUBJECT, TEST_EMAIL_TEMPLATE_MESSAGE);
		        emailTemplate.setPosition(emailTemplatePosition++);
		        return emailTemplate;
		    }
		});
		
		when(emailTemplateRepo.create(any(String.class), any(String.class) , any(String.class) )).then(new Answer<EmailTemplate>() {
			@Override
			public EmailTemplate answer(InvocationOnMock invocation) throws Throwable {
				return createEmailTemplate( (String)invocation.getArguments()[0] , (String)invocation.getArguments()[1] , (String)invocation.getArguments()[2]);
			}
		});
		
		when(emailTemplateRepo.update( any(EmailTemplate.class) )).then(new Answer<EmailTemplate>() {
			@Override
			public EmailTemplate answer(InvocationOnMock invocation) throws Throwable {
				return updateEmailTemplate( (EmailTemplate) invocation.getArguments()[0]);
			}
		});
		
		//Embargo
		when(embargoRepo.findAllByOrderByGuarantorAscPositionAsc()).thenReturn(mockEmbargoList);
		
		when(embargoRepo.create(any(String.class), any(String.class), any(Integer.class), any(EmbargoGuarantor.class) , any(Boolean.class))).then(new Answer<Embargo>() {
			@Override
			public Embargo answer(InvocationOnMock invocation) throws Throwable {
				return createEmabrgo( (String) invocation.getArguments()[0] , (String) invocation.getArguments()[1] , (Integer)invocation.getArguments()[2], (EmbargoGuarantor) invocation.getArguments()[3] , (Boolean) invocation.getArguments()[4]);
			}
		});
		
		when(embargoRepo.update(any(Embargo.class))).then( new Answer<Embargo>() {
			@Override
			public Embargo answer(InvocationOnMock invocation) throws Throwable {
				return updateEmabrgo((Embargo) invocation.getArguments()[0]);
			}
		});
		
		//FieldGloss
		when(fieldGlossRepo.findAll()).thenReturn(mockFieldGlossList);
		
		when(languageRepo.findByName(any(String.class))).then(new Answer<Language>() {
			@Override
			public Language answer(InvocationOnMock invocation) throws Throwable {
				return findLanguageByName((String) invocation.getArguments()[0]);
			}
		});
		
		when(fieldGlossRepo.create( any(String.class), any(Language.class) )).then(new Answer<FieldGloss>() {
			@Override
			public FieldGloss answer(InvocationOnMock invocation) throws Throwable {
				return createFieldGloss( (String)invocation.getArguments()[0], (Language)invocation.getArguments()[1]);
			}
		});
		
		when(fieldGlossRepo.update(any(FieldGloss.class) )).then(new Answer<FieldGloss>() {
		
			@Override
			public FieldGloss answer(InvocationOnMock invocation) throws Throwable {
				return updateFieldGloss( (FieldGloss)invocation.getArguments()[0]);
			}
		});
		
		//User
		when(userRepo.findAll()).thenReturn(mockUsers);
		
		when(userRepo.findByEmail(any(String.class))).then(new Answer<Object>() {
		    @Override
		    public Object answer(InvocationOnMock invocation) throws Throwable {
		        return findByEmail((String) invocation.getArguments()[0]);
		    }
		});
		
		when(userRepo.save(any(User.class))).then(new Answer<Object>() {
		    @Override
		    public Object answer(InvocationOnMock invocation) throws Throwable {
		        return updateUser((User) invocation.getArguments()[0]);
		    }
		});
		
		when(vireoUserCredentialsService.createUserFromRegistration(any(String.class), any(String.class), any(String.class), any(String.class))).then(new Answer<Object>() {
		    @Override
		    public Object answer(InvocationOnMock invocation) throws Throwable {
		        return userRepo.save(new User((String) invocation.getArguments()[0], (String) invocation.getArguments()[1], (String) invocation.getArguments()[2], (String) invocation.getArguments()[3], TEST_USER_ROLE));
		    }
		});
    }

    @After
	public void cleanUp() {
		response = null;
		configurationRepo.deleteAll();
		controlledVocabularyRepo.deleteAll();
		customActionDefinitionRepo.deleteAll();
		degreeRepo.deleteAll();
		depositLocationRepo.deleteAll();
		documentTypeRepo.deleteAll();
		emailTemplateRepo.deleteAll();
		embargoRepo.deleteAll();
		fieldGlossRepo.deleteAll();
		languageRepo.deleteAll();
		userRepo.deleteAll();
	}

}
