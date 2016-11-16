package com.sphoton.hoangviet.sixmin.managers;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by hoangviet on 9/7/16.
 */
public class HostReachability {
    private static boolean hasConnection = false;

    public static void checkHostReachable(Context context) throws IOException{
        boolean exists = false;
        SocketAddress sockaddr = new InetSocketAddress("sixmin.sphoton.com", 80);
        Socket sock = new Socket();

        int timeoutMs = 5000;   // 5 seconds
        sock.connect(sockaddr, timeoutMs);
        exists = true;
        if(!HostReachability.isHasConnection()) {
            Log.d("Connection","Reachable");
            setHasConnection(true);
        }
        sock.close();
    }

    public static boolean isHasConnection() {
        return hasConnection;
    }

    public static void setHasConnection(boolean hasConnection) {
        HostReachability.hasConnection = hasConnection;
    }
}
