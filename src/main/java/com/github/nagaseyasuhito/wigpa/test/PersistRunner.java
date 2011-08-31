package com.github.nagaseyasuhito.wigpa.test;

import javax.persistence.EntityManager;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.wideplay.warp.persist.PersistenceService;
import com.wideplay.warp.persist.UnitOfWork;
import com.wideplay.warp.persist.jpa.JpaUnit;

public class PersistRunner extends BlockJUnit4ClassRunner {

	public PersistRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	@Override
	protected Statement methodInvoker(final FrameworkMethod method, final Object test) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				Module persistenceModule = PersistenceService.usingJpa().across(UnitOfWork.TRANSACTION).buildModule();
				Module JpaUnitModule = new AbstractModule() {
					@Override
					protected void configure() {
						this.bindConstant().annotatedWith(JpaUnit.class).to("default");
					}
				};

				Injector injector = Guice.createInjector(persistenceModule, JpaUnitModule);
				injector.injectMembers(test);

				EntityManager entityManager = injector.getInstance(EntityManager.class);

				try {
					entityManager.getTransaction().begin();

					method.invokeExplosively(test);
				} finally {
					if (entityManager.getTransaction().getRollbackOnly()) {
						entityManager.getTransaction().rollback();
					} else {
						entityManager.getTransaction().commit();
					}
				}
			}
		};
	}

}
