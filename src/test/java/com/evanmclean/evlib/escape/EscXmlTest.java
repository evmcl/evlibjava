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

import junit.framework.TestCase;

/**
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class EscXmlTest extends TestCase
{
  public static final String TEST_ALL_TEST_TYPE = "UNIT";

  public void testNull()
  {
    assertNull(Esc.xml.attr(null));
    assertNull(Esc.xml.cdata(null));
    assertNull(Esc.xml.comment(null));
    assertNull(Esc.xml.text(null));
    assertNull(Esc.xmlAscii.attr(null));
    assertNull(Esc.xmlAscii.cdata(null));
    assertNull(Esc.xmlAscii.comment(null));
    assertNull(Esc.xmlAscii.text(null));
  }

  public void testUnchanged()
  {
    final String unchanged = "abcdef";
    assertSame(unchanged, Esc.xml.attr(unchanged));
    assertSame(unchanged, Esc.xml.cdata(unchanged));
    assertSame(unchanged, Esc.xml.comment(unchanged));
    assertSame(unchanged, Esc.xml.text(unchanged));
    assertSame(unchanged, Esc.xmlAscii.attr(unchanged));
    assertSame(unchanged, Esc.xmlAscii.cdata(unchanged));
    assertSame(unchanged, Esc.xmlAscii.comment(unchanged));
    assertSame(unchanged, Esc.xmlAscii.text(unchanged));
  }

  public void testXml()
  {
    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;&#13;&#10;stuff&#10;don&apos;t&#13;\u201Ccut it\u201D&lt;/a&gt;",
      Esc.xml
          .attr("<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>"));

    assertEquals(
      "<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut <![CDATA[it]]]><![CDATA[]>\u201D</a>",
      Esc.xml
          .cdata("<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut <![CDATA[it]]>\u201D</a>"));

    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;\r\nstuff\ndon&apos;t\r\u201Ccut it\u201D&lt;/a&gt;",
      Esc.xml
          .text("<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>"));

    assertEquals(
      "Comments\rare\nlargly unchanged -_- just \u201Cprotecting\u201D <double hyphens>",
      Esc.xml
          .comment("Comments\rare\nlargly unchanged -- just \u201Cprotecting\u201D <double hyphens>"));
  }

  public void testXmlAscii()
  {
    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;&#13;&#10;stuff&#10;don&apos;t&#13;&#8220;cut it&#8221;&lt;/a&gt;",
      Esc.xmlAscii
          .attr("<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>"));

    assertEquals(
      "<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut <![CDATA[it]]]><![CDATA[]>\u201D</a>",
      Esc.xmlAscii
          .cdata("<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut <![CDATA[it]]>\u201D</a>"));

    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;\r\nstuff\ndon&apos;t\r&#8220;cut it&#8221;&lt;/a&gt;",
      Esc.xmlAscii
          .text("<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>"));

    assertEquals(
      "Comments\rare\nlargly unchanged -_- just \u201Cprotecting\u201D <double hyphens>",
      Esc.xmlAscii
          .comment("Comments\rare\nlargly unchanged -- just \u201Cprotecting\u201D <double hyphens>"));
  }
}
