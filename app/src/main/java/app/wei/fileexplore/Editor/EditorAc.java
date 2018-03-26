package app.wei.fileexplore.Editor;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import app.wei.fileexplore.R;

public class EditorAc extends Activity {
    public Editor editor;
    private String textPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        this.editor= (Editor) findViewById(R.id.editor);
//        editor.addTextChangedListener(this.watcher);
        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        textPath=bundle.getString("textPath");
        if (textPath!=null){
            showText(textPath);
        }
        else {
            Toast.makeText(this,"打开失败！",Toast.LENGTH_SHORT).show();
        }
        overridePendingTransition(R.anim.activity, R.anim.exit);
    }


    private TextWatcher watcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            editor.sethighLight();
        }
    };

    public void showText(final String filePath)
    {
        File file=new File(filePath);
        if (!file.exists())
            return;
        final Handler handler=new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader reader= null;
                final StringBuilder content=new StringBuilder();
                try {
                    reader = new BufferedReader(new FileReader(filePath));
                    String temp;
                    while ((temp=reader.readLine())!=null){
                        content.append(temp).append('\n');
                    }
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                final String set=content;
//                content=null;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        editor.setText(content);
//                        editor.highLight();
                    }
                });

            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,0,0,"打开");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case 0:
                this.showFileChooser();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showFileChooser()
    {
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        this.startActivityForResult(Intent.createChooser(intent,"Select File"),10);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10&&data!=null){
            Uri uri=data.getData();
            this.showText(uri.getPath());
        }
    }
}
