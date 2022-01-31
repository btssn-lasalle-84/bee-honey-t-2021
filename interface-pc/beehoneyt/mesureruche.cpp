#include "communication.h"
#include "mesureruche.h"
#include "ihmpc.h"
#include <QDebug>
#include <QObject>

/**
* @file mesureruche.cpp
* @brief La définition de la classe MesureRuche
* $LastChangedRevision: 116 $
* $LastChangedDate: 2021-05-26 10:40:13 +0200 (mer. 26 mai 2021) $
*/

/**
 * @brief Constructeur de la classe MesureRuche
 * @fn MesureRuche::MesureRuche
 * @param parent
 * @param temperatureInterieure
 * @param temperatureExterieure
 * @param humiditeInterieure
 * @param humiditeExterieure
 * @param pression
 * @param poids
 */
MesureRuche::MesureRuche(QObject *parent, double temperatureInterieure , double temperatureExterieure , double humiditeInterieure , double humiditeExterieure,
                         double pression , double poids) : QObject(parent),
                                                           temperatureInterieure(temperatureInterieure),
                                                           temperatureExterieure(temperatureExterieure),
                                                           humiditeInterieure(humiditeInterieure),
                                                           humiditeExterieure(humiditeExterieure),
                                                           pression(pression),
                                                           poids(poids)
{
    qDebug() << Q_FUNC_INFO;
}

/**
 * @brief Constructeur de la classe MesureRuche par recopie
 * @fn MesureRuche::MesureRuche
 * @param mesureRuche
 */
MesureRuche::MesureRuche(const MesureRuche &mesureRuche) : QObject(mesureRuche.parent()),
                                                           temperatureInterieure(mesureRuche.temperatureInterieure),
                                                           temperatureExterieure(mesureRuche.temperatureExterieure),
                                                           humiditeInterieure(mesureRuche.humiditeInterieure),
                                                           humiditeExterieure(mesureRuche.humiditeExterieure),
                                                           pression(mesureRuche.pression),
                                                           poids(mesureRuche.poids),
                                                           horodatage(mesureRuche.horodatage)
{
    qDebug() << Q_FUNC_INFO;
}

/**
 * @brief Destructeur de la classe MesureRuche
 * @fn MesureRuche::~MesureRuche
 */
MesureRuche::~MesureRuche()
{
    qDebug() << Q_FUNC_INFO;
}

// Services ==============================================================
/**
 * @brief Methode qui intialise les attributs
 * @fn MesureRuche::initialiser
 * @return void
 */
void MesureRuche::initialiser()
{
    temperatureInterieure = 0.;
    temperatureExterieure = 0.;
    humiditeInterieure = 0.;
    humiditeExterieure = 0.;
    pression = 0.;
    poids = 0.;
    horodatage = "";
}

// Mutateurs =============================================================
/**
 * @brief Methode qui change la valeur de la temperature interieure
 * @fn MesureRuche::setTemperatureInterieure
 * @param temperatureInterieure
 * @return void
 */
void MesureRuche::setTemperatureInterieure(double temperatureInterieure)
{
    this->temperatureInterieure=temperatureInterieure;
}

/**
 * @brief Methode qui change la valeur de la temperature exterieure
 * @fn MesureRuche::setTemperatureExterieure
 * @param temperatureExterieure
 * @return void
 */
void MesureRuche::setTemperatureExterieure(double temperatureExterieure)
{
    this->temperatureExterieure=temperatureExterieure;
}

/**
 * @brief Methode qui change la valeur de l'humidite interieure
 * @fn MesureRuche::setHumiditeInterieure
 * @param humiditeInterieure
 * @return void
 */
void MesureRuche::setHumiditeInterieure(double humiditeInterieure)
{
    this->humiditeInterieure=humiditeInterieure;
}

/**
 * @brief Methode qui change la valeur de l'humidite exterieure
 * @fn MesureRuche::setHumiditeExterieure
 * @param humiditeExterieure
 * @return void
 */
void MesureRuche::setHumiditeExterieure(double humiditeExterieure)
{
    this->humiditeExterieure=humiditeExterieure;
}

/**
 * @brief Methode qui change la valeur du poids
 * @fn MesureRuche::setPoids
 * @param poids
 * @return void
 */
void MesureRuche::setPoids(double poids)
{
    this->poids=poids;
}

/**
 * @brief Methode qui change la valeur de la pression
 * @fn MesureRuche::setPression
 * @param pression
 * @return void
 */
void MesureRuche::setPression(double pression)
{
    this->pression=pression;
}

/**
 * @brief Methode qui change la valeur de l'horodatage
 * @fn MesureRuche::setHorodatage
 * @param horodatage
 */
void MesureRuche::setHorodatage(QString horodatage)
{
    this->horodatage=horodatage;
}

// Accesseurs ============================================================
/**
 * @brief Methode qui retourne la valeur de la temperature interieure
 * @fn MesureRuche::getTemperatureInterieure
 * @return double
 */
double MesureRuche::getTemperatureInterieure() const
{
    return this->temperatureInterieure;
}

/**
 * @brief Methode qui retourne la valeur de la temperature exterieure
 * @fn MesureRuche::getTemperatureExterieure
 * @return double
 */
double MesureRuche::getTemperatureExterieure() const
{
    return this->temperatureExterieure;
}

/**
 * @brief Methode qui retourne la valeur de l'humidite interieure
 * @fn MesureRuche::getHumiditeInterieure
 * @return double
 */
double MesureRuche::getHumiditeInterieure() const
{
    return this->humiditeInterieure;
}

/**
 * @brief Methode qui retourne la valeur de l'humidite exterieure
 * @fn MesureRuche::getHumiditeExterieure
 * @return
 */
double MesureRuche::getHumiditeExterieure() const
{
    return this->humiditeExterieure;
}

/**
 * @brief Methode qui retourne la valeur du poids
 * @fn MesureRuche::getPoids
 * @return double
 */
double MesureRuche::getPoids() const
{
    return this->poids;
}

/**
 * @brief Methode qui retourne la valeur de la pression
 * @fn MesureRuche::getPression
 * @return double
 */
double MesureRuche::getPression() const
{
    return this->pression;
}

/**
 * @brief Methode qui retourne la valeur de l'horodatage
 * @fn MesureRuche::getHorodatage
 * @return QString
 */
QString MesureRuche::getHorodatage() const
{
    return this->horodatage;
}
