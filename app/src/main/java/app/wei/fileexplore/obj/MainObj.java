package app.wei.fileexplore.obj;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

import app.wei.fileexplore.Fileadapter;
import app.wei.fileexplore.Myfile;
import app.wei.fileexplore.R;

/**
 * Created by wei on 2017/1/19.
 */

public class MainObj implements Runnable,Serializable{
    public ListView listView;
    public Fileadapter fileadapter;
    public ArrayList<Fileobj> filelist;
    public ArrayList<Fileobj> dirlist;
    public ArrayList<Fileobj> List;
    public Stack<Integer> parentmp;
    public File parentfile;
    public Stack<String> p_zip;
    public int type;
    public String CurrentDir;
    public ArrayList<Fileobj> SearchList;
    public Activity activity;
    private File f;
    private Myhandle myhandle;
    public Toolbar toolbar;
    public boolean IsSelectMode=false;
    public MainObj(Activity activity, File f)
    {
        this.activity=activity;
        this.f=f;
        this.List=new ArrayList<>();
        myhandle=new Myhandle();
        new Thread(this).start();
    }
    public MainObj()
    {
      List=new ArrayList<>();
    }
    public void setParentfile(int type)
    {
        if (parentfile!=null)
        {
            this.CurrentDir=parentfile.getPath();
            this.parentfile=parentfile.getParentFile();
            this.type=type;
            toolbar.setTitle(CurrentDir);
        }

    }
    public void clear()
    {
        filelist.clear();
        dirlist.clear();
        List.clear();
    }
    public void setParentfile(File f,int type)
    {
        parentfile=f.getParentFile();
        CurrentDir=f.getPath();
        toolbar.setTitle(CurrentDir);
        this.type=type;
    }

    public void setParentfile(File file,String f,int type)
    {
        this.parentfile=file;
        File p=new File(f);
        if (p.getParent()==null)
        {
            this.p_zip.push("");
        }
        else {
            this.p_zip.push(p.getParent());
        }
        this.type=type;
    }

    private class Myhandle extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            fileadapter.notifyDataSetChanged();
            toolbar.setTitle(CurrentDir);
        }
    }

    @Override
    public void run() {
        CurrentDir=f.getPath();
        parentmp=new Stack<>();
        p_zip=new Stack<>();
        parentfile=f.getParentFile();
        dirlist=new ArrayList<>();
        filelist=new ArrayList<>();
        this.List=new Myfile(this).getfiles(f);
        myhandle.sendEmptyMessage(1);
    }
}
