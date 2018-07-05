package org.ganymede.boundaries;

import org.ganymede.boundaries.eo.OsmRelation;

import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

import er.extensions.appserver.ERXSession;

public class Session extends ERXSession {

	private static final long serialVersionUID = 1L;

	public static final String MAIN_LIST_FILTER_SHOW_ALL = "SHOW_ALL";
	public static final String MAIN_LIST_FILTER_SHOW_GOOD = "SHOW_GOOD";
	public static final String MAIN_LIST_FILTER_SHOW_BAD = "SHOW_BAD";

	public Session() {
	}
	
	@Override
	public Application application() {
		return (Application)super.application();
	}

	public boolean authenticated = false;

	public NSArray<EOSortOrdering> mainListOrderings = OsmRelation.NAME.ascs();

	/**
	 * Filter the main page's list of relations, set to either "SHOW_ALL", "SHOW_GOOD", or "SHOW_BAD".
	 */
	public String mainListFilter = "SHOW_ALL";
}
