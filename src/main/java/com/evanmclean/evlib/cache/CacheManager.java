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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.evanmclean.evlib.util.CompareCase;

/**
 * Manages a set of {@link ConcurrentHashMapCache} caches. Caches are
 * thread-safe, expire entries after a specified time-to-live, can be bound to a
 * maximum number of entries, and can contain strong, soft or weak references to
 * their values.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class CacheManager
{
  private Logger log;
  private int maxTotalEntries = 0;

  private final Object[] cachesLock = new Object[0];
  private final ConcurrentMap<String, HashMapCache<?, ?>> caches = new ConcurrentHashMap<String, HashMapCache<?, ?>>();

  private static final long EXPIRATION_RUN_WINDOW = TimeUnit.SECONDS
      .toMillis(30L);
  long nextExpirationRun = 0L;

  final Object[] entriesLock = new Object[0];
  final TreeSet<CacheEntry<?, ?>> entries = new TreeSet<CacheEntry<?, ?>>(
      new Comparator<CacheEntry<?, ?>>() {
        public int compare( final CacheEntry<?, ?> lhs,
            final CacheEntry<?, ?> rhs )
        {
          if ( lhs == rhs )
            return 0;
          long ediff = lhs.origExpires - rhs.origExpires;
          if ( ediff != 0L )
            return (ediff < 0) ? -1 : 1;
          int hdiff = System.identityHashCode(lhs)
              - System.identityHashCode(rhs);
          if ( hdiff == 0 )
            return lhs.getCacheName().compareTo(rhs.getCacheName());
          return (hdiff < 0) ? -1 : 1;
        }
      });

  /**
   * Construct a cache manager with no total maximum entries.
   */
  public CacheManager()
  {
    this(0, null);
  }

  /**
   * Construct a cache manager with the specified total maximum entries.
   * 
   * @param max_total_entries
   *        The total maximum entries to be held across all caches (0 for no
   *        overall limit).
   */
  public CacheManager( final int max_total_entries )
  {
    this(max_total_entries, null);
  }

  /**
   * Construct a cache manager with the specified total maximum entries.
   * 
   * @param max_total_entries
   *        The total maximum entries to be held across all caches (0 for no
   *        overall limit).
   * @param log
   *        Use this logger to perform detailed logging (null for no logging).
   */
  public CacheManager( final int max_total_entries, final Logger log )
  {
    setMaxTotalEntries(max_total_entries);
    this.log = log;
  }

  /**
   * Construct a cache manager with no total maximum entries.
   * 
   * @param log
   *        Use this logger to perform detailed logging (null for no logging).
   */
  public CacheManager( final Logger log )
  {
    this(0, log);
  }

  public CacheBuilder builder()
  {
    return new CacheBuilder(this);
  }

  /**
   * Check if a cache with a particular name exists.
   * 
   * @param name
   *        Name of cache to check for.
   * @return True if a cache with that name exists.
   */
  public boolean exists( final String name )
  {
    return caches.containsKey(name);
  }

  /**
   * Run an expiration over all managed caches.
   * 
   * @return Return true if there were any changes to any cache.
   */
  public boolean expire()
  {
    log("expire: Getting entries lock.");
    try
    {
      synchronized ( entriesLock )
      {
        log("expire: Got entries lock.");
        return expire(System.currentTimeMillis());
      }
    }
    finally
    {
      log("expire: Released entries lock.");
    }
  }

  /**
   * Returns a previously created cache. Use {@link #builder()} to create a new
   * cache.
   * 
   * @param <K>
   * @param <V>
   * @param name
   *        The name of the cache.
   * @return The previously created cache.
   * @throws UnknownCacheException
   *         If the cache does not exist.
   */
  @SuppressWarnings( "unchecked" )
  public <K, V> ConcurrentHashMapCache<K, V> getCache( final String name )
  {
    final ConcurrentHashMapCache<K, V> cache = (ConcurrentHashMapCache<K, V>) caches
        .get(name);
    if ( cache == null )
      throw new UnknownCacheException(name);
    return cache;
  }

  /**
   * Gets the list off all caches being managed.
   * 
   * @return Gets the list off all caches being managed.
   */
  public Set<String> getCacheNames()
  {
    log("getCacheNames: Getting caches lock.");
    try
    {
      synchronized ( cachesLock )
      {
        log("getCacheNames: Got caches lock.");
        return Collections
            .unmodifiableSet(new TreeSet<String>(caches.keySet()));
      }
    }
    finally
    {
      log("getCacheNames: Released caches lock.");
    }
  }

  /**
   * The logger being used (or null for no logging).
   * 
   * @return The logger being used (or null for no logging).
   */
  public Logger getLogger()
  {
    return log;
  }

  /**
   * The total number of entries that will be held across all caches (0 for
   * unlimited).
   * 
   * @return The total number of entries that will be held across all caches (0
   *         for unlimited).
   */
  public int getMaxTotalEntries()
  {
    return maxTotalEntries;
  }

  /**
   * Remove all managed caches. Removed caches pretty much become noop objects
   * which act as empty maps that don't store anything.
   */
  public void removeAllCaches()
  {
    log("removeAllCaches: Getting entries lock.");
    try
    {
      synchronized ( entriesLock )
      {
        log("removeAllCaches: Got entries lock. Getting caches lock.");
        try
        {
          synchronized ( cachesLock )
          {
            log("removeAllCaches: Got caches lock. Removing all caches.");
            final ArrayList<HashMapCache<?, ?>> arr = new ArrayList<HashMapCache<?, ?>>(
                caches.values());
            caches.clear();
            for ( HashMapCache<?, ?> cache : arr )
              cache.removed();
            log("removeAllCaches: Removed all the caches.");
          }
        }
        finally
        {
          log("removeAllCaches: Released caches lock.");
        }
      }
    }
    finally
    {
      log("removeAllCaches: Released entries lock.");
    }
  }

  /**
   * Remove the specified cache. Removed caches pretty much become noop objects
   * which act as empty maps that don't store anything.
   * 
   * @param name
   *        Name of the cache to remove.
   * @return True if the cache existed and was removed.
   */
  public boolean removeCache( final String name )
  {
    log("removeCache: Getting entries lock.");
    try
    {
      synchronized ( entriesLock )
      {
        log("removeCache: Got entries lock. Getting caches lock.");
        try
        {
          synchronized ( cachesLock )
          {
            log("removeCache: Got caches lock. Removing cache {}.", name);
            final HashMapCache<?, ?> cache = caches.remove(name);
            if ( cache == null )
            {
              log("removeCache: Cache {} did not exist.", name);
              return false;
            }
            cache.removed();
            log("removeCache: Cache {} removed.", name);
            return true;
          }
        }
        finally
        {
          log("removeCache: Released caches lock.");
        }
      }
    }
    finally
    {
      log("removeCache: Released entries lock.");
    }
  }

  /**
   * Set the logger to use to perform detailed logging (null for no logging).
   * 
   * @param log
   *        Use this logger to perform detailed logging (null for no logging).
   */
  public void setLogger( final Logger log )
  {
    this.log = log;
  }

  /**
   * Set the total number of entries that will be held across all caches (0 for
   * unlimited).
   * 
   * @param max_total_entries
   *        The total number of entries that will be held across all caches (0
   *        for unlimited).
   */
  public void setMaxTotalEntries( final int max_total_entries )
  {
    if ( max_total_entries < 0 )
      throw new IllegalArgumentException(
          "Max Total Entries must be zero or positive number.");
    log("Max total entries set to {}.", max_total_entries);
    this.maxTotalEntries = Math.max(0, max_total_entries);
  }

  /**
   * Total number of entries across all managed caches.
   * 
   * @return Total number of entries across all managed caches.
   */
  public int size()
  {
    log("size: Getting entries lock.");
    try
    {
      synchronized ( entriesLock )
      {
        log("size: Got entries lock.");
        int sz = entries.size();
        log("size: Managing {} entries.", sz);
        return sz;
      }
    }
    finally
    {
      log("size: Released entries lock.");
    }
  }

  @Override
  public String toString()
  {
    final Map<String, Integer> cs = new TreeMap<String, Integer>(
        new CompareCase());
    final int sz;
    log("toString: Getting caches lock.");
    try
    {
      synchronized ( cachesLock )
      {
        log("toString: Got caches lock.");
        for ( Map.Entry<String, HashMapCache<?, ?>> entry : caches.entrySet() )
          cs.put(entry.getKey(), entry.getValue().size());
        sz = entries.size();
      }
    }
    finally
    {
      log("toString: Released caches lock.");
    }
    final StringBuilder buff = new StringBuilder("[Total Entries: ");
    buff.append(sz);
    buff.append(", Caches[");
    boolean first = true;
    for ( Map.Entry<String, Integer> entry : cs.entrySet() )
    {
      if ( first )
        first = false;
      else
        buff.append(", ");
      buff.append(entry.getKey());
      buff.append(": ");
      buff.append(entry.getValue());
    }
    buff.append("]]");
    return buff.toString();
  }

  void log( final String msg )
  {
    if ( log != null )
      log.info(msg);
  }

  void log( final String msg, final Object arg )
  {
    if ( log != null )
      log.info(msg, arg);
  }

  void log( final String msg, final Object... args )
  {
    if ( log != null )
      log.info(msg, args);
  }

  void log( final String msg, final Object arg1, final Object arg2 )
  {
    if ( log != null )
      log.info(msg, arg1, arg2);
  }

  void log( final String msg, final Throwable ex )
  {
    if ( log != null )
      log.info(msg, ex);
  }

  /**
   * Create a cache (if it doesn't already exist). Caches are identified by
   * their name, and if a cache already exists, then this method returns the
   * pre-existing cache and the rest of the arguments to the method are ignored.
   * 
   * @param <K>
   * @param <V>
   * @param name
   *        The name of the cache.
   * @param ttl
   *        The default time-to-live for entries, in milliseconds.
   * @param refresh
   *        Retrieving values refreshs their expiry time in the cache.
   * @param max_entries
   *        The maximum number of entries this cache can hold (0 for unlimited).
   * @param crt
   *        How values in the cache's map are referenced.
   * @param value_disposer
   *        The value disposer (can be null).
   * @param force
   *        The cache must not already exist.
   * @return The cache.
   * @throws CacheExistsException
   *         Thrown in <code>force</code> is true and the cache already exists.
   */
  @SuppressWarnings( "unchecked" )
  <K, V> ConcurrentHashMapCache<K, V> makeCache( final String name,
      final long ttl, final boolean refresh, final int max_entries,
      final CacheReferenceType crt, final CacheValueDisposer<V> value_disposer,
      final boolean force ) throws CacheExistsException
  {
    log("makeCache: Getting caches lock.");
    try
    {
      synchronized ( cachesLock )
      {
        log("makeCache: Got caches lock.");
        HashMapCache<K, V> cache = (HashMapCache<K, V>) caches.get(name);
        if ( cache != null )
        {
          if ( force )
          {
            log("Cache {} already exists, to throwing an exception.", name);
            throw new CacheExistsException(name);
          }
        }
        else
        {
          cache = (HashMapCache<K, V>) caches.get(name);
          if ( cache != null )
          {
            log("Cache {} already exists, so just returning it.", name);
          }
          else
          {
            cache = new HashMapCache<K, V>(this, name, ttl, refresh,
                max_entries, crt, value_disposer);
            caches.put(name, cache);
            log(
              "Made cache {} with ttl {}, refresh {}, max entries {}, and ref type {}.",
              name, ttl, refresh, max_entries, crt);
          }
        }
        return cache;
      }
    }
    finally
    {
      log("makeCache: Released caches lock.");
    }
  }

  boolean expire( final long now )
  {
    boolean any_changes = false;
    log("expire: Performing expiration run.");

    // Expire anything that has passed its TTL.
    {
      while ( !entries.isEmpty() )
        try
        {
          final CacheEntry<?, ?> entry = entries.first();
          final Object value = entry.getValue();
          if ( (entry.origExpires > now) && (value != null) )
            break;

          entries.remove(entry);
          if ( (entry.currExpires > now) && (value != null) )
          {
            entry.origExpires = entry.currExpires;
            entries.add(entry);
          }
          else
          {
            entry.expire();
            any_changes = true;
            log("Expired {}.Key[{}] (old)", entry.getCacheName(), entry.key);
          }
        }
        catch ( NoSuchElementException ex )
        {
          // ignore
          log("Exception while going through the entries container.", ex);
        }
    }

    // For each cache, expire entries if they are greater than the max entries
    // value for that cache.
    {
      log("expire: Getting caches lock.");
      try
      {
        synchronized ( cachesLock )
        {
          log("expire: Got caches lock.");
          for ( HashMapCache<?, ?> cache : caches.values() )
          {
            final int max_entries = cache.getMaxEntries();
            if ( max_entries > 0 )
            {
              final int sz = cache.size();
              if ( sz > max_entries )
              {
                final String cache_name = cache.getName();
                log(
                  "Cache {} has {} too many entries, getting rid of some old ones.",
                  cache_name, sz - max_entries);
                while ( cache.size() > max_entries )
                {
                  try
                  {
                    final Iterator<CacheEntry<?, ?>> it = entries.iterator();
                    CacheEntry<?, ?> entry = it.hasNext() ? it.next() : null;
                    while ( (cache.size() > max_entries) && (entry != null) )
                    {
                      while ( (entry != null)
                          && (!entry.getCacheName().equals(cache_name)) )
                      {
                        if ( entry.getValue() == null )
                        {
                          final CacheEntry<?, ?> remove = entry;
                          it.remove();
                          remove.expire();
                          any_changes = true;
                          log("Expired {}.Key[{}] (no value).",
                            remove.getCacheName(), remove.key);
                        }
                        entry = it.hasNext() ? it.next() : null;
                      }
                      if ( entry != null )
                      {
                        final CacheEntry<?, ?> remove = entry;
                        it.remove();
                        entry = it.hasNext() ? it.next() : null;
                        if ( (remove.origExpires != remove.currExpires)
                            && (remove.getValue() != null) )
                        {
                          remove.origExpires = remove.currExpires;
                          entries.add(remove);
                        }
                        else
                        {
                          remove.expire();
                          any_changes = true;
                          log("Expired {}.Key[{}] (max entries).",
                            remove.getCacheName(), remove.key);
                        }
                      }
                    }
                  }
                  catch ( ConcurrentModificationException ex )
                  {
                    log("Exception while going through the entries container.",
                      ex);
                  }
                }
              }
            }
          }
        }
      }
      finally
      {
        log("expire: Released caches lock.");
      }
    }

    // Now check the total max entries.
    final int max_total_entries = maxTotalEntries;
    if ( max_total_entries > 0 )
    {
      final int sz = entries.size();
      if ( sz > max_total_entries )
      {
        log(
          "Overall we have {} too many entires, getting rid of some old ones.",
          sz - max_total_entries);
        while ( entries.size() > maxTotalEntries )
          try
          {
            final CacheEntry<?, ?> entry = entries.first();
            entries.remove(entry);
            if ( (entry.origExpires != entry.currExpires)
                && (entry.getValue() != null) )
            {
              entry.origExpires = entry.currExpires;
              entries.add(entry);
            }
            else
            {
              entry.expire();
              any_changes = true;
              log("Expired {}.Key[{}] (total max entries).",
                entry.getCacheName(), entry.key);
            }
          }
          catch ( NoSuchElementException ex )
          {
            // ignore
            log("Exception while going through the entries container.", ex);
          }
      }
    }
    nextExpirationRun = now + EXPIRATION_RUN_WINDOW;
    return any_changes;
  }
}
