package com.electroweb.restaurantandbar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class IPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);

        final EditText txtIP = (EditText)findViewById(R.id.txtIP);


        Button btnIP = (Button)findViewById(R.id.btnIP);
        btnIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ip = txtIP.getText().toString();
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                i.putExtra("ip",ip);
                startActivity(i);
                finish();
            }
        });


    }
}
