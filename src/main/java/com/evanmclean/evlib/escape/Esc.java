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
 * Provides worker objects that can perform character escapes on strings to make
 * them safe for various output (e.g. HTML, XML, Javascript).
 * </p>
 * 
 * <p>
 * <a name="html_xhtml_note"></a>The only difference between the
 * <code>html</code> escape objects and their <code>xhtml</code> equivalents is
 * that the {@link EscHtml#textbr(Object)} methods inserts a &quot;
 * <code>&lt;br&gt;</code> for HTML, and a &quot;<code>&lt;br/&gt;</code>&quot;
 * for XHTML.
 * </p>
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class Esc
{
  /**
   * <p>
   * Makes a string safe to use as a field within a comma separated (CSV) file.
   * </p>
   * 
   * <p>
   * Basically encapsulates the string in a pair of double-quotes if the string
   * contains any of the following:
   * </p>
   * 
   * <ul>
   * <li>A comma.</li>
   * <li>A double-quote (they are also encoded as two double-quotes).</li>
   * <li>White space</li>
   * <li>Any character outside the standard printable ASCII range.</li>
   * </ul>
   */
  public static final EscCsv csv = new EscCsv();

  /**
   * <p>
   * HTML escaper that will convert the absolute minimum characters required to
   * make it safe for HTML (basically the <code>&lt;</code> and
   * <code>&amp;</code> characters, plus unprintable control characters).
   * </p>
   * 
   * <p>
   * See <a href="#html_xhtml_note">note about HTML and XHTML</a>.
   * </p>
   */
  public static final EscHtml htmlMin = new EscHtml(HtmlConv.HTML_MIN,
      HtmlConv.HTML_MIN_ATTR, HtmlConv.BR);

  /**
   * <p>
   * HTML escaper that will convert the basic necessary characters required to
   * make it safe for HTML (basically the <code>&lt;</code>, <code>&gt;</code>,
   * <code>&quot;</code> and <code>&amp;</code> characters, plus unprintable
   * control characters). <strong>This is probably the one you'll want most of
   * the time.</strong>
   * </p>
   * 
   * <p>
   * See <a href="#html_xhtml_note">note about HTML and XHTML</a>.
   * </p>
   */
  public static final EscHtml html = new EscHtml(HtmlConv.HTML_BASIC,
      HtmlConv.HTML_BASIC_ATTR, HtmlConv.BR);

  /**
   * <p>
   * HTML escaper that will convert any character that falls outside of the
   * printable ASCII set (as well as the standard <code>&lt;</code>,
   * <code>&gt;</code>, <code>&quot;</code> and <code>&amp;</code> characters).
   * </p>
   * 
   * <p>
   * See <a href="#html_xhtml_note">note about HTML and XHTML</a>.
   * </p>
   */
  public static final EscHtml htmlAscii = new EscHtml(HtmlConv.HTML_ASCII,
      HtmlConv.HTML_ASCII_ATTR, HtmlConv.BR);

  /**
   * <p>
   * HTML escaper that will convert any character that has a standard HTML
   * entity code equivalent. (e.g., &quot;&dagger;&quot; becomes
   * &quot;&amp;dagger;&quot;.)
   * </p>
   * 
   * <p>
   * See <a href="#html_xhtml_note">note about HTML and XHTML</a>.
   * </p>
   */
  public static final EscHtml htmlFull = new EscHtml(HtmlConv.HTML_FULL,
      HtmlConv.HTML_FULL_ATTR, HtmlConv.BR);

  /**
   * <p>
   * XHTML escaper that will convert the basic necessary characters required to
   * make it safe for XHTML (basically the <code>&lt;</code>, <code>&gt;</code>,
   * <code>&quot;</code> and <code>&amp;</code> characters, plus unprintable
   * control characters). <strong>This is probably the one you'll want most of
   * the time.</strong>
   * </p>
   * 
   * <p>
   * See <a href="#html_xhtml_note">note about HTML and XHTML</a>.
   * </p>
   */
  public static final EscHtml xhtml = new EscHtml(HtmlConv.HTML_BASIC,
      HtmlConv.HTML_BASIC_ATTR, HtmlConv.XBR);

  /**
   * <p>
   * XHTML escaper that will convert any character that falls outside of the
   * printable ASCII set (as well as the standard <code>&lt;</code>,
   * <code>&gt;</code>, <code>&quot;</code> and <code>&amp;</code> characters).
   * </p>
   * 
   * <p>
   * See <a href="#html_xhtml_note">note about HTML and XHTML</a>.
   * </p>
   */
  public static final EscHtml xhtmlAscii = new EscHtml(HtmlConv.HTML_ASCII,
      HtmlConv.HTML_ASCII_ATTR, HtmlConv.XBR);

  /**
   * <p>
   * XHTML escaper that will convert any character that has a standard XHTML
   * entity code equivalent. (e.g., &quot;&dagger;&quot; becomes
   * &quot;&amp;dagger;&quot;.)
   * </p>
   * 
   * <p>
   * See <a href="#html_xhtml_note">note about HTML and XHTML</a>.
   * </p>
   */
  public static final EscHtml xhtmlFull = new EscHtml(HtmlConv.HTML_FULL,
      HtmlConv.HTML_FULL_ATTR, HtmlConv.XBR);

  /**
   * <p>
   * XML escaper that will convert the standard characters required to make it
   * safe for XML (basically the <code>&lt;</code>, <code>&gt;</code>,
   * <code>&quot;</code>, <code>'</code> and <code>&amp;</code> characters, plus
   * unprintable control characters). <strong>This is probably the one you'll
   * want most of the time.</strong>
   * </p>
   */
  public static final EscXml xml = new EscXml(XmlConv.XML, XmlConv.XML_ATTR);

  /**
   * <p>
   * XML escaper that will convert any character that falls outside of the
   * printable ASCII set (as well as the standard <code>&lt;</code>,
   * <code>&gt;</code>, <code>&quot;</code>, <code>'</code> and
   * <code>&amp;</code> characters).
   * </p>
   */
  public static final EscXml xmlAscii = new EscXml(XmlConv.XML_ASCII,
      XmlConv.XML_ASCII_ATTR);

  /**
   * <p>
   * Makes a string safe to use as a URL parameter.
   * </p>
   */
  public static final EscUrl url = new EscUrl();

  /**
   * <p>
   * Makes a string safe to use as a string literal inside a piece of Java code
   * (does not add enclosing double quotes).
   * </p>
   */
  public static final EscJava java = new EscJava();

  /**
   * <p>
   * Makes a string safe to use as a string literal inside a piece of Javascript
   * code (does not add enclosing double quotes).
   * </p>
   */
  public static final EscJavascript javascript = new EscJavascript();
}
