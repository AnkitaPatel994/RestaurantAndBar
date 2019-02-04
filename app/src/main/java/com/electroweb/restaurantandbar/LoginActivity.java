package com.electroweb.restaurantandbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText txtUName,txtPassword;
    SessionManager session;
    String BASE_URL;
    String IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUName = (EditText) findViewById(R.id.txtUName);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        session = new SessionManager(getApplicationContext());

        IP = getIntent().getExtras().getString("ip");

        BASE_URL = "http://"+IP+"/";

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckLogin checkLogin = new CheckLogin();
                checkLogin.execute();
            }
        });

    }

     private class CheckLogin extends AsyncTask<String,Void,String> {

        String status,message,LoginId,OutletId,OutletName,BranchName,AdminIsActive,UserIsActive;
        String Username = txtUName.getText().toString();
        String Password = txtPassword.getText().toString();
         ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Loading....");
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            JSONObject joLogin = new JSONObject();
            try {

                joLogin.put("UserName",Username);
                joLogin.put("Password",Password);
                Postdata postdata = new Postdata();
                String pdUser=postdata.post(BASE_URL+"loginmaster.aspx",joLogin.toString());
                JSONObject j = new JSONObject(pdUser);
                status=j.getString("status");
                if(status.equals("1"))
                {
                    Log.d("Like","Successfully");
                    message=j.getString("message");
                    JSONArray JsArry=j.getJSONArray("LoginMaster");
                    for (int i=0;i<JsArry.length();i++)
                    {
                        JSONObject jo=JsArry.getJSONObject(i);

                        String Name =jo.getString("Name");
                        String DesignationName =jo.getString("DesignationName");
                        LoginId =jo.getString("LoginId");
                        OutletId =jo.getString("OutletId");
                        OutletName =jo.getString("OutletName");
                        BranchName =jo.getString("BranchName");
                        AdminIsActive =jo.getString("AdminIsActive");
                        UserIsActive =jo.getString("UserIsActive");

                    }
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
                if (AdminIsActive.equals("YES") && UserIsActive.equals("YES"))
                {
                    session.createLoginSession(Username,Password,LoginId,OutletId,OutletName,BranchName,IP);

                    Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Invalid Username and Password",Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(LoginActivity.this,"Invalid Username and Password",Toast.LENGTH_SHORT).show();

            }
        }
    }
}
