package com.vz.onosproject.provider.zeromq.device;

import com.vz.onosproject.provider.zeromq.api.AbstractZeromqProvider;
import com.vz.onosproject.provider.zeromq.api.ZeromqSBController;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.onosproject.net.DeviceId;
import org.onosproject.net.MastershipRole;
import org.onosproject.net.PortNumber;
import org.onosproject.net.device.DeviceProvider;
import org.onosproject.net.device.DeviceProviderRegistry;
import org.onosproject.net.device.DeviceProviderService;
import org.onosproject.net.device.DeviceService;
import org.slf4j.Logger;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.onosproject.net.DeviceId.deviceId;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by kspviswa-onos-mcord on 2/12/16.
 */

public class ZeromqDeviceProvider extends AbstractZeromqProvider implements DeviceProvider {
    private final Logger log = getLogger(getClass());

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected DeviceProviderRegistry providerRegistry;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected DeviceService deviceService;

    public ZeromqSBController getController() {
        return controller;
    }

    public void setController(ZeromqSBController controller) {
        this.controller = controller;
    }

    //@Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected  ZeromqSBController controller;

    private DeviceProviderService providerService;

    public DeviceProviderService getProviderService() {
        return providerService;
    }

    public void setProviderService(DeviceProviderService providerService) {
        this.providerService = providerService;
    }

    public void activate() {
        //providerService = providerRegistry.register(this);

        if(controller != null) {
            log.info("#### Controller is not null");
        }

        if(this.providerService != null) {
            log.info("#### providerService is not null");
        }

        controller.initConnections(providerService);
        log.info("DeviceProvider Started");
    }

    public void deactivate() {
        //providerRegistry.unregister(this);
        //providerService = null;
        controller.destroyConnections();
        log.info("DeviceProvider Stopped");
    }

    @Override
    public void triggerProbe(DeviceId deviceId) {

        log.info("### DeviceProvider triggeringProble ###");

    }

    @Override
    public void roleChanged(DeviceId deviceId, MastershipRole mastershipRole) {

        log.info("### DeviceProvider roleChanged ####");

    }

    @Override
    public boolean isReachable(DeviceId deviceId) {

        log.info("### DeviceProvider isReachable ####");
        return true;

    }

    @Override
    public void changePortState(DeviceId deviceId, PortNumber portNumber, boolean b) {
        log.info("### DeviceProvider changePortState ####");
    }
}
