package net.ddns.sabr.janus;

import android.app.DialogFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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

import net.ddns.sabr.marssupport.Session;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

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

        assert uploadButton != null;
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
                json = "{\n" +
                        "  \"date\": \"14/4/2016\",\n" +
                        "  \"entries\": [\n" +
                        "    [\n" +
                        "      \"RAF Field Day\",\n" +
                        "      \" \",\n" +
                        "      \"Blues\",\n" +
                        "      \"No CCF due to this event\",\n" +
                        "      \" \"\n" +
                        "    ]\n" +
                        "  ]\n" +
                        "}";

                File file = new File(Environment.getExternalStorageDirectory().toString() + "/ccf.docx");

                FileInputStream fis = new FileInputStream(file);
                byte[] b = readFully(fis);

                //String s = Base64.encodeBase64String(b);

                String html = "<html xmlns:v=\"urn:schemas-microsoft-com:vml\"\n" +
                        "xmlns:o=\"urn:schemas-microsoft-com:office:office\"\n" +
                        "xmlns:w=\"urn:schemas-microsoft-com:office:word\"\n" +
                        "xmlns:m=\"http://schemas.microsoft.com/office/2004/12/omml\"\n" +
                        "xmlns=\"http://www.w3.org/TR/REC-html40\">\n" +
                        "\n" +
                        "<head>\n" +
                        "<meta http-equiv=Content-Type content=\"text/html; charset=windows-1252\">\n" +
                        "<meta name=ProgId content=Word.Document>\n" +
                        "<meta name=Generator content=\"Microsoft Word 15\">\n" +
                        "<meta name=Originator content=\"Microsoft Word 15\">\n" +
                        "<link rel=File-List href=\"letter_files/filelist.xml\">\n" +
                        "<link rel=Edit-Time-Data href=\"letter_files/editdata.mso\">\n" +
                        "<!--[if !mso]>\n" +
                        "<style>\n" +
                        "v\\:* {behavior:url(#default#VML);}\n" +
                        "o\\:* {behavior:url(#default#VML);}\n" +
                        "w\\:* {behavior:url(#default#VML);}\n" +
                        ".shape {behavior:url(#default#VML);}\n" +
                        "</style>\n" +
                        "<![endif]--><!--[if gte mso 9]><xml>\n" +
                        " <o:DocumentProperties>\n" +
                        "  <o:Author>Frances Greaney</o:Author>\n" +
                        "  <o:LastAuthor>Abdel Abdalla</o:LastAuthor>\n" +
                        "  <o:Revision>2</o:Revision>\n" +
                        "  <o:TotalTime>231</o:TotalTime>\n" +
                        "  <o:LastPrinted>2015-10-08T15:58:00Z</o:LastPrinted>\n" +
                        "  <o:Created>2016-04-13T21:12:00Z</o:Created>\n" +
                        "  <o:LastSaved>2016-04-13T21:12:00Z</o:LastSaved>\n" +
                        "  <o:Pages>1</o:Pages>\n" +
                        "  <o:Words>391</o:Words>\n" +
                        "  <o:Characters>2230</o:Characters>\n" +
                        "  <o:Company>Reading School</o:Company>\n" +
                        "  <o:Lines>18</o:Lines>\n" +
                        "  <o:Paragraphs>5</o:Paragraphs>\n" +
                        "  <o:CharactersWithSpaces>2616</o:CharactersWithSpaces>\n" +
                        "  <o:Version>16.00</o:Version>\n" +
                        " </o:DocumentProperties>\n" +
                        " <o:OfficeDocumentSettings>\n" +
                        "  <o:AllowPNG/>\n" +
                        " </o:OfficeDocumentSettings>\n" +
                        "</xml><![endif]-->\n" +
                        "<link rel=dataStoreItem href=\"letter_files/item0001.xml\"\n" +
                        "target=\"letter_files/props002.xml\">\n" +
                        "<link rel=dataStoreItem href=\"letter_files/item0003.xml\"\n" +
                        "target=\"letter_files/props004.xml\">\n" +
                        "<link rel=dataStoreItem href=\"letter_files/item0005.xml\"\n" +
                        "target=\"letter_files/props006.xml\">\n" +
                        "<link rel=dataStoreItem href=\"letter_files/item0007.xml\"\n" +
                        "target=\"letter_files/props008.xml\">\n" +
                        "<link rel=themeData href=\"letter_files/themedata.thmx\">\n" +
                        "<link rel=colorSchemeMapping href=\"letter_files/colorschememapping.xml\">\n" +
                        "<!--[if gte mso 9]><xml>\n" +
                        " <w:WordDocument>\n" +
                        "  <w:EmbedTrueTypeFonts/>\n" +
                        "  <w:SaveSubsetFonts/>\n" +
                        "  <w:SpellingState>Clean</w:SpellingState>\n" +
                        "  <w:GrammarState>Clean</w:GrammarState>\n" +
                        "  <w:TrackMoves>false</w:TrackMoves>\n" +
                        "  <w:TrackFormatting/>\n" +
                        "  <w:PunctuationKerning/>\n" +
                        "  <w:DrawingGridHorizontalSpacing>6 pt</w:DrawingGridHorizontalSpacing>\n" +
                        "  <w:DrawingGridVerticalSpacing>6 pt</w:DrawingGridVerticalSpacing>\n" +
                        "  <w:DisplayHorizontalDrawingGridEvery>0</w:DisplayHorizontalDrawingGridEvery>\n" +
                        "  <w:DisplayVerticalDrawingGridEvery>3</w:DisplayVerticalDrawingGridEvery>\n" +
                        "  <w:UseMarginsForDrawingGridOrigin/>\n" +
                        "  <w:ValidateAgainstSchemas>false</w:ValidateAgainstSchemas>\n" +
                        "  <w:SaveIfXMLInvalid>false</w:SaveIfXMLInvalid>\n" +
                        "  <w:IgnoreMixedContent>false</w:IgnoreMixedContent>\n" +
                        "  <w:AlwaysShowPlaceholderText>false</w:AlwaysShowPlaceholderText>\n" +
                        "  <w:DoNotUnderlineInvalidXML/>\n" +
                        "  <w:DoNotPromoteQF/>\n" +
                        "  <w:LidThemeOther>EN-GB</w:LidThemeOther>\n" +
                        "  <w:LidThemeAsian>X-NONE</w:LidThemeAsian>\n" +
                        "  <w:LidThemeComplexScript>X-NONE</w:LidThemeComplexScript>\n" +
                        "  <w:DoNotShadeFormData/>\n" +
                        "  <w:Compatibility>\n" +
                        "   <w:SpaceForUL/>\n" +
                        "   <w:BalanceSingleByteDoubleByteWidth/>\n" +
                        "   <w:DoNotLeaveBackslashAlone/>\n" +
                        "   <w:ULTrailSpace/>\n" +
                        "   <w:DoNotExpandShiftReturn/>\n" +
                        "   <w:AdjustLineHeightInTable/>\n" +
                        "   <w:BreakWrappedTables/>\n" +
                        "   <w:SnapToGridInCell/>\n" +
                        "   <w:WrapTextWithPunct/>\n" +
                        "   <w:UseAsianBreakRules/>\n" +
                        "   <w:UseWord2010TableStyleRules/>\n" +
                        "   <w:DontGrowAutofit/>\n" +
                        "   <w:SplitPgBreakAndParaMark/>\n" +
                        "   <w:EnableOpenTypeKerning/>\n" +
                        "   <w:DontFlipMirrorIndents/>\n" +
                        "   <w:OverrideTableStyleHps/>\n" +
                        "   <w:UseFELayout/>\n" +
                        "  </w:Compatibility>\n" +
                        "  <w:NoLineBreaksAfter Lang=\"JA\">$([\\egikmoqsuwy{\u0081\u008F\u0090¢’</w:NoLineBreaksAfter>\n" +
                        "  <w:NoLineBreaksBefore Lang=\"JA\">!%),.:;?@ABCDEFGHIJKRSTUX[]bfhjlnprtvxz}\u0081\u008D¡£¤¥§¨©ª«¬\u00AD®¯°ÁÞßáãåìñŒŸŽƒ–‘‚“‡•…‹</w:NoLineBreaksBefore>\n" +
                        "  <m:mathPr>\n" +
                        "   <m:mathFont m:val=\"Cambria Math\"/>\n" +
                        "   <m:brkBin m:val=\"before\"/>\n" +
                        "   <m:brkBinSub m:val=\"&#45;-\"/>\n" +
                        "   <m:smallFrac m:val=\"off\"/>\n" +
                        "   <m:dispDef/>\n" +
                        "   <m:lMargin m:val=\"0\"/>\n" +
                        "   <m:rMargin m:val=\"0\"/>\n" +
                        "   <m:defJc m:val=\"centerGroup\"/>\n" +
                        "   <m:wrapIndent m:val=\"1440\"/>\n" +
                        "   <m:intLim m:val=\"subSup\"/>\n" +
                        "   <m:naryLim m:val=\"undOvr\"/>\n" +
                        "  </m:mathPr></w:WordDocument>\n" +
                        "</xml><![endif]--><!--[if gte mso 9]><xml>\n" +
                        " <w:LatentStyles DefLockedState=\"false\" DefUnhideWhenUsed=\"false\"\n" +
                        "  DefSemiHidden=\"false\" DefQFormat=\"false\" DefPriority=\"99\"\n" +
                        "  LatentStyleCount=\"372\">\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"0\" QFormat=\"true\" Name=\"Normal\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"9\" QFormat=\"true\" Name=\"heading 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"9\" SemiHidden=\"true\"\n" +
                        "   UnhideWhenUsed=\"true\" QFormat=\"true\" Name=\"heading 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"9\" SemiHidden=\"true\"\n" +
                        "   UnhideWhenUsed=\"true\" QFormat=\"true\" Name=\"heading 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"9\" SemiHidden=\"true\"\n" +
                        "   UnhideWhenUsed=\"true\" QFormat=\"true\" Name=\"heading 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"9\" SemiHidden=\"true\"\n" +
                        "   UnhideWhenUsed=\"true\" QFormat=\"true\" Name=\"heading 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"9\" SemiHidden=\"true\"\n" +
                        "   UnhideWhenUsed=\"true\" QFormat=\"true\" Name=\"heading 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"9\" SemiHidden=\"true\"\n" +
                        "   UnhideWhenUsed=\"true\" QFormat=\"true\" Name=\"heading 7\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"9\" SemiHidden=\"true\"\n" +
                        "   UnhideWhenUsed=\"true\" QFormat=\"true\" Name=\"heading 8\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"9\" SemiHidden=\"true\"\n" +
                        "   UnhideWhenUsed=\"true\" QFormat=\"true\" Name=\"heading 9\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"index 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"index 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"index 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"index 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"index 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"index 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"index 7\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"index 8\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"index 9\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"39\" SemiHidden=\"true\"\n" +
                        "   UnhideWhenUsed=\"true\" Name=\"toc 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"39\" SemiHidden=\"true\"\n" +
                        "   UnhideWhenUsed=\"true\" Name=\"toc 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"39\" SemiHidden=\"true\"\n" +
                        "   UnhideWhenUsed=\"true\" Name=\"toc 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"39\" SemiHidden=\"true\"\n" +
                        "   UnhideWhenUsed=\"true\" Name=\"toc 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"39\" SemiHidden=\"true\"\n" +
                        "   UnhideWhenUsed=\"true\" Name=\"toc 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"39\" SemiHidden=\"true\"\n" +
                        "   UnhideWhenUsed=\"true\" Name=\"toc 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"39\" SemiHidden=\"true\"\n" +
                        "   UnhideWhenUsed=\"true\" Name=\"toc 7\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"39\" SemiHidden=\"true\"\n" +
                        "   UnhideWhenUsed=\"true\" Name=\"toc 8\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"39\" SemiHidden=\"true\"\n" +
                        "   UnhideWhenUsed=\"true\" Name=\"toc 9\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Normal Indent\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"footnote text\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"annotation text\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"header\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"footer\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"index heading\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"35\" SemiHidden=\"true\"\n" +
                        "   UnhideWhenUsed=\"true\" QFormat=\"true\" Name=\"caption\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"table of figures\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"envelope address\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"envelope return\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"footnote reference\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"annotation reference\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"line number\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"page number\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"endnote reference\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"endnote text\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"table of authorities\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"macro\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"toa heading\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"List\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"List Bullet\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"List Number\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"List 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"List 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"List 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"List 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"List Bullet 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"List Bullet 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"List Bullet 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"List Bullet 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"List Number 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"List Number 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"List Number 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"List Number 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"10\" QFormat=\"true\" Name=\"Title\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Closing\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Signature\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"1\" SemiHidden=\"true\"\n" +
                        "   UnhideWhenUsed=\"true\" Name=\"Default Paragraph Font\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Body Text\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Body Text Indent\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"List Continue\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"List Continue 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"List Continue 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"List Continue 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"List Continue 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Message Header\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"11\" QFormat=\"true\" Name=\"Subtitle\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Salutation\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Date\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Body Text First Indent\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Body Text First Indent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Note Heading\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Body Text 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Body Text 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Body Text Indent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Body Text Indent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Block Text\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Hyperlink\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"FollowedHyperlink\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"22\" QFormat=\"true\" Name=\"Strong\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"20\" QFormat=\"true\" Name=\"Emphasis\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Document Map\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Plain Text\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"E-mail Signature\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"HTML Top of Form\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"HTML Bottom of Form\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Normal (Web)\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"HTML Acronym\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"HTML Address\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"HTML Cite\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"HTML Code\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"HTML Definition\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"HTML Keyboard\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"HTML Preformatted\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"HTML Sample\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"HTML Typewriter\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"HTML Variable\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Normal Table\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"annotation subject\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"No List\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Outline List 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Outline List 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Outline List 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Simple 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Simple 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Simple 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Classic 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Classic 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Classic 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Classic 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Colorful 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Colorful 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Colorful 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Columns 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Columns 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Columns 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Columns 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Columns 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Grid 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Grid 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Grid 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Grid 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Grid 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Grid 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Grid 7\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Grid 8\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table List 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table List 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table List 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table List 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table List 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table List 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table List 7\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table List 8\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table 3D effects 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table 3D effects 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table 3D effects 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Contemporary\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Elegant\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Professional\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Subtle 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Subtle 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Web 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Web 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Web 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Balloon Text\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"59\" Name=\"Table Grid\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Table Theme\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" Name=\"Placeholder Text\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"1\" QFormat=\"true\" Name=\"No Spacing\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"60\" Name=\"Light Shading\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"61\" Name=\"Light List\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"62\" Name=\"Light Grid\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"63\" Name=\"Medium Shading 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"64\" Name=\"Medium Shading 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"65\" Name=\"Medium List 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"66\" Name=\"Medium List 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"67\" Name=\"Medium Grid 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"68\" Name=\"Medium Grid 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"69\" Name=\"Medium Grid 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"70\" Name=\"Dark List\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"71\" Name=\"Colorful Shading\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"72\" Name=\"Colorful List\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"73\" Name=\"Colorful Grid\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"60\" Name=\"Light Shading Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"61\" Name=\"Light List Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"62\" Name=\"Light Grid Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"63\" Name=\"Medium Shading 1 Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"64\" Name=\"Medium Shading 2 Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"65\" Name=\"Medium List 1 Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" Name=\"Revision\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"34\" QFormat=\"true\"\n" +
                        "   Name=\"List Paragraph\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"29\" QFormat=\"true\" Name=\"Quote\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"30\" QFormat=\"true\"\n" +
                        "   Name=\"Intense Quote\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"66\" Name=\"Medium List 2 Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"67\" Name=\"Medium Grid 1 Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"68\" Name=\"Medium Grid 2 Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"69\" Name=\"Medium Grid 3 Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"70\" Name=\"Dark List Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"71\" Name=\"Colorful Shading Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"72\" Name=\"Colorful List Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"73\" Name=\"Colorful Grid Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"60\" Name=\"Light Shading Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"61\" Name=\"Light List Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"62\" Name=\"Light Grid Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"63\" Name=\"Medium Shading 1 Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"64\" Name=\"Medium Shading 2 Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"65\" Name=\"Medium List 1 Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"66\" Name=\"Medium List 2 Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"67\" Name=\"Medium Grid 1 Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"68\" Name=\"Medium Grid 2 Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"69\" Name=\"Medium Grid 3 Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"70\" Name=\"Dark List Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"71\" Name=\"Colorful Shading Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"72\" Name=\"Colorful List Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"73\" Name=\"Colorful Grid Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"60\" Name=\"Light Shading Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"61\" Name=\"Light List Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"62\" Name=\"Light Grid Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"63\" Name=\"Medium Shading 1 Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"64\" Name=\"Medium Shading 2 Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"65\" Name=\"Medium List 1 Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"66\" Name=\"Medium List 2 Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"67\" Name=\"Medium Grid 1 Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"68\" Name=\"Medium Grid 2 Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"69\" Name=\"Medium Grid 3 Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"70\" Name=\"Dark List Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"71\" Name=\"Colorful Shading Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"72\" Name=\"Colorful List Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"73\" Name=\"Colorful Grid Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"60\" Name=\"Light Shading Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"61\" Name=\"Light List Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"62\" Name=\"Light Grid Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"63\" Name=\"Medium Shading 1 Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"64\" Name=\"Medium Shading 2 Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"65\" Name=\"Medium List 1 Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"66\" Name=\"Medium List 2 Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"67\" Name=\"Medium Grid 1 Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"68\" Name=\"Medium Grid 2 Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"69\" Name=\"Medium Grid 3 Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"70\" Name=\"Dark List Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"71\" Name=\"Colorful Shading Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"72\" Name=\"Colorful List Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"73\" Name=\"Colorful Grid Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"60\" Name=\"Light Shading Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"61\" Name=\"Light List Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"62\" Name=\"Light Grid Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"63\" Name=\"Medium Shading 1 Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"64\" Name=\"Medium Shading 2 Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"65\" Name=\"Medium List 1 Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"66\" Name=\"Medium List 2 Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"67\" Name=\"Medium Grid 1 Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"68\" Name=\"Medium Grid 2 Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"69\" Name=\"Medium Grid 3 Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"70\" Name=\"Dark List Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"71\" Name=\"Colorful Shading Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"72\" Name=\"Colorful List Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"73\" Name=\"Colorful Grid Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"60\" Name=\"Light Shading Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"61\" Name=\"Light List Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"62\" Name=\"Light Grid Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"63\" Name=\"Medium Shading 1 Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"64\" Name=\"Medium Shading 2 Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"65\" Name=\"Medium List 1 Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"66\" Name=\"Medium List 2 Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"67\" Name=\"Medium Grid 1 Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"68\" Name=\"Medium Grid 2 Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"69\" Name=\"Medium Grid 3 Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"70\" Name=\"Dark List Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"71\" Name=\"Colorful Shading Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"72\" Name=\"Colorful List Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"73\" Name=\"Colorful Grid Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"19\" QFormat=\"true\"\n" +
                        "   Name=\"Subtle Emphasis\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"21\" QFormat=\"true\"\n" +
                        "   Name=\"Intense Emphasis\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"31\" QFormat=\"true\"\n" +
                        "   Name=\"Subtle Reference\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"32\" QFormat=\"true\"\n" +
                        "   Name=\"Intense Reference\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"33\" QFormat=\"true\" Name=\"Book Title\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"37\" SemiHidden=\"true\"\n" +
                        "   UnhideWhenUsed=\"true\" Name=\"Bibliography\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"39\" SemiHidden=\"true\"\n" +
                        "   UnhideWhenUsed=\"true\" QFormat=\"true\" Name=\"TOC Heading\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"41\" Name=\"Plain Table 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"42\" Name=\"Plain Table 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"43\" Name=\"Plain Table 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"44\" Name=\"Plain Table 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"45\" Name=\"Plain Table 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"40\" Name=\"Grid Table Light\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"46\" Name=\"Grid Table 1 Light\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"Grid Table 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"Grid Table 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"Grid Table 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"Grid Table 5 Dark\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"51\" Name=\"Grid Table 6 Colorful\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"52\" Name=\"Grid Table 7 Colorful\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"46\"\n" +
                        "   Name=\"Grid Table 1 Light Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"Grid Table 2 Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"Grid Table 3 Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"Grid Table 4 Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"Grid Table 5 Dark Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"51\"\n" +
                        "   Name=\"Grid Table 6 Colorful Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"52\"\n" +
                        "   Name=\"Grid Table 7 Colorful Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"46\"\n" +
                        "   Name=\"Grid Table 1 Light Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"Grid Table 2 Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"Grid Table 3 Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"Grid Table 4 Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"Grid Table 5 Dark Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"51\"\n" +
                        "   Name=\"Grid Table 6 Colorful Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"52\"\n" +
                        "   Name=\"Grid Table 7 Colorful Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"46\"\n" +
                        "   Name=\"Grid Table 1 Light Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"Grid Table 2 Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"Grid Table 3 Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"Grid Table 4 Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"Grid Table 5 Dark Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"51\"\n" +
                        "   Name=\"Grid Table 6 Colorful Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"52\"\n" +
                        "   Name=\"Grid Table 7 Colorful Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"46\"\n" +
                        "   Name=\"Grid Table 1 Light Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"Grid Table 2 Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"Grid Table 3 Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"Grid Table 4 Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"Grid Table 5 Dark Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"51\"\n" +
                        "   Name=\"Grid Table 6 Colorful Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"52\"\n" +
                        "   Name=\"Grid Table 7 Colorful Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"46\"\n" +
                        "   Name=\"Grid Table 1 Light Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"Grid Table 2 Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"Grid Table 3 Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"Grid Table 4 Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"Grid Table 5 Dark Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"51\"\n" +
                        "   Name=\"Grid Table 6 Colorful Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"52\"\n" +
                        "   Name=\"Grid Table 7 Colorful Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"46\"\n" +
                        "   Name=\"Grid Table 1 Light Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"Grid Table 2 Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"Grid Table 3 Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"Grid Table 4 Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"Grid Table 5 Dark Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"51\"\n" +
                        "   Name=\"Grid Table 6 Colorful Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"52\"\n" +
                        "   Name=\"Grid Table 7 Colorful Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"46\" Name=\"List Table 1 Light\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"List Table 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"List Table 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"List Table 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"List Table 5 Dark\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"51\" Name=\"List Table 6 Colorful\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"52\" Name=\"List Table 7 Colorful\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"46\"\n" +
                        "   Name=\"List Table 1 Light Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"List Table 2 Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"List Table 3 Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"List Table 4 Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"List Table 5 Dark Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"51\"\n" +
                        "   Name=\"List Table 6 Colorful Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"52\"\n" +
                        "   Name=\"List Table 7 Colorful Accent 1\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"46\"\n" +
                        "   Name=\"List Table 1 Light Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"List Table 2 Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"List Table 3 Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"List Table 4 Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"List Table 5 Dark Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"51\"\n" +
                        "   Name=\"List Table 6 Colorful Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"52\"\n" +
                        "   Name=\"List Table 7 Colorful Accent 2\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"46\"\n" +
                        "   Name=\"List Table 1 Light Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"List Table 2 Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"List Table 3 Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"List Table 4 Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"List Table 5 Dark Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"51\"\n" +
                        "   Name=\"List Table 6 Colorful Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"52\"\n" +
                        "   Name=\"List Table 7 Colorful Accent 3\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"46\"\n" +
                        "   Name=\"List Table 1 Light Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"List Table 2 Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"List Table 3 Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"List Table 4 Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"List Table 5 Dark Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"51\"\n" +
                        "   Name=\"List Table 6 Colorful Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"52\"\n" +
                        "   Name=\"List Table 7 Colorful Accent 4\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"46\"\n" +
                        "   Name=\"List Table 1 Light Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"List Table 2 Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"List Table 3 Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"List Table 4 Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"List Table 5 Dark Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"51\"\n" +
                        "   Name=\"List Table 6 Colorful Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"52\"\n" +
                        "   Name=\"List Table 7 Colorful Accent 5\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"46\"\n" +
                        "   Name=\"List Table 1 Light Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"47\" Name=\"List Table 2 Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"48\" Name=\"List Table 3 Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"49\" Name=\"List Table 4 Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"50\" Name=\"List Table 5 Dark Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"51\"\n" +
                        "   Name=\"List Table 6 Colorful Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" Priority=\"52\"\n" +
                        "   Name=\"List Table 7 Colorful Accent 6\"/>\n" +
                        "  <w:LsdException Locked=\"false\" SemiHidden=\"true\" UnhideWhenUsed=\"true\"\n" +
                        "   Name=\"Mention\"/>\n" +
                        " </w:LatentStyles>\n" +
                        "</xml><![endif]-->\n" +
                        "<style>\n" +
                        "<!--\n" +
                        " /* Font Definitions */\n" +
                        " @font-face\n" +
                        "\t{font-family:\"Cambria Math\";\n" +
                        "\tpanose-1:2 4 5 3 5 4 6 3 2 4;\n" +
                        "\tmso-font-charset:0;\n" +
                        "\tmso-generic-font-family:roman;\n" +
                        "\tmso-font-pitch:variable;\n" +
                        "\tmso-font-signature:-536870145 1107305727 0 0 415 0;\n" +
                        "\tmso-font-src:0;}\n" +
                        "@font-face\n" +
                        "\t{font-family:Calibri;\n" +
                        "\tpanose-1:2 15 5 2 2 2 4 3 2 4;\n" +
                        "\tmso-font-charset:0;\n" +
                        "\tmso-generic-font-family:swiss;\n" +
                        "\tmso-font-pitch:variable;\n" +
                        "\tmso-font-signature:-536870145 1073786111 1 0 415 0;\n" +
                        "\tmso-font-src:1;}\n" +
                        "@font-face\n" +
                        "\t{font-family:Tahoma;\n" +
                        "\tpanose-1:2 11 6 4 3 5 4 4 2 4;\n" +
                        "\tmso-font-charset:0;\n" +
                        "\tmso-generic-font-family:swiss;\n" +
                        "\tmso-font-pitch:variable;\n" +
                        "\tmso-font-signature:-520081665 -1073717157 41 0 66047 0;\n" +
                        "\tmso-font-src:2;}\n" +
                        "@font-face\n" +
                        "\t{font-family:\"AvenirNext LT Pro Regular\";\n" +
                        "\tpanose-1:0 0 0 0 0 0 0 0 0 0;\n" +
                        "\tmso-font-alt:Arial;\n" +
                        "\tmso-font-charset:0;\n" +
                        "\tmso-generic-font-family:swiss;\n" +
                        "\tmso-font-format:other;\n" +
                        "\tmso-font-pitch:variable;\n" +
                        "\tmso-font-signature:1 1342185546 0 0 155 0;\n" +
                        "\tmso-font-src:3;}\n" +
                        " /* Style Definitions */\n" +
                        " p.MsoNormal, li.MsoNormal, div.MsoNormal\n" +
                        "\t{mso-style-unhide:no;\n" +
                        "\tmso-style-qformat:yes;\n" +
                        "\tmso-style-parent:\"\";\n" +
                        "\tmargin-top:0cm;\n" +
                        "\tmargin-right:0cm;\n" +
                        "\tmargin-bottom:6.0pt;\n" +
                        "\tmargin-left:0cm;\n" +
                        "\tline-height:118%;\n" +
                        "\tmso-pagination:none;\n" +
                        "\tmso-layout-grid-align:none;\n" +
                        "\tpunctuation-wrap:simple;\n" +
                        "\ttext-autospace:none;\n" +
                        "\tfont-size:10.0pt;\n" +
                        "\tfont-family:\"Calibri\",sans-serif;\n" +
                        "\tmso-fareast-font-family:\"Times New Roman\";\n" +
                        "\tmso-fareast-theme-font:minor-fareast;\n" +
                        "\tmso-bidi-font-family:Calibri;\n" +
                        "\tcolor:black;\n" +
                        "\tmso-font-kerning:14.0pt;}\n" +
                        "p.MsoHeader, li.MsoHeader, div.MsoHeader\n" +
                        "\t{mso-style-priority:99;\n" +
                        "\tmso-style-link:\"Header Char\";\n" +
                        "\tmargin-top:0cm;\n" +
                        "\tmargin-right:0cm;\n" +
                        "\tmargin-bottom:6.0pt;\n" +
                        "\tmargin-left:0cm;\n" +
                        "\tline-height:118%;\n" +
                        "\tmso-pagination:none;\n" +
                        "\ttab-stops:center 225.65pt right 451.3pt;\n" +
                        "\tmso-layout-grid-align:none;\n" +
                        "\tpunctuation-wrap:simple;\n" +
                        "\ttext-autospace:none;\n" +
                        "\tfont-size:10.0pt;\n" +
                        "\tfont-family:\"Calibri\",sans-serif;\n" +
                        "\tmso-fareast-font-family:\"Times New Roman\";\n" +
                        "\tmso-fareast-theme-font:minor-fareast;\n" +
                        "\tmso-bidi-font-family:Calibri;\n" +
                        "\tcolor:black;\n" +
                        "\tmso-font-kerning:14.0pt;}\n" +
                        "p.MsoFooter, li.MsoFooter, div.MsoFooter\n" +
                        "\t{mso-style-priority:99;\n" +
                        "\tmso-style-link:\"Footer Char\";\n" +
                        "\tmargin-top:0cm;\n" +
                        "\tmargin-right:0cm;\n" +
                        "\tmargin-bottom:6.0pt;\n" +
                        "\tmargin-left:0cm;\n" +
                        "\tline-height:118%;\n" +
                        "\tmso-pagination:none;\n" +
                        "\ttab-stops:center 225.65pt right 451.3pt;\n" +
                        "\tmso-layout-grid-align:none;\n" +
                        "\tpunctuation-wrap:simple;\n" +
                        "\ttext-autospace:none;\n" +
                        "\tfont-size:10.0pt;\n" +
                        "\tfont-family:\"Calibri\",sans-serif;\n" +
                        "\tmso-fareast-font-family:\"Times New Roman\";\n" +
                        "\tmso-fareast-theme-font:minor-fareast;\n" +
                        "\tmso-bidi-font-family:Calibri;\n" +
                        "\tcolor:black;\n" +
                        "\tmso-font-kerning:14.0pt;}\n" +
                        "a:link, span.MsoHyperlink\n" +
                        "\t{mso-style-priority:99;\n" +
                        "\tcolor:blue;\n" +
                        "\tmso-themecolor:hyperlink;\n" +
                        "\ttext-decoration:underline;\n" +
                        "\ttext-underline:single;}\n" +
                        "a:visited, span.MsoHyperlinkFollowed\n" +
                        "\t{mso-style-noshow:yes;\n" +
                        "\tmso-style-priority:99;\n" +
                        "\tcolor:purple;\n" +
                        "\tmso-themecolor:followedhyperlink;\n" +
                        "\ttext-decoration:underline;\n" +
                        "\ttext-underline:single;}\n" +
                        "p.MsoAcetate, li.MsoAcetate, div.MsoAcetate\n" +
                        "\t{mso-style-noshow:yes;\n" +
                        "\tmso-style-priority:99;\n" +
                        "\tmso-style-link:\"Balloon Text Char\";\n" +
                        "\tmargin:0cm;\n" +
                        "\tmargin-bottom:.0001pt;\n" +
                        "\tmso-pagination:none;\n" +
                        "\tmso-layout-grid-align:none;\n" +
                        "\tpunctuation-wrap:simple;\n" +
                        "\ttext-autospace:none;\n" +
                        "\tfont-size:8.0pt;\n" +
                        "\tfont-family:\"Tahoma\",sans-serif;\n" +
                        "\tmso-fareast-font-family:\"Times New Roman\";\n" +
                        "\tmso-fareast-theme-font:minor-fareast;\n" +
                        "\tcolor:black;\n" +
                        "\tmso-font-kerning:14.0pt;}\n" +
                        "p.MsoListParagraph, li.MsoListParagraph, div.MsoListParagraph\n" +
                        "\t{mso-style-priority:34;\n" +
                        "\tmso-style-unhide:no;\n" +
                        "\tmso-style-qformat:yes;\n" +
                        "\tmargin-top:0cm;\n" +
                        "\tmargin-right:0cm;\n" +
                        "\tmargin-bottom:0cm;\n" +
                        "\tmargin-left:36.0pt;\n" +
                        "\tmargin-bottom:.0001pt;\n" +
                        "\tmso-pagination:widow-orphan;\n" +
                        "\tfont-size:10.0pt;\n" +
                        "\tfont-family:\"Times New Roman\",serif;\n" +
                        "\tmso-fareast-font-family:\"Times New Roman\";}\n" +
                        "span.HeaderChar\n" +
                        "\t{mso-style-name:\"Header Char\";\n" +
                        "\tmso-style-priority:99;\n" +
                        "\tmso-style-unhide:no;\n" +
                        "\tmso-style-locked:yes;\n" +
                        "\tmso-style-link:Header;\n" +
                        "\tmso-ansi-font-size:10.0pt;\n" +
                        "\tmso-bidi-font-size:10.0pt;\n" +
                        "\tfont-family:\"Calibri\",sans-serif;\n" +
                        "\tmso-ascii-font-family:Calibri;\n" +
                        "\tmso-hansi-font-family:Calibri;\n" +
                        "\tmso-bidi-font-family:Calibri;\n" +
                        "\tcolor:black;\n" +
                        "\tmso-font-kerning:14.0pt;}\n" +
                        "span.FooterChar\n" +
                        "\t{mso-style-name:\"Footer Char\";\n" +
                        "\tmso-style-priority:99;\n" +
                        "\tmso-style-unhide:no;\n" +
                        "\tmso-style-locked:yes;\n" +
                        "\tmso-style-link:Footer;\n" +
                        "\tmso-ansi-font-size:10.0pt;\n" +
                        "\tmso-bidi-font-size:10.0pt;\n" +
                        "\tfont-family:\"Calibri\",sans-serif;\n" +
                        "\tmso-ascii-font-family:Calibri;\n" +
                        "\tmso-hansi-font-family:Calibri;\n" +
                        "\tmso-bidi-font-family:Calibri;\n" +
                        "\tcolor:black;\n" +
                        "\tmso-font-kerning:14.0pt;}\n" +
                        "span.BalloonTextChar\n" +
                        "\t{mso-style-name:\"Balloon Text Char\";\n" +
                        "\tmso-style-noshow:yes;\n" +
                        "\tmso-style-priority:99;\n" +
                        "\tmso-style-unhide:no;\n" +
                        "\tmso-style-locked:yes;\n" +
                        "\tmso-style-link:\"Balloon Text\";\n" +
                        "\tmso-ansi-font-size:8.0pt;\n" +
                        "\tmso-bidi-font-size:8.0pt;\n" +
                        "\tfont-family:\"Tahoma\",sans-serif;\n" +
                        "\tmso-ascii-font-family:Tahoma;\n" +
                        "\tmso-hansi-font-family:Tahoma;\n" +
                        "\tmso-bidi-font-family:Tahoma;\n" +
                        "\tcolor:black;\n" +
                        "\tmso-font-kerning:14.0pt;}\n" +
                        "p.Default, li.Default, div.Default\n" +
                        "\t{mso-style-name:Default;\n" +
                        "\tmso-style-unhide:no;\n" +
                        "\tmso-style-parent:\"\";\n" +
                        "\tmargin:0cm;\n" +
                        "\tmargin-bottom:.0001pt;\n" +
                        "\tmso-pagination:widow-orphan;\n" +
                        "\tmso-layout-grid-align:none;\n" +
                        "\ttext-autospace:none;\n" +
                        "\tfont-size:12.0pt;\n" +
                        "\tfont-family:\"Arial\",sans-serif;\n" +
                        "\tmso-fareast-font-family:Calibri;\n" +
                        "\tcolor:black;\n" +
                        "\tmso-fareast-language:EN-US;}\n" +
                        "span.SpellE\n" +
                        "\t{mso-style-name:\"\";\n" +
                        "\tmso-spl-e:yes;}\n" +
                        ".MsoChpDefault\n" +
                        "\t{mso-style-type:export-only;\n" +
                        "\tmso-default-props:yes;\n" +
                        "\tfont-family:\"Calibri\",sans-serif;\n" +
                        "\tmso-ascii-font-family:Calibri;\n" +
                        "\tmso-ascii-theme-font:minor-latin;\n" +
                        "\tmso-fareast-font-family:\"Times New Roman\";\n" +
                        "\tmso-fareast-theme-font:minor-fareast;\n" +
                        "\tmso-hansi-font-family:Calibri;\n" +
                        "\tmso-hansi-theme-font:minor-latin;\n" +
                        "\tmso-bidi-font-family:\"Times New Roman\";\n" +
                        "\tmso-bidi-theme-font:minor-bidi;}\n" +
                        " /* Page Definitions */\n" +
                        " @page\n" +
                        "\t{mso-page-border-surround-header:no;\n" +
                        "\tmso-page-border-surround-footer:no;\n" +
                        "\tmso-footnote-separator:url(\"letter_files/header.htm\") fs;\n" +
                        "\tmso-footnote-continuation-separator:url(\"letter_files/header.htm\") fcs;\n" +
                        "\tmso-endnote-separator:url(\"letter_files/header.htm\") es;\n" +
                        "\tmso-endnote-continuation-separator:url(\"letter_files/header.htm\") ecs;\n" +
                        "\tmso-endnote-position:end-of-section;}\n" +
                        "@page WordSection1\n" +
                        "\t{size:21.0cm 841.95pt;\n" +
                        "\tmargin:127.6pt 51.05pt 22.7pt 51.05pt;\n" +
                        "\tmso-header-margin:2.85pt;\n" +
                        "\tmso-footer-margin:2.85pt;\n" +
                        "\tmso-header:url(\"letter_files/header.htm\") h1;\n" +
                        "\tmso-footer:url(\"letter_files/header.htm\") f1;\n" +
                        "\tmso-paper-source:0;}\n" +
                        "div.WordSection1\n" +
                        "\t{page:WordSection1;}\n" +
                        " /* List Definitions */\n" +
                        " @list l0\n" +
                        "\t{mso-list-id:1484540723;\n" +
                        "\tmso-list-type:hybrid;\n" +
                        "\tmso-list-template-ids:-1152115670 -416613392 134807577 134807579 134807567 134807577 134807579 134807567 134807577 134807579;}\n" +
                        "@list l0:level1\n" +
                        "\t{mso-level-tab-stop:none;\n" +
                        "\tmso-level-number-position:left;\n" +
                        "\ttext-indent:-18.0pt;\n" +
                        "\tmso-ansi-font-weight:bold;}\n" +
                        "@list l0:level2\n" +
                        "\t{mso-level-number-format:alpha-lower;\n" +
                        "\tmso-level-tab-stop:none;\n" +
                        "\tmso-level-number-position:left;\n" +
                        "\ttext-indent:-18.0pt;}\n" +
                        "@list l0:level3\n" +
                        "\t{mso-level-number-format:roman-lower;\n" +
                        "\tmso-level-tab-stop:none;\n" +
                        "\tmso-level-number-position:right;\n" +
                        "\ttext-indent:-9.0pt;}\n" +
                        "@list l0:level4\n" +
                        "\t{mso-level-tab-stop:none;\n" +
                        "\tmso-level-number-position:left;\n" +
                        "\ttext-indent:-18.0pt;}\n" +
                        "@list l0:level5\n" +
                        "\t{mso-level-number-format:alpha-lower;\n" +
                        "\tmso-level-tab-stop:none;\n" +
                        "\tmso-level-number-position:left;\n" +
                        "\ttext-indent:-18.0pt;}\n" +
                        "@list l0:level6\n" +
                        "\t{mso-level-number-format:roman-lower;\n" +
                        "\tmso-level-tab-stop:none;\n" +
                        "\tmso-level-number-position:right;\n" +
                        "\ttext-indent:-9.0pt;}\n" +
                        "@list l0:level7\n" +
                        "\t{mso-level-tab-stop:none;\n" +
                        "\tmso-level-number-position:left;\n" +
                        "\ttext-indent:-18.0pt;}\n" +
                        "@list l0:level8\n" +
                        "\t{mso-level-number-format:alpha-lower;\n" +
                        "\tmso-level-tab-stop:none;\n" +
                        "\tmso-level-number-position:left;\n" +
                        "\ttext-indent:-18.0pt;}\n" +
                        "@list l0:level9\n" +
                        "\t{mso-level-number-format:roman-lower;\n" +
                        "\tmso-level-tab-stop:none;\n" +
                        "\tmso-level-number-position:right;\n" +
                        "\ttext-indent:-9.0pt;}\n" +
                        "ol\n" +
                        "\t{margin-bottom:0cm;}\n" +
                        "ul\n" +
                        "\t{margin-bottom:0cm;}\n" +
                        "-->\n" +
                        "</style>\n" +
                        "<!--[if gte mso 10]>\n" +
                        "<style>\n" +
                        " /* Style Definitions */\n" +
                        " table.MsoNormalTable\n" +
                        "\t{mso-style-name:\"Table Normal\";\n" +
                        "\tmso-tstyle-rowband-size:0;\n" +
                        "\tmso-tstyle-colband-size:0;\n" +
                        "\tmso-style-noshow:yes;\n" +
                        "\tmso-style-priority:99;\n" +
                        "\tmso-style-parent:\"\";\n" +
                        "\tmso-padding-alt:0cm 5.4pt 0cm 5.4pt;\n" +
                        "\tmso-para-margin:0cm;\n" +
                        "\tmso-para-margin-bottom:.0001pt;\n" +
                        "\tmso-pagination:widow-orphan;\n" +
                        "\tfont-size:11.0pt;\n" +
                        "\tfont-family:\"Calibri\",sans-serif;\n" +
                        "\tmso-ascii-font-family:Calibri;\n" +
                        "\tmso-ascii-theme-font:minor-latin;\n" +
                        "\tmso-hansi-font-family:Calibri;\n" +
                        "\tmso-hansi-theme-font:minor-latin;}\n" +
                        "</style>\n" +
                        "<![endif]--><!--[if gte mso 9]><xml>\n" +
                        " <o:shapedefaults v:ext=\"edit\" spidmax=\"2053\"/>\n" +
                        "</xml><![endif]--><!--[if gte mso 9]><xml>\n" +
                        " <o:shapelayout v:ext=\"edit\">\n" +
                        "  <o:idmap v:ext=\"edit\" data=\"1\"/>\n" +
                        " </o:shapelayout></xml><![endif]-->\n" +
                        "</head>\n" +
                        "\n" +
                        "<body lang=EN-GB link=blue vlink=purple style='tab-interval:36.0pt;text-justify-trim:\n" +
                        "punctuation'>\n" +
                        "\n" +
                        "<div class=WordSection1>\n" +
                        "\n" +
                        "<p class=MsoNormal style='text-align:justify;text-justify:inter-ideograph'><span\n" +
                        "style='font-family:\"Arial\",sans-serif'>05 October 2015<o:p></o:p></span></p>\n" +
                        "\n" +
                        "<p class=MsoNormal style='text-align:justify;text-justify:inter-ideograph'><span\n" +
                        "style='font-family:\"Arial\",sans-serif'>Dear Parent/Guardian,<o:p></o:p></span></p>\n" +
                        "\n" +
                        "<p class=MsoNormal style='margin-bottom:0cm;margin-bottom:.0001pt;text-align:\n" +
                        "justify;text-justify:inter-ideograph;line-height:normal'><b style='mso-bidi-font-weight:\n" +
                        "normal'><span style='font-family:\"Arial\",sans-serif'>Trip:<span\n" +
                        "style='mso-tab-count:1'>     </span>CCF2 – Exercise TIRO<o:p></o:p></span></b></p>\n" +
                        "\n" +
                        "<p class=MsoNormal style='text-align:justify;text-justify:inter-ideograph'><b\n" +
                        "style='mso-bidi-font-weight:normal'><span style='font-family:\"Arial\",sans-serif'>Date:<span\n" +
                        "style='mso-tab-count:1'>    </span>Friday 23 October 2015- Sunday 25 October\n" +
                        "2015<o:p></o:p></span></b></p>\n" +
                        "\n" +
                        "<p class=MsoNormal style='text-align:justify;text-justify:inter-ideograph'><span\n" +
                        "style='font-family:\"Arial\",sans-serif'>Reading School CCF will be holding an\n" +
                        "Overnight Field Training Exercise, codenamed Ex TIRO for all CCF recruits and a\n" +
                        "group of Senior cadets running from Friday 23 October 2015 to Sunday 25 October\n" +
                        "2015 on Bramley Training Area B. During the course of that weekend, recruits\n" +
                        "and senior NCOs under staff supervision will run a series of instructional lessons\n" +
                        "and demonstrations to teach basic military skills for living and operating in\n" +
                        "the field. Senior cadets / NCOs will be in the field from Friday evening to\n" +
                        "Sunday morning. Recruits will be out only from Saturday to Sunday.<o:p></o:p></span></p>\n" +
                        "\n" +
                        "<p class=MsoNormal style='text-align:justify;text-justify:inter-ideograph'><span\n" +
                        "style='font-family:\"Arial\",sans-serif'>Senior cadets will parade at Reading\n" +
                        "School on Friday 23 October after School, and will travel out to the training\n" +
                        "area on school minibuses leaving School at 1600.<o:p></o:p></span></p>\n" +
                        "\n" +
                        "<p class=MsoNormal style='text-align:justify;text-justify:inter-ideograph'><span\n" +
                        "style='font-family:\"Arial\",sans-serif'>Recruits should arrive at Reading School\n" +
                        "by 0900 on Saturday 24 October, and report directly to the parade area by the\n" +
                        "Chemistry Department. Travel to and from the training area will be by hired\n" +
                        "coach and school minibuses. Return to school will be by 1300 Sunday.<o:p></o:p></span></p>\n" +
                        "\n" +
                        "<p class=MsoNormal style='text-align:justify;text-justify:inter-ideograph'><span\n" +
                        "style='font-family:\"Arial\",sans-serif'>Uniform is combat dress (C95 (RAF) or\n" +
                        "MTP (Army) trousers, shirt, smock, boots, beret). Warm clothing under combats is\n" +
                        "also advised, as it may be cold and wet. Army waterproofs will also be provided.\n" +
                        "A small bag/backpack will also be useful although we will also issue Army\n" +
                        "pattern webbing and Daysacks, but stocks are limited. Cadets can change at\n" +
                        "Reading School into CCF uniform and leave remaining clothes in a bag in the CCF\n" +
                        "office. Cadets may travel to and from school in CCF uniform but covering with\n" +
                        "civilian clothing is advised, though not essential. Boys can bring mobiles but\n" +
                        "should <u>only</u> use them in an emergency. We do not as a matter of policy accept\n" +
                        "any responsibility for any damages to personal items including mobile phones.<o:p></o:p></span></p>\n" +
                        "\n" +
                        "<p class=MsoNormal style='text-align:justify;text-justify:inter-ideograph'><span\n" +
                        "style='font-family:\"Arial\",sans-serif'>Food will be in the form of Army\n" +
                        "Rations. These come in the form of General / Normal and Vegetarian. Please\n" +
                        "indicate on the form which will be suitable for your Child. Rations also incur\n" +
                        "a charge of £2 per pack, which must be paid to 2Lt Morris in the CCF office\n" +
                        "prior to the exercise. If your child has any further dietary or medical requirements\n" +
                        "then please make this known to the SSI, 2Lt David Morris as soon as possible\n" +
                        "before the exercise starts. Water and hot drinks will be provided during the\n" +
                        "exercise.<o:p></o:p></span></p>\n" +
                        "\n" +
                        "<p class=MsoNormal style='text-align:justify;text-justify:inter-ideograph'><b\n" +
                        "style='mso-bidi-font-weight:normal'><span style='font-family:\"Arial\",sans-serif'>Please\n" +
                        "completing the consent slip attached and return to 2Lt Morris in the CCF office\n" +
                        "by 1700 Monday 19 Oct.<o:p></o:p></span></b></p>\n" +
                        "\n" +
                        "<p class=MsoNormal style='text-align:justify;text-justify:inter-ideograph'><span\n" +
                        "style='font-family:\"Arial\",sans-serif'>If you have any queries please do not\n" +
                        "hesitate to contact me at ccf@reading-school.co.uk.<o:p></o:p></span></p>\n" +
                        "\n" +
                        "<p class=MsoNormal><span style='font-family:\"Arial\",sans-serif'><o:p>&nbsp;</o:p></span></p>\n" +
                        "\n" +
                        "<p class=MsoNormal><span style='font-family:\"Arial\",sans-serif'><o:p>&nbsp;</o:p></span></p>\n" +
                        "\n" +
                        "<p class=MsoNormal align=right style='text-align:right'><span style='font-family:\n" +
                        "\"Arial\",sans-serif'>S P Donegan<o:p></o:p></span></p>\n" +
                        "\n" +
                        "<p class=MsoNormal align=right style='text-align:right'><span style='font-family:\n" +
                        "\"Arial\",sans-serif'>Major<o:p></o:p></span></p>\n" +
                        "\n" +
                        "<p class=MsoNormal align=right style='text-align:right'><span style='font-family:\n" +
                        "\"Arial\",sans-serif'>OC Reading School CCF<o:p></o:p></span></p>\n" +
                        "\n" +
                        "<p class=MsoNormal style='margin-bottom:0cm;margin-bottom:.0001pt;line-height:\n" +
                        "normal;mso-pagination:widow-orphan;mso-layout-grid-align:auto;punctuation-wrap:\n" +
                        "hanging;text-autospace:ideograph-numeric ideograph-other'><span\n" +
                        "style='font-size:11.0pt;font-family:\"Arial\",sans-serif;mso-fareast-font-family:\n" +
                        "Calibri;mso-fareast-language:EN-US'><o:p>&nbsp;</o:p></span></p>\n" +
                        "\n" +
                        "</div>\n" +
                        "\n" +
                        "</body>\n" +
                        "\n" +
                        "</html>\n";
                fis.close();
                myApiService.resetFile().execute().getData();

                String[] ab = splitStringEvery(html, 5000);
                int length = ab.length;
                ArrayList<String> a = new ArrayList<>(Arrays.asList(ab));

                for(String ah: a){
                    myApiService.addToFile(ah).execute().getData();
                    length--;
                    Log.v("done",Integer.toString(length));
                    if(length < 1){
                        Log.v("done","done");
                    }
                }

                return myApiService.upload(json).execute().getData();
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        public String[] splitStringEvery(String s, int interval) {
            int arrayLength = (int) Math.ceil(((s.length() / (double)interval)));
            String[] result = new String[arrayLength];

            int j = 0;
            int lastIndex = result.length - 1;
            for (int i = 0; i < lastIndex; i++) {
                result[i] = s.substring(j, j + interval);
                j += interval;
            } //Add the last bit
            result[lastIndex] = s.substring(j);

            return result;
        }

        public byte[] readFully(InputStream stream) throws IOException {
            byte[] buffer = new byte[8192];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int bytesRead;
            while ((bytesRead = stream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        }

        @Override
        protected void onPostExecute(String result) {
            Log.v("ayyy", result);
            Toast t = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT);
            t.show();
        }
    }

/*    "{\n" +
            "  \"date\":\"14/4/2016\",\n" +
            "  \"entries\":[\n" +
            "    [\"RAF Recruits\",\"Blues\",\"Sgt T Hard\",\"MRAF Cox\",\"Drill\"],\n" +
            "    [\"Army Recruits\",\"MTP\",\"CSM D Sack\",\"5Lt Dorris\",\"Drill\"],\n" +
            "    [\"RAF Advanced\",\"Blues\",\"LCpl No '1' Cares\",\"MRAF Cox\",\"Ultilearn Stuff. In e4\"],\n" +
            "    [\"Army Advanced\",\"MTP\",\"FSgt L Brioche\",\"5Lt Dorris\",\"Wait for LSW's\"],\n" +
            "    [\"REME\",\"Blues/MTP\",\"SSgt N V Keen\",\"5Lt Dorris\",\"Presentations with Cpl A Awesome\"],\n" +
            "    [\"Signals\",\"Blues/MTP\",\"Cpl V Keen\",\"MRAF Cox\",\"Do something useless as usual\"]\n" +
            "    ]\n" +
            "}"*/

}
