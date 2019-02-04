package com.electroweb.restaurantandbar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    SessionManager session;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());
        flag = session.checkLogin();

        Thread background = new Thread()
        {
            public void run()
            {
                try {
                    sleep(5*1000);

                    if (flag == 1)
                    {
                        Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Intent i = new Intent(getApplicationContext(),IPActivity.class);
                        startActivity(i);
                        finish();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        background.start();
    }
}
