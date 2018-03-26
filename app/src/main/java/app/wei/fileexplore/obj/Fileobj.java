package app.wei.fileexplore.obj;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.service.voice.VoiceInteractionService;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableResource;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;

import java.io.File;
import java.io.Serializable;

import app.wei.fileexplore.Image.Updatamedia;
import app.wei.fileexplore.MainActivity;
import app.wei.fileexplore.R;
import app.wei.fileexplore.Tools.ID;
import app.wei.fileexplore.Tools.Mytools;
import app.wei.fileexplore.Tools.SavaData;

/**
 * Created by wei on 2017/1/13.
 */

public class Fileobj implements Serializable{
    public String filepath;
    public Object fileicon;
    public boolean ischeck;
    public String filename;
    public String Endname=null;
    public int type;
    public File file;
    public String zippath=null;
    public Fileobj(String path, boolean ischeck)
    {
        this.filepath=path;
        file=new File(path);
        this.filename=file.getName();
        this.ischeck=ischeck;
        bootype(path);
    }

    public Fileobj(File file)
    {
        this.filepath=file.getPath();
        this.file=file;
        this.filename=file.getName();
        this.ischeck=false;
        bootype(filepath);
    }

    public Fileobj(File root,String zippath,int typetmp)
    {
        ischeck=false;
        this.file=root;
        this.filepath=root.getPath();
        File f=new File(zippath);
        this.zippath=f.getPath();
        this.filename=f.getName();
        if (typetmp==ID.FILE_ZIP_DIR)
        {
            fileicon= R.drawable.filedir;
            type=ID.FILE_ZIP_DIR;
        }
        else
        {
            setEndname(this.filename);
        }
    }
    public int getType()
    {
        return type;
    }
    public void setFiletype(Context context)
    {
        String path=filename+".jpeg";
        String imgtmp= ID.IMGCACHEDIR+"/"+path;
        File tmp=new File(imgtmp);
        if (!tmp.exists())
        {
            SavaData savaData=new SavaData(ID.IMGCACHEDIR,path);
            boolean b=savaData.saveImg(Mytools.getapkIcon(context,filepath));
            if (b)
                Updatamedia.fileScan(context,imgtmp);
            else {
                this.fileicon= R.drawable.apkharmed;
            }
            savaData=null;
            return;
        }
        this.fileicon=imgtmp;
    }
    public void setName(Context context)
    {
        this.filename=Mytools.getapkName(context,filepath);
    }
    public void setIscheck()
    {
        this.ischeck=!ischeck;
    }
    public void setIscheck(boolean boo)
    {
        this.ischeck=boo;
    }
    private void bootype(String path)
    {
        if (file.isDirectory())
        {
            type=ID.FILE_DIR;
            fileicon= R.drawable.filedir;
        }
        else
        {
            setEndname(path);
        }
    }

    private void setEndname(String path)
    {
        type=ID.FILE_;
        String url=Mytools.getendname(path).toLowerCase();
        switch (url)
        {
            case "apk":
                fileicon=null;
                break;
            case "jpeg":
                type=ID.FILE_IMG;
                fileicon=path;
                break;
            case "jpg":
                type=ID.FILE_IMG;
                fileicon=path;
                break;
            case "png":
                type=ID.FILE_IMG;
                fileicon=path;
                break;
            case "bmp":
                type=ID.FILE_IMG;
                fileicon=path;
                break;
            case "zip":
                type=ID.FILE_ZIP;
                fileicon= R.drawable.fi_zip;
                break;
            case "rar":
                fileicon= R.drawable.fi_zip;
                break;
            case "jar":
                fileicon= R.drawable.fi_zip;
                break;
            case "txt":
            case "c":
            case "cpp":
            case "py":
            case "html":
            case "js":
            case "cs":
            case "java":
                type=ID.FILE_TEXT;
                fileicon= R.drawable.text;
                break;
            default:
                fileicon= R.drawable.fileicon;
        }
        Endname=url;
    }
    public void reName(String path){
        this.filepath=path;
        file=null;
        file=new File(path);
        this.filename=file.getName();
        this.ischeck=false;
        bootype(path);
    }
}
