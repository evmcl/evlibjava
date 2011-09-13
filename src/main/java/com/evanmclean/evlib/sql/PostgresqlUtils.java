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
package com.evanmclean.evlib.sql;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * Utilities specific to the Postgres JDBC database.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class PostgresqlUtils
{
  /**
   * Read the current value from a sequence.
   * 
   * @param name
   *        The name of the sequence.
   * @param conn
   *        The database connection to use.
   * @return The current value of the sequence.
   * @throws SQLException
   */
  public static int getCurrVal( final String name, final Connection conn )
    throws SQLException
  {
    final Statement stmt = conn.createStatement();
    try
    {
      final ResultSet rs = stmt.executeQuery("SELECT CURRVAL('" + name + "')");
      if ( !rs.next() )
        throw new SQLException("Could not read sequence: " + name);
      return rs.getInt(1);
    }
    finally
    {
      stmt.close();
    }
  }

  /**
   * Read the next value from a sequence.
   * 
   * @param name
   *        The name of the sequence.
   * @param conn
   *        The database connection to use.
   * @return The next value of the sequence.
   * @throws SQLException
   */
  public static int getNextVal( final String name, final Connection conn )
    throws SQLException
  {
    final Statement stmt = conn.createStatement();
    try
    {
      final ResultSet rs = stmt.executeQuery("SELECT NEXTVAL('" + name + "')");
      if ( !rs.next() )
        throw new SQLException("Could not read sequence: " + name);
      return rs.getInt(1);
    }
    finally
    {
      stmt.close();
    }
  }

  /**
   * <p>
   * Converts an array of integers to an Array object that can be used in a
   * PreparedStatement for Postgresql to set an <tt>int4[]</tt> column.
   * </p>
   * 
   * <p>
   * <i>FYI:</i> When reading an int array column from Postgres, you can take
   * the <tt>Array</tt> object returned by <tt>ResultSet.getArray</tt> and get
   * the details by the code &quot;<tt>(int[]) arrobject.getArray()</tt>&quot;,
   * or you could use the <tt>getResultSet</tt> method instead.
   * </p>
   * 
   * <p>
   * <b>NOTE:</b> If you are using a JDBC driver that fully implements java 1.6
   * Connection objects then use <code>Connection.createArrayOf()</code> instead
   * of this function.
   * </p>
   * 
   * @param arr
   *        The array of integers.
   * @return The SQL Array object, if a null array was specified.
   */
  public static Array intToArray( final int[] arr )
  {
    return intToArray(arr, "int4");
  }

  /**
   * <p>
   * Converts an array of integers to an Array object that can be used in a
   * PreparedStatement for Postgresql.
   * </p>
   * 
   * <p>
   * <i>FYI:</i> When reading an int array column from Postgres, you can take
   * the <tt>Array</tt> object returned by <tt>ResultSet.getArray</tt> and get
   * the details by the code &quot;<tt>(int[]) arrobject.getArray()</tt>&quot;,
   * or you could use the <tt>getResultSet</tt> method instead.
   * </p>
   * 
   * <p>
   * <b>NOTE:</b> If you are using a JDBC driver that fully implements java 1.6
   * Connection objects then use <code>Connection.createArrayOf()</code> instead
   * of this function.
   * </p>
   * 
   * @param arr
   *        The array of integers.
   * @param base_type_name
   *        The base type of the integer array column you are trying to set.
   *        Will usually be <tt>int4</tt>, or one of the other integer types
   *        such as <tt>integer</tt>, <tt>int</tt>, <tt>int2</tt>, <tt>int8</tt>
   *        , <tt>smallint</tt> or <tt>bigint</tt>.
   * @return The SQL Array object, if a null array was specified.
   */
  public static Array intToArray( final int[] arr, final String base_type_name )
  {
    if ( arr == null )
      return null;
    return new Array() {
      @SuppressWarnings( "unused" )
      public void free()
      {
        // empty
      }

      public Object getArray()
      {
        throw new RuntimeException("Should never call this.");
      }

      public Object getArray( final long index, final int count )
      {
        throw new RuntimeException("Should never call this.");
      }

      public Object getArray( final long index, final int count,
          final Map<String, Class<?>> map )
      {
        throw new RuntimeException("Should never call this.");
      }

      public Object getArray( final Map<String, Class<?>> map )
      {
        throw new RuntimeException("Should never call this.");
      }

      public int getBaseType()
      {
        throw new RuntimeException("Should never call this.");
      }

      public String getBaseTypeName()
      {
        return base_type_name;
      }

      public ResultSet getResultSet()
      {
        throw new RuntimeException("Should never call this.");
      }

      public ResultSet getResultSet( final long index, final int count )
      {
        throw new RuntimeException("Should never call this.");
      }

      public ResultSet getResultSet( final long index, final int count,
          final Map<String, Class<?>> map )
      {
        throw new RuntimeException("Should never call this.");
      }

      public ResultSet getResultSet( final Map<String, Class<?>> map )
      {
        throw new RuntimeException("Should never call this.");
      }

      @Override
      public String toString()
      {
        final StringBuilder buff = new StringBuilder("{");
        if ( arr.length != 0 )
        {
          buff.append(arr[0]);
          for ( int xi = 1; xi < arr.length; xi++ )
          {
            buff.append(',');
            buff.append(arr[xi]);
          }
        }
        buff.append('}');
        return buff.toString();
      }
    };
  }

  /**
   * <p>
   * Converts an array of Strings to an Array object that can be used in a
   * PreparedStatement for Postgresql to set an <tt>varchar[]</tt> column.
   * </p>
   * 
   * <p>
   * <i>FYI:</i> When reading a varchar array column from Postgres, you can take
   * the <tt>Array</tt> object returned by <tt>ResultSet.getArray</tt> and get
   * the details by the code &quot;<tt>(String[]) arrobject.getArray()</tt>
   * &quot;, or you could use the <tt>getResultSet</tt> method instead.
   * </p>
   * 
   * <p>
   * <b>NOTE:</b> If you are using a JDBC driver that fully implements java 1.6
   * Connection objects then use <code>Connection.createArrayOf()</code> instead
   * of this function.
   * </p>
   * 
   * @param arr
   *        The array of Strings.
   * @return The SQL Array object, if a null array was null.
   */
  public static Array stringToArray( final String[] arr )
  {
    return stringToArray(arr, "varchar");
  }

  /**
   * <p>
   * Converts an array of Strings to an Array object that can be used in a
   * PreparedStatement for Postgresql.
   * </p>
   * 
   * <p>
   * <i>FYI:</i> When reading a varchar array column from Postgres, you can take
   * the <tt>Array</tt> object returned by <tt>ResultSet.getArray</tt> and get
   * the details by the code &quot;<tt>(String[]) arrobject.getArray()</tt>
   * &quot;, or you could use the <tt>getResultSet</tt> method instead.
   * </p>
   * 
   * <p>
   * <b>NOTE:</b> If you are using a JDBC driver that fully implements java 1.6
   * Connection objects then use <code>Connection.createArrayOf()</code> instead
   * of this function.
   * </p>
   * 
   * @param arr
   *        The array of Strings.
   * @param base_type_name
   *        The base type of the String array column you are trying to set. Will
   *        usually be <tt>varchar</tt>.
   * @return The SQL Array object, if a null array was null.
   */
  public static Array stringToArray( final String[] arr,
      final String base_type_name )
  {
    if ( arr == null )
      return null;
    return new Array() {
      @SuppressWarnings( "unused" )
      public void free()
      {
        // empty
      }

      public Object getArray()
      {
        throw new RuntimeException("Should never call this.");
      }

      public Object getArray( final long index, final int count )
      {
        throw new RuntimeException("Should never call this.");
      }

      public Object getArray( final long index, final int count,
          final Map<String, Class<?>> map )
      {
        throw new RuntimeException("Should never call this.");
      }

      public Object getArray( final Map<String, Class<?>> map )
      {
        throw new RuntimeException("Should never call this.");
      }

      public int getBaseType()
      {
        throw new RuntimeException("Should never call this.");
      }

      public String getBaseTypeName()
      {
        return base_type_name;
      }

      public ResultSet getResultSet()
      {
        throw new RuntimeException("Should never call this.");
      }

      public ResultSet getResultSet( final long index, final int count )
      {
        throw new RuntimeException("Should never call this.");
      }

      public ResultSet getResultSet( final long index, final int count,
          final Map<String, Class<?>> map )
      {
        throw new RuntimeException("Should never call this.");
      }

      public ResultSet getResultSet( final Map<String, Class<?>> map )
      {
        throw new RuntimeException("Should never call this.");
      }

      @Override
      public String toString()
      {
        final StringBuilder buff = new StringBuilder("{");
        if ( arr.length != 0 )
        {
          appendString(buff, arr[0]);
          for ( int xi = 1; xi < arr.length; xi++ )
          {
            buff.append(',');
            appendString(buff, arr[xi]);
          }
        }
        buff.append('}');
        return buff.toString();
      }

      private void appendString( final StringBuilder buff, final String str )
      {
        final int len = str.length();
        buff.append('"');
        for ( int xi = 0; xi < len; ++xi )
        {
          final char ch = str.charAt(xi);
          switch ( ch )
          {
            case '"':
              buff.append("\\\"");
              break;

            case '\\':
              buff.append("\\\\");
              break;

            default:
              buff.append(ch);
              break;
          }
        }
        buff.append('"');
      }
    };
  }

  private PostgresqlUtils()
  {
    // empty
  }
}
