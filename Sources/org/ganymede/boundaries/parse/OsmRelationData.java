package org.ganymede.boundaries.parse;

import com.webobjects.foundation.NSMutableArray;

public class OsmRelationData {

	public String id;

	public String timestamp;

	public NSMutableArray<OsmWayData> ways = new NSMutableArray<OsmWayData>();

	public boolean isConnected;

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("relation: " + this.id + "\n");
		for (OsmWayData way : this.ways) {
			str.append(way.toString());
		}
		str.append("\n");
		return str.toString();
	}
}