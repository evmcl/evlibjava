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
public class StrTest extends TestCase
{
  public static final String TEST_ALL_TEST_TYPE = "UNIT";

  public void testEndsWithIgnoreCase()
  {
    assertTrue(Str.endsWithIgnoreCase("abcdef", "DEF"));
    assertTrue(Str.endsWithIgnoreCase("abcdef", "DeF"));
    assertTrue(Str.endsWithIgnoreCase("abcdef", "def"));
    assertFalse(Str.endsWithIgnoreCase("abcdef", "efg"));
    assertTrue(Str.endsWithIgnoreCase("abcdef", "ABCDEF"));
    assertTrue(Str.endsWithIgnoreCase("abcdef", ""));
    assertFalse(Str.endsWithIgnoreCase("abcdef", "012abcdef"));
  }

  public void testEquals()
  {
    assertTrue(Str.equals(null, null));
    assertTrue(Str.equals("abc", "abc"));
    assertFalse(Str.equals("abc", "def"));
    assertFalse(Str.equals("abc", "ABC"));
    assertFalse(Str.equals("", null));
    assertFalse(Str.equals(null, ""));
  }

  public void testEqualsIgnoreCase()
  {
    assertTrue(Str.equalsIgnoreCase(null, null));
    assertTrue(Str.equalsIgnoreCase("abc", "abc"));
    assertFalse(Str.equalsIgnoreCase("abc", "def"));
    assertTrue(Str.equalsIgnoreCase("abc", "ABC"));
    assertFalse(Str.equalsIgnoreCase("", null));
    assertFalse(Str.equalsIgnoreCase(null, ""));
  }

  public void testEqualsOneOf()
  {
    assertFalse(Str.equalsOneOf(null, "abc", "def", "ghi"));
    assertTrue(Str.equalsOneOf(null, "abc", null, "ghi"));
    assertTrue(Str.equalsOneOf("def", "abc", "def", "ghi"));
    assertFalse(Str.equalsOneOf("123", "abc", "def", "ghi"));
  }

  public void testEqualsOneOfIgnoreCase()
  {
    assertFalse(Str.equalsOneOfIgnoreCase(null, "abc", "def", "ghi"));
    assertTrue(Str.equalsOneOfIgnoreCase(null, "abc", null, "ghi"));
    assertTrue(Str.equalsOneOfIgnoreCase("DEF", "abc", "def", "ghi"));
    assertFalse(Str.equalsOneOfIgnoreCase("123", "abc", "def", "ghi"));
  }

  public void testIfBlank()
  {
    assertEquals("def", Str.ifBlank(null, "def"));
    assertEquals("def", Str.ifBlank("", "def"));
    assertEquals("def", Str.ifBlank("     ", "def"));
    assertEquals("abc", Str.ifBlank("abc", "def"));
    assertEquals(" a b c ", Str.ifBlank(" a b c ", "def"));
  }

  public void testIfEmpty()
  {
    assertEquals("def", Str.ifEmpty(null, "def"));
    assertEquals("def", Str.ifEmpty("", "def"));
    assertEquals("     ", Str.ifEmpty("     ", "def"));
    assertEquals("abc", Str.ifEmpty("abc", "def"));
    assertEquals(" a b c ", Str.ifEmpty(" a b c ", "def"));
  }

  public void testIfNull()
  {
    assertEquals("", Str.ifNull(null));
    assertEquals("abc", Str.ifNull("abc"));
    assertEquals("def", Str.ifNull(null, "def"));
    assertEquals("abc", Str.ifNull("abc", "def"));
    assertEquals("", Str.ifNull("", "def"));
  }

  public void testIsBlank()
  {
    assertTrue(Str.isBlank(null));
    assertTrue(Str.isBlank(""));
    assertTrue(Str.isBlank("     "));
    assertFalse(Str.isBlank("abc"));
    assertFalse(Str.isBlank(" a b c "));
  }

  public void testIsEmpty()
  {
    assertTrue(Str.isEmpty(null));
    assertTrue(Str.isEmpty(""));
    assertFalse(Str.isEmpty("     "));
    assertFalse(Str.isEmpty("abc"));
    assertFalse(Str.isEmpty(" a b c "));
  }

  public void testIsNotBlank()
  {
    assertFalse(Str.isNotBlank(null));
    assertFalse(Str.isNotBlank(""));
    assertFalse(Str.isNotBlank("     "));
    assertTrue(Str.isNotBlank("abc"));
    assertTrue(Str.isNotBlank(" a b c "));
  }

  public void testIsNotEmpty()
  {
    assertFalse(Str.isNotEmpty(null));
    assertFalse(Str.isNotEmpty(""));
    assertTrue(Str.isNotEmpty("     "));
    assertTrue(Str.isNotEmpty("abc"));
    assertTrue(Str.isNotEmpty(" a b c "));
  }

  public void testLength()
  {
    assertEquals(0, Str.length(null));
    assertEquals(0, Str.length(""));
    assertEquals(1, Str.length("a"));
  }

  public void testLtrim()
  {
    assertNull(Str.rtrim(null));
    assertEquals("", Str.ltrim(new StringBuilder("")).toString());
    assertEquals("", Str.ltrim(new StringBuilder(" ")).toString());
    assertEquals("", Str.ltrim(new StringBuilder("     ")).toString());
    assertEquals("abc ", Str.ltrim(new StringBuilder(" abc ")).toString());
    assertEquals("abc def ", Str.ltrim(new StringBuilder(" abc def "))
        .toString());
  }

  public void testLtrimToEmpty()
  {
    assertEquals("", Str.ltrimToEmpty(null));
    assertEquals("", Str.ltrimToEmpty(""));
    assertEquals("", Str.ltrimToEmpty(" "));
    assertEquals("", Str.ltrimToEmpty("     "));
    assertEquals("abc ", Str.ltrimToEmpty(" abc "));
    assertEquals("abc def ", Str.ltrimToEmpty(" abc def "));
  }

  public void testLtrimToNull()
  {
    assertNull(Str.ltrimToNull(null));
    assertNull(Str.ltrimToNull(""));
    assertNull(Str.ltrimToNull(" "));
    assertNull(Str.ltrimToNull("     "));
    assertEquals("abc ", Str.ltrimToNull(" abc "));
    assertEquals("abc def ", Str.ltrimToNull(" abc def "));
  }

  public void testNotEquals()
  {
    assertFalse(Str.notEquals(null, null));
    assertFalse(Str.notEquals("abc", "abc"));
    assertTrue(Str.notEquals("abc", "def"));
    assertTrue(Str.notEquals("abc", "ABC"));
    assertTrue(Str.notEquals("", null));
    assertTrue(Str.notEquals(null, ""));
  }

  public void testNotEqualsIgnoreCase()
  {
    assertFalse(Str.notEqualsIgnoreCase(null, null));
    assertFalse(Str.notEqualsIgnoreCase("abc", "abc"));
    assertTrue(Str.notEqualsIgnoreCase("abc", "def"));
    assertFalse(Str.notEqualsIgnoreCase("abc", "ABC"));
    assertTrue(Str.notEqualsIgnoreCase("", null));
    assertTrue(Str.notEqualsIgnoreCase(null, ""));
  }

  public void testReplace()
  {
    assertEquals("defg", Str.replace("abcd", 0, 4, "defg"));
    assertEquals("defgcd", Str.replace("abcd", 0, 2, "defg"));
    assertEquals("abdefg", Str.replace("abcd", 2, 4, "defg"));
    assertEquals("abdefgd", Str.replace("abcd", 2, 3, "defg"));
    assertEquals("abdefgcd", Str.replace("abcd", 2, 2, "defg"));
  }

  public void testRtrim()
  {
    assertNull(Str.rtrim(null));
    assertEquals("", Str.rtrim(new StringBuilder("")).toString());
    assertEquals("", Str.rtrim(new StringBuilder(" ")).toString());
    assertEquals("", Str.rtrim(new StringBuilder("     ")).toString());
    assertEquals(" abc", Str.rtrim(new StringBuilder(" abc ")).toString());
    assertEquals(" abc def", Str.rtrim(new StringBuilder(" abc def "))
        .toString());
  }

  public void testRtrimToEmpty()
  {
    assertEquals("", Str.rtrimToEmpty(null));
    assertEquals("", Str.rtrimToEmpty(""));
    assertEquals("", Str.rtrimToEmpty(" "));
    assertEquals("", Str.rtrimToEmpty("     "));
    assertEquals(" abc", Str.rtrimToEmpty(" abc "));
    assertEquals(" abc def", Str.rtrimToEmpty(" abc def "));
  }

  public void testRtrimToNull()
  {
    assertNull(Str.rtrimToNull(null));
    assertNull(Str.rtrimToNull(""));
    assertNull(Str.rtrimToNull(" "));
    assertNull(Str.rtrimToNull("     "));
    assertEquals(" abc", Str.rtrimToNull(" abc "));
    assertEquals(" abc def", Str.rtrimToNull(" abc def "));
  }

  public void testSplitLines()
  {
    arrEquals(arr("1", "2"), Str.splitLines("1\n2"));
    arrEquals(arr("1", "2"), Str.splitLines("1\n2\n"));
    arrEquals(arr("1", "2"), Str.splitLines("1\n2\n\n"));
    arrEquals(arr("1", "2", "3"), Str.splitLines("1\r2\r\n3\n"));
    arrEquals(arr("1", "", "3"), Str.splitLines("1\n\n3"));
    arrEquals(arr("1", "", "3"), Str.splitLines("1\n\r\n3"));
    arrEquals(arr("1", "", "", "3"), Str.splitLines("1\n\r\n\r3"));
    arrEquals(arr(" 1 ", "2", "3"), Str.splitLines(" 1 \r2\r\n3\n"));

    arrEquals(arr("1", "2", "3"), Str.splitLines(" 1 \n2\n3", true, false));
    arrEquals(arr("1", "", "3"), Str.splitLines(" 1 \n\n3", true, false));
    arrEquals(arr("1", "", "3"), Str.splitLines(" 1 \n \n3", true, false));

    arrEquals(arr("1", "2", "3"), Str.splitLines(" 1 \n2\n3", true, true));
    arrEquals(arr("1", "3"), Str.splitLines(" 1 \n\n3", true, true));
    arrEquals(arr("1", "3"), Str.splitLines(" 1 \n \n3", true, true));

    arrEquals(arr(" 1 ", "2", "3"), Str.splitLines(" 1 \n2\n3", false, true));
    arrEquals(arr(" 1 ", "3"), Str.splitLines(" 1 \n\n3", false, true));
    arrEquals(arr(" 1 ", "3"), Str.splitLines(" 1 \n \n3", false, true));
  }

  public void testStartsWithIgnoreCase()
  {
    assertTrue(Str.startsWithIgnoreCase("abcdef", "ABC"));
    assertTrue(Str.startsWithIgnoreCase("abcdef", "AbC"));
    assertTrue(Str.startsWithIgnoreCase("abcdef", "abc"));
    assertFalse(Str.startsWithIgnoreCase("abcdef", "bcd"));
    assertTrue(Str.startsWithIgnoreCase("abcdef", "bcd", 1));
    assertTrue(Str.startsWithIgnoreCase("abcdef", "ABCDEF"));
    assertTrue(Str.startsWithIgnoreCase("abcdef", ""));
    assertFalse(Str.startsWithIgnoreCase("abcdef", "abcdefghi"));
  }

  public void testTrim()
  {
    assertNull(Str.trim(null));
    assertEquals("", Str.trim(new StringBuilder("")).toString());
    assertEquals("", Str.trim(new StringBuilder(" ")).toString());
    assertEquals("", Str.trim(new StringBuilder("     ")).toString());
    assertEquals("abc", Str.trim(new StringBuilder(" abc ")).toString());
    assertEquals("abc def", Str.trim(new StringBuilder(" abc def ")).toString());
  }

  public void testTrimToEmpty()
  {
    assertEquals("", Str.trimToEmpty(null));
    assertEquals("", Str.trimToEmpty(""));
    assertEquals("", Str.trimToEmpty(" "));
    assertEquals("", Str.trimToEmpty("     "));
    assertEquals("abc", Str.trimToEmpty(" abc "));
    assertEquals("abc def", Str.trimToEmpty(" abc def "));
  }

  public void testTrimToNull()
  {
    assertNull(Str.trimToNull(null));
    assertNull(Str.trimToNull(""));
    assertNull(Str.trimToNull(" "));
    assertNull(Str.trimToNull("     "));
    assertEquals("abc", Str.trimToNull(" abc "));
    assertEquals("abc def", Str.trimToNull(" abc def "));
  }

  private String[] arr( final String... args )
  {
    return args;
  }

  private void arrEquals( final String[] expected, final String[] actual )
  {
    if ( expected == null )
    {
      assertNull(actual);
      return;
    }
    assertNotNull(actual);
    if ( actual == null )
      return;

    assertEquals(expected.length, actual.length);
    if ( expected.length == actual.length )
      for ( int xi = 0; xi < expected.length; ++xi )
        assertEquals(expected[xi], actual[xi]);
  }
}
