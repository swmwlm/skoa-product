package BP.WF;

import java.util.*;
import BP.DA.*;
import BP.WF.*;
import BP.Port.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.Template.*;

/** 
 流程状态(简)
*/
public enum WFSta
{
	/** 
	 运行中
	*/
	Runing(0),
	/** 
	 已完成
	*/
	Complete(1),
	/** 
	 其他
	*/
	Etc(2);

	private int intValue;
	private static java.util.HashMap<Integer, WFSta> mappings;
	private static java.util.HashMap<Integer, WFSta> getMappings()
	{
		if (mappings == null)
		{
			synchronized (WFSta.class)
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, WFSta>();
				}
			}
		}
		return mappings;
	}

	private WFSta(int value)
	{
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue()
	{
		return intValue;
	}

	public static WFSta forValue(int value)
	{
		return getMappings().get(value);
	}
}