/*
 * jlib - Open Source Java Library
 *
 *     www.jlib.org
 *
 *
 *     Copyright 2005-2015 Igor Akkerman
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package org.jlib.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * <p>
 * {@link Map} caching the last {@link Value} looked up using {@link #containsKey(Object)} and returning it by
 * a subsequent call to {@link #get(Object)} for the same {@link Key}. Note that the {@link Key} is <em>only</em> tested
 * for <em>identity</em>, <em>not</em> for <em>equality</em>. The other methods are delegated to the {@link Map}
 * specified to the constructor.
 * </p>
 * <p>
 * Note that if the requested {@link Key} is mapped to another {@link Value} in the delegate {@link Map} between the
 * calls to {@link #containsKey(Object)} and {@link Map#get(Object)}, the <em>former, now wrong</em>, {@link Value} will
 * be returned by the latter call. This also happens, if the corresponding {@link Entry} has been removed.
 * </p>
 * <p>
 * As in all <em>jlib</em> classes, neither {@code null} {@link Key}s nor {@code null} {@link Value}s are permitted and
 * cause undefined behaviour, such as {@link RuntimeException}s or invalid results. Hence, a {@link CachingMap}
 * may not be used on delegate {@link Map}s containing {@code null} {@link Key}s or {@link Value}s.
 * </p>
 * <p>
 * The key idea behind this proxy is to be able to use the following idiom without worrying about performance issues due
 * to multiple lookups:
 * </p>
 * <pre>
 * if (map.containsKey(key)) {
 *     value = map.get(key);
 *     // commands with value
 * }
 * else {
 *     // commands with no value
 * }</pre>
 * <p>
 * Instead, many developers use the following technique which enforces comparing the result with {@code null}. This is a
 * discouraged code style and less readable:
 * </p>
 * <pre>
 * // dicouraged code style
 * value = map.get(key);
 * if (value != null) {
 *     // commands with value
 * }
 * else {
 *     // commands with no value
 * }</pre>
 *
 * @param <Key>
 *        type of the keys
 *
 * @param <Value>
 *        type of the values
 *
 * @author Igor Akkerman
 */
public final class CachingMap<Key, Value>
implements Map<Key, Value> {

    /** delegate {@link Map} */
    private final Map<Key, Value> delegateMap;

    /** last looked up key */
    private Object lastLookedUpKey;

    /** last looked up value for {@link #lastLookedUpKey} */
    private Value lastLookedUpValue;

    /**
     * Creates a new {@link CachingMap}.
     *
     * @param delegateMap
     *        delegate {@link Map} to which all calls are delegated
     */
    public CachingMap(final Map<Key, Value> delegateMap) {

        this.delegateMap = delegateMap;
    }

    @Override
    public int size() {
        return delegateMap.size();
    }

    @Override
    public boolean isEmpty() {
        return delegateMap.isEmpty();
    }

    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean containsKey(final Object key) {
        final Value value = delegateMap.get(key);

        lastLookedUpKey = key;
        lastLookedUpValue = value;

        return value != null;
    }

    @Override
    public boolean containsValue(final Object value) {
        return false;
    }

    @Override
    @SuppressWarnings({ "ReturnOfNull", "ObjectEquality" })
    @Nullable
    public Value get(final Object key) {
        if (lastLookedUpKey == key)
            return lastLookedUpValue;

        clearLastLookedUpItems();
        return delegateMap.get(key);
    }

    @Override
    @SuppressWarnings("ObjectEquality")
    public Value put(final @NonNull Key key, final @NonNull Value value) {
        if (lastLookedUpKey == key)
            clearLastLookedUpItems();

        return delegateMap.put(key, value);
    }

    @Override
    @SuppressWarnings("ObjectEquality")
    public Value remove(final @NonNull Object key) {
        if (lastLookedUpKey == key)
            clearLastLookedUpItems();

        return delegateMap.remove(key);
    }

    @Override
    public void clear() {
        clearLastLookedUpItems();

        delegateMap.clear();
    }

    @Override
    @NonNull
    public Set<Key> keySet() {
        return delegateMap.keySet();
    }

    @Override
    @NonNull
    public Collection<Value> values() {
        return delegateMap.values();
    }

    @Override
    @NonNull
    public Set<Entry<Key, Value>> entrySet() {
        return delegateMap.entrySet();
    }

    /**
     * Clears the last looked up contained Key and Value.
     */
    @SuppressWarnings("AssignmentToNull")
    private void clearLastLookedUpItems() {
        lastLookedUpKey = null;
        lastLookedUpValue = null;
    }

    @Override
    @SuppressWarnings({ "NullableProblems", "SuspiciousMethodCalls" })
    public void putAll(final Map<? extends Key, ? extends Value> map) {
        if (lastLookedUpKey != null && map.containsKey(lastLookedUpKey)) {
            clearLastLookedUpItems();
        }

        delegateMap.putAll(map);
    }
}
