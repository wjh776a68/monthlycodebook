package com.wjh776a68.monthlycodebook;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.FileProvider.getUriForFile;
import static com.wjh776a68.monthlycodebook.fileio.*;
import static com.wjh776a68.monthlycodebook.passwordslist.refreshlistview;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;




public class settings extends Fragment {
    private ListView settings_listid;
    private static final int PICKFILE_REQUEST_CODE = 8777;
    private static final int PICKFILE_RESULT_CODE = 8778;
    private static List<ImageListArray> onePieceList = new ArrayList<>();
    private static int used=0;
    private static String passwordpath;
    public static settings newInstance() {

        Bundle args = new Bundle();

        settings fragment = new settings();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settingslist, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        passwordpath =  getActivity().getFilesDir().getPath() + File.separator + "self"+File.separator + "passwordslist.txt";
        addingData(); //初始化数据
        //创建适配器，在适配器中导入数据 1.当前类 2.list_view一行的布局 3.数据集合


        ImageListAdapter imageListAdapter = new ImageListAdapter(getView().getContext(),R.layout.settings_itemlist,onePieceList);
        ListView listView = (ListView)view.findViewById(R.id.settings_list); //将适配器导入Listview
        listView.setAdapter(imageListAdapter);
        listView.setOnItemClickListener( settingsonclickf );

        Toolbar toolbar = view.findViewById(R.id.toolbar_settings);

        toolbar.setTitle(R.string.settings_navigation);

    }

    private AdapterView.OnItemClickListener settingsonclickf = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView < ? > arg0, View arg1, int arg2, long arg3) {

            switch((int)arg3){
                case 0://exportpasswords
                    File newFile = new File(passwordpath);
                    Uri contentUri = getUriForFile(getContext(), "com.wjh776a68.monthlycodebook.provider", newFile);

                    shareFile(getContext(),contentUri);
                    break;
                case 1://importpasswords
                    //TODO add function
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("*/*");

                    try{
                        startActivityForResult(intent,PICKFILE_REQUEST_CODE);


                    } catch (ActivityNotFoundException e){
                        Toast.makeText(getActivity(), "There are no file explorer clients installed.", Toast.LENGTH_SHORT).show();
                    }


                    break;
                case 2://about
                  //  String info = ( (TextView) arg1 ).getText().toString();.
                    Intent intent_about = new Intent(getActivity(), about_activity.class);
                    startActivity(intent_about);
                    break;

            }



        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permission,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);

        //requestCode就是requestPermissions()的第三个参数
        //permission就是requestPermissions()的第二个参数
        //grantResults是结果，0调试通过，-1表示拒绝
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch(requestCode){
            case PICKFILE_REQUEST_CODE:
                if(data!=null){
                    Uri selectedfile = data.getData();
                    selectedfile.toString();
                   String importfilepath = getRealPathFromURI(selectedfile);
                    Log.v("path",importfilepath);
                    if (Build.VERSION.SDK_INT >= 23) {
                        int REQUEST_CODE_PERMISSION_STORAGE = 100;
                        String[] permissions = {
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        };

                        for (String str : permissions) {
                            if (getContext().checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                                this.requestPermissions(permissions, REQUEST_CODE_PERMISSION_STORAGE);
                                return;
                            }
                        }
                    }
                    Log.v("contentimport","start");
                    String contentimport = readFile(importfilepath);

                    String[] passwordsgroup = contentimport.split("\n");

                    for(int i=0;i<passwordsgroup.length;i++){
                        String[] passwordbit = passwordsgroup[i].split("\b");
                        if(passwordbit.length != 8)
                        {
                            Snackbar.make(getView(), R.string.invaildpasswordsfile, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            return;
                        }
                        // passwordsgroup[i] = passwordbit[0] + "\b   " + getString(R.string.LastTimeUsed) + passwordbit[1] + '.' + passwordbit[2];
                       // persons.add(new Person(passwordbit[0],getString(R.string.LastTimeUsed) + passwordbit[1] + '.' + passwordbit[2],R.drawable.sample));
                    }



                    String contentinside = readFile(passwordpath);
                    Log.v("contentimport",contentimport);
                    writeToFile(passwordpath, contentinside + contentimport);
                    for(int i=0;i<passwordsgroup.length;i++){
                        String[] passwordbit = passwordsgroup[i].split("\b");

                        refreshlistview(new Person(passwordbit[0],getString(R.string.LastTimeUsed) + passwordbit[1] + '.' + passwordbit[2],R.drawable.sample));

                        // passwordsgroup[i] = passwordbit[0] + "\b   " + getString(R.string.LastTimeUsed) + passwordbit[1] + '.' + passwordbit[2];
                        // persons.add(new Person(passwordbit[0],getString(R.string.LastTimeUsed) + passwordbit[1] + '.' + passwordbit[2],R.drawable.sample));
                    }

                }
                break;
        }


    }

    public void addingData(){
        if(used==1)
            return;
        used=1;
        ImageListArray ace =new ImageListArray(getString(R.string.exportpasswords),R.drawable.export_dark);
        onePieceList.add(ace);
        ImageListArray arlong =new ImageListArray(getString(R.string.importpasswords),R.drawable.import_dark);
        onePieceList.add(arlong);
        ImageListArray barbe_blanche =new ImageListArray(getString(R.string.about),R.drawable.about_dark);
        onePieceList.add(barbe_blanche);

    }

    public static void shareFile(Context context, Uri uri) {
        // File file = new File("\sdcard\android123.cwj"); //附件文件地址


        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra("subject", ""); //
        intent.putExtra("body", ""); // 正文
        intent.putExtra(Intent.EXTRA_STREAM, uri); // 添加附件，附件为file对象
        if (uri.toString().endsWith(".gz")) {
            intent.setType("application/x-gzip"); // 如果是gz使用gzip的mime
        } else if (uri.toString().endsWith(".txt")) {
            intent.setType("text/plain"); // 纯文本则用text/plain的mime
        } else {
            intent.setType("application/octet-stream"); // 其他的均使用流当做二进制数据来发送
        }
        context.startActivity(intent); // 调用系统的mail客户端进行发送
    }

    private String getRealPathFromURI(Uri contentUri) {
        /*String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor=getContext().getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToNext()){
            return cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
        }
        cursor.close();
        return null;*/

        String filePath = "";

        // ExternalStorageProvider
        String docId = DocumentsContract.getDocumentId(contentUri);
        String[] split = docId.split(":");
        String type = split[0];

        if ("primary".equalsIgnoreCase(type)) {
            return Environment.getExternalStorageDirectory() + "/" + split[1];
        } else {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                //getExternalMediaDirs() added in API 21
                File[] external = getContext().getExternalMediaDirs();
                if (external.length > 1) {
                    filePath = external[1].getAbsolutePath();
                    filePath = filePath.substring(0, filePath.indexOf("Android")) + split[1];
                }
            } else {
                filePath = "/storage/" + type + "/" + split[1];
            }
            return filePath;
        }


    }




}
