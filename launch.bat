@echo off
echo ========================================
echo   RACING GAME v3.0 - Launcher
echo ========================================
echo.

cd /d "%~dp0"

REM Verifier si le repertoire bin existe
if not exist "bin" (
    echo Creating bin directory...
    mkdir bin
)

REM Compiler si necessaire
if not exist "bin\GameLauncher.class" (
    echo Compiling game files...
    cd src
    javac -d ../bin GameEntities.java RaceGamePanel.java RaceGamePanelAdvanced.java GameLauncher.java Main.java
    cd ..
)

REM Lancer le jeu
echo.
echo Launching Racing Game v3.0...
echo.
cd bin
java GameLauncher
