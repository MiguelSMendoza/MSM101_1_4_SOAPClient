package es.netrunners;

import java.util.ArrayList;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SOAPClientActivity extends ListActivity {

	String NAMESPACE = "http://localhost/";
	String URL = "http://10.0.2.2:21647/ClientsService.asmx";
	String NEW_METHOD_NAME = "NewClient";
	String LIST_METHOD_NAME = "getClients";
	String SOAP_NEW_ACTION = "http://localhost/NewClient";
	String SOAP_LIST_ACTION = "http://localhost/getClients";

	String[] from = new String[] { "Name", "Surname", "Age" };
	int[] to = new int[] { R.id.name, R.id.surname, R.id.age };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		fillList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addClient:
			showClientDialog();
			return true;
		}
		return false;
	}

	private void showClientDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(R.layout.dialog, null);
		builder.setView(textEntryView);
		builder.setTitle("New Client")
				.setCancelable(false)
				.setPositiveButton("Add",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								EditText name = (EditText) textEntryView
										.findViewById(R.id.newname);
								EditText surname = (EditText) textEntryView
										.findViewById(R.id.newsurname);
								EditText age = (EditText) textEntryView
										.findViewById(R.id.newage);
								addClient(name.getText().toString(), surname
										.getText().toString(), age.getText()
										.toString());

							}

						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();

	}

	private void addClient(String name, String surname, String age) {
		SoapObject request = new SoapObject(NAMESPACE, NEW_METHOD_NAME);

		request.addProperty("Name", name);
		request.addProperty("Surname", surname);
		request.addProperty("Age", age);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);

		HttpTransportSE transporte = new HttpTransportSE(URL);

		try {
			transporte.call(SOAP_NEW_ACTION, envelope);

			SoapPrimitive resultado_xml = (SoapPrimitive) envelope
					.getResponse();
			String res = resultado_xml.toString();

			if (res.equals("1"))
				Toast.makeText(getApplicationContext(),
						name + " " + surname + " Added Succesfully !!",
						Toast.LENGTH_LONG).show();
			fillList();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.getMessage(),
					Toast.LENGTH_LONG).show();
		}

	}

	private void fillList() {
		SoapObject request = new SoapObject(NAMESPACE, LIST_METHOD_NAME);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);

		HttpTransportSE transporte = new HttpTransportSE(URL);

		try {
			transporte.call(SOAP_LIST_ACTION, envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();

			Client[] listClients = new Client[resSoap.getPropertyCount()];

			for (int i = 0; i < listClients.length; i++) {
				SoapObject ic = (SoapObject) resSoap.getProperty(i);

				Client cli = new Client();
				cli.setID(Integer.parseInt(ic.getProperty(0).toString()));
				cli.setName(ic.getProperty(1).toString());
				cli.setSurname(ic.getProperty(2).toString());
				cli.setAge(Integer.parseInt(ic.getProperty(3).toString()));

				listClients[i] = cli;
			}
			ArrayList<HashMap<String, String>> Clients = new ArrayList<HashMap<String, String>>();
			for (Client client : listClients) {

				HashMap<String, String> clientData = new HashMap<String, String>();

				clientData.put(from[0], client.getName());
				clientData.put(from[1], client.getSurname());
				clientData.put(from[2], String.valueOf(client.getAge()));

				Clients.add(clientData);
			}

			SimpleAdapter ListAdapter = new SimpleAdapter(this, Clients,
					R.layout.row, from, to);
			setListAdapter(ListAdapter);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}
}