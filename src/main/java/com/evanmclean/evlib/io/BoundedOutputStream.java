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
import java.io.OutputStream;
import java.util.Arrays;

/**
 * <p>
 * Output stream wrapper that limits and optionally enforces the number of bytes
 * written. <strong>BE CAREFUL.</strong> The right combination of parameters
 * could lead to a <em>lot</em> of padding bytes being written.
 * </p>
 * 
 * <p>
 * May throw an {@link OverrunIOException} during a write operation if the total
 * number of bytes written exceeds the limit.
 * </p>
 * 
 * <p>
 * May throw an {@link UnderrunIOException} during a call to {@link #pad()} or
 * {@link #close()} if the total number of bytes written is less than the limit.
 * </p>
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class BoundedOutputStream extends OutputStream
{
  protected final OutputStream out;
  private final boolean discardOverrun;
  private final boolean ignoreUnderrun;
  private long remaining;
  private final Byte pad;

  /**
   * Write at most <code>max</code> bytes to the output stream. Will throw an
   * exception on {@link #close()} or {@link #pad()} if exactly <code>max</code>
   * bytes have not been written. Will throw an exception if you try and write
   * more than <code>max</code> bytes to the output stream.
   * 
   * @param out
   *        The output stream to write.
   * @param max
   *        The maximum number of bytes to write. If less than zero than no
   *        padding or limit enforcement is made.
   */
  public BoundedOutputStream( final OutputStream out, final long max )
  {
    this(out, max, null, false, false);
  }

  /**
   * Write at most <code>max</code> bytes to the output stream.
   * 
   * @param out
   *        The output stream to write.
   * @param max
   *        The maximum number of bytes to write. If less than zero than no
   *        padding or limit enforcement is made.
   * @param ignore_underrun
   *        If <code>true</code> we will not throw an exception on
   *        {@link #close()} or {@link #pad()} if at least <code>max</code>
   *        bytes have not been written.
   * @param discard_overrun
   *        If <code>true</code> we will silently throw away any writes that go
   *        over a total of <code>max</code> bytes.
   */
  public BoundedOutputStream( final OutputStream out, final long max,
      final boolean ignore_underrun, final boolean discard_overrun )
  {
    this(out, max, null, ignore_underrun, discard_overrun);
  }

  /**
   * Write at most <code>max</code> bytes to the output stream. Will pad the
   * output stream with the specified byte on {@link #close()} or {@link #pad()}
   * if exactly <code>max</code> bytes have not been written. Will throw an
   * exception if you try and write more than <code>max</code> bytes to the
   * output stream.
   * 
   * @param out
   *        The output stream to write.
   * @param max
   *        The maximum number of bytes to write. If less than zero than no
   *        padding or limit enforcement is made.
   * @param pad
   *        The byte value to pad the output stream with if we ran under.
   */
  public BoundedOutputStream( final OutputStream out, final long max,
      final byte pad )
  {
    this(out, max, Byte.valueOf(pad), false, false);
  }

  /**
   * Write at most <code>max</code> bytes to the output stream. Will pad the
   * output stream with the specified byte on {@link #close()} or {@link #pad()}
   * if exactly <code>max</code> bytes have not been written.
   * 
   * @param out
   *        The output stream to write.
   * @param max
   *        The maximum number of bytes to write. If less than zero than no
   *        padding or limit enforcement is made.
   * @param pad
   *        The byte value to pad the output stream with if we run under.
   * @param discard_overrun
   *        If <code>true</code> we will silently throw away any writes that go
   *        over a total of <code>max</code> bytes.
   */
  public BoundedOutputStream( final OutputStream out, final long max,
      final byte pad, final boolean discard_overrun )
  {
    this(out, max, Byte.valueOf(pad), false, discard_overrun);
  }

  private BoundedOutputStream( final OutputStream out, final long max,
      final Byte pad, final boolean ignore_underrun,
      final boolean discard_overrun )
  {
    this.out = out;
    this.remaining = (max > 0) ? max : Long.MAX_VALUE;
    this.pad = (max > 0) ? pad : null;
    this.discardOverrun = (max > 0) ? discard_overrun : true;
    this.ignoreUnderrun = (max > 0) ? ignore_underrun : true;
  }

  /**
   * Close the output stream after calling {@link #pad()}.
   * 
   * @throws UnderrunIOException
   *         If there were not enough bytes written and a pad character was not
   *         specified in the constructor, or <code>ignore_underrun</code> was
   *         <code>false</code> (the default).
   * @throws IOException
   *         If an IO exception occurred while writing padding characters to the
   *         output stream or in closing the stream.
   */
  @Override
  public void close() throws UnderrunIOException, IOException
  {
    try
    {
      pad();
    }
    finally
    {
      remaining = 0;
      out.close();
    }
  }

  @Override
  public void flush() throws IOException
  {
    out.flush();
  }

  /**
   * Get the number of bytes that we can still write to this stream.
   * 
   * @return The remaining number of bytes that can be written (always greater
   *         or equal to zero).
   */
  public long getRemaining()
  {
    return remaining;
  }

  /**
   * If <code>max</code> bytes have not been written, then pad with the pad
   * byte.
   * 
   * @return The number of bytes written.
   * @throws UnderrunIOException
   *         If there were not enough bytes written and a pad character was not
   *         specified in the constructor, or <code>ignore_underrun</code> was
   *         <code>false</code> (the default).
   * @throws IOException
   *         If an IO exception occurred while writing padding characters to the
   *         output stream.
   */
  public long pad() throws UnderrunIOException, IOException
  {
    if ( remaining <= 0 )
      return 0;
    if ( pad == null )
    {
      if ( ignoreUnderrun )
        return 0;
      throw new UnderrunIOException("Needed to write another " + remaining
          + " bytes to the output stream.");
    }

    return _pad(pad.byteValue());
  }

  /**
   * If <code>max</code> bytes have not been written, then pad with the
   * specified byte (this overrides the <code>ignore_underrun</code> flag).
   * 
   * @param bt
   *        The byte to use for padding.
   * @return The number of bytes written.
   * @throws IOException
   */
  public long pad( final byte bt ) throws IOException
  {
    if ( (remaining <= 0) || ignoreUnderrun )
      return 0;
    return _pad(bt);
  }

  @Override
  public void write( final byte[] arr, final int off, final int len )
    throws IOException
  {
    if ( remaining <= 0 )
    {
      if ( discardOverrun || (len <= 0) )
        return;
      throw new OverrunIOException(
          "Tried to write too many bytes to output stream.");
    }
    int tlen = Math.max(len, 0);
    int wlen = (int) Math.min(tlen, remaining);
    if ( wlen > 0 )
    {
      out.write(arr, off, wlen);
      remaining -= wlen;
    }

    if ( (!discardOverrun) && (wlen < tlen) )
      throw new OverrunIOException(
          "Tried to write too many bytes to output stream.");
  }

  @Override
  public void write( final int bt ) throws IOException
  {
    if ( remaining <= 0 )
    {
      if ( discardOverrun )
        return;
      throw new OverrunIOException(
          "Tried to write too many bytes written to output stream.");
    }
    out.write(bt);
    --remaining;
  }

  private long _pad( final byte bt ) throws IOException
  {
    final byte[] buff = new byte[(int) Math.min(4096, remaining)];
    Arrays.fill(buff, bt);
    long tot = 0;
    while ( remaining > 0 )
    {
      int len = (int) Math.min(buff.length, remaining);
      write(buff, 0, len);
      tot += len;
    }
    out.flush();
    return tot;
  }
}
