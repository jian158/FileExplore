package app.wei.fileexplore.Tools;

import android.content.Context;
import android.os.storage.StorageManager;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wei on 2017/3/9.
 */

public class Volume {

    public ArrayList<StorageInfo> getStorageList(Context context) {
        ArrayList<StorageInfo> storagges = new ArrayList<StorageInfo>();
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            Class<?>[] paramClasses = {};
            Method getVolumeList = StorageManager.class.getMethod("getVolumeList", paramClasses);
            getVolumeList.setAccessible(true);
            Object[] params = {};
            Object[] invokes = (Object[]) getVolumeList.invoke(storageManager, params);
            if (invokes != null) {
                StorageInfo info = null;
                for (int i = 0; i < invokes.length; i++) {
                    Object obj = invokes[i];
                    Method getPath = obj.getClass().getMethod("getPath", new Class[0]);
                    String path = (String) getPath.invoke(obj, new Object[0]);
                    info = new StorageInfo(path);
                    File file = new File(info.path);
                    if ((file.exists()) && (file.isDirectory()) && (file.canWrite())) {
                        Method isRemovable = obj.getClass().getMethod("isRemovable", new Class[0]);
                        String state = null;
                        try {
                            Method getVolumeState = StorageManager.class.getMethod("getVolumeState", String.class);
                            state = (String) getVolumeState.invoke(storageManager, info.path);
                            info.state = state;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (info.isMounted()) {
                            info.isRemoveable = (Boolean) isRemovable.invoke(obj, new Object[0]);
                            storagges.add(info);
                        }
                    }
                }
            }
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        storagges.trimToSize();

        return storagges;
    }

    public class StorageInfo
    {
        public String path;
        public String state;
        public boolean isRemoveable;

        public StorageInfo(String path)
        {
            this.path = path;
        }
        public boolean isMounted() {
            return "mounted".equals(state);
        }
    }
}
