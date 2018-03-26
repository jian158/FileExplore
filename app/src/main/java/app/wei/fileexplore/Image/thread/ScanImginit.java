package app.wei.fileexplore.Image.thread;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

import app.wei.fileexplore.Image.CreateImgList;
import app.wei.fileexplore.Image.MainLvAdapter;
import app.wei.fileexplore.Image.Myobj.MLvobj;


/**
 * Created by wei on 2017/1/13.
 */

public class ScanImginit implements Runnable{
    private CreateImgList tmp;
    private ArrayList<MLvobj> pathlist;
    private Context context;
    private MainLvAdapter lvAdapter;
    private Handler handler=new Handler();
    public ScanImginit(Context context,ArrayList list,MainLvAdapter lvAdapter)
    {
        this.context=context;
        this.pathlist=list;
        tmp = new CreateImgList();
        this.lvAdapter=lvAdapter;
    }

    @Override
    public void run() {
        String path;
        ArrayList tmplist;
        for (int i = 0; i < pathlist.size(); i++) {
            path = pathlist.get(i).imgpath;
            tmplist = pathlist.get(i).ImgList = tmp.getImgPathList(context, path);
            if (tmplist.size() > 0) {
                pathlist.get(i).setIconpath(tmplist.get(0).toString());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        lvAdapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }
}
