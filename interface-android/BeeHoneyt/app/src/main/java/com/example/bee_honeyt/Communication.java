package com.example.bee_honeyt;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @class Communication
 * @brief Déclaration de la classe Communication
 * @details Permet la communication MQTT avec le serveur The Things Network
 * @author Thierry Vaira
 * $LastChangedRevision: 166 $
 * $LastChangedDate: 2021-06-14 09:28:36 +0200 (lun. 14 juin 2021) $
 */

/**
 * @class Communication
 * @brief Classe pour gérer la communication MQTT
 */
public class Communication
{
    /**
     * Constantes
     */
    private static final String TAG = "_Communication";

    /**
     * Attributs
     */
    private MqttAndroidClient mqttAndroidClient;
    private Handler handler = null; // pour la communication entre classes

    /**
     * Constantes pour le Handler
     */
    public static final int TTN_CONNECTE = 1;
    public static final int TTN_DECONNECTE = 2;
    public static final int TTN_MESSAGE = 3;

    /**
     * @todo Prévoir une solution pour configurer/stocker les paramètres de connexion TTN
     */
    private String serverUri = "tcp://eu.thethings.network:1883"; //!<  lien vers TTN
    private String clientId = "rucher"; //!< Application ID
    //private String username = "rucher"; //!<  nom d'utilisateur
    //private String password = "ttn-account-v2.a4GRsjloPzQ_4hswG-rmWaO9MzWkAtzCvggWeO2DvL4"; //!<  mot de passe TTN
    private String username = ""; //!<  nom d'utilisateur
    private String password = ""; //!<  mot de passe TTN

    /**
     * @brief Constructeur de la classe Communication
     *
     * @fn Communication::Communication(Context context, final Handler handler)
     * @param context
     * @param handler
     */
    public Communication(Context context, final Handler handler)
    {
        Log.v(TAG, "[Communication()] clientId = " + clientId);
        this.handler = handler;

        creerClientMQTTT(context, handler);

        /**
         * @todo A revoir
         */
        //connecter();
    }

    public String getUsername()
    {
        return this.username;
    }

    public String getPassword()
    {
        return this.password;
    }

    public void setClientId(String username)
    {
        setUsername(username);
    }

    public void setUsername(String username)
    {
        if(username != null)
        {
            this.clientId = username;
            this.username = username;
        }
    }

    public void setPassword(String password)
    {
        if(password != null)
            this.password = password;
    }

    private void creerClientMQTTT(Context context, Handler handler)
    {
        mqttAndroidClient = new MqttAndroidClient(context, serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended()
        {
            @Override
            public void connectComplete(boolean b, String s)
            {
                Log.w(TAG, "[connectComplete()] serverUri = " + s + " connecte = " + mqttAndroidClient.isConnected());
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putInt("etat", TTN_CONNECTE); // clé -> valeur ici etat -> TTN_CONNECTE
                msg.setData(bundle);
                handler.sendMessage(msg);
            }

            @Override
            public void connectionLost(Throwable throwable)
            {
                Log.w(TAG, "[connectionLost()]");
                Message msg = Message.obtain();
                Bundle b = new Bundle();
                b.putInt("etat", TTN_DECONNECTE);
                msg.setData(b);
                handler.sendMessage(msg);
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception
            {
                Log.w(TAG, "[messageArrived()] topic = " + topic + " message = " + mqttMessage.toString());
                Message msg = Message.obtain();
                Bundle b = new Bundle();
                b.putInt("etat", TTN_MESSAGE);
                b.putString("topic", topic);
                b.putString("message", mqttMessage.toString());
                msg.setData(b);
                handler.sendMessage(msg);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken)
            {
                Log.w(TAG, "[deliveryComplete()]");
            }
        });
    }

    /**
     * @brief Installe les fonctions de rappel
     *
     * @fn Communication::setCallback(MqttCallbackExtended callback)
     * @param callback le retour
     */
    public void setCallback(MqttCallbackExtended callback)
    {
        mqttAndroidClient.setCallback(callback);
    }

    /**
     * @brief Connexion au TTN
     *
     * @fn Communication::connecter()
     */
    public void connecter()
    {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());

        try
        {
            Log.d(TAG, "[connecter()] serverUri = " + serverUri + " clientId = " + clientId);
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener()
            {
                @Override
                public void onSuccess(IMqttToken asyncActionToken)
                {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    Log.d(TAG, "[onSuccess()] serverUri = " + serverUri + " clientId = " + clientId);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception)
                {
                    Log.d(TAG, "[onFailure()] serverUri = " + serverUri + " clientId = " + clientId + " exception = " + exception.toString());
                }
            });
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @brief Deconnexion du TTN
     *
     * @fn Communication::deconnecter()
     */
    public void deconnecter()
    {
        if(!estConnecte())
            return;
        Log.d(TAG, "[deconnecter()] serverUri = " + serverUri + " clientId = " + clientId);
        try
        {
            IMqttToken disconToken = mqttAndroidClient.disconnect();
            disconToken.setActionCallback(new IMqttActionListener()
            {
                @Override
                public void onSuccess(IMqttToken asyncActionToken)
                {
                    Log.d(TAG, "[onSuccess()] serverUri = " + serverUri + " clientId = " + clientId);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception)
                {
                    Log.d(TAG, "[onFailure()] serverUri = " + serverUri + " clientId = " + clientId + " exception = " + exception.toString());
                }
            });
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @brief Retourne l'état de connexion au serveur TTN
     *
     * @return boolean
     * @fn Communication::estConnecte()
     */
    public boolean estConnecte()
    {
        Log.w(TAG, "[estConnecte()] " + mqttAndroidClient.isConnected());

        return mqttAndroidClient.isConnected();
    }

    /**
     * @brief S'abonne à un topic (deviceID = ESP32 d'une ruche dans TTN)
     *
     * @fn Communication::souscrireTopic(String topic)
     * @param deviceID le deviceID dans TTN
     */
    public boolean souscrireTopic(String deviceID)
    {
        // Vérifications
        if(mqttAndroidClient == null && !mqttAndroidClient.isConnected())
        {
            return false;
        }

        final String topicTTN = clientId + "/devices/" + deviceID + "/up";
        Log.w(TAG, "[souscrireTopic()] topic = " + topicTTN);
        try
        {
            final boolean[] retour = {false};
            mqttAndroidClient.subscribe(topicTTN, 0, null, new IMqttActionListener()
            {
                @Override
                public void onSuccess(IMqttToken asyncActionToken)
                {
                    Log.w(TAG, "[onSuccess()] topic = " + topicTTN);
                    retour[0] = true;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception)
                {
                    Log.w(TAG, "[onFailure()] topic = " + topicTTN);
                    retour[0] = false;
                }
            });
            return retour[0];
        }
        catch (MqttException e)
        {
            Log.w(TAG, "Erreur topic = " + topicTTN);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @brief S'desabonne à un topic (deviceID = ESP32 d'une ruche dans TTN)
     *
     * @fn Communication::unsubscribe(String deviceID)
     * @param deviceID le deviceID dans TTN
     */
    public boolean unsubscribe(String deviceID)
    {
        if(mqttAndroidClient == null && !mqttAndroidClient.isConnected())
        {
            return false;
        }
        final String topicTTN = clientId + "/devices/" + deviceID + "/up";
        Log.w(TAG, "[unsouscrireTopic()] topic = " + topicTTN);
        try
        {
            final boolean[] retour = {false};
            mqttAndroidClient.unsubscribe(topicTTN, null, null);

            return retour[0];
        }
        catch (MqttException e)
        {
            Log.w(TAG, "Erreur topic = " + topicTTN);
            e.printStackTrace();
            return false;
        }
    }

}
