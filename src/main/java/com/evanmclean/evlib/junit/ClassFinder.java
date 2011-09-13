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
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

/**
 * This class is responsible for searching a directory for class files. It
 * builds a list of fully qualified class names from the class files in the
 * directory tree.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
class ClassFinder
{
  final private Vector<String> classNameList = new Vector<String>();
  final private int startPackageName;

  /**
   * Construct the class finder and locate all the classes in the directory
   * structured pointed to by <code>classPathRoot</code>. Only classes in the
   * package <code>packageRoot</code> are considered.
   * 
   * @param classPathRoot
   * @param packageRoot
   * @throws IOException
   */
  public ClassFinder( final File classPathRoot, final String packageRoot )
    throws IOException
  {
    startPackageName = classPathRoot.getAbsolutePath().length() + 1;
    final String directoryOffset = packageRoot.replace('.', File.separatorChar);
    findAndStoreTestClasses(new File(classPathRoot, directoryOffset));
  }

  /**
   * Return the found classes.
   * 
   * @return The iterator
   */
  public Iterator<String> getClasses()
  {
    return classNameList.iterator();
  }

  /**
   * Given a file name, guess the fully qualified class name.
   */
  private String computeClassName( final File file )
  {
    final String absPath = file.getAbsolutePath();
    final String packageBase = absPath.substring(startPackageName,
      absPath.length() - 6);
    String className;
    className = packageBase.replace(File.separatorChar, '.');
    return className;
  }

  /**
   * This method does all the work. It runs down the directory structure looking
   * for java classes.
   */
  private void findAndStoreTestClasses( final File currentDirectory )
    throws IOException
  {
    final String[] files = currentDirectory.list();
    if ( files != null )
      for ( int i = 0; i < files.length; i++ )
      {
        final File file = new File(currentDirectory, files[i]);
        final String fileBase = file.getName();
        final int idx = fileBase.indexOf(".class");
        final int CLASS_EXTENSION_LENGTH = 6;

        if ( idx != -1 && (fileBase.length() - idx) == CLASS_EXTENSION_LENGTH )
        {
          /*
           * This used to use JCF. However, JCF seems to have disappeared from
           * the web so we fallback to a less elegant method. We compute the
           * class name from the file name :-( JcfClassInputStream inputStream =
           * new JcfClassInputStream(new FileInputStream (file)); JcfClassFile
           * classFile = new JcfClassFile (inputStream); System.out.println
           * ("Processing: " + classFile.getFullName ().replace ('/','.'));
           * classNameList.add (classFile.getFullName ().replace ('/','.'));
           */
          final String className = computeClassName(file);
          classNameList.add(className);
        }
        else
        {
          if ( file.isDirectory() )
          {
            findAndStoreTestClasses(file);
          }
        }
      }
  }

}
