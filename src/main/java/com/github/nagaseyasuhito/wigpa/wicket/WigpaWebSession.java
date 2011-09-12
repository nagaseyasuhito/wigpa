package com.github.nagaseyasuhito.wigpa.wicket;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;

public abstract class WigpaWebSession extends AuthenticatedWebSession {
	private static final long serialVersionUID = 1L;

	public static WigpaWebSession get() {
		return (WigpaWebSession) AuthenticatedWebSession.get();
	}

	public WigpaWebSession(Request request) {
		super(request);

		Injector.get().inject(this);
	}

	public boolean signIn(CharSequence username, CharSequence password) {
		return this.signIn(username == null ? null : username.toString(), password == null ? null : password.toString());
	}
}
