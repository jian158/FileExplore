package app.wei.fileexplore;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.service.voice.VoiceInteractionService;
import android.support.annotation.RequiresApi;
import android.text.LoginFilter;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import app.wei.fileexplore.Image.Filesort.Filesort;
import app.wei.fileexplore.Image.ID.MyID;
import app.wei.fileexplore.Image.Updatamedia;
import app.wei.fileexplore.Tools.ID;
import app.wei.fileexplore.Tools.Mytools;
import app.wei.fileexplore.obj.Fileobj;
import app.wei.fileexplore.obj.MainObj;


/**
 * Created by wei on 2017/1/13.
 */

public class Myfile {
    private MainObj lv;
    private Context context;
    public Myfile(MainObj lv)
    {
        this.lv=lv;
    }
    public Myfile(Context context,MainObj lv)
    {
        this.context=context;
        this.lv=lv;
    }
    public ArrayList<Fileobj> getfiles(File file)
    {
        ArrayList<Fileobj> list=new ArrayList<Fileobj>();
        if (file==null)
        return list;
        File[] files=file.listFiles();
        if (files!=null)
        {
            for (File f:files)
            {
                list.add(new Fileobj(f));
//                if (f.isDirectory())
//                    lv.dirlist.add(new Fileobj(f.getPath(),false));
//                else lv.filelist.add(new Fileobj(f.getPath(),false));
            }
//            list.addAll(lv.dirlist);
//            list.addAll(lv.filelist);
            Filesort.SortList(list);
        }
        return list;
    }


    public ArrayList getZipList(File root, String path)
    {
        ArrayList<Fileobj> list=new ArrayList<>();
        int length;
        if (path.equals(""))
            length=0;
        else length=path.length();
        Enumeration enumeration= null;
        try {
            enumeration = getEnumeration(root);
            if (enumeration==null)
                return list;
            lv.filelist.clear();
            lv.dirlist.clear();
            ZipEntry entry;
            String tmp;
            String dir="";
            String name;
            HashMap<String,String> map=new HashMap<>();
            while (enumeration.hasMoreElements())
            {
                entry= (ZipEntry) enumeration.nextElement();
                name=getname(entry);
                tmp=Mytools.Substring(name,length);
                if (!path.equals(tmp))  //比较是否是path文件夹下的文件
                    continue;
                if (!path.equals("")&&!Mytools.IsChar(name,path.length()))//比较是否是path文件夹下的文件
                    continue;
                dir=Mytools.getsubstring(name,length+1);
                if (map.containsKey(dir))//判断是否已经添加到list
                    continue;
                if (Mytools.Ishasstring(name,length+1))
                {
                    map.put(dir,dir);
                    lv.dirlist.add(new Fileobj(root,dir,ID.FILE_ZIP_DIR));
                }
                else lv.filelist.add(new Fileobj(root,dir,ID.FILE_ZIP_FILE));
            }
            map=null;
            list.addAll(lv.dirlist);
            list.addAll(lv.filelist);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    public Enumeration getEnumeration(File zipfile)
    {
        ZipFile file= null;
        try {
            file = new ZipFile(zipfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file!=null)
        return file.entries();
        return null;
    }

    public String getname(ZipEntry entry) throws ZipException,IOException,UnsupportedEncodingException
    {
        return entry.getName();
    }

    public static void UnZip(String dir,String path)
    {
        Charset charset=Charset.forName("GBK");
        ZipFile zipFile= null;
        try {
            zipFile = new ZipFile(path);
            Enumeration enumeration=zipFile.entries();
            ZipEntry entry;
            while (enumeration.hasMoreElements())
            {
                entry= (ZipEntry) enumeration.nextElement();
                if (entry.isDirectory())
                {
                    File f=new File(dir+"/"+entry.getName());
                    f.mkdirs();
                }
                else
                {
                    File f=new File(dir+"/"+entry.getName());
                    f.getParentFile().mkdirs();
                    f.createNewFile();
                    InputStream inputStream=zipFile.getInputStream(entry);
                    FileOutputStream out=new FileOutputStream(f);
                    int length=0;
                    byte[] bytes=new byte[10240];
                    while ((length=inputStream.read(bytes))!=-1)
                    {
                        out.write(bytes,0,length);
                    }
                    inputStream.close();
                    out.close();
                }
            }
            if (zipFile!=null)
            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
