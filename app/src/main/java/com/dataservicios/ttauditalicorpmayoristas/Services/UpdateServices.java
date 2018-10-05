package com.dataservicios.ttauditalicorpmayoristas.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dataservicios.ttauditalicorpmayoristas.Model.Media;
import com.dataservicios.ttauditalicorpmayoristas.Repositories.MediaRepo;
import com.dataservicios.ttauditalicorpmayoristas.app.AppController;
import com.dataservicios.ttauditalicorpmayoristas.util.AuditAlicorp;
import com.dataservicios.ttauditalicorpmayoristas.util.BitmapLoader;
import com.dataservicios.ttauditalicorpmayoristas.util.Connectivity;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Jaime on 25/08/2016.
 */
public class UpdateServices extends Service {
    private final String LOG_TAG = UpdateServices.class.getSimpleName();
    private final Integer contador = 0;

    private Context context = this;

    static final int DELAY = 120000; //2 minutos de espera
    //static final int DELAY = 9000; //9 segundo de espera
    private boolean runFlag = false;
    private Updater updater;

    private AppController application;

    private MediaRepo mediaRepo;

    private AuditAlicorp auditAlicorp;

    private Media media;
    private ArrayList<Media> medias;
    private File file;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        application = (AppController) getApplication();
        updater = new Updater();
        mediaRepo = new MediaRepo(this);
        media = new Media();
        auditAlicorp = new AuditAlicorp(context);
        Log.d(LOG_TAG, "onCreated");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        runFlag = false;
        application.setServiceRunningFlag(false);
        updater.interrupt();
        updater = null;

        Log.d(LOG_TAG, "onDestroyed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!runFlag){
            runFlag = true;
            application.setServiceRunningFlag(true);
            updater.start();
        }

        Log.d(LOG_TAG, "onStarted");
        return START_STICKY;
    }

    private class Updater extends Thread {
        public Updater(){
            super("UpdaterService-UpdaterThread");
        }


        @Override
        public void run() {

            UpdateServices updaterService = UpdateServices.this;
            while (updaterService.runFlag) {
                Log.d(LOG_TAG, "UpdaterThread running");
                try{

//                    int hour = Integer.valueOf(new SimpleDateFormat("k").format(new Date()));
//                    if(hour < 6 || hour > 12){

                        if(Connectivity.isConnected(context)) {
                            if (Connectivity.isConnectedFast(context)) {

//                                Log.i(LOG_TAG," Connectivity fast" );
//                                m = mediaRepo.getFirstMedia();
//                                if(m.getId() != 0){
//                                    // Toast.makeText(context,"Segundo plano",Toast.LENGTH_SHORT);
//                                    boolean response = auditAlicorp.uploadMedia(m,1);
//                                    if (response) {
//                                        mediaRepo.deleteForId(m.getId());
//                                        Log.i(LOG_TAG," Send success images database server and delete local database and file " );
//                                    }
//                                } else {
//                                    Log.i(LOG_TAG, "No found records in media table for send");
//                                }

                                media.setId(0);
                                medias = (ArrayList<Media>) mediaRepo.getAllMedias();
                                for (Media m: medias){
                                    file = null;
                                    file = new File(BitmapLoader.getAlbumDirTemp(context).getAbsolutePath() + "/" + m.getFile());
                                    if(file.exists()){
                                        media = m;
                                        break;
                                    }
                                }
//
                                if (media.getId() != 0){
                                    // NOTA eliminar  de "auditUtil.uploadMedia"
                                    // la eliminación de archivos
                                    //  file.delete() para controlar la eliminación en base de datos
                                    boolean response = auditAlicorp.uploadMedia(media,1);
                                    if (response) {
                                        file = null;
                                        file = new File(BitmapLoader.getAlbumDirTemp(context).getAbsolutePath() + "/" + media.getFile());
                                        if(file.exists()){
                                            file.delete();
//                                            mediaRepo.delete(media);
                                            mediaRepo.deleteForId(media.getId());
                                        }
                                        Log.i(LOG_TAG," Send success images database server and delete local database and file " );
                                    }
                                }

                            }else {
                                Log.i(LOG_TAG," Connectivity slow" );
                            }
                        } else {
                            Log.i(LOG_TAG," No internet connection" );
                        }

//                        Log.i(LOG_TAG,"Se está enviando la foto " + String.valueOf(hour));
//                    } else {
//                        Log.i(LOG_TAG,"No se envía fuera del horario" + String.valueOf(hour));
//                    }







                    Thread.sleep(DELAY);
                }catch(InterruptedException e){
                    updaterService.runFlag = false;
                    application.setServiceRunningFlag(true);
                }

            }
        }


    }
}
