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
package com.evanmclean.evlib.commons.fileupload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;

/**
 * Misc handy functions for the <a href="http://commons.apache.org/fileupload/"
 * target="_blank">Apache Commons File Upload</a> package.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class FileUploadUtils
{
  /**
   * Remaps the file items returned by FileUpload.parseRequest into an array.
   * 
   * @param file_items
   * @return A map keyed on the field name.
   */
  public static FileItem[] arrItems( final List<?> file_items )
  {
    final FileItem[] arr = new FileItem[file_items.size()];
    int xi = 0;
    for ( Object object : file_items )
      arr[xi++] = (FileItem) object;
    return arr;
  }

  /**
   * Call FileItem.delete for all items that have been stored on disk.
   * 
   * @param file_items
   */
  public static void delete( final FileItem[] file_items )
  {
    for ( FileItem fi : file_items )
      if ( !fi.isInMemory() )
        fi.delete();
  }

  /**
   * Call FileItem.delete for all items that have been stored on disk.
   * 
   * @param file_items
   */
  public static void delete( final List<?> file_items )
  {
    for ( Object object : file_items )
    {
      final FileItem fi = (FileItem) object;
      if ( !fi.isInMemory() )
        fi.delete();
    }
  }

  /**
   * Call FileItem.delete for all items that have been stored on disk.
   * 
   * @param file_items
   */
  public static void delete( final Map<String, FileItem[]> file_items )
  {
    for ( FileItem[] fis : file_items.values() )
      for ( FileItem fi : fis )
        if ( !fi.isInMemory() )
          fi.delete();
  }

  /**
   * Remaps the file items returned by FileUpload.parseRequest into a map based
   * on the field names.
   * 
   * @param file_items
   * @return A map keyed on the field name.
   */
  public static Map<String, FileItem[]> mapItems( final List<?> file_items )
  {
    final Map<String, FileItem[]> map = new HashMap<String, FileItem[]>();
    for ( Object object : file_items )
    {
      final FileItem fi = (FileItem) object;
      final String key = fi.getFieldName();
      FileItem[] arr = map.get(key);
      if ( arr == null )
      {
        arr = new FileItem[1];
        arr[0] = fi;
      }
      else
      {
        final FileItem[] newarr = new FileItem[arr.length + 1];
        System.arraycopy(arr, 0, newarr, 0, arr.length);
        newarr[arr.length] = fi;
      }
      map.put(key, arr);
    }
    return map;
  }

  private FileUploadUtils()
  {
    // empty
  }
}
