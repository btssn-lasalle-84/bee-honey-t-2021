package com.example.bee_honeyt;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @file Alertes.java
 * @brief Déclaration de la classe Alertes
 * @details Permet de gérer les alertes
 * @author FOUCAULT Clémentine
 * $LastChangedRevision: 162 $
 * $LastChangedDate: 2021-06-11 15:01:29 +0200 (ven. 11 juin 2021) $
 */

/**
 * @class Alertes
 * @brief Classe pour gérer les alertes
 */
public class Alertes
{
    private static final String TAG = "_Alertes"; //!< TAG pour les logs

    // les seuils par défaut
    public final static double TEMPERATURE_INTERIEURE_MAX = 25.0; //!< La temperature intérieure maximum
    public final static double TEMPERATURE_INTERIEURE_MIN = 24.0; //!< La temperature intérieure minimum
    public final static double TEMPERATURE_EXTERIEURE_MIN = 10.0; //!< La temperature extérieure minimum
    public final static double TEMPERATURE_EXTERIEURE_MAX = 25.0; //!< La temperature extérieure maximum
    public  final static int HUMIDITE_INTERIEURE_MIN = 10; //!< L'humidité intérieure minimum
    public  final static int HUMIDITE_INTERIEURE_MAX = 30; //!< L'humidité intérieure maximum
    public  final static int HUMIDITE_EXTERIEURE_MIN = 29; //!< L'humidité extérieure minimum
    public  final static int HUMIDITE_EXTERIEURE_MAX = 30; //!< L'humidité extérieure maximum
    public  final static double POIDS_MAX = 20.0; //!< Le poids maximum
    public  final static double POIDS_MIN = 14.0; //!< Le poids minimum
    public  final static double PRESSION_MAX = 1085; //!< Le poids maximum
    public  final static double PRESSION_MIN = 900; //!< Le poids minimum

    // les seuils
    private double temperatureInterieureMin = 0.0; //!< La temperature interieure minimum
    private double temperatureInterieureMax = 0.0; //!< La temperature interieure maximun
    private double temperatureExterieureMin = 0.0; //!< La temperature exterieure minimum
    private double temperatureExterieureMax = 0.0; //!< La temperature exteireure maximum
    private int humiditeInterieureMin = 0; //!< L'humidité interieure minimum
    private int humiditeInterieureMax = 0; //!< L'humidité interieure maximum
    private int humiditeExterieureMin = 0; //!< L'humidité exterieure minimum
    private int humiditeExterieureMax = 0; //!< L'humidité exterieure maximum
    private double poidsMin = 0.0; //!< Le poids mimimum
    private double poidsMax = 0.0; //!< Le poids maximum
    private double pressionMin = 0.0; //!< Le pression mimimum
    private double pressionMax = 0.0; //!< Le pression maximum

    /**
     * @brief constructeur par defaut
     */
    public Alertes()
    {
        this.temperatureInterieureMin = TEMPERATURE_INTERIEURE_MIN;
        this.temperatureInterieureMax = TEMPERATURE_INTERIEURE_MAX;
        this.temperatureExterieureMin = TEMPERATURE_EXTERIEURE_MIN;
        this.temperatureExterieureMax = TEMPERATURE_EXTERIEURE_MAX;
        this.humiditeInterieureMin = HUMIDITE_INTERIEURE_MIN;
        this.humiditeInterieureMax = HUMIDITE_INTERIEURE_MAX;
        this.humiditeExterieureMin = HUMIDITE_EXTERIEURE_MIN;
        this.humiditeExterieureMax = HUMIDITE_EXTERIEURE_MAX;
        this.poidsMin = POIDS_MIN;
        this.poidsMax = POIDS_MAX;
        this.pressionMin = PRESSION_MIN;
        this.pressionMax = PRESSION_MAX;
    }

    /**
     * @brief constructeur de la classe alertes
     */
    public Alertes(double temperatureInterieureMin, double temperatureInterieureMax, double temperatureExterieureMin,
                   double temperatureExterieureMax, int humiditeInterieureMin, int humiditeInterieureMax,
                   int humiditeExterieureMin, int humiditeExterieureMax, double poidsMin, double poidsMax, double pressionMin, double pressionMax)
    {
        this.temperatureInterieureMin = temperatureInterieureMin;
        this.temperatureInterieureMax = temperatureInterieureMax;
        this.temperatureExterieureMin = temperatureExterieureMin;
        this.temperatureExterieureMax = temperatureExterieureMax;
        this.humiditeInterieureMin = humiditeInterieureMin;
        this.humiditeInterieureMax = humiditeInterieureMax;
        this.humiditeExterieureMin = humiditeExterieureMin;
        this.humiditeExterieureMax = humiditeExterieureMax;
        this.poidsMin = poidsMin;
        this.poidsMax = poidsMax;
        this.pressionMin = pressionMin;
        this.pressionMax = pressionMax;
    }

    /**
     * @brief constructeur de la classe alertes pour crée un format JSON pour pouvoir stocker les alertes.
     */
    public Alertes(String fromJSON)
    {
        try
        {
            Log.i(TAG, "JSON = " + fromJSON);

            JSONObject json = new JSONObject(fromJSON);

            this.temperatureInterieureMax = json.getDouble("temperatureInterieureMax");
            this.temperatureInterieureMin = json.getDouble("temperatureInterieureMin");
            this.temperatureExterieureMax = json.getDouble("temperatureExterieureMax");
            this.temperatureExterieureMin = json.getDouble("temperatureExterieureMin");
            this.humiditeInterieureMax = json.getInt("humiditeInterieureMax");
            this.humiditeInterieureMin = json.getInt("humiditeInterieureMin");
            this.humiditeExterieureMax = json.getInt("humiditeExterieureMax");
            this.humiditeExterieureMin = json.getInt("humiditeExterieureMin");
            this.poidsMax = json.getDouble("poidsMax");
            this.poidsMin = json.getDouble("poidsMin");
            this.pressionMin = json.getDouble("pressionMin");
            this.pressionMax = json.getDouble("pressionMax");

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @brief accesseur Temperature interieure minimum
     * @return double la temperature interieure minimum
     */
    public final double getTemperatureInterieurMin()
    {
        return temperatureInterieureMin;
    }

    /**
     * @brief accesseur Temperature interieure maximum
     * @return double la temperature interieure maximum
     */
    public final double getTemperatureInterieurMax()
    {
        return temperatureInterieureMax;
    }

    /**
     * @brief accesseur Temperature exterieure minimum
     * @return double la temperature exterieure minimum
     */
    public final double getTemperatureExterieurMin()
    {
        return temperatureExterieureMin;
    }

    /**
     * @brief accesseur Temperature exterieure maximum
     * @return double le temperature maximum
     */
    public final double getTemperatureExterieurMax()
    {
        return temperatureExterieureMax;
    }

    /**
     * @brief accesseur Humiditer interieure minimum
     * @return int l'humiditer interieure minimum
     */
    public final int getHumiditeInterieurMin()
    {
        return humiditeInterieureMin;
    }

    /**
     * @brief accesseur Humiditer interieure maximum
     * @return int l'humiditer interireure maximum
     */
    public final int getHumiditeInterieurMax()
    {
        return humiditeInterieureMax;
    }

    /**
     * @brief accesseur Humiditer exterieure minimum
     * @return int l'humidite exterieure minimum
     */
    public final int getHumiditeExterieurMin()
    {
        return humiditeExterieureMin;
    }

    /**
     * @brief accesseur Humiditer exterieure maximum
     * @return int l'humidite exterieure maximum
     */
    public final int getHumiditeExterieurMax()
    {
        return humiditeExterieureMax;
    }

    /**
     * @brief accesseur Poids minimum
     * @return double le poids minimum
     */
    public final double getPoidsMin()
    {
        return poidsMin;
    }

    /**
     * @brief accesseur Poids maximum
     * @return double le poids maximum
     */
    public final double getPoidsMax()
    {
        return poidsMax;
    }

    /**
     * @brief accesseur Pression minimum
     * @return double la pression minimum
     */
    public final double getPressionMin()
    {
        return pressionMin;
    }

    /**
     * @brief accesseur Pression maximum
     * @return double la pression maximum
     */
    public final double getPressionMax()
    {
        return pressionMax;
    }


    /**
     * @brief Création de données JSON pour pouvoir stocker les seuils d'alerte dans le stockage
     */
    public String toJSON()
    {
        JSONObject objet = new JSONObject();
        try
        {
            objet.put("temperatureInterieureMin", this.temperatureInterieureMin);
            objet.put("temperatureInterieureMax", this.temperatureInterieureMax);
            objet.put("temperatureExterieureMin", this.temperatureExterieureMin);
            objet.put("temperatureExterieureMax", this.temperatureExterieureMax);
            objet.put("humiditeInterieureMin", this.humiditeInterieureMin);
            objet.put("humiditeInterieureMax", this.humiditeInterieureMax);
            objet.put("humiditeExterieureMin", this.humiditeExterieureMin);
            objet.put("humiditeExterieureMax", this.humiditeExterieureMax);
            objet.put("poidsMin", this.poidsMin);
            objet.put("poidsMax", this.poidsMax);
            objet.put("pressionMin", this.pressionMin);
            objet.put("pressionMax", this.pressionMax);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
            Log.i(TAG, "Erreur JSON !");
        }

        Log.i(TAG, "JSON = " + objet.toString());
        return objet.toString();
    }
}
