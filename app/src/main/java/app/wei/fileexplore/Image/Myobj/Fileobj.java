package app.wei.fileexplore.Image.Myobj;

import java.io.File;

/**
 * Created by wei on 2017/1/13.
 */

public class Fileobj {
    public String filepath;
    public boolean ischeck;
    public String filename;
    private File file;
    public Fileobj(String path,boolean ischeck)
    {
        this.filepath=path;
        file=new File(path);
        this.filename=file.getName();
        this.ischeck=ischeck;
    }
    public void setIscheck()
    {
        this.ischeck=!ischeck;
    }
    public void setIscheck(boolean boo)
    {
        this.ischeck=boo;
    }

}
