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

import info.clearthought.layout.TableLayout;

/**
 * Utility to make it easy to create TableLayout objects with the specified rows
 * and columns. See <a
 * href="package-summary.html#package_description">summary</a> for an example of
 * typical usage.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class TableLayoutFactory implements TL
{
  /**
   * Create a factory builder, with horizontal and vertical gaps of zero.
   * 
   * @return The new builder.
   */
  public static TableLayoutFactory build()
  {
    return new TableLayoutFactory();
  }

  /**
   * Create a factory builder, with a specified horizontal and vertical gap.
   * 
   * @param gap
   *        The value for both the horizontal and vertical gaps.
   * 
   * @return The new builder.
   */
  public static TableLayoutFactory build( final int gap )
  {
    return new TableLayoutFactory(gap);
  }

  /**
   * Create a factory builder, with a specified horizontal and vertical gaps.
   * 
   * @param hgap
   *        The horizontal gap.
   * @param vgap
   *        The vertical gap.
   * 
   * @return The new builder.
   */
  public static TableLayoutFactory build( final int hgap, final int vgap )
  {
    return new TableLayoutFactory(hgap, vgap);
  }

  public double[] cols;
  public double[] rows;
  public int vgap;
  public int hgap;

  /**
   * Create a factory.
   */
  private TableLayoutFactory()
  {
    this(0, 0);
  }

  /**
   * Create a factory.
   */
  private TableLayoutFactory( final int gap )
  {
    this(gap, gap);
  }

  /**
   * Create a factory.
   */
  private TableLayoutFactory( final int hgap, final int vgap )
  {
    this.hgap = hgap;
    this.vgap = vgap;
  }

  /**
   * Set the columns to be used in the layout.
   * 
   * @param colargs
   * @return Factory to allow stringing method calls together.
   */
  public TableLayoutFactory cols( final double... colargs )
  {
    cols = colargs;
    return this;
  }

  /**
   * Set the columns to be used to either FILL or PREFERRED depending on if the
   * boolean arg is true or false respectively.
   * 
   * @param colargs
   * @return Factory to allow stringing method calls together.
   */
  public TableLayoutFactory fillcols( final boolean... colargs )
  {
    cols = conv(FILL, PREFERRED, colargs);
    return this;
  }

  /**
   * Set the rows to be used to either FILL or PREFERRED depending on if the
   * boolean arg is true or false respectively.
   * 
   * @param rowargs
   * @return Factory to allow stringing method calls together.
   */
  public TableLayoutFactory fillrows( final boolean... rowargs )
  {
    rows = conv(FILL, PREFERRED, rowargs);
    return this;
  }

  /**
   * Set the horizontal and vertical gaps to the same value.
   * 
   * @param gap
   * @return Factory to allow stringing method calls together.
   */
  public TableLayoutFactory gap( final int gap )
  {
    this.hgap = gap;
    this.vgap = gap;
    return this;
  }

  /**
   * Set the vertical gap.
   * 
   * @param hgap
   * @param vgap
   * @return Factory to allow stringing method calls together.
   */
  public TableLayoutFactory gap( @SuppressWarnings( "hiding" ) final int hgap,
      @SuppressWarnings( "hiding" ) final int vgap )
  {
    this.hgap = hgap;
    this.vgap = vgap;
    return this;
  }

  /**
   * Set the horizontal gap.
   * 
   * @param hgap
   * @return Factory to allow stringing method calls together.
   */
  public TableLayoutFactory hgap( @SuppressWarnings( "hiding" ) final int hgap )
  {
    this.hgap = hgap;
    return this;
  }

  /**
   * Create the table layout with the specified rows and columns. Will throw
   * {@link TLFException} if the columns or rows were not specified, or either
   * of the arrays is zero length.
   * 
   * @return The new table layout.
   * @throws TLFException
   *         If the columns or rows were not specified, or either of the arrays
   *         is zero length.
   */
  public TableLayout make()
  {
    if ( (cols == null) || (cols.length < 1) )
      throw new TLFException("No columns specified.");
    if ( (rows == null) || (rows.length < 1) )
      throw new TLFException("No rows specified.");
    if ( hgap < 0 )
      throw new TLFException("Horizontal gap cannot be less than zero.");
    if ( vgap < 0 )
      throw new TLFException("Vertical gap cannot be less than zero.");
    final TableLayout layout = new TableLayout(cols, rows);
    layout.setHGap(hgap);
    layout.setVGap(vgap);
    return layout;
  }

  /**
   * Set the rows to be used in the layout.
   * 
   * @param rowargs
   * @return Factory to allow stringing method calls together.
   */
  public TableLayoutFactory rows( final double... rowargs )
  {
    rows = rowargs;
    return this;
  }

  /**
   * Set the vertical gap.
   * 
   * @param vgap
   * @return Factory to allow stringing method calls together.
   */
  public TableLayoutFactory vgap( @SuppressWarnings( "hiding" ) final int vgap )
  {
    this.vgap = vgap;
    return this;
  }

  private double[] conv( final double trueval, final double falseval,
      final boolean[] arg )
  {
    final double[] arr = new double[arg.length];
    for ( int xi = 0; xi < arr.length; ++xi )
      arr[xi] = arg[xi] ? trueval : falseval;
    return arr;
  }
}
