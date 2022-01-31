package com.example.bee_honeyt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @file IHMConnexion.java
 * @brief Déclaration de la classe IHMConnexion
 * @details Permet de paramétrer la connexion au TTN
 * @author FOUCAULT Clémentine
 * $LastChangedRevision: 159 $
 * $LastChangedDate: 2021-06-09 18:09:01 +0200 (mer. 09 juin 2021) $
 */

/**
 * @class IHMConnexion
 * @brief Activité pour paramétrer la connexion au TTN
 */
public class IHMConnexion extends AppCompatActivity
{
    private static final String TAG = "_IHMConnexion"; //!< TAG pour les logs
    private EditText editTextapplicationID; //!< Pour la saisie de l'applicationID de TTN
    private EditText editMotDePasse; //!< Pour la saisie de la clé d'accès de TTN
    private String applicationID; //!< L'applicationID de TTN
    private String key; //!< La clé d'accès de TTN
    private Button boutonConnexion;

    public String getApplicationID()
    {
        return applicationID;
    }

    public String getKey()
    {
        return key;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ihm_connexion);
        //Log.d(TAG,"onCreate()");

        Intent intent = getIntent();
        applicationID = intent.getStringExtra("application");
        key = intent.getStringExtra("key");
        Log.d(TAG,"onCreate() applicationID = " + applicationID + " - key = " + key);

        initialiserIHM();
    }

    /**
     * @brief Initialise l'IHM de l'activité
     */
    private void initialiserIHM()
    {
        editTextapplicationID = (EditText) findViewById(R.id.editTextTextApplicationID);
        editMotDePasse = (EditText) findViewById(R.id.editTextTextPassword);
        boutonConnexion = (Button) findViewById(R.id.button);

        editTextapplicationID.setText(applicationID);
        editMotDePasse.setText(key);

        boutonConnexion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG,"onClick() applicationID = " + editTextapplicationID.getText() + " - key = " + editMotDePasse.getText());
                finish();
            }
        });
    }

    /**
     * @brief Termine l'activité
     */
    @Override
    public void finish()
    {
        Log.d(TAG, "finish()");

        final Intent intent = new Intent(IHMConnexion.this, IHMMobile.class);

        intent.putExtra("application", editTextapplicationID.getText().toString());
        intent.putExtra("key", editMotDePasse.getText().toString());
        Log.d(TAG,"finish() applicationID = " + editTextapplicationID.getText().toString() + " - key = " + editMotDePasse.getText().toString());

        setResult(RESULT_OK, intent);
        super.finish();
    }
}