package jp.wigpa.wicket;

import javax.persistence.EntityManager;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.wideplay.warp.persist.WorkManager;

public class TransactionRequestCycleListener extends AbstractRequestCycleListener {

	@Inject
	private Provider<EntityManager> entityManager;

	@Inject
	private Provider<WorkManager> workManager;

	public TransactionRequestCycleListener() {
		Injector.get().inject(this);
	}

	private EntityManager getEntityManager() {
		return this.entityManager.get();
	}

	private WorkManager getWorkManager() {
		return this.workManager.get();
	}

	@Override
	public void onBeginRequest(RequestCycle cycle) {
		super.onBeginRequest(cycle);

		this.getWorkManager().beginWork();
		this.getEntityManager().getTransaction().begin();
	}

	@Override
	public void onEndRequest(RequestCycle cycle) {
		super.onEndRequest(cycle);

		try {
			if (this.getEntityManager().getTransaction().isActive()) {
				if (this.getEntityManager().getTransaction().getRollbackOnly()) {
					this.getEntityManager().getTransaction().rollback();
				} else {
					this.getEntityManager().getTransaction().commit();
				}
			}
		} finally {
			this.getWorkManager().endWork();
		}
	}

	@Override
	public IRequestHandler onException(RequestCycle cycle, Exception ex) {
		this.getEntityManager().getTransaction().rollback();

		return super.onException(cycle, ex);
	}
}
