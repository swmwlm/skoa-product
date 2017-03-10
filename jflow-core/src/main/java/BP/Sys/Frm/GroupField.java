package BP.Sys.Frm;

import BP.DA.DBAccess;
import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.EntityOID;
import BP.En.Map;
import BP.En.RefMethod;
import BP.En.RefMethodType;

/**
 * GroupField
 */
public class GroupField extends EntityOID
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 属性
	public boolean IsUse = false;
	
	public final String getEnName()
	{
		return this.GetValStrByKey(GroupFieldAttr.EnName);
	}
	
	public final void setEnName(String value)
	{
		this.SetValByKey(GroupFieldAttr.EnName, value);
	}
	
	/** 
	 标签
	*/
	public final String getLab()
	{
		return this.GetValStrByKey(GroupFieldAttr.Lab);
	}
	
	public final void setLab(String value)
	{
		this.SetValByKey(GroupFieldAttr.Lab, value);
	}
	
	/** 
	 顺序号
	*/
	public final int getIdx()
	{
		return this.GetValIntByKey(GroupFieldAttr.Idx);
	}
	
	public final void setIdx(int value)
	{
		this.SetValByKey(GroupFieldAttr.Idx, value);
	}
	
	/** 
	 控件类型
	*/
	public final String getCtrlType()
	{
		return this.GetValStrByKey(GroupFieldAttr.CtrlType);
	}
	public final void setCtrlType(String value)
	{
		this.SetValByKey(GroupFieldAttr.CtrlType, value);
	}
	/** 
	 控件ID
	*/
	public final String getCtrlID()
	{
		return this.GetValStrByKey(GroupFieldAttr.CtrlID);
	}
	public final void setCtrlID(String value)
	{
		this.SetValByKey(GroupFieldAttr.CtrlID, value);
	}

	// 构造方法
	/**
	 * GroupField
	 */
	public GroupField()
	{
	}
	
	public GroupField(int oid)
	{
		super(oid);
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
		Map map = new Map("Sys_GroupField");
		//map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfEntity(Depositary.Application);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnDesc("GroupField");
		map.setEnType(EnType.Sys);
		
		map.AddTBIntPKOID();
		map.AddTBString(GroupFieldAttr.Lab, null, "标签Lab", true, false, 0, 500,
				20);
		map.AddTBString(GroupFieldAttr.EnName, null, "主表", true, false, 0, 200,
				20);
		map.AddTBInt(GroupFieldAttr.Idx, 99, "顺序号Idx", true, false);
		map.AddTBString(FrmBtnAttr.GUID, null, "GUID", true, false, 0, 128, 20);
		map.AddTBString(GroupFieldAttr.CtrlType, null, "控件类型", false, false, 0, 50, 20);
		map.AddTBString(GroupFieldAttr.CtrlID, null, "控件ID", false, false, 0, 500, 20);
		map.AddTBAtParas(3000);
		
		RefMethod rm = new RefMethod();
		//rm.Title = "增加字段";
		//rm.Icon = "../WF/Img/Btn/New.gif";
		//rm.ClassMethodName = this.ToString() + ".DoAddField";
		//rm.RefMethodType = RefMethodType.LinkeWinOpen;
		//map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "删除隶属分组的字段";
		rm.Icon = "../WF/Img/Btn/Delete.gif";
		rm.Warning = "您确定要删除该分组下的所有字段吗？";
		rm.ClassMethodName = this.toString() + ".DoDelAllField";
		rm.refMethodType = RefMethodType.Func;
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}
	
	public final void DoDown()
	{
		try
		{
			this.DoOrderDown(GroupFieldAttr.EnName, this.getEnName(),
					GroupFieldAttr.Idx);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return;
	}
	
	public final void DoUp()
	{
		try
		{
			this.DoOrderUp(GroupFieldAttr.EnName, this.getEnName(),
					GroupFieldAttr.Idx);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return;
	}
	
	@Override
	protected boolean beforeInsert()
	{
		/*
		if (this.IsExit(GroupFieldAttr.EnName, this.getEnName(),
				GroupFieldAttr.Lab, this.getLab()))
		{
			throw new RuntimeException("@已经在(" + this.getEnName() + ")里存在("
					+ this.getLab() + ")的分组了。");
		}
		*/
		try
		{
			String sql = "SELECT MAX(IDX) FROM "
					+ this.getEnMap().getPhysicsTable() + " WHERE EnName='"
					+ this.getEnName() + "'";
			this.setIdx(DBAccess.RunSQLReturnValInt(sql, 0) + 1);
		} catch (java.lang.Exception e)
		{
			this.setIdx(1);
		}
		return super.beforeInsert();
	}
}