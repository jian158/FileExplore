package app.wei.fileexplore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import app.wei.fileexplore.obj.Fileobj;


/**
 * Created by wei on 2017/1/13.
 */

public class MySQL extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "mytable";
    public final static String ID = "id";
    public final static String PATH = "path";
    public final static String SAVE_OBJECTS="object";
//    SQLiteDatabase db;

    public MySQL(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }


    //创建table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" + ID
                + " INTEGER primary key, " + PATH + " text);";
//        key后添加autoincrement自动增加
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);

    }

    public Cursor select() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db
                .query(TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }

    public ArrayList getList()
    {
                Cursor cursor=select();
                ArrayList<Fileobj> tmplist=null;
                if (cursor.moveToFirst())
                {
                    byte[] data=cursor.getBlob(1);

                    try {
                        ByteArrayInputStream is=new ByteArrayInputStream(data);
                        ObjectInputStream ois=new ObjectInputStream(is);
                        tmplist= (ArrayList<Fileobj>) ois.readObject();
                        is.close();
                        ois.close();
                        cursor.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        return tmplist;
    }
    //增加操作
    public long insert(int id, String path)
    {
        long row=-1;
        File file=new File(path);
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID,id);
        cv.put(PATH,path);
        row = db.insert(TABLE_NAME, null, cv);
        return row;
    }

    public void insertobject(int id,Object object)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID,id);
        try {
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            byte data[] = arrayOutputStream.toByteArray();
            cv.put(PATH,data);
            db.insert(TABLE_NAME, null, cv);
            objectOutputStream.close();
            arrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //删除操作
    public void delete(int id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String where =ID + "=?";
//        String[] whereValue ={ Integer.toString(id) };
        db.delete(TABLE_NAME,where,new String[]{String.valueOf(id)});
    }
    //修改操作
    public void update(int id,String path)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String where =ID + "=?";
        String[] whereValue = { Integer.toString(id) };
        ContentValues cv = new ContentValues();
        cv.put(PATH, path);
        db.update(TABLE_NAME, cv, where, whereValue);
    }
}

