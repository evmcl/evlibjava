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
package com.evanmclean.evlib.misc;

import java.util.Locale;

import com.evanmclean.evlib.lang.Str;

/**
 * Functions to convert strings to numbers, returning default values on errors.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class Conv
{
  /**
   * <p>
   * Converts a string representing a size such as &quot;1.5MB&quot; to a raw
   * value of bytes.
   * </p>
   * 
   * <p>
   * Size suffixes follow SI and IEC conventions as can be seen on the table on
   * <a href="http://en.wikipedia.org/wiki/Kibibyte">Wikipedia</a>. So
   * &quot;KB&quot; equals a multiplier of 1000, while &quot;KiB&quot; equals a
   * multiplier of 1024.
   * </p>
   * 
   * <p>
   * The allowed (case-insensitive) multiplier suffixes are:
   * </p>
   * 
   * <table>
   * <caption>Allowed suffixes and their multiplier values</caption> <thead>
   * <tr>
   * <th>Name</th>
   * <th>Suffix</th>
   * <th>Multiplier</th>
   * <th>Name</th>
   * <th>Suffix</th>
   * <th>Multiplier</th>
   * </tr>
   * </thead> <tbody>
   * <tr>
   * <td></td>
   * <td><em>(none)</em></td>
   * <td>1</td>
   * </tr>
   * <tr>
   * <td>kilobyte</td>
   * <td>KB</td>
   * <td>10<sup>3</sup></td>
   * <td>kibibyte</td>
   * <td>KiB</td>
   * <td>2<sup>10</sup></td>
   * </tr>
   * <tr>
   * <td>megabyte</td>
   * <td>MB</td>
   * <td>10<sup>6</sup></td>
   * <td>mebibyte</td>
   * <td>MiB</td>
   * <td>2<sup>20</sup></td>
   * </tr>
   * <tr>
   * <td>gigabyte</td>
   * <td>GB</td>
   * <td>10<sup>9</sup></td>
   * <td>gibibyte</td>
   * <td>GiB</td>
   * <td>2<sup>30</sup></td>
   * </tr>
   * <tr>
   * <td>terabyte</td>
   * <td>TB</td>
   * <td>10<sup>12</sup></td>
   * <td>tebibyte</td>
   * <td>TiB</td>
   * <td>2<sup>40</sup></td>
   * </tr>
   * <tr>
   * <td>petabyte</td>
   * <td>PB</td>
   * <td>10<sup>15</sup></td>
   * <td>pebibyte</td>
   * <td>PiB</td>
   * <td>2<sup>50</sup></td>
   * </tr>
   * <tr>
   * <td>exabyte</td>
   * <td>EB</td>
   * <td>10<sup>18</sup></td>
   * <td>exbibyte</td>
   * <td>EiB</td>
   * <td>2<sup>60</sup></td>
   * </tr>
   * <tr>
   * <td>zettabyte</td>
   * <td>ZB</td>
   * <td>10<sup>21</sup></td>
   * <td>zebibyte</td>
   * <td>ZiB</td>
   * <td>2<sup>70</sup></td>
   * </tr>
   * <tr>
   * <td>yottabyte</td>
   * <td>YB</td>
   * <td>10<sup>24</sup></td>
   * <td>yobibyte</td>
   * <td>YiB</td>
   * <td>2<sup>80</sup></td>
   * </tr>
   * </tbody>
   * </table>
   * 
   * <p>
   * If the final value, converted from a double to a long would be negative, or
   * greater than <code>Long.MAX_VALUE</code> then this is considered an error
   * and the default value is returned.
   * </p>
   * 
   * @param numstr
   *        The string to convert.
   * @param def
   *        The default value to return in case of an error.
   * @return The converted value, or the default value if case of an error.
   */
  public static long sizeToLong( final String numstr, final long def )
  {
    if ( numstr == null )
      return def;
    String str = numstr.trim();
    final int len = str.length();
    if ( len <= 0 )
      return def;

    int pos = 0;
    boolean seen_decimal = false;
    while ( pos < len )
    {
      final char ch = str.charAt(pos);
      if ( !Character.isDigit(ch) )
      {
        if ( ((ch != '.') && (ch != ',')) || seen_decimal )
          break;
        seen_decimal = true;
      }
      ++pos;
    }

    if ( pos <= 0 )
      return def;

    double val = toDouble(str.substring(0, pos), -1.0);
    if ( val < 0.0 )
      return 0L;

    if ( pos < len )
    {
      str = str.substring(pos).trim().toLowerCase(Locale.ENGLISH);
      if ( "kb".equals(str) )
        val *= Math.pow(10.0, 3.0);
      else if ( "kib".equals(str) )
        val *= Math.pow(2.0, 10.0);
      else if ( "mb".equals(str) )
        val *= Math.pow(10.0, 6.0);
      else if ( "mib".equals(str) )
        val *= Math.pow(2.0, 20.0);
      else if ( "gb".equals(str) )
        val *= Math.pow(10.0, 9.0);
      else if ( "gib".equals(str) )
        val *= Math.pow(2.0, 30.0);
      else if ( "tb".equals(str) )
        val *= Math.pow(10.0, 12.0);
      else if ( "tib".equals(str) )
        val *= Math.pow(2.0, 40.0);
      else if ( "pb".equals(str) )
        val *= Math.pow(10.0, 15.0);
      else if ( "pib".equals(str) )
        val *= Math.pow(2.0, 50.0);
      else if ( "eb".equals(str) )
        val *= Math.pow(10.0, 18.0);
      else if ( "eib".equals(str) )
        val *= Math.pow(2.0, 60.0);
      else if ( "zb".equals(str) )
        val *= Math.pow(10.0, 21.0);
      else if ( "zib".equals(str) )
        val *= Math.pow(2.0, 70.0);
      else if ( "yb".equals(str) )
        val *= Math.pow(10.0, 24.0);
      else if ( "yib".equals(str) )
        val *= Math.pow(2.0, 80.0);
    }

    if ( Double.isInfinite(val) || Double.isNaN(val) )
      return def;
    final long lval = Math.round(val);
    if ( (lval < 0) || (lval >= Long.MAX_VALUE) )
      return def;

    return lval;
  }

  /**
   * Converts the (trimmed) string to a double. If the conversion fails for any
   * reason whatsoever then the specified default value is returned.
   * 
   * @param str
   *        The string to convert.
   * @param def
   *        The default to return if the string parameter cannot be converted to
   *        a number.
   * @return The converted number or the default.
   */
  public static double toDouble( final String str, final double def )
  {
    final String numstr = Str.trimToNull(str);
    if ( numstr == null )
      return def;

    try
    {
      return Double.parseDouble(numstr);
    }
    catch ( NumberFormatException ex )
    {
      return def;
    }
  }

  /**
   * Converts the (trimmed) string to a Double, an empty string translating to
   * null. If the conversion fails for any other reason whatsoever then the
   * specified default value is returned.
   * 
   * @param str
   *        The string to convert.
   * @param def
   *        The default to return if the string parameter cannot be converted to
   *        a number.
   * @return The converted number or the default.
   */
  public static Double toDoubleObj( final String str, final Double def )
  {
    final String numstr = Str.trimToNull(str);
    if ( numstr == null )
      return null;
    try
    {
      return Double.valueOf(numstr);
    }
    catch ( NumberFormatException ex )
    {
      return def;
    }
  }

  /**
   * Converts the (trimmed) string to a integer. If the conversion fails for any
   * reason whatsoever (such as the string being null or not a valid base 10
   * number) then the specified default value is returned.
   * 
   * @param str
   *        The string to convert.
   * @param def
   *        The default to return if the string parameter cannot be converted to
   *        a number.
   * @return The converted number or the default.
   */
  public static int toInt( final String str, final int def )
  {
    final String numstr = Str.trimToNull(str);
    if ( numstr == null )
      return def;
    try
    {
      return Integer.parseInt(numstr);
    }
    catch ( NumberFormatException ex )
    {
      return def;
    }
  }

  /**
   * Converts the (trimmed) string to a long. If the conversion fails for any
   * reason whatsoever (such as the string being null or not a valid base 10
   * number) then the specified default value is returned.
   * 
   * @param str
   *        The string to convert.
   * @param def
   *        The default to return if the string parameter cannot be converted to
   *        a number.
   * @return The converted number or the default.
   */
  public static long toLong( final String str, final long def )
  {
    final String numstr = Str.trimToNull(str);
    if ( numstr == null )
      return def;
    try
    {
      return Long.parseLong(numstr);
    }
    catch ( NumberFormatException ex )
    {
      return def;
    }
  }

  private Conv()
  {
    // empty
  }
}
