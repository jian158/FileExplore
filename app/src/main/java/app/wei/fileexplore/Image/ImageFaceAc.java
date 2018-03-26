package app.wei.fileexplore.Image;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.util.ArrayList;

import app.wei.fileexplore.R;

public class ImageFaceAc extends Activity {

    private int position;
    private ViewPager pager;
    private IntruderViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showimage);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        pager= (ViewPager) findViewById(R.id.imgpaper);
        Intent intent=getIntent();
        String action=intent.getAction();
        ArrayList<String> List;
        String path=intent.getDataString().substring(7);
        CreateImgList createImgList=new CreateImgList();
        File file=new File(path);
        List=createImgList.getImgPathList(this,file.getParent());
        for (int i=0;i<List.size();i++)
        {
            if (file.getPath().equals(List.get(i)))
            {
                position=i;
                break;
            }
        }
        pagerAdapter=new IntruderViewPagerAdapter(this,List,this);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(position);
        overridePendingTransition(R.anim.activity, R.anim.exit);
    }

    @Override
    protected void onDestroy() {
        System.gc();
        super.onDestroy();
    }
}
