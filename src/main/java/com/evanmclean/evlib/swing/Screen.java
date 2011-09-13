/*
 * = License =
 * 
 * McLean Computer Services Open Source Software License
 * 
 * (Looks like the BSD license, but less restrictive.)
 * 
 * Copyright (c) 2006-2011 Evan McLean. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * 2. Neither the names "Evan McLean", "McLean Computer Services", "EvLib" nor
 * the names of any contributors may be used to endorse or promote products
 * derived from this software without prior written permission.
 * 
 * 3. Products derived from this software may not be called "Evlib", nor may
 * "Evlib" appear in their name, without prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * = License =
 */
package com.evanmclean.evlib.swing;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.evanmclean.evlib.lang.MutableBoolean;
import com.evanmclean.evlib.lang.Str;
import com.evanmclean.evlib.prefs.Prefs;

/**
 * Utilities for managing windows on screen and screen dimensions.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class Screen
{
  /**
   * Dummy value used to show we are not interested in this parameter.
   */
  private static final int INVALID_VALUE = Integer.MIN_VALUE;

  /**
   * Default key for saving windows preferences.
   */
  private static final String DEF_KEY = "_DefaultSizePos";

  /**
   * Centre the window over the parent window, ensuring that it is still
   * entirely visible on the screen.
   * 
   * @param window
   *        The window to position.
   * @param parent
   *        The window to centre over.
   */
  public static void centreOn( final Window window, final Window parent )
  {
    final Point pos = getLocationOnScreen(window);
    final int width = window.getWidth();
    final int height = window.getHeight();
    if ( window != parent )
    {
      final Point ppos = getLocationOnScreen(parent);
      pos.x = ppos.x + ((parent.getWidth() - width) / 2);
      pos.y = ppos.y + ((parent.getHeight() - height) / 2);
    }

    onScreen(pos, width, height);
    window.setLocation(pos);
  }

  /**
   * Centre the window on the primary display screen. If it is bigger then the
   * screen it will be positioned in the top left corner.
   * 
   * @param window
   *        The window to position.
   */
  public static void centreScreen( final Window window )
  {
    final Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
    final Point pos = getLocationOnScreen(window);
    pos.x = (ss.width - window.getWidth()) / 2;
    pos.y = (ss.height - window.getHeight()) / 2;
    if ( pos.x < 0 )
      pos.x = 0;
    if ( pos.y < 0 )
      pos.y = 0;
    window.setLocation(pos);
  }

  /**
   * Clear the size position preferences for the window.
   * 
   * @param cls
   *        The class to clear.
   */
  public static void clearWindowSizePos( final Class<?> cls )
  {
    clearWindowSizePos(cls, DEF_KEY);
  }

  /**
   * Clear the size position preferences for the window.
   * 
   * @param cls
   *        The class to clear.
   * @param key
   *        The key to retrieve from.
   */
  public static void clearWindowSizePos( final Class<?> cls, final String key )
  {
    final Preferences pref = Prefs.userNodeForClass(cls);
    pref.remove(key);
    Prefs.sync(pref);
  }

  /**
   * Clear the size position preferences for the window.
   * 
   * @param window
   *        The window to clear.
   */
  public static void clearWindowSizePos( final Window window )
  {
    clearWindowSizePos(window, DEF_KEY);
  }

  /**
   * Clear the size position preferences for the window.
   * 
   * @param window
   *        The window to clear.
   * @param key
   *        The key to retrieve from.
   */
  public static void clearWindowSizePos( final Window window, final String key )
  {
    final Preferences pref = getPref(window);
    pref.remove(key);
    Prefs.sync(pref);
  }

  /**
   * Helper function that gets the default preferences node for a window. Based
   * on the name of the class (not just the package).
   * 
   * @param window
   * @return The default preferences node for the window.
   */
  public static Preferences getPref( final Window window )
  {
    final Class<? extends Window> cls = window.getClass();
    return Prefs.userNodeForClass(cls);
  }

  /**
   * Retrieve the saved position information from a preferences key.
   * 
   * @param pref
   *        The preferences key to retrieve from.
   * @param key
   *        The key to retrieve from.
   * @param pos
   *        Store the position values here.
   * @return Returns true if the position changed.
   */
  public static boolean getWindowPos( final Preferences pref, final String key,
      final Point pos )
  {
    return getWindowSizePos(pref, key, null, pos);
  }

  /**
   * Position a window based on the saved position information from a
   * preferences key, ensuring that the window is still entirely on the screen.
   * 
   * @param window
   *        The window to size and position.
   * @return Returns true if the size or position was changed.
   */
  public static boolean getWindowPos( final Window window )
  {
    return getWindowPos(window, DEF_KEY);
  }

  /**
   * Position a window based on the saved position information from a
   * preferences key, ensuring that the window is still entirely on the screen.
   * 
   * @param window
   *        The window to size and position.
   * @param key
   *        The key to retrieve from.
   * @return Returns true if the size or position was changed.
   */
  public static boolean getWindowPos( final Window window, final String key )
  {
    final Preferences pref = getPref(window);
    final Point pos = getLocationOnScreen(window);
    final Dimension size = window.getSize();
    final Point orig_pos = new Point(pos);
    final Dimension orig_size = new Dimension(size);
    if ( !getWindowSizePos(pref, key, null, pos) )
      return false;

    final Dimension minsize = window.getMinimumSize();
    if ( size.width < minsize.width )
      size.width = minsize.width;
    if ( size.height < minsize.height )
      size.height = minsize.height;

    final Dimension maxsize = window.getMaximumSize();
    if ( size.width > maxsize.width )
      size.width = maxsize.width;
    if ( size.height > maxsize.height )
      size.height = maxsize.height;

    onScreen(pos, size);

    boolean changed = false;
    if ( !size.equals(orig_size) )
    {
      window.setSize(size);
      changed = true;
    }
    if ( !pos.equals(orig_pos) )
    {
      window.setLocation(pos);
      changed = true;
    }
    return changed;
  }

  /**
   * Retrieve the saved size information from a preferences key.
   * 
   * @param pref
   *        The preferences key to retrieve from.
   * @param key
   *        The key to retrieve from.
   * @param size
   *        Store the size values here.
   * @return Returns true if the size changed.
   */
  public static boolean getWindowSize( final Preferences pref,
      final String key, final Dimension size )
  {
    return getWindowSizePos(pref, key, size, null);
  }

  /**
   * Position a window based on the saved size information from a preferences
   * key, ensuring that the window is still entirely on the screen.
   * 
   * @param window
   *        The window to size and position.
   * @return Returns true if the size or position was changed.
   */
  public static boolean getWindowSize( final Window window )
  {
    return getWindowSize(window, DEF_KEY);
  }

  /**
   * Position a window based on the saved size information from a preferences
   * key, ensuring that the window is still entirely on the screen.
   * 
   * @param window
   *        The window to size and position.
   * @param key
   *        The key to retrieve from.
   * @return Returns true if the size or position was changed.
   */
  public static boolean getWindowSize( final Window window, final String key )
  {
    final Preferences pref = getPref(window);
    final Point pos = getLocationOnScreen(window);
    final Dimension size = window.getSize();
    final Point orig_pos = new Point(pos);
    final Dimension orig_size = new Dimension(size);
    if ( !getWindowSizePos(pref, key, size, null) )
      return false;

    final Dimension minsize = window.getMinimumSize();
    if ( size.width < minsize.width )
      size.width = minsize.width;
    if ( size.height < minsize.height )
      size.height = minsize.height;

    final Dimension maxsize = window.getMaximumSize();
    if ( size.width > maxsize.width )
      size.width = maxsize.width;
    if ( size.height > maxsize.height )
      size.height = maxsize.height;

    onScreen(pos, size);

    boolean changed = false;
    if ( !size.equals(orig_size) )
    {
      window.setSize(size);
      changed = true;
    }
    if ( !pos.equals(orig_pos) )
    {
      window.setLocation(pos);
      changed = true;
    }
    return changed;
  }

  /**
   * Retrieve the saved size and position information from a preferences key.
   * 
   * @param pref
   *        The preferences key to retrieve from.
   * @param key
   *        The key to retrieve from.
   * @param size
   *        Store the size values here (if not null).
   * @param pos
   *        Store the position values here (if not null).
   * @return Returns true if the size or position was changed.
   */
  public static boolean getWindowSizePos( final Preferences pref,
      final String key, final Dimension size, final Point pos )
  {
    final String str = pref.get(key, null);
    if ( Str.isBlank(str) )
      return false;

    boolean changed = false;
    final int len = str.length();
    int idx = 0;
    while ( idx < len )
    {
      // Find next ID char
      char id = '\0';
      while ( (id == '\0') && (idx < len) )
      {
        final char ch = str.charAt(idx);
        switch ( ch )
        {
          case 'w':
          case 'h':
          case 'x':
          case 'y':
            id = ch;
            break;

          default:
            break;
        }
        ++idx;
      }

      if ( idx >= len )
        break;

      boolean negative = false;
      if ( str.charAt(idx) == '-' )
      {
        negative = true;
        ++idx;
        if ( idx >= len )
          break;
      }

      int val = INVALID_VALUE;
      while ( idx < len )
      {
        final char ch = str.charAt(idx);
        if ( (ch < '0') || (ch > '9') )
          break;
        if ( val == INVALID_VALUE )
          val = ch - '0';
        else
          val = (val * 10) + (ch - '0');
        ++idx;
      }

      if ( val != INVALID_VALUE )
      {
        if ( negative )
          val *= -1;

        switch ( id )
        {
          case 'w':
            if ( (size != null) && (size.width != val) )
            {
              size.width = val;
              changed = true;
            }
            break;

          case 'h':
            if ( (size != null) && (size.height != val) )
            {
              size.height = val;
              changed = true;
            }
            break;

          case 'x':
            if ( (pos != null) && (pos.x != val) )
            {
              pos.x = val;
              changed = true;
            }
            break;

          case 'y':
            if ( (pos != null) && (pos.y != val) )
            {
              pos.y = val;
              changed = true;
            }
            break;

          default:
            break;
        }
      }
    }

    return changed;
  }

  /**
   * Position a window based on the saved size and position information from a
   * preferences key, ensuring that the window is still entirely on the screen.
   * 
   * @param window
   *        The window to size and position.
   * @return Returns true if the size or position was changed.
   */
  public static boolean getWindowSizePos( final Window window )
  {
    return getWindowSizePos(window, DEF_KEY);
  }

  /**
   * Position a window based on the saved size and position information from a
   * preferences key, ensuring that the window is still entirely on the screen.
   * 
   * @param window
   *        The window to size and position.
   * @param key
   *        The key to retrieve from.
   * @return Returns true if the size or position was changed.
   */
  public static boolean getWindowSizePos( final Window window, final String key )
  {
    final Preferences pref = getPref(window);
    final Point pos = getLocationOnScreen(window);
    final Dimension size = window.getSize();
    final Point orig_pos = new Point(pos);
    final Dimension orig_size = new Dimension(size);
    if ( !getWindowSizePos(pref, key, size, pos) )
      return false;

    final Dimension minsize = window.getMinimumSize();
    if ( size.width < minsize.width )
      size.width = minsize.width;
    if ( size.height < minsize.height )
      size.height = minsize.height;

    final Dimension maxsize = window.getMaximumSize();
    if ( size.width > maxsize.width )
      size.width = maxsize.width;
    if ( size.height > maxsize.height )
      size.height = maxsize.height;

    onScreen(pos, size);

    boolean changed = false;
    if ( !size.equals(orig_size) )
    {
      window.setSize(size);
      changed = true;
    }
    if ( !pos.equals(orig_pos) )
    {
      window.setLocation(pos);
      changed = true;
    }
    return changed;
  }

  public static void main( final String[] args )
  {
    SwingUtilities.invokeLater(new Runnable() {
      public void run()
      {
        final JFrame inst = new JFrame();
        final MutableBoolean clearing = new MutableBoolean(false);
        inst.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        inst.addWindowListener(new WindowAdapter() {
          @Override
          public void windowClosed( final WindowEvent evt )
          {
            System.exit(0);
          }

          @Override
          public void windowClosing( final WindowEvent evt )
          {
            if ( clearing.getValue() )
              Screen.clearWindowSizePos(inst);
            else
              Screen.saveWindowSizePos(inst);
          }

          @Override
          public void windowOpened( final WindowEvent evt )
          {
            Screen.centreScreen(inst);
            Screen.getWindowSizePos(inst);
          }
        });
        final JButton btn = new JButton("Clear");
        btn.addActionListener(new ActionListener() {
          public void actionPerformed( final ActionEvent e )
          {
            clearing.setValue(true);
          }
        });
        inst.add(btn);
        inst.pack();
        inst.setVisible(true);
      }
    });
  }

  /**
   * Ensure that, given the specified size, the specified position will have the
   * entire window on the screen (or failing that, in the top left corner).
   * 
   * @param xpos
   *        The current x coordinate of the window.
   * @param ypos
   *        The current y coordinate of the window.
   * 
   * @param size
   *        The size of the window.
   * @return The position, modified if necessary.
   */
  public static Point onScreen( final int xpos, final int ypos,
      final Dimension size )
  {
    return onScreen(xpos, ypos, size.width, size.height);
  }

  /**
   * Ensure that, given the specified size, the specified position will have the
   * entire window on the screen (or failing that, in the top left corner).
   * 
   * @param xpos
   *        The current x coordinate of the window.
   * @param ypos
   *        The current y coordinate of the window.
   * @param width
   *        The width of the window.
   * @param height
   *        The size of the window.
   * @return The position, modified if necessary.
   */
  public static Point onScreen( final int xpos, final int ypos,
      final int width, final int height )
  {
    final Point pt = new Point(xpos, ypos);
    onScreen(pt, width, height);
    return pt;
  }

  /**
   * Ensure that, given the specified size, the specified position will have the
   * entire window on the screen (or failing that, in the top left corner).
   * 
   * @param pos
   *        The current position, which will be modified if necessary.
   * @param size
   *        The size of the window.
   * @return Returns true if the position has changed.
   */
  public static boolean onScreen( final Point pos, final Dimension size )
  {
    return onScreen(pos, size.width, size.height);
  }

  /**
   * Ensure that, given the specified size, the specified position will have the
   * entire window on a screen (or failing that, in the top left corner).
   * 
   * @param pos
   *        The current position, which will be modified if necessary.
   * @param width
   *        The width of the window.
   * @param height
   *        The size of the window.
   * @return Returns true if the position has changed.
   */
  public static boolean onScreen( final Point pos, final int width,
      final int height )
  {
    // Get the rectangles that make up the displays.
    final Rectangle[] screens;
    {
      final GraphicsEnvironment genv = GraphicsEnvironment
          .getLocalGraphicsEnvironment();
      final GraphicsDevice[] gdevs = genv.getScreenDevices();
      screens = new Rectangle[gdevs.length];
      for ( int xi = 0; xi < gdevs.length; ++xi )
        screens[xi] = gdevs[xi].getDefaultConfiguration().getBounds();
    }

    // Calculate which screen we most overlap with.
    Rectangle best = null;
    {
      int best_coverage = 0;
      for ( Rectangle re : screens )
      {
        final int x1 = Math.max(pos.x, re.x);
        final int y1 = Math.max(pos.y, re.y);
        final int x2 = Math.min(pos.x + width, re.x + re.width);
        final int y2 = Math.min(pos.y + height, re.y + re.height);
        final int coverage = (x2 - x1) * (y2 - y1);
        if ( coverage > best_coverage )
        {
          best = re;
          best_coverage = coverage;
        }
      }
    }
    if ( best == null )
    {
      // Don't overlap, so figure out which one we are closest to.
      // Compute distance of window x/y from screen x/y.
      double best_distance = Double.MAX_VALUE;
      for ( Rectangle re : screens )
      {
        final int xdist = Math.abs(pos.x - re.x);
        final int ydist = Math.abs(pos.y - re.y);
        final double dist = Math.sqrt((xdist * xdist) + (ydist * ydist));
        if ( dist < best_distance )
        {
          best = re;
          best_distance = dist;
        }
      }
      // If all else fails, pick the first one.
      if ( best == null )
        best = screens[0];
    }

    int xpos = pos.x;
    final int orig_xpos = xpos;
    int ypos = pos.y;
    final int orig_ypos = ypos;

    if ( (xpos + width) > (best.x + best.width) )
      xpos = best.x + best.width - width;
    if ( (ypos + height) > (best.y + best.height) )
      ypos = best.y + best.height - height;

    if ( xpos < best.x )
      xpos = best.x;
    if ( ypos < best.y )
      ypos = best.y;

    if ( (xpos == orig_xpos) && (ypos == orig_ypos) )
      return false;

    pos.setLocation(xpos, ypos);
    return true;
  }

  /**
   * Ensure that the entire window is on the screen (or failing that, in the top
   * left corner).
   * 
   * @param window
   *        The window to reposition if necessary.
   * @return Returns true if the location was changed.
   */
  public static boolean onScreen( final Window window )
  {
    final Point pos = getLocationOnScreen(window);
    if ( !onScreen(pos, window.getWidth(), window.getHeight()) )
      return false;

    window.setLocation(pos);
    return true;
  }

  /**
   * Save position information in a preferences key.
   * 
   * @param pref
   *        The preference node to store in.
   * @param key
   *        The key to store in.
   * @param xpos
   * @param ypos
   * @return True if stored successfully.
   */
  public static boolean saveWindowPos( final Preferences pref,
      final String key, final int xpos, final int ypos )
  {
    return saveWindowSizePos(pref, key, INVALID_VALUE, INVALID_VALUE, xpos,
      ypos);
  }

  /**
   * Save position information in the default preferences key for the window.
   * 
   * @param window
   *        The window to store the information for.
   * @return True if stored successfully.
   */
  public static boolean saveWindowPos( final Window window )
  {
    return saveWindowPos(window, DEF_KEY);
  }

  /**
   * Save position information in the default preferences key for the window.
   * 
   * @param window
   *        The window to store the information for.
   * @param key
   *        The key to store in.
   * @return True if stored successfully.
   */
  public static boolean saveWindowPos( final Window window, final String key )
  {
    final Preferences pref = getPref(window);
    final Point pos = getLocationOnScreen(window);
    return saveWindowSizePos(pref, key, INVALID_VALUE, INVALID_VALUE, pos.x,
      pos.y);
  }

  /**
   * Save size information in a preferences key.
   * 
   * @param pref
   *        The preference node to store in.
   * @param key
   *        The key to store in.
   * @param width
   * @param height
   * @return True if stored successfully.
   */
  public static boolean saveWindowSize( final Preferences pref,
      final String key, final int width, final int height )
  {
    return saveWindowSizePos(pref, key, width, height, INVALID_VALUE,
      INVALID_VALUE);
  }

  /**
   * Save size information in the default preferences key for the window.
   * 
   * @param window
   *        The window to store the information for.
   * @return True if stored successfully.
   */
  public static boolean saveWindowSize( final Window window )
  {
    return saveWindowSize(window, DEF_KEY);
  }

  /**
   * Save size information in the default preferences key for the window.
   * 
   * @param window
   *        The window to store the information for.
   * @param key
   *        The key to store in.
   * @return True if stored successfully.
   */
  public static boolean saveWindowSize( final Window window, final String key )
  {
    final Preferences pref = getPref(window);
    return saveWindowSizePos(pref, key, window.getWidth(), window.getHeight(),
      INVALID_VALUE, INVALID_VALUE);
  }

  /**
   * Save size and position information in a preferences key.
   * 
   * @param pref
   *        The preference node to store in.
   * @param key
   *        The key to store in.
   * @param width
   * @param height
   * @param xpos
   * @param ypos
   * @return True if stored successfully.
   */
  public static boolean saveWindowSizePos( final Preferences pref,
      final String key, final int width, final int height, final int xpos,
      final int ypos )
  {
    final StringBuilder buff = new StringBuilder();
    if ( width != INVALID_VALUE )
    {
      buff.append('w');
      buff.append(Math.max(0, width));
    }
    if ( height != INVALID_VALUE )
    {
      buff.append('h');
      buff.append(Math.max(0, height));
    }
    if ( xpos != INVALID_VALUE )
    {
      buff.append('x');
      buff.append(xpos);
    }
    if ( ypos != INVALID_VALUE )
    {
      buff.append('y');
      buff.append(ypos);
    }

    pref.put(key, buff.toString());

    return Prefs.sync(pref);
  }

  /**
   * Save size and position information in the default preferences key for the
   * window.
   * 
   * @param window
   *        The window to store the information for.
   * @return True if stored successfully.
   */
  public static boolean saveWindowSizePos( final Window window )
  {
    return saveWindowSizePos(window, DEF_KEY);
  }

  /**
   * Save size and position information in the default preferences key for the
   * window.
   * 
   * @param window
   *        The window to store the information for.
   * @param key
   *        The key to store in.
   * @return True if stored successfully.
   */
  public static boolean saveWindowSizePos( final Window window, final String key )
  {
    final Preferences pref = getPref(window);
    final Point pos = getLocationOnScreen(window);
    return saveWindowSizePos(pref, key, window.getWidth(), window.getHeight(),
      pos.x, pos.y);
  }

  /**
   * Little helper function to get the location of the window on the screen, or
   * if that throws an exception, just call getLocation.
   * 
   * @param window
   * @return The window's location.
   */
  private static Point getLocationOnScreen( final Window window )
  {
    try
    {
      return window.getLocationOnScreen();
    }
    catch ( IllegalComponentStateException ex )
    {
      return window.getLocation();
    }
  }

  private Screen()
  {
    // empty
  }
}
