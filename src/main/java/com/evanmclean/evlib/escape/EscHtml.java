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
 * Performs character escaping suitable for HTML output.
 * </p>
 * 
 * <p>
 * In general, if you pass a <code>null</code> in, you'll get a
 * <code>null</code> out. Otherwise, you'll get the {@link Object#toString()}
 * value of the object you pass in, escaped appropriately.
 * </p>
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class EscHtml
{
  private final CharConv textConv;
  private final CharConv attrConv;
  private final String br;

  EscHtml( final CharConv text_conv, final CharConv attr_conv, final String br )
  {
    this.textConv = text_conv;
    this.attrConv = attr_conv;
    this.br = br;
  }

  /**
   * Performs escaping for an HTML attribute.
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
   * Performs escaping for an HTML comment.
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
   * Performs escaping for general HTML text.
   * 
   * @param obj
   * @return The substituted string, or <code>null</code> if <code>obj</code>
   *         was <code>null</code>.
   */
  public String text( final Object obj )
  {
    return Converter.sub(obj, textConv);
  }

  /**
   * <p>
   * Performs escaping for general HTML text, and also inserts a
   * <code>&lt;br&gt;</code> or <code>&lt;br/&gt;</code> as appropriate before
   * each new line.
   * </p>
   * 
   * <p>
   * A newline is one of:
   * </p>
   * 
   * <dl>
   * <dt><code>\r</code></dt>
   * <dd>A single Carriage Return (ASCII 13)</dd>
   * <dt><code>\n</code></dt>
   * <dd>A single Line Feed or Newline (ASCII 10)</dd>
   * <dt><code>\r\n</code></dt>
   * <dd>A Carriage Return followed by a Line Feed</dd>
   * </dl>
   * 
   * @param obj
   * @return The substituted string, or <code>null</code> if <code>obj</code>
   *         was <code>null</code>.
   */
  public String textbr( final Object obj )
  {
    return Converter.sub(obj, textConv, br, false);
  }

  /**
   * <p>
   * Performs escaping for general HTML text, and also inserts or replaces a
   * <code>&lt;br&gt;</code> or <code>&lt;br/&gt;</code> as appropriate before
   * each new line.
   * </p>
   * 
   * <p>
   * A newline is one of:
   * </p>
   * 
   * <dl>
   * <dt><code>\r</code></dt>
   * <dd>A single Carriage Return (ASCII 13)</dd>
   * <dt><code>\n</code></dt>
   * <dd>A single Line Feed or Newline (ASCII 10)</dd>
   * <dt><code>\r\n</code></dt>
   * <dd>A Carriage Return followed by a Line Feed</dd>
   * </dl>
   * 
   * @param obj
   * @param replace
   *        If <code>true</code> then replace the newline character(s), else
   *        just insert the element before it.
   * @return The substituted string, or <code>null</code> if <code>obj</code>
   *         was <code>null</code>.
   */
  public String textbr( final Object obj, final boolean replace )
  {
    return Converter.sub(obj, textConv, br, replace);
  }
}
