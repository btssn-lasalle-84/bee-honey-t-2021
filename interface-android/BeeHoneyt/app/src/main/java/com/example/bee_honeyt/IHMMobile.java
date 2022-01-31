package com.example.bee_honeyt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @file IHMMobile.java
 * @brief Déclaration de la classe IHMMobile
 * @details Permet d'afficher les données de l'IHM
 * @author FOUCAULT Clémentine
 * $LastChangedRevision: 166 $
 * $LastChangedDate: 2021-06-14 09:28:36 +0200 (lun. 14 juin 2021) $
 */

// Pour Logcat : _IHMMobile|_Communication|_Ruche|_Alertes|_Historique|_IHMGraphique|FATAL|Exception

/**
 * @class IHMMobile
 * @brief L'activité principale de l'application BeeHoney't
 */
public class IHMMobile extends AppCompatActivity
{
    /**
     * Constantes
     */
    private static final String TAG = "_IHMMobile"; //!< TAG pour les logs
    private static final int EXTRAIRE_DONNEE_POIDS = 1;//!< Le numéro de port pour les données de poids
    private static final int EXTRAIRE_DONNNEES_ENVIRONEMENT = 2; //!< Le numéro de port pour les données de l'environnement

    private static final String APPLICATION_ID = "rucher"; //!< L'application ID par défaut
    private static final int CHAMP_TOPIC_APPLICATION_ID = 0;
    private static final int CHAMP_TOPIC_DEVICE_ID = 1;
    private static final int ACTIVITE_CONNEXION = 1; //!< L'identifiant de l'activité IHMConnexion

    /**
     * Attributs
     */
    private static Vector<Ruche> rucher; //!< Conteneur pour les ruches
    private static Vector<Alertes> parametrageAlertes;
    private StockageRucher stockage; //!< Le stockage
    private Ruche rucheSelectionnee; //!< Relation avec la classe ruche
    private int indexRucheSelectionnee; //!< index dans la liste
    private ArrayAdapter<String> adapterRuche; //!< Adaptateur pour mettre la liste de noms de ruche
    private Communication communicationMQTT; //!< Relation avec la classe Communication
    private String reponseNom; //!< La réponse du nom entré dans la zone de texte pour ajouté une ruche
    private String reponseDeviceID; //!< La réponse du device ID entré dans la zone de texte pour ajouté une ruche
    private String reponseTemperatureInterieureMax; //!< La réponse de la température intérieure maximum entré dans la zone de texte pour paramétrer une ruche
    private String reponseTemperatureInterieureMin; //!< La réponse de la température intérieure minimum entré dans la zone de texte pour paramétrer une ruche
    private String reponseTemperatureExterieureMax; //!< La réponse de la température extérieure maximum entré dans la zone de texte pour paramétrer une ruche
    private String reponseTemperatureExterieureMin; //!< La réponse de la température extérieure minimum entré dans la zone de texte pour paramétrer une ruche
    private String reponseHumiditeInterieureMax; //!< La réponse de l'humidite intérieure maximum entré dans la zone de texte pour paramétrer une ruche
    private String reponseHumiditeInterieureMin; //!< La réponse de l'humidite intérieure minimum entré dans la zone de texte pour paramétrer une ruche
    private String reponseHumiditeExterieureMax; //!< La réponse de l'humidite extérieure maximum entré dans la zone de texte pour paramétrer une ruche
    private String reponseHumiditeExterieureMin; //!< La réponse de l'humidite extérieure minimum entré dans la zone de texte pour paramétrer une ruche
    private String reponsePoidsMax; //!< La réponse du poids maximum entré dans la zone de texte pour paramétrer une ruche
    private String reponsePoidsMin; //!< La réponse du poids minimum entré dans la zone de texte pour paramétrer une ruche
    private String reponsePressionMax; //!< La réponse de la pression maximum entré dans la zone de texte pour paramétrer une ruche
    private String reponsePressionMin; //!< La réponse de la pression minimum entré dans la zone de texte pour paramétrer une ruche
    private String applicationID;
    private String key;
    private boolean reconnexion = false;

    /**
     * Les éléments graphiques de l'IHM
     */
    private Spinner listeRuches; //!< Liste déroulante pour les ruches
    private TextView afficheTemperatureInterieur; //!< Zone pour afficher la température interieure
    private TextView afficheTemperatureExterieur; //!< Zone pour afficher la température exterieure
    private TextView afficheHumiditeInterieur; //!< Zone pour afficher l'humidité interieure
    private TextView afficheHumiditeExterieur; //!< Zone pour afficher l'humidité exterieure
    private TextView affichePoids; //!< Zone pour afficher le poids
    private TextView affichePression; //!< Zone pour afficher la pression
    private TextView afficheHorodatage; //!< Zone pour afficher l'horodatage
    private TextView afficheConnexion; //!< Zone pour afficher la connexion
    private TextView afficheDeconnexion; //!< Zone pour afficher la deconnexion
    private List<String> listeNomsRuches; //!< Zone pour afficher la liste du noms des ruches
    private TextView alerteTemperatureInterieure; //!< Zone pour afficher une alerte Temperature interieure
    private TextView alerteTemperatureExterieure;//!< Zone pour afficher une alerte Temperature exterieure
    private TextView alerteHumiditeInterieure; //!< Zone pour afficher une alerte Humidite interieure
    private TextView alerteHumiditeExterieure; //!< Zone pour afficher une alerte Humidite exterieure
    private TextView alertePoids; //!< Zone pour afficher une alerte Poids
    private TextView alertePression; //!< Zone pour afficher une alerte Pression
    private ImageView rucheTemperatureExterieureNormale;
    private ImageView rucheTemperatureExterieureFroid;
    private ImageView rucheTemperatureExterieureChaud;
    private ImageView rucheTemperatureInterieureNormale;
    private ImageView rucheTemperatureInterieureChaud;
    private ImageView rucheTemperatureInterieureFroid;
    private ImageView rucheHumiditeExterieureNormale;
    private ImageView rucheHumiditeExterieureHaute;
    private ImageView rucheHumiditeExterieureBasse;
    private ImageView rucheHumiditeInterieureNormale;
    private ImageView rucheHumiditeInterieureHaute;
    private ImageView rucheHumiditeInterieureBasse;
    private ImageView ruchePoidsNormale;
    private ImageView ruchePoidsHaute;
    private ImageView ruchePoidsBas;
    private ImageView ruchePressionNormale;
    private ImageView ruchePressionHaute;
    private ImageView ruchePressionBasse;
    private Button connexion;
    /**
     * @brief Méthode appelée à la création de l'application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"onCreate()");

        getSupportActionBar().setTitle(getString(R.string.app_name) + " v" + BuildConfig.VERSION_NAME);

        initialiserStockage();

        creerRucher();

        initialiserIHM();

        initialiserMQTT();
    }

    /**
     * @brief Initialiser le stackage des ruches
     */
    private void initialiserStockage()
    {
        stockage = new StockageRucher(this);
    }

    /**
     * @brief Initialiser la communication MQTT
     */
    private void initialiserMQTT()
    {
        recupererParametreConnexion();

        communicationMQTT = new Communication(getApplicationContext(), handler);

        if(!applicationID.isEmpty() && !key.isEmpty())
        {
            communicationMQTT.setUsername(applicationID);
            communicationMQTT.setPassword(key);

            communicationMQTT.connecter();
        }
    }

    /**
     * @brief Méthode pour créé une ruche
     */
    private void creerRucher()
    {
        // Crée un rucher de test (avec les simulateurs)
        rucher = stockage.obtenirRuches();

        // On sélectionne une ruche par défaut
        indexRucheSelectionnee = -1;
        if(rucher.size() > 0)
        {
            rucheSelectionnee = rucher.get(0);
            //indexRucheSelectionnee = 0;
        }
    }

    /**
     * @brief Méthode appelée au démarrage après le onCreate() ou un restart après un onStop()
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d(TAG,"onStart()");
    }

    /**
     * @brief Méthode appelée après onStart() ou après onPause()
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(TAG,"onResume()");
    }

    /**
     * @brief Méthode appelée après qu'une boîte de dialogue s'est affichée (on reprend sur un onResume()) ou avant onStop() (activité plus visible)
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d(TAG,"onPause()");
    }

    /**
     * @brief Méthode appelée lorsque l'activité n'est plus visible
     */
    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d(TAG,"onStop()");
    }

    /**
     * @brief Méthode appelée à la destruction de l'application (après onStop() et détruite par le système Android)
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG,"onDestroy()");
    }

    /**
     * @brief Méthode pour initialiser l'IHM
     */
    private void initialiserIHM()
    {
        initialiserListeRuches();

        alerteTemperatureInterieure = (TextView) findViewById(R.id.alerteTemperatureInterieure);
        alerteTemperatureExterieure = (TextView) findViewById(R.id.alerteTemperatureExterieure);
        alerteHumiditeInterieure = (TextView) findViewById(R.id.alerteHumiditeInterieure);
        alerteHumiditeExterieure = (TextView) findViewById(R.id.alerteHumiditeExterieure);
        alertePoids = (TextView) findViewById(R.id.alertePoids);
        alertePression = (TextView) findViewById(R.id.alertePression);
        rucheTemperatureExterieureNormale = (ImageView) findViewById(R.id.rucheTemperatureExterieureNormale);
        rucheTemperatureExterieureFroid = (ImageView) findViewById(R.id.rucheTemperatureExterieureFroid);
        rucheTemperatureExterieureChaud = (ImageView) findViewById(R.id.rucheTemperatureExterieurechaud);
        rucheTemperatureInterieureNormale = (ImageView) findViewById(R.id.rucheTemperatureInterieureNormale);
        rucheTemperatureInterieureFroid = (ImageView) findViewById(R.id.rucheTemperatureInterieureFroid);
        rucheTemperatureInterieureChaud = (ImageView) findViewById(R.id.rucheTemperatureInterieureChaud);
        rucheHumiditeExterieureNormale = (ImageView) findViewById(R.id.rucheHumiditeExterieureNormale);
        rucheHumiditeExterieureBasse = (ImageView) findViewById(R.id.rucheHumiditeExterieureBasse);
        rucheHumiditeExterieureHaute = (ImageView) findViewById(R.id.rucheHumiditeExterieureHaute);
        rucheHumiditeInterieureNormale = (ImageView) findViewById(R.id.rucheHumiditeInterieureNormale);
        rucheHumiditeInterieureBasse = (ImageView) findViewById(R.id.rucheHumiditeInterieureBasse);
        rucheHumiditeInterieureHaute = (ImageView) findViewById(R.id.rucheHumiditeInterieureHaute);
        ruchePoidsNormale = (ImageView) findViewById(R.id.ruchePoidsNormale);
        ruchePoidsBas = (ImageView) findViewById(R.id.ruchePoidsTropFaible);
        ruchePoidsHaute = (ImageView) findViewById(R.id.ruchePoidsTropLourd);
        ruchePressionNormale = (ImageView) findViewById(R.id.ruchepressionNormale);
        ruchePressionBasse = (ImageView) findViewById(R.id.ruchePressionBasse);
        ruchePressionHaute = (ImageView) findViewById(R.id.ruchePressionHaute);

        initialiserImageAlertes();


        initialiserAffichageDonnees();
    }

    private void initialiserImageAlertes()
    {
        rucheTemperatureExterieureChaud.setVisibility(View.INVISIBLE);
        rucheTemperatureExterieureFroid.setVisibility(View.INVISIBLE);
        rucheTemperatureExterieureNormale.setVisibility(View.VISIBLE);
        rucheTemperatureInterieureChaud.setVisibility(View.INVISIBLE);
        rucheTemperatureInterieureFroid.setVisibility(View.INVISIBLE);
        rucheTemperatureInterieureNormale.setVisibility(View.VISIBLE);
        rucheHumiditeInterieureHaute.setVisibility(View.INVISIBLE);
        rucheHumiditeInterieureBasse.setVisibility(View.INVISIBLE);
        rucheHumiditeInterieureNormale.setVisibility(View.VISIBLE);
        rucheHumiditeExterieureHaute.setVisibility(View.INVISIBLE);
        rucheHumiditeExterieureBasse.setVisibility(View.INVISIBLE);
        rucheHumiditeExterieureNormale.setVisibility(View.VISIBLE);
        ruchePoidsHaute.setVisibility(View.INVISIBLE);
        ruchePoidsBas.setVisibility(View.INVISIBLE);
        ruchePoidsNormale.setVisibility(View.VISIBLE);
        ruchePressionHaute.setVisibility(View.INVISIBLE);
        ruchePressionBasse.setVisibility(View.INVISIBLE);
        ruchePressionNormale.setVisibility(View.VISIBLE);
    }

    /**
     * @brief Méthode pour initialiser l'affichage des données
     */
    private void initialiserAffichageDonnees()
    {
        afficherTemperatureInterieure(0.0);
        afficherTemperatureExterieure(0.0);
        afficherHumiditeInterieure(0);
        afficherHumiditeExterieure(0);
        afficherPoids(0.0);
        afficherPression(0);
        afficherHorodatage("");

        remettreAZeroAlertes();
    }

    /**
     * @brief Réinitialise l'affichage des alertes
     */
    private void remettreAZeroAlertes()
    {
        alerteTemperatureExterieure.setText("");
        alerteTemperatureInterieure.setText("");
        alerteHumiditeExterieure.setText("");
        alerteHumiditeInterieure.setText("");
        alertePoids.setText("");
        alertePression.setText("");
    }

    /**
     * @brief Initialise la liste déroulante des ruches
     */
    private void initialiserListeRuches()
    {
        listeRuches = findViewById(R.id.listeRuches);

        listeNomsRuches = new ArrayList<String>();
        for (int i = 0; i < rucher.size(); i++)
        {
            listeNomsRuches.add(rucher.get(i).getNom());
        }

        adapterRuche = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, listeNomsRuches);
        adapterRuche.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listeRuches.setAdapter(adapterRuche);

        listeRuches.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Log.d(TAG, "listeRuches onItemSelected()");
                for (int i = 0; i < rucher.size(); i++)
                {
                    if (rucher.get(i).getNom().equals(listeNomsRuches.get(position)))
                    {
                        Log.d(TAG, "Sélection : " + rucher.get(i).getNom() + " - indexRucheSelectionnee = " + indexRucheSelectionnee);

                        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                        ((TextView) parent.getChildAt(0)).setTextSize(22);

                        int indexRucheSelectionneePrecedente = indexRucheSelectionnee;
                        // Stocke la ruche précédente
                        if(indexRucheSelectionnee != -1)
                        {
                            rucher.set(indexRucheSelectionnee, rucheSelectionnee);
                        }

                        // Sélectionne une autre ruche
                        rucheSelectionnee = rucher.get(i);
                        indexRucheSelectionnee = i;

                        // Affichage des données de la ruche sélectionnée
                        double temperatureInterieure = rucheSelectionnee.getMesureRuche().getTemperatureInterieure();
                        afficherTemperatureInterieure(temperatureInterieure);
                        double temperatureExterieure = rucheSelectionnee.getMesureRuche().getTemperatureExterieure();
                        afficherTemperatureExterieure(temperatureExterieure);
                        int humiditeInterieure = rucheSelectionnee.getMesureRuche().getHumiditeInterieure();
                        afficherHumiditeInterieure(humiditeInterieure);
                        int humiditeExterieure = rucheSelectionnee.getMesureRuche().getHumiditeExterieure();
                        afficherHumiditeExterieure(humiditeExterieure);
                        remettreAZeroAlertes();
                        initialiserImageAlertes();

                        double poids = rucheSelectionnee.getMesureRuche().getPoids();
                        afficherPoids(poids);
                        remettreAZeroAlertes();
                        initialiserImageAlertes();

                        int pression = rucheSelectionnee.getMesureRuche().getPression();
                        afficherPression(pression);

                        String horodatage = rucheSelectionnee.getHorodatage();
                        afficherHorodatage(horodatage);
                        break;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    /**
     * @brief Affiche la température intérieure
     * @param temperatureInterieure la température intérieure en °C
     */
    public void afficherTemperatureInterieure(double temperatureInterieure)
    {
        afficheTemperatureInterieur = (TextView) findViewById(R.id.afficheTemperatureInterieure);
        afficheTemperatureInterieur.setText(temperatureInterieure + " °C");
    }

    /**
     * @brief Affiche la température exterieure
     * @param temperatureExterieure la température exterieure en °C
     */
    public void afficherTemperatureExterieure(double temperatureExterieure)
    {
        afficheTemperatureExterieur = (TextView) findViewById(R.id.afficheTemperatureExterieure);
        afficheTemperatureExterieur.setText(temperatureExterieure + " °C");
    }

    /**
     * @brief Affiche l'humidite interieure
     * @param humiditeInterieure l'humidite interieure en %
     */
    public void afficherHumiditeInterieure(int humiditeInterieure)
    {
        afficheHumiditeInterieur = (TextView) findViewById(R.id.afficheHumiditeInterieure);
        afficheHumiditeInterieur.setText(humiditeInterieure + " %");
    }

    /**
     * @brief Affiche l'humidite exterieure
     * @param humiditeExterieure l'humidite exterieure en %
     */
    public void afficherHumiditeExterieure(int humiditeExterieure)
    {
        afficheHumiditeExterieur = (TextView) findViewById(R.id.afficheHumiditeExterieure);
        afficheHumiditeExterieur.setText(humiditeExterieure + " %");
    }

    /**
     * @brief Affiche le poids
     * @param poids le poids en Kg
     */
    public void afficherPoids(double poids)
    {
        affichePoids = (TextView) findViewById(R.id.affichePoids);
        affichePoids.setText(poids + " Kg");
    }

    /**
     * @brief Affiche la pression
     * @param pression la pression en hPA
     */
    public void afficherPression(int pression)
    {
        affichePression = (TextView) findViewById(R.id.affichePression);
        affichePression.setText(pression  + " hPa");
    }

    /**
     * @brief Affiche l'horodatage
     * @param horodatage l'horodatage en JJ/MM/AA + l'heure
     */
    public void afficherHorodatage(String horodatage)
    {
        afficheHorodatage = (TextView) findViewById(R.id.afficheHorodatage);
        afficheHorodatage.setText(horodatage);
    }

    /**
     * @brief Affiche la connexion au topic
     * @param message
     */
    public void afficherConnexion(String message)
    {
        afficheConnexion = (TextView) findViewById(R.id.etatConnexion);
        afficheConnexion.setText(message);
    }

    /**
     * @brief Affiche la Deconnexion au topic
     * @param message
     */
    public void afficherDeconnexion(String message)
    {
        afficheDeconnexion = (TextView) findViewById(R.id.etatConnexion);
        afficheDeconnexion.setText(message);
    }

    /**
     * @brief Handler de communication entre l'activité et la communication MQTT
     */
    final private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);

            Bundle bundle = msg.getData();

            switch(bundle.getInt("etat"))
            {
                case Communication.TTN_CONNECTE:
                    Log.d(TAG, "handleMessage() TTN connecté");
                    afficherConnexion("Connectée");
                    abonnerRuches();
                    break;
                case Communication.TTN_DECONNECTE:
                    Log.d(TAG, "handleMessage() TTN déconnecté");
                    afficherDeconnexion("Déconnecté");
                    if(reconnexion)
                    {
                        communicationMQTT.connecter();
                        reconnexion = false;
                    }
                    break;
                case Communication.TTN_MESSAGE:
                    Log.d(TAG, "handleMessage() TTN topic = " + bundle.getString("topic") + " message = " + bundle.getString("message"));
                    String topic = bundle.getString("topic");

                    //decoderTopic(topic); // pour déboguage
                    String deviceId = extraireDeviceId(topic);

                    // Filtrer le deviceId avec celui de la ruche sélectionnée
                    Log.d(TAG, "handleMessage() deviceId = " + deviceId + " ? " + rucheSelectionnee.getDeviceID());
                    if(deviceId.equals(rucheSelectionnee.getDeviceID()))
                    {
                        Log.d(TAG, "handleMessage() Données pour la ruche = " + rucheSelectionnee.getNom() + " !!!");
                        String message = bundle.getString("message");
                        traiterMessage(message);
                    }
                    break;
            }
        }
    };

    /**
     * @brief Methode pour traiter le message en extrayant les données
     * @param message
     */
    private void traiterMessage(String message)
    {
        int port = extrairePort(message);
        String payloadFields = extraireDonneeMessage(message);
        String metaData = extraireHorodatageMessage(message);

        switch (port)
        {
            case EXTRAIRE_DONNEE_POIDS:
                double poids = extrairePoids(payloadFields);
                rucheSelectionnee.getMesureRuche().setPoids(poids);
                afficherPoids(poids);
                afficherAlertePoids();
                break;
            case EXTRAIRE_DONNNEES_ENVIRONEMENT:
                double temperatureInterieure = extraireTemperatureInterieure(payloadFields);
                rucheSelectionnee.getMesureRuche().setTemperatureInterieure(temperatureInterieure);
                afficherTemperatureInterieure(temperatureInterieure);
                double temperatureExterieure = extraireTemperatureExterieure(payloadFields);
                rucheSelectionnee.getMesureRuche().setTemperatureExterieure(temperatureExterieure);
                afficherTemperatureExterieure(temperatureExterieure);
                int humiditeInterieure = extraireHumiditeInterieure(payloadFields);
                rucheSelectionnee.getMesureRuche().setHumiditeInterieure(humiditeInterieure);
                afficherHumiditeInterieure(humiditeInterieure);
                int humiditeExterieure = extraireHumiditeExterieure(payloadFields);
                rucheSelectionnee.getMesureRuche().setHumiditeExterieure(humiditeExterieure);
                afficherHumiditeExterieure(humiditeExterieure);
                int pression = extrairePression(payloadFields);
                rucheSelectionnee.getMesureRuche().setPression(pression);
                afficherPression(pression);
                afficherAlertesEnvironnement();
                break;
            default:
                return;
        }

        // le dernier horodatage reçu
        String horodatage = extraireHorodatage(metaData);
        rucheSelectionnee.setHorodatage(horodatage);
        afficherHorodatage(horodatage);
    }

    /**
     * @brief Méthode pour afficher les alertes de l'environnement
     */
    private void afficherAlertesEnvironnement()
    {
        afficherAlerteTemperatureInterieure();
        afficherAlerteTemperatureExterieure();
        afficherAlerteHumiditeInterieure();
        afficherAlerteHumiditeExterieure();
        afficherAlertePression();
    }

    /**
     * @brief Méthode pour extraire la pression
     *
     * @param payloadFields
     * @return int La pression
     */
    private int extrairePression(String payloadFields)
    {
        int pression = 0; // ce n'est pas un double !

        try
        {
            JSONObject json = null;
            Iterator<String> it = null;
            json = new JSONObject(payloadFields);

            /**
             * payloadFields = {"humiditeExt":44,"humiditeInt":44,"pression":1021,"temperatureExt":14.5,"temperatureInt":23.1}
             */

            pression = json.getInt("pression");
            Log.d(TAG, "extrairePression() pression = " + pression);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return pression;
    }

    /**
     * @brief Méthode pour extraire l'humidité exterieure
     *
     * @param payloadFields
     * @return int L'humidité exterieure
     */
    private int extraireHumiditeExterieure(String payloadFields)
    {
        int humiditeExterieure = 0;

        try
        {
            JSONObject json = null;
            Iterator<String> it = null;
            json = new JSONObject(payloadFields);

            /**
             * payloadFields = {"humiditeExt":44,"humiditeInt":44,"pression":1021,"temperatureExt":14.5,"temperatureInt":23.1}
             */

            humiditeExterieure = json.getInt("humiditeExt");
            Log.d(TAG, "extraireHumiditeExterieure() Humidite exterieure = " + humiditeExterieure);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return humiditeExterieure;
    }

    /**
     * @brief Méthode pour extraire l'humidité intérieure
     *
     * @param payloadFields
     * @return int l'humidité interieure
     */
    private int extraireHumiditeInterieure(String payloadFields)
    {
        int humiditeInterieure = 0; // ce n'est pas un double !

        try
        {
            JSONObject json = null;
            Iterator<String> it = null;
            json = new JSONObject(payloadFields);

            /**
             * payloadFields = {"humiditeExt":44,"humiditeInt":44,"pression":1021,"temperatureExt":14.5,"temperatureInt":23.1}
             */

            humiditeInterieure = json.getInt("humiditeInt");
            Log.d(TAG, "extraireHumiditeInterieure() Humidite interieure = " + humiditeInterieure);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return humiditeInterieure;
    }

    /**
     * @brief Méthode pour extraire la temperature exterieure
     *
     * @param payloadFields
     * @return double la temperature exterieure
     */
    private double extraireTemperatureExterieure(String payloadFields)
    {
        double temperatureExterieure = 0.0;

        try
        {
            JSONObject json = null;
            Iterator<String> it = null;
            json = new JSONObject(payloadFields);

            /**
             * payloadFields = {"humiditeExt":44,"humiditeInt":44,"pression":1021,"temperatureExt":14.5,"temperatureInt":23.1}
             */

            temperatureExterieure = json.getDouble("temperatureExt");
            Log.d(TAG, "extraireTemperatureExterieure() Temperature exterieure = " + temperatureExterieure);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return temperatureExterieure;
    }

    /**
     * @brief Méthode pour extraire la temperature interieure
     *
     * @param payloadFields
     * @return double la température interieure
     */
    private double extraireTemperatureInterieure(String payloadFields)
    {
        double temperatureInterieure = 0.0;

        try
        {
            JSONObject json = null;
            Iterator<String> it = null;
            json = new JSONObject(payloadFields);

            /**
             * payloadFields = {"humiditeExt":44,"humiditeInt":44,"pression":1021,"temperatureExt":14.5,"temperatureInt":23.1}
             */

            temperatureInterieure = json.getDouble("temperatureInt");
            Log.d(TAG, "extraireTemperatureInterieure() Temperature interieure = " + temperatureInterieure);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return temperatureInterieure;
    }

    /**
     * @brief Méthode pour extraire le port
     *
     * @param message
     * @return int le port
     */
    private int extrairePort(String message)
    {
        int port = 0;

        try
        {
            JSONObject json = null;
            json = new JSONObject(message);

            // Récupère le port
            port = json.getInt("port");
            Log.d(TAG, "extrairePort() port = " + port);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return port;
    }

    /**
     * @brief Méthode pour s'abonner a une ruche
     */
    private void abonnerRuches()
    {
        // Abonner les ruches pour recevoir les données
        for (int i = 0; i < rucher.size(); i++)
        {
            communicationMQTT.souscrireTopic(rucher.get(i).getDeviceID());
        }
    }

    /**
     * @brief Méthode pour se desabonner d'une ruche
     */
    private void desAbonnerRuches()
    {
        // Abonner les ruches pour recevoir les données
        for (int i = 0; i < rucher.size(); i++)
        {
           communicationMQTT.unsubscribe(rucher.get(i).getDeviceID());
        }
    }

    /**
     * @brief Méthode pour decoder le topic
     * @param topic
     */
    private void decoderTopic(String topic)
    {
        Log.d(TAG, "extraireTopic() topic = " + topic);
        /**
         *  Exemple -> topic = rucher/devices/ruche-2-sim/up
         *  "rucher" => ApplicationID
         *  "ruche-2-sim" => DeviceID
         */
        try
        {
            if (topic.startsWith(APPLICATION_ID))
            {
                String[] champs = topic.split("/");

                for (int i = 0; i < champs.length; i++)
                {
                    Log.d(TAG, "champs = " + champs[i]);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @brief Méthode pour extraire les données du message
     * @param message
     * @return String Le payloadFields
     */
    private String extraireDonneeMessage(String message)
    {
        //Log.d(TAG, "extraireDonneeMessage() message = " + message);
        /**
         *  Exemple -> message = {"app_id":"rucher","dev_id":"ruche-2-sim","hardware_serial":"0004A30B00203CF8","port":1,"counter":15372,"payload_raw":"AN4=","payload_fields":{"poids":22.2},"metadata":{"time":"2021-04-08T14:30:07.730608914Z","frequency":867.5,"modulation":"LORA","data_rate":"SF7BW125","airtime":46336000,"coding_rate":"4/5","gateways":[{"gtw_id":"btssn-lasalle-84","timestamp":3222441660,"time":"2021-04-08T14:30:08Z","channel":0,"rssi":-77,"snr":7.75,"rf_chain":0}]}}
         *
         *  "port":1 => les données associées (ici le poids)
         *  "payload_fields":{"poids":22.2}
         *  "time":"2021-04-08T14:30:07.730608914Z" ou "timestamp":3222441660,"time":"2021-04-08T14:30:08Z" (Attention au décalage horaire +GMT)
         */
        String payloadFields = null;

        try
        {
            JSONObject json = null;
            Iterator<String> it = null;

            json = new JSONObject(message);

            it = json.keys();
            while (it.hasNext())
            {
                String cle = it.next();

                // Récupère le payload_fields ?
                if (cle.equals("payload_fields"))
                {
                    payloadFields = json.getString(cle);
                    Log.d(TAG, "extraireDonneeMessage() payload_fields = " + payloadFields);
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return payloadFields;
    }

    /**
     * @brief Méthode pour extraire l'horodatage
     * @param message
     * @return String La meta data
     */
    private String extraireHorodatageMessage(String message)
    {
        //Log.d(TAG, "extraireHorodatageMessage() message = " + message);
        /**
         *  Exemple -> message = {"app_id":"rucher","dev_id":"ruche-2-sim","hardware_serial":"0004A30B00203CF8","port":1,"counter":15372,"payload_raw":"AN4=","payload_fields":{"poids":22.2},"metadata":{"time":"2021-04-08T14:30:07.730608914Z","frequency":867.5,"modulation":"LORA","data_rate":"SF7BW125","airtime":46336000,"coding_rate":"4/5","gateways":[{"gtw_id":"btssn-lasalle-84","timestamp":3222441660,"time":"2021-04-08T14:30:08Z","channel":0,"rssi":-77,"snr":7.75,"rf_chain":0}]}}
         *
         *  "port":1 => les données associées (ici le poids)
         *  "payload_fields":{"poids":22.2}
         *  "time":"2021-04-08T14:30:07.730608914Z" ou "timestamp":3222441660,"time":"2021-04-08T14:30:08Z" (Attention au décalage horaire +GMT)
         */
        String metaData = null;

        try
        {
            JSONObject json = null;
            Iterator<String> it = null;

            json = new JSONObject(message);

            it = json.keys();
            while (it.hasNext())
            {
                String cle = it.next();

                if (cle.equals("metadata"))
                {
                    metaData = json.getString(cle);
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return metaData;
    }

    /**
     * @brief Méthode pour mettre l'horodatage sur le bon fuseau horaire et la date au bon format
     * @param metaData
     * @return String Le format de l'horodatage
     */
    public String extraireHorodatage(String metaData)
    {
        String horodatageFormate = "";

        try
        {
            Log.d(TAG, "extraireHorodatage() metadata = " + metaData);
            JSONObject json = null;
            json = new JSONObject(metaData);

            /**
             * {"time":"2021-04-22T18:09:27.104487894Z","frequency":868.3,"modulation":"LORA","data_rate":"SF7BW125","airtime":46336000,"coding_rate":"4\/5","gateways":[{"gtw_id":"btssn-lasalle-84","timestamp":2948051339,"time":"2021-04-22T18:09:27Z","channel":0,"rssi":-71,"snr":8.75,"rf_chain":0}]}
             */

            String time = json.getString("time");
            Log.d(TAG, "extraireHorodatage() time = " + time);
            // time = 2021-04-22T18:46:49.40690864Z

            SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat sdfOut = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRENCH); // choix du format
            sdfIn.setTimeZone(TimeZone.getTimeZone("UTC"));
            // Conversion fuseau horaire UTC -> France
            Date date = null;
            try
            {
                date = sdfIn.parse(time);
                sdfOut.setTimeZone(TimeZone.getDefault());
                horodatageFormate = sdfOut.format(date);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            Log.d(TAG,"extraireHorodatage() horodatageFormate = " + horodatageFormate);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return horodatageFormate;
    }

    /**
     * @brief Méthode pour extraire le deviceID
     * @param topic
     * @return String le device ID
     */
    public String extraireDeviceId(String topic)
    {
        Log.d(TAG, "extraireDeviceId() topic = " + topic);
        String deviceId = "";

        //Expression régulière : le . est pour n'importe quel caractere sauf retour a la ligne, le + est pour 1 ou plus, et le ? est pour 0 ou 1 mais dans notre cas il precède un + alors il signifie 1 ou + non gourmand
        Pattern topicPattern = Pattern.compile("/(.+?)/(.+?)/");
        String contenueTopic = topic;

        Matcher topicMatcher = topicPattern.matcher( contenueTopic );
        if (topicMatcher.find())
        {
            Log.d( TAG," Device ID = " + topicMatcher.group(2));
            deviceId = topicMatcher.group(2);
        }

        return deviceId;
    }

    /**
     * @brief Méthode pour extraire le poids
     * @param payloadFields
     * @return double le poids
     */
    private double extrairePoids(String payloadFields)
    {
        double poids = 0.0;

        try
        {
            JSONObject json = null;
            Iterator<String> it = null;
            json = new JSONObject(payloadFields);

            poids = json.getDouble("poids");
            Log.d(TAG, "extrairePoids() poids = " + poids);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return poids;
    }

    /**
     * @brief Méthode pour afficher la fenetre supprimer une ruche
     * @param supprimer
     */
    public void afficheFenetreSupprimer(View supprimer)
    {
        if(indexRucheSelectionnee == -1)
            return;

        AlertDialog.Builder alert = new AlertDialog.Builder(this );
        String oui = "Oui";
        String non = "Non";
        alert.setTitle("Supprimer une ruche");
        alert.setMessage("Etes-vous sur de vouloir supprimer " + rucheSelectionnee.getNom() + " ?");
        alert.setPositiveButton(oui, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(IHMMobile.this,"Votre ruche a bien été supprimée",Toast.LENGTH_LONG).show();
                supprimerRuche(rucheSelectionnee);
            }
        });
        alert.setNegativeButton(non, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //Toast.makeText(IHMMobile.this,"Votre ruche n'a pas été suppriméé",Toast.LENGTH_LONG).show();
            }
        });
        alert.create().show();
    }

    /**
     * @brief Méthode pour afficher la fenetre ajouter une ruche
     * @param Ajouter
     */
    public void afficheFenetreAjouter(View Ajouter)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this );
        String ajout = "Ajouter";
        String annule = "Annuler";

        alert.setTitle("Ajouter une ruche");
        //alert.setView(R.layout.activity_main);

        LinearLayout layout = new LinearLayout(IHMMobile.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final TextView labelNom = new TextView(IHMMobile.this);
        labelNom.setText(" Nom :");
        layout.addView(labelNom);

        final EditText nom = new EditText(IHMMobile.this);
        layout.addView(nom);
        nom.setInputType(InputType.TYPE_CLASS_TEXT);

        final TextView labelDeviceID = new TextView(IHMMobile.this);
        labelDeviceID.setText(" DeviceID :");
        layout.addView(labelDeviceID);

        final EditText deviceID = new EditText((IHMMobile.this));
        layout.addView(deviceID);
        deviceID.setInputType(InputType.TYPE_CLASS_TEXT);

        alert.setView(layout);

        alert.setPositiveButton(ajout, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                reponseNom = nom.getText().toString();
                reponseDeviceID = deviceID.getText().toString();
                ajouterRuche(reponseNom, reponseDeviceID);
                Toast.makeText(IHMMobile.this,"Votre ruche a bien été ajoutée",Toast.LENGTH_LONG).show();
            }
        });
        alert.setNegativeButton(annule, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
                //Toast.makeText(IHMMobile.this,"Votre ruche n'a pas été ajoutée",Toast.LENGTH_LONG).show();
            }
        });
        alert.create().show();
    }

    /**
     * @brief Méthode pour ajouter une ruche
     * @param nom
     * @param deviceID
     */
    public void ajouterRuche(String nom, String deviceID)
    {
        rucher.add(new Ruche(nom, deviceID));
        stockage.editerRuche(nom,deviceID);
        adapterRuche.add(nom);

        abonnerRuches();
    }

    /**
     * @brief Méthode pour supprimer une ruche
     * @param ruche
     */
    public void supprimerRuche(Ruche ruche)
    {
        for (int i = 0; i < rucher.size(); i++)
        {
            desAbonnerRuches();

            if(rucher.get(i).getNom() == ruche.getNom() )
            {
                listeNomsRuches.remove(rucher.get(i).getNom());
                stockage.supprimerRuche(ruche.getDeviceID());
                stockage.supprimerAlertes(ruche.getNom());
                adapterRuche.remove(ruche.getNom());
            }
        }
    }

    /**
     * @brief Méthode pour afficher la fenetre ajouter une ruche
     * @param parametrer
     */
    public void afficheFenetreParametrageAlertes(View parametrer)
    {
        if(indexRucheSelectionnee == -1)
            return;

        AlertDialog.Builder alert = new AlertDialog.Builder(this );
        String ajout = "Ajouter";
        String annule = "Annuler";

        alert.setTitle("Paramétrer les seuils d'alerte");

        LinearLayout layout = new LinearLayout(IHMMobile.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final TextView labelTemperatureInterieureMax = new TextView(IHMMobile.this);
        labelTemperatureInterieureMax.setText(" Température intérieure maximale :");
        layout.addView(labelTemperatureInterieureMax);

        final EditText temperatureInterieureMax = new EditText(IHMMobile.this);
        temperatureInterieureMax.setText(Double.toString(rucheSelectionnee.getAlerteRuche().getTemperatureInterieurMax()));
        layout.addView(temperatureInterieureMax);
        temperatureInterieureMax.setInputType(InputType.TYPE_CLASS_TEXT);

        final TextView labelTemperatureInterieureMin = new TextView(IHMMobile.this);
        labelTemperatureInterieureMin.setText(" Température intérieure minimale:");
        layout.addView(labelTemperatureInterieureMin);

        final EditText temperatureInterieureMin = new EditText((IHMMobile.this));
        temperatureInterieureMin.setText(Double.toString(rucheSelectionnee.getAlerteRuche().getTemperatureInterieurMin()));
        layout.addView(temperatureInterieureMin);
        temperatureInterieureMin.setInputType(InputType.TYPE_CLASS_TEXT);

        final TextView labelTemperatureExterieureMax = new TextView(IHMMobile.this);
        labelTemperatureExterieureMax.setText(" Température extérieure maximale :");
        layout.addView(labelTemperatureExterieureMax);

        final EditText temperatureExterieureMax = new EditText(IHMMobile.this);
        temperatureExterieureMax.setText(Double.toString(rucheSelectionnee.getAlerteRuche().getTemperatureExterieurMax()));
        layout.addView(temperatureExterieureMax);
        temperatureExterieureMax.setInputType(InputType.TYPE_CLASS_TEXT);

        final TextView labelTemperatureExterieureMin = new TextView(IHMMobile.this);
        labelTemperatureExterieureMin.setText(" Température extérieure minimale :");
        layout.addView(labelTemperatureExterieureMin);

        final EditText temperatureExterieureMin = new EditText((IHMMobile.this));
        temperatureExterieureMin.setText(Double.toString(rucheSelectionnee.getAlerteRuche().getTemperatureExterieurMin()));
        layout.addView(temperatureExterieureMin);
        temperatureExterieureMin.setInputType(InputType.TYPE_CLASS_TEXT);

        final TextView labelHumiditeInterieureMax = new TextView(IHMMobile.this);
        labelHumiditeInterieureMax.setText(" Humidité intérieure maximale :");
        layout.addView(labelHumiditeInterieureMax);

        final EditText humiditeInterieureMax= new EditText(IHMMobile.this);
        humiditeInterieureMax.setText(Integer.toString(rucheSelectionnee.getAlerteRuche().getHumiditeInterieurMax()));
        layout.addView(humiditeInterieureMax);
        humiditeInterieureMax.setInputType(InputType.TYPE_CLASS_TEXT);

        final TextView labelHumiditeInterieureMin = new TextView(IHMMobile.this);
        labelHumiditeInterieureMin.setText(" Humidité intérieure minimale :");
        layout.addView(labelHumiditeInterieureMin);

        final EditText humiditeInterieureMin = new EditText((IHMMobile.this));
        humiditeInterieureMin.setText(Integer.toString(rucheSelectionnee.getAlerteRuche().getHumiditeInterieurMin()));
        layout.addView(humiditeInterieureMin);
        humiditeInterieureMin.setInputType(InputType.TYPE_CLASS_TEXT);

        final TextView labelHumiditeExterieureMax = new TextView(IHMMobile.this);
        labelHumiditeExterieureMax.setText(" Humidité extérieure maximale :");
        layout.addView(labelHumiditeExterieureMax);

        final EditText humiditeExterieureMax= new EditText(IHMMobile.this);
        humiditeExterieureMax.setText(Integer.toString(rucheSelectionnee.getAlerteRuche().getHumiditeExterieurMax()));
        layout.addView(humiditeExterieureMax);
        humiditeExterieureMax.setInputType(InputType.TYPE_CLASS_TEXT);

        final TextView labelHumiditeExterieureMin = new TextView(IHMMobile.this);
        labelHumiditeExterieureMin.setText(" Humidité extérieure minimale :");
        layout.addView(labelHumiditeExterieureMin);

        final EditText humiditeExterieureMin = new EditText((IHMMobile.this));
        humiditeExterieureMin.setText(Integer.toString(rucheSelectionnee.getAlerteRuche().getHumiditeExterieurMin()));
        layout.addView(humiditeExterieureMin);
        humiditeExterieureMin.setInputType(InputType.TYPE_CLASS_TEXT);

        final TextView labelPoidsMax = new TextView(IHMMobile.this);
        labelPoidsMax.setText(" Poids maximal :");
        layout.addView(labelPoidsMax);

        final EditText poidsMax = new EditText((IHMMobile.this));
        poidsMax.setText(Double.toString(rucheSelectionnee.getAlerteRuche().getPoidsMax()));
        layout.addView(poidsMax);
        poidsMax.setInputType(InputType.TYPE_CLASS_TEXT);

        final TextView labelPoidsMin = new TextView(IHMMobile.this);
        labelPoidsMin.setText(" Poids minimal :");
        layout.addView(labelPoidsMin);

        final EditText poidsMin = new EditText((IHMMobile.this));
        poidsMin.setText(Double.toString(rucheSelectionnee.getAlerteRuche().getPoidsMin()));
        layout.addView(poidsMin);
        poidsMin.setInputType(InputType.TYPE_CLASS_TEXT);

        final TextView labelPressionMax = new TextView(IHMMobile.this);
        labelPressionMax.setText(" Pression maximal :");
        layout.addView(labelPressionMax);

        final EditText pressionMax = new EditText((IHMMobile.this));
        pressionMax.setText(Double.toString(rucheSelectionnee.getAlerteRuche().getPressionMax()));
        layout.addView(pressionMax);
        pressionMax.setInputType(InputType.TYPE_CLASS_TEXT);

        final TextView labelPressionMin = new TextView(IHMMobile.this);
        labelPressionMin.setText(" Pression minimal :");
        layout.addView(labelPressionMin);

        final EditText pressionMin = new EditText((IHMMobile.this));
        pressionMin.setText(Double.toString(rucheSelectionnee.getAlerteRuche().getPressionMin()));
        layout.addView(pressionMin);
        pressionMin.setInputType(InputType.TYPE_CLASS_TEXT);
        alert.setView(layout);

        alert.setPositiveButton(ajout, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                reponseTemperatureInterieureMax = temperatureInterieureMax.getText().toString();
                reponseTemperatureInterieureMin = temperatureInterieureMin.getText().toString();
                reponseTemperatureExterieureMax = temperatureExterieureMax.getText().toString();
                reponseTemperatureExterieureMin = temperatureExterieureMin.getText().toString();
                reponseHumiditeInterieureMax = humiditeInterieureMax.getText().toString();
                reponseHumiditeInterieureMin = humiditeInterieureMin.getText().toString();
                reponseHumiditeExterieureMax = humiditeExterieureMax.getText().toString();
                reponseHumiditeExterieureMin = humiditeExterieureMin.getText().toString();
                reponsePoidsMax = poidsMax.getText().toString();
                reponsePoidsMin = poidsMin.getText().toString();
                reponsePressionMax = pressionMax.getText().toString();
                reponsePressionMin = pressionMin.getText().toString();

                ajouterParametreAlerte(reponseTemperatureInterieureMax,reponseTemperatureInterieureMin,reponseTemperatureExterieureMax,reponseTemperatureExterieureMin,reponseHumiditeInterieureMax,reponseHumiditeInterieureMin,reponseHumiditeExterieureMax,reponseHumiditeExterieureMin,reponsePoidsMax,reponsePoidsMin,reponsePressionMax,reponsePressionMin);
                Toast.makeText(IHMMobile.this,"Vos paramètres ont bien été ajoutés",Toast.LENGTH_LONG).show();

                // Redéterminer les alertes
                afficherAlertesEnvironnement();
                afficherAlertePoids();
            }
        });
        alert.setNegativeButton(annule, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
                //Toast.makeText(IHMMobile.this,"Vos paramètres n'ont pas été ajoutés",Toast.LENGTH_LONG).show();
            }
        });
        alert.create().show();
    }

    /**
     * @brief Méthode pour afficher les graphiques
     * @param view
     */
    public void afficheGraphiques(View view)
    {
        if(indexRucheSelectionnee != -1)
        {
            Intent intent = new Intent(IHMMobile.this, IHMGraphique.class);
            intent.putExtra("application", communicationMQTT.getUsername());
            intent.putExtra("key", communicationMQTT.getPassword());
            intent.putExtra("device", rucher.get(indexRucheSelectionnee).getDeviceID());
            startActivity(intent);
        }
    }

    /**
     * @brief Méthode pour afficher l'activité de paramétrage de connexion TTN
     */
    public void afficheIHMConnexion(View view)
    {
        Intent intent = new Intent(IHMMobile.this, IHMConnexion.class);
        if(communicationMQTT != null)
        {
            intent.putExtra("application", communicationMQTT.getUsername());
            intent.putExtra("key", communicationMQTT.getPassword());
        }
        else
        {
            intent.putExtra("application", "");
            intent.putExtra("key", "");
        }

        // Démarre l'activité et attend un retour qui se fera dans onActivityResult()
        startActivityForResult(intent, ACTIVITE_CONNEXION);
    }

    /**
     * @brief Traite le retour de l'activité IHMGestionPartie
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d(TAG, "onActivityResult() resultCode : " + resultCode);

        /**
         * @todo Vérifier les paramètres et les stocker dans SharedPreferences
         * @todo Se reconnecter à TTN avec les nouveaux paramètres
         */
        if(requestCode == ACTIVITE_CONNEXION)
        {
            if(resultCode == RESULT_OK)
            {
                applicationID = intent.getStringExtra("application");
                key = intent.getStringExtra("key");
                Log.d(TAG, "onActivityResult() applicationID = " + applicationID + " - key = " + key);

                stockerParametreConnexion();

                communicationMQTT.setUsername(applicationID);
                communicationMQTT.setPassword(key);

                desAbonnerRuches();
                reconnexion = true;
                communicationMQTT.deconnecter();
            }
        }
    }

    /**
     * @brief Méthode pour définir les nouveaux seuils d'alerte d'une ruche
     */
    public void ajouterParametreAlerte(String reponseTemperatureInterieureMax, String reponseTemperatureInterieureMin, String reponseTemperatureExterieureMax, String reponseTemperatureExterieureMin, String reponseHumiditeInterieureMax, String reponseHumiditeInterieureMin, String reponseHumiditeExterieureMax, String reponseHumiditeExterieureMin, String reponsePoidsMax, String reponsePoidsMin,String reponsePressionMax, String reponsePressionMin)
    {
        Alertes seuilsAlertes = new Alertes(Double.parseDouble(reponseTemperatureInterieureMin),Double.parseDouble(reponseTemperatureInterieureMax),Double.parseDouble(reponseTemperatureExterieureMin),Double.parseDouble(reponseTemperatureExterieureMax),Integer.parseInt(reponseHumiditeInterieureMin),Integer.parseInt(reponseHumiditeInterieureMax),Integer.parseInt(reponseHumiditeExterieureMin),Integer.parseInt(reponseHumiditeExterieureMax),Double.parseDouble(reponsePoidsMin),Double.parseDouble(reponsePoidsMax),Double.parseDouble(reponsePressionMin),Double.parseDouble(reponsePressionMax));
        rucheSelectionnee.setAlerteRuche(seuilsAlertes);
        String alertesJSON = seuilsAlertes.toJSON();
        String nomRuche = rucheSelectionnee.getNom();
        stockage.editerAlertes(nomRuche, alertesJSON);
    }

    public void stockerParametreConnexion()
    {
        //Log.d(TAG, "stockerParametreConnexion() applicationID = " + applicationID + " - key = " + key);
        stockage.editerConnexionTTN(applicationID, key);
    }

    public void recupererParametreConnexion()
    {
        applicationID = stockage.obtenir("application");
        key = stockage.obtenir("key");
        Log.d(TAG, "recupererParametreConnexion() applicationID = " + applicationID + " - key = " + key);
    }

    /**
     * @brief Méthode pour afficher l'alerte de la température interieure
     */
    public void afficherAlerteTemperatureInterieure()
    {
        Log.d(TAG,"afficherAlerteTemperatureInterieure()");
        if(rucheSelectionnee.getMesureRuche().getTemperatureInterieure() > rucheSelectionnee.getAlerteRuche().getTemperatureInterieurMax())
        {
            alerteTemperatureInterieure.setText("Température intérieure trop élevée !");
            Log.d(TAG,"afficherAlerteTemperatureInterieure() = " + alerteTemperatureInterieure);
            alerteTemperatureInterieure.setTextColor(Color.parseColor("#ff0000"));
            rucheTemperatureInterieureChaud.setVisibility(View.VISIBLE);
            rucheTemperatureInterieureFroid.setVisibility(View.INVISIBLE);
            rucheTemperatureInterieureNormale.setVisibility(View.INVISIBLE);
        }
        else if(rucheSelectionnee.getMesureRuche().getTemperatureInterieure() < rucheSelectionnee.getAlerteRuche().getTemperatureInterieurMin())
        {
            alerteTemperatureInterieure.setText("Température intérieure trop basse !");
            alerteTemperatureInterieure.setTextColor(Color.parseColor("#498df7"));
            rucheTemperatureInterieureChaud.setVisibility(View.INVISIBLE);
            rucheTemperatureInterieureFroid.setVisibility(View.VISIBLE);
            rucheTemperatureInterieureNormale.setVisibility(View.INVISIBLE);
        }
        else
        {
            alerteTemperatureInterieure.setText("");
            rucheTemperatureInterieureChaud.setVisibility(View.INVISIBLE);
            rucheTemperatureInterieureFroid.setVisibility(View.INVISIBLE);
            rucheTemperatureInterieureNormale.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @brief Méthode pour afficher l'alerte de la température extérieure minimum
     */
    public void afficherAlerteTemperatureExterieure()
    {
        if(rucheSelectionnee.getMesureRuche().getTemperatureExterieure() > rucheSelectionnee.getAlerteRuche().getTemperatureExterieurMax())
        {
            alerteTemperatureExterieure.setText("Température extérieure trop élevée !");
            alerteTemperatureExterieure.setTextColor(Color.parseColor("#ff0000"));
            rucheTemperatureExterieureChaud.setVisibility(View.VISIBLE);
            rucheTemperatureExterieureFroid.setVisibility(View.INVISIBLE);
            rucheTemperatureExterieureNormale.setVisibility(View.INVISIBLE);
        }
        else if(rucheSelectionnee.getMesureRuche().getTemperatureExterieure() < rucheSelectionnee.getAlerteRuche().getTemperatureExterieurMin())
        {
            alerteTemperatureExterieure.setText("Température extérieure trop basse !");
            alerteTemperatureExterieure.setTextColor(Color.parseColor("#498df7"));
            rucheTemperatureExterieureChaud.setVisibility(View.INVISIBLE);
            rucheTemperatureExterieureFroid.setVisibility(View.VISIBLE);
            rucheTemperatureExterieureNormale.setVisibility(View.INVISIBLE);
        }
        else
        {
            alerteTemperatureExterieure.setText("");
            rucheTemperatureExterieureChaud.setVisibility(View.INVISIBLE);
            rucheTemperatureExterieureFroid.setVisibility(View.INVISIBLE);
            rucheTemperatureExterieureNormale.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @brief Méthode pour afficher l'alerte de l'humidité intérieure
     */
    public void afficherAlerteHumiditeInterieure()
    {
        if(rucheSelectionnee.getMesureRuche().getHumiditeInterieure() > rucheSelectionnee.getAlerteRuche().getHumiditeInterieurMax())
        {
            alerteHumiditeInterieure.setText("Humidité intérieure trop élevée !");
            alerteHumiditeInterieure.setTextColor(Color.parseColor("#ff0000"));
            rucheHumiditeInterieureHaute.setVisibility(View.VISIBLE);
            rucheHumiditeInterieureBasse.setVisibility(View.INVISIBLE);
            rucheHumiditeInterieureNormale.setVisibility(View.INVISIBLE);
        }
        else if(rucheSelectionnee.getMesureRuche().getHumiditeInterieure() < rucheSelectionnee.getAlerteRuche().getHumiditeInterieurMin())
        {
            alerteHumiditeInterieure.setText("Humidité intérieure trop basse !");
            alerteHumiditeInterieure.setTextColor(Color.parseColor("#498df7"));
            rucheHumiditeInterieureHaute.setVisibility(View.INVISIBLE);
            rucheHumiditeInterieureBasse.setVisibility(View.VISIBLE);
            rucheHumiditeInterieureNormale.setVisibility(View.INVISIBLE);
        }
        else
        {
            alerteHumiditeInterieure.setText("");
            rucheHumiditeInterieureHaute.setVisibility(View.INVISIBLE);
            rucheHumiditeInterieureBasse.setVisibility(View.INVISIBLE);
            rucheHumiditeInterieureNormale.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @brief Méthode pour afficher l'alerte de l'humidité extérieure
     */
    public void afficherAlerteHumiditeExterieure()
    {
        if(rucheSelectionnee.getMesureRuche().getHumiditeExterieure() > rucheSelectionnee.getAlerteRuche().getHumiditeExterieurMax())
        {
            alerteHumiditeExterieure.setText("Humidité extérieure trop élevée !");
            alerteHumiditeExterieure.setTextColor(Color.parseColor("#ff0000"));
            rucheHumiditeExterieureHaute.setVisibility(View.VISIBLE);
            rucheHumiditeExterieureBasse.setVisibility(View.INVISIBLE);
            rucheHumiditeExterieureNormale.setVisibility(View.INVISIBLE);
        }
        else if(rucheSelectionnee.getMesureRuche().getHumiditeExterieure() < rucheSelectionnee.getAlerteRuche().getHumiditeExterieurMin())
        {
            alerteHumiditeExterieure.setText("Humidité extérieure trop basse !");
            alerteHumiditeExterieure.setTextColor(Color.parseColor("#498df7"));
            rucheHumiditeExterieureHaute.setVisibility(View.INVISIBLE);
            rucheHumiditeExterieureBasse.setVisibility(View.VISIBLE);
            rucheHumiditeExterieureNormale.setVisibility(View.INVISIBLE);
        }
        else
        {
            alerteHumiditeExterieure.setText("");
            rucheHumiditeExterieureHaute.setVisibility(View.INVISIBLE);
            rucheHumiditeExterieureBasse.setVisibility(View.INVISIBLE);
            rucheHumiditeExterieureNormale.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @brief Méthode pour afficher l'alerte de poids
     */
    public void afficherAlertePoids()
    {
        if(rucheSelectionnee.getMesureRuche().getPoids() > rucheSelectionnee.getAlerteRuche().getPoidsMax())
        {
            alertePoids.setText("Poids trop haut !");
            alertePoids.setTextColor(Color.parseColor("#ff0000"));
            ruchePoidsHaute.setVisibility(View.VISIBLE);
            ruchePoidsBas.setVisibility(View.INVISIBLE);
            ruchePoidsNormale.setVisibility(View.INVISIBLE);

        }
        else if(rucheSelectionnee.getMesureRuche().getPoids() < rucheSelectionnee.getAlerteRuche().getPoidsMin())
        {
            alertePoids.setText("Poids trop bas !");
            alertePoids.setTextColor(Color.parseColor("#498df7"));
            ruchePoidsHaute.setVisibility(View.INVISIBLE);
            ruchePoidsBas.setVisibility(View.VISIBLE);
            ruchePoidsNormale.setVisibility(View.INVISIBLE);
        }
        else
        {
            alertePoids.setText("");
            ruchePoidsHaute.setVisibility(View.INVISIBLE);
            ruchePoidsBas.setVisibility(View.INVISIBLE);
            ruchePoidsNormale.setVisibility(View.VISIBLE);
        }
    }

    public void afficherAlertePression()
    {
        if(rucheSelectionnee.getMesureRuche().getPression() > rucheSelectionnee.getAlerteRuche().getPressionMax())
        {
            alertePression.setText("Pression trop haute !");
            alertePression.setTextColor(Color.parseColor("#ff0000"));
            ruchePressionHaute.setVisibility(View.VISIBLE);
            ruchePressionBasse.setVisibility(View.INVISIBLE);
            ruchePressionNormale.setVisibility(View.INVISIBLE);

        }
        else if(rucheSelectionnee.getMesureRuche().getPression() < rucheSelectionnee.getAlerteRuche().getPressionMin())
        {
            alertePression.setText("Pression trop basse !");
            alertePression.setTextColor(Color.parseColor("#498df7"));
            ruchePressionHaute.setVisibility(View.INVISIBLE);
            ruchePressionBasse.setVisibility(View.VISIBLE);
            ruchePressionNormale.setVisibility(View.INVISIBLE);
        }
        else
        {
            alertePression.setText("");
            ruchePressionHaute.setVisibility(View.INVISIBLE);
            ruchePressionBasse.setVisibility(View.INVISIBLE);
            ruchePressionNormale.setVisibility(View.VISIBLE);
        }
    }
}
