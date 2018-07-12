package org.ganymede.boundaries.ui;

import org.ganymede.boundaries.eo.OsmRelation;
import org.ganymede.boundaries.eo.OsmRelationCheck;
import org.ganymede.boundaries.parse.OsmRelationData;

import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSTimestamp;

import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXQ;

public class CheckRelationNow extends BaseComponent {

	private static final long serialVersionUID = 2944586395872498816L;

	public String osmType;
	public String osmId;

	public CheckRelationNow(WOContext context) {
        super(context);
    }

	public String pageContent() {

		if (osmType == null || osmId == null) {
			return "{ \"success\": \"FALSE\" }";
		}

		EOEditingContext ec = ERXEC.newEditingContext();

		OsmRelation relation = OsmRelation.fetchOsmRelation(ec, ERXQ.is(OsmRelation.OSM_TYPE_KEY, osmType).and(ERXQ.is(OsmRelation.OSM_ID_KEY, osmId)));

		if (relation == null) {
			return "{ \"success\": \"FALSE\" }";
		}

		OsmReader reader = new OsmReader();

		String message = null;

		OsmRelationData data = reader.fetchOsmUpdateDate(relation);

		if (data.id == null) {
			message = "Could not fetch relation";
		}

		if (data.ways == null || data.ways.size() == 0) {
			message = "Could not fetch ways of relation";
		}

		boolean connectionsAreGood = false;

		if (message == null) {

			connectionsAreGood = reader.connectionsAllConnect(data);

			if ( ! connectionsAreGood) {
				message = "Some nodes did not connect";
			}
		}

		OsmRelationCheck nextCheck = OsmRelationCheck.createOsmRelationCheck(
				ec,
				new NSTimestamp(),
				connectionsAreGood ? 1L : 0L,
				relation);

		if (data.timestamp != null) {
			nextCheck.setOsmUpdateDate(data.timestamp);
		}

		if (message != null) {
			nextCheck.setMessage(message);
		}

		ec.saveChanges();

		return "{ \"success\": \"" + (connectionsAreGood ? "TRUE" : "FALSE") + "\" }";
	}
}