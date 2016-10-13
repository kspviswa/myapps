package org.test;

import org.onosproject.net.flowobjective.Objective;
import org.onosproject.net.flowobjective.ObjectiveContext;
import org.onosproject.net.flowobjective.ObjectiveError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kspviswa on 13/10/16.
 */
public class TimerObjectiveContext implements ObjectiveContext {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void onSuccess(Objective objective) {
        log.info("#### Installed flow : Success ops => " + objective.id());
    }

    @Override
    public void onError(Objective objective, ObjectiveError error) {

    }
}
