package app.wei.fileexplore;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Deque;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import app.wei.fileexplore.Tools.Mytools;
import app.wei.fileexplore.extend.CircleImageView;
import app.wei.fileexplore.extend.ThreadPool;
import app.wei.fileexplore.obj.Fileobj;
import app.wei.fileexplore.obj.MainObj;

/**
 * Created by wei on 2017/1/13.
 */

public class Fileadapter extends BaseAdapter implements ListView.OnScrollListener{
    private MainObj lv;
    private Context context;
    public boolean IshowCheckbox;
    private AppHandle appHandle = new AppHandle();
    private ThreadPool pool;
    private LinkedBlockingDeque<Runnable> queue;
    private ShowIcon showIcon;
    private SimpleDateFormat dateFormat;
    private boolean isScrolling;
    public Fileadapter(Context context, MainObj lv) {
        this.context = context;
        this.lv = lv;
        dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        IshowCheckbox = false;
        isScrolling=false;
        queue=new LinkedBlockingDeque<>();
        pool = new ThreadPool(1);
        showIcon = new ShowIcon();
        lv.listView.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState==SCROLL_STATE_IDLE){
            this.isScrolling=false;
            notifyDataSetChanged();
        }
        else{
            this.isScrolling=true;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    private class ShowIcon implements Runnable {
        private int pos;
        private String path;
        public ShowIcon(){

        }
        public ShowIcon(int pos){
            this.pos=pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        @Override
        public void run() {
            path = lv.List.get(pos).filepath;
            if (Mytools.getendname(path).equals("apk")) {
                lv.List.get(pos).setFiletype(context);
                if (!isScrolling&&pool.isFree())
                    appHandle.sendEmptyMessage(1);
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        pool=null;
        Log.i("Tag","free");
        super.finalize();
    }

    private class AppHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public int getCount() {
        return lv.List.size();
    }

    @Override
    public Object getItem(int position) {
        return lv.List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private Holder holder;
    private Fileobj obj;
    private String fileLength;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(context, R.layout.fileitem, null);
            holder.imageView = (CircleImageView) convertView.findViewById(R.id.id_fileicon);
            holder.textView = (TextView) convertView.findViewById(R.id.id_filename);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.file_check);
            holder.textInfo= (TextView) convertView.findViewById(R.id.tv_info);
            convertView.setTag(holder);
        } else holder = (Holder) convertView.getTag();
        obj = lv.List.get(position);
        if (obj.fileicon == null) {
            pool.execute(new ShowIcon(position));
        }
        Glide
                .with(context)
                .load(obj.fileicon)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop()
                .into(holder.imageView);
        holder.textView.setText(obj.filename);
        holder.checkBox.setChecked(obj.ischeck);
        if (obj.file.isDirectory())
            fileLength="";
        else if (obj.file.length()>=(2L<<30))
            fileLength="\t"+String.format("%.2f",(obj.file.length()>>20)/1024.0f)+" GB";
        else if (obj.file.length()>=(2L<<20))
            fileLength="\t"+String.format("%.2f",(obj.file.length()>>10)/1024.0f)+" MB";
        else if (obj.file.length()>=(2L<<10))
            fileLength="\t"+String.format("%.2f",obj.file.length()/1024.0f)+" KB";
        holder.textInfo.setText(dateFormat.format(obj.file.lastModified())+fileLength);
        if (IshowCheckbox) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else holder.checkBox.setVisibility(View.GONE);
        return convertView;
    }

    public void setIshowCheckbox() {
        IshowCheckbox = !IshowCheckbox;
        if (IshowCheckbox == false) {
            for (int i = 0; i < lv.List.size(); i++) {
                lv.List.get(i).setIscheck(false);
            }
        }
        lv.IsSelectMode=!lv.IsSelectMode;
        this.notifyDataSetChanged();
    }

    public void setIshowCheckbox(boolean b)
    {
        IshowCheckbox=b;
        this.notifyDataSetChanged();
    }

    public void InitCheck()
    {
        IshowCheckbox=false;
        lv.IsSelectMode=false;
        for (int i = 0; i < lv.List.size(); i++) {
            lv.List.get(i).setIscheck(false);
        }
    }


    private class Check implements View.OnClickListener {
        private int pos;

        public Check(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            lv.List.get(pos).setIscheck();
        }
    }


    private class Holder {
        public CircleImageView imageView;
        public TextView textView;
        public CheckBox checkBox;
        public TextView textInfo;
    }

}
