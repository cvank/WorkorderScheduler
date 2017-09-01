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
public class SkillAllocationQ<T> {

	private final PublishSubject<T> skillAllocationQ;

	public SkillAllocationQ() {

		skillAllocationQ = PublishSubject.<T>create();
		attachErrorHandler(skillAllocationQ);
	}

	public void addToQ(T t) {
		skillAllocationQ.onNext(t);
	}

	public Observable<T> getSkillAllocationQ() {
		return skillAllocationQ.asObservable();
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