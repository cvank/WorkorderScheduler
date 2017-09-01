/**
 * 
 */
package com.workorder.scheduler.framework;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * @author chandrashekar.v
 *
 */
public class TechnicianWorkOrderQueue<T> {

	private final PublishSubject<T> technicianWorkOrderQueue;

	public TechnicianWorkOrderQueue() {

		technicianWorkOrderQueue = PublishSubject.<T>create();
		attachErrorHandler(technicianWorkOrderQueue);
	}

	public void addToQ(T t) {
		technicianWorkOrderQueue.onNext(t);
	}

	public Observable<T> getTechnicianWorkOrderQueue() {
		return technicianWorkOrderQueue.asObservable();
	}

	private void attachErrorHandler(Observable<T> observable) {
		// observable.onErrorResumeNext(throwable ->
		// Observable.error(throwable));
		observable.onErrorResumeNext(throwable -> Observable.empty()).doOnError((t) -> {
			System.out.println("ERROR:" + t.getMessage());
			t.printStackTrace();
		});
	}

}
