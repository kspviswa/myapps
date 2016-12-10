package com.vz.onosproject.provider.zeromq.api;

import org.onosproject.net.provider.AbstractProvider;
import org.onosproject.net.provider.ProviderId;

/**
 * Created by kspviswa-onos-mcord on 2/12/16.
 */

public class AbstractZeromqProvider extends AbstractProvider {

    static final String SCHEME = "zeromq";
    static final String PROVIDER_ID = "com.vz.onosproject.provider.zeromq";

    protected AbstractZeromqProvider() {
        super(new ProviderId(SCHEME, PROVIDER_ID));
    }
}
