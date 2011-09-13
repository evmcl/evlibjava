package com.evanmclean.evlib.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * A byte array output stream which you can then directly get an input stream
 * for without the overhead of copying the byte array. Input stream behaviour
 * may get screwy if you write to the output stream, and particularly if you
 * call {@link #reset()} after calling {@link #getInputStream()}.
 */
public class ByteArrayInputOutputStream extends ByteArrayOutputStream
{
  public ByteArrayInputOutputStream()
  {
    // empty
  }

  public ByteArrayInputOutputStream( final int size )
  {
    super(size);
  }

  /**
   * Get a byte array input stream based on the current byte buffer for the
   * output stream. Behaviour of the input stream may get screwy if you write to
   * the output stream, and particularly if you call {@link #reset()} after
   * calling this method.
   * 
   * @return A byte array input stream.
   */
  public ByteArrayInputStream getInputStream()
  {
    return new ByteArrayInputStream(buf, 0, count);
  }
}
