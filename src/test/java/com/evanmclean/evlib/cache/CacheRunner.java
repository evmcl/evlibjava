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
package com.evanmclean.evlib.cache;

import java.util.Random;

import org.slf4j.LoggerFactory;

import com.evanmclean.evlib.lang.MutableBoolean;

/**
 * Manages a set of {@link ConcurrentHashMapCache} caches. Caches are
 * thread-safe, expire entries after a specified time-to-live, can be bound to a
 * maximum number of entries, and can contain strong, soft or weak references to
 * their values.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
final class CacheRunner
{
  // A bit of test code to exercise things.
  public static void main( final String[] args ) throws Exception
  {
    final CacheManager cm = new CacheManager(15,
        LoggerFactory.getLogger(CacheRunner.class));
    final ConcurrentHashMapCache<Integer, Integer> map1 = cm.makeCache("one",
      10000L, true, 0, CacheReferenceType.STRONG, null, true);
    final ConcurrentHashMapCache<Integer, Integer> map2 = cm.makeCache("two",
      10000L, false, 5, CacheReferenceType.STRONG, null, true);
    final MutableBoolean running = new MutableBoolean(true);

    final Thread pusherA = new Thread() {
      @Override
      public void run()
      {
        final Random rand = new Random(System.currentTimeMillis());
        while ( running.getValue() )
        {
          map1.put(Integer.valueOf(rand.nextInt(8)),
            Integer.valueOf(rand.nextInt()));
          map2.put(Integer.valueOf(rand.nextInt(3)),
            Integer.valueOf(rand.nextInt()));
          try
          {
            Thread.sleep(10L);
          }
          catch ( InterruptedException ex )
          {
            // ignore
          }
        }
      }
    };
    pusherA.start();

    try
    {
      Thread.sleep(1000L);
    }
    catch ( InterruptedException ex )
    {
      // ignore
    }

    final Thread pusherB = new Thread() {
      @Override
      public void run()
      {
        final Random rand = new Random(System.currentTimeMillis());
        while ( running.getValue() )
        {
          map1.put(Integer.valueOf(rand.nextInt(8) + 8),
            Integer.valueOf(rand.nextInt()));
          map2.put(Integer.valueOf(rand.nextInt(3) + 3),
            Integer.valueOf(rand.nextInt()));
          try
          {
            Thread.sleep(22L);
          }
          catch ( InterruptedException ex )
          {
            // ignore
          }
        }
      }
    };
    pusherB.start();

    final long stopat = System.currentTimeMillis() + 20000L;
    while ( System.currentTimeMillis() < stopat )
    {
      System.out.println("Map 1: " + map1.size());
      System.out.println("Map 2: " + map2.size());
      System.out.println("Total: " + cm.size());
      try
      {
        Thread.sleep(10L);
      }
      catch ( InterruptedException ex )
      {
        // ignore
      }
    }

    running.setValue(false);
    System.out.println("Stopping...");
    try
    {
      pusherA.join();
    }
    catch ( InterruptedException ex )
    {
      ex.printStackTrace();
    }
    try
    {
      pusherB.join();
    }
    catch ( InterruptedException ex )
    {
      ex.printStackTrace();
    }
    System.out.println("Stopped.");
    final long stopped = System.currentTimeMillis();

    System.out.println("Map 1: " + map1.size());
    System.out.println("Map 2: " + map2.size());
    System.out.println("Total: " + cm.size());

    System.out.println("Waiting for stuff to expire.");
    try
    {
      Thread.sleep(6000L);
    }
    catch ( InterruptedException ex )
    {
      ex.printStackTrace();
    }
    cm.expire();

    System.out.println("Running the key set of map1");
    int xi = 7;
    for ( Integer key : map1.keySet() )
    {
      map1.get(key);
      if ( --xi <= 0 )
        break;
    }
    final long rankeyset = System.currentTimeMillis();

    System.out.println("Waiting for stuff to expire.");
    try
    {
      Thread.sleep(stopped + 11000L - System.currentTimeMillis());
    }
    catch ( InterruptedException ex )
    {
      ex.printStackTrace();
    }
    cm.expire();

    System.out.println("Map 1: " + map1.size());
    System.out.println("Map 2: " + map2.size());
    System.out.println("Total: " + cm.size());

    System.out.println("Waiting for stuff to expire.");
    try
    {
      Thread.sleep(rankeyset + 11000L - System.currentTimeMillis());
    }
    catch ( InterruptedException ex )
    {
      ex.printStackTrace();
    }
    cm.expire();

    System.out.println("Map 1: " + map1.size());
    System.out.println("Map 2: " + map2.size());
    System.out.println("Total: " + cm.size());

    cm.removeAllCaches();

    System.exit(0);
  }
}
