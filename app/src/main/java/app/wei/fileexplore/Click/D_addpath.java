package app.wei.fileexplore.Click;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;

import app.wei.fileexplore.Image.MainLvAdapter;
import app.wei.fileexplore.Image.Myobj.MLvobj;
import app.wei.fileexplore.Image.Myobj.addpathobj;
import app.wei.fileexplore.Image.thread.ScanImginit;
import app.wei.fileexplore.R;


/**
 * Created by wei on 2017/1/13.
 */

public class D_addpath implements View.OnClickListener{
    private addpathobj holder;
    private ArrayList<MLvobj> pathlist;
    private Context context;
    private MainLvAdapter lvAdapter;
    public D_addpath(Context context, ArrayList list, addpathobj tmp, MainLvAdapter lvAdapter)
    {
        this.holder=tmp;
        this.pathlist=list;
        this.context=context;
        this.lvAdapter=lvAdapter;
    }
    @Override
    public void onClick(View v) {
        String path;
        switch (v.getId())
        {
            case R.id.okadd:
                for (int i=0;i<holder.filelist.size();i++)
                {
                    if (holder.filelist.get(i).ischeck)
                    {
                        holder.filelist.get(i).setIscheck();
                        path=holder.filelist.get(i).filepath;
                        pathlist.add(new MLvobj(path,null));
                    }
                }
                new Thread(new ScanImginit(context,pathlist,lvAdapter)).start();
                holder.dialog.dismiss();
                break;
            case R.id.canceladd:
                for (int i=0;i<holder.filelist.size();i++)
                {
                    holder.filelist.get(i).setIscheck(false);
                }
                holder.dialog.dismiss();
                break;
        }
        System.gc();
    }
}
