/**
 * 
 */
package com.workorder.scheduler.scheduler.strategy;

import java.util.Set;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.workorder.scheduler.domain.SchedulingAlgo;

/**
 * @author chandrashekar.v
 *
 */
@Component
public final class StrategyIdentifier {

	private static final String STRATEGY_PACKAGE = "com.workorder.scheduler.scheduler.strategy";

	@Autowired
	ApplicationContext applicationContext;

	/**
	 * Identifies validation strategy for the given document.
	 * 
	 * @param documentType
	 * @return
	 */
	public SchedulingStrategy identifyStrategy(SchedulingAlgo algo) {
		Reflections reflections = new Reflections(STRATEGY_PACKAGE);

		Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Strategy.class);
		SchedulingStrategy strategy = null;

		if (!org.springframework.util.CollectionUtils.isEmpty(annotatedClasses)) {
			Class<?> clazz1 = null;
			int prevSalience = Integer.MIN_VALUE;
			for (Class<?> t : annotatedClasses) {
				Strategy annotation = (Strategy) t.getAnnotation(Strategy.class);
				if (null != annotation.value() && annotation.value() == algo) {
					if (annotation.isDefault() || prevSalience < annotation.salience()) {
						clazz1 = t;
						prevSalience = annotation.salience();
						if (annotation.isDefault())
							break;
					}
				}
			}

			if (clazz1 != null)
				strategy = (SchedulingStrategy) applicationContext.getBean(clazz1);
		}
		return strategy;
	}
}
