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
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class EscCsv
{
  EscCsv()
  {
    // empty
  }

  /**
   * <p>
   * Makes a string safe to use as a field within a comma separated (CSV) file.
   * </p>
   * 
   * @param obj
   * @return The substituted string, or <code>null</code> if <code>obj</code>
   *         was <code>null</code>.
   */
  public String text( final Object obj )
  {
    return Converter.xsub(obj, CsvConv.CSV);
  }
}
