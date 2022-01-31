package com.example.bee_honeyt;

/**
 * @file MesureRuche.java
 * @brief Déclaration de la classe MesureRuche
 * @details Contient les mesures d’une ruche sélectionnée.
 * @author FOUCAULT Clémentine
 * $LastChangedRevision: 129 $
 * $LastChangedDate: 2021-05-28 16:20:17 +0200 (ven. 28 mai 2021) $
 */

/**
 * @class MesureRuche
 * @brief Les mesures associées à une ruche
 */
public class MesureRuche
{
    private static final String TAG = "_MesureRuche"; //!< TAG pour les logs

    private double temperatureInterieure = 0.0; //!< La température interieure
    private double temperatureExterieure= 0.0; //!< La température exterieure
    private int humiditeInterieure = 0; //!< L'humidité interieure
    private int humiditeExterieure = 0; //!< L'humidité exterieure
    private int pression = 0; //!< La pression
    private double poids = 0.0; // le poids

    /**
     * @brief constructeur par defaut
     */
    public MesureRuche()
    {

    }

    /**
     * @brief constructeur de la classe MesureRuche
     */
    public MesureRuche(double temperatureInterieure, double temperatureExterieure, int humiditeInterieure, int humiditeExterieure, int pression, double poids)
    {
        this.temperatureInterieure = temperatureInterieure;
        this.temperatureExterieure = temperatureExterieure;
        this.humiditeInterieure = humiditeInterieure;
        this.humiditeExterieure = humiditeExterieure;
        this.pression = pression;
        this.poids = poids;

    }

    /**
     * @brief accesseur Temperature interieure
     * @return double la temperature interieure
     */
    public final double getTemperatureInterieure()
    {
        return temperatureInterieure;
    }

    /**
     * @brief mutateur Temperature interieure
     * @param temperatureInterieure
     */
    public void setTemperatureInterieure(double temperatureInterieure)
    {
        this.temperatureInterieure = temperatureInterieure;
    }

    /**
     * @brief accesseur Temperature exterieure
     * @return double la temperature exterieure
     */
    public final double getTemperatureExterieure()
    {
        return temperatureExterieure;
    }

    /**
     * @brief mutateur Temperature exterieure
     * @param temperatureExterieure
     */
    public void setTemperatureExterieure(double temperatureExterieure)
    {
        this.temperatureExterieure = temperatureExterieure;
    }

    /**
     * @brief accesseur Humidite interieure
     * @return int l'humidite interieure
     */
    public final int getHumiditeInterieure()
    {
        return humiditeInterieure;
    }

    /**
     * @brief mutateur Humidite interieure
     * @param humiditeInterieure
     */
    public void setHumiditeInterieure(int humiditeInterieure)
    {
        this.humiditeInterieure = humiditeInterieure;
    }

    /**
     * @brief accesseur Humidite exterieure
     * @return int l'humidite exterieure
     */
    public final int getHumiditeExterieure()
    {
        return humiditeExterieure;
    }

    /**
     * @brief mutateur Humidite exterieure
     * @param humiditeExterieure
     */
    public void setHumiditeExterieure(int humiditeExterieure)
    {
        this.humiditeExterieure = humiditeExterieure;
    }

    /**
     * @brief accesseur Pression
     * @return int la pression
     */
    public final int getPression()
    {
        return pression;
    }

    /**
     * @brief mutateur Pression
     * @param pression
     */
    public void setPression(int pression)
    {
        this.pression = pression;
    }

    /**
     * @brief accesseur Poids
     * @return double le poids
     */
    public final double getPoids()
    {
        return poids;
    }

    /**
     * @brief mutateur Poids
     * @param poids
     */
    public void setPoids(double poids)
    {
        this.poids = poids;
    }
}
