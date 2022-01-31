#include "historique.h"
#include <QJsonDocument>
#include <QJsonObject>
#include <QJsonArray>
#include <QDateTime>

/**
 * @file        historique.cpp
 * @brief       La définition de la classe Historique
 * @author      VAIRA Thierry
 * @author      MHADI Zakariya
 * @version     1.1
 * @date        2021
 * $LastChangedRevision$
 * $LastChangedDate$
 */

/**
 * @brief Constructeur par defaut de l'objet Historique
 * @fn storique::Historique
 * @param parent
 */
Historique::Historique(QObject *parent) : QObject(parent)
{
    manager = new QNetworkAccessManager(this);
    connect(manager, SIGNAL(finished(QNetworkReply*)), this, SLOT(replyFinished(QNetworkReply*)));
}
/**
 * @brief Methode qui modifie l'URL et la clé
 * @fn Historique::setAuthentification
 * @param url
 * @param cle
 */
void Historique::setAuthentification(QString url, QString cle)
{
    this->url = url;
    this->cle = cle;
    qDebug() << Q_FUNC_INFO << url << cle;
}
/**
 * @brief Methode qui recupère toutes les donnees (mesures des capteurs) de la ruche selectionnee (deviceID)
 * @fn Historique::recupererDonnees
 * @param deviceID
 * @param duree
 */
void Historique::recupererDonnees(QString deviceID, QString duree)
{
    if(url.isEmpty() || cle.isEmpty())
        return;

    this->deviceID = deviceID;

    QString strURL;
    if(deviceID.isEmpty())
        strURL = url + "/api/v2/query";
    else
        strURL = url + "/api/v2/query/" + deviceID;
    if(!duree.isEmpty()) // par défaut 1 heure
        strURL += "?last=" + duree;

    QUrl URL(strURL);
    QNetworkRequest request;

    //"https://rucher.data.thethingsnetwork.org/api/v2/query"
    request.setUrl(URL);
    //--header 'Accept: application/json'
    request.setRawHeader("Accept", "application/json");
    //--header 'Authorization: key ttn-account-v2.vC-aqMRnLLzGkNjODWgy81kLqzxBPAT8_mE-L7U2C_w'
    request.setRawHeader("Authorization", "key " + cle.toLatin1());
    //qDebug() << Q_FUNC_INFO << request.url();

    manager->get(request);
}
/**
 * @brief Methode qui retourne un conteneur dynamique, contenant les mesures du poids
 * @fn Historique::getMesuresPoids
 * @return QVector<QStringList>
 */
QVector<QStringList> Historique::getMesuresPoids() const
{
    return mesuresPoids;
}
/**
 * @brief Methode qui retourne un conteneur dynamique, contenant les mesures de la pression
 * @fn Historique::getMesuresPression
 * @return QVector<QStringList>
 */
QVector<QStringList> Historique::getMesuresPression() const
{
    return mesuresPression;
}
/**
 * @brief Methode qui retourne un conteneur dynamique, contenant les mesures de la temperature exterieure
 * @fn Historique::getMesuresTemperatureExterieure
 * @return QVector<QStringList>
 */
QVector<QStringList> Historique::getMesuresTemperatureExterieure() const
{
    return mesuresTemperatureExterieure;
}
/**
 * @brief Methode qui retourne un conteneur dynamique, contenant les mesures de la temperature interieure
 * @fn Historique::getMesuresTemperatureInterieure
 * @return QVector<QStringList>
 */
QVector<QStringList> Historique::getMesuresTemperatureInterieure() const
{
    return mesuresTemperatureInterieure;
}
/**
 * @brief Methode qui retourne un conteneur dynamique, contenant les mesures de l'humidite exterieure
 * @fn Historique::getMesuresHumiditeExterieure
 * @return QVector<QStringList>
 */
QVector<QStringList> Historique::getMesuresHumiditeExterieure() const
{
    return mesuresHumiditeExterieure;
}
/**
 * @brief Methode qui retourne un conteneur dynamique, contenant les mesures de l'humidite interieure
 * @fn Historique::getMesuresHumiditeInterieure
 * @return QVector<QStringList>
 */
QVector<QStringList> Historique::getMesuresHumiditeInterieure() const
{
    return mesuresHumiditeInterieure;
}
/**
 * @brief Methode qui traite les donnees et emet un signal afin de generer des graphique a partor des mesures recuperees
 * @fn Historique::replyFinished
 * @param reply
 */
void Historique::replyFinished(QNetworkReply *reply)
{
    QByteArray datas = reply->readAll();
    //QString donneesRecuperees(datas);
    //qDebug() << Q_FUNC_INFO << donneesRecuperees;

    /**
      * Exemple :
      * "[{\"charge\":null,\"device_id\":\"ruche-1-sim\",\"humiditeExt\":null,\"humiditeInt\":null,\"poids\":22.4,\"pression\":null,\"raw\":\"AOA=\",\"temperatureExt\":null,\"temperatureInt\":null,\"time\":\"2021-05-26T08:26:53.789498119Z\"},{\"charge\":null,\"device_id\":\"ruche-2-sim\",\"humiditeExt\":null,\"humiditeInt\":null,\"poids\":14,\"pression\":null,\"raw\":\"AIw=\",\"temperatureExt\":null,\"temperatureInt\":null,\"time\":\"2021-05-26T08:27:03.901713955Z\"},{\"charge\":null,\"device_id\":\"ruche-1-sim\",\"humiditeExt\":29,\"humiditeInt\":29,\"poids\":null,\"pression\":1027,\"raw\":\"ARYdALQdBAM=\", ...]\n"
      */

    //emit messageJournal(donneesRecuperees);

    QJsonDocument documentJSON = QJsonDocument::fromJson(datas);
    if(documentJSON.isArray())
    {
        QJsonArray tableauJSON = documentJSON.array();
        //qDebug() << Q_FUNC_INFO << "deviceID" << deviceID;
        //qDebug() << Q_FUNC_INFO << "Nb valeurs récupérées" << tableauJSON.size();

        QString message("Nombre de valeurs récupérées : ");
        int valeursRecuperees = tableauJSON.size();
        message.append(QString::number(valeursRecuperees));
        emit messageJournal(message);

        QJsonValue poids, pression, temperatureExt, temperatureInt, humiditeExt, humiditeInt;
        QStringList donnees;

        donneesPoids.clear();
        donneesPression.clear();
        donneesTemperatureExterieure.clear();
        donneesTemperatureInterieure.clear();
        donneesHumiditeExterieure.clear();
        donneesHumiditeInterieure.clear();
        for(int i = 0; i < tableauJSON.count(); i++)
        {
            QJsonValue valeur = tableauJSON.at(i);
            if(valeur.isObject())
            {
                QJsonObject objet = valeur.toObject();
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
                if(objet.value(QString("device_id")).toString() == deviceID)
                {
                    poids = objet.value(QString("poids"));
                    pression = objet.value(QString("pression"));
                    temperatureExt = objet.value(QString("temperatureExt"));
                    temperatureInt = objet.value(QString("temperatureInt"));
                    humiditeExt = objet.value(QString("humiditeExt"));
                    humiditeInt = objet.value(QString("humiditeInt"));

                    //qDebug() << Q_FUNC_INFO << "temperature Ext = " << temperatureExt;

                    if(!poids.isNull())
                    {
                        donnees.clear();
                        QDateTime horodatage = QDateTime::fromString(objet.value(QString("time")).toString(), Qt::ISODate).toLocalTime();
                        donnees << horodatage.toString("HH") << QString::number(poids.toDouble()) << horodatage.toString("dd/MM/yyyy") << horodatage.toString("HH:mm:ss");
                        donneesPoids.push_back(donnees);
                    }
                    else if(!pression.isNull())
                    {
                        donnees.clear();
                        QDateTime horodatagePression = QDateTime::fromString(objet.value(QString("time")).toString(), Qt::ISODate).toLocalTime();
                        donnees << horodatagePression.toString("HH") << QString::number(pression.toDouble()) << horodatagePression.toString("dd/MM/yyyy") << horodatagePression.toString("HH:mm:ss");
                        qDebug() << donnees;
                        donneesPression.push_back(donnees);

                        donnees.clear();
                        QDateTime horodatageTemperatureExt = QDateTime::fromString(objet.value(QString("time")).toString(), Qt::ISODate).toLocalTime();
                        donnees << horodatageTemperatureExt.toString("HH") << QString::number(temperatureExt.toDouble()) << horodatageTemperatureExt.toString("dd/MM/yyyy") << horodatageTemperatureExt.toString("HH:mm:ss");
                        donneesTemperatureExterieure.push_back(donnees);

                        donnees.clear();
                        QDateTime horodatageTemperatureInt = QDateTime::fromString(objet.value(QString("time")).toString(), Qt::ISODate).toLocalTime();
                        donnees << horodatageTemperatureInt.toString("HH") << QString::number(temperatureInt.toDouble()) << horodatageTemperatureInt.toString("dd/MM/yyyy") << horodatageTemperatureInt.toString("HH:mm:ss");
                        donneesTemperatureInterieure.push_back(donnees);

                        donnees.clear();
                        QDateTime horodatageHumiditeExt = QDateTime::fromString(objet.value(QString("time")).toString(), Qt::ISODate).toLocalTime();
                        donnees << horodatageHumiditeExt.toString("HH") << QString::number(humiditeExt.toDouble()) << horodatageHumiditeExt.toString("dd/MM/yyyy") << horodatageHumiditeExt.toString("HH:mm:ss");
                        donneesHumiditeExterieure.push_back(donnees);

                        donnees.clear();
                        QDateTime horodatageHumiditeInt = QDateTime::fromString(objet.value(QString("time")).toString(), Qt::ISODate).toLocalTime();
                        donnees << horodatageHumiditeInt.toString("HH") << QString::number(humiditeInt.toDouble()) << horodatageHumiditeInt.toString("dd/MM/yyyy") << horodatageHumiditeInt.toString("HH:mm:ss");
                        donneesHumiditeInterieure.push_back(donnees);
                    }
              }
           }
        }

        //qDebug() << Q_FUNC_INFO << donneesTemperatureExterieure;
        //qDebug() << Q_FUNC_INFO << "Nb mesures temperature exterieure" << donneesTemperatureExterieure.size();

        traiterDonneesPoids();
        traiterDonneesPression();
        traiterDonneesTemperatureExterieure();
        traiterDonneesTemperatureInterieure();
        traiterDonneesHumiditeExterieure();
        traiterDonneesHumiditeInterieure();

        emit recuperationTerminee();
    }
}
/**
 * @brief Methode qui traite les donnees du poids
 * @fn Historique::traiterDonneesPoids
 */
void Historique::traiterDonneesPoids()
{
    if(donneesPoids.isEmpty())
        return;

    QStringList mesure;
    double somme = 0.;
    int nbMesures = 0;

    mesuresPoids.clear();
    for(int heure = 0; heure < 24; heure++)
    {
        somme = 0.;
        nbMesures = 0;
        for(QVector<QStringList>::iterator it = donneesPoids.begin(); it != donneesPoids.end(); it++)
        {

            if((*it).at(0).toInt() == heure)
            {
                //qDebug() << Q_FUNC_INFO << heure << (*it).at(0) << (*it).at(1).toDouble();
                somme += (*it).at(1).toDouble();
                nbMesures++;
            }
            else if((*it).at(0).toInt() < heure)
            {
                if(donneesPoids.erase(it) == donneesPoids.end())
                    break;
            }
        }
        if(nbMesures > 0)
        {
            //qDebug() << Q_FUNC_INFO << "heure" << heure << "Nb mesures" << nbMesures << "somme" << somme << "moyenne" << (somme/(double)nbMesures);
            mesure.clear();
            mesure << QString::number(heure) << QString::number((somme/(double)nbMesures));
            mesuresPoids.push_back(mesure);
        }
    }

    //qDebug() << Q_FUNC_INFO << mesuresPoids;
    //qDebug() << Q_FUNC_INFO << "Nb mesures" << mesuresPoids.size();
    //qDebug() << Q_FUNC_INFO << "Nb mesures poids" << donneesPoids.size();
}
/**
 * @brief Methode qui traite les donnees de la pression
 * @fn Historique::traiterDonneesPression
 */
void Historique::traiterDonneesPression()
{
    if(donneesPression.isEmpty())
        return;

    QStringList mesure;
    double somme = 0.;
    int nbMesures = 0;

    mesuresPression.clear();
    for(int heure = 0; heure < 24; heure++)
    {
        somme = 0.;
        nbMesures = 0;
        for(QVector<QStringList>::iterator it = donneesPression.begin(); it != donneesPression.end(); it++)
        {

            if((*it).at(0).toInt() == heure)
            {
                //qDebug() << Q_FUNC_INFO << heure << (*it).at(0) << (*it).at(1).toDouble();
                somme += (*it).at(1).toDouble();
                nbMesures++;
            }
            else if((*it).at(0).toInt() < heure)
            {
                if(donneesPression.erase(it) == donneesPression.end())
                    break;
            }
        }
        if(nbMesures > 0)
        {
            //qDebug() << Q_FUNC_INFO << "heure" << heure << "Nb mesures" << nbMesures << "somme" << somme << "moyenne" << (somme/(double)nbMesures);
            mesure.clear();
            mesure << QString::number(heure) << QString::number((somme/(double)nbMesures));
            mesuresPression.push_back(mesure);
        }
    }

    //qDebug() << Q_FUNC_INFO << mesuresPression;
    //qDebug() << Q_FUNC_INFO << "Nb mesures" << mesuresPression.size();
    //qDebug() << Q_FUNC_INFO << "Nb mesures pression" << donneesPression.size();
}
/**
 * @brief Methode qui traite les donnees de la temperature exterieure
 * @fn Historique::traiterDonneesTemperatureExterieure
 */
void Historique::traiterDonneesTemperatureExterieure()
{
    if(donneesTemperatureExterieure.isEmpty())
        return;

    QStringList mesure;
    double somme = 0.;
    int nbMesures = 0;

    mesuresTemperatureExterieure.clear();
    for(int heure = 0; heure < 24; heure++)
    {
        somme = 0.;
        nbMesures = 0;
        for(QVector<QStringList>::iterator it = donneesTemperatureExterieure.begin(); it != donneesTemperatureExterieure.end(); it++)
        {

            if((*it).at(0).toInt() == heure)
            {
                //qDebug() << Q_FUNC_INFO << heure << (*it).at(0) << (*it).at(1).toDouble();
                somme += (*it).at(1).toDouble();
                nbMesures++;
            }
            else if((*it).at(0).toInt() < heure)
            {
                if(donneesTemperatureExterieure.erase(it) == donneesTemperatureExterieure.end())
                    break;
            }
        }
        if(nbMesures > 0)
        {
            //qDebug() << Q_FUNC_INFO << "heure" << heure << "Nb mesures" << nbMesures << "somme" << somme << "moyenne" << (somme/(double)nbMesures);
            mesure.clear();
            mesure << QString::number(heure) << QString::number((somme/(double)nbMesures));
            mesuresTemperatureExterieure.push_back(mesure);
        }
    }

    //qDebug() << Q_FUNC_INFO << mesuresTemperatureExterieure;
    qDebug() << Q_FUNC_INFO << "Nb mesures" << mesuresTemperatureExterieure.size();
    //qDebug() << Q_FUNC_INFO << "Nb mesures temperature exterieure" << donneesTemperatureExterieure.size();
}
/**
 * @brief Methode qui traite les donnees de la temperature interieure
 * @fn Historique::traiterDonneesTemperatureInterieure
 */
void Historique::traiterDonneesTemperatureInterieure()
{
    if(donneesTemperatureInterieure.isEmpty())
        return;

    QStringList mesure;
    double somme = 0.;
    int nbMesures = 0;

    mesuresTemperatureInterieure.clear();
    for(int heure = 0; heure < 24; heure++)
    {
        somme = 0.;
        nbMesures = 0;
        for(QVector<QStringList>::iterator it = donneesTemperatureInterieure.begin(); it != donneesTemperatureInterieure.end(); it++)
        {

            if((*it).at(0).toInt() == heure)
            {
                //qDebug() << Q_FUNC_INFO << heure << (*it).at(0) << (*it).at(1).toDouble();
                somme += (*it).at(1).toDouble();
                nbMesures++;
            }
            else if((*it).at(0).toInt() < heure)
            {
                if(donneesTemperatureInterieure.erase(it) == donneesTemperatureInterieure.end())
                    break;
            }
        }
        if(nbMesures > 0)
        {
            //qDebug() << Q_FUNC_INFO << "heure" << heure << "Nb mesures" << nbMesures << "somme" << somme << "moyenne" << (somme/(double)nbMesures);
            mesure.clear();
            mesure << QString::number(heure) << QString::number((somme/(double)nbMesures));
            mesuresTemperatureInterieure.push_back(mesure);
        }
    }

    //qDebug() << Q_FUNC_INFO << mesuresTemperatureInterieure;
    //qDebug() << Q_FUNC_INFO << "Nb mesures" << mesuresTemperatureInterieure.size();
    //qDebug() << Q_FUNC_INFO << "Nb mesures temeperature interieure" << donneesTemperatureInterieure.size();
}
/**
 * @brief Methode qui traite les donnees de l'humidite exterieure
 * @fn Historique::traiterDonneesHumiditeExterieure
 */
void Historique::traiterDonneesHumiditeExterieure()
{
    if(donneesHumiditeExterieure.isEmpty())
        return;

    QStringList mesure;
    double somme = 0.;
    int nbMesures = 0;

    mesuresHumiditeExterieure.clear();
    for(int heure = 0; heure < 24; heure++)
    {
        somme = 0.;
        nbMesures = 0;
        for(QVector<QStringList>::iterator it = donneesHumiditeExterieure.begin(); it != donneesHumiditeExterieure.end(); it++)
        {
            if((*it).at(0).toInt() == heure)
            {
                //qDebug() << Q_FUNC_INFO << heure << (*it).at(0) << (*it).at(1).toDouble();
                somme += (*it).at(1).toDouble();
                nbMesures++;
            }
            else if((*it).at(0).toInt() < heure)
            {
                if(donneesHumiditeExterieure.erase(it) == donneesHumiditeExterieure.end())
                    break;
            }
        }
        if(nbMesures > 0)
        {
            //qDebug() << Q_FUNC_INFO << "heure" << heure << "Nb mesures" << nbMesures << "somme" << somme << "moyenne" << (somme/(double)nbMesures);
            mesure.clear();
            mesure << QString::number(heure) << QString::number((somme/(double)nbMesures));
            mesuresHumiditeExterieure.push_back(mesure);
        }
    }

    //qDebug() << Q_FUNC_INFO << mesuresHumiditeExterieure;
    //qDebug() << Q_FUNC_INFO << "Nb mesures" << mesuresHumiditeExterieure.size();
    //qDebug() << Q_FUNC_INFO << "Nb mesures humidite exterieure" << donneesHumiditeExterieure.size();
}
/**
 * @brief Methode qui traite les donnees de l'humidite interieure
 * @fn Historique::traiterDonneesHumiditeInterieure
 */
void Historique::traiterDonneesHumiditeInterieure()
{
    if(donneesHumiditeInterieure.isEmpty())
        return;

    QStringList mesure;
    double somme = 0.;
    int nbMesures = 0;

    mesuresHumiditeInterieure.clear();
    for(int heure = 0; heure < 24; heure++)
    {
        somme = 0.;
        nbMesures = 0;
        for(QVector<QStringList>::iterator it = donneesHumiditeInterieure.begin(); it != donneesHumiditeInterieure.end(); it++)
        {

            if((*it).at(0).toInt() == heure)
            {
                //qDebug() << Q_FUNC_INFO << heure << (*it).at(0) << (*it).at(1).toDouble();
                somme += (*it).at(1).toDouble();
                nbMesures++;
            }
            else if((*it).at(0).toInt() < heure)
            {
                if(donneesHumiditeInterieure.erase(it) == donneesHumiditeInterieure.end())
                    break;
            }
        }
        if(nbMesures > 0)
        {
            //qDebug() << Q_FUNC_INFO << "heure" << heure << "Nb mesures" << nbMesures << "somme" << somme << "moyenne" << (somme/(double)nbMesures);
            mesure.clear();
            mesure << QString::number(heure) << QString::number((somme/(double)nbMesures));
            mesuresHumiditeInterieure.push_back(mesure);
        }
    }

    //qDebug() << Q_FUNC_INFO << mesuresHumiditeInterieure;
    //qDebug() << Q_FUNC_INFO << "Nb mesures" << mesuresHumiditeInterieure.size();
    //qDebug() << Q_FUNC_INFO << "Nb mesures humidite interieure" << donneesHumiditeInterieure.size();
}
