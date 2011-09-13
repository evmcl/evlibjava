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
 * An extended converter used for some more specialised conversions.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
interface ExtendedCharConv extends CharConv
{
  /**
   * Performs an initial check of the string and returns the point we should
   * begin conversion from.
   * 
   * @param str
   *        The string to be converted.
   * @return The position to begin conversion from, or -1 if the string does not
   *         require conversion.
   */
  int convFrom( final String str );

  /**
   * Initialise the StringBuilder buffer before we start the conversion. This is
   * a good spot to put anything that prefix the converted string, such as a
   * double-quote.
   * 
   * @param buff
   *        An empty buffer.
   * @param str
   *        The string that will be converted.
   * @return Should normally return <code>buff</code>.
   */
  StringBuilder initialise( StringBuilder buff, String str );

  /**
   * Finish-off the StringBuilder buffer after we've done the conversion. This
   * is a good spot to put anything that suffix the converted string, such as a
   * double-quote.
   * 
   * @param buff
   *        The buffer containing the converted string.
   * @param str
   *        The string that was converted.
   * @return Normally return <code>buff.toString()</code>.
   */
  String finish( StringBuilder buff, String str );
}
