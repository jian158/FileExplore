package app.wei.fileexplore.Image;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * Created by wei on 2017/1/12.
 */

public class Updatamedia {
    public Context context;
    public Updatamedia(Context context)
    {
        this.context=context;
    }
    public void scanSdCard(){
        String file= Environment.getExternalStorageDirectory().getAbsolutePath()+"/Photo";
        UpdataDb(file);
    }

    public void fileScan(String file){
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
    }

    public static void fileScan(Context context,String file){
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
    }

    public void UpdataDb(String path){
        File file = new File(path);

        if(file.exists() && file.isDirectory()){
            File[] array = file.listFiles();

            for(int i=0;i<array.length;i++){
                File f = array[i];

                if(f.isFile()){//FILE TYPE
                    String name = f.getName();
                    fileScan(f.getPath());
                }
                else {//FOLDER TYPE
                    UpdataDb(f.getAbsolutePath());
                }
            }
        }
        else
        {
            fileScan(file.getPath());
        }

    }

    public static final String ACTION_MEDIA_SCANNER_SCAN_DIR = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";
    public void scanDirAsync(String dir) {
        Intent scanIntent = new Intent(ACTION_MEDIA_SCANNER_SCAN_DIR);
        scanIntent.setData(Uri.fromFile(new File(dir)));
        context.sendBroadcast(scanIntent);
    }
}
