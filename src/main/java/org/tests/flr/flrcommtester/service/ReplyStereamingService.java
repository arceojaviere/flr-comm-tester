package org.tests.flr.flrcommtester.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;
import org.tests.flr.flrcommtester.model.ReplyEvent;
import org.tests.flr.flrcommtester.util.LinkedByteBuffer;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.MultiEmitter;

@ApplicationScoped
public class ReplyStereamingService {

    private static final int REQUEST_BUFFER_SIZE = 512;
    private static final Logger LOGGER = Logger.getLogger(ReplyStereamingService.class);

    @Inject
    @Named("listening-threadpool")
    ExecutorService executor;

    @Inject
    SocketService socketService;

    @Outgoing("frontend-reply-events")
    public Multi<ReplyEvent> streamReplies(){
        return Multi.createFrom().<ReplyEvent>emitter(this::listenOnSocket).runSubscriptionOn(this.executor);
    }

    public void listenOnSocket(MultiEmitter<? super ReplyEvent> emitter){
            ReplyEvent replyEvent;
            InputStream is;
            LOGGER.info("Listening on Socket...");
            try{
                is = this.socketService.getSocketInputStream();
                while(true){
                    replyEvent = new ReplyEvent();
                    replyEvent.setPayload(this.getInput(is));
                    emitter.emit(replyEvent);
                }
            }catch(IOException ioexc){
                emitter.fail(ioexc);
            }
            
        }

        private byte[] getInput(InputStream inputStream) {
            byte[] buff;
            int bytesRead = 0;
            LinkedByteBuffer bb = new LinkedByteBuffer();
            try{
                do{
                    buff = new byte[REQUEST_BUFFER_SIZE];
                    bytesRead = inputStream.read(buff);
                    if(bytesRead < 0){
                        break;
                    }else{ 
                        bb.put(buff,0,bytesRead);          
                    }

                    if(inputStream.available() <= 0)
                        break;

                }while(true);

            }catch(Throwable t){
                throw new RuntimeException("Error while getting input", t);
            }
    
            return bb.array();
        }
}
