package de.derfrzocker.ore.control.cache.config;

import de.derfrzocker.ore.control.api.config.Config;
import de.derfrzocker.ore.control.api.config.ConfigInfo;
import de.derfrzocker.ore.control.cache.config.part.FeatureConfigCachePart;
import de.derfrzocker.spigot.utils.function.TripleConsumer;
import org.bukkit.NamespacedKey;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigCacheSlice {

    private final Map<ConfigInfo, FeatureConfigCachePart> cache = new ConcurrentHashMap<>();

    public void clear() {
        cache.clear();
    }

    public void forEachFlatMap(TripleConsumer<ConfigInfo, NamespacedKey, Optional<Config>> consumer) {
        cache.forEach((configInfo, part) -> part.forEach((featureKey, config) -> consumer.accept(configInfo, featureKey, config)));
    }

    public FeatureConfigCachePart getOrCreate(ConfigInfo configInfo) {
        return cache.computeIfAbsent(configInfo, ignore -> new FeatureConfigCachePart());
    }

    public Optional<FeatureConfigCachePart> get(ConfigInfo configInfo) {
        return Optional.ofNullable(cache.get(configInfo));
    }
}
