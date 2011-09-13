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

import java.util.Iterator;

/**
 * Wrapper around an {@link Iterable} or an {@link Iterator} that makes an
 * iterator that will throw an {@link UnsupportedOperationException} if
 * {@link #remove()} is called.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 * @param <E>
 */
public class UnmodifiableIterator<E> implements Iterator<E>
{
  public static <E> UnmodifiableIterator<E> create(
      final Iterable<? extends E> iterable )
  {
    return new UnmodifiableIterator<E>(iterable);
  }

  public static <E> UnmodifiableIterator<E> create(
      final Iterator<? extends E> iterator )
  {
    return new UnmodifiableIterator<E>(iterator);
  }

  private final Iterator<? extends E> it;

  public UnmodifiableIterator( final Iterable<? extends E> iterable )
  {
    this.it = iterable.iterator();
  }

  public UnmodifiableIterator( final Iterator<? extends E> iterator )
  {
    this.it = iterator;
  }

  public boolean hasNext()
  {
    return it.hasNext();
  }

  public E next()
  {
    return it.next();
  }

  public void remove()
  {
    throw new UnsupportedOperationException();
  }
}
