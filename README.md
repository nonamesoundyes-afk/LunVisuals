# LunVisuals — Fabric 1.21.11

Client-side Fabric mod project targeting Java 21.

## Implemented in this source revision
- Right Shift opens the ClickGUI.
- Fullbright module temporarily raises gamma while enabled and restores the previous value when disabled.
- Zoom module uses C while enabled and restores the previous FOV when released/disabled.
- HUD module displays XYZ coordinates and FPS.
- Module toggles are saved to `config/lunvisuals.properties`.

## Build
Use JDK 21 and Gradle. From this folder run:

```sh
./gradlew build
```

If no Gradle wrapper is present, use a compatible installed Gradle:

```sh
gradle build
```

The output JAR should be in `build/libs/`.

## Notes
This is a source implementation and has not been compiled in the current environment because Gradle is not installed and network access to the Gradle distribution host is unavailable. The remaining placeholder modules are listed in the ClickGUI but are not implemented yet.
