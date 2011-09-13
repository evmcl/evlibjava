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
package com.evanmclean.evlib.servlet;

import java.io.File;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;

import com.evanmclean.evlib.lang.Str;
import com.evanmclean.evlib.misc.Conv;

/**
 * Various utility functions for servlets.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class ServletUtils
{
  /**
   * Get the parameter from the request, trimming it and coverting to an
   * integer. If the parameter is not specified or is not a valid number, then a
   * specified default value is returned.
   * 
   * @param req
   *        The request object.
   * @param name
   *        The name of the parameter.
   * @param def
   *        The default value to return if the parameter is not a valid number.
   * @return Returns the parameter as an integer, or the defautl vaule.
   */
  public static int getIntParameter( final ServletRequest req,
      final String name, final int def )
  {
    final String str = Str.trimToNull(req.getParameter(name));
    if ( str == null )
      return def;
    return Conv.toInt(str, def);
  }

  /**
   * Get the parameter from the request, trimming it and returning an empty
   * string if it doesn't exist.
   * 
   * @param req
   *        The request object.
   * @param name
   *        The name of the parameter.
   * @return Returns the trimmed parameter, or an empty string.
   */
  public static String getParameter( final ServletRequest req, final String name )
  {
    return Str.trimToEmpty(req.getParameter(name));
  }

  /**
   * Get the parameter from the request, trimming it, and if blank then return
   * the specified default value.
   * 
   * @param req
   *        The request object.
   * @param name
   *        The name of the parameter.
   * @param def
   *        The default to return if the parameter is blank.
   * @return Returns the trimmed parameter, or the default value.
   */
  public static String getParameter( final ServletRequest req,
      final String name, final String def )
  {
    final String ret = Str.trimToNull(req.getParameter(name));
    if ( ret == null )
      return def;
    return ret;
  }

  /**
   * Get the directory where the servlet can put temporary files.
   * 
   * @param servlet
   * @return Returns the directory for temporary files.
   * @throws ServletException
   *         An error occurred getting the temporary directory.
   */
  public static File getTempDir( final GenericServlet servlet )
    throws ServletException
  {
    return getTempDir(servlet.getServletContext());
  }

  /**
   * Get the directory where the servlet can put temporary files.
   * 
   * @param servlet
   * @return Returns the directory for temporary files.
   * @throws ServletException
   *         An error occurred getting the temporary directory.
   */
  public static File getTempDir( final Servlet servlet )
    throws ServletException
  {
    return getTempDir(servlet.getServletConfig().getServletContext());
  }

  /**
   * Get the directory where the servlet can put temporary files.
   * 
   * @param servlet_config
   * @return Returns the directory for temporary files.
   * @throws ServletException
   *         An error occurred getting the temporary directory.
   */
  public static File getTempDir( final ServletConfig servlet_config )
    throws ServletException
  {
    return getTempDir(servlet_config.getServletContext());
  }

  /**
   * Get the directory where the servlet can put temporary files.
   * 
   * @param servlet_context
   * @return Returns the directory for temporary files.
   * @throws ServletException
   *         An error occurred getting the temporary directory.
   */
  public static File getTempDir( final ServletContext servlet_context )
    throws ServletException
  {
    final Object obj = servlet_context
        .getAttribute("javax.servlet.context.tmpdir");
    if ( obj == null )
      throw new ServletException("Could not get tmpdir attribute.");
    if ( !(obj instanceof File) )
      throw new ServletException("Unexpected class for tmpdir object: "
          + obj.getClass().getName());
    final File tmpdir = (File) obj;
    if ( !tmpdir.exists() )
      throw new ServletException("Temp folder does not exist: " + tmpdir);
    if ( !tmpdir.isDirectory() )
      throw new ServletException("Temp folder is not a directory: " + tmpdir);
    return tmpdir;
  }

  private ServletUtils()
  {
    // empty
  }
}
