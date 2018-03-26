package app.wei.fileexplore.Editor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by wei on 2017/7/9.
 */

public class Editor extends android.support.v7.widget.AppCompatEditText{
    private Paint paint,linePaint;
    private Rect currentRect;
    private int lineHeight;
    private int start,end;
    private float lightTop;
    private HashSet<String> keySet;

    public Editor(Context context) {
        super(context);
        Init();
    }

    public Editor(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init();
    }

    public Editor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init();
    }


    private void Init(){
        this.currentRect=new Rect();
        this.lineHeight=this.getLineHeight();
        paint=new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(this.getTextSize());
        this.linePaint=new Paint();
        this.linePaint.setColor(Color.rgb(150,150,150));
        this.setPadding(95,0,0,0);
        WindowManager wm= (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        this.setMinWidth(metrics.widthPixels);
        this.setMinHeight(metrics.heightPixels);
        String[] keys={"int","char","long","short","bool","byte",
                "float","double","true","false","return","if","else",
                "switch","case","continue","break","struct","typedef",
                "class","public","private","protected","void"};
        this.keySet=new HashSet<String>();
        keySet.addAll(Arrays.asList(keys));
    }



    public int getcurrentLine(){
        return this.getLayout().getLineForOffset(this.getSelectionStart());
    }

    public int getFirstVisiableLine(){
        this.getLocalVisibleRect(currentRect);
        return currentRect.top/lineHeight;
    }



    public int getLastVisiableLine(){
        this.getLocalVisibleRect(currentRect);
        start=currentRect.top/lineHeight;
        return currentRect.bottom/lineHeight+start;
    }


    public String getCurrentLineText(){
        int cpos=this.getSelectionStart();
        String text= this.getText().toString();
        int begin=0,end=text.length();
        for(int i=cpos;i<end;i++){
            if (text.charAt(i)=='\n'){
                end=i;
                break;
            }
        }
        for (int i=cpos-1;i>0;i--){
            if (text.charAt(i)=='\n'){
                begin=i+1;
                break;
            }
        }
        return text.substring(begin,end);
    }

    public void sethighLight(){
        int cpos=this.getSelectionStart();
        String text= this.getText().toString();

        int beginLine=0,endLine=text.length();
        for(int i=cpos;i<endLine;i++){
            if (text.charAt(i)=='\n'){
                endLine=i;
                break;
            }
        }
        for (int i=cpos-1;i>0;i--){
            if (text.charAt(i)=='\n'){
                beginLine=i+1;
                break;
            }
        }
        if (beginLine==endLine)
            return;
        String sub=text.substring(beginLine,endLine);

        String result=sub.replaceAll("[^\\w]"," ");
        String[] strings=result.split(" ");
        int begin=0,end=0;
        for (int i = 0; i < strings.length; i++) {
            if (strings[i].length()==0){
                begin=begin+1;
            }
            else if (keySet.contains(strings[i])){
                end=begin+strings[i].length();
                this.getEditableText().setSpan(new ForegroundColorSpan(Color.RED),beginLine+begin,beginLine+end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                System.out.println("start:"+begin+"\t"+"end:"+end+"\t("+s.substring(begin,end)+")");
                begin=end+1;
            }
            else {
                end=begin+strings[i].length();
                this.getEditableText().setSpan(null,beginLine+begin,beginLine+end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                begin=begin+strings[i].length()+1;
            }
        }
    }

    public void highLight(){

        String text= this.getText().toString();
        if (text.equals(""))
            return;
        String result=text.replaceAll("[^\\w]"," ");
        String[] strings=result.split(" ");

        int begin=0,end=0;
        for (int i = 0; i < strings.length; i++) {
            if (strings[i].length()==0){
                begin=begin+1;
            }
            else if (keySet.contains(strings[i])){
                end=begin+strings[i].length();
                this.getEditableText().setSpan(new ForegroundColorSpan(Color.RED),begin,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                System.out.println("start:"+begin+"\t"+"end:"+end+"\t("+s.substring(begin,end)+")");
                begin=end+1;
            }
            else {
                end=begin+strings[i].length();
                this.getEditableText().setSpan(null,begin,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                begin=begin+strings[i].length()+1;
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas){
//
        this.getLocalVisibleRect(currentRect);
        start=currentRect.top/lineHeight;
        end=currentRect.bottom/lineHeight+start;
        end=end>this.getLineCount()?this.getLineCount():end;
        for (;start<end;start++){
            canvas.drawText(String.valueOf(start+1),0,(start+1)*lineHeight,paint);
        }
//        Log.i("start",String.valueOf(this.getLayout().getLineForOffset(this.getSelectionStart())));
        this.lightTop=this.getLayout().getLineForOffset(this.getSelectionStart())*this.lineHeight;
        canvas.drawRect(95,this.lightTop,this.getWidth(),this.lightTop+this.lineHeight+10,linePaint);
        super.onDraw(canvas);
    }

}
