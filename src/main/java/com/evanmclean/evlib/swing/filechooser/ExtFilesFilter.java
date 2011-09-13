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
package com.evanmclean.evlib.swing.filechooser;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import com.evanmclean.evlib.lang.Str;
import com.evanmclean.evlib.util.TreeSetIgnoreCase;

/**
 * File filter for the JFileChooser that accepts files of the specified
 * extension(s).
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class ExtFilesFilter extends FileFilter
{
  private boolean showFolders = true;
  private boolean filesOnly = true;
  private final String description;
  private TreeSetIgnoreCase exts = new TreeSetIgnoreCase();

  public ExtFilesFilter( final String description )
  {
    this.description = description;
  }

  public ExtFilesFilter( final String description, final String ext )
  {
    this.description = description;
    exts.add(ext);
  }

  public ExtFilesFilter( final String description, final String ext,
      final String... exts_arr )
  {
    this.description = description;
    exts.add(ext);
    for ( String ex : exts_arr )
      exts.add(ex);
  }

  @Override
  public boolean accept( final File file )
  {
    if ( showFolders )
      if ( file.isDirectory() )
        return true;
    if ( filesOnly )
      if ( !file.isFile() )
        return false;
    final String ext = getExtension(file.getName());
    return exts.contains(ext);
  }

  /**
   * Add an extension to be allowed.
   * 
   * @param ext
   * @return <code>true</code> if the extension is not already included.
   */
  public boolean addExtension( final String ext )
  {
    return exts.add(ext);
  }

  @Override
  public String getDescription()
  {
    return description;
  }

  /**
   * @return The current set of extensions.
   */
  public String[] getExtensions()
  {
    return exts.toArray(new String[exts.size()]);
  }

  /**
   * @return Returns true if the filter does not accept folders (directories)
   *         with one of the allowed extensions (default true).
   */
  public boolean isFilesOnly()
  {
    return filesOnly;
  }

  /**
   * @return Returns true if the filter accepts folders (directories)
   *         irrespective of the extension (default true).
   */
  public boolean isShowFolders()
  {
    return showFolders;
  }

  /**
   * Remove the extension from the filer.
   * 
   * @param ext
   * @return True if the extension was being filtered.
   */
  public boolean removeExtension( final String ext )
  {
    return exts.remove(ext);
  }

  /**
   * Set if the filer does not accepts folders (directories) with one of the
   * allowed extensions (default true).
   * 
   * @param files_only
   */
  public void setFilesOnly( final boolean files_only )
  {
    filesOnly = files_only;
  }

  /**
   * Set if the filter accepts folders (directories) irrespective of the
   * extension (default true).
   * 
   * @param show_folders
   */
  public void setShowFolders( final boolean show_folders )
  {
    showFolders = show_folders;
  }

  /**
   * Get the extension of the file (not including the '.'), or an empty string
   * if none.
   * 
   * @param filename
   * @return The file extension.
   */
  private String getExtension( final String filename )
  {
    if ( filename == null )
      return null;
    final int extpos = filename.lastIndexOf('.');
    if ( extpos <= 0 )
      return Str.EMPTY;

    // Check that we are still in the base file part.
    final int len = filename.length();
    for ( int xi = extpos + 1; xi < len; ++xi )
    {
      final char ch = filename.charAt(xi);
      switch ( ch )
      {
        case '/':
        case '\\':
          return Str.EMPTY;
      }
    }

    // Check that we are not at the start of the base file part. (i.e., The base
    // filename starts with a '.' and has no extension.
    if ( extpos > 0 )
      switch ( filename.charAt(extpos - 1) )
      {
        case '/':
        case '\\':
          return Str.EMPTY;
      }

    return filename.substring(extpos + 1);
  }

}
