#ifndef ALERTES_H
#define ALERTES_H

/**
 * @file alertes.h
 * @brief La déclaration de la classe Alertes
 * $LastChangedRevision: 150 $
 * $LastChangedDate: 2021-06-07 10:36:44 +0200 (lun. 07 juin 2021) $
 */

class Alertes
{
    public:
        Alertes(double temperatureExterieureMax = 0., double temperatureExterieureMin = 0., double temperatureInterieureMax = 0., double temperatureInterieureMin = 0.,
                double humiditeExterieureMax = 0., double humiditeExterieureMin = 0., double humiditeInterieureMax = 0., double humiditeInterieureMin = 0.,
                double pressionMax = 0., double pressionMin = 0.,double poidsMax = 0., double poidsMin = 0.);
        Alertes(const Alertes & alerte);
        ~Alertes();

        // Services
        bool verifierTemperatureExterieure(double temperature) const;
        bool verifierTemperatureInterieure(double temperature) const;
        bool verifierHumiditeExterieure(double humidite) const;
        bool verifierHumiditeInterieure(double humidite) const;
        bool verifierPoids(double poids) const;
        bool verifierPression(double pression) const;

        // Mutateurs
        void setTemperatureInterieureMax(double temperatureInterieureMax);
        void setTemperatureInterieureMin(double temperatureInterieureMin);
        void setTemperatureExterieureMax(double temperatureExterieureMax);
        void setTemperatureExterieureMin(double temperatureExterieureMin);
        void setHumiditeInterieureMax(double humiditeInterieureMax);
        void setHumiditeInterieureMin(double humiditeInterieureMin);
        void setHumiditeExterieureMax(double humiditeExterieureMax);
        void setHumiditeExterieureMin(double humiditeExterieureMin);
        void setPressionMax(double pressionMax);
        void setPressionMin(double pressionMin);
        void setPoidsMax(double poidsMax);
        void setPoidsMin(double poidsMin);

        // Accesseurs
        double getTemperatureInterieureMax() const;
        double getTemperatureInterieureMin() const;
        double getTemperatureExterieureMax() const;
        double getTemperatureExterieureMin() const;
        double getHumiditeInterieureMax() const;
        double getHumiditeInterieureMin() const;
        double getHumiditeExterieureMax() const;
        double getHumiditeExterieureMin() const;
        double getPressionMax() const;
        double getPressionMin() const;
        double getPoidsMax() const;
        double getPoidsMin() const;

    private:
        double temperatureExterieureMax;
        double temperatureExterieureMin;
        double temperatureInterieureMax;
        double temperatureInterieureMin;
        double humiditeExterieureMax;
        double humiditeExterieureMin;
        double humiditeInterieureMax;
        double humiditeInterieureMin;
        double pressionMax;
        double pressionMin;
        double poidsMax;
        double poidsMin;
};

#endif // ALERTES_H
