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
package com.evanmclean.evlib.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.WeakHashMap;

/**
 * <p>
 * Factory for creating collection objects with a bit of short-hand (plus a few
 * other utilities). See <a
 * href="http://www-128.ibm.com/developerworks/java/library/j-jtp02216.html"
 * target="_blank">this article</a> about how it works.
 * </p>
 * 
 * <p>
 * Quick example:
 * </p>
 * 
 * <blockquote>
 * <tt>ArrayList&lt;String&gt; arrlist = CollFactory.newArrayList()</tt>
 * </blockquote>
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class Colls
{
  public static <E> ArrayList<E> newArrayList()
  {
    return new ArrayList<E>();
  }

  public static <E> ArrayList<E> newArrayList(
      final Collection<? extends E> coll )
  {
    return new ArrayList<E>(coll);
  }

  public static <E> ArrayList<E> newArrayList( final int initial_capacity )
  {
    return new ArrayList<E>(initial_capacity);
  }

  public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(
      final Class<K> key_type )
  {
    return new EnumMap<K, V>(key_type);
  }

  public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(
      final EnumMap<K, ? extends V> map )
  {
    return new EnumMap<K, V>(map);
  }

  public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(
      final Map<K, ? extends V> map )
  {
    return new EnumMap<K, V>(map);
  }

  public static <K, V> HashMap<K, V> newHashMap()
  {
    return new HashMap<K, V>();
  }

  public static <K, V> HashMap<K, V> newHashMap( final int initial_capacity )
  {
    return new HashMap<K, V>(initial_capacity);
  }

  public static <K, V> HashMap<K, V> newHashMap( final int initial_capacity,
      final float load_factor )
  {
    return new HashMap<K, V>(initial_capacity, load_factor);
  }

  public static <K, V> HashMap<K, V> newHashMap(
      final Map<? extends K, ? extends V> map )
  {
    return new HashMap<K, V>(map);
  }

  public static <V> HashSet<V> newHashSet()
  {
    return new HashSet<V>();
  }

  public static <V> HashSet<V> newHashSet( final Collection<? extends V> coll )
  {
    return new HashSet<V>(coll);
  }

  public static <V> HashSet<V> newHashSet( final int initial_capacity )
  {
    return new HashSet<V>(initial_capacity);
  }

  public static <V> HashSet<V> newHashSet( final int initial_capacity,
      final float load_factor )
  {
    return new HashSet<V>(initial_capacity, load_factor);
  }

  public static <K, V> Hashtable<K, V> newHashtable()
  {
    return new Hashtable<K, V>();
  }

  public static <K, V> Hashtable<K, V> newHashtable( final int initial_capacity )
  {
    return new Hashtable<K, V>(initial_capacity);
  }

  public static <K, V> Hashtable<K, V> newHashtable(
      final int initial_capacity, final float load_factor )
  {
    return new Hashtable<K, V>(initial_capacity, load_factor);
  }

  public static <K, V> Hashtable<K, V> newHashtable(
      final Map<? extends K, ? extends V> map )
  {
    return new Hashtable<K, V>(map);
  }

  public static <K, V> IdentityHashMap<K, V> newIdentityHashMap()
  {
    return new IdentityHashMap<K, V>();
  }

  public static <K, V> IdentityHashMap<K, V> newIdentityHashMap(
      final int expected_max_size )
  {
    return new IdentityHashMap<K, V>(expected_max_size);
  }

  public static <K, V> IdentityHashMap<K, V> newIdentityHashMap(
      final Map<? extends K, ? extends V> map )
  {
    return new IdentityHashMap<K, V>(map);
  }

  public static <K, V> LinkedHashMap<K, V> newLinkedHashMap()
  {
    return new LinkedHashMap<K, V>();
  }

  public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(
      final int initial_capacity )
  {
    return new LinkedHashMap<K, V>(initial_capacity);
  }

  public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(
      final int initial_capacity, final float load_factor )
  {
    return new LinkedHashMap<K, V>(initial_capacity, load_factor);
  }

  public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(
      final int initial_capacity, final float load_factor,
      final boolean access_order )
  {
    return new LinkedHashMap<K, V>(initial_capacity, load_factor, access_order);
  }

  public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(
      final Map<? extends K, ? extends V> map )
  {
    return new LinkedHashMap<K, V>(map);
  }

  public static <V> LinkedHashSet<V> newLinkedHashSet()
  {
    return new LinkedHashSet<V>();
  }

  public static <V> LinkedHashSet<V> newLinkedHashSet(
      final Collection<? extends V> coll )
  {
    return new LinkedHashSet<V>(coll);
  }

  public static <V> LinkedHashSet<V> newLinkedHashSet(
      final int initial_capacity )
  {
    return new LinkedHashSet<V>(initial_capacity);
  }

  public static <V> LinkedHashSet<V> newLinkedHashSet(
      final int initial_capacity, final float load_factor )
  {
    return new LinkedHashSet<V>(initial_capacity, load_factor);
  }

  public static <V> LinkedList<V> newLinkedList()
  {
    return new LinkedList<V>();
  }

  public static <V> LinkedList<V> newLinkedList(
      final Collection<? extends V> coll )
  {
    return new LinkedList<V>(coll);
  }

  public static <V> PriorityQueue<V> newPriorityQueue()
  {
    return new PriorityQueue<V>();
  }

  public static <V> PriorityQueue<V> newPriorityQueue(
      final Collection<? extends V> coll )
  {
    return new PriorityQueue<V>(coll);
  }

  public static <V> PriorityQueue<V> newPriorityQueue(
      final int initial_capacity )
  {
    return new PriorityQueue<V>(initial_capacity);
  }

  public static <V> PriorityQueue<V> newPriorityQueue(
      final int initial_capacity, final Comparator<? super V> comparator )
  {
    return new PriorityQueue<V>(initial_capacity, comparator);
  }

  public static <V> PriorityQueue<V> newPriorityQueue(
      final PriorityQueue<? extends V> queue )
  {
    return new PriorityQueue<V>(queue);
  }

  public static <V> PriorityQueue<V> newPriorityQueue(
      final SortedSet<? extends V> set )
  {
    return new PriorityQueue<V>(set);
  }

  public static <K, V> TreeMap<K, V> newTreeMap()
  {
    return new TreeMap<K, V>();
  }

  public static <K, V> TreeMap<K, V> newTreeMap(
      final Comparator<? super K> comparator )
  {
    return new TreeMap<K, V>(comparator);
  }

  public static <K, V> TreeMap<K, V> newTreeMap(
      final Map<? extends K, ? extends V> map )
  {
    return new TreeMap<K, V>(map);
  }

  public static <K, V> TreeMap<K, V> newTreeMap(
      final SortedMap<? extends K, ? extends V> map )
  {
    return new TreeMap<K, V>(map);
  }

  public static <V> TreeMapIgnoreCase<V> newTreeMapIgnoreCase()
  {
    return new TreeMapIgnoreCase<V>();
  }

  public static <V> TreeMapIgnoreCase<V> newTreeMapIgnoreCase(
      final Map<String, ? extends V> map )
  {
    return new TreeMapIgnoreCase<V>(map);
  }

  public static <V> TreeSet<V> newTreeSet()
  {
    return new TreeSet<V>();
  }

  public static <V> TreeSet<V> newTreeSet( final Collection<? extends V> coll )
  {
    return new TreeSet<V>(coll);
  }

  public static <V> TreeSet<V> newTreeSet(
      final Comparator<? super V> comparator )
  {
    return new TreeSet<V>(comparator);
  }

  public static <V> TreeSet<V> newTreeSet( final SortedSet<V> set )
  {
    return new TreeSet<V>(set);
  }

  public static <E> Vector<E> newVector()
  {
    return new Vector<E>();
  }

  public static <E> Vector<E> newVector( final Collection<? extends E> coll )
  {
    return new Vector<E>(coll);
  }

  public static <E> Vector<E> newVector( final int initial_capacity )
  {
    return new Vector<E>(initial_capacity);
  }

  public static <E> Vector<E> newVector( final int initial_capacity,
      final int capacity_increment )
  {
    return new Vector<E>(initial_capacity, capacity_increment);
  }

  public static <K, V> WeakHashMap<K, V> newWeakHashMap()
  {
    return new WeakHashMap<K, V>();
  }

  public static <K, V> WeakHashMap<K, V> newWeakHashMap(
      final int initial_capacity )
  {
    return new WeakHashMap<K, V>(initial_capacity);
  }

  public static <K, V> WeakHashMap<K, V> newWeakHashMap(
      final int initial_capacity, final float load_factor )
  {
    return new WeakHashMap<K, V>(initial_capacity, load_factor);
  }

  public static <K, V> WeakHashMap<K, V> newWeakHashMap(
      final Map<? extends K, ? extends V> map )
  {
    return new WeakHashMap<K, V>(map);
  }

  /**
   * Returns the collection, list, map, set, etc if it is not empty, otherwise
   * returns null.
   * 
   * @param coll
   *        The container object to examine.
   * @return The collection unless it is null or empty.
   */
  public static <T, E extends Collection<T>> E nullIfEmpty( final E coll )
  {
    return ((coll == null) || coll.isEmpty()) ? null : coll;
  }

  private Colls()
  {
    // empty
  }
}
