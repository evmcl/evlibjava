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
import java.io.StringReader;

import junit.framework.TestCase;

/**
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class MergedReaderTest extends TestCase
{
  public static final String TEST_ALL_TEST_TYPE = "UNIT";

  private static final String expected = "abc";
  private Reader in = null;

  public void testBulk() throws IOException
  {
    assertEquals(expected, copyBulk(5));
  }

  public void testBulkSmallbuff() throws IOException
  {
    assertEquals(expected, copyBulk(2));
  }

  public void testSingle() throws IOException
  {
    assertEquals(expected, copySingle());
  }

  private String copyBulk( final int bufsize ) throws IOException
  {
    final char[] arr = new char[bufsize];
    final StringBuilder out = new StringBuilder();
    int rd;
    while ( (rd = in.read(arr)) != -1 )
      out.append(arr, 0, rd);
    return out.toString();
  }

  private String copySingle() throws IOException
  {
    final StringBuilder out = new StringBuilder();
    int rd;
    while ( (rd = in.read()) != -1 )
      out.append((char) rd);
    return out.toString();
  }

  @Override
  protected void setUp()
  {
    in = new MergedReader( //
        new StringReader("a") //
        , new StringReader("b") //
        , new StringReader("c") //
    );
  }

  @Override
  protected void tearDown() throws Exception
  {
    in.close();
  }
}
