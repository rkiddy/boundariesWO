package org.ganymede.boundaries.ui;

import org.apache.axis.utils.StringUtils;
import org.ganymede.boundaries.eo.OsmRelation;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;

public class AddRelationPage extends BaseComponent {

	private static final long serialVersionUID = 5697909059270183727L;

	public AddRelationPage(WOContext context) {
		super(context);
	}

	public String message;

	public String relationName;
	public String relationPlace;
	public String relationUrl;
	public String relationUps;
	public String relationNote;

	public OsmRelation nextRelation;

	public WOActionResults cancelForm() {
		relationName = null;
		relationPlace = null;
		relationUrl = null;
		relationUps = null;
		relationNote = null;
		return pageWithName(Main.class);
	}

	public WOActionResults save() {

		if (relationName != null && relationPlace != null && relationUrl != null) {
			nextRelation = OsmRelation.createOsmRelation(ec(), 0L);
			nextRelation.setPk(nextRelation.nextPk(ec()));
			nextRelation.setName(relationName);
			nextRelation.setPlace(relationPlace);

			String[] parts = StringUtils.split(relationUrl, '/');
			nextRelation.setOsmId(parts[parts.length-1]);
			nextRelation.setOsmType(parts[parts.length-2]);

			if (relationNote != null) {
				nextRelation.setNote(relationNote);
			}

			ec().saveChanges();

			return pageWithName(Main.class);

		} else {
			message = "ERROR! Is something wrong? The name, place value and url must all be non-null.";
			return this.context().page();
		}
	}
}