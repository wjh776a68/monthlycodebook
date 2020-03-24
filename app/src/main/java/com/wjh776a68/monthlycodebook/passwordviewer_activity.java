package com.wjh776a68.monthlycodebook;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.wjh776a68.monthlycodebook.fileio.*;
import static com.wjh776a68.monthlycodebook.ScrollingActivity.encode;
import static com.wjh776a68.monthlycodebook.passwordslist.refreshlistview;

import java.io.File;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class passwordviewer_activity extends AppCompatActivity {
    private  static ArrayAdapter<String> adapter;
    private String passwordpath;
    //声名 ImageView id
    private ImageView welcomeImg;
    private static String[] passwordsbit;
    private static long nowpasswordid;
    static int wjhmonth;
    static int wjhyear;
    private static int calendaroffset;

    private static int passwordlength=0;
    private static boolean allownum=false;
    private static boolean allowletter=false;
    private static boolean allowdilemma=false;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        passwordpath = getFilesDir().getPath() + File.separator + "self"+File.separator + "passwordslist.txt";
        setContentView (R.layout.fragment_passwordviewer);

        Toolbar toolbar = findViewById(R.id.toolbar_viewer);
        toolbar.setNavigationIcon(R.drawable.left);

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle recievedargument = getIntent().getExtras();
        int value = -1; // or other values
        if(recievedargument != null)
            value = recievedargument.getInt("itemposition");
        Log.v("wjh776a68",Integer.toString(value));

        String filecontent = readFile(passwordpath);
        String[] passwordsgroup = filecontent.split("\n");
        passwordsbit = passwordsgroup[(int)value].split("\b");
        nowpasswordid = value;
        EditText DeviceNameinputid = findViewById(R.id.DeviceNameInput_viewer);
        DeviceNameinputid.setText(passwordsbit[0]);
        EditText UserKeyinputid = findViewById(R.id.UserKeyInput_viewer);

        UserKeyinputid.setText(passwordsbit[3]);

        passwordlength=Integer.parseInt(passwordsbit[4]);
        allownum=passwordsbit[5].equals("true");
        allowletter=passwordsbit[6].equals("true");
        allowdilemma=passwordsbit[7].equals("true");

        SeekBar seekbarid = findViewById(R.id.passwordlength_viewer);
        seekbarid.setProgress(passwordlength,true);
        CheckBox allownumid = findViewById((R.id.allownumber_viewer));
        allownumid.setChecked(allownum);
        CheckBox allowletterid = findViewById((R.id.allowletter_viewer));
        allowletterid.setChecked(allowletter);
        CheckBox allowdilemmaid = findViewById((R.id.allowdilemma_viewer));
        allowdilemmaid.setChecked(allowdilemma);


        Calendar thecalendar=Calendar.getInstance();
        wjhmonth = thecalendar.get(Calendar.MONTH) + 1;
        Log.v("wjhmonth",Integer.toString(wjhmonth));
        wjhyear = thecalendar.get(Calendar.YEAR);
        Log.v("wjhyear",Integer.toString(wjhyear));
        calendaroffset =  12 * Integer.parseInt(passwordsbit[1]) + Integer.parseInt(passwordsbit[2]);
        if(!passwordsbit[1].equals(Integer.toString(wjhyear))|| !passwordsbit[2].equals(Integer.toString(wjhmonth))  )
        {

            showNormalDialog();

        }

        String encoderesult = encode(passwordsbit[0],passwordsbit[3],Integer.parseInt(passwordsbit[1]),Integer.parseInt(passwordsbit[2]),passwordlength,allownum,allowletter,allowdilemma);

        final TextView PasswordViewid=(TextView)findViewById(R.id.PasswordView_viewer);
        PasswordViewid.setText(encoderesult);
        PasswordViewid.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view){
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                if(PasswordViewid.getText().length()==0)
                {

                    return true;
                }
                ClipData mClipData = ClipData.newPlainText("Label", PasswordViewid.getText());
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                Toast toast=Toast.makeText(getApplicationContext(),R.string.toclipboard,Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }

        });
    }

    public void NextMonth(View view){
        calendaroffset ++ ;
        String encoderesult = encode(passwordsbit[0],passwordsbit[3],0,calendaroffset,passwordlength,allownum,allowletter,allowdilemma);
        TextView PasswordViewid=(TextView)findViewById(R.id.PasswordView_viewer);
        PasswordViewid.setText(encoderesult);

        TextView Degreeid=findViewById(R.id.degree);
        if(calendaroffset == 12* wjhyear + wjhmonth) {
            Degreeid.setText(getString(R.string.currentmonth));
        }
        else if(calendaroffset == 12* wjhyear + wjhmonth + 1){
            Degreeid.setText(getString(R.string.nextmonth));
        }
        else if(calendaroffset == 12* wjhyear + wjhmonth - 1){
            Degreeid.setText(getString(R.string.previousmonth));
        }
        else if((calendaroffset )%12==0)
            Degreeid.setText((calendaroffset/12-1) + " . " + "12");
        else
            Degreeid.setText(calendaroffset/12 + " . " + ((calendaroffset )%12));
    }

    public void PreviousMonth(View view){
        calendaroffset --;
        String encoderesult = encode(passwordsbit[0],passwordsbit[3],0,calendaroffset,passwordlength,allownum,allowletter,allowdilemma);
        TextView PasswordViewid=(TextView)findViewById(R.id.PasswordView_viewer);
        PasswordViewid.setText(encoderesult);

        TextView Degreeid=findViewById(R.id.degree);
        if(calendaroffset == 12* wjhyear + wjhmonth) {
            Degreeid.setText(getString(R.string.currentmonth));
        }
        else if(calendaroffset == 12* wjhyear + wjhmonth + 1){
            Degreeid.setText(getString(R.string.nextmonth));
        }
        else if(calendaroffset == 12* wjhyear + wjhmonth - 1){
            Degreeid.setText(getString(R.string.previousmonth));
        }
        else if((calendaroffset )%12==0)
            Degreeid.setText((calendaroffset/12-1) + " . " + "12");
        else
            Degreeid.setText(calendaroffset/12 + " . " + ((calendaroffset )%12));
    }

    public void HomeMonth(View view){
        calendaroffset = 12 * wjhyear + wjhmonth;
        String encoderesult = encode(passwordsbit[0],passwordsbit[3],0,calendaroffset,passwordlength,allownum,allowletter,allowdilemma);
        TextView PasswordViewid=(TextView)findViewById(R.id.PasswordView_viewer);
        PasswordViewid.setText(encoderesult);

        TextView Degreeid=findViewById(R.id.degree);
        if(calendaroffset == 12* wjhyear + wjhmonth) {
            Degreeid.setText(getString(R.string.currentmonth));
        }
        else if(calendaroffset == 12* wjhyear + wjhmonth + 1){
            Degreeid.setText(getString(R.string.nextmonth));
        }
        else if(calendaroffset == 12* wjhyear + wjhmonth - 1){
            Degreeid.setText(getString(R.string.previousmonth));
        }
        else if((calendaroffset )%12==0)
            Degreeid.setText((calendaroffset/12-1) + " . " + "12");
        else
            Degreeid.setText(calendaroffset/12 + " . " + ((calendaroffset )%12));
    }

    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(passwordviewer_activity.this);
        normalDialog.setIcon(R.drawable.icon_add);
        normalDialog.setTitle("! ! !");
        normalDialog.setMessage( getString(R.string.passwordupdated_header) +  encode(passwordsbit[0],passwordsbit[3],Integer.parseInt(passwordsbit[1]),Integer.parseInt(passwordsbit[2]),passwordlength,allownum,allowletter,allowdilemma)  +   getString(R.string.passwordupdated_middle) +  encode(passwordsbit[0],passwordsbit[3],wjhyear,wjhmonth,passwordlength,allownum,allowletter,allowdilemma) +  getString(R.string.passwordupdated_footer));
        normalDialog.setPositiveButton(getString(R.string.continuebutton),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                            passwordsbit[2] = Integer.toString(wjhmonth);
                            passwordsbit[1] = Integer.toString(wjhyear);


                        String content = readFile(passwordpath);
                        String[] group = content.split("\n");
                        content = "";
                        group[(int)nowpasswordid] = passwordsbit[0] + "\b" + passwordsbit[1] + "\b" + passwordsbit[2] + "\b" + passwordsbit[3] + '\b' + passwordsbit[4] + '\b' + passwordsbit[5] + '\b' + passwordsbit[6] + '\b' + passwordsbit[7] + '\n';
                        int i=0;
                        for(i=0;i<group.length;i++){
                            content = content + group[i] + "\n";
                        }
                        writeToFile(passwordpath,content);

                        Person passwords_item=new Person(passwordsbit[0],getString(R.string.LastTimeUsed) + passwordsbit[1] + '.' + passwordsbit[2],R.drawable.sample);
                        refreshlistview(passwords_item);
                    }
                });
        normalDialog.setNegativeButton(getString(R.string.notnowbutton),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        return;
                    }
                });
        // 显示
        normalDialog.show();
    }

}