package app.wei.fileexplore.Image;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import app.wei.fileexplore.Image.Myobj.Gridobj;
import app.wei.fileexplore.R;


/**
 * Created by wei on 2017/1/5.
 */

public class ImgAdpter extends BaseAdapter{
    private Gridobj list;
    private Context context;
    private GridView gridView;
    private boolean isshowcheck;
    public ImgAdpter(Context context, Gridobj gridobj,GridView gridView)
    {
        list=gridobj;
        this.context=context;
        this.gridView=gridView;
        isshowcheck=false;
    }

    @Override
    public int getCount() {
        return list.ImgList.size();
    }

    @Override
    public Object getItem(int position) {
        return list.ImgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private Holder holder;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView==null)
        {
            convertView=View.inflate(context, R.layout.picitem,null);
            holder=new Holder();
            holder.imageView= (ImageView) convertView.findViewById(R.id.imgid);
            holder.checkBox= (CheckBox) convertView.findViewById(R.id.imgcheck);
            convertView.setTag(holder);
        }
        else
            holder = (Holder)convertView.getTag();
        Glide.with(context).load(list.ImgList.get(position)).centerCrop().diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.imageView);
        if (isshowcheck)
            holder.checkBox.setVisibility(View.VISIBLE);
        else
            holder.checkBox.setVisibility(View.GONE);
        holder.checkBox.setChecked(list.IscheckList.get(position));
        holder.checkBox.setOnClickListener(new Check(position));
        return convertView;
    }


    public class Check implements View.OnClickListener{
        private int pos;
        public Check(int pos)
        {
            this.pos=pos;
        }
        @Override
        public void onClick(View v) {
            list.setIscheck(pos,!list.IscheckList.get(pos));
        }
    }
    public void clearcache()
    {
        Glide.get(context).clearMemory();
        new Thread(new Runnable() {
            @Override
            public void run() {

                Glide.get(context).clearDiskCache();
            }
        }).start();
        notifyDataSetChanged();
    }

    public class Holder
    {
        ImageView imageView;
        CheckBox checkBox;
    }
    public void setIsshowcheck()
    {
        isshowcheck=!isshowcheck;
        notifyDataSetChanged();
    }
}
