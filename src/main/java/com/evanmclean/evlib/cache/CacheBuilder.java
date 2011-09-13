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
import java.util.concurrent.TimeUnit;

import com.evanmclean.evlib.exceptions.UnhandledException;

/**
 * <a name="cache_builder_overview"><!-- --></a>
 * <p>
 * Builds caches to spec for a cache manager (see {@link CacheManager#builder()}
 * ).
 * </p>
 * 
 * <p>
 * Default builder has the following properites:
 * </p>
 * <ul>
 * <li>Stores values in the cache map with a strong reference.</li>
 * <li><code>ttl</code> of 10 seconds.</li>
 * <li><code>refresh</code> of <code>false</code> indicating expiry times are
 * never refreshed.</li>
 * <li>Does not limit the maximum number of entries in a map (
 * <code>max entries</code> equals 0).</li>
 * <li>No cached value disposer.</li>
 * </ul>
 * 
 * <strong>Refreshing</strong>
 * 
 * <p>
 * Caches include a refresh facility, which can effectively keep most used items
 * in the cache while least used ones expire out of the cache. This is different
 * to the default behaviour in which each item in the cache has a set lifetime
 * (time-to-live).
 * </p>
 * 
 * <p>
 * Note that while you can change the expiry time on an existing cache, this
 * only effects new values that are added to the cache. Existing values will
 * always use the time to live value that was in effect when it was initially
 * added to the cache.
 * </p>
 * 
 * <strong>Reference Types and Value Disposers</strong>
 * 
 * <p>
 * Entries in a cache can be stored with either a strong, soft or weak
 * reference.
 * </p>
 * 
 * <p>
 * A cache can also have a helper value disposer, that is called only any value
 * that is expired by reaching the end of its time to live, or by a call to
 * {@link Map#clear()}. This is useful for cleaning up resources when a value
 * goes out of scope. Only caches that keep a strong reference type can have a
 * value disposer.
 * </p>
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class CacheBuilder
{
  private static final long DEFAULT_TTL = TimeUnit.SECONDS.toMillis(10L);

  private final CacheManager cacheManager;
  private long ttl = DEFAULT_TTL;
  private boolean refresh = false;
  private int maxEntries = 0;
  private CacheReferenceType cacheReferenceType = CacheReferenceType.STRONG;
  private CacheValueDisposer<?> valueDisposer;

  CacheBuilder( final CacheManager cache_manager )
  {
    this.cacheManager = cache_manager;
  }

  /**
   * Builds a cache with the current factory settings. Caches are identified by
   * their name, and an exception is thrown if the cache already exists.
   * 
   * @param <K>
   * @param <V>
   * @param cls
   *        The name of the cache will be the name of the class.
   * @return The cache.
   * @throws CacheExistsException
   *         Thrown if the cache already exists.
   */
  public <K, V> ConcurrentHashMapCache<K, V> build( final Class<?> cls )
    throws CacheExistsException
  {
    return build(cls.getName());
  }

  /**
   * Builds a cache with the current factory settings. Caches are identified by
   * their name, and an exception is thrown if the cache already exists.
   * 
   * @param <K>
   * @param <V>
   * @param name
   *        The name of the cache.
   * @return The cache.
   * @throws CacheExistsException
   *         Thrown if the cache already exists.
   */
  @SuppressWarnings( "unchecked" )
  public <K, V> ConcurrentHashMapCache<K, V> build( final String name )
    throws CacheExistsException
  {
    return cacheManager.makeCache(name, ttl, refresh, maxEntries,
      cacheReferenceType, (CacheValueDisposer<V>) valueDisposer, true);
  }

  /**
   * Builds a cache (if it doesn't already exist) with the current factory
   * settings. Caches are identified by their name, and if a cache already
   * exists, then this method returns the pre-existing cache and the rest of the
   * arguments to the method are ignored.
   * 
   * @param <K>
   * @param <V>
   * @param cls
   *        The name of the cache will be the name of the class.
   * @return The cache.
   */
  public <K, V> ConcurrentHashMapCache<K, V> buildOrGet( final Class<?> cls )
  {
    return buildOrGet(cls.getName());
  }

  /**
   * Builds a cache (if it doesn't already exist) with the current factory
   * settings. Caches are identified by their name, and if a cache already
   * exists, then this method returns the pre-existing cache and the rest of the
   * arguments to the method are ignored.
   * 
   * @param <K>
   * @param <V>
   * @param name
   *        The name of the cache.
   * @return The cache.
   */
  @SuppressWarnings( "unchecked" )
  public <K, V> ConcurrentHashMapCache<K, V> buildOrGet( final String name )
  {
    try
    {
      return cacheManager.makeCache(name, ttl, refresh, maxEntries,
        cacheReferenceType, (CacheValueDisposer<V>) valueDisposer, false);
    }
    catch ( CacheExistsException ex )
    {
      throw new UnhandledException(ex);
    }
  }

  /**
   * Object used to clean up cached values that expire or are removed via a call
   * to {@link ConcurrentHashMapCache#clear()}.
   * 
   * @param value_disposer
   *        Method to clean up expired values (may be <code>null</code>).
   * @return This cache builder.
   */
  public CacheBuilder disposer( final CacheValueDisposer<?> value_disposer )
  {
    this.valueDisposer = value_disposer;
    return this;
  }

  /**
   * Get the cache reference type that caches with be created with.
   * 
   * @return The cache reference type that caches with be created with.
   */
  public CacheReferenceType getCacheReferenceType()
  {
    return cacheReferenceType;
  }

  /**
   * The maximum number of entries caches with contain (0 for unlimited).
   * 
   * @return The maximum number of entries caches with contain (0 for
   *         unlimited).
   */
  public int getMaxEntries()
  {
    return maxEntries;
  }

  /**
   * The time to live for each entry (in milliseconds).
   * 
   * @return The time to live for each entry (in milliseconds).
   */
  public long getTtlMillisec()
  {
    return ttl;
  }

  /**
   * True if retrieving a value from the cache refreshes its expiry time.
   * 
   * @return True if retrieving a value from the cache refreshes its expiry
   *         time.
   */
  public boolean isRefreshTtl()
  {
    return refresh;
  }

  /**
   * Set the maximum number of entries caches will be allowed to contain (0 for
   * unlimited).
   * 
   * @param max_entries
   *        The maximum number of entries caches will be allowed to contain (0
   *        for unlimited).
   * @return This cache builder.
   */
  public CacheBuilder max( final int max_entries )
  {
    if ( max_entries < 0 )
      throw new IllegalArgumentException(
          "Max Entries must be zero or positive number.");
    this.maxEntries = max_entries;
    return this;
  }

  /**
   * Set caches to have no value disposer.
   * 
   * @return This cache builder.
   */
  public CacheBuilder nodisposer()
  {
    this.valueDisposer = null;
    return this;
  }

  /**
   * Sets the cache to not refresh the expiry time of values when they are
   * retrieved by calls like {@link Map#get(Object)}.
   * 
   * @return This cache builder.
   */
  public CacheBuilder norefresh()
  {
    this.refresh = false;
    return this;
  }

  /**
   * The reference type that will be used by caches for storing their values.
   * 
   * @param cache_reference_type
   *        The reference type that will be used by caches for storing their
   *        values.
   * @return This cache builder.
   */
  public CacheBuilder ref( final CacheReferenceType cache_reference_type )
  {
    if ( cache_reference_type == null )
      throw new NullPointerException("Argument cannot be a null.");
    this.cacheReferenceType = cache_reference_type;
    return this;
  }

  /**
   * Sets the cache to refresh the expiry time of values when they are retrieved
   * by calls like {@link Map#get(Object)}.
   * 
   * @return This cache builder.
   */
  public CacheBuilder refresh()
  {
    this.refresh = true;
    return this;
  }

  /**
   * Sets the cache to refresh the expiry time of values when they are retrieved
   * by calls like {@link Map#get(Object)}.
   * 
   * @param refresh_ttl
   *        True to refresh the expiry time of values when they are retrieved.
   * 
   * @return This cache builder.
   */
  public CacheBuilder refresh( final boolean refresh_ttl )
  {
    this.refresh = refresh_ttl;
    return this;
  }

  /**
   * Resets the builder to <a href="#cache_builder_overview">default values</a>.
   * 
   * @return This cache builder.
   */
  public CacheBuilder reset()
  {
    ttl = DEFAULT_TTL;
    refresh = false;
    maxEntries = 0;
    cacheReferenceType = CacheReferenceType.STRONG;
    return this;
  }

  /**
   * Values will be stored in created caches with a soft reference (see
   * {@link CacheReferenceType#SOFT}).
   * 
   * @return This cache builder.
   */
  public CacheBuilder soft()
  {
    this.cacheReferenceType = CacheReferenceType.SOFT;
    return this;
  }

  /**
   * Values will be stored in created caches with a strong reference (see
   * {@link CacheReferenceType#STRONG}).
   * 
   * @return This cache builder.
   */
  public CacheBuilder strong()
  {
    this.cacheReferenceType = CacheReferenceType.STRONG;
    return this;
  }

  /**
   * Set the time to live for values that will be used in the caches created.
   * 
   * @param ttl
   *        The time to live for values that will be used in the caches created.
   * @param time_unit
   *        The time unit for <code>ttl</code>.
   * @return This cache builder.
   */
  public CacheBuilder ttl( @SuppressWarnings( "hiding" ) final long ttl,
      final TimeUnit time_unit )
  {
    final long newttl = time_unit.toMillis(ttl);
    if ( newttl < 1 )
      throw new IllegalArgumentException(
          "TTL must be at least one millisecond.");
    this.ttl = newttl;
    return this;
  }

  /**
   * Values will be stored in created caches with a weak reference (see
   * {@link CacheReferenceType#WEAK}).
   * 
   * @return This cache builder.
   */
  public CacheBuilder weak()
  {
    this.cacheReferenceType = CacheReferenceType.WEAK;
    return this;
  }
}
