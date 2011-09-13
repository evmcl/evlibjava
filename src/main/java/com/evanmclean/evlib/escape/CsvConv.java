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

import com.evanmclean.evlib.lang.Str;

/**
 * <p>
 * The various character converters and stuff for dealing with comma separated
 * (CSV) files.
 * </p>
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
final class CsvConv
{
  static final ExtendedCharConv CSV = new ExtendedCharConv() {
    public String conv( final char ch )
    {
      return (ch == '"') ? "\"\"" : null;
    }

    public int convFrom( final String str )
    {
      final int len = Str.length(str);
      for ( int xi = 0; xi < len; ++xi )
      {
        final char ch = str.charAt(xi);
        switch ( ch )
        {
          case '"':
          case ',':
            return xi;
        }
        if ( (ch <= ' ') || (ch > '~') || Character.isWhitespace(ch) )
          return xi;
      }
      return -1;
    }

    public String finish( final StringBuilder buff, final String str )
    {
      return buff.append('"').toString();
    }

    public StringBuilder initialise( final StringBuilder buff, final String str )
    {
      return buff.append('"');
    }
  };

  private CsvConv()
  {
    // empty
  }
}
