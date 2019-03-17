package com.mrpteam.amozesh;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mrpteam.amozesh.Models.CatModel;
import com.mrpteam.amozesh.Models.HttpsTrustManager;
import com.mrpteam.amozesh.Models.VidModel;
import com.mrpteam.amozesh.Models.catadapter;
import com.mrpteam.amozesh.Models.vidadapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Movielist extends AppCompatActivity {
    ArrayList<VidModel> catList;
    vidadapter adapter;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist);
        catList = new ArrayList<>();
        getcat();

        id = getIntent().getExtras().getInt("id");
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new vidadapter(this, catList, new vidadapter.OnItemClickListener() {
            @Override
            public void onItemClick(VidModel image) {
                Intent i = new Intent(Movielist.this, Full.class);
                i.putExtra("id", image.getVideo());
                i.putExtra("vid", image.getName());

                startActivity(i);
            }

        });
        recyclerView.setAdapter(adapter);
    }
    public void getcat(){

        HttpsTrustManager.allowAllSSL();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "https://www.rosependar.ir/project/toys/vids.json", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i=0;i<response.length(); i++){
                        JSONObject obj = (JSONObject) response.get(i);
                        if (obj.getInt("cat")==id){
                            VidModel vidmodel = new VidModel(obj.getInt("cat"),obj.getString("name"),obj.getString("img"),obj.getString("video"));
                            if (vidmodel != null){
                                catList.add(vidmodel);
                            }
                        }

                    }
                    Log.i("", "onResponse: ");
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        jsonArrayRequest.setShouldCache(false);
        requestQueue.add(jsonArrayRequest);
    }
}
