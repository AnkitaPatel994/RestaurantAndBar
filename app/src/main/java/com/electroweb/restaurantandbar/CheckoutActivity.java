package com.electroweb.restaurantandbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class CheckoutActivity extends AppCompatActivity {

    RecyclerView rvOrderList;
    DatabaseHelper myDb;
    ArrayList<HashMap<String,String>> listArray = new ArrayList<>();
    TextView tvTotal;
    Spinner spTableNo;
    EditText txtNP; /*,txtPName,txtPMobile*/
    LinearLayout llPlaceOrder;
    RecyclerView.Adapter rvAdapterList;
    ArrayList<String> spListArray=new ArrayList<>();

    ArrayList<String> ITEMIDArray = listAdapter.ITEMIDArray;
    ArrayList<String> ITEMCODEArray = listAdapter.ITEMCODEArray;
    ArrayList<String> ITEMNAMEArray = listAdapter.ITEMNAMEArray;
    ArrayList<String> RATEArray = listAdapter.RATEArray;
    ArrayList<String> QTYArray = listAdapter.QTYArray;
    ArrayList<String> AMOUNTArray = listAdapter.AMOUNTArray;
    ArrayList<String> GSTRATEArray = listAdapter.GSTRATEArray;

    String ITEMIDList ="";
    String ITEMCODEList ="";
    String ITEMNAMEList ="";
    String RATEList ="";
    String QTYList ="";
    String AMOUNTList ="";
    String GSTRATEList ="";

    SessionManager session;
    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        myDb = new DatabaseHelper(CheckoutActivity.this);

        df = new DecimalFormat("0.00");

        session = new SessionManager(CheckoutActivity.this);

        HashMap<String,String> user = session.getUserDetails();
        String outlet_name = user.get(SessionManager.outlet_name);
        String branch_name = user.get(SessionManager.branch_name);

        setTitle(outlet_name+" - "+branch_name);

        rvOrderList = (RecyclerView)findViewById(R.id.rvOrderList);
        rvOrderList.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        rvOrderList.setLayoutManager(layoutManager);

        llPlaceOrder = (LinearLayout)findViewById(R.id.llPlaceOrder);

        GetOrderList getOrderList = new GetOrderList();
        getOrderList.execute();

        tvTotal = (TextView)findViewById(R.id.tvTotal);

        DatabaseHelper myDb = new DatabaseHelper(getApplicationContext());

        String rs = getResources().getString(R.string.RS);
        Cursor res = myDb.getCountData();
        String total = res.getString(res.getColumnIndex(res.getColumnName(0)));
        tvTotal.setText(rs+" "+df.format(Double.parseDouble(total)));

        spTableNo = (Spinner)findViewById(R.id.spTableNo);
        GetSppinner getSppinner = new GetSppinner();
        getSppinner.execute();

        txtNP = (EditText) findViewById(R.id.txtNP);
        txtNP.setText("1");

        /*txtPName = (EditText) findViewById(R.id.txtPName);
        txtPMobile = (EditText) findViewById(R.id.txtPMobile);*/

        llPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (String ss : ITEMIDArray)
                {
                    if(ITEMIDList == "")
                    {
                        ITEMIDList += ss;
                    }
                    else
                    {
                        ITEMIDList += "," + ss;
                    }
                }

                for (String ss : ITEMCODEArray)
                {
                    if(ITEMCODEList == "")
                    {
                        ITEMCODEList += ss;
                    }
                    else
                    {
                        ITEMCODEList += "," + ss;
                    }
                }

                for (String ss : ITEMNAMEArray)
                {
                    if(ITEMNAMEList == "")
                    {
                        ITEMNAMEList += ss;
                    }
                    else
                    {
                        ITEMNAMEList += "," + ss;
                    }
                }

                for (String ss : RATEArray)
                {
                    if(RATEList == "")
                    {
                        RATEList += df.format(Double.parseDouble(ss));
                    }
                    else
                    {
                        RATEList += "," + df.format(Double.parseDouble(ss));
                    }
                }

                for (String ss : QTYArray)
                {
                    if(QTYList == "")
                    {
                        QTYList += ss;
                    }
                    else
                    {
                        QTYList += "," + ss;
                    }
                }

                for (String ss : AMOUNTArray)
                {
                    if(AMOUNTList == "")
                    {
                        AMOUNTList += df.format(Double.parseDouble(ss));
                    }
                    else
                    {
                        AMOUNTList += "," + df.format(Double.parseDouble(ss));
                    }
                }

                for (String ss : GSTRATEArray)
                {
                    if(GSTRATEList == "")
                    {
                        GSTRATEList += df.format(Double.parseDouble(ss));
                    }
                    else
                    {
                        GSTRATEList += "," + df.format(Double.parseDouble(ss));
                    }
                }

                HashMap<String,String> user = session.getUserDetails();

                String user_id = user.get(SessionManager.login_id);
                String user_name = user.get(SessionManager.user_name);
                String outlet_id = user.get(SessionManager.outlet_id);

                String table_id = "1";
                String table_no = spTableNo.getSelectedItem().toString();

                String item_id = ITEMIDList;
                String item_code = ITEMCODEList;
                String item_name = ITEMNAMEList;
                String item_rate = RATEList;
                String item_qty = QTYList;
                String item_amount = AMOUNTList;
                String item_gstrate = GSTRATEList;

                String noofperson = txtNP.getText().toString();
                /*String personname = txtPName.getText().toString();
                String personmobile = txtPMobile.getText().toString();*/

                InsertValue insertValue = new InsertValue(user_id,outlet_id,table_id,table_no,item_id,item_code,item_name,item_rate,item_qty,item_amount,item_gstrate,noofperson);
                insertValue.execute();

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private class GetOrderList extends AsyncTask<String,Void,String> {


        @Override
        protected String doInBackground(String... strings) {

            Cursor res = myDb.getAllData();

            while (res.moveToNext()){

                HashMap<String,String > hashMap = new HashMap<>();

                String ID = res.getString(0);
                String ITEMID = res.getString(3);
                String ITEMCODE = res.getString(4);
                String ITEMNAME = res.getString(5);
                String RATE = res.getString(6);
                String QTY = res.getString(7);
                String AMOUNT = res.getString(8);
                String GSTRATE = res.getString(9);

                hashMap.put("ID",ID);
                hashMap.put("ITEMID",ITEMID);
                hashMap.put("ITEMCODE",ITEMCODE);
                hashMap.put("ITEMNAME",ITEMNAME);
                hashMap.put("RATE",RATE);
                hashMap.put("QTY",QTY);
                hashMap.put("AMOUNT",AMOUNT);
                hashMap.put("GSTRATE",GSTRATE);

                listArray.add(hashMap);

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            rvAdapterList=new listAdapter(getApplicationContext(),listArray);
            rvOrderList.setAdapter(rvAdapterList);

        }
    }

    private class GetSppinner extends AsyncTask<String,Void,String> {

        String status,message;
        @Override
        protected String doInBackground(String... strings) {

            JSONObject joItem = new JSONObject();
            try {
                Postdata postdata = new Postdata();
                String pdUser=postdata.post(HomeActivity.BASE_URL+"tablemaster.aspx",joItem.toString());
                JSONObject j = new JSONObject(pdUser);
                status=j.getString("status");
                if(status.equals("1"))
                {
                    Log.d("Like","Successfully");
                    message=j.getString("message");
                    JSONArray JsArry=j.getJSONArray("TableMaster");
                    for (int i=0;i<JsArry.length();i++)
                    {
                        JSONObject jo=JsArry.getJSONObject(i);

                        String TableId =jo.getString("TableId");
                        String TableNo =jo.getString("TableNo");
                        String Capacity =jo.getString("Capacity");

                        spListArray.add(TableNo);

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
            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spListArray);
            spTableNo.setAdapter(spinnerArrayAdapter);
        }
    }

    private class InsertValue extends AsyncTask<String,Void,String> {

        String status,message,user_id,outlet_id,table_id,table_no,item_id,item_code,item_name,item_rate,item_qty,item_amount,item_gstrate,noofperson;
        ProgressDialog dialog;

        public InsertValue(String user_id, String outlet_id, String table_id, String table_no, String item_id, String item_code, String item_name, String item_rate, String item_qty, String item_amount, String item_gstrate, String noofperson) {
            this.user_id = user_id;
            this.outlet_id = outlet_id;
            this.table_id = table_id;
            this.table_no = table_no;
            this.item_id = item_id;
            this.item_code = item_code;
            this.item_name = item_name;
            this.item_rate = item_rate;
            this.item_qty = item_qty;
            this.item_amount = item_amount;
            this.item_gstrate = item_gstrate;
            this.noofperson = noofperson;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(CheckoutActivity.this);
            dialog.setMessage("Loading....");
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            JSONObject joUser = new JSONObject();
            try {
                joUser.put("UserId",user_id);
                joUser.put("OutletId",outlet_id);
                joUser.put("TableId",table_id);
                joUser.put("TableNo",table_no);
                joUser.put("ItemId",item_id);
                joUser.put("ItemCode",item_code);
                joUser.put("ItemName",item_name);
                joUser.put("Rate",item_rate);
                joUser.put("Qty",item_qty);
                joUser.put("Amount",item_amount);
                joUser.put("GSTRate",item_gstrate);
                joUser.put("Person",noofperson);
                Postdata postdata = new Postdata();
                String pdUser=postdata.post(HomeActivity.BASE_URL+"salesave.aspx",joUser.toString());
                JSONObject j = new JSONObject(pdUser);
                status=j.getString("status");
                if(status.equals("1"))
                {
                    Log.d("Like","Successfully");
                    message=j.getString("message");
                }
                else
                {
                    Log.d("Like","Successfully");
                    message=j.getString("message");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if(status.equals("1"))
            {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                Integer deletedRows = myDb.deleteTable();
                if(deletedRows > 0)
                {
                    Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(i);
                    finish();

                    ITEMIDArray.clear();
                    ITEMCODEArray.clear();
                    ITEMNAMEArray.clear();
                    RATEArray.clear();
                    QTYArray.clear();
                    AMOUNTArray.clear();
                    GSTRATEArray.clear();
                }
                else
                {
                    Toast.makeText(CheckoutActivity.this, "Data not Deleted", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                ITEMIDArray.clear();
                ITEMCODEArray.clear();
                ITEMNAMEArray.clear();
                RATEArray.clear();
                QTYArray.clear();
                AMOUNTArray.clear();
                GSTRATEArray.clear();
            }
        }
    }
}
