// CelestialObject.java
package org.estar.astrometry;

import java.lang.*;
import java.text.*;
import java.util.*;

/**
 * This class holds some data for a celestial object.
 * @author Chris Mottram
 * @version $Revision: 1.1 $
 */
public class CelestialObject
{
	public final static String RCSID = "$Id: CelestialObject.java,v 1.1 2002-12-29 22:00:57 cjm Exp $";
	protected String name = null;
	protected int number = 0;
	protected RA ra = null;
	protected Dec dec = null;
	protected String type = null;
	protected String spectralType = null;
	protected double bMagnitude = 0.0;
	protected double vMagnitude = 0.0;
	protected double rMagnitude = 0.0;
	protected String comment = null;
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
				intValue = Integer.parseInt(valueString);
				ra.setMinutes(intValue);
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
				intValue = Integer.parseInt(valueString);
				dec.setMinutes(intValue);
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
//
