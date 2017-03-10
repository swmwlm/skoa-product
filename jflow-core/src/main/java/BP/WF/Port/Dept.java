package BP.WF.Port;

import BP.DA.Depositary;
import BP.En.AdjunctType;
import BP.En.EntityNoName;
import BP.En.Map;
import BP.En.UAC;
import BP.GPM.DeptTypes;
import BP.Port.DeptAttr;

/** 
 部门
*/
public class Dept extends EntityNoName
{
		///#region 属性
	/** 
	 父节点编号
	*/
	public final String getParentNo()
	{
		return this.GetValStrByKey(DeptAttr.ParentNo);
	}
	public final void setParentNo(String value)
	{
		this.SetValByKey(DeptAttr.ParentNo, value);
	}

		///#endregion

		///#region 构造函数
	/** 
	 部门
	*/
	public Dept()
	{
	}
	/** 
	 部门
	 
	 @param no 编号
	*/
	public Dept(String no)
	{
		super(no);
	}
		///#endregion

		///#region 重写方法
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	/** 
	 Map
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Port_Dept", "部门");

		map.Java_SetDepositaryOfEntity(Depositary.Application); //实体map的存放位置.
		map.setDepositaryOfMap(Depositary.Application); // Map 的存放位置.

		map.setAdjunctType (AdjunctType.None);

		map.AddTBStringPK(DeptAttr.No, null, "编号", true, false, 1, 30, 40);
		map.AddTBString(DeptAttr.Name, null,"名称", true, false, 0, 60, 200);
		map.AddTBString(DeptAttr.ParentNo, null, "父节点编号", true, false, 0, 30, 40);
		//map.AddDDLEntities(DeptAttr.FK_DeptType, "0", "部门类型", new DeptTypes(),true);
		  //  map.AddTBString(DeptAttr.FK_Unit, "1", "隶属单位", false, false, 0, 50, 10);

		this.set_enMap(map);
		return this.get_enMap();
	}
		///#endregion
}