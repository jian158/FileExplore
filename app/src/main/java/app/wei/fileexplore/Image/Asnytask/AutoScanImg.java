package app.wei.fileexplore.Image.Asnytask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import app.wei.fileexplore.Image.CreateImgList;
import app.wei.fileexplore.Image.MainLvAdapter;
import app.wei.fileexplore.Image.Myobj.MLvobj;


/**
 * Created by wei on 2017/1/19.
 */

public class AutoScanImg extends AsyncTask {
    private Context context;
    private ArrayList<MLvobj> pathlist;
    private ProgressDialog progressDialog;
    private MainLvAdapter lvAdapter;
    private File[] files;
    public AutoScanImg(Context context, ArrayList list, MainLvAdapter lvAdapter, File[] files)
    {
        this.context=context;
        this.pathlist=list;
        this.files=files;
        this.lvAdapter=lvAdapter;
        progressDialog=new ProgressDialog(context);
        progressDialog.setTitle("自动扫描");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
    }
    @Override
    protected Object doInBackground(Object[] params) {
        CreateImgList createImgList=new CreateImgList();
        pathlist.clear();
        ArrayList<String> tmp;
        int length=files.length;
        for (int i=0;i<length;i++)
        {
            tmp=createImgList.getImgPathList(context,files[i].getPath());
            if (tmp.size()!=0)
            {
                pathlist.add(new MLvobj(files[i].getPath(),tmp.get(0),tmp));
            }
            publishProgress((i*100/length));
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        Log.i("TAG",String.valueOf((Integer) values[0]));
        progressDialog.setProgress((Integer) values[0]);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        lvAdapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }
}
