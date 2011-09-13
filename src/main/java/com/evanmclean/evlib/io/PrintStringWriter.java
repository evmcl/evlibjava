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

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Combines a PrintWriter with a StringWriter.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class PrintStringWriter extends PrintWriter
{
  private final StringWriter sout;

  /**
   * Create a new print string writer using the default initial string-buffer
   * size.
   */
  public PrintStringWriter()
  {
    super(new StringWriter());
    sout = (StringWriter) out;
  }

  /**
   * Create a new print string writer using the specified initial string-buffer
   * size.
   * 
   * @param initialSize
   *        The number of <code>char</code> values that will fit into this
   *        buffer before it is automatically expanded.
   */
  public PrintStringWriter( final int initialSize )
  {
    super(new StringWriter(initialSize));
    sout = (StringWriter) out;
  }

  /**
   * Return the string buffer itself.
   * 
   * @return StringBuffer holding the current buffer value.
   */
  public StringBuffer getBuffer()
  {
    flush();
    return sout.getBuffer();
  }

  /**
   * Return the buffer's current value as a string.
   */
  @Override
  public String toString()
  {
    flush();
    return sout.toString();
  }
}
