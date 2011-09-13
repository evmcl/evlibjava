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
package com.evanmclean.evlib.lang;

import junit.framework.TestCase;

/**
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class ObjTest extends TestCase
{
  private static class ChildA extends Parent
  {
    ChildA( final String val )
    {
      super(val);
    }
  }

  private static class ChildB extends Parent
  {
    ChildB( final String val )
    {
      super(val);
    }
  }

  private static class Parent
  {
    private final String val;

    Parent( final String val )
    {
      this.val = val;
    }

    @Override
    public boolean equals( final Object obj )
    {
      if ( this == obj )
        return true;
      if ( obj == null )
        return false;
      if ( getClass() != obj.getClass() )
        return false;
      Parent other = (Parent) obj;
      if ( val == null )
      {
        if ( other.val != null )
          return false;
      }
      else if ( !val.equals(other.val) )
        return false;
      return true;
    }

    @Override
    public int hashCode()
    {
      return val.hashCode();
    }
  }

  public static final String TEST_ALL_TEST_TYPE = "UNIT";

  @edu.umd.cs.findbugs.annotations.SuppressWarnings( "DM_BOOLEAN_CTOR" )
  public void testEquals()
  {
    assertTrue(Obj.equals(null, null));
    assertTrue(Obj.equals("abc", "abc"));
    assertFalse(Obj.equals("abc", "def"));
    assertFalse(Obj.equals("", null));
    assertFalse(Obj.equals(null, ""));
    assertFalse(Obj.equals(null, Boolean.FALSE));
    assertFalse(Obj.equals("false", Boolean.FALSE));
    assertTrue(Obj.equals(new Boolean(true), Boolean.TRUE));
  }

  public void testEqualsOneOf()
  {
    assertFalse(Obj.equalsOneOf(null, "abc", "def", "ghi"));
    assertTrue(Obj.equalsOneOf(null, "abc", null, "ghi"));
    assertTrue(Obj.equalsOneOf("def", "abc", "def", "ghi"));
    assertFalse(Obj.equalsOneOf("123", "abc", "def", "ghi"));
  }

  @edu.umd.cs.findbugs.annotations.SuppressWarnings( "DLS_DEAD_LOCAL_STORE" )
  public void testIfNull()
  {
    final ChildA a0 = null;
    final ChildA a1 = new ChildA("1");
    final ChildA a2 = new ChildA("2");
    final ChildB b1 = new ChildB("1");
    final ChildB b2 = new ChildB("2");

    assertEquals(a1, Obj.ifNull(a1, a2));
    assertEquals(a1, Obj.ifNull(a1, b2));
    assertEquals(a1, Obj.ifNull(a1, b1));
    assertEquals(a2, Obj.ifNull(a0, a2));
    assertEquals(b2, Obj.ifNull(a0, b2));

    // Just to prove that these compile.
    @SuppressWarnings( "unused" ) Parent x1 = Obj.ifNull(a1, b1);
    @SuppressWarnings( "unused" ) ChildA x2 = Obj.ifNull(a1, a2);
  }

  @edu.umd.cs.findbugs.annotations.SuppressWarnings( "DM_BOOLEAN_CTOR" )
  public void testNotEquals()
  {
    assertFalse(Obj.notEquals(null, null));
    assertFalse(Obj.notEquals("abc", "abc"));
    assertTrue(Obj.notEquals("abc", "def"));
    assertTrue(Obj.notEquals("", null));
    assertTrue(Obj.notEquals(null, ""));
    assertTrue(Obj.notEquals(null, Boolean.FALSE));
    assertTrue(Obj.notEquals("false", Boolean.FALSE));
    assertFalse(Obj.notEquals(new Boolean(true), Boolean.TRUE));
  }
}
