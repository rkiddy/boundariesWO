package org.ganymede.boundaries.parse;

public class OsmNodeData {

	public String id;
	public String latitude;
	public String longitude;

	public String relationId;
	public String wayId;

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("relation: " + this.relationId + " way: " + this.wayId + " node: " + this.id + ": (");
		str.append(this.latitude);
		str.append(", ");
		str.append(this.longitude);
		str.append(")");
		return str.toString();
	}
}
