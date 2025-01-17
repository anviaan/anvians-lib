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
        name = 'Repsy Anvian'
        url = 'https://repo.repsy.io/mvn/anvian/anvians-lib'
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