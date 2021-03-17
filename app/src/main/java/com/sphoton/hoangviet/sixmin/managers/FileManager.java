package com.sphoton.hoangviet.sixmin.managers;

import android.content.Context;
import android.util.Log;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.sphoton.hoangviet.sixmin.App;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by hoangviet on 10/25/16.
 */

public class FileManager {
    public static String getAudioFilePath(Context context, String audioLink){
//        File directory = context.getFilesDir();
//        String fileName = audioLink.substring(audioLink.lastIndexOf('/') + 1);
//        File file = new File(directory, fileName);
//        if (!file.exists()){
//            downloadFile(context, audioLink, file);
//        }
//
//        return file.getAbsolutePath();

        HttpProxyCacheServer proxy = App.getProxy(context);
        proxy.registerCacheListener(new CacheListener() {
            @Override
            public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
                Log.d("LOG","cache available: "+percentsAvailable);
            }
        }, audioLink);
        String proxyUrl = proxy.getProxyUrl(audioLink);

        return proxyUrl;
    }

    public static boolean isDownloaded(Context context, String link){
        HttpProxyCacheServer proxy = App.getProxy(context);

        return proxy.isCached(link);
    }

    private static File downloadFile(Context context, String link, File file){
        try {

            URL url = new URL(link);

            /* Open a connection to that URL. */
            URLConnection ucon = url.openConnection();

           /*
            * Define InputStreams to read from the URLConnection.
            */
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

           /*
            * Read bytes to the Buffer until there is nothing more to read(-1).
            */

            FileOutputStream fos = new FileOutputStream(file);
            int current = 0;
            while ((current = bis.read()) != -1) {
                fos.write(current);
            }

            fos.close();

        } catch (IOException e) {
            Log.d("DownloadManager", "Error: " + e);
        }
        return file;
    }
}
