package com.vz.onosproject.provider.zeromq.api;

/**
 * Created by kspviswa-onos-mcord on 4/12/16.
 */

import org.onosproject.net.DeviceId;
import org.onosproject.net.flow.FlowRule;

public interface ZeromqSBController {

    public void initConnections();

    public void destroyConnections();

    public void writeToDevice(FlowRule rule);

    public void monitorConnections();
}
