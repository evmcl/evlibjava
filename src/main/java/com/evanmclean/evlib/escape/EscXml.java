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
package com.evanmclean.evlib.escape;

/**
 * <p>
 * Escapes character escaping suitable for XML output.
 * </p>
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class EscXml
{
  /**
   * The start of an XML CDATA element: &quot;<code>&lt;![CDATA[</code>&quot;
   */
  public static final String CDATA_START = "<![CDATA[";

  /**
   * The end of an XML CDATA element: &quot;<code>]]&gt;</code>&quot;
   */
  public static final String CDATA_END = "]]>";

  private final CharConv textConv;
  private final CharConv attrConv;

  EscXml( final CharConv text_conv, final CharConv attr_conv )
  {
    this.textConv = text_conv;
    this.attrConv = attr_conv;
  }

  /**
   * Performs escaping for an XML attribute.
   * 
   * @param obj
   * @return The substituted string, or <code>null</code> if <code>obj</code>
   *         was <code>null</code>.
   */
  public String attr( final Object obj )
  {
    return Converter.sub(obj, attrConv);
  }

  /**
   * Performs escaping for text inside an XML CDATA element.
   * 
   * @param obj
   * @return The substituted string, or <code>null</code> if <code>obj</code>
   *         was <code>null</code>.
   */
  public String cdata( final Object obj )
  {
    if ( obj == null )
      return null;
    final String orig = obj.toString();
    if ( orig.indexOf(XmlConv.CDATA_BAD) < 0 )
      return orig;

    return orig.replace(XmlConv.CDATA_BAD, XmlConv.CDATA_GOOD);
  }

  /**
   * Performs escaping for text inside an XML comment.
   * 
   * @param obj
   * @return The substituted string, or <code>null</code> if <code>obj</code>
   *         was <code>null</code>.
   */
  public String comment( final Object obj )
  {
    return Converter.sub(obj, MlConv.COMMENT_BAD, MlConv.COMMENT_GOOD);
  }

  /**
   * Performs escaping for text inside an XML text element.
   * 
   * @param obj
   * @return The substituted string, or <code>null</code> if <code>obj</code>
   *         was <code>null</code>.
   */
  public String text( final Object obj )
  {
    return Converter.sub(obj, textConv);
  }
}
