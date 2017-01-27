package com.vz.onosproject.provider.zeromq.controller;

import com.google.common.collect.Lists;
import com.vz.onosproject.provider.zeromq.api.ZeromqSBController;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.onlab.packet.ChassisId;
import org.onosproject.net.AnnotationKeys;
import org.onosproject.net.DefaultAnnotations;
import org.onosproject.net.Device;
import org.onosproject.net.DeviceId;
import org.onosproject.net.Port;
import org.onosproject.net.PortNumber;
import org.onosproject.net.SparseAnnotations;
import org.onosproject.net.device.DefaultDeviceDescription;
import org.onosproject.net.device.DefaultPortDescription;
import org.onosproject.net.device.DeviceDescription;
import org.onosproject.net.device.DeviceProviderService;
import org.onosproject.net.device.PortDescription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.onosproject.net.DeviceId.deviceId;

import com.vz.onosproject.BlobStore.Blob;

/**
 * Created by kspviswa-onos-mcord on 4/12/16.
 */
@Component(immediate = true)
@Service
public class ZeromqSBControllerImpl implements ZeromqSBController{

    private static final Logger log =
            LoggerFactory.getLogger(ZeromqSBControllerImpl.class);

    private DeviceProviderService deviceProviderService;
    private final Map<DeviceId, String> deviceMap = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors
                                                    .newFixedThreadPool(1);

    private final Context context = ZMQ.context(1);
    private final Socket routerSocket = context.socket(ZMQ.ROUTER);

    private final String connectionString = "tcp://localhost:7900";
    private Future<?> listeningTask;

    @Activate
    public void activate() {
        //initConnections();
        log.info("ZMQ DeviceController Started");
        //routerSocket.bind(connectionString);
    }

    @Deactivate
    public void deactivate() {
        //deviceMap.clear();
        //destroyConnections();
        log.info("ZMQ DeviceController Stopped");
    }

    @Override
    public void initConnections(DeviceProviderService providerService) {
        this.deviceProviderService = providerService;
        //DeviceId deviceId = generateDeviceIdFromIdentity("test");
        //createDevice(deviceId);
        //log.info("#### Added device " + deviceId.toString());

        routerSocket.bind(connectionString);
        listeningTask = executorService.submit(() -> {
            log.info("### Starting executorService ###");
            while (!Thread.currentThread().isInterrupted()) {
                String clientAddress = routerSocket.recvStr();
                log.info("Got connection from " + clientAddress);
                String empty = routerSocket.recvStr();
                String clientRequest = routerSocket.recvStr();
                ///log.info(" Message content " + clientRequest);
                //log.info("Before generating deviceId");
                DeviceId deviceId = generateDeviceIdFromIdentity(clientAddress);

                //log.info("##### About to enter if");
                if(clientRequest.equalsIgnoreCase("Ready")) {
                    log.info("### Adding device " + deviceId.toString());
                    deviceMap.putIfAbsent(deviceId, clientAddress);
                    createDevice(deviceId);
                }
            }
        });
    }

    /**
     * Generates a list of a configured number of ports.
     *
     * @param portCount number of ports
     * @return list of ports
     */
    protected List<PortDescription> buildPorts(int portCount) {
        List<PortDescription> ports = Lists.newArrayList();
        for (int i = 1; i <= portCount; i++) {
            ports.add(new DefaultPortDescription(PortNumber.portNumber(i), true,
                                                 Port.Type.COPPER, 0));
        }
        return ports;
    }

    public void createDevice(DeviceId id) {
        //int chassisId = Integer.parseInt(id.uri().getSchemeSpecificPart());
        int chassisId = 123123;
        Device.Type type = Device.Type.SWITCH;
        int portCount = 1;
        SparseAnnotations annotations = DefaultAnnotations.builder()
                .set(AnnotationKeys.PROTOCOL, "ZeroMQ")
                .set(AnnotationKeys.CHANNEL_ID, "xxx")
                .set(AnnotationKeys.MANAGEMENT_ADDRESS, "127.0.0.1")
                .build();

        DeviceDescription descBase =
                new DefaultDeviceDescription(id.uri(), type,
                                             "Verizon", "0.1", "0.1", "xxx",
                                             new ChassisId(chassisId));
        DeviceDescription desc = new DefaultDeviceDescription(descBase, annotations);
        deviceProviderService.deviceConnected(id, desc);
        deviceProviderService.updatePorts(id, buildPorts(portCount));
    }
    @Override
    public void destroyConnections() {
        executorService.shutdown();
        routerSocket.close();
        context.term();
    }

    public DeviceId generateDeviceIdFromIdentity(String identity) {
        DeviceId id;
        //id = deviceId("ZMQ:"+identity);
        try {
            id = deviceId(new URI("ZMQ", identity, null));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Unable to build deviceID for device " + toString(), e);
        }
        return id;
    }

    @Override
    public void writeToDevice(DeviceId deviceId, Blob blob) {
        routerSocket.send(deviceMap.get(deviceId));
        routerSocket.send("");
        routerSocket.send(blob.getBlob());
    }

    @Override
    public void monitorConnections() {

    }

    @Override
    public List<String> getAvailableDevices() {
        List<String> devices = new ArrayList<>();
        for( DeviceId d : deviceMap.keySet()) {
            devices.add(d.toString());
        }

        return devices;
    }
}
