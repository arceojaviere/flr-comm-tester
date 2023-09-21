package org.tests.flr.flrcommtester.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

@ApplicationScoped
public class ExecutorServiceProducer {
    
    @Produces
    @Named("listening-threadpool")
    public ExecutorService createExecutorService(){
        return Executors.newFixedThreadPool(10, r -> new Thread(r, "SOCKET_LISTEN_THREAD"));
    }
    
}
