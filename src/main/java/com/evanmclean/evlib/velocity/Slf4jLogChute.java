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

import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A log chute for velocity that uses <a href="http://slf4j.org/">Simple Logging
 * Facade for Java</a>.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class Slf4jLogChute implements LogChute
{
  /**
   * Property key for specifying the name for the logger instance
   */
  public static final String RUNTIME_LOG_JDK_LOGGER = "runtime.log.logsystem.slf4j.logger";

  /**
   * Default name for the JDK logger instance
   */
  public static final String DEFAULT_LOG_NAME = "org.apache.velocity";

  protected Logger logger;

  public void init( final RuntimeServices rs )
  {
    String name = (String) rs.getProperty(RUNTIME_LOG_JDK_LOGGER);
    if ( name == null )
    {
      name = DEFAULT_LOG_NAME;
    }
    logger = LoggerFactory.getLogger(name);
  }

  public boolean isLevelEnabled( final int level )
  {
    switch ( level )
    {
      case LogChute.WARN_ID:
        return logger.isWarnEnabled();
      case LogChute.INFO_ID:
        return logger.isInfoEnabled();
      case LogChute.DEBUG_ID:
        return logger.isDebugEnabled();
      case LogChute.TRACE_ID:
        return logger.isTraceEnabled();
      case LogChute.ERROR_ID:
        return logger.isErrorEnabled();
      default:
        return logger.isInfoEnabled();
    }
  }

  public void log( final int level, final String message )
  {
    switch ( level )
    {
      case LogChute.WARN_ID:
        logger.warn(message);
        break;
      case LogChute.DEBUG_ID:
        logger.debug(message);
        break;
      case LogChute.TRACE_ID:
        logger.trace(message);
        break;
      case LogChute.ERROR_ID:
        logger.error(message);
        break;
      case LogChute.INFO_ID:
      default:
        logger.info(message);
        break;
    }
  }

  public void log( final int level, final String message, final Throwable ex )
  {
    switch ( level )
    {
      case LogChute.WARN_ID:
        logger.warn(message, ex);
        break;
      case LogChute.DEBUG_ID:
        logger.debug(message, ex);
        break;
      case LogChute.TRACE_ID:
        logger.trace(message, ex);
        break;
      case LogChute.ERROR_ID:
        logger.error(message, ex);
        break;
      case LogChute.INFO_ID:
      default:
        logger.info(message, ex);
        break;
    }
  }

}
