# Anvian's Lib

This library is designed for use in my mods and includes various helpful functions for my projects. Additionally, it
introduces telemetry to track how many players are using my mods. If desired, telemetry can be disabled via the
configuration file. For more details, visit the [Telemetry](https://anvian.net/telemetry) page.

## How to use

Add the following to your `gradle.properties`:

```properties
anvianslib_version=<version>
```

Add the following to your `build.gradle`:

```groovy
repositories {
    maven {
        name = 'Reposilite Anvian'
        url = 'https://maven.anvian.net/releases'
    }
}
```

- On Common:

```groovy
  implementation "net.anvian.anvianslib:anvianslib-common-1.21:${anvianslib_version}"
```

- On Fabric:

```groovy
  modImplementation "net.anvian.anvianslib:anvianslib-fabric-1.21:${anvianslib_version}"
```

- On Forge:

```groovy
  implementation "net.anvian.anvianslib:anvianslib-forge-1.21:${anvianslib_version}"
```

- On NeoForge:

```groovy
  implementation "net.anvian.anvianslib:anvianslib-neoforge-1.21:${anvianslib_version}"
```

## Telemetry Integration

Anvian's Lib includes built-in telemetry to track mod usage and usage patterns. Users can disable telemetry via configuration file if desired.

### Quick Start

To send telemetry data from your mod:

1. **Initialize telemetry during mod startup:**

```java
LibUtil.setupTelemetry("your_mod_id", "your_mod_version");
```

This will:
- Create a config directory for your mod
- Initialize the telemetry config file
- Send initial telemetry data

2. **Send custom telemetry data (optional):**

```java
String modId = "your_mod_id";
String modVersion = "1.0.0";
String gameVersion = LibUtil.getMinecraftVersion();
String loader = "Fabric"; // or "NeoForge", "Forge", etc.

TelemetrySender.send(modId, modVersion, gameVersion, loader, true);
```

The last parameter (`true`) indicates production environment. Set to `false` for development.

### Configuration

Telemetry configuration is stored at:
- **Windows:** `%appdata%/minecraft/config/your_mod_id/telemetry.json`
- **Linux/Mac:** `~/.minecraft/config/your_mod_id/telemetry.json`

Users can disable telemetry by setting `enableTelemetry` to `false` in the JSON file:

```json
{
  "enableTelemetry": false
}
```

### Data Collected

The following data is sent to track mod usage:
- Mod ID and version
- Minecraft version
- Loader name (Fabric, NeoForge, etc.)

No personal or sensitive data is collected.
