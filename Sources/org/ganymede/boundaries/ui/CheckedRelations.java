package org.ganymede.boundaries.ui;

import org.ganymede.boundaries.eo.OsmRelation;
import org.ganymede.boundaries.eo.OsmRelationCheck;
import org.ganymede.boundaries.eo.OsmRelationSet;

import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSMutableArray;

import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXQ;

public class CheckedRelations extends BaseComponent {

	private static final long serialVersionUID = -317936222970368184L;

	public String osmType;
	public String osmId;
	public String osmSetName;

	public CheckedRelations(WOContext context) {
        super(context);
    }

	public String pageContent() {

		EOEditingContext ec = ERXEC.newEditingContext();

		if (osmType != null && osmId != null) {

			return jsonForCheck(ec, osmType, osmId);
		}

		if (osmSetName != null) {

			OsmRelationSet relationSet = OsmRelationSet.fetchOsmRelationSet(ec, ERXQ.is(OsmRelationSet.NAME_KEY, osmSetName));

			NSMutableArray<String> parts = new NSMutableArray<>();

			for (OsmRelation relation: relationSet.relations()) {

				parts.add(jsonForCheck(ec, relation.osmType(), relation.osmId()));
			}

			return "[\n" + parts.componentsJoinedByString(",\n") + "\n]";
		}

		return "";
	}

	private String jsonForCheck(EOEditingContext ec, String osmType, String osmId) {

		OsmRelation relation = OsmRelation.fetchOsmRelation(ec, ERXQ.is(OsmRelation.OSM_TYPE_KEY, osmType).and(ERXQ.is(OsmRelation.OSM_ID_KEY, osmId)));

		NSMutableArray<String> parts = new NSMutableArray<>();

		parts.add("\"name\": \"" + relation.name() + "\"");
		parts.add("\"url\": \"" + relation.url() + "\"");

		OsmRelationCheck check = relation.lastCheck();

		if (check == null) {
			parts.add("\"checkDate\": \"NULL\"");
			parts.add("\"url\": \"NULL\"");
		} else {
			parts.add("\"checkDate\": \"" +check.checkedTime() + "\"");
			parts.add("\"checkResult\": \"" +check.checkResult() + "\"");
		}

		return "{" + parts.componentsJoinedByString(", ") + "}";
	}
}