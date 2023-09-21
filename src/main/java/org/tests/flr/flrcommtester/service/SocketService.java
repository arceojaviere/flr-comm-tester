package org.tests.flr.flrcommtester.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public class SocketService {

    private static final Logger LOGGER = Logger.getLogger(SocketService.class);
    
    @ConfigProperty(name = "flrcommtest.ip")
    String ipAddress;

    @ConfigProperty(name = "flrcommtest.port")
    String port;

    private Socket socket;

    void initSocket() throws IOException{
        try{
            if(this.socket == null || this.socket.isClosed()){
                LOGGER.info("(Re-)Creating socket connection");
                this.socket = new Socket(this.ipAddress, Integer.parseInt(port));
            }
        }catch(Throwable t){
            throw new IOException("Error while creating client socket: ", t);
        }

    }

    public InputStream getSocketInputStream() throws IOException{
        InputStream is = null;
        this.initSocket();
        try{
            is = this.socket.getInputStream();
        }catch(Throwable t){
            throw new IOException("Error while getting the socket's input stream: ", t);
        }
        return is;
    }
    public OutputStream getSocketOutputStream() throws IOException{
        OutputStream os = null;
        this.initSocket();
        try{
            os = this.socket.getOutputStream();
        }catch(Throwable t){
            throw new IOException("Error while getting the socket's output stream: ", t);
        }
        return os;
    }

    @PreDestroy
    void clean(){
        try{
            if(!(this.socket == null && this.socket.isClosed())){
                LOGGER.info("Cleaning socket...");
                this.socket.close();
            }
        }catch(Throwable t){
            throw new RuntimeException("Error while cleaning socket: ", t);
        }
    }
}
