#include "ruche.h"
#include <QDebug>

/**
* @file ruche.cpp
* @brief La définition de la classe Ruche
* $LastChangedRevision: 170 $
* $LastChangedDate: 2021-06-16 15:06:12 +0200 (mer. 16 juin 2021) $
*/

/**
 * @brief Constructeur de la classe Ruche
 * @fn Ruche::Ruche
 * @param nom
 * @param deviceID
 * @param parent
 */
Ruche::Ruche(QString nom, QString deviceID, QObject *parent) : QObject(parent), nom(nom), deviceID(deviceID)/*latitude(0.), longtitude(0.),*/
{
    qDebug() << Q_FUNC_INFO << nom << deviceID;
}
/**
 * @brief Destructeur de la classe Ruche
 * @fn Ruche::~Ruche
 */
Ruche::~Ruche()
{
    qDebug() << Q_FUNC_INFO << nom;
}

// Services ==============================================================

// Mutateurs =============================================================
/**
 * @brief Methode qui change le nom
 * @fn Ruche::setNom
 * @param nom
 * @return void
 */
void Ruche::setNom(QString nom)
{
    this->nom = nom;
}

/*
void Ruche::setTopicTTN(QString topicTTN)
{
    // De la forme : applicationID/devices/deviceID/up
}
*/

/**
 * @brief Methode qui change la latitude
 * @fn Ruche::setLatitude
 * @param latitude
 * @return void
 */
/*
void Ruche::setLatitude(double latitude)
{
    this->latitude = latitude;
}
*/

/**
 * @brief Methode qui change la longtitude
 * @fn Ruche::setLongtitude
 * @param longtitude
 * @return void
 */
/*
void Ruche::setLongtitude(double longtitude)
{
    this->longtitude = longtitude;
}
*/

// Accesseurs ============================================================
/**
 * @brief Methode qui retourne le nom
 * @fn Ruche::getNom
 * @return QString
 */
QString Ruche::getNom() const
{
    return this->nom;
}

/*
 * @brief Methode qui retourne le deviceID
 * @fn Ruche::getDeviceID
 * @return QString
 */
QString Ruche::getDeviceID() const
{
    return this->deviceID;
}

/**
 * @brief Methode qui retourne la latitude
 * @fn Ruche::getLatitude
 * @return double
 */
/*
double Ruche::getLatitude() const
{
    return this->latitude;
}
*/

/**
 * @brief Methode qui retourne la longtitude
 * @fn Ruche::getLongtitude
 * @return double
 */
/*
double Ruche::getLongtitude() const
{
    return this->longtitude;
}
*/
