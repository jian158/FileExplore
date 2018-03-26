package app.wei.fileexplore.app;

import android.content.Context;
import android.service.voice.VoiceInteractionService;

import java.io.File;
import java.io.Serializable;

import app.wei.fileexplore.Image.Updatamedia;
import app.wei.fileexplore.R;
import app.wei.fileexplore.Tools.ID;
import app.wei.fileexplore.Tools.Mytools;
import app.wei.fileexplore.Tools.SavaData;

/**
 * Created by wei on 2017/1/22.
 */

public class AppObj implements Serializable{
    public String Path;
    public String Name;
    public Object Icon;
    public boolean Ischeck;
    public AppObj(String path)
    {
        this.Path=path;
        this.Ischeck=false;
        this.Name=new File(path).getName();
        this.Icon= R.drawable.apkharmed;
    }

    public void setIcon(Context context)
    {
        setName(context);
        String path=Name+".jpeg";
        String imgtmp=ID.IMGCACHEDIR+"/"+path;
        File tmp=new File(imgtmp);
        if (!tmp.exists())
        {
            SavaData savaData=new SavaData(ID.IMGCACHEDIR,path);
            savaData.saveImg(Mytools.getapkIcon(context,Path));
            new Updatamedia(context).fileScan(imgtmp);
        }
        this.Icon=imgtmp;
    }
    public void setName(Context context)
    {
        this.Name=Mytools.getapkName(context,Path);
    }
    public void setIscheck()
    {
        this.Ischeck=!Ischeck;
    }
    public void setIcon(String icon)
    {
        this.Icon=icon;
    }
}
