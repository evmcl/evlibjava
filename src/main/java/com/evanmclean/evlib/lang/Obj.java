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

/**
 * Misc functions for various object related operations.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class Obj
{
  /**
   * Compares two objects, handling nulls.
   * 
   * <pre>
   * Obj.equals(null, null) = true
   * Obj.equals(null, "") = false
   * Obj.equals("", "") = true
   * Obj.equals(Boolean.TRUE, "true") = false
   * Obj.equals(Boolean.TRUE, new Boolean(true)) = true
   * </pre>
   * 
   * @param lhs
   * @param rhs
   * @return True if both parameters are <code>null</code>, or
   *         <code>lhs.equals(rhs)</code> returns <code>true</code>.
   */
  public static boolean equals( final Object lhs, final Object rhs )
  {
    if ( lhs == null )
      return rhs == null;
    return lhs.equals(rhs);
  }

  /**
   * Calls <code>lhs.equals()</code> for each of the other objects until one of
   * them returns true.
   * 
   * @param lhs
   *        The object to compare.
   * @param rhs
   *        One or more objects to compare against for equality.
   * @return True if <code>lhs.equals(rhs)</code> returns <code>true</code> for
   *         one of the <code>rhs</code> objects specified. Returns
   *         <code>true</code> if <code>lhs</code> is null and one of the
   *         <code>rhs</code> objects is null.
   */
  public static boolean equalsOneOf( final Object lhs, final Object... rhs )
  {
    if ( lhs == null )
    {
      for ( Object obj : rhs )
        if ( obj == null )
          return true;
      return false;
    }
    for ( Object obj : rhs )
      if ( lhs.equals(obj) )
        return true;
    return false;
  }

  /**
   * If the first argument is <code>null</code>, returns the second argument.
   * 
   * @param <T>
   *        Return type. Will be common ancestor type of the two parameters.
   * @param <O>
   *        Type of the first argument.
   * @param <A>
   *        Type of the second argument.
   * @param obj
   *        The object to check.
   * @param alt
   *        The object to return if <code>obj</code> is <code>null</code>.
   * @return The returned argument.
   */
  public static <T, O extends T, A extends T> T ifNull( final O obj, final A alt )
  {
    // return (obj != null) ? obj : alt;
    if ( obj != null )
      return obj;
    return alt;
  }

  /**
   * Compares two objects, handling nulls.
   * 
   * <pre>
   * Obj.equals(null, null) = false
   * Obj.equals(null, "") = true
   * Obj.equals("", "") = false
   * Obj.equals(Boolean.TRUE, "true") = true
   * Obj.equals(Boolean.TRUE, new Boolean(true)) = false
   * </pre>
   * 
   * @param lhs
   * @param rhs
   * @return True if both parameters are <code>null</code>, or
   *         <code>lhs.equals(rhs)</code> returns <code>true</code>.
   */
  public static boolean notEquals( final Object lhs, final Object rhs )
  {
    if ( lhs == null )
      return rhs != null;
    return !lhs.equals(rhs);
  }

  private Obj()
  {
    // empty
  }
}
