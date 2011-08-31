package com.github.nagaseyasuhito.wigpa.wicket.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;

@AuthorizeInstantiation({ "USER" })
public class IndexPage extends WebPage {

}
