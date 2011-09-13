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
package com.evanmclean.evlib.text;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.text.StrMatcher;
import org.apache.commons.lang.text.StrTokenizer;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/**
 * <p>
 * Performs a fuzzy comparison between two strings to try and find similar
 * strings.
 * </p>
 * 
 * <p>
 * <strong>Note:</strong> This class needs a recent version of <a
 * href="http://commons.apache.org/lang/">Apache Commons Lang</a> library.
 * </p>
 * 
 * <p>
 * The algorithm is roughly:
 * </p>
 * 
 * <ol>
 * <li>Break each string up into a lexicon of unique words.</li>
 * <li>Eliminate any words under the minimum length or are on the ignored list.</li>
 * <li>Eliminate any words that do not have at least one character in them.</li>
 * <li>For each of the remaining words in the first list, find the closest match
 * based on the Levenstein Distance.</li>
 * <li>Do the same for the remaining words in the second list.</li>
 * <li>Return the average Levenstein Distance.</li>
 * </ol>
 * 
 * <p>
 * All comparisons are case insensitive (mainly by converting the lexicon to all
 * lower case).
 * </p>
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class FuzzyCompare
{
  private static final Set<String> DEFAULT_IGNORED_WORDS;
  private static final int DEFAULT_MINIMUM_WORD_LENGTH = 3;

  private static final StrMatcher WORD_DELIM_MATCH = new StrMatcher() {

    @Override
    public int isMatch( final char[] buffer, final int pos, final int start,
        final int end )
    {
      int xi = pos;
      int ret = 0;
      while ( (xi < end) && Character.isWhitespace(buffer[xi++]) )
        ++ret;
      return ret;
    }
  };

  static
  {
    final Set<String> diw = new HashSet<String>();
    diw.add("and");
    DEFAULT_IGNORED_WORDS = Collections.unmodifiableSet(diw);
  }

  private Set<String> ignoredWords;
  private int minimumWordLength;
  private StrTokenizer tokenizer;
  private FuzzyLexicon leftLexicon;

  /**
   * Construct a new fuzzy comparator with the default ignored words and minimum
   * word length.
   * 
   * @see #getMinimumWordLength()
   */
  public FuzzyCompare()
  {
    this(null, DEFAULT_IGNORED_WORDS, DEFAULT_MINIMUM_WORD_LENGTH);
  }

  /**
   * Construct a new fuzzy comparator with the default minimum word length.
   * 
   * @param ignored_words
   *        The set of words to be ignored.
   * @see #getMinimumWordLength()
   */
  public FuzzyCompare( final Collection<String> ignored_words )
  {
    this(null, ignored_words, DEFAULT_MINIMUM_WORD_LENGTH);
  }

  /**
   * Construct a new fuzzy comparator.
   * 
   * @param ignored_words
   *        The set of words to be ignored.
   * @param min_word_length
   *        Ignore words that are smaller than this length.
   * @see #getMinimumWordLength()
   */
  public FuzzyCompare( final Collection<String> ignored_words,
      final int min_word_length )
  {
    this(null, ignored_words, min_word_length);
  }

  /**
   * Construct a new fuzzy comparator with the default ignored words.
   * 
   * @param min_word_length
   *        Ignore words that are smaller than this length.
   * @see #getMinimumWordLength()
   */
  public FuzzyCompare( final int min_word_length )
  {
    this(null, DEFAULT_IGNORED_WORDS, min_word_length);
  }

  /**
   * Construct a new fuzzy comparator with the default ignored words and minimum
   * word length.
   * 
   * @param lhs
   *        The left string to compare.
   * @see #getMinimumWordLength()
   */
  public FuzzyCompare( final String lhs )
  {
    this(lhs, DEFAULT_IGNORED_WORDS, DEFAULT_MINIMUM_WORD_LENGTH);
  }

  /**
   * Construct a new fuzzy comparator with the default minimum word length.
   * 
   * @param lhs
   *        The left string to compare.
   * @param ignored_words
   *        The set of words to be ignored.
   * @see #getMinimumWordLength()
   */
  public FuzzyCompare( final String lhs, final Collection<String> ignored_words )
  {
    this(lhs, ignored_words, DEFAULT_MINIMUM_WORD_LENGTH);
  }

  /**
   * Construct a new fuzzy comparator.
   * 
   * @param lhs
   *        The left string to compare.
   * @param ignored_words
   *        The set of words to be ignored.
   * @param min_word_length
   *        Ignore words that are smaller than this length.
   */
  public FuzzyCompare( final String lhs,
      final Collection<String> ignored_words, final int min_word_length )
  {
    if ( lhs != null )
      leftLexicon = makeLexicon(lhs);
    if ( ignored_words == null )
      ignoredWords = DEFAULT_IGNORED_WORDS;
    else
      ignoredWords = makeIgnoredWordSet(ignored_words);
    minimumWordLength = Math.max(0, min_word_length);
  }

  /**
   * Construct a new fuzzy comparator with the default ignored words.
   * 
   * @param lhs
   *        The left string to compare.
   * @param min_word_length
   *        Ignore words that are smaller than this length.
   */
  public FuzzyCompare( final String lhs, final int min_word_length )
  {
    this(lhs, DEFAULT_IGNORED_WORDS, min_word_length);
  }

  /**
   * Add the additional collection of words to the ignored words list.
   * 
   * @param ignored_words
   */
  @SuppressWarnings( "DM_CONVERT_CASE" )
  public void addIgnoredWords( final Collection<String> ignored_words )
  {
    if ( ignored_words != null )
    {
      Set<String> iw = ignoredWords;
      if ( iw == null )
        iw = new HashSet<String>();
      else if ( iw == DEFAULT_IGNORED_WORDS )
        iw = new HashSet<String>(iw);
      for ( String str : ignored_words )
        iw.add(str.toLowerCase());
      ignoredWords = iw;
    }
  }

  /**
   * Add the additional collection of words to the ignored words list.
   * 
   * @param ignored_words
   */
  @SuppressWarnings( "DM_CONVERT_CASE" )
  public void addIgnoredWords( final String... ignored_words )
  {
    if ( ignored_words != null )
    {
      Set<String> iw = ignoredWords;
      if ( iw == null )
        iw = new HashSet<String>();
      else if ( iw == DEFAULT_IGNORED_WORDS )
        iw = new HashSet<String>(iw);
      for ( String str : ignored_words )
        iw.add(str.toLowerCase());
      ignoredWords = iw;
    }
  }

  /**
   * Perform the difference comparison against the specified string. The left
   * string must already have been {@link #setLeft(String) set}.
   * 
   * @param rhs
   *        The string to compare against.
   * @return The difference between the strings.
   * @see #difference(String, String)
   * @see #setLeft(String)
   */
  public double difference( final String rhs )
  {
    if ( leftLexicon == null )
      throw new IllegalStateException("LHS not specified.");
    return leftLexicon.difference(rhs);
  }

  /**
   * Perform the difference comparison against the two strings. The left string
   * is remembers and can be used for future {@link #difference(String)
   * comparisons}.
   * 
   * @param lhs
   *        The left string to compare.
   * @param rhs
   *        The right string to compare.
   * @return The difference between the strings.
   * @see #difference(String)
   * @see #setLeft(String)
   */
  public double difference( final String lhs, final String rhs )
  {
    setLeft(lhs);
    return difference(rhs);
  }

  /**
   * Returns the current set of ignored words.
   * 
   * @return The current set of ignored words.
   */
  public String[] getIgnoredWords()
  {
    return ignoredWords.toArray(new String[ignoredWords.size()]);
  }

  /**
   * The current minimum word length (default of 3).
   * 
   * @return The current minimum word length.
   */
  public int getMinimumWordLength()
  {
    return minimumWordLength;
  }

  /**
   * Construct a lexicon of all the good words in the string.
   * 
   * @param str
   *        The string to process.
   * @return A new lexicon.
   */
  @SuppressWarnings( "DM_CONVERT_CASE" )
  public FuzzyLexicon makeLexicon( final String str )
  {
    if ( tokenizer == null )
      tokenizer = new StrTokenizer(str, WORD_DELIM_MATCH);
    else
      tokenizer.reset(str);

    final HashSet<String> lexicon = new HashSet<String>();
    while ( tokenizer.hasNext() )
    {
      final String word = tokenizer.nextToken().toLowerCase();
      if ( goodWord(word) )
        lexicon.add(trim(word));
    }
    return new FuzzyLexicon(str, lexicon, this);
  }

  /**
   * Set the ignored word set to the specified collection.
   * 
   * @param ignored_words
   */
  public void setIgnoredWords( final Collection<String> ignored_words )
  {
    ignoredWords = makeIgnoredWordSet(ignored_words);
  }

  /**
   * Set the left side to be compared.
   * 
   * @param lhs
   *        The string to be compared.
   * @see #difference(String)
   * @see #difference(String, String)
   */
  public void setLeft( final String lhs )
  {
    leftLexicon = makeLexicon(lhs);
  }

  /**
   * Set the minimum word length that will be used.
   * 
   * @param minimumWordLength
   */
  public void setMinimumWordLength( final int minimumWordLength )
  {
    this.minimumWordLength = Math.max(0, minimumWordLength);
  }

  /**
   * Returns true if the word meets the minimum length, is not an ignored word,
   * and contains at least one letter.
   * 
   * @param word
   * @return
   */
  private boolean goodWord( final String word )
  {
    final int len = word.length();
    if ( len < minimumWordLength )
      return false;
    if ( ignoredWords.contains(word) )
      return false;
    for ( int xi = 0; xi < len; ++xi )
      if ( Character.isLetter(word.charAt(xi)) )
        return true;
    return false;
  }

  @SuppressWarnings( "DM_CONVERT_CASE" )
  private Set<String> makeIgnoredWordSet( final Collection<String> word_list )
  {
    final Set<String> iw = new HashSet<String>();
    if ( word_list != null )
      for ( String str : word_list )
        iw.add(str.toLowerCase());
    return iw;
  }

  /**
   * Remove any leading or tailing non-alphanumeric (in particular, punctuation)
   * characters.
   * 
   * @param word
   * @return
   */
  private String trim( final String word )
  {
    final int len = word.length();
    int end = len;
    while ( (end > 0) && (!Character.isLetterOrDigit(word.charAt(end - 1))) )
      --end;
    int start = 0;
    while ( (start < end) && (!Character.isLetterOrDigit(word.charAt(start))) )
      ++start;
    if ( (start <= 0) && (end >= len) )
      return word;
    return word.substring(start, end);
  }
}
