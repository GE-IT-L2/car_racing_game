# 🏎️ RACING GAME v3.0

## Améliorations Majeures vs v1.0

### ✨ Graphisme 3D Amélioré
- **Perspective 3D réaliste** : La route se rétrécit vers l'horizon pour créer une illusion de profondeur
- **Opacité progressive** : Les éléments au loin s'estompent (fog effect)
- **Dégradés de couleurs** : Carrosseries avec dégradés modernes
- **Ombres réalistes** : Ombres portées sous les véhicules
- **Phares dynamiques** : Éclairage des phares avec animation

### 🎨 Palettes Couleurs Modernes
- **Thème sombre élégant** : Fond noir avec accents bleus et oranges
- **Dégradés sophistiqués** : Ciel bleu dégradé, route avec profondeur
- **Phares et indicateurs** : Éclairage LED moderne
- **GUI moderne** : Interface nettoyée et optimisée

### 🎮 Mécanique de Jeu Opérationnels
- **Accélération fonctionnelle** ✅ : W/Haut pour accélérer
- **Freinage efficace** ✅ : S/Bas pour freiner
- **Direction fluide** ✅ : A/D ou Flèches pour orienter
- **Vitesse dynamique** ✅ : Augmente progressivement et génère des obstacles
- **Collision détectée** ✅ : Collision entre joueur et obstacles
- **Système de score** ✅ : Points + Record personnel

### 🚀 Fonctionnalités Nouvelles dans v3.0
1. **Accélération automatique du jeu** : Les obstacles arrivent plus vite au fur et à mesure
2. **Barre de vitesse visuelle** : Indicateur en bas de l'écran avec couleurs dynamiques
3. **Phares LED** : Éclairage réaliste sur les véhicules
4. **Score en temps réel** : Affichage du score, record, et vitesse
5. **Game Over élégant** : Écran de fin avec animations
6. **Redémarrage instantané** : SPACE pour rejouer

## 🎮 Contrôles

| Action | Clavier |
|--------|---------|
| Tourner Gauche | `A` ou `←` |
| Tourner Droit | `D` ou `→` |
| Accélérer | `W` ou `↑` |
| Freiner | `S` ou `↓` |
| Recommencer | `SPACE` (après Game Over) |

## 📊 Statistiques Techniques

- **Résolution** : 1000x700 pixels
- **FPS** : ~60 FPS (16ms par frame)
- **Pool Graphique** : 3 voies de circulation
- **Anti-aliasing** : Activé pour un rendu lisse

## 🛠️ Compilation et Exécution

### Compilation
```bash
cd src
javac -d ../bin Main.java RaceGamePanel.java
```

### Exécution
```bash
cd bin
java Main
```

## 📁 Structure du Projet

```
COURSE_DE_VOITURE/
├── src/
│   ├── Main.java              # Point d'entrée
│   ├── RaceGamePanel.java     # Moteur graphique et gameplay
│   └── (Archives v1.0: App.java, model/, ui/)
├── bin/                        # Fichiers compilés
└── README.md
```

## 🎯 Points Clés Améliorés

| Aspect | v1.0 | v3.0 |
|--------|------|------|
| Graphisme | 2D basique | 3D avec perspective |
| Couleurs | Limitées | Palette moderne (20+ couleurs) |
| Accélération | Non-fonctionnelle | ✅ Opérationnelle |
| Vitesse | Statique | Dynamique avec progression |
| Obstacles | Génération simple | Spawn adaptatif |
| Collision | De base | Détection précise |
| UI/HUD | Minimal | Complet avec indicateurs |

## 🚀 Future Améliorations (v4.0+)

- Support multi-niveaux de difficulté
- Power-ups et bonus
- Son et musique de fond
- Meilleur système de persistance des scores
- Multijoueur local
- Effets de fumée au freinage

---

**Version** : 3.0  
**Date** : Aujourd'hui  
**Status** : ✅ Production-Ready
