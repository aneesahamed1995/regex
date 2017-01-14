package com.nmk.aneesahamed.regix;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
public EditText ins_id;
    public EditText password;
    public String ids;
    public String pswd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ins_id=(EditText)findViewById(R.id.ins_id);
        password=(EditText)findViewById(R.id.password);
    }
    public void auth(View view)
    {
         ids=ins_id.getText().toString().trim();
        pswd=password.getText().toString().trim();
        if(!TextUtils.isEmpty(ids)&& !TextUtils.isEmpty(pswd))
        {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected())
            {

                aunthenticate(ids,pswd);
            }
            else
            {
                alert();
            }
        }
       else if(TextUtils.isEmpty(ids)&& TextUtils.isEmpty(pswd))
        {
           ins_id.setError("ID Required!");
            ins_id.requestFocus();
            password.setError("password Required!");
            password.requestFocus();
        }
        else if(TextUtils.isEmpty(pswd))
        {
            password.setError("password Required!");
            password.requestFocus();
        }
        else if(TextUtils.isEmpty(ids))
        {
            ins_id.setError("ID Required!");
            ins_id.requestFocus();
        }

    }
    public void alert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("No Internet connection.Please connect with Internet ")
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }

                });


        AlertDialog alert = builder.create();
        alert.show();
    }
    public void aunthenticate(final String id,final String password)
    {
        setContentView(R.layout.progress);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://regix-aadhilrf.rhcloud.com/checkid.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.print(response);
                        System.out.println("first");
                        Toast.makeText(getApplicationContext(),response.trim(),Toast.LENGTH_LONG);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.print(error.toString());
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG);
                        retry();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("clg_id", id);
                params.put("password",password);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    public void retry()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("poor network ")
                .setCancelable(false)
                .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        aunthenticate(ids,pswd);
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
