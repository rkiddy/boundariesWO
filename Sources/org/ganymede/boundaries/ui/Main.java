package org.ganymede.boundaries.ui;

import org.ganymede.boundaries.eo.OsmRelation;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.foundation.NSArray;

public class Main extends BaseComponent {

	private static final long serialVersionUID = -5911071423566668623L;

	public Main(WOContext context) {
		super(context);
	}

	public OsmRelation relation;

	@SuppressWarnings("unchecked")
	public NSArray<OsmRelation> relations() {
		return EOUtilities.objectsForEntityNamed(ec(), OsmRelation.ENTITY_NAME);
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
}
