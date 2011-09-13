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

import java.sql.ResultSet;
import java.sql.SQLException;

import com.evanmclean.evlib.lang.Str;

/**
 * A bunch of static utility functions related to result sets.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class Rs
{
  /**
   * Retrieves a double from a result set, checking for null.
   * 
   * @param rs
   *        The result set.
   * @param col_idx
   *        The index of the column to retrieve.
   * @return Null if the column was null, or the value of the column.
   * @throws SQLException
   */
  public static Double getDouble( final ResultSet rs, final int col_idx )
    throws SQLException
  {
    final double ret = rs.getDouble(col_idx);
    if ( rs.wasNull() )
      return null;
    return ret;
  }

  /**
   * Retrieves a double from a result set, checking for null.
   * 
   * @param rs
   *        The result set.
   * @param col_name
   *        The name of the column to retrieve.
   * @return Null if the column was null, or the value of the column.
   * @throws SQLException
   */
  public static Double getDouble( final ResultSet rs, final String col_name )
    throws SQLException
  {
    final double ret = rs.getDouble(col_name);
    if ( rs.wasNull() )
      return null;
    return ret;
  }

  /**
   * Retrieves an integer from a result set, checking for null.
   * 
   * @param rs
   *        The result set.
   * @param col_idx
   *        The index of the column to retrieve.
   * @return Null if the column was null, or the value of the column.
   * @throws SQLException
   */
  public static Integer getInt( final ResultSet rs, final int col_idx )
    throws SQLException
  {
    final int ret = rs.getInt(col_idx);
    if ( rs.wasNull() )
      return null;
    return ret;
  }

  /**
   * Retrieves an integer from a result set, checking for null.
   * 
   * @param rs
   *        The result set.
   * @param col_name
   *        The name of the column to retrieve.
   * @return Null if the column was null, or the value of the column.
   * @throws SQLException
   */
  public static Integer getInt( final ResultSet rs, final String col_name )
    throws SQLException
  {
    final int ret = rs.getInt(col_name);
    if ( rs.wasNull() )
      return null;
    return ret;
  }

  /**
   * Retrieves an integer from a result set, nulls and zeros return null.
   * 
   * @param rs
   *        The result set.
   * @param col_idx
   *        The index of the column to retrieve.
   * @return Null if the column was null or zero, or the value of the column.
   * @throws SQLException
   */
  public static Integer getIntZN( final ResultSet rs, final int col_idx )
    throws SQLException
  {
    final int ret = rs.getInt(col_idx);
    if ( ret == 0 )
      return null;
    if ( rs.wasNull() )
      return null;
    return ret;
  }

  /**
   * Retrieves an integer from a result set, nulls and zeros return null.
   * 
   * @param rs
   *        The result set.
   * @param col_name
   *        The name of the column to retrieve.
   * @return Null if the column was null or zero, or the value of the column.
   * @throws SQLException
   */
  public static Integer getIntZN( final ResultSet rs, final String col_name )
    throws SQLException
  {
    final int ret = rs.getInt(col_name);
    if ( ret == 0 )
      return null;
    if ( rs.wasNull() )
      return null;
    return ret;
  }

  /**
   * Retrieves a trimmed string from a result set. Null returns an empty string.
   * 
   * @param rs
   *        The result set.
   * @param col_idx
   *        The index of the column to retrieve.
   * @return The trimmed string.
   * @throws SQLException
   */
  public static String getTrimString( final ResultSet rs, final int col_idx )
    throws SQLException
  {
    return Str.trimToEmpty(rs.getString(col_idx));
  }

  /**
   * Retrieves a trimmed string from a result set. Null returns an empty string.
   * 
   * @param rs
   *        The result set.
   * @param col_name
   *        The name of the column to retrieve.
   * @return The trimmed string.
   * @throws SQLException
   */
  public static String getTrimString( final ResultSet rs, final String col_name )
    throws SQLException
  {
    return Str.trimToEmpty(rs.getString(col_name));
  }

  private Rs()
  {
    // empty
  }
}
