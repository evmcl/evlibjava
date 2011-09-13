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
package com.evanmclean.evlib.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Various utilities to make it simpler to get common message digests.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public final class Digests
{
  private static long COPY_BUFFER_LENGTH = 1024L * 1024L * 4L;
  private static final char[] HEX = {
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
      'e', 'f'
  };

  /**
   * Run a digest against the contents of a file.
   * 
   * @param file
   *        The file to read.
   * @param digest
   *        The digest object to use.
   * @return The return of <code>digest.digest()</code> converted to a
   *         hexidecimal string.
   * @throws IOException
   */
  public static String digest( final File file, final MessageDigest digest )
    throws IOException
  {
    final FileInputStream in = new FileInputStream(file);
    try
    {
      return digest(in, digest, file.length());
    }
    finally
    {
      in.close();
    }
  }

  /**
   * Run a digest against the input stream.
   * 
   * @param in
   *        The input stream to read.
   * @param digest
   *        The digest object to use.
   * @return The return of <code>digest.digest()</code> converted to a
   *         hexidecimal string.
   * @throws IOException
   */
  public static String digest( final InputStream in, final MessageDigest digest )
    throws IOException
  {
    return digest(in, digest, COPY_BUFFER_LENGTH);
  }

  /**
   * Returns a MessageDigest object that implements the specified digest
   * algorithm. Basically a wrapper around
   * <code>MessageDigest.getInstance(String)</code> that throws a
   * <code>RuntimeException</code> instead of a
   * <code>NoSuchAlgorithmException</code>.
   * 
   * @param algorithm
   *        The name of the algorithm requested.
   * @return A Message Digest object that implements the specified algorithm.
   * @throws RuntimeException
   *         If no Provider supports a MessageDigestSpi implementation for the
   *         specified algorithm.
   */
  public static MessageDigest getDigest( final String algorithm )
  {
    try
    {
      return MessageDigest.getInstance(algorithm);
    }
    catch ( NoSuchAlgorithmException ex )
    {
      throw new RuntimeException(ex.getMessage(), ex);
    }
  }

  /**
   * Returns a MessageDigest object that implements the MD5 algorithm.
   * 
   * @return A Message Digest object that implements the MD5 algorithm.
   * @throws RuntimeException
   *         If no Provider supports a MessageDigestSpi implementation for the
   *         MD5 algorithm.
   */
  public static MessageDigest getMd5Digest()
  {
    return getDigest("MD5");
  }

  /**
   * Returns a MessageDigest object that implements the SHA algorithm.
   * 
   * @return A Message Digest object that implements the SHA algorithm.
   * @throws RuntimeException
   *         If no Provider supports a MessageDigestSpi implementation for the
   *         SHA algorithm.
   */
  public static MessageDigest getShaDigest()
  {
    return getDigest("SHA");
  }

  /**
   * Converts an array of bytes into a <code>String</code> representing the
   * hexidecimal values of each byte in order.
   * 
   * @param buffer
   *        A byte[] to convert to Hex characters.
   * @return A String representing the hexidecimal values of each byte in order.
   */
  public static String hex( final byte[] buffer )
  {
    final StringBuilder out = new StringBuilder(buffer.length * 2);
    for ( byte bt : buffer )
    {
      out.append(HEX[(bt >> 4) & 0x0F]);
      out.append(HEX[bt & 0x0F]);
    }
    return out.toString();
  }

  /**
   * Converts the results of <code>MessageDigest.digest()</code> into a
   * <code>String</code> representing the hexidecimal values of each byte in
   * order.
   * 
   * @param digest
   *        A digest to convert to Hex characters.
   * @return A String representing the hexidecimal values of each byte in order.
   */
  public static String hex( final MessageDigest digest )
  {
    return hex(digest.digest());
  }

  /**
   * Product an MD5 checksum against contents of a file. This will use nio
   * ByteBuffer logic on files larger than 100K for efficiency.
   * 
   * @param file
   *        The file to read.
   * @return The MD5 checksum.
   * @throws IOException
   */
  public static String md5( final File file ) throws IOException
  {
    return digest(file, getMd5Digest());
  }

  /**
   * Product an MD5 checksum against a file input stream. This will use nio
   * ByteBuffer logic on files larger than 100K for efficiency.
   * 
   * @param in
   *        The file input stream to read.
   * @return The MD5 checksum.
   * @throws IOException
   */
  public static String md5( final FileInputStream in ) throws IOException
  {
    return digest(in, getMd5Digest());
  }

  /**
   * Product an MD5 checksum against an input stream.
   * 
   * @param in
   *        The input stream to read.
   * @return The MD5 checksum.
   * @throws IOException
   */
  public static String md5( final InputStream in ) throws IOException
  {
    return digest(in, getMd5Digest());
  }

  /**
   * Product an SHA checksum against contents of a file. This will use nio
   * ByteBuffer logic on files larger than 100K for efficiency.
   * 
   * @param file
   *        The file to read.
   * @return The SHA checksum.
   * @throws IOException
   */
  public static String sha( final File file ) throws IOException
  {
    return digest(file, getShaDigest());
  }

  /**
   * Product an SHA checksum against a file input stream. This will use nio
   * ByteBuffer logic on files larger than 100K for efficiency.
   * 
   * @param in
   *        The file input stream to read.
   * @return The SHA checksum.
   * @throws IOException
   */
  public static String sha( final FileInputStream in ) throws IOException
  {
    return digest(in, getShaDigest());
  }

  /**
   * Product an SHA checksum against an input stream.
   * 
   * @param in
   *        The input stream to read.
   * @return The SHA checksum.
   * @throws IOException
   */
  public static String sha( final InputStream in ) throws IOException
  {
    return digest(in, getShaDigest());
  }

  private static String digest( final InputStream in,
      final MessageDigest digest, final long expected_length )
    throws IOException
  {
    final byte[] buff = new byte[(int) Math.min(expected_length + 1,
      COPY_BUFFER_LENGTH)];
    final DigestInputStream ins = new DigestInputStream(in, digest);
    while ( ins.read(buff) != -1 )
      continue;

    return hex(digest);
  }

  private Digests()
  {
    // empty
  }
}
