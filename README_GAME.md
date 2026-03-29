# 🏎️ RACING GAME v3.0

## Description
Un jeu de course automobile 2D en Java avec **Swing**, développé selon une architecture propre et performante.

### Caractéristiques
- ✅ **Menu principal** avec sélection du mode de jeu
- ✅ **Jeu 1 joueur** contre des obstacles avec difficulté ajustable
- ✅ **3 terrains différents**: Ville, Désert, Campagne
- ✅ **Contrôles clavier robustes** (Flèches ou ZQSD)
- ✅ **Système de collision** précis
- ✅ **Score et statistiques** en temps réel
- ✅ **Obstacles fixes et mobiles** avec IA
- ✅ **Architecture moderne** avec séparation des rôles

---

## 🎮 Comment Jouer

### Lancement
```bash
# Windows
./launch.bat

# Linux/Mac
./launch.sh

# Ou directement avec Java
java -cp build Main
```

### Contrôles
| Touche | Action |
|--------|--------|
| **⬅️ Flèche Gauche** ou **A** | Déplacer à gauche |
| **➡️ Flèche Droite** ou **D** | Déplacer à droite |
| **ESC** ou **P** | Pause/Reprendre |

### Objectif
- Éviter les **obstacles** (rochers, voitures)
- Accumuler le maximum de **points**
- Survirbez au plus longtemps possible!

---

## 🏗️ Architecture

### Structure des Dossiers
```
src/
├── App.java                    # Fenêtre principale
├── Main.java                   # Point d'entrée
├── model/
│   ├── car/                    # Voitures (PlayerCar, PlayerCar1/2)
│   ├── obstacle/               # Obstacles (Fixe, Mobile)
│   ├── difficulty/             # Difficultés (Easy, Medium, Hard)
│   └── game/                   # Logique (Score, GameState, Stats)
├── engine/
│   ├── GameEngine.java         # Moteur principal
│   ├── CollisionManager.java   # Collisions
│   └── ObstacleManager.java    # Gestion des obstacles
└── ui/
    ├── GameRacePanel.java      # Panel de jeu optimisé
    ├── menu/                   # Menus (Principal, Pause, GameOver)
    ├── scene/                  # Terrains (Ville, Désert, Campagne)
    ├── view/                   # Rendu (Voiture, Obstacle)
    └── sound/                  # Gestionnaire audio (extensible)
```

### Points Clés de Design
- **GameEngine**: Orchestre la logique du jeu, les collisions et les obstacles
- **CollisionManager**: Détecte les collisions avec padding adaptatif
- **ObstacleManager**: Génère et gère les obstacles selon la difficulté
- **GameRacePanel**: Panel de rendu optimisé avec 60 FPS
- **Paysages**: Classes abstraites pour différents environnements

---

## 🎯 Difficulté

La difficulté affecte:
- **Facile**: Vitesse initiale basse, obstacles rares
- **Moyen**: Vitesse normale, obstacles constants
- **Difficile**: Vitesse rapide, obstacles très fréquents

---

## 🔧 Compilation Manuelle

```bash
# Créer le répertoire de build
mkdir build

# Compiler tous les fichiers
javac -d build -sourcepath src src/**/*.java

# Lancer le jeu
java -cp build Main
```

---

## 📊 Performance

- **FPS**: 60 FPS constant
- **Obstacles limités**: Maximum 50 obstacles à l'écran
- **Rendu optimisé**: Utilisation de Graphics2D avec RenderingHints
- **Hitbox adaptatif**: Collision avec 85% de précision

---

## 🚀 Améliorations Futures

- [ ] Mode 2 joueurs complet
- [ ] Powerups et bonus
- [ ] Système de sons (ambiance, collisions, musique)
- [ ] Palette de couleurs personnalisée
- [ ] Leaderboard et sauvegarde des scores
- [ ] Animations de particules
- [ ] Obstacles plus variés

---

## 🐛 Dépannage

### Le jeu ne compile pas?
- Vérifier que Java JDK est installé
- Vérifier le chemin vers le dossier src
- Voir les messages d'erreur du compilateur

### Le jeu lag?
- Réduire la difficté
- Fermer d'autres applications

### Les contrôles ne répondent pas?
- Cliquer sur la fenêtre pour récupérer le focus
- Vérifier le clavier (AZERTY vs QWERTY)

---

## 📝 Développeur
Jeu créé avec Java 8+ et Swing  
Architecture propre et extensible  
Code documenté et facilement maintenable

**Divertissez-vous bien! 🏎️💨**
