/*
 * Copyright 2016-present Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vz.onosproject.zeromqprovider;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vz.onosproject.BlobStore.BlobStore;
import com.vz.onosproject.ZMQAppComponent;
import com.vz.onosproject.provider.zeromq.api.ZeromqSBController;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.onosproject.rest.AbstractWebResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import static org.onlab.util.Tools.nullIsNotFound;

/**
 * Sample web resource.
 */
@Path("provider/zmq/")
public class AppWebResource extends AbstractWebResource {

    private static final Logger log =
            LoggerFactory.getLogger(AppWebResource.class);

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected BlobStore store;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected ZMQAppComponent app;

    @Reference
    private ZeromqSBController controller;
    /**
     * Get hello world greeting.
     *
     * @return 200 OK
     */
    @GET
    @Path("Test")
    public Response getGreeting() {
        String str = "";
        for( String s : controller.getAvailableDevices()) {
            str = str + s + " ";
        }
        ObjectNode node = mapper().createObjectNode().put("Devices : ", str);
        return ok(node).build();
    }

}
