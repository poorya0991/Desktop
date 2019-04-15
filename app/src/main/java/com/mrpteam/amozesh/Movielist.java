package com.mrpteam.amozesh;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrpteam.amozesh.Models.CatModel;
import com.mrpteam.amozesh.Models.HttpsTrustManager;
import com.mrpteam.amozesh.Models.VidModel;
import com.mrpteam.amozesh.Models.catadapter;
import com.mrpteam.amozesh.Models.vidadapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Movielist extends AppCompatActivity {
    ArrayList<VidModel> catList;
    vidadapter adapter;
    String name;
    int id;
    private Typeface sans;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist);

        pref=Movielist.this.getSharedPreferences("amozesh", MODE_PRIVATE);

        catList = new ArrayList<>();

        ImageView backimg = findViewById(R.id.backimg);
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        id = getIntent().getExtras().getInt("id");
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new vidadapter(this, catList, new vidadapter.OnItemClickListener() {
            @Override
            public void onItemClick(VidModel image) {


               name=image.getName();
              String  fileName = image.getVideo().substring(image.getVideo().lastIndexOf('/') + 1, image.getVideo().length());
               String patch=  Environment.getExternalStorageDirectory() + File.separator + ".androiddeft/"+fileName;
                File file = new File( patch );


                if(file.exists()){
                                    Intent i = new Intent(Movielist.this, Full.class);
                i.putExtra("id", patch);
                i.putExtra("vid", image.getName());

                startActivity(i);
                }
                else {
                    new DownloadFile().execute(image.getVideo());

                }


            }

        });
        recyclerView.setAdapter(adapter);

        if(isNetworkConnected()){
            getcat();

        }
        else {
            Gson gson = new Gson();
            String json = pref.getString("vids","");
            Type type= new TypeToken<ArrayList<VidModel>>(){}.getType();
            catList = gson.fromJson(json,type);
            ArrayList<VidModel> catlist2=new ArrayList <>();
            for(int i=0;i<catList.size(); i++){
                if(catList.get(i).getId()==id){
                    catlist2.add(catList.get(i));
                }
            }

            Log.i("", "onResponse: ");
            adapter.newlist(catlist2);


            Log.i("", "onCreate: ");
        }


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

                            VidModel vidmodel = new VidModel(obj.getInt("cat"),obj.getString("name"),obj.getString("img"),obj.getString("video"));
                            if (vidmodel != null){
                                catList.add(vidmodel);
                            }


                    }
                    String json = new Gson().toJson(catList);
                    pref.edit().putString("vids",json).apply();

                    ArrayList<VidModel> catlist2=new ArrayList <>();
                    for(int i=0;i<catList.size(); i++){
                        if(catList.get(i).getId()==id){
                            catlist2.add(catList.get(i));
                        }
                    }


                    Log.i("", "onResponse: ");
                    adapter.newlist(catlist2);
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

    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(Movielist.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);


                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                //Append timestamp to file name

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + ".androiddeft/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d("", "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return folder + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();



            Intent i = new Intent(Movielist.this, Full.class);
            i.putExtra("id", message);
            i.putExtra("vid",name);

            startActivity(i);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
