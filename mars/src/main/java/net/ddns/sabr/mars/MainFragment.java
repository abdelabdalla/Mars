package net.ddns.sabr.mars;

/*
*Copyright 2016 Abdel-Rahim Abdalla
*/

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import net.ddns.sabr.session.Session;

import java.util.ArrayList;
import java.util.Arrays;

public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView listView = (RecyclerView) root.findViewById(R.id.listView);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);

        Gson g = new Gson();

        Session s = g.fromJson(getArguments().getString("json"), Session.class);

        ArrayList<String[]> aList = new ArrayList<>(Arrays.asList(s.entries));
        TimetableAdapter timetableAdapter = new TimetableAdapter(aList);
        listView.setAdapter(timetableAdapter);

        return root;
    }

}
