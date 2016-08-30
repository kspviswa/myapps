package org.test;

import org.onosproject.net.Device;
import org.onosproject.net.device.DeviceService;
import org.onosproject.net.device.PortStatistics;
import org.slf4j.Logger;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kspviswa-onos on 15/8/16.
 */
public class portStatsReaderTask {

    class Task extends TimerTask {

        public Device getDevice() {
            return device;
        }
        public DeviceService getDeviceService() {
            return deviceService;
        }
        public long getDelay() {
            return delay;
        }

        @Override
        public void run() {
            while (!isExit()) {
                //log.info("####### Into run() ");
                List<PortStatistics> portStatisticsList = getDeviceService().getPortDeltaStatistics(getDevice().id());
                for (PortStatistics portStats : portStatisticsList) {
                  //  log.info("########## port is " + portStats.port());
                    if (portStats.port() == getPort()) {
                        double rate = (portStats.bytesReceived() / (1024 * 1024));
                        log.info("Port " + port + " Rate " + rate + " MB/s");
                        try {
                            Thread.sleep((getDelay() * 1000));
                            break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void schedule() {
        this.getTimer().schedule(new Task(), 0, 1000);
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    private Timer timer = new Timer();

    public Logger getLog() {
        return log;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    private  Logger log;

    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    private boolean exit;

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    private long delay;

    public PortStatistics getPortStats() {
        return portStats;
    }

    public void setPortStats(PortStatistics portStats) {
        this.portStats = portStats;
    }

    private PortStatistics portStats;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private int port;

    public DeviceService getDeviceService() {
        return deviceService;
    }

    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    protected DeviceService deviceService;

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    private Device device;
}
