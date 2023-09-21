package org.tests.flr.flrcommtester.service;

import java.io.OutputStream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;


@ApplicationScoped
public class CommandService {

    private static final Logger LOGGER = Logger.getLogger(CommandService.class);

    @Inject
    SocketService socketService;

    @Incoming("command-stream")
    public void sendCommand(byte[] command){
        OutputStream os;
        LOGGER.info("Sending command: " + command);
        try{
            os = this.socketService.getSocketOutputStream();
            os.write(command);
            os.flush();
        }catch(Throwable t){
            throw new RuntimeException("Error writing to the Socket's output stream", t);
        }
    }

    
    
}
