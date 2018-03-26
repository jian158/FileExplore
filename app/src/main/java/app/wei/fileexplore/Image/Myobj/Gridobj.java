package app.wei.fileexplore.Image.Myobj;

import java.util.ArrayList;

/**
 * Created by wei on 2017/1/9.
 */

public class Gridobj{
    public ArrayList<String> ImgList;
    public ArrayList<Boolean> IscheckList;
    public Gridobj(ArrayList<String> imgList)
    {
        this.ImgList=imgList;
        IscheckList=new ArrayList<Boolean>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<ImgList.size();i++)
                    IscheckList.add(false);
            }
        }).start();
    }

    public void setIscheck(int itemid,boolean boo)
    {
        IscheckList.set(itemid,boo);
    }

}
