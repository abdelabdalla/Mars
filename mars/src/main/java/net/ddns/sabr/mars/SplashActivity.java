package net.ddns.sabr.mars;

/*
*Copyright 2016 Abdel-Rahim Abdalla
*/

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.abdel.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(isConnected()) {
            new EndpointsAsyncTask().execute(getApplicationContext());
        } else {
            String result = "{\n" +
                    "  \"date\":\"31/3/2016\",\n" +
                    "  \"entries\":[\n" +
                    "    [\"RAF Recruits\",\"Blues\",\"Sgt T Hard\",\"MRAF Cox\",\"Drill\"],\n" +
                    "    [\"Army Recruits\",\"MTP\",\"CSM D Sack\",\"5Lt Dorris\",\"Drill\"],\n" +
                    "    [\"RAF Advanced\",\"Blues\",\"LCpl No '1' Cares\",\"MRAF Cox\",\"Ultilearn Stuff. In e4\"],\n" +
                    "    [\"Army Advanced\",\"MTP\",\"FSgt L Brioche\",\"5Lt Dorris\",\"Wait for LSW's\"],\n" +
                    "    [\"REME\",\"Blues/MTP\",\"SSgt N V Keen\",\"5Lt Dorris\",\"Presentations with Cpl A Awesome\"],\n" +
                    "    [\"Signals\",\"Blues/MTP\",\"Cpl V Keen\",\"MRAF Cox\",\"Do something useless as usual\"]\n" +
                    "    ]\n" +
                    "}";

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("json",result);
            startActivity(intent);
            finish();
        }

    }

    private boolean isConnected(){
        try{
            //http://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html
            ConnectivityManager cm =
                    (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();}
        catch (NullPointerException e){
            return false;
        }
    }

    class EndpointsAsyncTask extends AsyncTask<Context, Void, String> {
        private MyApi myApiService = null;
        private Context context;

        @Override
        protected String doInBackground(Context... params) {
            if (myApiService == null) {
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://reading-school-ccf.appspot.com/_ah/api/");
                myApiService = builder.build();
            }

            context = params[0];
            try {
                return myApiService.download().execute().getData();
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.v("ayyy",result);
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("json",result);
            startActivity(intent);
            finish();
        }
    }

}

