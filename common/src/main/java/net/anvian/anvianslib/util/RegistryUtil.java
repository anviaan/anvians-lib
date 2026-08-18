package net.anvian.anvianslib.util;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

/**
 * Small helpers for namespaced registry objects used by mods.
 */
public final class RegistryUtil {
    /**
     * Utility class; do not instantiate.
     */
    private RegistryUtil() {}

    /**
     * Registers a value using a namespaced identifier.
     *
     * @param registry  the registry that will receive the value
     * @param namespace the namespace of the identifier, normally the mod id
     * @param path      the path of the identifier
     * @param value     the value to register
     * @param <T>       the registry value type
     * @return the registered value
     */
    public static <T> T register(Registry<T> registry, String namespace, String path, T value) {
        return Registry.register(registry, id(namespace, path), value);
    }

    /**
     * Creates a resource key for a value in a registry.
     *
     * @param registry  the registry key that contains the value
     * @param namespace the namespace of the identifier, normally the mod id
     * @param path      the path of the identifier
     * @param <T>       the registry value type
     * @return a resource key for the namespaced value
     */
    public static <T> ResourceKey<T> key(ResourceKey<? extends Registry<T>> registry, String namespace, String path) {
        return ResourceKey.create(registry, id(namespace, path));
    }

    /**
     * Creates a tag key for values in a registry.
     *
     * @param registry  the registry key associated with the tag values
     * @param namespace the namespace of the identifier, normally the mod id
     * @param path      the path of the tag
     * @param <T>       the tagged value type
     * @return a tag key for the namespaced tag
     */
    public static <T> TagKey<T> tag(ResourceKey<? extends Registry<T>> registry, String namespace, String path) {
        return TagKey.create(registry, id(namespace, path));
    }

    /**
     * Creates a namespaced identifier.
     *
     * @param namespace the identifier namespace
     * @param path      the identifier path
     * @return the resulting identifier
     */
    private static Identifier id(String namespace, String path) {
        return Identifier.fromNamespaceAndPath(namespace, path);
    }
}
