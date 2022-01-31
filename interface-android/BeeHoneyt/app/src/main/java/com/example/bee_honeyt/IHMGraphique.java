package com.example.bee_honeyt;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;

/**
 * @class IHMGraphique
 * @brief L'activité pour afficher les graphiques
 * @author Thierry Vaira
 * @author FOUCAULT Clémentine
 * $LastChangedRevision$
 * $LastChangedDate$
 */

/**
 * @class IHMGraphique
 * @brief Classe pour gérer les graphiques
 */
public class IHMGraphique extends AppCompatActivity
{
    /**
     * Constantes
     */
    private static final String TAG = "_IHMGraphique"; //!< TAG pour les logs
    private static final int CHOIX_GRAPHE_POIDS = 0;
    private static final int CHOIX_GRAPHE_TEMPERATURE_INTERIEURE = 1;
    private static final int CHOIX_GRAPHE_HUMIDITE_INTERIEURE = 2;
    private static final int CHOIX_GRAPHE_TEMPERATURE_EXTERIEURE = 3;
    private static final int CHOIX_GRAPHE_HUMIDITE_EXTERIEURE = 4;
    private static final int CHOIX_GRAPHE_PRESSION = 5;

    private static final int MAX_TEMPERATURE_INTERIEURE = 50; //!< En °C
    private static final int MIN_TEMPERATURE_INTERIEURE = 0; //!< En °C
    private static final int MAX_HUMIDITE_INTERIEURE = 100; //!< En %
    private static final int MIN_HUMIDITE_INTERIEURE = 0; //!< En %
    private static final int MAX_TEMPERATURE_EXTERIEURE = 50; //!< En °C
    private static final int MIN_TEMPERATURE_EXTERIEURE = 0; //!< En °C
    private static final int MAX_HUMIDITE_EXTERIEURE = 100; //!< En %
    private static final int MIN_HUMIDITE_EXTERIEURE = 0; //!< En %
    private static final int MAX_PRESSION = 1200; //!< En hPA
    private static final int MIN_PRESSION = 900; //!< En hPA
    private static final int MAX_POIDS = 50; //!< En kg
    private static final int MIN_POIDS = 0; //!< En kg

    /**
     * Attributs
     */
    private Historique historique; //!< Relation avec la classe Historique
    private String contenuHistorique; //!< Le contenu récupéré de l'historique
    private String applicationID; //!< L'applicationID pour l'historique Data Storage
    private String key; //!< La clé d'accès pour l'historique Data Storage
    private String deviceID; //!< Le deviceId de la ruche
    private int choixGraphique = CHOIX_GRAPHE_POIDS; //!< Le numéro de graphique à afficher (par défaut)
    // Pour le poids
    private Vector<List<String>> donneesPoids; //!< Toutes les mesures récupérées dans l'historique pour le poids (dernières 24h)
    private List<Double> mesuresPoids; //!< Les mesures moyennées par heure pour le poids
    private Double poidsCourant = new Double(0.);
    private Double poidsMin = new Double((double)MAX_POIDS);
    private Double poidsMoyenne = new Double(0.);
    private Double poidsMax = new Double((double)MIN_POIDS);
    // Pour la pression
    private Vector<List<String>> donneesPression; //!< Toutes les mesures récupérées dans l'historique pour la pression (dernières 24h)
    private List<Double> mesuresPression; //!< Les mesures moyennées par heure pour le pression
    private Double pressionCourant = new Double(0.);
    private Double pressionMin = new Double((double)MAX_PRESSION);
    private Double pressionMoyenne = new Double(0.);
    private Double pressionMax = new Double((double)MIN_PRESSION);
    //Pour la température intérieure
    private Vector<List<String>> donneeTemperatureInterieure; //!< Toutes les mesures récupérées dans l'historique pour la température intérieure (dernières 24h)
    private List<Double> mesuresTemperatureInterieure; //!< Les mesures moyennées par heure pour la température intérieure
    private Double temperatureInterieureCourante = new Double(0.);
    private Double temperatureInterieureMin = new Double((double)MIN_TEMPERATURE_INTERIEURE);
    private Double temperatureInterieureMoyenne = new Double(0.);
    private Double temperatureInterieureMax = new Double((double)MAX_TEMPERATURE_INTERIEURE);
    //Pour l'humidité intérieure
    private Vector<List<String>> donneeHumiditeInterieure; //!< Toutes les mesures récupérées dans l'historique pour l'humidite intérieure (dernières 24h)
    private List<Double> mesuresHumiditeInterieure; //!< Les mesures moyennées par heure pour l'humidite intérieure
    private Double humiditeInterieureCourante = new Double(0.);
    private Double humiditeInterieureMin = new Double((double)MIN_HUMIDITE_INTERIEURE);
    private Double humiditeInterieureMoyenne = new Double(0.);
    private Double humiditeInterieureMax = new Double((double)MAX_HUMIDITE_INTERIEURE);
    //Pour la température extérieure
    private Vector<List<String>> donneeTemperatureExterieure; //!< Toutes les mesures récupérées dans l'historique pour la température extérieure (dernières 24h)
    private List<Double> mesuresTemperatureExterireure; //!< Les mesures moyennées par heure pour la température extérieure
    private Double temperatureExterireureCourante = new Double(0.);
    private Double temperatureExterireureMin = new Double((double)MIN_TEMPERATURE_EXTERIEURE);
    private Double temperatureExterireureMoyenne = new Double(0.);
    private Double temperatureExterireureMax = new Double((double)MAX_TEMPERATURE_EXTERIEURE);
    //Pour l'humidité extérireure
    private Vector<List<String>> donneeHumiditeExterireure; //!< Toutes les mesures récupérées dans l'historique pour l'humidite extérieure (dernières 24h)
    private List<Double> mesuresHumiditeExterireure; //!< Les mesures moyennées par heure pour l'humidite extérieure
    private Double humiditeExterireureCourante = new Double(0.);
    private Double humiditeExterireureMin = new Double((double)MIN_HUMIDITE_EXTERIEURE);
    private Double humiditeExterireureMoyenne = new Double(0.);
    private Double humiditeExterireureMax = new Double((double)MAX_HUMIDITE_EXTERIEURE);


    /**
     * Les éléments graphiques de l'IHM
     */
    private GraphView graphique;
    private LineGraphSeries<DataPoint> seriesPoids;
    private LineGraphSeries<DataPoint> seriesPression;
    private LineGraphSeries<DataPoint> seriesTemperatureInterieure;
    private LineGraphSeries<DataPoint> seriesHumiditeInterieure;
    private LineGraphSeries<DataPoint> seriesTemperatureExterieure;
    private LineGraphSeries<DataPoint> seriesHumiditeExterieure;

    private Button boutonTempInt;
    private Button boutonTempExt;
    private Button boutonHumInt;
    private Button boutonHumExt;
    private Button boutonPoids;
    private Button boutonPression;
    private TextView valeurCourante;
    private TextView donneesMin;
    private TextView donneesMoyenne;
    private TextView donneesMax;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ihmgraphique);
        Log.d(TAG,"onCreate()");

        getSupportActionBar().setTitle(getString(R.string.app_name) + " v" + BuildConfig.VERSION_NAME);

        Intent intent = getIntent();
        applicationID = intent.getStringExtra("application");
        key = intent.getStringExtra("key");
        deviceID = intent.getStringExtra("device");

        initialiserHistorique();

        initialiserDonnees();

        initialiserIHM();

        initialiserGraphique();

        chargerHistorique();
    }

    /**
     * @brief Initialiser l'historique
     */
    private void initialiserHistorique()
    {
        historique = new Historique();
        historique.setCallback(new HistoriqueEventListener()
        {
            // Déclenchée sur évènement chargement
            public void onLoad(String contenuJSON)
            {
                //Log.d(TAG, "onLoad() contenuJSON = " + contenuJSON);
                contenuHistorique = contenuJSON;

                switch(choixGraphique)
                {
                    case CHOIX_GRAPHE_POIDS:
                        extraireDonneesPoids();
                        break;
                    case CHOIX_GRAPHE_TEMPERATURE_INTERIEURE:
                        extraireDonneesTemperatureInterieure();
                        break;
                    case CHOIX_GRAPHE_HUMIDITE_INTERIEURE:
                        extraireDonneesHumiditeInterieure();
                        break;
                    case CHOIX_GRAPHE_TEMPERATURE_EXTERIEURE:
                        extraireDonneesTemperatureExterieure();
                        break;
                    case CHOIX_GRAPHE_HUMIDITE_EXTERIEURE:
                        extraireDonneesHumiditeExterieure();
                        break;
                    case CHOIX_GRAPHE_PRESSION:
                        extraireDonneesPression();
                        break;
                }
            }

            // Déclenchée sur évènement erreur de chargement
            public void onError()
            {
                Log.d(TAG, "onError()");
            }
        });
    }

    /**
     * @brief Charge l'historique pour une ruche
     */
    private void chargerHistorique()
    {
        String url = "https://" + applicationID + ".data.thethingsnetwork.org";
        Date date = new Date();
        historique.charger(url, key, deviceID, date.getHours() + "h"); // Sur la journée seulement
        Toast.makeText(IHMGraphique.this,"Chargement des données de l'historique", Toast.LENGTH_SHORT).show();
    }

    /**
     * @brief Initialiser les conteneurs
     */
    private void initialiserDonnees()
    {
        donneesPoids = new Vector<List<String>>();
        mesuresPoids = new ArrayList<Double>(24); // pour 24 h

        donneeTemperatureInterieure = new Vector<List<String>>();
        mesuresTemperatureInterieure = new ArrayList<Double>(24);

        donneeHumiditeInterieure = new Vector<List<String>>();
        mesuresHumiditeInterieure = new ArrayList<Double>(24);

        donneeTemperatureExterieure = new Vector<List<String>>();
        mesuresTemperatureExterireure = new ArrayList<Double>(24);

        donneeHumiditeExterireure = new Vector<List<String>>();
        mesuresHumiditeExterireure = new ArrayList<Double>(24);

        donneesPression = new Vector<List<String>>();
        mesuresPression = new ArrayList<Double>(24);
    }

    /**
     * @brief Initialiser les ressources de l'IHM
     */
    private void initialiserIHM()
    {
        graphique = (GraphView) findViewById(R.id.graphique);

        boutonHumExt = findViewById(R.id.boutonHumExt);
        boutonHumInt = findViewById(R.id.boutonHumInt);
        boutonTempExt = findViewById(R.id.boutonTempExt);
        boutonTempInt = findViewById(R.id.boutonTempInt);
        boutonPoids = findViewById(R.id.boutonPoids);
        boutonPression = findViewById(R.id.boutonPression);

        valeurCourante = findViewById(R.id.valeurCourante);
        donneesMin = findViewById(R.id.donneesMin);
        donneesMoyenne = findViewById(R.id.donneesMoyenne);
        donneesMax = findViewById(R.id.donneesMax);

        boutonPoids.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                graphique.removeAllSeries();
                graphique.getViewport().setMaxY(MAX_POIDS);
                graphique.getViewport().setMinY(0);
                graphique.getGridLabelRenderer().setNumVerticalLabels((MAX_POIDS/10)+1);
                changerChoix(CHOIX_GRAPHE_POIDS);

                chargerHistorique();
            }
        });

        boutonTempInt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                graphique.removeAllSeries();
                graphique.getViewport().setMaxY(MAX_TEMPERATURE_INTERIEURE);
                graphique.getViewport().setMinY(0);
                graphique.getGridLabelRenderer().setNumVerticalLabels((MAX_TEMPERATURE_INTERIEURE/10)+1);
                changerChoix(CHOIX_GRAPHE_TEMPERATURE_INTERIEURE);

                chargerHistorique();
            }
        });

        boutonHumInt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                graphique.removeAllSeries();
                graphique.getViewport().setMaxY(MAX_HUMIDITE_INTERIEURE);
                graphique.getViewport().setMinY(0);
                graphique.getGridLabelRenderer().setNumVerticalLabels((MAX_HUMIDITE_INTERIEURE/10)+1);
                changerChoix(CHOIX_GRAPHE_HUMIDITE_INTERIEURE);

                chargerHistorique();
            }
        });

        boutonTempExt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                graphique.removeAllSeries();
                graphique.getViewport().setMaxY(MAX_TEMPERATURE_EXTERIEURE);
                graphique.getViewport().setMinY(0);
                graphique.getGridLabelRenderer().setNumVerticalLabels((MAX_TEMPERATURE_EXTERIEURE/10)+1);
                changerChoix(CHOIX_GRAPHE_TEMPERATURE_EXTERIEURE);

                chargerHistorique();
            }
        });

        boutonHumExt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                graphique.removeAllSeries();
                graphique.getViewport().setMaxY(MAX_HUMIDITE_EXTERIEURE);
                graphique.getViewport().setMinY(0);
                graphique.getGridLabelRenderer().setNumVerticalLabels((MAX_HUMIDITE_EXTERIEURE/10)+1);
                changerChoix(CHOIX_GRAPHE_HUMIDITE_EXTERIEURE);

                chargerHistorique();
            }
        });

        boutonPression.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                graphique.removeAllSeries();
                graphique.getViewport().setMaxY(MAX_PRESSION);
                graphique.getViewport().setMinY(900);
                graphique.getGridLabelRenderer().setNumVerticalLabels((MAX_PRESSION/100)+1);
                changerChoix(CHOIX_GRAPHE_PRESSION);

                chargerHistorique();
            }
        });
    }

    /**
     * @brief Change le choix de graphique
     */
    private void changerChoix(int choix)
    {
        if(choix == choixGraphique)
            return;
        deselectionnerChoix(choixGraphique);
        selectionnerChoix(choix);
        choixGraphique = choix;
    }

    /**
     * @brief Sélectionne la couleur de fond du choix actif
     */
    private void selectionnerChoix(int choix)
    {
        switch(choix)
        {
            case CHOIX_GRAPHE_POIDS:
                boutonPoids.setBackgroundResource(R.color.selection_on);
                break;
            case CHOIX_GRAPHE_TEMPERATURE_INTERIEURE:
                boutonTempInt.setBackgroundResource(R.color.selection_on);
                break;
            case CHOIX_GRAPHE_HUMIDITE_INTERIEURE:
                boutonHumInt.setBackgroundResource(R.color.selection_on);
                break;
            case CHOIX_GRAPHE_HUMIDITE_EXTERIEURE:
                boutonHumExt.setBackgroundResource(R.color.selection_on);
                break;
            case CHOIX_GRAPHE_TEMPERATURE_EXTERIEURE:
                boutonTempExt.setBackgroundResource(R.color.selection_on);
                break;
            case CHOIX_GRAPHE_PRESSION:
                boutonPression.setBackgroundResource(R.color.selection_on);
                break;
        }
    }

    /**
     * @brief Désélectionne la couleur de fond de l'ancien choix
     */
    private void deselectionnerChoix(int choix)
    {
        switch(choix)
        {
            case CHOIX_GRAPHE_POIDS:
                boutonPoids.setBackgroundResource(R.color.selection_off);
                break;
            case CHOIX_GRAPHE_TEMPERATURE_INTERIEURE:
                boutonTempInt.setBackgroundResource(R.color.selection_off);
                break;
            case CHOIX_GRAPHE_HUMIDITE_INTERIEURE:
                boutonHumInt.setBackgroundResource(R.color.selection_off);
                break;
            case CHOIX_GRAPHE_TEMPERATURE_EXTERIEURE:
                boutonTempExt.setBackgroundResource(R.color.selection_off);
                break;
            case CHOIX_GRAPHE_HUMIDITE_EXTERIEURE:
                boutonHumExt.setBackgroundResource(R.color.selection_off);
                break;
            case CHOIX_GRAPHE_PRESSION:
                boutonPression.setBackgroundResource(R.color.selection_off);
                break;
        }
    }

    /**
     * @brief Initialiser l'affichage du graphique
     */
    private void initialiserGraphique()
    {
        // Axe Y
        graphique.getViewport().setMinY(0);
        graphique.getViewport().setMaxY(MAX_POIDS); // par défaut
        graphique.getViewport().setYAxisBoundsManual(true);

        // Axe X (24 heures)
        graphique.getViewport().setMinX(0);
        graphique.getViewport().setMaxX(23);
        graphique.getViewport().setXAxisBoundsManual(true);

        // Grille
        graphique.getGridLabelRenderer().setLabelFormatter(new TimeAsXAxisLabelFormatter("H'h'")); // 0h 1h 2h ...
        graphique.getGridLabelRenderer().setNumHorizontalLabels(12); // nb d'heures affichées sur l'axe X
        graphique.getGridLabelRenderer().setNumVerticalLabels((MAX_POIDS/10)+1); // par défaut
        graphique.getGridLabelRenderer().setGridColor(Color.BLACK);
        graphique.getGridLabelRenderer().setVerticalLabelsColor(Color.BLACK);
        graphique.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLACK);
        graphique.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);

        // Légende
        graphique.getLegendRenderer().setVisible(true);
        //graphique.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graphique.getLegendRenderer().setFixedPosition(1,48);
        graphique.getLegendRenderer().setBackgroundColor(0);

        // Contenu
        graphique.removeAllSeries();
    }

    /**
     * @brief Extraire les données de poids de l'historique
     */
    private void extraireDonneesPoids()
    {
        // Obligation d'utiliser un Thread
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    JSONArray tableauJSON = new JSONArray(contenuHistorique);
                    JSONObject valeur = null;
                    Date date = null;
                    SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    SimpleDateFormat sdfOutHeure = new SimpleDateFormat("HH", Locale.FRENCH);

                    Log.d(TAG, "extraireDonnees() Nb mesures récupérées = " + tableauJSON.length());
                    //Toast.makeText(IHMGraphique.this,tableauJSON.length() + " données récupérées", Toast.LENGTH_SHORT).show();

                    donneesPoids.clear();
                    for(int i = 0; i < tableauJSON.length(); i++)
                    {
                        valeur = tableauJSON.getJSONObject(i);
                        //Log.d(TAG, "extraireDonnees() valeur = " + valeur);
                        /* {
                                "device_id":"ruche-1-sim",
                                "humiditeExt":null,
                                "humiditeInt":null,
                                "poids":22.4,
                                "pression":null,
                                "temperatureExt":null,
                                "temperatureInt":null,
                                "time":"2021-05-26T08:26:53.789498119Z"
                            }
                         */
                        if(valeur.getString("device_id").equals(deviceID))
                        {
                            if(!valeur.isNull("poids"))
                            {
                                Double poids = valeur.getDouble("poids");
                                String time = valeur.getString("time");
                                //Log.d(TAG, "extraireDonnees() poids = " + poids);
                                //Log.d(TAG, "extraireDonnees() time = " + time);
                                sdfIn.setTimeZone(TimeZone.getTimeZone("UTC"));
                                String heure = "";
                                try
                                {
                                    date = sdfIn.parse(time);
                                    sdfOutHeure.setTimeZone(TimeZone.getDefault());
                                    heure = sdfOutHeure.format(date);
                                }
                                catch (ParseException e)
                                {
                                    e.printStackTrace();
                                    Log.d(TAG, "extraireDonnees() Erreur !");
                                }
                                List<String> donnees = new ArrayList<>();
                                donnees.add(heure);
                                donnees.add(poids.toString());
                                donneesPoids.add(donnees);
                                //Log.d(TAG, "extraireDonnees() heure = " + heure + " - poids = " + poids + " [" + time + "]");
                                //Log.d(TAG, "extraireDonnees() heure = " + heure + " - poids = " + poids);
                                //Log.d(TAG, "extraireDonnees() donnees = " + donnees);
                            }
                        }
                    }
                    //Log.d(TAG,"extraireDonnees() donneesPoids = " + donneesPoids);
                    //Log.d(TAG,"extraireDonnees() Nb mesures poids = " + donneesPoids.size());
                    Toast.makeText(IHMGraphique.this,donneesPoids.size() + " données de poids récupérées", Toast.LENGTH_SHORT).show();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                traiterDonneesPoids();

                afficherDonneesPoids();
            }
        });
    }

    /**
     * @brief Traiter les donnes de poids pour l'affichage de graphique
     */
    private void traiterDonneesPoids()
    {
        double somme24h = 0.;
        double somme = 0.;
        int nbMesures24h = 0;
        int nbMesures = 0;

        /*if(donneesPoids.size() < 1)
            return;*/

        /*poidsMin = new Double((double)Double.parseDouble(donneesPoids.get(0).get(1)));
        poidsMax = new Double((double)Double.parseDouble(donneesPoids.get(0).get(1)));*/

        //Log.d(TAG,"traiterDonneesPoids() Nb mesures poids = " + donneesPoids.size());
        mesuresPoids.clear();
        boolean initMinMax = false;
        Date date = new Date();
        for(int heure = 0; heure < date.getHours(); heure++)
        {
            somme = 0.;
            nbMesures = 0;
            for (List<String> donnees : donneesPoids)
            {
                if(Integer.parseInt(donnees.get(0)) == heure)
                {
                    //Log.d(TAG, "traiterDonneesPoids() heure = " + donnees.get(0) + " - poids = " + donnees.get(1));
                    somme += Double.parseDouble(donnees.get(1));
                    nbMesures++;
                }
            }

            if(nbMesures > 0)
            {
                //Log.d(TAG,"traiterDonneesPoids() heure = " + heure + " - Nb mesures = " + nbMesures + " - somme = " + somme + " - moyenne = " + (somme/(double)nbMesures));
                double valeur = (somme/(double)nbMesures);
                mesuresPoids.add(heure, valeur);
                // Première mesure -> initialise les min/max
                if(!initMinMax)
                {
                    poidsMax = valeur;
                    poidsMin = valeur;
                    initMinMax = true;
                }
                somme24h += valeur;
                nbMesures24h++;
                poidsCourant = valeur;
                if(valeur > poidsMax)
                    poidsMax = valeur;
                if(valeur < poidsMin)
                    poidsMin = valeur;
            }
            else
            {
                mesuresPoids.add(heure, 0.);
            }
        }

        poidsMoyenne = (somme24h/(double)nbMesures24h);
        // Ce poidsCourant peut être supérieur à poidsMax (moyenne sur une heure)
        //poidsCourant = Double.parseDouble(donneesPoids.get(donneesPoids.size()-1).get(1));
    }

    /**
     * @brief Afficher les données de poids dans le graphique
     */
    private void afficherDonneesPoids()
    {
        DataPoint[] dataPoints = new DataPoint[mesuresPoids.size()];
        for (int i = 0; i < mesuresPoids.size(); i++)
        {
            dataPoints[i] = new DataPoint(i, mesuresPoids.get(i));
        }

        seriesPoids = new LineGraphSeries<DataPoint>(dataPoints);

        graphique.addSeries(seriesPoids);
        seriesPoids.setColor(Color.GREEN);
        seriesPoids.setTitle("Kg");

        valeurCourante.setText("Dernière valeur : " + String.valueOf(arrondir(poidsCourant, 2)) + " Kg");
        donneesMin.setText(String.valueOf(arrondir(poidsMin, 1)) + " Kg");
        donneesMax.setText(String.valueOf(arrondir(poidsMax, 1)) + " Kg");
        donneesMoyenne.setText(String.valueOf(arrondir(poidsMoyenne, 1)) + " Kg");
    }

    /**
     * @brief Extraire les données de la pression de l'historique
     */
    private void extraireDonneesPression()
    {
        // Obligation d'utiliser un Thread
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    JSONArray tableauJSON = new JSONArray(contenuHistorique);
                    JSONObject valeur = null;
                    Date date = null;
                    SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    SimpleDateFormat sdfOutHeure = new SimpleDateFormat("HH", Locale.FRENCH);

                    Log.d(TAG, "extraireDonnees() Nb mesures récupérées = " + tableauJSON.length());
                    //Toast.makeText(IHMGraphique.this,tableauJSON.length() + " données récupérées", Toast.LENGTH_SHORT).show();

                    donneesPression.clear();
                    for(int i = 0; i < tableauJSON.length(); i++)
                    {
                        valeur = tableauJSON.getJSONObject(i);
                        //Log.d(TAG, "extraireDonnees() valeur = " + valeur);
                        /* {
                                "device_id":"ruche-1-sim",
                                "humiditeExt":null,
                                "humiditeInt":null,
                                "poids":22.4,
                                "pression":null,
                                "temperatureExt":null,
                                "temperatureInt":null,
                                "time":"2021-05-26T08:26:53.789498119Z"
                            }
                         */
                        if(valeur.getString("device_id").equals(deviceID))
                        {
                            if(!valeur.isNull("pression"))
                            {
                                Double pression = valeur.getDouble("pression");
                                String time = valeur.getString("time");
                                //Log.d(TAG, "extraireDonnees() pression = " + pression);
                                //Log.d(TAG, "extraireDonnees() time = " + time);
                                sdfIn.setTimeZone(TimeZone.getTimeZone("UTC"));
                                String heure = "";
                                try
                                {
                                    date = sdfIn.parse(time);
                                    sdfOutHeure.setTimeZone(TimeZone.getDefault());
                                    heure = sdfOutHeure.format(date);
                                }
                                catch (ParseException e)
                                {
                                    e.printStackTrace();
                                    Log.d(TAG, "extraireDonnees() Erreur !");
                                }
                                List<String> donnees = new ArrayList<>();
                                donnees.add(heure);
                                donnees.add(pression.toString());
                                donneesPression.add(donnees);
                                //Log.d(TAG, "extraireDonnees() heure = " + heure + " - pression = " + pression + " [" + time + "]");
                                //Log.d(TAG, "extraireDonnees() heure = " + heure + " - pression = " + pression);
                                //Log.d(TAG, "extraireDonnees() donnees = " + donnees);
                            }
                        }
                    }
                    //Log.d(TAG,"extraireDonnees() donneesPression = " + donneesPression);
                    //Log.d(TAG,"extraireDonnees() Nb mesures pression = " + donneesPression.size());
                    Toast.makeText(IHMGraphique.this,donneesPression.size() + " données de la pression récupérées", Toast.LENGTH_SHORT).show();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                traiterDonneesPression();

                afficherDonneesPression();
            }
        });
    }

    /**
     * @brief Traiter les donnes de la pression pour l'affichage de graphique
     */
    private void traiterDonneesPression()
    {
        double somme24h = 0.;
        double somme = 0.;
        int nbMesures24h = 0;
        int nbMesures = 0;

        /*if(donneesPression.size() < 1)
            return;*/

        /*pressionMin = new Double((double)Double.parseDouble(donneesPression.get(0).get(1)));
        pressionMax = new Double((double)Double.parseDouble(donneesPression.get(0).get(1)));*/

        //Log.d(TAG,"traiterDonneesPression() Nb mesures pression = " + donneesPression.size());
        mesuresPression.clear();
        boolean initMinMax = false;
        Date date = new Date();
        for(int heure = 0; heure < date.getHours(); heure++)
        {
            somme = 0.;
            nbMesures = 0;
            for (List<String> donnees : donneesPression)
            {
                if(Integer.parseInt(donnees.get(0)) == heure)
                {
                    //Log.d(TAG, "traiterDonneesPression() heure = " + donnees.get(0) + " - pression = " + donnees.get(1));
                    somme += Double.parseDouble(donnees.get(1));
                    nbMesures++;
                }
            }

            if(nbMesures > 0)
            {
                //Log.d(TAG,"traiterDonneesPression() heure = " + heure + " - Nb mesures = " + nbMesures + " - somme = " + somme + " - moyenne = " + (somme/(double)nbMesures));
                double valeur = (somme/(double)nbMesures);
                mesuresPression.add(heure, valeur);
                // Première mesure -> initialise les min/max
                if(!initMinMax)
                {
                    pressionMax = valeur;
                    pressionMin = valeur;
                    initMinMax = true;
                }
                somme24h += valeur;
                nbMesures24h++;
                pressionCourant = valeur;
                if(valeur > pressionMax)
                    pressionMax = valeur;
                if(valeur < pressionMin)
                    pressionMin = valeur;
            }
            else
            {
                mesuresPression.add(heure, 0.);
            }
        }

        pressionMoyenne = (somme24h/(double)nbMesures24h);
        // Ce pressionCourant peut être supérieur à pressionMax (moyenne sur une heure)
    }

    /**
     * @brief Afficher les données de la pression dans le graphique
     */
    private void afficherDonneesPression()
    {
        DataPoint[] dataPoints = new DataPoint[mesuresPression.size()];
        for (int i = 0; i < mesuresPression.size(); i++)
        {
            dataPoints[i] = new DataPoint(i, mesuresPression.get(i));
        }

        seriesPression = new LineGraphSeries<DataPoint>(dataPoints);

        graphique.addSeries(seriesPression);
        seriesPression.setColor(Color.rgb(217,87,255));
        seriesPression.setTitle("hPA");

        valeurCourante.setText("Dernière valeur : " + String.valueOf(arrondir(poidsCourant, 2)) + " hPA");
        donneesMin.setText(String.valueOf(arrondir(pressionMin, 1)) + " hPA");
        donneesMax.setText(String.valueOf(arrondir(pressionMax, 1)) + " hPA");
        donneesMoyenne.setText(String.valueOf(arrondir(pressionMoyenne, 1)) + " hPA");
    }

    /**
     * @brief Extraire les données de température intérieure de l'historique
     */
    private void extraireDonneesTemperatureInterieure()
    {
        // Obligation d'utiliser un Thread
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    JSONArray tableauJSON = new JSONArray(contenuHistorique);
                    JSONObject valeur = null;
                    Date date = null;
                    SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    SimpleDateFormat sdfOutHeure = new SimpleDateFormat("HH", Locale.FRENCH);

                    Log.d(TAG, "extraireDonneesTemperatureInterieure() Nb mesures récupérées = " + tableauJSON.length());
                    //Toast.makeText(IHMGraphique.this,tableauJSON.length() + " données récupérées", Toast.LENGTH_SHORT).show();

                    donneeTemperatureInterieure.clear();
                    for(int i = 0; i < tableauJSON.length(); i++)
                    {
                        valeur = tableauJSON.getJSONObject(i);
                        //Log.d(TAG, "extraireDonneesTemperatureInterieure() valeur = " + valeur);
                        /* {
                                "device_id":"ruche-1-sim",
                                "humiditeExt":null,
                                "humiditeInt":null,
                                "poids":null,
                                "pression":null,
                                "temperatureExt":null,
                                "temperatureInt":24.4,
                                "time":"2021-05-26T08:26:53.789498119Z"
                            }
                         */
                        if(valeur.getString("device_id").equals(deviceID))
                        {
                            if(!valeur.isNull("temperatureInt"))
                            {
                                Double temperatureInterieure = valeur.getDouble("temperatureInt");
                                String time = valeur.getString("time");
                                //Log.d(TAG, "extraireDonneesTemperatureInterieure() température intérieure = " + temperatureInterieure);
                                //Log.d(TAG, "extraireDonneesTemperatureInterieure() time = " + time);
                                sdfIn.setTimeZone(TimeZone.getTimeZone("UTC"));
                                String heure = "";
                                try
                                {
                                    date = sdfIn.parse(time);
                                    sdfOutHeure.setTimeZone(TimeZone.getDefault());
                                    heure = sdfOutHeure.format(date);
                                }
                                catch (ParseException e)
                                {
                                    e.printStackTrace();
                                    Log.d(TAG, "extraireDonneesTemperatureInterieure() Erreur !");
                                }
                                List<String> donnees = new ArrayList<>();
                                donnees.add(heure);
                                donnees.add(temperatureInterieure.toString());
                                donneeTemperatureInterieure.add(donnees);
                                //Log.d(TAG, "extraireDonnees() heure = " + heure + " - temperatureInterieure = " + temperatureInterieure + " [" + time + "]");
                                //Log.d(TAG, "extraireDonnees() heure = " + heure + " - temperatureInterieure = " + temperatureInterieure);
                                //Log.d(TAG, "extraireDonnees() donnees = " + donnees);
                            }
                        }
                    }
                    //Log.d(TAG,"extraireDonneesTemperatureInterieure() donneeTemperatureInterieure = " + donneeTemperatureInterieure);
                    //Log.d(TAG,"extraireDonneesTemperatureInterieure() Nb mesures température interieure = " + donneeTemperatureInterieure.size());
                    Toast.makeText(IHMGraphique.this,donneeTemperatureInterieure.size() + " données de temperature intérireure récupérées", Toast.LENGTH_SHORT).show();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                traiterDonneesTempetatureInteireure();

                afficherDonneesTemperatureInterieure();
            }
        });
    }

    /**
     * @brief Traiter les donnes de température interieure pour l'affichage de graphique
     */
    private void traiterDonneesTempetatureInteireure()
    {
        double somme24h = 0.;
        double somme = 0.;
        int nbMesures24h = 0;
        int nbMesures = 0;

        /*if(donneeTemperatureInterieure.size() < 1)
            return;*/

        //Log.d(TAG,"traiterDonneesTempetatureInteireure() Nb mesures temperature interieure = " + donneeTemperatureInterieure.size());
        mesuresTemperatureInterieure.clear();
        boolean initMinMax = false;
        Date date = new Date();
        for(int heure = 0; heure < date.getHours(); heure++)
        {
            somme = 0.;
            nbMesures = 0;
            for (List<String> donnees : donneeTemperatureInterieure)
            {
                if(Integer.parseInt(donnees.get(0)) == heure)
                {
                    //Log.d(TAG, "traiterDonneesTempetatureInteireure() heure = " + donnees.get(0) + " - temperatureInterieure = " + donnees.get(1));
                    somme += Double.parseDouble(donnees.get(1));
                    nbMesures++;
                }
            }

            if(nbMesures > 0)
            {
                //Log.d(TAG,"traiterDonneesTempetatureInteireure() heure = " + heure + " - Nb mesures = " + nbMesures + " - somme = " + somme + " - moyenne = " + (somme/(double)nbMesures));
                double valeur = (somme/(double)nbMesures);
                mesuresTemperatureInterieure.add(heure, valeur);
                // Première mesure -> initialise les min/max
                if(!initMinMax)
                {
                    temperatureInterieureMax = valeur;
                    temperatureInterieureMin = valeur;
                    initMinMax = true;
                }
                somme24h += valeur;
                nbMesures24h++;
                temperatureInterieureCourante = valeur;
                if(valeur > temperatureInterieureMax)
                    temperatureInterieureMax = valeur;
                if(valeur < temperatureInterieureMin)
                    temperatureInterieureMin = valeur;
            }
            else
            {
                mesuresTemperatureInterieure.add(heure, 0.);
            }
        }

        temperatureInterieureMoyenne = (somme24h/(double)nbMesures24h);
        // Cet temperatureInterieureMoyenne peut être supérieur à temperatureInterieureMax (moyenne sur une heure) 
    }

    /**
     * @brief Afficher les données de température intérieure dans le graphique
     */
    private void afficherDonneesTemperatureInterieure()
    {
        DataPoint[] dataPoints = new DataPoint[mesuresTemperatureInterieure.size()];
        for (int i = 0; i < mesuresTemperatureInterieure.size(); i++)
        {
            dataPoints[i] = new DataPoint(i, mesuresTemperatureInterieure.get(i));
        }

        seriesTemperatureInterieure = new LineGraphSeries<DataPoint>(dataPoints);

        graphique.addSeries(seriesTemperatureInterieure);
        seriesTemperatureInterieure.setColor(Color.RED);
        seriesTemperatureInterieure.setTitle("°C");

        valeurCourante.setText("Dernière valeur : " + String.valueOf(arrondir(temperatureInterieureCourante, 2)) + " °C");
        donneesMin.setText(String.valueOf(arrondir(temperatureInterieureMin, 1)) + " °C");
        donneesMax.setText(String.valueOf(arrondir(temperatureInterieureMax, 1)) + " °C");
        donneesMoyenne.setText(String.valueOf(arrondir(temperatureInterieureMoyenne, 1)) + " °C");
    }

    /**
     * @brief Extraire les données de humidité intérieure de l'historique
     */
    private void extraireDonneesHumiditeInterieure()
    {
        // Obligation d'utiliser un Thread
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    JSONArray tableauJSON = new JSONArray(contenuHistorique);
                    JSONObject valeur = null;
                    Date date = null;
                    SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    SimpleDateFormat sdfOutHeure = new SimpleDateFormat("HH", Locale.FRENCH);

                    Log.d(TAG, "extraireDonneesHumiditeInterieure() Nb mesures récupérées = " + tableauJSON.length());
                    //Toast.makeText(IHMGraphique.this,tableauJSON.length() + " données récupérées", Toast.LENGTH_SHORT).show();

                    donneeHumiditeInterieure.clear();
                    for(int i = 0; i < tableauJSON.length(); i++)
                    {
                        valeur = tableauJSON.getJSONObject(i);
                        //Log.d(TAG, "extraireDonneesHumiditeInterieure() valeur = " + valeur);
                        /* {
                                "device_id":"ruche-1-sim",
                                "humiditeExt":null,
                                "humiditeInt":null,
                                "poids":null,
                                "pression":null,
                                "temperatureExt":null,
                                "temperatureInt":24.4,
                                "time":"2021-05-26T08:26:53.789498119Z"
                            }
                         */
                        if(valeur.getString("device_id").equals(deviceID))
                        {
                            if(!valeur.isNull("humiditeInt"))
                            {
                                Double humiditeInterieure = valeur.getDouble("humiditeInt");
                                String time = valeur.getString("time");
                                //Log.d(TAG, "extraireDonneesHumiditeInterieure() humidité intérieure = " + humiditeInterieure);
                                //Log.d(TAG, "extraireDonneesHumiditeInterieure() time = " + time);
                                sdfIn.setTimeZone(TimeZone.getTimeZone("UTC"));
                                String heure = "";
                                try
                                {
                                    date = sdfIn.parse(time);
                                    sdfOutHeure.setTimeZone(TimeZone.getDefault());
                                    heure = sdfOutHeure.format(date);
                                }
                                catch (ParseException e)
                                {
                                    e.printStackTrace();
                                    Log.d(TAG, "extraireDonneesHumiditeInterieure() Erreur !");
                                }
                                List<String> donnees = new ArrayList<>();
                                donnees.add(heure);
                                donnees.add(humiditeInterieure.toString());
                                donneeHumiditeInterieure.add(donnees);
                                //Log.d(TAG, "extraireDonnees() heure = " + heure + " - humiditeInterieure = " + humiditeInterieure + " [" + time + "]");
                                //Log.d(TAG, "extraireDonnees() heure = " + heure + " - humiditeInterieure = " + humiditeInterieure);
                                //Log.d(TAG, "extraireDonnees() donnees = " + donnees);
                            }
                        }
                    }
                    //Log.d(TAG,"extraireDonneesHumiditeInterieure() humiditeInterieure = " + donneehumiditeInterieure);
                    //Log.d(TAG,"extraireDonneesHumiditeInterieure() Nb mesures de l'humidité intérieure = " + donneeTemperatureInterieure.size());
                    Toast.makeText(IHMGraphique.this,donneeHumiditeInterieure.size() + " données de l'humidité intérireure récupérées", Toast.LENGTH_SHORT).show();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                traiterDonneesHumiditeInteireure();

                afficherDonneesHumiditeInterieure();
            }
        });
    }

    /**
     * @brief Traiter les donnes de l'humidité interieure pour l'affichage de graphique
     */
    private void traiterDonneesHumiditeInteireure()
    {
        double somme24h = 0.;
        double somme = 0.;
        int nbMesures24h = 0;
        int nbMesures = 0;

        /*if(humiditeInterieure.size() < 1)
            return;*/

        //Log.d(TAG,"traiterDonneesHumiditeInteireure() Nb mesures de l'humidité intérieure = " + donneeHumiditeInterieure.size());
        mesuresHumiditeInterieure.clear();
        boolean initMinMax = false;
        Date date = new Date();
        for(int heure = 0; heure < date.getHours(); heure++)
        {
            somme = 0.;
            nbMesures = 0;
            for (List<String> donnees : donneeHumiditeInterieure)
            {
                if(Integer.parseInt(donnees.get(0)) == heure)
                {
                    //Log.d(TAG, "traiterDonneesHumiditeInteireure() heure = " + donnees.get(0) + " - humiditeInterieure = " + donnees.get(1));
                    somme += Double.parseDouble(donnees.get(1));
                    nbMesures++;
                }
            }

            if(nbMesures > 0)
            {
                //Log.d(TAG,"traiterDonneesHumiditeInteireure() heure = " + heure + " - Nb mesures = " + nbMesures + " - somme = " + somme + " - moyenne = " + (somme/(double)nbMesures));
                double valeur = (somme/(double)nbMesures);
                mesuresHumiditeInterieure.add(heure, valeur);
                // Première mesure -> initialise les min/max
                if(!initMinMax)
                {
                    humiditeInterieureMax = valeur;
                    humiditeInterieureMin = valeur;
                    initMinMax = true;
                }
                somme24h += valeur;
                nbMesures24h++;
                humiditeInterieureCourante = valeur;
                if(valeur > humiditeInterieureMax)
                    humiditeInterieureMax = valeur;
                if(valeur < humiditeInterieureMin)
                    humiditeInterieureMin = valeur;
            }
            else
            {
                mesuresHumiditeInterieure.add(heure, 0.);
            }
        }

        humiditeInterieureMoyenne = (somme24h/(double)nbMesures24h);
        // Cet humiditeInterieureMoyenne peut être supérieur à humiditeInterieureMax (moyenne sur une heure)
    }

    /**
     * @brief Afficher les données de l'humidité intérieure dans le graphique
     */
    private void afficherDonneesHumiditeInterieure()
    {
        DataPoint[] dataPoints = new DataPoint[mesuresHumiditeInterieure.size()];
        for (int i = 0; i < mesuresHumiditeInterieure.size(); i++)
        {
            dataPoints[i] = new DataPoint(i, mesuresHumiditeInterieure.get(i));
        }

        seriesHumiditeInterieure = new LineGraphSeries<DataPoint>(dataPoints);

        graphique.addSeries(seriesHumiditeInterieure);
        seriesHumiditeInterieure.setColor(Color.BLUE);
        seriesHumiditeInterieure.setTitle("%");

        valeurCourante.setText("Dernière valeur : " + String.valueOf(arrondir(humiditeInterieureCourante, 2)) + " %");
        donneesMin.setText(String.valueOf(arrondir(humiditeInterieureMin, 1)) + " %");
        donneesMax.setText(String.valueOf(arrondir(humiditeInterieureMax, 1)) + " %");
        donneesMoyenne.setText(String.valueOf(arrondir(humiditeInterieureMoyenne, 1)) + " %");
    }

    /**
     * @brief Extraire les données de température extérieure de l'historique
     */
    private void extraireDonneesTemperatureExterieure()
    {
        // Obligation d'utiliser un Thread
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    JSONArray tableauJSON = new JSONArray(contenuHistorique);
                    JSONObject valeur = null;
                    Date date = null;
                    SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    SimpleDateFormat sdfOutHeure = new SimpleDateFormat("HH", Locale.FRENCH);

                    Log.d(TAG, "extraireDonneesTemperatureExterieure() Nb mesures récupérées = " + tableauJSON.length());
                    //Toast.makeText(IHMGraphique.this,tableauJSON.length() + " données récupérées", Toast.LENGTH_SHORT).show();

                    donneeTemperatureExterieure.clear();
                    for(int i = 0; i < tableauJSON.length(); i++)
                    {
                        valeur = tableauJSON.getJSONObject(i);
                        //Log.d(TAG, "extraireDonneesTemperatureExterieure() valeur = " + valeur);
                        /* {
                                "device_id":"ruche-1-sim",
                                "humiditeExt":null,
                                "humiditeInt":null,
                                "poids":null,
                                "pression":null,
                                "temperatureExt":null,
                                "temperatureInt":24.4,
                                "time":"2021-05-26T08:26:53.789498119Z"
                            }
                         */
                        if(valeur.getString("device_id").equals(deviceID))
                        {
                            if(!valeur.isNull("temperatureExt"))
                            {
                                Double temperatureExterieure = valeur.getDouble("temperatureExt");
                                String time = valeur.getString("time");
                                //Log.d(TAG, "extraireDonneesTemperatureExterieure() température extérieure = " + temperatureExterieure);
                                //Log.d(TAG, "extraireDonneesTemperatureExterieure() time = " + time);
                                sdfIn.setTimeZone(TimeZone.getTimeZone("UTC"));
                                String heure = "";
                                try
                                {
                                    date = sdfIn.parse(time);
                                    sdfOutHeure.setTimeZone(TimeZone.getDefault());
                                    heure = sdfOutHeure.format(date);
                                }
                                catch (ParseException e)
                                {
                                    e.printStackTrace();
                                    Log.d(TAG, "extraireDonneesTemperatureExterieure() Erreur !");
                                }
                                List<String> donnees = new ArrayList<>();
                                donnees.add(heure);
                                donnees.add(temperatureExterieure.toString());
                                donneeTemperatureExterieure.add(donnees);
                                //Log.d(TAG, "extraireDonnees() heure = " + heure + " - temperatureExterieure = " + temperatureExterieure + " [" + time + "]");
                                //Log.d(TAG, "extraireDonnees() heure = " + heure + " - temperatureExterieure = " + temperatureExterieure);
                                //Log.d(TAG, "extraireDonnees() donnees = " + donnees);
                            }
                        }
                    }
                    //Log.d(TAG,"extraireDonneesTemperatureExterieure() donneeTemperatureExterieure = " + donneeTemperatureExterieure);
                    //Log.d(TAG,"extraireDonneesTemperatureExterieure() Nb mesures température extérieure = " + donneeTemperatureExterieure.size());
                    Toast.makeText(IHMGraphique.this,donneeTemperatureExterieure.size() + " données de temperature extérieure récupérées", Toast.LENGTH_SHORT).show();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                traiterDonneesTempetatureExterieure();

                afficherDonneesTemperatureExterieure();
            }
        });
    }

    /**
     * @brief Traiter les donnes de température extérieure pour l'affichage de graphique
     */
    private void traiterDonneesTempetatureExterieure()
    {
        double somme24h = 0.;
        double somme = 0.;
        int nbMesures24h = 0;
        int nbMesures = 0;

        /*if(donneeTemperatureExterieure.size() < 1)
            return;*/

        //Log.d(TAG,"traiterDonneesTempetatureExterieure() Nb mesures temperature extérieure = " + donneeTemperatureExterieure.size());
        mesuresTemperatureExterireure.clear();
        boolean initMinMax = false;
        Date date = new Date();
        for(int heure = 0; heure < date.getHours(); heure++)
        {
            somme = 0.;
            nbMesures = 0;
            for (List<String> donnees : donneeTemperatureExterieure)
            {
                if(Integer.parseInt(donnees.get(0)) == heure)
                {
                    //Log.d(TAG, "traiterDonneesTempetatureExterieure() heure = " + donnees.get(0) + " - temperatureExterieure = " + donnees.get(1));
                    somme += Double.parseDouble(donnees.get(1));
                    nbMesures++;
                }
            }

            if(nbMesures > 0)
            {
                //Log.d(TAG,"traiterDonneesTempetatureExterieure() heure = " + heure + " - Nb mesures = " + nbMesures + " - somme = " + somme + " - moyenne = " + (somme/(double)nbMesures));
                double valeur = (somme/(double)nbMesures);
                mesuresTemperatureExterireure.add(heure, valeur);
                // Première mesure -> initialise les min/max
                if(!initMinMax)
                {
                    temperatureExterireureMax = valeur;
                    temperatureExterireureMin = valeur;
                    initMinMax = true;
                }
                somme24h += valeur;
                nbMesures24h++;
                temperatureExterireureCourante = valeur;
                if(valeur > temperatureExterireureMax)
                    temperatureExterireureMax = valeur;
                if(valeur < temperatureExterireureMin)
                    temperatureExterireureMin = valeur;
            }
            else
            {
                mesuresTemperatureExterireure.add(heure, 0.);
            }
        }

        temperatureExterireureMoyenne = (somme24h/(double)nbMesures24h);
        // Cet temperatureExterireureMoyenne peut être supérieur à temperatureExterieureMax (moyenne sur une heure)
    }

    /**
     * @brief Afficher les données de température extérieure dans le graphique
     */
    private void afficherDonneesTemperatureExterieure()
    {
        DataPoint[] dataPoints = new DataPoint[mesuresTemperatureExterireure.size()];
        for (int i = 0; i < mesuresTemperatureExterireure.size(); i++)
        {
            dataPoints[i] = new DataPoint(i, mesuresTemperatureExterireure.get(i));
        }

        seriesTemperatureExterieure = new LineGraphSeries<DataPoint>(dataPoints);

        graphique.addSeries(seriesTemperatureExterieure);
        seriesTemperatureExterieure.setColor(Color.RED);
        seriesTemperatureExterieure.setTitle("°C");

        valeurCourante.setText("Dernière valeur : " + String.valueOf(arrondir(temperatureExterireureCourante, 2)) + " °C");
        donneesMin.setText(String.valueOf(arrondir(temperatureExterireureMin, 1)) + " °C");
        donneesMax.setText(String.valueOf(arrondir(temperatureExterireureMax, 1)) + " °C");
        donneesMoyenne.setText(String.valueOf(arrondir(temperatureExterireureMoyenne, 1)) + " °C");
    }

    /**
     * @brief Extraire les données de l'humidité extérieure de l'historique
     */
    private void extraireDonneesHumiditeExterieure()
    {
        // Obligation d'utiliser un Thread
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    JSONArray tableauJSON = new JSONArray(contenuHistorique);
                    JSONObject valeur = null;
                    Date date = null;
                    SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    SimpleDateFormat sdfOutHeure = new SimpleDateFormat("HH", Locale.FRENCH);

                    Log.d(TAG, "extraireDonneesHumiditeExterieure() Nb mesures récupérées = " + tableauJSON.length());
                    //Toast.makeText(IHMGraphique.this,tableauJSON.length() + " données récupérées", Toast.LENGTH_SHORT).show();

                    donneeHumiditeExterireure.clear();
                    for(int i = 0; i < tableauJSON.length(); i++)
                    {
                        valeur = tableauJSON.getJSONObject(i);
                        //Log.d(TAG, "extraireDonneesHumiditeExterieure() valeur = " + valeur);
                        /* {
                                "device_id":"ruche-1-sim",
                                "humiditeExt":null,
                                "humiditeInt":null,
                                "poids":null,
                                "pression":null,
                                "temperatureExt":null,
                                "temperatureInt":24.4,
                                "time":"2021-05-26T08:26:53.789498119Z"
                            }
                         */
                        if(valeur.getString("device_id").equals(deviceID))
                        {
                            if(!valeur.isNull("humiditeInt"))
                            {
                                Double humiditeExterieure = valeur.getDouble("humiditeInt");
                                String time = valeur.getString("time");
                                //Log.d(TAG, "extraireDonneesHumiditeExterieure() humidité extérieure = " + humiditeExterieure);
                                //Log.d(TAG, "extraireDonneesHumiditeExterieure() time = " + time);
                                sdfIn.setTimeZone(TimeZone.getTimeZone("UTC"));
                                String heure = "";
                                try
                                {
                                    date = sdfIn.parse(time);
                                    sdfOutHeure.setTimeZone(TimeZone.getDefault());
                                    heure = sdfOutHeure.format(date);
                                }
                                catch (ParseException e)
                                {
                                    e.printStackTrace();
                                    Log.d(TAG, "extraireDonneesHumiditeExterieure() Erreur !");
                                }
                                List<String> donnees = new ArrayList<>();
                                donnees.add(heure);
                                donnees.add(humiditeExterieure.toString());
                                donneeHumiditeExterireure.add(donnees);
                                //Log.d(TAG, "extraireDonnees() heure = " + heure + " - humiditeExterieure = " + humiditeExterieure + " [" + time + "]");
                                //Log.d(TAG, "extraireDonnees() heure = " + heure + " - humiditeExterieure = " + humiditeExterieure);
                                //Log.d(TAG, "extraireDonnees() donnees = " + donnees);
                            }
                        }
                    }
                    //Log.d(TAG,"extraireDonneesHumiditeExterieure() humiditeInterieure = " + donneehumiditeInterieure);
                    //Log.d(TAG,"extraireDonneesHumiditeExterieure() Nb mesures de l'humidité extérieure = " + donneeHumiditeExeterieure.size());
                    Toast.makeText(IHMGraphique.this,donneeHumiditeExterireure.size() + " données de l'humidité extérieure récupérées", Toast.LENGTH_SHORT).show();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                traiterDonneesHumiditeExterieure();

                afficherDonneesHumiditeExterieure();
            }
        });
    }

    /**
     * @brief Traiter les donnes de l'humidité extérieure pour l'affichage de graphique
     */
    private void traiterDonneesHumiditeExterieure()
    {
        double somme24h = 0.;
        double somme = 0.;
        int nbMesures24h = 0;
        int nbMesures = 0;

        /*if(humiditeInterieure.size() < 1)
            return;*/

        //Log.d(TAG,"traiterDonneesHumiditeExterieure() Nb mesures de l'humidité intérieure = " + donneeHumiditeExterieure.size());
        mesuresHumiditeExterireure.clear();
        boolean initMinMax = false;
        Date date = new Date();
        for(int heure = 0; heure < date.getHours(); heure++)
        {
            somme = 0.;
            nbMesures = 0;
            for (List<String> donnees : donneeHumiditeExterireure)
            {
                if(Integer.parseInt(donnees.get(0)) == heure)
                {
                    //Log.d(TAG, "traiterDonneesHumiditeExterieure() heure = " + donnees.get(0) + " - humiditeExterieure = " + donnees.get(1));
                    somme += Double.parseDouble(donnees.get(1));
                    nbMesures++;
                }
            }

            if(nbMesures > 0)
            {
                //Log.d(TAG,"traiterDonneesHumiditeExterieure() heure = " + heure + " - Nb mesures = " + nbMesures + " - somme = " + somme + " - moyenne = " + (somme/(double)nbMesures));
                double valeur = (somme/(double)nbMesures);
                mesuresHumiditeExterireure.add(heure, valeur);
                // Première mesure -> initialise les min/max
                if(!initMinMax)
                {
                    humiditeExterireureMax = valeur;
                    humiditeExterireureMin = valeur;
                    initMinMax = true;
                }
                somme24h += valeur;
                nbMesures24h++;
                humiditeExterireureCourante = valeur;
                if(valeur > humiditeExterireureMax)
                    humiditeExterireureMax = valeur;
                if(valeur < humiditeExterireureMin)
                    humiditeExterireureMin = valeur;
            }
            else
            {
                mesuresHumiditeExterireure.add(heure, 0.);
            }
        }

        humiditeExterireureMoyenne = (somme24h/(double)nbMesures24h);
        // Cet humiditeExterieureMoyenne peut être supérieur à humiditeExterieureMax (moyenne sur une heure)
    }

    /**
     * @brief Afficher les données de l'humidité extérieure dans le graphique
     */
    private void afficherDonneesHumiditeExterieure()
    {
        DataPoint[] dataPoints = new DataPoint[mesuresHumiditeExterireure.size()];
        for (int i = 0; i < mesuresHumiditeExterireure.size(); i++)
        {
            dataPoints[i] = new DataPoint(i, mesuresHumiditeExterireure.get(i));
        }

        seriesHumiditeExterieure = new LineGraphSeries<DataPoint>(dataPoints);

        graphique.addSeries(seriesHumiditeExterieure);
        seriesHumiditeExterieure.setColor(Color.BLUE);
        seriesHumiditeExterieure.setTitle("%");

        valeurCourante.setText("Dernière valeur : " + String.valueOf(arrondir(humiditeExterireureCourante, 2)) + " %");
        donneesMin.setText(String.valueOf(arrondir(humiditeExterireureMin, 1)) + " %");
        donneesMax.setText(String.valueOf(arrondir(humiditeExterireureMax, 1)) + " %");
        donneesMoyenne.setText(String.valueOf(arrondir(humiditeExterireureMoyenne, 1)) + " %");
    }

    /**
     * @brief Utilitaire pour arrondir un double
     */
    public static double arrondir(double nombre, double nbApresVirgule)
    {
        return (double)((int)(nombre * Math.pow(10, nbApresVirgule) + .5)) / Math.pow(10, nbApresVirgule);
    }

}