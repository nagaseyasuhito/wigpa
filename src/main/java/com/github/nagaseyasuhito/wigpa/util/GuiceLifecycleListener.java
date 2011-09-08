package com.github.nagaseyasuhito.wigpa.util;

import java.lang.ref.PhantomReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuiceLifecycleListener implements ServletContextListener {
	private static final Logger log = LoggerFactory.getLogger(GuiceLifecycleListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		Thread[] threads = this.getThreads();

		String[] guicePrefixes = { "com.google.inject.internal.util.$" };
		for (String guicePrefix : guicePrefixes) {
			for (Thread thread : threads) {
				if (thread == null) {
					continue;
				}

				// Try to shutdown the Finalizer Thread
				try {
					if (thread.getClass().getName().equals(guicePrefix + "Finalizer")) {
						Class<?> mapMakerClass = Class.forName(guicePrefix + "MapMaker");

						Class<?>[] classes = mapMakerClass.getDeclaredClasses();
						for (Class<?> clazz : classes) {
							if (clazz.getName().equals(guicePrefix + "MapMaker$QueueHolder")) {
								Object finalizableReferenceQueue = this.getFieldInstance(null, clazz, "queue");
								Object referenceQueue = this.getFieldInstance(finalizableReferenceQueue, finalizableReferenceQueue.getClass(), "queue");
								Object finalizerQueue = this.getFieldInstance(thread, thread.getClass(), "queue");

								// Check that the thread is our instance
								if (referenceQueue == finalizerQueue) {
									PhantomReference<?> frqReference = (PhantomReference<?>) this.getFieldInstance(thread, thread.getClass(), "frqReference");

									// Notify the finalizer it can stop
									Method enqueue = finalizerQueue.getClass().getDeclaredMethod("enqueue", new Class[] { java.lang.ref.Reference.class });
									enqueue.setAccessible(true);
									enqueue.invoke(finalizerQueue, new Object[] { frqReference });
									log.info(thread.getClass().getName() + " successfully notified to shutdown.");
								}
							}
						}
					}
				} catch (Exception e) {
					log.warn(thread.getClass().getName() + " couldn't be notified to shutdown !", e);
				}

				// Try to cancel the Expiration Timer
				try {
					if (thread.getClass().getName().equals("java.util.TimerThread")) {
						Class<?> expirationTimerClass = null;
						try {
							expirationTimerClass = Class.forName(guicePrefix + "ExpirationTimer");
						} catch (Exception e) {
							// Silently fail
						}
						if (expirationTimerClass != null) {
							Timer instance = (Timer) this.getFieldInstance(null, expirationTimerClass, "instance");
							Thread instanceThread = (Thread) this.getFieldInstance(instance, instance.getClass(), "thread");

							// Check that the thread is our instance
							if (instanceThread == thread) {
								instance.cancel();
								log.info(expirationTimerClass.getName() + " successfully cancelled.");
							}
						}
					}
				} catch (Exception e) {
					log.warn(thread.getClass().getName() + " couldn't be cancelled !", e);
				}
			}

		}
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
	}

	private Object getFieldInstance(Object instance, Class<?> clazz, String name) {
		try {
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			return field.get(instance);
		} catch (Exception e) {
			return null;
		}
	}

	/* Code from apache tomcat 6.0.32 */
	private Thread[] getThreads() {
		// Get the current thread group
		ThreadGroup tg = Thread.currentThread().getThreadGroup();
		// Find the root thread group
		while (tg.getParent() != null) {
			tg = tg.getParent();
		}

		int threadCountGuess = tg.activeCount() + 50;
		Thread[] threads = new Thread[threadCountGuess];
		int threadCountActual = tg.enumerate(threads);
		// Make sure we don't miss any threads
		while (threadCountActual == threadCountGuess) {
			threadCountGuess *= 2;
			threads = new Thread[threadCountGuess];
			// Note tg.enumerate(Thread[]) silently ignores any threads that
			// can't fit into the array
			threadCountActual = tg.enumerate(threads);
		}

		return threads;
	}
}
