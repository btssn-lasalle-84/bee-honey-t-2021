#ifndef RUCHE_H
#define RUCHE_H

#include <QObject>
#include <QString>

/**
 * @file ruche.h
 * @brief La déclaration de la classe Ruche
 * $LastChangedRevision: 170 $
 * $LastChangedDate: 2021-06-16 15:06:12 +0200 (mer. 16 juin 2021) $
 */


class Ruche : public QObject
{
        Q_OBJECT
    public:
        Ruche(QString nom, QString deviceID, QObject *parent = nullptr);
        ~Ruche();

        // Services

        // Mutateurs
        void setNom(QString nom);
        //void setDeviceID(QString deviceID);
        //void setTopicTTN(QString topicTTN);
        //void setLatitude(double latitude);
        //void setLongtitude(double longtitude);
        //void setMesureRuche(MesureRuche *mesureRuche);
        //void setAlertes(Alertes *alertes);

        // Accesseurs
        QString getNom() const;
        QString getDeviceID() const;
        //QString getTopicTTN() const;
        //double getLatitude() const;
        //double getLongtitude() const;
        //MesureRuche* getMesureRuche() const;
        //Alertes* getAlertes() const;

    private:
        QString nom;
        QString deviceID;
        //QString topicTTN;
        //double latitude;
        //double longtitude;
};

#endif // RUCHE_H
