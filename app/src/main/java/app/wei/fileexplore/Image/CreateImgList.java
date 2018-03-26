package app.wei.fileexplore.Image;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by wei on 2017/1/5.
 */

public class CreateImgList {
    public void getImgPathList(Context context,ArrayList list, String imagepath ) {
        String[] selectionargs={imagepath+"%"};
        String selection= MediaStore.Images.Media.DATA+" like ?";
        //new String[] { "_id", "_data" }
        //EXTERNAL_CONTENT_URI
        Cursor cursor =context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null,selection,selectionargs,null);
        while (cursor.moveToNext()) {
            list.add(cursor.getString(1));// 将图片路径添加到list中
        }
        cursor.close();
    }

    public ArrayList<String> getImgPathList(Context context, String imagepath ) {
        ArrayList<String> imglist=new ArrayList<String>();
        String[] selectionargs={imagepath+"%"};
        String selection= MediaStore.Images.Media.DATA+" like ?";
        //new String[] { "_id", "_data" }
        //EXTERNAL_CONTENT_URI
        Cursor cursor =context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null,selection,selectionargs,null);
        while (cursor.moveToNext()) {
            imglist.add(cursor.getString(1));// 将图片路径添加到list中
        }
        cursor.close();
        return imglist;
    }

    public ArrayList getallfile(Context context, String path ) {
        ArrayList<String> list=new ArrayList<>();
        String[] selectionargs={path+"%"};
        String selection= MediaStore.Files.FileColumns.DATA+" like ?";
        String[] projection={MediaStore.Files.FileColumns.DATA};
        Cursor cursor =context.getContentResolver().query(
                MediaStore.Files.getContentUri("external"),projection,selection,selectionargs,null);
        if (cursor.moveToFirst())
        {
             int item=cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
             do
             {
                list.add(cursor.getString(item));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public Cursor getcursor(Context context, String path ) {
        ArrayList<String> list=new ArrayList<>();
        String[] selectionargs={path+"%"};
        String selection= MediaStore.Files.FileColumns.DATA+" like ?";
        String[] projection={MediaStore.Files.FileColumns.DATA};
        Cursor cursor =context.getContentResolver().query(
                MediaStore.Files.getContentUri("external"),projection,selection,selectionargs,null);
        return cursor;
    }

    public Cursor getcursor2(Context context, String path ) {
        ArrayList<String> list=new ArrayList<>();
        String[] selectionargs={path+"%"};
        String selection= MediaStore.Files.FileColumns.DATA+" like ?";
        String[] projection={MediaStore.Files.FileColumns.DATA,MediaStore.Files.FileColumns.TITLE};
//        projection是筛选那一列数据

        Cursor cursor =context.getContentResolver().query(
                MediaStore.Files.getContentUri("external"),projection,selection,selectionargs,null);
        return cursor;
    }

    public ArrayList<String> getList(Context context,String format)
    {
        ArrayList<String> list=new ArrayList<>();
        String[] projection={MediaStore.Files.FileColumns.DATA};
        String[] extention={"%"+"."+format};
        Cursor cursor=context
                .getContentResolver()
                .query(MediaStore.Files.getContentUri("external"),
                        projection,
                        MediaStore.Files.FileColumns.DATA + " like ?",extention,null);
        if (cursor.moveToFirst())
        {
            int itemid=cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            do {
                list.add(cursor.getString(itemid));
            }while (cursor.moveToNext());
        }
        return list;
    }
}
