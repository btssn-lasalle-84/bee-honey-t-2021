#include "alertes.h"
#include <QDebug>

/**
* @file alertes.cpp
* @brief La définition de la classe Alertes
* $LastChangedRevision: 170 $
* $LastChangedDate: 2021-06-16 15:06:12 +0200 (mer. 16 juin 2021) $
*/

/**
 * @brief Constructeur de la classe Alertes par défaut
 * @fn Alertes::Alertes
 * @param temperatureExterieureMax
 * @param temperatureExterieureMin
 * @param temperatureInterieureMax
 * @param temperatureInterieureMin
 * @param humiditeExterieureMax
 * @param humiditeExterieureMin
 * @param humiditeInterieureMax
 * @param humiditeInterieureMin
 * @param poidsMax
 * @param poidsMin
 */
Alertes::Alertes(double temperatureExterieureMax, double temperatureExterieureMin, double temperatureInterieureMax, double temperatureInterieureMin, double humiditeExterieureMax,
                 double humiditeExterieureMin, double humiditeInterieureMax, double humiditeInterieureMin, double pressionMax, double pressionMin, double poidsMax, double poidsMin)
                                                                                   : temperatureExterieureMax(temperatureExterieureMax),
                                                                                     temperatureExterieureMin(temperatureExterieureMin),
                                                                                     temperatureInterieureMax(temperatureInterieureMax),
                                                                                     temperatureInterieureMin(temperatureInterieureMin),
                                                                                     humiditeExterieureMax(humiditeExterieureMax),
                                                                                     humiditeExterieureMin(humiditeExterieureMin),
                                                                                     humiditeInterieureMax(humiditeInterieureMax),
                                                                                     humiditeInterieureMin(humiditeInterieureMin),
                                                                                     pressionMax(pressionMax),
                                                                                     pressionMin(pressionMin),
                                                                                     poidsMax(poidsMax),
                                                                                     poidsMin(poidsMin)
{
    qDebug() << Q_FUNC_INFO;
}
/**
 * @brief Constructeur de la classe Alertes par recopie
 * @param alerte
 * @fn Alertes::Alertes
 */
Alertes::Alertes(const Alertes &alerte) : temperatureExterieureMax(alerte.temperatureExterieureMax),
                                          temperatureExterieureMin(alerte.temperatureExterieureMin),
                                          temperatureInterieureMax(alerte.temperatureInterieureMax),
                                          temperatureInterieureMin(alerte.temperatureInterieureMin),
                                          humiditeExterieureMax(alerte.humiditeExterieureMax),
                                          humiditeExterieureMin(alerte.humiditeExterieureMin),
                                          humiditeInterieureMax(alerte.humiditeInterieureMax),
                                          humiditeInterieureMin(alerte.humiditeInterieureMin),
                                          poidsMax(alerte.poidsMax),
                                          poidsMin(alerte.poidsMin)
{
    qDebug() << Q_FUNC_INFO;
}
/**
 * @brief Destructeur de la classe Alertes
 * @fn Alertes::~Alertes
 */
Alertes::~Alertes()
{
    qDebug() << Q_FUNC_INFO;
}

// Services ==============================================================
/**
 * @brief Methode qui verifie la temperature exterieure
 * @fn Alertes::verifierTemperature
 * @param temperature
 * @return bool
 */
bool Alertes::verifierTemperatureExterieure(double temperature) const
{
    if(temperature>=this->getTemperatureExterieureMin() && temperature<=getTemperatureExterieureMax())
    {
        return true;
    }
    else
    {
        return false;
    }
}
/**
 * @brief Methode qui verifie la temperature interieure
 * @fn Alertes::verifierTemperatureInterieure
 * @param temperature
 * @return bool
 */
bool Alertes::verifierTemperatureInterieure(double temperature) const
{
    if(temperature>=this->getTemperatureInterieureMin() && temperature<=getTemperatureInterieureMax())
    {
        return true;
    }
    else
    {
        return false;
    }
}

/**
 * @brief Methode qui verifie l'humidite exterieure
 * @fn Alertes::verifierHumidite
 * @param humidite
 * @return bool
 */
bool Alertes::verifierHumiditeExterieure(double humidite) const
{
    if(humidite>=this->getHumiditeExterieureMin() && humidite<=getHumiditeExterieureMax())
    {
        return true;
    }
    else
    {
        return false;
    }
}
/**
 * @brief Methode qui verifie l'humidite interieure
 * @fn Alertes::verifierHumiditeInterieure
 * @param humidite
 * @return bool
 */
bool Alertes::verifierHumiditeInterieure(double humidite) const
{
    if(humidite>=this->getHumiditeInterieureMin() && humidite<=getHumiditeInterieureMax())
    {
        return true;
    }
    else
    {
        return false;
    }
}

bool Alertes::verifierPoids(double poids) const
{
    if(poids>=this->getPoidsMin() && poids<=this->getPoidsMax())
    {
        return true;
    }
    else
    {
        return false;
    }
}

/**
 * @brief Methode qui verifie le poids
 * @fn Alertes::verifierPoids
 * @return bool
 */
bool Alertes::verifierPression(double pression) const
{
    if(pression>=this->getPressionMin() && pression<=this->getPressionMax())
    {
        return true;
    }
    else
    {
        return false;
    }
}



// Mutateurs =============================================================
/**
 * @brief Methode qui change la temperature interieure maximale
 * @fn Alertes::setTemperatureInterieureMax
 * @param temperatureInterieureMax
 * @return void
 */
void Alertes::setTemperatureInterieureMax(double temperatureInterieureMax)
{
    this->temperatureInterieureMax=temperatureInterieureMax;
}
/**
 * @brief Methode qui change la temperature interieure minimum
 * @fn Alertes::setTemperatureInterieureMin
 * @param temperatureInterieureMin
 * @return void
 */
void Alertes::setTemperatureInterieureMin(double temperatureInterieureMin)
{
    this->temperatureInterieureMin=temperatureInterieureMin;
}
/**
 * @brief Methode qui change la temperature exterieure maximale
 * @fn Alertes::setTemperatureExterieureMax
 * @param temperatureExterieureMax
 * @return void
 */
void Alertes::setTemperatureExterieureMax(double temperatureExterieureMax)
{
    this->temperatureExterieureMax=temperatureExterieureMax;
}
/**
 * @brief Methode qui change la temperature exterieure minimale
 * @fn Alertes::setTemperatureExterieureMin
 * @param temperatureExterieureMin
 * @return void
 */
void Alertes::setTemperatureExterieureMin(double temperatureExterieureMin)
{
    this->temperatureExterieureMin=temperatureExterieureMin;
}
/**
 * @brief Methode qui change l'humidite interieure maximale
 * @fn Alertes::setHumiditeInterieureMax
 * @param humiditeInterieureMax
 * @return void
 */
void Alertes::setHumiditeInterieureMax(double humiditeInterieureMax)
{
    this->humiditeInterieureMax=humiditeInterieureMax;
}
/**
 * @brief Methode qui change l'humidite interieure minimale
 * @fn Alertes::setHumiditeInterieureMin
 * @param humiditeInterieureMin
 * @return void
 */
void Alertes::setHumiditeInterieureMin(double humiditeInterieureMin)
{
    this->humiditeInterieureMin=humiditeInterieureMin;
}
/**
 * @brief Methode qui change l'humidite exterieure maximale
 * @fn Alertes::setHumiditeExterieureMax
 * @param humiditeExterieureMax
 * @return void
 */
void Alertes::setHumiditeExterieureMax(double humiditeExterieureMax)
{
    this->humiditeExterieureMax=humiditeExterieureMax;
}
/**
 * @brief Methode qui change l'humidite exterieure minimale
 * @fn Alertes::setHumiditeExterieureMin
 * @param humiditeExterieureMin
 * @return void
 */
void Alertes::setHumiditeExterieureMin(double humiditeExterieureMin)
{
    this->humiditeExterieureMin=humiditeExterieureMin;
}
/**
 * @brief Methode qui change la pression maximale
 * @fn Alertes::setPressionMax
 * @param pressionMax
 * @return void
 */
void Alertes::setPressionMax(double pressionMax)
{
    this->pressionMax=pressionMax;
}
/**
 * @brief Methode qui change la pression minimale
 * @fn Alertes::setPressionMin
 * @param pressionMin
 * @return void
 */
void Alertes::setPressionMin(double pressionMin)
{
    this->pressionMin=pressionMin;
}
/**
 * @brief Methode qui change le poids maximale
 * @fn Alertes::setPoidsMax
 * @param poidsMax
 * @return void
 */
void Alertes::setPoidsMax(double poidsMax)
{
    this->poidsMax=poidsMax;
}
/**
 * @brief Methode qui change le poids minimale
 * @fn Alertes::setPoidsMin
 * @param poidsMin
 * @return void
 */
void Alertes::setPoidsMin(double poidsMin)
{
    this->poidsMin=poidsMin;
}

// Accesseurs ============================================================
/**
 * @brief Methode qui retourne la temperature interieure maximale
 * @fn Alertes::getTemperatureInterieureMax
 * @return double
 */
double Alertes::getTemperatureInterieureMax() const
{
    return this->temperatureInterieureMax;
}
/**
 * @brief Methode qui retourne la temperature interieure minimale
 * @fn Alertes::getTemperatureInterieureMin
 * @return double
 */
double Alertes::getTemperatureInterieureMin() const
{
    return this->temperatureInterieureMin;
}
/**
 * @brief Methode qui retourne la temperature exterieure maximale
 * @fn Alertes::getTemperatureExterieureMax
 * @return double
 */
double Alertes::getTemperatureExterieureMax() const
{
    return this->temperatureExterieureMax;
}
/**
 * @brief Methode qui retourne la temperature exterieure minimale
 * @fn Alertes::getTemperatureExterieureMin
 * @return double
 */
double Alertes::getTemperatureExterieureMin() const
{
    return this->temperatureExterieureMin;
}
/**
 * @brief Methode qui retourne l'humidite interieure maximale
 * @fn Alertes::getHumiditeInterieureMax
 * @return double
 */
double Alertes::getHumiditeInterieureMax() const
{
    return this->humiditeInterieureMax;
}
/**
 * @brief Methode qui retourne l'humidite interieure minimale
 * @fn Alertes::getHumiditeInterieureMin
 * @return double
 */
double Alertes::getHumiditeInterieureMin() const
{
    return this->humiditeInterieureMin;
}
/**
 * @brief Methode qui retourne l'humidite exterieure maximale
 * @fn Alertes::getHumiditeExterieureMax
 * @return double
 */
double Alertes::getHumiditeExterieureMax() const
{
    return this->humiditeExterieureMax;
}
/**
 * @brief Methode qui retourne l'humidite exterieure minimale
 * @fn Alertes::getHumiditeExterieureMin
 * @return double
 */
double Alertes::getHumiditeExterieureMin() const
{
    return this->humiditeExterieureMin;
}
/**
 * @brief Methode qui retourne la pression maximale
 * @fn Alertes::getPressionMax
 * @return
 */
double Alertes::getPressionMax() const
{
    return this->pressionMax;
}
/**
 * @brief Methode qui retourne la pression minimale
 * @fn Alertes::getPressionMin
 * @return
 */
double Alertes::getPressionMin() const
{
    return this->pressionMin;
}
/**
 * @brief Methode qui retourne le poids maximale
 * @fn Alertes::getPoidsMax
 * @return double
 */
double Alertes::getPoidsMax() const
{
    return this->poidsMax;
}
/**
 * @brief Methode qui retourne le poids minimale
 * @fn Alertes::getPoidsMin
 * @return double
 */
double Alertes::getPoidsMin() const
{
    return this->poidsMin;
}
