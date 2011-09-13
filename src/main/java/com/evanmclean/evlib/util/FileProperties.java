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
package com.evanmclean.evlib.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Load properties object from a file.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class FileProperties
{
  /**
   * Create a new properties object which has been loaded from the specified
   * file.
   * 
   * @param file
   * @return The newly created properties file.
   * @throws IOException
   */
  public static Properties load( final File file ) throws IOException
  {
    final Properties props = new Properties();
    load(props, file);
    return props;
  }

  /**
   * Load the properties from the specified file.
   * 
   * @param props
   * @param file
   * @throws IOException
   */
  public static void load( final Properties props, final File file )
    throws IOException
  {
    final InputStream in = new BufferedInputStream(new FileInputStream(file));
    try
    {
      props.load(in);
    }
    finally
    {
      in.close();
    }
  }

  /**
   * Load the properties from the specified file.
   * 
   * @param props
   * @param file_name
   * @throws IOException
   */
  public static void load( final Properties props, final String file_name )
    throws IOException
  {
    load(props, new File(file_name));
  }

  /**
   * Create a new properties object which has been loaded from the specified
   * file.
   * 
   * @param file_name
   * @return The newly created properties file.
   * @throws IOException
   */
  public static Properties load( final String file_name ) throws IOException
  {
    final Properties props = new Properties();
    load(props, file_name);
    return props;
  }

  private FileProperties()
  {
    // empty
  }
}
