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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.evanmclean.evlib.io.Folders;

/**
 * Unzips an entire zip file into a specified base folder, creating sub-folders
 * as needed.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class Unzipper
{
  /**
   * How to handle folder entries in the zip file (sub-folders are always
   * created for files as needed).
   */
  public static enum FolderAction
  {
    /**
     * Ignore folder entries in the zip file. Only create sub-folders when
     * needed by a file (you will never end up with an empty sub-folder).
     */
    IGNORE,
    /**
     * Create the folders that are specified in the zip file, even if they end
     * up being empty.
     */
    CREATE
  }

  /**
   * Unzip the specified zip file.
   * 
   * @param zip_file
   *        The zip file to unzip.
   * @param base_folder
   *        The base folder to unzip into (will be created if necessary).
   * @param folder_action
   *        See {@link FolderAction}.
   * @throws IOException
   */
  public static void run( final File zip_file, final File base_folder,
      final FolderAction folder_action ) throws IOException
  {
    new Unzipper(folder_action).unzip(zip_file, base_folder);
  }

  /**
   * Unzip the specified input stream.
   * 
   * @param in
   *        The input stream to unzip.
   * @param base_folder
   *        The base folder to unzip into (will be created if necessary).
   * @param folder_action
   *        See {@link FolderAction}.
   * @throws IOException
   */
  public static void run( final InputStream in, final File base_folder,
      final FolderAction folder_action ) throws IOException
  {
    new Unzipper(folder_action).unzip(in, base_folder);
  }

  /**
   * Unzip the specified zip input stream.
   * 
   * @param in
   *        The zip input stream to unzip.
   * @param base_folder
   *        The base folder to unzip into (will be created if necessary).
   * @param folder_action
   *        See {@link FolderAction}.
   * @throws IOException
   */
  public static void run( final ZipInputStream in, final File base_folder,
      final FolderAction folder_action ) throws IOException
  {
    new Unzipper(folder_action).unzip(in, base_folder);
  }

  private final byte[] buff = new byte[4 * 1024];
  private FolderAction folderAction;

  /**
   * Create unzipper with the default {@link FolderAction} of
   * {@link FolderAction#CREATE}.
   */
  public Unzipper()
  {
    this(FolderAction.CREATE);
  }

  /**
   * Create unzipper with the specified {@link FolderAction}.
   * 
   * @param folder_action
   *        See {@link FolderAction}.
   */
  public Unzipper( final FolderAction folder_action )
  {
    this.folderAction = folder_action;
  }

  /**
   * Get the current {@link FolderAction}.
   * 
   * @return The current {@link FolderAction}.
   */
  public FolderAction getFolderAction()
  {
    return folderAction;
  }

  /**
   * Set the {@link FolderAction} to use with the next <code>unzip</code> call.
   * 
   * @param folder_action
   *        See {@link FolderAction}.
   */
  public void setFolderAction( final FolderAction folder_action )
  {
    if ( folder_action == null )
      throw new NullPointerException();
    this.folderAction = folder_action;
  }

  /**
   * Unzip the specified zip file.
   * 
   * @param zip_file
   *        The zip file to unzip.
   * @param base_folder
   *        The base folder to unzip into (will be created if necessary).
   * @throws IOException
   */
  public void unzip( final File zip_file, final File base_folder )
    throws IOException
  {
    final InputStream in = new FileInputStream(zip_file);
    try
    {
      unzip(new ZipInputStream(new BufferedInputStream(in)), base_folder);
    }
    finally
    {
      in.close();
    }
  }

  /**
   * Unzip the specified input stream.
   * 
   * @param in
   *        The input stream to unzip.
   * @param base_folder
   *        The base folder to unzip into (will be created if necessary).
   * @throws IOException
   */
  public void unzip( final InputStream in, final File base_folder )
    throws IOException
  {
    unzip(new ZipInputStream(in), base_folder);
  }

  /**
   * Unzip the specified zip input stream.
   * 
   * @param in
   *        The zip input stream to unzip.
   * @param base_folder
   *        The base folder to unzip into (will be created if necessary).
   * @throws IOException
   */
  public void unzip( final ZipInputStream in, final File base_folder )
    throws IOException
  {
    ZipEntry entry;
    while ( (entry = in.getNextEntry()) != null )
    {
      try
      {
        if ( entry.isDirectory() )
        {
          if ( folderAction.equals(FolderAction.CREATE) )
          {
            final File folder = new File(base_folder, entry.getName());
            if ( !folder.exists() )
              Folders.mks(folder);
          }
        }
        else
        {
          final File file = new File(base_folder, entry.getName());
          final File parent = file.getParentFile();
          if ( !parent.exists() )
            Folders.mks(parent);

          final OutputStream out = new BufferedOutputStream(
              new FileOutputStream(file));
          try
          {
            int len;
            while ( (len = in.read(buff)) != -1 )
              out.write(buff, 0, len);
          }
          finally
          {
            out.close();
          }
        }
      }
      finally
      {
        in.closeEntry();
      }
    }
  }
}
