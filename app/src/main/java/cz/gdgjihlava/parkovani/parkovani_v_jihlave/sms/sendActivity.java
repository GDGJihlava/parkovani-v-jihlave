package cz.gdgjihlava.parkovani.parkovani_v_jihlave.sms;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.Manifest;

import cz.gdgjihlava.parkovani.parkovani_v_jihlave.R;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.SMS;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.notifications.OngoingNotification;


public class sendActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0 ;
    private EditText zoneInput;
    private EditText idInput;
    private Button sendButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        zoneInput = (EditText)findViewById(R.id.zone_input);
        idInput = (EditText)findViewById(R.id.id_input);
        sendButton = (Button)findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(sendActivity.this,
                        Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(sendActivity.this,
                            Manifest.permission.SEND_SMS)) {
                        ActivityCompat.requestPermissions(sendActivity.this,
                                new String[]{Manifest.permission.SEND_SMS},
                                MY_PERMISSIONS_REQUEST_SEND_SMS);
                    }
                }
                else {
                    //sendSMS();
                    Toast.makeText(getApplicationContext(),
                            R.string.sms_sent, Toast.LENGTH_LONG).show();
                    OngoingNotification ongoingNotification = new OngoingNotification(getApplicationContext());
                    ongoingNotification.showCurrentTicket();
                }
            }
        });

    }

    private void sendSMS() {
        SMS sms = new SMS(sendActivity.this, zoneInput.getText().toString(), idInput.getText().toString());
        sms.send();
        OngoingNotification ongoingNotification = new OngoingNotification(getApplicationContext());
        ongoingNotification.showCurrentTicket();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMS();
                    Toast.makeText(sendActivity.this, R.string.sms_sent, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            R.string.sms_failed, Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}

