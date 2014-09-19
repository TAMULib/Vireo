package org.tdl.vireo.model;

/**
 * The possible months each year for a program.
 * 
 * @author Jason Savell jsavell@library.tamu.edu
 * @author <a href="http://www.scottphillips.com">Scott Phillips</a>
 */
public interface ProgramMonth extends AbstractOrderedModel {
	
	/**
	 * @return the month, integer 0 ... 1 where 0 = January and 11 = December.
	 */
	public int getMonth();
	
	/**
	 * @return The english name of the month, i.e: January, February, ... December.
	 */
	public String getMonthName();
	
	/**
	 * @return the new month.
	 */
	public void setMonth(int month);

}
