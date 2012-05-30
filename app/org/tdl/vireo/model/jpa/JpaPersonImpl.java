package org.tdl.vireo.model.jpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.tdl.vireo.model.AbstractModel;
import org.tdl.vireo.model.CustomActionValue;
import org.tdl.vireo.model.Person;
import org.tdl.vireo.model.Preference;
import org.tdl.vireo.model.RoleType;

import play.db.jpa.Model;

/**
 * Jpa specific implementation of Vireo's Person interface.
 * 
 * @author <a href="http://www.scottphillips.com">Scott Phillips</a>
 */
@Entity
@Table(name = "person")
public class JpaPersonImpl extends JpaAbstractModel<JpaPersonImpl> implements Person {

	@Column(nullable = false, unique = true)
	public String netid;

	@Column(nullable = false, unique = true)
	public String email;

	@Column(nullable = false)
	public String firstName;

	@Column(nullable = false)
	public String lastName;
	public String middleInitial;
	public String displayName;

	public Integer birthYear;

	public String currentPhoneNumber;
	public String currentPostalAddress;
	public String currentEmailAddress;

	public String permanentPhoneNumber;
	public String permanentPostalAddress;
	public String permanentEmailAddress;

	public String currentDepartment;
	public String currentCollege;
	public String currentMajor;
	public Integer currentGraduationYear;
	public Integer currentGraduationMonth;

	@OneToMany(targetEntity=JpaPreferenceImpl.class, mappedBy="person", cascade=CascadeType.ALL)
	public List<Preference> preferences;
	
	@Column(nullable = false)
	public RoleType role;

	/**
	 * Create a new JpaPersonImpl
	 * 
	 * @param netid
	 *            The netid of the new person.
	 * @param email
	 *            The email of the new person.
	 * @param firstName
	 *            The first name of the new person.
	 * @param lastName
	 *            The last name of the new person.
	 * @param role
	 *            The role for the new person.
	 */
	protected JpaPersonImpl(String netid, String email, String firstName,
			String lastName, RoleType role) {

		if (netid == null || netid.length() == 0)
			throw new IllegalArgumentException("Netid is required");
		
		if (email == null || email.length() == 0)
			throw new IllegalArgumentException("Email is required");
		
		if (firstName == null || firstName.length() == 0)
			throw new IllegalArgumentException("FirstName is required");
		
		if (lastName == null || lastName.length() == 0)
			throw new IllegalArgumentException("lastName is required");
		
		if (role == null )
			throw new IllegalArgumentException("Role is required");

		// Hint: You probably want to turn off the authorization on the
		// context when creating a new person other than Student.
		assertAdministrator();
		
		this.netid = netid;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.preferences = new ArrayList<Preference>();
		this.role = role;
	}
	
	@Override
	public JpaPersonImpl save() {
		
		// While only administrators are able to modify another person's data,
		// the manager can change the person's role. So when saving we just
		// allow managers (or above) or the original person to edit the object.
		assertManagerOrOwner(this);
		
		return super.save();
	}
	
	@Override
	public JpaPersonImpl delete() {
		
		assertAdministratorOrOwner(this);
		
		return super.delete();
	}

	@Override
	public String getNetId() {
		return netid;
	}

	@Override
	public void setNetId(String netid) {
		if (netid == null || netid.length() == 0)
			throw new IllegalArgumentException("Netid is required");
		
		assertAdministratorOrOwner(this);
		
		this.netid = netid;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		if (email == null || email.length() == 0)
			throw new IllegalArgumentException("Email is required");
		
		assertAdministratorOrOwner(this);

		this.email = email;
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public void setFirstName(String firstName) {
		
		if (firstName == null || firstName.length() == 0)
			throw new IllegalArgumentException("firstName is required");
		
		assertAdministratorOrOwner(this);
		
		this.firstName = firstName;
	}

	@Override
	public String getMiddleInitial() {
		return middleInitial;
	}

	@Override
	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public void setLastName(String lastName) {
		
		if (lastName == null || lastName.length() == 0)
			throw new IllegalArgumentException("lastName is required");
		
		assertAdministratorOrOwner(this);
		
		this.lastName = lastName;
	}
	
	@Override
	public String getFullName() {
		return firstName + " " + lastName;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public void setDisplayName(String displayName) {
		
		assertAdministratorOrOwner(this);
		
		this.displayName = displayName;
	}

	@Override
	public Integer getBirthYear() {
		return birthYear;
	}

	@Override
	public void setBirthYear(Integer year) {
		
		assertAdministratorOrOwner(this);
		
		this.birthYear = year;
	}

	@Override
	public String getCurrentPhoneNumber() {
		return currentPhoneNumber;
	}

	@Override
	public void setCurrentPhoneNumber(String phoneNumber) {
		
		assertAdministratorOrOwner(this);
		
		this.currentPhoneNumber = phoneNumber;
	}

	@Override
	public String getCurrentPostalAddress() {
		return currentPostalAddress;
	}

	@Override
	public void setCurrentPostalAddress(String postalAddress) {
		
		assertAdministratorOrOwner(this);
		
		this.currentPostalAddress = postalAddress;
	}

	@Override
	public String getCurrentEmailAddress() {
		return currentEmailAddress;
	}

	@Override
	public void setCurrentEmailAddress(String email) {
		
		assertAdministratorOrOwner(this);
		
		this.currentEmailAddress = email;
	}

	@Override
	public String getPermanentPhoneNumber() {
		return permanentPhoneNumber;
	}

	@Override
	public void setPermanentPhoneNumber(String phoneNumber) {
		
		assertAdministratorOrOwner(this);
		
		this.permanentPhoneNumber = phoneNumber;
	}

	@Override
	public String getPermanentPostalAddress() {
		return permanentPostalAddress;
	}

	@Override
	public void setPermanentPostalAddress(String postalAddress) {
		
		assertAdministratorOrOwner(this);
		
		this.permanentPostalAddress = postalAddress;
	}

	@Override
	public String getPermanentEmailAddress() {
		return permanentEmailAddress;
	}

	@Override
	public void setPermanentEmailAddress(String email) {
		
		assertAdministratorOrOwner(this);
		
		this.permanentEmailAddress = email;
	}

	@Override
	public String getCurrentDepartment() {
		return currentDepartment;
	}

	@Override
	public void setCurrentDepartment(String department) {
		
		assertAdministratorOrOwner(this);
		
		this.currentDepartment = department;
	}

	@Override
	public String getCurrentCollege() {
		return currentCollege;
	}

	@Override
	public void setCurrentCollege(String college) {
		
		assertAdministratorOrOwner(this);
		
		this.currentCollege = college;
	}

	@Override
	public String getCurrentMajor() {
		return currentMajor;
	}

	@Override
	public void setCurrentMajor(String major) {
		
		assertAdministratorOrOwner(this);
		
		this.currentMajor = major;
	}

	@Override
	public Integer getCurrentGraduationYear() {
		return currentGraduationYear;
	}

	@Override
	public void setCurrentGraduationYear(Integer year) {
		
		assertAdministratorOrOwner(this);
		
		this.currentGraduationYear = year;
	}

	@Override
	public Integer getCurrentGraduationMonth() {
		return currentGraduationMonth;
	}

	@Override
	public void setCurrentGraduationMonth(Integer month) {
		if (month != null && ( month > 11 || month < 0)) {
			throw new IllegalArgumentException("Graduation month is out of bounds.");
		}
		
		assertAdministratorOrOwner(this);
		
		this.currentGraduationMonth = month;
	}

	@Override
	public Preference getPreference(String name) {
		return JpaPreferenceImpl.find("person = ? and name = ?", this,name).first();
	}
	
	@Override
	public List<Preference> getPreferences() {
		return preferences;
	}

	@Override
	public Preference addPreference(String name, String value) {
		assertAdministratorOrOwner(this);
		
		Preference preference = new JpaPreferenceImpl(this, name, value);
		this.preferences.add(preference);
		return preference;
	}
	
	/**
	 * Protected call back to remove a deleted preference.
	 * 
	 * @param preference
	 *            The preference to delete.
	 */
	protected void removePreference(Preference preference) {
		
		assertAdministratorOrOwner(this);
		
		preferences.remove(preference);
	}
	
	@Override
	public RoleType getRole() {
		return role;
	}

	@Override
	public void setRole(RoleType role) {
		if (role == null )
			throw new IllegalArgumentException("Role is required");

		if (role == RoleType.STUDENT) {
			assertAdministratorOrOwner(this);
			
		} else if (role == RoleType.REVIEWER || role == RoleType.MANAGER) {
			assertManager();
		
		} else if (role == RoleType.ADMINISTRATOR) {
			assertAdministrator();
		}
		
		this.role = role;
	}

}