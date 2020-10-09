/*   
    Copyright 2006, Astrophysics Research Institute, Liverpool John Moores University.

    This file is part of org.estar.astrometry.

    org.estar.astrometry is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    org.estar.astrometry is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with org.estar.astrometry; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/
// RA.java
package org.estar.astrometry;

import java.io.*;
import java.lang.*;
import java.text.*;
import java.util.*;

/**
 * This class hold the coordinates for Right Ascension. The right ascension is represented internally
 * with hours, minutes and seconds fields.
 * @author Chris Mottram
 * @version $Revision$
 */
public class RA implements Serializable
{
	/**
	 * Revision Control System ID.
	 */
	public final static String RCSID = "$Id$";
	/**
	 * Serial version ID. Fixed as instances of this class can be used as parameters (inside
	 * org.estar.rtml.RTMLDocument's) in RMI calls across JVMs.
	 */
	static final long serialVersionUID = 685728745827694925L;
	/**
	 * Default character to separate hours, minutes and seconds. This is a colon ':'.
	 */
	public final static char DEFAULT_SEPERATOR = ':';
	/**
	 * How many arc-seconds in 1 second of RA. A double, of value 15.
	 */
	public final static double SECONDS_TO_ARCSECONDS = 15.0;
	/**
	 * How many arc-seconds in 1 day, The maximum number of arc-seconds represented by an RA
	 * before going back to zero.
	 */
	public final static double ARCSECONDS_PER_DAY = (24.0*60.0*60.0*15.0);
	/**
	 * Index for tokenisation, for Hours of RA (0).
	 */
	private final static int TOKEN_INDEX_H = 0;
	/**
	 * Index for tokenisation, for Minutes of RA (1).
	 */
	private final static int TOKEN_INDEX_M = 1;
	/**
	 * Index for tokenisation, for Seconds of RA (2).
	 */
	private final static int TOKEN_INDEX_S = 2;
	/**
	 * The number of hours of RA this object represents. Should be in the range 0..23.
	 */
	public int hours;
	/**
	 * The number of minutes of RA this object represents. Should be in the range 0..59.
	 */
	public int minutes;
	/**
	 * The number of seconds of RA this object represents. Should be in the range 0..59.999...
	 */
	public double seconds;

	/**
	 * Default constructor.
	 */
	public RA()
	{
		super();
	};

	/**
	 * Constructor that sets field values.
	 * @param h The number of hours.
	 * @param m The number of minutes.
	 * @param s The number of seconds.
	 * @see #setHours
	 * @see #setMinutes
	 * @see #setSeconds
	 */
	public RA(int h,int m,double s)
	{
		super();
		setHours(h);
		setMinutes(m);
		setSeconds(s);
	}

	/**
	 * Set the hours of Right Ascension.
	 * @param h Number of hours, in the range (0..23) inclusive.
	 * @exception IllegalArgumentException Thrown if hours are out of range.
	 * @see #hours
	 */
	public void setHours(int h) throws IllegalArgumentException
	{
		if(h < 0)
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setHours:+Illegal number of hours:"+h+
							   ": Must be positive.");
		}
		if(h > 23)
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setHours:+Illegal number of hours:"+h+
							   ": Must be less than 24.");
		}
		hours = h;
	}

	/**
	 * Return the number of hours of right ascension represented by this object.
	 * @return An integer, between 0  and 23 inclusive.
	 * @see #hours
	 */
	public int getHours()
	{
		return hours;
	}

	/**
	 * Set the minutes of Right Ascension.
	 * @param m Number of minutes, in the range (0..59) inclusive.
	 * @exception IllegalArgumentException Thrown if the value is out of range.
	 * @see #minutes
	 */
	public void setMinutes(int m) throws IllegalArgumentException
	{
		if(m < 0)
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setMinutes:+Illegal number of minutes:"+m+
							   ": Must be positive.");
		}
		if(m > 59)
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setMinutes:+Illegal number of minutes:"+m+
							   ": Must be less than 60.");
		}
		minutes = m;
	}

	/**
	 * Return the number of minutes of right ascension represented by this object.
	 * @return An integer, between 0 and 59 inclusive.
	 * @see #minutes
	 */
	public int getMinutes()
	{
		return minutes;
	}
	
	/**
	 * Set the seconds of Right Ascension.
	 * @param m Number of seconds, greater or equal to 0 and less than 60.
	 *          However, we allow seconds of 60.0 as they can occur due to rounding errors.
	 *          e.g. The ESO/ECF server returns them!
	 * @exception IllegalArgumentException Thrown if the value is out of range.
	 * @see #seconds
	 */
	public void setSeconds(double s) throws IllegalArgumentException
	{
		if(s < 0.0)
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setSeconds:+Illegal number of seconds:"+s+
							   ": Must be positive.");
		}
		if(s > 60.0)
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setSeconds:+Illegal number of seconds:"+s+
							   ": Must be less than 60.");
		}
		seconds = s;
	}

	/**
	 * Return the number of seconds of right ascension represented by this object.
	 * @return A double, greater or equal to 0 and less than 60.
	 * @see #seconds
	 */
	public double getSeconds()
	{
		return seconds;
	}

	/**
	 * Method to parse a Right Ascension from a string. The hours, minutes and seconds
	 * should be delimited by a dot ('.').
	 * @param s A string representing a right ascension.
	 * @see #parseSeperator
	 */
	public void parseDot(String s)
	{
		parseSeperator(s,".");
	}

	/**
	 * Method to parse a Right Ascension from a string. The hours, minutes and seconds
	 * should be delimited by a colon (':').
	 * @param s A string representing a right ascension.
	 * @see #parseSeperator
	 */
	public void parseColon(String s)
	{
		parseSeperator(s,":");
	}

	/**
	 * Method to parse a Right Ascension from a string. The hours, minutes and seconds
	 * should be delimited by a space (' ').
	 * @param s A string representing a right ascension.
	 * @see #parseSeperator
	 */
	public void parseSpace(String s)
	{
		parseSeperator(s," ");
	}

	/**
	 * Return the RA as a number of arc-seconds. Note, there are 15 arc-seconds in every second.
	 * @return This RA, as a number of arc-seconds.
	 * @see #SECONDS_TO_ARCSECONDS
	 */
	public double toArcSeconds()
	{
		double d;

		d = (((((double)hours)*60.0)+((double)minutes))*60.0)+seconds;
		d = d * SECONDS_TO_ARCSECONDS;
		return d;
	}

	/**
	 * Routine to set RA from a number of arc-seconds.
	 * @param as The number of arc-seconds, greater or equal to zero and less than ARCSECONDS_PER_DAY.
	 * @exception IllegalArgumentException Thrown if the value is out of range.
	 * @see #SECONDS_TO_ARCSECONDS
	 * @see #ARCSECONDS_PER_DAY
	 */
	public void fromArcSeconds(double as) throws IllegalArgumentException
	{
		int h,m;
		double seconds;

		// range check
		if(as < 0.0)
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":fromArcSeconds:+Illegal number of arc-seconds:"+as+
							   ": Must be positive.");
		}
		if(as >= ARCSECONDS_PER_DAY)
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":fromArcSeconds:+Illegal number of arc-seconds:"+as+
							   ": Must be less than "+ARCSECONDS_PER_DAY+".");
		}
		h = ((int)as) / (60*60*15);
		setHours(h);
		m = (((int)as) - (h*3600*15))/(60*15);
		setMinutes(m);
		// seconds = remainder arc-secs / SECONDS_TO_ARCSECONDS
		seconds = (as-((h*3600.0*SECONDS_TO_ARCSECONDS)+(m*60.0*SECONDS_TO_ARCSECONDS)))/SECONDS_TO_ARCSECONDS;
		setSeconds(seconds);
	}

	/**
	 * Routine to set RA from radians. Done by conversion into arc-seconds, and then calling
	 * fromArcSeconds. The number of arc-seonds for this number of radians is:
	 * <pre>(ARCSECONDS_PER_DAY*radians)/2*PI</pre>
	 * @param radians The number of radians.
	 * @see #ARCSECONDS_PER_DAY
	 * @see #fromArcSeconds
	 */
	public void fromRadians(double radians) throws IllegalArgumentException
	{
		double as;

		if(radians < 0.0)
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":fromRadians:+Illegal number of radians:"+radians+
							   ": Must be positive.");
		}
		if(radians >= (2*Math.PI))
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":fromRadians:+Illegal number of radians:"+radians+
							   ": Must be less than "+(2*Math.PI)+".");
		}
		as = (ARCSECONDS_PER_DAY * radians)/(2*Math.PI); 
		fromArcSeconds(as);
	}

	/**
	 * Routine to convert RA to radians. Done by conversion into arc-seconds, and then converting to radians. 
	 * @return The number of radians.
	 * @see #ARCSECONDS_PER_DAY
	 * @see #toArcSeconds
	 */
	public double toRadians() throws IllegalArgumentException
	{
		return (toArcSeconds()*(2*Math.PI))/ARCSECONDS_PER_DAY;
	}

	/**
	 * Method to print out a right ascension as a string.
	 * e.g.: <pre>HH:MM:SS.ss</pre>
	 * @see #toString(char)
	 * @see #DEFAULT_SEPERATOR
	 */
	public String toString()
	{
		return toString(DEFAULT_SEPERATOR);
	}

	/**
	 * Method to print out a right ascension as a string.
	 * e.g.: <pre>HH&lt;separator&gt;MM&lt;separator&gt;SS.ss</pre>
	 * @param separator The character to delimit the hours, minutes and seconds by. This is usually a colon, dot
	 *     or space.
	 * @return A valid string representation of the right ascension specified by this object. Note, the
	 *         seconds field is formatted to two decimal places.
	 */
	public String toString(char separator)
	{
		DecimalFormat df = null;
		DecimalFormat dfd = null;
		df = new DecimalFormat("00");
		dfd = new DecimalFormat("00.00");
		return new String(df.format(hours)+separator+df.format(minutes)+separator+dfd.format(seconds));
	}

	/**
	 * Method to parse a string representation of a right ascension, and set the fields of this object
	 * accordingly. Note this can throw a NumberFormatException from the Integer.parseInt and 
	 * Double.parseDouble calls it makes..
	 * @param s The string to parse.
	 * @param separator A string representing the seperator. N.B. Should be a char?
	 */
	private void parseSeperator(String s,String seperator)
	{
		StringTokenizer st = null;
		String valueString = null;
		int tokenCount,intValue;
		double doubleValue;
		
		st = new StringTokenizer(s,seperator);
		tokenCount = st.countTokens();
		for(int i=0; i< tokenCount; i++)
		{
			valueString = st.nextToken();
			switch(i)
			{
			case TOKEN_INDEX_H:
				hours = Integer.parseInt(valueString);
				break;
			case TOKEN_INDEX_M:
				minutes = Integer.parseInt(valueString);
				break;
			case TOKEN_INDEX_S:
				seconds = Double.parseDouble(valueString);
				break;
			}// end switch
		}// end for
	}
};
//
// $Log: not supported by cvs2svn $
// Revision 1.7  2007/01/30 18:30:36  cjm
// gnuify: Added GNU General Public License.
//
// Revision 1.6  2005/05/12 11:31:28  cjm
// Made serializable for deep-cloning purposes.
//
// Revision 1.5  2005/01/18 15:44:49  cjm
// Added toRadians.
//
// Revision 1.4  2003/03/04 13:20:21  cjm
// Relaxed checking of seconds, to allow seconds of 60.0 exactley, as
// ESO ECF USNOA2 server returns these (presumably a rounding error).
//
// Revision 1.3  2003/02/24 13:14:12  cjm
// Commenting and error checks.
//
// Revision 1.2  2003/01/27 19:32:01  cjm
// Added fromArcSeconds.
//
// Revision 1.1  2002/12/29 22:00:57  cjm
// Initial revision
//
//
