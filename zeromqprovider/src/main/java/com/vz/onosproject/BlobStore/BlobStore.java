package com.vz.onosproject.BlobStore;

import org.onosproject.net.DeviceId;

import java.util.List;

/**
 * Created by kspviswa-onos-mcord on 21/1/17.
 */
public interface BlobStore {

    public void InsertBlob(DeviceId device, Blob blob);
    public void RemoveBlob(DeviceId device);
    public List<Blob> getBlobs(DeviceId device);

}
