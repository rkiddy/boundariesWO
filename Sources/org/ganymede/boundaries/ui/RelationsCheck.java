package org.ganymede.boundaries.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.IOUtils;
import org.ganymede.boundaries.eo.OsmRelation;
import org.ganymede.boundaries.eo.OsmRelationCheck;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSTimestamp;

import er.extensions.eof.ERXEditingContextDelegate;

public class RelationsCheck extends BaseComponent {

	private static final long serialVersionUID = 8912066359227031726L;

	public RelationsCheck(WOContext context) {
		super(context);
	}

	public OSMRelationData data = new OSMRelationData();

	public OSMWayData foundWay;
	public OSMNodeData foundNode;
	public OSMWayConnection foundConnection;

	private EOEditingContext ec;

	public void awake() {
		ec = new EOEditingContext();
		ec.setDelegate(new ERXEditingContextDelegate());
	}

	public void appendToResponse(WOResponse aResponse, WOContext aContext) {

		System.out.println("appendToResponse: ec: " + ec);

		@SuppressWarnings("unchecked")
		NSArray<OsmRelation> found = EOUtilities.objectsForEntityNamed(ec, OsmRelation.ENTITY_NAME);

		for (OsmRelation oneFound : found) {

			//System.out.println("found relation: " + oneFound.url());

			if ("relation".equals(oneFound.osmType())) {
				readRelation(oneFound);
				checkRelation(oneFound.name());

				// For now, only check one relation.
				//
				return;
			}
		}
	}

	public NSMutableArray<OSMWayConnection> connections = new NSMutableArray<>();

	private void checkRelation(String relationName) {

		System.out.println("checkRelation: ec: " + ec);

		boolean broken = true;

		data.isConnected = connectionsAllConnect();

		NSTimestamp now = new NSTimestamp();

		OsmRelation relation = OsmRelation.fetchOsmRelation(ec, OsmRelation.NAME.is(relationName));

		System.out.println("found relation: " + relation);

		if (broken) {

			OsmRelationCheck.createOsmRelationCheck(ec, data.isConnected ? 1L : 0L, now, now, relation);

		} else {

			OsmRelationCheck lastCheck = null;

			NSArray<OsmRelationCheck> checks = OsmRelationCheck.fetchOsmRelationChecks(ec, OsmRelationCheck.RELATION.is(relation), null);

			for (OsmRelationCheck check : checks) {
				if (lastCheck == null) {
					lastCheck = check;
				}
				if (lastCheck.startCondition().compare(check.startCondition()) < 0) {
					lastCheck = check;
				}
			}

			if (lastCheck == null || (lastCheck.checkResult() == 1L) != data.isConnected) {
				OsmRelationCheck.createOsmRelationCheck(ec, data.isConnected ? 1L : 0L, now, now, relation);
			} else {
				lastCheck.setEndCondition(now);
			}
		}

		ec.saveChanges();
	}

	private boolean connectionsAllConnect() {

		NSMutableDictionary<String,OSMWayConnection> found = new NSMutableDictionary<>();

		for (OSMWayData way : data.ways) {

			for (OSMNodeData node : way.nodes) {

				String key = "(" + node.latitude + ", " + node.longitude + ")";

				if ( ! found.containsKey(key)) {
					found.put(key, new OSMWayConnection());
				}

				OSMWayConnection c = found.get(key);

				c.latitude = node.latitude;
				c.longitude = node.longitude;

				c.ways.add(way);
			}
		}

		for (OSMWayConnection connection : found.values()) {

			if (connection.ways.size() > 1) {
				connections.add(connection);
			}
		}

		NSMutableArray<String> checkables = new NSMutableArray<>();

		for (OSMWayData way : data.ways) {
			checkables.add(way.id);
		}

		for (OSMWayConnection connection : connections) {
			for (OSMWayData way : connection.ways) {
				checkables.remove(way.id);
			}
		}

		return checkables.isEmpty();
	}

	private void readRelation(OsmRelation rel) {

		readFromOSM("http://www.openstreetmap.org/api/0.6/" + rel.url(), null, null);

		for (OSMWayData way : data.ways) {
			readFromOSM("http://www.openstreetmap.org/api/0.6/way/" + way.id, way, null);

			for (OSMNodeData node : way.nodes) {
				readFromOSM("http://www.openstreetmap.org/api/0.6/node/" + node.id, way, node);
			}
		}

		//System.out.println(data.toString());
	}

	public void readFromOSM(String url, OSMWayData way, OSMNodeData node) {

		//System.out.println("goRead: \"" + url + "\"");

		String contents = null;
		
		try {
			contents = IOUtils.toString(new URL(url), "UTF-8");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

					//dump(qName, attributes);

					// in a relation, read its id.
					//
					if (way == null && node == null && qName.equals("relation")) {
						data.id = valueOfAttributeWithName(attributes, "id");
					}

					// in a relation, read one of its ways.
					//
					if (way == null && node == null && 
							"way".equals(valueOfAttributeWithName(attributes,"type")) &&
							"outer".equals(valueOfAttributeWithName(attributes, "role"))) {
						OSMWayData nextWay = new OSMWayData();
						nextWay.id = valueOfAttributeWithName(attributes, "ref");
						nextWay.relationId = data.id;
						data.ways.add(nextWay);
					}

					if (way != null && node == null && qName.equals("nd")) {
						OSMNodeData nextNode = new OSMNodeData();
						nextNode.id = valueOfAttributeWithName(attributes, "ref");
						nextNode.wayId = way.id;
						nextNode.relationId = data.id;
						way.nodes.add(nextNode);
						//System.out.println("count: way.nodes # " + way.nodes.size());
					}

					if (way != null && node != null && qName.equals("node")) {
						node.latitude = valueOfAttributeWithName(attributes, "lat");
						node.longitude = valueOfAttributeWithName(attributes, "lon");
					}
				}

				public void endElement(String uri, String localName, String qName) throws SAXException {
					//System.out.println("End Element :" + qName);
				}

				public void characters(char ch[], int start, int length) throws SAXException {
					//System.out.println("found: " + new String(ch, start, length));
				}
			};

			saxParser.parse(new ByteArrayInputStream(contents.getBytes()), handler);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String valueOfAttributeWithName(Attributes attributes, String name) {
		int len = attributes.getLength();
		for (int idx = 0; idx < len; idx++) {
			if (attributes.getQName(idx).equals(name)) {
				return attributes.getValue(idx);
			}
		}
		return null;
	}

	@SuppressWarnings("unused")
	private void dump(String name, Attributes attributes) {
		System.out.println("name: " + name);
		int len = attributes.getLength();
		for (int idx = 0; idx < len; idx++) {
			System.out.println("attributes[" + idx + "]: \"" + attributes.getQName(idx) + "\" -> \"" + attributes.getValue(idx) + "\"");;
		}
		System.out.println("");
	}

	public class OSMRelationData {
		public String id;
		public NSMutableArray<OSMWayData> ways = new NSMutableArray<OSMWayData>();

		public boolean isConnected;

		public String toString() {
			StringBuilder str = new StringBuilder();
			str.append("relation: " + this.id + "\n");
			for (OSMWayData way : this.ways) {
				str.append(way.toString());
			}
			str.append("\n");
			return str.toString();
		}
	}

	public class OSMWayData {
		public String id;

		public String relationId;

		public NSMutableArray<OSMNodeData> nodes = new NSMutableArray<OSMNodeData>();

		public String toString() {
			StringBuilder str = new StringBuilder();
			str.append("relation: " + this.relationId + " way: " + this.id + "\n");
			for (OSMNodeData node : this.nodes) {
				str.append(node.toString() + "\n");
			}
			return str.toString();
		}
	}

	public class OSMNodeData {
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

	public class OSMWayConnection {
		public String latitude;
		public String longitude;

		public NSMutableArray<OSMWayData> ways = new NSMutableArray<>();

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
}