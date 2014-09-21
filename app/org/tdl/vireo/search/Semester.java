package org.tdl.vireo.search;

/**
 * A Simple data structure to hold the pair of Graduation Year and Graduation
 * Semester. This is primarily used for filter searching where users may select
 * a semester, this is a convenient object to house the pairing od data.
 * 
 * 
 * @author <a href="http://www.scottphillips.com">Scott Phillips</a>
 */
public class Semester {
	public Integer year = null;
	public Integer month = null;
	public Type type = null;

	/**
	 * Construct a new graduation semester with null values.
	 */
	public Semester() {
	}

	/**
	 * Construct a new graduation semester
	 * 
	 * @param year
	 *            The year
	 * @param month
	 *            The month
	 */
	public Semester(Integer year, Integer month) {
		this.year = year;
		this.month = month;
	}
	
	/**
	 * Construct a new semester
	 * 
	 * @param year
	 *            The year
	 * @param month
	 *            The month
	 * @param type
	 * 			  The type            
	 */
	public Semester(Integer year, Integer month, Type type) {
		this.year = year;
		this.month = month;
		this.type = type;
	}

	/**
	 * @return true if the two objects are equal, otherwise false.
	 */
	public boolean equals(Object otherObject) {

		if (!(otherObject instanceof Semester))
			return false;
		Semester other = (Semester) otherObject;

		if (this.year == null && other.year != null)
			return false;

		if (this.year != null && !this.year.equals(other.year))
			return false;

		if (this.month == null && other.month != null)
			return false;

		if (this.month != null && !this.month.equals(other.month))
			return false;
		
		if (this.type == null && other.type != null)
			return false;

		if (this.type != null && !this.type.equals(other.type))
			return false;

		return true;
	}
	
	public Type getType(String stringType)
	{
		if(stringType.contains("GRADUATION"))
			return Type.GRADUATION;
		else if(stringType.contains("PROGRAM"))
			return Type.PROGRAM;
		else
			return Type.UNKNOWN;
	}
	
	public String toString(Type type) 
	{
		if(type == Type.GRADUATION)
			return "GRADUATION";
		else if(type == Type.PROGRAM)
			return "PROGRAM";
		else
			return "UNKNOWN";
	}
	
	public enum Type
	{
		GRADUATION, PROGRAM, UNKNOWN
	}
}