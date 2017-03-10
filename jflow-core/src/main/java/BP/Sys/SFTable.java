package BP.Sys;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.Depositary;
import BP.DA.Log;
import BP.En.ClassFactory;
import BP.En.EnType;
import BP.En.Entities;
import BP.En.EntitiesNoName;
import BP.En.EntityNoName;
import BP.En.Map;
import BP.En.RefMethod;
import BP.En.RefMethodType;
import BP.En.UAC;
import BP.Sys.Frm.MapAttr;
import BP.Sys.Frm.MapAttrAttr;
import BP.Sys.Frm.MapAttrs;
import BP.Tools.Json;
import BP.Tools.StringHelper;

/**
 * 用户自定义表
 */
public class SFTable extends EntityNoName
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 链接到其他系统获取数据的属性
	/**
	 * 数据源
	 */
	public final String getFK_SFDBSrc()
	{
		return this.GetValStringByKey(SFTableAttr.FK_SFDBSrc);
	}
	
	public final void setFK_SFDBSrc(String value)
	{
		this.SetValByKey(SFTableAttr.FK_SFDBSrc, value);
	}
	
	/**
	 * 物理表名称
	 */
	public final String getSrcTable()
	{
		return this.GetValStringByKey(SFTableAttr.SrcTable);
	}
	
	public final void setSrcTable(String value)
	{
		this.SetValByKey(SFTableAttr.SrcTable, value);
	}
	
	/**
	 * 值/主键字段名
	 */
	public final String getColumnValue()
	{
		return this.GetValStringByKey(SFTableAttr.ColumnValue);
	}
	
	public final void setColumnValue(String value)
	{
		this.SetValByKey(SFTableAttr.ColumnValue, value);
	}
	
	/**
	 * 显示字段/显示字段名
	 */
	public final String getColumnText()
	{
		return this.GetValStringByKey(SFTableAttr.ColumnText);
	}
	
	public final void setColumnText(String value)
	{
		this.SetValByKey(SFTableAttr.ColumnText, value);
	}
	
	/**
	 * 父结点字段名
	 */
	public final String getParentValue()
	{
		return this.GetValStringByKey(SFTableAttr.ParentValue);
	}
	
	public final void setParentValue(String value)
	{
		this.SetValByKey(SFTableAttr.ParentValue, value);
	}
	
	/**
	 * 查询语句
	 */
	public final String getSelectStatement()
	{
		return this.GetValStringByKey(SFTableAttr.SelectStatement);
	}
	
	public final void setSelectStatement(String value)
	{
		this.SetValByKey(SFTableAttr.SelectStatement, value);
	}
	
	// 属性
	/**
	 * 是否是类
	 */
	public final boolean getIsClass()
	{
		if (this.getNo().contains("."))
		{
			return true;
		} else
		{
			return false;
		}
	}
	
	/**
	 * 是否是树形实体?
	 */
	/*public final boolean getIsTree()
	{
		return this.GetValBooleanByKey(SFTableAttr.IsTree);
	}
	
	public final void setIsTree(boolean value)
	{
		this.SetValByKey(SFTableAttr.IsTree, value);
	}*/
	
	/**
	 * 字典表类型 0：NoName类型 1：NoNameTree类型 2：NoName行政区划类型
	 */
	/*public final int getSFTableType()
	{
		return this.GetValIntByKey(SFTableAttr.SFTableType);
	}
	
	public final void setSFTableType(int value)
	{
		this.SetValByKey(SFTableAttr.SFTableType, value);
	}*/
	
	/**
	 * 值
	 */
	public final String getFK_Val()
	{
		return this.GetValStringByKey(SFTableAttr.FK_Val);
	}
	
	public final void setFK_Val(String value)
	{
		this.SetValByKey(SFTableAttr.FK_Val, value);
	}
	
	public final String getTableDesc()
	{
		return this.GetValStringByKey(SFTableAttr.TableDesc);
	}
	
	public final void setTableDesc(String value)
	{
		this.SetValByKey(SFTableAttr.TableDesc, value);
	}
	
	public final String getDefVal()
	{
		return this.GetValStringByKey(SFTableAttr.DefVal);
	}
	
	public final void setDefVal(String value)
	{
		this.SetValByKey(SFTableAttr.DefVal, value);
	}
	
	public final EntitiesNoName getHisEns()
	{
		if (this.getIsClass())
		{
			EntitiesNoName ens = (EntitiesNoName) BP.En.ClassFactory
					.GetEns(this.getNo());
			ens.RetrieveAll();
			return ens;
		}
		
		GENoNames ges = new GENoNames(this.getNo(), this.getName());
		ges.RetrieveAll();
		return ges;
	}
	
	// 构造方法
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		uac.IsInsert = false;
		return uac;
	}
	
	/**
	 * 用户自定义表
	 */
	public SFTable()
	{
	}
	
	public SFTable(String mypk)
	{
		this.setNo(mypk);
		try
		{
			this.Retrieve();
		} catch (RuntimeException ex)
		{
			if (this.getNo().equals("BP.Pub.NYs"))
			{
				this.setName("年月");
				// this.HisSFTableType = SFTableType.ClsLab;
				this.setFK_Val("FK_NY");
				// this.IsEdit = true;
				this.Insert();
			} else if (this.getNo().equals("BP.Pub.YFs"))
			{
				this.setName("月");
				// this.HisSFTableType = SFTableType.ClsLab;
				this.setFK_Val("FK_YF");
				// this.IsEdit = true;
				this.Insert();
			} else if (this.getNo().equals("BP.Pub.Days"))
			{
				this.setName("天");
				// this.HisSFTableType = SFTableType.ClsLab;
				this.setFK_Val("FK_Day");
				// this.IsEdit = true;
				this.Insert();
			} else if (this.getNo().equals("BP.Pub.NDs"))
			{
				this.setName("年");
				// this.HisSFTableType = SFTableType.ClsLab;
				this.setFK_Val("FK_ND");
				// this.IsEdit = true;
				this.Insert();
			} else
			{
				throw new RuntimeException(ex.getMessage());
			}
		}
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
		Map map = new Map("Sys_SFTable");
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnDesc("字典表");
		map.setEnType(EnType.Sys);
		
		map.AddTBStringPK(SFTableAttr.No, null, "表英文名称", true, false, 1, 20, 20);
		map.AddTBString(SFTableAttr.Name, null, "表中文名称", true, false, 0, 30, 20);
		map.AddTBString(SFTableAttr.FK_Val, null, "默认创建的字段名", true, false, 0,
				50, 20);
		map.AddTBString(SFTableAttr.TableDesc, null, "表描述", true, false, 0, 50,
				20);
		map.AddTBString(SFTableAttr.DefVal, null, "默认值", true, false, 0, 200,
				20);
		map.AddDDLSysEnum(SFTableAttr.SrcType, 0, "数据表类型", true, false, SFTableAttr.SrcType, "@0=外键表@1=外部表(SQL表)@2=WebService表(通过WS服务表)");


		//map.AddBoolean(SFTableAttr.IsTree, false, "是否是树实体", true, true);
		/*map.AddDDLSysEnum(SFTableAttr.SFTableType, 0, "字典表类型", true, false,
				SFTableAttr.SFTableType,
				"@0=NoName类型@1=NoNameTree类型@2=NoName行政区划类型");
		*/
		map.AddDDLSysEnum(SFTableAttr.CodeStruct, 0, "字典表类型", true, false, SFTableAttr.CodeStruct);

		//与同步相关的数据.
		map.AddTBString(SFTableAttr.CashDT, null, "上次同步的时间", false, false, 0, 200, 20);
		map.AddTBInt(SFTableAttr.CashMinute, 0, "数据缓存时间(0表示不缓存)", false, false);


		
		// 数据源.
		map.AddDDLEntities(SFTableAttr.FK_SFDBSrc, "local", "数据源",
				new BP.Sys.SFDBSrcs(), true);
		map.AddTBString(SFTableAttr.SrcTable, null, "数据源表", true, false, 0, 50,
				20);
		map.AddTBString(SFTableAttr.ColumnValue, null, "显示的值(编号列)", true,
				false, 0, 50, 20);
		map.AddTBString(SFTableAttr.ColumnText, null, "显示的文字(名称列)", true,
				false, 0, 50, 20);
		map.AddTBString(SFTableAttr.ParentValue, null, "父级值(父级列)", true, false,
				0, 50, 20);
		map.AddTBString(SFTableAttr.SelectStatement, null, "查询语句", true, false,
				0, 1000, 600, true);
		
		// 查找.
		map.AddSearchAttr(SFTableAttr.FK_SFDBSrc);
		
		RefMethod rm = new RefMethod();
		rm.Title = "编辑数据";
		rm.ClassMethodName = this.toString() + ".DoEdit";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.IsForEns = false;
		map.AddRefMethod(rm);
		
		rm = new RefMethod();
		rm.Title = "创建Table向导";
		rm.ClassMethodName = this.toString() + ".DoGuide";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.IsForEns = false;
		map.AddRefMethod(rm);
		
		rm = new RefMethod();
		rm.Title = "数据源管理";
		rm.ClassMethodName = this.toString() + ".DoMangDBSrc";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.IsForEns = false;
		map.AddRefMethod(rm);
		
		this.set_enMap(map);
		return this.get_enMap();
	}
	
	/**
	 * 数据源管理
	 * 
	 * @return
	 */
	public final String DoMangDBSrc()
	{
		return BP.WF.Glo.getCCFlowAppPath()
				+ "WF/Comm/Search.jsp?EnsName=BP.Sys.SFDBSrcs";
	}
	
	/**
	 * 创建表向导
	 * 
	 * @return
	 */
	public final String DoGuide()
	{
		return BP.WF.Glo.getCCFlowAppPath() + "WF/Comm/Sys/SFGuide.jsp";
	}
	
	/**
	 * 编辑数据
	 * 
	 * @return
	 */
	public final String DoEdit()
	{
		if (this.getIsClass())
		{
			return BP.WF.Glo.getCCFlowAppPath() + "WF/Comm/Ens.jsp?EnsName="
					+ this.getNo();
		} else
		{
			return BP.WF.Glo.getCCFlowAppPath()
					+ "WF/MapDef/SFTableEditData.jsp?RefNo=" + this.getNo();
		}
	}
	
	@Override
	protected boolean beforeDelete()
	{
		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.UIBindKey, this.getNo());
		if (attrs.size() != 0)
		{
			String err = "";
			for (Object item : attrs)
			{
				err += " @ " + ((MapAttr) item).getMyPK() + " "
						+ ((MapAttr) item).getName();
			}
			throw new RuntimeException("@如下实体字段在引用:" + err);
		}
		return super.beforeDelete();
	}
	
	/** 
	 编码表类型
	 
	*/
	public enum CodeStruct {
		/** 
		 普通的编码表
		 
		*/
		NoName,
		/** 
		 树编码表(No,Name,ParentNo)
		 
		*/
		Tree,
		/** 
		 行政机构编码表
		 
		*/
		GradeNoName;

		public int getValue() {
			return this.ordinal();
		}

		public static CodeStruct forValue(int value) {
			return values()[value];
		}
	}
	
	/** 
	 是否是树形实体?
	 
	*/
	public final boolean getIsTree() {
		if (this.getCodeStruct() == CodeStruct.NoName) {
			return false;
		}
		return true;
	}
	public final void setIsTree(boolean value)
	{
		this.SetValByKey(SFTableAttr.IsTree, value);
	}
	/** 
	 字典表类型
	 <p>0：NoName类型</p>
	 <p>1：NoNameTree类型</p>
	 <p>2：NoName行政区划类型</p>
	 
	*/
	public final CodeStruct getCodeStruct() {
		return CodeStruct.forValue(this.GetValIntByKey(SFTableAttr.CodeStruct));
	}
	public final void setCodeStruct(CodeStruct value) {
		this.SetValByKey(SFTableAttr.CodeStruct, value.getValue());
	}
	public final String getCodeStructT() {
		return this.GetValRefTextByKey(SFTableAttr.CodeStruct);
	}
	
	/** 
	 数据源类型
	 
	*/
	public final SrcType getSrcType() {
		return SrcType.forValue(this.GetValIntByKey(SFTableAttr.SrcType));
	}
	public final void setSrcType(SrcType value) {
		this.SetValByKey(SFTableAttr.SrcType, value.getValue());
	}
	
	/** 
	 数据缓存时间
	 
	*/
	public final String getCashDT() {
		return this.GetValStringByKey(SFTableAttr.CashDT);
	}
	public final void setCashDT(String value) {
		this.SetValByKey(SFTableAttr.CashDT, value);
	}
	/** 
	 同步间隔
	 
	*/
	public final int getCashMinute() {
		return this.GetValIntByKey(SFTableAttr.CashMinute);
	}
	public final void setCashMinute(int value) {
		this.SetValByKey(SFTableAttr.CashMinute, value);
	}

	/** 
	 *获得该数据源的数据
	 * @return 
	 */
	public final DataTable GenerData()
	{
		String sql = "";
		if (this.getSrcType() == SrcType.CreateTable)
		{
			sql = "SELECT * FROM " + getSrcTable();
			return this.RunSQLReturnTable(sql);
		}

		if (getSrcType() == SrcType.TableOrView)
		{
			sql = "SELECT * FROM " + getSrcTable();
			return this.RunSQLReturnTable(sql);
		}

		throw new RuntimeException("@没有判断的数据.");
	}
	
	/** 
	 * 获得外部数据表
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public final DataTable getGenerHisDataTable()
	{
		//创建数据源.
		SFDBSrc src = new SFDBSrc(this.getFK_SFDBSrc());

		//#region 自己定义的表.
		if (this.getSrcType() == SrcType.BPClass)
		{
			Entities ens = ClassFactory.GetEns(this.getNo());
			return ens.RetrieveAllToTable();
		}
		//#endregion
		
		//#region  WebServices
		// this.SrcType == Sys.SrcType.WebServices，by liuxc 
		//暂只考虑No,Name结构的数据源，2015.10.04，added by liuxc
		if (this.getSrcType() == SrcType.WebServices)
		{
			String[] td = this.getTableDesc().split("[,]", -1); //接口名称,返回类型
			String[] ps = ((this.getSelectStatement() != null) ? this.getSelectStatement() : "").split("[&]", -1);
			ArrayList args = new ArrayList();
			String[] pa = null;

			for (String p : ps)
			{
				if (StringHelper.isNullOrWhiteSpace(p))
				{
					continue;
				}

				pa = p.split("[=]", -1);
				if (pa.length != 2)
				{
					continue;
				}

					//此处要SL中显示表单时，会有问题
				try
				{
					if (pa[1].contains("@WebUser.No"))
					{
						pa[1] = pa[1].replace("@WebUser.No", BP.Web.WebUser.getNo());
					}
					if (pa[1].contains("@WebUser.Name"))
					{
						pa[1] = pa[1].replace("@WebUser.Name", BP.Web.WebUser.getName());
					}
					if (pa[1].contains("@WebUser.FK_Dept"))
					{
						pa[1] = pa[1].replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
					}
					if (pa[1].contains("@WebUser.FK_DeptName"))
					{
						pa[1] = pa[1].replace("@WebUser.FK_DeptName", BP.Web.WebUser.getFK_DeptName());
					}
				}
				catch (java.lang.Exception e)
				{
				}

				if (pa[1].contains("@WorkID"))
				{
					pa[1] = pa[1].replace("@WorkID", (BP.Sys.Glo.getRequest().getParameter("WorkID") != null) ? BP.Sys.Glo.getRequest().getParameter("WorkID") : "");
				}
				if (pa[1].contains("@NodeID"))
				{
					pa[1] = pa[1].replace("@NodeID", (BP.Sys.Glo.getRequest().getParameter("NodeID") != null) ? BP.Sys.Glo.getRequest().getParameter("NodeID") : "");
				}
				if (pa[1].contains("@FK_Node"))
				{
					pa[1] = pa[1].replace("@FK_Node", (BP.Sys.Glo.getRequest().getParameter("FK_Node") != null) ? BP.Sys.Glo.getRequest().getParameter("FK_Node") : "");
				}
				if (pa[1].contains("@FK_Flow"))
				{
					pa[1] = pa[1].replace("@FK_Flow", (BP.Sys.Glo.getRequest().getParameter("FK_Flow") != null) ? BP.Sys.Glo.getRequest().getParameter("FK_Flow") : "");
				}
				if (pa[1].contains("@FID"))
				{
					pa[1] = pa[1].replace("@FID", (BP.Sys.Glo.getRequest().getParameter("FID") != null) ? BP.Sys.Glo.getRequest().getParameter("FID") : "");
				}

				args.add(pa[1]);
			}

			Object result = InvokeWebService(src.getIP(), td[0], args.toArray(new Object[0]));
			
			if("DataSet".equals(td[1])){
				return result == null ? new DataTable() : ((DataSet)((result instanceof DataSet) ? result : null)).getTables().get(0);
			}else if("DataTable".equals(td[1]))
			{
				return (DataTable)((result instanceof DataTable) ? result : null);
			}else if("Json".equals(td[1]))
			{
				String json = Json.ToJson(((result instanceof String) ? result.toString() : null));
				/*
				if (!jdata.IsArray)
				{
					throw new RuntimeException("@返回的JSON格式字符串“" + ((result != null) ? result : "") + "”不正确");
				}*/

				DataTable dt = new DataTable();
				dt.Columns.Add("No", "");
				dt.Columns.Add("Name", "");
				dt = Json.ToDataTable(json);
				/*
				for (int i = 0; i < jdata.length; i++)
				{
					dt.Rows.Add(jdata["No"].toString(), jdata[i]["Name"].toString());
				}
				 */
				return dt;
			}else if("Xml".equals(td[1]))
			{
				if (result == null || StringHelper.isNullOrWhiteSpace(result.toString()))
				{
					throw new RuntimeException("@返回的XML格式字符串不正确。");
				}
				/*
				XmlDocument xml = new XmlDocument();
				xml.LoadXml((String)((result instanceof String) ? result : null));

				XmlNode root = null;
				if (xml.ChildNodes.size() < 2)
				{
					root = xml.ChildNodes[0];
				}
				else
				{
					root = xml.ChildNodes[1];
				}

				dt = new DataTable();
				dt.Columns.Add("No", String.class);
				dt.Columns.Add("Name", String.class);

				for (XmlNode node : root.ChildNodes)
				{
					dt.Rows.Add(node.SelectSingleNode("No").InnerText, node.SelectSingleNode("Name").InnerText);
				}

				return dt;*/
				
				DataTable dt = new DataTable();
				dt.Columns.Add("No", String.class);
				dt.Columns.Add("Name", String.class);
				
				try {
					DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					
					Document dc = builder.parse(new InputSource(((result instanceof String) ? result.toString() : "")));
					NodeList books  = dc.getChildNodes();
					
					for(int i=0;i<books.getLength();i++)
					{
						Node book = books.item(i);
						dt.Rows.Add(book.getNodeName(), book.getFirstChild().getNodeValue());
					}
				} catch (Exception e) {
					Log.DebugWriteError("getGenerHisDataTable() xml格式读取错误:"+e.getMessage());
				}
				return dt;
			}else
			{
				throw new RuntimeException("@不支持的返回类型" + td[1]);
			}
		}
		//#endregion

		//#region 如果是一个SQL.
		if (this.getSrcType() == SrcType.SQL)
		{
			String runObj = this.getSelectStatement();
			runObj = runObj.replace("~", "'");
			if (runObj.contains("@WebUser.No"))
			{
				runObj = runObj.replace("@WebUser.No", BP.Web.WebUser.getNo());
			}

			if (runObj.contains("@WebUser.Name"))
			{
				runObj = runObj.replace("@WebUser.Name", BP.Web.WebUser.getName());
			}

			if (runObj.contains("@WebUser.FK_Dept"))
			{
				runObj = runObj.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
			}
			return src.RunSQLReturnTable(runObj);
		}
		//#endregion 如果是一个SQL.

		//#region 如果是一个外键表.
		if (this.getSrcType() == SrcType.TableOrView)
		{
			String sql = "SELECT No, Name FROM " + this.getNo();
			return src.RunSQLReturnTable(sql);
		}
		//#endregion 如果是一个SQL.

		//#region 自己定义的表.
		if (this.getSrcType() == SrcType.CreateTable)
		{
			String sql = "SELECT No, Name FROM " + this.getNo();
			return src.RunSQLReturnTable(sql);
		}
		///#endregion
		
		throw new RuntimeException("@没有判断的数据类型." + this.getSrcType() + " - " + this.getSrcTypeText());
	}
	
	/** 
	 * 数据源类型名称
	 */
	public final String getSrcTypeText()
	{
		switch (this.getSrcType())
		{
			case TableOrView:
				if (this.getIsClass())
				{
					return "<img src='/WF/Img/Class.png' width='16px' broder='0' />实体类";
				}
				else
				{
					return "<img src='/WF/Img/Table.gif' width='16px' broder='0' />表/视图";
				}
			case SQL:
				return "<img src='/WF/Img/SQL.png' width='16px' broder='0' />SQL表达式";
			case WebServices:
				return "<img src='/WF/Img/WebServices.gif' width='16px' broder='0' />WebServices";
			default:
				return "";
		}
	}
	
	
	/** 
	 * 实例化 WebServices
	 * @param url WebServices地址
	 * @param methodname 调用的方法
	 * @param args 把webservices里需要的参数按顺序放到这个object[]里
	 */
	public final Object InvokeWebService(String url, String methodname, Object[] args)
	{
		return args;
		/*
		//这里的namespace是需引用的webservices的命名空间，在这里是写死的，大家可以加一个参数从外面传进来。
		String namespace = "BP.RefServices";
		try
		{
			if (url.endsWith(".asmx"))
			{
				url += "?wsdl";
			}
			else if (url.endsWith(".svc"))
			{
				url += "?singleWsdl";
			}

			//获取WSDL
			WebClient wc = new WebClient();
			Stream stream = wc.OpenRead(url);
			ServiceDescription sd = ServiceDescription.Read(stream);
			String classname = sd.Services[0].Name;
			ServiceDescriptionImporter sdi = new ServiceDescriptionImporter();
			sdi.AddServiceDescription(sd, "", "");
			CodeNamespace cn = new CodeNamespace(namespace);

			//生成客户端代理类代码
			CodeCompileUnit ccu = new CodeCompileUnit();
			ccu.Namespaces.Add(cn);
			sdi.Import(cn, ccu);
			CSharpCodeProvider csc = new CSharpCodeProvider();
			ICodeCompiler icc = csc.CreateCompiler();

			//设定编译参数
			CompilerParameters cplist = new CompilerParameters();
			cplist.GenerateExecutable = false;
			cplist.GenerateInMemory = true;
			cplist.ReferencedAssemblies.Add("System.dll");
			cplist.ReferencedAssemblies.Add("System.XML.dll");
			cplist.ReferencedAssemblies.Add("System.Web.Services.dll");
			cplist.ReferencedAssemblies.Add("System.Data.dll");

			//编译代理类
			CompilerResults cr = icc.CompileAssemblyFromDom(cplist, ccu);
			if (true == cr.Errors.HasErrors)
			{
				StringBuilder sb = new StringBuilder();
				for (System.CodeDom.Compiler.CompilerError ce : cr.Errors)
				{
					sb.append(ce.toString());
					sb.append(System.lineSeparator());
				}
				throw new RuntimeException(sb.toString());
			}

			//生成代理实例，并调用方法
			System.Reflection.Assembly assembly = cr.CompiledAssembly;
			java.lang.Class t = assembly.GetType(namespace + "." + classname, true, true);
			Object obj = Activator.CreateInstance(t);
			java.lang.reflect.Method mi = t.getMethod(methodname);

			return mi.Invoke(obj, args);
		}
		catch (java.lang.Exception e)
		{
			return null;
		}
		*/
	}

	
}