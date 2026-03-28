#!/bin/bash
echo "========================================"
echo "  RACING GAME v3.0 - Launcher"
echo "========================================"
echo ""

cd "$(dirname "$0")"

# Verifier si le repertoire bin existe
if [ ! -d "bin" ]; then
    echo "Creating bin directory..."
    mkdir -p bin
fi

# Compiler si necessaire
if [ ! -f "bin/GameLauncher.class" ]; then
    echo "Compiling game files..."
    cd src
    javac -d ../bin GameEntities.java RaceGamePanel.java RaceGamePanelAdvanced.java GameLauncher.java Main.java
    cd ..
fi

# Lancer le jeu
echo ""
echo "Launching Racing Game v3.0..."
echo ""
cd bin
java GameLauncher
