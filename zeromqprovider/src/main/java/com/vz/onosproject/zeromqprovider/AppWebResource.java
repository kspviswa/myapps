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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vz.onosproject.BlobStore.Blob;
import com.vz.onosproject.BlobStore.BlobStore;
import com.vz.onosproject.ZMQAppComponent;
import com.vz.onosproject.provider.zeromq.api.ZeromqSBController;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.onosproject.net.DeviceId;
import org.onosproject.net.flow.FlowRule;
import org.onosproject.rest.AbstractWebResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;


import static org.onlab.util.Tools.nullIsNotFound;

/**
 * Sample web resource.
 */
@Path("/zmq/")
public class AppWebResource extends AbstractWebResource {

    private static final Logger log =
            LoggerFactory.getLogger(AppWebResource.class);

    protected BlobStore store = get(BlobStore.class);

    private ZeromqSBController controller = get(ZeromqSBController.class);

    private final String INVALID_DEVICEID = "No such device available";
    private final String INVALID_FLOW = "Malformed flow payload";
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

    @POST
    @Path("flows")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response persisFlow(InputStream stream) {
        ObjectNode jsonTree = null;
        try {
            jsonTree = (ObjectNode) mapper().readTree(stream);
            JsonNode devId = jsonTree.get("DeviceId");
            JsonNode payload = jsonTree.get("Payload");

            if (devId == null || devId.asText().isEmpty()) {
                throw new IllegalArgumentException(INVALID_DEVICEID);
            }

            if (payload == null || payload.asText().isEmpty()) {
                throw new IllegalArgumentException(INVALID_FLOW);
            }

            DeviceId deviceId = DeviceId.deviceId(devId.asText());
            Blob blob = new Blob(payload.asText().getBytes());

            store.InsertBlob(deviceId, blob);
            controller.writeToDevice(deviceId, blob);

            return Response.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
            return Response.noContent().build();
    }

}
