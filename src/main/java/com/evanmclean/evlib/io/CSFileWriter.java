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

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * Create a file writer which writes the file with the specified character set.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class CSFileWriter extends OutputStreamWriter
{
  public CSFileWriter( final File file, final boolean append, final Charset cs )
    throws FileNotFoundException
  {
    super(new FileOutputStream(file, append), cs);
  }

  public CSFileWriter( final File file, final boolean append,
      final CharsetEncoder enc ) throws FileNotFoundException
  {
    super(new FileOutputStream(file, append), enc);
  }

  public CSFileWriter( final File file, final boolean append,
      final String charset_name )
    throws FileNotFoundException,
      UnsupportedEncodingException
  {
    super(new FileOutputStream(file, append), charset_name);
  }

  public CSFileWriter( final File file, final Charset cs )
    throws FileNotFoundException
  {
    super(new FileOutputStream(file), cs);
  }

  public CSFileWriter( final File file, final CharsetEncoder enc )
    throws FileNotFoundException
  {
    super(new FileOutputStream(file), enc);
  }

  public CSFileWriter( final File file, final String charset_name )
    throws FileNotFoundException,
      UnsupportedEncodingException
  {
    super(new FileOutputStream(file), charset_name);
  }

  public CSFileWriter( final FileDescriptor fd, final Charset cs )
  {
    super(new FileOutputStream(fd), cs);
  }

  public CSFileWriter( final FileDescriptor fd, final CharsetEncoder enc )
  {
    super(new FileOutputStream(fd), enc);
  }

  public CSFileWriter( final FileDescriptor fd, final String charset_name )
    throws UnsupportedEncodingException
  {
    super(new FileOutputStream(fd), charset_name);
  }

  public CSFileWriter( final String name, final boolean append, final Charset cs )
    throws FileNotFoundException
  {
    super(new FileOutputStream(name, append), cs);
  }

  public CSFileWriter( final String name, final boolean append,
      final CharsetEncoder enc ) throws FileNotFoundException
  {
    super(new FileOutputStream(name, append), enc);
  }

  public CSFileWriter( final String name, final boolean append,
      final String charset_name )
    throws FileNotFoundException,
      UnsupportedEncodingException
  {
    super(new FileOutputStream(name, append), charset_name);
  }

  public CSFileWriter( final String name, final Charset cs )
    throws FileNotFoundException
  {
    super(new FileOutputStream(name), cs);
  }

  public CSFileWriter( final String name, final CharsetEncoder enc )
    throws FileNotFoundException
  {
    super(new FileOutputStream(name), enc);
  }

  public CSFileWriter( final String name, final String charset_name )
    throws FileNotFoundException,
      UnsupportedEncodingException
  {
    super(new FileOutputStream(name), charset_name);
  }
}
