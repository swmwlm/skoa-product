package BP.WF.Data;

import java.io.IOException;
import java.util.*;

import cn.jflow.common.util.ContextHolderUtils;
import BP.DA.*;
import BP.WF.*;
import BP.WF.Glo;
import BP.WF.Template.FlowSheet;
import BP.Port.*;
import BP.Sys.*;
import BP.En.*;
import BP.En.Map;

/** 
 流程监控
*/
public class Monitor extends Entity
{
		///#region 基本属性
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.Readonly();
		return uac;
	}
	/** 
	 主键
	*/
	@Override
	public String getPK()
	{
		return MonitorAttr.WorkID;
	}
	/** 
	 工作流程编号
	*/
	public final String getFK_Flow()
	{
		return this.GetValStrByKey(MonitorAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		SetValByKey(MonitorAttr.FK_Flow,value);
	}
	/** 
	 BillNo
	*/
	public final String getBillNo()
	{
		return this.GetValStrByKey(MonitorAttr.BillNo);
	}
	public final void setBillNo(String value)
	{
		SetValByKey(MonitorAttr.BillNo, value);
	}
	/** 
	 流程名称
	*/
	public final String getFlowName()
	{
		return this.GetValStrByKey(MonitorAttr.FlowName);
	}
	public final void setFlowName(String value)
	{
		SetValByKey(MonitorAttr.FlowName, value);
	}
	/** 
	 优先级
	*/
	public final int getPRI()
	{
		return this.GetValIntByKey(MonitorAttr.PRI);
	}
	public final void setPRI(int value)
	{
		SetValByKey(MonitorAttr.PRI, value);
	}
	/** 
	 待办人员数量
	*/
	public final int getTodoEmpsNum()
	{
		return this.GetValIntByKey(MonitorAttr.TodoEmpsNum);
	}
	public final void setTodoEmpsNum(int value)
	{
		SetValByKey(MonitorAttr.TodoEmpsNum, value);
	}
	/** 
	 待办人员列表
	*/
	public final String getTodoEmps()
	{
		return this.GetValStrByKey(MonitorAttr.TodoEmps);
	}
	public final void setTodoEmps(String value)
	{
		SetValByKey(MonitorAttr.TodoEmps, value);
	}
	/** 
	 参与人
	*/
	public final String getEmps()
	{
		return this.GetValStrByKey(MonitorAttr.Emps);
	}
	public final void setEmps(String value)
	{
		SetValByKey(MonitorAttr.Emps, value);
	}
	/** 
	 状态
	*/
	public final TaskSta getTaskSta()
	{
		return TaskSta.forValue(this.GetValIntByKey(MonitorAttr.TaskSta));
	}
	public final void setTaskSta(TaskSta value)
	{
		SetValByKey(MonitorAttr.TaskSta, value.getValue());
	}
	/** 
	 类别编号
	*/
	public final String getFK_Emp()
	{
		return this.GetValStrByKey(MonitorAttr.FK_Emp);
	}
	public final void setFK_Emp(String value)
	{
		SetValByKey(MonitorAttr.FK_Emp, value);
	}
	/** 
	 部门编号
	*/
	public final String getFK_Dept()
	{
		return this.GetValStrByKey(MonitorAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	{
		SetValByKey(MonitorAttr.FK_Dept,value);
	}
	/** 
	 标题
	*/
	public final String getTitle()
	{
		return this.GetValStrByKey(MonitorAttr.Title);
	}
	public final void setTitle(String value)
	{
		SetValByKey(MonitorAttr.Title,value);
	}
	/** 
	 客户编号
	*/
	public final String getGuestNo()
	{
		return this.GetValStrByKey(MonitorAttr.GuestNo);
	}
	public final void setGuestNo(String value)
	{
		SetValByKey(MonitorAttr.GuestNo, value);
	}
	/** 
	 客户名称
	*/
	public final String getGuestName()
	{
		return this.GetValStrByKey(MonitorAttr.GuestName);
	}
	public final void setGuestName(String value)
	{
		SetValByKey(MonitorAttr.GuestName, value);
	}
	/** 
	 产生时间
	*/
	public final String getRDT()
	{
		return this.GetValStrByKey(MonitorAttr.RDT);
	}
	public final void setRDT(String value)
	{
		SetValByKey(MonitorAttr.RDT,value);
	}
	/** 
	 节点应完成时间
	*/
	public final String getSDTOfNode()
	{
		return this.GetValStrByKey(MonitorAttr.SDTOfNode);
	}
	public final void setSDTOfNode(String value)
	{
		SetValByKey(MonitorAttr.SDTOfNode, value);
	}
	/** 
	 流程应完成时间
	*/
	public final String getSDTOfFlow()
	{
		return this.GetValStrByKey(MonitorAttr.SDTOfFlow);
	}
	public final void setSDTOfFlow(String value)
	{
		SetValByKey(MonitorAttr.SDTOfFlow, value);
	}
	/** 
	 流程ID
	*/
	public final long getWorkID()
	{
		return this.GetValInt64ByKey(MonitorAttr.WorkID);
	}
	public final void setWorkID(long value)
	{
		SetValByKey(MonitorAttr.WorkID,value);
	}
	/** 
	 主线程ID
	*/
	public final long getFID()
	{
		return this.GetValInt64ByKey(MonitorAttr.FID);
	}
	public final void setFID(long value)
	{
		SetValByKey(MonitorAttr.FID, value);
	}
	/** 
	 父节点流程编号.
	*/
	public final long getPWorkID()
	{
		return this.GetValInt64ByKey(MonitorAttr.PWorkID);
	}
	public final void setPWorkID(long value)
	{
		SetValByKey(MonitorAttr.PWorkID, value);
	}
	/** 
	 父流程调用的节点
	*/
	public final int getPNodeID()
	{
		return this.GetValIntByKey(MonitorAttr.PNodeID);
	}
	public final void setPNodeID(int value)
	{
		SetValByKey(MonitorAttr.PNodeID, value);
	}
	/** 
	 PFlowNo
	*/
	public final String getPFlowNo()
	{
		return this.GetValStrByKey(MonitorAttr.PFlowNo);
	}
	public final void setPFlowNo(String value)
	{
		SetValByKey(MonitorAttr.PFlowNo, value);
	}
	/** 
	 吊起子流程的人员
	*/
	public final String getPEmp()
	{
		return this.GetValStrByKey(MonitorAttr.PEmp);
	}
	public final void setPEmp(String value)
	{
		SetValByKey(MonitorAttr.PEmp, value);
	}
	/** 
	 发起人
	*/
	public final String getStarter()
	{
		return this.GetValStrByKey(MonitorAttr.Starter);
	}
	public final void setStarter(String value)
	{
		SetValByKey(MonitorAttr.Starter, value);
	}
	/** 
	 发起人名称
	*/
	public final String getStarterName()
	{
		return this.GetValStrByKey(MonitorAttr.StarterName);
	}
	public final void setStarterName(String value)
	{
		this.SetValByKey(MonitorAttr.StarterName, value);
	}
	/** 
	 发起人部门名称
	*/
	public final String getDeptName()
	{
		return this.GetValStrByKey(MonitorAttr.DeptName);
	}
	public final void setDeptName(String value)
	{
		this.SetValByKey(MonitorAttr.DeptName, value);
	}
	/** 
	 当前节点名称
	*/
	public final String getNodeName()
	{
		return this.GetValStrByKey(MonitorAttr.NodeName);
	}
	public final void setNodeName(String value)
	{
		this.SetValByKey(MonitorAttr.NodeName, value);
	}
	/** 
	 当前工作到的节点
	*/
	public final int getFK_Node()
	{
		return this.GetValIntByKey(MonitorAttr.FK_Node);
	}
	public final void setFK_Node(int value)
	{
		SetValByKey(MonitorAttr.FK_Node, value);
	}
	/** 
	 工作流程状态
	*/
	public final WFState getWFState()
	{
		return WFState.forValue(this.GetValIntByKey(MonitorAttr.WFState));
	}
	public final void setWFState(WFState value)
	{
		if (value == WFState.Complete)
		{
			SetValByKey(MonitorAttr.WFSta, getWFSta().Complete.getValue());
		}
		else if (value == WFState.Delete)
		{
			SetValByKey(MonitorAttr.WFSta, getWFSta().Etc.getValue());
		}
		else
		{
			SetValByKey(MonitorAttr.WFSta, getWFSta().Runing.getValue());
		}

		SetValByKey(MonitorAttr.WFState, value.getValue());
	}
	/** 
	 状态(简单)
	*/
	public final WFSta getWFSta()
	{
		return WFSta.forValue(this.GetValIntByKey(MonitorAttr.WFSta));
	}
	public final void setWFSta(WFSta value)
	{
		SetValByKey(MonitorAttr.WFSta, value.getValue());
	}
	public final String getWFStateText()
	{
		BP.WF.WFState ws = (WFState)this.getWFState();
		switch (ws)
		{
			case Complete:
				return "已完成";
			case Runing:
				return "在运行";
			case HungUp:
				return "挂起";
			case Askfor:
				return "加签";
			default:
				return "未判断";
		}
	}
	/** 
	 GUID
	*/
	public final String getGUID()
	{
		return this.GetValStrByKey(MonitorAttr.GUID);
	}
	public final void setGUID(String value)
	{
		SetValByKey(MonitorAttr.GUID, value);
	}
		///#endregion

		///#region 参数属性.
		///#endregion 参数属性.

		///#region 构造函数
	/** 
	 产生的工作流程
	*/
	public Monitor()
	{
	}
	public Monitor(long workId)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(MonitorAttr.WorkID, workId);
		if (qo.DoQuery() == 0)
		{
			throw new RuntimeException("工作 Monitor [" + workId + "]不存在。");
		}
	}
	/** 
	 执行修复
	*/
	public final void DoRepair()
	{
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_EmpWorks", "流程监控");
		map.Java_SetEnType(EnType.View);

		map.AddTBIntPK(MonitorAttr.WorkID, 0, "工作ID", true, true);
		map.AddTBInt(MonitorAttr.FID, 0, "FID", false, false);
		map.AddTBString(MonitorAttr.Title, null, "流程标题", true, false, 0, 300, 10,true);
		map.AddTBString(MonitorAttr.FK_Emp, null, "当前处理人员", true, false, 0, 30, 10);
		map.AddDDLEntities(MonitorAttr.FK_Flow, null, "流程", new Flows(), false);
		map.AddDDLEntities(MonitorAttr.FK_Dept, null, "发起部门", new BP.Port.Depts(), false);
		map.AddTBString(MonitorAttr.Starter, null, "发起人编号", true, false, 0, 30, 10);
		map.AddTBString(MonitorAttr.StarterName, null, "名称", true, false, 0, 30, 10);
		map.AddTBString(MonitorAttr.NodeName, null, "停留节点", true, false, 0, 100, 10);
		map.AddTBString(MonitorAttr.TodoEmps, null, "处理人", true, false, 0, 100, 10);

		map.AddTBStringDoc(MonitorAttr.FlowNote, null, "备注", true, false,true);
		map.AddTBMyNum();
		map.AddTBInt(MonitorAttr.FK_Node, 0, "FK_Node", false, false);
		map.AddTBString(MonitorAttr.WorkerDept, null, "工作人员部门编号", false, false, 0, 30, 10);

			//查询条件.
		map.AddSearchAttr(MonitorAttr.FK_Dept);
		map.AddSearchAttr(MonitorAttr.FK_Flow);

			////增加隐藏的查询条件.
			//AttrOfSearch search = new AttrOfSearch(MonitorAttr.WorkerDept, "部门",
			//    MonitorAttr.WorkerDept, "=", BP.Web.WebUser.FK_Dept, 0, true);
			//map.getAttrsOfSearch().Add(search);

		RefMethod rm = new RefMethod();
		rm.Title = "流程轨迹";
		rm.ClassMethodName = this.toString() + ".DoTrack";
		rm.Icon = SystemConfig.getCCFlowWebPath() + "WF/Img/FileType/doc.gif";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Icon = SystemConfig.getCCFlowWebPath() + "WF/Img/Btn/CC.gif";
		rm.Title = "移交";
		rm.Warning = "您确定要进行移交操作吗？";
		rm.ClassMethodName = this.toString() + ".DoShift";
		rm.getHisAttrs().AddDDLEntities("ToEmp", null, "移交给:", new BP.WF.Data.MyDeptEmps(),true);
		rm.getHisAttrs().AddTBString("Note", null, "移交原因", true, false, 0, 300, 100);
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Icon = SystemConfig.getCCFlowWebPath() + "WF/Img/Btn/Delete.gif";
		rm.Title = "删除";
		rm.Warning = "您确定要删除该流程吗？";
		rm.ClassMethodName = this.toString() + ".DoDelete";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Icon = Glo.getCCFlowAppPath() + "WF/Img/Btn/Back.png";

		rm.Title = "回滚";
		rm.IsForEns = false;
		rm.ClassMethodName = this.toString() + ".DoComeBack";
		rm.getHisAttrs().AddTBInt("NodeID", 0, "回滚到节点", true, false);
		rm.getHisAttrs().AddTBString("Note", null, "回滚原因", true, false, 0, 300, 100);
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}
		///#endregion

		///#region 执行功能.
	public final String DoTrack()
	{
		try {
			PubClass.WinOpen(ContextHolderUtils.getResponse(),SystemConfig.getCCFlowWebPath() + "WF/WFRpt.jsp?WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow(), 900, 800);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/** 
	 执行移交
	 
	 @param ToEmp
	 @param Note
	 @return 
	*/
	public final String DoShift(String ToEmp, String Note)
	{
		try {
			if (BP.WF.Dev2Interface.Flow_IsCanViewTruck(this.getFK_Flow(), this.getWorkID(), this.getFID()) == false)
			{
				return "您没有操作该流程数据的权限.";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try
		{
			BP.WF.Dev2Interface.Node_Shift(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID(), ToEmp, Note);
			return "移交成功";
		}
		catch (RuntimeException ex)
		{
			return "移交失败@" + ex.getMessage();
		}
	}
	/** 
	 执行删除
	 
	 @return 
	*/
	public final String DoDelete()
	{
		try {
			if (BP.WF.Dev2Interface.Flow_IsCanViewTruck(this.getFK_Flow(), this.getWorkID(), this.getFID()) == false)
			{
				return "您没有操作该流程数据的权限.";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try
		{
			BP.WF.Dev2Interface.Flow_DoDeleteFlowByReal(this.getFK_Flow(), this.getWorkID(), true);
			return "删除成功";
		}
		catch (RuntimeException ex)
		{
			return "删除失败@" + ex.getMessage();
		}
	}
	public final String DoSkip()
	{
		try {
			PubClass.WinOpen(ContextHolderUtils.getResponse(),SystemConfig.getCCFlowWebPath() + "WF/Admin/FlowDB/FlowSkip.aspx?WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node(), 900, 800);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/** 
	 回滚
	 
	 @param nodeid 节点ID
	 @param note 回滚原因
	 @return 回滚的结果
	*/
	public final String DoComeBack(int nodeid, String note)
	{
		BP.WF.Template.FlowSheet fl = new FlowSheet(this.getFK_Flow());
		return fl.DoRebackFlowData(this.getWorkID(), nodeid, note);
	}
		///#endregion
}