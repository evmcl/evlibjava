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
package com.evanmclean.evlib.escape;

import java.util.ArrayList;

/**
 * <p>
 * The various character converters and stuff for dealing with HTML.
 * </p>
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
final class HtmlConv extends MlConv
{
  static final CharConv HTML_MIN;
  static final CharConv HTML_MIN_ATTR;
  static final CharConv HTML_BASIC;
  static final CharConv HTML_BASIC_ATTR;
  static final CharConv HTML_FULL;
  static final CharConv HTML_FULL_ATTR;
  static final CharConv HTML_ASCII;
  static final CharConv HTML_ASCII_ATTR;

  static final String BR = "<br>";
  static final String XBR = "<br/>";

  static
  {
    final ArrayList<CharSub> subs = new ArrayList<CharSub>();
    subs.add(new CharSub('<', "&lt;"));
    subs.add(new CharSub('&', "&amp;"));

    HTML_MIN = new AggregateCharConv(new CharSubConv(subs), ISO_CONV);

    subs.add(new CharSub('"', "&quot;"));
    HTML_MIN_ATTR = new AggregateCharConv(new CharSubConv(subs), ISO_CONV_ATTR);

    subs.add(new CharSub('>', "&gt;"));
    final CharSubConv basic_entity_conv = new CharSubConv(subs);

    HTML_BASIC = new AggregateCharConv(basic_entity_conv, ISO_CONV);
    HTML_BASIC_ATTR = new AggregateCharConv(basic_entity_conv, ISO_CONV_ATTR);

    subs.add(new CharSub('\u00A0', "&nbsp;"));
    subs.add(new CharSub('\u00A1', "&iexcl;"));
    subs.add(new CharSub('\u00A2', "&cent;"));
    subs.add(new CharSub('\u00A3', "&pound;"));
    subs.add(new CharSub('\u00A4', "&curren;"));
    subs.add(new CharSub('\u00A5', "&yen;"));
    subs.add(new CharSub('\u00A6', "&brvbar;"));
    subs.add(new CharSub('\u00A7', "&sect;"));
    subs.add(new CharSub('\u00A8', "&uml;"));
    subs.add(new CharSub('\u00A9', "&copy;"));
    subs.add(new CharSub('\u00AA', "&ordf;"));
    subs.add(new CharSub('\u00AB', "&laquo;"));
    subs.add(new CharSub('\u00AC', "&not;"));
    subs.add(new CharSub('\u00AD', "&shy;"));
    subs.add(new CharSub('\u00AE', "&reg;"));
    subs.add(new CharSub('\u00AF', "&macr;"));
    subs.add(new CharSub('\u00B0', "&deg;"));
    subs.add(new CharSub('\u00B1', "&plusmn;"));
    subs.add(new CharSub('\u00B2', "&sup2;"));
    subs.add(new CharSub('\u00B3', "&sup3;"));
    subs.add(new CharSub('\u00B4', "&acute;"));
    subs.add(new CharSub('\u00B5', "&micro;"));
    subs.add(new CharSub('\u00B6', "&para;"));
    subs.add(new CharSub('\u00B7', "&middot;"));
    subs.add(new CharSub('\u00B8', "&cedil;"));
    subs.add(new CharSub('\u00B9', "&sup1;"));
    subs.add(new CharSub('\u00BA', "&ordm;"));
    subs.add(new CharSub('\u00BB', "&raquo;"));
    subs.add(new CharSub('\u00BC', "&frac14;"));
    subs.add(new CharSub('\u00BD', "&frac12;"));
    subs.add(new CharSub('\u00BE', "&frac34;"));
    subs.add(new CharSub('\u00BF', "&iquest;"));
    subs.add(new CharSub('\u00C0', "&Agrave;"));
    subs.add(new CharSub('\u00C1', "&Aacute;"));
    subs.add(new CharSub('\u00C2', "&Acirc;"));
    subs.add(new CharSub('\u00C3', "&Atilde;"));
    subs.add(new CharSub('\u00C4', "&Auml;"));
    subs.add(new CharSub('\u00C5', "&Aring;"));
    subs.add(new CharSub('\u00C6', "&AElig;"));
    subs.add(new CharSub('\u00C7', "&Ccedil;"));
    subs.add(new CharSub('\u00C8', "&Egrave;"));
    subs.add(new CharSub('\u00C9', "&Eacute;"));
    subs.add(new CharSub('\u00CA', "&Ecirc;"));
    subs.add(new CharSub('\u00CB', "&Euml;"));
    subs.add(new CharSub('\u00CC', "&Igrave;"));
    subs.add(new CharSub('\u00CD', "&Iacute;"));
    subs.add(new CharSub('\u00CE', "&Icirc;"));
    subs.add(new CharSub('\u00CF', "&Iuml;"));
    subs.add(new CharSub('\u00D0', "&ETH;"));
    subs.add(new CharSub('\u00D1', "&Ntilde;"));
    subs.add(new CharSub('\u00D2', "&Ograve;"));
    subs.add(new CharSub('\u00D3', "&Oacute;"));
    subs.add(new CharSub('\u00D4', "&Ocirc;"));
    subs.add(new CharSub('\u00D5', "&Otilde;"));
    subs.add(new CharSub('\u00D6', "&Ouml;"));
    subs.add(new CharSub('\u00D7', "&times;"));
    subs.add(new CharSub('\u00D8', "&Oslash;"));
    subs.add(new CharSub('\u00D9', "&Ugrave;"));
    subs.add(new CharSub('\u00DA', "&Uacute;"));
    subs.add(new CharSub('\u00DB', "&Ucirc;"));
    subs.add(new CharSub('\u00DC', "&Uuml;"));
    subs.add(new CharSub('\u00DD', "&Yacute;"));
    subs.add(new CharSub('\u00DE', "&THORN;"));
    subs.add(new CharSub('\u00DF', "&szlig;"));
    subs.add(new CharSub('\u00E0', "&agrave;"));
    subs.add(new CharSub('\u00E1', "&aacute;"));
    subs.add(new CharSub('\u00E2', "&acirc;"));
    subs.add(new CharSub('\u00E3', "&atilde;"));
    subs.add(new CharSub('\u00E4', "&auml;"));
    subs.add(new CharSub('\u00E5', "&aring;"));
    subs.add(new CharSub('\u00E6', "&aelig;"));
    subs.add(new CharSub('\u00E7', "&ccedil;"));
    subs.add(new CharSub('\u00E8', "&egrave;"));
    subs.add(new CharSub('\u00E9', "&eacute;"));
    subs.add(new CharSub('\u00EA', "&ecirc;"));
    subs.add(new CharSub('\u00EB', "&euml;"));
    subs.add(new CharSub('\u00EC', "&igrave;"));
    subs.add(new CharSub('\u00ED', "&iacute;"));
    subs.add(new CharSub('\u00EE', "&icirc;"));
    subs.add(new CharSub('\u00EF', "&iuml;"));
    subs.add(new CharSub('\u00F0', "&eth;"));
    subs.add(new CharSub('\u00F1', "&ntilde;"));
    subs.add(new CharSub('\u00F2', "&ograve;"));
    subs.add(new CharSub('\u00F3', "&oacute;"));
    subs.add(new CharSub('\u00F4', "&ocirc;"));
    subs.add(new CharSub('\u00F5', "&otilde;"));
    subs.add(new CharSub('\u00F6', "&ouml;"));
    subs.add(new CharSub('\u00F7', "&divide;"));
    subs.add(new CharSub('\u00F8', "&oslash;"));
    subs.add(new CharSub('\u00F9', "&ugrave;"));
    subs.add(new CharSub('\u00FA', "&uacute;"));
    subs.add(new CharSub('\u00FB', "&ucirc;"));
    subs.add(new CharSub('\u00FC', "&uuml;"));
    subs.add(new CharSub('\u00FD', "&yacute;"));
    subs.add(new CharSub('\u00FE', "&thorn;"));
    subs.add(new CharSub('\u00FF', "&yuml;"));
    subs.add(new CharSub('\u0192', "&fnof;"));
    subs.add(new CharSub('\u0391', "&Alpha;"));
    subs.add(new CharSub('\u0392', "&Beta;"));
    subs.add(new CharSub('\u0393', "&Gamma;"));
    subs.add(new CharSub('\u0394', "&Delta;"));
    subs.add(new CharSub('\u0395', "&Epsilon;"));
    subs.add(new CharSub('\u0396', "&Zeta;"));
    subs.add(new CharSub('\u0397', "&Eta;"));
    subs.add(new CharSub('\u0398', "&Theta;"));
    subs.add(new CharSub('\u0399', "&Iota;"));
    subs.add(new CharSub('\u039A', "&Kappa;"));
    subs.add(new CharSub('\u039B', "&Lambda;"));
    subs.add(new CharSub('\u039C', "&Mu;"));
    subs.add(new CharSub('\u039D', "&Nu;"));
    subs.add(new CharSub('\u039E', "&Xi;"));
    subs.add(new CharSub('\u039F', "&Omicron;"));
    subs.add(new CharSub('\u03A0', "&Pi;"));
    subs.add(new CharSub('\u03A1', "&Rho;"));
    subs.add(new CharSub('\u03A3', "&Sigma;"));
    subs.add(new CharSub('\u03A4', "&Tau;"));
    subs.add(new CharSub('\u03A5', "&Upsilon;"));
    subs.add(new CharSub('\u03A6', "&Phi;"));
    subs.add(new CharSub('\u03A7', "&Chi;"));
    subs.add(new CharSub('\u03A8', "&Psi;"));
    subs.add(new CharSub('\u03A9', "&Omega;"));
    subs.add(new CharSub('\u03B1', "&alpha;"));
    subs.add(new CharSub('\u03B2', "&beta;"));
    subs.add(new CharSub('\u03B3', "&gamma;"));
    subs.add(new CharSub('\u03B4', "&delta;"));
    subs.add(new CharSub('\u03B5', "&epsilon;"));
    subs.add(new CharSub('\u03B6', "&zeta;"));
    subs.add(new CharSub('\u03B7', "&eta;"));
    subs.add(new CharSub('\u03B8', "&theta;"));
    subs.add(new CharSub('\u03B9', "&iota;"));
    subs.add(new CharSub('\u03BA', "&kappa;"));
    subs.add(new CharSub('\u03BB', "&lambda;"));
    subs.add(new CharSub('\u03BC', "&mu;"));
    subs.add(new CharSub('\u03BD', "&nu;"));
    subs.add(new CharSub('\u03BE', "&xi;"));
    subs.add(new CharSub('\u03BF', "&omicron;"));
    subs.add(new CharSub('\u03C0', "&pi;"));
    subs.add(new CharSub('\u03C1', "&rho;"));
    subs.add(new CharSub('\u03C2', "&sigmaf;"));
    subs.add(new CharSub('\u03C3', "&sigma;"));
    subs.add(new CharSub('\u03C4', "&tau;"));
    subs.add(new CharSub('\u03C5', "&upsilon;"));
    subs.add(new CharSub('\u03C6', "&phi;"));
    subs.add(new CharSub('\u03C7', "&chi;"));
    subs.add(new CharSub('\u03C8', "&psi;"));
    subs.add(new CharSub('\u03C9', "&omega;"));
    subs.add(new CharSub('\u03D1', "&thetasym;"));
    subs.add(new CharSub('\u03D2', "&upsih;"));
    subs.add(new CharSub('\u03D6', "&piv;"));
    subs.add(new CharSub('\u2022', "&bull;"));
    subs.add(new CharSub('\u2026', "&hellip;"));
    subs.add(new CharSub('\u2032', "&prime;"));
    subs.add(new CharSub('\u2033', "&Prime;"));
    subs.add(new CharSub('\u203E', "&oline;"));
    subs.add(new CharSub('\u2044', "&frasl;"));
    subs.add(new CharSub('\u2118', "&weierp;"));
    subs.add(new CharSub('\u2111', "&image;"));
    subs.add(new CharSub('\u211C', "&real;"));
    subs.add(new CharSub('\u2122', "&trade;"));
    subs.add(new CharSub('\u2135', "&alefsym;"));
    subs.add(new CharSub('\u2190', "&larr;"));
    subs.add(new CharSub('\u2191', "&uarr;"));
    subs.add(new CharSub('\u2192', "&rarr;"));
    subs.add(new CharSub('\u2193', "&darr;"));
    subs.add(new CharSub('\u2194', "&harr;"));
    subs.add(new CharSub('\u21B5', "&crarr;"));
    subs.add(new CharSub('\u21D0', "&lArr;"));
    subs.add(new CharSub('\u21D1', "&uArr;"));
    subs.add(new CharSub('\u21D2', "&rArr;"));
    subs.add(new CharSub('\u21D3', "&dArr;"));
    subs.add(new CharSub('\u21D4', "&hArr;"));
    subs.add(new CharSub('\u2200', "&forall;"));
    subs.add(new CharSub('\u2202', "&part;"));
    subs.add(new CharSub('\u2203', "&exist;"));
    subs.add(new CharSub('\u2205', "&empty;"));
    subs.add(new CharSub('\u2207', "&nabla;"));
    subs.add(new CharSub('\u2208', "&isin;"));
    subs.add(new CharSub('\u2209', "&notin;"));
    subs.add(new CharSub('\u220B', "&ni;"));
    subs.add(new CharSub('\u220F', "&prod;"));
    subs.add(new CharSub('\u2211', "&sum;"));
    subs.add(new CharSub('\u2212', "&minus;"));
    subs.add(new CharSub('\u2217', "&lowast;"));
    subs.add(new CharSub('\u221A', "&radic;"));
    subs.add(new CharSub('\u221D', "&prop;"));
    subs.add(new CharSub('\u221E', "&infin;"));
    subs.add(new CharSub('\u2220', "&ang;"));
    subs.add(new CharSub('\u2227', "&and;"));
    subs.add(new CharSub('\u2228', "&or;"));
    subs.add(new CharSub('\u2229', "&cap;"));
    subs.add(new CharSub('\u222A', "&cup;"));
    subs.add(new CharSub('\u222B', "&int;"));
    subs.add(new CharSub('\u2234', "&there4;"));
    subs.add(new CharSub('\u223C', "&sim;"));
    subs.add(new CharSub('\u2245', "&cong;"));
    subs.add(new CharSub('\u2248', "&asymp;"));
    subs.add(new CharSub('\u2260', "&ne;"));
    subs.add(new CharSub('\u2261', "&equiv;"));
    subs.add(new CharSub('\u2264', "&le;"));
    subs.add(new CharSub('\u2265', "&ge;"));
    subs.add(new CharSub('\u2282', "&sub;"));
    subs.add(new CharSub('\u2283', "&sup;"));
    subs.add(new CharSub('\u2284', "&nsub;"));
    subs.add(new CharSub('\u2286', "&sube;"));
    subs.add(new CharSub('\u2287', "&supe;"));
    subs.add(new CharSub('\u2295', "&oplus;"));
    subs.add(new CharSub('\u2297', "&otimes;"));
    subs.add(new CharSub('\u22A5', "&perp;"));
    subs.add(new CharSub('\u22C5', "&sdot;"));
    subs.add(new CharSub('\u2308', "&lceil;"));
    subs.add(new CharSub('\u2309', "&rceil;"));
    subs.add(new CharSub('\u230A', "&lfloor;"));
    subs.add(new CharSub('\u230B', "&rfloor;"));
    subs.add(new CharSub('\u2329', "&lang;"));
    subs.add(new CharSub('\u232A', "&rang;"));
    subs.add(new CharSub('\u25CA', "&loz;"));
    subs.add(new CharSub('\u2660', "&spades;"));
    subs.add(new CharSub('\u2663', "&clubs;"));
    subs.add(new CharSub('\u2665', "&hearts;"));
    subs.add(new CharSub('\u2666', "&diams;"));
    subs.add(new CharSub('\u0152', "&OElig;"));
    subs.add(new CharSub('\u0153', "&oelig;"));
    subs.add(new CharSub('\u0160', "&Scaron;"));
    subs.add(new CharSub('\u0161', "&scaron;"));
    subs.add(new CharSub('\u0178', "&Yuml;"));
    subs.add(new CharSub('\u02C6', "&circ;"));
    subs.add(new CharSub('\u02DC', "&tilde;"));
    subs.add(new CharSub('\u2002', "&ensp;"));
    subs.add(new CharSub('\u2003', "&emsp;"));
    subs.add(new CharSub('\u2009', "&thinsp;"));
    subs.add(new CharSub('\u200C', "&zwnj;"));
    subs.add(new CharSub('\u200D', "&zwj;"));
    subs.add(new CharSub('\u200E', "&lrm;"));
    subs.add(new CharSub('\u200F', "&rlm;"));
    subs.add(new CharSub('\u2013', "&ndash;"));
    subs.add(new CharSub('\u2014', "&mdash;"));
    subs.add(new CharSub('\u2018', "&lsquo;"));
    subs.add(new CharSub('\u2019', "&rsquo;"));
    subs.add(new CharSub('\u201A', "&sbquo;"));
    subs.add(new CharSub('\u201C', "&ldquo;"));
    subs.add(new CharSub('\u201D', "&rdquo;"));
    subs.add(new CharSub('\u201E', "&bdquo;"));
    subs.add(new CharSub('\u2020', "&dagger;"));
    subs.add(new CharSub('\u2021', "&Dagger;"));
    subs.add(new CharSub('\u2030', "&permil;"));
    subs.add(new CharSub('\u2039', "&lsaquo;"));
    subs.add(new CharSub('\u203A', "&rsaquo;"));
    subs.add(new CharSub('\u20AC', "&euro;"));

    final CharSubConv full_entity_conv = new CharSubConv(subs);
    HTML_FULL = new AggregateCharConv(full_entity_conv, ISO_CONV);
    HTML_FULL_ATTR = new AggregateCharConv(full_entity_conv, ISO_CONV_ATTR);

    HTML_ASCII = new AggregateCharConv(full_entity_conv, ASCII_CONV);
    HTML_ASCII_ATTR = new AggregateCharConv(full_entity_conv, ASCII_CONV_ATTR);
  }

  private HtmlConv()
  {
    // empty
  }
}
