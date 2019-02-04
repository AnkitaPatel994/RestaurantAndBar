package com.electroweb.restaurantandbar;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.HashMap;

public class UDActivity extends AppCompatActivity {

    TextView txtItemUDName,tvUDPrice,txtUDQty,tvPriceUDTotal;
    ImageView ivUDMinus,ivUDPlus;
    LinearLayout llUpdate,llDelete;
    String id,itemname,rate,qty,amount;
    DatabaseHelper myDb;
    SessionManager session;
    int count= 1;
    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ud);

        myDb = new DatabaseHelper(UDActivity.this);

        session = new SessionManager(UDActivity.this);
        HashMap<String,String> user = session.getUserDetails();
        String outlet_name = user.get(SessionManager.outlet_name);
        String branch_name = user.get(SessionManager.branch_name);

        setTitle(outlet_name+" - "+branch_name);

        final String rs = getResources().getString(R.string.RS);

        df = new DecimalFormat("0.00");

        id = getIntent().getExtras().getString("id");
        itemname = getIntent().getExtras().getString("itemname");
        rate = getIntent().getExtras().getString("rate");
        qty = getIntent().getExtras().getString("qty");
        amount = getIntent().getExtras().getString("amount");

        txtItemUDName = (TextView)findViewById(R.id.txtItemUDName);
        txtItemUDName.setText(itemname);

        tvUDPrice = (TextView)findViewById(R.id.tvUDPrice);
        tvUDPrice.setText(rs+" "+df.format(Double.parseDouble(rate)));

        txtUDQty = (TextView)findViewById(R.id.txtUDQty);
        txtUDQty.setText(qty);

        tvPriceUDTotal = (TextView)findViewById(R.id.tvPriceUDTotal);
        tvPriceUDTotal.setText(rs+" "+df.format(Double.parseDouble(amount)));

        ivUDMinus = (ImageView)findViewById(R.id.ivUDMinus);
        ivUDPlus = (ImageView)findViewById(R.id.ivUDPlus);

        llUpdate = (LinearLayout) findViewById(R.id.llUpdate);
        llDelete = (LinearLayout)findViewById(R.id.llDelete);

        count = Integer.parseInt(qty);

        ivUDPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                txtUDQty.setText(String.valueOf(count));
                amount = df.format(Double.parseDouble(rate)*count);
                tvPriceUDTotal.setText(rs+" "+amount);
            }
        });

        ivUDMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count>1)
                {
                    count--;
                    txtUDQty.setText(String.valueOf(count));
                    amount = df.format(Double.parseDouble(rate)*count);
                    tvPriceUDTotal.setText(rs+" "+amount);
                }
            }
        });

        listAdapter.ITEMIDArray.clear();
        listAdapter.ITEMCODEArray.clear();
        listAdapter.ITEMNAMEArray.clear();
        listAdapter.RATEArray.clear();
        listAdapter.QTYArray.clear();
        listAdapter.AMOUNTArray.clear();
        listAdapter.GSTRATEArray.clear();

        llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer deletedRows = myDb.deleteData(id);
                if(deletedRows > 0)
                {
                    Cursor res = myDb.getCountData();
                    String count = res.getString(res.getColumnIndex(res.getColumnName(1)));
                    if(count.equals("0"))
                    {
                        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        /*Intent intent = new Intent(getApplicationContext(),CheckoutActivity.class);
                        startActivity(intent);
                        finish();*/
                        UDActivity.super.onBackPressed();
                    }

                }
                else
                {
                    Toast.makeText(UDActivity.this,"Data not Deleted",Toast.LENGTH_LONG).show();
                }

            }
        });

        llUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String qtyUD = txtUDQty.getText().toString();

                boolean isUpdate = myDb.updateData(id,qtyUD,amount);
                if(isUpdate == true)
                {
                    /*Intent intent = new Intent(getApplicationContext(),CheckoutActivity.class);
                    startActivity(intent);
                    finish();*/
                    UDActivity.super.onBackPressed();
                }
                else
                {
                    Toast.makeText(UDActivity.this,"Data not Updated",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
