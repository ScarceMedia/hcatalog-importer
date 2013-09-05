/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.hcatalog.importer.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author jeremy
 */
public class DateParserTests {

  DateParser parser;
  DateFormat formatter;

  @Before
  public void setup() {
    parser = new DateParser();

    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
  }

  void assertDate(String expected, String input) throws ParseException {
    formatter.parse(expected); //Ensure the input date is at least parsable.
    final Date actualDate = parser.parse(input);

    String actual = formatter.format(actualDate);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void test_0001() throws ParseException {
    assertDate("2007-11-10 02:56:18 UTC", "2007-11-10 02.56.18.000000");
  }
}
