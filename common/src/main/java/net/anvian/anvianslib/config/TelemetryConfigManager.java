package net.anvian.anvianslib.config;

import net.anvian.anvianslib.Constants;
import net.anvian.anvianslib.platform.Services;
import net.anvian.anvianslib.telemetry.TelemetrySender;
import net.anvian.anvianslib.util.LibUtil;

import java.io.File;
import java.util.Optional;

/**
 * Gestiona la configuración y el envío de telemetría para mods.
 * Carga, guarda y administra la configuración de telemetría mediante un archivo JSON.
 * Implementa singleton thread-safe y delega el envío a TelemetrySender.
 */
public class TelemetryConfigManager extends Config<TelemetryConfigManager.TelemetryConfig> {

    private static final TelemetryConfigManager INSTANCE = new TelemetryConfigManager();
    private final TelemetrySender telemetrySender = new TelemetrySender();

    private TelemetryConfigManager() {
        super(TelemetryConfig.class, Constants.LOG);
    }

    public static TelemetryConfigManager getInstance() {
        return INSTANCE;
    }

    @Override
    protected TelemetryConfig createDefaultConfig() {
        return new TelemetryConfig();
    }

    /**
     * Devuelve la configuración de telemetría como Optional.
     */
    public Optional<TelemetryConfig> getTelemetryConfig() {
        return Optional.ofNullable(config);
    }

    /**
     * Envía datos de telemetría con información de mod y versión de juego.
     */
    public void sendTelemetryData(String modId, String modVersion, String gameVersion) {
        getTelemetryConfig().filter(TelemetryConfig::isEnableTelemetry).ifPresent(cfg ->
            telemetrySender.send(
                modId,
                modVersion,
                gameVersion,
                Services.PLATFORM.getPlatformName(),
                !Services.PLATFORM.isDevelopmentEnvironment()
            )
        );
    }

    /**
     * Envía datos de telemetría con información de mod y versión de juego autodetectada.
     */
    public void sendTelemetryData(String modId, String modVersion) {
        sendTelemetryData(modId, modVersion, LibUtil.getMinecraftVersion());
    }

    /**
     * Método legado para enviar datos detallados de telemetría.
     * @deprecated Usar {@link #sendTelemetryData(String, String)}
     */
    @Deprecated
    public void sendTelemetryDataLegacy(String modId, String modVersion, String gameVersion, String loader, boolean isProduction) {
        getTelemetryConfig().filter(TelemetryConfig::isEnableTelemetry).ifPresent(cfg ->
            telemetrySender.send(modId, modVersion, gameVersion, loader, isProduction)
        );
    }

    /**
     * Inicializa la configuración de telemetría para el mod indicado.
     */
    public void initialize(File configDir) {
        super.initialize(configDir, "telemetry");
    }

    /**
     * Clase de configuración para telemetría.
     */
    public static class TelemetryConfig {
        /** Flag para habilitar/deshabilitar la telemetría */
        public boolean enableTelemetry;

        /**
         * Crea una configuración de telemetría con valores por defecto (habilitado).
         */
        public TelemetryConfig() {
            enableTelemetry = true;
        }

        /**
         * Indica si la telemetría está habilitada.
         */
        public boolean isEnableTelemetry() {
            return enableTelemetry;
        }
    }
}