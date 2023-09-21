package org.tests.flr.flrcommtester.rest;

import java.util.Base64;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.resteasy.reactive.RestStreamElementType;
import org.reactivestreams.Publisher;
import org.tests.flr.flrcommtester.model.ReplyEvent;


@Path("/commtest")
public class CommunicationsResource {

    private static final Logger LOGGER = Logger.getLogger(CommunicationsResource.class.getCanonicalName());

    @Inject
    @Channel("frontend-reply-events")
    Publisher<ReplyEvent> replyEventsChannel;

    @Inject
    @Channel("command-stream")
    Emitter<byte[]> commandStream;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    public Publisher<ReplyEvent> streamReplies() {
        return this.replyEventsChannel;
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public void sendCommand(String commandBase64){
        byte[] command;
        LOGGER.info("Received Command (Base64): " + commandBase64);
        command = Base64.getDecoder().decode(commandBase64);
        LOGGER.info("Received Command: " + new String(command));
        this.commandStream.send(command);
    }
}