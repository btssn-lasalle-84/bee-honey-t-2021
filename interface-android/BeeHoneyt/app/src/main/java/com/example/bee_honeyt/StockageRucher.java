package com.example.bee_honeyt;

import android.content.SharedPreferences;

import java.util.Map;
import java.util.Vector;
/**
 * @file StockageRucher.java
 * @brief Déclaration de la classe StockageRucher
 * @details Permet de stocker les ruches et le paramétrage des alertes
 * @author FOUCAULT Clémentine
 * $LastChangedRevision: 107 $
 * $LastChangedDate: 2021-05-21 10:28:22 +0200 (ven. 21 mai 2021) $
 */

/**
 * @class StockageRucher
 * @brief Classe pour stocker les ruches
 */
public class StockageRucher
{
    private static final String TAG = "_StockageRuche"; //!< TAG pour les logs
    // Les éléments pour les stockages des ruches
    private SharedPreferences stockage; //!< Le stockage

    /**
     * @brief Constructeur de la classe StockageRucher
     */
    public StockageRucher(IHMMobile ihmMobile)
    {
        stockage = ihmMobile.getPreferences(IHMMobile.MODE_PRIVATE);
    }

    /**
     * @brief Méthode pour obtenir toute les données du stockage
     * @return String les données
     */
    public String obtenir(String cle)
    {
        Map<String,?> donnees = stockage.getAll();
        if(stockage.contains(cle))
        {
            return (String) donnees.get(cle);
        }
        else
        {
            return "";
        }
    }

    /**
     * @brief Méthode pour obtenir les ruches
     * @return Vector<Ruche> Les ruches
     */
    public Vector<Ruche> obtenirRuches()
    {
        Vector<Ruche> ruches = new Vector<Ruche>();
        Map<String,?> donnees = stockage.getAll();
        for (String id : donnees.keySet())
        {
            String nom = "";
            // Le DeviceID doit contenir au moins un "-"
            if(stockage.contains(id) && id.contains("-"))
            {
                nom = stockage.getString(id, "");
                String alertesJSON = this.obtenir(nom);
                Ruche rucheActuelle = new Ruche(nom, id, alertesJSON);
                ruches.add(rucheActuelle);
            }
        }
        return ruches;
    }

    /**
     * @brief Methode pour editer une ruche dans le stockage
     */
    public void editerRuche(String nom, String deviceID)
    {
        stockage.edit().putString(deviceID, nom).apply();
    }

    /**
     * @brief Methode pour supprimer une ruche dans le stockage
     */
    public void supprimerRuche(String deviceID)
    {
        stockage.edit().remove(deviceID).apply();
    }

    /**
     * @brief Methode pour savoir si le stockage est vide ou non
     * @return boolean
     */
    public boolean contient(String cle)
    {
        if(stockage.contains(cle))
        {
            return true;
        }
        return false;
    }

    /**
     * @brief Methode pour connaitre le nombre de ruche dans le stockage
     * @return int la taille du stockage
     */
    public int obtenirNombreRuches()
    {
        Map<String,?> donnees = stockage.getAll();
        return donnees.size();
    }

    /**
     * @brief Methode pour editer les seuils d'alerte d'une ruche dans le stockage
     */
    public void editerAlertes(String nomRuche, String alertesJSON)
    {
        stockage.edit().putString(nomRuche, alertesJSON).apply();
    }

    /**
     * @brief Methode pour supprimer les seuils d'alertes d'une ruche dans le stockage
     */
    public void supprimerAlertes(String nomRuche)
    {
        stockage.edit().remove(nomRuche).apply();
    }
}
