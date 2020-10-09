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
// CelestialObject.java
package org.estar.astrometry;

import java.io.*;
import java.lang.*;
import java.text.*;
import java.util.*;

/**
 * This class holds some data for a celestial object.
 * @author Chris Mottram
 * @version $Revision$
 */
public class CelestialObject implements Serializable
{
	public final static String RCSID = "$Id$";
	public String name = null;
	public int number = 0;
	public RA ra = null;
	public Dec dec = null;
	public String type = null;
	public String spectralType = null;
	public double bMagnitude = 0.0;
	public double vMagnitude = 0.0;
	public double rMagnitude = 0.0;
        public String comment = null;
	private final static int TOKEN_INDEX_RAH = 0;
	private final static int TOKEN_INDEX_RAM = 1;
	private final static int TOKEN_INDEX_RAS = 2;
	private final static int TOKEN_INDEX_DECD = 3;
	private final static int TOKEN_INDEX_DECM = 4;
	private final static int TOKEN_INDEX_DECS = 5;

	public CelestialObject()
	{
		super();
	};

	public void setName(String s)
	{
		name = s;
	}

	public String getName()
	{
		return name;
	}

	public void setNumber(int n)
	{
		number = n;
	}

	public int getNumber()
	{
		return number;
	}

	public void setRA(RA r)
	{
		ra = r;
	}

	public RA getRA()
	{
		return ra;
	}

	public void setDec(Dec d)
	{
		dec = d;
	}

	public Dec getDec()
	{
		return dec;
	}

	public void setType(String s)
	{
		type = s;
	}

	public String getType()
	{
		return type;
	}

	public void setSpectralType(String s)
	{
		spectralType = s;
	}

	public String getSpectralType()
	{
		return spectralType;
	}
    
	public void setBMagnitude(double m)
	{
		bMagnitude = m;
	}

	public double getBMagnitude()
	{
		return bMagnitude;
	}

	public void setVMagnitude(double m)
	{
		vMagnitude = m;
	}

	public double getVMagnitude()
	{
		return vMagnitude;
	}

	public void setRMagnitude(double m)
	{
		rMagnitude = m;
	}

	public double getRMagnitude()
	{
		return rMagnitude;
	}

	public void setComment(String s)
	{
		comment = s;
	}

	public String getComment()
	{
		return comment;
	}

	/**
	 * Routine to parse the RA and Dec returned from Simbad.
	 * This copes with strings of the form: "01 10 12.98  +60 04 35.9"
	 * but also "05 28 43     +35 51.3" where decimal minutes are returned and no seconds.
	 * @param s The string to parse.
	 * @see #ra
	 * @see #dec
	 * @see #TOKEN_INDEX_RAH
	 * @see #TOKEN_INDEX_RAM
	 * @see #TOKEN_INDEX_RAS
	 * @see #TOKEN_INDEX_DECD
	 * @see #TOKEN_INDEX_DECM
	 * @see #TOKEN_INDEX_DECS
	 */
	public void parseSimbadRADec(String s)
	{
		StringTokenizer st = null;
		String valueString = null;
		int tokenCount,intValue;
		char signChar;
		double doubleValue;
		
		if(ra == null)
			ra = new RA();
		if(dec == null)
			dec = new Dec();
		st = new StringTokenizer(s," ");
		tokenCount = st.countTokens();
		for(int i=0; i< tokenCount; i++)
		{
			valueString = st.nextToken();
			switch(i)
			{
			case TOKEN_INDEX_RAH:
				intValue = Integer.parseInt(valueString);
				ra.setHours(intValue);
				break;
			case TOKEN_INDEX_RAM:
				// if a decimal ra minutes, then next token is dec degrees
				if(valueString.indexOf(".") > -1)
				{
					// minutes are from start of string up to '.'
					intValue = Integer.parseInt(valueString.substring(0,valueString.indexOf(".")));
					ra.setMinutes(intValue);
					// decimal minutes are from '.' to end of string
					doubleValue = Double.parseDouble(valueString.
									 substring(valueString.indexOf("."),
										   valueString.length()));
					doubleValue *= 60.0;// decimal minutes to seconds
					ra.setSeconds(doubleValue);
					// next token is degrees, inc index to compensate.
					i++;
				}
				else
				{
					intValue = Integer.parseInt(valueString);
					ra.setMinutes(intValue);
				}
				break;
			case TOKEN_INDEX_RAS:
				doubleValue = Double.parseDouble(valueString);
				ra.setSeconds(doubleValue);
				break;
			case TOKEN_INDEX_DECD:
				signChar = valueString.charAt(0);
				dec.setNegative(signChar);
				valueString = valueString.substring(1,valueString.length());
				intValue = Integer.parseInt(valueString);
				dec.setDegrees(intValue);
				break;
			case TOKEN_INDEX_DECM:
				// if a decimal dec minutes, then next token is dec degrees
				if(valueString.indexOf(".") > -1)
				{
					// minutes are from start of string up to '.'
					intValue = Integer.parseInt(valueString.substring(0,valueString.indexOf(".")));
					dec.setMinutes(intValue);
					// decimal minutes are from '.' to end of string
					doubleValue = Double.parseDouble(valueString.
									 substring(valueString.indexOf("."),
										   valueString.length()));
					doubleValue *= 60.0;// decimal minutes to seconds
					dec.setSeconds(doubleValue);
					// This should be last token, inc index to DECS is not called.
					i++;
				}
				else
				{
					intValue = Integer.parseInt(valueString);
					dec.setMinutes(intValue);
				}
				break;
			case TOKEN_INDEX_DECS:
				doubleValue = Double.parseDouble(valueString);
				dec.setSeconds(doubleValue);
				break;
			}// end switch
		}// end for
	}

	public String toString()
	{
		return new String(name+" ("+number+") "+ra+" "+dec+" B:"+bMagnitude+" V:"+vMagnitude+" R:"+rMagnitude);
	}
};
//
// $Log: not supported by cvs2svn $
// Revision 1.5  2005/05/12 11:31:29  cjm
// Made serializable for deep-cloning purposes.
//
// Revision 1.4  2003/07/23 18:07:06  cjm
// Tried making fields public so web-services over applet don't fail
//
// Revision 1.3  2003/02/27 20:09:10  cjm
// Fixed parseSimbadRADec comment.
//
// Revision 1.2  2003/02/27 19:57:53  cjm
// Fixed parseSimbadRADec for the case where 6 tokens do not exist. i.e. for "m38" simbad returns string:
// " 05 28 43     +35 51.3" i.e. with decimal dec minutes. Will also cope with decimal ra minutes.
//
// Revision 1.1  2002/12/29 22:00:57  cjm
// Initial revision
//
//
