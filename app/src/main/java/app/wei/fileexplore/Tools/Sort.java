package app.wei.fileexplore.Tools;

import java.util.Comparator;

import app.wei.fileexplore.obj.Fileobj;

/**
 * Created by wei on 2017/1/25.
 */

public class Sort  {

    public static class SortName implements Comparator
    {
        @Override
        public int compare(Object o1, Object o2) {
            Fileobj obj1= (Fileobj) o1;
            Fileobj obj2= (Fileobj) o2;
            return obj1.filename.compareToIgnoreCase(obj2.filename);
        }
    }

    public static class SortTime implements Comparator
    {
        @Override
        public int compare(Object o1, Object o2) {
            Fileobj obj1= (Fileobj) o1;
            Fileobj obj2= (Fileobj) o2;
            if (obj1.file.lastModified()>obj2.file.lastModified())
                return 1;
            else
                return -1;
        }
    }

    public static class SortLength implements Comparator
    {
        @Override
        public int compare(Object o1, Object o2) {
            Fileobj obj1= (Fileobj) o1;
            Fileobj obj2= (Fileobj) o2;
            if (obj1.file.length()>obj2.file.length())
                return 1;
            else
                return -1;
        }
    }
}
