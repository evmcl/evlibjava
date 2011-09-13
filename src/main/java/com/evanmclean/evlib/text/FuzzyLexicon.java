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

import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * Produced by {@link FuzzyCompare} to perform fuzzy comparisons between
 * strings.
 * </p>
 * 
 * <p>
 * <strong>Note:</strong> This class needs a recent version of <a
 * href="http://commons.apache.org/lang/">Apache Commons Lang</a> library.
 * </p>
 * 
 * @see FuzzyCompare
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class FuzzyLexicon
{
  private final String str;
  private final FuzzyCompare fc;
  private Set<String> lexicon;

  FuzzyLexicon( final String str, final Set<String> lex,
      final FuzzyCompare fuzzy_comparator )
  {
    this.str = str;
    lexicon = lex;
    fc = fuzzy_comparator;
  }

  /**
   * Perform the difference comparison against the specified lexicon.
   * 
   * @param right_lexicon
   *        The lexicon to compare against.
   * @return The difference between the lexicons.
   * @see #difference(String)
   */

  public double difference( final FuzzyLexicon right_lexicon )
  {
    double sum = 0.0;
    int cnt = 0;

    for ( String word : lexicon )
    {
      sum += difference(word, right_lexicon.lexicon);
      ++cnt;
    }

    for ( String word : right_lexicon.lexicon )
    {
      sum += difference(word, lexicon);
      ++cnt;
    }

    if ( cnt == 0 )
      return 0.0;
    return sum / cnt;
  }

  /**
   * Perform the difference comparison against the specified string.
   * 
   * @param rhs
   *        The string to compare against.
   * @return The difference between the strings.
   * @see #difference(FuzzyLexicon)
   */
  public double difference( final String rhs )
  {
    return difference(fc.makeLexicon(rhs));
  }

  /**
   * The original string this lexicon represents.
   * 
   * @return The original string this lexicon represents.
   */
  public String getStr()
  {
    return str;
  }

  /**
   * Find the minimum difference between the word and the set of words in the
   * lexicon.
   * 
   * @param word
   * @param word_set
   * @return
   */
  private int difference( final String word, final Set<String> word_set )
  {
    if ( (word_set == null) || (word_set.size() <= 0) )
      return StringUtils.getLevenshteinDistance(word, "");

    int diff = Integer.MAX_VALUE;
    for ( String lex : word_set )
      diff = Math.min(diff, StringUtils.getLevenshteinDistance(word, lex));
    return diff;
  }
}
