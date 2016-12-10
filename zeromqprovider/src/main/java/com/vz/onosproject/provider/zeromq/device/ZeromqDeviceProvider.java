package com.vz.onosproject.provider.zeromq.device;

import com.vz.onosproject.provider.zeromq.api.AbstractZeromqProvider;
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

    private DeviceProviderService providerService;

    private Map<String, DeviceId> deviceMap;
    private ReentrantReadWriteLock rwl;
    private Lock rl;
    private Lock wl;

    Context context = ZMQ.context(1);
    Socket socket = context.socket(ZMQ.ROUTER);

    //socket.bind( "tcp://localhost:5550");

    private final ExecutorService executorService = Executors
            .newFixedThreadPool(1);

    public void activate() {
        providerService = providerRegistry.register(this);
        deviceMap = new HashMap<String, DeviceId>();
        rwl = new ReentrantReadWriteLock();
        rl = rwl.readLock();
        wl = rwl.writeLock();

        socket.bind("tcp://localhost:5550");

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while(!Thread.currentThread().isInterrupted()) {
                    String clientId = socket.recvStr();
                    DeviceId deviceid = getDevice(clientId);
                    if(deviceid == null)
                    {
                        deviceid = deviceId("0mq://" + clientId);
                        putDevice(clientId, deviceid);
                    }
                }
            }
        });

        log.info("Started");
    }

    public DeviceId getDevice(String key) {
        DeviceId deviceId;
        rl.lock();
        deviceId = deviceMap.getOrDefault(key, null);
        rl.unlock();
        return deviceId;
    }

    public void putDevice(String key, DeviceId value) {
        wl.lock();
        deviceMap.putIfAbsent(key, value);
        wl.unlock();
    }
    public void deactivate() {
        providerRegistry.unregister(this);
        providerService = null;
        executorService.shutdown();
        log.info("Stopped");
    }

    @Override
    public void triggerProbe(DeviceId deviceId) {

    }

    @Override
    public void roleChanged(DeviceId deviceId, MastershipRole mastershipRole) {

    }

    @Override
    public boolean isReachable(DeviceId deviceId) {
        return false;
    }

    @Override
    public void changePortState(DeviceId deviceId, PortNumber portNumber, boolean b) {

    }
}
