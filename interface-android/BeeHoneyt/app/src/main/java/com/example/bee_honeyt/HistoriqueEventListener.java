package com.example.bee_honeyt;

/**
 * @class HistoriqueEventListener
 * @brief Déclaration de l'interface HistoriqueEventListener
 * @details Permet de définir l'interface pour les méthodes de rappel
 * @author Thierry Vaira
 * $LastChangedRevision: 109 $
 * $LastChangedDate: 2021-05-21 12:25:01 +0200 (ven. 21 mai 2021) $
 */

public interface HistoriqueEventListener
{
    public void onLoad(String contenu);
    public void onError();
}
