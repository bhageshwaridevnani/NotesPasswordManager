package com.example.demo.Security;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by parita on 8/5/20
 */
public class ImprovedThreadLocal<T> extends ThreadLocal<T> {

    /**
     * We add an entry to this map only once for every thread. We do not add an
     * entry for every thread local value so this will not affect the
     * performance of accessing and creating thread local values. This
     * synchronized map is not used to access ThreadLocal values. It is used to
     * preserve strong references to the objects because Thread objects will
     * have only weak references to ThreadLocal objects. WeakHashMap allows
     * values to be garbage collected once no one else is holding onto the
     * Thread
     */
    private static final Map<Thread, Map<ThreadLocal<?>, Object>>				 strongReferencesToThreadLocalValues		 = Collections
            .synchronizedMap(new WeakHashMap<>());

    /**
     * This is weak reference to the set of objects belonging to the current
     * thread. Using this reference we can access the list of strong references
     * in a non-synchronous way.
     */
    private static final ThreadLocal<WeakReference<Map<ThreadLocal<?>, Object>>> threadLocalWeakReferenceToMapOfThreadLocals = new ThreadLocal<WeakReference<Map<ThreadLocal<?>, Object>>>() {

        @Override
        protected WeakReference<Map<ThreadLocal<?>, Object>> initialValue() {
            Map<ThreadLocal<?>, Object> value = new WeakHashMap<>();
            strongReferencesToThreadLocalValues
                    .put(
                            Thread
                                    .currentThread(),
                            value);
            return new WeakReference<>(
                    value);
        }
    };

    @SuppressWarnings("unchecked")
    @Override
    public T get() {
        Map<ThreadLocal<?>, Object> threadLocalMap = threadLocalWeakReferenceToMapOfThreadLocals.get().get();
        T value = (T) threadLocalMap.get(this);
        if (value == null && !threadLocalMap.containsKey(this)) {
            value = this.initialValue();
            set(value);
        }
        return value;
    }

    @Override
    public void set(T value) {
        // Adding strong reference of this value to the map corresponding to the
        // current thread. We access this map through weak reference in a
        // non-synchronized way to keep good performance.
        threadLocalWeakReferenceToMapOfThreadLocals.get().get().put(this, value);
    }

    @Override
    public void remove() {
        threadLocalWeakReferenceToMapOfThreadLocals.get().get().remove(this);
    }

}
