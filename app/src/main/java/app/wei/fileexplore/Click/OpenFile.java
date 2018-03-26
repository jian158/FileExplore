package app.wei.fileexplore.Click;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import app.wei.fileexplore.Editor.EditorAc;
import app.wei.fileexplore.Fileadapter;
import app.wei.fileexplore.Image.showimage;
import app.wei.fileexplore.Myfile;
import app.wei.fileexplore.R;
import app.wei.fileexplore.Tools.ID;
import app.wei.fileexplore.obj.Fileobj;
import app.wei.fileexplore.obj.MainObj;

/**
 * Created by wei on 2017/1/19.
 */

public class OpenFile implements AdapterView.OnItemClickListener {
    private Context context;
    private Fileadapter fileadapter;
    private MainObj Lv;
    private Myfile myfile;
    public OpenFile(Context context,MainObj lv)
    {
        this.context=context;
        this.Lv=lv;
        this.fileadapter=lv.fileadapter;
        myfile=new Myfile(lv);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (fileadapter.IshowCheckbox)
        {
            CheckClick(position);
        }
        else
            Open(parent,position);
    }
    private void CheckClick(int pos)
    {
        Lv.List.get(pos).setIscheck();
        fileadapter.notifyDataSetChanged();
    }

    private void Open(AdapterView<?> parent,int position)
    {
        int type=Lv.List.get(position).type;
        String path=Lv.List.get(position).filepath;
        switch (type)
        {
            case ID.FILE_DIR:
                OpenDirectory(parent,path);
                Lv.listView.startLayoutAnimation();
                break;
            case ID.FILE_IMG:
                OpenImgFile(path);
                break;
            case ID.FILE_ZIP:
                OpenZip(parent,path);
                break;
            case ID.FILE_ZIP_DIR:
                OpenZipDir(parent,path,position);
                break;
            case ID.FILE_TEXT:
                OpenEditor(path);
                break;
            default:
                OpenFile(path,Lv.List.get(position).Endname);
        }
    }

    private void OpenEditor(String path){
        Intent intent=new Intent(context, EditorAc.class);
        intent.putExtra("textPath",path);
        context.startActivity(intent);
    }

    private void OpenDirectory(AdapterView<?> parent,String path)
    {
        Lv.clear();
        File file=new File(path);
        Lv.List=myfile.getfiles(file);
        Lv.setParentfile(file,ID.FILE_REGULAR);
        Lv.parentmp.push(parent.getFirstVisiblePosition());
        fileadapter.notifyDataSetChanged();
    }

    private void OpenImgFile(String path)
    {
        Intent intent=new Intent();
        int pos=0;
        intent.setClass(context,showimage.class);
        ArrayList<String> list=new ArrayList<>();
        int length=Lv.List.size();
        for (int i=0;i<length;i++)
        {
            if (Lv.List.get(i).type== ID.FILE_IMG)
            list.add(Lv.List.get(i).filepath);
        }
        for (int i=0;i<list.size();i++)
        {
            if (path.equals(list.get(i)))
            {
                pos=i;
                break;
            }
        }
        intent.putStringArrayListExtra("list",list);
        intent.putExtra("position",pos);
        context.startActivity(intent);
    }

    private void OpenZip(AdapterView<?> parent,String path)
    {
        ArrayList list=myfile.getZipList(new File(path),"");
        if (list.size()==0)
        {
            Toast.makeText(context,"打开失败",Toast.LENGTH_SHORT).show();
            return;
        }
        Lv.List.clear();
        Lv.List=list;
        Lv.setParentfile(new File(path),ID.FILE_REGULAR);
        Lv.parentmp.push(parent.getFirstVisiblePosition());
        fileadapter.notifyDataSetChanged();
    }

    private void OpenZipDir(AdapterView<?> parent,String path,int position)
    {
        path=Lv.List.get(position).zippath;
        File f=Lv.List.get(position).file;
        ArrayList list=myfile.getZipList(f,path);
        if (list.size()==0)
        {
            Toast.makeText(context,"打开失败",Toast.LENGTH_SHORT).show();
            return;
        }
        Lv.List.clear();
        Lv.List=list;
        Lv.setParentfile(f,path,ID.FILE_IN_ZIP);
        Lv.parentmp.push(parent.getFirstVisiblePosition());
        fileadapter.notifyDataSetChanged();
    }

    private void OpenFile(String path,String endname)
    {
        String type=MimeTypeMap.getSingleton().getMimeTypeFromExtension(endname);
        if (type==null)
            type="*/*";
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(path)),type);
        context.startActivity(intent);
    }


}
