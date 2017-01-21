package com.vz.onosproject.BlobStore;

import org.onosproject.net.DeviceId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kspviswa-onos-mcord on 21/1/17.
 */
public class DeviceBlob {

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(DeviceId deviceId) {
        this.deviceId = deviceId;
    }

    public void addBlob(Blob blob) {
        blobs.add(blob);
    }

    public void removeBlob(int index) {
        blobs.remove(index);
    }

    public void removeBlobLast() {
        blobs.remove(-1);
    }

    public List<Blob> getBlobs() {
        return blobs;
    }

    DeviceId deviceId;
    List<Blob> blobs = new ArrayList<Blob>();
}
