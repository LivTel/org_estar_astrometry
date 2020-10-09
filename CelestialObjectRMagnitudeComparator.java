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
// CelestialObjectRMagnitudeComparator.java
package org.estar.astrometry;

import java.lang.*;
import java.util.*;

/**
 * This class holds some data for a celestial object.
 * @author Chris Mottram
 * @version $Revision$
 */
public class CelestialObjectRMagnitudeComparator implements Comparator
{
	/**
	 * Revision control system Identifier.
	 */
	public final static String RCSID = "$Id$";

	/**
	 * Default constructor.
	 */
	public CelestialObjectRMagnitudeComparator()
	{
		super();
	};

	/**
	 * Comparison routine
	 */
	public int compare(Object o1,Object o2)
	{
		CelestialObject co1 = null;
		CelestialObject co2 = null;

		co1 = (CelestialObject)o1;
		co2 = (CelestialObject)o2;

		if((co1.getRMagnitude() - co2.getRMagnitude()) < 0.0)
			return -1;
		else if ((co1.getRMagnitude() - co2.getRMagnitude()) > 0.0)
			return 1;
		else
			return 0;
	}
};
//
// $Log: not supported by cvs2svn $
// Revision 1.1  2003/01/08 19:59:23  cjm
// Initial revision
//
// Revision 1.1  2002/12/29 22:00:57  cjm
// Initial revision
//
//
