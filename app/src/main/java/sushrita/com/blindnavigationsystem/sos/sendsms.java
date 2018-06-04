package sushrita.com.blindnavigationsystem.sos;

import java.security.Permission;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import sushrita.com.blindnavigationsystem.R;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class sendsms extends Activity {
	Button button;

	private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
	private Button dial;
	private String number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main3);
		dial = (Button) findViewById(R.id.dial);
		final DatabaseHandler db = new DatabaseHandler(sendsms.this);
		List<Contact> entries=db.getAllContacts();
		if(entries.get(0)!=null) number=entries.get(0).getPhoneNumber();
		else number = "100";
			dial.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
					if (checkPermission(Manifest.permission.CALL_PHONE)) {
						if(!number.isEmpty())
						startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number)));
						else startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "100")));

					} else {
						Toast.makeText(sendsms.this, "Permission Call Phone denied", Toast.LENGTH_SHORT).show();
					}

			}
		});

		if (checkPermission(Manifest.permission.CALL_PHONE)) {
			dial.setEnabled(true);
		} else {
			dial.setEnabled(false);
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);
		}
	}

	private boolean checkPermission(String permission) {
		return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch(requestCode) {
			case MAKE_CALL_PERMISSION_REQUEST_CODE :
				if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
					dial.setEnabled(true);
					Toast.makeText(this, "You can call the number by clicking on the button", Toast.LENGTH_SHORT).show();
				}
				return;
		}
	}
}
