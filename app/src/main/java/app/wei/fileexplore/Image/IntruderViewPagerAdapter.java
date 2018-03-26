package app.wei.fileexplore.Image;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import app.wei.fileexplore.R;
import app.wei.fileexplore.extend.ThreadPool;

/**
 * Created by Administrator on 2016/10/22.
 */

public class IntruderViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> mDrawableResIdList;
    private int window_width, window_height;// 控件宽度
    private int state_height;// 状态栏的高度
    private ViewTreeObserver viewTreeObserver;
    private Activity activity;
    private ThreadPool pool;
    public IntruderViewPagerAdapter(Context context, List<String> resIdList, Activity activity) {
        super();
        mContext = context;
        mDrawableResIdList = resIdList;
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        window_width = manager.getDefaultDisplay().getWidth();
        window_height = manager.getDefaultDisplay().getHeight();
        this.activity=activity;
        pool=new ThreadPool(2);
    }



    @Override
    public int getCount() {
        if (mDrawableResIdList != null) {
            return mDrawableResIdList.size();
        }
        return 0;
    }

    @Override
    public int getItemPosition(Object object) {
        if (object != null && mDrawableResIdList != null) {
            String resId = (String) ((ImageView)object).getTag();
            if (resId != null) {
                for (int i = 0; i < mDrawableResIdList.size(); i++) {
                    if (resId.equals(mDrawableResIdList.get(i))) {
                        return i;
                    }
                }
            }
        }
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mDrawableResIdList != null && position < mDrawableResIdList.size()) {
            final String resId = mDrawableResIdList.get(position);
            if (resId != null) {
                final ZooImageView itemView = new ZooImageView(mContext);
                final Handler handler=new Handler();
                pool.execute(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bmp=getBitmap(resId);
                        itemView.setDrawingCacheEnabled(true);
                        itemView.setTag(resId);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                itemView.setScaleType(ImageView.ScaleType.MATRIX);
                                itemView.setImageBitmap(bmp);
                            }
                        });
                    }
                });
//                final Thread thread=new Thread();
//                thread.start();
                container.addView(itemView);
                return itemView;
            }
        }
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ZooImageView ImageView= (ZooImageView) object;
        container.removeView(ImageView);
        Bitmap bmp=ImageView.getDrawingCache();
        ImageView.setDrawingCacheEnabled(false);
        if (bmp!=null)bmp.recycle();
        bmp=null;
        ImageView.setImageBitmap(null);
        ImageView=null;
        System.gc();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public void startUpdate(ViewGroup container) {
    }
    @Override
    public void finishUpdate(ViewGroup container) {
    }

    public void updateData(List<String> itemsResId) {
        if (itemsResId == null) {
            return;
        }
        mDrawableResIdList = itemsResId;
        this.notifyDataSetChanged();
    }
    public Bitmap getBitmap(String path){
        Bitmap bitmap = null;
        try {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeStream(new FileInputStream(path),null,opts);
            if (opts.outWidth>window_width||opts.outHeight>window_height)
            opts.inSampleSize= Math.max((int)(opts.outHeight / (float) window_height), (int)(opts.outWidth / (float) window_width));
        opts.inJustDecodeBounds=false;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(path),null,opts);
            if (bitmap!=null)
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.fail);
    }
}
