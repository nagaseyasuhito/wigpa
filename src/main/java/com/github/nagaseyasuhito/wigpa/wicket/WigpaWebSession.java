package com.github.nagaseyasuhito.wigpa.wicket;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;

public abstract class WigpaWebSession extends AuthenticatedWebSession {

	public WigpaWebSession(Request request) {
		super(request);

		Injector.get().inject(this);
	}
}
