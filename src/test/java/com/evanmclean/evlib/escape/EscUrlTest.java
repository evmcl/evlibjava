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

import com.evanmclean.evlib.charset.Charsets;

/**
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class EscUrlTest extends TestCase
{
  public static final String TEST_ALL_TEST_TYPE = "UNIT";

  public void testNull()
  {
    assertNull(Esc.url.text(null));
  }

  public void testUnchanged()
  {
    final String unchanged = "abcdef";
    assertSame(unchanged, Esc.url.text(unchanged));
    final String unchanged2 = "abc*def-_foo.";
    assertSame(unchanged2, Esc.url.text(unchanged2));
  }

  public void testUrl()
  {
    assertEquals("abc%20%2B%20def", Esc.url.text("abc + def"));
    assertEquals("abc*def-_foo.", Esc.url.text("abc*def-_foo."));
    assertEquals("The%20%E2%80%9Ctest%E2%80%9D",
      Esc.url.text("The \u201Ctest\u201D"));
    assertEquals("%FE%FF%00T%00h%00e%00%20%20%1C%00t%00e%00s%00t%20%1D",
      Esc.url.text("The \u201Ctest\u201D", Charsets.UTF16));
  }
}
