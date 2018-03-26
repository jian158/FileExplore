package app.wei.fileexplore;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import app.wei.fileexplore.Image.Updatamedia;
import app.wei.fileexplore.obj.MainObj;
import android.*;
/**
 * Created by wei on 2017/3/4.
 */

public class FileOperate {
    private String newpath;
    private Dialog progressDialog;
    private ProgressBar allTaskBar,singleBar;
    private MainObj lv;
    private Updatamedia updatamedia=null;
    private String oldpath=null;
    private ArrayList<String> list=null;
    private CopyTask task;
    public FileOperate(MainObj lv) {
        task=new CopyTask();
        this.lv = lv;
        updatamedia=new Updatamedia(lv.activity);
        AlertDialog.Builder builder=new AlertDialog.Builder(lv.activity);
        View view=View.inflate(lv.activity, R.layout.progress,null);
        builder.setView(view);
        builder.setCancelable(false);
        progressDialog=builder.create();
        allTaskBar= (ProgressBar) view.findViewById(R.id.progress_alltask);
        singleBar= (ProgressBar) view.findViewById(R.id.progress_single);
    }

    public void CopyFile(String oldpath,ArrayList<String> list) {
        this.list=list;
        this.newpath = lv.CurrentDir;
        this.oldpath=oldpath;
        task.execute();
    }

    public void DeleteFile(int pos) {
        if (!lv.IsSelectMode)
        {
            lv.List.get(pos).file.delete();
            updatamedia.UpdataDb(lv.List.get(pos).filepath);
            lv.List.remove(pos);
            return;
        }
        for (int i=0;i<lv.List.size();)
        {
            if (lv.List.get(i).ischeck)
            {
                lv.List.get(i).file.delete();
                updatamedia.UpdataDb(lv.List.get(i).filepath);
                lv.List.remove(i);
                continue;
            }
            i++;
        }
    }

    public static void reName(final Context context, final MainObj lv, String path, final int pos){
        final File file=new File(path);
        try {
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
//            builder.setCustomTitle(View.inflate(context, R.layout.renametitle,null));
            View view=View.inflate(context, R.layout.rename,null);
            builder.setView(view);
            final Dialog dialog=builder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();
            final Button ok= (Button) view.findViewById(R.id.ok);
            final Button cancel= (Button) view.findViewById(R.id.cancel);
            final EditText text= (EditText) view.findViewById(R.id.renameText);
            text.setText(file.getName());
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File newFile=new File(file.getParent()+File.separator+text.getText().toString());
                    file.renameTo(newFile);
                    dialog.dismiss();
                    Updatamedia updatamedia=new Updatamedia(context);
                    updatamedia.fileScan(file.getPath());
                    updatamedia.fileScan(newFile.getPath());
                    lv.List.get(pos).reName(newFile.getPath());
                    lv.fileadapter.notifyDataSetChanged();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }catch (Exception e){
            Toast.makeText(context,"ReName error!",Toast.LENGTH_SHORT).show();
        }
    }

    private class CopyTask extends AsyncTask {
        Handler handle=new Handler();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                if (!lv.IsSelectMode)
                {
                    Copy(oldpath,newpath);
                    return null;
                }
                else if (list.size()==0)
                {
                    Log.i("NULL","列表空");
                    return null;
                }
                for (int i=0;i<list.size();i++)
                {
                    Copy(list.get(i), newpath);
                    handle.post(new UpdateTask(i+1));
                }
                lv.fileadapter.InitCheck();
                updatamedia.UpdataDb(newpath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        class UpdateTask implements Runnable{
            private int progress;
            public UpdateTask(int progress){
                this.progress=progress;
            }
            @Override
            public void run() {
                allTaskBar.setProgress(progress*100/list.size());
            }
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            singleBar.setProgress((Integer) values[0]);
        }

        @Override
        protected void onPostExecute(Object o) {
            Myfile myfile = new Myfile(lv);
            progressDialog.dismiss();
            lv.clear();
            lv.List = myfile.getfiles(new File(newpath));
            lv.fileadapter.notifyDataSetChanged();
            super.onPostExecute(o);
        }

        public void CopyFile(String oldpath, String newpath) throws IOException {
            FileInputStream inputStream = new FileInputStream(oldpath);
            FileOutputStream outputStream = new FileOutputStream(newpath);
            byte[] bytes = new byte[1024 * 5];
            int l = 0;
            long sum = 0;
            long filelength = new File(oldpath).length();
            int progess;
            while ((l = inputStream.read(bytes)) != -1) {
                sum += l;
                if ((progess=(int) (sum * 100 / filelength))%5==0)
                    publishProgress(progess);
                outputStream.write(bytes, 0, l);
            }
            inputStream.close();
            outputStream.close();
        }

        public void Copy(String oldpath, String newpath) throws IOException {
            File file = new File(oldpath);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                File newdir = new File(newpath + File.separator + file.getName());
                newdir.mkdir();
                for (File f : files) {
                    Copy(f.getPath(), newdir.getPath());
                }
            } else {
                File oldfile = new File(oldpath);
                File newfile = new File(newpath + File.separator + oldfile.getName());
                CopyFile(oldpath, newfile.getPath());
            }
        }
    }

}
