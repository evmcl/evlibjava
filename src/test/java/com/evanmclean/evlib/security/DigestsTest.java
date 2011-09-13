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
package com.evanmclean.evlib.security;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import junit.framework.TestCase;

import com.evanmclean.evlib.io.Folders;

/**
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class DigestsTest extends TestCase
{
  public static final String TEST_ALL_TEST_TYPE = "UNIT";

  private File tempdir;

  public void testMd5BigFile() throws IOException
  {
    assertEquals("8e2b75d8b4655bd7bc05a03c013fd601", Digests.md5(makeBigFile()));
  }

  public void testMd5EmptyFile() throws IOException
  {
    assertEquals("d41d8cd98f00b204e9800998ecf8427e",
      Digests.md5(makeEmptyFile()));
  }

  public void testMd5SmallFile() throws IOException
  {
    assertEquals("f0ef7081e1539ac00ef5b761b4fb01b3",
      Digests.md5(makeSmallFile()));
  }

  @Override
  protected void setUp() throws Exception
  {
    final File dir = File.createTempFile("evlib", "test");
    Folders.del(dir);
    Folders.mks(dir);
    tempdir = dir;
  }

  @Override
  protected void tearDown() throws Exception
  {
    Folders.del(tempdir);
  }

  private File makeBigFile() throws IOException
  {
    final File file = new File(tempdir, "big.txt");
    final OutputStream out = new BufferedOutputStream(
        new FileOutputStream(file));
    try
    {
      final byte[] bytes = "Hello world\n".getBytes("UTF8");
      final int max = ((2 * 1024 * 1024) / bytes.length) + 1;
      for ( int xi = 0; xi < max; ++xi )
        out.write(bytes);
    }
    finally
    {
      out.close();
    }
    return file;
  }

  private File makeEmptyFile() throws IOException
  {
    final File file = new File(tempdir, "empty.txt");
    final OutputStream out = new FileOutputStream(file);
    out.close();
    return file;
  }

  private File makeSmallFile() throws IOException
  {
    final File file = new File(tempdir, "small.txt");
    final OutputStream out = new BufferedOutputStream(
        new FileOutputStream(file));
    try
    {
      final byte[] bytes = "Hello world\n".getBytes("UTF8");
      out.write(bytes);
    }
    finally
    {
      out.close();
    }
    return file;
  }
}
