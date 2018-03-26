package app.wei.fileexplore.Tools;

import android.os.Environment;

import com.bumptech.glide.load.data.FileDescriptorAssetPathFetcher;

/**
 * Created by wei on 2017/1/21.
 */

public class ID {
    public final static String SDCARD= Environment.getExternalStorageDirectory().getPath();
    public final static int FILE_DIR=0,FILE_=2,FILE_IMG=1,FILE_ZIP=3,FILE_TEXT=4,FILE_ZIP_ROOT=5;
    public final static int FILE_ZIP_DIR=31;
    public final static int FILE_ZIP_FILE=32;
    public final static int FILE_REGULAR=0;
    public final static int FILE_IN_ZIP=1;
    public final static String DATADIR=Environment.getExternalStorageDirectory().getPath()+"/myapp";
    public final static String IMGCACHEDIR=Environment.getExternalStorageDirectory().getPath()+"/myapp/icon";
    public final static String LOVEFILE="love.txt";
    public final static int SORTMODE=1;
    public final static String CONF="conf.txt";
    public final static String FALSE="false";
    public final static String TRUE="true";
}
