package net.ddns.sabr.mars;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

public class StoresFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stores, container, false);

        final Spinner spinner = (Spinner) root.findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.groups, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        assert spinner != null;
        spinner.setAdapter(adapter);

        final EditText emailText = (EditText) root.findViewById(R.id.emailText);
        final EditText passwordText = (EditText) root.findViewById(R.id.passwordText);
        EditText nameText = (EditText) root.findViewById(R.id.nameText);
        EditText uniformText = (EditText) root.findViewById(R.id.uniformText);
        EditText qtyText = (EditText) root.findViewById(R.id.qtyText);
        EditText reasonText = (EditText) root.findViewById(R.id.reasonText);

        Button orderButton = (Button) root.findViewById(R.id.orderButton);

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //BackgroundMail.newBuilder(getContext()).withUsername(emailText.getText().toString()).withPassword(passwordText.getText().toString()).withMailto(spinner.getSelectedItemPosition() == 0 ? "abdelabdalla@gmail.com" : "k00729@reading-school.co.uk").withSubject()

                BackgroundMail.newBuilder(getContext())
                        .withUsername(emailText.getText().toString())
                        .withPassword(passwordText.getText().toString())
                        .withMailto(spinner.getSelectedItemPosition() == 0 ? "abdelabdalla@gmail.com" : "k00729@reading-school.co.uk")
                        .withSubject("this is the subject")
                        .withBody("this is the body")
                        .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                            @Override
                            public void onFail() {
                                Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .send();
            }
        });

        return root;
    }
}