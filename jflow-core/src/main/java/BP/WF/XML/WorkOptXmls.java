package BP.WF.XML;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 工作选项s
*/
public class WorkOptXmls extends XmlEns
{
		///#region 构造
	/** 
	 工作选项s
	*/
	public WorkOptXmls()
	{
	}
		///#endregion

		///#region 重写基类属性或方法。
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new WorkOptXml();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfWebApp() + "WF/Style/Tools.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "WorkOpt";
	}
	@Override
	public Entities getRefEns()
	{
		return null;
	}
		///#endregion

}