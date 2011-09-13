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
package com.evanmclean.evlib.lang;

import java.util.Iterator;
import java.util.ListIterator;

import com.evanmclean.evlib.util.ArrayListIterator;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/**
 * Create an {@link Iterable} from an array that can produce an {@link Iterator}
 * or a {@link ListIterator}.
 * 
 * @param <T>
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class ArrayIterable<T> implements Iterable<T>
{
  public static <T> ArrayIterable<T> create( final T[] arr )
  {
    return new ArrayIterable<T>(arr);
  }

  public static <T> ArrayIterable<T> create( final T[] arr,
      final int start_index )
  {
    return new ArrayIterable<T>(arr, start_index);
  }

  public static <T> ArrayIterable<T> create( final T[] arr,
      final int start_index, final int end_index )
  {
    return new ArrayIterable<T>(arr, start_index, end_index);
  }

  private final T[] arr;
  private final int start;
  private final int end;

  public ArrayIterable( final T[] arr )
  {
    this(arr, 0, Arr.length(arr));
  }

  public ArrayIterable( final T[] arr, final int start_index )
  {
    this(arr, start_index, Arr.length(arr));
  }

  @SuppressWarnings( "EI_EXPOSE_REP" )
  public ArrayIterable( final T[] arr, final int start_index,
      final int end_index )
  {
    this.arr = arr;
    this.start = start_index;
    this.end = end_index;
  }

  /**
   * Returns an iterator over a set of elements of type T.
   * 
   * @return An iterator over a set of elements of type T.
   */
  public ListIterator<T> iterator()
  {
    return new ArrayListIterator<T>(arr, start, end, 0);
  }

  /**
   * Returns a list iterator over a set of elements of type T.
   * 
   * @return A list iterator over a set of elements of type T.
   */
  public ListIterator<T> listIterator()
  {
    return new ArrayListIterator<T>(arr, start, end, 0);
  }

  /**
   * Returns a list iterator over a set of elements of type T.
   * 
   * @param initial_index
   *        Index of first element to be returned from the list iterator (by a
   *        call to the <code>next</code> method).
   * 
   * @return A list iterator over a set of elements of type T.
   */
  public ListIterator<T> listIterator( final int initial_index )
  {
    return new ArrayListIterator<T>(arr, start, end, initial_index);
  }
}
