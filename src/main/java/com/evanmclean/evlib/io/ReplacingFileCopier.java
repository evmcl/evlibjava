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
package com.evanmclean.evlib.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Copies files, replacing tokens like &quot;%token%&quot; with the substitute
 * value. The token must start and end with the delimiter character (percent by
 * default).
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class ReplacingFileCopier
{
  private char delimiter;
  private boolean noMatchError = true;
  private final HashMap<String, String> replacements = new HashMap<String, String>();

  public ReplacingFileCopier()
  {
    delimiter = '%';
  }

  public ReplacingFileCopier( final char delimiter )
  {
    this.delimiter = delimiter;
  }

  public void clear()
  {
    replacements.clear();
  }

  public boolean copy( final File from, final File to )
  {
    try
    {
      final Writer out = new BufferedWriter(new FileWriter(to));
      try
      {
        copy(from, out);
      }
      finally
      {
        out.close();
      }
      return true;
    }
    catch ( IOException ex )
    {
      errorHandler(from, to, ex);
      return false;
    }
  }

  public void copy( final File from, final PrintWriter out ) throws IOException
  {
    final BufferedReader in = new BufferedReader(new FileReader(from));
    try
    {
      String line;
      while ( (line = in.readLine()) != null )
      {
        line = process(line);
        out.println(line);
      }
    }
    finally
    {
      in.close();
    }
  }

  public void copy( final File from, final Writer out ) throws IOException
  {
    final PrintWriter pout = new PrintWriter(out);
    try
    {
      copy(from, pout);
    }
    finally
    {
      pout.flush();
    }
  }

  public Set<Map.Entry<String, String>> entrySet()
  {
    return replacements.entrySet();
  }

  /**
   * Can be overridden to handle errors.
   * 
   * @param from
   * @param to
   * @param exception
   */
  public void errorHandler( @SuppressWarnings( "unused" ) final File from,
      @SuppressWarnings( "unused" ) final File to,
      @SuppressWarnings( "unused" ) final IOException exception )
  {
    // empty
  }

  public char getDelimiter()
  {
    return delimiter;
  }

  public boolean isEmpty()
  {
    return replacements.isEmpty();
  }

  public boolean isNoMatchError()
  {
    return noMatchError;
  }

  public String put( final String key, final String value )
  {
    return replacements.put(key, value);
  }

  public void putAll( final Map<? extends String, ? extends String> map )
  {
    replacements.putAll(map);
  }

  public String remove( final String key )
  {
    return replacements.remove(key);
  }

  public void setDelimiter( char delim )
  {
    delimiter = delim;
  }

  public void setNoMatchError( boolean no_match_error )
  {
    noMatchError = no_match_error;
  }

  public int size()
  {
    return replacements.size();
  }

  private String process( final String line ) throws IOException
  {
    int pos = line.indexOf(delimiter);
    if ( pos < 0 )
      return line;

    final StringBuilder buff = new StringBuilder(line.length() + 50);
    int lastpos = 0;
    while ( pos >= 0 )
    {
      // Copy standard portion of the line across.
      if ( lastpos < pos )
        buff.append(line, lastpos, pos);

      final int endpos = line.indexOf(delimiter, pos + 1);
      if ( endpos <= pos )
      {
        // No end delimiter, just copy the rest of the line
        // including the delimiter.
        buff.append(line, pos, line.length());
        pos = -1;
      }
      else if ( endpos == (pos + 1) )
      {
        // Double delimiter character, copy one delimiter
        // to the output and continue.
        buff.append(line, pos, pos + 1);
        lastpos = endpos + 1;
        pos = line.indexOf(delimiter, lastpos);
      }
      else
      {
        // Perform substitution.
        final String key = line.substring(pos + 1, endpos);
        final String value = replacements.get(key);
        if ( value == null )
        {
          if ( noMatchError )
            throw new IOException("Unknown token: " + key);
          buff.append(delimiter);
          buff.append(key);
          buff.append(delimiter);
        }
        else
        {
          buff.append(value);
        }
        lastpos = endpos + 1;
        pos = line.indexOf(delimiter, lastpos);
      }
    }

    if ( lastpos < line.length() )
      buff.append(line, lastpos, line.length());

    return buff.toString();
  }
}
