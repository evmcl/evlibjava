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

import java.util.ArrayList;

/**
 * <p>
 * The various character converters and stuff for dealing with Java and
 * Javascript code.
 * </p>
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
final class JavaConv extends MlConv
{
  static final CharConv JAVA;
  static final CharConv JAVASCRIPT;

  static
  {
    final CharConv unicode_conv = new CharConv() {
      private final char[] hex = "0123456789ABCDEF".toCharArray();

      public String conv( final char ch )
      {
        if ( (ch >= ' ') && (ch <= '~') )
          return null;
        final char[] arr = new char[6];
        int val = ch;
        for ( int xi = 5; xi > 1; --xi )
        {
          arr[xi] = hex[val & 0xF];
          val = (val >> 4) & 0xFFF;
        }
        arr[0] = '\\';
        arr[1] = 'u';
        return new String(arr);
      }
    };

    final ArrayList<CharSub> subs = new ArrayList<CharSub>(5);
    subs.add(new CharSub('\b', "\\b"));
    subs.add(new CharSub('\n', "\\n"));
    subs.add(new CharSub('\t', "\\t"));
    subs.add(new CharSub('\r', "\\r"));
    subs.add(new CharSub('\f', "\\f"));
    subs.add(new CharSub('"', "\\\""));
    subs.add(new CharSub('\\', "\\\\"));

    JAVA = new AggregateCharConv(new CharSubConv(subs), unicode_conv);

    subs.add(new CharSub('\'', "\\'"));

    JAVASCRIPT = new AggregateCharConv(new CharSubConv(subs), unicode_conv);
  }

  private JavaConv()
  {
    // empty
  }
}
