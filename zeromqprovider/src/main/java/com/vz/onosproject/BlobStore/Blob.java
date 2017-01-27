package com.vz.onosproject.BlobStore;

/**
 * Created by kspviswa-onos-mcord on 21/1/17.
 */
public class Blob {
    public byte[] getBlob() {
        return blob;
    }

    public Blob(byte[] blob) {
        this.blob = blob;
    }

    public void setBlob(byte[] blob) {
        this.blob = blob;
    }

    private byte[] blob;
}
