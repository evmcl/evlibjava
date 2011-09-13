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
import java.security.SecureRandom;

import com.evanmclean.evlib.lang.Sys;

/**
 * Perform certain folder/directory related operations. Also see {@link Files}.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class Folders
{
  private static class MyRD extends RecursiveDelete
  {
    File errFile;

    MyRD()
    {
      super(true);
    }

    @Override
    protected void errorHandler( final File file )
    {
      if ( errFile == null )
        errFile = file;
    }
  }

  private static volatile SecureRandom random;

  /**
   * Recursively clears a folder of any empty folders and optionally,
   * zero-length files as well.
   * 
   * @param path
   *        The path to clear.
   * @param including_empty_files
   *        Remove zero-length files as well as empty folders.
   * @return True if the specified path ended up being completely removed.
   * @throws IOException
   */
  public static boolean clean( final File path,
      final boolean including_empty_files ) throws IOException
  {
    if ( !path.exists() )
      return true;
    return _clean(path, including_empty_files);
  }

  /**
   * Clear the contents of a folder.
   * 
   * @param folder
   *        The folder to delete.
   * @throws IOException
   *         If unable to fully delete the contents of the folder or the folder
   *         doesn't exist.
   */
  public static void clear( final File folder ) throws IOException
  {
    if ( (!folder.exists()) || (!folder.isDirectory()) )
      throw new IOException(folder.toString());

    final MyRD rd = new MyRD();
    rd.deleteChildren(folder);
    if ( rd.errFile != null )
      throw new IOException("Unable to delete " + rd.errFile.toString());
  }

  /**
   * Will recursively copy a folder to the target path.
   * 
   * @param from
   *        Where to copy from.
   * @param to
   *        Where to copy to. This folder should either not exist or be an empty
   *        folder.
   * @throws IOException
   *         If there was an error.
   */
  public static void copy( final File from, final File to ) throws IOException
  {
    boolean clear_only = false;
    if ( to.exists() )
    {
      if ( from.isDirectory() )
      {
        if ( !to.isDirectory() )
          throw new IOException("Not a folder: " + to.toString());
        if ( isNotEmpty(to) )
          throw new IOException("Folder not empty: " + to.toString());
        clear_only = true;
      }
      else
      {
        if ( to.isDirectory() )
        {
          if ( !Files.delhard(to) ) // Only works if empty
            throw new IOException("Not a file: " + to.toString());
        }
      }
    }

    boolean okay = false;
    try
    {
      final FileStructureCopier fsc = new FileStructureCopier();
      fsc.copy(from, to);
      okay = true;
    }
    finally
    {
      if ( !okay )
      {
        if ( clear_only )
          clear(to);
        else
          del(to);
      }
    }
  }

  /**
   * Creates an new folder in the default temporary-file folder, using the given
   * prefix and suffix to generate its name. Invoking this method is equivalent
   * to invoking <code>{@link #createTempFolder(java.lang.String,
   * java.lang.String, java.io.File)
   * createTempFolder(prefix,&nbsp;suffix,&nbsp;null)}</code>.
   * 
   * @param prefix
   *        The prefix string to be used in generating the folder's name; must
   *        be at least three characters long.
   * 
   * @param suffix
   *        The suffix string to be used in generating the folder's name; may be
   *        <code>null</code>, in which case the suffix <code>".tmp"</code> will
   *        be used.
   * 
   * @return An abstract pathname denoting a newly-created empty folder.
   * 
   * @throws IllegalArgumentException
   *         If the <code>prefix</code> argument contains fewer than three
   *         characters.
   * 
   * @throws IOException
   *         If a folder could not be created.
   */
  public static File createTempFolder( final String prefix, final String suffix )
    throws IOException
  {
    return createTempFolder(prefix, suffix, null);
  }

  /**
   * <p>
   * Creates a new folder in the specified parent folder, using the given prefix
   * and suffix strings to generate its name. If this method returns
   * successfully then it is guaranteed that:
   * </p>
   * 
   * <ol>
   * <li>The folder denoted by the returned abstract pathname did not exist
   * before this method was invoked, and</li>
   * <li>Neither this method nor any of its variants will return the same
   * abstract pathname again in the current invocation of the virtual machine.</li>
   * </ol>
   * 
   * <p>
   * Note: Folders are not automatically deleted on exit or anything. It is up
   * to the application to cleanup as necessary.
   * </p>
   * 
   * <p>
   * The <code>prefix</code> argument must be at least three characters long. It
   * is recommended that the prefix be a short, meaningful string such as
   * <code>"hjb"</code> or <code>"mail"</code>. The <code>suffix</code> argument
   * may be <code>null</code>, in which case the suffix <code>".tmp"</code> will
   * be used.
   * </p>
   * 
   * <p>
   * The name of the new folder will be generated by concatenating the prefix,
   * some internally-generated characters, and the suffix.
   * </p>
   * 
   * <p>
   * If the <code>parent</code> argument is <code>null</code> then the
   * system-dependent default temporary-file folder will be used. The default
   * temporary-file folder is specified by the system property
   * <code>java.io.tmpdir</code>. On UNIX systems the default value of this
   * property is typically <code>"/tmp"</code> or <code>"/var/tmp"</code>; on
   * Microsoft Windows systems it is typically <code>"C:\\WINNT\\TEMP"</code>. A
   * different value may be given to this system property when the Java virtual
   * machine is invoked, but programmatic changes to this property are not
   * guaranteed to have any effect upon the temporary folder used by this
   * method.
   * </p>
   * 
   * @param prefix
   *        The prefix string to be used in generating the folder's name; must
   *        be at least three characters long.
   * @param suffix
   *        The suffix string to be used in generating the folder's name; may be
   *        <code>null</code>, in which case the suffix <code>".tmp"</code> will
   *        be used.
   * @param parent
   *        The parent folder in which the folder is to be created, or
   *        <code>null</code> if the default temporary-file folder is to be
   *        used.
   * @return An abstract pathname denoting a newly-created empty folder.
   * @throws IOException
   *         If a file could not be created.
   * @throws IllegalArgumentException
   *         If the <code>prefix</code> argument contains fewer than three
   *         characters
   */
  public static File createTempFolder( final String prefix,
      final String suffix, final File parent ) throws IOException
  {
    if ( prefix == null )
      throw new NullPointerException();
    if ( prefix.length() < 3 )
      throw new IllegalArgumentException("Prefix string too short");
    final String suff = (suffix == null) ? ".tmp" : suffix;
    final File base = (parent != null) ? parent : Sys.tmpFolder();

    if ( !base.exists() )
      throw new IOException("Base folder does not exist: " + base);
    if ( !base.isDirectory() )
      throw new IOException("Expecting a folder: " + base);

    if ( random == null )
      random = new SecureRandom();

    for ( int xi = 0; xi < 1000; ++xi )
    {
      long val = random.nextLong();
      if ( val == Long.MIN_VALUE )
        val = 0L;
      else
        val = Math.abs(val);
      final File fldr = new File(base, prefix + String.valueOf(val) + suff);
      if ( !fldr.exists() )
        if ( fldr.mkdir() )
          return fldr;
    }

    throw new IOException("Could not create temporary folder in: " + base);
  }

  /**
   * Delete the folder (if it exists) and all its contents. If you only want to
   * delete the folder if it is empty, use {@link Files#del(File)}. Note, if
   * given a file, it will just delete the file, equivalent behaviour to
   * {@link Files#del(File)} in this case.
   * 
   * @param path
   *        The folder to delete.
   * @throws IOException
   *         If unable to fully delete the folder.
   */
  public static void del( final File path ) throws IOException
  {
    final MyRD rd = new MyRD();
    rd.deleteAll(path);
    if ( rd.errFile != null )
      throw new IOException("Unable to delete " + rd.errFile.toString());
  }

  /**
   * Delete the folder (if it exists) and all its contents. If you only want to
   * delete the folder if it is empty, use {@link Files#del(File)}. Note, if
   * given a file, it will just delete the file, equivalent behaviour to
   * {@link Files#del(File)} in this case.
   * 
   * @param path
   *        The folder to delete.
   * @param quietly
   *        Only throw the IO exception if <code>quietly</code> is
   *        <code>false</code>.
   * @return True if successfully deleted, false if failed to delete and
   *         <code>quietly</code> is <code>true</code>.
   * @throws IOException
   *         If unable to fully delete the folder and <code>quietly</code> is
   *         <code>false</code>.
   */
  public static boolean del( final File path, final boolean quietly )
    throws IOException
  {
    final MyRD rd = new MyRD();
    rd.deleteAll(path);
    if ( rd.errFile != null )
    {
      if ( quietly )
        return false;
      throw new IOException("Unable to delete " + rd.errFile.toString());
    }
    return true;
  }

  /**
   * Delete the folder (if it exists) and all its contents. Does not throw an
   * exception on failure.
   * 
   * @param path
   *        The folder to delete.
   * @return True if successfully deleted, false otherwise.
   */
  public static boolean delQuietly( final File path )
  {
    final MyRD rd = new MyRD();
    rd.deleteAll(path);
    return rd.errFile == null;
  }

  /**
   * If the folder already exists, clear its contents, otherwise create it and
   * all necessary parent folders - if the specified path is a file, delete it
   * first then create the folder with the same name.
   * 
   * @param folder
   *        Folder to create or clear.
   * @throws IOException
   *         If it could create or delete whatever it needed to.
   */
  public static void forceMksClear( final File folder ) throws IOException
  {
    if ( !folder.exists() )
    {
      mks(folder);
    }
    else
    {
      if ( folder.isDirectory() )
      {
        clear(folder);
      }
      else
      {
        Files.del(folder);
        mks(folder);
      }
    }
  }

  /**
   * Checks if the path has any non-zero length files, or is just folders and
   * empty files (or doesn't even exist).
   * 
   * @param path
   * @return False if the path exists and contains any non-zero length files.
   */
  public static boolean isAllEmpty( final File path )
  {
    if ( !path.exists() )
      return true;
    if ( !path.isDirectory() )
    {
      if ( path.isFile() )
        return path.length() <= 0;
      return false;
    }
    return _isAllEmpty(path);
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
    final String[] files = path.list();
    if ( files == null )
    {
      if ( path.isFile() )
        return path.length() <= 0;
      if ( !path.exists() )
        return true;
      throw new IOException("Not a folder (or a file): " + path);
    }
    return files.length <= 0;
  }

  /**
   * Checks if the path has any non-zero length files, or is just folders and
   * empty files (or doesn't even exist).
   * 
   * @param path
   * @return True if the path exists and contains any non-zero length files.
   */
  public static boolean isNotAllEmpty( final File path )
  {
    if ( !path.exists() )
      return false;
    if ( !path.isDirectory() )
    {
      if ( path.isFile() )
        return path.length() > 0;
      return true;
    }
    return !_isAllEmpty(path);
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
    final String[] files = path.list();
    if ( files == null )
    {
      if ( path.isFile() )
        return path.length() > 0;
      if ( !path.exists() )
        return false;
      throw new IOException("Not a folder (or a file): " + path);
    }
    return files.length > 0;
  }

  /**
   * Checks if the path contains any files at all, or just zero or more folders
   * and sub-folders.
   * 
   * @param path
   * @return True if the path exists and contains any files.
   */
  public static boolean isNotOnlyFolders( final File path )
  {
    if ( !path.exists() )
      return false;
    if ( !path.isDirectory() )
      return true;
    return !_isOnlyFolders(path);
  }

  /**
   * Checks if the path contains any files at all, or just zero or more folders
   * and sub-folders.
   * 
   * @param path
   * @return False if the path exists and contains any files.
   */
  public static boolean isOnlyFolders( final File path )
  {
    if ( !path.exists() )
      return true;
    if ( !path.isDirectory() )
      return false;
    return _isOnlyFolders(path);
  }

  /**
   * Create the folder if it does not already exist. This is basically the same
   * as {@link File#mkdir()} but throws an exception on error instead of
   * returning <code>false</code>.
   * 
   * @param folder
   *        Folder to create.
   * @throws IOException
   *         If it could not create the folder, or the path already exists and
   *         is not a folder.
   */
  public static void mk( final File folder ) throws IOException
  {
    if ( !folder.mkdir() )
      if ( (!folder.exists()) || (!folder.isDirectory()) )
        throw new IOException(folder.toString());
  }

  /**
   * Create the folder and all necessary parent folders if they do not already
   * exist. This is basically the same as {@link File#mkdirs()} but throws an
   * exception on error instead of returning <code>false</code>.
   * 
   * @param folder
   *        Folder to create.
   * @throws IOException
   *         If it could not create the folder, or the path already exists and
   *         is not a folder.
   */
  public static void mks( final File folder ) throws IOException
  {
    if ( !folder.mkdirs() )
      if ( (!folder.exists()) || (!folder.isDirectory()) )
        throw new IOException(folder.toString());
  }

  /**
   * If the folder already exists, clear its contents, otherwise create it and
   * all necessary parent folders.
   * 
   * @param folder
   *        Folder to create or clear.
   * @throws IOException
   *         If it could create or delete whatever it needed to, or the path
   *         already exists and is not a folder.
   */
  public static void mksClear( final File folder ) throws IOException
  {
    if ( !folder.exists() )
    {
      mks(folder);
    }
    else
    {
      if ( !folder.isDirectory() )
        throw new IOException("Not a folder: " + folder.toString());
      clear(folder);
    }
  }

  /**
   * Will try and rename a folder, and failing that will copy then delete.
   * 
   * @param from
   *        Folder to copy.
   * @param to
   *        Where to copy to. This path must not already exist.
   * @throws IOException
   */
  public static void move( final File from, final File to ) throws IOException
  {
    if ( to.exists() )
      throw new IOException("Target already exists: " + to.toString());
    if ( from.renameTo(to) )
      return;
    boolean okay = false;
    try
    {
      copy(from, to);
      okay = true;
    }
    finally
    {
      if ( !okay )
        del(to);
    }
    del(from);
  }

  private static boolean _clean( final File path,
      final boolean including_empty_files ) throws IOException
  {
    if ( path.isFile() )
    {
      if ( (!including_empty_files) || (path.length() > 0) )
        return false;
      Files.del(path);
      return true;
    }

    if ( !path.isDirectory() )
      return false;

    boolean allempty = true;
    final File[] paths = path.listFiles();
    for ( File subpath : paths )
      if ( !_clean(subpath, including_empty_files) )
        allempty = false;

    if ( allempty )
      Files.delhard(path);

    return allempty;
  }

  private static boolean _isAllEmpty( final File folder )
  {
    boolean has_subfolders = false;
    final File[] paths = folder.listFiles();
    for ( File path : paths )
    {
      if ( path.isDirectory() )
        has_subfolders = true;
      else if ( path.isFile() )
        return path.length() <= 0;
      else
        return false;
    }

    if ( has_subfolders )
      for ( File path : paths )
        if ( path.isDirectory() )
          if ( !_isAllEmpty(path) )
            return false;

    return true;
  }

  private static boolean _isOnlyFolders( final File folder )
  {
    final File[] paths = folder.listFiles();
    for ( File path : paths )
      if ( !path.isDirectory() )
        return false;

    // At this point, either empty or only contains folders.
    for ( File path : paths )
      if ( !_isOnlyFolders(path) )
        return false;

    return true;
  }

  private Folders()
  {
    // empty
  }
}
