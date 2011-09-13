/*
 * = License =

McLean Computer Services Open Source Software License

(Looks like the BSD license, but less restrictive.)

Copyright (c) 2006-2011 Evan McLean. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
list of conditions and the following disclaimer.

2. Neither the names "Evan McLean", "McLean Computer Services", "EvLib" nor the
names of any contributors may be used to endorse or promote products derived
from this software without prior written permission.

3. Products derived from this software may not be called "Evlib", nor may
"Evlib" appear in their name, without prior written permission.

THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

= License =
 */
package com.evanmclean.evlib.util;

import java.util.Iterator;
import java.util.ListIterator;

import junit.framework.TestCase;

/**
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class ArrayListIteratorTest extends TestCase
{
  public static final String TEST_ALL_TEST_TYPE = "UNIT";

  /**
   * @tests java.util.AbstractList#iterator()
   */
  public void testIterator()
  {
    final Object[] arr = new Object[2];
    arr[0] = new Object();
    arr[1] = new Object();
    Iterator<Object> it = new ArrayListIterator<Object>(arr);
    assertTrue(it.hasNext());
    assertSame(arr[0], it.next());
    assertTrue(it.hasNext());
    assertSame(arr[1], it.next());
    assertFalse(it.hasNext());
  }

  /**
   * @tests java.util.AbstractList#listIterator()
   */
  public void testListIterator()
  {
    final Integer[] arr = new Integer[7];
    arr[1] = new Integer(8);
    arr[2] = new Integer(12);
    arr[3] = new Integer(3);
    arr[4] = new Integer(7);
    arr[5] = new Integer(5);

    {
      final ListIterator<Integer> it = new ArrayListIterator<Integer>(arr, 1, 6);
      assertFalse(it.hasPrevious());
      assertTrue(it.hasNext());
      assertEquals(new Integer(8), it.next());
      assertEquals(1, it.nextIndex());
      assertTrue(it.hasPrevious());
      assertEquals(0, it.previousIndex());
      assertEquals(new Integer(8), it.previous());
      assertFalse(it.hasPrevious());
      assertTrue(it.hasNext());
      assertEquals(0, it.nextIndex());
      assertEquals(new Integer(8), it.next());
      assertTrue(it.hasNext());
      assertEquals(1, it.nextIndex());
      assertEquals(new Integer(12), it.next());
      assertTrue(it.hasNext());
      assertEquals(2, it.nextIndex());
      assertEquals(new Integer(3), it.next());
      assertTrue(it.hasNext());
      assertEquals(3, it.nextIndex());
      assertEquals(new Integer(7), it.next());
      assertTrue(it.hasNext());
      assertEquals(4, it.nextIndex());
      assertEquals(new Integer(5), it.next());
      assertFalse(it.hasNext());
      assertEquals(5, it.nextIndex());
      assertTrue(it.hasPrevious());
      assertEquals(4, it.previousIndex());
      assertEquals(new Integer(5), it.previous());
      assertTrue(it.hasPrevious());
      assertEquals(3, it.previousIndex());
      assertEquals(new Integer(7), it.previous());
      assertTrue(it.hasPrevious());
      assertEquals(2, it.previousIndex());
      assertEquals(new Integer(3), it.previous());
      assertTrue(it.hasPrevious());
      assertEquals(1, it.previousIndex());
      assertEquals(new Integer(12), it.previous());
      assertTrue(it.hasPrevious());
      assertEquals(0, it.previousIndex());
      assertEquals(new Integer(8), it.previous());
      assertFalse(it.hasPrevious());
      assertEquals(-1, it.previousIndex());
    }

    {
      final ListIterator<Integer> it = new ArrayListIterator<Integer>(arr, 1,
          6, 5);
      assertTrue(it.hasPrevious());
      assertFalse(it.hasNext());
      assertEquals(new Integer(5), it.previous());
      assertTrue(it.hasNext());
      assertEquals(new Integer(5), it.next());
      assertFalse(it.hasNext());
    }

    {
      final ListIterator<Integer> it = new ArrayListIterator<Integer>(arr, 1, 6);
      assertTrue(it.hasNext());
      assertEquals(new Integer(8), it.next());
      assertTrue(it.hasNext());
      assertEquals(new Integer(12), it.next());
      it.set(new Integer(13));
      assertEquals(new Integer(13), arr[2]);
    }
  }
}
