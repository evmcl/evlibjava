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
public class EscHtmlTest extends TestCase
{
  public static final String TEST_ALL_TEST_TYPE = "UNIT";

  public void testHtml()
  {
    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;&#13;&#10;stuff&#10;don&#39;t&#13;\u201Ccut it\u201D&lt;/a&gt;",
      Esc.html
          .attr("<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>"));

    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;\r\nstuff\ndon't\r\u201Ccut it\u201D&lt;/a&gt;",
      Esc.html
          .text("<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>"));

    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;<br>\r\nstuff<br>\ndon't<br>\r\u201Ccut it\u201D&lt;/a&gt;",
      Esc.html.textbr(
        "<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>", false));

    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;<br>stuff<br>don't<br>\u201Ccut it\u201D&lt;/a&gt;",
      Esc.html.textbr(
        "<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>", true));

    assertEquals(
      "Comments\rare\nlargly unchanged -_- just \u201Cprotecting\u201D <double hyphens>",
      Esc.html
          .comment("Comments\rare\nlargly unchanged -- just \u201Cprotecting\u201D <double hyphens>"));
  }

  public void testHtmlAscii()
  {
    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;&#13;&#10;stuff&#10;don&#39;t&#13;&ldquo;cut it&rdquo;&lt;/a&gt;",
      Esc.htmlAscii
          .attr("<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>"));

    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;\r\nstuff\ndon't\r&ldquo;cut it&rdquo;&lt;/a&gt;",
      Esc.htmlAscii
          .text("<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>"));

    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;<br>\r\nstuff<br>\ndon't<br>\r&ldquo;cut it&rdquo;&lt;/a&gt;",
      Esc.htmlAscii.textbr(
        "<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>", false));

    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;<br>stuff<br>don't<br>&ldquo;cut it&rdquo;&lt;/a&gt;",
      Esc.htmlAscii.textbr(
        "<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>", true));

    assertEquals(
      "Comments\rare\nlargly unchanged -_- just \u201Cprotecting\u201D <double hyphens>",
      Esc.htmlAscii
          .comment("Comments\rare\nlargly unchanged -- just \u201Cprotecting\u201D <double hyphens>"));
  }

  public void testHtmlFull()
  {
    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;&#13;&#10;stuff&#10;don&#39;t&#13;&ldquo;cut it&rdquo;&lt;/a&gt;",
      Esc.htmlFull
          .attr("<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>"));

    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;\r\nstuff\ndon't\r&ldquo;cut it&rdquo;&lt;/a&gt;",
      Esc.htmlFull
          .text("<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>"));

    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;<br>\r\nstuff<br>\ndon't<br>\r&ldquo;cut it&rdquo;&lt;/a&gt;",
      Esc.htmlFull.textbr(
        "<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>", false));

    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;<br>stuff<br>don't<br>&ldquo;cut it&rdquo;&lt;/a&gt;",
      Esc.htmlFull.textbr(
        "<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>", true));

    assertEquals(
      "Comments\rare\nlargly unchanged -_- just \u201Cprotecting\u201D <double hyphens>",
      Esc.htmlFull
          .comment("Comments\rare\nlargly unchanged -- just \u201Cprotecting\u201D <double hyphens>"));
  }

  public void testHtmlMin()
  {
    assertEquals(
      "&lt;a href=&quot;foo&quot;>Foo &amp;&#13;&#10;stuff&#10;don&#39;t&#13;\u201Ccut it\u201D&lt;/a>",
      Esc.htmlMin
          .attr("<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>"));

    assertEquals(
      "&lt;a href=\"foo\">Foo &amp;\r\nstuff\ndon't\r\u201Ccut it\u201D&lt;/a>",
      Esc.htmlMin
          .text("<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>"));

    assertEquals(
      "&lt;a href=\"foo\">Foo &amp;<br>\r\nstuff<br>\ndon't<br>\r\u201Ccut it\u201D&lt;/a>",
      Esc.htmlMin.textbr(
        "<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>", false));

    assertEquals(
      "&lt;a href=\"foo\">Foo &amp;<br>stuff<br>don't<br>\u201Ccut it\u201D&lt;/a>",
      Esc.htmlMin.textbr(
        "<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>", true));

    assertEquals(
      "Comments\rare\nlargly unchanged -_- just \u201Cprotecting\u201D <double hyphens>",
      Esc.htmlMin
          .comment("Comments\rare\nlargly unchanged -- just \u201Cprotecting\u201D <double hyphens>"));
  }

  public void testNull()
  {
    assertNull(Esc.htmlMin.attr(null));
    assertNull(Esc.htmlMin.comment(null));
    assertNull(Esc.htmlMin.text(null));
    assertNull(Esc.htmlMin.textbr(null));
    assertNull(Esc.html.attr(null));
    assertNull(Esc.html.comment(null));
    assertNull(Esc.html.text(null));
    assertNull(Esc.html.textbr(null));
    assertNull(Esc.htmlFull.attr(null));
    assertNull(Esc.htmlFull.comment(null));
    assertNull(Esc.htmlFull.text(null));
    assertNull(Esc.htmlFull.textbr(null));
    assertNull(Esc.htmlAscii.attr(null));
    assertNull(Esc.htmlAscii.comment(null));
    assertNull(Esc.htmlAscii.text(null));
    assertNull(Esc.htmlAscii.textbr(null));
    assertNull(Esc.xhtml.attr(null));
    assertNull(Esc.xhtml.comment(null));
    assertNull(Esc.xhtml.text(null));
    assertNull(Esc.xhtml.textbr(null));
    assertNull(Esc.xhtmlFull.attr(null));
    assertNull(Esc.xhtmlFull.comment(null));
    assertNull(Esc.xhtmlFull.text(null));
    assertNull(Esc.xhtmlFull.textbr(null));
    assertNull(Esc.xhtmlAscii.attr(null));
    assertNull(Esc.xhtmlAscii.comment(null));
    assertNull(Esc.xhtmlAscii.text(null));
    assertNull(Esc.xhtmlAscii.textbr(null));
  }

  public void testUnchanged()
  {
    final String unchanged = "abcdef";
    assertSame(unchanged, Esc.htmlMin.attr(unchanged));
    assertSame(unchanged, Esc.htmlMin.comment(unchanged));
    assertSame(unchanged, Esc.htmlMin.text(unchanged));
    assertSame(unchanged, Esc.htmlMin.textbr(unchanged));
    assertSame(unchanged, Esc.html.attr(unchanged));
    assertSame(unchanged, Esc.html.comment(unchanged));
    assertSame(unchanged, Esc.html.text(unchanged));
    assertSame(unchanged, Esc.html.textbr(unchanged));
    assertSame(unchanged, Esc.htmlFull.attr(unchanged));
    assertSame(unchanged, Esc.htmlFull.comment(unchanged));
    assertSame(unchanged, Esc.htmlFull.text(unchanged));
    assertSame(unchanged, Esc.htmlFull.textbr(unchanged));
    assertSame(unchanged, Esc.htmlAscii.attr(unchanged));
    assertSame(unchanged, Esc.htmlAscii.comment(unchanged));
    assertSame(unchanged, Esc.htmlAscii.text(unchanged));
    assertSame(unchanged, Esc.htmlAscii.textbr(unchanged));
    assertSame(unchanged, Esc.xhtml.attr(unchanged));
    assertSame(unchanged, Esc.xhtml.comment(unchanged));
    assertSame(unchanged, Esc.xhtml.text(unchanged));
    assertSame(unchanged, Esc.xhtml.textbr(unchanged));
    assertSame(unchanged, Esc.xhtmlFull.attr(unchanged));
    assertSame(unchanged, Esc.xhtmlFull.comment(unchanged));
    assertSame(unchanged, Esc.xhtmlFull.text(unchanged));
    assertSame(unchanged, Esc.xhtmlFull.textbr(unchanged));
    assertSame(unchanged, Esc.xhtmlAscii.attr(unchanged));
    assertSame(unchanged, Esc.xhtmlAscii.comment(unchanged));
    assertSame(unchanged, Esc.xhtmlAscii.text(unchanged));
    assertSame(unchanged, Esc.xhtmlAscii.textbr(unchanged));
  }

  public void testXhtml()
  {
    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;&#13;&#10;stuff&#10;don&#39;t&#13;\u201Ccut it\u201D&lt;/a&gt;",
      Esc.xhtml
          .attr("<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>"));

    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;\r\nstuff\ndon't\r\u201Ccut it\u201D&lt;/a&gt;",
      Esc.xhtml
          .text("<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>"));

    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;<br/>\r\nstuff<br/>\ndon't<br/>\r\u201Ccut it\u201D&lt;/a&gt;",
      Esc.xhtml.textbr(
        "<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>", false));

    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;<br/>stuff<br/>don't<br/>\u201Ccut it\u201D&lt;/a&gt;",
      Esc.xhtml.textbr(
        "<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>", true));

    assertEquals(
      "Comments\rare\nlargly unchanged -_- just \u201Cprotecting\u201D <double hyphens>",
      Esc.xhtml
          .comment("Comments\rare\nlargly unchanged -- just \u201Cprotecting\u201D <double hyphens>"));
  }

  public void testXhtmlAscii()
  {
    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;&#13;&#10;stuff&#10;don&#39;t&#13;&ldquo;cut it&rdquo;&lt;/a&gt;",
      Esc.xhtmlAscii
          .attr("<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>"));

    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;\r\nstuff\ndon't\r&ldquo;cut it&rdquo;&lt;/a&gt;",
      Esc.xhtmlAscii
          .text("<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>"));

    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;<br/>\r\nstuff<br/>\ndon't<br/>\r&ldquo;cut it&rdquo;&lt;/a&gt;",
      Esc.xhtmlAscii.textbr(
        "<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>", false));

    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;<br/>stuff<br/>don't<br/>&ldquo;cut it&rdquo;&lt;/a&gt;",
      Esc.xhtmlAscii.textbr(
        "<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>", true));

    assertEquals(
      "Comments\rare\nlargly unchanged -_- just \u201Cprotecting\u201D <double hyphens>",
      Esc.xhtmlAscii
          .comment("Comments\rare\nlargly unchanged -- just \u201Cprotecting\u201D <double hyphens>"));
  }

  public void testXhtmlFull()
  {
    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;&#13;&#10;stuff&#10;don&#39;t&#13;&ldquo;cut it&rdquo;&lt;/a&gt;",
      Esc.xhtmlFull
          .attr("<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>"));

    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;\r\nstuff\ndon't\r&ldquo;cut it&rdquo;&lt;/a&gt;",
      Esc.xhtmlFull
          .text("<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>"));

    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;<br/>\r\nstuff<br/>\ndon't<br/>\r&ldquo;cut it&rdquo;&lt;/a&gt;",
      Esc.xhtmlFull.textbr(
        "<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>", false));

    assertEquals(
      "&lt;a href=&quot;foo&quot;&gt;Foo &amp;<br/>stuff<br/>don't<br/>&ldquo;cut it&rdquo;&lt;/a&gt;",
      Esc.xhtmlFull.textbr(
        "<a href=\"foo\">Foo &\r\nstuff\ndon't\r\u201Ccut it\u201D</a>", true));

    assertEquals(
      "Comments\rare\nlargly unchanged -_- just \u201Cprotecting\u201D <double hyphens>",
      Esc.xhtmlFull
          .comment("Comments\rare\nlargly unchanged -- just \u201Cprotecting\u201D <double hyphens>"));
  }
}
