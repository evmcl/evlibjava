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
package com.evanmclean.evlib.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import com.evanmclean.evlib.lang.Str;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/**
 * Handy utilities for swing.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class Swing
{
  /**
   * Displays a message dialog box with details of the exception object,
   * including the stack trace in a scrollbox.
   * 
   * @param parent
   *        The parent component of the dialog box.
   * @param ex
   *        The exception object.
   * @param msg
   *        An optional message (null to not use).
   * @param title
   *        The title for the dialog box.
   */
  public static void displayException( final Component parent,
      final Throwable ex, final String msg, final String title )
  {
    try
    {
      String dmsg = msg;
      if ( Str.isBlank(dmsg) )
        dmsg = null;

      String exmsg = abbr(ex.getMessage());
      if ( Str.isBlank(exmsg) )
      {
        if ( dmsg == null )
          exmsg = "An exception occurred.";
        else
          exmsg = null;
      }

      final Object[] msgarr = new Object[((dmsg != null) && (exmsg != null)) ? 4
          : 3];
      int msgidx = 0;
      if ( dmsg != null )
        msgarr[msgidx++] = dmsg;
      if ( exmsg != null )
        msgarr[msgidx++] = exmsg;
      msgarr[msgidx++] = "Stack trace:";

      final StringWriter outs = new StringWriter();
      final PrintWriter out = new PrintWriter(outs);
      ex.printStackTrace(out);
      final JTextArea text_area = new JTextArea(outs.toString());
      text_area.setEditable(false);
      final JScrollPane stack_trace = new JScrollPane(text_area);

      final Dimension max = Toolkit.getDefaultToolkit().getScreenSize();
      max.width = Math.max(600, (int) (max.width * 0.9));
      max.height = Math.max(400, (int) (max.height * 0.8));

      final Dimension pref = stack_trace.getPreferredSize();
      pref.width = Math.min(pref.width, max.width);
      pref.height = Math.min(pref.height, max.height);

      stack_trace.setPreferredSize(pref);
      stack_trace.setMaximumSize(max);
      msgarr[msgidx++] = stack_trace;
      SafeOptionPane.showMessageDialog(parent, msgarr, title,
        JOptionPane.ERROR_MESSAGE);
    }
    catch ( Throwable ex2 )
    {
      ex2.printStackTrace();
    }
  }

  /**
   * Sets swing to use the system look and feel for the operating system (if
   * available), ignoring any exceptions if they occur.
   */
  @SuppressWarnings( {
      "DE_MIGHT_IGNORE", "REC_CATCH_EXCEPTION"
  } )
  public static void setSystemLookAndFeel()
  {
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch ( Exception ex )
    {
      // ignore
    }
  }

  /**
   * Abbreviate the message string, adding ellipses if it is longer than 100
   * characters.
   * 
   * @param msg
   * @return The original (trimmed) string or an abbreviated version.
   */
  private static String abbr( final String msg )
  {
    String abbr = Str.trimToNull(msg);
    if ( abbr == null )
      return null;
    return (abbr.length() <= 100) ? abbr : (abbr.substring(0, 97) + "...");
  }

  private Swing()
  {
    // empty
  }
}
