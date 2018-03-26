package app.wei.fileexplore.Image;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

import app.wei.fileexplore.Click.D_addpath;
import app.wei.fileexplore.Image.Asnytask.AutoScanImg;
import app.wei.fileexplore.Image.ID.MyID;
import app.wei.fileexplore.Image.Myobj.MLvobj;
import app.wei.fileexplore.Image.Myobj.addpathobj;
import app.wei.fileexplore.Image.thread.ScanImginit;
import app.wei.fileexplore.R;
import app.wei.fileexplore.Tools.SavaData;


public class ImageActivity extends AppCompatActivity{
    private ListView listView;
    private ArrayList<MLvobj> pathlist;
    private MainLvAdapter lvAdapter;
    private final static String DATAPATH="FileExploredata.txt";
    private FrameLayout Mlayot;
    private final static String SDCARD=Environment.getExternalStorageDirectory().getPath();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagemainac);
        Mlayot= (FrameLayout) findViewById(R.id.activity_main);
        listView= (ListView) findViewById(R.id.mainlv);
        initList();
        lvAdapter=new MainLvAdapter(this,pathlist,listView);
        listView.setAdapter(lvAdapter);
        new Thread(new ScanImginit(this,pathlist,lvAdapter)).start();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.setClass(ImageActivity.this, gridviewac.class);
                    intent.putExtra("itemid",position);
                    intent.putStringArrayListExtra("pathlist",pathlist.get(position).ImgList);
                    startActivityForResult(intent,100);
            }
        });
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    v.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                        @Override
                        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                            menu.setHeaderTitle("选项");
                            menu.add(0,0,0,"删除");
                        }
                    });
            }
        });
        overridePendingTransition(R.anim.activity, R.anim.exit);
    }

    public void initList()
    {
        File file= new File(SDCARD);
        SavaData savaData=new SavaData(DATAPATH);
        pathlist=savaData.getList();
        if (pathlist==null)
        {
            ArrayList<String> tmp=new ArrayList<String>();
            tmp=new ArrayList<String>();
            tmp.add(file.getPath()+"/DCIM");
            tmp.add(file.getPath()+"/Pictures");
            pathlist=new ArrayList<MLvobj>();
            for (int i=0;i<tmp.size();i++)
            {
                MLvobj mLvobj=new MLvobj();
                mLvobj.imgpath=tmp.get(i);
                mLvobj.iconpath=null;
                pathlist.add(mLvobj);
            }
        }

    }

        private class ScanserImgadd implements Runnable {
            private String path;
            public ScanserImgadd(String path) {
                this.path = path;
            }
            @Override
            public void run() {
                CreateImgList tmp = new CreateImgList();
                ArrayList tmplist = pathlist.get(pathlist.size() - 1).ImgList = tmp.getImgPathList(ImageActivity.this, path);
                if (tmplist.size() > 0) {
                    seticon(tmplist.get(0).toString(), pathlist.size() - 1);
                }

            }
        }

    @Override
    protected void onDestroy() {
        SavaData savaData=new SavaData(DATAPATH);
        savaData.savedata(pathlist);
        super.onDestroy();
    }

    public void seticon(final String imgpath,final int itemid)
    {
       new Handler().post(new Runnable() {
            @Override
            public void run() {
                pathlist.get(itemid).setIconpath(imgpath);
                lvAdapter.notifyDataSetChanged();
            }
        });

    }
    private addpathobj holder=null;
    public void Addpath()
    {

        if (holder==null)
        {
            holder=new addpathobj();
            holder.dialog=new Dialog(this);
            holder.view=View.inflate(this, R.layout.addpathdialog,null);
            holder.okadd=(Button) holder.view.findViewById(R.id.okadd);
            holder.canceladd= (Button)  holder.view.findViewById(R.id.canceladd);
            holder.file_lv=(ListView) holder.view.findViewById(R.id.Lv_pathselect);
            holder.dialog.setContentView(holder.view);
            holder.dialog.setTitle("新增路径");
            holder.myfile=new ImageFile();
            holder.filelist=holder.myfile.getfiles(new File(SDCARD));
            D_addpath onclick=new D_addpath(this,pathlist,holder,lvAdapter);
            holder.okadd.setOnClickListener(onclick);
            holder.canceladd.setOnClickListener(onclick);
            holder.fileadapter=new ImageFileAdapter(this,holder.filelist);
            holder.file_lv.setAdapter(holder.fileadapter);
        }
        holder.dialog.show();
    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int itemid=(int)info.id;
        switch (item.getItemId())
        {
            case 0:
                String tmp=pathlist.get(itemid).imgpath;
                pathlist.remove(itemid);
                lvAdapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MyID.M_ADD,0,"新增");
        menu.add(0,MyID.M_AUTOSEAR,1,"自动搜索");
        menu.add(0,MyID.M_CLEAR,2,"清空列表");
        menu.add(0,MyID.M_TEST_1,3,"测试");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SavaData savaData=new SavaData("datatest");
        switch (item.getItemId()) {
            case MyID.M_ADD:
                Addpath();
                break;
            case MyID.M_AUTOSEAR:
                File file=new File(SDCARD);
                File[] files=file.listFiles();
                if (files==null)
                    break;
                new AutoScanImg(this,pathlist,lvAdapter,files).execute();
                break;
            case MyID.M_CLEAR:
                for (int i=0;i<pathlist.size();i++)
                {
                    pathlist.get(i).ImgList.clear();
                }
                pathlist.clear();
                lvAdapter.notifyDataSetChanged();
                break;
            case MyID.M_TEST_1:
                File tmpfile=new File(SDCARD);
                File[] listfile=tmpfile.listFiles();
                if (listfile==null)
                    break;
                for (File f:listfile)
                {
                    if (f.isFile())
                    {
                        String type=MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(f.toString()));
                        Log.i("type",f.toString()+"\t"+type);
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void flash()
    {
        Updatamedia Updatamedia =new Updatamedia(this);
        for (int j=0;j<pathlist.size();j++) {
            ArrayList<String> tmp = pathlist.get(j).ImgList;
            for (int i = 0; i < tmp.size(); i++) {
                Updatamedia.fileScan(tmp.get(i));
                if (!(new File(tmp.get(i)).exists()))
                    Log.i("scan_dir", tmp.get(i));
            }
        }
        new Thread(new ScanImginit(this,pathlist,lvAdapter)).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==MyID.RESULTCODE)
        {
            int item=data.getIntExtra("item",-1);
            if (item!=-1)
            {
                pathlist.get(item).ImgList=data.getStringArrayListExtra("List");
                lvAdapter.notifyDataSetChanged();
            }
        }
    }
}
