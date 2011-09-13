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

import org.apache.velocity.exception.VelocityException;

/**
 * <p>
 * A wrapper around a JDBC ResultSet that allows Velocity templates to access
 * the columns in a template friendly way.
 * </p>
 * 
 * <p>
 * If you had a result set based on the following SQL statement:
 * </p>
 * 
 * <pre>
 * SELECT user_id, first_name, surname
 * FROM users
 * </pre>
 * 
 * <p>
 * And you add the result set to your context, wrapped in this methodizer, such
 * as:
 * </p>
 * 
 * <pre>
 * context.put(&quot;query&quot;, new ResultSetMethodizer(result_set));
 * </pre>
 * 
 * <p>
 * Then in your velocity template you could access it like this:
 * </p>
 * 
 * <pre>
 * #foreach( $row in $query )
 *   The ID for $row.FirstName $row.Surname is $row.UserId
 * #end
 * </pre>
 * 
 * <p>
 * Note that the result set must remain open while the template is merged. If
 * you want a version that stores the results so you can close it immediately,
 * use {@link RowSetMethodizer}.
 * </p>
 * 
 * <p>
 * See {@link AbstractResultSetMethodizer#getPropertyNames(String)} for details
 * on how column names are mapped to property names by default.
 * </p>
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class ResultSetMethodizer extends AbstractResultSetMethodizer
{
  private final ResultSet rs;

  private final Row row = new Row() {
    @SuppressWarnings( "synthetic-access" )
    @Override
    Object get( int column_index )
    {
      try
      {
        return rs.getObject(column_index);
      }
      catch ( SQLException ex )
      {
        throw new VelocityException(ex);
      }
    }
  };

  public ResultSetMethodizer( final ResultSet rs ) throws SQLException
  {
    super(rs);
    this.rs = rs;
  }

  public boolean hasNext()
  {
    try
    {
      return !rs.isLast();
    }
    catch ( SQLException ex )
    {
      throw new VelocityException(ex);
    }
  }

  public Row next()
  {
    return row;
  }
}
