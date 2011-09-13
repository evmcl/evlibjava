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

/**
 * Delete entire contents of a folder, with optional error handling.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class RecursiveDelete
{
  private boolean continueOnError = true;

  /**
   * Instantiates a recursive deleter that will continue even if it has an error
   * during file deletion.
   */
  public RecursiveDelete()
  {
    // empty
  }

  /**
   * Instantiates a recursive deleter.
   * 
   * @param continue_on_error
   *        Indicates if we continue despite any errors that occurred during
   *        file deletion.
   */
  public RecursiveDelete( final boolean continue_on_error )
  {
    continueOnError = continue_on_error;
  }

  /**
   * Deletes the specified file/directory, plus any sub-directories.
   * 
   * @param base
   * @return False if any errors.
   */
  public boolean deleteAll( final File base )
  {
    if ( !base.exists() )
      return true;
    if ( base.isFile() )
    {
      final boolean ret = Files.delhard(base);
      if ( !ret )
        errorHandler(base);
      return ret;
    }
    boolean ret = true;
    final File[] files = base.listFiles();
    for ( File subfile : files )
    {
      if ( !deleteAll(subfile) )
      {
        ret = false;
        if ( !continueOnError )
          return false;
      }
    }
    if ( !Files.delhard(base) )
    {
      errorHandler(base);
      ret = false;
    }
    return ret;
  }

  /**
   * Perform recursive delete of all children of the specified directory.
   * 
   * @param base
   * @return False if there was an error deleting any file at all.
   */
  public boolean deleteChildren( final File base )
  {
    if ( !base.exists() )
      return true;
    if ( !base.isDirectory() )
      return true;

    boolean ret = true;
    final File[] files = base.listFiles();
    for ( File subfile : files )
    {
      if ( !deleteAll(subfile) )
      {
        ret = false;
        if ( !continueOnError )
          return false;
      }
    }

    return ret;
  }

  /**
   * Indicates if we continue despite any errors that occurred during file
   * copying. The default to true.
   * 
   * @return The current setting.
   */
  public boolean isContinueOnError()
  {
    return continueOnError;
  }

  /**
   * Indicates if we continue despite any errors that occurred during file
   * copying. The default to true.
   * 
   * @param continue_on_error
   */
  public void setContinueOnError( final boolean continue_on_error )
  {
    continueOnError = continue_on_error;
  }

  /**
   * Can be overridden to handle errors.
   * 
   * @param base
   *        The file that caused the error.
   */
  protected void errorHandler( @SuppressWarnings( "unused" ) final File base )
  {
    // empty
  }
}
