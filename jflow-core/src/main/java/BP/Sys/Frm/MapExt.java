package BP.Sys.Frm;

import java.util.Hashtable;

import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.EntityMyPK;
import BP.En.Map;
import BP.Sys.PercentModel;
import BP.Sys.RptTemplateAttr;
import BP.Tools.StringHelper;
import BP.Web.WebUser;

/**
 * 扩展
 */
public class MapExt extends EntityMyPK
{	    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	/**
	 * Pop参数.
	 */
	public final int getPopValFormat()
	{
		return this.GetParaInt("PopValFormat");
	}
	
	public final void setPopValFormat(int value)
	{
		this.SetPara("PopValFormat", value);
	}
	
	public final void setPopValFormatNew(PopValFormat value)
	{
		this.SetPara("PopValFormat", value.getValue());
	}
	
	/**
	 * pop 选择方式 0,多选,1=单选.
	 */
	public final int getPopValSelectModel()
	{
		return this.GetParaInt("PopValSelectModel");
	}
	
	public final void setPopValSelectModel(int value)
	{
		this.SetPara("PopValSelectModel", value);
	}
	
	public PopValSelectModel getPopValSelectModelNew()
    {
        return PopValSelectModel.forValue(this.GetParaInt("PopValSelectModel"));
    }
	public  final void setPopValSelectModelNew(PopValSelectModel value)
    {
        this.SetPara("PopValSelectModel", value.getValue());
    }
	
	/**
	 * 工作模式 0=url, 1=内置.
	 */
	public final PopValWorkModel getPopValWorkModelNew()
	{
		 return PopValWorkModel.forValue(this.GetParaInt("PopValWorkModel"));
	}
	
	public final void setPopValWorkModelNew(PopValWorkModel value)
	{
		this.SetPara("PopValWorkModel", value.getValue());
	}
	
	public int getPopValWorkModel()
	{
		 return this.GetParaInt("PopValWorkModel");
	}
	public void setPopValWorkModel(int value)
	{
		 this.SetPara("PopValWorkModel",value);
	}
	
	
	/**
	 * 开窗的列中文名称.
	 */
    public String getPopValColNames()
    {
    	return  this.getTag3();
    }
    public void setPopValColNames(String value)
    {
    	this.setTag3(value);
    }
	
	/**
	 * pop 呈现方式 0,表格,1=目录.
	 */
	public final int getPopValShowModel()
	{
		return this.GetParaInt("PopValShowModel");
	}
	
	public final void setPopValShowModel(int value)
	{
		this.SetPara("PopValShowModel", value);
	}
	
	// 属性
	public final String getExtDesc()
	{
		String dec = "";
		if (this.getExtType().equals(MapExtXmlList.ActiveDDL))
		{
			dec += "字段" + this.getAttrOfOper();
		} else if (this.getExtType().equals(MapExtXmlList.TBFullCtrl))
		{
			dec += this.getAttrOfOper();
		} else if (this.getExtType().equals(MapExtXmlList.DDLFullCtrl))
		{
			dec += "" + this.getAttrOfOper();
		} else if (this.getExtType().equals(MapExtXmlList.InputCheck))
		{
			dec += "字段：" + this.getAttrOfOper() + " 检查内容：" + this.getTag1();
		} else if (this.getExtType().equals(MapExtXmlList.PopVal))
		{
			dec += "字段：" + this.getAttrOfOper() + " Url：" + this.getTag();
		} else
		{
		}
		return dec;
	}
	
	/**
	 * 是否自适应大小
	 */
	public final boolean getIsAutoSize()
	{
		return this.GetValBooleanByKey(MapExtAttr.IsAutoSize);
	}
	
	public final void setIsAutoSize(boolean value)
	{
		this.SetValByKey(MapExtAttr.IsAutoSize, value);
	}
	 /**
	  * 查询条件
	  */
    public String getPopValSearchCond()
    {
    	return  this.getTag4();
    }
    public void setPopValSearchCond(String value)
    {
    	this.setTag4(value);
    }
        
	/**
	 * 搜索提示关键字
	 */
    public String getPopValSearchTip()
    {
    	return this.GetParaString("PopValSearchTip", "请输入关键字");
    }
    public void setPopValSearchTip(String value)
    {
    	 this.SetPara("PopValSearchTip", value);
    }
        
	/**
	 * 数据源
	 */
	public final String getDBSrc()
	{
		return this.GetValStrByKey(MapExtAttr.DBSrc);
	}
	
	public final void setDBSrc(String value)
	{
		this.SetValByKey(MapExtAttr.DBSrc, value);
	}
	
	public final String getAtPara()
	{
		return this.GetValStrByKey(MapExtAttr.AtPara);
	}
	
	public final void setAtPara(String value)
	{
		this.SetValByKey(MapExtAttr.AtPara, value);
	}
	
	public final String getExtType()
	{
		return this.GetValStrByKey(MapExtAttr.ExtType);
	}
	
	public final void setExtType(String value)
	{
		this.SetValByKey(MapExtAttr.ExtType, value);
	}
	
	public final int getDoWay()
	{
		return this.GetValIntByKey(MapExtAttr.DoWay);
	}
	
	public final void setDoWay(int value)
	{
		this.SetValByKey(MapExtAttr.DoWay, value);
	}
	
	/**
	 * 操作的attrs
	 */
	public final String getAttrOfOper()
	{
		return this.GetValStrByKey(MapExtAttr.AttrOfOper);
	}
	
	public final void setAttrOfOper(String value)
	{
		this.SetValByKey(MapExtAttr.AttrOfOper, value);
	}
	
	public final String getAttrOfOperToLowerCase()
	{
		return getAttrOfOper().toLowerCase();
	}
	
	/**
	 * 激活的attrs
	 */
	public final String getAttrsOfActive()
	{
		// return this.GetValStrByKey(MapExtAttr.AttrsOfActive).Replace("~",
		// "'");
		return this.GetValStrByKey(MapExtAttr.AttrsOfActive);
	}
	
	public final String getAttrsOfActiveToLowerCase()
	{
		return getAttrsOfActive().toLowerCase();
	}
	
	public final void setAttrsOfActive(String value)
	{
		this.SetValByKey(MapExtAttr.AttrsOfActive, value);
	}
	
	public final String getFK_MapData()
	{
		return this.GetValStrByKey(MapExtAttr.FK_MapData);
	}
	
	public final void setFK_MapData(String value)
	{
		this.SetValByKey(MapExtAttr.FK_MapData, value);
	}
	
	/**
	 * Doc
	 */
	public final String getDoc()
	{
		return this.GetValStrByKey("Doc").replace("~", "'");
	}
	
	public final void setDoc(String value)
	{
		this.SetValByKey("Doc", value);
	}
	
	public final String getTagOfSQL_autoFullTB()
	{
		if (StringHelper.isNullOrEmpty(this.getTag()))
		{
			return this.getDocOfSQLDeal();
		}
		
		String sql = this.getTag();
		sql = sql.replace("@WebUser.No", WebUser.getNo());
		sql = sql.replace("@WebUser.Name", WebUser.getName());
		sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
		sql = sql.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());
		return sql;
	}
	
	public final String getDocOfSQLDeal()
	{
		String sql = this.getDoc();
		sql = sql.replace("@WebUser.No", WebUser.getNo());
		sql = sql.replace("@WebUser.Name", WebUser.getName());
		sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
		sql = sql.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());
		return sql;
	}
	
	public final String getTag()
	{
		String s = this.GetValStrByKey("Tag").replace("~", "'");
		
		s = s.replace("\\\\", "\\");
		s = s.replace("\\\\", "\\");
		
		s = s.replace("CCFlow\\Data\\", "CCFlow\\WF\\Data\\");
		
		return s;
	}
	
	public final void setTag(String value)
	{
		this.SetValByKey("Tag", value);
	}
	
	public final String getTag1()
	{
		return this.GetValStrByKey("Tag1").replace("~", "'");
	}
	
	public final void setTag1(String value)
	{
		this.SetValByKey("Tag1", value);
	}
	
	public final String getTag2()
	{
		return this.GetValStrByKey("Tag2").replace("~", "'");
	}
	
	public final void setTag2(String value)
	{
		this.SetValByKey("Tag2", value);
	}
	
	public final String getTag3()
	{
		return this.GetValStrByKey("Tag3").replace("~", "'");
	}
	
	public final void setTag3(String value)
	{
		this.SetValByKey("Tag3", value);
	}
	
	public final String getTag4()
	{
		return this.GetValStrByKey("Tag4").replace("~", "'");
	}
	
	public final void setTag4(String value)
	{
		this.SetValByKey("Tag4", value);
	}
	
	public final int getH()
	{
		return this.GetValIntByKey(MapExtAttr.H);
	}
	
	public final void setH(int value)
	{
		this.SetValByKey(MapExtAttr.H, value);
	}
	
	public final int getW()
	{
		return this.GetValIntByKey(MapExtAttr.W);
	}
	
	public final void setW(int value)
	{
		this.SetValByKey(MapExtAttr.W, value);
	}
	/** 数据源
	 
	*/
	public final String getFK_DBSrc()
	{
		return this.GetValStrByKey(MapExtAttr.FK_DBSrc);
	}
	public final void setFK_DBSrc(String value)
	{
		this.SetValByKey(MapExtAttr.FK_DBSrc, value);
	}
	// 构造方法
	/**
	 * 扩展
	 */
	public MapExt()
	{
		
	}
	/**
	 * 转化JSON
	 * @return
	 */
    public String PopValToJson()
    {
        return BP.Tools.Json.ToJson(this.PopValToHashtable(),false);
    }
    @SuppressWarnings("unchecked")
	public Hashtable PopValToHashtable()
    {

        //创建一个ht, 然后把他转化成json返回出去。
        Hashtable ht = new Hashtable();
        
        switch (this.getPopValWorkModelNew())
        {
            case SelfUrl:
                ht.put("URL", this.getPopValUrl());
                break;
            case TableOnly:
                ht.put("EntitySQL", this.getPopValEntitySQL());
                break;
            case TablePage:
                ht.put("PopValTablePageSQL", this.getPopValTablePageSQL());
                ht.put("PopValTablePageSQLCount", this.getPopValTablePageSQLCount());
                break;
            case Group:
                ht.put("GroupSQL", this.getTag1());
                ht.put("EntitySQL", this.getPopValEntitySQL());
                break;
            case Tree:
                ht.put("TreeSQL", this.getPopValTreeSQL());
                ht.put("TreeParentNo", this.getPopValTreeParentNo());
                break;
            case TreeDouble:
                ht.put("DoubleTreeSQL", this.getPopValTreeSQL());
                ht.put("DoubleTreeParentNo", this.getPopValTreeParentNo());
                ht.put("DoubleTreeEntitySQL", this.getPopValDoubleTreeEntitySQL());
                break;
            default:
                break;
        }

        ht.put(MapExtAttr.W, this.getW());
        ht.put(MapExtAttr.H, this.getH());

        ht.put("PopValWorkModel", this.getPopValWorkModelNew()); //工作模式.
        ht.put("PopValSelectModel", this.getPopValSelectModelNew()); //单选，多选.
            
        ht.put("PopValFormat", this.getPopValFormat()); //返回值格式.
        ht.put("PopValTitle", this.getPopValTitle()); //窗口标题.
        ht.put("PopValColNames", this.getPopValColNames()); //列名 @No=编号@Name=名称@Addr=地址.
        ht.put("PopValSearchTip", this.getPopValSearchTip()); //搜索提示..

        //查询条件.
        ht.put("PopValSearchCond", this.getPopValSearchCond()); //查询条件..


        //转化为Json.
        return ht;
    }
	
    /**
     * 链接
     */
    public String getPopValUrl()
    {
            return this.getDoc();
    }
    public void setPopValUrl(String value)
    {
    	this.setDoc(value);
    }
	  /**
	   * 实体SQL
	   */
    public String getPopValEntitySQL()
    {
          return this.getTag2();
    }
    public void setPopValEntitySQL(String value)
    {
        this.setTag2(value);
    }
    /**
     * 分组SQL
     */
    public String getPopValGroupSQL()
    {
        return this.getTag1();
    }
    public void setPopValGroupSQL(String value)
    {
    	this.setTag1(value);
    }
  /**
   * 分页SQL带有关键字
   */
    public String getPopValTablePageSQL()
    {
        return this.getTag();
    }
    public void setPopValTablePageSQL(String value)
    {
       this.setTag(value);
    }
	 /**
	  * 分页SQL获取总行数
	  */
    public String getPopValTablePageSQLCount()
    {
    	return this.getTag1();
    }
    public void setPopValTablePageSQLCount(String value)
    {
        this.setTag1(value);
    }
    /**
     * 标题
     */
    public String getPopValTitle()
    {
       return this.GetParaString("PopValTitle");
    }
    public void setPopValTitle(String value)
    {
    	this.SetPara("PopValTitle", value);
    }
        
    
    public String getPopValTreeSQL()
    {
    	return this.getPopValEntitySQL();
    }
    public void setPopValTreeSQL(String value)
    {
        this.setPopValEntitySQL(value);
    }
    
  /**
   * 根目录
   */
    public String getPopValTreeParentNo()
    {
    	return this.GetParaString("PopValTreeParentNo");
    }
    public void setPopValTreeParentNo(String value)
    {
    	 this.SetPara("PopValTreeParentNo", value);
    }
    /**
     * 双实体树的实体
     */
    public String getPopValDoubleTreeEntitySQL()
    {
    	return this.getTag1();
    }
    public void setPopValDoubleTreeEntitySQL(String value)
	{
    	this.setTag1(value);
    }
    
	public MapExt(String mypk)
	{
		this.setMyPK(mypk);
		this.Retrieve();
	}
	
	
	/**
	 * EnMap
	 */
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		
		Map map = new Map("Sys_MapExt");
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnDesc("扩展");
		map.setEnType(EnType.Sys);
		map.AddMyPK();
		map.AddTBString(MapExtAttr.FK_MapData, null, "主表", true, false, 0, 30,
				20);
		map.AddTBString(MapExtAttr.ExtType, null, "类型", true, false, 0, 30, 20);
		map.AddTBInt(MapExtAttr.DoWay, 0, "执行方式", true, false);
		map.AddTBString(MapExtAttr.AttrOfOper, null, "操作的Attr", true, false, 0,
				30, 20);
		map.AddTBString(MapExtAttr.AttrsOfActive, null, "激活的字段", true, false,
				0, 900, 20);
		map.AddTBStringDoc();
		map.AddTBString(MapExtAttr.Tag, null, "Tag", true, false, 0, 2000, 20);
		map.AddTBString(MapExtAttr.Tag1, null, "Tag1", true, false, 0, 2000, 20);
		map.AddTBString(MapExtAttr.Tag2, null, "Tag2", true, false, 0, 2000, 20);
		map.AddTBString(MapExtAttr.Tag3, null, "Tag3", true, false, 0, 2000, 20);
		
		map.AddTBString(MapExtAttr.AtPara, null, "参数", true, false, 0, 2000, 20);
		map.AddTBString(MapExtAttr.DBSrc, null, "数据源", true, false, 0, 20, 20);
		
		map.AddTBInt(MapExtAttr.H, 500, "高度", false, false);
		map.AddTBInt(MapExtAttr.W, 400, "宽度", false, false);
		map.AddTBString(MapExtAttr.FK_DBSrc, null, "数据源", true, false, 0, 100, 20);
		
		// add by stone 2013-12-21 计算的优先级,用于js的计算.
		map.AddTBInt(MapExtAttr.PRI, 0, "PRI", false, false);
		
		this.set_enMap(map);
		return this.get_enMap();
	}


}