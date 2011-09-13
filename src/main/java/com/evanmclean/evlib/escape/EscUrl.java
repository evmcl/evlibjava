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

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.BitSet;

import com.evanmclean.evlib.charset.Charsets;
import com.evanmclean.evlib.exceptions.UnhandledException;
import com.evanmclean.evlib.util.TreeSetIgnoreCase;

/**
 * <p>
 * Makes a string safe to use as a URL parameter.
 * </p>
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class EscUrl
{
  private final BitSet safe;
  private final char[] hex = "0123456789ABCDEF".toCharArray();
  private final TreeSetIgnoreCase safeCharsets = new TreeSetIgnoreCase();

  EscUrl()
  {
    safe = new BitSet(256);
    for ( int xi = 'a'; xi <= 'z'; ++xi )
      safe.set(xi);
    for ( int xi = 'A'; xi <= 'Z'; ++xi )
      safe.set(xi);
    for ( int xi = '0'; xi <= '9'; ++xi )
      safe.set(xi);
    safe.set('.');
    safe.set('-');
    safe.set('*');
    safe.set('_');

    safeCharsets.add("UTF-8");
    safeCharsets.add("US-ASCII");
    safeCharsets.add("ISO-8859-1");
    safeCharsets.add("ISO-LATIN-1");
  }

  /**
   * <p>
   * Makes a string safe to use as a URL parameter. Uses UTF-8 encoding for
   * extended unicode characters if necessary. <strong>This is the one you'll
   * probably use most of the time.</strong>
   * </p>
   * 
   * @param obj
   * @return The substituted string, or <code>null</code> if <code>obj</code>
   *         was <code>null</code>.
   */
  public String text( final Object obj )
  {
    return text(obj, Charsets.UTF8_NAME);
  }

  /**
   * <p>
   * Makes a string safe to use as a URL parameter. Uses the specified character
   * set for encoding extended unicode characters if necessary.
   * </p>
   * 
   * @param obj
   * @param cs
   *        The character set to use.
   * @return The substituted string, or <code>null</code> if <code>obj</code>
   *         was <code>null</code>.
   */
  public String text( final Object obj, final Charset cs )
  {
    return text(obj, cs.name());
  }

  /**
   * <p>
   * Makes a string safe to use as a URL parameter. Uses the specified character
   * set for encoding extended unicode characters if necessary.
   * </p>
   * 
   * @param obj
   * @param charset
   *        The character set to use.
   * @return The substituted string, or <code>null</code> if <code>obj</code>
   *         was <code>null</code>.
   */
  public String text( final Object obj, final String charset )
  {
    if ( obj == null )
      return null;
    final String orig = obj.toString();
    final int len = orig.length();
    int pos = 0;
    if ( safeCharsets.contains(charset) )
      while ( pos < len )
      {
        if ( !safe.get(orig.charAt(pos)) )
          break;
        ++pos;
      }
    if ( pos >= len )
      return orig;
    try
    {
      final StringBuffer buff = new StringBuffer((len * 2) / 3);
      buff.append(orig, 0, pos);
      final byte[] arr = orig.substring(pos).getBytes(charset);
      for ( int xi = 0; xi < arr.length; ++xi )
      {
        final int bt = (arr[xi]) & 0xFF;
        if ( safe.get(bt) )
        {
          buff.append((char) bt);
        }
        else
        {
          buff.append('%');
          buff.append(hex[(bt >> 4) & 0x0F]);
          buff.append(hex[bt & 0x0F]);
        }
      }
      return buff.toString();
    }
    catch ( UnsupportedEncodingException ex )
    {
      throw new UnhandledException(ex);
    }
  }
}
