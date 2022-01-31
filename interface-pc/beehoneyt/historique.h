#ifndef HISTORIQUE_H
#define HISTORIQUE_H

#include <QObject>
#include <QNetworkAccessManager>
#include <QNetworkReply>

/**
 * @file        historique.h
 * @brief       La déclaration de la classe Historique
 * @author      VAIRA Thierry
 * @author      MHADI Zakariya
 * @version     1.1
 * @date        2021
 * $LastChangedRevision$
 * $LastChangedDate$
 */

/**
 * @class Historique
 * @brief Déclaration de la classe Historique
 * @details Cette classe s'occupe de la récupération des données de l'historique Data Storage (TTN)
 * $LastChangedRevision$
 * $LastChangedDate$
*/
class Historique : public QObject
{
    Q_OBJECT
public:
    explicit Historique(QObject *parent = nullptr);

    void setAuthentification(QString url, QString cle);
    void recupererDonnees(QString deviceID, QString duree="");
    QVector<QStringList> getMesuresPoids() const;
    QVector<QStringList> getMesuresPression() const;
    QVector<QStringList> getMesuresTemperatureExterieure() const;
    QVector<QStringList> getMesuresTemperatureInterieure() const;
    QVector<QStringList> getMesuresHumiditeExterieure() const;
    QVector<QStringList> getMesuresHumiditeInterieure() const;

private:
    QString url;
    QString cle;
    QString deviceID;
    QNetworkAccessManager *manager;
    QNetworkReply *reply;
    QVector<QStringList> donneesPoids;
    QVector<QStringList> mesuresPoids;
    QVector<QStringList> donneesPression;
    QVector<QStringList> mesuresPression;
    QVector<QStringList> donneesTemperatureExterieure;
    QVector<QStringList> mesuresTemperatureExterieure;
    QVector<QStringList> donneesTemperatureInterieure;
    QVector<QStringList> mesuresTemperatureInterieure;
    QVector<QStringList> donneesHumiditeExterieure;
    QVector<QStringList> mesuresHumiditeExterieure;
    QVector<QStringList> donneesHumiditeInterieure;
    QVector<QStringList> mesuresHumiditeInterieure;

    void traiterDonneesPoids();
    void traiterDonneesPression();
    void traiterDonneesTemperatureExterieure();
    void traiterDonneesTemperatureInterieure();
    void traiterDonneesHumiditeExterieure();
    void traiterDonneesHumiditeInterieure();

public slots:
    void replyFinished(QNetworkReply *reply);

signals:
    void recuperationTerminee();

    void messageJournal(QString message);
};

#endif // HISTORIQUE_H
