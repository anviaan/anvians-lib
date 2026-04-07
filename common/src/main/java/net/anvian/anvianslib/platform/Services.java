package net.anvian.anvianslib.platform;

import net.anvian.anvianslib.Constants;
import net.anvian.anvianslib.platform.services.IPlatformHelper;

import java.util.ServiceLoader;

/**
 * Service loader for platform-specific implementations.
 *
 * <p>This class uses the Java {@link ServiceLoader} SPI (Service Provider Interface)
 * mechanism to load platform-specific service implementations in a thread-safe manner.
 */
public class Services {
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    /**
     * Loads a service implementation using the ServiceLoader SPI.
     *
     * @param <T>   the service interface type
     * @param clazz the class of the service to load
     * @return the loaded service implementation
     * @throws NullPointerException if no service implementation is found
     */
    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz, Services.class.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}