package com.example.admin.projectmobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class postpic extends AppCompatActivity {
    RecyclerView postpic;
    private List<p_photo> Listphoto;
    MainActivity mn;
    Spinner sp;
    private String idna;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postpic);
        mn = new MainActivity();
        postpic = findViewById(R.id.repost);
        sp = findViewById(R.id.spinseach);
        Listphoto = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ArrayAdapter<String> array = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,getResources().getStringArray(R.array.type));
        sp.setAdapter(array);
        postpic.setLayoutManager(linearLayoutManager);
        postpic.setHasFixedSize(true);
        Bundle bd = getIntent().getExtras();
        if(bd != null) {
            Toast.makeText(getApplicationContext(),bd.getString("id"),Toast.LENGTH_SHORT).show();
            idna = bd.getString("id");
        }
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    loadpic(idna);
                }else{
                    String str = parent.getItemAtPosition(position).toString();
                    Searchpic(idna,str);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

    }
    private void loadpic(final String id){
        String url = "http://10.51.5.222/post.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for(int i = 0;i<array.length();i++) {
                        JSONObject posts = array.getJSONObject(i);
                        Listphoto.add(new p_photo (
                                posts.getString("img"),
                                posts.getString("name"),
                                posts.getString("url"),
                                posts.getInt("count")

                                ));
                        photoAdapter pa = new photoAdapter(Listphoto,getApplicationContext());
                        postpic.setAdapter(pa);

                    }

                    } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("id",id);
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void Searchpic(final String id,final String type){
        String url = "http://10.51.5.222/search.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Listphoto.clear();
                try {
                    JSONArray array = new JSONArray(response);
                    for(int i = 0;i<array.length();i++) {
                        JSONObject posts = array.getJSONObject(i);
                        Listphoto.add(new p_photo (
                                posts.getString("img"),
                                posts.getString("name"),
                                posts.getString("url"),
                                posts.getInt("count")
                        ));
                        photoAdapter pa = new photoAdapter(Listphoto,getApplicationContext());
                        pa.notifyDataSetChanged();
                        postpic.setAdapter(pa);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("id",id);
                param.put("type",type);
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
