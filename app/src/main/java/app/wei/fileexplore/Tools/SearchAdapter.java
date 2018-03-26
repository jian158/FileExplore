package app.wei.fileexplore.Tools;

/**
 * Created by wei on 2017/1/23.
 */

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import app.wei.fileexplore.R;
import app.wei.fileexplore.obj.Fileobj;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.ArrayList;


/**
 * Created by wei on 2017/1/13.
 */

public class SearchAdapter extends BaseAdapter {
    private ArrayList<Fileobj> list;
    private Context context;
    public boolean IshowCheckbox;
    private AppHandle appHandle=new AppHandle();
    public SearchAdapter(Context context, ArrayList list)
    {
        this.context=context;
        this.list=list;
        IshowCheckbox=false;
    }

    private void updata(final int pos)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path;
                path=list.get(pos).filepath;
                if (Mytools.getendname(path).equals("apk"))
                {
                    list.get(pos).setFiletype(context);
                    appHandle.sendEmptyMessage(1);
                }
            }
        }).start();
    }

    private class AppHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    notifyDataSetChanged();
                    break;
            }
        }
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private Holder holder;
    private Fileobj obj;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null)
        {
            holder=new Holder();
            convertView=View.inflate(context, R.layout.fileitem,null);
            holder.imageView= (ImageView) convertView.findViewById(R.id.id_fileicon);
            holder.textView= (TextView) convertView.findViewById(R.id.id_filename);
            holder.checkBox= (CheckBox) convertView.findViewById(R.id.file_check);
            convertView.setTag(holder);
        }
        else holder= (Holder) convertView.getTag();
        if (position>=getCount())
            return convertView;
        obj=list.get(position);
        if (obj==null)
            return convertView;
        if (obj.fileicon==null)
        {
            updata(position);
        }
        Glide
                .with(context)
                .load(obj.fileicon)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop()
                .into(holder.imageView);
        holder.textView.setText(obj.filename);
        holder.checkBox.setChecked(obj.ischeck);
        if (IshowCheckbox)
        {
            holder.checkBox.setVisibility(View.VISIBLE);
        }
        else holder.checkBox.setVisibility(View.GONE);
        return convertView;
    }

    public void setIshowCheckbox()
    {
        IshowCheckbox=!IshowCheckbox;
        if (IshowCheckbox==false)
        {
            for (int i=0;i<list.size();i++)
            {
                list.get(i).setIscheck(false);
            }
        }
        this.notifyDataSetChanged();
    }

    private class Check implements View.OnClickListener{
        private int pos;
        public Check(int pos)
        {
            this.pos=pos;
        }
        @Override
        public void onClick(View v) {
            list.get(pos).setIscheck();
        }
    }


    private class Holder
    {
        public ImageView imageView;
        public TextView textView;
        public CheckBox checkBox;
    }
}

