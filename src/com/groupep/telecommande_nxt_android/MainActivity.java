package com.groupep.telecommande_nxt_android;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class MainActivity extends Activity
{
	private Button boutonConnecter, boutonDeconnecter, boutonSortieMilieu, boutonSortieDernier;
	private RadioButton radioAvancer, radioReculer;
	private SeekBar vitesseAvancer, vitesseReculer;
	private BluetoothAdapter connexionRobot;
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.boutonConnecter = (Button) findViewById(R.id.boutonConnecter);
		this.boutonDeconnecter = (Button) findViewById(R.id.boutonDeconnecter);
		this.boutonSortieMilieu = (Button) findViewById(R.id.boutonSortieMilieu);
		this.boutonSortieDernier = (Button) findViewById(R.id.boutonSortieDernier);
		this.radioAvancer = (RadioButton) findViewById(R.id.radioAvancer);
		this.radioReculer = (RadioButton) findViewById(R.id.radioReculer);
		this.vitesseAvancer = (SeekBar) findViewById(R.id.vitesseAvancer);
		this.vitesseReculer = (SeekBar) findViewById(R.id.vitesseReculer);
		
		this.setEnabled(false);
		
		
		this.boutonConnecter.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO : connexion bluetooth
				/*connexionRobot = BluetoothAdapter.getDefaultAdapter();
				if(connexionRobot == null)
					Toast.makeText(MainActivity.this, "Pas de bluetooth", Toast.LENGTH_SHORT).show();
				else
				{
					if (!connexionRobot.isEnabled())
						Toast.makeText(MainActivity.this, "Vous devez activer le bluetooth pour continuer", Toast.LENGTH_SHORT).show();

					else
					{
						private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver()
						{
							public void onReceive(Context context, Intent intent)
							{
								String action = intent.getAction();
								if(BluetoothDevice.ACTION_FOUND.equals(action))
								{
								    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
								    Toast.makeText(TutoBluetoothActivity.this, "New Device = " + device.getName(), Toast.LENGTH_SHORT).show();
								}
							}
						};
					}
				}*/
				
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
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2)
			{
				// TODO : Envoi de la vitesse
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
}
