package org.ganymede.boundaries.eo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

public class OsmRelation extends _OsmRelation {

	private static final long serialVersionUID = 5644160825448587726L;

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(OsmRelation.class);

	public String fullUrl() {
		StringBuilder str = new StringBuilder();
		str.append("http://www.openstreetmap.org/");
		str.append(this.url());
		return str.toString();
	}

	public Long nextPk(EOEditingContext ec) {
		@SuppressWarnings("unchecked")
		NSArray<NSDictionary<String,Long>> results = EOUtilities.rawRowsForSQL(ec, "Boundaries", "select max(pk) as maxPk from osm_relations", new NSArray<String>("maxPk"));
		return (Long)results.get(0).valueForKey("maxPk") + 1L;
	}
}
