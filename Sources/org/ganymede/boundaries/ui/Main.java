package org.ganymede.boundaries.ui;

import org.ganymede.boundaries.Session;
import org.ganymede.boundaries.eo.OsmRelation;
import org.ganymede.boundaries.eo.OsmRelationCheck;
import org.ganymede.boundaries.eo.OsmRelationSet;
import org.ganymede.boundaries.parse.OsmRelationData;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
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
				ec(),
				new NSTimestamp(),
				connectionsAreGood ? 1L : 0L,
				relation);

		if (data.timestamp != null) {
			nextCheck.setOsmUpdateDate(data.timestamp);
		}

		if (message != null) {
			nextCheck.setMessage(message);
		}

		ec().saveChanges();

		return this.context().page();
	}
}
