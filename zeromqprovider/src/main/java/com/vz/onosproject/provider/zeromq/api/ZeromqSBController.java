package com.vz.onosproject.provider.zeromq.api;

/**
 * Created by kspviswa-onos-mcord on 4/12/16.
 */

import org.onosproject.net.DeviceId;
import org.onosproject.net.device.DeviceProviderService;
import org.onosproject.net.flow.FlowRule;

import java.util.List;

public interface ZeromqSBController {

    public void initConnections(DeviceProviderService providerService);

    public void destroyConnections();

    public void writeToDevice(FlowRule rule);

    public void monitorConnections();

    public List<String> getAvailableDevices();
}
