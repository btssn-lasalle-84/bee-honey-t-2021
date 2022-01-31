package com.example.bee_honeyt;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * @class Historique
 * @brief Déclaration de la classe Historique
 * @details Permet la récupération de l'historique Data Storage TTN sur 7 jours max
 * @author Thierry Vaira
 * $LastChangedRevision: 109 $
 * $LastChangedDate: 2021-05-21 12:25:01 +0200 (ven. 21 mai 2021) $
 */

public class Historique
{
    // Constantes
    private static final String TAG = "_Historique"; //!< TAG pour les logs
    // Attributs
    private boolean chargement = false; //!< Indique si le contenu a été chargé
    private AsyncTaskHTTP tache = null; //!< Tâche d'arrière plan pour récupérer le contenu de l'historique
    private HistoriqueEventListener callback; //!< Pour les fonctions de rappel sur évènement (onLoad, onError, ...)

    /**
     * @brief Constructeur par défaut de la classe Historique
     */
    public Historique()
    {
        Log.d(TAG, "new Historique()");
    }

    /**
     * @brief Installe les fonctions de rappel pour les évènements onLoad, ...
     * @param callback L'objet contenant les fonctions de rappel
     */
    public void setCallback(HistoriqueEventListener callback)
    {
        this.callback = callback;
    }

    /**
     * @brief Retourne l'état de chargement du contenu
     * @return boolean
     */
    public boolean estCharge()
    {
        Log.d(TAG, "estCharge() " + this.chargement);
        return this.chargement;
    }

    /**
     * @brief Charge à partir d'une URL vers Data Storage TTN
     * @param strUrl L'URL en https
     * @param key La clé d'authentification TTN
     * @param duree Voir la documentation Data Storage TTN
     */
    public void charger(String strUrl, String key, String deviceID, String duree)
    {
        if(strUrl != null && !strUrl.isEmpty())
        {
            String url;
            if(deviceID.isEmpty())
                url = strUrl + "/api/v2/query";
            else
                url = strUrl + "/api/v2/query/" + deviceID;
            if(!duree.isEmpty()) // par défaut 1 heure
                url += "?last=" + duree;
            Log.d(TAG, "charger() " + strUrl);
            this.tache = new AsyncTaskHTTP();
            tache.execute(url, key);
        }
    }

    /**
     * @brief Classe permettant de lancer la tâche de chargement du contenu de l'historique en arrière plan
     */
    public class AsyncTaskHTTP extends AsyncTask<String, Void, String>
    {
        public AsyncTaskHTTP()
        {
            Log.d(TAG, "<AsyncTaskHTTP> new AsyncTaskHTTP()");
        }

        @Override
        protected String doInBackground(String... strings)
        {
            URL url = null;
            HttpsURLConnection connexionHTTPS = null;
            InputStream in = null;
            String contenu = null;

            Log.d(TAG, "<AsyncTaskHTTP> doInBackground() url : " + strings[0] + " - key : " + strings[1]);
            try
            {
                url = new URL(strings[0]);
                connexionHTTPS = (HttpsURLConnection) url.openConnection();
                connexionHTTPS.setRequestProperty("Accept", "application/json");
                connexionHTTPS.setRequestProperty("Authorization", "key " + strings[1]);
                int codeReponse = connexionHTTPS.getResponseCode();
                if (codeReponse != HttpURLConnection.HTTP_OK)
                {
                    Log.d(TAG,"<AsyncTaskHTTP> Erreur code réponse = " + codeReponse);
                    return contenu;
                }

                in = new BufferedInputStream(connexionHTTPS.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String ligne = null;
                while(true)
                {
                    try
                    {
                        if (!((ligne = r.readLine()) != null))
                            break;
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    sb.append(ligne);
                    sb.append("\r\n");
                }
                contenu = sb.toString();
                Log.d(TAG,"<AsyncTaskHTTP> données récupérées : " + contenu.length() + " octet(s)");
            }
            catch (MalformedURLException e)
            {
                Log.d(TAG,"MalformedURLException");
                e.printStackTrace();
            }
            catch (IOException e)
            {
                Log.d(TAG,"IOException");
                e.printStackTrace();
            }
            finally
            {
                if (connexionHTTPS != null)
                    connexionHTTPS.disconnect();
            }

            return contenu;
        }

        @Override
        protected void onPostExecute(String contenu)
        {
            Log.d(TAG,"<AsyncTaskHTTP> onPostExecute()");
            if(contenu == null)
            {
                chargement = false; // pas de chargement
                if(callback != null)
                {
                    callback.onError();
                }
                return;
            }

            StringReader sin = new StringReader(contenu);
            if(sin != null)
            {
                chargement = true;

                if(chargement)
                {
                    if(callback != null)
                    {
                        callback.onLoad(contenu);
                    }
                }
            }
        }
    }
}
