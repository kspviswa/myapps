package com.vz.onosproject.provider.zeromq.api;

/**
 * Created by kspviswa-onos-mcord on 4/12/16.
 */

import org.onosproject.net.DeviceId;
import org.onosproject.net.device.DeviceProviderService;

import com.vz.onosproject.BlobStore.Blob;
import java.util.List;

public interface ZeromqSBController {

    public void initConnections(DeviceProviderService providerService);

    public void destroyConnections();

    public void writeToDevice(DeviceId deviceId, Blob blob);

    public void monitorConnections();

    public List<String> getAvailableDevices();
}
