#include "ihmpc.h"
#include <QApplication>

/**
 * @file main.cpp
 *
 * @brief Programme principal beehoneyt
 * @details Crée et affiche la fenêtre principale de l'application beehoneyt
 * @author Zakariya MHADI <zakariya.mhadi@gmail.com>
 * @version 1.0
 * @date 2021
 * $LastChangedRevision: 59 $
 * $LastChangedDate: 2021-04-21 07:34:00 +0200 (mer. 21 avril 2021) $
 *
 * @param argc
 * @param argv[]
 * @return int
 *
 */
int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    IHMPc w;
    w.show();
    return a.exec();
}
