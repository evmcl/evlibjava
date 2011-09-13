/*
 * = License =
 * 
 * McLean Computer Services Open Source Software License
 * 
 * (Looks like the BSD license, but less restrictive.)
 * 
 * Copyright (c) 2006-2011 Evan McLean. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * 2. Neither the names "Evan McLean", "McLean Computer Services", "EvLib" nor
 * the names of any contributors may be used to endorse or promote products
 * derived from this software without prior written permission.
 * 
 * 3. Products derived from this software may not be called "Evlib", nor may
 * "Evlib" appear in their name, without prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * = License =
 */
package com.evanmclean.evlib.cache;

import java.io.Closeable;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.evanmclean.evlib.lang.Obj;

/**
 * Implementation of {@link ConcurrentHashMapCache} that works hand in hand with
 * the {@link CacheManager}.
 * 
 * @param <K>
 * @param <V>
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
class HashMapCache<K, V> implements ConcurrentHashMapCache<K, V>, Closeable
{
  private class Active
  {
    final CacheManager cacheManager;
    final ConcurrentHashMap<K, BaseCacheEntry> realMap = new ConcurrentHashMap<K, BaseCacheEntry>();

    Active( final CacheManager cache_manager )
    {
      this.cacheManager = cache_manager;
    }

    @SuppressWarnings( "synthetic-access" )
    V internalPut( final K key, final V value, final long now,
        final long thisttl )
    {
      if ( value == null )
        throw new NullPointerException();
      final BaseCacheEntry new_entry = createEntry(now, thisttl, key, value);
      final BaseCacheEntry prev = realMap.put(key, new_entry);
      if ( prev != null )
        cacheManager.entries.remove(prev);
      cacheManager.entries.add(new_entry);
      cacheManager.log("{}: Put {}", name, key);
      if ( prev != null )
        return prev.getValue();
      return null;
    }

    @SuppressWarnings( "synthetic-access" )
    V internalPutIfAbsent( final K key, final V value, final long now,
        final long thisttl )
    {
      if ( value == null )
        throw new NullPointerException();
      final BaseCacheEntry new_entry = createEntry(now, thisttl, key, value);
      final BaseCacheEntry prev = realMap.putIfAbsent(key, new_entry);
      cacheManager.log("{}: PutIfAbsent {}", name, key);
      if ( prev != null )
        cacheManager.entries.remove(prev);
      cacheManager.entries.add(new_entry);
      if ( prev != null )
        return prev.getValue();
      return null;
    }

    @SuppressWarnings( "synthetic-access" )
    V internalReplace( final K key, final V value, final long now,
        final long thisttl )
    {
      if ( value == null )
        throw new NullPointerException();
      final BaseCacheEntry new_entry = createEntry(now, thisttl, key, value);
      final BaseCacheEntry prev = realMap.replace(key, new_entry);
      cacheManager.log("{}: replace {}", name, key);
      if ( prev != null )
        cacheManager.entries.remove(prev);
      cacheManager.entries.add(new_entry);
      final V prev_value = (prev == null) ? null : prev.getValue();
      if ( prev_value == null )
        realMap.remove(key, new_entry);
      return prev_value;
    }

    @SuppressWarnings( "synthetic-access" )
    boolean internalReplace( final K key, final V old_value, final V new_value,
        final long now, final long thisttl )
    {
      if ( (old_value == null) || (new_value == null) )
        throw new NullPointerException();
      final BaseCacheEntry old_entry = realMap.get(key);
      if ( old_entry == null )
        return false;
      final V prev_val = old_entry.getValue();
      if ( (prev_val == null) || (!prev_val.equals(old_value)) )
        return false;
      final BaseCacheEntry new_entry = createEntry(now, thisttl, key, new_value);
      final boolean replaced = realMap.replace(key, old_entry, new_entry);
      if ( replaced )
      {
        cacheManager.log("{}: replace {}", name, key);
        cacheManager.entries.remove(old_entry);
        cacheManager.entries.add(new_entry);
      }
      return replaced;
    }

    @SuppressWarnings( "synthetic-access" )
    private BaseCacheEntry createEntry( final long now, final long thisttl,
        final K key, final V value )
    {
      switch ( cacheReferenceType )
      {
        case STRONG:
          return new EntryStrong(now, thisttl, key, value);
        case SOFT:
          return new EntrySoft(now, thisttl, key, value);
        case WEAK:
          return new EntryWeak(now, thisttl, key, value);
      }
      throw new IllegalArgumentException("Un-catered for enum vaule: "
          + cacheReferenceType);
    }
  }

  private abstract class BaseCacheEntry extends CacheEntry<K, V>
  {
    BaseCacheEntry( final long now, final long ttl, final K key )
    {
      super(now, ttl, key);
    }

    protected void removeFromCache()
    {
      @SuppressWarnings( "synthetic-access" ) final Active act = active;
      if ( act != null )
        act.realMap.remove(key, this);
    }

    @SuppressWarnings( "synthetic-access" )
    void doRefresh( final long now )
    {
      if ( refresh )
        currExpires = now + ttl;
    }

    @Override
    HashMapCache<?, ?> getCache()
    {
      return HashMapCache.this;
    }

    @SuppressWarnings( "synthetic-access" )
    @Override
    String getCacheName()
    {
      return name;
    }

    abstract void remove();
  }

  private final class EntryIterator extends MapIterator
      implements Iterator<Map.Entry<K, V>>
  {
    public EntryIterator()
    {
      super();
    }

    public Map.Entry<K, V> next()
    {
      return new MapEntry(nextEntry());
    }
  }

  private abstract class EntryRef extends BaseCacheEntry
  {
    private volatile Reference<V> value;

    EntryRef( final long now, final long ttl, final K key,
        final Reference<V> value )
    {
      super(now, ttl, key);
      this.value = value;
    }

    @SuppressWarnings( "synthetic-access" )
    @Override
    void expire()
    {
      final Reference<V> ref = value;
      value = null;
      removeFromCache();
      if ( (valueDisposer != null) && (ref != null) )
      {
        final V val = ref.get();
        if ( val != null )
          try
          {
            valueDisposer.dispose(val);
          }
          catch ( Exception ex )
          {
            // ignore
          }
      }
    }

    @Override
    V getValue()
    {
      final Reference<V> ref = value;
      return (ref == null) ? null : ref.get();
    }

    @Override
    void remove()
    {
      value = null;
      removeFromCache();
    }
  }

  private final class EntrySet extends AbstractSet<Map.Entry<K, V>>
  {
    public EntrySet()
    {
      super();
    }

    @Override
    public void clear()
    {
      HashMapCache.this.clear();
    }

    @Override
    public boolean contains( final Object obj )
    {
      if ( !(obj instanceof Entry<?, ?>) )
        return false;
      final Entry<?, ?> entry = (java.util.Map.Entry<?, ?>) obj;
      final Object key = entry.getKey();
      if ( key == null )
        return false;
      final Object val = HashMapCache.this.get(key);
      return Obj.equals(val, entry.getValue());
    }

    @Override
    public boolean isEmpty()
    {
      return HashMapCache.this.isEmpty();
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator()
    {
      return new EntryIterator();
    }

    @Override
    public boolean remove( final Object obj )
    {
      if ( !(obj instanceof Entry<?, ?>) )
        return false;
      final Entry<?, ?> entry = (java.util.Map.Entry<?, ?>) obj;
      final Object key = entry.getKey();
      if ( key == null )
        return false;
      return HashMapCache.this.remove(key, entry.getValue());
    }

    @Override
    public int size()
    {
      return HashMapCache.this.size();
    }
  }

  private class EntrySoft extends EntryRef
  {
    EntrySoft( final long now, final long ttl, final K key, final V value )
    {
      super(now, ttl, key, new SoftReference<V>(value));
    }
  }

  private class EntryStrong extends BaseCacheEntry
  {
    private volatile V value;

    EntryStrong( final long now, final long ttl, final K key, final V value )
    {
      super(now, ttl, key);
      this.value = value;
    }

    @SuppressWarnings( "synthetic-access" )
    @Override
    void expire()
    {
      final V val = value;
      value = null;
      removeFromCache();
      if ( (valueDisposer != null) && (val != null) )
        try
        {
          valueDisposer.dispose(val);
        }
        catch ( Exception ex )
        {
          // ignore
        }
    }

    @Override
    V getValue()
    {
      return value;
    }

    @Override
    void remove()
    {
      value = null;
      removeFromCache();
    }
  }

  private class EntryWeak extends EntryRef
  {
    EntryWeak( final long now, final long ttl, final K key, final V value )
    {
      super(now, ttl, key, new WeakReference<V>(value));
    }
  }

  private final class KeyIterator extends MapIterator implements Iterator<K>
  {
    public KeyIterator()
    {
      super();
    }

    public K next()
    {
      return nextEntry().key;
    }
  }

  private final class KeySet extends AbstractSet<K>
  {
    public KeySet()
    {
      super();
    }

    @Override
    public void clear()
    {
      HashMapCache.this.clear();
    }

    @Override
    public boolean contains( final Object obj )
    {
      return HashMapCache.this.containsKey(obj);
    }

    @Override
    public boolean isEmpty()
    {
      return HashMapCache.this.isEmpty();
    }

    @Override
    public Iterator<K> iterator()
    {
      return new KeyIterator();
    }

    @Override
    public boolean remove( final Object obj )
    {
      return HashMapCache.this.remove(obj) != null;
    }

    @Override
    public int size()
    {
      return HashMapCache.this.size();
    }
  }

  private final class MapEntry implements Map.Entry<K, V>
  {
    private final K key;
    private V value;

    MapEntry( final BaseCacheEntry entry )
    {
      this.key = entry.key;
      this.value = entry.getValue();
    }

    @Override
    public boolean equals( final Object obj )
    {
      if ( obj == null )
        return false;
      if ( obj == this )
        return true;
      if ( obj instanceof Map.Entry<?, ?> )
      {
        final Map.Entry<?, ?> rhs = (Map.Entry<?, ?>) obj;
        return Obj.equals(key, rhs.getKey())
            && Obj.equals(value, rhs.getValue());
      }
      return false;
    }

    public K getKey()
    {
      return key;
    }

    public V getValue()
    {
      return value;
    }

    @Override
    public int hashCode()
    {
      return ((key == null) ? 0 : key.hashCode()) //
          ^ ((value == null) ? 0 : value.hashCode());
    }

    public V setValue( final V value )
    {
      final V ret = HashMapCache.this.put(key, value);
      this.value = value;
      return ret;
    }
  }

  private abstract class MapIterator
  {
    private final Iterator<BaseCacheEntry> it;
    private BaseCacheEntry nextReturn;
    private BaseCacheEntry lastReturned;

    @SuppressWarnings( "synthetic-access" )
    MapIterator()
    {
      it = (active != null) ? active.realMap.values().iterator()
          : new HashSet<BaseCacheEntry>(1).iterator();
      advance();
    }

    public boolean hasNext()
    {
      return nextReturn != null;
    }

    public void remove()
    {
      final BaseCacheEntry entry = lastReturned;
      lastReturned = null;
      if ( entry == null )
        throw new IllegalStateException();
      HashMapCache.this.remove(entry.key);
    }

    protected BaseCacheEntry nextEntry()
    {
      final BaseCacheEntry entry = nextReturn;
      advance();
      if ( entry == null )
        throw new NoSuchElementException();
      lastReturned = entry;
      return entry;
    }

    private void advance()
    {
      nextReturn = null;
      final long now = System.currentTimeMillis();
      while ( (nextReturn == null) && it.hasNext() )
      {
        final BaseCacheEntry entry = it.next();
        if ( (entry.currExpires > now) && (entry.getValue() != null) )
        {
          nextReturn = entry;
          entry.doRefresh(now);
        }
      }
    }
  }

  private final class ValueColl extends AbstractCollection<V>
  {
    public ValueColl()
    {
      super();
    }

    @Override
    public void clear()
    {
      HashMapCache.this.clear();
    }

    @Override
    public boolean contains( final Object obj )
    {
      return HashMapCache.this.containsValue(obj);
    }

    @Override
    public boolean isEmpty()
    {
      return HashMapCache.this.isEmpty();
    }

    @Override
    public Iterator<V> iterator()
    {
      return new ValueIterator();
    }

    @Override
    public int size()
    {
      return HashMapCache.this.size();
    }
  }

  private final class ValueIterator extends MapIterator implements Iterator<V>
  {
    public ValueIterator()
    {
      super();
    }

    public V next()
    {
      return nextEntry().getValue();
    }
  }

  private volatile Active active;
  private final String name;
  private final CacheReferenceType cacheReferenceType;
  private long ttl;
  private final boolean refresh;
  private int maxEntries;
  private final CacheValueDisposer<V> valueDisposer;

  HashMapCache( final CacheManager cache_manager, final String name,
      final long ttl, final boolean refresh, final int max_entries,
      final CacheReferenceType crt, final CacheValueDisposer<V> value_disposer )
  {
    if ( (value_disposer != null) && (crt != CacheReferenceType.STRONG) )
      throw new IllegalStateException(
          "Can only use a value disposer with strongly referenced caches.");

    this.active = new Active(cache_manager);
    this.name = name;
    this.ttl = ttl;
    this.refresh = refresh;
    this.maxEntries = max_entries;
    this.cacheReferenceType = crt;
    this.valueDisposer = value_disposer;
  }

  public void clear()
  {
    clear(active);
  }

  public void close()
  {
    final Active act = active;
    if ( act != null )
      act.cacheManager.removeCache(name);
  }

  public boolean containsKey( final Object key )
  {
    return get(key) != null;
  }

  public boolean containsValue( final Object value )
  {
    if ( value == null )
      throw new NullPointerException();
    final Active act = active;
    if ( act != null )
    {
      for ( BaseCacheEntry entry : act.realMap.values() )
        if ( value.equals(entry.getValue()) )
          return true;
    }
    return false;
  }

  public Set<Map.Entry<K, V>> entrySet()
  {
    final Active act = active;
    if ( act == null )
      return new HashMap<K, V>().entrySet();
    return new EntrySet();
  }

  @Override
  public boolean equals( final Object obj )
  {
    if ( obj == null )
      return false;
    if ( obj == this )
      return true;
    if ( obj instanceof Map<?, ?> )
      return entrySet().equals(((Map<?, ?>) obj).entrySet());
    return false;
  }

  public V get( final Object key )
  {
    final Active act = active;
    if ( act != null )
    {
      final BaseCacheEntry entry = act.realMap.get(key);
      if ( entry == null )
        return null;
      final long now = System.currentTimeMillis();
      final V value = entry.getValue();
      if ( (value == null) || (entry.currExpires <= now) )
      {
        entry.expire();
        return null;
      }
      entry.doRefresh(now);
      return value;
    }
    return null;
  }

  public CacheManager getCacheManager()
  {
    return active.cacheManager;
  }

  public int getMaxEntries()
  {
    return maxEntries;
  }

  public String getName()
  {
    return name;
  }

  public long getTtlMillis()
  {
    return ttl;
  }

  @Override
  public int hashCode()
  {
    int hc = 0;
    for ( Map.Entry<K, V> entry : entrySet() )
      hc += entry.hashCode();
    return hc;
  }

  public boolean isEmpty()
  {
    final Active act = active;
    if ( act != null )
      return act.realMap.isEmpty();
    return true;
  }

  public boolean isRefreshTtl()
  {
    // emmark Auto-generated method stub
    return false;
  }

  public Set<K> keySet()
  {
    final Active act = active;
    if ( act == null )
      return new HashSet<K>();
    return new KeySet();
  }

  public V put( final K key, final V value )
  {
    return put(key, value, ttl);
  }

  public V put( final K key, final V value, final long thisttl )
  {
    final Active act = active;
    if ( act == null )
      return null;
    validTtl(thisttl);
    act.cacheManager.log("{}.put: Getting entries lock.", name);
    try
    {
      synchronized ( act.cacheManager.entriesLock )
      {
        act.cacheManager.log("{}.put: Got entries lock.", name);
        final long now = System.currentTimeMillis();
        final V prev = act.internalPut(key, value, now, thisttl);
        if ( now <= act.cacheManager.nextExpirationRun )
          act.cacheManager.expire(now);
        return prev;
      }
    }
    finally
    {
      act.cacheManager.log("{}.put: Released entries lock.", name);
    }
  }

  public V put( final K key, final V value, final long thisttl,
      final TimeUnit time_unit )
  {
    return put(key, value, time_unit.toMillis(thisttl));
  }

  public void putAll( final Map<? extends K, ? extends V> map )
  {
    putAll(map, ttl);
  }

  public void putAll( final Map<? extends K, ? extends V> map,
      final long thisttl )
  {
    final Active act = active;
    if ( act != null )
    {
      validTtl(thisttl);
      act.cacheManager.log("{}.putAll: Getting entries lock.", name);
      try
      {
        synchronized ( act.cacheManager.entriesLock )
        {
          act.cacheManager.log("{}.putAll: Got entries lock.", name);
          final long now = System.currentTimeMillis();
          for ( Map.Entry<? extends K, ? extends V> entry : map.entrySet() )
            act.internalPut(entry.getKey(), entry.getValue(), now, thisttl);
          if ( now <= act.cacheManager.nextExpirationRun )
            act.cacheManager.expire(now);
        }
      }
      finally
      {
        act.cacheManager.log("{}.putAll: Released entries lock.", name);
      }
    }
  }

  public void putAll( final Map<? extends K, ? extends V> map,
      final long thisttl, final TimeUnit time_unit )
  {
    putAll(map, time_unit.toMillis(thisttl));
  }

  public V putIfAbsent( final K key, final V value )
  {
    return putIfAbsent(key, value, ttl);
  }

  public V putIfAbsent( final K key, final V value, final long thisttl )
  {
    final Active act = active;
    if ( act == null )
      return null;
    validTtl(thisttl);
    act.cacheManager.log("{}.putIfAbsent: Getting entries lock.", name);
    try
    {
      synchronized ( act.cacheManager.entriesLock )
      {
        act.cacheManager.log("{}.putIfAbsent: Got entries lock.", name);
        final long now = System.currentTimeMillis();
        final V prev = act.internalPutIfAbsent(key, value, now, thisttl);
        if ( now <= act.cacheManager.nextExpirationRun )
          act.cacheManager.expire(now);
        return prev;
      }
    }
    finally
    {
      act.cacheManager.log("{}.putIfAbsent: Released entries lock.", name);
    }
  }

  public V putIfAbsent( final K key, final V value, final long thisttl,
      final TimeUnit time_unit )
  {
    return putIfAbsent(key, value, time_unit.toMillis(thisttl));
  }

  public V remove( final Object key )
  {
    final Active act = active;
    if ( act == null )
      return null;
    act.cacheManager.log("{}.remove(k): Getting entries lock.", name);
    try
    {
      synchronized ( act.cacheManager.entriesLock )
      {
        act.cacheManager.log("{}.remove(k): Got entries lock.", name);
        final BaseCacheEntry prev = act.realMap.remove(key);
        if ( prev != null )
        {
          act.cacheManager.entries.remove(prev);
          return prev.getValue();
        }
        return null;
      }
    }
    finally
    {
      act.cacheManager.log("{}.remove(k): Released entries lock.", name);
    }
  }

  public boolean remove( final Object key, final Object value )
  {
    final Active act = active;
    if ( act == null )
      return false;
    act.cacheManager.log("{}.remove(k,v): Getting entries lock.", name);
    try
    {
      synchronized ( act.cacheManager.entriesLock )
      {
        act.cacheManager.log("{}.remove(k,v): Got entries lock.", name);
        final BaseCacheEntry prev = act.realMap.get(key);
        if ( prev == null )
          return false;
        final V prev_value = prev.getValue();
        if ( (prev_value != null) && (!prev_value.equals(value)) )
          return false;
        final boolean removed = act.realMap.remove(key, prev);
        if ( removed )
          act.cacheManager.entries.remove(prev);
        return removed && (prev_value != null);
      }
    }
    finally
    {
      act.cacheManager.log("{}.remove(k,v): Released entries lock.", name);
    }
  }

  public V replace( final K key, final V value )
  {
    return replace(key, value, ttl);
  }

  public V replace( final K key, final V value, final long thisttl )
  {
    final Active act = active;
    if ( act == null )
      return null;
    validTtl(thisttl);
    act.cacheManager.log("{}.replace(k,v): Getting entries lock.", name);
    try
    {
      synchronized ( act.cacheManager.entriesLock )
      {
        act.cacheManager.log("{}.replace(k,v): Got entries lock.", name);
        final long now = System.currentTimeMillis();
        final V prev = act.internalReplace(key, value, now, thisttl);
        if ( now <= act.cacheManager.nextExpirationRun )
          act.cacheManager.expire(now);
        return prev;
      }
    }
    finally
    {
      act.cacheManager.log("{}.replace(k,v): Released entries lock.", name);
    }
  }

  public V replace( final K key, final V value, final long thisttl,
      final TimeUnit time_unit )
  {
    return replace(key, value, time_unit.toMillis(thisttl));
  }

  public boolean replace( final K key, final V old_value, final V new_value )
  {
    return replace(key, old_value, new_value, ttl);
  }

  public boolean replace( final K key, final V old_value, final V new_value,
      final long thisttl )
  {
    final Active act = active;
    if ( act == null )
      return false;
    validTtl(thisttl);
    act.cacheManager.log("{}.replace(k,v,v): Getting entries lock.", name);
    try
    {
      synchronized ( act.cacheManager.entriesLock )
      {
        act.cacheManager.log("{}.replace(k,v,v): Got entries lock.", name);
        final long now = System.currentTimeMillis();
        final boolean ret = act.internalReplace(key, old_value, new_value, now,
          thisttl);
        if ( now <= act.cacheManager.nextExpirationRun )
          act.cacheManager.expire(now);
        return ret;
      }
    }
    finally
    {
      act.cacheManager.log("{}.replace(k,v,v): Released entries lock.", name);
    }
  }

  public boolean replace( final K key, final V old_value, final V new_value,
      final long thisttl, final TimeUnit time_unit )
  {
    return replace(key, old_value, new_value, time_unit.toMillis(thisttl));
  }

  public void setMaxEntries( final int max_entries )
  {
    if ( max_entries < 0 )
      throw new IllegalArgumentException(
          "Max Entries must be zero or positive number.");
    this.maxEntries = max_entries;
  }

  public void setTtl( final long ttl, final TimeUnit time_unit )
  {
    final long newttl = time_unit.toMillis(ttl);
    if ( newttl < 1 )
      throw new IllegalArgumentException(
          "TTL must be at least one millisecond.");
    this.ttl = newttl;
  }

  public int size()
  {
    final Active act = active;
    if ( act != null )
      return act.realMap.size();
    return 0;
  }

  @Override
  public String toString()
  {
    final StringBuilder buff = new StringBuilder();
    buff.append('[');
    buff.append(name);
    buff.append(", ");
    final int sz = size();
    buff.append(sz);
    buff.append((sz == 1) ? " entry]" : " entries]");
    return buff.toString();
  }

  public Collection<V> values()
  {
    final Active act = active;
    if ( act == null )
      return new HashMap<K, V>().values();
    return new ValueColl();
  }

  /**
   * This cache has been removed from the cache manager.
   */
  void removed()
  {
    final Active act = active;
    active = null;
    clear(act);
  }

  private void clear( final Active act )
  {
    if ( act != null )
    {
      act.cacheManager.log("{}.clear: Getting entries lock.", name);
      try
      {
        synchronized ( act.cacheManager.entriesLock )
        {
          act.cacheManager.log("{}.clear: Got entries lock.", name);
          final Collection<BaseCacheEntry> copy = new ArrayList<BaseCacheEntry>(
              act.realMap.values());
          act.cacheManager.entries.removeAll(copy);
          for ( BaseCacheEntry entry : copy )
          {
            act.realMap.remove(entry.key, entry);
            entry.expire();
          }
          act.cacheManager.log("Cleared all entries in cache {}.", name);
        }
      }
      finally
      {
        act.cacheManager.log("{}.clear: Released entries lock.", name);
      }
    }
  }

  private void validTtl( final long thisttl )
  {
    if ( thisttl < 1 )
      throw new IllegalArgumentException(
          "TTL must be at least one millisecond.");
  }
}
