<h1>Nemesis</h1>

Nemesis is a 4x multiplayer game where you try to discover and defend the star system or devour and conquer it.

## Class Diagram

```mermaid
classDiagram
    Entity <-- GameObject
    GameObject <-- Animation
    Animation <-- DamageAnimation
    Animation <-- PathAnimation
    GameObject <-- Marker
    Marker <-- ControlPoint
    Marker <-- SpawnPoint
    GameObject <-- Unit
    GameObject <-- Projectile
    Entity <-- HardPoint
    HardPoint <-- Weapon
    HardPoint <-- Engine
```