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
package com.evanmclean.evlib.lang;

import java.io.File;
import java.io.IOException;

/**
 * Misc functions for System related info.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class Sys
{
  private static File tmpFolder;
  private static File userHome;
  private static File currentFolder;

  /**
   * Return the current working folder from the system properties.
   * 
   * @return Return the current working folder from the system properties.
   * @throws IOException
   *         If missing property, missing folder or not a folder.
   */
  public static File currentFolder() throws IOException
  {
    File fldr = currentFolder;
    if ( fldr == null )
      currentFolder = fldr = getFolder("user.dir", true);
    return fldr;
  }

  /**
   * Return the temporary folder from the system properties.
   * 
   * @return Return the temporary folder from the system properties.
   * @throws IOException
   *         If missing property, missing folder or not a folder.
   */
  public static File tmpFolder() throws IOException
  {
    File fldr = tmpFolder;
    if ( fldr == null )
      tmpFolder = fldr = getFolder("java.io.tmpdir", true);
    return fldr;
  }

  /**
   * Return the user's home folder from the system properties.
   * 
   * @return Return the user's home folder from the system properties.
   * @throws IOException
   *         If missing property, missing folder or not a folder.
   */
  public static File userHome() throws IOException
  {
    File fldr = userHome;
    if ( fldr == null )
      userHome = fldr = getFolder("user.home", true);
    return fldr;
  }

  private static File getFolder( final String property, final boolean must_exist )
    throws IOException
  {
    final String val = System.getProperty(property);
    if ( (val == null) || (val.length() <= 0) )
      throw new IOException(property + " system property is not set.");
    final File fldr = new File(val).getCanonicalFile();
    if ( must_exist )
    {
      if ( !fldr.exists() )
        throw new IOException("Folder does not exist: " + fldr);
      if ( !fldr.isDirectory() )
        throw new IOException("Folder expected: " + fldr);
    }
    return fldr;
  }
}
