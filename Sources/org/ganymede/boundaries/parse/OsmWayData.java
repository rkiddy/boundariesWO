package org.ganymede.boundaries.parse;

import com.webobjects.foundation.NSMutableArray;

public class OsmWayData {

	public String id;

	public String relationId;

	public NSMutableArray<OsmNodeData> nodes = new NSMutableArray<OsmNodeData>();

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("relation: " + this.relationId + " way: " + this.id + "\n");
		for (OsmNodeData node : this.nodes) {
			str.append(node.toString() + "\n");
		}
		return str.toString();
	}

}
