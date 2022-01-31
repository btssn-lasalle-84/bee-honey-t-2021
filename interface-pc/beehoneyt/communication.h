#ifndef COMMUNICATION_H
#define COMMUNICATION_H

/**
 * @file communication.h
 * @brief La déclaration de la classe Communication
 * $LastChangedRevision: 117 $
 * $LastChangedDate: 2021-05-26 11:48:36 +0200 (mer. 26 mai 2021) $
 */

#include <QObject>
#include <QtMqtt/QtMqtt>
#include <QtMqtt/QMqttClient>
#include "mesureruche.h"

#define PORT_TTN_POIDS          1
#define PORT_TTN_ENVIRONNEMENT  2

class Communication : public QObject
{
        Q_OBJECT
    public:
        Communication(QObject *parent = nullptr);
        ~Communication();

        bool estConnecte() const;
        void connecter();
        void connecter(QString hostname, int port, QString username, QString password);
        void reconnecter();
        void reconnecter(QString hostname, int port, QString username, QString password);
        void deconnecter();
        bool abonner(QString deviceID);
        void desabonner(QString deviceID);        
        QString getHostname() const;
        QString getUsername() const;
        QString getPassword() const;

    private:
        QMqttClient *clientMqtt;
        QMqttSubscription *abonnementMqtt;
        QString hostname;
        int port;
        QString username;
        QString password;
        MesureRuche mesure;

        void configurer();
        void sauvegarder();

    public slots:
        void changerEtat();
        void recevoirMessage(const QByteArray &message, const QMqttTopicName &topic);

    signals:
        void ttnConnecte();
        void ttnDeconnecte();
        void messageJournal(QString message);
        void nouvellesMesures(MesureRuche mesure);
};

#endif // COMMUNICATION_H
