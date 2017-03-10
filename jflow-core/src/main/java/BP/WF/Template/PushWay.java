package BP.WF.Template;

import java.util.*;
import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.*;
import BP.WF.*;

/** 
 推送的方式
*/
public enum PushWay
{
	/** 
	 当前节点的接受人
	*/
	CurrentWorkers,
	/** 
	 指定节点的工作人员
	*/
	NodeWorker,
	/** 
	 指定的工作人员s
	*/
	SpecEmps,
	/** 
	 指定的工作岗位s
	*/
	SpecStations,
	/** 
	 指定的部门人员
	*/
	SpecDepts,
	/** 
	 指定的SQL
	*/
	SpecSQL,
	/** 
	 按照系统指定的字段
	*/
	ByParas;

	public int getValue()
	{
		return this.ordinal();
	}

	public static PushWay forValue(int value)
	{
		return values()[value];
	}
}