package com.keuin.dynamicmotd;

import com.keuin.dynamicmotd.data.WorldData;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public class RenderDataMap implements Map<String, Object> {

    private final Supplier<WorldData> lazyWorld;
    private final Supplier<List<String>> lazyPlayers;
    private final Set<String> keySet = new HashSet<>(Arrays.asList("world", "players"));

    public RenderDataMap(@NotNull MinecraftServer server) {
        this.lazyWorld = () -> new WorldData(server.getOverworld());
        this.lazyPlayers = () -> Arrays.asList(server.getPlayerNames());
    }

    @Override
    public int size() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public boolean containsKey(Object key) {
        return keySet.contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Object get(Object key) {
        if (!(key instanceof String)) {
            return null;
        }
        if (key.equals("world")) {
            return lazyWorld.get();
        }
        if (key.equals("players")) {
            return lazyPlayers.get();
        }
        return null;
    }

    @Nullable
    @Override
    public Object put(String key, Object value) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Object remove(Object key) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ?> m) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void clear() {
        throw new RuntimeException("Not implemented");
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return this.keySet;
    }

    @NotNull
    @Override
    public Collection<Object> values() {
        final List<Object> collection = new ArrayList<>();
        for (String k : this.keySet()) {
            collection.add(this.get(k));
        }
        return Collections.unmodifiableList(collection);
    }

    @NotNull
    @Override
    public Set<Entry<String, Object>> entrySet() {
        final HashSet<Entry<String, Object>> collection = new HashSet<>();
        for (String k : this.keySet()) {
            collection.add(new Entry<>() {
                @Override
                public String getKey() {
                    return k;
                }

                @Override
                public Object getValue() {
                    return RenderDataMap.this.get(k);
                }

                @Override
                public Object setValue(Object value) {
                    throw new RuntimeException("Not implemented");
                }
            });
        }
        return Collections.unmodifiableSet(collection);
    }

    private static class LazyEvaluated<T> implements Supplier<T> {
        private final Supplier<T> supplier;
        private T value = null;
        private boolean evaluated = false;

        public LazyEvaluated(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public T get() {
            if (!evaluated) {
                value = supplier.get();
                evaluated = true;
            }
            return value;
        }
    }

    private static class KeyedSupplier<T> implements Supplier<T> {
        private final String key;
        private final Supplier<T> supplier;

        public KeyedSupplier(String key, Supplier<T> supplier) {
            this.key = key;
            this.supplier = supplier;
        }

        public String getKey() {
            return key;
        }

        public T get() {
            return this.supplier.get();
        }
    }
}
