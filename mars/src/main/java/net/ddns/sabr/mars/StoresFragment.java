package net.ddns.sabr.mars;

import android.os.AsyncTask;
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

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class StoresFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stores, container, false);

        final Spinner spinner = (Spinner) root.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.groups, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assert spinner != null;
        spinner.setAdapter(adapter);

        final Spinner rankSpinner = (Spinner) root.findViewById(R.id.rankSpinner);
        ArrayAdapter<CharSequence> rankAdapter = ArrayAdapter.createFromResource(getContext(),R.array.ranks, android.R.layout.simple_spinner_item);
        rankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assert rankSpinner != null;
        rankSpinner.setAdapter(rankAdapter);

        final EditText emailText = (EditText) root.findViewById(R.id.emailText);
        final EditText passwordText = (EditText) root.findViewById(R.id.passwordText);
        final EditText nameText = (EditText) root.findViewById(R.id.nameText);
        final EditText uniformText = (EditText) root.findViewById(R.id.uniformText);
        final EditText qtyText = (EditText) root.findViewById(R.id.qtyText);
        final EditText sizeText = (EditText) root.findViewById(R.id.sizenText);
        final EditText reasonText = (EditText) root.findViewById(R.id.reasonText);

        Button orderButton = (Button) root.findViewById(R.id.orderButton);

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailText.getText().toString();
                String[] emailParts = email.split("@");
                email = emailParts[0] + "@reading-school.co.uk";

                String order = "Name: " + nameText.getText().toString() + "\nItem: " + uniformText.getText().toString() + " x " + qtyText.getText().toString() + "\nSize: " + sizeText.getText().toString() + "\nReason for Order: " + reasonText.getText().toString();

                String[] a = {email,passwordText.getText().toString(),Integer.toString(spinner.getSelectedItemPosition()),order};
                new EmailAsyncTask().execute(a);
            }
        });

        return root;
    }

    private class EmailAsyncTask extends AsyncTask<String[],Void,String>{

        @Override
        protected String doInBackground(String[]... params) {

            final String[] a = params[0];

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.office365.com");
            props.put("mail.smtp.port", "587");
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(a[0], a[1]);
                        }
                    });
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(a[0]));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(a[2].equals("0") ? "k00682@reading-school.co.uk" : "k00675@reading-school.co.uk"));
                message.setSubject("Stores Order");
                // Create the message part
                BodyPart messageBodyPart = new MimeBodyPart();

                // Fill the message

                messageBodyPart.setText(a[3]);

                // Create a multipart message
                Multipart multipart = new MimeMultipart();

                // Set text message part
                multipart.addBodyPart(messageBodyPart);

                // Send the complete message parts
                message.setContent(multipart);
                Transport.send(message);
            } catch (MessagingException e) {
                return e.getMessage();
            }

            return "done";
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getContext(),result,Toast.LENGTH_SHORT).show();
        }

    }
}