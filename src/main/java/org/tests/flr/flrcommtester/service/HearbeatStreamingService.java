package org.tests.flr.flrcommtester.service;

import java.time.Duration;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.tests.flr.flrcommtester.model.ReplyEvent;

import io.smallrye.mutiny.Multi;

@ApplicationScoped
public class HearbeatStreamingService {
    
    @Outgoing("frontend-reply-events")
    public Multi<ReplyEvent> hearbeat(){
        return Multi.createFrom().ticks().
                    every(Duration.ofSeconds(10))
                    .onOverflow().drop()
                    .map((tick) -> {
                        ReplyEvent event = new ReplyEvent();
                        event.setHeartbeat(true);
                        return event;
                    });
    }
}
