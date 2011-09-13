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
package com.evanmclean.evlib.lang;

import junit.framework.TestCase;

/**
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class ArrTest extends TestCase
{
  public static final String TEST_ALL_TEST_TYPE = "UNIT";

  public void testIsEmptyNonZero()
  {
    assertFalse(Arr.isEmpty(new boolean[1]));
    assertFalse(Arr.isEmpty(new byte[2]));
    assertFalse(Arr.isEmpty(new char[3]));
    assertFalse(Arr.isEmpty(new double[4]));
    assertFalse(Arr.isEmpty(new float[5]));
    assertFalse(Arr.isEmpty(new int[6]));
    assertFalse(Arr.isEmpty(new long[7]));
    assertFalse(Arr.isEmpty(new short[8]));
    assertFalse(Arr.isEmpty(new Object[9]));
  }

  public void testIsEmptyNull()
  {
    assertTrue(Arr.isEmpty((boolean[]) null));
    assertTrue(Arr.isEmpty((byte[]) null));
    assertTrue(Arr.isEmpty((char[]) null));
    assertTrue(Arr.isEmpty((double[]) null));
    assertTrue(Arr.isEmpty((float[]) null));
    assertTrue(Arr.isEmpty((int[]) null));
    assertTrue(Arr.isEmpty((long[]) null));
    assertTrue(Arr.isEmpty((short[]) null));
    assertTrue(Arr.isEmpty((Object[]) null));
  }

  public void testIsEmptyZero()
  {
    assertTrue(Arr.isEmpty(new boolean[0]));
    assertTrue(Arr.isEmpty(new byte[0]));
    assertTrue(Arr.isEmpty(new char[0]));
    assertTrue(Arr.isEmpty(new double[0]));
    assertTrue(Arr.isEmpty(new float[0]));
    assertTrue(Arr.isEmpty(new int[0]));
    assertTrue(Arr.isEmpty(new long[0]));
    assertTrue(Arr.isEmpty(new short[0]));
    assertTrue(Arr.isEmpty(new Object[0]));
  }

  public void testIsNotEmptyNonZero()
  {
    assertTrue(Arr.isNotEmpty(new boolean[1]));
    assertTrue(Arr.isNotEmpty(new byte[2]));
    assertTrue(Arr.isNotEmpty(new char[3]));
    assertTrue(Arr.isNotEmpty(new double[4]));
    assertTrue(Arr.isNotEmpty(new float[5]));
    assertTrue(Arr.isNotEmpty(new int[6]));
    assertTrue(Arr.isNotEmpty(new long[7]));
    assertTrue(Arr.isNotEmpty(new short[8]));
    assertTrue(Arr.isNotEmpty(new Object[9]));
  }

  public void testIsNotEmptyNull()
  {
    assertFalse(Arr.isNotEmpty((boolean[]) null));
    assertFalse(Arr.isNotEmpty((byte[]) null));
    assertFalse(Arr.isNotEmpty((char[]) null));
    assertFalse(Arr.isNotEmpty((double[]) null));
    assertFalse(Arr.isNotEmpty((float[]) null));
    assertFalse(Arr.isNotEmpty((int[]) null));
    assertFalse(Arr.isNotEmpty((long[]) null));
    assertFalse(Arr.isNotEmpty((short[]) null));
    assertFalse(Arr.isNotEmpty((Object[]) null));
  }

  public void testIsNotEmptyZero()
  {
    assertFalse(Arr.isNotEmpty(new boolean[0]));
    assertFalse(Arr.isNotEmpty(new byte[0]));
    assertFalse(Arr.isNotEmpty(new char[0]));
    assertFalse(Arr.isNotEmpty(new double[0]));
    assertFalse(Arr.isNotEmpty(new float[0]));
    assertFalse(Arr.isNotEmpty(new int[0]));
    assertFalse(Arr.isNotEmpty(new long[0]));
    assertFalse(Arr.isNotEmpty(new short[0]));
    assertFalse(Arr.isNotEmpty(new Object[0]));
  }

  public void testLengthNonZero()
  {
    assertEquals(1, Arr.length(new boolean[1]));
    assertEquals(2, Arr.length(new byte[2]));
    assertEquals(3, Arr.length(new char[3]));
    assertEquals(4, Arr.length(new double[4]));
    assertEquals(5, Arr.length(new float[5]));
    assertEquals(6, Arr.length(new int[6]));
    assertEquals(7, Arr.length(new long[7]));
    assertEquals(8, Arr.length(new short[8]));
    assertEquals(9, Arr.length(new Object[9]));
  }

  public void testLengthNull()
  {
    assertEquals(0, Arr.length((boolean[]) null));
    assertEquals(0, Arr.length((byte[]) null));
    assertEquals(0, Arr.length((char[]) null));
    assertEquals(0, Arr.length((double[]) null));
    assertEquals(0, Arr.length((float[]) null));
    assertEquals(0, Arr.length((int[]) null));
    assertEquals(0, Arr.length((long[]) null));
    assertEquals(0, Arr.length((short[]) null));
    assertEquals(0, Arr.length((Object[]) null));
  }

  public void testLengthZero()
  {
    assertEquals(0, Arr.length(new boolean[0]));
    assertEquals(0, Arr.length(new byte[0]));
    assertEquals(0, Arr.length(new char[0]));
    assertEquals(0, Arr.length(new double[0]));
    assertEquals(0, Arr.length(new float[0]));
    assertEquals(0, Arr.length(new int[0]));
    assertEquals(0, Arr.length(new long[0]));
    assertEquals(0, Arr.length(new short[0]));
    assertEquals(0, Arr.length(new Object[0]));
  }

  public void testNullIfEmptyNonZero()
  {
    assertNotNull(Arr.nullIfEmpty(new boolean[1]));
    assertNotNull(Arr.nullIfEmpty(new byte[1]));
    assertNotNull(Arr.nullIfEmpty(new char[1]));
    assertNotNull(Arr.nullIfEmpty(new double[1]));
    assertNotNull(Arr.nullIfEmpty(new float[1]));
    assertNotNull(Arr.nullIfEmpty(new int[1]));
    assertNotNull(Arr.nullIfEmpty(new long[1]));
    assertNotNull(Arr.nullIfEmpty(new short[1]));
    assertNotNull(Arr.nullIfEmpty(new Object[1]));
  }

  public void testNullIfEmptyNull()
  {
    assertNull(Arr.nullIfEmpty((boolean[]) null));
    assertNull(Arr.nullIfEmpty((byte[]) null));
    assertNull(Arr.nullIfEmpty((char[]) null));
    assertNull(Arr.nullIfEmpty((double[]) null));
    assertNull(Arr.nullIfEmpty((float[]) null));
    assertNull(Arr.nullIfEmpty((int[]) null));
    assertNull(Arr.nullIfEmpty((long[]) null));
    assertNull(Arr.nullIfEmpty((short[]) null));
    assertNull(Arr.nullIfEmpty((Object[]) null));
  }

  public void testNullIfEmptyZero()
  {
    assertNull(Arr.nullIfEmpty(new boolean[0]));
    assertNull(Arr.nullIfEmpty(new byte[0]));
    assertNull(Arr.nullIfEmpty(new char[0]));
    assertNull(Arr.nullIfEmpty(new double[0]));
    assertNull(Arr.nullIfEmpty(new float[0]));
    assertNull(Arr.nullIfEmpty(new int[0]));
    assertNull(Arr.nullIfEmpty(new long[0]));
    assertNull(Arr.nullIfEmpty(new short[0]));
    assertNull(Arr.nullIfEmpty(new Object[0]));
  }
}
