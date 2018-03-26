package app.wei.fileexplore.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableLayout;

import java.io.File;
import java.util.ArrayList;

import app.wei.fileexplore.Fileadapter;
import app.wei.fileexplore.Image.CreateImgList;
import app.wei.fileexplore.Image.Updatamedia;
import app.wei.fileexplore.MainActivity;
import app.wei.fileexplore.R;
import app.wei.fileexplore.Tools.ID;
import app.wei.fileexplore.Tools.Mytools;
import app.wei.fileexplore.Tools.SavaData;

public class AppActivity extends Activity {

    private ListView listView;
    private AppAdapter appAdapter;
    private ArrayList<AppObj> list;
    private TableLayout AppbtnGroup;
    private AppHandle appHandle=new AppHandle();
    private final static String APPDATA="appdata.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        listView= (ListView) findViewById(R.id.lv_app);
        AppbtnGroup= (TableLayout) findViewById(R.id.app_view_group);
        list=new SavaData<AppObj>(APPDATA).getList();
        if (list==null)
            list=new ArrayList<>();
        appAdapter=new AppAdapter(this,list);
        listView.setAdapter(appAdapter);
        listView.setOnItemClickListener(new AppOnClick());
        new Thread(new ScanApp()).start();
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                getMenuInflater().inflate(R.menu.favorite,menu);
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId())
        {
            case R.id.love_openpath:
                Intent intent=new Intent();
                intent.putExtra("path",list.get(info.position).Path);
                setResult(20,intent);
                finish();
                break;
            case R.id.love_delete:
                break;
            case R.id.item_select:
                appAdapter.setIshowCheckbox();
                AppbtnGroup.setVisibility(View.VISIBLE);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private class ScanApp implements Runnable{
        private CreateImgList createImgList=new CreateImgList();
        @Override
        public void run() {
            list.clear();
            ArrayList<String> tmp=createImgList.getList(AppActivity.this,"apk");
            for (int i=0;i<tmp.size();i++)
            {
                list.add(new AppObj(tmp.get(i)));
            }
            tmp.clear();
            appHandle.sendEmptyMessage(1);
            for (int i=0;i<list.size();i++)
            {
                list.get(i).setIcon(AppActivity.this);
                appHandle.sendEmptyMessage(1);
            }
        }
    }
    private class AppOnClick implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Mytools.OpenFile(AppActivity.this,list.get(position).Path);
        }
    }

    private class AppHandle extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    appAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }
    public void DeleteApp(View view)
    {
        Updatamedia updatamedia=new Updatamedia(this);
        File file;
        for (int i=0;i<list.size();)
        {
            if (list.get(i).Ischeck)
            {
                list.get(i).setIscheck();
                file=new File(list.get(i).Path);
                if (file.exists())
                    file.delete();
                updatamedia.fileScan(list.get(i).Path);
                list.remove(i);
                continue;
            }
            i++;
        }
        appAdapter.notifyDataSetChanged();
    }

    public void Clappgbtn(View view)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<list.size();i++)
                {
                    if (list.get(i).Ischeck)
                    list.get(i).setIscheck();
                }
                appHandle.sendEmptyMessage(1);
            }
        }).start();
        appAdapter.setIshowCheckbox();
        AppbtnGroup.setVisibility(View.GONE);
    }
    @Override
    protected void onDestroy() {
        SavaData savaData=new SavaData(APPDATA);
        savaData.savedata(list);
        super.onDestroy();
    }
}
