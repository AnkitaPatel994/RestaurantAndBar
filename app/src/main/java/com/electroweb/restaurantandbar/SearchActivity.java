package com.electroweb.restaurantandbar;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {

    EditText txtSearch;
    ImageView ivClose;
    RecyclerView rvSearchList;
    ArrayList<ListModel> itemListArray=new ArrayList<>();
    itemSearchListAdapter rvAdapterItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        txtSearch = (EditText)findViewById(R.id.txtSearch);
        ivClose = (ImageView)findViewById(R.id.ivClose);

        rvSearchList = (RecyclerView)findViewById(R.id.rvSearchList);
        rvSearchList.setHasFixedSize(true);

        itemListArray.clear();

        DatabaseHelper myDb = new DatabaseHelper(getApplicationContext());

        String rs = getResources().getString(R.string.RS);

        LinearLayout llCheckOut = (LinearLayout)findViewById(R.id.llCheckOut1);
        TextView tvTotalPrice = (TextView)findViewById(R.id.tvTotalPrice1);
        TextView tvTotalOrder = (TextView)findViewById(R.id.tvTotalOrder1);

        llCheckOut.setVisibility(View.GONE);

        Cursor res = myDb.getCountData();
        String count = res.getString(res.getColumnIndex(res.getColumnName(1)));
        if(count.equals("0"))
        {
            llCheckOut.setVisibility(View.GONE);
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
                Intent i = new Intent(getApplicationContext(),CheckoutActivity.class);
                startActivity(i);
            }
        });

        RecyclerView.LayoutManager rvManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        rvSearchList.setLayoutManager(rvManager);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSearch.setText("");
            }
        });

        GetSearchItemList getSearchItemList = new GetSearchItemList();
        getSearchItemList.execute();

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                rvAdapterItemList.getFilter().filter(s.toString());
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private class GetSearchItemList extends AsyncTask<String,Void,String> {

        String status,message;

        @Override
        protected String doInBackground(String... strings) {

            JSONObject joItem = new JSONObject();
            try {
                joItem.put("CategoryId","%");
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
                        String ItemName =jo.getString("ItemName");
                        String ItemImage =jo.getString("ItemImage");
                        String ItemCode =jo.getString("ItemCode");
                        String HSNCode =jo.getString("HSNCode");
                        String MenuRate =jo.getString("MenuRate");
                        String GSTRate =jo.getString("GSTRate");
                        String CatId =jo.getString("CategoryId");
                        String Item_id_name = ItemCode +" - "+ ItemName;

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
                rvAdapterItemList=new itemSearchListAdapter(getApplicationContext(),itemListArray);
                rvSearchList.setAdapter(rvAdapterItemList);
            }
            else
            {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
            }

        }
    }

}
