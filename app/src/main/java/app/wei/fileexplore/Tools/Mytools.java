package app.wei.fileexplore.Tools;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.ResourceBundle;
import java.util.concurrent.ForkJoinTask;

import app.wei.fileexplore.R;

/**
 * Created by wei on 2017/1/20.
 */

public class Mytools {

    public static Drawable getapkIcon( Context context,String absPath) {

        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = pm.getPackageArchiveInfo(absPath, PackageManager.GET_ACTIVITIES);
        if (pkgInfo != null) {
            ApplicationInfo appInfo = pkgInfo.applicationInfo;
        /* 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
            appInfo.sourceDir = absPath;
            appInfo.publicSourceDir = absPath;
//            String appName = pm.getApplicationLabel(appInfo).toString();// 得到应用名
//            String packageName = appInfo.packageName; // 得到包名
//            String version = pkgInfo.versionName; // 得到版本信息
//        /* icon1和icon2其实是一样的 */
//            Drawable icon1 = pm.getApplicationIcon(appInfo);// 得到图标信息
            Drawable icon2 = appInfo.loadIcon(pm);
            if (icon2!=null)
                return icon2;
        }

        return context.getDrawable(R.drawable.apkharmed);
    }

    public static String getapkName( Context context,String absPath) {

        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = pm.getPackageArchiveInfo(absPath, PackageManager.GET_ACTIVITIES);
        if (pkgInfo != null) {
            ApplicationInfo appInfo = pkgInfo.applicationInfo;
        /* 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
            appInfo.sourceDir = absPath;
            appInfo.publicSourceDir = absPath;
            String appName = pm.getApplicationLabel(appInfo).toString();// 得到应用名
//            String packageName = appInfo.packageName; // 得到包名
//            String version = pkgInfo.versionName; // 得到版本信息
//        /* icon1和icon2其实是一样的 */
//            Drawable icon1 = pm.getApplicationIcon(appInfo);// 得到图标信息
            return appName;
        }
        return "此包已损坏";
    }

    public static String getendname(String path)
    {
        int i=path.lastIndexOf(".");
        if (i<0)
            return "*/*";
        String end=path.substring(i+1).toLowerCase();
        return end;
    }
    public static String getsubstring(String string,int sl)
    {
        char[] ch=string.toCharArray();
        int i=0;
        for (i=sl;i<ch.length;i++)
        {
            if (ch[i]=='/')
                break;
        }
        return string.substring(0,i);
    }

    public static boolean IsChar(String string,int pos)
    {
        if (string.length()<pos)
            return false;
        else if (string.toCharArray()[pos]=='/'&&string.length()>pos+1)
            return true;
        return false;
    }
    public static String Substring(String string,int end)
    {
        if (end>string.length())
            return string.substring(0);
        else return string.substring(0,end);
    }
    public static boolean Ishasstring(String string,int start)
    {
        char[] ch=string.toCharArray();
        for (int i=start;i<ch.length;i++)
        {
            if (ch[i]=='/')
            {
                return true;
            }
        }
        return false;
    }

    public static void OpenFile(Context context,String path)
    {
        String endname=Mytools.getendname(path);
        String type= MimeTypeMap.getSingleton().getMimeTypeFromExtension(endname);
        if (type==null)
            type="*/*";
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(path)),type);
        context.startActivity(intent);
    }
}
