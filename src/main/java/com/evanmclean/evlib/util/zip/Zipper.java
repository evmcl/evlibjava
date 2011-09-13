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
package com.evanmclean.evlib.util.zip;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.evanmclean.evlib.io.Files;
import com.evanmclean.evlib.lang.Str;

/**
 * Convenience wrapper around a {@link ZipOutputStream} to easily add files and
 * folders to a zip file.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class Zipper
{
  /**
   * Action to take for folders when recursively adding the contents of a
   * folder.
   */
  public static enum FolderAction
  {
    /**
     * Do not add an entry for the folder itself, entries are only added for
     * files.
     */
    IGNORE,
    /**
     * Add an entry for the folder if it has any files or sub-folders in it.
     */
    ADD,
    /**
     * Add an entry for all folders, even if they are empty.
     */
    ADD_EMPTIES
  }

  /** Compression method for compressed (DEFLATED) entries. */
  public static final int DEFLATED = ZipOutputStream.DEFLATED;

  /** Compression method for uncompressed (STORED) entries. */
  public static final int STORED = ZipOutputStream.STORED;

  private final ZipOutputStream out;
  private final byte[] buff = new byte[4 * 1024];
  private FolderAction folderAction;

  /**
   * Create a zip to the specified file with the default {@link FolderAction} of
   * {@link FolderAction#ADD}.
   * 
   * @param file
   *        File to create as a zip file.
   * @throws FileNotFoundException
   */
  public Zipper( final File file ) throws FileNotFoundException
  {
    this(new ZipOutputStream(new BufferedOutputStream(
        new FileOutputStream(file))), FolderAction.ADD);
  }

  /**
   * Zip to the specified file.
   * 
   * @param file
   *        File to create as a zip file.
   * @param folder_action
   *        The {@link FolderAction} to use.
   * @throws FileNotFoundException
   */
  public Zipper( final File file, final FolderAction folder_action )
    throws FileNotFoundException
  {
    this(new ZipOutputStream(new BufferedOutputStream(
        new FileOutputStream(file))), folder_action);
  }

  /**
   * Create a zip to the specified output stream with the default
   * {@link FolderAction} of {@link FolderAction#ADD}.
   * 
   * @param out
   *        Output stream to write as a zip file.
   */
  public Zipper( final OutputStream out )
  {
    this(new ZipOutputStream(out), FolderAction.ADD);
  }

  /**
   * Create a zip to the specified output stream.
   * 
   * @param out
   *        Output stream to write as a zip file.
   * @param folder_action
   *        The {@link FolderAction} to use.
   */
  public Zipper( final OutputStream out, final FolderAction folder_action )
  {
    this(new ZipOutputStream(out), folder_action);
  }

  /**
   * Create a zip to the specified zip output stream with the default
   * {@link FolderAction} of {@link FolderAction#ADD}.
   * 
   * @param out
   *        Zip output stream to write as a zip file.
   */
  public Zipper( final ZipOutputStream out )
  {
    this(out, FolderAction.ADD);
  }

  /**
   * Create a zip to the specified zip output stream.
   * 
   * @param out
   *        Zip output stream to write as a zip file.
   * @param folder_action
   *        The {@link FolderAction} to use.
   */
  public Zipper( final ZipOutputStream out, final FolderAction folder_action )
  {
    this.out = out;
    this.folderAction = folder_action;
  }

  /**
   * Closes the zip output stream as well as the underlying stream.
   * 
   * @throws IOException
   */
  public void close() throws IOException
  {
    out.close();
  }

  /**
   * Copy a file into the zip, just using the base name of the file.
   * 
   * @param file
   * @throws IOException
   */
  public void copy( final File file ) throws IOException
  {
    if ( !file.isFile() )
      throw new IllegalStateException(file.toString());
    copy(file, file.getName());
  }

  /**
   * Recursively copy all files in a folder into the zip.
   * 
   * @param folder
   *        Folder to copy.
   * @param zip_folder_name
   *        If true, then the name of the <code>folder</code> is used as the
   *        base folder for all the files within the zip, otherwise files and
   *        folders are stored relative to the root of the zip file.
   * @throws IOException
   */
  public void copy( final File folder, final boolean zip_folder_name )
    throws IOException
  {
    if ( !folder.isDirectory() )
      throw new IllegalStateException(folder.toString());

    final String prefix = Files.getCanonicalPath(
      zip_folder_name ? folder.getParentFile() : folder).replace('\\', '/') + '/';
    copyFolder(folder, prefix);
  }

  /**
   * Copy the file into the zip, using the specified file name.
   * 
   * @param file
   *        The file to zip.
   * @param name
   *        The name for the file within the zip, which may include a path
   *        component.
   * @throws IOException
   */
  public void copy( final File file, final String name ) throws IOException
  {
    final FileInputStream in = new FileInputStream(file);
    try
    {
      copy(in, name, file.length());
    }
    finally
    {
      in.close();
    }
  }

  /**
   * Read the input stream into the zip, using the specified file name.
   * 
   * @param in
   *        The input to zip.
   * @param name
   *        The name for the file within the zip, which may include a path
   *        component.
   * @throws IOException
   */
  public void copy( final InputStream in, final String name )
    throws IOException
  {
    copy(in, name, -1);
  }

  /**
   * Finishes writing the contents of the zip output stream without closing the
   * underlying stream. Use this method when applying multiple filters in
   * succession to the same output stream.
   * 
   * @throws IOException
   */
  public void finish() throws IOException
  {
    out.finish();
  }

  /**
   * Get the current {@link FolderAction} that is used for calls to
   * {@link #copy(File, boolean)}.
   * 
   * @return See {@link FolderAction}
   */
  public FolderAction getFolderAction()
  {
    return folderAction;
  }

  /**
   * Set the comment to be included in the zip file.
   * 
   * @param comment
   *        The comment.
   */
  public void setComment( final String comment )
  {
    out.setComment(comment);
  }

  /**
   * Set the {@link FolderAction} to be used for calls to
   * {@link #copy(File, boolean)}.
   * 
   * @param folder_action
   *        See {@link FolderAction}
   */
  public void setFolderAction( final FolderAction folder_action )
  {
    if ( folder_action == null )
      throw new NullPointerException();
    this.folderAction = folder_action;
  }

  /**
   * Set the compression level to a value between 0 (none), 1 (fast) and 9
   * (best).
   * 
   * @param level
   *        Compression level to use.
   */
  public void setLevel( final int level )
  {
    out.setLevel(level);
  }

  /**
   * Sets the default compression method for subsequent entries. This default
   * will be used whenever the compression method is not specified for an
   * individual ZIP file entry, and is initially set to {@link #DEFLATED}.
   * 
   * @param method
   *        One of {@link #STORED} or {@link #DEFLATED}.
   */
  public void setMethod( final int method )
  {
    out.setMethod(method);
  }

  private void addFolder( final File folder, final String prefix )
    throws IOException
  {
    final ZipEntry entry = new ZipEntry(getEntryName(folder, prefix) + '/');
    entry.setSize(0);
    out.putNextEntry(entry);
    out.closeEntry();
  }

  private void copy( final InputStream in, final String name, final long inlen )
    throws IOException
  {
    final ZipEntry entry = new ZipEntry(name.replace('\\', '/'));
    if ( inlen >= 0L )
      entry.setSize(inlen);
    out.putNextEntry(entry);
    try
    {
      int len;
      while ( (len = in.read(buff)) != -1 )
        out.write(buff, 0, len);
    }
    finally
    {
      out.closeEntry();
    }
  }

  private void copyFolder( final File folder, final String prefix )
    throws IOException
  {
    if ( folderAction.equals(FolderAction.ADD) )
      addFolder(folder, prefix);

    boolean any_files = false;
    boolean any_folders = false;
    final File[] files = folder.listFiles();
    for ( File file : files )
    {
      if ( file.isDirectory() )
      {
        any_folders = true;
      }
      else if ( file.isFile() )
      {
        copy(file, getEntryName(file, prefix));
        any_files = true;
      }
      else
      {
        throw new IllegalStateException(file.toString());
      }
    }

    if ( any_folders )
      for ( File file : files )
        if ( file.isDirectory() )
          copyFolder(file, prefix);

    if ( (!any_files) && (!any_folders)
        && folderAction.equals(FolderAction.ADD_EMPTIES) )
      addFolder(folder, prefix);
  }

  private String getEntryName( final File file, final String prefix )
  {
    String name = Files.getCanonicalPath(file).replace('\\', '/');
    if ( !Str.startsWithIgnoreCase(name, prefix) )
      throw new IllegalStateException(name);
    return name.substring(prefix.length());
  }
}
