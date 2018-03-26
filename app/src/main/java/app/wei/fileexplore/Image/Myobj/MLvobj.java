package app.wei.fileexplore.Image.Myobj;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wei on 2017/1/9.
 */

public class MLvobj implements Serializable{
    public String imgpath;
    public String iconpath;
    public ArrayList<String> ImgList;
    public MLvobj(){};
    public MLvobj(String imgpath,String iconpath)
    {
        this.imgpath=imgpath;
        this.iconpath=iconpath;
    }
    public MLvobj(String imgpath,String iconpath,ArrayList list)
    {
        this.imgpath=imgpath;
        this.iconpath=iconpath;
        this.ImgList=list;
    }
    public void setIconpath(String newpath)
    {
        iconpath=newpath;
    }

}
