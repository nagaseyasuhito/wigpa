package com.github.nagaseyasuhito.wigpa.wicket.page;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import com.github.nagaseyasuhito.wigpa.persist.dao.UserDao;
import com.github.nagaseyasuhito.wigpa.persist.entity.User;
import com.github.nagaseyasuhito.wigpa.wicket.TestWebApplication;
import com.google.inject.Inject;

public class SignInPageTest {

	private WicketTester tester;

	@Inject
	private UserDao userDao;

	@Before
	public void before() {
		this.tester = new WicketTester(new TestWebApplication());

		Injector.get().inject(this);

		User user = new User();
		user.setSignInId("signInId");
		user.setPassword("password");
		this.userDao.persist(user);
	}

	@Test
	public void testSignInFailedWithEmptyPassword() {
		this.tester.startPage(IndexPage.class);
		this.tester.assertRenderedPage(SignInPage.class);

		FormTester signInForm = this.tester.newFormTester("signInForm");
		signInForm.setValue("signInId", "signInId");
		signInForm.setValue("password", null);
		signInForm.submit("signIn");

		this.tester.assertRenderedPage(SignInPage.class);
		this.tester.assertErrorMessages("パスワードは必須です");
	}

	@Test
	public void testSignInFailedWithEmptySignInId() {
		this.tester.startPage(IndexPage.class);
		this.tester.assertRenderedPage(SignInPage.class);

		FormTester signInForm = this.tester.newFormTester("signInForm");
		signInForm.setValue("signInId", null);
		signInForm.setValue("password", "password");
		signInForm.submit("signIn");

		this.tester.assertRenderedPage(SignInPage.class);
		this.tester.assertErrorMessages("サインインIDは必須です");
	}

	@Test
	public void testSignInFailedWithEmptySignInIdAndPassword() {
		this.tester.startPage(IndexPage.class);
		this.tester.assertRenderedPage(SignInPage.class);

		FormTester signInForm = this.tester.newFormTester("signInForm");
		signInForm.setValue("signInId", null);
		signInForm.setValue("password", null);
		signInForm.submit("signIn");

		this.tester.assertRenderedPage(SignInPage.class);
		this.tester.assertErrorMessages("サインインIDは必須です", "パスワードは必須です");
	}

	@Test
	public void testSignInFailedWithWrongPassword() {
		this.tester.startPage(IndexPage.class);
		this.tester.assertRenderedPage(SignInPage.class);

		FormTester signInForm = this.tester.newFormTester("signInForm");
		signInForm.setValue("signInId", "signInId");
		signInForm.setValue("password", "wrong password");
		signInForm.submit("signIn");

		this.tester.assertRenderedPage(SignInPage.class);
		this.tester.assertErrorMessages("サインインIDまたはパスワードが違います");
	}

	@Test
	public void testSignInSuccessed() {
		this.tester.startPage(IndexPage.class);
		this.tester.assertRenderedPage(SignInPage.class);

		FormTester signInForm = this.tester.newFormTester("signInForm");
		signInForm.setValue("signInId", "signInId");
		signInForm.setValue("password", "password");
		signInForm.submit("signIn");

		this.tester.assertRenderedPage(IndexPage.class);
		this.tester.assertNoErrorMessage();
	}
}
