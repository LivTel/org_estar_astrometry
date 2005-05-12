// Dec.java
package org.estar.astrometry;

import java.lang.*;
import java.text.*;
import java.util.*;

/**
 * This class hold the coordinates for Declination.
 * @author Chris Mottram
 * @version $Revision: 1.8 $
 */
public class Dec
{
	/**
	 * Revision control system Id.
	 */
	public final static String RCSID = "$Id: Dec.java,v 1.8 2005-05-12 10:04:45 cjm Exp $";
	/**
	 * Default separator.
	 */
	public final static char DEFAULT_SEPERATOR = ':';
	/**
	 * Sign character used to indicate positive declinations, a plus '+'.
	 */
	public final static char SIGN_CHAR_POSITIVE = '+';
	/**
	 * Sign character used to indicate negative declinations, a plus '-'.
	 */
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
	 * Sets the degress part of a declination. This must be positive, (0..90) inclusive. Use setNegative
	 * to set a negative number of degrees (to get round the minus zero degrees bug).
	 * @param degrees An integer representing a number of degrees.
	 * @exception IllegalArgumentException Thrown if the argument is out of range.
	 * @see #setNegative
	 * @see #degrees
	 */
	public void setDegrees(int d) throws IllegalArgumentException
	{
		if(d < 0)
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setDegrees:Illegal number of degrees:"+d+
							   ": Must be positive.");
		}
		if(d > 90)
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setDegrees:Illegal number of degrees:"+d+
							   ": Must be less than or equal to 90.");
		}
		degrees = d;
	}

	/**
	 * Returns the number of degrees. This is always positive, use getNegative to find out
	 * whether the declination is negative.
	 * @return The number of degrees, between (0..90) inclusive.
	 * @see #getNegative
	 */
	public int getDegrees()
	{
		return degrees;
	}

	/**
	 * Routine to set whether declination is negative.
	 * @param b If true, the declination is negative, otherwise it is positive.
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
	 * @see #SIGN_CHAR_POSITIVE
	 * @see #SIGN_CHAR_NEGATIVE
	 */
	public void setNegative(char signChar) throws IllegalArgumentException
	{
		if(signChar == SIGN_CHAR_POSITIVE)
			negative = false;
		else if(signChar == SIGN_CHAR_NEGATIVE)
			negative = true;
		else
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setNegative:Illegal sign character:"+signChar+
							   ": Must be [+/-].");
		}
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
	 * @param m The number of minutes. This should be in the range (0..59) inclusive.
	 * @exception IllegalArgumentException Thrown if the minutes are out of range.
	 */
	public void setMinutes(int m) throws IllegalArgumentException
	{
		if(m < 0)
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setMinutes:Illegal number of minutes:"+m+
							   ": Must be positive.");
		}
		if(m > 59)
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setMinutes:Illegal number of minutes:"+m+
							   ": Must be less than 60.");
		}
		minutes = m;
	}

	/**
	 * Method to return the number of minutes.
	 * @return The number of minutes.
	 */
	public int getMinutes()
	{
		return minutes;
	}
	
	/**
	 * Method to set the seconds of declination.
	 * @param s The number of seconds. This should be graeter or equal to zero and less than 60.
	 *          However, we allow seconds of 60.0 as they can occur due to rounding errors.
	 *          e.g. The ESO/ECF server returns them!
	 * @exception IllegalArgumentException Thrown if the input value is out of range.
	 */
	public void setSeconds(double s) throws IllegalArgumentException
	{
		if(s < 0.0)
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setSecondsIllegal number of seconds:"+s+
							   ": Must be positive.");
		}
		if(s > 60.0)
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setSecondsIllegal number of seconds:"+s+
							   ": Must be less than 60.");
		}
		seconds = s;
	}

	/**
	 * Return the number of seconds.
	 * @return The number of seconds.
	 */
	public double getSeconds()
	{
		return seconds;
	}

	/**
	 * Parse a dot-seperated (.) Declination, and set this objects fields accordingly.
	 * This method asssumes the declination starts with a [+|-] sign.
	 * @see #parseSeparator
	 */
	public void parseDot(String s) throws IllegalArgumentException
	{
		parseSeparator(s,".",false);
	}
	
	/**
	 * Parse a colon-seperated (:) Declination, and set this objects fields accordingly.
	 * This method asssumes the declination starts with a [+|-] sign.
	 * @see #parseSeparator
	 */
	public void parseColon(String s) throws IllegalArgumentException
	{
		parseSeparator(s,":",false);
	}
	
	/**
	 * Parse a colon-seperated (:) Declination, and set this objects fields accordingly.
	 * @param s The colon separated declination string.
	 * @param checkSignChar If true, the first character of the string <b>must be</b> a [+|-] sign.
	 *        Otherwise it doesn't have to be, if the first character of the string <b>is not</b> a [+|-] sign,
	 *        '+' is assumed.
	 * @see #parseSeparator
	 */
	public void parseColon(String s,boolean checkSignChar) throws IllegalArgumentException
	{
		parseSeparator(s,":",checkSignChar);
	}
	
	/**
	 * Parse a space-seperated ( ) Declination, and set this objects fields accordingly.
	 * This method asssumes the declination starts with a [+|-] sign.
	 * @see #parseSeparator
	 */
	public void parseSpace(String s) throws IllegalArgumentException
	{
		parseSeparator(s," ",false);
	}

	/**
	 * Method to parse a string into a valid Declination.
	 * @param s The string to parse.
	 * @param separator The separator string between the degress,minutes and seconds.
	 * @param checkSignChar We normally assume the sign char is always present. However, some
	 *        servers return positive declinations without a +ve sign. If this is the case
	 *        set this value to true. <b>Use this option with care, it should normally be false</b>.
	 * @see #SIGN_CHAR_POSITIVE
	 * @see #SIGN_CHAR_NEGATIVE
	 */
	public void parseSeparator(String s,String separator,boolean checkSignChar) throws IllegalArgumentException
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
				// explicitly check sign char if checkSignChar is set,
				// and assume positive if not [+|-]
				if(checkSignChar)
				{
					if(signChar == SIGN_CHAR_POSITIVE)
					{
						setNegative(false);
						valueString = valueString.substring(1,valueString.length());
					}
					else if(signChar == SIGN_CHAR_NEGATIVE)
					{
						setNegative(true);
						valueString = valueString.substring(1,valueString.length());
					}
					else
					{
						// Normally, if no positive/negative sign is present,
						// throw an exception. However checkSignChar is true,
						// so assume positive.
						setNegative(false);
						// Don't get rid of first character in valueString
						// for degrees parsing in this case.
					}
				}
				else
				{
					setNegative(signChar);
					valueString = valueString.substring(1,valueString.length());
				}
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
	 * Routine to set Dec from a number of arc-seconds.
	 * @param as The number of arc-seconds.
	 */
	public void fromArcSeconds(double as)
	{
		int d,m;
		boolean negative;

		// range check
		if(as < (-90.0*60.0*60.0))
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":fromArcSeconds:+Illegal number of arc-seconds:"+as+
							   ": Must be greater than "+(-90.0*60.0*60.0)+".");
		}
		if(as > (90.0*60.0*60.0))
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":fromArcSeconds:+Illegal number of arc-seconds:"+as+
							   ": Must be less than "+(90.0*60.0*60.0)+".");
		}
		// sort out negative
		if(as < 0.0)
		{
			negative = true;
			as = Math.abs(as);
		}
		else
			negative = false;
		setNegative(negative);
		d = ((int)as) / (60*60);
		setDegrees(d);
		m = (((int)as) - (d*3600))/60;
		setMinutes(m);
		setSeconds((as-((d*3600.0)+(m*60.0))));
	}

	/**
	 * Routine to set Dec from a number of radians.
	 * @param radians The number of radians.
	 * @see #fromArcSeconds
	 */
	public void fromRadians(double radians)
	{
		double as;

		// range check
		if(radians < (-Math.PI/2.0))
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":fromRadians:+Illegal number of radians:"+radians+
							   ": Must be greater than "+(-Math.PI/2.0)+".");
		}
		if(radians > (Math.PI/2.0))
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":fromRadians:+Illegal number of radians:"+radians+
							   ": Must be less than "+(Math.PI/2.0)+".");
		}
		as = (90.0*60.0*60.0 * radians)/(Math.PI/2.0); 
		fromArcSeconds(as);
	}

	/**
	 * Routine to get Dec as radians.
	 * @return The number of radians.
	 * @see #toArcSeconds
	 */
	public double toRadians()
	{
		return (toArcSeconds()*(Math.PI/2.0))/(90.0*60.0*60.0);
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
// Revision 1.7  2005/01/18 15:49:41  cjm
// Added toRadians.
//
// Revision 1.6  2003/03/04 13:20:28  cjm
// Relaxed checking of seconds, to allow seconds of 60.0 exactley, as
// ESO ECF USNOA2 server returns these (presumably a rounding error).
//
// Revision 1.5  2003/02/24 13:14:12  cjm
// Commenting and error checks.
// Added fromRadians method.
//
// Revision 1.4  2003/01/27 19:32:01  cjm
// Added fromArcSeconds.
//
// Revision 1.3  2003/01/17 18:56:11  cjm
// Added extra parameter to parseSeparator, that allows +ve Decs with no '+' character.
// This is to parse ESO Tycho website, that returns Decs in this form.
// Default is still to fail if Dec not prepended with [+|-].
//
// Revision 1.2  2003/01/08 19:59:23  cjm
// *** empty log message ***
//
// Revision 1.1  2002/12/29 22:00:57  cjm
// Initial revision
//
//
