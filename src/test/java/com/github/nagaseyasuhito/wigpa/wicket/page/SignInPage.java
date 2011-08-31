package com.github.nagaseyasuhito.wigpa.wicket.page;

import org.apache.wicket.Application;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.validation.ValidationError;

public class SignInPage extends WebPage {
	private static final long serialVersionUID = -6298692049570890085L;

	private String password;

	private String signInId;

	public SignInPage() {
		Form<SignInPage> signInForm = new StatelessForm<SignInPage>("signInForm", new CompoundPropertyModel<SignInPage>(this));
		signInForm.add(new FeedbackPanel("feedback"));
		signInForm.add(new TextField<CharSequence>("signInId").setRequired(true));
		signInForm.add(new PasswordTextField("password"));
		signInForm.add(new Button("signIn") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				if (AuthenticatedWebSession.get().signIn(SignInPage.this.signInId, SignInPage.this.password)) {
					if (!this.continueToOriginalDestination()) {
						this.setResponsePage(Application.get().getHomePage());
					}
				} else {
					this.error(new ValidationError().addMessageKey("signInFailed"));
				}
			}
		});
		this.add(signInForm);
	}
}
