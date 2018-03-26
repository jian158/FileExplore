package app.wei.fileexplore;


import android.content.Intent;

import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;

import android.text.Layout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;

import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import app.wei.fileexplore.Click.MlvLongClick;
import app.wei.fileexplore.Click.OpenFile;
import app.wei.fileexplore.Image.ImageActivity;
import app.wei.fileexplore.Image.Updatamedia;
import app.wei.fileexplore.Tools.ID;
import app.wei.fileexplore.Tools.SavaData;
import app.wei.fileexplore.Tools.Search;
import app.wei.fileexplore.Tools.SearchThread;
import app.wei.fileexplore.app.AppActivity;
import app.wei.fileexplore.favorite.FavoriteAc;
import app.wei.fileexplore.obj.MainObj;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private MainObj Lv;
    private LinearLayout footLayout;
    private Button ok,cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        setContentView(R.layout.activity_main);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextAppearance(this, R.style.TitleText);//只能动态设置
        Lv=new MainObj(this,new File(ID.SDCARD));
        Lv.toolbar=toolbar;
        InitUi();
        setSupportActionBar(toolbar);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackFile();
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Lv.fileadapter.setIshowCheckbox();
                return true;
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        overridePendingTransition(R.anim.activity, R.anim.exit);
    }

    private Search search;
    public void InitUi()
    {

        footLayout= (LinearLayout) findViewById(R.id.mainfoot);
        ok= (Button) findViewById(R.id.ok);
        cancel= (Button) findViewById(R.id.cancel);
        Lv.listView= (ListView) findViewById(R.id.lv);
        Lv.fileadapter=new Fileadapter(this,Lv);
        Lv.listView.setAdapter(Lv.fileadapter);
        Lv.listView.setOnItemClickListener(new OpenFile(this,Lv));
        Lv.listView.setOnCreateContextMenuListener(new MlvLongClick(this,Lv));
        LayoutAnimationController controller=new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.openfile));
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        Lv.listView.setLayoutAnimation(controller);
        search=new Search(this,Lv);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SavaData savaData=new SavaData(ID.DATADIR,ID.CONF);
                if (savaData.Read().equals(ID.TRUE))
                {
                    new SearchThread(Lv).CreateSearchList();
                }
            }
        }).start();
    }

    public  void BackFile()
    {
        Lv.clear();
        Myfile myfile=new Myfile(Lv);
        switch (Lv.type)
        {
            case ID.FILE_REGULAR:
                Lv.List=myfile.getfiles(Lv.parentfile);
                Lv.setParentfile(ID.FILE_REGULAR);
                break;
            case ID.FILE_IN_ZIP:
                ArrayList list;
                list=myfile.getZipList(Lv.parentfile,Lv.p_zip.pop());
                if (Lv.p_zip.empty())
                {
                    Lv.setParentfile(ID.FILE_REGULAR);
                }
                if (list.size()==0)
                {
                    Toast.makeText(this,"打开失败",Toast.LENGTH_SHORT).show();
                    break;
                }
                Lv.List.clear();
                Lv.List=list;
                break;
        }

        if (Lv.List==null||Lv.List.size()==0)
            finish();
        Lv.fileadapter.notifyDataSetChanged();
        if (!Lv.parentmp.isEmpty())
            Lv.listView.setSelection((int)Lv.parentmp.pop());
        Lv.listView.startLayoutAnimation();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            BackFile();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_hidecheck:
                Lv.fileadapter.setIshowCheckbox();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private Intent intent=new Intent();
    private Notify notify=null;
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            intent.setClass(this, AppActivity.class);
            startActivityForResult(intent,100);
        } else if (id == R.id.nav_gallery) {
            intent.setClass(this, ImageActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            intent.setClass(this, FavoriteAc.class);
            startActivityForResult(intent,100);
        } else if (id == R.id.nav_manage) {
            new Conf(Lv);
        } else if (id == R.id.nav_share) {
            search.StartUi();
        } else if (id == R.id.nav_send) {
           if (notify==null)
               notify=new Notify(this);
            notify.show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private ArrayList<String> slist=new ArrayList<>();
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos=info.position;
        if (Lv.List.get(pos).zippath!=null)
        {
            Toast.makeText(MainActivity.this,"此文件不能收藏！",Toast.LENGTH_SHORT).show();
            return false;
        }
        final String path=Lv.List.get(pos).filepath;
        final FileOperate operate=new FileOperate(Lv);
        slist.clear();
        switch (item.getItemId())
        {
            case R.id.file_copy:
                for (int i=0;i<Lv.List.size();i++)
                {
                    if (Lv.List.get(i).ischeck)
                        slist.add(Lv.List.get(i).filepath);
                }
                footLayout.setVisibility(View.VISIBLE);
                Lv.fileadapter.setIshowCheckbox(false);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        operate.CopyFile(path,slist);
                        footLayout.setVisibility(View.GONE);
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Lv.fileadapter.InitCheck();
                        footLayout.setVisibility(View.GONE);
                    }
                });

                break;
            case R.id.file_delete:
                operate.DeleteFile(pos);
                Lv.fileadapter.InitCheck();
                Lv.fileadapter.notifyDataSetChanged();
                break;
            case R.id.file_rename:
                FileOperate.reName(this,Lv,path,pos);
                break;
            case R.id.file_love:
                SavaData savaData=new SavaData(ID.LOVEFILE);
                savaData.Insert(path);
                break;
            case R.id.file_unzip:
                footLayout.setVisibility(View.VISIBLE);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Lv.clear();
                        Myfile myfile=new Myfile(Lv);
                        Myfile.UnZip(Lv.CurrentDir,path);
                        new Updatamedia(MainActivity.this).UpdataDb(Lv.CurrentDir);
                        Lv.List=myfile.getfiles(new File(Lv.CurrentDir));
                        Lv.fileadapter.notifyDataSetChanged();
                        footLayout.setVisibility(View.GONE);
                    }
                });
                break;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==20)
        {
            String path=data.getStringExtra("path");
            Myfile myfile=new Myfile(Lv);
            Lv.clear();
            File file=new File(path).getParentFile();
            Lv.List=myfile.getfiles(file);
            Lv.setParentfile(file,ID.FILE_REGULAR);
            int pos=0;
            for (int i=0;i<Lv.List.size();i++)
            {
                if (path.equals(Lv.List.get(i).filepath))
                {
                    pos=i;
                    break;
                }
            }
            Lv.fileadapter.notifyDataSetChanged();
            Lv.listView.setSelection(pos);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
