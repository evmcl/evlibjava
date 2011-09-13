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
package com.evanmclean.evlib.junit;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Vector;

import junit.framework.TestCase;

/**
 * Responsible for loading classes representing valid test cases.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
class TestCaseLoader
{
  final private Vector<Class<?>> classList = new Vector<Class<?>>();
  final private String requiredType;

  /**
   * Construct this instance. Load all the test cases possible that derive from
   * <code>baseClass</code> and cannot be ignored.
   * 
   * @param requiredType
   *        The value for the <code>TEST_ALL_TEST_TYPE</code> variable.
   */
  public TestCaseLoader( final String requiredType )
  {
    if ( requiredType == null )
      throw new IllegalArgumentException("requiredType is null");
    this.requiredType = requiredType;
  }

  /**
   * Obtain an iterator over the collection of test case classes loaded by
   * <code>loadTestCases</code>
   * 
   * @return The iterator.
   */
  public Iterator<Class<?>> getClasses()
  {
    return classList.iterator();
  }

  /**
   * Load the classes that represent test cases we are interested.
   * 
   * @param classNamesIterator
   *        An iterator over a collection of fully qualified class names
   */
  public void loadTestCases( final Iterator<String> classNamesIterator )
  {
    while ( classNamesIterator.hasNext() )
    {
      final String className = classNamesIterator.next();
      try
      {
        final Class<?> candidateClass = Class.forName(className);
        addClassIfTestCase(candidateClass);
      }
      catch ( ClassNotFoundException e )
      {
        System.err.println("Cannot load class: " + className + " "
            + e.getMessage());
      }
      catch ( NoClassDefFoundError e )
      {
        System.err.println("Cannot load class that " + className
            + " is dependant on");
      }
    }
  }

  /**
   * Adds <code>testCaseClass</code> to the list of classdes if the class is a
   * test case we wish to load. Calls <code>shouldLoadTestCase ()</code> to
   * determine that.
   */
  private void addClassIfTestCase( final Class<?> testCaseClass )
  {
    if ( shouldAddTestCase(testCaseClass) )
    {
      classList.add(testCaseClass);
    }
  }

  /**
   * Determine if we should load this test case.
   */
  private boolean shouldAddTestCase( final Class<?> testCaseClass )
  {
    boolean isOfTheCorrectType = false;
    if ( TestCase.class.isAssignableFrom(testCaseClass) )
    {
      try
      {
        final Field testAllIgnoreThisField = testCaseClass
            .getDeclaredField("TEST_ALL_TEST_TYPE");
        final int EXPECTED_MODIFIERS = Modifier.STATIC | Modifier.PUBLIC
            | Modifier.FINAL;
        if ( ((testAllIgnoreThisField.getModifiers() & EXPECTED_MODIFIERS) != EXPECTED_MODIFIERS)
            || (testAllIgnoreThisField.getType() != String.class) )
        {
          throw new IllegalArgumentException(
              "TEST_ALL_TEST_TYPE should be static public final String");
        }
        final String testType = (String) testAllIgnoreThisField
            .get(testCaseClass);
        isOfTheCorrectType = requiredType.equals(testType);
      }
      catch ( NoSuchFieldException e )
      {
        // empty
      }
      catch ( IllegalAccessException e )
      {
        throw new IllegalArgumentException("The field "
            + testCaseClass.getName()
            + ".TEST_ALL_TEST_TYPE is not accessible.");
      }
    }
    return isOfTheCorrectType;
  }

}
