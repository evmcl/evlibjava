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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.velocity.exception.VelocityException;

import com.evanmclean.evlib.util.TreeMapIgnoreCase;

/**
 * A wrapper class around a JDBC ResultSet that allows access to the columns as
 * properties. See {@link ResultSetMethodizer} for more information.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public abstract class AbstractResultSetMethodizer
    implements Iterator<AbstractResultSetMethodizer.Row>
{
  /**
   * Each row from the result set is returned as one of these objects which does
   * the translation from property name to a column in the result set.
   * 
   * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
   *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
   *         overview for copyright and licensing.)
   */
  public abstract class Row
  {
    /**
     * Called by velocity to get the property of the specified name.
     * 
     * @param property_name
     *        The name of the property to retrieve.
     * @return The object represented by the property name.
     * @throws VelocityException
     *         If the property name is unknown.
     */
    @SuppressWarnings( "synthetic-access" )
    public Object get( final String property_name )
    {
      final Integer column_index = columnMapping.get(property_name);
      if ( column_index == null )
        throw new VelocityException("Unknown property: " + property_name);
      return get(column_index);
    }

    /**
     * Retrieve the object based on the column index.
     * 
     * @param column_index
     *        The column index, starting from one.
     * @return The object for the specified column index.
     */
    abstract Object get( int column_index );
  }

  private final Map<String, Integer> columnMapping;

  protected AbstractResultSetMethodizer( final ResultSet rs )
    throws SQLException
  {
    this(rs.getMetaData());
  }

  protected AbstractResultSetMethodizer( final ResultSetMetaData meta )
    throws SQLException
  {
    columnMapping = new TreeMapIgnoreCase<Integer>();
    final int numcols = meta.getColumnCount();
    for ( int xi = 1; xi <= numcols; ++xi )
    {
      final String column_name = meta.getColumnName(xi);
      final Collection<String> names = getPropertyNames(column_name);
      addMapping(column_name, xi);
      if ( names != null )
        for ( String name : names )
          if ( !name.equals(column_name) )
            addMapping(name, xi);
    }
  }

  /**
   * Throws UnsupportedOperationException as this functionality is not
   * supported.
   * 
   * @throws UnsupportedOperationException
   */
  public void remove()
  {
    throw new UnsupportedOperationException();
  }

  /**
   * <p>
   * A way of translating column names to potential property names. If two or
   * more columns translate to the same property name then a VelocityException
   * will be thrown from the constructor.
   * </p>
   * 
   * <p>
   * Note that the column name itself is always adding as a mapping, so you do
   * not need to include it in the returned collection. Also, all property names
   * are case-insensitive.
   * </p>
   * 
   * <p>
   * The default behaviour is to include a version of the column name with all
   * underscore characters removed. So if your result set is based on an SQL
   * statement such as:
   * </p>
   * 
   * <pre>
   * SELECT user_id
   * FROM users
   * </pre>
   * 
   * <p>
   * Then in your velocity template you could use any of the following names to
   * match it:
   * </p>
   * 
   * <ul>
   * <li><code>user_id</code></li>
   * <li><code>userid</code></li>
   * <li><code>UserId</code></li>
   * </ul>
   * 
   * @param column_name
   *        The name of the column to provide mapping translations for.
   * @return <code>null</code> if you only want the column name itself used as a
   *         property name, or a collection of other potential column names.
   */
  protected Collection<String> getPropertyNames( final String column_name )
  {
    final String name = column_name.replace("_", "");
    if ( name.equals(column_name) )
      return null;
    final Collection<String> coll = new ArrayList<String>(1);
    coll.add(name);
    return coll;
  }

  private void addMapping( String name, int column_index ) throws SQLException
  {
    if ( columnMapping.put(name, column_index) != null )
      throw new SQLException("Duplicate mapping of property name: " + name);
  }
}
