package org.ganymede.boundaries;

import org.ganymede.boundaries.ui.CheckRelationNow;
import org.ganymede.boundaries.ui.CheckedRelations;
import org.ganymede.boundaries.ui.Main;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSArray;

import er.extensions.appserver.ERXDirectAction;

public class DirectAction extends ERXDirectAction {

	public DirectAction(WORequest request) {
		super(request);
	}

	@Override
	public WOActionResults defaultAction() {
		return pageWithName(Main.class);
	}

	public Application application() {
		return (Application)WOApplication.application();
	}

	@Override
	public Session session() {
		return (Session)super.session();
	}

	public WOActionResults checkedRelationsAction() {

		NSArray<Object> osmTypeArray = this.context().request().formValuesForKey("t");
		NSArray<Object> osmIdArray = this.context().request().formValuesForKey("id");
		NSArray<Object> osmSetArray = this.context().request().formValuesForKey("set");

		CheckedRelations page = pageWithName(CheckedRelations.class);

		if (osmTypeArray != null && osmTypeArray.size() > 0) {
			page.takeValueForKey(osmTypeArray.get(0).toString(), "osmType");
		}

		if (osmIdArray != null && osmIdArray.size() > 0) {
			page.takeValueForKey(osmIdArray.get(0).toString(), "osmId");
		}

		if (osmSetArray != null && osmSetArray.size() > 0) {
			page.takeValueForKey(osmSetArray.get(0).toString(), "osmSetName");
		}

		WOResponse response = page.generateResponse();

		response.setHeader("Content=Type", "application/json");

		response.setContentEncoding("utf-8");

		return response;
	}

	public WOActionResults checkRelationNowAction() {

		NSArray<Object> osmTypeArray = this.context().request().formValuesForKey("t");
		NSArray<Object> osmIdArray = this.context().request().formValuesForKey("id");

		CheckRelationNow page = pageWithName(CheckRelationNow.class);

		if (osmTypeArray != null && osmTypeArray.size() > 0) {
			page.takeValueForKey(osmTypeArray.get(0).toString(), "osmType");
		}

		if (osmIdArray != null && osmIdArray.size() > 0) {
			page.takeValueForKey(osmIdArray.get(0).toString(), "osmId");
		}

		WOResponse response = page.generateResponse();

		response.setHeader("Content=Type", "application/json");

		response.setContentEncoding("utf-8");

		return response;
	}
}
