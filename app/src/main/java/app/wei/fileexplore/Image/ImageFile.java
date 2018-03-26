package app.wei.fileexplore.Image;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import app.wei.fileexplore.obj.Fileobj;


/**
 * Created by wei on 2017/1/13.
 */

public class ImageFile {
    public ArrayList getfiles(File file)
    {
        ArrayList<Fileobj> list=new ArrayList<Fileobj>();
        Log.i("path",file.getPath());
        File[] files=file.listFiles();
        if (files!=null)
        {
            for (File f:files)
            {
                if (f.isDirectory())
                list.add(new Fileobj(f.getPath(),false));
            }
        }

        Log.i("filecount",String.valueOf(list.size()));
        return list;
    }
}
