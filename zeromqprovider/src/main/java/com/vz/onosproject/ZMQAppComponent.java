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
package com.vz.onosproject;

import com.vz.onosproject.provider.zeromq.api.ZeromqSBController;
import com.vz.onosproject.provider.zeromq.controller.ZeromqSBControllerImpl;
import com.vz.onosproject.provider.zeromq.device.ZeromqDeviceProvider;
import com.vz.onosproject.provider.zeromq.flowrule.ZeromqFlowRuleProvider;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.Service;
import org.onosproject.mastership.MastershipAdminService;
import org.onosproject.net.device.DeviceAdminService;
import org.onosproject.net.device.DeviceProviderRegistry;
import org.onosproject.net.device.DeviceProviderService;
import org.onosproject.net.flow.FlowRuleProviderRegistry;
import org.onosproject.net.flow.FlowRuleProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Skeletal ONOS application component.
 */
@Component(immediate = true)
public class ZMQAppComponent {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected MastershipAdminService mastershipService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected DeviceAdminService deviceService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected DeviceProviderRegistry deviceProviderRegistry;

    //@Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    //protected FlowRuleProviderRegistry flowRuleProviderRegistry;

    private final ZeromqDeviceProvider deviceProvider = new ZeromqDeviceProvider();
    //private final ZeromqFlowRuleProvider flowRuleProvider = new ZeromqFlowRuleProvider();
    //private final ZeromqSBController controller = new ZeromqSBControllerImpl();

    @Reference
    private ZeromqSBController controller;

    private DeviceProviderService deviceProviderService;
    private FlowRuleProviderService flowRuleProviderService;

    @Activate
    protected void activate() {
        deviceProviderService = deviceProviderRegistry.register(deviceProvider);
        //flowRuleProviderService = flowRuleProviderRegistry.register(flowRuleProvider);
        deviceProvider.setProviderService(deviceProviderService);
        deviceProvider.setController(controller);
        deviceProvider.activate();


        log.info("Started");
    }

    @Deactivate
    protected void deactivate() {
        deviceProvider.deactivate();
        deviceProviderRegistry.unregister(deviceProvider);
        //flowRuleProviderRegistry.unregister(flowRuleProvider);

        deviceProviderService = null;
        //flowRuleProviderService = null;

        log.info("Stopped");
    }

    public List<String> test() {
        return controller.getAvailableDevices();
    }

}
