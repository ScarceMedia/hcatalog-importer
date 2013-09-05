/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.hcatalog.importer.typeconverter;

/**
 *
 * @author jeremy
 */
public class IntTypeConverter extends TypeConverter {

  @Override
  public Object doConvert(String s) {
    return Integer.parseInt(s);
  }

  @Override
  public Class getType() {
    return Integer.class;
  }
  
}
