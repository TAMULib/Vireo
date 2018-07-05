package org.tdl.vireo.mock;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

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
import org.tdl.vireo.model.InputType;
import org.tdl.vireo.model.Language;
import org.tdl.vireo.model.ManagedConfiguration;
import org.tdl.vireo.model.Organization;
import org.tdl.vireo.model.OrganizationCategory;
import org.tdl.vireo.model.Role;
import org.tdl.vireo.model.User;
import org.tdl.vireo.model.VocabularyWord;
import org.tdl.vireo.model.WorkflowStep;

import edu.emory.mathcs.backport.java.util.Arrays;

@SuppressWarnings("unchecked")
public abstract class MockData {

	protected Configuration TEST_CONFIGURATION_SETTING1 = new ManagedConfiguration("name1", "value1", "type1");
	protected Configuration TEST_CONFIGURATION_SETTING2 = new ManagedConfiguration("name2", "value2", "type2");

	protected static CustomActionDefinition TEST_CUSTOM_ACTION_DEF1 = new CustomActionDefinition(
			"Custom Action Label 1", false);
	protected static CustomActionDefinition TEST_CUSTOM_ACTION_DEF2 = new CustomActionDefinition(
			"Custom Action Label 2", false);

	protected static DegreeLevel TEST_DEGREE_LEVEL = new DegreeLevel("Degree Level name");
	protected static Degree TEST_DEGREE1 = new Degree("A Degree Name", TEST_DEGREE_LEVEL, "ProquestCode for Degree1 ");
	protected static Degree TEST_DEGREE2 = new Degree("Second Degree Name", TEST_DEGREE_LEVEL,
			"ProquestCode for Second Degree ");

	protected static Language TEST_LANGUAGE1 = new Language("A Language");
	protected static Language TEST_LANGUAGE2 = new Language("Another Language");

	protected static FieldPredicate TEST_FIELD_PREDICATE = new FieldPredicate("FieldPredicate Value", true);
	protected static FieldPredicate TEST_FIELD_PREDICATE1 = new FieldPredicate("FieldPredicate1_Value", false);
	protected static FieldPredicate TEST_FIELD_PREDICATE2 = new FieldPredicate("FieldPredicate2_Value", true);

	protected static DocumentType TEST_DOCUMENT_TYPE1 = new DocumentType("DocumentTypeName1", TEST_FIELD_PREDICATE);
	protected static DocumentType TEST_DOCUMENT_TYPE2 = new DocumentType("DocumentTypeName2", TEST_FIELD_PREDICATE);
	protected static DocumentType TEST_DOCUMENT_TYPE3 = new DocumentType("DocumentTypeName3", TEST_FIELD_PREDICATE);

	protected static EmailTemplate TEST_EMAIL_TEMPLATE1 = new EmailTemplate("EmailTemplate1_Name",
			"EmailTemplate1_Subject", "EmailTemplate1_Message");
	protected static EmailTemplate TEST_EMAIL_TEMPLATE2 = new EmailTemplate("EmailTemplate2_Name",
			"EmailTemplate2_Subject", "EmailTemplate2_Message");
	protected static EmailTemplate TEST_EMAIL_TEMPLATE3 = new EmailTemplate("EmailTemplate3_Name",
			"EmailTemplate3_Subject", "EmailTemplate3_Message");

	protected static Embargo TEST_EMBARGO1 = new Embargo("Embargo1_name", "Embargo1_description", new Integer(10),
			EmbargoGuarantor.PROQUEST, true);
	protected static Embargo TEST_EMBARGO2 = new Embargo("Embargo2_name", "Embargo2_description", new Integer(20),
			EmbargoGuarantor.DEFAULT, false);

	protected static FieldGloss TEST_FIELD_GLOSS1 = new FieldGloss("FieldGloss1_Value", TEST_LANGUAGE1);
	protected static FieldGloss TEST_FIELD_GLOSS2 = new FieldGloss("FieldGloss2_Value", TEST_LANGUAGE2);

	protected static FieldProfile TEST_FIELD_PROFILE1 = new FieldProfile();

	protected static ControlledVocabulary TEST_CONTROLLED_VOCABULARY_1 = new ControlledVocabulary(
			"Controlled Vocabulary Name1", TEST_LANGUAGE1, false);
	protected static ControlledVocabulary TEST_CONTROLLED_VOCABULARY_2 = new ControlledVocabulary(
			"Controlled Vocabulary Name2", TEST_LANGUAGE2, false);

	protected static ControlledVocabularyCache TEST_CONTROLLED_VOCABULARY_CACHE = new ControlledVocabularyCache(1l,
			TEST_CONTROLLED_VOCABULARY_1.getName());
	protected static List<ControlledVocabulary> mockControlledVocabularyList = new ArrayList<ControlledVocabulary>(
			Arrays.asList(new ControlledVocabulary[] { TEST_CONTROLLED_VOCABULARY_1, TEST_CONTROLLED_VOCABULARY_2 }));

	protected static List<String> mockVocabularyWordContactList = Arrays
			.asList(new String[] { "VocabWordContact1", "VocabWordContact2" });

	protected static OrganizationCategory TEST_ORGANIZATION_CATEGORY1 = new OrganizationCategory(
			"OrganizationCategory1_name");

	protected static Organization TEST_ORGANIZATION1 = new Organization("Organization1_name",
			TEST_ORGANIZATION_CATEGORY1);

	protected static WorkflowStep TEST_WORKFLOW1 = new WorkflowStep("WorkFlow name 1", TEST_ORGANIZATION1);

	protected static InputType TEST_INPUT_TYPE1 = new InputType("Input Type 1");

	protected static FieldProfile TEST_FILED_PROFILE1 = new FieldProfile(TEST_WORKFLOW1, TEST_FIELD_PREDICATE1,
			TEST_INPUT_TYPE1, true, false, true, false, true, true, "defaultValue");

	protected static VocabularyWord TEST_VOCABULARY_WORD1 = new VocabularyWord("Vocabulary Word Name1",
			"Vocabulary Word Definition1", "Vocabulary Word Identifier1", mockVocabularyWordContactList);
	protected static VocabularyWord TEST_VOCABULARY_WORD2 = new VocabularyWord("Vocabulary Word Name2",
			"Vocabulary Word Definition2", "Vocabulary Word Identifier2", mockVocabularyWordContactList);

	protected static List<CustomActionDefinition> mockCustomActionDefList = new ArrayList<>(
			Arrays.asList(new CustomActionDefinition[] { TEST_CUSTOM_ACTION_DEF1, TEST_CUSTOM_ACTION_DEF2 }));
	protected static List<VocabularyWord> mockVocabularyWordList = Arrays
			.asList(new VocabularyWord[] { TEST_VOCABULARY_WORD1, TEST_VOCABULARY_WORD2 });

	protected static List<Degree> mockDegreeList = new ArrayList<>(
			Arrays.asList(new Degree[] { TEST_DEGREE1, TEST_DEGREE2 }));

	protected static List<DocumentType> mockDocumentTypeList = new ArrayList<>(
			Arrays.asList(new DocumentType[] { TEST_DOCUMENT_TYPE1, TEST_DOCUMENT_TYPE2 }));

	protected static List<EmailTemplate> mockEmailTemplateList = new ArrayList<>(
			Arrays.asList(new EmailTemplate[] { TEST_EMAIL_TEMPLATE1, TEST_EMAIL_TEMPLATE2, TEST_EMAIL_TEMPLATE3 }));

	protected static List<Embargo> mockEmbargoList = new ArrayList<>(
			Arrays.asList(new Embargo[] { TEST_EMBARGO1, TEST_EMBARGO2 }));

	protected static List<FieldGloss> mockFieldGlossList = new ArrayList<>(
			Arrays.asList(new FieldGloss[] { TEST_FIELD_GLOSS1, TEST_FIELD_GLOSS2 }));

	protected static List<Language> mockLanguageList = Arrays.asList(new Language[] { TEST_LANGUAGE1, TEST_LANGUAGE2 });

	protected static List<FieldPredicate> mockFieldPredicateList = new ArrayList<>(
			Arrays.asList(new FieldPredicate[] { TEST_FIELD_PREDICATE, TEST_FIELD_PREDICATE1, TEST_FIELD_PREDICATE2 }));

	protected static List<FieldProfile> mockFieldProfileList = new ArrayList<>(
			Arrays.asList(new FieldProfile[] { TEST_FILED_PROFILE1 }));

	static {
		TEST_FIELD_PREDICATE.setId(1l);
		TEST_FIELD_PREDICATE1.setId(2l);
		TEST_FIELD_PREDICATE2.setId(3l);
		TEST_LANGUAGE1.setId(1l);
		TEST_LANGUAGE2.setId(2l);
		TEST_CONTROLLED_VOCABULARY_1.setId(1l);
		TEST_CONTROLLED_VOCABULARY_1.setDictionary(mockVocabularyWordList);
		TEST_CONTROLLED_VOCABULARY_2.setId(2l);
		TEST_CONTROLLED_VOCABULARY_2.setDictionary(mockVocabularyWordList);
		TEST_CUSTOM_ACTION_DEF1.setId(1l);
		TEST_CUSTOM_ACTION_DEF2.setId(2l);

		TEST_DEGREE1.setId(1l);
		TEST_DEGREE1.setPosition(1l);
		TEST_DEGREE2.setId(2l);
		TEST_DEGREE2.setPosition(2l);

		TEST_DOCUMENT_TYPE1.setId(1l);
		TEST_DOCUMENT_TYPE2.setId(2l);
		TEST_DOCUMENT_TYPE3.setId(3l);

		TEST_EMAIL_TEMPLATE1.setId(1l);
		TEST_EMAIL_TEMPLATE2.setId(2l);
		TEST_EMAIL_TEMPLATE3.setId(3l);

		TEST_EMBARGO1.setId(1l);
		TEST_EMBARGO2.setId(2l);

		TEST_FIELD_GLOSS1.setId(1l);
		TEST_FIELD_GLOSS2.setId(2l);

		TEST_FILED_PROFILE1.setId(1l);

		TEST_VOCABULARY_WORD1.setId(1l);
		TEST_VOCABULARY_WORD1.setControlledVocabulary(TEST_CONTROLLED_VOCABULARY_1);
		TEST_VOCABULARY_WORD2.setId(2l);
	}

	protected static final String SECRET_PROPERTY_NAME = "secret";
	protected static final String SECRET_VALUE = "verysecretsecret";

	protected static final String AUTH_SECRET_KEY_PROPERTY_NAME = "secret";
	protected static final String AUTH_SECRET_KEY_VALUE = "verysecretsecret";

	protected static final String AUTH_ISSUER_KEY_PROPERTY_NAME = "issuer";
	protected static final String AUTH_ISSUER_KEY_VALUE = "localhost";

	protected static final String AUTH_DURATION_PROPERTY_NAME = "duration";
	protected static final int AUTH_DURATION_VALUE = 2;

	protected static final String AUTH_KEY_PROPERTY_NAME = "key";
	protected static final Key AUTH_KEY_VALUE = new SecretKeySpec(SECRET_VALUE.getBytes(), "AES");

	protected static final String SHIB_KEYS_PROPERTY_NAME = "shibKeys";
	protected static final String[] SHIB_KEYS = new String[] { "netid", "uin", "lastName", "firstName", "email" };

	protected static final String SHIB_SUBJECT_PROPERTY_NAME = "email";

	protected static final String EMAIL_HOST_PROPERTY_NAME = "host";
	protected static final String EMAIL_HOST_VALUE = "relay.tamu.edu";

	protected static final String HTTP_DEFAULT_TIMEOUT_NAME = "DEFAULT_TIMEOUT";
	protected static final int HTTP_DEFAULT_TIMEOUT_VALUE = 10000;

	protected static Long emailTemplatePosition = 0L;
	protected static final String REGISTRATION_TEMPLATE = "SYSTEM New User Registration";

	protected final static String EMAIL_VERIFICATION_TYPE = "EMAIL_VERIFICATION";

	protected final static String TEST_EMAIL = "test@email.com";

	protected final static String TEST_USER_EMAIL = "testUser@email.com";
	protected final static String TEST_USER_FIRST_NAME = "Test";
	protected final static String TEST_USER_LAST_NAME = "User";
	protected final static String TEST_USER_PASSWORD = "abc123";
	protected final static String TEST_USER_CONFIRM = "abc123";
	protected final static Role TEST_USER_ROLE = Role.ROLE_STUDENT;
	protected final static Role TEST_USER_ROLE_UPDATE = Role.ROLE_ADMIN;

	protected final static String TEST_REGISTRATION_EMAIL_TEMPLATE_NAME = "SYSTEM New User Registration";
	protected final static String TEST_EMAIL_TEMPLATE_NAME = "Test Email Template Name";
	protected final static String TEST_EMAIL_TEMPLATE_SUBJECT = "Test Email Template Subject";
	protected final static String TEST_EMAIL_TEMPLATE_MESSAGE = "Test Email Template Message";

	protected final static String TEST_LANGUAGE_NAME1 = "English";
	protected final static String TEST_LANGUAGE_NAME2 = "Spanish";
	protected final static String TEST_LANGUAGE_NAME3 = "French";

	protected final static String TEST_CONTROLLED_VOCABULARY_NAME1 = "CCVTest1";
	protected final static String TEST_CONTROLLED_VOCABULARY_NAME2 = "BCVTest2";
	protected final static String TEST_CONTROLLED_VOCABULARY_NAME3 = "ACVTest3";

	protected final static String TEST_VOCABULARY_WORD_NAME1 = "Hello";
	protected final static String TEST_VOCABULARY_WORD_NAME2 = "World";
	protected final static String TEST_VOCABULARY_WORD_NAME3 = "TAMU";

	protected final static String TEST_VOCABULARY_WORD_DEFINITION1 = "A greeting.";
	protected final static String TEST_VOCABULARY_WORD_DEFINITION2 = "The earth.";
	protected final static String TEST_VOCABULARY_WORD_DEFINITION3 = "Awesome!";

	protected final static String TEST_VOCABULARY_WORD_IDENTIFIER1 = "http://google.com/Hello";
	protected final static String TEST_VOCABULARY_WORD_IDENTIFIER2 = "http://nasa.gov";
	protected final static String TEST_VOCABULARY_WORD_IDENTIFIER3 = "http://library.tamu.edu";

	protected final static String TEST_USER2_EMAIL = "aggieJack@tamu.edu";
	protected final static String TEST_USER3_EMAIL = "aggieJill@tamu.edu";
	protected final static String TEST_USER4_EMAIL = "jimInny@tdl.org";

	protected static User TEST_USER2 = new User(TEST_USER2_EMAIL, "Jack", "Daniels", Role.ROLE_ADMIN);
	protected static User TEST_USER3 = new User(TEST_USER3_EMAIL, "Jill", "Daniels", Role.ROLE_MANAGER);
	protected static User TEST_USER4 = new User(TEST_USER4_EMAIL, "Jim", "Inny", Role.ROLE_STUDENT);

	protected static User TEST_USER = new User(TEST_USER_EMAIL, TEST_USER_FIRST_NAME, TEST_USER_LAST_NAME,
			Role.ROLE_STUDENT);

	protected static List<User> mockUsers = Arrays.asList(new User[] { TEST_USER, TEST_USER2, TEST_USER3, TEST_USER4 });

	protected static List<Configuration> mockConfigurationSettings;

	public Configuration saveConfiguration(ManagedConfiguration modifiedConfiguration) {
		ManagedConfiguration managedConfiguration = null;
		if (modifiedConfiguration.getName().equals(TEST_CONFIGURATION_SETTING1.getName())) {
			managedConfiguration = (ManagedConfiguration) TEST_CONFIGURATION_SETTING1;
		}
		return managedConfiguration;
	}

	public Language findLanguageByName(String languageName) {
		for (Language language : mockLanguageList) {
			if (language.getName().equals(languageName)) {
				return language;
			}
		}
		return null;
	}

	public ControlledVocabulary findControlledVocabularyById(Long cvObjId) {
		for (ControlledVocabulary cvObj : mockControlledVocabularyList) {
			if (cvObj.getId().equals(cvObjId)) {
				return cvObj;
			}
		}
		return null;
	}

	public ControlledVocabulary findControlledVocabularyByName(String controlledVocabularyName) {
		for (ControlledVocabulary cvObj : mockControlledVocabularyList) {
			if (cvObj.getName().equals(controlledVocabularyName)) {
				return cvObj;
			}
		}
		return null;
	}

	public ControlledVocabulary updateControlledVocabulary(ControlledVocabulary modifiedControlledVocabulary) {
		for (ControlledVocabulary cvObj : mockControlledVocabularyList) {
			if (cvObj.getName().equals(modifiedControlledVocabulary.getName())) {
				return modifiedControlledVocabulary;
			}
		}
		return null;
	}

	public VocabularyWord findVocabularyWordById(Long vwId) {
		for (VocabularyWord vw : mockVocabularyWordList) {
			if (vwId.equals(vw.getId())) {
				return vw;
			}
		}
		return null;
	}

	public VocabularyWord updateVocabularyWord(VocabularyWord modifiedVocabularyWord) {
		for (VocabularyWord vwObj : mockVocabularyWordList) {
			if (modifiedVocabularyWord.getId().equals(vwObj.getId())) {
				vwObj.setContacts(modifiedVocabularyWord.getContacts());
				vwObj.setControlledVocabulary(modifiedVocabularyWord.getControlledVocabulary());
				vwObj.setDefinition(modifiedVocabularyWord.getDefinition());
				vwObj.setName(modifiedVocabularyWord.getName());
				vwObj.setIdentifier(modifiedVocabularyWord.getIdentifier());
				return vwObj;
			}
		}
		return null;
	}

	public CustomActionDefinition createCustomActionDefinition(String label, Boolean isStudentVisible) {
		return new CustomActionDefinition(label, isStudentVisible);
	}

	public CustomActionDefinition updateCustomActionDefinition(CustomActionDefinition modifiedCustomActionDefinition) {
		CustomActionDefinition cadObj = null;
		for (CustomActionDefinition customActionDefinition : mockCustomActionDefList) {
			if (modifiedCustomActionDefinition.getId().equals(customActionDefinition.getId())) {
				customActionDefinition.setLabel(modifiedCustomActionDefinition.getLabel());
				customActionDefinition.isStudentVisible(modifiedCustomActionDefinition.isStudentVisible());
				cadObj = customActionDefinition;
				break;
			}
		}
		return cadObj;
	}

	public Degree createDegree(String name, DegreeLevel level) {
		return new Degree(name, level);
	}

	public Degree updateDegree(Degree modifiedDegree) {
		Degree updatedDegree = null;
		for (Degree degree : mockDegreeList) {
			if (modifiedDegree.getId().equals(degree.getId())) {
				degree.setLevel(modifiedDegree.getLevel());
				degree.setName(modifiedDegree.getName());
				degree.setDegreeCode(modifiedDegree.getDegreeCode());
				updatedDegree = degree;
				break;
			}
		}
		return updatedDegree;
	}

	public Map<String, String> getProquestDegreeCodes(String degreeCode) {
		Map<String, String> degrees = new HashMap<String, String>();
		degrees.put("name", "This is a name");
		degrees.put("level", "This is a degree level");
		degrees.put("code", "This is a degree code");
		return degrees;
	}

	public DocumentType createDocumentType(String documentTypeName) {
		return new DocumentType(documentTypeName);
	}

	public DocumentType updateDocumentType(DocumentType modifiedDocumentType) {
		DocumentType documentType = null;
		for (DocumentType docType : mockDocumentTypeList) {
			if (docType.getId().equals(modifiedDocumentType.getId())) {
				docType.setName(modifiedDocumentType.getName());
				docType.setFieldPredicate(modifiedDocumentType.getFieldPredicate());
				documentType = docType;
				break;
			}
		}
		return documentType;
	}

	public EmailTemplate createEmailTemplate(String name, String subject, String message) {
		return new EmailTemplate(name, subject, message);
	}

	public EmailTemplate updateEmailTemplate(EmailTemplate modifiedEmailTemplate) {
		EmailTemplate emailTemplate = null;
		for (EmailTemplate et : mockEmailTemplateList) {
			if (et.getId().equals(modifiedEmailTemplate.getId())) {
				et.setName(modifiedEmailTemplate.getName());
				et.setName(modifiedEmailTemplate.getName());
				et.setName(modifiedEmailTemplate.getName());
				emailTemplate = et;
				break;
			}
		}
		return emailTemplate;
	}

	public EmailTemplate findEmailTemplateByNameOverride(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public Embargo updateEmabrgo(Embargo modifiedEmbargo) {
		Embargo embargo = null;
		for (Embargo e : mockEmbargoList) {
			if (e.getId().equals(modifiedEmbargo.getId())) {
				e.setName(modifiedEmbargo.getName());
				e.setDescription(modifiedEmbargo.getDescription());
				e.setDuration(modifiedEmbargo.getDuration());
				e.setGuarantor(modifiedEmbargo.getGuarantor());
				e.isActive(modifiedEmbargo.isActive());
				embargo = e;
				break;
			}
		}
		return embargo;
	}

	public Embargo createEmabrgo(String name, String description, Integer duration, EmbargoGuarantor guarantor,
			Boolean isActive) {
		return new Embargo(name, description, duration, guarantor, isActive);
	}

	public FieldGloss createFieldGloss(String value, Language language) {
		return new FieldGloss(value, language);
	}

	public FieldGloss updateFieldGloss(FieldGloss modifiedFieldGloss) {
		FieldGloss fieldGloss = null;
		for (FieldGloss fg : mockFieldGlossList) {
			if (fg.getId().equals(modifiedFieldGloss.getId())) {
				fg.setValue(modifiedFieldGloss.getValue());
				fg.setLanguage(modifiedFieldGloss.getLanguage());
				fieldGloss = fg;
				break;
			}
		}
		return fieldGloss;
	}

	public FieldPredicate createFieldPredicate(String value, Boolean documentTypePredicate) {
		return new FieldPredicate(value, documentTypePredicate);
	}

	public FieldPredicate findFieldPredicateByValue(String fieldPredicateValue) {
		for (FieldPredicate fp : mockFieldPredicateList) {
			if (fp.getValue().equals(fieldPredicateValue)) {
				return fp;
			}
		}
		return null;
	}

	public FieldPredicate updateFieldPredicate(FieldPredicate modifiedFieldPredicate) {
		FieldPredicate fieldPredicate = null;
		for (FieldPredicate fp : mockFieldPredicateList) {
			if (fp.getId().equals(modifiedFieldPredicate.getId())) {
				fp.setValue(modifiedFieldPredicate.getValue());
				fp.setDocumentTypePredicate(modifiedFieldPredicate.getDocumentTypePredicate());
				fieldPredicate = fp;
				break;
			}
		}
		return fieldPredicate;
	}

	public User findByEmail(String email) {
		for (User user : mockUsers) {
			if (user.getEmail().equals(email)) {
				return user;
			}
		}
		return null;
	}

	public User updateUser(User updatedUser) {
		for (User user : mockUsers) {
			if (user.getEmail().equals(updatedUser.getEmail())) {
				user.setEmail(updatedUser.getEmail());
				user.setFirstName(updatedUser.getFirstName());
				user.setLastName(updatedUser.getLastName());
				user.setPassword(updatedUser.getPassword());
				user.setRole(updatedUser.getRole());
				return user;
			}
		}
		return null;
	}
}
