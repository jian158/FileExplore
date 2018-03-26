package app.wei.fileexplore.Image;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;

import app.wei.fileexplore.Image.Myobj.MLvobj;
import app.wei.fileexplore.R;


/**
 * Created by wei on 2017/1/6.
 */

public class MainLvAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MLvobj> list;
    private ListView listView;
    private Handler handler;
    public MainLvAdapter(Context context, ArrayList tmp, ListView listView) {
        list = tmp;
        this.context = context;
        this.listView = listView;
        handler=new Handler();
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.pathitem, null);
            holder = new Holder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.pathicon);
            holder.pathname = (TextView) convertView.findViewById(R.id.pathname);
            holder.imgcount= (TextView) convertView.findViewById(R.id.imgcount);
            convertView.setTag(holder);
        } else holder = (Holder) convertView.getTag();
            File file = new File(list.get(position).imgpath);
            if (list.get(position).ImgList!=null)
            holder.imgcount.setText(String.valueOf(list.get(position).ImgList.size()));
            holder.pathname.setText(file.getName());
            if (list.get(position).iconpath == null)
                Glide.with(context).load(R.drawable.icon).centerCrop().diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.imageView);
            else
                Glide.with(context).load(list.get(position).iconpath).centerCrop().diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.imageView);
        return convertView;
    }

    private class Holder {
        public ImageView imageView;
        public TextView pathname;
        public TextView imgcount;
    }

}
