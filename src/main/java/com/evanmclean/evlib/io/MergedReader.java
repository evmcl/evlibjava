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

import java.io.IOException;
import java.io.Reader;

/**
 * Read from a set of readers, as if they are all one big reader concatenated
 * together.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class MergedReader extends Reader
{
  private final Reader[] readers;
  private int current;

  public MergedReader( final Reader... readers )
  {
    this.readers = readers;
  }

  @Override
  public void close() throws IOException
  {
    current = readers.length;
    close(0);
  }

  @Override
  public int read() throws IOException
  {
    while ( current < readers.length )
    {
      final int rd = readers[current].read();
      if ( rd != -1 )
        return rd;
      try
      {
        readers[current].close();
      }
      finally
      {
        readers[current] = null;
        ++current;
      }
    }
    return -1;
  }

  @Override
  public int read( final char[] cbuf, final int off, final int len )
    throws IOException
  {
    int offset = off;
    final int endpos = off + len;
    while ( (current < readers.length) && (offset < endpos) )
    {
      final int rd = readers[current].read(cbuf, offset, endpos - offset);
      if ( rd != -1 )
      {
        offset += rd;
      }
      else
      {
        try
        {
          readers[current].close();
        }
        finally
        {
          readers[current] = null;
          ++current;
        }
      }
    }
    final int numread = offset - off;
    if ( numread > 0 )
      return numread;
    if ( current >= readers.length )
      return -1;
    return 0;
  }

  @Override
  public boolean ready() throws IOException
  {
    if ( current < readers.length )
      return readers[current].ready();
    return false;
  }

  private void close( final int xi ) throws IOException
  {
    if ( xi < readers.length )
      try
      {
        if ( readers[xi] != null )
          readers[xi].close();
      }
      finally
      {
        readers[xi] = null;
        close(xi + 1);
      }
  }

}
