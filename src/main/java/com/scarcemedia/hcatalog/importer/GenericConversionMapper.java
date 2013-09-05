/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scarcemedia.hcatalog.importer;

import com.google.common.base.Preconditions;
import com.scarcemedia.hcatalog.importer.typeconverter.TypeConverter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hcatalog.data.DefaultHCatRecord;
import org.apache.hcatalog.data.HCatRecord;

/**
 *
 * @author jeremy
 */
public class GenericConversionMapper extends Mapper<LongWritable, Text, WritableComparable, HCatRecord> {

  String[] columns;
  Pattern pattern;
  Map<String, TypeConverter> converterLookup;
  boolean emitKey;

  @Override
  protected void setup(Context context) throws IOException, InterruptedException {
    emitKey = context.getNumReduceTasks() > 0;
    columns = context.getConfiguration().getTrimmedStrings("conversion.generic.columns");

    Preconditions.checkArgument(null == columns && columns.length > 0, "conversion.generic.columns cannot be null.");

    String patternText = context.getConfiguration().get("conversion.generic.pattern");
    Preconditions.checkArgument(null != patternText && !patternText.isEmpty(), "conversion.generic.pattern cannot be null.");
    try {
      pattern = Pattern.compile(patternText);
    } catch (Exception ex) {
      throw new IOException("Could not create regex from '" + patternText + "'", ex);
    }

    converterLookup = new HashMap<String, TypeConverter>();
    for (String column : columns) {
      String key = String.format("conversion.generic.column.%s", column);
      String typeConverter = context.getConfiguration().get(key, "string");

      TypeConverter converter = TypeConverter.get(typeConverter);
      converterLookup.put(column, converter);
    }
  }

  @Override
  protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    if (value.getLength() == 0) {
      return;
    }

    Matcher matcher = pattern.matcher(value.toString());
    HCatRecord record = new DefaultHCatRecord(columns.length);

    int index = 0;
    while (matcher.find()) {
      if (index >= record.size()) {
        break;
      }

      String v = matcher.group(0);
      String column = columns[index];
      TypeConverter converter = converterLookup.get(column);

      try {
        Object o = converter.convert(v);
        record.set(index, o);
      } catch (Exception ex) {
        throw new IOException("Exception while converting '" + v + "' to " + converter.getType(), ex);
      }

      index++;
    }

    WritableComparable outputKey = emitKey ? key : NullWritable.get();
    context.write(outputKey, record);
    context.progress();
  }
}
