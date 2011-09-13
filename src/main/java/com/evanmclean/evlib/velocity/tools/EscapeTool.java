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
package com.evanmclean.evlib.velocity.tools;

import com.evanmclean.evlib.escape.Esc;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/**
 * <p>
 * Extends the velocity tools EscapeTool with extra things that can be escaped.
 * </p>
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
@SuppressWarnings( "NM_SAME_SIMPLE_NAME_AS_SUPERCLASS" )
public class EscapeTool extends org.apache.velocity.tools.generic.EscapeTool
{
  /**
   * <p>
   * Return the string with any characters that require escaping done to make it
   * safe for use in XML CDATA text. Null returns null.
   * </p>
   * 
   * @param obj
   *        The string to make safe.
   * @return The string with any substitutions necessary.
   */
  public String cdata( final Object obj )
  {
    return Esc.xml.cdata(obj);
  }

  /**
   * <p>
   * Makes a string safe to use as a field within a comma separated (CSV) file.
   * Null returns null.
   * </p>
   * 
   * @param obj
   *        The string to make safe.
   * @return The string with any substitutions necessary.
   */
  public String csv( final Object obj )
  {
    return Esc.csv.text(obj);
  }

  /**
   * <p>
   * Return the string with any characters that require escaping done to make it
   * safe for use in HTML text. Null returns null.
   * </p>
   * 
   * @param obj
   *        The string to make safe.
   * @return The string with any substitutions necessary.
   */
  @Override
  public String html( final Object obj )
  {
    return Esc.html.text(obj);
  }

  /**
   * Return the string with any characters that require escaping done to make it
   * safe for an HTML attribute. Null returns null.
   * 
   * @param obj
   *        The string to make safe.
   * @return The string with any substitutions necessary.
   */
  public String htmlAttr( final Object obj )
  {
    return Esc.html.attr(obj);
  }

  /**
   * Return the string with any characters that require escaping done to make it
   * safe for use in HTML text. New line characters have &quot;&lt;br
   * /&gt;&quot; inserted in front of them. Null returns null.
   * 
   * @param obj
   *        The string to make safe.
   * @return The string with any substitutions necessary.
   */
  public String htmlBr( final Object obj )
  {
    return Esc.html.textbr(obj);
  }

  /**
   * <p>
   * Return the string with any characters that require escaping done to make it
   * safe for use in an HTML comment. Null returns null.
   * </p>
   * 
   * @param obj
   *        The string to make safe.
   * @return The string with any substitutions necessary.
   */
  public String htmlComment( final Object obj )
  {
    return Esc.html.comment(obj);
  }

  /**
   * <p>
   * Makes a string safe to use as a string literal inside a piece of Java code
   * (does not add enclosing double quotes). Null returns null.
   * </p>
   * 
   * @param obj
   *        The string to make safe.
   * @return The string with any substitutions necessary.
   */
  @Override
  public String java( final Object obj )
  {
    return Esc.java.text(obj);
  }

  /**
   * <p>
   * Makes a string safe to use as a string literal inside a piece of Javascript
   * code (does not add enclosing double quotes). Null returns null.
   * </p>
   * 
   * @param obj
   *        The string to make safe.
   * @return The string with any substitutions necessary.
   */
  @Override
  public String javascript( final Object obj )
  {
    return Esc.javascript.text(obj);
  }

  /**
   * Return the string with any characters that require escaping done to make it
   * safe for a URL argument. Null returns null.
   * 
   * @param obj
   *        The string to make safe.
   * @return The string with any substitutions necessary.
   * @throws RuntimeException
   *         In the extremely unlikely event that your JVM doesn't know how to
   *         do UTF-8 encoding, we rethrow the UnsupportedEncodingException as a
   *         RuntimeException instead. Should never happen.
   */
  @Override
  public String url( final Object obj )
  {
    return Esc.url.text(obj);
  }

  /**
   * <p>
   * Return the string with any characters that require escaping done to make it
   * safe for use in XML text. Null returns null.
   * </p>
   * 
   * @param obj
   *        The string to make safe.
   * @return The string with any substitutions necessary.
   */
  @Override
  public String xml( final Object obj )
  {
    return Esc.xml.text(obj);
  }

  /**
   * Return the string with any characters that require escaping done to make it
   * safe for an XML attribute. Null returns null.
   * 
   * @param obj
   *        The string to make safe.
   * @return The string with any substitutions necessary.
   */
  public String xmlAttr( final Object obj )
  {
    return Esc.xml.attr(obj);
  }

  /**
   * <p>
   * Return the string with any characters that require escaping done to make it
   * safe for use in an XML comment. Null returns null.
   * </p>
   * 
   * @param obj
   *        The string to make safe.
   * @return The string with any substitutions necessary.
   */
  public String xmlComment( final Object obj )
  {
    return Esc.xml.comment(obj);
  }
}
