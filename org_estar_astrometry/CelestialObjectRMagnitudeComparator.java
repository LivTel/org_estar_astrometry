// CelestialObjectRMagnitudeComparator.java
package org.estar.astrometry;

import java.lang.*;
import java.util.*;

/**
 * This class holds some data for a celestial object.
 * @author Chris Mottram
 * @version $Revision: 1.1 $
 */
public class CelestialObjectRMagnitudeComparator implements Comparator
{
	/**
	 * Revision control system Identifier.
	 */
	public final static String RCSID = "$Id: CelestialObjectRMagnitudeComparator.java,v 1.1 2003-01-08 19:59:23 cjm Exp $";

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
// Revision 1.1  2002/12/29 22:00:57  cjm
// Initial revision
//
//
