@echo off
setlocal enabledelayedexpansion
title RACING GAME v4.0 - Compiler & Launcher

echo ========================================
echo   RACING GAME v4.0 - AmÃ©liorÃ©
echo ========================================
echo.

cd /d "%~dp0"

REM Verifier si le repertoire bin existe
if not exist "bin" (
    echo Creating bin directory...
    mkdir bin
)

echo Compiling game files...
cd src

REM Compiler tous les fichiers Java
javac -d "..\bin" -encoding UTF-8 ^
    Main.java ^
    App.java ^
    model\car\*.java ^
    model\difficulty\*.java ^
    model\game\*.java ^
    model\obstacle\*.java ^
    engine\*.java ^
    ui\menu\*.java ^
    ui\scene\*.java ^
    ui\view\*.java ^
    ui\sound\*.java ^
    ui\GameRacePanel*.java

if %ERRORLEVEL% neq 0 (
    echo.
    echo Erreur de compilation!
    cd ..
    pause
    exit /b 1
)

echo Compilation successful!
echo.
echo Launching Racing Game v4.0...
echo.

cd ..\bin
java Main

cd ..
pause
