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

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A thread-safe hash map where the entries expire after a specified
 * time-to-live. The cache is designed such that read-only operations such as
 * {@link #get(Object)} are fast while operations that modify the map may be
 * wrapped in {@link ReentrantLock}s where necessary.
 * 
 * @param <K>
 * @param <V>
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public interface ConcurrentHashMapCache<K, V> extends ConcurrentMap<K, V>
{
  /**
   * Tests if the specified object is a key in this table.
   * 
   * @param key
   *        Possible key.
   * @return <code>true</code> if and only if the specified object is a key in
   *         this table, as determined by the <code>equals</code> method;
   *         <code>false</code> otherwise.
   */
  public boolean containsKey( Object key );

  /**
   * Returns <code>true</code> if this map maps one or more keys to the
   * specified value. Note: This method requires a full internal traversal of
   * the hash table, and so is much slower than method <code>containsKey</code>.
   * 
   * @param value
   *        Value whose presence in this map is to be tested.
   * @return <code>true</code> if this map maps one or more keys to the
   *         specified value.
   */
  public boolean containsValue( Object value );

  /**
   * Get the cache manager responsible for this cache.
   * 
   * @return Get the cache manager responsible for this cache.
   */
  CacheManager getCacheManager();

  /**
   * The maximum number of entries this map will contain.
   * 
   * @return The maximum number of entries this map will contain. 0 for
   *         unbounded.
   */
  int getMaxEntries();

  /**
   * Get the name of this cache.
   * 
   * @return Get the name of this cache.
   */
  String getName();

  /**
   * The default number of milliseconds each entry in the map will stay before
   * being cleared out.
   * 
   * @return The default number of milliseconds each entry in the map will stay
   *         before being cleared out.
   */
  long getTtlMillis();

  /**
   * If true, entries retrieved with {@link #get(Object)} or similar will have
   * their expiry time refreshed.
   * 
   * @return True if entries get their expiry time refreshed on a get.
   */
  boolean isRefreshTtl();

  /**
   * Put an entry in with the specific time to live.
   * 
   * @param key
   * @param value
   * @param thisttl_millisec
   * @return The original entry (if any).
   */
  V put( K key, V value, long thisttl_millisec );

  /**
   * Put an entry in with the specific time to live.
   * 
   * @param key
   * @param value
   * @param thisttl
   * @param time_unit
   * @return The original entry (if any).
   */
  V put( K key, V value, long thisttl, TimeUnit time_unit );

  /**
   * Copies all of the mappings from the specified map to this map, with a
   * specific time to live.
   * 
   * @param map
   * @param thisttl_millisec
   */
  void putAll( Map<? extends K, ? extends V> map, long thisttl_millisec );

  /**
   * Copies all of the mappings from the specified map to this map, with the
   * specified time to live.
   * 
   * @param map
   * @param thisttl
   * @param time_unit
   */
  void putAll( Map<? extends K, ? extends V> map, long thisttl,
      TimeUnit time_unit );

  /**
   * If the specified key is not already associated with a value, associate it
   * with the given value using a specified time to live.
   * 
   * @param key
   * @param value
   * @param thisttl_millisec
   * @return The previous value associated with the specified key, or
   *         <code>null</code> if there was no mapping for the key.
   */
  V putIfAbsent( K key, V value, long thisttl_millisec );

  /**
   * If the specified key is not already associated with a value, associate it
   * with the given value using a specified time to live.
   * 
   * @param key
   * @param value
   * @param thisttl
   * @param time_unit
   * @return The previous value associated with the specified key, or
   *         <code>null</code> if there was no mapping for the key.
   */
  V putIfAbsent( K key, V value, long thisttl, TimeUnit time_unit );

  /**
   * Replaces the entry for a key only if currently mapped to some value. The
   * replaced value with have a specified time to live.
   * 
   * @param key
   * @param value
   * @param thisttl_millisec
   * @return The previous value associated with the specified key, or
   *         <code>null</code> if there was no mapping for the key.
   */
  V replace( K key, V value, long thisttl_millisec );

  /**
   * Replaces the entry for a key only if currently mapped to some value. The
   * replaced value with have a specified time to live.
   * 
   * @param key
   * @param value
   * @param thisttl
   * @param time_unit
   * @return The previous value associated with the specified key, or
   *         <code>null</code> if there was no mapping for the key.
   */
  V replace( K key, V value, long thisttl, TimeUnit time_unit );

  /**
   * Replaces the entry for a key only if currently mapped to a given value. The
   * replaced value with have a specified time to live.
   * 
   * @param key
   * @param old_value
   * @param new_value
   * @param thisttl_millisec
   * @return True if the value was replaced.
   */
  boolean replace( K key, V old_value, V new_value, long thisttl_millisec );

  /**
   * Replaces the entry for a key only if currently mapped to a given value. The
   * replaced value with have a specified time to live.
   * 
   * @param key
   * @param old_value
   * @param new_value
   * @param thisttl
   * @param time_unit
   * @return True if the value was replaced.
   */
  boolean replace( K key, V old_value, V new_value, long thisttl,
      TimeUnit time_unit );

  /**
   * Set the maximum number of entries the map can contain (zero for unbounded).
   * 
   * @param max_entries
   */
  void setMaxEntries( int max_entries );

  /**
   * Set the default time to live for new entries.
   * 
   * @param ttl
   * @param time_unit
   */
  void setTtl( long ttl, TimeUnit time_unit );
}
