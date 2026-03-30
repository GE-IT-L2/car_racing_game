@echo off
REM Script compilation project
cd src
setlocal enabledelayedexpansion

echo Compilation du projet Racing Game v4.0...
echo.

REM Compiler tous les fichiers
javac -d "..\bin" -encoding UTF-8 -cp "..\bin" ^
  Main.java ^
  model\car\Car.java ^
  model\car\LaneCar.java ^
  model\car\PlayerCar.java ^
  model\car\PlayerCar1.java ^
  model\car\PlayerCar2.java ^
  model\car\AIPlayer.java ^
  model\difficulty\Difficulty.java ^
  model\difficulty\Easy.java ^
  model\difficulty\Medium.java ^
  model\difficulty\Hard.java ^
  model\game\GameState.java ^
  model\game\Score.java ^
  model\game\PlayerStatistics.java ^
  model\game\GameConfiguration.java ^
  model\game\DynamicSpeedSystem.java ^
  model\obstacle\Obstacle.java ^
  model\obstacle\ObstacleFixe.java ^
  model\obstacle\ObstacleMobile.java ^
  engine\CollisionManager.java ^
  engine\ObstacleManager.java ^
  engine\GameEngine.java ^
  engine\GameEngine2Players.java ^
  ui\menu\PlayerConfigurationDialog.java ^
  ui\menu\MenuPrincipal.java ^
  ui\menu\MenuGameOver.java ^
  ui\menu\MenuPause.java ^
  ui\scene\Paysage.java ^
  ui\scene\Ville.java ^
  ui\scene\Desert.java ^
  ui\scene\Campagne.java ^
  ui\view\VoitureView.java ^
  ui\view\ObstacleView.java ^
  ui\sound\SoundManager.java ^
  ui\GameRacePanel.java ^
  ui\GameRacePanel2Players.java ^
  App.java

IF %ERRORLEVEL% NEQ 0 (
    echo Erreur de compilation!
    pause
    exit /b 1
)

echo Compilation reussie!
echo Lancement de l'application...
echo.

cd ..\bin
java Main

pause
