package com.goldenfuturecommunication.mysqljson;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button button,search;
    EditText Name,Email,search_email;
    String lemail;
    String server_url="http://192.168.43.37/insert.php";
    String server_url2="http://192.168.43.37/login.php";//"https://goldenfuturecommunication.com/get_data.php"
    AlertDialog.Builder builder;
    List<List_Data>list_data;
    TextView search_name;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        button=(Button)findViewById(R.id.button);
        search=(Button)findViewById(R.id.search);
        Name=(EditText)findViewById(R.id.name);
        Email=(EditText)findViewById(R.id.email);
        search_email=(EditText)findViewById(R.id.search_email);
        search_name=(TextView)findViewById(R.id.search_name);
        list_data=new ArrayList<>();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name,email;
                name=Name.getText().toString().trim();
                email=Email.getText().toString().trim();

                StringRequest stringRequest=new StringRequest(Request.Method.POST, server_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();




                            }
                        }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Error!",Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params=new HashMap<String, String>();
                        params.put("name",name);  //name should be same as database
                        params.put("email",email);

                        return params;
                    }
                };

                MySingleton.getInstance(MainActivity.this).addTorequestque(stringRequest);

            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name;
                name=search_email.getText().toString().trim();
                StringRequest stringRequest=new StringRequest(Request.Method.POST, server_url2, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray array=jsonObject.getJSONArray("data");
                            for (int i=0; i<array.length(); i++ ){
                                JSONObject ob=array.getJSONObject(i);
                                List_Data listData=new List_Data(ob.getString("name"),ob.getString("email"));
                                list_data.add(listData);
                                search_name.setText(listData.getEmail());
                            }
                            //rv.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params=new HashMap<String, String>();
                        params.put("email",search_email.getText().toString().trim());  //name should be same as database

                        return params;
                    }
                };
                RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        });




    }
}
