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
// CelestialObjectRADecComparator.java
package org.estar.astrometry;

import java.io.*;
import java.lang.*;
import java.util.*;

/**
 * This class is a comparator for CelestialObjects. In orders them in approximate RA/Dec order, for quick searching
 * of a large list of CelestialObjects. Note it uses an errorRadius, which is a number of arc-seconds. If two 
 * celestial objects have RAs within errorRadius of each other, they are said to have equal RAs. The same is true
 * for declinations. This allows us to search for RA/Decs in a list that are close to the required value,
 * (within  a square error box of radius errorRadius).
 * @author Chris Mottram
 * @version $Revision$
 */
public class CelestialObjectRADecComparator implements Comparator,Serializable
{
	/**
	 * Revision control system Identifier.
	 */
	public final static String RCSID = "$Id$";
	/**
	 * The error value, in arc-seconds, used to determine when RA/Decs are close enough to be the same.
	 */
	protected double errorRadius;

	/**
	 * Default constructor.
	 * @param e The error radius value, in arc-seconds.
	 */
	public CelestialObjectRADecComparator(double e)
	{
		super();
		errorRadius = e;
	};

	/**
	 * Comparison routine.
	 * @return Returns true if the CelestialObjects are the same, i.e. within errorRadius arc-seconds
	 *         of each other in RA and Dec.
	 */
	public int compare(Object o1,Object o2)
	{
		CelestialObject co1 = null;
		CelestialObject co2 = null;
		double co1RAArcseconds,co2RAArcseconds,co1DecArcseconds,co2DecArcseconds;
		double raArcsecDiff,decArcsecDiff;

		co1 = (CelestialObject)o1;
		co2 = (CelestialObject)o2;
		co1RAArcseconds = co1.getRA().toArcSeconds();
		co2RAArcseconds = co2.getRA().toArcSeconds();
		raArcsecDiff = co1RAArcseconds - co2RAArcseconds;
		if(Math.abs(raArcsecDiff) < errorRadius)
		{
			// check declination
			co1DecArcseconds = co1.getDec().toArcSeconds();
			co2DecArcseconds = co2.getDec().toArcSeconds();
			decArcsecDiff = co1DecArcseconds - co2DecArcseconds;
			if(Math.abs(decArcsecDiff) < errorRadius)
				return 0;
			else if(co1DecArcseconds < co2DecArcseconds)
				return -1;
			else if(co1DecArcseconds > co2DecArcseconds)
				return 1;
			else
			{
				throw new IllegalArgumentException(this.getClass().getName()+
								   "Comparator failed(2):co1:"+
								   co1+":co2:"+co2+
								   ":co1DecArcseconds:"+co1DecArcseconds+
								   ":co2DecArcseconds:"+co2DecArcseconds+
								   ":errorRadius:"+errorRadius);
			}
		}// end if Ras are same
		else if(co1RAArcseconds < co2RAArcseconds)
			return -1;
		else if(co1RAArcseconds > co2RAArcseconds)
			return 1;
		else
		{
			throw new IllegalArgumentException(this.getClass().getName()+"Comparator failed(1):co1:"+
							   co1+":co2:"+co2+":co1RAArcseconds:"+co1RAArcseconds+
							   ":co2RAArcseconds:"+co2RAArcseconds+
							   ":errorRadius:"+errorRadius);
		}
	}
};
//
// $Log: not supported by cvs2svn $
// Revision 1.1  2003/01/27 19:32:01  cjm
// Initial revision
//
// Revision 1.1  2003/01/08 19:59:23  cjm
// Initial revision
//
// Revision 1.1  2002/12/29 22:00:57  cjm
// Initial revision
//
//
