package app.wei.fileexplore.Image;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import app.wei.fileexplore.Image.Filesort.Filesort;
import app.wei.fileexplore.Image.ID.MyID;
import app.wei.fileexplore.Image.Myobj.Gridobj;
import app.wei.fileexplore.R;


public class gridviewac extends Activity {
    private GridView gridView;
    private Button hidebtn;
    private Button delete;
    private ImgAdpter imgAdpter;
    private Gridobj List;
    private LinearLayout linearLayout;
    private int parentitem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridviewac);
        gridView= (GridView) findViewById(R.id.gridview);
        hidebtn= (Button) findViewById(R.id.hidecheckofimg);
        delete= (Button) findViewById(R.id.deleteimg);
        linearLayout= (LinearLayout) findViewById(R.id.btngroup);
        ArrayList<String> arrayList=new ArrayList<String>();
        File file= Environment.getExternalStorageDirectory();
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        arrayList= (ArrayList<String>) bundle.get("pathlist");
        parentitem=(int)bundle.get("itemid");
        Collections.sort(arrayList,new Filesort().sortdate);
        List=new Gridobj(arrayList);
        imgAdpter=new ImgAdpter(this,List,gridView);
        gridView.setAdapter(imgAdpter);
        gridView.setOnItemClickListener(new ItemPIC(List,this));
        gridView.setOnItemLongClickListener(new OnlongItemclick());
        hidebtn.setOnClickListener(new Hidebtn());
    }

    private class Hidebtn implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            imgAdpter.setIsshowcheck();
            linearLayout.setVisibility(View.GONE);

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putStringArrayListExtra("List",List.ImgList);
        intent.putExtra("item",parentitem);
        setResult(MyID.RESULTCODE,intent);
        finish();
//        super.onBackPressed();
    }

    private class OnlongItemclick implements AdapterView.OnItemLongClickListener{
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            linearLayout.setVisibility(View.VISIBLE);
            imgAdpter.setIsshowcheck();
            return true;
        }
    }
    public void DeleteImg(View view)
    {
        for (int i=0;i<List.IscheckList.size();)
        {
            if (List.IscheckList.get(i))
            {
                List.setIscheck(i,false);
                File file=new File(List.ImgList.get(i));
                if (file.exists())
                {
                    List.ImgList.remove(i);
                    List.IscheckList.remove(i);
                    file.delete();
                    MediaScannerConnection.scanFile(this,
                            new String[] { file.toString() }, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> uri=" + uri);
                                }
                            });
                }
                continue;
            }
            i++;
        }
//        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(parentpath))));
//        imgAdpter.clearcache();
        imgAdpter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,MyID.M_SORTMODE,0,"排序");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case MyID.M_SORTMODE:
                setsortmode();break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setsortmode()
    {
        Dialog dialog=new Dialog(this);
        View view=View.inflate(this, R.layout.sortdialog,null);
        RadioGroup radioGroup= (RadioGroup) view.findViewById(R.id.Sort_Group);
        Sort_btn_Onclick onclick=new Sort_btn_Onclick(radioGroup,dialog);
        Button sure= (Button) view.findViewById(R.id.ok);
        Button close= (Button) view.findViewById(R.id.cancel);
        sure.setOnClickListener(onclick);
        close.setOnClickListener(onclick);
        dialog.setTitle("排序方式");
        dialog.setContentView(view);
        dialog.show();
    }

    public class Sort_btn_Onclick implements View.OnClickListener{
        private RadioGroup radioGroup;
        private Dialog dialog;
        public Sort_btn_Onclick(RadioGroup radioGroup,Dialog dialog)
        {
            this.radioGroup=radioGroup;
            this.dialog=dialog;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.ok:
                    new Filesort(getSortMode(radioGroup),List.ImgList);
                imgAdpter.notifyDataSetChanged();
                    dialog.dismiss();
                break;
                case R.id.cancel:
                    dialog.dismiss();break;
            }
        }
    }

    public String getSortMode(RadioGroup radioGroup)
    {
        switch (radioGroup.getCheckedRadioButtonId())
        {
            case R.id.Sort_name:
                return MyID.SORTNAME;
            case R.id.Sort_time:
                return MyID.SORTTIME;
            case R.id.Sort_length:
                return MyID.SORTLENGTH;
        }
        return MyID.SORTTIME;
    }

    private class ItemPIC implements AdapterView.OnItemClickListener
    {
       private Gridobj ImgList;
        private Activity activity;
        public ItemPIC(Gridobj imgList,Activity activity)
        {
            this.activity=activity;
            this.ImgList=imgList;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent();
//            intent.setAction(Intent.ACTION_VIEW);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setDataAndType(Uri.fromFile(new File(ImgList.ImgList.get(position))),"image/*");
//            startActivity(intent);
            intent.setClass(gridviewac.this,showimage.class);
            intent.putStringArrayListExtra("list",ImgList.ImgList);
            intent.putExtra("position",position);
            startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
        }
    }
}
