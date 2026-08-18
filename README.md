# Anvian's Lib

Shared runtime utilities for Anvian's Minecraft mods.

The current line targets **Minecraft 26.1**, **Java 25**, **Fabric**, and **NeoForge**.

## Installation

Add the version to `gradle.properties`:

```properties
anvianslib_version=1.5.0
```

Add the repository to the project that consumes the library:

```groovy
repositories {
    maven {
        name = 'Anvian'
        url = 'https://maven.anvian.net/releases'
    }
}
```

Use the artifact that matches the source set:

```groovy
// Common
implementation "net.anvian.anvianslib:anvianslib-common-26.1:${anvianslib_version}"

// Fabric
implementation "net.anvian.anvianslib:anvianslib-fabric-26.1:${anvianslib_version}"

// NeoForge
implementation "net.anvian.anvianslib:anvianslib-neoforge-26.1:${anvianslib_version}"
```

## Configuration

Create a `Config<T>` subclass and initialize it with the mod id:

```java
configs.initialize(Constants.MOD_ID);
```

The library resolves the platform config directory, creates `<config>/<mod_id>`, and stores the file as
`<mod_id>-config.json`.

## Registry helpers

`RegistryUtil` centralizes namespaced registry boilerplate:

```java
RegistryUtil.register(BuiltInRegistries.ITEM, MOD_ID, "example", item);
ResourceKey<Block> key = RegistryUtil.key(Registries.BLOCK, MOD_ID, "example");
TagKey<Item> tag = RegistryUtil.tag(Registries.ITEM, MOD_ID, "examples");
```

## Telemetry

Set up telemetry during mod initialization:

```java
LibUtil.setupTelemetry("your_mod_id", "your_mod_version");
```

Telemetry configuration is stored at `<config>/<your_mod_id>/telemetry-config.json`:

```json
{
  "enable_telemetry": false
}
```

When enabled, the library sends the mod id and version, Minecraft version, and loader name. No personal data is
collected. Users can disable telemetry at any time by changing the configuration value.
