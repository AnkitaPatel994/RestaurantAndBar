package com.electroweb.restaurantandbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager vpPager;
    ArrayList<String> tabTitles=new ArrayList<>();
    public static ArrayList<String> tabTitlesId=new ArrayList<>();
    Pager adapter;
    SessionManager session;
    static String BASE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabTitles.clear();
        tabTitlesId.clear();

        session = new SessionManager(getApplicationContext());

        session.checkLogin();

        HashMap<String,String> user = session.getUserDetails();
        String ip_address = user.get(SessionManager.ip_address);
        BASE_URL = "http://"+ip_address+"/";

        String outlet_name = user.get(SessionManager.outlet_name);
        String branch_name = user.get(SessionManager.branch_name);

        toolbar.setTitle(outlet_name +" - "+ branch_name);

        vpPager = (ViewPager) findViewById(R.id.vpPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(vpPager);

        GetTabCategrory tabCat=new GetTabCategrory();
        tabCat.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_search)
        {
            Intent i = new Intent(getApplicationContext(),SearchActivity.class);
            startActivity(i);
        }
        else if (id == R.id.menu_logout)
        {
            session.logoutUser();
            finish();
        }
        return super.onOptionsItemSelected(item);
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
        finishAffinity();
    }

    private class GetTabCategrory extends AsyncTask<String,Void,String> {

        String status,message;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog=new ProgressDialog(HomeActivity.this);
            dialog.setMessage("Loading....");
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            JSONObject joUser=new JSONObject();
            try {

                Postdata postdata = new Postdata();
                String pdUser=postdata.post(BASE_URL+"category.aspx",joUser.toString());
                JSONObject j = new JSONObject(pdUser);
                status=j.getString("status");
                if(status.equals("1"))
                {
                    Log.d("Like","Successfully");
                    message=j.getString("message");
                    JSONArray JsArry=j.getJSONArray("Category");
                    for (int i=0;i<JsArry.length();i++)
                    {
                        JSONObject jo=JsArry.getJSONObject(i);

                        String id = jo.getString("CategoryId");
                        String name = jo.getString("CategoryName");

                        tabTitlesId.add(id);
                        tabTitles.add(name);
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
            dialog.dismiss();
            adapter = new Pager(getSupportFragmentManager());
            for (int i = 0; i < tabTitles.size(); i++) {
                adapter.addFrag(new ItemFragment(), tabTitles.get(i).trim());
            }
            vpPager.setAdapter(adapter);

        }
    }
}
