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

import java.io.File;
import java.lang.reflect.Method;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>
 * Generates a test suite for the entire Java implementation. Runs all Java test
 * cases in the source tree that extend TestCase and are of a specified type.
 * </p>
 * 
 * <p>
 * A typical implementation of a test suite class that uses this object is:
 * </p>
 * 
 * <pre>
 * public class AllTests
 * {
 *   public static Test suite() throws Throwable
 *   {
 *     return TestAll.getSuite(&quot;target/test-classes&quot;, &quot;com.company.app.sub1&quot;,
 *       &quot;UNIT&quot;);
 *   }
 * }
 * </pre>
 * 
 * <p>
 * See {@link #getSuite(String, String, String)} for more details.
 * </p>
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class TestAll
{
  /**
   * The default test type value for <code>test_type</code>, which is
   * &quot;UNIT&quot;.
   */
  public static final String DEFAULT_TEST_TYPE = "UNIT";

  /**
   * Dynamically create a test suite from a set of class files in a directory
   * tree, using the default <code>test_type</code> of &quot;UNIT&quot;.
   * 
   * @param class_root
   *        The directory containing the classes. e.g.,
   *        <code>/product/classes</code>
   * @param package_root
   *        Where to start looking for packages within the package tree. e.g.,
   *        <code>com.company.app.sub1</code> will only run tests in package
   *        <code>com.company.app.sub1</code> or below.
   * 
   * @return The test suite.
   * @throws Throwable
   */
  public static Test getSuite( final String class_root,
      final String package_root ) throws Throwable
  {
    return getSuite(class_root, package_root, DEFAULT_TEST_TYPE);
  }

  /**
   * Dynamically create a test suite from a set of class files in a directory
   * tree.
   * 
   * @param class_root
   *        The directory containing the classes. e.g.,
   *        <code>/product/classes</code>
   * @param package_root
   *        Where to start looking for packages within the package tree. e.g.,
   *        <code>com.company.app.sub1</code> will only run tests in package
   *        <code>com.company.app.sub1</code> or below.
   * @param test_type
   *        Tye type of test case to load. All test cases with
   *        <code>public static
   * final String TEST_ALL_TEST_TYPE</code> defined to match the value of
   *        <code>test_type</code> will be loaded. The default value (specified
   *        by {@link #DEFAULT_TEST_TYPE}) is &quot;UNIT&quot;.
   * 
   * @return The test suite.
   * @throws Throwable
   */
  public static Test getSuite( final String class_root,
      final String package_root, final String test_type ) throws Throwable
  {
    try
    {

      final String classRootString = class_root;
      if ( classRootString == null )
        throw new IllegalArgumentException(
            "Static field class_root must be set.");
      final String testType = test_type;
      if ( testType == null )
        throw new IllegalArgumentException(
            "Static field test_type must be set.");
      File classRoot = new File(classRootString);
      classRoot = classRoot.getAbsoluteFile();
      System.out.println("Class root: " + classRoot);
      String packageRoot = package_root;
      if ( packageRoot == null )
        packageRoot = "";
      final ClassFinder classFinder = new ClassFinder(classRoot, packageRoot);
      final TestCaseLoader testCaseLoader = new TestCaseLoader(testType);
      testCaseLoader.loadTestCases(classFinder.getClasses());
      final TestSuite suite = new TestSuite();
      final int numberOfTests = addAllTests(suite, testCaseLoader.getClasses());
      System.out.println("Number of test classes found: " + numberOfTests);
      return suite;
    }
    catch ( Throwable t )
    {
      // This ensures we have extra information. Otherwise all we get is a
      // "Could not invoke the suite method." message.
      t.printStackTrace();
      throw t;
    }
  }

  /**
   * Iterates over the classes accessible via the iterator and adds them to the
   * test suite.
   */
  private static int addAllTests( final TestSuite suite,
      final Iterator<Class<?>> classIterator )
  {
    int testClassCount = 0;
    while ( classIterator.hasNext() )
    {
      final Class<?> testCaseClass = classIterator.next();

      try
      {
        final Method suiteMethod = testCaseClass.getMethod("suite",
          new Class[0]);
        final Test test = (Test) suiteMethod.invoke(null, new Object[0]); // static
        // method
        suite.addTest(test);
      }
      catch ( NoSuchMethodException e )
      {
        suite.addTest(new TestSuite(testCaseClass));
      }
      catch ( Exception e )
      {
        System.err.println("Failed to execute suite ()");
        e.printStackTrace();
      }
      System.out.println("Loaded test case: " + testCaseClass.getName());
      testClassCount++;
    }
    return testClassCount;
  }

  private TestAll()
  {
    // empty
  }
}
