package app.wei.fileexplore.Image.Myobj;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import app.wei.fileexplore.Image.ImageFile;
import app.wei.fileexplore.Image.ImageFileAdapter;
import app.wei.fileexplore.obj.*;

/**
 * Created by wei on 2017/1/13.
 */

public class addpathobj {
    public View view;
    public ListView file_lv;
    public Button okadd,canceladd;
    public Dialog dialog;
    public ImageFile myfile;
    public ArrayList<app.wei.fileexplore.obj.Fileobj> filelist;
    public ImageFileAdapter fileadapter;
}
