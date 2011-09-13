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

import java.util.ListIterator;
import java.util.NoSuchElementException;

import com.evanmclean.evlib.lang.Arr;

/**
 * A (list) iterator over an array of any objects.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 * @param <T>
 */
public class ArrayListIterator<T> implements ListIterator<T>
{
  public static <T> ArrayListIterator<T> create( final T... arr )
  {
    return new ArrayListIterator<T>(arr);
  }

  public static <T> ArrayListIterator<T> create( final T[] arr,
      final int start_index, final int end_index )
  {
    return new ArrayListIterator<T>(arr, start_index, end_index, 0);
  }

  public static <T> ArrayListIterator<T> create( final T[] arr,
      final int start_index, final int end_index, final int initial_index )
  {
    return new ArrayListIterator<T>(arr, start_index, end_index, initial_index);
  }

  private final T[] arr;
  private int nextPos;
  private int lastPos;
  private final int start;
  private final int end;

  public ArrayListIterator( final T... arr )
  {
    this(arr, 0, Arr.length(arr), 0);
  }

  public ArrayListIterator( final T[] arr, final int start_index,
      final int end_index )
  {
    this(arr, start_index, end_index, 0);
  }

  public ArrayListIterator( final T[] arr, final int start_index,
      final int end_index, final int initial_index )
  {
    this.arr = arr;
    this.end = Math.max(0, Math.min(end_index, Arr.length(arr)));
    this.start = Math.min(this.end, Math.max(start_index, 0));
    this.nextPos = Math.min(this.end,
      Math.max(this.start + initial_index, this.start));
    this.lastPos = -1;
  }

  /**
   * Always throws UnsupportedOperationException.
   * 
   * @param obj
   * @throws UnsupportedOperationException
   */
  public void add( final T obj )
  {
    throw new UnsupportedOperationException();
  }

  public boolean hasNext()
  {
    return nextPos < end;
  }

  public boolean hasPrevious()
  {
    return nextPos > start;
  }

  public T next()
  {
    if ( nextPos >= end )
      throw new NoSuchElementException();
    int idx = nextPos++;
    lastPos = idx;
    return arr[idx];
  }

  public int nextIndex()
  {
    return Math.min(end, nextPos) - start;
  }

  public T previous()
  {
    if ( nextPos <= start )
      throw new NoSuchElementException();
    int idx = --nextPos;
    lastPos = idx;
    return arr[idx];
  }

  public int previousIndex()
  {
    return Math.max(0, nextPos - start) - 1;
  }

  /**
   * Always throws UnsupportedOperationException.
   * 
   * @throws UnsupportedOperationException
   */
  public void remove()
  {
    throw new UnsupportedOperationException();
  }

  /**
   * Replaces the last element in the array returned by <code>next</code> or
   * <code>previous</code> with the specified element. If
   * 
   * @param obj
   *        The object (may be <code>null</code>) to replace the corresponding
   *        entry in the array.
   * @throws IllegalStateException
   *         if neither {@link #next()} or {@link #previous()} have been called.
   */
  public void set( final T obj )
  {
    if ( (lastPos < start) || (lastPos >= end) )
      throw new IllegalStateException();
    arr[lastPos] = obj;
  }
}
