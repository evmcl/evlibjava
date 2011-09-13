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
package com.evanmclean.evlib.prefs;

import java.io.File;
import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Quick utilities for preferences (java.util.prefs.Preferences).
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class Prefs
{
  /**
   * Gets an existing folder from the specified preference. If the preference
   * points to a path that does not exist, or is a file and not a folder, it
   * will try getting the first parent folder that does exist.
   * 
   * @param pref
   *        The preference object.
   * @param key
   *        Key for the preference.
   * @param def
   *        Default to return if existing folder could not be found (null is
   *        okay).
   * @return The existing folder, or the default.
   */
  public static File getFolder( final Preferences pref, final String key,
      final File def )
  {
    final String fn = pref.get(key, null);
    if ( (fn == null) || (fn.length() <= 0) )
      return def;
    File file = new File(fn);
    try
    {
      file = file.getCanonicalFile();
    }
    catch ( IOException ex )
    {
      file = file.getAbsoluteFile();
    }
    while ( (file != null) && ((!file.exists()) || (!file.isDirectory())) )
      file = file.getParentFile();
    if ( file == null )
      return def;
    return file;
  }

  /**
   * Save the part of the specified file path that represents an existing
   * folder. If the specified file points to a path that does not exist, or is a
   * file and not a folder, it will try getting the first parent folder that
   * does exist.
   * 
   * @param pref
   *        The preference object.
   * @param key
   *        Key for the preference.
   * @param file
   *        The path (or part there of) to save.
   * @return True if it could find an existing folder to save.
   */
  public static boolean putFolder( final Preferences pref, final String key,
      final File file )
  {
    File fi = file;
    try
    {
      fi = file.getCanonicalFile();
    }
    catch ( IOException ex )
    {
      fi = file.getAbsoluteFile();
    }

    while ( (fi != null) && ((!fi.exists()) || (!fi.isDirectory())) )
      fi = fi.getParentFile();
    if ( fi != null )
    {
      pref.put(key, fi.toString());
      return true;
    }
    return false;
  }

  /**
   * Performs the synchronisation and catches any backing store exceptions.
   * 
   * @param pref
   *        The preference to sync.
   * @return True if synced okay, false if there was a backing store exception.
   */
  public static boolean sync( final Preferences pref )
  {
    try
    {
      pref.sync();
    }
    catch ( BackingStoreException ex )
    {
      return false;
    }
    return true;
  }

  /**
   * Returns a system node specific to the class, rather then at the package
   * level.
   * 
   * @param cls
   *        The class to get a node for.
   * @return The class level node.
   */
  public static Preferences systemNodeForClass( final Class<?> cls )
  {
    return getClassNode(Preferences.systemNodeForPackage(cls), cls);
  }

  /**
   * Returns a user node specific to the class, rather then at the package
   * level.
   * 
   * @param cls
   *        The class to get a node for.
   * @return The class level node.
   */
  public static Preferences userNodeForClass( final Class<?> cls )
  {
    return getClassNode(Preferences.userNodeForPackage(cls), cls);
  }

  private static Preferences getClassNode( final Preferences pkg_node,
      final Class<?> cls )
  {
    String name = cls.getName();

    // Get the base name without the package.
    int pos = name.lastIndexOf('.');
    if ( pos >= 0 )
    {
      ++pos;
      if ( pos < (name.length() - 1) )
        name = name.substring(pos);
    }

    return pkg_node.node(name);
  }

  private Prefs()
  {
    // empty
  }
}
