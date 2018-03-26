package app.wei.fileexplore.Click;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import app.wei.fileexplore.R;
import app.wei.fileexplore.obj.MainObj;
import app.wei.fileexplore.obj.MenuBtnGp;

import static app.wei.fileexplore.R.id.file_unzip;

/**
 * Created by wei on 2017/1/22.
 */

public class MlvLongClick implements View.OnCreateContextMenuListener {

    private Activity activity;
    private MainObj lv;
    public MlvLongClick(Activity activity, MainObj lv)
    {
        this.activity=activity;
        this.lv=lv;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        activity.getMenuInflater().inflate(R.menu.mainlv, menu);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle("菜单");
        MenuItem file_unzip=menu.getItem(2);
        if ("zip".equals(lv.List.get(info.position).Endname))
            file_unzip.setVisible(true);
        else
            file_unzip.setVisible(false);
    }

}
