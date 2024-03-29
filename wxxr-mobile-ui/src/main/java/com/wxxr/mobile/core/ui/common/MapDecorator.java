/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import com.wxxr.mobile.core.bean.api.ICollectionDecorator;

/**
 * @author neillin
 *
 */
public class MapDecorator<K, V> implements Map<K, V>, ICollectionDecorator {

	private Map<K,V> data;
	final private BindableBeanSupport support;
	final private String name;
	private AtomicBoolean changed = new AtomicBoolean();
	
	public MapDecorator(String propertyName,BindableBeanSupport p){
		this.support = p;
		this.name = propertyName;
	}

	/**
	 * 
	 * @see java.util.Map#clear()
	 */
	public void clear() {
		data.clear();
		this.changed.set(true);
		this.support.firePropertyChange(name,  this, null);
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key) {
		return data.containsKey(key);
	}

	/**
	 * @param value
	 * @return
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object value) {
		return data.containsValue(value);
	}

	/**
	 * @return
	 * @see java.util.Map#entrySet()
	 */
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return data.entrySet();
	}

	/**
	 * @param object
	 * @return
	 * @see java.util.Map#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		return data.equals(object);
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public V get(Object key) {
		return data.get(key);
	}

	/**
	 * @return
	 * @see java.util.Map#hashCode()
	 */
	public int hashCode() {
		return data.hashCode();
	}

	/**
	 * @return
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty() {
		return data.isEmpty();
	}

	/**
	 * @return
	 * @see java.util.Map#keySet()
	 */
	public Set<K> keySet() {
		return data.keySet();
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public V put(K key, V value) {
		V val = data.put(key, value);
		this.changed.set(true);
		this.support.firePropertyChange(name,val, value);
		return val;
	}

	/**
	 * @param arg0
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map<? extends K, ? extends V> arg0) {
		data.putAll(arg0);
		this.changed.set(true);
		this.support.firePropertyChange(name, null, arg0);		
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public V remove(Object key) {
		V val = data.remove(key);
		this.changed.set(true);
		this.support.firePropertyChange(name, val, null);
		return val;
	}

	/**
	 * @return
	 * @see java.util.Map#size()
	 */
	public int size() {
		return data.size();
	}

	/**
	 * @return
	 * @see java.util.Map#values()
	 */
	public Collection<V> values() {
		return data.values();
	}

	public boolean checkChangedNClear() {
		return this.changed.compareAndSet(true, false);
	}

	public boolean isChanged() {
		return this.changed.get();
	}
}
