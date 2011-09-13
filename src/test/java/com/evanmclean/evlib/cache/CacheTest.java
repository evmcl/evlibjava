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
package com.evanmclean.evlib.cache;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.evanmclean.evlib.exceptions.UnhandledException;

/**
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class CacheTest extends MapInterfaceTest<String, String>
{
  public static final String TEST_ALL_TEST_TYPE = "UNIT";

  private final CacheManager cm = new CacheManager();
  private final AtomicInteger nextId = new AtomicInteger();

  public CacheTest()
  {
    super(false, false, true, true, true, true);
  }

  @Override
  protected String getKeyNotInPopulatedMap()
    throws UnsupportedOperationException
  {
    return "Not Expected Key";
  }

  @Override
  protected String getValueNotInPopulatedMap()
    throws UnsupportedOperationException
  {
    return "Not Expected Value";
  }

  @Override
  protected Map<String, String> makeEmptyMap()
    throws UnsupportedOperationException
  {
    try
    {
      return cm.builder().ttl(60 * 60, TimeUnit.SECONDS).build(
        "Cache " + nextId.incrementAndGet());
    }
    catch ( CacheExistsException ex )
    {
      throw new UnhandledException(ex);
    }
  }

  @Override
  protected Map<String, String> makePopulatedMap()
    throws UnsupportedOperationException
  {
    final Map<String, String> map = makeEmptyMap();
    map.put("Key 1", "Value 1");
    map.put("Key 2", "Value 2");
    map.put("Key 3", "Value 3");
    map.put("Key 4", "Value 4");
    map.put("Key 5", "Value 5");
    return map;
  }

  @Override
  protected void setUp()
  {
    cm.removeAllCaches();
    nextId.set(0);
  }

  @Override
  protected void tearDown()
  {
    cm.removeAllCaches();
  }
}
