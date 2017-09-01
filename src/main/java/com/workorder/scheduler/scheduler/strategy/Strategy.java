/**
 * 
 */
package com.workorder.scheduler.scheduler.strategy;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.workorder.scheduler.domain.SchedulingAlgo;

/**
 * @author chandrashekar.v
 *
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Strategy {
	public SchedulingAlgo value();

	public int salience() default 0;

	public boolean isDefault() default false;
}
