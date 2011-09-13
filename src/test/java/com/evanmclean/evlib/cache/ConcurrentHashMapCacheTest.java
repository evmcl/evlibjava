/*
 * Written by Doug Lea with assistance from members of JCP JSR-166 Expert Group
 * and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain Other contributors include
 * Andrew Wright, Jeffrey Hayes, Pat Fisher, Mike Judd.
 */

package com.evanmclean.evlib.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.TestCase;

/**
 * Written by Doug Lea with assistance from members of JCP JSR-166 Expert Group
 * and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain Other contributors include
 * Andrew Wright, Jeffrey Hayes, Pat Fisher, Mike Judd.
 * 
 * Originally named <code>ConcurrentHashMapTest</code>, adapted to test a
 * <code>ConcurrentHashMapCache</code> by Evan M<sup>c</sup>Lean.
 */
public class ConcurrentHashMapCacheTest extends TestCase
{
  public static final String TEST_ALL_TEST_TYPE = "UNIT";

  private final CacheManager cm = new CacheManager();
  private final AtomicInteger nextId = new AtomicInteger();

  private static final Integer zero = new Integer(0);
  private static final Integer one = new Integer(1);
  private static final Integer two = new Integer(2);
  private static final Integer three = new Integer(3);
  private static final Integer four = new Integer(4);
  private static final Integer five = new Integer(5);
  private static final Integer six = new Integer(6);
  private static final Integer seven = new Integer(7);
  private static final Integer m10 = new Integer(-10);

  /**
   * clear removes all pairs
   */
  public void testClear()
  {
    final ConcurrentMap<Integer, String> map = map5();
    map.clear();
    assertEquals(map.size(), 0);
  }

  /**
   * contains returns true for contained value
   */
  public void testContains()
  {
    final ConcurrentHashMapCache<Integer, String> map = map5();
    assertTrue(map.containsValue("A"));
    assertFalse(map.containsValue("Z"));
  }

  /**
   * contains(null) throws NPE
   */
  public void testContains_NullPointerException()
  {
    try
    {
      final ConcurrentHashMapCache<Integer, String> map = map5();
      map.containsValue(null);
      shouldThrow();
    }
    catch ( NullPointerException ex )
    {
      // ignore
    }
  }

  /**
   * containsKey returns true for contained key
   */
  public void testContainsKey()
  {
    final ConcurrentHashMapCache<Integer, String> map = map5();
    assertTrue(map.containsKey(one));
    assertFalse(map.containsKey(zero));
  }

  /**
   * containsKey(null) throws NPE
   */
  public void testContainsKey_NullPointerException()
  {
    try
    {
      final ConcurrentHashMapCache<Integer, String> map = map5();
      map.containsKey(null);
      shouldThrow();
    }
    catch ( NullPointerException ex )
    {
      // ignore
    }
  }

  /**
   * containsValue returns true for held values
   */
  public void testContainsValue()
  {
    final ConcurrentHashMapCache<Integer, String> map = map5();
    assertTrue(map.containsValue("A"));
    assertFalse(map.containsValue("Z"));
  }

  /**
   * containsValue(null) throws NPE
   */
  public void testContainsValue_NullPointerException()
  {
    try
    {
      final ConcurrentHashMapCache<Integer, String> map = map5();
      map.containsValue(null);
      shouldThrow();
    }
    catch ( NullPointerException ex )
    {
      // ignore
    }
  }

  /**
   * entrySet contains all pairs
   */
  public void testEntrySet()
  {
    final ConcurrentHashMapCache<Integer, String> map = map5();
    final Set<Map.Entry<Integer, String>> set = map.entrySet();
    assertEquals(5, set.size());
    final Iterator<Map.Entry<Integer, String>> it = set.iterator();
    while ( it.hasNext() )
    {
      final Map.Entry<Integer, String> entry = it.next();
      assertTrue((entry.getKey().equals(one) && entry.getValue().equals("A"))
          || (entry.getKey().equals(two) && entry.getValue().equals("B"))
          || (entry.getKey().equals(three) && entry.getValue().equals("C"))
          || (entry.getKey().equals(four) && entry.getValue().equals("D"))
          || (entry.getKey().equals(five) && entry.getValue().equals("E")));
    }
  }

  /**
   * entrySet.toArray contains all entries
   */
  @SuppressWarnings( "unchecked" )
  public void testEntrySetToArray()
  {
    final ConcurrentHashMapCache<Integer, String> map = map5();
    final Set<Map.Entry<Integer, String>> set = map.entrySet();
    final Object[] ar = set.toArray();
    assertEquals(5, ar.length);
    for ( int xi = 0; xi < 5; ++xi )
    {
      assertTrue(map.containsKey(((Map.Entry<Integer, String>) (ar[xi]))
          .getKey()));
      assertTrue(map.containsValue(((Map.Entry<Integer, String>) (ar[xi]))
          .getValue()));
    }
  }

  /**
   * Maps with same contents are equal
   */
  public void testEquals()
  {
    final ConcurrentHashMapCache<Integer, String> map1 = map5();
    final ConcurrentHashMapCache<Integer, String> map2 = map5();
    assertEquals(map1, map2);
    assertEquals(map2, map1);
    map1.clear();
    assertFalse(map1.equals(map2));
    assertFalse(map2.equals(map1));
  }

  /**
   * get returns the correct element at the given key, or null if not present
   */
  public void testGet()
  {
    final ConcurrentHashMapCache<Integer, String> map = map5();
    assertEquals("A", map.get(one));
    assertNull(map.get("anything"));
  }

  /**
   * get(null) throws NPE
   */
  public void testGet_NullPointerException()
  {
    try
    {
      final ConcurrentHashMapCache<Integer, String> map = map5();
      map.get(null);
      shouldThrow();
    }
    catch ( NullPointerException ex )
    {
      // ignore
    }
  }

  /**
   * isEmpty is true of empty map and false for non-empty
   */
  public void testIsEmpty()
  {
    final ConcurrentHashMapCache<Integer, String> empty = empty();
    final ConcurrentHashMapCache<Integer, String> map = map5();
    assertTrue(empty.isEmpty());
    assertFalse(map.isEmpty());
  }

  /**
   * keySet returns a Set containing all the keys
   */
  public void testKeySet()
  {
    final ConcurrentHashMapCache<Integer, String> map = map5();
    final Set<Integer> set = map.keySet();
    assertEquals(5, set.size());
    assertTrue(set.contains(one));
    assertTrue(set.contains(two));
    assertTrue(set.contains(three));
    assertTrue(set.contains(four));
    assertTrue(set.contains(five));
  }

  /**
   * keySet.toArray returns contains all keys
   */
  public void testKeySetToArray()
  {
    final ConcurrentHashMapCache<Integer, String> map = map5();
    final Set<Integer> set = map.keySet();
    Object[] ar = set.toArray();
    assertTrue(set.containsAll(Arrays.asList(ar)));
    assertEquals(5, ar.length);
    ar[0] = m10;
    assertFalse(set.containsAll(Arrays.asList(ar)));
  }

  /**
   * put(null,x) throws NPE
   */
  public void testPut1_NullPointerException()
  {
    try
    {
      final ConcurrentHashMapCache<Integer, String> map = map5();
      map.put(null, "whatever");
      shouldThrow();
    }
    catch ( NullPointerException ex )
    {
      // ignore
    }
  }

  /**
   * put(x, null) throws NPE
   */
  public void testPut2_NullPointerException()
  {
    try
    {
      final ConcurrentHashMapCache<Integer, String> map = map5();
      map.put(seven, null);
      shouldThrow();
    }
    catch ( NullPointerException ex )
    {
      // ignore
    }
  }

  /**
   * putAll adds all key-value pairs from the given map
   */
  public void testPutAll()
  {
    final ConcurrentHashMapCache<Integer, String> empty = empty();
    final ConcurrentHashMapCache<Integer, String> map = map5();
    empty.putAll(map);
    assertEquals(5, empty.size());
    assertTrue(empty.containsKey(one));
    assertTrue(empty.containsKey(two));
    assertTrue(empty.containsKey(three));
    assertTrue(empty.containsKey(four));
    assertTrue(empty.containsKey(five));
  }

  /**
   * putIfAbsent works when the given key is not present
   */
  public void testPutIfAbsent()
  {
    final ConcurrentHashMapCache<Integer, String> map = map5();
    map.putIfAbsent(six, "Z");
    assertTrue(map.containsKey(six));
  }

  /**
   * putIfAbsent(null, x) throws NPE
   */
  public void testPutIfAbsent1_NullPointerException()
  {
    try
    {
      final ConcurrentHashMapCache<Integer, String> map = map5();
      map.putIfAbsent(null, "whatever");
      shouldThrow();
    }
    catch ( NullPointerException ex )
    {
      // ignore
    }
  }

  /**
   * putIfAbsent does not add the pair if the key is already present
   */
  public void testPutIfAbsent2()
  {
    final ConcurrentHashMapCache<Integer, String> map = map5();
    assertEquals("A", map.putIfAbsent(one, "Z"));
  }

  /**
   * putIfAbsent(x, null) throws NPE
   */
  public void testPutIfAbsent2_NullPointerException()
  {
    try
    {
      final ConcurrentHashMapCache<Integer, String> map = map5();
      map.putIfAbsent(seven, null);
      shouldThrow();
    }
    catch ( NullPointerException ex )
    {
      // ignore
    }
  }

  /**
   * remove removes the correct key-value pair from the map
   */
  public void testRemove()
  {
    final ConcurrentHashMapCache<Integer, String> map = map5();
    map.remove(five);
    assertEquals(4, map.size());
    assertFalse(map.containsKey(five));
  }

  /**
   * remove(null) throws NPE
   */
  public void testRemove1_NullPointerException()
  {
    try
    {
      final ConcurrentHashMapCache<Integer, String> map = map5();
      map.put(seven, "asdads");
      map.remove(null);
      shouldThrow();
    }
    catch ( NullPointerException ex )
    {
      // ignore
    }
  }

  // Exception tests

  /**
   * remove(key,value) removes only if pair present
   */
  public void testRemove2()
  {
    final ConcurrentHashMapCache<Integer, String> map = map5();
    map.remove(five, "E");
    assertEquals(4, map.size());
    assertFalse(map.containsKey(five));
    map.remove(four, "A");
    assertEquals(4, map.size());
    assertTrue(map.containsKey(four));

  }

  /**
   * remove(null, x) throws NPE
   */
  public void testRemove2_NullPointerException()
  {
    try
    {
      final ConcurrentHashMapCache<Integer, String> map = map5();
      map.put(seven, "asdads");
      map.remove(null, "whatever");
      shouldThrow();
    }
    catch ( NullPointerException ex )
    {
      // ignore
    }
  }

  /**
   * remove(x, null) returns false
   */
  public void testRemove3()
  {
    try
    {
      final ConcurrentHashMapCache<Integer, String> map = map5();
      map.put(seven, "asdads");
      assertFalse(map.remove(seven, null));
    }
    catch ( NullPointerException ex )
    {
      fail();
    }
  }

  /**
   * replace fails when the given key is not present
   */
  public void testReplace()
  {
    final ConcurrentHashMapCache<Integer, String> map = map5();
    assertNull(map.replace(six, "Z"));
    assertFalse(map.containsKey(six));
  }

  /**
   * replace(null, x) throws NPE
   */
  public void testReplace_NullPointerException()
  {
    try
    {
      final ConcurrentHashMapCache<Integer, String> map = map5();
      map.replace(null, "whatever");
      shouldThrow();
    }
    catch ( NullPointerException ex )
    {
      // ignore
    }
  }

  /**
   * replace succeeds if the key is already present
   */
  public void testReplace2()
  {
    final ConcurrentHashMapCache<Integer, String> map = map5();
    assertNotNull(map.replace(one, "Z"));
    assertEquals("Z", map.get(one));
  }

  /**
   * replace(x, null) throws NPE
   */
  public void testReplace2_NullPointerException()
  {
    try
    {
      final ConcurrentHashMapCache<Integer, String> map = map5();
      map.replace(seven, null);
      shouldThrow();
    }
    catch ( NullPointerException ex )
    {
      // ignore
    }
  }

  /**
   * replace value fails when the given key not mapped to expected value
   */
  public void testReplaceValue()
  {
    final ConcurrentHashMapCache<Integer, String> map = map5();
    assertEquals("A", map.get(one));
    assertFalse(map.replace(one, "Z", "Z"));
    assertEquals("A", map.get(one));
  }

  /**
   * replace(null, x, y) throws NPE
   */
  public void testReplaceValue_NullPointerException()
  {
    try
    {
      final ConcurrentHashMapCache<Integer, String> map = map5();
      map.replace(null, "whatever", "whatever", map.getTtlMillis());
      shouldThrow();
    }
    catch ( NullPointerException ex )
    {
      // ignore
    }
  }

  /**
   * replace value succeeds when the given key mapped to expected value
   */
  public void testReplaceValue2()
  {
    final ConcurrentHashMapCache<Integer, String> map = map5();
    assertEquals("A", map.get(one));
    assertTrue(map.replace(one, "A", "Z"));
    assertEquals("Z", map.get(one));
  }

  /**
   * replace(x, null, y) throws NPE
   */
  public void testReplaceValue2_NullPointerException()
  {
    try
    {
      final ConcurrentHashMapCache<Integer, String> map = map5();
      map.replace(seven, null, "A", map.getTtlMillis());
      shouldThrow();
    }
    catch ( NullPointerException ex )
    {
      // ignore
    }
  }

  /**
   * replace(x, y, null) throws NPE
   */
  public void testReplaceValue3_NullPointerException()
  {
    try
    {
      final ConcurrentHashMapCache<Integer, String> map = map5();
      map.replace(seven, "whatever", null, map.getTtlMillis());
      shouldThrow();
    }
    catch ( NullPointerException ex )
    {
      // ignore
    }
  }

  /**
   * SetValue of an EntrySet entry sets value in the map.
   */
  public void testSetValueWriteThrough()
  {
    // Adapted from a bug report by Eric Zoerner
    final ConcurrentHashMapCache<Integer, String> map = empty();
    assertTrue(map.isEmpty());
    for ( int xi = 0; xi < 20; xi++ )
      map.put(new Integer(xi), String.valueOf(xi));
    assertFalse(map.isEmpty());
    Map.Entry<Integer, String> entry1 = map.entrySet().iterator().next();

    // assert that entry1 is not 16
    assertTrue("entry is 16, test not valid",
      !entry1.getKey().equals(new Integer(16)));

    // remove 16 (a different key) from map
    // which just happens to cause entry1 to be cloned in map
    map.remove(new Integer(16));
    entry1.setValue("XYZ");
    assertTrue(map.containsValue("XYZ")); // fails
  }

  /**
   * size returns the correct values
   */
  public void testSize()
  {
    final ConcurrentHashMapCache<Integer, String> map = map5();
    final ConcurrentHashMapCache<Integer, String> empty = empty();
    assertEquals(0, empty.size());
    assertEquals(5, map.size());
  }

  /**
   * values collection contains all values
   */
  public void testValues()
  {
    final ConcurrentHashMapCache<Integer, String> map = map5();
    final Collection<String> coll = map.values();
    assertEquals(5, coll.size());
    assertTrue(coll.contains("A"));
    assertTrue(coll.contains("B"));
    assertTrue(coll.contains("C"));
    assertTrue(coll.contains("D"));
    assertTrue(coll.contains("E"));
  }

  /**
   * Values.toArray contains all values
   */
  public void testValuesToArray()
  {
    final ConcurrentHashMapCache<Integer, String> map = map5();
    final Collection<String> vals = map.values();
    Object[] ar = vals.toArray();
    ArrayList<Object> list = new ArrayList<Object>(Arrays.asList(ar));
    assertEquals(5, ar.length);
    assertTrue(list.contains("A"));
    assertTrue(list.contains("B"));
    assertTrue(list.contains("C"));
    assertTrue(list.contains("D"));
    assertTrue(list.contains("E"));
  }

  /**
   * Create an empty map.
   */
  private ConcurrentHashMapCache<Integer, String> empty()
  {
    try
    {
      final ConcurrentHashMapCache<Integer, String> map = cm.builder()
          .ttl(60 * 60, TimeUnit.SECONDS)
          .build("Cache " + nextId.incrementAndGet());
      assertTrue(map.isEmpty());
      assertEquals(0, map.size());
      return map;
    }
    catch ( CacheExistsException ex )
    {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Create a map from Integers 1-5 to Strings "A"-"E".
   */
  private ConcurrentHashMapCache<Integer, String> map5()
  {
    try
    {
      final ConcurrentHashMapCache<Integer, String> map = cm.builder()
          .ttl(60 * 60, TimeUnit.SECONDS)
          .build("Cache " + nextId.incrementAndGet());
      assertTrue(map.isEmpty());
      map.put(one, "A");
      map.put(two, "B");
      map.put(three, "C");
      map.put(four, "D");
      map.put(five, "E");
      assertFalse(map.isEmpty());
      assertEquals(5, map.size());
      return map;
    }
    catch ( CacheExistsException ex )
    {
      throw new RuntimeException(ex);
    }
  }

  /**
   * fail with message "should throw exception"
   */
  private void shouldThrow()
  {
    fail("Should throw exception");
  }

  @Override
  protected void setUp()
  {
    cm.removeAllCaches();
    nextId.set(0);
  }

  @Override
  protected void tearDown()
  {
    cm.removeAllCaches();
  }
}
