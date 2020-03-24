package com.wjh776a68.monthlycodebook;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.wjh776a68.monthlycodebook.fileio;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static android.content.res.Resources.getSystem;
import static com.wjh776a68.monthlycodebook.fileio.readFile;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{
    private List<Person> persons;
    private List<Person> mFilterList;
    private List<Person> personsbackup;

    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    RVAdapter(List<Person> persons){
        this.persons = persons;
        this.personsbackup = persons;
        this.mFilterList = this.persons;
    }




    @Override
    public int getItemCount() {
        return persons.size();
    }
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.passwords_itemlist, viewGroup, false);
       PersonViewHolder pvh = new PersonViewHolder(v);

        return pvh;
    }
    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, final int i) {
        personViewHolder.personName.setText(persons.get(i).name);
        personViewHolder.personAge.setText(persons.get(i).age);
        personViewHolder.personPhoto.setImageResource(persons.get(i).photoId);

        View itemView = ((LinearLayout) personViewHolder.itemView).getChildAt(0);

        if (mOnItemClickListener != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = personViewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(personViewHolder.itemView, position);
                }
            });
        }

        if (mOnItemLongClickListener != null) {
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = personViewHolder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(personViewHolder.itemView ,position);
                    return false;
                }
            });
        }


    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView personName;
        TextView personAge;
        ImageView personPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            personName = (TextView)itemView.findViewById(R.id.card_view_head);
            personAge = (TextView)itemView.findViewById(R.id.card_view_content);
            personPhoto = (ImageView)itemView.findViewById(R.id.card_view_image);
        }

    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

   // public void DisabledFilter(){
     //   persons = personsbackup;
    //    notifyDataSetChanged();
   // }



    //@Override
    public Filter getFilter() {
        return new Filter() {
            //执行过滤操作
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    //没有过滤的内容，则使用源数据
                    persons = personsbackup;//excellent
                    mFilterList = persons;
                    personsbackup = persons;
                } else {
                    persons = personsbackup;
                    List<Person> filteredList = new ArrayList<>();

                    for (int i=0;i<persons.size();i++) {
                        //这里根据需求，添加匹配规则
                        String str = persons.get(i).name;
                        Log.v("striswhat",str);
                        if (str.contains(charString)) {
                            filteredList.add( persons.get(i));
                        }
                    }

                    mFilterList = filteredList;
                   // Log.v("mFilterList",mFilterList.toString());
                    Log.v("mFilterList1",mFilterList.toString());
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilterList;
                return filterResults;
            }
            //把过滤后的值返回出来
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilterList = (List<Person>) filterResults.values;
               persons = mFilterList;
                Log.v("mFilterList2",mFilterList.toString());
               notifyDataSetChanged();
            }
        };
    }

    public void notifypersonChanged(String passwordpath){//temporary cancel this function
        String filecontent = readFile(passwordpath);
        String[] passwordsgroup = filecontent.split("\n");
       // personsbackup.clear();
        for(int i=0;i<passwordsgroup.length;i++){

            String[] passwordbit = passwordsgroup[i].split("\b");

           // personsbackup.add(new Person(passwordbit[0],  /*Resources.getSystem().getString(R.string.LastTimeUsed)*/"Last Used times: " + passwordbit[1] + '.' + passwordbit[2],R.drawable.sample));
        }

        notifyDataSetChanged();
    }


}
