package app.wei.fileexplore.app;

import android.content.Context;
import android.os.Handler;
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

/**
 * Created by wei on 2017/1/22.
 */

public class AppAdapter extends BaseAdapter {
    private ArrayList<AppObj> list;
    private Context context;
    private boolean IshowCheckbox;
    private Handler handler=new Handler();
    public AppAdapter(Context context, ArrayList list)
    {
        this.context=context;
        this.list=list;
        IshowCheckbox=false;
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
    private AppObj obj;
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
        obj=list.get(position);
        Glide
                .with(context)
                .load(obj.Icon)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop()
                .into(holder.imageView);
        holder.textView.setText(obj.Name);
        holder.checkBox.setChecked(obj.Ischeck);
        holder.checkBox.setOnClickListener(new Check(position));
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
