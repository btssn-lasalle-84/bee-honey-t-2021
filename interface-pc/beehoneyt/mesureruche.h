#ifndef MESURERUCHE_H
#define MESURERUCHE_H

#include <QObject>
#include <QString>

/**
 * @file mesureruche.h
 * @brief La déclaration de la classe MesureRuche
 * $LastChangedRevision: 116 $
 * $LastChangedDate: 2021-05-26 10:40:13 +0200 (mer. 26 mai 2021) $
 */

class MesureRuche : public QObject
{
        Q_OBJECT
    public:
        MesureRuche(QObject *parent = 0, double temperatureInterieure = 0., double temperatureExterieure = 0., double humiditeInterieure = 0., double humiditeExterieure = 0., double pression = 0., double poids = 0.);
        MesureRuche(const MesureRuche & mesureRuche);
        ~MesureRuche();

        // Services
        void initialiser();

        // Mutateurs
        void setTemperatureInterieure(double temperatureInterieure);
        void setTemperatureExterieure(double temperatureExterieure);
        void setHumiditeInterieure(double humiditeInterieure);
        void setHumiditeExterieure(double humiditeExterieure);
        void setPoids(double poids);
        void setPression(double pression);
        void setHorodatage(QString horodatage);

        // Accesseurs
        double getTemperatureInterieure() const;
        double getTemperatureExterieure() const;
        double getHumiditeInterieure() const;
        double getHumiditeExterieure() const;
        double getPoids() const;
        double getPression() const;
        QString getHorodatage() const;

    private:
        double temperatureInterieure;
        double temperatureExterieure;
        double humiditeInterieure;
        double humiditeExterieure;
        double pression;
        double poids;
        QString horodatage;
};

Q_DECLARE_METATYPE(MesureRuche)

#endif // MESURERUCHE_H
