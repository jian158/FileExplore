package app.wei.fileexplore.Image;

import android.graphics.drawable.Drawable;

import java.io.File;


/**
 * Created by Administrator on 2016/10/14.
 */

public class fileinfo {
    private String path;
    private Drawable icon;
    private File file;
    public fileinfo(String path)
    {
        this.path=path;
        file=new File(path);
    }

    public String getPath()
    {
        return path;
    }
    public Drawable getIcon(){return icon;}
    public String getName(){
        return file.getName();
    }
    public long getdate(){
        File file=new File(path);
        return file.lastModified();
    }
    public long getlengh(){
        return file.length();
    }
    public void Mdicon(Drawable icon)
    {
        this.icon=icon;
    }
    public void Mdpath(String path){ this.path=path;}
    public String getsize()
    {
        return String.valueOf(file.length());
    }
}
