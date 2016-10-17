/*
 * Copyright 2016-present Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.test;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.Service;
import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreService;
import org.onosproject.net.Device;
import org.onosproject.net.device.DeviceService;
import org.onosproject.net.flow.DefaultTrafficSelector;
import org.onosproject.net.flow.DefaultTrafficTreatment;
import org.onosproject.net.flow.FlowRuleOperations;
import org.onosproject.net.flow.FlowRuleOperationsContext;
import org.onosproject.net.flow.FlowRuleService;
import org.onosproject.net.flow.TrafficSelector;
import org.onosproject.net.flow.TrafficTreatment;
import org.onosproject.net.flowobjective.DefaultForwardingObjective;
import org.onosproject.net.flowobjective.FlowObjectiveService;
import org.onosproject.net.flowobjective.ForwardingObjective;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Skeletal ONOS application component.
 */
@Component(immediate = true)
public class AppComponent {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final int DEFAULT_TIMEOUT = 10;
    private static final int DEFAULT_PRIORITY = 10;

    private static final long TOTAL_OBJECTIVES = 10000;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected DeviceService deviceService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected CoreService coreService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected FlowRuleService flowRuleService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected FlowObjectiveService flowObjectiveService;

    protected TrafficSelector.Builder selectorBuilder;
    protected TrafficTreatment treatment;
    protected FlowRuleOperationsContext flowRuleOperationsContext;
    protected FlowRuleOperations.Builder builder;

    protected ApplicationId appId;
    private long startTime, endTime;

    @Activate
    protected void activate() {

        selectorBuilder = DefaultTrafficSelector.builder();
        selectorBuilder.matchTunnelId(123);

        treatment = DefaultTrafficTreatment.builder()
                    .drop()
                    .build();

        builder = FlowRuleOperations.builder();
        log.info("Started");
        appId = coreService.registerApplication("org.test.app2");
        Iterable<Device> devices = deviceService.getDevices();
        startTime = System.currentTimeMillis();
        for(Device d : devices) {
            long nTotal = 0;
            for (; nTotal <= TOTAL_OBJECTIVES; nTotal++) {
                ForwardingObjective forwardingObjective = DefaultForwardingObjective.builder()
                        .withSelector(selectorBuilder.build())
                        .withTreatment(treatment)
                        .withPriority(ThreadLocalRandom.current().nextInt(100, 4000 + 1))
                        .withFlag(ForwardingObjective.Flag.VERSATILE)
                        .fromApp(appId)
                        /*.makePermanent()*/
                    .makeTemporary(ThreadLocalRandom.current().nextInt(100, 4000 + 1))
                        .add(/*new TimerObjectiveContext()*/);
                flowObjectiveService.forward(d.id(),
                                             forwardingObjective);
            }
        }

        endTime = System.currentTimeMillis() - startTime;

        log.info("### ONOS has took " + endTime + " ms to install " + TOTAL_OBJECTIVES + " flow objectives ####");

    }

    @Deactivate
    protected void deactivate() {

        log.info("Stopped");
    }

}
