package com.wjh776a68.monthlycodebook;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.wjh776a68.monthlycodebook.fileio.*;

public class passwordslist extends Fragment{
    //private ListView lv;
    //private static ArrayAdapter<String> adapter;
    private static RVAdapter adapter;
    //private String text;
    private static int count = 0;
   // private static ListView passwords_listid;
    private static RecyclerView passwords_listid;
    private static String passwordpath ;
    private SearchView mSearchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private static List<Person> persons;

    private static int lastdeletedposition;
    private static String lastdeletedcontent;

    //private static ArrayList<String> passwordsgrouplist;
    public static passwordslist newInstance() {

        Bundle args = new Bundle();

        passwordslist fragment = new passwordslist();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_passwordlist, container, false);

        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.passwordlist_menu, menu);
       /*  Log.v("msearchview",mSearchView.toString());
      //  SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
       MenuItem myActionMenuItem = menu.findItem(R.id.menu_search);
       mSearchView = (SearchView) myActionMenuItem.getActionView();
       // mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        Log.v("msearchview",mSearchView.toString());
        mSearchView.setOnSearchClickListener(new SearchView.OnClickListener(){
            @Override
            public void onClick(View view){
                String arg0 = mSearchView.getQuery().toString();
                if (TextUtils.isEmpty(arg0)) {
                    adapter.getFilter().filter("");
                    Log.i("Nomad", "onQueryTextChange Empty String");
                    // placesListView.clearTextFilter();
                } else {
                    Log.i("Nomad", "onQueryTextChange " + arg0.toString());
                    adapter.getFilter().filter(arg0.toString());
                }
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String arg0) {
                // filter listview
                Log.v("here","here");

                return true;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                Log.v("changehere","changehere");
                if (TextUtils.isEmpty(arg0)) {
                    adapter.getFilter().filter("");
                    Log.i("Nomad", "onQueryTextChange Empty String");
                   // placesListView.clearTextFilter();
                } else {
                    Log.i("Nomad", "onQueryTextChange " + arg0.toString());
                    adapter.getFilter().filter(arg0.toString());
                }
                return false;
            }
        });*/
       /* MenuItem searchItem = menu.findItem(R.id.menu_search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);

                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }*/
        super.onCreateOptionsMenu(menu, inflater);
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }
*/
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar_passwordlist);
        toolbar.setTitle(R.string.list_navigation);
        toolbar.inflateMenu(R.menu.passwordlist_menu);

        mSearchView = (SearchView) toolbar.findViewById(R.id.menu_search);

        FloatingActionButton myFab = (FloatingActionButton) view.findViewById(R.id.addpasswordbutton);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), addPassword.class);
                startActivity(intent);

            }
        });



        passwordpath = getActivity().getFilesDir().getPath() + File.separator + "self"+File.separator + "passwordslist.txt";
       // passwords_listid = (ListView) view.findViewById(R.id.passwords_list);
        passwords_listid = (RecyclerView) view.findViewById(R.id.passwordslistcontainer);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        passwords_listid.setLayoutManager(llm);
        persons = new ArrayList<>();


        // This method creates an ArrayList that has three Person objects
// Checkout the project associated with this tutorial on Github if
// you want to use the same images.


        String filecontent = readFile(passwordpath);
        if(filecontent.length()<=5)
        {
            Calendar thecalendar=Calendar.getInstance();
            int wjhmonth = thecalendar.get(Calendar.MONTH) + 1;
            Log.v("wjhmonth",Integer.toString(wjhmonth));
            int wjhyear = thecalendar.get(Calendar.YEAR);
            Log.v("wjhyear",Integer.toString(wjhyear));
            writeToFile(passwordpath,"Sample" + '\b' + wjhyear + '\b' + wjhmonth + '\b' + "sampleuserkey" + '\b' + 10 + '\b' + true + '\b' + true + '\b' + false + '\n');
            filecontent = "Sample" + '\b' + wjhyear + '\b' + wjhmonth + '\b' + "sampleuserkey" +  '\b' + 10 + '\b' + true + '\b' + true + '\b' + true + '\n';
        }

        String[] passwordsgroup = filecontent.split("\n");

        for(int i=0;i<passwordsgroup.length;i++){
            String[] passwordbit = passwordsgroup[i].split("\b");
           // passwordsgroup[i] = passwordbit[0] + "\b   " + getString(R.string.LastTimeUsed) + passwordbit[1] + '.' + passwordbit[2];
            persons.add(new Person(passwordbit[0],getString(R.string.LastTimeUsed) + passwordbit[1] + '.' + passwordbit[2],R.drawable.sample));
        }

        adapter = new RVAdapter(persons);
        passwords_listid.setAdapter(adapter);

        adapter.setOnItemClickListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(getActivity(), passwordviewer_activity.class);
                Bundle argument=new Bundle();
                argument.putInt("itemposition",position);
                intent.putExtras(argument);
                startActivity(intent);
                adapter.notifyDataSetChanged();
                adapter.notifypersonChanged(passwordpath);
            }
        });

        adapter.setOnItemLongClickListener(new RVAdapter.OnItemLongClickListener() {

            @Override
            public void onItemLongClick(View view, int position) {
                Snackbar mySnackbar = Snackbar.make(getActivity().findViewById(R.id.passwordslistcontainer),
                        R.string.hasdeleted, Snackbar.LENGTH_SHORT);
                mySnackbar.setAction(R.string.undo_string, new MyUndoListener());
                mySnackbar.show();

                persons.remove(position);
                adapter.notifyDataSetChanged();
                adapter.notifypersonChanged(passwordpath);
                // passwords_listid.invalidate();

                String content = readFile(passwordpath);
                String[] group = content.split("\n");
                content ="";
                for(int i=0;i<group.length;i++){
                    if(i==position)
                    {
                        lastdeletedposition = i;
                        lastdeletedcontent = group[i] + "\n";

                        continue;
                    }

                    content = content + group[i] + "\n";
                }
                writeToFile(passwordpath,content);


            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String arg0) {
                // filter listview
                Log.v("here","here");

                return true;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                Log.v("changehere","changehere");
                if (TextUtils.isEmpty(arg0)) {
                    adapter.getFilter().filter("");
                    Log.i("Nomad", "onQueryTextChange Empty String");
                    // placesListView.clearTextFilter();
                } else {
                    Log.i("Nomad", "onQueryTextChange " + arg0.toString());
                    adapter.getFilter().filter(arg0.toString());
                }
                return false;
            }
        });
      /*  mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mSearchView.setQuery("",true);
                Log.v("close","close");
                adapter.DisabledFilter();
                return false;
            }
        });*/



    }

    public class MyUndoListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String[] itemsplit = lastdeletedcontent.split("\b");

            persons.add(lastdeletedposition,new Person(itemsplit[0],getString(R.string.LastTimeUsed) + itemsplit[1] + '.' + itemsplit[2],R.drawable.sample));

            String content = readFile(passwordpath);
            String[] group = content.split("\n");
            if(group.length<lastdeletedposition){
                content ="";
                for(int i=0;i<group.length;i++){
                    if(i==lastdeletedposition)
                    {
                        content = content + lastdeletedcontent;
                    }

                    content = content + group[i] + "\n";
                }
            }else{
                content = content + lastdeletedcontent;
            }



            writeToFile(passwordpath,content);

            adapter.notifyDataSetChanged();
            adapter.notifypersonChanged(passwordpath);
            // Code to undo the user's last action
        }
    }


    public static void refreshlistview(int index,Person itemname){
        persons.set(index,itemname);
        //passwordsgrouplist.set(index,itemname);
        adapter.notifyDataSetChanged();
        adapter.notifypersonChanged(passwordpath);
        passwords_listid.invalidate();


    }

    public static void refreshlistview(Person itemname) {

        persons.add(itemname);
        adapter.notifyDataSetChanged();
        adapter.notifypersonChanged(passwordpath);
    }

    public static Resources makepicture(){


    }


}



