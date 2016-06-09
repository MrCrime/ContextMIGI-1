package com.github.migi_1.ContextApp.client;

import android.util.Log;
import com.github.migi_1.ContextApp.MainActivity;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Connects a Client automatically to the first Server found
 * on the LAN.
 * 
 * SINGLETON class.
 */
public final class AutoConnector {
    
    private static final AutoConnector INSTANCE = new AutoConnector();
    
    /**
     * A wrapper of the wrapper of the client.
     */
    private class ClientWrapperWrapper {
        private ClientWrapper wrapper;
    }
    
    /**
     * Gets the instance of this class.
     * 
     * @return 
     *      The instance of this class.
     */
    public static AutoConnector getInstance() {
        return INSTANCE;
    }
    
    /** Private constructor to prevent initialization. */
    private AutoConnector() { }
    
    /**
     * Automatically starts the  client by connecting it to the
     * first Server found on this LAN.
     * 
     * @param executorService
     *      On which executorService the server finder should be executed.
     * 
     * @return The clientWrapper inside the clientWrapperWrapper
     */
    public ClientWrapper autoStart(ExecutorService executorService, 
            MainActivity main) {
        ServerFinder serverFinder = ServerFinder.getInstance();
        
        ClientWrapperWrapper client = new ClientWrapperWrapper();
        serverFinder.findServers(executorService, getConnector(client));
        int counter = 0;
        
        while (client.wrapper == null) {
            if (counter == 500) {
                break;
            }
            
            Log.d("CarrierAway", "Waiting for client to connect");
            counter++;
        }
        
        if (client.wrapper == null) {
            main.alert();
        }
            
        Log.d("CarrierAway", "Connected succesfully");
        return client.wrapper;
    }
    
    /**
     * @param client 
     *      The client that should be started on servery discovery.
     * @return
     *      A ServerDiscoveryHandler that connects the client to a server
     *      that has been discovered.
     */
    private ServerDiscoveryHandler getConnector(final ClientWrapperWrapper client) {
        return new ServerDiscoveryHandler() {
            @Override
            public void onServerDiscovery(InetAddress server) {
                try {
                    client.wrapper = new ClientWrapper(server.getHostAddress());
                    ServerFinder.getInstance().stop();
                } catch (Exception e) {
                    e.printStackTrace();
                    //Connection failed. Trying again.
                    onServerDiscovery(server);
                }
            }
        };
    }
    
}
