QT       += core gui widgets charts mqtt network

CONFIG += c++11

#DEFINES += QT_DEPRECATED_WARNINGS
#DEFINES += QT_DISABLE_DEPRECATED_BEFORE=0x060000    # disables all the APIs deprecated before Qt 6.0.0

SOURCES += \
    alertes.cpp \
    communication.cpp \
    historique.cpp \
    main.cpp \
    ihmpc.cpp \
    mesureruche.cpp \
    ruche.cpp

HEADERS += \
    alertes.h \
    communication.h \
    historique.h \
    ihmpc.h \
    mesureruche.h \
    ruche.h

FORMS += \
    ihmpc.ui

TRANSLATIONS += \
    beehoneyt_fr_FR.ts

COPIES += configuration
configuration.files = beehoneyt.ini
configuration.path = $$OUT_PWD/
configuration.base = $$PWD/

COPIES += images
images.files = images
images.path = $$OUT_PWD/
images.base = $$PWD/

CONFIG(release, debug|release):DEFINES+=QT_NO_DEBUG_OUTPUT
