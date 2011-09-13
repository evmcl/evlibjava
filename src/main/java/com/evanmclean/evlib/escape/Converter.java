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
package com.evanmclean.evlib.escape;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.evanmclean.evlib.lang.Str;

/**
 * The heart of all the escape objects, this object performs the actual
 * substitutions, if necessary.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
final class Converter
{
  /**
   * Simplest substitution of obj.toString() value based on a character
   * converter.
   * 
   * @param obj
   *        The object to perform conversion on. Will first call
   *        {@link Object#toString()} and then perform the necessary
   *        substitution on that.
   * @param conv
   *        The character conversion to perform.
   * @return The substituted string, or <code>null</code> if <code>obj</code>
   *         was <code>null</code>.
   */
  static String sub( final Object obj, final CharConv conv )
  {
    if ( obj == null )
      return null;
    final String orig = Str.ifNull(obj.toString());
    final int len = orig.length();
    int pos = 0;
    String entity = null;
    while ( pos < len )
    {
      if ( (entity = conv.conv(orig.charAt(pos))) != null )
        break;
      ++pos;
    }
    if ( entity == null )
      return orig;

    final StringBuilder buff = new StringBuilder(((len * 3) / 2));
    if ( pos > 0 )
      buff.append(orig, 0, pos);
    buff.append(entity);
    for ( ++pos; pos < len; ++pos )
    {
      final char ch = orig.charAt(pos);
      if ( (entity = conv.conv(ch)) == null )
        buff.append(ch);
      else
        buff.append(entity);
    }
    return buff.toString();
  }

  /**
   * <p>
   * Performs substitution of obj.toString() value based on a character
   * converter, but also replacing all occurances of a new line with the
   * specified string.
   * </p>
   * 
   * <p>
   * A newline in this case is one of the following character combinations:
   * </p>
   * <dl>
   * <dt><code>\r</code></dt>
   * <dd>A single Carriage Return (ASCII 13)</dd>
   * <dt><code>\n</code></dt>
   * <dd>A single Line Feed or Newline (ASCII 10)</dd>
   * <dt><code>\r\n</code></dt>
   * <dd>A Carriage Return followed by a Line Feed</dd>
   * </dl>
   * 
   * @param obj
   *        The object to perform conversion on. Will first call
   *        {@link Object#toString()} and then perform the necessary
   *        substitution on that.
   * @param conv
   *        The character conversion to perform.
   * @param nl
   *        The string to substitute as a newline.
   * @param replace
   *        If <code>true</code>, then <code>nl</code> replaces the new line
   *        character sequence, if false it is just inserted before the
   *        sequence.
   * @return The substituted string, or <code>null</code> if <code>obj</code>
   *         was <code>null</code>.
   */
  static String sub( final Object obj, final CharConv conv, final String nl,
      final boolean replace )
  {
    if ( obj == null )
      return null;
    final String orig = Str.ifNull(obj.toString());
    final int len = orig.length();
    int pos = 0;
    char ch = 0;
    String entity = null;
    while ( pos < len )
    {
      ch = orig.charAt(pos);
      if ( (ch == '\r') || (ch == '\n') )
        break;
      if ( (entity = conv.conv(ch)) != null )
        break;
      ++pos;
    }
    if ( pos >= len )
      return orig;

    final StringBuilder buff = new StringBuilder(((len * 3) / 2));
    if ( pos > 0 )
      buff.append(orig, 0, pos);
    if ( entity != null )
    {
      buff.append(entity);
      ++pos;
    }
    else
    {
      buff.append(nl);
      if ( !replace )
        buff.append(ch);
      ++pos;
      if ( (ch == '\r') && (pos < len) && (orig.charAt(pos) == '\n') )
      {
        if ( !replace )
          buff.append('\n');
        ++pos;
      }
    }
    for ( /* empty */; pos < len; ++pos )
    {
      ch = orig.charAt(pos);
      if ( ch == '\n' )
      {
        buff.append(nl);
        if ( !replace )
          buff.append(ch);
      }
      else if ( ch == '\r' )
      {
        buff.append(nl);
        if ( !replace )
          buff.append(ch);
        if ( ((pos + 1) < len) && ((ch = orig.charAt(pos + 1)) == '\n') )
        {
          if ( !replace )
            buff.append(ch);
          ++pos;
        }
      }
      else if ( (entity = conv.conv(ch)) == null )
      {
        buff.append(ch);
      }
      else
      {
        buff.append(entity);
      }
    }
    return buff.toString();
  }

  /**
   * Performs substitution of obj.toString() value based on a regular
   * expression, which is run repeatedly until no matches are found (BE
   * CAREFUL).
   * 
   * @param obj
   *        The object to perform conversion on. Will first call
   *        {@link Object#toString()} and then perform the necessary
   *        substitution on that.
   * @param bad
   *        The regular expression matching bad part of the string to be
   *        substituted.
   * @param good
   *        What to replace bad parts of the string with.
   * @return The substituted string, or <code>null</code> if <code>obj</code>
   *         was <code>null</code>.
   */
  static String sub( final Object obj, final Pattern bad, final String good )
  {
    if ( obj == null )
      return null;
    final String orig = Str.ifNull(obj.toString());
    Matcher mat = bad.matcher(orig);
    if ( !mat.find() )
      return orig;

    String ret = mat.replaceAll(good);
    mat = bad.matcher(ret);
    while ( mat.find() )
    {
      ret = mat.replaceAll(good);
      mat = bad.matcher(ret);
    }
    return ret;
  }

  /**
   * Performs substitution of obj.toString() value based on an extended
   * character converter.
   * 
   * @param obj
   *        The object to perform conversion on. Will first call
   *        {@link Object#toString()} and then perform the necessary
   *        substitution on that.
   * @param conv
   *        The character conversion to perform.
   * @return The substituted string, or <code>null</code> if <code>obj</code>
   *         was <code>null</code>.
   */
  static String xsub( final Object obj, final ExtendedCharConv conv )
  {
    if ( obj == null )
      return null;
    final String orig = Str.ifNull(obj.toString());
    final int len = orig.length();
    int pos = conv.convFrom(orig);
    if ( pos < 0 )
      return orig;

    final StringBuilder buff = conv.initialise(new StringBuilder(
        ((len * 3) / 2)), orig);
    if ( pos > 0 )
      buff.append(orig, 0, pos);
    for ( /* empty */; pos < len; ++pos )
    {
      final char ch = orig.charAt(pos);
      final String entity = conv.conv(ch);
      if ( entity == null )
        buff.append(ch);
      else
        buff.append(entity);
    }
    return conv.finish(buff, orig);
  }

  private Converter()
  {
    // empty
  }
}
