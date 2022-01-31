#include "ihmpc.h"
#include "ui_ihmpc.h"
#include "ruche.h"
#include "communication.h"
#include "historique.h"
#include "alertes.h"
#include <QDebug>
#include <QGraphicsScene>
#include <QGraphicsRectItem>
#include <QGraphicsView>
#include <QObject>

/**
 * @file ihmpc.cpp
 *
 * @brief Définition de la classe IHMPc
 * @author Zakariya MHADI <zakariya.mhadi@gmail.com>
 * @version 1.0
 * @date 2021
 * $LastChangedRevision: 171 $
 * $LastChangedDate: 2021-06-16 16:13:02 +0200 (mer. 16 juin 2021) $
 */

/**
 * @brief Constructeur de la classe IHMPc
 * @fn IHMPc::IHMPc
 * @param parent L'adresse de l'objet parent, si nullptr IHMPc => fenêtre principale
 */
IHMPc::IHMPc(QWidget *parent) : QMainWindow(parent),
                                ui(new Ui::IHMPc),
                                communicationTTN(new Communication(this)),
                                historique(new Historique(this)),
                                indexRucheSelectionnee(-1)
{
    qDebug() << Q_FUNC_INFO;

    initialiserIHM();

    creerRucher();

    initialiserAlertes();

    initialiserGestionEvenements();

    communicationTTN->connecter();

    QString url = "https://" + communicationTTN->getUsername() + ".data.thethingsnetwork.org";
    historique->setAuthentification(url, communicationTTN->getPassword());
}

/**
 * @brief Destructeur de la classe IHMPc
 * @fn IHMPc::~IHMPc
 */
IHMPc::~IHMPc()
{
    if(indexRucheSelectionnee > -1)
        communicationTTN->desabonner(rucher[indexRucheSelectionnee]->getDeviceID());
    delete ui;
    qDebug() << Q_FUNC_INFO;
}

/**
 * @brief Méthode qui affiche la temperature interieure
 * @fn IHMPc::afficherTemperatureInterieure
 * @param temperatureInterieure
 * @param uniteTemperature
 * @return void : une nouvelle valeur de la temperature interieure
 */
void IHMPc::afficherTemperatureInterieure(double temperatureInterieure, QString uniteTemperature) const
{
    ui->lcdTemperatureInterieure->display(temperatureInterieure);
    ui->labelUniteTemperatureInterieure->setText(uniteTemperature);
}

/**
 * @brief Méthode qui affiche la temperature exterieure
 * @fn IHMPc::afficherTemperatureExterieure
 * @param temperatureExterieure
 * @param uniteTemperature
 * @return void : une nouvelle valeur de la temperature exterieure
 */
void IHMPc::afficherTemperatureExterieure(double temperatureExterieure, QString uniteTemperature) const
{
    ui->lcdTemperatureExterieure->display(temperatureExterieure);
    ui->labelUniteTemperatureExterieure->setText(uniteTemperature);
}

/**
 * @brief Méthode qui affiche l'humidite interieure
 * @fn IHMPc::afficherHumiditeInterieure
 * @param humiditeInterieure
 * @param uniteHumidite
 * @return void : une nouvelle valeur de l'humidite interieure
 */
void IHMPc::afficherHumiditeInterieure(double humiditeInterieure, QString uniteHumidite) const
{
    ui->lcdHumiditeInterieure->display(humiditeInterieure);
    ui->labelUniteHumiditeInterieure->setText(uniteHumidite);
}

/**
 * @brief Méthode qui affiche l'humidite interieure
 * @fn IHMPc::afficherHumiditeExterieure
 * @param humiditeExterieure
 * @param uniteHumidite
 * @return void : une nouvelle valeur de l'humidite exterieure
 */
void IHMPc::afficherHumiditeExterieure(double humiditeExterieure, QString uniteHumidite) const
{
    ui->lcdHumiditeExterieure->display(humiditeExterieure);
    ui->labelUniteHumiditeExterieure->setText(uniteHumidite);
}

/**
 * @brief Méthode qui affiche le poids
 * @fn IHMPc::afficherPoids
 * @param poids
 * @param unitePoids
 * @return void : une nouvelle valeur du poids
 */
void IHMPc::afficherPoids(double poids, QString unitePoids) const
{
    ui->lcdPoids->display(poids);
    ui->labelUnitePoids->setText(unitePoids);
}

/**
 * @brief Méthode qui affiche la pression
 * @fn IHMPc::afficherPression
 * @param pression
 * @param unitePression
 * @return void : une nouvelle valeur de la pression
 */
void IHMPc::afficherPression(double pression, QString unitePression) const
{
    ui->lcdPression->display(pression);
    ui->labelUnitePression->setText(unitePression);
}

/**
 * @brief Méthode qui affiche l'état de la connexion (connecté), à partir d'une image en haut à droite
 * @fn IHMPc::afficherEtatConnecte
 * @return void : une led verte
 */
void IHMPc::afficherEtatConnecte()
{
    ui->boutonTTN->setText("Déconnecter");
    ui->labelEtatConnexion->setPixmap(QPixmap(QDir::currentPath() + "/images/boutton_vert.png"));
    qDebug() << Q_FUNC_INFO << "Ruche sélectionnée (connectee)" << ui->listeRuches->currentText() << rucher[ui->listeRuches->currentIndex()]->getDeviceID();
    indexRucheSelectionnee = ui->listeRuches->currentIndex();
    communicationTTN->abonner(rucher[ui->listeRuches->currentIndex()]->getDeviceID());
}

/**
 * @brief Méthode qui affiche l'état de la connexion (deconnecté), à partir d'une image en haut à droite
 * @fn IHMPc::afficherEtatDeconnecte
 * @return void : une led rouge
 */
void IHMPc::afficherEtatDeconnecte()
{
    ui->boutonTTN->setText("Connecter");
    ui->labelHorodatage->setText("");
    ui->lcdTemperatureInterieure->display(0.);
    ui->lcdTemperatureExterieure->display(0.);
    ui->lcdHumiditeInterieure->display(0.);
    ui->lcdHumiditeExterieure->display(0.);
    ui->lcdPoids->display(0.);
    ui->lcdPression->display(0.);
    ui->labelAlerteTemperatureExterieure->setPixmap(QPixmap(QDir::currentPath() + ""));
    ui->labelAlerteTemperatureInterieure->setPixmap(QPixmap(QDir::currentPath() + ""));
    ui->labelAlerteHumiditeExterieure->setPixmap(QPixmap(QDir::currentPath() + ""));
    ui->labelAlerteHumiditeInterieure->setPixmap(QPixmap(QDir::currentPath() + ""));
    ui->labelAlertePoids->setPixmap(QPixmap(QDir::currentPath() + ""));
    ui->labelAlertePression->setPixmap(QPixmap(QDir::currentPath() + ""));

    ui->labelEtatConnexion->setPixmap(QPixmap(QDir::currentPath() + "/images/boutton_rouge.png"));
    qDebug() << Q_FUNC_INFO << "Ruche sélectionnée (deconnectee)" << ui->listeRuches->currentText() << rucher[ui->listeRuches->currentIndex()]->getDeviceID();
}
/**
 * @brief Methode qui verifie si les seuils sont depasses
 * @fn IHMPc::verifierAlertes
 * @param mesure
 */
void IHMPc::verifierAlertes(MesureRuche mesure)
{
    afficherAlerteTemperatureExterieure(mesure.getTemperatureExterieure());
    afficherAlerteTemperatureInterieure(mesure.getTemperatureInterieure());
    afficherAlerteHumiditeExterieure(mesure.getHumiditeExterieure());
    afficherAlerteHumiditeInterieure(mesure.getHumiditeInterieure());
    afficherAlertePoids(mesure.getPoids());
    afficherAlertePression(mesure.getPression());
}
/**
 * @brief Methode qui affiche (ou pas) un temoin lumineu d'alerte pour la temperature exterieure
 * @fn IHMPc::afficherAlerteTemperatureExterieure
 * @param temperature
 */
void IHMPc::afficherAlerteTemperatureExterieure(double temperature)
{
    if(alertes.verifierTemperatureExterieure(temperature))
    {
        ui->labelAlerteTemperatureExterieure->setPixmap(QPixmap(QDir::currentPath() + ""));
    }
    else
    {
        ui->labelAlerteTemperatureExterieure->setPixmap(QPixmap(QDir::currentPath() + "/images/attention-rouge.png"));
    }
}
/**
 * @brief Methode qui affiche (ou pas) un temoin lumineu d'alerte pour la temperature interieure
 * @fn IHMPc::afficherAlerteTemperatureInterieure
 * @param temperature
 */
void IHMPc::afficherAlerteTemperatureInterieure(double temperature)
{
    if(alertes.verifierTemperatureInterieure(temperature))
    {
        ui->labelAlerteTemperatureInterieure->setPixmap(QPixmap(QDir::currentPath() + ""));
    }
    else
    {
        ui->labelAlerteTemperatureInterieure->setPixmap(QPixmap(QDir::currentPath() + "/images/attention-rouge.png"));
    }
}
/**
 * @brief Methode qui affiche (ou pas) un temoin lumineu d'alerte pour l'humidite exterieure
 * @fn IHMPc::afficherAlerteHumiditeExterieure
 * @param humidite
 */
void IHMPc::afficherAlerteHumiditeExterieure(double humidite)
{
    if(alertes.verifierHumiditeExterieure(humidite))
    {
        ui->labelAlerteHumiditeExterieure->setPixmap(QPixmap(QDir::currentPath() + ""));
    }
    else
    {
        ui->labelAlerteHumiditeExterieure->setPixmap(QPixmap(QDir::currentPath() + "/images/attention-rouge.png"));
    }
}
/**
 * @brief Methode qui affiche (ou pas) un temoin lumineu d'alerte pour l'humidite interieure
 * @fn IHMPc::afficherAlerteHumiditeInterieure
 * @param humidite
 */
void IHMPc::afficherAlerteHumiditeInterieure(double humidite)
{
    if(alertes.verifierHumiditeInterieure(humidite))
    {
        ui->labelAlerteHumiditeInterieure->setPixmap(QPixmap(QDir::currentPath() + ""));
    }
    else
    {
        ui->labelAlerteHumiditeInterieure->setPixmap(QPixmap(QDir::currentPath() + "/images/attention-rouge.png"));
    }
}
/**
 * @brief Methode qui affiche (ou pas) un temoin lumineu d'alerte pour le poids
 * @fn IHMPc::afficherAlertePoids
 * @param poids
 */
void IHMPc::afficherAlertePoids(double poids)
{
    if(alertes.verifierPoids(poids))
    {
        ui->labelAlertePoids->setPixmap(QPixmap(QDir::currentPath() + ""));
    }
    else
    {
        ui->labelAlertePoids->setPixmap(QPixmap(QDir::currentPath() + "/images/attention-rouge.png"));
    }
}
/**
 * @brief Methode qui affiche (ou pas) un temoin lumineu d'alerte pour la pression
 * @fn IHMPc::afficherAlertePression
 * @param pression
 */
void IHMPc::afficherAlertePression(double pression)
{
    if(alertes.verifierPression(pression))
    {
        ui->labelAlertePression->setPixmap(QPixmap(QDir::currentPath() + ""));
    }
    else
    {
        ui->labelAlertePression->setPixmap(QPixmap(QDir::currentPath() + "/images/attention-rouge.png"));
    }
}

/**
 * @brief Méthode qui initialise l'IHM
 * @fn IHMPc::initialiserIHM
 * @return void
 */
void IHMPc::initialiserIHM()
{
    ui->setupUi(this);
    ui->ongletIHM->setCurrentIndex(OngletIHM::ONGLET_MESURES);

    creerGraphique();

    showMaximized();
}

/**
 * @brief Méthode qui créer un rucher
 * @fn IHMPc::creerRucher
 * @return void : deux ruches selectionnables
 */
void IHMPc::creerRucher()
{
    // Tests d'un rucher composé de deux ruches (simulateur)
    rucher.push_back(new Ruche("Ruche BeeHoneyT", "bee-honeyt-1", this));
    rucher.push_back(new Ruche("Ruche 1", "ruche-1-sim", this));
    //rucher.push_back(new Ruche("Ruche 2", "ruche-2-sim", this));

    for(int i=0;i<rucher.size();++i)
    {
        ui->listeRuches->addItem(rucher[i]->getNom());
    }

    indexRucheSelectionnee = 0;
    ui->listeRuches->setCurrentIndex(indexRucheSelectionnee);
    ui->labelRucheSelectionnee->setText("Ruche sélectionnée : " + ui->listeRuches->currentText());
}

/**
 * @brief Méthode qui intialise les évenements
 * @fn IHMPc::initialiserGestionEvenements
 * @return void
 */
void IHMPc::initialiserGestionEvenements()
{
    qRegisterMetaType<MesureRuche>();
    connect(communicationTTN, SIGNAL(ttnConnecte()), this, SLOT(afficherEtatConnecte()));
    connect(communicationTTN, SIGNAL(ttnDeconnecte()), this, SLOT(afficherEtatDeconnecte()));
    connect(communicationTTN, SIGNAL(messageJournal(QString)), this, SLOT(journaliser(QString)));
    qRegisterMetaType<MesureRuche>();
    connect(communicationTTN, SIGNAL(nouvellesMesures(MesureRuche)), this, SLOT(afficherNouvellesMesures(MesureRuche)));
    connect(historique, SIGNAL(messageJournal(QString)), this, SLOT(journaliser(QString)));

    connect(ui->boutonTTN, SIGNAL(clicked(bool)), this, SLOT(gererConnexionTTN()));
    connect(ui->boutonAjouter, SIGNAL(clicked(bool)), this, SLOT(ouvrirFenetreAjouter()));
    connect(ui->boutonSupprimer, SIGNAL(clicked(bool)), this, SLOT(supprimerRuche()));
    connect(ui->boutonAlertes, SIGNAL(clicked(bool)), this, SLOT(configurerAlertes()));
    connect(ui->boutonConfigurerTTN, SIGNAL(clicked(bool)), this, SLOT(configurerConnexionTTN()));
    connect(ui->listeRuches, SIGNAL(currentIndexChanged(int)), this, SLOT(selectionnerRuche()));
    connect(ui->ongletIHM, SIGNAL(currentChanged(int)), this, SLOT(gererChangementOnglet(int)));

    connect(ui->listeMesuresGraphiques, SIGNAL(currentIndexChanged(int)), this, SLOT(selectionnerGraphique()));
    connect(ui->bouttonRecharger, SIGNAL(clicked(bool)), this, SLOT(rechargerGraphique()));
    connect(historique, SIGNAL(recuperationTerminee()), this, SLOT(selectionnerGraphique()));
}
/**
 * @brief Methode qui initialise les seuils d'alertes
 * @fn IHMPc::initialiserAlertes
 */
void IHMPc::initialiserAlertes()
{
    alertes.setTemperatureExterieureMax(TEMPERATURE_EXTERIEURE_MAX);
    alertes.setTemperatureExterieureMin(TEMPERATURE_EXTERIEURE_MIN);
    alertes.setTemperatureInterieureMax(TEMPERATURE_INTERIEURE_MAX);
    alertes.setTemperatureInterieureMin(TEMPERATURE_INTERIEURE_MIN);
    alertes.setHumiditeExterieureMax(HUMIDITE_EXTERIEURE_MAX);
    alertes.setHumiditeExterieureMin(HUMIDITE_EXTERIEURE_MIN);
    alertes.setHumiditeInterieureMax(HUMIDITE_INTERIEURE_MAX);
    alertes.setHumiditeInterieureMin(HUMIDITE_INTERIEURE_MIN);
    alertes.setPressionMax(PRESSION_MAX);
    alertes.setPressionMin(PRESSION_MIN);
    alertes.setPoidsMax(POIDS_MAX);
    alertes.setPoidsMin(POIDS_MIN);
}
/**
 * @brief Méthode qui ouvre une fenetre de dialogue pour ajouter une ruche : nom et deviceID
 * @fn IHMPc::ouvrirFenetreAjouter
 * @return void : une ruche en plus
 */
void IHMPc::ouvrirFenetreAjouter()
{
    QString nomNouvelleRuche = QInputDialog::getText(this, "Ajouter", "Veuillez entrer le nom de la Ruche : ", QLineEdit::Normal, QString());
    if (!nomNouvelleRuche.isEmpty())
    {
        QString deviceIDNouvelleRuche = QInputDialog::getText(this, "Ajouter", "Veuillez entrer le deviceID de la Ruche : ", QLineEdit::Normal, QString());
        if(!deviceIDNouvelleRuche.isEmpty())
        {
            rucher.push_back(new Ruche(nomNouvelleRuche, deviceIDNouvelleRuche, this));
            ui->listeRuches->addItem(rucher[rucher.size() - 1]->getNom());
            qDebug() << Q_FUNC_INFO << "Ruche ajoutée" << rucher[rucher.size() - 1]->getNom();
            ui->listeRuches->setCurrentIndex(rucher.size() - 1);
        }
        else
        {
            QMessageBox::critical(this, "Device ID", "Champ de saisi vide !");
        }
    }
    else
    {
        QMessageBox::critical(this, "Nom de la ruche", "Champ de saisi vide !");
    }
    /**
      *@todo Faire une methode pour sauvegarder la nouvelle Ruche
      */
}

/**
 * @brief Méthode qui supprime une ruche, dans la listedeRucheSelectionnee et dans le vector rucher<>
 * @fn IHMPc::supprimerRuche
 * @return void : une ruche en moins
 */
void IHMPc::supprimerRuche()
{
    for(int i=0;i<rucher.size();++i)
    {
        if(rucher[i]->getNom() == ui->listeRuches->currentText())
        {
            qDebug() << Q_FUNC_INFO << "Ruche supprimée" << rucher[i]->getNom();
            communicationTTN->desabonner(rucher[ui->listeRuches->currentIndex()]->getDeviceID());
            indexRucheSelectionnee = -1;
            delete rucher.at(i);
            rucher.remove(i);
            ui->labelRucheSelectionnee->setText("Ruche sélectionnée : aucune");
            ui->listeRuches->removeItem(ui->listeRuches->currentIndex());
            return;
        }
    }
}

/**
 * @brief Methode qui permet la configuration des seuils d'alertes
 * @fn IHMPc::configurerAlertes
 */
void IHMPc::configurerAlertes()
{    

    double temperatureExterieureMax = QInputDialog::getDouble(this, "Configurer les alertes", "Temperature exterieure maximale : ", QLineEdit::Normal, double());
    alertes.setTemperatureExterieureMax(temperatureExterieureMax);
    QString messageTemperature = "Seuils d'alertes changes : temperatures ext. max. = ";
    messageTemperature.append(QString::number(temperatureExterieureMax));

    double temperatureExterieureMin = QInputDialog::getDouble(this, "Configurer les alertes", "Temperature exterieure minimale : ", QLineEdit::Normal, double());
    alertes.setTemperatureExterieureMin(temperatureExterieureMin);
    messageTemperature += ", temperatures ext. min. = ";
    messageTemperature.append(QString::number(temperatureExterieureMin));

    double temperatureInterieureMax = QInputDialog::getDouble(this, "Configurer les alertes", "Temperature interieure maximale : ", QLineEdit::Normal, double());
    alertes.setTemperatureInterieureMax(temperatureInterieureMax);
    messageTemperature += ", temperatures int. max. = ";
    messageTemperature.append(QString::number(temperatureInterieureMax));

    double temperatureInterieureMin = QInputDialog::getDouble(this, "Configurer les alertes", "Temperature interieure minimale : ", QLineEdit::Normal, double());
    alertes.setTemperatureInterieureMin(temperatureInterieureMin);
    messageTemperature += ", temperatures int. min. = ";
    messageTemperature.append(QString::number(temperatureInterieureMin));

    journaliser(messageTemperature);

    double humiditeExterieureMax = QInputDialog::getDouble(this, "Configurer les alertes", "Humidite exterieure maximale : ", QLineEdit::Normal, double());
    alertes.setHumiditeExterieureMax(humiditeExterieureMax);
    QString messageHumidite = "Seuils d'alertes changes : humidite ext. max. = ";
    messageHumidite.append(QString::number(humiditeExterieureMax));

    double humiditeExterieureMin = QInputDialog::getDouble(this, "Configurer les alertes", "Humidite exterieure minimale : ", QLineEdit::Normal, double());
    alertes.setHumiditeExterieureMin(humiditeExterieureMin);
    messageHumidite += ", humidite ext. min. = ";
    messageHumidite.append(QString::number(humiditeExterieureMin));

    double humiditeInterieureMax = QInputDialog::getDouble(this, "Configurer les alertes", "Humidite interieure maximale : ", QLineEdit::Normal, double());
    alertes.setHumiditeInterieureMax(humiditeInterieureMax);
    messageHumidite += ", humidite int. max. = ";
    messageHumidite.append(QString::number(humiditeInterieureMax));

    double humiditeInterieureMin = QInputDialog::getDouble(this, "Configurer les alertes", "Humidite interieure minimale : ", QLineEdit::Normal, double());
    alertes.setHumiditeInterieureMin(humiditeInterieureMin);
    messageHumidite += ", humidite int. min. = ";
    messageHumidite.append(QString::number(humiditeInterieureMin));

    journaliser(messageHumidite);

    double pressionMax = QInputDialog::getDouble(this, "Configurer les alertes", "Pression maximale : ", QLineEdit::Normal, double());
    alertes.setPressionMax(pressionMax);
    QString messagePression = "Seuils d'alertes changes : pression max. = ";
    messagePression.append(QString::number(pressionMax));

    double pressionMin = QInputDialog::getDouble(this, "Configurer les alertes", "Pression minimale : ", QLineEdit::Normal, double());
    alertes.setPressionMin(pressionMin);
    messagePression += ", pression min. = ";
    messagePression.append(QString::number(pressionMin));

    journaliser(messagePression);

    double poidsMax = QInputDialog::getDouble(this, "Configurer les alertes", "Poids maximale : ", QLineEdit::Normal, double());
    alertes.setPoidsMax(poidsMax);
    QString messagePoids = "Seuils d'alertes changes : poids max. = ";
    messagePoids.append(QString::number(poidsMax));

    double poidsMin = QInputDialog::getDouble(this, "Configurer les alertes", "Poids minimale : ", QLineEdit::Normal, double());
    alertes.setPoidsMin(poidsMin);
    messagePoids += ", poids min. = ";
    messagePoids.append(QString::number(poidsMin));

    journaliser(messagePoids);


}
/**
 * @brief Methode qui permet la configuration de l'acces TTN
 * @fn IHMPc::configurerConnexionTTN
 */
void IHMPc::configurerConnexionTTN()
{
    QString applicationID = QInputDialog::getText(this, "Configuration TTN", "Veuillez entrer l'application ID : ", QLineEdit::Normal, QString());
    if (!applicationID.isEmpty())
    {
        QString motDePasse = QInputDialog::getText(this, "Configuration TTN", "Veuillez entrer la clé : ", QLineEdit::Normal, QString());
        if(!motDePasse.isEmpty())
        {
            communicationTTN->reconnecter(communicationTTN->getHostname(),1883, applicationID, motDePasse);
        }
        else
        {
            QMessageBox::critical(this, "Configuration TTN", "Champ de mot de passe vide !");
        }
    }
    else
    {
        QMessageBox::critical(this, "Configuration TTN", "Champ de l'Application ID vide !");
    }
}
/**
 * @brief Méthode qui affiche l'horodatage
 * @fn IHMPc::afficherHorodatage
 * @param horodatage
 * @return void : un affichage de l'horodatage
 */
void IHMPc::afficherHorodatage(QString horodatage)
{
    ui->labelHorodatage->setText(horodatage);
}
/**
 * @brief Méthode qui affiche les nouvelles mesures
 * @fn IHMPc::afficherNouvellesMesures
 * @param mesure
 * @return void : un affichage des valeurs
 */
void IHMPc::afficherNouvellesMesures(MesureRuche mesure)
{
    if(!mesure.getHorodatage().isEmpty())
    {
        afficherHorodatage(mesure.getHorodatage());
        afficherPoids(mesure.getPoids());
        afficherTemperatureInterieure(mesure.getTemperatureInterieure());
        afficherTemperatureExterieure(mesure.getTemperatureExterieure());
        afficherHumiditeInterieure(mesure.getHumiditeInterieure());
        afficherHumiditeExterieure(mesure.getHumiditeExterieure());
        afficherPression(mesure.getPression());
        verifierAlertes(mesure);
    }
}
/**
 * @brief Méthode qui selectionne la ruche (s'abonne) et se desabonne de l'ancienne : renitialisation des valeurs
 * @fn IHMPc::selectionnerRuche
 * @return void : un affichage à 0 des valeurs
 */
void IHMPc::selectionnerRuche()
{
    if(indexRucheSelectionnee > -1)
        communicationTTN->desabonner(rucher[indexRucheSelectionnee]->getDeviceID());
    if(ui->listeRuches->count() > 0)
    {
        qDebug() << Q_FUNC_INFO << "Ruche sélectionnée" << ui->listeRuches->currentText();
        communicationTTN->abonner(rucher[ui->listeRuches->currentIndex()]->getDeviceID());
        indexRucheSelectionnee = ui->listeRuches->currentIndex();
        ui->labelRucheSelectionnee->setText("Ruche sélectionnée : " + ui->listeRuches->currentText());
        ui->labelHorodatage->setText("");
        ui->lcdTemperatureInterieure->display(0.);
        ui->lcdTemperatureExterieure->display(0.);
        ui->lcdHumiditeInterieure->display(0.);
        ui->lcdHumiditeExterieure->display(0.);
        ui->lcdPoids->display(0.);
        ui->lcdPression->display(0.);
        ui->labelAlerteTemperatureExterieure->setPixmap(QPixmap(QDir::currentPath() + ""));
        ui->labelAlerteTemperatureInterieure->setPixmap(QPixmap(QDir::currentPath() + ""));
        ui->labelAlerteHumiditeExterieure->setPixmap(QPixmap(QDir::currentPath() + ""));
        ui->labelAlerteHumiditeInterieure->setPixmap(QPixmap(QDir::currentPath() + ""));
        ui->labelAlertePoids->setPixmap(QPixmap(QDir::currentPath() + ""));
        ui->labelAlertePression->setPixmap(QPixmap(QDir::currentPath() + ""));
    }
}
/**
 * @brief Methode qui permet la gestion de la connexion au TTN
 * @fn IHMPc::gererConnexionTTN
 */
void IHMPc::gererConnexionTTN()
{
    if(communicationTTN->estConnecte())
    {
        if(indexRucheSelectionnee > -1)
            communicationTTN->desabonner(rucher[indexRucheSelectionnee]->getDeviceID());
        communicationTTN->deconnecter();
    }
    else
    {
        communicationTTN->connecter();
    }
}
/**
 * @brief Methode qui gere le changement d'onglet selon l'index
 * @fn IHMPc::gererChangementOnglet
 * @param index
 */
void IHMPc::gererChangementOnglet(int index)
{
    QDateTime maintenant;
    switch (index)
    {
    case OngletIHM::ONGLET_MESURES:
        break;
    case OngletIHM::ONGLET_GRAPHIQUES:
        qDebug() << Q_FUNC_INFO << "ONGLET_GRAPHIQUES";
        maintenant = QDateTime::currentDateTime();
        historique->recupererDonnees(rucher[indexRucheSelectionnee]->getDeviceID(), QString::number(maintenant.time().hour()) + "h");
        break;
    case OngletIHM::ONGLET_JOURNAL:

        break;
    default:
        break;
    }
}
/**
 * @brief Methode qui ajoute le message dans l'onglet journal
 * @fn IHMPc::journaliser
 * @param message
 */
void IHMPc::journaliser(QString message)
{
    ui->journal->append(message);
}

/**
 * @brief Méthode qui crée et initialise la vue pour les graphiques
 * @fn IHMPc::creerGraphique
 */
void IHMPc::creerGraphique()
{
    QHBoxLayout *layoutGraphePoids = new QHBoxLayout;

    initialiserGraphiquePoids();
    initialiserGraphiquePression();
    initialiserGraphiqueTemperatureExterieure();
    initialiserGraphiqueTemperatureInterieure();
    initialiserGraphiqueHumiditeExterieure();
    initialiserGraphiqueHumiditeInterieure();

    graphique = new QChartView(graphePoids);
    graphique->setRenderHint(QPainter::Antialiasing);

    ui->layoutGraphiques->addLayout(layoutGraphePoids);
    layoutGraphePoids->addWidget(graphique);
}

/**
 * @brief Méthode qui initialise le graphique pour le poids
 * @fn IHMPc::initialiserGraphiquePoids
 */
void IHMPc::initialiserGraphiquePoids()
{
    courbePoids = new QLineSeries();

    courbePoids->setName(QString::fromUtf8("<font color=\"#FFFFFF\">Poids</font>"));
    QPen pen;
    pen.setColor(QColor(0x99, 0x49, 0x2e));
    pen.setWidth(2);
    courbePoids->setPen(pen);

    graphePoids = new QChart();
    graphePoids->setBackgroundVisible(false);
    graphePoids->addSeries(courbePoids);
    graphePoids->setBackgroundBrush(QColor(0xFFFFFF));

    QValueAxis *axeXPoids = new QValueAxis;
    axeXPoids->setRange(0, 23);
    axeXPoids->setTickCount(24);
    axeXPoids->setLabelFormat("%02dh");
    axeXPoids->setTitleText(QString::fromUtf8("Heure"));
    axeXPoids->setLabelsColor(0xFFFFFF);
    axeXPoids->setTitleBrush(QColor(0xFFFFFF));
    graphePoids->addAxis(axeXPoids, Qt::AlignBottom);
    courbePoids->attachAxis(axeXPoids);

    QValueAxis *axeYPoids = new QValueAxis;
    axeYPoids->setRange(0, 50); // définir des constantes !
    axeYPoids->setLabelFormat("%.2f");
    axeYPoids->setTitleText(QString::fromUtf8("Poids en Kg"));
    axeYPoids->setLabelsColor(0xFFFFFF);
    axeYPoids->setTitleBrush(QColor(0xFFFFFF));
    graphePoids->addAxis(axeYPoids, Qt::AlignLeft);

    courbePoids->setPointsVisible(true);
    courbePoids->setPointLabelsFormat("@yPoint" "              ");
    courbePoids->setPointLabelsVisible(true);
    courbePoids->attachAxis(axeYPoids);

    courbePoids->setPointLabelsColor(0xFFFFFF);
}
/**
 * @brief Méthode qui initialise le graphique pour la pression
 * @fn IHMPc::initialiserGraphiquePression
 */
void IHMPc::initialiserGraphiquePression()
{
    courbePression = new QLineSeries();

    courbePression->setName(QString::fromUtf8("<font color=\"#FFFFFF\">Pression</font>"));
    QPen pen;
    pen.setColor(QColor(Qt::yellow));
    pen.setWidth(2);
    courbePression->setPen(pen);

    graphePression = new QChart();
    graphePression->setBackgroundVisible(false);
    graphePression->addSeries(courbePression);
    graphePression->setBackgroundBrush(QColor(0xFFFFFF));

    QValueAxis *axeXPression = new QValueAxis;
    axeXPression->setRange(0, 23);
    axeXPression->setTickCount(24);
    axeXPression->setLabelFormat("%02dh");
    axeXPression->setTitleText(QString::fromUtf8("Heure"));
    axeXPression->setLabelsColor(0xFFFFFF);
    axeXPression->setTitleBrush(QColor(0xFFFFFF));
    graphePression->addAxis(axeXPression, Qt::AlignBottom);
    courbePression->attachAxis(axeXPression);

    QValueAxis *axeYPression = new QValueAxis;
    axeYPression->setRange(900, 1100); // définir des constantes !
    axeYPression->setLabelFormat("%.2f");
    axeYPression->setTitleText(QString::fromUtf8("Pression en hPa"));
    axeYPression->setLabelsColor(0xFFFFFF);
    axeYPression->setTitleBrush(QColor(0xFFFFFF));
    graphePression->addAxis(axeYPression, Qt::AlignLeft);

    courbePression->setPointsVisible(true);
    courbePression->setPointLabelsFormat("@yPoint" "              ");
    courbePression->setPointLabelsVisible(true);
    courbePression->attachAxis(axeYPression);

    courbePression->setPointLabelsColor(0xFFFFFF);
}
/**
 * @brief Méthode qui initialise le graphique pour la temperature exterieure
 * @fn IHMPc::initialiserGraphiqueTemperatureExterieure
 */
void IHMPc::initialiserGraphiqueTemperatureExterieure()
{
    courbeTemperatureExt = new QLineSeries();

    courbeTemperatureExt->setName(QString::fromUtf8("<font color=\"#FFFFFF\">Temperature Exterieure</font>"));
    QPen pen;
    pen.setColor(QColor(Qt::darkRed));
    pen.setWidth(2);
    courbeTemperatureExt->setPen(pen);

    grapheTemperatureExt = new QChart();
    grapheTemperatureExt->setBackgroundVisible(false);
    grapheTemperatureExt->addSeries(courbeTemperatureExt);
    grapheTemperatureExt->setBackgroundBrush(QColor(0xFFFFFF));

    QValueAxis *axeXTemperatureExt = new QValueAxis;
    axeXTemperatureExt->setRange(0, 23);
    axeXTemperatureExt->setTickCount(24);
    axeXTemperatureExt->setLabelFormat("%02dh");
    axeXTemperatureExt->setTitleText(QString::fromUtf8("Heure"));
    axeXTemperatureExt->setLabelsColor(0xFFFFFF);
    axeXTemperatureExt->setTitleBrush(QColor(0xFFFFFF));
    grapheTemperatureExt->addAxis(axeXTemperatureExt, Qt::AlignBottom);
    courbeTemperatureExt->attachAxis(axeXTemperatureExt);

    QValueAxis *axeYTemperatureExt = new QValueAxis;
    axeYTemperatureExt->setRange(-20, 60); // définir des constantes !
    axeYTemperatureExt->setLabelFormat("%.2f");
    axeYTemperatureExt->setTitleText(QString::fromUtf8("Temperature Ext. en °C"));
    axeYTemperatureExt->setLabelsColor(0xFFFFFF);
    axeYTemperatureExt->setTitleBrush(QColor(0xFFFFFF));
    grapheTemperatureExt->addAxis(axeYTemperatureExt, Qt::AlignLeft);

    courbeTemperatureExt->setPointsVisible(true);
    courbeTemperatureExt->setPointLabelsFormat("@yPoint" "              ");
    courbeTemperatureExt->setPointLabelsVisible(true);
    courbeTemperatureExt->attachAxis(axeYTemperatureExt);

    courbeTemperatureExt->setPointLabelsColor(0xFFFFFF);
}
/**
 * @brief Méthode qui initialise le graphique pour la temperature interieure
 * @fn IHMPc::initialiserGraphiqueTemperatureInterieure
 */
void IHMPc::initialiserGraphiqueTemperatureInterieure()
{
    courbeTemperatureInt = new QLineSeries();

    courbeTemperatureInt->setName(QString::fromUtf8("<font color=\"#FFFFFF\">Temperature Interieure</font>"));
    QPen pen;
    pen.setColor(QColor(Qt::red));
    pen.setWidth(2);
    courbeTemperatureInt->setPen(pen);

    grapheTemperatureInt = new QChart();
    grapheTemperatureInt->setBackgroundVisible(false);
    grapheTemperatureInt->addSeries(courbeTemperatureInt);
    grapheTemperatureInt->setBackgroundBrush(QColor(0xFFFFFF));

    QValueAxis *axeXTemperatureInt = new QValueAxis;
    axeXTemperatureInt->setRange(0, 23);
    axeXTemperatureInt->setTickCount(24);
    axeXTemperatureInt->setLabelFormat("%02dh");
    axeXTemperatureInt->setTitleText(QString::fromUtf8("Heure"));
    axeXTemperatureInt->setLabelsColor(0xFFFFFF);
    axeXTemperatureInt->setTitleBrush(QColor(0xFFFFFF));
    grapheTemperatureInt->addAxis(axeXTemperatureInt, Qt::AlignBottom);
    courbeTemperatureInt->attachAxis(axeXTemperatureInt);

    QValueAxis *axeYTemperatureInt = new QValueAxis;
    axeYTemperatureInt->setRange(-20, 60); // définir des constantes !
    axeYTemperatureInt->setLabelFormat("%.2f");
    axeYTemperatureInt->setTitleText(QString::fromUtf8("Temperature Int. en °C"));
    axeYTemperatureInt->setLabelsColor(0xFFFFFF);
    axeYTemperatureInt->setTitleBrush(QColor(0xFFFFFF));
    grapheTemperatureInt->addAxis(axeYTemperatureInt, Qt::AlignLeft);

    courbeTemperatureInt->setPointsVisible(true);
    courbeTemperatureInt->setPointLabelsFormat("@yPoint" "              ");
    courbeTemperatureInt->setPointLabelsVisible(true);
    courbeTemperatureInt->attachAxis(axeYTemperatureInt);

    courbeTemperatureInt->setPointLabelsColor(0xFFFFFF);
}
/**
 * @brief Méthode qui initialise le graphique pour l'humidite exterieure
 * @fn IHMPc::initialiserGraphiqueHumiditeExterieure
 */
void IHMPc::initialiserGraphiqueHumiditeExterieure()
{
    courbeHumiditeExt = new QLineSeries();

    courbeHumiditeExt->setName(QString::fromUtf8("<font color=\"#FFFFFF\">Humidite Exterieure</font>"));
    QPen pen;
    pen.setColor(QColor(Qt::darkBlue));
    pen.setWidth(2);
    courbeHumiditeExt->setPen(pen);

    grapheHumiditeExt = new QChart();
    grapheHumiditeExt->setBackgroundVisible(false);
    grapheHumiditeExt->addSeries(courbeHumiditeExt);
    grapheHumiditeExt->setBackgroundBrush(QColor(0xFFFFFF));

    QValueAxis *axeXHumiditeExt = new QValueAxis;
    axeXHumiditeExt->setRange(0, 23);
    axeXHumiditeExt->setTickCount(24);
    axeXHumiditeExt->setLabelFormat("%02dh");
    axeXHumiditeExt->setTitleText(QString::fromUtf8("Heure"));
    axeXHumiditeExt->setLabelsColor(0xFFFFFF);
    axeXHumiditeExt->setTitleBrush(QColor(0xFFFFFF));
    grapheHumiditeExt->addAxis(axeXHumiditeExt, Qt::AlignBottom);
    courbeHumiditeExt->attachAxis(axeXHumiditeExt);

    QValueAxis *axeYHumiditeExt = new QValueAxis;
    axeYHumiditeExt->setRange(0, 100); // définir des constantes !
    axeYHumiditeExt->setLabelFormat("%.2f");
    axeYHumiditeExt->setTitleText(QString::fromUtf8("Humidite Ext. en %"));
    axeYHumiditeExt->setLabelsColor(0xFFFFFF);
    axeYHumiditeExt->setTitleBrush(QColor(0xFFFFFF));
    grapheHumiditeExt->addAxis(axeYHumiditeExt, Qt::AlignLeft);

    courbeHumiditeExt->setPointsVisible(true);
    courbeHumiditeExt->setPointLabelsFormat("@yPoint" "              ");
    courbeHumiditeExt->setPointLabelsVisible(true);
    courbeHumiditeExt->attachAxis(axeYHumiditeExt);

    courbeHumiditeExt->setPointLabelsColor(0xFFFFFF);
}
/**
 * @brief Méthode qui initialise le graphique pour l'humidite interieure
 * @fn IHMPc::initialiserGraphiqueHumiditeInterieure
 */
void IHMPc::initialiserGraphiqueHumiditeInterieure()
{
    courbeHumiditeInt = new QLineSeries();

    courbeHumiditeInt->setName(QString::fromUtf8("<font color=\"#FFFFFF\">Humidite Interieure</font>"));
    QPen pen;
    pen.setColor(QColor(Qt::blue));
    pen.setWidth(2);
    courbeHumiditeInt->setPen(pen);

    grapheHumiditeInt = new QChart();
    grapheHumiditeInt->setBackgroundVisible(false);
    grapheHumiditeInt->addSeries(courbeHumiditeInt);
    grapheHumiditeInt->setBackgroundBrush(QColor(0xFFFFFF));

    QValueAxis *axeXHumiditeInt = new QValueAxis;
    axeXHumiditeInt->setRange(0, 23);
    axeXHumiditeInt->setTickCount(24);
    axeXHumiditeInt->setLabelFormat("%02dh");
    axeXHumiditeInt->setTitleText(QString::fromUtf8("Heure"));
    axeXHumiditeInt->setLabelsColor(0xFFFFFF);
    axeXHumiditeInt->setTitleBrush(QColor(0xFFFFFF));
    grapheHumiditeInt->addAxis(axeXHumiditeInt, Qt::AlignBottom);
    courbeHumiditeInt->attachAxis(axeXHumiditeInt);

    QValueAxis *axeYHumiditeInt = new QValueAxis;
    axeYHumiditeInt->setRange(0, 100); // définir des constantes !
    axeYHumiditeInt->setLabelFormat("%.2f");
    axeYHumiditeInt->setTitleText(QString::fromUtf8("Humidite Int. en %"));
    axeYHumiditeInt->setLabelsColor(0xFFFFFF);
    axeYHumiditeInt->setTitleBrush(QColor(0xFFFFFF));
    grapheHumiditeInt->addAxis(axeYHumiditeInt, Qt::AlignLeft);

    courbeHumiditeInt->setPointsVisible(true);
    courbeHumiditeInt->setPointLabelsFormat("@yPoint" "              ");
    courbeHumiditeInt->setPointLabelsVisible(true);
    courbeHumiditeInt->attachAxis(axeYHumiditeInt);

    courbeHumiditeInt->setPointLabelsColor(0xFFFFFF);
}
/**
 * @brief Methode qui gere la selection du graphique
 * @fn IHMPc::selectionnerGraphique
 */
void IHMPc::selectionnerGraphique()
{
    qDebug() << Q_FUNC_INFO << "index" << ui->listeMesuresGraphiques->currentIndex();
    switch((TypeGraphique)ui->listeMesuresGraphiques->currentIndex())
    {
        case TypeGraphique::GRAPHIQUE_POIDS:
            afficherGraphiquePoids();
            break;
        case TypeGraphique::GRAPHIQUE_PRESSION:
            afficherGraphiquePression();
            break;
        case TypeGraphique::GRAPHIQUE_TEMPERATURE_EXTERIEURE:
            afficherGraphiqueTemperatureExterieure();
            break;
        case TypeGraphique::GRAPHIQUE_TEMPERATURE_INTERIEURE:
            afficherGraphiqueTemperatureInterieure();
            break;
        case TypeGraphique::GRAPHIQUE_HUMIDITE_EXTERIEURE:
            afficherGraphiqueHumiditeExterieure();
            break;
        case TypeGraphique::GRAPHIQUE_HUMIDITE_INTERIEURE:
            afficherGraphiqueHumiditeInterieure();
            break;
    }
}
/**
 * @brief Methode qui recharge le graphique à l'heure actuelle
 * @fn IHMPc::rechargerGraphique
 */
void IHMPc::rechargerGraphique()
{
    QDateTime maintenant = QDateTime::currentDateTime();
    historique->recupererDonnees(rucher[indexRucheSelectionnee]->getDeviceID(), QString::number(maintenant.time().hour()) + "h");
}
/**
 * @brief Méthode qui affiche le graphique pour le poids
 * @fn IHMPc::afficherGraphiquePoids
 */
void IHMPc::afficherGraphiquePoids()
{
    QVector<QStringList> mesures = historique->getMesuresPoids();
    qDebug() << Q_FUNC_INFO << "Nb mesures poids" << mesures.size();
    qDebug() << Q_FUNC_INFO << "mesures poids" << mesures;

    courbePoids->clear();
    for(QVector<QStringList>::iterator it = mesures.begin(); it != mesures.end(); it++)
    {
        //qDebug() << Q_FUNC_INFO << "heure" << (*it).at(0).toInt() << "mesure" << (*it).at(1).toDouble();
        courbePoids->append((*it).at(0).toInt(), (*it).at(1).toDouble());
    }

    graphique->setChart(graphePoids);
    graphique->update();
}
/**
 * @brief Méthode qui affiche le graphique pour la pression
 * @fn IHMPc::afficherGraphiquePression
 */
void IHMPc::afficherGraphiquePression()
{
    QVector<QStringList> mesures = historique->getMesuresPression();
    qDebug() << Q_FUNC_INFO << "Nb mesures pression" << mesures.size();
    qDebug() << Q_FUNC_INFO << "mesures pression" << mesures;

    courbePression->clear();
    for(QVector<QStringList>::iterator it = mesures.begin(); it != mesures.end(); it++)
    {
        //qDebug() << Q_FUNC_INFO << "heure" << (*it).at(0).toInt() << "mesure" << (*it).at(1).toDouble();
        courbePression->append((*it).at(0).toInt(), (*it).at(1).toDouble());
    }

    graphique->setChart(graphePression);
    graphique->update();
}
/**
 * @brief Méthode qui affiche le graphique pour la temperature exterieure
 * @fn IHMPc::afficherGraphiqueTemperatureExterieure
 */
void IHMPc::afficherGraphiqueTemperatureExterieure()
{
    QVector<QStringList> mesures = historique->getMesuresTemperatureExterieure();
    qDebug() << Q_FUNC_INFO << "Nb mesures temperature exterieure" << mesures.size();
    qDebug() << Q_FUNC_INFO << "mesures termperature exterieure" << mesures;

    courbeTemperatureExt->clear();
    for(QVector<QStringList>::iterator it = mesures.begin(); it != mesures.end(); it++)
    {
        //qDebug() << Q_FUNC_INFO << "heure" << (*it).at(0).toInt() << "mesure" << (*it).at(1).toDouble();
        courbeTemperatureExt->append((*it).at(0).toInt(), (*it).at(1).toDouble());
    }

    graphique->setChart(grapheTemperatureExt);
    graphique->update();
}
/**
 * @brief Méthode qui affiche le graphique pour la temperature interieure
 * @fn IHMPc::afficherGraphiqueTemperatureInterieure
 */
void IHMPc::afficherGraphiqueTemperatureInterieure()
{
    QVector<QStringList> mesures = historique->getMesuresTemperatureInterieure();
    qDebug() << Q_FUNC_INFO << "Nb mesures temperature interieure" << mesures.size();
    qDebug() << Q_FUNC_INFO << "mesures termperature interieure" << mesures;

    courbeTemperatureInt->clear();
    for(QVector<QStringList>::iterator it = mesures.begin(); it != mesures.end(); it++)
    {
        //qDebug() << Q_FUNC_INFO << "heure" << (*it).at(0).toInt() << "mesure" << (*it).at(1).toDouble();
        courbeTemperatureInt->append((*it).at(0).toInt(), (*it).at(1).toDouble());
    }

    graphique->setChart(grapheTemperatureInt);
    graphique->update();
}
/**
 * @brief Méthode qui affiche le graphique pour l'humidite exterieure
 * @fn IHMPc::afficherGraphiqueHumiditeExterieure
 */
void IHMPc::afficherGraphiqueHumiditeExterieure()
{
    QVector<QStringList> mesures = historique->getMesuresHumiditeExterieure();
    qDebug() << Q_FUNC_INFO << "Nb mesures humidite exterieure" << mesures.size();
    qDebug() << Q_FUNC_INFO << "mesures humidite exterieure" << mesures;

    courbeHumiditeExt->clear();
    for(QVector<QStringList>::iterator it = mesures.begin(); it != mesures.end(); it++)
    {
        //qDebug() << Q_FUNC_INFO << "heure" << (*it).at(0).toInt() << "mesure" << (*it).at(1).toDouble();
        courbeHumiditeExt->append((*it).at(0).toInt(), (*it).at(1).toDouble());
    }

    graphique->setChart(grapheHumiditeExt);
    graphique->update();
}
/**
 * @brief Méthode qui affiche le graphique pour l'humidite interieure
 * @fn IHMPc::afficherGraphiqueHumiditeInterieure
 */
void IHMPc::afficherGraphiqueHumiditeInterieure()
{
    QVector<QStringList> mesures = historique->getMesuresHumiditeInterieure();
    qDebug() << Q_FUNC_INFO << "Nb mesures humidite interieure" << mesures.size();
    qDebug() << Q_FUNC_INFO << "mesures humidite interieure" << mesures;

    courbeHumiditeInt->clear();
    for(QVector<QStringList>::iterator it = mesures.begin(); it != mesures.end(); it++)
    {
        //qDebug() << Q_FUNC_INFO << "heure" << (*it).at(0).toInt() << "mesure" << (*it).at(1).toDouble();
        courbeHumiditeInt->append((*it).at(0).toInt(), (*it).at(1).toDouble());
    }

    graphique->setChart(grapheHumiditeInt);
    graphique->update();
}
