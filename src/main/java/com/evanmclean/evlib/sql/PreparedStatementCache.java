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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Keeps a cache prepared statements, based on the SQL. Calling the close()
 * method closes and removes all statements in the cache.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class PreparedStatementCache
{
  private final Connection conn;
  private final HashMap<String, PreparedStatement> stmts = new HashMap<String, PreparedStatement>();

  /**
   * Initialise the cache to use the specified database connection.
   * 
   * @param conn
   */
  public PreparedStatementCache( final Connection conn )
  {
    this.conn = conn;
  }

  /**
   * Closes all statements in the cache. If there is an exception for one or
   * more of close calls, it will still try and close the rest of the statements
   * in the cache.
   * 
   * @throws SQLException
   */
  public void close() throws SQLException
  {
    final Iterator<PreparedStatement> it = stmts.values().iterator();
    close(it);
  }

  /**
   * Close a specific prepared statement, based on it's SQL.
   * 
   * @param sql
   *        The SQL of the statement to close.
   * @throws SQLException
   */
  public void close( final String sql ) throws SQLException
  {
    final PreparedStatement stmt = stmts.remove(sql);
    if ( stmt != null )
      stmt.close();
  }

  /**
   * Get the prepared statement based on the sql string. It is created the first
   * time it is needed.
   * 
   * @param sql
   *        The SQL for the statement.
   * @return The prepared statement.
   * @throws SQLException
   */
  public PreparedStatement get( final String sql ) throws SQLException
  {
    PreparedStatement stmt = stmts.get(sql);
    if ( stmt == null )
    {
      stmt = conn.prepareStatement(sql);
      final PreparedStatement old = stmts.put(sql, stmt);
      if ( old != null )
        old.close();
    }
    return stmt;
  }

  /**
   * Get the prepared statement based on the sql string. It is created the first
   * time it is needed. Note that the result set parameters only have effect if
   * there is no statement already in the cache with the matching SQL, otherwise
   * it is ignored.
   * 
   * @param sql
   *        The SQL for the statement.
   * @param resultSetType
   * @param resultSetConcurrency
   * @return The prepared statement.
   * @throws SQLException
   */
  public PreparedStatement get( final String sql, final int resultSetType,
      final int resultSetConcurrency ) throws SQLException
  {
    PreparedStatement stmt = stmts.get(sql);
    if ( stmt == null )
    {
      stmt = conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
      final PreparedStatement old = stmts.put(sql, stmt);
      if ( old != null )
        old.close();
    }
    return stmt;
  }

  /**
   * @return The database connection this cache uses.
   */
  public Connection getConnection()
  {
    return conn;
  }

  private void close( final Iterator<PreparedStatement> it )
    throws SQLException
  {
    try
    {
      while ( it.hasNext() )
      {
        final PreparedStatement stmt = it.next();
        it.remove();
        stmt.close();
      }
    }
    finally
    {
      if ( it.hasNext() )
        close(it);
    }
  }
}
