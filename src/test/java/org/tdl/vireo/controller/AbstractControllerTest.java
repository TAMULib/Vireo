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
import org.tdl.vireo.model.ActionLog;
import org.tdl.vireo.model.Configuration;
import org.tdl.vireo.model.ControlledVocabulary;
import org.tdl.vireo.model.ControlledVocabularyCache;
import org.tdl.vireo.model.CustomActionDefinition;
import org.tdl.vireo.model.Degree;
import org.tdl.vireo.model.DegreeLevel;
import org.tdl.vireo.model.DocumentType;
import org.tdl.vireo.model.EmailTemplate;
import org.tdl.vireo.model.Embargo;
import org.tdl.vireo.model.EmbargoGuarantor;
import org.tdl.vireo.model.FieldGloss;
import org.tdl.vireo.model.FieldPredicate;
import org.tdl.vireo.model.FieldProfile;
import org.tdl.vireo.model.FieldValue;
import org.tdl.vireo.model.GraduationMonth;
import org.tdl.vireo.model.InputType;
import org.tdl.vireo.model.Language;
import org.tdl.vireo.model.ManagedConfiguration;
import org.tdl.vireo.model.Note;
import org.tdl.vireo.model.Organization;
import org.tdl.vireo.model.OrganizationCategory;
import org.tdl.vireo.model.Role;
import org.tdl.vireo.model.Submission;
import org.tdl.vireo.model.SubmissionFieldProfile;
import org.tdl.vireo.model.SubmissionStatus;
import org.tdl.vireo.model.User;
import org.tdl.vireo.model.VocabularyWord;
import org.tdl.vireo.model.WorkflowStep;
import org.tdl.vireo.model.repo.AbstractEmailRecipientRepo;
import org.tdl.vireo.model.repo.ActionLogRepo;
import org.tdl.vireo.model.repo.ConfigurationRepo;
import org.tdl.vireo.model.repo.ControlledVocabularyRepo;
import org.tdl.vireo.model.repo.CustomActionDefinitionRepo;
import org.tdl.vireo.model.repo.DegreeRepo;
import org.tdl.vireo.model.repo.DepositLocationRepo;
import org.tdl.vireo.model.repo.DocumentTypeRepo;
import org.tdl.vireo.model.repo.EmailTemplateRepo;
import org.tdl.vireo.model.repo.EmailWorkflowRuleRepo;
import org.tdl.vireo.model.repo.EmbargoRepo;
import org.tdl.vireo.model.repo.FieldGlossRepo;
import org.tdl.vireo.model.repo.FieldPredicateRepo;
import org.tdl.vireo.model.repo.FieldProfileRepo;
import org.tdl.vireo.model.repo.FieldValueRepo;
import org.tdl.vireo.model.repo.GraduationMonthRepo;
import org.tdl.vireo.model.repo.InputTypeRepo;
import org.tdl.vireo.model.repo.LanguageRepo;
import org.tdl.vireo.model.repo.NamedSearchFilterGroupRepo;
import org.tdl.vireo.model.repo.NoteRepo;
import org.tdl.vireo.model.repo.OrganizationCategoryRepo;
import org.tdl.vireo.model.repo.OrganizationRepo;
import org.tdl.vireo.model.repo.SubmissionFieldProfileRepo;
import org.tdl.vireo.model.repo.SubmissionListColumnRepo;
import org.tdl.vireo.model.repo.SubmissionRepo;
import org.tdl.vireo.model.repo.SubmissionStatusRepo;
import org.tdl.vireo.model.repo.UserRepo;
import org.tdl.vireo.model.repo.VocabularyWordRepo;
import org.tdl.vireo.model.repo.WorkflowStepRepo;
import org.tdl.vireo.service.ControlledVocabularyCachingService;
import org.tdl.vireo.service.DepositorService;
import org.tdl.vireo.service.EntityControlledVocabularyService;
import org.tdl.vireo.service.ProquestCodesService;
import org.tdl.vireo.utility.FileIOUtility;
import org.tdl.vireo.utility.PackagerUtility;
import org.tdl.vireo.utility.TemplateUtility;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.weaver.auth.model.Credentials;
import edu.tamu.weaver.auth.service.CryptoService;
import edu.tamu.weaver.email.service.EmailSender;
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
	protected EmailSender emailSender;

	@Mock
	protected Environment env;

	@Mock
	protected MockEmailService mockEmailService;

	@Mock
	protected TemplateUtility templateUtility;

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
	protected FileIOUtility fileIOUtility;

	@Spy
	@InjectMocks
	protected PackagerUtility packagerUtility;

	@Spy
	protected ControlledVocabularyCachingService controlledVocabularyCachingService;

	@InjectMocks
	protected EntityControlledVocabularyService entityControlledVocabularyService;

	@Mock
	protected ControlledVocabularyRepo controlledVocabularyRepo;

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

	@InjectMocks
	protected FieldPredicateController fieldPredicateController;

	@InjectMocks
	protected FieldProfileController fieldProfileController;

	@InjectMocks
	protected GraduationMonthController graduationMonthController;

	@InjectMocks
	protected InputTypeController inputTypeController;

	@InjectMocks
	protected LanguageController languageController;

	@InjectMocks
	protected NoteController noteController;

	@InjectMocks
	protected OrganizationController organizationController;

	@InjectMocks
	protected OrganizationCategoryController organizationCategoryController;

	@InjectMocks
	protected SubmissionController submissionController;

	@InjectMocks
	protected SubmissionStatusController submissionStatusController;

	@InjectMocks
	protected UserController userController;

	@InjectMocks
	protected WorkflowStepController workflowStepController;

	@Mock
	protected AbstractEmailRecipientRepo abstractEmailRecipientRepo;

	@Mock
	protected ActionLogRepo actionLogRepo;

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
	protected EmailWorkflowRuleRepo emailWorkflowRuleRepo;

	@Mock
	protected EmbargoRepo embargoRepo;

	@Mock
	protected FieldGlossRepo fieldGlossRepo;

	@Mock
	protected FieldPredicateRepo fieldPredicateRepo;

	@Mock
	protected FieldProfileRepo fieldProfileRepo;

	@Mock
	protected FieldValueRepo fieldValueRepo;

	@Mock
	protected GraduationMonthRepo graduationMonthRepo;

	@Mock
	protected InputTypeRepo inputTypeRepo;

	@Mock
	protected LanguageRepo languageRepo;

	@Mock
	protected NamedSearchFilterGroupRepo namedSearchFilterGroupRepo;

	@Mock
	protected NoteRepo noteRepo;

	@Mock
	protected OrganizationCategoryRepo organizationCategoryRepo;

	@Mock
	protected OrganizationRepo organizationRepo;

	@Mock
	protected SubmissionRepo submissionRepo;

	@Mock
	protected SubmissionFieldProfileRepo submissionFieldProfileRepo;

	@Mock
	protected SubmissionStatusRepo submissionStatusRepo;

	@Mock
	protected UserRepo userRepo;

	@Mock
	protected WorkflowStepRepo workflowStepRepo;

	@Mock
	protected VocabularyWordRepo vocabularyWordRepo;

	protected Credentials TEST_CREDENTIALS = new Credentials();

	@Mock
	protected VireoUserCredentialsService vireoUserCredentialsService;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {

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

		// Configurable Settings

		Map<String, List<Configuration>> parameters = new HashMap<String, List<Configuration>>();
		mockConfigurationSettings = Arrays.asList(new ManagedConfiguration[] { (ManagedConfiguration) TEST_CONFIGURATION_SETTING1,(ManagedConfiguration) TEST_CONFIGURATION_SETTING2 });
		parameters.put("list", mockConfigurationSettings);

		when(configurationRepo.getCurrentConfigurations()).thenReturn(parameters);

		when(configurationRepo.save(any(ManagedConfiguration.class))).then(new Answer<ManagedConfiguration>() {
			@Override
			public ManagedConfiguration answer(InvocationOnMock invocation) throws Throwable {
				return (ManagedConfiguration) saveConfiguration((ManagedConfiguration) invocation.getArguments()[0]);
			}
		});

		when(configurationRepo.reset(any(ManagedConfiguration.class))).then(new Answer<Configuration>() {
			@Override
			public ManagedConfiguration answer(InvocationOnMock invocation) throws Throwable {
				Configuration defaultConfiguration = new ManagedConfiguration("defaultConfigurationName",
						"defaultConfigurationValue", "defaultConfigurationType");
				return (ManagedConfiguration) defaultConfiguration;
			}
		});

		// ControlledVocabulary

		when(controlledVocabularyRepo.findAllByOrderByPositionAsc()).thenReturn(mockControlledVocabularyList);

		when(controlledVocabularyRepo.create(any(String.class), any(Language.class))).then(new Answer<ControlledVocabulary>() {
					@Override
					public ControlledVocabulary answer(InvocationOnMock invocation) throws Throwable {
						return TEST_CONTROLLED_VOCABULARY_1;
					}
				});

		when(controlledVocabularyRepo.update(any(ControlledVocabulary.class))).then(new Answer<ControlledVocabulary>() {
			@Override
			public ControlledVocabulary answer(InvocationOnMock invocation) throws Throwable {
				return updateControlledVocabulary((ControlledVocabulary) invocation.getArguments()[0]);
			}
		});

		when(controlledVocabularyRepo.findByName(any(String.class))).then(new Answer<ControlledVocabulary>() {
			@Override
			public ControlledVocabulary answer(InvocationOnMock invocation) throws Throwable {
				return findControlledVocabularyByName((String) invocation.getArguments()[0]);
			}
		});

		when(controlledVocabularyCachingService.doesControlledVocabularyExist(any(String.class)))
				.then(new Answer<Boolean>() {

					@Override
					public Boolean answer(InvocationOnMock invocation) throws Throwable {
						return true;
					}
				});

		when(controlledVocabularyCachingService.getControlledVocabularyCache(any(String.class)))
				.then(new Answer<ControlledVocabularyCache>() {
					@Override
					public ControlledVocabularyCache answer(InvocationOnMock invocation) throws Throwable {
						return TEST_CONTROLLED_VOCABULARY_CACHE;
					}
				});

		when(controlledVocabularyRepo.findOne(any(Long.class))).then(new Answer<ControlledVocabulary>() {
			@Override
			public ControlledVocabulary answer(InvocationOnMock invocation) throws Throwable {
				return findControlledVocabularyById((Long) invocation.getArguments()[0]);
			}
		});

		when(vocabularyWordRepo.create(any(ControlledVocabulary.class), any(String.class), any(String.class),
				any(String.class), any(List.class))).then(new Answer<VocabularyWord>() {
					@Override
					public VocabularyWord answer(InvocationOnMock invocation) throws Throwable {
						return TEST_VOCABULARY_WORD1;
					}
				});

		when(vocabularyWordRepo.findOne(any(Long.class))).then(new Answer<VocabularyWord>() {
			@Override
			public VocabularyWord answer(InvocationOnMock invocation) throws Throwable {
				return findVocabularyWordById((Long) invocation.getArguments()[0]);
			}
		});

		when(vocabularyWordRepo.update(any(VocabularyWord.class))).then(new Answer<VocabularyWord>() {
			@Override
			public VocabularyWord answer(InvocationOnMock invocation) throws Throwable {
				return updateVocabularyWord((VocabularyWord) invocation.getArguments()[0]);
			}
		});

		// Custom Settings

		when(customActionDefinitionRepo.findAllByOrderByPositionAsc()).thenReturn(mockCustomActionDefList);

		when(customActionDefinitionRepo.create(any(String.class), any(Boolean.class)))
				.then(new Answer<CustomActionDefinition>() {
					@Override
					public CustomActionDefinition answer(InvocationOnMock invocation) throws Throwable {
						return createCustomActionDefinition((String) invocation.getArguments()[0],
								(Boolean) invocation.getArguments()[1]);
					}
				});

		when(customActionDefinitionRepo.update(any(CustomActionDefinition.class)))
				.then(new Answer<CustomActionDefinition>() {
					@Override
					public CustomActionDefinition answer(InvocationOnMock invocation) throws Throwable {
						return updateCustomActionDefinition((CustomActionDefinition) invocation.getArguments()[0]);
					}
				});

		//Degree

		when(degreeRepo.findAllByOrderByPositionAsc()).thenReturn(mockDegreeList);

		when(degreeRepo.create( any(String.class), any(DegreeLevel.class) )).then(new Answer<Degree>() {
			@Override
			public Degree answer(InvocationOnMock invocation) throws Throwable {
				return createDegree( (String)invocation.getArguments()[0], (DegreeLevel) invocation.getArguments()[1]);
			}
		});

		when(degreeRepo.update( any(Degree.class) )).then(new Answer<Degree>() {
			@Override
			public Degree answer(InvocationOnMock invocation) throws Throwable {
				return updateDegree((Degree) invocation.getArguments()[0] );
			}
		});

		when(proquestCodesService.getCodes(any(String.class) )).then(new Answer<Map<String,String>>() {
			@Override
			public Map<String, String> answer(InvocationOnMock invocation) throws Throwable {
				return getProquestLanguageCodes();
			}
		});

		// DocumentType

		when(documentTypeRepo.findAllByOrderByPositionAsc()).thenReturn(mockDocumentTypeList);

		when(documentTypeRepo.create(any(String.class))).then(new Answer<DocumentType>() {
			@Override
			public DocumentType answer(InvocationOnMock invocation) throws Throwable {
				mockDocumentTypeList.add(TEST_DOCUMENT_TYPE3);
				return createDocumentType((String) invocation.getArguments()[0]);
			}
		});

		when(documentTypeRepo.update(any(DocumentType.class))).then(new Answer<DocumentType>() {
			@Override
			public DocumentType answer(InvocationOnMock invocation) throws Throwable {
				return updateDocumentType((DocumentType) invocation.getArguments()[0]);
			}
		});

		// Email Template
		when(emailTemplateRepo.findByNameAndSystemRequired( any(String.class), any(Boolean.class) )).then( new Answer<EmailTemplate>() {
			@Override
			public EmailTemplate answer(InvocationOnMock invocation) throws Throwable {
				return findEmailTemplateByNameAndSystemRequired( (String)invocation.getArguments()[0], (Boolean) invocation.getArguments()[1]);
			}
		});

		when(emailTemplateRepo.findAllByOrderByPositionAsc()).thenReturn(mockEmailTemplateList);

		/*
		 * when(emailTemplateRepo.findByNameOverride( any(String.class))).then(new
		 * Answer<EmailTemplate>() {
		 * @Override public EmailTemplate answer(InvocationOnMock invocation) throws
		 * Throwable { return findEmailTemplateByNameOverride((String)
		 * invocation.getArguments()[0] ); } });
		 */

		when(emailTemplateRepo.findByNameOverride(REGISTRATION_TEMPLATE)).then(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				EmailTemplate emailTemplate = new EmailTemplate(TEST_EMAIL_TEMPLATE_NAME, TEST_EMAIL_TEMPLATE_SUBJECT,
						TEST_EMAIL_TEMPLATE_MESSAGE);
				emailTemplate.setPosition(emailTemplatePosition++);
				return emailTemplate;
			}
		});

		when(emailTemplateRepo.create(any(String.class), any(String.class), any(String.class)))
				.then(new Answer<EmailTemplate>() {
					@Override
					public EmailTemplate answer(InvocationOnMock invocation) throws Throwable {
						return createEmailTemplate((String) invocation.getArguments()[0],
								(String) invocation.getArguments()[1], (String) invocation.getArguments()[2]);
					}
				});

		when(emailTemplateRepo.update(any(EmailTemplate.class))).then(new Answer<EmailTemplate>() {
			@Override
			public EmailTemplate answer(InvocationOnMock invocation) throws Throwable {
				return updateEmailTemplate((EmailTemplate) invocation.getArguments()[0]);
			}
		});

		// Embargo

		when(embargoRepo.findAllByOrderByGuarantorAscPositionAsc()).thenReturn(mockEmbargoList);

		when(embargoRepo.create(any(String.class), any(String.class), any(Integer.class), any(EmbargoGuarantor.class),
				any(Boolean.class))).then(new Answer<Embargo>() {
					@Override
					public Embargo answer(InvocationOnMock invocation) throws Throwable {
						return createEmabrgo((String) invocation.getArguments()[0],
								(String) invocation.getArguments()[1], (Integer) invocation.getArguments()[2],
								(EmbargoGuarantor) invocation.getArguments()[3],
								(Boolean) invocation.getArguments()[4]);
					}
				});

		when(embargoRepo.update(any(Embargo.class))).then(new Answer<Embargo>() {
			@Override
			public Embargo answer(InvocationOnMock invocation) throws Throwable {
				return updateEmabrgo((Embargo) invocation.getArguments()[0]);
			}
		});

		// FieldGloss

		when(fieldGlossRepo.findAll()).thenReturn(mockFieldGlossList);

		when(languageRepo.findByName(any(String.class))).then(new Answer<Language>() {
			@Override
			public Language answer(InvocationOnMock invocation) throws Throwable {
				return findLanguageByName((String) invocation.getArguments()[0]);
			}
		});

		when(fieldGlossRepo.create(any(String.class), any(Language.class))).then(new Answer<FieldGloss>() {
			@Override
			public FieldGloss answer(InvocationOnMock invocation) throws Throwable {
				return createFieldGloss((String) invocation.getArguments()[0], (Language) invocation.getArguments()[1]);
			}
		});

		when(fieldGlossRepo.update(any(FieldGloss.class))).then(new Answer<FieldGloss>() {

			@Override
			public FieldGloss answer(InvocationOnMock invocation) throws Throwable {
				return updateFieldGloss((FieldGloss) invocation.getArguments()[0]);
			}
		});

		//FieldPredicateController

		when(fieldPredicateRepo.findAll()).thenReturn(mockFieldPredicateList);

		when(fieldPredicateRepo.create( any(String.class), any(Boolean.class) )).then(new Answer<FieldPredicate>() {
			@Override
			public FieldPredicate answer(InvocationOnMock invocation) throws Throwable {
				return createFieldPredicate( (String) invocation.getArguments()[0], (Boolean) invocation.getArguments()[1]);
			}
		});

		when(fieldPredicateRepo.findByValue( any(String.class))).then(new Answer<FieldPredicate>() {
			@Override
			public FieldPredicate answer(InvocationOnMock invocation) throws Throwable {
				return findFieldPredicateByValue( (String) invocation.getArguments()[0]);
			}
		});

		when(fieldPredicateRepo.update(any(FieldPredicate.class))).then(new Answer<FieldPredicate>() {
			@Override
			public FieldPredicate answer(InvocationOnMock invocation) throws Throwable {
				return updateFieldPredicate( (FieldPredicate) invocation.getArguments()[0]);
			}
		});

		//FieldProfile

		when(fieldProfileRepo.findAll()).thenReturn(mockFieldProfileList);

		when(fieldValueRepo.findOne( any(Long.class) )).then( new Answer<FieldValue>() {
			@Override
			public FieldValue answer(InvocationOnMock invocation) throws Throwable {
				return getFieldValueById( (Long)invocation.getArguments()[0]);
			}
		});

		when( fieldProfileRepo.create(any(WorkflowStep.class) , any(FieldPredicate.class) , any(InputType.class) ,  any(String.class), any(String.class), any(Boolean.class) , any(Boolean.class) , any(Boolean.class) , any(Boolean.class) , any(Boolean.class) , any(Boolean.class) , any(List.class) , any(List.class) ,any(String.class))).then( new Answer<FieldProfile>() {
			@Override
			public FieldProfile answer(InvocationOnMock invocation) throws Throwable {
				return createFieldProfile((WorkflowStep) invocation.getArguments()[0], (FieldPredicate) invocation.getArguments()[1] , (InputType) invocation.getArguments()[2],  (String) invocation.getArguments()[3], (String) invocation.getArguments()[4], (Boolean) invocation.getArguments()[5], (Boolean) invocation.getArguments()[6] , (Boolean) invocation.getArguments()[7] , (Boolean) invocation.getArguments()[8], (Boolean) invocation.getArguments()[9] , (Boolean) invocation.getArguments()[10], (List<ControlledVocabulary>) invocation.getArguments()[11], (List<FieldGloss>) invocation.getArguments()[12] ,(String) invocation.getArguments()[13]);
			}
		});

		when(fieldProfileRepo.findOne(any(Long.class))).then( new Answer<FieldProfile>() {
			@Override
			public FieldProfile answer(InvocationOnMock invocation) throws Throwable {
				return findFieldProfileById( (Long)invocation.getArguments()[0]);
			}
		});

		when(fieldProfileRepo.update(any(FieldProfile.class), any(Organization.class)) ).then( new Answer<FieldProfile>() {
			@Override
			public FieldProfile answer(InvocationOnMock invocation) throws Throwable {
				return updateFieldProfile( (FieldProfile) invocation.getArguments()[0] , (Organization) invocation.getArguments()[1]);
			}
		});

		when(graduationMonthRepo.findAllByOrderByPositionAsc()).thenReturn(mockGraduationMonthList);

		when(graduationMonthRepo.create( any(Integer.class))).then(new Answer<GraduationMonth>() {
			@Override
			public GraduationMonth answer(InvocationOnMock invocation) throws Throwable {
				return createGraduationMonth((Integer) invocation.getArguments()[0]);
			}
		});

		when( graduationMonthRepo.update(any(GraduationMonth.class) ) ).then(new Answer<GraduationMonth>() {
			@Override
			public GraduationMonth answer(InvocationOnMock invocation) throws Throwable {
				return updateGraduationMonth( (GraduationMonth) invocation.getArguments()[0] );
			}
		});

		when(inputTypeRepo.findAll()).thenReturn(mockInputTypeList);

		when(inputTypeRepo.findByName( any(String.class) )).then( new Answer<InputType>() {
			@Override
			public InputType answer(InvocationOnMock invocation) throws Throwable {
				return findInputTypeByName( (String)invocation.getArguments()[0]);
			}
		});

		//Language

		when(languageRepo.findAllByOrderByPositionAsc()).thenReturn(mockLanguageList);

		when( languageRepo.create( any(String.class)) ).then(new Answer<Language>() {
			@Override
			public Language answer(InvocationOnMock invocation) throws Throwable {
				return createLanguage( (String) invocation.getArguments()[0]);
			}
		});

		when( languageRepo.update(any(Language.class)) ).then(new Answer<Language>() {
			@Override
			public Language answer(InvocationOnMock invocation) throws Throwable {
				return updateLanguage( (Language) invocation.getArguments()[0]);
			}
		});

		when(noteRepo.findAll()).thenReturn(mockNoteList);

		when(noteRepo.findOne( any(Long.class)) ).then( new Answer<Note>() {
			@Override
			public Note answer(InvocationOnMock invocation) throws Throwable {
				return findNoteById( (Long)invocation.getArguments()[0]);
			}
		});

		when(organizationRepo.findAllByOrderByIdAsc()).thenReturn(mockOrganizationList);

		when(organizationRepo.read(any(Long.class))).then( new Answer<Organization>() {
			@Override
			public Organization answer(InvocationOnMock invocation) throws Throwable {
				return getOrganizationById( (Long) invocation.getArguments()[0]);
			}
		});

		when(organizationRepo.create( any(String.class) , any(Organization.class),  any(OrganizationCategory.class) )).then(new Answer<Organization>() {
			@Override
			public Organization answer(InvocationOnMock invocation) throws Throwable {
				return createOrganization( (String) invocation.getArguments()[0] , (Organization) invocation.getArguments()[1] , (OrganizationCategory) invocation.getArguments()[2]);
			}
		});

		when(organizationRepo.findOne( any(Long.class))).then( new Answer<Organization>() {
			@Override
			public Organization answer(InvocationOnMock invocation) throws Throwable {
				return getOrganizationById( (Long) invocation.getArguments()[0]);
			}
		});

		when( organizationRepo.update(any(Organization.class)) ).then(new Answer<Organization>() {
			@Override
			public Organization answer(InvocationOnMock invocation) throws Throwable {
				return updateOrganization( (Organization) invocation.getArguments()[0]);
			}
		});

		when(organizationRepo.restoreDefaults( any(Organization.class))).then( new Answer<Organization>() {
			@Override
			public Organization answer(InvocationOnMock invocation) throws Throwable {
				return getDefaultOrganization( (Organization)invocation.getArguments()[0]);
			}
		});

		when(organizationCategoryRepo.findAll()).thenReturn(mockOrganizationCategoryList);

		when( organizationCategoryRepo.create( any(String.class)) ).then(new Answer<OrganizationCategory>() {
			@Override
			public OrganizationCategory answer(InvocationOnMock invocation) throws Throwable {
				return createOrganizationCategory( (String) invocation.getArguments()[0]);
			}
		});

		when( organizationCategoryRepo.update(any(OrganizationCategory.class)) ).then(new Answer<OrganizationCategory>() {
			@Override
			public OrganizationCategory answer(InvocationOnMock invocation) throws Throwable {
				return updateOrganizationCategory( (OrganizationCategory) invocation.getArguments()[0]);
			}
		});

		when(submissionFieldProfileRepo.getOne( any(Long.class))).then( new Answer<SubmissionFieldProfile>() {
			@Override
			public SubmissionFieldProfile answer(InvocationOnMock invocation) throws Throwable {
				return getSubmissionFieldProfileById( (Long)invocation.getArguments()[0]);
			}
		});

		when(submissionRepo.create( any(User.class), any(Organization.class), any(SubmissionStatus.class), any(Credentials.class))).then(new Answer<Submission>() {
			@Override
			public Submission answer(InvocationOnMock invocation) throws Throwable {
				//TODO
				return  createSubmission((User) invocation.getArguments()[0], (Organization) invocation.getArguments()[1], (SubmissionStatus)invocation.getArguments()[2]);
			}
		});

		when(submissionRepo.findAll()).thenReturn(mockSubmissionList);

		when(submissionRepo.findAllBySubmitter( any(User.class) )).thenReturn(mockSubmissionList);

		when(submissionRepo.findOneByAdvisorAccessHash( any(String.class) )).then(new Answer<Submission>() {
			@Override
			public Submission answer(InvocationOnMock invocation) throws Throwable {
				return findSubmissionByAdvisorAccessHash((String)invocation.getArguments()[0]) ;
			}
		});

		when(submissionRepo.read( any(Long.class))).then( new Answer<Submission>() {
			@Override
			public Submission answer(InvocationOnMock invocation) throws Throwable {
				return getSubmissionById( (Long) invocation.getArguments()[0]);
			}
		});

		when(submissionRepo.findOneBySubmitterAndId( any(User.class), any(Long.class))).then(new Answer<Submission>() {
			@Override
			public Submission answer(InvocationOnMock invocation) throws Throwable {
				return getSubmissionBySubmitterAndId( (User) invocation.getArguments()[0] , (Long) invocation.getArguments()[1]);
			}
		});

		when( submissionRepo.save( any(Submission.class))).then(new Answer<Submission>() {
			@Override
			public Submission answer(InvocationOnMock invocation) throws Throwable {
				return saveSubmission( (Submission) invocation.getArguments()[0]);
			}
		});

		when( submissionRepo.update( any(Submission.class))).then(new Answer<Submission>() {
			@Override
			public Submission answer(InvocationOnMock invocation) throws Throwable {
				return updateSubmission( (Submission) invocation.getArguments()[0]);
			}
		});

		when( submissionRepo.updateStatus( any(Submission.class) , any(SubmissionStatus.class), any(User.class) ) ).then(new Answer<Submission>() {
			@Override
			public Submission answer(InvocationOnMock invocation) throws Throwable {
				return updateSubmissionStatus( (Submission) invocation.getArguments()[0] , (SubmissionStatus) invocation.getArguments()[1] , (User) invocation.getArguments()[2]);
			}
		});

		when(actionLogRepo.createPublicLog(any(Submission.class), any(User.class), any(String.class) )).then(new Answer<ActionLog>() {
			@Override
			public ActionLog answer(InvocationOnMock invocation) throws Throwable {
				return createPublicSubmissionActionLog((Submission)invocation.getArguments()[0], (User)invocation.getArguments()[1], (String)invocation.getArguments()[2]);
			}
		});

		when(actionLogRepo.createPrivateLog(any(Submission.class), any(User.class), any(String.class) )).then(new Answer<ActionLog>() {
			@Override
			public ActionLog answer(InvocationOnMock invocation) throws Throwable {
				return createPrivateSubmissionActionLog((Submission)invocation.getArguments()[0], (User)invocation.getArguments()[1], (String)invocation.getArguments()[2]);
			}
		});

		when(submissionStatusRepo.findAll()).thenReturn(mockSubmissionStatusList);

		when(submissionStatusRepo.findByName( any(String.class))).then( new Answer<SubmissionStatus>() {
			@Override
			public SubmissionStatus answer(InvocationOnMock invocation) throws Throwable {
				return getSubmissionStatusByName( (String)invocation.getArguments()[0]);
			}
		});

		when(submissionStatusRepo.findOne(any(Long.class))).then( new Answer<SubmissionStatus>() {
			@Override
			public SubmissionStatus answer(InvocationOnMock invocation) throws Throwable {
				return getSubmissionStatusById( (Long)invocation.getArguments()[0]);
			}
		});

		when(userRepo.findAll()).thenReturn(mockUsers);

		when(userRepo.findAllByRole( any(Role.class))).then(new Answer<List<User>>() {
			@Override
			public List<User> answer(InvocationOnMock invocation) throws Throwable {
				return findUsersByRole((Role) invocation.getArguments()[0]);
			}
		});

		when(userRepo.findByEmail(any(String.class))).then(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return findByEmail((String) invocation.getArguments()[0]);
			}
		});

		when(userRepo.findOne( any(Long.class))).then( new Answer<User>() {
			@Override
			public User answer(InvocationOnMock invocation) throws Throwable {
				return findUserById( (Long) invocation.getArguments()[0]);
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

		when(workflowStepRepo.findAll()).thenReturn(mockWorkflowStepList);

		when(workflowStepRepo.create( any(String.class), any(Organization.class) )).then( new Answer<WorkflowStep>() {
			@Override
			public WorkflowStep answer(InvocationOnMock invocation) throws Throwable {
				return createWorkflowStepForOrganization( (String)invocation.getArguments()[0] , (Organization)invocation.getArguments()[1]);
			}
		});

		when(workflowStepRepo.findOne( any(Long.class) )).then( new Answer<WorkflowStep>() {
			@Override
			public WorkflowStep answer(InvocationOnMock invocation) throws Throwable {
				return getWorkflowStepById( (Long) invocation.getArguments()[0]);
			}
		});

		when(workflowStepRepo.update( any(WorkflowStep.class), any(Organization.class) )).then( new Answer<WorkflowStep>() {
			@Override
			public WorkflowStep answer(InvocationOnMock invocation) throws Throwable {
				return updateWorkFlowStepByWorkflowStepAndReqOrganization( (WorkflowStep) invocation.getArguments()[0] , (Organization) invocation.getArguments()[1] );
			}
		});
	}

	@After
	public void cleanUp() {
		response = null;
		abstractEmailRecipientRepo.deleteAll();
		actionLogRepo.deleteAll();
		configurationRepo.deleteAll();
		controlledVocabularyRepo.deleteAll();
		customActionDefinitionRepo.deleteAll();
		degreeRepo.deleteAll();
		depositLocationRepo.deleteAll();
		degreeRepo.deleteAll();
		documentTypeRepo.deleteAll();
		emailTemplateRepo.deleteAll();
		emailWorkflowRuleRepo.deleteAll();
		embargoRepo.deleteAll();
		fieldGlossRepo.deleteAll();
		fieldPredicateRepo.deleteAll();
		fieldProfileRepo.deleteAll();
		fieldValueRepo.deleteAll();
		graduationMonthRepo.deleteAll();
		inputTypeRepo.deleteAll();
		languageRepo.deleteAll();
		namedSearchFilterGroupRepo.deleteAll();
		noteRepo.deleteAll();
		organizationCategoryRepo.deleteAll();
		organizationRepo.deleteAll();
		submissionFieldProfileRepo.deleteAll();
		submissionRepo.deleteAll();
		submissionStatusRepo.deleteAll();
		userRepo.deleteAll();
		workflowStepRepo.deleteAll();
	}

}
