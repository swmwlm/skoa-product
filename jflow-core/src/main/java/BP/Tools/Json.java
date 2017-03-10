package BP.Tools;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataRowCollection;
import BP.DA.DataSet;
import BP.DA.DataTable;

public class Json
{
	/**
	 * 对象转换为Json字符串
	 */
	public static String ToJson(Object jsonObject)
	{
		JSONObject object = JSONObject.fromObject(jsonObject);
		return object.toString();
	}
	
	/**
	 * 对象集合转换Json
	 */
	@SuppressWarnings("rawtypes")
	public static String ToJson(Iterable array)
	{
		JSONArray jsonArray = JSONArray.fromObject(array);
		return jsonArray.toString();
	}
	
	/**
	 * 普通集合转换Json
	 */
	@SuppressWarnings("rawtypes")
	public static String ToArrayString(Iterable array)
	{
		JSONArray jsonArray = JSONArray.fromObject(array);
		return jsonArray.toString();
	}
	
	/**
	 * 删除结尾字符
	 * 
	 * @param str
	 *            需要删除的字符
	 */
	private static String DeleteLast(String str)
	{
		if (str.length() > 1)
		{
			return str.substring(0, str.length() - 1);
		}
		return str;
	}
	
	/**
	 * Datatable转换为Json
	 * 
	 * @param Datatable对象
	 */
	public static String ToJson(DataTable table)
	{
		String jsonString = "[";
		DataRowCollection drc = table.Rows;
		for (int i = 0; i < drc.size(); i++)
		{
			jsonString += "{";
			for (DataColumn column : table.Columns)
			{
				jsonString += "\"" + ToJson(column.ColumnName) + "\":";
				Object obj = drc.get(i).getValue(column.ColumnName);
				if (column.DataType == java.util.Date.class
						|| column.DataType == String.class)
				{
					if (null != obj)
					{
						jsonString += "\"" + ToJson(obj.toString()) + "\",";
					} else
					{
						jsonString += "\"\",";
					}
				} else
				{
					if (null != obj)
					{
						jsonString += ToJson(obj.toString()) + ",";
					} else
					{
						jsonString += ",";
					}
				}
			}
			jsonString = DeleteLast(jsonString) + "},";
		}
		return DeleteLast(jsonString) + "]";
	}
	
	/**
	 * DataSet转换为Json
	 * 
	 * @param DataSet对象
	 */
	public static String ToJson(DataSet dataSet)
	{
		String jsonString = "{";
		for (DataTable table : dataSet.Tables)
		{
			jsonString += "\"" + ToJson(table.TableName) + "\":"
					+ ToJson(table) + ",";
		}
		return jsonString = DeleteLast(jsonString) + "}";
	}
	
	/**
	 * String转换为Json
	 * 
	 * @param value
	 *            String对象
	 * @return Json字符串
	 */
	public static String ToJson(String value)
	{
		if (StringHelper.isNullOrEmpty(value))
		{
			return "";
		}
		
		String temstr;
		temstr = value;
		temstr = temstr.replace("{", "｛").replace("}", "｝").replace(":", "：")
				.replace(",", "，").replace("[", "【").replace("]", "】")
				.replace(";", "；").replace("\n", "<br/>").replace("\r", "");
		
		temstr = temstr.replace("\t", "   ");
		temstr = temstr.replace("'", "\'");
		temstr = temstr.replace("\\", "\\\\");
		temstr = temstr.replace("\"", "\"\"");
		return temstr;
	}
	
	 /** 
	  * 把一个json转化一个datatable
	  * 
	  * @param json 一个json字符串
	  * @return 序列化的datatable
	  */
	public static DataTable ToDataTable(String strJson)
	{
		DataTable tb = new DataTable();
		DataRow row = null;
		DataColumn dc = null;
		String key = "";
		String value  = "";
		//转换json格式
		JSONArray json = JSONArray.fromObject(strJson);
		tb = new DataTable();
        tb.TableName = "";
		for(int i = 0;i < json.size();i++) {
			JSONObject pjson = (JSONObject)json.get(i);
			@SuppressWarnings("unchecked")
			Iterator<String> iterator = pjson.keys();
			row = tb.NewRow();
			dc = new DataColumn();
			while(iterator.hasNext())
			{
				key = (String) iterator.next();
				value = pjson.getString(key);
				dc.ColumnName = key;
				tb.Columns.Add(dc.ColumnName);
				row.setValue(dc.ColumnName, value);
			}
			tb.Rows.add(i, row);
		}
		
		return tb;
	}

	/** 
	 * 转化成Json.
	 * @param ht Hashtable
	 * @param isNoNameFormat 是否编号名称格式
	 * @return 
	 */
	public static String ToJson(Hashtable ht, boolean isNoNameFormat)
	{
		if (isNoNameFormat)
		{
			/*如果是datatable 模式. */
			DataTable dt = new DataTable();
			dt.TableName = "HT"; //此表名不能修改.
			dt.Columns.Add(new DataColumn("No", String.class));
			dt.Columns.Add(new DataColumn("Name", String.class));
			for (Object key : ht.keySet())
			{
				if (key == null || "".equals(key.toString()))
				{
					continue;
				}

				DataRow dr = dt.NewRow();
				dr.setValue("No", key);

				String v = (String)((ht.get(key) instanceof String) ? ht.get(key) : null);
				if (v == null)
				{
					v = "";
				}
				dr.setValue("Name", v);
				dt.Rows.Add(dr);
			}
			return ToJson(dt);
		}

		String strs = "{";
		for (Object key : ht.keySet())
		{
			strs += "\"" + key.toString() + "\":\"" + ht.get(key.toString()) + "\",";
		}
		strs += "\"OutEnd\":\"1\"";
		strs += "}";
		strs = TranJsonStr(strs);
		return strs;
	}
	
	/** 
	 * JSON字符串的转义
	 * @param jsonStr
	 * @return 
	 */
	private static String TranJsonStr(String jsonStr)
	{
		String strs = jsonStr;
		strs = strs.replace("\\", "\\\\");
		strs = strs.replace("\n", "\\n");
		strs = strs.replace("\b", "\\b");
		strs = strs.replace("\t", "\\t");
		strs = strs.replace("\f", "\\f");
		strs = strs.replace("/", "\\/");
		return strs;
	}
}