package com.electroweb.restaurantandbar;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.HashMap;

public class QtyActivity extends AppCompatActivity {

    TextView txtItemName,tvPrice,txtQty,tvPriceTotal;
    ImageView ivMinus,ivPlus;
    LinearLayout llAdd;
    SessionManager session;
    String amount = "0";
    int count=1;
    String user_name,login_id,itemId,ItemCode,itemName,itemPrice,GSTRate,CatId;
    DecimalFormat df;
    DatabaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qty);

        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mydb = new DatabaseHelper(QtyActivity.this);

        final String rs = getResources().getString(R.string.RS);
        session = new SessionManager(QtyActivity.this);

        HashMap<String,String> user = session.getUserDetails();
        String outlet_name = user.get(SessionManager.outlet_name);
        String branch_name = user.get(SessionManager.branch_name);

        setTitle(outlet_name+" - "+branch_name);

        txtItemName = (TextView)findViewById(R.id.txtItemName);

        tvPrice = (TextView)findViewById(R.id.tvPrice);

        ivMinus = (ImageView)findViewById(R.id.ivMinus);
        ivPlus = (ImageView)findViewById(R.id.ivPlus);
        txtQty = (TextView)findViewById(R.id.txtQty);

        llAdd = (LinearLayout)findViewById(R.id.llAdd);
        tvPriceTotal = (TextView)findViewById(R.id.tvPriceTotal);

        itemId = getIntent().getExtras().getString("itemId");
        ItemCode = getIntent().getExtras().getString("ItemCode");
        itemName = getIntent().getExtras().getString("itemName");
        itemPrice = getIntent().getExtras().getString("itemPrice");
        GSTRate = getIntent().getExtras().getString("GSTRate");
        CatId = getIntent().getExtras().getString("CatId");

        df = new DecimalFormat("0.00");

        txtItemName.setText(itemName);
        tvPrice.setText(rs+" "+itemPrice);
        tvPriceTotal.setText(rs+" "+itemPrice);

        amount = df.format(Double.parseDouble(itemPrice)*count);

        ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                txtQty.setText(String.valueOf(count));

                amount = df.format(Double.parseDouble(itemPrice)*count);
                tvPriceTotal.setText(rs+" "+amount);
            }
        });

        ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count>1)
                {
                    count--;
                    txtQty.setText(String.valueOf(count));
                    amount = df.format(Double.parseDouble(itemPrice)*count);
                    tvPriceTotal.setText(rs+" "+amount);
                }
            }
        });

        llAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String qty = txtQty.getText().toString();
                HashMap<String,String> user = session.getUserDetails();

                user_name = user.get(SessionManager.user_name);
                login_id = user.get(SessionManager.login_id);

                Cursor res = mydb.getItemNameData(itemId);
                String item_count = res.getString(res.getColumnIndex(res.getColumnName(0)));
                String ID = res.getString(res.getColumnIndex(res.getColumnName(1)));
                String QTYDB = res.getString(res.getColumnIndex(res.getColumnName(8)));

                if (item_count.equals("1"))
                {
                    int i = Integer.parseInt(qty);
                    int QTYTotal = i + Integer.parseInt(QTYDB);
                    String qtyUD = String.valueOf(QTYTotal);
                    amount = df.format(Double.parseDouble(itemPrice)*QTYTotal);

                    boolean isUpdate = mydb.updateData(ID,qtyUD,amount);
                    if(isUpdate == true)
                    {
                        QtyActivity.super.onBackPressed();

                        listAdapter.ITEMIDArray.clear();
                        listAdapter.ITEMCODEArray.clear();
                        listAdapter.ITEMNAMEArray.clear();
                        listAdapter.RATEArray.clear();
                        listAdapter.QTYArray.clear();
                        listAdapter.AMOUNTArray.clear();
                        listAdapter.GSTRATEArray.clear();
                    }
                    else
                    {
                        Toast.makeText(QtyActivity.this,"Data not Updated",Toast.LENGTH_LONG).show();
                    }
                }
                else if (item_count.equals("0"))
                {
                    boolean isInserted = mydb.insertData(login_id,user_name,itemId,ItemCode,itemName,itemPrice,qty,amount,GSTRate);
                    if(isInserted == true)
                    {
                        QtyActivity.super.onBackPressed();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Data Not Inserted",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
