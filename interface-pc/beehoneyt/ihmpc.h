#ifndef IHMPC_H
#define IHMPC_H

#include <QtWidgets>
// Pour les graphiques
#include <QtCharts>
#include "mesureruche.h"
#include "alertes.h"

/**
 * @file        ihmpc.h
 * @brief       La déclaration de la classe IHMPc
 * @details     La classe principale
 * @author      MHADI Zakariya
 * @version     1.0
 * @date        2021
 * $LastChangedRevision: 172 $
 * $LastChangedDate: 2021-06-16 21:31:10 +0200 (mer. 16 juin 2021) $
 */

/**
 * @enum OngletIHM
 * @brief Définit les numéros des onglets de l'IHM
 */
enum OngletIHM
{
    ONGLET_MESURES,         //!< L'onglet de visualisation des mesures des ruches
    ONGLET_GRAPHIQUES,      //!< L'onglet de visualisation des graphiques des ruches
    ONGLET_JOURNAL          //!< L'onglet de visualisation du log
};

/**
 * @enum TypeGraphique
 * @brief Définit les différents types de graphiques
 */
enum TypeGraphique
{
    GRAPHIQUE_POIDS,
    GRAPHIQUE_PRESSION,
    GRAPHIQUE_TEMPERATURE_EXTERIEURE,
    GRAPHIQUE_TEMPERATURE_INTERIEURE,
    GRAPHIQUE_HUMIDITE_EXTERIEURE,
    GRAPHIQUE_HUMIDITE_INTERIEURE
};

#define TEMPERATURE_EXTERIEURE_MAX  30
#define TEMPERATURE_EXTERIEURE_MIN  0
#define TEMPERATURE_INTERIEURE_MAX  40
#define TEMPERATURE_INTERIEURE_MIN  10
#define HUMIDITE_EXTERIEURE_MAX     80
#define HUMIDITE_EXTERIEURE_MIN     20
#define HUMIDITE_INTERIEURE_MAX     90
#define HUMIDITE_INTERIEURE_MIN     10
#define PRESSION_MAX                1050
#define PRESSION_MIN                1000
#define POIDS_MAX                   25
#define POIDS_MIN                   1

QT_BEGIN_NAMESPACE
namespace Ui { class IHMPc; }
QT_END_NAMESPACE

class Communication;
class Ruche;
class Historique;
class Alertes;

/**
 * @class IHMPc
 * @brief Déclaration de la classe IHMPc
 * @details Cette classe s'occupe de l'affichage de l'application beehoneyt
 * $LastChangedRevision: 172 $
 * $LastChangedDate: 2021-06-16 21:31:10 +0200 (mer. 16 juin 2021) $
*/

class IHMPc : public QMainWindow
{
        Q_OBJECT

    public:
        IHMPc(QWidget *parent = nullptr);
        ~IHMPc();

    private:
        Ui::IHMPc *ui; //!< Association vers l'interface utilisateur (Qt Designer)
        Communication *communicationTTN; //!< Pointeur sur l'objet Communication
        Historique *historique; //!< Pointeur sur l'objet Historique
        QVector<Ruche*> rucher; //!< Les ruches de l'apiculteur
        int indexRucheSelectionnee; //!< L'index courant de la ruche sélectionnée

        QComboBox *listeGraphiques;
        QChartView *graphique;  //!< widget pour afficher le graphe
        QChart *graphePoids;                  //!< la représentation du graphe
        QLineSeries *courbePoids;             //!< Les données sous forme de courbe
        QChart *graphePression;               //!< la représentation du graphe
        QLineSeries *courbePression;          //!< Les données sous forme de courbe
        QLineSeries *courbeTemperatureExt;    //!< Les données sous forme de courbe
        QChart *grapheTemperatureExt;         //!< la représentation du graphe
        QLineSeries *courbeTemperatureInt;    //!< Les données sous forme de courbe
        QChart *grapheTemperatureInt;         //!< la représentation du graphe
        QLineSeries *courbeHumiditeExt;       //!< Les données sous forme de courbe
        QChart *grapheHumiditeExt;            //!< la représentation du graphe
        QLineSeries *courbeHumiditeInt;       //!< Les données sous forme de courbe
        QChart *grapheHumiditeInt;            //!< la représentation du graphe

        Alertes alertes;

        void initialiserIHM();
        void creerRucher();
        void initialiserGestionEvenements();
        void initialiserAlertes();

        void creerGraphique();
        void initialiserGraphiquePoids();
        void initialiserGraphiquePression();
        void initialiserGraphiqueTemperatureExterieure();
        void initialiserGraphiqueTemperatureInterieure();
        void initialiserGraphiqueHumiditeExterieure();
        void initialiserGraphiqueHumiditeInterieure();


    signals:

    private slots:
        void afficherTemperatureInterieure(double temperatureInterieure = 0., QString uniteTemperature = QString("   °C")) const;
        void afficherTemperatureExterieure(double temperatureExterieure = 0., QString uniteTemperature = QString("   °C")) const;
        void afficherHumiditeInterieure(double humiditeInterieure = 0., QString uniteHumidite = QString("   %")) const;
        void afficherHumiditeExterieure(double humiditeExterieure = 0., QString uniteHumidite = QString("   %")) const;
        void afficherPoids(double poids = 0., QString unitePoids = QString("   Kg")) const;
        void afficherPression(double pression = 0., QString unitePression = QString("   hPa")) const;
        void afficherEtatConnecte();
        void afficherEtatDeconnecte();
        void verifierAlertes(MesureRuche mesure);
        void afficherAlerteTemperatureExterieure(double temperature);
        void afficherAlerteTemperatureInterieure(double temperature);
        void afficherAlerteHumiditeExterieure(double humidite);
        void afficherAlerteHumiditeInterieure(double humidite);
        void afficherAlertePoids(double poids);
        void afficherAlertePression(double pression);
        void ouvrirFenetreAjouter();
        void supprimerRuche();
        void configurerAlertes();
        void configurerConnexionTTN();
        void afficherHorodatage(QString horodatage);
        void afficherNouvellesMesures(MesureRuche mesure);
        void selectionnerRuche();
        void gererConnexionTTN();
        void gererChangementOnglet(int);
        void journaliser(QString message);
        void selectionnerGraphique();
        void rechargerGraphique();
        void afficherGraphiquePoids();
        void afficherGraphiquePression();
        void afficherGraphiqueTemperatureExterieure();
        void afficherGraphiqueTemperatureInterieure();
        void afficherGraphiqueHumiditeExterieure();
        void afficherGraphiqueHumiditeInterieure();

};

#endif // IHMPC_H
