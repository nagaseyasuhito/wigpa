package com.github.nagaseyasuhito.wigpa.wicket;

import java.util.Locale;

import org.apache.wicket.ConverterLocator;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.guice.GuiceInjectorHolder;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.convert.converter.AbstractConverter;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.wideplay.warp.persist.PersistenceService;
import com.wideplay.warp.persist.UnitOfWork;
import com.wideplay.warp.persist.jpa.JpaUnit;

public abstract class WigpaWebApplication extends AuthenticatedWebApplication {

	public static WigpaWebApplication get() {
		return (WigpaWebApplication) WebApplication.get();
	}

	/**
	 * {@code Application}が持っている{@code Injector}を返す
	 * 
	 * @return
	 */
	public Injector getInjector() {
		return this.getMetaData(GuiceInjectorHolder.INJECTOR_KEY).getInjector();
	}

	public abstract CharSequence getJpaUnitName();

	@Override
	protected void init() {
		super.init();

		// GuiceのJPAモジュールの初期化
		Module persistenceModule = PersistenceService.usingJpa().across(UnitOfWork.TRANSACTION).buildModule();
		Module JpaUnitModule = new AbstractModule() {
			@Override
			protected void configure() {
				this.bindConstant().annotatedWith(JpaUnit.class).to(WigpaWebApplication.this.getJpaUnitName().toString());
			}
		};
		this.getComponentInstantiationListeners().add(new GuiceComponentInjector(this, persistenceModule, JpaUnitModule));
		this.getRequestCycleListeners().add(new TransactionRequestCycleListener());
	}

	@Override
	protected IConverterLocator newConverterLocator() {
		ConverterLocator converterLocator = (ConverterLocator) super.newConverterLocator();
		converterLocator.set(CharSequence.class, new AbstractConverter<CharSequence>() {
			private static final long serialVersionUID = 1L;

			@Override
			public CharSequence convertToObject(String value, Locale locale) {
				return value;
			}

			@Override
			protected Class<CharSequence> getTargetType() {
				return CharSequence.class;
			}
		});
		return converterLocator;
	}
}
