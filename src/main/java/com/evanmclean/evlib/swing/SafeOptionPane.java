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
import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Thread safe JOptionPane calls.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class SafeOptionPane
{
  private static class Confirm implements Runnable
  {
    int ret;
    Component parent;
    Object msg;
    String title;
    int optionType;

    Confirm()
    {
      // empty
    }

    public void run()
    {
      ret = JOptionPane.showConfirmDialog(parent, msg, title, optionType);
    }
  }

  private static class Input implements Runnable
  {
    String ret;
    Component parent;
    Object msg;
    String initial;

    Input()
    {
      // empty
    }

    public void run()
    {
      ret = JOptionPane.showInputDialog(parent, msg, initial);
    }
  }

  /**
   * Calls JOptionPane.showConfirmDialog in a thread-safe manner. If it is in
   * the event dispatch thread then it calls directly, otherwise it uses
   * SwingUtilities.invokeAndWait.
   * 
   * 
   * @param parent
   * @param msg
   * @param title
   * @param option_type
   * @return Same value os JoptoinPane.showConfirmDialog.
   * @throws InterruptedException
   * @throws InvocationTargetException
   */
  public static int showConfirmDialog( final Component parent,
      final Object msg, final String title, final int option_type )
    throws InterruptedException,
      InvocationTargetException
  {
    if ( SwingUtilities.isEventDispatchThread() )
      return JOptionPane.showConfirmDialog(parent, msg, title, option_type);

    final Confirm confirm = new Confirm();
    confirm.parent = parent;
    confirm.msg = msg;
    confirm.title = title;
    confirm.optionType = option_type;
    SwingUtilities.invokeAndWait(confirm);
    return confirm.ret;
  }

  /**
   * Calls JOptionPane.showInputDialog in a thread-safe manner. If it is in the
   * event dispatch thread then it calls directly, otherwise it uses
   * SwingUtilities.invokeAndWait.
   * 
   * @param parent
   * @param msg
   * @param initial_value
   * @return Same value as JOptonPane.showInputDialog.
   * @throws InterruptedException
   * @throws InvocationTargetException
   */
  public static String showInputDialog( final Component parent,
      final Object msg, final String initial_value )
    throws InterruptedException,
      InvocationTargetException
  {
    if ( SwingUtilities.isEventDispatchThread() )
      return JOptionPane.showInputDialog(parent, msg, initial_value);

    final Input input = new Input();
    input.parent = parent;
    input.msg = msg;
    input.initial = initial_value;
    SwingUtilities.invokeAndWait(input);
    return input.ret;
  }

  /**
   * Calls JOptionPane.showMessageDialog in a thread-safe manner. If it is in
   * the event dispatch thread then it calls directly, otherwise it uses
   * SwingUtilities.invokeAndWait.
   * 
   * @param parent
   * @param msg
   * @param title
   * @param message_type
   * @throws InterruptedException
   * @throws InvocationTargetException
   */
  public static void showMessageDialog( final Component parent,
      final Object msg, final String title, final int message_type )
    throws InterruptedException,
      InvocationTargetException
  {
    if ( SwingUtilities.isEventDispatchThread() )
    {
      JOptionPane.showMessageDialog(parent, msg, title, message_type);
    }
    else
    {
      SwingUtilities.invokeAndWait(new Runnable() {
        public void run()
        {
          JOptionPane.showMessageDialog(parent, msg, title, message_type);
        }
      });
    }
  }

  private SafeOptionPane()
  {
    // empty
  }
}
