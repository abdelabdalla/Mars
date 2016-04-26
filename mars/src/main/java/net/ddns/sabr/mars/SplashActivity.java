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
import android.util.Pair;

import com.example.abdel.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(isConnected()) {
            new EndpointsAsyncTask().execute(getApplicationContext());
        } else {
            String json;
            String news;

            File jsonFile = new File(getApplicationContext().getFilesDir(), "json");
            File newsFile = new File(getApplicationContext().getFilesDir(), "news");

            try {
                json = FileUtils.readFileToString(jsonFile);
                news = FileUtils.readFileToString(newsFile);
            } catch (Exception e) {
                e.printStackTrace();
               json = "{\n" +
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
                news = "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<body>\n" +
                        "\n" +
                        "<h1>Something's wrong</h1>\n" +
                        "\n" +
                        "<p>" + e.getMessage() + "</p>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>";
            }

            Log.v("newshere",json);

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("json",json);
            intent.putExtra("news",news);
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

    class EndpointsAsyncTask extends AsyncTask<Context, Void, Pair<String, String>> {
        private MyApi myApiService = null;
        private Context context;

        @Override
        protected Pair<String, String> doInBackground(Context... params) {
            if (myApiService == null) {
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://reading-school-ccf.appspot.com/_ah/api/");
                myApiService = builder.build();
            }

            context = params[0];
            try {

                String s = myApiService.download().execute().getData();
                String sb = myApiService.download().execute().getFile();
                //byte[] b = Base64.decodeBase64(sb);

                Pair<String, String> pair = new Pair<>(s, sb);
                return pair;
            } catch (IOException e) {
                Pair<String, String> a = new Pair<>(e.getMessage(),"<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<body>\n" +
                        "\n" +
                        "<h1>Something's wrong</h1>\n" +
                        "\n" +
                        "<p>My first paragraph.</p>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>");
                return a;
            }
        }

        @Override
        protected void onPostExecute(Pair<String, String> result) {

            FileOutputStream outputStream;

            try{
                outputStream = openFileOutput("json", Context.MODE_PRIVATE);
                outputStream.write(result.first.getBytes());
                outputStream.close();

                outputStream = openFileOutput("news", Context.MODE_PRIVATE);
                outputStream.write(result.second.getBytes());
                outputStream.close();
            } catch (Exception e){

            }

            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("json",result.first);
            String b = result.second;
            intent.putExtra("news",b);
            startActivity(intent);
            finish();
        }
    }

}

