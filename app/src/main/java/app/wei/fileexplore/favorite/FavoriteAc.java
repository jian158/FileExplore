package app.wei.fileexplore.favorite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import app.wei.fileexplore.Click.MlvLongClick;
import app.wei.fileexplore.Fileadapter;
import app.wei.fileexplore.R;
import app.wei.fileexplore.Tools.ID;
import app.wei.fileexplore.Tools.SavaData;
import app.wei.fileexplore.obj.Fileobj;
import app.wei.fileexplore.obj.MainObj;

public class FavoriteAc extends Activity {
    private Fileadapter fileadapter;
    private ListView listView;
    private MainObj oo;
    private AppHandle appHandle=new AppHandle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        listView= (ListView) findViewById(R.id.lv_love);
        SavaData savaData=new SavaData(ID.LOVEFILE);
        oo=new MainObj();
        ArrayList<String> tmp=savaData.getdata();
        if (tmp==null)
            tmp=new ArrayList<>();
        for (int i=0;i<tmp.size();i++)
        {
            oo.List.add(new Fileobj(tmp.get(i),false));
        }
        oo.listView=listView;
        fileadapter=new Fileadapter(this,oo);
        listView.setAdapter(fileadapter);
        listView.setOnCreateContextMenuListener(this);
        new Thread(new CheckRun()).start();
    }
    private class CheckRun implements Runnable{
        @Override
        public void run() {
            for (int i=0;i<oo.List.size();i++)
            {
                if ("apk".equals(oo.List.get(i).Endname))
                {
                    oo.List.get(i).setFiletype(FavoriteAc.this);
                    oo.List.get(i).setName(FavoriteAc.this);
                    appHandle.sendEmptyMessage(1);
                }
            }
        }
    }
    private class AppHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    fileadapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.favorite,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId())
        {
            case R.id.love_openpath:
                Intent intent=new Intent();
                intent.putExtra("path",oo.List.get(info.position).filepath);
                setResult(20,intent);
                finish();
                break;
            case R.id.love_delete:
                break;
        }
        return super.onContextItemSelected(item);
    }
}
