package com.github.nagaseyasuhito.wigpa.wicket;

import org.apache.wicket.Page;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;

import com.github.nagaseyasuhito.wigpa.wicket.page.IndexPage;
import com.github.nagaseyasuhito.wigpa.wicket.page.SignInPage;

public class TestWebApplication extends WigpaWebApplication {

	@Override
	public Class<? extends Page> getHomePage() {
		return IndexPage.class;
	}

	@Override
	public CharSequence getJpaUnitName() {
		return "default";
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return SignInPage.class;
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
		return TestSession.class;
	}
}
