package org.ganymede.boundaries.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.IOUtils;
import org.ganymede.boundaries.Session;
import org.ganymede.boundaries.eo.OsmRelation;
import org.ganymede.boundaries.eo.OsmRelationCheck;
import org.ganymede.boundaries.eo.OsmRelationSet;
import org.ganymede.boundaries.parse.OsmNodeData;
import org.ganymede.boundaries.parse.OsmRelationData;
import org.ganymede.boundaries.parse.OsmWayConnection;
import org.ganymede.boundaries.parse.OsmWayData;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSTimestamp;

public class Main extends BaseComponent {

	private static final long serialVersionUID = -5911071423566668623L;

	public Main(WOContext context) {
		super(context);
	}

	public boolean authenticated() {
		return (Boolean)session().valueForKey("authenticated");
	}

	public String pass;

	public WOActionResults authenticate() {
		if (pass.equals("none")) {
			session().authenticated = true;
		}
		return this.context().page();
	}

	public OsmRelation relation;

	@SuppressWarnings("unchecked")
	public NSArray<OsmRelation> relations() {
		NSArray<OsmRelation> relations = EOUtilities.objectsForEntityNamed(ec(), OsmRelation.ENTITY_NAME);
		if (session().mainListFilter.equals(Session.MAIN_LIST_FILTER_SHOW_GOOD)) {
			NSMutableArray<OsmRelation> nextRelations = new NSMutableArray<>();
			for (OsmRelation relation : relations) {
				OsmRelationCheck check = relation.lastCheck();
				if (check != null && check.checkResult().intValue() == 1) {
					nextRelations.add(relation);
				}
			}
			relations = nextRelations;
		}
		if (session().mainListFilter.equals(Session.MAIN_LIST_FILTER_SHOW_BAD)) {
			NSMutableArray<OsmRelation> nextRelations = new NSMutableArray<>();
			for (OsmRelation relation : relations) {
				OsmRelationCheck check = relation.lastCheck();
				if (check == null || check.checkResult().intValue() == 0) {
					nextRelations.add(relation);
				}
			}
			relations = nextRelations;
		}
		return EOSortOrdering.sortedArrayUsingKeyOrderArray(relations, session().mainListOrderings);
	}

	public WOActionResults showAllRelations() {
		session().mainListFilter = Session.MAIN_LIST_FILTER_SHOW_ALL;
		return this.context().page();
	}

	public WOActionResults showOnlyGoodRelations() {
		session().mainListFilter = Session.MAIN_LIST_FILTER_SHOW_GOOD;
		return this.context().page();
	}

	public WOActionResults showOnlyBadRelations() {
		session().mainListFilter = Session.MAIN_LIST_FILTER_SHOW_BAD;
		return this.context().page();
	}

	public WOActionResults sortByName() {
		if (session().mainListOrderings.equals(OsmRelation.NAME.ascs())) {
			session().mainListOrderings = OsmRelation.NAME.descs();
		} else {
			session().mainListOrderings = OsmRelation.NAME.ascs();
		}
		return this.context().page();
	}

	public WOActionResults sortByPlace() {
		if (session().mainListOrderings.equals(OsmRelation.PLACE.ascs())) {
			session().mainListOrderings = OsmRelation.PLACE.descs();
		} else {
			session().mainListOrderings = OsmRelation.PLACE.ascs();
		}
		return this.context().page();
	}

	public WOActionResults save() {
		ec().saveChanges();
		return this.context().page();
	}

	public WOActionResults cancel() {
		return this.context().page();
	}

	public WOActionResults addOne() {
		return pageWithName(AddRelationPage.class);
	}

	public WOActionResults checkIsGood() {
		OsmRelationCheck.createOsmRelationCheck(ec(), new NSTimestamp(), 1L, relation);
		ec().saveChanges();
		return this.context().page();
	}

	public WOActionResults checkIsBad() {
		OsmRelationCheck.createOsmRelationCheck(ec(), new NSTimestamp(), 0L, relation);
		ec().saveChanges();
		return this.context().page();
	}

	public NSArray<OsmRelationSet> relationSets() {
		return OsmRelationSet.fetchAllOsmRelationSets(ec());
	}

	public OsmRelationSet relationSet;

	public OsmRelationSet selectedRelationSet;

	public WOActionResults selectRelationSet() {
		selectedRelationSet = relationSet;
		return this.context().page();
	}

	public WOActionResults clearRelationSet() {
		selectedRelationSet = null;
		return this.context().page();
	}

	public WOActionResults verifyRelation() {

		OsmRelationData data = new OsmRelationData();

		readFromOSM("https://www.openstreetmap.org/api/0.6/" + relation.url(), data, null, null);

		if (data.ways != null && data.ways.size() > 0) {


			for (OsmWayData way : data.ways) {
				readFromOSM("https://www.openstreetmap.org/api/0.6/way/" + way.id, data, way, null);

				for (OsmNodeData node : way.nodes) {
					readFromOSM("https://www.openstreetmap.org/api/0.6/node/" + node.id, data, way, node);
				}
			}

			boolean connectionsAreGood = this.connectionsAllConnect(data);

			OsmRelationCheck nextCheck = OsmRelationCheck.createOsmRelationCheck(
					ec(),
					new NSTimestamp(),
					connectionsAreGood ? 1L : 0L,
							relation);

			if (data.timestamp != null) {
				nextCheck.setOsmUpdateDate(data.timestamp);
			}
		} else {

			System.err.println("ERROR: No ways found in relation: " + "https://www.openstreetmap.org/api/0.6/" + relation.url());

			OsmRelationCheck.createOsmRelationCheck(
					ec(),
					new NSTimestamp(),
					0L,
					relation);
		}

		ec().saveChanges();

		data = null;

		return this.context().page();
	}

	private void readFromOSM(String url, OsmRelationData data, OsmWayData way, OsmNodeData node) {

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

	private boolean connectionsAllConnect(OsmRelationData data) {

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
