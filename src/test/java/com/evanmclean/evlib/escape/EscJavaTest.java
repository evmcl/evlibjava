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
public class EscJavaTest extends TestCase
{
  public static final String TEST_ALL_TEST_TYPE = "UNIT";

  public void testJava()
  {
    assertEquals("\\\\\\\\ \\\"Don't go\\r\\nback there\\\"", Esc.java
        .text("\\\\ \"Don't go\r\nback there\""));
    assertEquals("\\u201Chello world\\u201D", Esc.java
        .text("\u201Chello world\u201D"));
  }

  public void testNull()
  {
    assertNull(Esc.java.text(null));
  }

  public void testUnchanged()
  {
    final String unchanged = "abcdef";
    assertSame(unchanged, Esc.java.text(unchanged));
  }
}
