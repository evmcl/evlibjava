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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

/**
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class MergedInputStreamTest extends TestCase
{
  public static final String TEST_ALL_TEST_TYPE = "UNIT";

  private static final byte[] expected;
  static
  {
    expected = new byte[3];
    expected[0] = 0;
    expected[1] = 1;
    expected[2] = 2;
  }

  private InputStream in = null;

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

  @Override
  protected void setUp()
  {
    in = new MergedInputStream( //
        makeIn((byte) 0), makeIn((byte) 1), makeIn((byte) 2) //
    );
  }

  @Override
  protected void tearDown() throws Exception
  {
    in.close();
  }

  private void assertEquals( final byte[] lhs, final byte[] rhs )
  {
    if ( lhs == null )
    {
      if ( rhs != null )
        fail();
    }
    else if ( rhs == null )
    {
      fail();
    }
    else if ( lhs.length != rhs.length )
    {
      fail();
    }
    else
    {
      for ( int xi = 0; xi < lhs.length; ++xi )
        if ( lhs[xi] != rhs[xi] )
        {
          fail();
          break;
        }
    }
  }

  private byte[] copyBulk( final int bufsize ) throws IOException
  {
    final byte[] arr = new byte[bufsize];
    final ByteArrayOutputStream out = new ByteArrayOutputStream(3);
    int rd;
    while ( (rd = in.read(arr)) != -1 )
      out.write(arr, 0, rd);
    return out.toByteArray();
  }

  private byte[] copySingle() throws IOException
  {
    final ByteArrayOutputStream out = new ByteArrayOutputStream(3);
    int rd;
    while ( (rd = in.read()) != -1 )
      out.write(rd);
    return out.toByteArray();
  }

  private InputStream makeIn( final byte val )
  {
    final byte[] buff = new byte[1];
    buff[0] = val;
    return new ByteArrayInputStream(buff);
  }
}
