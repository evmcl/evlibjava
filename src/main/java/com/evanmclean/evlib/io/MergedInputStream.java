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
import java.io.InputStream;

/**
 * Read from a set of input streams, as if they are all one big input stream
 * concatenated together.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class MergedInputStream extends InputStream
{
  private final InputStream[] ins;
  private int current = 0;

  public MergedInputStream( final InputStream... ins )
  {
    this.ins = ins;
  }

  @Override
  public int available() throws IOException
  {
    if ( current < ins.length )
      return ins[current].available();
    return 0;
  }

  @Override
  public void close() throws IOException
  {
    current = ins.length;
    close(0);
  }

  @Override
  public int read() throws IOException
  {
    while ( current < ins.length )
    {
      int rd = ins[current].read();
      if ( rd != -1 )
        return rd;
      try
      {
        ins[current].close();
      }
      finally
      {
        ins[current] = null;
        ++current;
      }
    }
    return -1;
  }

  @Override
  public int read( final byte[] buf, final int off, final int len )
    throws IOException
  {
    int offset = off;
    final int endpos = off + len;
    while ( (current < ins.length) && (offset < endpos) )
    {
      final int rd = ins[current].read(buf, offset, endpos - offset);
      if ( rd != -1 )
      {
        offset += rd;
      }
      else
      {
        try
        {
          ins[current].close();
        }
        finally
        {
          ins[current] = null;
          ++current;
        }
      }
    }
    final int numread = offset - off;
    if ( numread > 0 )
      return numread;
    if ( current >= ins.length )
      return -1;
    return 0;
  }

  private void close( final int xi ) throws IOException
  {
    if ( xi < ins.length )
      try
      {
        if ( ins[xi] != null )
          ins[xi].close();
      }
      finally
      {
        ins[xi] = null;
        close(xi + 1);
      }
  }
}
