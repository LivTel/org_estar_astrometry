// RA.java
package org.estar.astrometry;

import java.lang.*;
import java.text.*;
import java.util.*;

/**
 * This class hold the coordinates for Right Ascension.
 * @author Chris Mottram
 * @version $Revision: 1.2 $
 */
public class RA
{
	public final static String RCSID = "$Id: RA.java,v 1.2 2003-01-27 19:32:01 cjm Exp $";
	public final static char DEFAULT_SEPERATOR = ':';
	public final static double SECONDS_TO_ARCSECONDS = 15.0;
	public final static double ARCSECONDS_PER_DAY = (24.0*60.0*60.0*15.0);
	private final static int TOKEN_INDEX_H = 0;
	private final static int TOKEN_INDEX_M = 1;
	private final static int TOKEN_INDEX_S = 2;
	public int hours;
	public int minutes;
	public double seconds;

	public RA()
	{
		super();
	};

	public RA(int h,int m,double s)
	{
		super();
		hours = h;
		minutes = m;
		seconds = s;
	}
	
	public void setHours(int h)
	{
		hours = h;
	}

	public int getHours()
	{
		return hours;
	}

	public void setMinutes(int m)
	{
		minutes = m;
	}
	
	public int getMinutes()
	{
		return minutes;
	}
	
	public void setSeconds(double s)
	{
		seconds = s;
	}

	public double getSeconds()
	{
		return seconds;
	}

	public void parseDot(String s)
	{
		parseSeperator(s,".");
	}

	public void parseColon(String s)
	{
		parseSeperator(s,":");
	}

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
	 * @param as The number of arc-seconds.
	 * @see #SECONDS_TO_ARCSECONDS
	 */
	public void fromArcSeconds(double as)
	{
		int h,m;

		h = ((int)as) / (60*60*15);
		setHours(h);
		m = (((int)as) - (h*3600*15))/(60*15);
		setMinutes(m);
		setSeconds((as-((h*3600.0*15.0)+(m*60.0*15.0))));
	}

	public String toString()
	{
		return toString(DEFAULT_SEPERATOR);
	}

	public String toString(char separator)
	{
		DecimalFormat df = null;
		DecimalFormat dfd = null;
		df = new DecimalFormat("00");
		dfd = new DecimalFormat("00.00");
		return new String(df.format(hours)+separator+df.format(minutes)+separator+dfd.format(seconds));
	}

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
// Revision 1.1  2002/12/29 22:00:57  cjm
// Initial revision
//
//
