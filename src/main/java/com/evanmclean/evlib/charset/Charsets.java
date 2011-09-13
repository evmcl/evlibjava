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
package com.evanmclean.evlib.charset;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Map;

import com.evanmclean.evlib.util.TreeMapIgnoreCase;

/**
 * Defines constants for the standard set of {@link Charset}s that are supported
 * by every java platform implementation.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class Charsets
{
  public static final Charset USASCII;
  public static final Charset ISO88591;
  public static final Charset ISOLATIN1;
  public static final Charset UTF8;
  public static final Charset UTF16;
  public static final Charset UTF16BE;
  public static final Charset UTF16LE;

  public static final String USASCII_NAME;
  public static final String ISO88591_NAME;
  public static final String ISOLATIN1_NAME;
  public static final String UTF8_NAME;
  public static final String UTF16_NAME;
  public static final String UTF16BE_NAME;
  public static final String UTF16LE_NAME;

  private static final Map<String, Charset> charsets = new TreeMapIgnoreCase<Charset>();

  static
  {
    USASCII = Charset.forName("US-ASCII");
    USASCII_NAME = USASCII.name();
    ISO88591 = Charset.forName("ISO-8859-1");
    ISO88591_NAME = USASCII.name();
    ISOLATIN1 = ISO88591;
    ISOLATIN1_NAME = ISOLATIN1.name();
    UTF8 = Charset.forName("UTF-8");
    UTF8_NAME = UTF8.name();
    UTF16 = Charset.forName("UTF-16");
    UTF16_NAME = UTF16.name();
    UTF16BE = Charset.forName("UTF-16BE");
    UTF16BE_NAME = UTF16BE.name();
    UTF16LE = Charset.forName("UTF-16LE");
    UTF16LE_NAME = UTF16LE.name();

    charsets.put("US-ASCII", USASCII);
    charsets.put("ISO-8859-1", ISO88591);
    charsets.put("ISO-LATIN-1", ISO88591);
    charsets.put("UTF-8", UTF8);
    charsets.put("UTF-16", UTF16);
    charsets.put("UTF-16BE", UTF16BE);
    charsets.put("UTF-16LE", UTF16LE);

    charsets.put(USASCII_NAME, USASCII);
    charsets.put(ISO88591_NAME, ISO88591);
    charsets.put(UTF8_NAME, UTF8);
    charsets.put(UTF16_NAME, UTF16);
    charsets.put(UTF16BE_NAME, UTF16BE);
    charsets.put(UTF16LE_NAME, UTF16LE);
  }

  /**
   * Returns a charset object for the named charset.
   * 
   * @param name
   * @return The matching charset.
   * @throws IllegalCharsetNameException
   *         If the given charset name is illegal .
   * @throws IllegalArgumentException
   *         If the given charsetName is null.
   * @throws UnsupportedCharsetException
   *         If no support for the named charset is available in this instance
   *         of the Java virtual machine.
   */
  public static Charset get( final String name )
  {
    Charset cs = charsets.get(name);
    if ( cs == null )
      cs = Charset.forName(name);
    return cs;
  }

  private Charsets()
  {
    // empty
  }
}
