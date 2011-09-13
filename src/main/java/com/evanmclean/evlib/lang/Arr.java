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

/**
 * Misc functions for various array related operations.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class Arr
{
  /**
   * Returns a variable number of arguments as an array.
   * 
   * @param args
   *        The values to return as an array.
   * @return An array of the arguments.
   */
  public static boolean[] arr( final boolean... args )
  {
    return args;
  }

  /**
   * Returns a variable number of arguments as an array.
   * 
   * @param args
   *        The values to return as an array.
   * @return An array of the arguments.
   */
  public static byte[] arr( final byte... args )
  {
    return args;
  }

  /**
   * Returns a variable number of arguments as an array.
   * 
   * @param args
   *        The values to return as an array.
   * @return An array of the arguments.
   */
  public static char[] arr( final char... args )
  {
    return args;
  }

  /**
   * Returns a variable number of arguments as an array.
   * 
   * @param args
   *        The values to return as an array.
   * @return An array of the arguments.
   */
  public static double[] arr( final double... args )
  {
    return args;
  }

  /**
   * Returns a variable number of arguments as an array.
   * 
   * @param args
   *        The values to return as an array.
   * @return An array of the arguments.
   */
  public static float[] arr( final float... args )
  {
    return args;
  }

  /**
   * Returns a variable number of arguments as an array.
   * 
   * @param args
   *        The values to return as an array.
   * @return An array of the arguments.
   */
  public static int[] arr( final int... args )
  {
    return args;
  }

  /**
   * Returns a variable number of arguments as an array.
   * 
   * @param args
   *        The values to return as an array.
   * @return An array of the arguments.
   */
  public static long[] arr( final long... args )
  {
    return args;
  }

  /**
   * Returns a variable number of arguments as an array.
   * 
   * @param args
   *        The values to return as an array.
   * @return An array of the arguments.
   */
  public static short[] arr( final short... args )
  {
    return args;
  }

  /**
   * Returns a variable number of arguments as an array.
   * 
   * @param <T>
   *        Type of arguments array to return.
   * @param args
   *        The objects to return as an array.
   * @return An array of the arguments.
   */
  public static <T> T[] arr( final T... args )
  {
    return args;
  }

  /**
   * Return true if the array is of zero length or null;
   * 
   * @param arr
   * @return Return true if the array is of zero length or <code>null</code>.
   */
  public static boolean isEmpty( final boolean[] arr )
  {
    return (arr == null) || (arr.length <= 0);
  }

  /**
   * Return true if the array is of zero length or null;
   * 
   * @param arr
   * @return Return true if the array is of zero length or <code>null</code>.
   */
  public static boolean isEmpty( final byte[] arr )
  {
    return (arr == null) || (arr.length <= 0);
  }

  /**
   * Return true if the array is of zero length or null;
   * 
   * @param arr
   * @return Return true if the array is of zero length or <code>null</code>.
   */
  public static boolean isEmpty( final char[] arr )
  {
    return (arr == null) || (arr.length <= 0);
  }

  /**
   * Return true if the array is of zero length or null;
   * 
   * @param arr
   * @return Return true if the array is of zero length or <code>null</code>.
   */
  public static boolean isEmpty( final double[] arr )
  {
    return (arr == null) || (arr.length <= 0);
  }

  /**
   * Return true if the array is of zero length or null;
   * 
   * @param arr
   * @return Return true if the array is of zero length or <code>null</code>.
   */
  public static boolean isEmpty( final float[] arr )
  {
    return (arr == null) || (arr.length <= 0);
  }

  /**
   * Return true if the array is of zero length or null;
   * 
   * @param arr
   * @return Return true if the array is of zero length or <code>null</code>.
   */
  public static boolean isEmpty( final int[] arr )
  {
    return (arr == null) || (arr.length <= 0);
  }

  /**
   * Return true if the array is of zero length or null;
   * 
   * @param arr
   * @return Return true if the array is of zero length or <code>null</code>.
   */
  public static boolean isEmpty( final long[] arr )
  {
    return (arr == null) || (arr.length <= 0);
  }

  /**
   * Return true if the array is of zero length or null;
   * 
   * @param arr
   * @return Return true if the array is of zero length or <code>null</code>.
   */
  public static boolean isEmpty( final short[] arr )
  {
    return (arr == null) || (arr.length <= 0);
  }

  /**
   * Return true if the array is of zero length or null;
   * 
   * @param <T>
   * @param arr
   * @return Return true if the array is of zero length or <code>null</code>.
   */
  public static <T> boolean isEmpty( final T[] arr )
  {
    return (arr == null) || (arr.length <= 0);
  }

  /**
   * Return true if the array is not null and not of zero length.
   * 
   * @param arr
   * @return Return true if the array is not null and not of zero length.
   */
  public static boolean isNotEmpty( final boolean[] arr )
  {
    return (arr != null) && (arr.length > 0);
  }

  /**
   * Return true if the array is not null and not of zero length.
   * 
   * @param arr
   * @return Return true if the array is not null and not of zero length.
   */
  public static boolean isNotEmpty( final byte[] arr )
  {
    return (arr != null) && (arr.length > 0);
  }

  /**
   * Return true if the array is not null and not of zero length.
   * 
   * @param arr
   * @return Return true if the array is not null and not of zero length.
   */
  public static boolean isNotEmpty( final char[] arr )
  {
    return (arr != null) && (arr.length > 0);
  }

  /**
   * Return true if the array is not null and not of zero length.
   * 
   * @param arr
   * @return Return true if the array is not null and not of zero length.
   */
  public static boolean isNotEmpty( final double[] arr )
  {
    return (arr != null) && (arr.length > 0);
  }

  /**
   * Return true if the array is not null and not of zero length.
   * 
   * @param arr
   * @return Return true if the array is not null and not of zero length.
   */
  public static boolean isNotEmpty( final float[] arr )
  {
    return (arr != null) && (arr.length > 0);
  }

  /**
   * Return true if the array is not null and not of zero length.
   * 
   * @param arr
   * @return Return true if the array is not null and not of zero length.
   */
  public static boolean isNotEmpty( final int[] arr )
  {
    return (arr != null) && (arr.length > 0);
  }

  /**
   * Return true if the array is not null and not of zero length.
   * 
   * @param arr
   * @return Return true if the array is not null and not of zero length.
   */
  public static boolean isNotEmpty( final long[] arr )
  {
    return (arr != null) && (arr.length > 0);
  }

  /**
   * Return true if the array is not null and not of zero length.
   * 
   * @param arr
   * @return Return true if the array is not null and not of zero length.
   */
  public static boolean isNotEmpty( final short[] arr )
  {
    return (arr != null) && (arr.length > 0);
  }

  /**
   * Return true if the array is not null and not of zero length.
   * 
   * @param <T>
   * @param arr
   * @return Return true if the array is not null and not of zero length.
   */
  public static <T> boolean isNotEmpty( final T[] arr )
  {
    return (arr != null) && (arr.length > 0);
  }

  /**
   * Return the length of the array (zero for null).
   * 
   * @param arr
   * @return Return the length of the array, or zero of <code>arr</code> is
   *         <code>null</code>.
   */
  public static int length( final boolean[] arr )
  {
    return (arr == null) ? 0 : arr.length;
  }

  /**
   * Return the length of the array (zero for null).
   * 
   * @param arr
   * @return Return the length of the array, or zero of <code>arr</code> is
   *         <code>null</code>.
   */
  public static int length( final byte[] arr )
  {
    return (arr == null) ? 0 : arr.length;
  }

  /**
   * Return the length of the array (zero for null).
   * 
   * @param arr
   * @return Return the length of the array, or zero of <code>arr</code> is
   *         <code>null</code>.
   */
  public static int length( final char[] arr )
  {
    return (arr == null) ? 0 : arr.length;
  }

  /**
   * Return the length of the array (zero for null).
   * 
   * @param arr
   * @return Return the length of the array, or zero of <code>arr</code> is
   *         <code>null</code>.
   */
  public static int length( final double[] arr )
  {
    return (arr == null) ? 0 : arr.length;
  }

  /**
   * Return the length of the array (zero for null).
   * 
   * @param arr
   * @return Return the length of the array, or zero of <code>arr</code> is
   *         <code>null</code>.
   */
  public static int length( final float[] arr )
  {
    return (arr == null) ? 0 : arr.length;
  }

  /**
   * Return the length of the array (zero for null).
   * 
   * @param arr
   * @return Return the length of the array, or zero of <code>arr</code> is
   *         <code>null</code>.
   */
  public static int length( final int[] arr )
  {
    return (arr == null) ? 0 : arr.length;
  }

  /**
   * Return the length of the array (zero for null).
   * 
   * @param arr
   * @return Return the length of the array, or zero of <code>arr</code> is
   *         <code>null</code>.
   */
  public static int length( final long[] arr )
  {
    return (arr == null) ? 0 : arr.length;
  }

  /**
   * Return the length of the array (zero for null).
   * 
   * @param arr
   * @return Return the length of the array, or zero of <code>arr</code> is
   *         <code>null</code>.
   */
  public static int length( final short[] arr )
  {
    return (arr == null) ? 0 : arr.length;
  }

  /**
   * Return the length of the array (zero for null).
   * 
   * @param <T>
   * @param arr
   * @return Return the length of the array, or zero of <code>arr</code> is
   *         <code>null</code>.
   */
  public static <T> int length( final T[] arr )
  {
    return (arr == null) ? 0 : arr.length;
  }

  /**
   * Return the array if is not null and not empty (zero length).
   * 
   * @param arr
   * @return Returns the array if it is not empty, otherwise null.
   */
  public static boolean[] nullIfEmpty( final boolean[] arr )
  {
    return ((arr == null) || (arr.length <= 0)) ? null : arr;
  }

  /**
   * Return the array if is not null and not empty (zero length).
   * 
   * @param arr
   * @return Returns the array if it is not empty, otherwise null.
   */
  public static byte[] nullIfEmpty( final byte[] arr )
  {
    return ((arr == null) || (arr.length <= 0)) ? null : arr;
  }

  /**
   * Return the array if is not null and not empty (zero length).
   * 
   * @param arr
   * @return Returns the array if it is not empty, otherwise null.
   */
  public static char[] nullIfEmpty( final char[] arr )
  {
    return ((arr == null) || (arr.length <= 0)) ? null : arr;
  }

  /**
   * Return the array if is not null and not empty (zero length).
   * 
   * @param arr
   * @return Returns the array if it is not empty, otherwise null.
   */
  public static double[] nullIfEmpty( final double[] arr )
  {
    return ((arr == null) || (arr.length <= 0)) ? null : arr;
  }

  /**
   * Return the array if is not null and not empty (zero length).
   * 
   * @param arr
   * @return Returns the array if it is not empty, otherwise null.
   */
  public static float[] nullIfEmpty( final float[] arr )
  {
    return ((arr == null) || (arr.length <= 0)) ? null : arr;
  }

  /**
   * Return the array if is not null and not empty (zero length).
   * 
   * @param arr
   * @return Returns the array if it is not empty, otherwise null.
   */
  public static int[] nullIfEmpty( final int[] arr )
  {
    return ((arr == null) || (arr.length <= 0)) ? null : arr;
  }

  /**
   * Return the array if is not null and not empty (zero length).
   * 
   * @param arr
   * @return Returns the array if it is not empty, otherwise null.
   */
  public static long[] nullIfEmpty( final long[] arr )
  {
    return ((arr == null) || (arr.length <= 0)) ? null : arr;
  }

  /**
   * Return the array if is not null and not empty (zero length).
   * 
   * @param arr
   * @return Returns the array if it is not empty, otherwise null.
   */
  public static short[] nullIfEmpty( final short[] arr )
  {
    return ((arr == null) || (arr.length <= 0)) ? null : arr;
  }

  /**
   * Return the array if is not null and not empty (zero length).
   * 
   * @param arr
   * @return Returns the array if it is not empty, otherwise null.
   */
  public static <T> T[] nullIfEmpty( final T[] arr )
  {
    return ((arr == null) || (arr.length <= 0)) ? null : arr;
  }

  private Arr()
  {
    // empty
  }
}
