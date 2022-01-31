package com.example.bee_honeyt;

/**
 * @file Ruche.java
 * @brief Déclaration de la classe Ruche
 * @details Contient les informations d’une ruche.
 * @author FOUCAULT Clémentine
 * $LastChangedRevision: 129 $
 * $LastChangedDate: 2021-05-28 16:20:17 +0200 (ven. 28 mai 2021) $
 */

import android.util.Log;

/**
 * @class Ruche
 * @brief Les données et seuils d'une ruche
 */
public class Ruche
{
    private static final String TAG = "_Ruche"; //!< TAG pour les logs
    // Attributs
    private String nom = ""; //!< le nom de la ruche pour l'apiculteur
    private String deviceID; //!< l'id de la ruche pour TTN
    private String horodatage; //!< l'horodatage
    private double longitude = 0.0;
    private double latitude = 0.0;
    // Relations
    MesureRuche mesureRuche = new MesureRuche(); //!< associer les mesures pour cette ruche
    Alertes alerteRuche = null; //!< associer les seuils par défaut pour cette ruche

    /**
     * @brief Constructeur par defaut de la classe Ruche
     */
    public Ruche(String nom, String deviceID)
    {
        this.nom = nom;
        this.deviceID = deviceID;
        this.alerteRuche = new Alertes();
    }

    /**
     * @brief Constructeur par defaut de la classe Ruche
     */
    public Ruche(String nom, String deviceID, String alertesJSON)
    {
        this.nom = nom;
        this.deviceID = deviceID;
        Log.d(TAG,"Ruche(String nom, String deviceID, String alertesJSON) alertesJSON : " + alertesJSON);
        if(alertesJSON.isEmpty())
        {
            Log.d(TAG,"Alertes()");
            this.alerteRuche = new Alertes();
        }
        else
        {
            Log.d(TAG,"Alertes(alertesJSON)");
            this.alerteRuche = new Alertes(alertesJSON);
        }
    }

    /**
     * @brief accesseur Nom
     * @return String le nom
     */
    public final String getNom()
    {
        return nom;
    }

    /**
     * @brief mutateur Nom
     * @param nouveauNom
     */
    public void setNom(String nouveauNom)
    {
        this.nom = nouveauNom;
    }

    /**
     * @brief accesseur DeviceID
     * @return String le deviceID
     */
    public final String getDeviceID()
    {
        return deviceID;
    }

    /**
     * @brief mutateur DeviceID
     * @param deviceID
     */
    public void setDeviceID(String deviceID) { this.deviceID = deviceID;}

    /**
     * @brief accesseur Horodatage
     * @return String l'horodatage
     */
    public String getHorodatage() { return horodatage; }

    /**
     * @brief mutateur Horodatage
     * @param horodatage
     */
    public void setHorodatage(String horodatage)
    {
        this.horodatage = horodatage;
    }

    /**
     * @brief accesseur MesureRuche
     * @return mesureRuche
     */
    public final MesureRuche getMesureRuche()
    {
        return mesureRuche;
    }

    /**
     * @brief mutateur MesureRuche
     * @param mesureRuche
     */
    public void setMesureRuche(MesureRuche mesureRuche)
    {
        this.mesureRuche = mesureRuche;
    }

    /**
     * @brief accesseur Alertes
     * @return alerteRuche
     */
    public final Alertes getAlerteRuche()
    {
        return alerteRuche;
    }

    /**
     * @brief mutateur AlerteRuche
     * @param alerteRuche
     */
    public void setAlerteRuche(Alertes alerteRuche)
    {
        Log.d(TAG,"setAlerteRuche()");
        this.alerteRuche = alerteRuche;
    }

    /**
     * @brief accesseur Longitude
     * @return double la longitude
     */
    public final double getLongitude()
    {
        return longitude;
    }

    /**
     * @brief mutateur Longitude
     * @param longitude
     */
    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    /**
     * @brief accesseur Latitude
     * @return double la latitude
     */
    public final double getLatitude()
    {
        return latitude;
    }

    /**
     * @brief mutateur Latitude
     * @param latitude
     */
    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }
}
