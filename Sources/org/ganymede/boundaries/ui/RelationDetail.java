package org.ganymede.boundaries.ui;

import org.ganymede.boundaries.eo.OsmRelation;
import org.ganymede.boundaries.eo.OsmRelationCheck;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSTimestamp;

public class RelationDetail extends BaseComponent {

	private static final long serialVersionUID = 6195636158903218987L;

	public RelationDetail(WOContext context) {
        super(context);
    }

	public OsmRelation relation;
	public OsmRelationCheck check;

	public boolean checkIsGood;

	public WOActionResults addCheck() {

		NSTimestamp now  = new NSTimestamp();

		OsmRelationCheck.createOsmRelationCheck(ec(), now, checkIsGood ? 1L : 0L, relation);

		ec().saveChanges();

		return pageWithName(Main.class);
	}
}