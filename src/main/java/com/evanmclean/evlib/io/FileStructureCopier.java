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
import java.io.IOException;

/**
 * Recursively copies a set of files. Can implement your own error handling.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class FileStructureCopier
{
  public FileStructureCopier()
  {
    // empty
  }

  /**
   * Perform recursive copying.
   * 
   * @param from
   * @param to
   * @return False if there was an error copying any file at all.
   * @throws IOException
   *         Other errors.
   */
  public final boolean copy( final File from, final File to )
    throws IOException
  {
    if ( !from.isDirectory() )
      return simpleFileCopy(from, to);

    if ( !filter(from, to) )
      return true;

    if ( to.exists() )
    {
      if ( !to.isDirectory() )
      {
        errorHandler(from, to, new IOException("Not a directory: "
            + to.toString()));
        return false;
      }
    }
    else if ( !to.mkdirs() )
    {
      errorHandler(from, to, new IOException("Could not create directory: "
          + to.toString()));
      return false;
    }

    postCopy(from, to);

    boolean okay = true;
    final String from_dir_path = from.getCanonicalPath();
    final int from_dir_path_len = from_dir_path.length();
    final String to_dir_path = to.getCanonicalPath();

    final File[] files = from.listFiles();
    for ( File ff : files )
    {
      final File from_file = ff.getCanonicalFile();
      final String from_path = from_file.toString();
      final String to_path = to_dir_path
          + from_path.substring(from_dir_path_len);
      final File to_file = new File(to_path);

      if ( from_file.isDirectory() )
      {
        if ( !copy(from_file, to_file) )
          okay = false;
      }
      else
      {
        if ( !simpleFileCopy(from_file, to_file) )
          okay = false;
      }
    }

    return okay;
  }

  /**
   * Performs simple, standard copy of file.
   * 
   * @param from
   * @param to
   * @return False if any errors.
   */
  private boolean simpleFileCopy( final File from, final File to )
    throws IOException
  {
    if ( !filter(from, to) )
      return true;

    try
    {
      Files.copy(from, to);
      postCopy(from, to);
      return true;
    }
    catch ( IOException ex )
    {
      errorHandler(from, to, ex);
      return false;
    }
  }

  /**
   * By default, throws the IOException that was passed in to it. Can be
   * overridden to perform other behaviour.
   * 
   * @param from
   * @param to
   * @param exception
   * @throws IOException
   */
  protected void errorHandler( @SuppressWarnings( "unused" ) final File from,
      @SuppressWarnings( "unused" ) final File to, final IOException exception )
    throws IOException
  {
    throw exception;
  }

  /**
   * Can be overridden to filter which files and folders to copy.
   * 
   * @param from
   * @param to
   * @return Return true if we should copy this file or folder.
   */
  protected boolean filter( @SuppressWarnings( "unused" ) final File from,
      @SuppressWarnings( "unused" ) final File to )
  {
    return true;
  }

  /**
   * Can be overridden to perform processing once a file or folder has been
   * successfully copied.
   * 
   * @param from
   * @param to
   */
  protected void postCopy( @SuppressWarnings( "unused" ) final File from,
      @SuppressWarnings( "unused" ) final File to )
  {
    // empty
  }
}
