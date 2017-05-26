package org.ganymede.boundaries.ui;

import org.ganymede.boundaries.Application;
import org.ganymede.boundaries.Session;

import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;

import er.extensions.components.ERXComponent;

public class BaseComponent extends ERXComponent {

	private static final long serialVersionUID = -3049539125218416960L;

	public BaseComponent(WOContext context) {
		super(context);
	}

	@Override
	public Application application() {
		return (Application)super.application();
	}

	@Override
	public Session session() {
		return (Session)super.session();
	}

	public EOEditingContext ec() {
		return session().defaultEditingContext();
	}
}
