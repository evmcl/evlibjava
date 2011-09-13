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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.regex.Pattern;

import com.evanmclean.evlib.util.ArrayListIterator;

/**
 * <p>
 * Misc string operations.
 * </p>
 * 
 * <a name="whitespace"></a>
 * <p>
 * For the purposes of this class, a whitespace character is any character equal
 * or below ' ' (ASCII 32, which is the space character, and all below are
 * control characters) or any character for which
 * {@link Character#isWhitespace(char)} returns <code>true</code>. This does not
 * include non-breaking space characters in the Unicode set.
 * </p>
 * 
 * <p>
 * The standard {@link java.lang.String#trim()} method only strips characters
 * equal or below ' ' (ASCII 32), so I think these methods are more complete.
 * </p>
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class Str
{
  /**
   * Can be passed into a <code>join</code> call perform specialised object to
   * string conversions.
   * 
   * @param <T>
   */
  public static interface ToString<T>
  {
    String toString( T obj );
  }

  private static final Pattern lineSplitter = Pattern.compile("\r?\n|\r");

  /**
   * A final, unmodifiable, zero length string, in case you need one. Saves you
   * allocating others.
   */
  public static final String EMPTY = "";

  /**
   * Tests if the string ends with the specified suffix, ignoring case. This is
   * really just a thin wrapper around
   * {@link java.lang.String#regionMatches(boolean, int, String, int, int)} that
   * calculates the right offsets.
   * 
   * @param str
   *        The string to check.
   * @param suffix
   *        The suffix.
   * @return Returns <code>true</code> if <code>str</code> ends with
   *         <code>suffix</code>, ignoring case. Note that the result will be
   *         <code>true</code> if <code>suffix</code> is an empty string or is
   *         equal to <code>str</code> as determined by the
   *         <code>equalsIgnoreCase(Object)</code> method.
   */
  public static boolean endsWithIgnoreCase( final String str,
      final String suffix )
  {
    if ( (str == null) || (suffix == null) )
      return false;
    final int len = suffix.length();
    final int start = str.length() - len;
    if ( start < 0 )
      return false;
    return str.regionMatches(true, start, suffix, 0, len);
  }

  /**
   * Compares two strings, handling nulls.
   * 
   * <pre>
   * Str.equals(null, null) = true
   * Str.equals(null, "") = false
   * Str.equals("", "") = true
   * Str.equals("", " ") = false
   * Str.equals("abc", "abc") = true
   * Str.equals("abc", "ABC") = false
   * Str.equals("abc", "def") = false
   * </pre>
   * 
   * @param lhs
   * @param rhs
   * @return True if both parameters are <code>null</code>, or
   *         <code>lhs.equals(rhs)</code> returns <code>true</code>.
   */
  public static boolean equals( final String lhs, final String rhs )
  {
    if ( lhs == null )
      return rhs == null;
    return lhs.equals(rhs);
  }

  /**
   * Compares two strings in a case insensitive manner, handling nulls.
   * 
   * <pre>
   * Str.equals(null, null) = true
   * Str.equals(null, "") = false
   * Str.equals("", "") = true
   * Str.equals("", " ") = false
   * Str.equals("abc", "abc") = true
   * Str.equals("abc", "ABC") = true
   * Str.equals("abc", "def") = false
   * </pre>
   * 
   * @param lhs
   * @param rhs
   * @return True if both parameters are <code>null</code>, or
   *         <code>lhs.equalsIgnoreCase(rhs)</code> returns <code>true</code>.
   */
  public static boolean equalsIgnoreCase( final String lhs, final String rhs )
  {
    if ( lhs == null )
      return rhs == null;
    return lhs.equalsIgnoreCase(rhs);
  }

  /**
   * Calls <code>lhs.equals()</code> for each of the other strings until one of
   * them returns true.
   * 
   * @param lhs
   *        The string to compare.
   * @param rhs
   *        One or more string to compare against for equality.
   * @return True if <code>lhs.equals(rhs)</code> returns <code>true</code> for
   *         one of the <code>rhs</code> objects specified. Returns
   *         <code>true</code> if <code>lhs</code> is </code>null</code> and one
   *         of the <code>rhs</code> objects is also <code>null</code>.
   */
  public static boolean equalsOneOf( final String lhs, final String... rhs )
  {
    if ( lhs == null )
    {
      for ( String obj : rhs )
        if ( obj == null )
          return true;
      return false;
    }
    for ( String obj : rhs )
      if ( lhs.equals(obj) )
        return true;
    return false;
  }

  /**
   * Calls <code>lhs.equalsIgnoreCase()</code> for each of the other strings
   * until one of them returns true.
   * 
   * @param lhs
   *        The string to compare.
   * @param rhs
   *        One or more string to compare against for equality.
   * @return True if <code>lhs.equalsIgnoreCase(rhs)</code> returns
   *         <code>true</code> for one of the <code>rhs</code> objects
   *         specified. Returns <code>true</code> if <code>lhs</code> is
   *         </code>null</code> and one of the <code>rhs</code> objects is also
   *         <code>null</code>.
   */
  public static boolean equalsOneOfIgnoreCase( final String lhs,
      final String... rhs )
  {
    if ( lhs == null )
    {
      for ( String obj : rhs )
        if ( obj == null )
          return true;
      return false;
    }
    for ( String obj : rhs )
      if ( lhs.equalsIgnoreCase(obj) )
        return true;
    return false;
  }

  /**
   * <p>
   * Takes what would normally be output from
   * {@link Throwable#printStackTrace()} and returns it as a string.
   * </p>
   * 
   * @param ex
   *        The exception to process.
   * @return The output as a string.
   */
  public static String getStackTrace( final Throwable ex )
  {
    if ( ex == null )
      return null;
    final StringWriter out = new StringWriter();
    final PrintWriter pout = new PrintWriter(out);
    ex.printStackTrace(pout);
    pout.flush();
    return out.toString();
  }

  /**
   * <p>
   * Return the first parameter if it is not <code>null</code>, zero length, and
   * contains at least one non-<a href="#whitespace">whitespace</a> character,
   * otherwise return the second parameter.
   * </p>
   * 
   * <p>
   * See <a href="#whitespace">note</a> above for our definition of <a
   * href="#whitespace">whitespace</a>.
   * </p>
   * 
   * @param <T>
   *        Anything that implements {@link CharSequence} (such as
   *        {@link String}s).
   * @param str
   * @param alt
   * @return Return <code>str</code> parameter if it is not <code>null</code>,
   *         zero length, and contains at least one non-<a
   *         href="#whitespace">whitespace</a> character, otherwise return
   *         <code>alt</code>.
   */
  public static <T extends CharSequence> T ifBlank( final T str, final T alt )
  {
    return isNotBlank(str) ? str : alt;
  }

  /**
   * Return the first parameter if it is not <code>null</code> or a zero length
   * string, otherwise return the second parameter.
   * 
   * @param <T>
   *        Anything that implements {@link CharSequence} (such as
   *        {@link String}s).
   * @param str
   * @param alt
   * @return Return <code>str</code> if it is not <code>null</code> or a zero
   *         length string, otherwise return <code>alt</code>.
   */
  public static <T extends CharSequence> T ifEmpty( final T str, final T alt )
  {
    return ((str != null) && (str.length() > 0)) ? str : alt;
  }

  /**
   * Return the parameter if it is not <code>null</code>, otherwise return an
   * empty string.
   * 
   * @param str
   * @return Return <code>str</code> if it is not <code>null</code>, otherwise
   *         return an empty string.
   */
  public static String ifNull( final String str )
  {
    return (str != null) ? str : EMPTY;
  }

  /**
   * Return the first parameter if it is not <code>null</code>, otherwise return
   * the second parameter.
   * 
   * @param <T>
   *        Anything that implements {@link CharSequence} (such as
   *        {@link String}s).
   * @param str
   * @param alt
   * @return Return <code>str</code> if it is not <code>null</code>, otherwise
   *         return <code>alt</code>.
   */
  public static <T extends CharSequence> T ifNull( final T str, final T alt )
  {
    return (str != null) ? str : alt;
  }

  /**
   * <p>
   * Returns <code>true</code> if the parameter is <code>null</code>, zero
   * length, or only contains <a href="#whitespace">whitespace</a>.
   * </p>
   * 
   * <p>
   * See <a href="#whitespace">note</a> above for our definition of <a
   * href="#whitespace">whitespace</a>.
   * </p>
   * 
   * @param str
   * @return Returns <code>true</code> if the parameter is <code>null</code>,
   *         zero length, or only contains <a href="#whitespace">whitespace</a>.
   */
  public static boolean isBlank( final CharSequence str )
  {
    if ( str == null )
      return true;

    final int len = str.length();
    if ( len <= 0 )
      return true;

    for ( int xi = 0; xi < len; ++xi )
      if ( !isWS(str.charAt(xi)) )
        return false;

    return true;
  }

  /**
   * Returns <code>true</code> if the parameter is <code>null</code> or a zero
   * length string.
   * 
   * @param str
   * @return Returns <code>true</code> if the parameter is <code>null</code> or
   *         a zero length string.
   */
  public static boolean isEmpty( final CharSequence str )
  {
    return (str == null) || (str.length() <= 0);
  }

  /**
   * <p>
   * Returns <code>true</code> if the parameter is not <code>null</code>, not
   * zero length, and contains at least one non-<a
   * href="#whitespace">whitespace</a> character.
   * </p>
   * 
   * <p>
   * See <a href="#whitespace">note</a> above for our definition of <a
   * href="#whitespace">whitespace</a>.
   * </p>
   * 
   * @param str
   * @return Returns <code>true</code> if the parameter is not <code>null</code>
   *         , not zero length, and contains at least one non-<a
   *         href="#whitespace">whitespace</a> character.
   */
  public static boolean isNotBlank( final CharSequence str )
  {
    if ( str == null )
      return false;

    final int len = str.length();
    if ( len <= 0 )
      return false;

    for ( int xi = 0; xi < len; ++xi )
      if ( !isWS(str.charAt(xi)) )
        return true;

    return false;
  }

  /**
   * Returns <code>true</code> if the parameter is not <code>null</code> and not
   * a zero length string.
   * 
   * @param str
   * @return Returns <code>true</code> if the parameter is not <code>null</code>
   *         and not a zero length string.
   */
  public static boolean isNotEmpty( final CharSequence str )
  {
    return (str != null) && (str.length() > 0);
  }

  /**
   * <p>
   * Joins the elements in the provided iterable into a string. Null objects and
   * empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link Object#toString()} is called on each element.
   * </p>
   * 
   * @param <T>
   *        <code>&lt;T&gt;.toString()</code> called.
   * @param sep
   *        What to insert between each element.
   * @param it
   *        The elements to join together.
   * @return The string of joined elements.
   */
  public static <T> String join( final char sep, final Iterable<T> it )
  {
    return join(new StringBuilder(), sep, it.iterator()).toString();
  }

  /**
   * <p>
   * Joins the elements in the provided iterator into a string. Null objects and
   * empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link Object#toString()} is called on each element.
   * </p>
   * 
   * @param <T>
   *        <code>&lt;T&gt;.toString()</code> called.
   * @param sep
   *        What to insert between each element.
   * @param it
   *        The elements to join together.
   * @return The string of joined elements.
   */
  public static <T> String join( final char sep, final Iterator<T> it )
  {
    return join(new StringBuilder(), sep, it).toString();
  }

  /**
   * <p>
   * Joins the elements in the provided array into a string. Null objects and
   * empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link Object#toString()} is called on each element.
   * </p>
   * 
   * @param <T>
   *        <code>&lt;T&gt;.toString()</code> called.
   * @param sep
   *        What to insert between each element.
   * @param arr
   *        The array of elements to join together.
   * @return The string of joined elements.
   */
  public static <T> String join( final char sep, final T... arr )
  {
    return join(new StringBuilder(), sep, new ArrayListIterator<T>(arr))
        .toString();
  }

  /**
   * <p>
   * Joins the elements in the provided array into a string. Null objects and
   * empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link Object#toString()} is called on each element.
   * </p>
   * 
   * @param <T>
   *        <code>&lt;T&gt;.toString()</code> called.
   * @param sep
   *        What to insert between each element.
   * @param arr
   *        The array of elements to join together.
   * @param start_index
   *        The index of the first element to start joining from, to the end of
   *        the array.
   * @return The string of joined elements.
   */
  public static <T> String join( final char sep, final T[] arr,
      final int start_index )
  {
    return join(new StringBuilder(), sep,
      new ArrayListIterator<T>(arr, start_index, Arr.length(arr))).toString();
  }

  /**
   * <p>
   * Joins the elements in the provided array into a string. Null objects and
   * empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link Object#toString()} is called on each element.
   * </p>
   * 
   * @param <T>
   *        <code>&lt;T&gt;.toString()</code> called.
   * @param sep
   *        What to insert between each element.
   * @param arr
   *        The array of elements to join together.
   * @param start_index
   *        The index of the first element to start joining from.
   * @param end_index
   *        The index (exclusive) to stop joining at.
   * @return The string of joined elements.
   */
  public static <T> String join( final char sep, final T[] arr,
      final int start_index, final int end_index )
  {
    return join(new StringBuilder(), sep,
      new ArrayListIterator<T>(arr, start_index, end_index)).toString();
  }

  /**
   * <p>
   * Joins the elements in the provided iterable into a string. Null objects and
   * empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link ToString#toString(Object)} is called on each element.
   * </p>
   * 
   * @param <T>
   * @param sep
   *        What to insert between each element.
   * @param tos
   *        Convert each object to the desired string format.
   * @param it
   *        The elements to join together.
   * @return The string of joined elements.
   */
  public static <T> String join( final char sep, final ToString<T> tos,
      final Iterable<T> it )
  {
    return join(new StringBuilder(), sep, tos, it.iterator()).toString();
  }

  /**
   * <p>
   * Joins the elements in the provided iterator into a string. Null objects and
   * empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link ToString#toString(Object)} is called on each element.
   * </p>
   * 
   * @param <T>
   * @param sep
   *        What to insert between each element.
   * @param tos
   *        Convert each object to the desired string format.
   * @param it
   *        The elements to join together.
   * @return The string of joined elements.
   */
  public static <T> String join( final char sep, final ToString<T> tos,
      final Iterator<T> it )
  {
    return join(new StringBuilder(), sep, tos, it).toString();
  }

  /**
   * <p>
   * Joins the elements in the provided array into a string. Null objects and
   * empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link ToString#toString(Object)} is called on each element.
   * </p>
   * 
   * @param <T>
   * @param sep
   *        What to insert between each element.
   * @param tos
   *        Convert each object to the desired string format.
   * @param arr
   *        The array of elements to join together.
   * @return The string of joined elements.
   */
  public static <T> String join( final char sep, final ToString<T> tos,
      final T... arr )
  {
    return join(new StringBuilder(), sep, tos, new ArrayListIterator<T>(arr))
        .toString();
  }

  /**
   * <p>
   * Joins the elements in the provided array into a string. Null objects and
   * empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link ToString#toString(Object)} is called on each element.
   * </p>
   * 
   * @param <T>
   * @param sep
   *        What to insert between each element.
   * @param tos
   *        Convert each object to the desired string format.
   * @param arr
   *        The array of elements to join together.
   * @param start_index
   *        The index of the first element to start joining from, to the end of
   *        the array.
   * @return The string of joined elements.
   */
  public static <T> String join( final char sep, final ToString<T> tos,
      final T[] arr, final int start_index )
  {
    return join(new StringBuilder(), sep, tos,
      new ArrayListIterator<T>(arr, start_index, Arr.length(arr))).toString();
  }

  /**
   * <p>
   * Joins the elements in the provided array into a string. Null objects and
   * empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link ToString#toString(Object)} is called on each element.
   * </p>
   * 
   * @param <T>
   * @param sep
   *        What to insert between each element.
   * @param tos
   *        Convert each object to the desired string format.
   * @param arr
   *        The array of elements to join together.
   * @param start_index
   *        The index of the first element to start joining from.
   * @param end_index
   *        The index (exclusive) to stop joining at.
   * @return The string of joined elements.
   */
  public static <T> String join( final char sep, final ToString<T> tos,
      final T[] arr, final int start_index, final int end_index )
  {
    return join(new StringBuilder(), sep, tos,
      new ArrayListIterator<T>(arr, start_index, end_index)).toString();
  }

  /**
   * <p>
   * Joins the elements in the provided iterable into a string. Null objects and
   * empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link Object#toString()} is called on each element.
   * </p>
   * 
   * @param <T>
   *        <code>&lt;T&gt;.toString()</code> called.
   * @param sep
   *        What to insert between each element.
   * @param it
   *        The elements to join together.
   * @return The string of joined elements.
   */
  public static <T> String join( final String sep, final Iterable<T> it )
  {
    return join(new StringBuilder(), sep, it.iterator()).toString();
  }

  /**
   * <p>
   * Joins the elements in the provided iterator into a string. Null objects and
   * empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link Object#toString()} is called on each element.
   * </p>
   * 
   * @param <T>
   *        <code>&lt;T&gt;.toString()</code> called.
   * @param sep
   *        What to insert between each element.
   * @param it
   *        The elements to join together.
   * @return The string of joined elements.
   */
  public static <T> String join( final String sep, final Iterator<T> it )
  {
    return join(new StringBuilder(), sep, it).toString();
  }

  /**
   * <p>
   * Joins the elements in the provided array into a string. Null objects and
   * empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link Object#toString()} is called on each element.
   * </p>
   * 
   * @param <T>
   *        <code>&lt;T&gt;.toString()</code> called.
   * @param sep
   *        What to insert between each element.
   * @param arr
   *        The array of elements to join together.
   * @return The string of joined elements.
   */
  public static <T> String join( final String sep, final T... arr )
  {
    return join(new StringBuilder(), sep, new ArrayListIterator<T>(arr))
        .toString();
  }

  /**
   * <p>
   * Joins the elements in the provided array into a string. Null objects and
   * empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link Object#toString()} is called on each element.
   * </p>
   * 
   * @param <T>
   *        <code>&lt;T&gt;.toString()</code> called.
   * @param sep
   *        What to insert between each element.
   * @param arr
   *        The array of elements to join together.
   * @param start_index
   *        The index of the first element to start joining from, to the end of
   *        the array.
   * @return The string of joined elements.
   */
  public static <T> String join( final String sep, final T[] arr,
      final int start_index )
  {
    return join(new StringBuilder(), sep,
      new ArrayListIterator<T>(arr, start_index, Arr.length(arr))).toString();
  }

  /**
   * <p>
   * Joins the elements in the provided array into a string. Null objects and
   * empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link Object#toString()} is called on each element.
   * </p>
   * 
   * @param <T>
   *        <code>&lt;T&gt;.toString()</code> called.
   * @param sep
   *        What to insert between each element.
   * @param arr
   *        The array of elements to join together.
   * @param start_index
   *        The index of the first element to start joining from.
   * @param end_index
   *        The index (exclusive) to stop joining at.
   * @return The string of joined elements.
   */
  public static <T> String join( final String sep, final T[] arr,
      final int start_index, final int end_index )
  {
    return join(new StringBuilder(), sep,
      new ArrayListIterator<T>(arr, start_index, end_index)).toString();
  }

  /**
   * <p>
   * Joins the elements in the provided iterable into a string. Null objects and
   * empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link ToString#toString(Object)} is called on each element.
   * </p>
   * 
   * @param <T>
   * @param sep
   *        What to insert between each element.
   * @param tos
   *        Convert each object to the desired string format.
   * @param it
   *        The elements to join together.
   * @return The string of joined elements.
   */
  public static <T> String join( final String sep, final ToString<T> tos,
      final Iterable<T> it )
  {
    return join(new StringBuilder(), sep, tos, it.iterator()).toString();
  }

  /**
   * <p>
   * Joins the elements in the provided iterator into a string. Null objects and
   * empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link ToString#toString(Object)} is called on each element.
   * </p>
   * 
   * @param <T>
   * @param sep
   *        What to insert between each element.
   * @param tos
   *        Convert each object to the desired string format.
   * @param it
   *        The elements to join together.
   * @return The string of joined elements.
   */
  public static <T> String join( final String sep, final ToString<T> tos,
      final Iterator<T> it )
  {
    return join(new StringBuilder(), sep, tos, it).toString();
  }

  /**
   * <p>
   * Joins the elements in the provided array into a string. Null objects and
   * empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link ToString#toString(Object)} is called on each element.
   * </p>
   * 
   * @param <T>
   * @param sep
   *        What to insert between each element.
   * @param tos
   *        Convert each object to the desired string format.
   * @param arr
   *        The array of elements to join together.
   * @return The string of joined elements.
   */
  public static <T> String join( final String sep, final ToString<T> tos,
      final T... arr )
  {
    return join(new StringBuilder(), sep, tos, new ArrayListIterator<T>(arr))
        .toString();
  }

  /**
   * <p>
   * Joins the elements in the provided array into a string. Null objects and
   * empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link ToString#toString(Object)} is called on each element.
   * </p>
   * 
   * @param <T>
   * @param sep
   *        What to insert between each element.
   * @param tos
   *        Convert each object to the desired string format.
   * @param arr
   *        The array of elements to join together.
   * @param start_index
   *        The index of the first element to start joining from, to the end of
   *        the array.
   * @return The string of joined elements.
   */
  public static <T> String join( final String sep, final ToString<T> tos,
      final T[] arr, final int start_index )
  {
    return join(new StringBuilder(), sep, tos,
      new ArrayListIterator<T>(arr, start_index, Arr.length(arr))).toString();
  }

  /**
   * <p>
   * Joins the elements in the provided array into a string. Null objects and
   * empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link ToString#toString(Object)} is called on each element.
   * </p>
   * 
   * @param <T>
   * @param sep
   *        What to insert between each element.
   * @param tos
   *        Convert each object to the desired string format.
   * @param arr
   *        The array of elements to join together.
   * @param start_index
   *        The index of the first element to start joining from.
   * @param end_index
   *        The index (exclusive) to stop joining at.
   * @return The string of joined elements.
   */
  public static <T> String join( final String sep, final ToString<T> tos,
      final T[] arr, final int start_index, final int end_index )
  {
    return join(new StringBuilder(), sep, tos,
      new ArrayListIterator<T>(arr, start_index, end_index)).toString();
  }

  /**
   * <p>
   * Appends the elements in the provided iterable onto a string builder. Null
   * objects and empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link Object#toString()} is called on each element.
   * </p>
   * 
   * @param <T>
   *        <code>&lt;T&gt;.toString()</code> called.
   * @param buff
   *        The string builder to append to.
   * @param sep
   *        What to insert between each element.
   * @param it
   *        The elements to join together.
   * @return The <code>buff</code> parameter.
   */
  public static <T> StringBuilder join( final StringBuilder buff,
      final char sep, final Iterable<T> it )
  {
    return join(buff, sep, it.iterator());
  }

  /**
   * <p>
   * Appends the elements in the provided iterator onto a string builder. Null
   * objects and empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link Object#toString()} is called on each element.
   * </p>
   * 
   * @param <T>
   *        <code>&lt;T&gt;.toString()</code> called.
   * @param buff
   *        The string builder to append to.
   * @param sep
   *        What to insert between each element.
   * @param it
   *        The elements to join together.
   * @return The <code>buff</code> parameter.
   */
  public static <T> StringBuilder join( final StringBuilder buff,
      final char sep, final Iterator<T> it )
  {
    boolean first = true;
    while ( it.hasNext() )
    {
      final T obj = it.next();
      if ( obj != null )
      {
        final String str = obj.toString();
        if ( str.length() > 0 )
        {
          if ( first )
            first = false;
          else
            buff.append(sep);
          buff.append(str);
        }
      }
    }
    return buff;
  }

  /**
   * <p>
   * Appends the elements in the provided array onto a string builder. Null
   * objects and empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link Object#toString()} is called on each element.
   * </p>
   * 
   * @param <T>
   *        <code>&lt;T&gt;.toString()</code> called.
   * @param buff
   *        The string builder to append to.
   * @param sep
   *        What to insert between each element.
   * @param arr
   *        The array of elements to join together.
   * @return The <code>buff</code> parameter.
   */
  public static <T> StringBuilder join( final StringBuilder buff,
      final char sep, final T... arr )
  {
    return join(buff, sep, new ArrayListIterator<T>(arr));
  }

  /**
   * <p>
   * Appends the elements in the provided array onto a string builder. Null
   * objects and empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link Object#toString()} is called on each element.
   * </p>
   * 
   * @param <T>
   *        <code>&lt;T&gt;.toString()</code> called.
   * @param buff
   *        The string builder to append to.
   * @param sep
   *        What to insert between each element.
   * @param arr
   *        The array of elements to join together.
   * @param start_index
   *        The index of the first element to start joining from, to the end of
   *        the array.
   * @return The <code>buff</code> parameter.
   */
  public static <T> StringBuilder join( final StringBuilder buff,
      final char sep, final T[] arr, final int start_index )
  {
    return join(buff, sep,
      new ArrayListIterator<T>(arr, start_index, Arr.length(arr)));
  }

  /**
   * <p>
   * Appends the elements in the provided array onto a string builder. Null
   * objects and empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link Object#toString()} is called on each element.
   * </p>
   * 
   * @param <T>
   *        <code>&lt;T&gt;.toString()</code> called.
   * @param buff
   *        The string builder to append to.
   * @param sep
   *        What to insert between each element.
   * @param arr
   *        The array of elements to join together.
   * @param start_index
   *        The index of the first element to start joining from.
   * @param end_index
   *        The index (exclusive) to stop joining at.
   * @return The <code>buff</code> parameter.
   */
  public static <T> StringBuilder join( final StringBuilder buff,
      final char sep, final T[] arr, final int start_index, final int end_index )
  {
    return join(buff, sep,
      new ArrayListIterator<T>(arr, start_index, end_index));
  }

  /**
   * <p>
   * Appends the elements in the provided iterable onto a string builder. Null
   * objects and empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link ToString#toString(Object)} is called on each element.
   * </p>
   * 
   * @param <T>
   * @param buff
   *        The string builder to append to.
   * @param sep
   *        What to insert between each element.
   * @param tos
   *        Convert each object to the desired string format.
   * @param it
   *        The elements to join together.
   * @return The <code>buff</code> parameter.
   */
  public static <T> StringBuilder join( final StringBuilder buff,
      final char sep, final ToString<T> tos, final Iterable<T> it )
  {
    return join(buff, sep, tos, it.iterator());
  }

  /**
   * <p>
   * Appends the elements in the provided iterator onto a string builder. Null
   * objects and empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link ToString#toString(Object)} is called on each element.
   * </p>
   * 
   * @param <T>
   * @param buff
   *        The string builder to append to.
   * @param sep
   *        What to insert between each element.
   * @param tos
   *        Convert each object to the desired string format.
   * @param it
   *        The elements to join together.
   * @return The <code>buff</code> parameter.
   */
  public static <T> StringBuilder join( final StringBuilder buff,
      final char sep, final ToString<T> tos, final Iterator<T> it )
  {
    boolean first = true;
    while ( it.hasNext() )
    {
      final T obj = it.next();
      if ( obj != null )
      {
        final String str = tos.toString(obj);
        if ( str.length() > 0 )
        {
          if ( first )
            first = false;
          else
            buff.append(sep);
          buff.append(str);
        }
      }
    }
    return buff;
  }

  /**
   * <p>
   * Appends the elements in the provided array onto a string builder. Null
   * objects and empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link ToString#toString(Object)} is called on each element.
   * </p>
   * 
   * @param <T>
   * @param buff
   *        The string builder to append to.
   * @param sep
   *        What to insert between each element.
   * @param tos
   *        Convert each object to the desired string format.
   * @param arr
   *        The array of elements to join together.
   * @return The <code>buff</code> parameter.
   */
  public static <T> StringBuilder join( final StringBuilder buff,
      final char sep, final ToString<T> tos, final T... arr )
  {
    return join(buff, sep, tos, new ArrayListIterator<T>(arr));
  }

  /**
   * <p>
   * Appends the elements in the provided array onto a string builder. Null
   * objects and empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link ToString#toString(Object)} is called on each element.
   * </p>
   * 
   * @param <T>
   * @param buff
   *        The string builder to append to.
   * @param sep
   *        What to insert between each element.
   * @param tos
   *        Convert each object to the desired string format.
   * @param arr
   *        The array of elements to join together.
   * @param start_index
   *        The index of the first element to start joining from, to the end of
   *        the array.
   * @return The <code>buff</code> parameter.
   */
  public static <T> StringBuilder join( final StringBuilder buff,
      final char sep, final ToString<T> tos, final T[] arr,
      final int start_index )
  {
    return join(buff, sep, tos,
      new ArrayListIterator<T>(arr, start_index, Arr.length(arr)));
  }

  /**
   * <p>
   * Appends the elements in the provided array onto a string builder. Null
   * objects and empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link ToString#toString(Object)} is called on each element.
   * </p>
   * 
   * @param <T>
   * @param buff
   *        The string builder to append to.
   * @param sep
   *        What to insert between each element.
   * @param tos
   *        Convert each object to the desired string format.
   * @param arr
   *        The array of elements to join together.
   * @param start_index
   *        The index of the first element to start joining from.
   * @param end_index
   *        The index (exclusive) to stop joining at.
   * @return The <code>buff</code> parameter.
   */
  public static <T> StringBuilder join( final StringBuilder buff,
      final char sep, final ToString<T> tos, final T[] arr,
      final int start_index, final int end_index )
  {
    return join(buff, sep, tos, new ArrayListIterator<T>(arr, start_index,
        end_index));
  }

  /**
   * <p>
   * Appends the elements in the provided iterable onto a string builder. Null
   * objects and empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link Object#toString()} is called on each element.
   * </p>
   * 
   * @param <T>
   *        <code>&lt;T&gt;.toString()</code> called.
   * @param buff
   *        The string builder to append to.
   * @param sep
   *        What to insert between each element.
   * @param it
   *        The elements to join together.
   * @return The <code>buff</code> parameter.
   */
  public static <T> StringBuilder join( final StringBuilder buff,
      final String sep, final Iterable<T> it )
  {
    return join(buff, sep, it.iterator());
  }

  /**
   * <p>
   * Appends the elements in the provided iterator onto a string builder. Null
   * objects and empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link Object#toString()} is called on each element.
   * </p>
   * 
   * @param <T>
   *        <code>&lt;T&gt;.toString()</code> called.
   * @param buff
   *        The string builder to append to.
   * @param sep
   *        What to insert between each element.
   * @param it
   *        The elements to join together.
   * @return The <code>buff</code> parameter.
   */
  public static <T> StringBuilder join( final StringBuilder buff,
      final String sep, final Iterator<T> it )
  {
    boolean first = true;
    while ( it.hasNext() )
    {
      final T obj = it.next();
      if ( obj != null )
      {
        final String str = obj.toString();
        if ( str.length() > 0 )
        {
          if ( first )
            first = false;
          else
            buff.append(sep);
          buff.append(str);
        }
      }
    }
    return buff;
  }

  /**
   * <p>
   * Appends the elements in the provided array onto a string builder. Null
   * objects and empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link Object#toString()} is called on each element.
   * </p>
   * 
   * @param <T>
   *        <code>&lt;T&gt;.toString()</code> called.
   * @param buff
   *        The string builder to append to.
   * @param sep
   *        What to insert between each element.
   * @param arr
   *        The array of elements to join together.
   * @return The <code>buff</code> parameter.
   */
  public static <T> StringBuilder join( final StringBuilder buff,
      final String sep, final T... arr )
  {
    return join(buff, sep, new ArrayListIterator<T>(arr));
  }

  /**
   * <p>
   * Appends the elements in the provided array onto a string builder. Null
   * objects and empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link Object#toString()} is called on each element.
   * </p>
   * 
   * @param <T>
   *        <code>&lt;T&gt;.toString()</code> called.
   * @param buff
   *        The string builder to append to.
   * @param sep
   *        What to insert between each element.
   * @param arr
   *        The array of elements to join together.
   * @param start_index
   *        The index of the first element to start joining from, to the end of
   *        the array.
   * @return The <code>buff</code> parameter.
   */
  public static <T> StringBuilder join( final StringBuilder buff,
      final String sep, final T[] arr, final int start_index )
  {
    return join(buff, sep,
      new ArrayListIterator<T>(arr, start_index, Arr.length(arr)));
  }

  /**
   * <p>
   * Appends the elements in the provided array onto a string builder. Null
   * objects and empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link Object#toString()} is called on each element.
   * </p>
   * 
   * @param <T>
   *        <code>&lt;T&gt;.toString()</code> called.
   * @param buff
   *        The string builder to append to.
   * @param sep
   *        What to insert between each element.
   * @param arr
   *        The array of elements to join together.
   * @param start_index
   *        The index of the first element to start joining from.
   * @param end_index
   *        The index (exclusive) to stop joining at.
   * @return The <code>buff</code> parameter.
   */
  public static <T> StringBuilder join( final StringBuilder buff,
      final String sep, final T[] arr, final int start_index,
      final int end_index )
  {
    return join(buff, sep,
      new ArrayListIterator<T>(arr, start_index, end_index));
  }

  /**
   * <p>
   * Appends the elements in the provided iterable onto a string builder. Null
   * objects and empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link ToString#toString(Object)} is called on each element.
   * </p>
   * 
   * @param <T>
   * @param buff
   *        The string builder to append to.
   * @param sep
   *        What to insert between each element.
   * @param tos
   *        Convert each object to the desired string format.
   * @param it
   *        The elements to join together.
   * @return The <code>buff</code> parameter.
   */
  public static <T> StringBuilder join( final StringBuilder buff,
      final String sep, final ToString<T> tos, final Iterable<T> it )
  {
    return join(buff, sep, tos, it.iterator());
  }

  /**
   * <p>
   * Appends the elements in the provided iterator onto a string builder. Null
   * objects and empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link ToString#toString(Object)} is called on each element.
   * </p>
   * 
   * @param <T>
   * @param buff
   *        The string builder to append to.
   * @param sep
   *        What to insert between each element.
   * @param tos
   *        Convert each object to the desired string format.
   * @param it
   *        The elements to join together.
   * @return The <code>buff</code> parameter.
   */
  public static <T> StringBuilder join( final StringBuilder buff,
      final String sep, final ToString<T> tos, final Iterator<T> it )
  {
    boolean first = true;
    while ( it.hasNext() )
    {
      final T obj = it.next();
      if ( obj != null )
      {
        final String str = tos.toString(obj);
        if ( str.length() > 0 )
        {
          if ( first )
            first = false;
          else
            buff.append(sep);
          buff.append(str);
        }
      }
    }
    return buff;
  }

  /**
   * <p>
   * Appends the elements in the provided array onto a string builder. Null
   * objects and empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link ToString#toString(Object)} is called on each element.
   * </p>
   * 
   * @param <T>
   * @param buff
   *        The string builder to append to.
   * @param sep
   *        What to insert between each element.
   * @param tos
   *        Convert each object to the desired string format.
   * @param arr
   *        The array of elements to join together.
   * @return The <code>buff</code> parameter.
   */
  public static <T> StringBuilder join( final StringBuilder buff,
      final String sep, final ToString<T> tos, final T... arr )
  {
    return join(buff, sep, tos, new ArrayListIterator<T>(arr));
  }

  /**
   * <p>
   * Appends the elements in the provided array onto a string builder. Null
   * objects and empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link ToString#toString(Object)} is called on each element.
   * </p>
   * 
   * @param <T>
   * @param buff
   *        The string builder to append to.
   * @param sep
   *        What to insert between each element.
   * @param tos
   *        Convert each object to the desired string format.
   * @param arr
   *        The array of elements to join together.
   * @param start_index
   *        The index of the first element to start joining from, to the end of
   *        the array.
   * @return The <code>buff</code> parameter.
   */
  public static <T> StringBuilder join( final StringBuilder buff,
      final String sep, final ToString<T> tos, final T[] arr,
      final int start_index )
  {
    return join(buff, sep, tos,
      new ArrayListIterator<T>(arr, start_index, Arr.length(arr)));
  }

  /**
   * <p>
   * Appends the elements in the provided array onto a string builder. Null
   * objects and empty strings are skipped.
   * </p>
   * 
   * <p>
   * {@link ToString#toString(Object)} is called on each element.
   * </p>
   * 
   * @param <T>
   * @param buff
   *        The string builder to append to.
   * @param sep
   *        What to insert between each element.
   * @param tos
   *        Convert each object to the desired string format.
   * @param arr
   *        The array of elements to join together.
   * @param start_index
   *        The index of the first element to start joining from.
   * @param end_index
   *        The index (exclusive) to stop joining at.
   * @return The <code>buff</code> parameter.
   */
  public static <T> StringBuilder join( final StringBuilder buff,
      final String sep, final ToString<T> tos, final T[] arr,
      final int start_index, final int end_index )
  {
    return join(buff, sep, tos, new ArrayListIterator<T>(arr, start_index,
        end_index));
  }

  /**
   * Return the length of the string, or zero if passed a <code>null</code>.
   * 
   * @param str
   * @return Return the length of the string, or zero if passed a
   *         <code>null</code>.
   */
  public static int length( final CharSequence str )
  {
    return (str == null) ? 0 : str.length();
  }

  /**
   * <p>
   * Trims the string builder contents of any leading <a
   * href="#whitespace">whitespace</a>.
   * </p>
   * 
   * <p>
   * See <a href="#whitespace">note</a> above for our definition of <a
   * href="#whitespace">whitespace</a>.
   * </p>
   * 
   * @param buff
   * @return Returns <code>buff</code>.
   */
  @SuppressWarnings( "null" )
  public static StringBuilder ltrim( final StringBuilder buff )
  {
    final int len = (buff == null) ? 0 : buff.length();
    int start = 0;

    while ( (start < len) && isWS(buff.charAt(start)) )
      ++start;

    if ( start > 0 )
      buff.delete(0, start);

    return buff;
  }

  /**
   * <p>
   * Trims the string of any leading <a href="#whitespace">whitespace</a>, but
   * also returns a zero length string if the parameter was <code>null</code>.
   * Does not allocate a new string if it does not need to.
   * </p>
   * 
   * <p>
   * See <a href="#whitespace">note</a> above for our definition of <a
   * href="#whitespace">whitespace</a>.
   * </p>
   * 
   * @param str
   * @return Return the trimmed string. If <code>str</code> was
   *         <code>null</code> then return a zero length string.
   */
  public static String ltrimToEmpty( final String str )
  {
    final String ret = ltrimToNull(str);
    return (ret != null) ? ret : EMPTY;
  }

  /**
   * <p>
   * Trims the string of any leading <a href="#whitespace">whitespace</a>,
   * returning <code>null</code> if the result is a zero length string, or the
   * parameter was <code>null</code> to begin with. Does not allocate a new
   * string if it does not need to.
   * </p>
   * 
   * <p>
   * See <a href="#whitespace">note</a> above for our definition of <a
   * href="#whitespace">whitespace</a>.
   * </p>
   * 
   * @param str
   * @return Return the trimmed string, or <code>null</code> if <code>str</code>
   *         was <code>null</code> or the result is a zero length string.
   */
  @SuppressWarnings( "null" )
  public static String ltrimToNull( final String str )
  {
    final int len = (str == null) ? 0 : str.length();
    int start = 0;

    while ( (start < len) && isWS(str.charAt(start)) )
      ++start;

    if ( start >= len )
      return null;

    if ( start <= 0 )
      return str; // nothing to trim.

    return str.substring(start, len);
  }

  /**
   * Compares two strings, handling nulls.
   * 
   * <pre>
   * Str.equals(null, null) = false
   * Str.equals(null, "") = true
   * Str.equals("", "") = false
   * Str.equals("", " ") = true
   * Str.equals("abc", "abc") = false
   * Str.equals("abc", "ABC") = true
   * Str.equals("abc", "def") = true
   * </pre>
   * 
   * @param lhs
   * @param rhs
   * @return True if both parameters are <code>null</code>, or
   *         <code>lhs.equals(rhs)</code> returns <code>true</code>.
   */
  public static boolean notEquals( final String lhs, final String rhs )
  {
    if ( lhs == null )
      return rhs != null;
    return !lhs.equals(rhs);
  }

  /**
   * Compares two strings in a case insensitive manner, handling nulls.
   * 
   * <pre>
   * Str.equals(null, null) = false
   * Str.equals(null, "") = true
   * Str.equals("", "") = false
   * Str.equals("", " ") = true
   * Str.equals("abc", "abc") = false
   * Str.equals("abc", "ABC") = false
   * Str.equals("abc", "def") = true
   * </pre>
   * 
   * @param lhs
   * @param rhs
   * @return True if both parameters are <code>null</code>, or
   *         <code>lhs.equalsIgnoreCase(rhs)</code> returns <code>true</code>.
   */
  public static boolean notEqualsIgnoreCase( final String lhs, final String rhs )
  {
    if ( lhs == null )
      return rhs != null;
    return !lhs.equalsIgnoreCase(rhs);
  }

  /**
   * Replace a range of text within a string with a new string.
   * 
   * @param orig
   *        The original string to perform the replacement on.
   * @param start
   *        The start of the range (inclusive).
   * @param end
   *        The end of the range (exclusive).
   * @param replace
   *        The string to put in place of the specified range.
   * @return The string with the replaced range.
   */
  public static String replace( final String orig, final int start,
      final int end, final String replace )
  {
    final int len = orig.length();

    String before = null;
    if ( start > 0 )
      before = orig.substring(0, start);

    String after = null;
    if ( end < len )
      after = orig.substring(end);

    if ( before != null )
    {
      if ( after != null )
        return before + replace + after;
      return before + replace;
    }

    if ( after != null )
      return replace + after;

    return replace;
  }

  /**
   * <p>
   * Trims the string builder contents of any trailing <a
   * href="#whitespace">whitespace</a>.
   * </p>
   * 
   * <p>
   * See <a href="#whitespace">note</a> above for our definition of <a
   * href="#whitespace">whitespace</a>.
   * </p>
   * 
   * @param buff
   * @return Returns <code>buff</code>.
   */
  @SuppressWarnings( "null" )
  public static StringBuilder rtrim( final StringBuilder buff )
  {
    final int len = (buff == null) ? 0 : buff.length();
    int end = len;

    while ( (end > 0) && isWS(buff.charAt(end - 1)) )
      --end;

    if ( end < len )
      buff.setLength(end);

    return buff;
  }

  /**
   * <p>
   * Trims the string of any trailing <a href="#whitespace">whitespace</a>, but
   * also returns a zero length string if the parameter was <code>null</code>.
   * Does not allocate a new string if it does not need to.
   * </p>
   * 
   * <p>
   * See <a href="#whitespace">note</a> above for our definition of <a
   * href="#whitespace">whitespace</a>.
   * </p>
   * 
   * @param str
   * @return Return the trimmed string. If <code>str</code> was
   *         <code>null</code> then return a zero length string.
   */
  public static String rtrimToEmpty( final String str )
  {
    final String ret = rtrimToNull(str);
    return (ret != null) ? ret : EMPTY;
  }

  /**
   * <p>
   * Trims the string of any trailing <a href="#whitespace">whitespace</a>,
   * returning <code>null</code> if the result is a zero length string, or the
   * parameter was <code>null</code> to begin with. Does not allocate a new
   * string if it does not need to.
   * </p>
   * 
   * <p>
   * See <a href="#whitespace">note</a> above for our definition of <a
   * href="#whitespace">whitespace</a>.
   * </p>
   * 
   * @param str
   * @return Return the trimmed string, or <code>null</code> if <code>str</code>
   *         was <code>null</code> or the result is a zero length string.
   */
  @SuppressWarnings( "null" )
  public static String rtrimToNull( final String str )
  {
    int len = (str == null) ? 0 : str.length();
    int end = len;

    while ( (end > 0) && isWS(str.charAt(end - 1)) )
      --end;

    if ( end <= 0 )
      return null;

    if ( end >= len )
      return str; // nothing to trim.

    return str.substring(0, end);
  }

  /**
   * <p>
   * Splits a string on a regular expression.
   * </p>
   * 
   * <p>
   * This works as if you called {@link #split(Pattern, CharSequence, int)} with
   * a <code>limit</code> of zero.
   * </p>
   * 
   * @param regex
   *        The regular expression to split the strong on.
   * @param str
   *        The string to split.
   * @return The array of strings computed by splitting the input around matches
   *         of the given regular expression.
   */
  public static String[] split( final Pattern regex, final CharSequence str )
  {
    return split(regex, str, 0, false, false);
  }

  /**
   * <p>
   * Splits a string on a regular expression, providing various cleanups such as
   * trimming and elimination of blank lines.
   * </p>
   * 
   * <p>
   * This works as if you called
   * {@link #split(Pattern, CharSequence, int, boolean, boolean)} with a
   * <code>limit</code> of zero.
   * </p>
   * 
   * @param regex
   *        The regular expression to split the strong on.
   * @param str
   *        The string to split.
   * @param trim
   *        If <code>true</code>, trims each string in the array of leading and
   *        trailing whitespace.
   * @param remove_blank_lines
   *        If <code>true</code>, removes any strings out of the array that only
   *        contain whitespace, shortening the length of the array.
   * @return The array of strings computed by splitting the input around matches
   *         of the given regular expression.
   */
  public static String[] split( final Pattern regex, final CharSequence str,
      final boolean trim, final boolean remove_blank_lines )
  {
    return split(regex, str, 0, trim, remove_blank_lines);
  }

  /**
   * <p>
   * Splits a string on a regular expression.
   * </p>
   * 
   * <p>
   * The <code>limit</code> parameter controls the number of times the pattern
   * is applied and therefore affects the length of the resulting array. If the
   * limit <em>n</em> is greater than zero then the pattern will be applied at
   * most <em>n - 1</em> times, the array's length will be no greater than
   * <em>n</em>, and the array's last entry will contain all input beyond the
   * last matched delimiter. If <em>n</em> is non-positive then the pattern will
   * be applied as many times as possible and the array can have any length. If
   * <em>n</em> is zero then the pattern will be applied as many times as
   * possible, the array can have any length, and trailing empty strings will be
   * discarded.
   * </p>
   * 
   * @param regex
   *        The regular expression to split the strong on.
   * @param str
   *        The string to split.
   * @param limit
   *        The result threshold, as described above.
   * @return The array of strings computed by splitting the input around matches
   *         of the given regular expression.
   */
  public static String[] split( final Pattern regex, final CharSequence str,
      final int limit )
  {
    return split(regex, str, limit, false, false);
  }

  /**
   * <p>
   * Splits a string on a regular expression, providing various cleanups such as
   * trimming and elimination of blank lines.
   * </p>
   * 
   * <p>
   * The <code>limit</code> parameter controls the number of times the pattern
   * is applied and therefore affects the length of the resulting array. If the
   * limit <em>n</em> is greater than zero then the pattern will be applied at
   * most <em>n - 1</em> times, the array's length will be no greater than
   * <em>n</em>, and the array's last entry will contain all input beyond the
   * last matched delimiter. If <em>n</em> is non-positive then the pattern will
   * be applied as many times as possible and the array can have any length. If
   * <em>n</em> is zero then the pattern will be applied as many times as
   * possible, the array can have any length, and trailing empty strings will be
   * discarded.
   * </p>
   * 
   * @param regex
   *        The regular expression to split the strong on.
   * @param str
   *        The string to split.
   * @param limit
   *        The result threshold, as described above.
   * @param trim
   *        If <code>true</code>, trims each string in the array of leading and
   *        trailing whitespace.
   * @param remove_blank_lines
   *        If <code>true</code>, removes any strings out of the array that only
   *        contain whitespace, shortening the length of the array.
   * @return The array of strings computed by splitting the input around matches
   *         of the given regular expression.
   */
  public static String[] split( final Pattern regex, final CharSequence str,
      final int limit, final boolean trim, final boolean remove_blank_lines )
  {
    if ( str == null )
      return null;
    final String[] arr = regex.split(str, limit);
    if ( (!trim) && (!remove_blank_lines) ) // Short-circuit
      return arr;

    int blank_lines = 0;
    final int len = arr.length;
    for ( int xi = 0; xi < len; ++xi )
    {
      if ( trim )
      {
        if ( remove_blank_lines )
        {
          final String entry = arr[xi] = trimToNull(arr[xi]);
          if ( entry == null )
            ++blank_lines;
        }
        else
        {
          arr[xi] = trimToEmpty(arr[xi]);
        }
      }
      else
      // if ( remove_blank_lines ) // Implicitly true because of above
      // short-circuit.
      {
        if ( isBlank(arr[xi]) )
        {
          ++blank_lines;
          arr[xi] = null;
        }
      }
    }

    if ( blank_lines <= 0 )
      return arr;

    final String[] narr = new String[len - blank_lines];
    int nidx = 0;
    for ( int xi = 0; xi < len; ++xi )
      if ( arr[xi] != null )
        narr[nidx++] = arr[xi];

    return narr;
  }

  /**
   * <p>
   * Splits a string on a newlines.
   * </p>
   * 
   * <p>
   * A newline is represented by one of &quot;<code>\n</code>&quot;, &quot;
   * <code>\r</code>&quot;, or &quot;<code>\r\n</code>&quot;.
   * </p>
   * 
   * <p>
   * This works as if you called {@link #splitLines(CharSequence, int)} with a
   * <code>limit</code> of zero.
   * </p>
   * 
   * @param str
   *        The string to split.
   * @return The array of strings computed by splitting the input around
   *         newlines.
   */
  public static String[] splitLines( final CharSequence str )
  {
    return split(lineSplitter, str, 0, false, false);
  }

  /**
   * <p>
   * Splits a string on a newlines, providing various cleanups such as trimming
   * and elimination of blank lines.
   * </p>
   * 
   * <p>
   * A newline is represented by one of &quot;<code>\n</code>&quot;, &quot;
   * <code>\r</code>&quot;, or &quot;<code>\r\n</code>&quot;.
   * </p>
   * 
   * <p>
   * This works as if you called
   * {@link #splitLines(CharSequence, int, boolean, boolean)} with a
   * <code>limit</code> of zero.
   * </p>
   * 
   * @param str
   *        The string to split.
   * @param trim
   *        If <code>true</code>, trims each string in the array of leading and
   *        trailing whitespace.
   * @param remove_blank_lines
   *        If <code>true</code>, removes any strings out of the array that only
   *        contain whitespace, shortening the length of the array.
   * @return The array of strings computed by splitting the input around
   *         newlines.
   */
  public static String[] splitLines( final CharSequence str,
      final boolean trim, final boolean remove_blank_lines )
  {
    return split(lineSplitter, str, 0, trim, remove_blank_lines);
  }

  /**
   * <p>
   * Splits a string on a newlines.
   * </p>
   * 
   * <p>
   * A newline is represented by one of &quot;<code>\n</code>&quot;, &quot;
   * <code>\r</code>&quot;, or &quot;<code>\r\n</code>&quot;.
   * </p>
   * 
   * <p>
   * The <code>limit</code> parameter controls the number of times the pattern
   * is applied and therefore affects the length of the resulting array. If the
   * limit <em>n</em> is greater than zero then the pattern will be applied at
   * most <em>n - 1</em> times, the array's length will be no greater than
   * <em>n</em>, and the array's last entry will contain all input beyond the
   * last matched delimiter. If <em>n</em> is non-positive then the pattern will
   * be applied as many times as possible and the array can have any length. If
   * <em>n</em> is zero then the pattern will be applied as many times as
   * possible, the array can have any length, and trailing empty strings will be
   * discarded.
   * </p>
   * 
   * @param str
   *        The string to split.
   * @param limit
   *        The result threshold, as described above.
   * @return The array of strings computed by splitting the input around
   *         newlines.
   */
  public static String[] splitLines( final CharSequence str, final int limit )
  {
    return split(lineSplitter, str, limit, false, false);
  }

  /**
   * <p>
   * Splits a string on a newlines, providing various cleanups such as trimming
   * and elimination of blank lines.
   * </p>
   * 
   * <p>
   * A newline is represented by one of &quot;<code>\n</code>&quot;, &quot;
   * <code>\r</code>&quot;, or &quot;<code>\r\n</code>&quot;.
   * </p>
   * 
   * <p>
   * The <code>limit</code> parameter controls the number of times the pattern
   * is applied and therefore affects the length of the resulting array. If the
   * limit <em>n</em> is greater than zero then the pattern will be applied at
   * most <em>n - 1</em> times, the array's length will be no greater than
   * <em>n</em>, and the array's last entry will contain all input beyond the
   * last matched delimiter. If <em>n</em> is non-positive then the pattern will
   * be applied as many times as possible and the array can have any length. If
   * <em>n</em> is zero then the pattern will be applied as many times as
   * possible, the array can have any length, and trailing empty strings will be
   * discarded.
   * </p>
   * 
   * @param str
   *        The string to split.
   * @param limit
   *        The result threshold, as described above.
   * @param trim
   *        If <code>true</code>, trims each string in the array of leading and
   *        trailing whitespace.
   * @param remove_blank_lines
   *        If <code>true</code>, removes any strings out of the array that only
   *        contain whitespace, shortening the length of the array.
   * @return The array of strings computed by splitting the input around
   *         newlines.
   */
  public static String[] splitLines( final CharSequence str, final int limit,
      final boolean trim, final boolean remove_blank_lines )
  {
    return split(lineSplitter, str, limit, trim, remove_blank_lines);
  }

  /**
   * <p>
   * Takes what would normally be output from
   * {@link Throwable#printStackTrace()} and breaks it into an array of Strings
   * (one per line).
   * </p>
   * 
   * @param ex
   *        The exception to process.
   * @return The array of strings.
   */
  public static String[] splitStackTrace( final Throwable ex )
  {
    final String[] arr = lineSplitter.split(getStackTrace(ex));
    if ( arr == null )
      return null;

    int blank_lines = 0;
    final int len = arr.length;
    for ( int xi = 0; xi < len; ++xi )
    {
      final String entry = arr[xi] = rtrimToNull(arr[xi]);
      if ( entry == null )
        ++blank_lines;
    }

    if ( blank_lines <= 0 )
      return arr;

    final String[] narr = new String[len - blank_lines];
    int nidx = 0;
    for ( int xi = 0; xi < len; ++xi )
      if ( arr[xi] != null )
        narr[nidx++] = arr[xi];

    return narr;
  }

  /**
   * Tests if the string starts with the specified prefix, ignoring case. This
   * is really just a thin wrapper around
   * {@link java.lang.String#regionMatches(boolean, int, String, int, int)} that
   * calculates the right offsets.
   * 
   * @param str
   *        The string to check.
   * @param prefix
   *        The prefix.
   * @return Returns <code>true</code> if <code>str</code> starts with
   *         <code>prefix</code>, ignoring case. Note that the result will be
   *         <code>true</code> if <code>prefix</code> is an empty string or is
   *         equal to <code>str</code> as determined by the
   *         <code>equalsIgnoreCase(Object)</code> method.
   */
  public static boolean startsWithIgnoreCase( final String str,
      final String prefix )
  {
    if ( (str == null) || (prefix == null) )
      return false;
    return str.regionMatches(true, 0, prefix, 0, prefix.length());
  }

  /**
   * Tests if the string starts with the specified prefix, ignoring case. This
   * is really just a thin wrapper around
   * {@link java.lang.String#regionMatches(boolean, int, String, int, int)} that
   * calculates the right offsets.
   * 
   * @param str
   *        The string to check.
   * @param prefix
   *        The prefix.
   * @param toffset
   *        The position in <code>str</code> to start comparison from.
   * @return Returns <code>true</code> if <code>str</code> starts with
   *         <code>prefix</code>, ignoring case. Note that the result will be
   *         <code>true</code> if <code>prefix</code> is an empty string or is
   *         equal to <code>str</code> as determined by the
   *         <code>equalsIgnoreCase(Object)</code> method.
   */
  public static boolean startsWithIgnoreCase( final String str,
      final String prefix, final int toffset )
  {
    if ( (str == null) || (prefix == null) )
      return false;
    return str.regionMatches(true, toffset, prefix, 0, prefix.length());
  }

  /**
   * <p>
   * Trims the string builder contents of any leading and trailing <a
   * href="#whitespace">whitespace</a>.
   * </p>
   * 
   * <p>
   * See <a href="#whitespace">note</a> above for our definition of <a
   * href="#whitespace">whitespace</a>.
   * </p>
   * 
   * @param buff
   * @return Returns <code>buff</code>.
   */
  @SuppressWarnings( "null" )
  public static StringBuilder trim( final StringBuilder buff )
  {
    final int len = (buff == null) ? 0 : buff.length();
    int start = 0;
    int end = len;

    while ( (start < end) && isWS(buff.charAt(end - 1)) )
      --end;

    while ( (start < end) && isWS(buff.charAt(start)) )
      ++start;

    if ( end < len )
      buff.setLength(end);
    if ( start > 0 )
      buff.delete(0, start);

    return buff;
  }

  /**
   * <p>
   * Trims the string of any leading and trailing <a
   * href="#whitespace">whitespace</a>, but also returns a zero length string if
   * the parameter was <code>null</code>. Does not allocate a new string if it
   * does not need to.
   * </p>
   * 
   * <p>
   * See <a href="#whitespace">note</a> above for our definition of <a
   * href="#whitespace">whitespace</a>.
   * </p>
   * 
   * @param str
   * @return Return the trimmed string. If <code>str</code> was
   *         <code>null</code> then return a zero length string.
   */
  public static String trimToEmpty( final String str )
  {
    final String ret = trimToNull(str);
    return (ret != null) ? ret : EMPTY;
  }

  /**
   * <p>
   * Trims the string of any leading and trailing <a
   * href="#whitespace">whitespace</a>, returning <code>null</code> if the
   * result is a zero length string, or the parameter was <code>null</code> to
   * begin with. Does not allocate a new string if it does not need to.
   * </p>
   * 
   * <p>
   * See <a href="#whitespace">note</a> above for our definition of <a
   * href="#whitespace">whitespace</a>.
   * </p>
   * 
   * @param str
   * @return Return the trimmed string, or <code>null</code> if <code>str</code>
   *         was <code>null</code> or the result is a zero length string.
   */
  @SuppressWarnings( "null" )
  public static String trimToNull( final String str )
  {
    final int len = (str == null) ? 0 : str.length();
    int start = 0;
    int end = len;

    while ( (start < end) && isWS(str.charAt(start)) )
      ++start;

    while ( (start < end) && isWS(str.charAt(end - 1)) )
      --end;

    if ( start >= end )
      return null;

    if ( (start <= 0) && (end >= len) )
      return str; // nothing to trim.

    return str.substring(start, end);
  }

  private static boolean isWS( final char ch )
  {
    return (ch <= ' ') || Character.isWhitespace(ch);
  }

  private Str()
  {
    // empty
  }
}
