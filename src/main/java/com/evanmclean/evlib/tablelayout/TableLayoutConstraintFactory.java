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
import info.clearthought.layout.TableLayoutConstraints;

/**
 * Utility to make it easy to create TableLayoutConstraint objects for the
 * TableLayout layout manager. See <a
 * href="package-summary.html#package_description">summary</a> for an example of
 * typical usage.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class TableLayoutConstraintFactory
{
  /**
   * This is the builder that actually performs the assembly of constraints.
   */
  public class Builder
  {
    /**
     * Cell in which the upper left corner of the component lays.
     */
    int col1 = 0;

    /**
     * Cell in which the lower right corner of the component lays.
     */
    int col2 = 0;

    /**
     * Cell in which the upper left corner of the component lays.
     */
    int row1 = 0;

    /**
     * Cell in which the lower right corner of the component lays.
     */
    int row2 = 0;

    /**
     * Horizontal justification.
     */
    TLHA hAlign = TLHA.FULL;

    /**
     * Vertical justification.
     */
    TLVA vAlign = TLVA.FULL;

    Builder()
    {
      // empty
    }

    Builder( final int col, final int row )
    {
      col1 = col;
      col2 = col;
      row1 = row;
      row2 = row;
    }

    /**
     * Sets the horizontal alignment to the specified enumerated value.
     * 
     * @param halign
     * @return Builder to allow stringing method calls together.
     */
    public Builder align( final TLHA halign )
    {
      hAlign = halign;
      return this;
    }

    /**
     * Sets the vertical alignment to the specified enumerated value.
     * 
     * @param valign
     * @return Builder to allow stringing method calls together.
     */
    public Builder align( final TLVA valign )
    {
      vAlign = valign;
      return this;
    }

    /**
     * Sets the vertical alignment to the bottom of the cell.
     * 
     * @return Builder to allow stringing method calls together.
     */
    public Builder bottom()
    {
      vAlign = TLVA.BOTTOM;
      return this;
    }

    /**
     * Set the cell column, keeping the effective width constant. i.e. Changes
     * the value of col1 and col2 by the same amount.
     * 
     * @param col
     * @return Builder to allow stringing method calls together.
     */
    public Builder col( final int col )
    {
      final int diff = col - col1;
      col1 += diff;
      col2 += diff;
      return this;
    }

    /**
     * Sets the value for col1, without modifying col2 &mdash; You would
     * probably prefer to use {@link #col(int)}
     * 
     * @param col
     * @return Builder to allow stringing method calls together.
     * @see #col(int)
     */
    public Builder col1( final int col )
    {
      col1 = col;
      return this;
    }

    /**
     * Sets the value for col2, without modifying col1 &mdash; You would
     * probably prefer to use {@link #col(int)}
     * 
     * @param col
     * @return Builder to allow stringing method calls together.
     * @see #col(int)
     */
    public Builder col2( final int col )
    {
      col2 = col;
      return this;
    }

    /**
     * Sets the horizontal alignment to center.
     * 
     * @return Builder to allow stringing method calls together.
     */
    public Builder hcenter()
    {
      hAlign = TLHA.CENTER;
      return this;
    }

    /**
     * Sets the horizontal alignment to centre.
     * 
     * @return Builder to allow stringing method calls together.
     */
    public Builder hcentre()
    {
      hAlign = TLHA.CENTRE;
      return this;
    }

    /**
     * Sets the number of rows that will be spanned. Modifies row2 to be the
     * proper offset to row1.
     * 
     * @param height
     * @return Builder to allow stringing method calls together.
     */
    public Builder height( final int height )
    {
      row2 = row1 + height - 1;
      return this;
    }

    /**
     * Sets the horizontal alignment to full.
     * 
     * @return Builder to allow stringing method calls together.
     */
    public Builder hfull()
    {
      hAlign = TLHA.FULL;
      return this;
    }

    /**
     * Sets the horizontal alignment to leading.
     * 
     * @return Builder to allow stringing method calls together.
     */
    public Builder hleading()
    {
      hAlign = TLHA.LEADING;
      return this;
    }

    /**
     * Sets the horizontal alignment to left.
     * 
     * @return Builder to allow stringing method calls together.
     */
    public Builder hleft()
    {
      hAlign = TLHA.LEFT;
      return this;
    }

    /**
     * Sets the horizontal alignment to right.
     * 
     * @return Builder to allow stringing method calls together.
     */
    public Builder hright()
    {
      hAlign = TLHA.RIGHT;
      return this;
    }

    /**
     * Sets the horizontal alignment to trailing.
     * 
     * @return Builder to allow stringing method calls together.
     */
    public Builder htrailing()
    {
      hAlign = TLHA.TRAILING;
      return this;
    }

    /**
     * Sets the horizontal alignment to leading.
     * 
     * @return Builder to allow stringing method calls together.
     */
    public Builder leading()
    {
      hAlign = TLHA.LEADING;
      return this;
    }

    /**
     * Sets the horizontal alignment to left.
     * 
     * @return Builder to allow stringing method calls together.
     */
    public Builder left()
    {
      hAlign = TLHA.LEFT;
      return this;
    }

    /**
     * Make the table layout constraint object based on the current settings in
     * the builder. A {@link TLCFException} will be thrown if the constraints do
     * not make sense (e.g., col2 less than col1, the column range is exceeded,
     * and such.)
     * 
     * @return The TableLayoutConstraints object.
     * @throws TLCFException
     *         if the constraints to not make sense.
     */
    public TableLayoutConstraints make()
    {
      if ( col2 < col1 )
        throw new TLCFException("Column 2 less than column 1: " + col2 + " < "
            + col1);
      if ( row2 < row1 )
        throw new TLCFException("Row 2 less than row 1: " + row2 + " < " + row1);
      if ( hAlign == null )
        throw new TLCFException("Horizonal alignment unspecified (null).");
      if ( vAlign == null )
        throw new TLCFException("Horizonal alignment unspecified (null).");
      if ( (col1 < 0) || ((cols > 0) && (col2 >= cols)) )
      {
        if ( col1 == col2 )
          throw new TLCFException("Column " + col1
              + " outside allowed column range 0-" + (cols - 1));
        throw new TLCFException("Column range " + col1 + '-'
            + " outside allowed column range 0-" + (cols - 1));
      }
      if ( (row1 < 0) || ((rows > 0) && (row2 >= rows)) )
      {
        if ( row1 == row2 )
          throw new TLCFException("Row " + row1
              + " outside allowed row range 0-" + (rows - 1));
        throw new TLCFException("Row range " + row1 + '-'
            + " outside allowed row range 0-" + (rows - 1));
      }

      return new TableLayoutConstraints(col1, row1, col2, row2, hAlign.value,
          vAlign.value);
    }

    /**
     * Sets the horizontal alignment to right.
     * 
     * @return Builder to allow stringing method calls together.
     */
    public Builder right()
    {
      hAlign = TLHA.RIGHT;
      return this;
    }

    /**
     * Set the cell row, keeping the effective height constant. i.e. Changes the
     * value of row1 and row2 by the same amount.
     * 
     * @param row
     * @return Builder to allow stringing method calls together.
     */
    public Builder row( final int row )
    {
      final int diff = row - row1;
      row1 += diff;
      row2 += diff;
      return this;
    }

    /**
     * Sets the value for row1, without modifying row2 &mdash; You would
     * probably prefer to use {@link #row(int)}
     * 
     * @param row
     * @return Builder to allow stringing method calls together.
     * @see #row(int)
     */
    public Builder row1( final int row )
    {
      row1 = row;
      return this;
    }

    /**
     * Sets the value for row2, without modifying row1 &mdash; You would
     * probably prefer to use {@link #row(int)}
     * 
     * @param row
     * @return Builder to allow stringing method calls together.
     * @see #row(int)
     */
    public Builder row2( final int row )
    {
      row2 = row;
      return this;
    }

    /**
     * Sets the vertical alignment to the top of the cell.
     * 
     * @return Builder to allow stringing method calls together.
     */
    public Builder top()
    {
      vAlign = TLVA.TOP;
      return this;
    }

    /**
     * Returns a textual representation of the settings in the builder (useful
     * for debugging).
     */
    @Override
    public String toString()
    {
      final StringBuilder buff = new StringBuilder("[col1=");
      buff.append(col1);
      buff.append(", row1=");
      buff.append(row1);
      buff.append(", col2=");
      buff.append(col2);
      buff.append(", row2=");
      buff.append(row2);
      buff.append(", halign=");
      buff.append(hAlign.toString());
      buff.append(", valign=");
      buff.append(vAlign.toString());
      buff.append(']');
      return buff.toString();
    }

    /**
     * Sets the horizontal alignment to trailing.
     * 
     * @return Builder to allow stringing method calls together.
     */
    public Builder trailing()
    {
      hAlign = TLHA.TRAILING;
      return this;
    }

    /**
     * Sets the vertical alignment to the bottom of the cell.
     * 
     * @return Builder to allow stringing method calls together.
     */
    public Builder vbottom()
    {
      vAlign = TLVA.BOTTOM;
      return this;
    }

    /**
     * Sets the vertical alignment to center.
     * 
     * @return Builder to allow stringing method calls together.
     */
    public Builder vcenter()
    {
      vAlign = TLVA.CENTER;
      return this;
    }

    /**
     * Sets the vertical alignment to centre.
     * 
     * @return Builder to allow stringing method calls together.
     */
    public Builder vcentre()
    {
      vAlign = TLVA.CENTRE;
      return this;
    }

    /**
     * Sets the vertical alignment to full.
     * 
     * @return Builder to allow stringing method calls together.
     */
    public Builder vfull()
    {
      vAlign = TLVA.FULL;
      return this;
    }

    /**
     * Sets the vertical alignment to the top of the cell.
     * 
     * @return Builder to allow stringing method calls together.
     */
    public Builder vtop()
    {
      vAlign = TLVA.TOP;
      return this;
    }

    /**
     * Sets the number of columns that will be spanned. Modifies col2 to be the
     * proper offset to col1.
     * 
     * @param width
     * @return Builder to allow stringing method calls together.
     */
    public Builder width( final int width )
    {
      col2 = col1 + width - 1;
      return this;
    }
  }

  /**
   * The number of columns in this layout (0 for unspecified).
   */
  public final int cols;

  /**
   * The number of rows in this layout (0 for unspecified).
   */
  public final int rows;

  /**
   * Create a factory with no specified number of columns and rows.
   */
  public TableLayoutConstraintFactory()
  {
    this(0, 0);
  }

  /**
   * Create a factory with the specified number of columns and rows.
   * 
   * @param cols
   * @param rows
   */
  public TableLayoutConstraintFactory( final int cols, final int rows )
  {
    this.cols = cols;
    this.rows = rows;
  }

  /**
   * Create a factory with the number of columns and rows as defined in the
   * specified layout.
   * 
   * @param layout
   */
  public TableLayoutConstraintFactory( final TableLayout layout )
  {
    this(layout.getNumColumn(), layout.getNumRow());
  }

  /**
   * Create a builder initially set at column 0, row 0, width of 1, height of 1,
   * and full alignment both horizontally and vertically.
   * 
   * @return The new builder.
   */
  public Builder build()
  {
    return new Builder();
  }

  /**
   * Create a builder initially width the specified column and row position,
   * width of 1, height of 1, and full alignment both horizontally and
   * vertically.
   * 
   * @param col
   * @param row
   * @return The new builder.
   */
  public Builder build( final int col, final int row )
  {
    return new Builder(col, row);
  }
}
