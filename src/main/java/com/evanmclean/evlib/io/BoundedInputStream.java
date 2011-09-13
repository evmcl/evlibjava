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
 * Input stream wrapper which limits and optionally enforces the number of bytes
 * read from an input stream. Particularly useful for reading from HTTP
 * connections which can get out of whack if you don't read all the bytes, or
 * try and read too many bytes from them. {@link #mark(int)} and
 * {@link #reset()} will work if the underlying stream supports it.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class BoundedInputStream extends InputStream
{
  protected final InputStream in;
  private final boolean eat;
  private long remaining;
  private long marked = -1L;

  /**
   * <p>
   * Only read a maximum number of characters from the input stream. If
   * <code>max</code> is negative then it is effectively unbounded (actually
   * sets the internal remaining bytes counter to Long.MAX_VALUE).
   * </p>
   * 
   * <p>
   * A call to {@link #close()} will call {@link #eat()} before closing the
   * stream to read any remaining bytes.
   * </p>
   * 
   * @param in
   *        The input stream to read.
   * @param max
   *        The maximum number of bytes to read from the input stream.
   */
  public BoundedInputStream( final InputStream in, final long max )
  {
    this(in, max, true);
  }

  /**
   * Only read a maximum number of characters from the input stream. If
   * <code>max</code> is negative then it is effectively unbounded (actually
   * sets the internal remaining bytes counter to Long.MAX_VALUE).
   * 
   * @param in
   *        The input stream to read.
   * @param max
   *        The maximum number of bytes to read from the input stream.
   * @param eat
   *        If true, an implicit call to {@link #eat()} is made on a call to
   *        {@link #close()} to read any remaining bytes.
   */
  public BoundedInputStream( final InputStream in, final long max,
      final boolean eat )
  {
    this.in = in;
    this.remaining = (max >= 0) ? max : Long.MAX_VALUE;
    this.eat = (max >= 0) ? eat : false;
  }

  @Override
  public int available() throws IOException
  {
    return (int) Math.min(in.available(), remaining);
  }

  @Override
  public void close() throws IOException
  {
    try
    {
      if ( eat )
        eat();
    }
    finally
    {
      remaining = 0;
      in.close();
    }
  }

  /**
   * Read and discard any remaining bytes in the input stream, or until we reach
   * the end of the file, whichever comes first.
   * 
   * @return The number of bytes discarded.
   * @throws IOException
   */
  public long eat() throws IOException
  {
    if ( remaining <= 0 )
      return 0;

    final byte[] buff = new byte[(int) Math.min(4096, remaining)];
    long tot = 0;
    while ( remaining > 0 )
    {
      int len = read(buff, 0, buff.length);
      if ( len > 0 )
        tot += len;
    }
    return tot;
  }

  /**
   * The remaining maximum number of bytes we can read.
   * 
   * @return The remaining maximum number of bytes we can read.
   */
  public long getRemaining()
  {
    return remaining;
  }

  @Override
  public synchronized void mark( final int read_limit )
  {
    if ( in.markSupported() )
    {
      in.mark(read_limit);
      marked = remaining;
    }
  }

  @Override
  public boolean markSupported()
  {
    return in.markSupported();
  }

  @Override
  public int read() throws IOException
  {
    if ( remaining <= 0 )
      return -1;
    int ret = in.read();
    if ( ret <= -1 )
      remaining = 0;
    else
      --remaining;
    return ret;
  }

  @Override
  public int read( final byte[] arr, final int off, final int len )
    throws IOException
  {
    if ( remaining <= 0 )
      return -1;
    final int ret = in.read(arr, off, (int) Math.min(Math.max(len, 0),
      remaining));
    if ( ret <= -1 )
      remaining = 0;
    else
      remaining -= ret;
    return ret;
  }

  @Override
  public synchronized void reset() throws IOException
  {
    in.reset();
    if ( marked >= 0 )
    {
      remaining = marked;
      marked = -1;
    }
  }

  @Override
  public long skip( final long num ) throws IOException
  {
    if ( remaining <= 0 )
      return 0;
    long skipped = in.skip(Math.min(num, remaining));
    if ( skipped > 0 )
      remaining -= skipped;
    return skipped;
  }
}
