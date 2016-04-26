package net.ddns.sabr.mars;

/*
*Copyright 2016 Abdel-Rahim Abdalla
*/

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

public class NewsFragment extends Fragment {

    ImageView pdfView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);

/*        try {
            FileUtils.writeByteArrayToFile(new File(Environment.getExternalStorageDirectory()+"/news.pdf"),getArguments().getByteArray("file"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        PDFView pdfView = (PDFView) root.findViewById(R.id.pdfview);

        pdfView.fromFile(new File(Environment.getExternalStorageDirectory().toString()+"/news.pdf"))
                .defaultPage(1)
                .enableSwipe(true)
                .swipeVertical(true)
                .load();*/

        WebView webView = (WebView) root.findViewById(R.id.pdfview);
        //webView.getSettings().setJavaScriptEnabled(true);
        String html = getArguments().getString("news");
        webView.loadData(html,"text/html","UTF-8");

        return root;
    }
}