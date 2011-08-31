package com.github.nagaseyasuhito.wigpa.wicket;

import org.apache.wicket.Page;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebPage;

import com.github.nagaseyasuhito.wigpa.persist.dao.UserDao;
import com.github.nagaseyasuhito.wigpa.persist.entity.User;
import com.github.nagaseyasuhito.wigpa.wicket.page.IndexPage;
import com.github.nagaseyasuhito.wigpa.wicket.page.SignInPage;
import com.google.inject.Inject;

public class TestWebApplication extends WigpaWebApplication {

	@Inject
	private UserDao userDao;

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

	@Override
	protected void init() {
		super.init();

		Injector.get().inject(this);

		User user = new User();
		user.setSignInId("signInId");
		user.setPassword("password");
		this.userDao.persist(user);
	}
}
