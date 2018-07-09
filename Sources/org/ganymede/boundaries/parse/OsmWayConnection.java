package org.ganymede.boundaries.parse;

import com.webobjects.foundation.NSMutableArray;

public class OsmWayConnection {

	public String latitude;
	public String longitude;

	public NSMutableArray<OsmWayData> ways = new NSMutableArray<>();

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("( ");
		str.append(this.latitude);
		str.append(" ");
		str.append(this.longitude);
		str.append(" ) ");
		str.append("ways: ");
		str.append(this.ways.componentsJoinedByString(", "));		
		return str.toString();
	}
}
