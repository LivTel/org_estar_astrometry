// Dec.java
package org.estar.astrometry;

import java.lang.*;
import java.text.*;
import java.util.*;

/**
 * This class hold the coordinates for Declination.
 * @author Chris Mottram
 * @version $Revision: 1.1 $
 */
public class Dec
{
	/**
	 * Revision control system Id.
	 */
	public final static String RCSID = "$Id: Dec.java,v 1.1 2002-12-29 22:00:57 cjm Exp $";
	/**
	 * Default separator.
	 */
	public final static char DEFAULT_SEPERATOR = ':';
	public final static char SIGN_CHAR_POSITIVE = '+';
	public final static char SIGN_CHAR_NEGATIVE = '-';
	/**
	 * Parsing index for degrees.
	 */
	private final static int TOKEN_INDEX_D = 0;
	/**
	 * Parsing index for minutes.
	 */
	private final static int TOKEN_INDEX_M = 1;
	/**
	 * Parsing index for seconds.
	 */
	private final static int TOKEN_INDEX_S = 2;
	/**
	 * The number of degrees. This field should be positive.
	 */
	public int degrees;
	/**
	 * The number of minutes. This field should be positive.
	 */
	public int minutes;
	/**
	 * The number of seconds. This field should be positive.
	 */
	public double seconds;
	/**
	 * We cannot hold whether a declination is positive or negative in the
	 * degrees field, this is because when parsing a dec of '-00 10 20.0' the
	 * parsing of '-00' returns 0, and the negativity is lost. This field is used to
	 * keep track of whether the declination is negative.
	 */
	public boolean negative;

	/**
	 * Default constructor.
	 */
	public Dec()
	{
		super();
	};
	
	/**
	 * Constructor.
	 * @param signChar Whether the Declination is positive or negative. Must be '+' or '-'.
	 * @param d The number of degrees in the declination. Must be positive.
	 * @param m The number of minutes in the declination. Must be positive.
	 * @param s The number of seconds in the declination. Must be positive.
	 */
	public Dec(char signChar,int d,int m,double s)
	{
		super();
		setNegative(signChar);
		setDegrees(d);
		setMinutes(m);
		setSeconds(s);
	}
	
	/**
	 * Sets the degress part of a declination.
	 * @see #degrees
	 */
	public void setDegrees(int d) throws IllegalArgumentException
	{
		if(d < 0)
			throw new IllegalArgumentException("Illegal number of degrees:"+d+": Must be positive");
		degrees = d;
	}

	/**
	 * Returns the number of degrees. This is always positive, use getSign to find out
	 * whether the declination is negative.
	 */
	public int getDegrees()
	{
		return degrees;
	}

	/**
	 * Routine to set whether declination is negative.
	 */
	public void setNegative(boolean b)
	{
		negative = b;
	}

	/**
	 * Routine to set whether declination is negative.
	 * @param signChar The character, containing either '+' or '-', that determines
	 *        whether the declination is positive or negative.
	 * @exception IllegalArgumentException Thrown if the sign character is not a legal value.
	 */	public void setNegative(char signChar) throws IllegalArgumentException
	{
		if(signChar == SIGN_CHAR_POSITIVE)
			negative = false;
		else if(signChar == SIGN_CHAR_NEGATIVE)
			negative = true;
		else
			throw new IllegalArgumentException("Illegal sign character:"+signChar+": Must be [+/-].");
	}

	/**
	 * Returns whether the declination is negative.
	 * @return true if the declination is negative, otherwise false.
	 */
	public boolean getNegative()
	{
		return negative;
	}

	/**
	 * Method to set the minutes of declination.
	 * @param m The number of minutes. This should be positive/zero.
	 * @exception IllegalArgumentException Thrown if the minutes are negative.
	 */
	public void setMinutes(int m) throws IllegalArgumentException
	{
		if(m < 0)
			throw new IllegalArgumentException("Illegal number of minutes:"+m+": Must be positive");
		minutes = m;
	}

	/**
	 * Method to return the number of minutes.
	 */
	public int getMinutes()
	{
		return minutes;
	}
	
	/**
	 * Method to set the seconds of declination.
	 * @param s The number of seconds. This should be positive/zero.
	 * @exception IllegalArgumentException Thrown if the input value is negative.
	 */
	public void setSeconds(double s) throws IllegalArgumentException
	{
		if(s < 0.0)
			throw new IllegalArgumentException("Illegal number of seconds:"+s+": Must be positive");
		seconds = s;
	}

	public double getSeconds()
	{
		return seconds;
	}

	/**
	 * Parse a dot-seperated (.) Declination, and set this objects fields accordingly.
	 */
	public void parseDot(String s) throws IllegalArgumentException
	{
		parseSeparator(s,".");
	}
	
	/**
	 * Parse a colon-seperated (:) Declination, and set this objects fields accordingly.
	 */
	public void parseColon(String s) throws IllegalArgumentException
	{
		parseSeparator(s,":");
	}
	
	/**
	 * Parse a space-seperated ( ) Declination, and set this objects fields accordingly.
	 */
	public void parseSpace(String s) throws IllegalArgumentException
	{
		parseSeparator(s," ");
	}

	/**
	 * Method to parse a string into a valid Declination.
	 * @see #SIGN_CHAR_POSITIVE
	 * @see #SIGN_CHAR_NEGATIVE
	 */
	public void parseSeparator(String s,String separator) throws IllegalArgumentException
	{
		StringTokenizer st = null;
		Number number = null;
		String valueString = null;
		int tokenCount,intValue,d,m;
		char signChar;
		double doubleValue,secs;
		boolean b;
		
		st = new StringTokenizer(s,separator);
		tokenCount = st.countTokens();
		for(int i=0; i< tokenCount; i++)
		{
			valueString = st.nextToken();
			switch(i)
			{
			case TOKEN_INDEX_D:
				signChar = valueString.charAt(0);
				setNegative(signChar);
				valueString = valueString.substring(1,valueString.length());
				d = Integer.parseInt(valueString);
				setDegrees(d);
				break;
			case TOKEN_INDEX_M:
				m = Integer.parseInt(valueString);
				setMinutes(m);
				break;
			case TOKEN_INDEX_S:
				secs = Double.parseDouble(valueString);
				setSeconds(secs);
				break;
			}// end switch
		}// end for
	}

	/**
	 * Return the declination as a number of arc-seconds.
	 * @return This declination, as a number of arc-seconds.
	 */
	public double toArcSeconds()
	{
		double d;

		d = (((((double)degrees)*60.0)+((double)minutes))*60.0)+seconds;
		if(negative)
			d = -d;
		return d;
	}

	/**
	 * Print out the declination, in the form:
	 * <pre>&lt;+|-&gt;DD:MM:SS.ss</pre>
	 * @see #DEFAULT_SEPERATOR
	 * @see #toString(char)
	 */
	public String toString()
	{
		return toString(DEFAULT_SEPERATOR);
	}

	/**
	 * Print out the declination, in the form:
	 * <pre>&lt;+|-&gt;DD<separator>MM<separator>SS.ss</pre>
	 * @param separator A character to use as the separator.
	 */
	public String toString(char separator)
	{
		DecimalFormat df = null;
		DecimalFormat dfd = null;
		StringBuffer sb = null;
		
		df = new DecimalFormat("00");
		dfd = new DecimalFormat("00.00");
		sb = new StringBuffer();
		if(negative)
			sb.append("-");
		else
			sb.append("+");
		sb.append(df.format(degrees));
		sb.append(separator);
		sb.append(df.format(minutes));
		sb.append(separator);
		sb.append(dfd.format(seconds));
		return sb.toString();
	}
};
//
// $Log: not supported by cvs2svn $
//
