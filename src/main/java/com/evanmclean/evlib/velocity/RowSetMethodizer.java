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
package com.evanmclean.evlib.velocity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * <p>
 * A wrapper around the JDBC ResultSet that allows Velicoty templates to access
 * the columns in a template friendly way.
 * </p>
 * 
 * <p>
 * This class reads and stores the entire result set from within the
 * constructor, so you can close the result set after instantiation. You do not
 * need to keep the result set (or the statement it came from) open while
 * templates are merged.
 * </p>
 * 
 * <p>
 * See {@link ResultSetMethodizer} for more details.
 * </p>
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class RowSetMethodizer extends AbstractResultSetMethodizer
{
  private class RowSet extends Row
  {
    private final Object[] data;

    RowSet( final Object[] data )
    {
      this.data = data;
    }

    @Override
    Object get( int column_index )
    {
      return data[column_index - 1];
    }
  }

  private final Iterator<RowSet> it;

  public RowSetMethodizer( final ResultSet rs ) throws SQLException
  {
    super(rs);
    final int numcols = rs.getMetaData().getColumnCount();
    final Collection<RowSet> rows = new ArrayList<RowSet>();
    while ( rs.next() )
    {
      final Object[] data = new Object[numcols];
      for ( int xi = 1; xi <= numcols; ++xi )
        data[xi - 1] = rs.getObject(xi);
      rows.add(new RowSet(data));
    }

    it = rows.iterator();
  }

  public boolean hasNext()
  {
    return it.hasNext();
  }

  public Row next()
  {
    return it.next();
  }
}
