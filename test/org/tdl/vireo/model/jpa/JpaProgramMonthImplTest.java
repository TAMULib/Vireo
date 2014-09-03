package org.tdl.vireo.model.jpa;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tdl.vireo.model.ProgramMonth;
import org.tdl.vireo.model.MockPerson;
import org.tdl.vireo.security.SecurityContext;

import play.db.jpa.JPA;
import play.modules.spring.Spring;
import play.test.UnitTest;

/**
 * Jpa specific implementation of the graduation month interface
 * 
 * @author <a href="http://www.scottphillips.com">Scott Phillips</a>
 */
public class JpaProgramMonthImplTest extends UnitTest {
	
	// Repositories
	public static SecurityContext context = Spring.getBeanOfType(SecurityContext.class);
	public static JpaSettingsRepositoryImpl settingRepo = Spring.getBeanOfType(JpaSettingsRepositoryImpl.class);
	
	@Before
	public void setup() {
		context.login(MockPerson.getAdministrator());
	}
	
	@After
	public void cleanup() {
		JPA.em().clear();
		context.logout();
		
		JPA.em().getTransaction().rollback();
		JPA.em().getTransaction().begin();
	}
	
	/**
	 * Test creating an month
	 */
	@Test
	public void testCreate() {
		
		ProgramMonth month = settingRepo.createProgramMonth(0);
		
		assertNotNull(month);
		assertEquals(0,month.getMonth());
		assertEquals("January", month.getMonthName());
		
		month.delete();
	}
	
	/**
	 * Test creating the program month out of bounds.
	 */
	@Test
	public void testBadCreate() {
		try {
			settingRepo.createProgramMonth(-1);
			fail("Able to create a month out of bounds");
		} catch (IllegalArgumentException iae) {
			/* yay */
		}
		
		try {
			settingRepo.createProgramMonth(12);
			fail("Able to create a month ouf of bounds");
		} catch (IllegalArgumentException iae) {
			/* yay */
		}
	}
	
	/**
	 * Test creating a duplicate month
	 */
	@Test
	public void testCreateDuplicate() {
		
		ProgramMonth month = settingRepo.createProgramMonth(0).save();
		
		try {
			settingRepo.createProgramMonth(0).save();
			fail("Able to create duplicate program month");
		} catch (RuntimeException re) {
			/* yay */
		}
		
		// Recover the transaction after a failure.
		JPA.em().getTransaction().rollback();
		JPA.em().getTransaction().begin();
	}
	
	/**
	 * Test the id.
	 */
	@Test
	public void testId() {
		
		ProgramMonth month = settingRepo.createProgramMonth(0).save();

		assertNotNull(month.getId());
		
		month.delete();
	}
	
	/**
	 * Test retrieval by id.
	 */
	@Test
	public void testFindById() {
		ProgramMonth month = settingRepo.createProgramMonth(0).save();

		
		ProgramMonth retrieved = settingRepo.findProgramMonth(month.getId());
		
		assertEquals(month.getMonth(), retrieved.getMonth());
		
		retrieved.delete();
	}
	
	/**
	 * Test retrieving all months
	 */
	@Test
	public void testFindAllMonths() {

		int initialSize = settingRepo.findAllProgramMonths().size();
		
		ProgramMonth month1 = settingRepo.createProgramMonth(0).save();
		ProgramMonth month2 = settingRepo.createProgramMonth(1).save();

		int postSize = settingRepo.findAllProgramMonths().size();
		
		assertEquals(initialSize +2, postSize);
		
		month1.delete();
		month2.delete();
	}
	
	/**
	 * Test the validation 
	 */
	@Test 
	public void testValidation() {
		ProgramMonth january = settingRepo.createProgramMonth(0).save();
		ProgramMonth test = settingRepo.createProgramMonth(1).save();
		
		try {
			test.setMonth(-1);
			fail("Able to change month out of bounds");
		} catch (IllegalArgumentException iae) {
			/* yay */
		}
		
		try {
			test.setMonth(12);
			fail("Able to change month out of bounds");
		} catch (IllegalArgumentException iae) {
			/* yay */
		}
		
		try {
			test.setMonth(0);
			test.save();
			fail("Able to modify object into duplicate.");
		} catch(RuntimeException re) {
			/* yay */
		}
	
		// Recover the transaction after a failure.
		JPA.em().getTransaction().rollback();
		JPA.em().getTransaction().begin();
	}
	
	/**
	 * Test the display order attribute.
	 */
	@Test
	public void testOrder() {
		ProgramMonth month4 = settingRepo.createProgramMonth(0).save();
		ProgramMonth month1 = settingRepo.createProgramMonth(1).save();
		ProgramMonth month3 = settingRepo.createProgramMonth(2).save();
		ProgramMonth month2 = settingRepo.createProgramMonth(3).save();
		
		month1.setDisplayOrder(0);
		month2.setDisplayOrder(1);
		month3.setDisplayOrder(3);
		month4.setDisplayOrder(4);
		
		month1.save();
		month2.save();
		month3.save();
		month4.save();
		
		List<ProgramMonth> months = settingRepo.findAllProgramMonths();
		
		int index1 = months.indexOf(month1);
		int index2 = months.indexOf(month2);
		int index3 = months.indexOf(month3);
		int index4 = months.indexOf(month4);
		
		assertTrue(index4 > index3);
		assertTrue(index3 > index2);
		assertTrue(index2 > index1);

		month1.delete();
		month2.delete();
		month3.delete();
		month4.delete();
	}
	
	/**
	 * Test that the program month is persisted
	 */
	@Test
	public void testPersistance() {
		// Commit and reopen a new transaction because some of the other tests
		// may have caused exceptions which set the transaction to be rolled
		// back.
		if (JPA.em().getTransaction().getRollbackOnly())
			JPA.em().getTransaction().rollback();
		else
			JPA.em().getTransaction().commit();
		JPA.em().clear();
		JPA.em().getTransaction().begin();
		
		ProgramMonth month = settingRepo.createProgramMonth(0).save();
		
		// Commit and reopen a new transaction.
		JPA.em().getTransaction().commit();
		JPA.em().clear();
		JPA.em().getTransaction().begin();
		
		ProgramMonth retrieved = settingRepo.findProgramMonth(month.getId());
		
		assertEquals(month.getId(),retrieved.getId());
		assertEquals(month.getMonth(),retrieved.getMonth());
		
		retrieved.delete();
		
		// Commit and reopen a new transaction.
		JPA.em().getTransaction().commit();
		JPA.em().clear();
		JPA.em().getTransaction().begin();
	}
	
	/**
	 * Test that managers have access and other don't.
	 */
	@Test
	public void testAccess() {
		
		context.login(MockPerson.getManager());
		settingRepo.createProgramMonth(0).save().delete();
		
		try {
			context.login(MockPerson.getReviewer());
			settingRepo.createProgramMonth(0).save();
			fail("A reviewer was able to create a new object.");
		} catch (SecurityException se) {
			/* yay */
		}
		context.logout();
	}
	
}
