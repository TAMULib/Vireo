package org.tdl.vireo.search;

/**
 * List of all possible ordering of Vireo submissions.
 * 
 * @author <a href="http://www.scottphillips.com">Scott Phillips</a>
 */
public enum SearchOrder {
	
	ID(1),
	STUDENT_EMAIL(2),
	STUDENT_NAME(3),
	STUDENT_ID(4),
	
	STATE(5),
	
	ASSIGNEE(6),
	
	DOCUMENT_TITLE(7),
	DOCUMENT_ABSTRACT(8),
	DOCUMENT_KEYWORDS(9),
	DOCUMENT_SUBJECTS(10),
	DOCUMENT_LANGUAGE(11),
	
	PUBLISHED_MATERIAL(12),
	
	PRIMARY_DOCUMENT(13),

	PROGRAM_DATE(14),
	GRADUATION_DATE(15),
	DEFENSE_DATE(16),
	SUBMISSION_DATE(17),
	LICENSE_AGREEMENT_DATE(18),
	APPROVAL_DATE(19),
		
	COMMITTEE_APPROVAL_DATE(20),
	COMMITTEE_EMBARGO_APPROVAL_DATE(21),
	COMMITTEE_MEMBERS(22),
	COMMITTEE_CONTACT_EMAIL(23),

	DEGREE(24),
	DEGREE_LEVEL(25),
	
	PROGRAM(26),
	COLLEGE(27),
	DEPARTMENT(28),
	MAJOR(29),
	
	EMBARGO_TYPE(30),
	DOCUMENT_TYPE(31),
	
	UMI_RELEASE(32),
	
	CUSTOM_ACTIONS(33),
	
	DEPOSIT_ID(34),
	
	REVIEWER_NOTES(35),
	
	LAST_EVENT_ENTRY(35),
	LAST_EVENT_TIME(36);
	
	// The id for this search order.
	private int id;

	/**
	 * Private constructor for the defined search orders listed above.
	 * 
	 * @param id
	 *            The id of the search order.
	 */
	private SearchOrder(int id) {
		this.id = id;
	}

	/**
	 * @return The code of this search order.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Locate a search order based upon it's id.
	 * 
	 * @param id
	 *            The of the search order.
	 * @return The search order, or null if not found.
	 */
	public static SearchOrder find(int id) {

		for (SearchOrder searchOrder : SearchOrder.values()) {
			if (searchOrder.id == id)
				return searchOrder;
		}

		return null;
	}
}
