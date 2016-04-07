package net.ddns.sabr.vulcan;

import android.app.DialogFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abdel.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.gson.Gson;

import net.ddns.sabr.session.Session;

import java.io.IOException;

public class MainActivity extends AppCompatActivity{

    final Session session = new Session();

    private int pos = 0;

    Button dateButton;
    private TextView ncoView;
    private TextView officerView;
    private TextView uniformView;
    private TextView notesView;

    private String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateButton = (Button) findViewById(R.id.dateButton);
        Button uploadButton = (Button) findViewById(R.id.uploadButton);
        ncoView = (TextView) findViewById(R.id.ncoView);
        officerView = (TextView) findViewById(R.id.officerView);
        uniformView = (TextView) findViewById(R.id.uniformView);
        notesView = (TextView) findViewById(R.id.notesView);


        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.groups, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        assert spinner != null;
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] t = getResources().getStringArray(R.array.groups);

                session.entries[pos][0] = t[pos];
                session.entries[pos][1] = ncoView.getText().toString();
                session.entries[pos][2] = officerView.getText().toString();
                session.entries[pos][3] = uniformView.getText().toString();
                session.entries[pos][4] = notesView.getText().toString();
                pos = position;
                ncoView.setText(session.entries[position][1]);
                officerView.setText(session.entries[position][2]);
                uniformView.setText(session.entries[position][3]);
                notesView.setText(session.entries[position][4]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment f = new DateFragment();
                f.show(getFragmentManager(), "TAG");
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] t = getResources().getStringArray(R.array.groups);
                session.entries[pos][0] = t[pos];
                session.entries[pos][1] = ncoView.getText().toString();
                session.entries[pos][2] = officerView.getText().toString();
                session.entries[pos][3] = uniformView.getText().toString();
                session.entries[pos][4] = notesView.getText().toString();

                Gson g = new Gson();

                json = g.toJson(session);

                new EndpointsAsyncTask().execute(getApplicationContext());

            }
        });
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
/*                json = "{\n" +
                        "  \"date\":\"31/3/2016\",\n" +
                        "  \"entries\":[\n" +
                        "    [\"RAF Recruits\",\"Blues\",\"Sgt T Hard\",\"MRAF Cox\",\"Drill\"],\n" +
                        "    [\"Army Recruits\",\"MTP\",\"CSM D Sack\",\"5Lt Dorris\",\"Drill\"],\n" +
                        "    [\"RAF Advanced\",\"Blues\",\"LCpl No '1' Cares\",\"MRAF Cox\",\"U5Ltilearn Stuff. In e4\"],\n" +
                        "    [\"Army Advanced\",\"MTP\",\"FSgt L Brioche\",\"5Lt Dorris\",\"Wait for LSW's\"],\n" +
                        "    [\"REME\",\"Blues/MTP\",\"SSgt N V Keen\",\"5Lt Dorris\",\"Presentations with Cpl A Awesome\"],\n" +
                        "    [\"Signals\",\"Blues/MTP\",\"Cpl V Keen\",\"MRAF Cox\",\"Do something useless as usual\"]\n" +
                        "    ]\n" +
                        "}";*/
                return myApiService.upload(json).execute().getData();
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.v("ayyy", result);
            Toast t = Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT);
            t.show();
        }
    }


}
