package com.wjh776a68.monthlycodebook;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import 	android.widget.EditText;
import 	android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import 	java.util.Date;
import java.io.BufferedReader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import static com.wjh776a68.monthlycodebook.fileio.*;


public class ScrollingActivity extends AppCompatActivity {
  // public int bit;
    static MenuItem MenuitemGlobal;
    private BottomNavigationView bottomNavigationView;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private MenuItem menuItem;
    private String passwordpath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        passwordpath =  getFilesDir().getPath() + File.separator + "self"+File.separator + "passwordslist.txt";
        File fp=new File(passwordpath);
        if(!fp.exists())
        {
            //File file = new File(passwordpath);

            CreateFile(passwordpath);
          //  writeToFile(passwordpath,"");
            //show tutorial
        }



        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        viewPager = (ViewPager) findViewById(R.id.vp);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        List<Fragment> list = new ArrayList<>();
        list.add(passwordslist.newInstance());

        list.add(settings.newInstance());
        viewPagerAdapter.setList(list);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            menuItem = item;
            switch (item.getItemId()) {
                case R.id.navigation_list:
                    viewPager.setCurrentItem(0);
                    return true;

                case R.id.navigation_settings:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        MenuitemGlobal=menu.getItem(0);
        return true;
    }


    public static String encode(String DeviceNameString,String UserKeyInputString,int year,int month,int passwordlength,boolean withnum,boolean withalpha,boolean withexpression){

        if(DeviceNameString.length()==0)
        {
            return "";
        }


        int tmp=0;
        int pwd[]=new int[6];


        int multi=0;


        multi+=year * 12 + month - 1;//replace upon
        int seed=0;
        char a;
        String encoderesult="";
        String DeviceNameString_encode=md5value.getMd5Value(DeviceNameString + multi + UserKeyInputString + "wjh776a68software");
        for(int i=0;i<DeviceNameString_encode.length();i++){
            a=DeviceNameString_encode.charAt(i);
            if (a >= '0' && a <= '9')
            {
                tmp = a - '0';
            }
            else if (a >= 'a' && a <= 'z')
            {
                tmp = a - 'a' + 10;
            }
            seed=16*seed + tmp;
            if((i+1)%3==0){
                if(seed/64<10){
                    encoderesult=encoderesult+(char)(seed/64+'0');
                }
                else if(seed/64<36){
                    encoderesult=encoderesult+(char)(seed/64-10+'A');
                }
                else if(seed/64<62){
                    encoderesult=encoderesult+(char)(seed/64-36+'A');
                }
                else if(seed/64==62){
                    encoderesult=encoderesult+'-';
                }
                else if(seed/64==63){
                    encoderesult=encoderesult+'.';
                }
                seed%=64;
                if(seed<10){
                    encoderesult=encoderesult+(char)(seed+'0');
                }
                else if(seed<36){
                    encoderesult=encoderesult+(char)(seed-10+'A');
                }
                else if(seed<62){
                    encoderesult=encoderesult+(char)(seed-36+'a');
                }
                else if(seed==62){
                    encoderesult=encoderesult+'-';
                }
                else if(seed==63){
                    encoderesult=encoderesult+'.';
                }
                seed=0;
            }

        }



        if(withexpression==false){
            encoderesult = encoderesult.replace('-','2');
            encoderesult =  encoderesult.replace('.','3');
        }

        if(withalpha==false && withnum==false){
            return "";
        }
        else if(withnum==false) {
            Log.v("withnum","false");
            for(int i=0;i<encoderesult.length();i++){
                if((encoderesult.charAt(i)<='9' && encoderesult.charAt(i)>='0'))
                {
                   // Log.v("before",encoderesult.charAt(i) + "");
                    encoderesult = encoderesult.replace(encoderesult.charAt(i), Character.toChars(encoderesult.charAt(i)-'0'+'a')[0]);
                   // Log.v("after",Character.toChars(encoderesult.charAt(i)-'0'+'a')[0] + "");
                }

            }

        }
        else if(withalpha==false){
            Log.v("withalpha","false");
            for(int i=0;i<encoderesult.length();i++){
                if((encoderesult.charAt(i)<='z' && encoderesult.charAt(i)>='a'))
                {
                    encoderesult = encoderesult.replace(encoderesult.charAt(i), (char)(encoderesult.charAt(i)-'a'+'0'));
                }
                if( (encoderesult.charAt(i)<='Z' && encoderesult.charAt(i)>='A')){
                    encoderesult = encoderesult.replace(encoderesult.charAt(i), (char)(encoderesult.charAt(i)-'A'+'0'));
                }
            }
        }

        Log.v("encoderesult",encoderesult);
        if(passwordlength<=encoderesult.length())
            return encoderesult.substring(0,passwordlength);
        else
            return "";


    }

}
