package com.electroweb.restaurantandbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ItemFragment extends Fragment {

    private String title;

    RecyclerView rvItemList;
    ArrayList<ListModel> itemListArray=new ArrayList<>();

    public ItemFragment() {
        // Required empty public constructor
    }

    public static ItemFragment newInstance(int page, String title) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        rvItemList = (RecyclerView)view.findViewById(R.id.rvItemList);
        rvItemList.setHasFixedSize(true);

        RecyclerView.LayoutManager rvManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rvItemList.setLayoutManager(rvManager);

        itemListArray.clear();

        DatabaseHelper myDb = new DatabaseHelper(getActivity());

        String rs = getResources().getString(R.string.RS);

        LinearLayout llCheckOut = (LinearLayout)view.findViewById(R.id.llCheckOut);
        TextView tvTotalPrice = (TextView)view.findViewById(R.id.tvTotalPrice);
        TextView tvTotalOrder = (TextView)view.findViewById(R.id.tvTotalOrder);

        llCheckOut.setVisibility(View.GONE);

        Cursor res = myDb.getCountData();
        String count = res.getString(res.getColumnIndex(res.getColumnName(1)));
        if(count.equals("0"))
        {
            llCheckOut.setVisibility(View.GONE);
            /*String total = res.getString(res.getColumnIndex(res.getColumnName(0)));
            tvTotalOrder.setText(count);
            tvTotalPrice.setText(rs+" "+total);*/
        }
        else
        {
            llCheckOut.setVisibility(View.VISIBLE);
            String total = res.getString(res.getColumnIndex(res.getColumnName(0)));
            DecimalFormat df = new DecimalFormat("0.00");
            tvTotalOrder.setText(count);
            tvTotalPrice.setText(rs+" "+df.format(Double.parseDouble(total)));

        }

        llCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),CheckoutActivity.class);
                startActivity(i);
            }
        });

        GetItemList getItemList = new GetItemList();
        getItemList.execute();

        return view;
    }


    private class GetItemList extends AsyncTask<String,Void,String> {

        String status,message;

        @Override
        protected String doInBackground(String... strings) {

            JSONObject joItem = new JSONObject();
            try {
                joItem.put("CategoryId",title);
                Postdata postdata = new Postdata();
                String pdUser=postdata.post(HomeActivity.BASE_URL+"itemmaster.aspx",joItem.toString());
                JSONObject j = new JSONObject(pdUser);
                status=j.getString("status");
                if(status.equals("1"))
                {
                    Log.d("Like","Successfully");
                    message=j.getString("message");
                    JSONArray JsArry=j.getJSONArray("ItemMaster");
                    for (int i=0;i<JsArry.length();i++)
                    {
                        JSONObject jo=JsArry.getJSONObject(i);

                        String ItemId =jo.getString("ItemId");
                        String ItemName = jo.getString("ItemName");
                        String ItemImage =jo.getString("ItemImage");
                        String ItemCode =jo.getString("ItemCode");
                        String HSNCode =jo.getString("HSNCode");
                        String MenuRate =jo.getString("MenuRate");
                        String GSTRate =jo.getString("GSTRate");
                        String CatId =jo.getString("CategoryId");
                        String Item_id_name = ItemCode +"-"+ ItemName;

                        ListModel listModel = new ListModel(ItemId,ItemName,ItemImage,ItemCode,HSNCode,MenuRate,GSTRate,CatId,Item_id_name);
                        itemListArray.add(listModel);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(status.equals("1"))
            {
                RecyclerView.Adapter rvAdapterItemList=new itemListAdapter(getActivity(),itemListArray);
                rvItemList.setAdapter(rvAdapterItemList);
            }
            else
            {
                Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
            }

        }
    }
}
