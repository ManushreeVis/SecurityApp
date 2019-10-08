package com.example.womansecurityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import java.io.*;

import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import android.content.*;
import android.speech.RecognizerIntent;

public class MainActivity extends AppCompatActivity {
 String Name,Mobile,BName,BMobile;
 TextView tv;
 protected static final int RESULT_SPEECH = 1;//user will speak
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            File f = new File("sdcardd/userdata.txt");
            FileInputStream fin = new FileInputStream((f));
            BufferedReader br = new BufferedReader(new InputStreamReader(fin));
            String data = br.readLine();
            String finalData[] = data.split(",");
            Name = finalData[0];
            Mobile = finalData[1];
            BName = finalData[2];
            BMobile = finalData[3];
            setContentView(R.layout.speak);
            tv = (TextView)findViewById(R.id.textView);
            Toast.makeText(this,"Welcome "+Name,Toast.LENGTH_SHORT).show();
        }
        catch(Exception ex){
            setContentView(R.layout.activity_main);
        }
       // setContentView(R.layout.activity_main);
    }
    public void submitclick(View v){
        EditText t1 = (EditText)findViewById(R.id.editText);
        EditText t2 = (EditText)findViewById(R.id.editText3);
        EditText t3 = (EditText)findViewById(R.id.editText4);
        EditText t4 = (EditText)findViewById(R.id.editText5);

        Name = t1.getText().toString();
        Mobile = t2.getText().toString();
        BName = t3.getText().toString();
        BMobile = t4.getText().toString();
        if(Name.length()==0||Mobile.length()==0||BName.length()==0||BMobile.length()==0){
            Toast.makeText(getApplicationContext(),"Value cannot be empty",Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            String msg = Name+","+Mobile+","+BName+","+BMobile;
            File f = new File("/sdcard/userdata.txt");
            f.createNewFile();
            FileOutputStream fout = new FileOutputStream(f);
            OutputStreamWriter outwrt = new OutputStreamWriter(fout);
            outwrt.append(msg);
            outwrt.close();
            fout.close();
            Toast.makeText(this,"Done writing 'userdata.txt",Toast.LENGTH_SHORT
            ).show();
            String firstMsg = "Hi "+BName+"\n"+", I have added you as helping person";
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(BMobile,null,firstMsg,null,null);
            Toast.makeText(this,"Sms sent",Toast.LENGTH_SHORT).show();
            setContentView(R.layout.speak);
            tv = findViewById(R.id.textView);
        }
        catch(Exception ex){
        Toast.makeText(this,ex.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void speakclick(View v){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,"en-US");
        try{
            startActivityForResult(i,RESULT_SPEECH);
            tv.setText("");
        }
        catch(ActivityNotFoundException a){
            Toast t = Toast.makeText(this,"Oops! Your device do not suport speech to text",Toast.LENGTH_SHORT);
            t.show();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case RESULT_SPEECH:{
                if(resultCode == RESULT_OK && null != data){
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    tv.setText(text.get(0));
                    if(tv.getText().equals("help"));
                }
            }
            break;
        }
    }
}
