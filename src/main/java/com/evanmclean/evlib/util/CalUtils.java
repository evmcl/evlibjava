/*
 * = License =
 * 
 * McLean Computer Services Open Source Software License
 * 
 * (Looks like the BSD license, but less restrictive.)
 * 
 * Copyright (c) 2006-2011 Evan McLean. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * 2. Neither the names "Evan McLean", "McLean Computer Services", "EvLib" nor
 * the names of any contributors may be used to endorse or promote products
 * derived from this software without prior written permission.
 * 
 * 3. Products derived from this software may not be called "Evlib", nor may
 * "Evlib" appear in their name, without prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * = License =
 */
package com.evanmclean.evlib.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Misc utilities for handling Calendar objects.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class CalUtils
{
  /**
   * Return a date, set to midnight of the current local day.
   * 
   * @return Return a date, set to midnight of the current local day.
   */
  public static Date today()
  {
    final Calendar cal = Calendar.getInstance();
    setMidnight(cal);
    return cal.getTime();
  }

  /**
   * Return a date, set to midnight of the current day for the given time zone.
   * 
   * @param tz
   * @return Return a date, set to midnight of the current day for the given
   *         time zone.
   */
  public static Date today( final TimeZone tz )
  {
    final Calendar cal = Calendar.getInstance(tz);
    setMidnight(cal);
    return cal.getTime();
  }

  /**
   * Set the time component of the calendar to midday.
   * 
   * @param cal
   */
  public static void setMidday( final Calendar cal )
  {
    cal.set(Calendar.MILLISECOND, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.HOUR_OF_DAY, 12);
  }

  /**
   * Set the time component of the date to midday, using the local time zone.
   * 
   * @param dt
   */
  public static void setMidday( final Date dt )
  {
    final Calendar cal = Calendar.getInstance();
    cal.setTime(dt);
    setMidday(cal);
    dt.setTime(cal.getTimeInMillis());
  }

  /**
   * Set the time component of the date to midday.
   * 
   * @param dt
   * @param tz
   */
  public static void setMidday( final Date dt, final TimeZone tz )
  {
    final Calendar cal = Calendar.getInstance(tz);
    cal.setTime(dt);
    setMidday(cal);
    dt.setTime(cal.getTimeInMillis());
  }

  /**
   * Set the time component of the calendar to midnight.
   * 
   * @param cal
   */
  public static void setMidnight( final Calendar cal )
  {
    cal.set(Calendar.MILLISECOND, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.HOUR_OF_DAY, 0);
  }

  /**
   * Set the time component of the date to midnight, using the local time zone.
   * 
   * @param dt
   */
  public static void setMidnight( final Date dt )
  {
    final Calendar cal = Calendar.getInstance();
    cal.setTime(dt);
    setMidnight(cal);
    dt.setTime(cal.getTimeInMillis());
  }

  /**
   * Set the time component of the date to midnight.
   * 
   * @param dt
   * @param tz
   */
  public static void setMidnight( final Date dt, final TimeZone tz )
  {
    final Calendar cal = Calendar.getInstance(tz);
    cal.setTime(dt);
    setMidnight(cal);
    dt.setTime(cal.getTimeInMillis());
  }

  private CalUtils()
  {
    // empty
  }
}
