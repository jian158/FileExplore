package app.wei.fileexplore.Tools;

import android.app.ApplicationErrorReport;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.ResourceBundle;

import app.wei.fileexplore.Image.CreateImgList;
import app.wei.fileexplore.MySQL;
import app.wei.fileexplore.obj.Fileobj;
import app.wei.fileexplore.obj.MainObj;

/**
 * Created by wei on 2017/1/23.
 */

public class SearchThread implements Runnable {
    private MainObj lv;
    public SearchThread(MainObj lv)
    {
        this.lv=lv;
    }
    @Override
    public void run()
    {
        CreateSearchList();
    }

    public void CreateSearchList()
    {
        if (lv.SearchList!=null)
            lv.SearchList.clear();
        lv.SearchList=new ArrayList<>();
        CreateImgList createImgList=new CreateImgList();
        Cursor cursor=createImgList.getcursor(lv.activity,ID.SDCARD);
        if (cursor.moveToFirst())
        {
            int itemid=cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            do {
                lv.SearchList.add(new Fileobj(cursor.getString(itemid),false));
            }while (cursor.moveToNext());
            cursor.close();
        }
    }
}
