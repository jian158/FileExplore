package app.wei.fileexplore.Tools;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import static app.wei.fileexplore.Tools.ID.DATADIR;

/**
 * Created by wei on 2017/1/7.
 */

public class SavaData<T>{
    private File file;
    public SavaData(String path)
    {
        try {
        File dir=new File(DATADIR);
            if (!dir.exists())
                dir.mkdirs();
        file=new File(DATADIR+"/"+path);
        if (!file.exists())
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public SavaData(String dirtmp,String path)
    {
        try {
            File dir=new File(dirtmp);
            if (!dir.exists())
                dir.mkdirs();
            file=new File(dir,path);
            if (!file.exists())
                file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean saveImg(Drawable drawable)
    {
        try {
            BitmapDrawable bd= (BitmapDrawable) drawable;
            Bitmap bitmap=bd.getBitmap();
            FileOutputStream os=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,os);
            if (bitmap.isRecycled())
                bitmap.recycle();
            os.close();
        } catch (Exception e) {
            Log.e("error", "保存obj失败");
            return false;
        }
        return true;
    }
    public void savedata(ArrayList list,int flag)
    {
        try {
            FileWriter fileWriter=new FileWriter(file);
            for (int i=0;i<list.size();i++)
            {
               fileWriter.write(list.get(i)+"\n");
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> getdata()
    {
        ArrayList<String> list=new ArrayList<String>();
        try {
            BufferedReader reader=new BufferedReader(new FileReader(file));
            String temp;
            while ((temp=reader.readLine())!=null)
            {
                list.add(temp);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list.size()>0)
        return list;
        else return null;
    }

    public void savedata(ArrayList<T> list)
    {
        try {
            FileOutputStream os=new FileOutputStream(file);
            ObjectOutputStream oos=new ObjectOutputStream(os);
            if (list.size()!=0)
            oos.writeObject(list);
            else
            savedata(list,0);
            os.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("", "保存obj失败");
        }
    }

    public ArrayList<T> getList()
    {
        ArrayList<T> list=null;
        try {
            FileInputStream is=new FileInputStream(file);
            ObjectInputStream ois=new ObjectInputStream(is);
            list= (ArrayList<T>) ois.readObject();
            is.close();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("error", "读取失败");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (list==null)
            return null;
        else if (list.size()==0)
            return null;
        return list;
    }

    public void Write(String content)
    {
        try {
            FileWriter writer=new FileWriter(file);
            writer.write(content+"\n");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String Read()
    {
        String conf=null;
        try {
            BufferedReader reader=new BufferedReader(new FileReader(file));
            String temp;
            while ((temp=reader.readLine())!=null)
            {
                conf=temp;
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (conf==null)
            return ID.FALSE;
        else return conf;
    }

    public void Insert(String content)
    {
        try {
            FileWriter writer=new FileWriter(file,true);
            writer.write(content+"\n");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
