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
package com.evanmclean.evlib.commons.dbcp;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.ObjectPool;

/**
 * <p>
 * Misc handy functions for the <a href="http://commons.apache.org/dbcp/"
 * target="_blank">Apache Database Connection Pooling</a> package.
 * </p>
 * 
 * <p>
 * <em>NOTE:</em> The <a href="http://sourceforge.net/projects/c3p0/"
 * target="_blank">C3P0</a> library is generally a better database connection
 * pooling library all round.
 * </p>
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class DbcpUtils
{
  /**
   * Close the specifically named DBCP pool.
   * 
   * @param name
   *        The name of the pool to close.
   * @throws SQLException
   */
  public static void closePool( final String name ) throws SQLException
  {
    final PoolingDriver driver = getPoolingDriver();
    if ( driver != null )
      driver.closePool(name);
  }

  /**
   * Close all DBCP pools.
   */
  public static void closePools()
  {
    final PoolingDriver driver = getPoolingDriver();
    if ( driver != null )
    {
      final String[] pns = driver.getPoolNames();
      for ( String pn : pns )
        try
        {
          final ObjectPool pool = driver.getConnectionPool(pn);
          if ( pool != null )
            pool.clear();
        }
        catch ( Throwable ex )
        {
          // empty
        }
    }
  }

  /**
   * Get the DBCP pooling driver.
   * 
   * @return Return the DBCP pooling driver or null.
   */
  public static PoolingDriver getPoolingDriver()
  {
    PoolingDriver pdriver = null;
    final Enumeration<Driver> drivers = DriverManager.getDrivers();
    while ( drivers.hasMoreElements() )
    {
      final Driver driver = drivers.nextElement();
      if ( driver instanceof PoolingDriver )
      {
        if ( pdriver == null )
          pdriver = (PoolingDriver) driver;
        else
          throw new IllegalStateException("More than one DBCP pooling driver.");
      }
    }
    return pdriver;
  }

  private DbcpUtils()
  {
    // empty
  }
}
