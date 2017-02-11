package com.test.viswa;

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

/**
 * Created by kspviswa-onos-mcord on 1/12/16.
 */
public class ZeromqClient {

    public static void main (String[] args) {

        if(args.length != 1) {
            System.out.println("Please specify valid device identity");
            System.out.println("Eg : java -jar zeromqclient-1.0-SNAPSHOT-jar-with-dependencies.jar device1");
            System.exit(-1);
        }

        // Prepare our context and subscriber
        Context context = ZMQ.context(1);

        Socket client = context.socket(ZMQ.REQ);
        //Socket client = context.socket(ZMQ.DEALER);
        client.setIdentity(args[0].getBytes());
        client.connect("tcp://localhost:7900");


        while (!Thread.currentThread ().isInterrupted ()) {
            //String add = client.recvStr();
            //String empty = client.recvStr();
            client.send("Ready");
//            System.out.println("Backlog " + client.getBacklog());
//            System.out.println("HasrecieveMore " + client.hasReceiveMore());
            String content = client.recvStr();
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("Content : " + content);
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("");
        }

        /*Socket subscriber = context.socket(ZMQ.SUB);

        subscriber.connect("tcp://localhost:5563");
        subscriber.subscribe("B".getBytes());
        while (!Thread.currentThread ().isInterrupted ()) {
            // Read envelope with address
            String address = subscriber.recvStr ();
            // Read message contents
            String contents = subscriber.recvStr ();
            System.out.println(address + " : " + contents);
        }
        subscriber.close ();*/
        context.term ();
    }
}
