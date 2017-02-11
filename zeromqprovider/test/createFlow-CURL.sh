#!/bin/bash

#curl -u onos:rocks -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ "DeviceId" : "zmq:dev1","Payload" : "Sample payload10000000000000"}' http://localhost:8181/onos/zeromqprovider/zmq/flows
curl -u onos:rocks -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d @sample.json http://localhost:8181/onos/zeromqprovider/zmq/flows
