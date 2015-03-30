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

import java.util.HashMap;
import java.util.Map;

public final class MapUtility {

    /**
     * <p>
     * Proxies a newly created {@link HashMap} using a {@link CachingMap}.
     * </p>
     * <p>
     * The delegate {@link HashMap} is created using {@link HashMap#HashMap()}.
     * </p>
     *
     * @param <Key>
     *        type of the keys
     *
     * @param <Value>
     *        type of the values
     *
     * @return {@link CachingMap} proxying the new {@link HashMap}
     */
    public static <Key, Value> Map<Key, Value> cachingHashMap() {
        return new CachingMap<>(new HashMap<>());
    }

    /**
     * <p>
     * Proxies a newly created {@link HashMap} using a {@link CachingMap}.
     * </p>
     * <p>
     * The delegate {@link HashMap} is created using {@link HashMap#HashMap(int)}.
     * </p>
     *
     * @param <Key>
     *        type of the keys
     *
     * @param <Value>
     *        type of the values
     *
     * @param initialCapacity
     *        integer specifying the initial capacity of the delegate {@link HashMap}
     *
     * @return {@link CachingMap} proxying the new {@link HashMap}
     */
    public static <Key, Value> Map<Key, Value> cachingHashMap(final int initialCapacity) {
        return new CachingMap<>(new HashMap<>(initialCapacity));
    }

    /**
     * <p>
     * Proxies a newly created {@link HashMap} using a {@link CachingMap}.
     * </p>
     * <p>
     * The delegate {@link HashMap} is created using {@link HashMap#HashMap(int, float)}.
     * </p>
     *
     * @param <Key>
     *        type of the keys
     *
     * @param <Value>
     *        type of the values
     *
     * @param initialCapacity
     *        integer specifying the initial capacity of the delegate {@link HashMap}
     *
     * @param loadFactor
     *        integer specifying the load factro of the delegate {@link HashMap}
     *
     * @return {@link CachingMap} proxying the new {@link HashMap}
     */
    public static <Key, Value> Map<Key, Value> cachingHashMap(final int initialCapacity, final int loadFactor) {
        return new CachingMap<>(new HashMap<>(initialCapacity, loadFactor));
    }

    /**
     * <p>
     * Proxies a newly created {@link HashMap} using a {@link CachingMap}.
     * </p>
     * <p>
     * The delegate {@link HashMap} is created using {@link HashMap#HashMap(Map)}.
     * </p>
     *
     * @param <Key>
     *        type of the keys
     *
     * @param <Value>
     *        type of the values
     *
     * @param sourceMap
     *        {@link Map} containing the entries copied to the delegate {@link Map}
     *
     * @return {@link CachingMap} proxying the new {@link HashMap}
     */
    public static <Key, Value> Map<Key, Value> cachingHashMap(final Map<Key, Value> sourceMap) {
        return new CachingMap<>(new HashMap<>(sourceMap));
    }

    private MapUtility() {}
}
