package BP.WF.DTS;

import java.io.File;

import BP.DA.DBAccess;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.En.Method;
import BP.Sys.SFTable;
import BP.Sys.SFTables;
import BP.Sys.Frm.MapData;
import BP.Sys.Frm.MapDatas;
import BP.WF.Flow;
import BP.WF.Flows;
import BP.WF.Template.FlowSort;
import BP.WF.Template.SysFormTree;

/**
 * Method 的摘要说明
 */
public class OneKeyBackCCFlow extends Method
{
	/**
	 * 不带有参数的方法
	 */
	public OneKeyBackCCFlow()
	{
		this.Title = "一键备份流程与表单。";
		this.Help = "把流程、表单、组织结构数据都生成xml文档备份到C:\\CCFlowTemplete下面。";
	}
	
	/**
	 * 设置执行变量
	 * 
	 * @return
	 */
	@Override
	public void Init()
	{
		// this.Warning = "您确定要执行吗？";
		// HisAttrs.AddTBString("P1", null, "原密码", true, false, 0, 10, 10);
		// HisAttrs.AddTBString("P2", null, "新密码", true, false, 0, 10, 10);
		// HisAttrs.AddTBString("P3", null, "确认", true, false, 0, 10, 10);
	}
	
	/**
	 * 当前的操纵员是否可以执行这个方法
	 */
	@Override
	public boolean getIsCanDo()
	{
		return true;
	}
	
	/**
	 * 执行
	 * 
	 * @return 返回执行结果
	 */
	@Override
	public Object Do()
	{
		String path = "C:\\CCFlowTemplete"
				+ DataType
						.dateToStr(new java.util.Date(), "yy年MM月dd日HH时mm分ss秒");
		File file = new File(path);
		if (!file.exists())
		{
			file.mkdirs();
		}
		
		// 1.备份流程类别信息
		DataSet dsFlows = new DataSet();
		// WF_FlowSort
		DataTable dt = DBAccess.RunSQLReturnTable("SELECT * FROM WF_FlowSort");
		dt.TableName = "WF_FlowSort";
		dsFlows.Tables.add(dt);
		dsFlows.WriteXml(path + "/FlowTables.xml");
		
		// 备份流程类别信息.
		
		// 2.备份组织结构.
		DataSet dsPort = new DataSet();
		// emps
		dt = DBAccess.RunSQLReturnTable("SELECT * FROM Port_Emp");
		dt.TableName = "Port_Emp";
		dsPort.Tables.add(dt);
		
		// Port_Dept
		dt = DBAccess.RunSQLReturnTable("SELECT * FROM Port_Dept");
		dt.TableName = "Port_Dept";
		dsPort.Tables.add(dt);
		
		// Port_Station
		dt = DBAccess.RunSQLReturnTable("SELECT * FROM Port_Station");
		dt.TableName = "Port_Station";
		dsPort.Tables.add(dt);
		
		// Port_EmpStation
		dt = DBAccess.RunSQLReturnTable("SELECT * FROM Port_EmpStation");
		dt.TableName = "Port_EmpStation";
		dsPort.Tables.add(dt);
		
		// Port_EmpDept
		dt = DBAccess.RunSQLReturnTable("SELECT * FROM Port_EmpDept");
		dt.TableName = "Port_EmpDept";
		dsPort.Tables.add(dt);
		
		dsPort.WriteXml(path + "/PortTables.xml");
		
		// 备份表单相关数据.
		
		// 3.备份系统数据
		DataSet dsSysTables = new DataSet();
		
		// Sys_EnumMain
		dt = DBAccess.RunSQLReturnTable("SELECT * FROM Sys_EnumMain");
		dt.TableName = "Sys_EnumMain";
		dsSysTables.Tables.add(dt);
		
		// Sys_Enum
		dt = DBAccess.RunSQLReturnTable("SELECT * FROM Sys_Enum");
		dt.TableName = "Sys_Enum";
		dsSysTables.Tables.add(dt);
		
		// Sys_FormTree
		dt = DBAccess.RunSQLReturnTable("SELECT * FROM Sys_FormTree");
		dt.TableName = "Sys_FormTree";
		dsSysTables.Tables.add(dt);
		dsSysTables.WriteXml(path + "/SysTables.xml");
		
		// 备份系统数据.
		
		// 4.备份表单相关数据.
		String pathOfTables = path + "/SFTables";
		
		file = new File(pathOfTables);
		file.mkdirs();
		SFTables tabs = new SFTables();
		tabs.RetrieveAll();
		for (SFTable item : SFTables.convertSFTables(tabs))
		{
			if (item.getNo().contains("."))
			{
				continue;
			}
			
			String sql = "SELECT * FROM " + item.getNo() + " ";
			DataSet ds = new DataSet();
			ds.Tables.add(BP.DA.DBAccess.RunSQLReturnTable(sql));
			ds.WriteXml(pathOfTables + "/" + item.getNo() + ".xml");
		}
		
		// 备份表单相关数据.
		
		// 5.备份流程.
		Flows fls = new Flows();
		fls.RetrieveAllFromDBSource();
		for (Flow fl : fls.ToJavaList())
		{
			FlowSort fs = new FlowSort(fl.getFK_FlowSort());
			String pathDir = path + "/Flow/" + fs.getNo() + "."
					+ fs.getName();
			file = new File(pathDir);
			if (!file.exists())
			{
				file.mkdirs();
			}
			
			fl.DoExpFlowXmlTemplete(pathDir);
		}
		
		// 备份流程.
		
		// 6.备份表单.
		MapDatas mds = new MapDatas();
		mds.RetrieveAllFromDBSource();
		for (MapData md : MapDatas.convertMapDatas(mds))
		{
			if (md.getFK_FrmSort().length() < 2)
			{
				continue;
			}
			
			SysFormTree fs = new SysFormTree(md.getFK_FormTree());
			String pathDir = path + "/Form/" + fs.getNo() + "."
					+ fs.getName();
			file = new File(pathDir);
			if (!file.exists())
			{
				file.exists();
			}
			DataSet ds = md.GenerHisDataSet();
			ds.WriteXml(pathDir + "/" + md.getName() + ".xml");
		}
		
		// 备份表单.
		
		return "执行成功,存放路径:" + path;
	}
}