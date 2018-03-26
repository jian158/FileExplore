package app.wei.fileexplore.Image;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import app.wei.fileexplore.R;
import app.wei.fileexplore.obj.Fileobj;

/**
 * Created by wei on 2017/1/13.
 */

public class ImageFileAdapter extends BaseAdapter {
    private ArrayList<Fileobj> List;
    private  Context context;
    public ImageFileAdapter(Context context, ArrayList<Fileobj> list)
    {
        this.context=context;
        this.List=list;
    }
    @Override
    public int getCount() {
        return List.size();
    }

    @Override
    public Object getItem(int position) {
        return List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private Holder holder;
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
        Glide
                .with(context)
                .load(R.drawable.filedir)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop()
                .into(holder.imageView);
        holder.textView.setText(List.get(position).filename);
        holder.checkBox.setVisibility(View.VISIBLE);
        holder.checkBox.setChecked(List.get(position).ischeck);
        holder.checkBox.setOnClickListener(new Check(position));
        return convertView;
    }

    private class Check implements View.OnClickListener{
        private int pos;
        public Check(int pos)
        {
            this.pos=pos;
        }
        @Override
        public void onClick(View v) {
            List.get(pos).setIscheck();
        }
    }
    private class Holder
    {
        public ImageView imageView;
        public TextView textView;
        public CheckBox checkBox;
    }
}
