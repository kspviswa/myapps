package com.vz.onosproject.BlobStore;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.onosproject.net.DeviceId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by kspviswa-onos-mcord on 21/1/17.
 */
@Service
@Component
public class BlobStoreImpl implements BlobStore {

    Map<DeviceId, DeviceBlob> blobmap = new ConcurrentHashMap<>();

    @Override
    public void InsertBlob(DeviceId device, Blob blob) {
        DeviceBlob dblob = blobmap.getOrDefault(device, null);
        if(dblob != null) {
            dblob.addBlob(blob);
        }
    }

    @Override
    public void RemoveBlob(DeviceId device) {
        DeviceBlob dblob = blobmap.getOrDefault(device, null);
        if(dblob != null) {
            dblob.removeBlobLast();
        }
    }

    @Override
    public List<Blob> getBlobs(DeviceId device) {
        DeviceBlob dblob = blobmap.getOrDefault(device, null);
        if (dblob != null) {
            return dblob.getBlobs();
        }
        return null;
    }
}
