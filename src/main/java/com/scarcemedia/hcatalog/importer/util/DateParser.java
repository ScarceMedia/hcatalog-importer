/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.hcatalog.importer.util;

import com.google.common.base.Preconditions;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 *
 * @author jeremy
 */
public class DateParser {

  List<DateFormat> formatters;

  public DateParser(){
    this(TimeZone.getTimeZone("UTC"));
  }
  
  public DateParser(TimeZone defaultTimezone) {
    Preconditions.checkArgument(null!=defaultTimezone, "defaultTimezone cannot be null");
    formatters = new ArrayList<DateFormat>();
    
    //2007-11-10 02.56.18.000000
    formatters.add(new SimpleDateFormat("yyyy-MM-dd HH.mm.ss.000000")); 

    for (DateFormat formatter : formatters) {
      formatter.setTimeZone(defaultTimezone);
    }
    
  }

  public Date parse(String input) {
    Preconditions.checkArgument(null != input && !input.isEmpty(), "input cannot be null.");

    for (DateFormat formatter : formatters) {
      try {
        Date date = formatter.parse(input);
        return date;
      } catch (ParseException ex) {
      }
    }

    return null;
  }
}
