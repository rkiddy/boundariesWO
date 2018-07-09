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
import org.ganymede.boundaries.parse.OsmNodeData;
import org.ganymede.boundaries.parse.OsmRelationData;
import org.ganymede.boundaries.parse.OsmWayConnection;
import org.ganymede.boundaries.parse.OsmWayData;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSTimestamp;

public class RelationUpdates extends BaseComponent {

	private static final long serialVersionUID = 8944421947211919566L;

	public RelationUpdates(WOContext context) {
		super(context);
	}

	public NSArray<OsmRelationCheck> checks() {
		NSMutableArray<OsmRelationCheck> nextChecks = new NSMutableArray<>();
		for (OsmRelation relation : OsmRelation.fetchAllOsmRelations(ec())) {
			OsmRelationCheck lastCheck = relation.lastCheck();
			if (lastCheck != null) {
				nextChecks.add(relation.lastCheck());
			}
		}
		return nextChecks;
	}

	public OsmRelationCheck check;

	public OsmRelationData data;

	public WOActionResults fetchOsmUpdateDate() {

		data = new OsmRelationData();

		readFromOSM("https://www.openstreetmap.org/api/0.6/" + check.relation().url(), null, null);

		for (OsmWayData way : data.ways) {
			readFromOSM("https://www.openstreetmap.org/api/0.6/way/" + way.id, way, null);

			for (OsmNodeData node : way.nodes) {
				readFromOSM("https://www.openstreetmap.org/api/0.6/node/" + node.id, way, node);
			}
		}

		boolean connectionsAreGood = this.connectionsAllConnect();

		OsmRelationCheck nextCheck = OsmRelationCheck.createOsmRelationCheck(
				ec(),
				new NSTimestamp(),
				connectionsAreGood ? 1L : 0L,
				check.relation());

		if (data.timestamp != null) {
			nextCheck.setOsmUpdateDate(data.timestamp);
		}

		ec().saveChanges();

		return this.context().page();
	}

	private void readFromOSM(String url, OsmWayData way, OsmNodeData node) {

		System.out.println("readFromOSM: \"" + url + "\"");

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
						data.timestamp = valueOfAttributeWithName(attributes, "timestamp");
						System.out.println("data.id: " + data.id);
					}

					// in a relation, read one of its ways.
					//
					if (way == null && node == null && 
							"way".equals(valueOfAttributeWithName(attributes,"type")) &&
							"outer".equals(valueOfAttributeWithName(attributes, "role"))) {
						OsmWayData nextWay = new OsmWayData();
						nextWay.id = valueOfAttributeWithName(attributes, "ref");
						nextWay.relationId = data.id;
						data.ways.add(nextWay);
					}

					if (way != null && node == null && qName.equals("nd")) {
						OsmNodeData nextNode = new OsmNodeData();
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

	private String valueOfAttributeWithName(Attributes attributes, String name) {
		int len = attributes.getLength();
		for (int idx = 0; idx < len; idx++) {
			if (attributes.getQName(idx).equals(name)) {
				return attributes.getValue(idx);
			}
		}
		return null;
	}

	private boolean connectionsAllConnect() {

		NSMutableDictionary<String,OsmWayConnection> found = new NSMutableDictionary<>();

		NSMutableArray<OsmWayConnection> connections = new NSMutableArray<>();

		for (OsmWayData way : data.ways) {

			for (OsmNodeData node : way.nodes) {

				String key = "(" + node.latitude + ", " + node.longitude + ")";

				if ( ! found.containsKey(key)) {
					found.put(key, new OsmWayConnection());
				}

				OsmWayConnection c = found.get(key);

				c.latitude = node.latitude;
				c.longitude = node.longitude;

				c.ways.add(way);
			}
		}

		for (OsmWayConnection connection : found.values()) {

			if (connection.ways.size() > 1) {
				connections.add(connection);
			}
		}

		NSMutableArray<String> checkables = new NSMutableArray<>();

		for (OsmWayData way : data.ways) {
			checkables.add(way.id);
		}

		for (OsmWayConnection connection : connections) {
			for (OsmWayData way : connection.ways) {
				checkables.remove(way.id);
			}
		}

		return checkables.isEmpty();
	}
}