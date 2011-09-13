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
package com.evanmclean.evlib.sql;

import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.evanmclean.evlib.escape.Esc;
import com.evanmclean.evlib.lang.Arr;
import com.evanmclean.evlib.lang.Obj;
import com.evanmclean.evlib.lang.Str;
import com.evanmclean.evlib.util.TreeMapIgnoreCase;
import com.evanmclean.evlib.util.TreeSetIgnoreCase;

/**
 * Converts a result set to XML output. In it's simplest form it writes one
 * output record for each input record. It can also output sub-records to any
 * level, based on break values of columns that can change for each row.
 * 
 * @author Evan M<sup>c</sup>Lean <a href="http://evanmclean.com/"
 *         target="_blank">M<sup>c</sup>Lean Computer Services</a> (see the
 *         overview for copyright and licensing.)
 */
public class RsXml
{
  private class Meta
  {
    boolean first = true;
    boolean gotobjs;
    boolean eors;
    int read;
    int totalRead;
    int written;
    int totalWritten;
    int indent;
    int numcols;
    int lastlevel;

    // The column indicies to be written at each level.
    int[][] cols;

    // The column names for each column index.
    String[] columnNames;

    // The column SQL types.
    int[] columnTypes;

    // The element names to be used for each column index.
    String[] elementNames;

    // Indicates if the column should be written in a CDATA container.
    boolean[] cdata;

    // The break column indicies to be used for each level.
    int[][] brk;

    // The previous values for each break value.
    Object[][] prev;

    // The objects for the current row.
    Object[] objs;

    @SuppressWarnings( "synthetic-access" )
    @edu.umd.cs.findbugs.annotations.SuppressWarnings( "DM_CONVERT_CASE" )
    Meta() throws SQLException
    {
      if ( rs == null )
        throw new IllegalStateException("No result set has been set.");
      try
      {
        if ( rs.isBeforeFirst() )
          eors = !rs.next();
        else
          eors = rs.isAfterLast();
      }
      catch ( SQLException ex )
      {
        // Assuming it does not support isBeforeFirst or isAfterLast.
        eors = !rs.next();
      }

      if ( eors )
        return;

      final ResultSetMetaData rsmeta = rs.getMetaData();
      numcols = rsmeta.getColumnCount();

      final Map<String, Integer> colidxs = new TreeMapIgnoreCase<Integer>();

      columnNames = new String[numcols + 1];
      columnNames[0] = null;
      columnTypes = new int[numcols + 1];
      columnTypes[0] = 0;
      elementNames = new String[numcols + 1];
      elementNames[0] = null;
      cdata = new boolean[numcols + 1];
      cdata[0] = false;
      objs = new Object[numcols + 1];
      objs[0] = null;
      readCols();

      for ( int xi = 1; xi <= numcols; ++xi )
      {
        final String name = rsmeta.getColumnName(xi);
        colidxs.put(name, xi);
        columnNames[xi] = name;
        columnTypes[xi] = rsmeta.getColumnType(xi);
        String elname = null;
        if ( columnAliases != null )
          elname = columnAliases.get(name);
        if ( elname == null )
          elname = lowercase ? name.toLowerCase() : name;
        elementNames[xi] = elname;
        if ( (cdataColumns != null) && cdataColumns.contains(name) )
          cdata[xi] = true;
      }

      final boolean[] seen = new boolean[numcols + 1];
      Arrays.fill(seen, false);

      lastlevel = recordNames.length - 1;
      cols = new int[recordNames.length][];
      brk = new int[recordNames.length][];
      prev = new Object[recordNames.length][];

      for ( int lvl = 0; lvl < recordNames.length; ++lvl )
      {
        // Initiase cols for this level.
        if ( (columns == null) || (columns[lvl] == null)
            || (columns[lvl].size() <= 0) )
        {
          int len = 0;
          for ( int xi = 1; xi <= numcols; ++xi )
            if ( !seen[xi] )
              ++len;
          final int[] arr = new int[len];
          int nextidx = 0;
          for ( int xi = 1; xi <= numcols; ++xi )
            if ( !seen[xi] )
            {
              arr[nextidx++] = xi;
              seen[xi] = true;
            }
          cols[lvl] = resize(arr, nextidx);
        }
        else
        {
          final int[] arr = new int[columns[lvl].size()];
          int nextidx = 0;
          for ( String column : columns[lvl] )
          {
            final Integer idx = colidxs.get(column);
            if ( idx != null )
            {
              arr[nextidx++] = idx;
              seen[idx] = true;
            }
          }
          cols[lvl] = resize(arr, nextidx);
        }

        // Initialise brk for this level.
        if ( (breakColumns == null) || (breakColumns.length <= lvl) )
        {
          brk[lvl] = new int[0];
          prev[lvl] = null;
        }
        else
        {
          final int[] arr = new int[breakColumns[lvl].size()];
          int nextidx = 0;
          for ( String column : breakColumns[lvl] )
          {
            final Integer idx = colidxs.get(column);
            if ( idx != null )
              arr[nextidx++] = idx;
          }
          brk[lvl] = resize(arr, nextidx);

          final Object[] oarr = new Object[brk[lvl].length];
          for ( int xi = 0; xi < oarr.length; ++xi )
            oarr[xi] = objs[brk[lvl][xi]];
          prev[lvl] = oarr;
        }
      }
    }

    int findBreakLevel()
    {
      int brklvl = lastlevel;
      for ( int lvlxi = 0; lvlxi <= lastlevel; ++lvlxi )
      {
        final int[] brks = brk[lvlxi];
        final Object[] prevs = prev[lvlxi];
        for ( int xi = 0; xi < brks.length; ++xi )
        {
          final Object obj = objs[brks[xi]];
          if ( brklvl == lastlevel )
            if ( !Obj.equals(obj, prevs[xi]) )
              brklvl = lvlxi;
          prevs[xi] = obj;
        }
      }
      return brklvl;
    }

    @SuppressWarnings( "synthetic-access" )
    boolean next() throws SQLException
    {
      if ( eors )
        return false;
      ++read;
      ++totalRead;
      if ( !rs.next() )
      {
        eors = true;
        return false;
      }
      readCols();
      return true;
    }

    @SuppressWarnings( "synthetic-access" )
    private void readCols() throws SQLException
    {
      for ( int xi = 1; xi <= numcols; ++xi )
      {
        Object obj = rs.getObject(xi);
        if ( (obj != null) && rs.wasNull() )
          obj = null;
        objs[xi] = obj;
      }
      gotobjs = true;
    }

    private int[] resize( final int[] arr, final int size )
    {
      final int arrlen = Arr.length(arr);
      if ( arrlen < size )
        throw new IllegalStateException();
      if ( arrlen == size )
        return arr;
      final int[] ret = new int[size];
      System.arraycopy(arr, 0, ret, 0, size);
      return ret;
    }
  }

  /**
   * The default encoding (&quot;ISO-8859-1&quot;) which will be written to the
   * XML declaration.
   */
  public static final String DEFAULT_ENCODING = "ISO-8859-1";

  /**
   * The default format used (&quot;yyyy-MM-dd&quot;) to convert a date column
   * to a string.
   */
  public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

  /**
   * The default format used (&quot;HH:mm:ss.SSS&quot;) to convert a time column
   * to a string.
   */
  public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss.SSS";

  /**
   * The default format used (&quot;yyyy-MM-dd HH:mm:ss.SSS&quot;) to convert a
   * timestamp column to a string.
   */
  public static final String DEFAULT_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

  private static final String[] EMPTY_STRING_ARRAY = new String[0];
  private static final String SYSTEM_LINE_SEPARATOR = System
      .getProperty("line.separator");

  private ResultSet rs;
  private Meta meta;
  private boolean lowercase = true;
  private boolean prettyPrint;
  private boolean prettyIndent = true;
  private boolean includeXmlDeclaration = true;
  private String encoding = DEFAULT_ENCODING;
  private String containerElementName;
  private String containerAttributes;
  private String lineSeparator = SYSTEM_LINE_SEPARATOR;
  private boolean emptyAsOpenAndCloseElement;
  private String nullSnippet;
  private String[] recordNames;
  private Set<String>[] columns;
  private Set<String>[] breakColumns;
  private Map<String, String> columnAliases;
  private Set<String> cdataColumns;
  private DateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
  private DateFormat timeFormat = new SimpleDateFormat(DEFAULT_TIME_FORMAT);

  private DateFormat timestampFormat = new SimpleDateFormat(
      DEFAULT_TIMESTAMP_FORMAT);

  /**
   * Default constructor. A result set and at least one record name must be
   * specified before you can write.
   */
  public RsXml()
  {
    // empty
  }

  /**
   * Sets the record set to be used. At least one record name must be specified
   * before you can write.
   * 
   * @param rs
   *        Result set to use.
   */
  public RsXml( final ResultSet rs )
  {
    setResultSet(rs);
  }

  /**
   * Sets the record set and single record name to be used, thus writing one
   * record per row returned in the result set, and including all columns. By
   * default no XML declaration or container element will be written.
   * 
   * @param rs
   *        Result set to use.
   * @param record_name
   *        The single record name to be used.
   */
  public RsXml( final ResultSet rs, final String record_name )
  {
    setResultSet(rs);
    setRecord(record_name);
  }

  /**
   * Sets the record set, container name and single record name to be used, thus
   * writing one record per row returned in the result set, and including all
   * columns. By default an XML declaration with the default encoding and the
   * container element will be written.
   * 
   * @param rs
   *        Result set to use.
   * @param container_element_name
   *        The name of container element to be written, surrounding all the
   *        records written.
   * @param record_name
   *        The single record name to be used.
   */
  public RsXml( final ResultSet rs, final String container_element_name,
      final String record_name )
  {
    setResultSet(rs);
    setContainerElementName(container_element_name);
    setRecord(record_name);
  }

  /**
   * Sets the record set, container name and single record name to be used, thus
   * writing one record per row returned in the result set, only including the
   * specified columns. By default an XML declaration with the default encoding
   * and the container element will be written.
   * 
   * @param rs
   *        Result set to use.
   * @param container_element_name
   *        The name of container element to be written, surrounding all the
   *        records written.
   * @param record_name
   *        The single record name to be used.
   * @param columns
   *        The only columns that will be written in the XML output.
   */
  public RsXml( final ResultSet rs, final String container_element_name,
      final String record_name, final String[] columns )
  {
    setResultSet(rs);
    setContainerElementName(container_element_name);
    setRecord(record_name, columns);
  }

  /**
   * Sets the record set and single record name to be used, thus writing one
   * record per row returned in the result set, and including only the specified
   * columns. By default no XML declaration or container element will be
   * written.
   * 
   * @param rs
   *        Result set to use.
   * @param record_name
   *        The single record name to be used.
   * @param columns
   *        The only columns that will be written in the XML output.
   */
  public RsXml( final ResultSet rs, final String record_name,
      final String[] columns )
  {
    setResultSet(rs);
    setRecord(record_name, columns);
  }

  /**
   * Clear the list of the columns to be wrapped in a <code>&lt;![CDATA[</code>
   * container instead of the usual XML encoding.
   */
  public void clearCdataColumns()
  {
    if ( cdataColumns != null )
      cdataColumns.clear();
  }

  /**
   * Clear all the column aliases.
   */
  public void clearColumnAliases()
  {
    if ( columnAliases != null )
      columnAliases.clear();
  }

  /**
   * No container element (and hence, no XML declaration) will be written on a
   * call to write.
   */
  public void clearContainerElementName()
  {
    containerElementName = null;
  }

  /**
   * No encoding attribute will be written in the XML declaration.
   */
  public void clearEncoding()
  {
    encoding = null;
  }

  /**
   * Indicates if we have reached the end of the record set.
   * 
   * @return True if we have reached the end of the record set.
   * @throws SQLException
   */
  public boolean eors() throws SQLException
  {
    return getMeta().eors;
  }

  /**
   * The break columns being used.
   * 
   * @return The break columns being used, or an empty array if none have been
   *         specified yet.
   */
  public String[][] getBreakColumns()
  {
    if ( Arr.length(breakColumns) <= 0 )
      return new String[0][];
    final String[][] arr = new String[breakColumns.length][];
    for ( int xi = 0; xi < arr.length; ++xi )
    {
      final Set<String> cols = breakColumns[xi];
      final int len = cols.size();
      arr[xi] = cols.toArray(new String[len]);
    }
    return arr;
  }

  /**
   * A list of the columns to be wrapped in a <code>&lt;![CDATA[</code>
   * container instead of the usual XML encoding.
   * 
   * @return The columns.
   */
  public String[] getCdataColumns()
  {
    final int len = (cdataColumns == null) ? 0 : cdataColumns.size();
    if ( len <= 0 )
      return EMPTY_STRING_ARRAY;
    return cdataColumns.toArray(new String[len]);
  }

  /**
   * By default, the column name is used as the element name, however aliases
   * can be used.
   * 
   * @return Return the array map of column names and alias values that will be
   *         used.
   */
  public String[][] getColumnAliases()
  {
    final int len = (columnAliases == null) ? 0 : columnAliases.size();
    if ( len <= 0 )
      return new String[0][];
    final String[][] arr = new String[len][];
    int xi = -1;
    for ( Map.Entry<String, String> entry : columnAliases.entrySet() )
    {
      final String[] subarr = new String[2];
      subarr[0] = entry.getKey();
      subarr[1] = entry.getValue();
      arr[++xi] = subarr;
    }
    return arr;
  }

  /**
   * The columns to be written at each level.
   * 
   * @return The columns to be written at each level, or an empty array if none
   *         have been specified yet.
   */
  public String[][] getColumns()
  {
    if ( Arr.length(columns) <= 0 )
      return new String[0][];
    final String[][] arr = new String[columns.length][];
    for ( int xi = 0; xi < arr.length; ++xi )
    {
      final Set<String> cols = columns[xi];
      final int len = cols.size();
      arr[xi] = cols.toArray(new String[len]);
    }
    return arr;
  }

  /**
   * A snippet of XML attributes that will be written at part of the container
   * element (default none). This snippet must be valid XML. For example:
   * <code>xmlns:h=&quot;http://www.w3.org/TR/html4/&quot;</code>
   * 
   * @return The container snippet.
   */
  public String getContainerAttributes()
  {
    return containerAttributes;
  }

  /**
   * The name of the container element that will be written surrounding all the
   * records in the next write. By default this is an empty string, indicating
   * no container (and hence, no XML declaration) will be written.
   * 
   * @return The name of the container element.
   */
  public String getContainerElementElementName()
  {
    return Str.ifNull(containerElementName);
  }

  /**
   * For formatting object used to convert date columns to strings.
   * 
   * @return The date formatting object.
   */
  public DateFormat getDateFormat()
  {
    return dateFormat;
  }

  /**
   * The value of the encoding attribute that will be written in the XML
   * declaration (e.g. &quot;
   * <code>&lt;?xml&nbsp;version=&quot;1.0&quot;&nbsp;encoding=&quot;ISO-8859-1&quot;?&gt;</code>
   * &quot;). By default the value is specified by the {@link #DEFAULT_ENCODING}
   * field. If this is an empty string then no encoding attribute will be
   * written.
   * 
   * @return The encoding attribute value.
   */
  public String getEncoding()
  {
    return Str.ifNull(encoding);
  }

  /**
   * The line separator to be used after the XML declaration, and for pretty
   * printing (default is system property).
   * 
   * @return The line separator.
   */
  public String getLineSeperator()
  {
    return lineSeparator;
  }

  /**
   * If the column is null, then then this XML snipped is included in the
   * element being written (default blank). For example, if this property is set
   * to &quot;<code>null=&quot;true&quot;</code>&quot; then the element will be
   * written as &quot;<code>&lt;element null=&quot;true&quot; /&gt;</code>
   * &quot;.
   * 
   * @return The snippet used for nulls.
   */
  public String getNullSnippet()
  {
    return Str.ifNull(nullSnippet);
  }

  /**
   * Return the specified object from the last row read.
   * 
   * @param name
   *        The column name.
   * @return The specified object.
   */
  public Object getObject( final String name )
  {
    final Meta md = meta;
    if ( (md == null) || (!md.gotobjs) )
      throw new IllegalStateException();
    for ( int xi = 1; xi < md.columnNames.length; ++xi )
      if ( name.equalsIgnoreCase(md.columnNames[xi]) )
        return md.objs[xi];
    throw new IllegalArgumentException();
  }

  /**
   * Return the objects from the last row read.
   * 
   * @return The objects.
   */
  public Object[] getObjects()
  {
    final Meta md = meta;
    if ( (md == null) || (!md.gotobjs) )
      throw new IllegalStateException();
    final Object[] orig = md.objs;
    final Object[] objs = new Object[orig.length - 1];
    System.arraycopy(orig, 1, objs, 0, objs.length);
    return objs;
  }

  /**
   * The record names being used.
   * 
   * @return The record names being used, or an empty array if none have been
   *         specified yet.
   */
  public String[] getRecordNames()
  {
    if ( (recordNames == null) || (recordNames.length <= 0) )
      return EMPTY_STRING_ARRAY;
    final String[] arr = new String[recordNames.length];
    System.arraycopy(recordNames, 0, arr, 0, recordNames.length);
    return arr;
  }

  /**
   * Gets the result set to be read, or null if it has not been set yet.
   * 
   * @return Gets the result set to be read, or null if it has not been set yet.
   */
  public ResultSet getResultSet()
  {
    return rs;
  }

  /**
   * For formatting object used to convert time columns to strings.
   * 
   * @return The formatting object.
   */
  public DateFormat getTimeFormat()
  {
    return timeFormat;
  }

  /**
   * For formatting object used to convert timestamp columns to strings.
   * 
   * @return The formatting object.
   */
  public DateFormat getTimestampFormat()
  {
    return timestampFormat;
  }

  /**
   * By default, elements with an empty (or null) value will be written as a
   * single element (e.g. &quot;<code>&lt;element&nbsp;/gt;</code>&quot;). If
   * this is true then it will be written as an open element followed by a close
   * element (e.g. &quot;<code>&lt;element&gt;&lt;/element&gt;</code>&quot;).
   * 
   * @return True if we write as a separate open and close element.
   */
  public boolean isEmptyAsOpenAndCloseElement()
  {
    return emptyAsOpenAndCloseElement;
  }

  /**
   * Indicates if the XML declaration (i.e. &quot;
   * <code>&lt;?xml&nbsp;version=&quot;1.0&quot;?&gt;</code>&quot; will be
   * written at the start of the write (default true). <em>Note:</em> The XML
   * declaration will not be written if a container name has not been specified
   * regardless of the value of this flag.
   * 
   * @return Will write the XML declaration.
   */
  public boolean isIncludeXmlDeclaration()
  {
    return includeXmlDeclaration;
  }

  /**
   * Indicates if all column names (not including aliased columns) are forced to
   * lowercase before being written as element names (default true).
   * 
   * @return True if element names are always lowercase.
   */
  public boolean isLowercase()
  {
    return lowercase;
  }

  /**
   * Indicates if the XML is written with newlines and indentation (default true
   * when pretty print is true, false otherwise).
   * 
   * @return True if pretty printing with indentation.
   */
  public boolean isPrettyIndent()
  {
    return prettyPrint && prettyIndent;
  }

  /**
   * Indicates if the XML is written with newlines and (possibly) indentation
   * (default false).
   * 
   * @return True if pretty printing.
   */
  public boolean isPrettyPrint()
  {
    return prettyPrint;
  }

  /**
   * The number of individual rows read from the record set in the last write
   * operation.
   * 
   * @return int
   */
  public int read()
  {
    return (meta == null) ? 0 : meta.read;
  }

  /**
   * A list of the columns to be wrapped in a <code>&lt;![CDATA[</code>
   * container instead of the usual XML encoding.
   * 
   * @param colmns
   *        The list of columns.
   */
  public void setCdataColumns( final String[] colmns )
  {
    // Process columns.
    if ( Arr.length(colmns) <= 0 )
    {
      clearCdataColumns();
      return;
    }

    final Set<String> cols = new TreeSetIgnoreCase();
    for ( String val : colmns )
    {
      final String str = Str.trimToNull(val);
      if ( str == null )
        throw new IllegalArgumentException("Invalid column name for record.");
      cols.add(str);
    }

    cdataColumns = cols;
  }

  /**
   * By default, the column name is used as the element name, however aliases
   * can be used.
   * 
   * @param column_aliases
   *        The column names and aliases to be used.
   */
  public void setColumnAliases(
      final Map<? extends String, ? extends String> column_aliases )
  {
    if ( (column_aliases == null) || (column_aliases.size() <= 0) )
    {
      clearColumnAliases();
      return;
    }

    final Map<String, String> aliases = new TreeMapIgnoreCase<String>();
    for ( Map.Entry<? extends String, ? extends String> alias : column_aliases
        .entrySet() )
    {
      final String col = Str.trimToNull(alias.getKey());
      final String al = Str.trimToNull(alias.getValue());
      if ( (col == null) || (al == null) )
        throw new IllegalArgumentException(
            "Record name cannot be null or a blank string.");
      aliases.put(col, al);
    }
    columnAliases = aliases;
  }

  /**
   * By default, the column name is used as the element name, however aliases
   * can be used.
   * 
   * @param column_aliases
   *        The column names and aliases to be used.
   */
  public void setColumnAliases( final String[][] column_aliases )
  {
    if ( Arr.length(column_aliases) <= 0 )
    {
      clearColumnAliases();
      return;
    }

    final Map<String, String> aliases = new TreeMapIgnoreCase<String>();
    for ( String[] alias : column_aliases )
    {
      if ( (alias == null) || (alias.length != 2) )
        throw new IllegalArgumentException("Invalid column alias entry.");
      final String col = Str.trimToNull(alias[0]);
      final String al = Str.trimToNull(alias[1]);
      if ( (col == null) || (al == null) )
        throw new IllegalArgumentException(
            "Record name cannot be null or a blank string.");
      aliases.put(col, al);
    }
    columnAliases = aliases;
  }

  /**
   * A snippet of XML attributes that will be written at part of the container
   * element (default none). This snippet must be valid XML. For example:
   * <code>xmlns:h=&quot;http://www.w3.org/TR/html4/&quot;</code>
   * 
   * @param container_attributes
   *        A snippet of XML attributes to be included, or a blank/null string
   *        to indicae no attributes.
   */
  public void setContainerAttributes( final String container_attributes )
  {
    containerAttributes = Str.trimToNull(container_attributes);
  }

  /**
   * The name of the container element that will be written surrounding all the
   * records in the next write. By default this is an empty string, indicating
   * no container (and hence, no XML declaration) will be written.
   * 
   * @param container_element_name
   *        The name of the container element, or blank/null to indicate no
   *        container element is to be written.
   */
  public void setContainerElementName( final String container_element_name )
  {
    containerElementName = Str.trimToNull(container_element_name);
  }

  /**
   * For formatting object used to convert date columns to strings.
   * 
   * @param date_format
   */
  public void setDateFormat( final DateFormat date_format )
  {
    if ( date_format == null )
      throw new NullPointerException();
    dateFormat = date_format;
  }

  /**
   * By default, elements with an empty (or null) value will be written as a
   * single element (e.g. &quot;<code>&lt;element&nbsp;/gt;</code>&quot;). If
   * this is true then it will be written as an open element followed by a close
   * element (e.g. &quot;<code>&lt;element&gt;&lt;/element&gt;</code>&quot;).
   * 
   * @param empty_as_open_and_close_element
   */
  public void setEmptyAsOpenAndCloseElement(
      final boolean empty_as_open_and_close_element )
  {
    emptyAsOpenAndCloseElement = empty_as_open_and_close_element;
  }

  /**
   * The value of the encoding attribute that will be written in the XML
   * declaration (e.g. &quot;
   * <code>&lt;?xml&nbsp;version=&quot;1.0&quot;&nbsp;encoding=&quot;ISO-8859-1&quot;?&gt;</code>
   * &quot;). By default the value is specified by the {@link #DEFAULT_ENCODING}
   * field. If this is a blank string or null then no encoding attribute will be
   * written.
   * 
   * @param enc
   *        The value of the encoding attribute, or a blank/null string to
   *        indicate no encoding attribute is to be written.
   */
  public void setEncoding( final String enc )
  {
    encoding = Str.trimToNull(enc);
  }

  /**
   * Indicates if the XML declaration (i.e. &quot;
   * <code>&lt;?xml&nbsp;version=&quot;1.0&quot;?&gt;</code>&quot;) will be
   * written at the start of the write (default true). <em>Note:</em> The XML
   * declaration will not be written if a container name has not been specified
   * regardless of the value of this flag.
   * 
   * @param include_xml_declaration
   */
  public void setIncludeXmlDeclaration( final boolean include_xml_declaration )
  {
    includeXmlDeclaration = include_xml_declaration;
  }

  /**
   * The line separator to be used after the XML declaration, and for pretty
   * printing (default is system property).
   * 
   * @param line_separator
   *        The line separator string to use. If empty or null then the system
   *        default will be used.
   */
  public void setLineSeperator( final String line_separator )
  {
    lineSeparator = Str.ifEmpty(line_separator, SYSTEM_LINE_SEPARATOR);
  }

  /**
   * Set if all column names (not including aliased columns) are forced to
   * lowercase before being written as element names (default true).
   * 
   * @param lwrcase
   *        True if all column names are forced to lowercase before being
   *        written.
   */
  public void setLowercase( final boolean lwrcase )
  {
    lowercase = lwrcase;
  }

  /**
   * If the column is null, then then this XML snipped is included in the
   * element being written (default blank). For example, if this property is set
   * to &quot;<code>null=&quot;true&quot;</code>&quot; then the element will be
   * written as &quot;<code>&lt;element null=&quot;true&quot; /&gt;</code>
   * &quot;.
   * 
   * @param null_snippet
   */
  public void setNullSnippet( final String null_snippet )
  {
    nullSnippet = Str.trimToNull(null_snippet);
  }

  /**
   * Indicates if the XML is written with newlines and indentation (default
   * false).
   * 
   * @param pretty_print
   * @param pretty_indent
   */
  public void setPretty( final boolean pretty_print, final boolean pretty_indent )
  {
    prettyPrint = pretty_print;
    prettyIndent = pretty_indent;
  }

  /**
   * Indicates if the XML is written with newlines and indentation (default
   * false).
   * 
   * @param pretty_print
   */
  public void setPrettyPrint( final boolean pretty_print )
  {
    prettyPrint = pretty_print;
  }

  /**
   * Sets the record name to be used, thus writing one record per row returned
   * in the result set, and including all columns.
   * 
   * @param record_name
   *        The single record name to be used.
   */
  public void setRecord( final String record_name )
  {
    final String rec_name = Str.trimToNull(record_name);
    if ( rec_name == null )
      throw new IllegalArgumentException(
          "Record name cannot be null or a blank string.");
    final String[] arr = new String[1];
    arr[0] = rec_name;

    recordNames = arr;
    breakColumns = null;
    columns = null;
  }

  /**
   * Sets the single record name to be used, thus writing one record per row
   * returned in the result set, and including only the specified columns.
   * 
   * @param record_name
   *        The single record name to be used.
   * @param columns_filter
   *        The only columns that will be written in the XML output.
   */
  public void setRecord( final String record_name, final String[] columns_filter )
  {
    // Process record name.
    final String rec_name = Str.trimToNull(record_name);
    if ( rec_name == null )
      throw new IllegalArgumentException(
          "Record name cannot be null or a blank string.");
    final String[] rn = new String[1];
    rn[0] = rec_name;

    // Process columns.
    if ( Arr.length(columns_filter) <= 0 )
      throw new IllegalArgumentException(
          "Number of columns cannot be zero for record.");
    final Set<String> cols = new TreeSetIgnoreCase();
    for ( String val : columns_filter )
    {
      final String str = Str.trimToNull(val);
      if ( str == null )
        throw new IllegalArgumentException("Invalid column name for record.");
      cols.add(str);
    }
    final Set<String>[] co = new TreeSetIgnoreCase[1];
    co[0] = cols;

    recordNames = rn;
    breakColumns = null;
    columns = co;
  }

  /**
   * Sets the details of the XML records to be written, with sub-records
   * possible.
   * 
   * @param record_names
   *        The record element names to be used for each level.
   * @param break_columns
   *        The columns used at each level to indicate when a new sub record is
   *        to be written. The length of this array is one less than then
   *        record_names.
   * @param columns_filter
   *        The columns to be written at each record level. It's length is
   *        usually equal to the length of the record names array, however if it
   *        is one less, then all the columns that have not been specified so
   *        far are written in the lowest level record.
   */
  public void setRecords( final String[] record_names,
      final String[][] break_columns, final String[][] columns_filter )
  {
    // Process record names.
    if ( Arr.length(record_names) <= 0 )
      throw new IllegalArgumentException("Record names must be specified.");
    final String[] rn = new String[record_names.length];
    for ( int xi = 0; xi < rn.length; ++xi )
    {
      rn[xi] = Str.trimToNull(record_names[xi]);
      if ( rn[xi] == null )
        throw new IllegalArgumentException(
            "Record name cannot be null or a blank string.");
    }

    // Process break_columns.
    if ( Arr.length(break_columns) != (rn.length - 1) )
      throw new IllegalArgumentException(
          "Break columns array cannot be null and must be one less than the number of record names.");
    final Set<String>[] brkcols = new TreeSetIgnoreCase[rn.length - 1];
    if ( rn.length > 1 )
    {
      for ( int xi = 0; xi < brkcols.length; ++xi )
      {
        final String[] co = columns_filter[xi];
        if ( Arr.length(co) <= 0 )
          throw new IllegalArgumentException(
              "Number of break columns cannot be zero for record: " + rn[xi]);
        final Set<String> set = new TreeSetIgnoreCase();
        for ( String val : co )
        {
          final String str = Str.trimToNull(val);
          if ( str == null )
            throw new IllegalArgumentException(
                "Invalid break column name for record: " + rn[xi]);
          set.add(str);
        }
        brkcols[xi] = set;
      }
    }

    // Process columns.
    final int collen = Arr.length(columns_filter);
    if ( (collen < (rn.length - 1)) || (collen > rn.length) )
      throw new IllegalArgumentException(
          "Columns array cannot be null and must match or be one less than the number of record names.");
    final Set<String>[] cols = new TreeSetIgnoreCase[rn.length];
    for ( int xi = 0; xi < rn.length; ++xi )
    {
      final String[] co = (xi >= collen) ? null : columns_filter[xi];
      if ( (co == null) || (co.length <= 0) )
      {
        if ( (xi + 1) < rn.length )
          throw new IllegalArgumentException(
              "Number of columns cannot be zero for record: " + rn[xi]);
        cols[xi] = null;
      }
      else
      {
        final Set<String> set = new TreeSetIgnoreCase();
        for ( String val : co )
        {
          final String str = Str.trimToNull(val);
          if ( str == null )
            throw new IllegalArgumentException(
                "Invalid column name for record: " + rn[xi]);
          set.add(str);
        }
        cols[xi] = set;
      }
    }

    recordNames = rn;
    breakColumns = brkcols;
    columns = cols;
  }

  /**
   * Sets the result set to be read for producing the XML.
   * 
   * @param new_rs
   *        The result set to be read to product the XML. May not be null.
   */
  public void setResultSet( final ResultSet new_rs )
  {
    if ( new_rs == null )
      throw new IllegalArgumentException(
          "Cannot set result set parameter to null.");
    rs = new_rs;
    meta = null;
  }

  /**
   * For formatting object used to convert time columns to strings.
   * 
   * @param time_format
   */
  public void setTimeFormat( final DateFormat time_format )
  {
    if ( time_format == null )
      throw new NullPointerException();
    timeFormat = time_format;
  }

  /**
   * For formatting object used to convert timestamp columns to strings.
   * 
   * @param timestamp_format
   */
  public void setTimestampFormat( final DateFormat timestamp_format )
  {
    if ( timestamp_format == null )
      throw new NullPointerException();
    timestampFormat = timestamp_format;
  }

  /**
   * The total number of individual rows read from the record set across all
   * write operations.
   * 
   * @return Total rows read.
   */
  public int totalRead()
  {
    return (meta == null) ? 0 : meta.totalRead;
  }

  /**
   * The total number of top level records written in across all write
   * operations. If there is only one record level being used, this will be the
   * same as the {@link #totalRead()} value.
   * 
   * @return Total top level records written.
   */
  public int totalWritten()
  {
    return (meta == null) ? 0 : meta.totalWritten;
  }

  /**
   * Write all remaining records from the result set as XML to the specified
   * writer. If all rows have already been written, and the container element
   * name has been specified then an empty XML document will be written. Use
   * {@link #eors} to avoid this.
   * 
   * @param out
   *        The writer to write the XML to.
   * @throws IOException
   * @throws SQLException
   */
  public void write( final Writer out ) throws IOException, SQLException
  {
    write(out, 0);
  }

  /**
   * Write records from the result set as XML to the specified writer. If all
   * rows have already been written, and the container element name has been
   * specified then an empty XML document will be written. Use {@link #eors} to
   * avoid this.
   * 
   * @param out
   *        The writer to write the XML to.
   * @param limit
   *        The maximum number of (top-level) records to write (or 0 for all
   *        remaining records).
   * @return True if there are still more records to be written.
   * @throws IOException
   * @throws SQLException
   */
  public boolean write( final Writer out, final int limit )
    throws IOException,
      SQLException
  {
    final Meta md = getMeta();
    md.read = 0;
    md.written = 0;
    md.indent = 0;
    md.first = true;

    // Write XML declaration and opening container element.
    if ( containerElementName != null )
    {
      if ( includeXmlDeclaration )
      {
        out.write("<?xml version=\"1.0\"");
        if ( encoding != null )
        {
          out.write(" encoding=\"");
          out.write(encoding);
          out.write('"');
        }
      }
      out.write("?>");
      out.write(lineSeparator);

      out.write('<');
      out.write(containerElementName);
      if ( containerAttributes != null )
      {
        out.write(' ');
        out.write(containerAttributes);
      }
      out.write('>');
      nl(out);
      md.indent += 2;
    }

    while ( !md.eors )
    {
      if ( md.first )
      {
        md.first = false;
        writeLevel(out, 0);
      }
      else
      {
        final int lvl = md.findBreakLevel();
        closeLevel(out, lvl);
        if ( (limit > 0) && (md.written >= limit) )
          break;
        writeLevel(out, lvl);
      }

      if ( !md.next() )
        closeLevel(out, 0);
    }

    // Write closing container element.
    if ( containerElementName != null )
    {
      out.write("</");
      out.write(containerElementName);
      out.write('>');
      nl(out);
    }

    return !md.eors;
  }

  /**
   * The number of top level records written in the last write operation. If
   * there is only one record level being used, this will be the same as the
   * {@link #read()} value.
   * 
   * @return Number of top level records written in the last write operation.
   */
  public int written()
  {
    return (meta == null) ? 0 : meta.written;
  }

  /**
   * Called just before closing the level, allowing any specialised action to
   * take place. By default it does nothing.
   * 
   * @param out
   *        The writer we are sending our XML to.
   * @param lvl
   *        The level that is being closed.
   * @param record_name
   *        The record that is being closed.
   * @throws IOException
   *         Can be thrown if there is an issue.
   */
  @SuppressWarnings( "unused" )
  protected void closingLevel( final Writer out, final int lvl,
      final String record_name ) throws IOException
  {
    // empty
  }

  /**
   * Converts a date to a string.
   * 
   * @param obj
   * @param column
   * @param sql_type
   * @return Date formatted as a string.
   */
  protected String getDate( final Object obj, @SuppressWarnings( "unused" )
  final String column, @SuppressWarnings( "unused" )
  final int sql_type )
  {
    return dateFormat.format((Date) obj);
  }

  /**
   * Converts a float or double to a string, trimming any trailing zeros and
   * decimal point.
   * 
   * @param obj
   * @param column
   * @param sql_type
   * @return Double formatted as a string.
   */
  protected String getDouble( final Object obj, @SuppressWarnings( "unused" )
  final String column, @SuppressWarnings( "unused" )
  final int sql_type )
  {
    return trimTrailingZeros(obj.toString());
  }

  /**
   * Converts a time to a string, removing any trailing zeros or decimal point.
   * 
   * @param obj
   * @param column
   * @param sql_type
   * @return Time formatted as a string.
   */
  protected String getTime( final Object obj, @SuppressWarnings( "unused" )
  final String column, @SuppressWarnings( "unused" )
  final int sql_type )
  {
    return timeFormat.format((Date) obj);
  }

  /**
   * Converts a timestamp to a string, removing any trailing zeros or decimal
   * point.
   * 
   * @param obj
   * @param column
   * @param sql_type
   * @return Tiemstamp formatted as a string.
   */
  protected String getTimestamp( final Object obj, @SuppressWarnings( "unused" )
  final String column, @SuppressWarnings( "unused" )
  final int sql_type )
  {
    return timestampFormat.format((Date) obj);
  }

  /**
   * Converts the object retrieved from the result set to a string. By default
   * dispatches a float or double to getDouble, a date or time related object to
   * the appropriate method, otherwise just calls toString on the object passed
   * in.
   * 
   * @param obj
   *        The object from the result set.
   * @param column
   *        The name of the column.
   * @param sql_type
   *        The SQL type of the column, a constant from java.sql.Types.
   * @return The object converted to a string.
   */
  protected String getValue( final Object obj, final String column,
      final int sql_type )
  {
    switch ( sql_type )
    {
      case Types.DOUBLE:
      case Types.FLOAT:
      case Types.DECIMAL:
        return getDouble(obj, column, sql_type);
      case Types.DATE:
        return getDate(obj, column, sql_type);
      case Types.TIME:
        return getTime(obj, column, sql_type);
      case Types.TIMESTAMP:
        return getTimestamp(obj, column, sql_type);
    }
    return obj.toString();
  }

  /**
   * Called just after opening the level, allowing any specialised action to
   * take place. By default it does nothing.
   * 
   * @param out
   *        The writer we are sending our XML to.
   * @param lvl
   *        The level that is being closed.
   * @param record_name
   *        The record that is being closed.
   */
  protected void openingLevel( @SuppressWarnings( "unused" )
  final Writer out, @SuppressWarnings( "unused" )
  final int lvl, @SuppressWarnings( "unused" )
  final String record_name )
  {
    // empty
  }

  /**
   * Set the object for the specified column.
   * 
   * @param name
   *        The column name.
   * @param newobj
   *        The new object to put in it's place.
   * @return The original object for this column.
   */
  protected Object setObject( final String name, final Object newobj )
  {
    final Meta md = meta;
    if ( (md == null) || (!md.gotobjs) )
      throw new IllegalStateException();
    for ( int xi = 1; xi < md.columnNames.length; ++xi )
      if ( name.equalsIgnoreCase(md.columnNames[xi]) )
      {
        final Object orig = md.objs[xi];
        md.objs[xi] = newobj;
        return orig;
      }
    throw new IllegalArgumentException();
  }

  /**
   * Utility function that, if the string contains a decimal point, trims
   * trailing zeros (up to and including the decimal point) off the end of the
   * string.
   * 
   * @param str
   * @return The string with trailing zeros and decimal point trimmed off.
   */
  protected String trimTrailingZeros( final String str )
  {
    final int decpos = str.indexOf('.');
    if ( decpos <= 0 )
      return str;
    final int len = str.length();
    int last_zero = len;
    while ( last_zero > decpos )
    {
      final char ch = str.charAt(last_zero - 1);
      if ( (ch != '0') && (ch != '.') )
        break;
      --last_zero;
    }
    if ( last_zero >= len )
      return str;
    return str.substring(0, last_zero);
  }

  protected void writeElement( final Writer out, final String element_name,
      final String value, final boolean cdata ) throws IOException
  {
    indent(out);
    out.write('<');
    out.write(element_name);
    if ( value == null )
    {
      if ( nullSnippet != null )
      {
        out.write(' ');
        out.write(nullSnippet);
      }

      if ( !emptyAsOpenAndCloseElement )
      {
        out.write(" />");
      }
      else
      {
        out.write("></");
        out.write(element_name);
        out.write('>');
      }
    }
    else
    {
      if ( Str.isEmpty(value) )
      {
        if ( !emptyAsOpenAndCloseElement )
        {
          out.write(" />");
        }
        else
        {
          out.write("></");
          out.write(element_name);
          out.write('>');
        }
      }
      else
      {
        out.write('>');
        if ( !cdata )
        {
          out.write(Esc.xml.text(value));
        }
        else
        {
          out.write("<![CDATA[");
          out.write(Esc.xml.cdata(value));
          out.write("]]>");
        }
        out.write("</");
        out.write(element_name);
        out.write('>');
      }
    }
    nl(out);
  }

  private void closeLevel( final Writer out, final int lvl ) throws IOException
  {
    if ( lvl < meta.lastlevel )
      closeLevel(out, lvl + 1);
    closingLevel(out, lvl, recordNames[lvl]);
    meta.indent -= 2;
    indent(out);
    out.write("</");
    out.write(recordNames[lvl]);
    out.write('>');
    nl(out);

    if ( lvl <= 0 )
    {
      meta.written += 1;
      meta.totalWritten += 1;
    }
  }

  private Meta getMeta() throws SQLException
  {
    Meta md = meta;
    if ( md == null )
    {
      md = new Meta();
      meta = md;
    }
    return md;
  }

  private void indent( final Writer out ) throws IOException
  {
    if ( prettyPrint && prettyIndent && meta.indent > 0 )
      for ( int xi = 0; xi < meta.indent; ++xi )
        out.write(' ');
  }

  private void nl( final Writer out ) throws IOException
  {
    if ( prettyPrint )
      out.write(lineSeparator);
  }

  private void writeColumn( final Writer out, final int idx )
    throws IOException
  {
    final Object obj = meta.objs[idx];
    final String value;
    if ( obj == null )
      value = null;
    else
      value = getValue(obj, meta.columnNames[idx], meta.columnTypes[idx]);

    writeElement(out, meta.elementNames[idx], value, meta.cdata[idx]);
  }

  private void writeLevel( final Writer out, final int lvl ) throws IOException
  {
    indent(out);
    out.write('<');
    out.write(recordNames[lvl]);
    out.write('>');
    nl(out);

    meta.indent += 2;

    openingLevel(out, lvl, recordNames[lvl]);

    final int[] cols = meta.cols[lvl];
    for ( int xi = 0; xi < cols.length; ++xi )
      writeColumn(out, cols[xi]);

    if ( lvl < meta.lastlevel )
      writeLevel(out, lvl + 1);
  }
}
