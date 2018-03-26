package app.wei.fileexplore;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import java.util.Collections;

import app.wei.fileexplore.Tools.ID;
import app.wei.fileexplore.Tools.SavaData;
import app.wei.fileexplore.Tools.SearchThread;
import app.wei.fileexplore.Tools.Sort;
import app.wei.fileexplore.obj.MainObj;

/**
 * Created by wei on 2017/1/25.
 */

public class Conf {
    private Dialog dialog;
    private RadioGroup radioGroup;
    private CheckBox checkBox;
    private MainObj lv;
    SavaData savaData=new SavaData(ID.DATADIR,ID.CONF);
    private String IsTrue;
    public Conf(MainObj lv)
    {
      this.lv=lv;
      dialog=new Dialog(lv.activity);
      View view=View.inflate(lv.activity,R.layout.conf,null);
      dialog.setContentView(view);
      Button ok= (Button) view.findViewById(R.id.ok);
      Button cancel= (Button) view.findViewById(R.id.cancel);
        click click=new click();
        ok.setOnClickListener(click);
        cancel.setOnClickListener(click);
      checkBox= (CheckBox) view.findViewById(R.id.checkbox_search);
        if (ID.TRUE.equals((IsTrue=savaData.Read())))
            checkBox.setChecked(true);
        else
            checkBox.setChecked(false);
      radioGroup= (RadioGroup) view.findViewById(R.id.Sort_Group);
      dialog.show();
    }

    private class click implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.ok:
                    getSortMode(radioGroup);
                    if (checkBox.isChecked()&&ID.FALSE.equals(IsTrue))
                    {
                        savaData.Write("true");
                        new Thread(new SearchThread(lv)).start();
                    }
                    else
                    {
                        if (lv.SearchList!=null)
                        lv.SearchList.clear();
                        System.gc();
                        savaData.Write("false");
                    }
                    dialog.dismiss();
                    break;
                case R.id.cancel:
                    dialog.dismiss();
                    break;
            }
        }

        public void getSortMode(RadioGroup radioGroup)
        {
            switch (radioGroup.getCheckedRadioButtonId())
            {
                case R.id.Sort_name:
                    Collections.sort(lv.dirlist,new Sort.SortName());
                    Collections.sort(lv.filelist,new Sort.SortName());
                    break;
                case R.id.Sort_time:
                    Collections.sort(lv.dirlist,new Sort.SortTime());
                    Collections.sort(lv.filelist,new Sort.SortTime());
                    break;
                case R.id.Sort_length:
                    Collections.sort(lv.dirlist,new Sort.SortLength());
                    Collections.sort(lv.filelist,new Sort.SortLength());
                    break;
            }
            lv.List.clear();
            lv.List.addAll(lv.dirlist);
            lv.List.addAll(lv.filelist);
            lv.fileadapter.notifyDataSetChanged();
        }
    }
}
