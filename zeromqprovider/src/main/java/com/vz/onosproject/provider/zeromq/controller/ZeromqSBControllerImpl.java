package com.vz.onosproject.provider.zeromq.controller;

import com.vz.onosproject.provider.zeromq.api.ZeromqSBController;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.onosproject.net.DeviceId;
import org.onosproject.net.flow.FlowRule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMsg;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.onosproject.net.DeviceId.deviceId;

/**
 * Created by kspviswa-onos-mcord on 4/12/16.
 */
@Component(immediate = true)
@Service
public class ZeromqSBControllerImpl implements ZeromqSBController{

    private static final Logger log =
            LoggerFactory.getLogger(ZeromqSBControllerImpl.class);

    private final Map<DeviceId, String> deviceMap = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors
                                                    .newFixedThreadPool(1);

    private final Context context = ZMQ.context(1);
    private final Socket routerSocket = context.socket(ZMQ.ROUTER);

    private final String connectionString = "tcp://*:5500";
    private Future<?> listeningTask;

    @Activate
    public void activate() {
        initConnections();
        log.info("Started");
    }

    @Deactivate
    public void deactivate() {
        deviceMap.clear();
        destroyConnections();
        log.info("Stopped");
    }

    @Override
    public void initConnections() {
        routerSocket.bind(connectionString);
        listeningTask = executorService.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                String clientAddress = routerSocket.recvStr();
                String empty = routerSocket.recvStr();
                String clientRequest = routerSocket.recvStr();

                if(clientAddress.equalsIgnoreCase("Ready")) {
                    deviceMap.putIfAbsent(generateDeviceIdFromIdentity(clientAddress),
                                          clientAddress);
                }
            }
        });
    }

    @Override
    public void destroyConnections() {
        executorService.shutdown();
    }

    public DeviceId generateDeviceIdFromIdentity(String identity) {
        DeviceId id = deviceId("0mq://" + identity);
        return id;
    }

    @Override
    public void writeToDevice(FlowRule rule) {
        DeviceId device = rule.deviceId();
        byte[] payload = rule.payLoad().payLoad();
        routerSocket.send(deviceMap.get(device));
        routerSocket.send("");
        routerSocket.send(payload);
    }

    @Override
    public void monitorConnections() {

    }
}
