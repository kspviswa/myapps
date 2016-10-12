package org.test;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.onosproject.core.ApplicationId;
import org.onosproject.net.Device;
import org.onosproject.net.flow.DefaultTrafficSelector;
import org.onosproject.net.flow.DefaultTrafficTreatment;
import org.onosproject.net.flow.FlowRuleService;
import org.onosproject.net.flow.TrafficSelector;
import org.onosproject.net.flow.TrafficTreatment;
import org.onosproject.net.flowobjective.DefaultForwardingObjective;
import org.onosproject.net.flowobjective.FlowObjectiveService;
import org.onosproject.net.flowobjective.ForwardingObjective;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by kspviswa on 12/10/16.
 */
public class FlowPusherTask {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public FlowPusherTask() {
        timers = new ArrayList<Timer>();
    }

    Device device;
    private List<Timer> timers;
    private long nWorkers;

    public long getnWorkers() {
        return nWorkers;
    }

    public void setnWorkers(long nWorkers) {
        this.nWorkers = nWorkers;
    }

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected FlowRuleService flowRuleService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected FlowObjectiveService flowObjectiveService;

    protected TrafficSelector.Builder selectorBuilder;
    protected TrafficTreatment treatment;

    protected ApplicationId appId;

    public List<Timer> getTimers() {
        return timers;
    }

    public void setTimers(List<Timer> timers) {
        this.timers = timers;
    }

    public FlowRuleService getFlowRuleService() {
        return flowRuleService;
    }

    public void setFlowRuleService(FlowRuleService flowRuleService) {
        this.flowRuleService = flowRuleService;
    }

    public FlowObjectiveService getFlowObjectiveService() {
        return flowObjectiveService;
    }

    public void setFlowObjectiveService(FlowObjectiveService flowObjectiveService) {
        this.flowObjectiveService = flowObjectiveService;
    }

    public ApplicationId getAppId() {
        return appId;
    }

    public void setAppId(ApplicationId appId) {
        this.appId = appId;
    }

    public void init(long nWrkers) {
        this.nWorkers = nWrkers;
        while(nWorkers > 0) {
            this.getTimers().add(new Timer());
            nWorkers--;
        }

        selectorBuilder = DefaultTrafficSelector.builder();
        selectorBuilder.matchTunnelId(123);

        treatment = DefaultTrafficTreatment.builder()
                    .drop()
                    .build();

    }

    public void schedule() {
        for(Timer t : this.getTimers()) {
            t.schedule(new Task(), 0, 1000);
        }
    }

    public void cancel() {
        for(Timer t : this.getTimers()) {
            t.cancel();
        }
    }

    class Task extends TimerTask {

        public Device getDevice() {
            return device;
        }

        @Override
        public void run() {

            log.info("Starting the Flowpusher task for " +
                             FlowPusherTask.this.appId.name() +
                             " on device " +
                             FlowPusherTask.this.device.id());


            ForwardingObjective forwardingObjective = DefaultForwardingObjective.builder()
                    .withSelector(FlowPusherTask.this.selectorBuilder.build())
                    .withTreatment(FlowPusherTask.this.treatment)
                    .withPriority(ThreadLocalRandom.current().nextInt(100, 4000 + 1) )
                    .withFlag(ForwardingObjective.Flag.VERSATILE)
                    .fromApp(FlowPusherTask.this.appId)
                    /*.makePermanent()*/
                    .makeTemporary(ThreadLocalRandom.current().nextInt(100, 4000 + 1))
                    .add();

            flowObjectiveService.forward(FlowPusherTask.this.device.id(),
                                         forwardingObjective);

        }
    }
}
