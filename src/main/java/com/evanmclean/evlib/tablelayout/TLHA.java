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
package com.evanmclean.evlib.tablelayout;

import info.clearthought.layout.TableLayoutConstants;

/**
 * Shorthand enumerated type for TableLayout vertical alignments. See <a
 * href="package-summary.html#package_description">summary</a> for an example of
 * typical usage.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public enum TLHA
{
  CENTER(TableLayoutConstants.CENTER, "Center") //
  , CENTRE(TableLayoutConstants.CENTER, "Centre") //
  , FULL(TableLayoutConstants.FULL, "Full") //
  , LEADING(TableLayoutConstants.LEADING, "Leading") //
  , LEFT(TableLayoutConstants.LEFT, "Left") //
  , RIGHT(TableLayoutConstants.RIGHT, "Right") //
  , TRAILING(TableLayoutConstants.TRAILING, "Trailing") //
  ;

  public final int value;
  private final String name;

  private TLHA( final int constant, final String label )
  {
    value = constant;
    name = label;
  }

  @Override
  public String toString()
  {
    return name;
  }
}
