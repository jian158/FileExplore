package app.wei.fileexplore.Image.Filesort;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import app.wei.fileexplore.Image.ID.MyID;
import app.wei.fileexplore.Image.fileinfo;
import app.wei.fileexplore.Tools.Sort;
import app.wei.fileexplore.obj.Fileobj;


/**
 * Created by Administrator on 2016/10/27.
 */

public class Filesort {
    public Sortname sortname;
    public Sortdate sortdate;
    public Sortlengh sortlengh;

    public Filesort()
    {
        sortname=new Sortname();
        sortdate=new Sortdate();
        sortlengh=new Sortlengh();
    }
    public Filesort(String Mode, ArrayList list)
    {
        sortname=new Sortname();
        sortdate=new Sortdate();
        sortlengh=new Sortlengh();
        switch (Mode)
        {
            case MyID.SORTTIME:
                Collections.sort(list,this.sortdate);break;
            case MyID.SORTLENGTH:
                Collections.sort(list,this.sortlengh);break;
            case MyID.SORTNAME:
                Collections.sort(list,this.sortname);break;
        }
    }

    public static void SortList(ArrayList list)
    {
        final Comparator comparator=new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Fileobj f1= (Fileobj) o1;
                Fileobj f2= (Fileobj) o2;
                if (f1.getType()!=0&&f2.getType()!=0)
                    return f1.filename.compareToIgnoreCase(f2.filename);
                else if (f1.getType()==0&&f2.getType()==0)
                    return f1.filename.compareToIgnoreCase(f2.filename);
                else return f1.type-f2.type;
            }
        };
        Collections.sort(list,comparator);
    }
    public class Sortname implements Comparator {
        @Override
        public int compare(Object o, Object t1) {
            File f1=new File(o.toString());
            File f2=new File(t1.toString());
            return f1.getName().compareToIgnoreCase(f2.getName());
        }
    }
    public class Sortdate implements Comparator {
        @Override
        public int compare(Object o, Object t1) {
            fileinfo fileinfo1=new fileinfo(o.toString());
            fileinfo fileinfo2=new fileinfo(t1.toString());
            long date1=fileinfo1.getdate();
            long date2=fileinfo2.getdate();
            if (date1>=date2)return -1;
            else return 1;
        }
    }
    public class Sortlengh implements Comparator {
        @Override
        public int compare(Object o, Object t1) {
            fileinfo fileinfo1=new fileinfo(o.toString());
            fileinfo fileinfo2=new fileinfo(t1.toString());
            long getlengh1=fileinfo1.getlengh();
            long getlengh2=fileinfo2.getlengh();
            if (getlengh1>=getlengh2)return 1;
            else return -1;
        }
    }

}
