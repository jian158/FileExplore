package app.wei.fileexplore.Tools;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.drm.DrmStore;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.MainThread;
import android.telephony.CellSignalStrengthWcdma;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.TooManyListenersException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.wei.fileexplore.Fileadapter;
import app.wei.fileexplore.Image.CreateImgList;
import app.wei.fileexplore.MySQL;
import app.wei.fileexplore.Myfile;
import app.wei.fileexplore.R;
import app.wei.fileexplore.obj.Fileobj;
import app.wei.fileexplore.obj.MainObj;

/**
 * Created by wei on 2017/1/23.
 */

public class Search {
    private Context context;
    private ArrayList<Fileobj> list;
    private EditText editText;
    private ListView lv_search;
    private SearchAdapter searchAdapter;

    private PatterData patterData;
    private Dialog dialog=null;
    private Button close;
    private Button btn_search;
    private MainObj lv;
    private ProgressBar progressBar;
    private Cursor FilesCursor;
    private Button stopsearch;
    private SearchWatch searchWatch;
    private String[] strings;
    public Search(final Context context,MainObj lv)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CreateImgList createImgList=new CreateImgList();
                FilesCursor=createImgList.getcursor2(context,ID.SDCARD);
                strings=new String[FilesCursor.getCount()];
                int i=0;

                if (FilesCursor.moveToFirst())
                {
                    int pathitem=FilesCursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                    do {
                        strings[i++]=FilesCursor.getString(pathitem);
                    }while (FilesCursor.moveToNext());
                    FilesCursor.close();
                }
            }
        }).start();
        this.lv=lv;
        this.context=context;
        patterData=new PatterData();
        list=new ArrayList<>();
        searchWatch=new SearchWatch();
        InitUi();
    }
    public void StartUi()
    {
        SavaData savaData=new SavaData(ID.DATADIR,ID.CONF);
        if (ID.TRUE.equals(savaData.Read()))
            editText.addTextChangedListener(searchWatch);
        else
            editText.removeTextChangedListener(searchWatch);
        InitUi();
        dialog.show();
    }
    private ThreadPoolExecutor pool=null;
    private BlockingDeque deque=null;
    private void  InitUi()
    {

        if (dialog==null)
        {
            deque=new LinkedBlockingDeque();
            pool=new ThreadPoolExecutor(1,2,50,TimeUnit.SECONDS,deque);
            dialog=new Dialog(context);
            dialog.setTitle("搜索文件");
            View view=View.inflate(context, R.layout.searchview,null);
            dialog.setContentView(view);
            lv_search= (ListView) view.findViewById(R.id.Lv_searchview);
            close= (Button) view.findViewById(R.id.close_search);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Onsearching=false;
                    dialog.dismiss();
                }
            });
            btn_search= (Button) view.findViewById(R.id.btn_search);
            btn_search.setOnClickListener(onClickListener);
            stopsearch= (Button) view.findViewById(R.id.btn_stopsearch);
            stopsearch.setOnClickListener(StopSearch);
            searchAdapter=new SearchAdapter(context,list);
            lv_search.setAdapter(searchAdapter);
            lv_search.setOnItemClickListener(itemClickListener);
            editText= (EditText) view.findViewById(R.id.edit_search);
            progressBar= (ProgressBar) view.findViewById(R.id.bar_progress);
//            editText.addTextChangedListener(searchWatch);
        }
    }
    private String key="";
    private boolean Onsearching;
    private class SearchWatch implements TextWatcher
    {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Onsearching=false;

        }

        @Override
        public void afterTextChanged(Editable s) {
            key=s.toString();
            if (!key.isEmpty())
            {
                pool.execute(patterData);
            }
            else
            {
                list.clear();
                searchAdapter.notifyDataSetChanged();
            }
        }
    }


    private class PatterData implements Runnable{
        private int length;
        private String tmp="";
        private int i=0;
        private int times=0;
        @Override
        public void run() {
                    list.clear();
                    length=lv.SearchList.size()-1;
                    Onsearching=true;
                    times=0;
                    for (i=0;i<length&&Onsearching;i++)
                    {
                        tmp=lv.SearchList.get(i).filename;
                        times++;
                        if (tmp.indexOf(key)>=0&&times<30)
                        {
                            Message message=new Message();
                            message.what=3;
                            message.obj=lv.SearchList.get(i);
//                            myhandle.removeMessages(3,message.obj);
                            myhandle.sendMessage(message);
                        }
                        else if (times==30)
                            times=0;
                    }
//            myhandle.removeMessages(3);
            myhandle.sendEmptyMessage(6);
        }

    }

    private AdapterView.OnItemClickListener itemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Onsearching=false;
            String path=list.get(position).filepath;
            Myfile myfile=new Myfile(lv);
            lv.clear();
            File file=new File(path).getParentFile();
            lv.List=myfile.getfiles(file);
            lv.setParentfile(file,ID.FILE_REGULAR);
            int pos=0;
            for (int i=0;i<lv.List.size();i++)
            {
                if (path.equals(lv.List.get(i).filepath))
                {
                    pos=i;
                    break;
                }
            }
            lv.fileadapter.notifyDataSetChanged();
            lv.listView.setSelection(pos);
            dialog.dismiss();
        }
    };

    private int setProgress=0;

    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           SearchFace();
        }

    };

    private  String tmp="";
    private void SearchFace()
    {
        list.clear();
        Onsearching=true;
        final int length=strings.length;
        SearchTh(0,length/3,2);
        SearchTh(length/3,length/3*2,4);
        SearchTh(length/3*2,length,5);
    }

    private void SearchTh(final int start, final int length, final int what)
    {
        final String s=editText.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=start;i<length&&Onsearching;i++)
                {
                    tmp=new File(strings[i]).getName();
                    if (tmp.indexOf(s)>=0)
                    {
                        Message message=new Message();
                        message.what=what;
                        message.obj=strings[i];
                        myhandle.removeMessages(what,message.obj);
                        myhandle.sendMessage(message);
                    }
                }
                myhandle.removeMessages(what);
            }
        }).start();
    }
    private Myhandle myhandle=new Myhandle();
    private View.OnClickListener StopSearch=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Onsearching=false;
            searchAdapter.notifyDataSetChanged();
        }
    };
    private class Myhandle extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    progressBar.setProgress(setProgress);
                    break;
                case 2:
                    list.add(new Fileobj((String) msg.obj,false));
                    searchAdapter.notifyDataSetChanged();
                    break;
                case 4:
                    list.add(new Fileobj((String) msg.obj,false));
                    searchAdapter.notifyDataSetChanged();
                    break;
                case 5:
                    list.add(new Fileobj((String) msg.obj,false));
                    searchAdapter.notifyDataSetChanged();
                    break;
                case 3:
                    list.add((Fileobj) msg.obj);
                    searchAdapter.notifyDataSetChanged();
                    break;
                case 6:
                    searchAdapter.notifyDataSetChanged();
                    break;
            }

        }
    }

}
