package org.tdl.vireo.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.DOMBuilder;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import play.Logger;

/**
 * A catch-all class for various Vireo utilities
 * 
 * @author Alexey Maslov
 */
public class Utilities {

	private static final String[] CONTROL_RANGES = {
		"\u0000-\u0009", // CO Control (including: Bell, Backspace, and Horizontal Tab)
		"\u000B-\u000C", // CO Control (Line Tab and Form Feed)
		"\u000E-\u001F", // CO Control (including: Escape)
		"\u007F",        // CO Control (Delete Character)
		"\u0080-\u009F"  // C1 Control
	};

	private static final String ORCID_API = "http://pub.orcid.org/#/orcid-bio";
	private static final Namespace ORCIDns = Namespace.getNamespace("orcid", "http://www.orcid.org/ns/orcid");

	/**
	 * Scrub UNICODE control characters out of the provided string, deleteing them 
	 * @param input
	 * @param replace
	 * @return
	 */
	public static String scrubControl(String input) {
		return scrubControl(input, "");
	}
	
	/**
	 * Scrub UNICODE control characters out of the provided string, replacing them with the specified string 
	 * @param input
	 * @param replace
	 * @return
	 */
	public static String scrubControl(String input, String replace) {
		
		if (input == null)
			return null;
		if ("".equals(input))
			return "";
		
		return input.replaceAll("[" + CONTROL_RANGES[0] + CONTROL_RANGES[1] + 
				CONTROL_RANGES[2] + CONTROL_RANGES[3] + CONTROL_RANGES[4] + "]", replace);
	}	
	
	/**
	 * Vireo expects an ORCiD to be in the form "xxxx-xxxx-xxxx-xxxy",
	 * where x is numeric and y can be numeric or X. This function will
	 * determine is a give string is in the expected format.
	 * @param orcid A string that should be an ORCiD.
	 * @return true if the string is in the proper format.
	 */
	public static boolean validateOrcidFormat(String orcid) {
		String pattern = "[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{3}[0-9X]";
		return orcid.matches(pattern);
	}
	
	/**
	 * Vireo expects an ORCiD to be in the form "xxxx-xxxx-xxxx-xxxy",
	 * where x is numeric and y can be numeric or X. Sometimes you might 
	 * receive an ORCiD as a URL such as "http://www.orcid.org/xxxx-xxxx-xxxx-xxxy",
	 * or without dashes. This function attempts to format a given string
	 * into the format Vireo would prefer.
	 * @param unformattedOrcid - An orcid as a string in various formats.
	 * @return formattedOrcid - An orcid formatted as just an id with dashes.	 * 
	 */
	public static String formatOrcidAsDashedId(String unformattedOrcid) {
		String formattedOrcid = unformattedOrcid.toUpperCase();
		formattedOrcid = formattedOrcid.replaceAll("(HTTPS|HTTP|WWW|ORCID|ORG|COM|NET|[/:.\\s])","");
		
		if (validateOrcidFormat(formattedOrcid)) {
			return formattedOrcid;
		} else if (formattedOrcid.matches("[0-9]{15}[0-9X]")) {
			formattedOrcid = insertPeriodically(formattedOrcid, "-", 4);
			if(validateOrcidFormat(formattedOrcid))
				return formattedOrcid;
			else
				return unformattedOrcid;
		} else {
			return unformattedOrcid;
		}		
	}
	
	/**
	 * Given an ORCID identifier, this function will ping the ORCID public API and return
	 * true if a user is found.  
	 * @param orcid id in standard ORCID form "xxxx-xxxx-xxxx-xxxx"
	 * @return true if a user is found matching this id, false in all other cases
	 */
	public static boolean verifyOrcid(String orcid) {
		return verifyOrcid(orcid, null, null);
	}
	
	/**
	 * Given an ORCID identifier, this function will ping the ORCID public API and return
	 * true if a user is found that matches the provided name and id combination.  
	 * @param orcid id in standard ORCID form "xxxx-xxxx-xxxx-xxxx"
	 * @return true if a user is found matching this id and name, false in all other cases
	 */
	public static boolean verifyOrcid(String orcid, String firstName, String lastName) {
		
		try {
			URL orcidURL = new URL(ORCID_API.replace("#", orcid));

			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(orcidURL);
			
			List<Namespace> ns = new ArrayList<Namespace>();
			ns.add(ORCIDns);
			
			// Basic sanity check on the document itself
			if (singleXPath(doc, "/orcid:orcid-message", ns) == null) {
				Logger.warn("Failed to get an ORCID message");
				return false;
			}
			// Check for the error (orcid not found) message 
			if (singleXPath(doc, "//orcid:error-desc", ns) != null)
			{
				Logger.info("Invalid ORCID specified");
				return false;		
			}
			// Validate the name
			Element personalDetails = singleXPath(doc, "//orcid:personal-details", ns);
			
			if (personalDetails != null) {
				// If we are not actually checking the name, we are done
				if (firstName == null || lastName == null) {
					return true;
				}
				// Otherwise, do the comparison
				/* <personal-details>
	                <given-names>Alexey</given-names>
	                <family-name>Maslov</family-name>
	            </personal-details> */			
				else {
					Element orcidFirstName = personalDetails.getChild("given-names", ORCIDns);
					Element orcidLastName = personalDetails.getChild("family-name", ORCIDns);
					
					if (orcidFirstName == null || orcidLastName == null) {
						Logger.warn("ORCID response had missing names");
						return false;
					}
					else if (!firstName.equals(orcidFirstName.getTextNormalize()) || 
							!lastName.equals(orcidLastName.getTextNormalize())) {
						Logger.warn("ORCID response had wrong name. Expected " + firstName + " " + lastName + ", but got " + 
							orcidFirstName.getTextNormalize() + " " + orcidLastName.getTextNormalize() + ".");
						return false;
					}
					
					// Anything that's made it through the gauntlet, should be fine
					return true;
				}
			}
						
		} catch (MalformedURLException muex) {
			Logger.warn("URL error occured while validating ORCID: " + muex.getMessage());
			//muex.printStackTrace();
			return false;
		} catch (JDOMException jdex) {
			Logger.warn("JDOM error occured while validating ORCID: " + jdex.getMessage());
			//jdex.printStackTrace();
			return false;
		} catch (IOException ioex) {
			Logger.warn("IO error occured while validating ORCID: " + ioex.getMessage());
			//ioex.printStackTrace();
			return false;
		} 
		
		return false;
	}
	
	
	
	/**
	 * Utility method to execute an XPath expression and return back a single result	
	 * @param doc a JDOM document that the XPath is being executed on
	 * @param xpath an XPath expression encoded as a string
	 * @return a JDOM element matching the query or null
	 * @throws JDOMException
	 */
	public static Element singleXPath(Document doc, String xpath) throws JDOMException
	{
		return singleXPath(doc, xpath, null);
	}
	
	public static Element singleXPath(Document doc, String xpath, List<Namespace> namespaces) throws JDOMException 
	{
		XPath xp = XPath.newInstance(xpath);
		
		if (namespaces != null) {
			for (Namespace ns : namespaces)
				xp.addNamespace(ns);
		}
		
		List<Element> results = xp.selectNodes(doc);
		if (results == null || results.size() == 0 || !(results.get(0) instanceof Element))
			return null;
		else
			return results.get(0);
	}
	
	/**
	 * A helper method to insert a specific character every X characters in a string.
	 */
	public static String insertPeriodically(
		    String text, String insert, int period)
		{
		    StringBuilder builder = new StringBuilder(
		         text.length() + insert.length() * (text.length()/period)+1);

		    int index = 0;
		    String prefix = "";
		    while (index < text.length())
		    {
		        // Don't put the insert in the very first iteration.
		        // This is easier than appending it *after* each substring
		        builder.append(prefix);
		        prefix = insert;
		        builder.append(text.substring(index, 
		            Math.min(index + period, text.length())));
		        index += period;
		    }
		    return builder.toString();
		}
}
