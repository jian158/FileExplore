package app.wei.fileexplore.Image;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import java.util.ArrayList;

import app.wei.fileexplore.R;

public class showimage extends Activity {

    private int position;
    private ArrayList<String> piclist;
    private ViewPager pager;
    private IntruderViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showimage);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        pager= (ViewPager) findViewById(R.id.imgpaper);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        piclist= (ArrayList<String>) bundle.get("list");
        position=(int)bundle.get("position");
        pagerAdapter=new IntruderViewPagerAdapter(this,piclist,this);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(position);
        overridePendingTransition(R.anim.activity, R.anim.exit);
    }

    @Override
    protected void onDestroy() {
        pager.removeAllViews();
        piclist.clear();
        pagerAdapter=null;
        System.gc();
        super.onDestroy();
    }
}
