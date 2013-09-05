/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.hcatalog.importer.typeconverter;

/**
 *
 * @author jeremy
 */
public abstract class TypeConverter {

  protected abstract Object doConvert(String s);
  public abstract Class getType();
  
  public Object convert(String s){
    if(null==s||s.isEmpty()){
      return null;
    }
    
    return doConvert(s);
  }
  
  public static TypeConverter get(String type){
    return null;
  }
  
}
