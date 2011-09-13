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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;

import com.evanmclean.evlib.exceptions.UnhandledException;

/**
 * Perform various file related operations. Also see {@link Folders}.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class Files
{
  /**
   * <p>
   * Efficient file copy. The last modified date is preserved.
   * </p>
   * 
   * <p>
   * Uses the java.nio.channels logic to efficiently copy a file without having
   * to create ancillary temporary buffers and such.
   * </p>
   * 
   * @param from
   *        Source file to copy.
   * @param to
   *        Destination file that will be overwitten.
   * @throws IOException
   */
  public static void copy( final File from, final File to ) throws IOException
  {
    copy(from, to, true);
  }

  /**
   * <p>
   * Efficient file copy.
   * </p>
   * 
   * <p>
   * Uses the java.nio.channels logic to efficiently copy a file without having
   * to create ancillary temporary buffers and such.
   * </p>
   * 
   * @param from
   *        Source file to copy.
   * @param to
   *        Destination file that will be overwritten.
   * @param preserve_date
   *        If true then the last modified date is preserved.
   * @throws IOException
   */
  public static void copy( final File from, final File to,
      final boolean preserve_date ) throws IOException
  {
    final FileChannel fromc = new FileInputStream(from).getChannel();
    try
    {
      final FileChannel toc = new FileOutputStream(to).getChannel();
      try
      {
        final long size = fromc.size();
        long position = 0;
        int zeros_looped = 0;
        while ( position < size )
        {
          final long read = toc.transferFrom(fromc, position, size - position);
          if ( read > 0 )
          {
            position += read;
            zeros_looped = 0;
          }
          else
          {
            ++zeros_looped;
            if ( zeros_looped > 10 )
              throw new IOException("Error while reading file: " + from);
          }
        }
      }
      finally
      {
        toc.close();
      }
    }
    finally
    {
      fromc.close();
    }

    if ( preserve_date )
    {
      final long ts = from.lastModified();
      if ( ts != 0L )
        to.setLastModified(ts);
    }
  }

  /**
   * Deletes the file if it exists. Will also delete a folder if it is empty.
   * See {@link Folders#del(File)} to delete a folder and its contents. This is
   * basically the same as {@link #delhard(File)} but throws an exception on
   * error instead of returning <code>false</code>.
   * 
   * @param path
   *        The file to delete.
   * @throws IOException
   *         If unable to delete the file.
   */
  public static void del( final File path ) throws IOException
  {
    if ( !delhard(path) )
      throw new IOException("Unable to delete " + path.toString());
  }

  /**
   * Deletes the file if it exists. Will also delete a folder if it is empty.
   * See {@link Folders#del(File)} to delete a folder and its contents. This is
   * basically the same as {@link #delhard(File)} but throws an exception on
   * error instead of returning <code>false</code>.
   * 
   * @param path
   *        The file to delete.
   * @param quietly
   *        Only through an IO exception if <code>quietly</code> is
   *        <code>false</code>.
   * @return True if the file was deleted.
   * @throws IOException
   *         If unable to delete the file (and <code>quietly</code> is
   *         <code>false</code>).
   */
  public static boolean del( final File path, final boolean quietly )
    throws IOException
  {
    if ( !delhard(path) )
    {
      if ( quietly )
        return false;
      throw new IOException("Unable to delete " + path.toString());
    }
    return true;
  }

  /**
   * Deletes the file if it exists, trying to make the file and/or the parent
   * folder writable in order to do so. Will also delete a folder if it is
   * empty. See {@link Folders#del(File)} to delete a folder and its contents.
   * 
   * @param path
   *        The file to delete.
   * @return True if file was deleted.
   */
  public static boolean delhard( final File path )
  {
    if ( path.delete() || (!path.exists()) )
      return true;

    if ( path.isDirectory() )
      try
      {
        // Cannot delete a folder with something in it.
        if ( Folders.isNotEmpty(path) )
          return false;
      }
      catch ( IOException ex )
      {
        // ignore.
      }

    try
    {
      // If running java 1.6, then try and set the file as writable.
      final Method setWritable = File.class.getMethod("setWritable",
        boolean.class);

      setWritable.invoke(path, Boolean.TRUE);
      if ( path.delete() || (!path.exists()) )
        return true;

      // If that don't work, try setting the parent writable.
      final File parent = path.getParentFile();
      if ( parent != null )
      {
        setWritable.invoke(parent, Boolean.TRUE);
        if ( path.delete() || (!path.exists()) )
          return true;

        // Now that the folder is writable, give the file one more go.
        setWritable.invoke(path, Boolean.TRUE);
        if ( path.delete() || (!path.exists()) )
          return true;
      }
    }
    catch ( Exception ex )
    {
      // Fall through.
    }
    return !path.exists();
  }

  /**
   * Returns the canonical form of an abstract pathname. Basically a wrapper
   * around {@link File#getCanonicalFile()} which will throw a runtime exception
   * on error instead of an IOException.
   * 
   * @param path
   * @return The canonical form of the abstract pathname. If <code>path</code>
   *         is <code>null</code> then <code>null</code> is returned.
   * @throws UnhandledException
   *         Runtime wrapper around the IOException if it occurred.
   */
  public static File getCanonicalFile( final File path )
  {
    if ( path == null )
      return null;
    try
    {
      return path.getCanonicalFile();
    }
    catch ( IOException ex )
    {
      throw new UnhandledException(ex);
    }
  }

  /**
   * Returns the canonical pathname string of an abstract pathname. Basically a
   * wrapper around {@link File#getCanonicalPath()} which will throw a runtime
   * exception on error instead of an IOException.
   * 
   * @param path
   * @return The canonical pathname. If <code>path</code> is <code>null</code>
   *         then <code>null</code> is returned.
   * @throws UnhandledException
   *         Runtime wrapper around the IOException if it occurred.
   */
  public static String getCanonicalPath( final File path )
  {
    if ( path == null )
      return null;
    try
    {
      return path.getCanonicalPath();
    }
    catch ( IOException ex )
    {
      throw new UnhandledException(ex);
    }
  }

  /**
   * Checks the path specified is a zero length file, an empty folder or does
   * not exist.
   * 
   * @param path
   *        The path the check.
   * @return True if a zero length file, an empty folder or does not exist.
   * @throws IOException
   *         If <code>folder</code> does not represent a file or a folder.
   */
  public static boolean isEmpty( final File path ) throws IOException
  {
    if ( path.isFile() )
      return path.length() <= 0;
    return Folders.isEmpty(path);
  }

  /**
   * Checks the path specified is a zero length file, an empty folder or does
   * not exist.
   * 
   * @param path
   *        The path the check.
   * @return False if a zero length file, an empty folder or does not exist.
   * @throws IOException
   *         If <code>folder</code> does not represent a file or a folder.
   */
  public static boolean isNotEmpty( final File path ) throws IOException
  {
    if ( path.isFile() )
      return path.length() > 0;
    return Folders.isNotEmpty(path);
  }

  /**
   * <p>
   * Efficient file move. The last modified date is preserved.
   * </p>
   * 
   * <p>
   * Tries to do a rename, and if that fails calls
   * {@link #copy(File, File, boolean)} then {@link #del(File)} to efficiently
   * and effectively move the file.
   * </p>
   * 
   * @param from
   *        Source file to move.
   * @param to
   *        Destination file that will be overwritten.
   * @throws IOException
   */
  public static void move( final File from, final File to ) throws IOException
  {
    move(from, to, true);
  }

  /**
   * <p>
   * Efficient file move.
   * </p>
   * 
   * <p>
   * Tries to do a rename, and if that fails calls
   * {@link #copy(File, File, boolean)} then {@link #del(File)} to efficiently
   * and effectively move the file.
   * </p>
   * 
   * @param from
   *        Source file to move.
   * @param to
   *        Destination file that will be overwritten.
   * @param preserve_date
   *        If true then the last modified date is preserved.
   * @throws IOException
   */
  public static void move( final File from, final File to,
      final boolean preserve_date ) throws IOException
  {
    if ( to.exists() )
    {
      if ( !to.isFile() )
        throw new IOException("Target already exists and is not a file: "
            + to.toString());
      delhard(to);
    }

    if ( from.renameTo(to) )
      return;
    copy(from, to, preserve_date);
    del(from);
  }

  /**
   * Renames a file or folder. This is basically the same as
   * {@link File#renameTo(File)} but throws an exception on error instead of
   * returning <code>false</code>.
   * 
   * @param from
   *        The file or folder to rename.
   * @param to
   *        What we are renaming the file or folder to.
   * @throws IOException
   *         If unable to rename the file or folder.
   */
  public static void rename( final File from, final File to )
    throws IOException
  {
    if ( !from.renameTo(to) )
      throw new IOException("Unable to rename " + from.toString() + " to "
          + to.toString());
  }

  private Files()
  {
    // empty
  }
}
