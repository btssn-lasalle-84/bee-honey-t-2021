#include "communication.h"
#include "ihmpc.h"
#include <QSettings>
#include <QDir>
#include <QMessageBox>
#include <QJsonDocument>
#include <QDateTime>
#include <QDebug>


/**
 * @file communication.cpp
 * @brief La définition de la classe Communication
 * $LastChangedRevision: 171 $
 * $LastChangedDate: 2021-06-16 16:13:02 +0200 (mer. 16 juin 2021) $
 */

/**
 * @brief Constructeur de la classe Communication
 * @param parent
 * @fn Communication::Communication
 */
Communication::Communication(QObject *parent) : QObject(parent), clientMqtt(new QMqttClient(this)), abonnementMqtt(nullptr)
{
    qDebug() << Q_FUNC_INFO;

    configurer();
}

/**
 * @brief Destructeur de la classe Communication
 * @fn Communication::~Communication
 */
Communication::~Communication()
{
    this->deconnecter();
    this->sauvegarder();
    qDebug() << Q_FUNC_INFO;
}

// Services ==============================================================
/**
 * @brief Méthode qui retourne une valeur booléene true si le client Mqtt est connecté
 * @fn Communication::estConnecte
 * @return bool : une valeur booléene true ou false
 */
bool Communication::estConnecte() const
{
    if(clientMqtt->state() == QMqttClient::Connected)
        return true;
    return false;
}

/**
 * @brief Méthode qui connecte le client Mqtt (par défaut) et permet la récupération du message (à l'aide de la connection)
 * @fn Communication::connecter
 * @return void : un message dans le journal
 */
void Communication::connecter()
{
    if(clientMqtt->state() != QMqttClient::Connected)
    {
        qDebug() << Q_FUNC_INFO << hostname << port << username << password;

        clientMqtt->setHostname(hostname);
        clientMqtt->setPort(port);
        clientMqtt->setUsername(username);
        clientMqtt->setPassword(password);

        QString message = "Connexion " + hostname;
        emit messageJournal(message);
        clientMqtt->connectToHost();

        connect(clientMqtt, SIGNAL(messageReceived(QByteArray,QMqttTopicName)), this, SLOT(recevoirMessage(QByteArray,QMqttTopicName)));
    }
}

/**
 * @brief Méthode qui connecte le client Mqtt à partir des paramètres (surcharge de connecter())
 * @fn Communication::connecter
 * @param hostname
 * @param port
 * @param username
 * @param password
 * @return void
 */
void Communication::connecter(QString hostname, int port, QString username, QString password)
{
    if(clientMqtt->state() != QMqttClient::Connected)
    {
        this->hostname = hostname;
        this->port = port;
        this->username = username;
        this->password = password;

        this->connecter();
    }
}

/**
 * @brief Méthode qui déconnecte puis connecte le client Mqtt
 * @fn Communication::reconnecter
 * @return void
 */
void Communication::reconnecter()
{
    qDebug() << Q_FUNC_INFO;
    this->deconnecter();
    this->connecter();
}

/**
 * @brief Méthode qui déconnecte puis connecte le client Mqtt à partir des paramètres (surcharge de reconnecter())
 * @fn Communication::reconnecter
 * @param hostname
 * @param port
 * @param username
 * @param password
 * @return void
 */
void Communication::reconnecter(QString hostname, int port, QString username, QString password)
{
    this->deconnecter();
    this->connecter(hostname, port, username, password);
}

/**
 * @brief Méthode qui déconnecte le client Mqtt
 * @fn Communication::deconnecter
 * @return void : un message dans le journal
 */
void Communication::deconnecter()
{
    if(clientMqtt->state() != QMqttClient::Connected)
        return;
    clientMqtt->disconnectFromHost();
    QString message = "Déconnexion " + hostname;
    emit messageJournal(message);
    disconnect(clientMqtt, SIGNAL(messageReceived(QByteArray,QMqttTopicName)), this, SLOT(recevoirMessage(QByteArray,QMqttTopicName)));
    qDebug() << Q_FUNC_INFO;
}

/**
 * @brief Méthode qui abonne le client Mqtt à un topic , à partir du deviceID
 * @fn Communication::abonner
 * @param deviceID
 * @return bool : une valeur booléene true ou false , et un message dans le journal
 */
bool Communication::abonner(QString deviceID)
{
    if(clientMqtt->state() != QMqttClient::Connected)
        return false;

    // Exemple de deviceID : ruche-1-sim, ruche-2-sim
    // Format de topic TTN : ApplicationID/devices/DeviceID/up
    // Exemple de topic : rucher/devices/ruche-1-sim/up

    QString topic = username + "/devices/" + deviceID + "/up";
    qDebug() << Q_FUNC_INFO << deviceID << topic;
    QMqttTopicFilter topicFilter(topic.toLatin1());
    abonnementMqtt = clientMqtt->subscribe(topicFilter);
    QString message = "Abonnement " + deviceID;
    emit messageJournal(message);

    if (!abonnementMqtt)
    {
        QMessageBox::critical(0, "Erreur", "Impossible de s'abonner !");
        return false;
    }

    mesure.initialiser();

    return true;
}

/**
 * @brief Méthode qui désabonne le client Mqtt , à partir du deviceID
 * @fn Communication::desabonner
 * @param deviceID
 * @return void : un message dans le journal
 */
void Communication::desabonner(QString deviceID)
{
    if(clientMqtt->state() != QMqttClient::Connected)
        return;
    /**
     * @warning Si un client se déconnecte sans se désabonner, le serveur MQTT stockera tous les messages et les publiera lors de la prochaine reconnexion.
     */
    QString topic = username + "/devices/" + deviceID + "/up";
    qDebug() << Q_FUNC_INFO << deviceID << topic;
    QMqttTopicFilter topicFilter(topic.toLatin1());
    clientMqtt->unsubscribe(topicFilter);
    QString message = "Désabonnement " + deviceID;
    emit messageJournal(message);
}
/**
 * @brief Methode qui retourne le nom d'hote
 * @fn Communication::getHostname
 * @return QString
 */
QString Communication::getHostname() const
{
    return this->hostname;
}
/**
 * @brief Methode qui retourne le nom d'utilisateur
 * @fn Communication::getUsername
 * @return QString
 */
QString Communication::getUsername() const
{
    return this->username;
}
/**
 * @brief Methode qui retourne le mot de passe
 * @fn Communication::getPassword
 * @return
 */
QString Communication::getPassword() const
{
    return this->password;
}

/**
 * @brief Méthode qui configure le client Mqtt , à partir du fichier beehoneyt.ini
 * @fn Communication::configurer
 * @return void
 */
void Communication::configurer()
{
    QSettings settings(QDir::currentPath() + "/beehoneyt.ini", QSettings::IniFormat);

    settings.beginGroup("TTN");
    hostname = settings.value("Hostname").toString();
    port = settings.value("Port").toInt();
    username = settings.value("Username").toString();
    password = settings.value("Password").toString();
    settings.endGroup();
    settings.sync();
    //qDebug() << Q_FUNC_INFO << hostname << port <<  username << password;

    connect(clientMqtt, SIGNAL(stateChanged(ClientState)), this, SLOT(changerEtat()));
}

/**
 * @brief Méthode qui sauvegarde la configuration du client Mqtt , dans le fichier beehoneyt.ini
 * @fn Communication::sauvegarder
 * @return void : sauvegarde des parametres dans beehoneyt.ini
 */
void Communication::sauvegarder()
{
    QSettings settings(QDir::currentPath() + "/beehoneyt.ini", QSettings::IniFormat);

    settings.beginGroup("TTN");
    settings.setValue("Hostname" , hostname);
    settings.setValue("Port" , port);
    settings.setValue("Username" , username);
    settings.setValue("Password" , password);
    settings.sync();

    //qDebug() << Q_FUNC_INFO << hostname << port <<  username << password;
}

/**
 * @brief Méthode qui signal l'état du client Mqtt à l'IHM
 * @fn Communication::changerEtat
 * @return void : un etat connecté ou deconnecté dans l'IHM, et un message dans le journal
 */
void Communication::changerEtat()
{
    qDebug() << Q_FUNC_INFO << clientMqtt->state();
    QString message;
    switch (clientMqtt->state())
    {
        case QMqttClient::Disconnected:
            qDebug() << Q_FUNC_INFO << "Déconnecté";
            emit ttnDeconnecte();
            message = "Déconnecté " + hostname;
            emit messageJournal(message);
            break;
        case QMqttClient::Connecting:
            qDebug() << Q_FUNC_INFO << "En cours de connexion";
            break;
        case QMqttClient::Connected:
            qDebug() << Q_FUNC_INFO << "Connecté";
            emit ttnConnecte();
            sauvegarder();
            message = "Connecté " + hostname;
            emit messageJournal(message);
            break;
        default:
            break;
    }
}

/**
 * @brief Méthode qui reçoit et traite le message du topic, et signal les nouvelles mesures à l'IHM
 * @fn Communication::recevoirMessage
 * @param messageRecu, topic
 * @return void : des nouvelles mesures dans l'IHM, et un message dans le journal
 */
void Communication::recevoirMessage(const QByteArray &messageRecu, const QMqttTopicName &topic)
{
    QJsonDocument documentJSON = QJsonDocument::fromJson(messageRecu);
    QJsonObject objetJSON = documentJSON.object();

    // Extraction du deviceID et du port
    QString deviceID = objetJSON.value(QString("dev_id")).toString();
    int port = objetJSON.value(QString("port")).toInt();

    qDebug() << Q_FUNC_INFO << "topic" << topic;
    qDebug() << Q_FUNC_INFO << "DeviceID " << deviceID;
    //qDebug() << Q_FUNC_INFO << "message" << messageRecu;
    qDebug() << Q_FUNC_INFO << "port" << port;

    // Journalisation

    QString message = "Message reçu de " + deviceID + " :\n" + messageRecu;
    emit messageJournal(message);    // Extrait d'un message reçu
    // messageRecu contient du JSON
    // les clés à extraire :
    //  - port : 1, 2 ou 3 précise ce que contient le payload_fields
    //  - payload_fields : données du port
    //  - horodatage (timestamp - nombre de secondes depuis le 1 janvier 1970)
    // Exemple :
    //  "port":2
    //  "payload_fields":{"humiditeExt":32,"humiditeInt":32,"pression":1003,"temperatureExt":17.4,"temperatureInt":23.1}
    //  "timestamp":3459190155,"time":"2021-04-18T08:11:35Z"

    /**
        {
          "app_id":"rucher",
          "dev_id":"ruche-1-sim",
          ...,
          "port":2,
          "payload_fields":
          {
            "humiditeExt":32,
            "humiditeInt":32,
            "pression":1003,
            "temperatureExt":17.4,
            "temperatureInt":23.1
          },
          "metadata":
          {
            "time":"2021-04-18T08:11:35.714925102Z",
            "frequency":868.3,
            "modulation":"LORA",
            ...
            "gateways":
            [
              {
                "gtw_id":"btssn-lasalle-84",
                "timestamp":3459190155,
                "time":"2021-04-18T08:11:35Z",
                ...
              }
            ]
          }
        }
    */

    // Extraction de l'horodatage
    QJsonObject messageMetadata = objetJSON.value(QString("metadata")).toObject();
    QString valeurHorodatage = messageMetadata.value(QString("time")).toString();
    //qDebug() << Q_FUNC_INFO << "valeurHorodatage" << valeurHorodatage;
    QDateTime horodatage = QDateTime::fromString(valeurHorodatage, Qt::ISODate).toLocalTime();
    qDebug() << Q_FUNC_INFO << "horodatage" << horodatage.toString("dd/MM/yyyy HH:mm:ss");
    mesure.setHorodatage(horodatage.toString("dd/MM/yyyy HH:mm:ss"));

    // Extraction des mesures
    QJsonObject messageMesures = objetJSON.value(QString("payload_fields")).toObject();
    QJsonValue valeurPoids, valeurHumiditeExt, valeurHumiditeInt, valeurTemperatureExt, valeurTemperatureInt, valeurPression;

    switch (port)
    {
    case PORT_TTN_POIDS:
        valeurPoids = messageMesures.value(QString("poids")).toDouble();

        mesure.setPoids(valeurPoids.toDouble());

        emit nouvellesMesures(mesure);
        break;
    case PORT_TTN_ENVIRONNEMENT:
        valeurHumiditeExt = messageMesures.value(QString("humiditeExt")).toDouble();
        valeurHumiditeInt = messageMesures.value(QString("humiditeInt")).toDouble();
        valeurTemperatureExt = messageMesures.value(QString("temperatureExt")).toDouble();
        valeurTemperatureInt = messageMesures.value(QString("temperatureInt")).toDouble();
        valeurPression = messageMesures.value(QString("pression")).toDouble();

        mesure.setHumiditeExterieure(valeurHumiditeExt.toDouble());
        mesure.setHumiditeInterieure(valeurHumiditeInt.toDouble());
        mesure.setTemperatureExterieure(valeurTemperatureExt.toDouble());
        mesure.setTemperatureInterieure(valeurTemperatureInt.toDouble());
        mesure.setPression(valeurPression.toDouble());
        //qDebug() << Q_FUNC_INFO << "mesure" << mesure.getTemperatureInterieure() << mesure.getTemperatureExterieure() << mesure.getHumiditeInterieure() << mesure.getHumiditeExterieure() << mesure.getPression();

        emit nouvellesMesures(mesure);
        break;
    default:
        break;
    }
}
