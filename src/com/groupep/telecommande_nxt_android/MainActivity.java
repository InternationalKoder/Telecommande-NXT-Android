package com.groupep.telecommande_nxt_android;

import java.io.IOException;
import java.util.UUID;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
	private Button boutonConnecter, boutonDeconnecter, boutonSortieMilieu, boutonSortieDernier;
	private EditText champNomRobot;
	private RadioButton radioAvancer, radioReculer;
	private SeekBar vitesseAvancer, vitesseReculer;
	private BluetoothAdapter connexionBluetooth;
	private BluetoothSocket connexionRobot;
	private TextView texteNomRobotConnecte;
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(bluetoothReceiver, filter);
		
		this.boutonConnecter = (Button) findViewById(R.id.boutonConnecter);
		this.boutonDeconnecter = (Button) findViewById(R.id.boutonDeconnecter);
		this.boutonSortieMilieu = (Button) findViewById(R.id.boutonSortieMilieu);
		this.boutonSortieDernier = (Button) findViewById(R.id.boutonSortieDernier);
		this.champNomRobot = (EditText) findViewById(R.id.champNomRobot);
		this.radioAvancer = (RadioButton) findViewById(R.id.radioAvancer);
		this.radioReculer = (RadioButton) findViewById(R.id.radioReculer);
		this.vitesseAvancer = (SeekBar) findViewById(R.id.vitesseAvancer);
		this.vitesseReculer = (SeekBar) findViewById(R.id.vitesseReculer);
		this.texteNomRobotConnecte = (TextView) findViewById(R.id.texteNomRobotConnecte);
		
		this.setEnabled(false);
		
		
		this.boutonConnecter.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO : connexion bluetooth
				// Activation du bluetooth
				connexionBluetooth = BluetoothAdapter.getDefaultAdapter();
				if(!connexionBluetooth.isEnabled())
				{
					Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			        startActivityForResult(turnOn, 0);
			        Toast.makeText(getApplicationContext(),"Bluetooth activé", Toast.LENGTH_SHORT).show();
			    }
			    else
			        Toast.makeText(getApplicationContext(),"Bluetooth déjà activé", Toast.LENGTH_SHORT).show();
				
				// Recherche du robot dans les périphériques connus
				for(BluetoothDevice dev : connexionBluetooth.getBondedDevices())
				{
					if(dev.getName().equals(champNomRobot.getText().toString()))
					{
						try
						{
							connexionRobot = dev.createRfcommSocketToServiceRecord(MY_UUID);
						}
						catch (IOException e)
						{
							Toast.makeText(MainActivity.this, "Erreur de communication", Toast.LENGTH_SHORT).show();
						}
					}
				}
				
				// Recherche du robot si non trouvé dans les périphériques connus
				if(connexionRobot == null)
					connexionBluetooth.startDiscovery();
				
				if(connexionRobot != null)
				{
					String nom = connexionRobot.getRemoteDevice().getName();
					texteNomRobotConnecte.setText(nom);
					Toast.makeText(MainActivity.this, "Connecté à " + nom, Toast.LENGTH_SHORT).show();
					try
					{
						connexionRobot.getOutputStream().write(0);
					}
					catch (IOException e)
					{
						Toast.makeText(MainActivity.this, "Erreur de communication", Toast.LENGTH_SHORT).show();
					}
				}
				else
					Toast.makeText(MainActivity.this, "Robot non trouvé", Toast.LENGTH_SHORT).show();
					
				
				// affichage
				setEnabled(true);
				vitesseReculer.setEnabled(false);
				radioAvancer.setChecked(true);
			}
		});
		
		
		this.boutonDeconnecter.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO : déconnexion bluetooth
				if(connexionBluetooth != null)
				{
					try
					{
						if(connexionRobot != null)
							connexionRobot.close();
					}
					catch (IOException e)
					{
						Toast.makeText(MainActivity.this, "Erreur de communication", Toast.LENGTH_SHORT).show();
					}
					connexionBluetooth.disable();
					Toast.makeText(MainActivity.this, "Bluetooth désactivé", Toast.LENGTH_SHORT).show();
				}
				
				setEnabled(false);
			}
		});
		
		
		this.radioAvancer.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton bouton, boolean checked)
			{
				vitesseAvancer.setEnabled(checked);
				vitesseReculer.setEnabled(!checked);
			}
		});
		
		
		this.vitesseAvancer.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar arg0, int val, boolean arg2)
			{
				// TODO : Envoi de la vitesse
				try
				{
					if(connexionRobot != null)
						connexionRobot.getOutputStream().write(val);
				}
				catch(IOException e)
				{
					Toast.makeText(MainActivity.this, "Erreur de communication", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0)
			{
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0)
			{
			}
		});
		
		
		this.vitesseReculer.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar arg0, int val, boolean arg2)
			{
				// TODO : Envoi de la vitesse
				try
				{
					if(connexionRobot != null)
						connexionRobot.getOutputStream().write(val * -1);
				}
				catch(IOException e)
				{
					Toast.makeText(MainActivity.this, "Erreur de communication", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0)
			{
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0)
			{
			}
		});
		
		
		this.boutonSortieMilieu.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				try
				{
					if(connexionRobot != null)
						connexionRobot.getOutputStream().write(127);
				}
				catch (IOException e)
				{
					Toast.makeText(MainActivity.this, "Erreur de communication", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		
		this.boutonSortieDernier.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				try
				{
					if(connexionRobot != null)
						connexionRobot.getOutputStream().write(-127);
				}
				catch (IOException e)
				{
					Toast.makeText(MainActivity.this, "Erreur de communication", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	
	public void setEnabled(boolean enabled)
	{
		this.boutonConnecter.setEnabled(!enabled);
		this.boutonDeconnecter.setEnabled(enabled);
		this.boutonSortieMilieu.setEnabled(enabled);
		this.boutonSortieDernier.setEnabled(enabled);
		this.radioAvancer.setEnabled(enabled);
		this.radioReculer.setEnabled(enabled);
		this.vitesseAvancer.setEnabled(enabled);
		this.vitesseReculer.setEnabled(enabled);
	}
	
	
	private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver()
	{
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			if(BluetoothDevice.ACTION_FOUND.equals(action))
			{
				BluetoothDevice dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Toast.makeText(MainActivity.this, "dev : " + dev.getName(), Toast.LENGTH_SHORT).show();
			  
				if(dev.getName().equals(champNomRobot.getText().toString()))
				{
					try
					{
						connexionRobot = dev.createRfcommSocketToServiceRecord(MY_UUID);
					}
					catch (IOException e)
					{
						Toast.makeText(MainActivity.this, "Erreur de communication", Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
	};
	
	
	protected void onDestroy()
	{
		  super.onDestroy();
		  connexionBluetooth.cancelDiscovery();
		  unregisterReceiver(bluetoothReceiver);
	}
}
