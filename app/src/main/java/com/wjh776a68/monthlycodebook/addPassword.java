package com.wjh776a68.monthlycodebook;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.Calendar;
import java.util.UUID;

import static com.wjh776a68.monthlycodebook.ScrollingActivity.encode;
import static com.wjh776a68.monthlycodebook.fileio.*;
import static com.wjh776a68.monthlycodebook.passwordslist.*;

public class addPassword extends AppCompatActivity {
    private FloatingActionButton fab;
    private String passwordpath;
    private View thisview;
    private static boolean allownum,allowletter,allowdilemma;
    private static int passwordlength;


    //public static addPassword newInstance() {

    //    Bundle args = new Bundle();

     //   addPassword fragment = new addPassword();
        //fragment.setArguments(args);
   //     return fragment;
  //  }

    //@Nullable
   // @Override
    //public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
   //     View view = inflater.inflate(R.layout.fragment_addpassword, container, false);
    //    return view;
    //}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        passwordpath =  getFilesDir().getPath() + File.separator + "self"+File.separator + "passwordslist.txt";
        switch (item.getItemId()) {
            case R.id.action_settings:
                Calendar thecalendar=Calendar.getInstance();
                final int wjhmonth= thecalendar.get(Calendar.MONTH) + 1;
                final int wjhyear= thecalendar.get(Calendar.YEAR);

              //  Toast.makeText(this, "save", Toast.LENGTH_SHORT).show();
                SeekBar seekbarid = findViewById(R.id.passwordlength);
                passwordlength = seekbarid.getProgress();
                CheckBox allownumid = findViewById(R.id.allownumber);
                CheckBox allowletterid = findViewById(R.id.allowletter);
                CheckBox allowdilemmaid = findViewById(R.id.allowdilemma);
                allownum=allownumid.isChecked();
                allowletter=allowletterid.isChecked();
                allowdilemma=allowdilemmaid.isChecked();
                // Log.v("checkbox"," " + allownum + allowletter + allowdilemma);

                EditText DeviceNameInputID = (EditText)findViewById(R.id.DeviceNameInput);
                String DeviceNameString= DeviceNameInputID.getText().toString();
                if(DeviceNameString.length()==0)
                {
                    Snackbar.make(getWindow().getDecorView(), R.string.warning, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    break;
                }

                //  EditText DeviceNameInputID = (EditText)getView().findViewById(R.id.DeviceNameInput);
                EditText UserKeyInputID = (EditText)findViewById(R.id.UserKeyInput);
                UUID randomstring = UUID.randomUUID();

                //UUID randomstring = UUID.fromString(DeviceNameString);
                if(UserKeyInputID.getText().length()==0)
                {
                    UserKeyInputID.setText(randomstring.toString());
                }


                //TextView PasswordViewID = (TextView)findViewById(R.id.PasswordView);

                String encoderesult = encode(DeviceNameInputID.getText().toString(),UserKeyInputID.getText().toString(),wjhyear,wjhmonth,passwordlength,allownum,allowletter,allowdilemma);
                String originalcontent =  readFile(passwordpath);
              //  PasswordViewID.setText(encoderesult);
                // Log.v("checkpost",originalcontent + DeviceNameInputID.getText().toString() + '\b' + wjhyear + '\b' + wjhmonth +  '\b' + UserKeyInputID.getText().toString() + '\b' + passwordlength + '\b' + allownum + '\b' + allowletter + '\b' + allowdilemma + '\n');
                if( UserKeyInputID.getText().toString().equals(""))
                    writeToFile(passwordpath,originalcontent + DeviceNameInputID.getText().toString() + '\b' + wjhyear + '\b' + wjhmonth +  '\b' + ' ' + '\b' + passwordlength + '\b' + allownum + '\b' + allowletter + '\b' + allowdilemma + '\n');
                else
                    writeToFile(passwordpath,originalcontent + DeviceNameInputID.getText().toString() + '\b' + wjhyear + '\b' + wjhmonth +  '\b' + UserKeyInputID.getText().toString() + '\b' + passwordlength + '\b' + allownum + '\b' + allowletter + '\b' + allowdilemma + '\n');

                //refreshlistview(DeviceNameInputID.getText().toString() + '\b' + wjhyear + '\b' + wjhmonth);

                // refreshlistview(DeviceNameInputID.getText().toString() + "\b   " + getString(R.string.LastTimeUsed) + wjhyear + '.' + wjhmonth);
                Person passwords_item=new Person(DeviceNameInputID.getText().toString(),getString(R.string.LastTimeUsed) +  wjhyear + '.' + wjhmonth,R.drawable.sample);
                refreshlistview(passwords_item);
                onBackPressed();
                break;

        }
        return super.onOptionsItemSelected(item);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lunarpassword);

       // onViewCreated(view, savedInstanceState);

        Toolbar toolbar = findViewById(R.id.toolbar_lunarpassword);
        toolbar.setNavigationIcon(R.drawable.left);
        toolbar.setTitle(R.string.add_navigation);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_scrolling);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SeekBar passwordlengthid = findViewById(R.id.passwordlength);
        final TextView passwordlengthnumid = findViewById(R.id.passwordlengthnum);
        passwordlengthid.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                passwordlengthnumid.setText(Integer.toString(progress));
               // return false;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //.setSupportActionBar(toolbar);

       // fab = (FloatingActionButton) findViewById(R.id.fab);


      //  fab.setOnClickListener(new View.OnClickListener() {
       //     @Override
       //     public void onClick(View view) {

       //     }
      //  });

     /*   final TextView PasswordViewid=(TextView)findViewById(R.id.PasswordView);
        PasswordViewid.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view){
                ClipboardManager cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                if(PasswordViewid.getText().length()==0)
                {
                    //Toast toast=Toast.makeText(getApplicationContext(),"密码栏为空，无法复制",Toast.LENGTH_SHORT);
                    //toast.show();
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
*/


    }
}
