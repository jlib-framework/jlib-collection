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
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.UnaryOperator;

import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * <p>
 * {@link List} caching the index of the last {@link Item} looked up using {@link #indexOf(Object)} and
 * returning it by a subsequent call to {@link #get(int)} for the same index. The other methods are delegated to the
 * {@link List} specified to the constructor.
 * </p>
 * <p>
 * Note that if the index of the requested {@link Item} is changed in the delegate {@link List} between the calls to
 * {@link #indexOf(Object)} and {@link #get(int)}, the <em>former, now wrong</em>, index will be returned by the latter
 * call. This also happens when the {@link Item} is removed from the delegate {@link List}.
 * </p>
 * <p>
 * As in all <em>jlib</em> classes, {@code null} {@link Item}s are <em>not</em> permitted and cause undefined behaviour,
 * such as {@link RuntimeException}s or invalid results. Hence, a {@link CachingList} may not be used on delegate
 * {@link List}s containing {@code null} {@link Item}s.
 * </p>
 * <p>
 * The key idea behind this proxy is to be able to use the following idiom without worrying about performance issues due
 * to multiple lookups:
 * </p>
 * <pre>
 * if (list.contains(item)) {
 *     itemIndex = list.indexOf(item);
 *     // commands with index
 * }
 * else {
 *     // commands with no item
 * }</pre>
 * <p>
 * Instead, many developers use the following technique which enforces comparing the result with {@code -1}. This is a
 * discouraged code style and less readable:
 * </p>
 * <pre>
 * // dicouraged by clean coders
 * itemIndex = list.indexOf(item);
 * if (itemIndex != -1) {
 *     // commands with index
 * }
 * else {
 *     // commands with no index
 * }</pre>
 *
 * @param <Item>
 *        type of the values
 *
 * @author Igor Akkerman
 */
@RequiredArgsConstructor
public final class CachingList<Item>
    implements List<Item> {

    /** delegate {@link List} */
    private final List<Item> delegate;

    /** last looked up index; -1 if unset (for performance reasons) */
    private int lastLookedUpItemIndex;

    /** last looked up {@link Item}; {@code null} if unset (for performance reasons) */
    private @Nullable Object lastLookedUpItem;

    @Override
    public boolean contains(final @Nullable Object item) {
        return indexOf(item) != - 1;
    }

    @Override
    @NonNull
    public Iterator<Item> iterator() {
        return delegate.iterator();
    }

    @Override
    @NonNull
    public Object[] toArray() {
        return delegate.toArray();
    }

    @Override
    @NonNull
    @SuppressWarnings("SuspiciousToArrayCall")
    public <T> T[] toArray(@NonNull final T[] array) {
        return delegate.toArray(array);
    }

    @Override
    public boolean add(final Item item) {
        return delegate.add(item);
    }

    @Override
    public boolean remove(final Object item) {
        return delegate.remove(item);
    }

    @Override
    public boolean containsAll(@NonNull final Collection<?> items) {
        return delegate.containsAll(items);
    }

    @Override
    public boolean addAll(@NonNull final Collection<? extends Item> items) {
        return delegate.addAll(items);
    }

    @Override
    public boolean addAll(final int index, @NonNull final Collection<? extends Item> items) {
        return delegate.addAll(index, items);
    }

    @Override
    public boolean removeAll(@NonNull final Collection<?> items) {
        return delegate.removeAll(items);
    }

    @Override
    public boolean retainAll(@NonNull final Collection<?> items) {
        return delegate.retainAll(items);
    }

    @Override
    public void replaceAll(final UnaryOperator<Item> operator) {
        delegate.replaceAll(operator);
    }

    @Override
    public void sort(final Comparator<? super Item> items) {
        delegate.sort(items);
    }

    @Override
    public int indexOf(final @Nullable Object item) {
        if (item == lastLookedUpItem)
            return lastLookedUpItemIndex;

        @SuppressWarnings("ConstantConditions")
        final int itemIndex = delegate.indexOf(item);

        lastLookedUpItem = item;
        lastLookedUpItemIndex = itemIndex;

        return itemIndex;
    }

    @Override
    public int lastIndexOf(final Object o) {
        return delegate.lastIndexOf(o);
    }

    @Override
    @NonNull
    public ListIterator<Item> listIterator() {
        return delegate.listIterator();
    }

    @Override
    @NonNull
    public ListIterator<Item> listIterator(final int index) {
        return delegate.listIterator(index);
    }

    @Override
    @NonNull
    public List<Item> subList(final int fromIndex, final int toIndex) {
        return delegate.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<Item> spliterator() {
        return delegate.spliterator();
    }

    @Override
    public Item remove(final int index) {
        if (index == lastLookedUpItemIndex)
            clearLastLookedUpIndex();

        return delegate.remove(index);
    }

    @Override
    public void clear() {
        clearLastLookedUpIndex();

        delegate.clear();
    }

    @Override
    @SuppressWarnings({ "EqualsWhichDoesntCheckParameterClass", "EqualsBetweenInconvertibleTypes" })
    public boolean equals(final Object object) {
        return delegate.equals(object);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public Item get(final int index) {
        return delegate.get(index);
    }

    @Override
    public Item set(final int index, final Item element) {
        return delegate.set(index, element);
    }

    @Override
    public void add(final int index, final Item element) {
        delegate.add(index, element);
    }

    /**
     * Clears the last looked up contained Key and Item.
     */
    @SuppressWarnings("AssignmentToNull")
    private void clearLastLookedUpIndex() {
        lastLookedUpItem = null;
        lastLookedUpItemIndex = - 1;
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }
}
