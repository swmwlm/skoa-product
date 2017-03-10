// /** 
//	 写入日志
//	 
// */
//	public final void WriteLogApp()
//	{
//		// 写入一条消息.
//		BP.Sys.Glo.WriteLineInfo("这是一条消息. ");
//
//		// 写入一条警告.
//		BP.Sys.Glo.WriteLineWarning("这是一条警告. ");
//
//		// 写入一条异常或者错误.
//		BP.Sys.Glo.WriteLineError("这是一条错误. "); // 以上这些日志写入了 \DataUser\Log\*.*
//
//		//写入用户日志
//		BP.Sys.Glo.WriteUserLog("Login", "stone", "系统登录");
//		BP.Sys.Glo.WriteUserLog("Login", "stone", "系统登录", "192.168.1.100"); // 以上用户日志写入了 Sys_UserLog 表里.)
//	}
//	/** 
//	 全局的基本应用,获取当前操作员的信息.
//	 
//	*/
//	public final void GloBaseApp()
//	{
//		// 执行登录。
//		Emp emp = new Emp("guobaogeng");
//		BP.Web.WebUser.SignInOfGener(emp);
//
//		// 当前登录人员编号.
//		String currLoginUserNo = BP.Web.WebUser.getNo();
//		// 登录人员名称
//		String currLoginUserName = BP.Web.WebUser.getName();
//		// 登录人员部门编号.
//		String currLoginUserDeptNo = BP.Web.WebUser.getFK_Dept();
//		// 登录人员部门名称
//		String currLoginUserDeptName = BP.Web.WebUser.getFK_DeptName();
//
//		BP.Web.WebUser.Exit(); //执行退出.
//	}
//	/** 
//	 数据库操作访问
//	 
//	*/
//	public final void DataBaseAccess()
//	{
//
//		///#region 执行不带有参数.
//		// 执行Insert ,delete, update 语句.
//		int result = BP.DA.DBAccess.RunSQL("DELETE FROM Port_Emp WHERE 1=2");
//
//		// 执行多个sql
//		String sqls = "DELETE FROM Port_Emp WHERE 1=2";
//		sqls += "@DELETE FROM Port_Emp WHERE 1=2";
//		sqls += "@DELETE FROM Port_Emp WHERE 1=2";
//		BP.DA.DBAccess.RunSQLs(sqls);
//
//		//执行查询返回datatable.
//		String sql = "SELECT * FROM Port_Emp";
//		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
//
//		//执行查询返回 string 值.
//		sql = "SELECT FK_Dept FROM Port_Emp WHERE No='" + BP.Web.WebUser.getNo() + "'";
//		String fk_dept = BP.DA.DBAccess.RunSQLReturnString(sql);
//
//		//执行查询返回 int 值. 也可以返回float, string
//		sql = "SELECT count(*) as Num FROM Port_Emp ";
//		int empNum = BP.DA.DBAccess.RunSQLReturnValInt(sql);
//
//		//运行存储过程.
//		String spName = "MySp";
//		BP.DA.DBAccess.RunSP(spName);
//
//		///#endregion 执行不带有参数.
//
//
//		///#region 执行带有参数.
//		// 执行Insert ,delete, update 语句.
//		// 已经明确数据库类型.
//		Paras ps = new Paras();
//		ps.SQL = "DELETE FROM Port_Emp WHERE No=@UserNo";
//		ps.Add("UserNo", "abc");
//		BP.DA.DBAccess.RunSQL(ps);
//
//		// 不知道数据库类型.
//		ps = new Paras();
//		ps.SQL = "DELETE FROM Port_Emp WHERE No=" + BP.Sys.SystemConfig.AppCenterDBVarStr + "UserNo";
//		ps.Add("UserNo", "abc");
//		BP.DA.DBAccess.RunSQL(ps);
//
//
//		//执行查询返回datatable.
//		ps = new Paras();
//		ps.SQL = "SELECT * FROM Port_Emp WHERE FK_Dept=@DeptNoVar";
//		ps.Add("DeptNoVar", "0102");
//		DataTable dtDept = BP.DA.DBAccess.RunSQLReturnTable(ps);
//
//		//运行存储过程.
//		ps = new Paras();
//		ps.Add("DeptNoVar", "0102");
//		spName = "MySp";
//		BP.DA.DBAccess.RunSP(spName, ps);
//
//		///#endregion 执行带有参数.
//	}
//	/** 
//	 Entity 的基本应用.
//	 
//	*/
//	public final void EntityBaseApp()
//	{
//
//		///#region  直接插入一条数据.
//		BP.Port.Emp emp = new BP.Port.Emp();
//		emp.CheckPhysicsTable();
////          检查物理表是否与Map一致 
////         *  1，如果没有这个物理表则创建。
////         *  2，如果缺少字段则创建。
////         *  3，如果字段类型不一直则删除创建，比如原来是int类型现在map修改成string类型。
////         *  4，map字段减少则不处理。
////         *  5，手工的向物理表中增加的字段则不处理。
////         *  6，数据源是视图字段不匹配则创建失败。
////         * 
//		emp.No = "zhangsan";
//		emp.setName("张三");
//		emp.FK_Dept = "01";
//		emp.Pass = "pub";
//		emp.Insert(); // 如果主键重复要抛异常。
//
//		///#endregion  直接插入一条数据.
//
//
//		///#region  保存的方式插入一条数据.
//		emp = new BP.Port.Emp();
//		emp.No = "zhangsan";
//		emp.setName("张三");
//		emp.FK_Dept = "01";
//		emp.Pass = "pub";
//		emp.Save(); // 如果主键重复直接更新，不会抛出异常。
//
//		///#endregion  保存的方式插入一条数据.
//
//
//		///#region  数据复制.
////        
////         * 如果一个实体与另外的一个实体两者的属性大致相同，就可以执行copy.
////         *  比如：在创建人员时，张三与李四两者只是编号与名称不同，只是改变不同的属性就可以执行相关的业务操作。
////         
//		Emp emp1 = new BP.Port.Emp("zhangsan");
//		emp = new BP.Port.Emp();
//		emp.Copy(emp1); // 同实体copy, 不同的实体也可以实现copy.
//		emp.getNo() = "lisi";
//		emp.setName("李四");
//		emp.Insert();
//		// copy 在业务逻辑上会经常应用，比如: 在一个流程中A节点表单与B节点表单字段大致相同，ccflow就是采用的copy方式处理。
//
//		///#endregion  数据复制.
//
//
//		///#region 查询.
//		String msg = ""; // 查询这条数据.
//		BP.Port.Emp myEmp = new BP.Port.Emp();
//		myemp.getNo() = "zhangsan";
//		if (myEmp.RetrieveFromDBSources() == 0) // RetrieveFromDBSources() 返回来的是查询数量.
//		{
//			this.Response.Write("没有查询到编号等于zhangsan的人员记录.");
//			return;
//		}
//		else
//		{
//			msg = "";
//			msg += "<BR>编号:" + myemp.getNo();
//			msg += "<BR>名称:" + myEmp.getName();
//			msg += "<BR>密码:" + myEmp.Pass;
//			msg += "<BR>部门编号:" + myEmp.FK_Dept;
//			msg += "<BR>部门名称:" + myEmp.FK_DeptText;
//			this.Response.Write(msg);
//		}
//
//		myEmp = new BP.Port.Emp();
//		myemp.getNo() = "zhangsan";
//		myEmp.Retrieve(); // 执行查询，如果查询不到则要抛出异常。
//
//		msg = "";
//		msg += "<BR>编号:" + myemp.getNo();
//		msg += "<BR>名称:" + myEmp.getName();
//		msg += "<BR>密码:" + myEmp.Pass;
//		msg += "<BR>部门编号:" + myEmp.FK_Dept;
//		msg += "<BR>部门名称:" + myEmp.FK_DeptText;
//		this.Response.Write(msg);
//
//		///#endregion 查询.
//
//
//		///#region 两种方式的删除。
//		// 删除操作。
//		emp = new BP.Port.Emp();
//		emp.getNo() = "zhangsan";
//		int delNum = emp.Delete(); // 执行删除。
//		if (delNum == 0)
//		{
//			this.Response.Write("删除 zhangsan 失败.");
//		}
//
//		if (delNum == 1)
//		{
//			this.Response.Write("删除 zhangsan 成功..");
//		}
//		if (delNum > 1)
//		{
//			this.Response.Write("不应该出现的异常。");
//		}
//		// 初试化实例后，执行删除，这种方式要执行两个sql.
//		emp = new BP.Port.Emp("abc");
//		emp.Delete();
//
//		///#endregion 两种方式的删除。
//
//
//		///#region 更新。
//		emp = new BP.Port.Emp("zhangyifan"); // 事例化它.
//		emp.setName("张一帆123"); //改变属性.
//		emp.Update(); // 更新它，这个时间BP将会把所有的属性都要执行更新，UPDATA 语句涉及到各个列。
//
//		emp = new BP.Port.Emp("fuhui"); // 事例化它.
//		emp.Update("Name", "福慧123"); //仅仅更新这一个属性。.UPDATA 语句涉及到Name列。
//
//		///#endregion 更新。
//	}
//	/** 
//	 Entities 的基本应用.
//	 
//	*/
//	public final void EntitiesBaseApp()
//	{
//
//		///#region 查询全部
//		// 查询全部分为两种方式，1 从缓存里查询全部。2，从数据库查询全部。  
//		Emps emps = new Emps();
//		int num = emps.RetrieveAll(); //从缓存里查询全部数据.
//
//		this.Response.Write("RetrieveAll查询出来(" + num + ")个");
//		for (Emp emp : emps.ToJavaList())
//		{
//			this.Response.Write("<hr>人员名称:" + emp.getName());
//			this.Response.Write("<br>人员编号:" + emp.getNo());
//			this.Response.Write("<br>部门编号:" + emp.FK_Dept);
//			this.Response.Write("<br>部门名称:" + emp.FK_DeptText);
//		}
//
//		//把entities 数据转入到DataTable里。
//		DataTable empsDTfield = emps.ToDataTableField(); //以英文字段做为列名。
//		DataTable empsDTDesc = emps.ToDataTableDesc(); //以中文字段做为列名。
//
//		// 从数据库里查询全部。
//		emps = new Emps();
//		num = emps.RetrieveAllFromDBSource();
//		this.Response.Write("RetrieveAllFromDBSource查询出来(" + num + ")个");
//		for (Emp emp : emps.ToJavaList())
//		{
//			this.Response.Write("<hr>人员名称:" + emp.getName());
//			this.Response.Write("<br>人员编号:" + emp.getNo());
//			this.Response.Write("<br>部门编号:" + emp.FK_Dept);
//			this.Response.Write("<br>部门名称:" + emp.FK_DeptText);
//		}
//
//		///#endregion 查询全部
//
//
//		///#region 按条件查询
//		// 单个条件查询。
//		Emps myEmps = new Emps();
//		QueryObject qo = new QueryObject(myEmps);
//		qo.AddWhere(EmpAttr.FK_Dept, "01");
//		qo.addOrderBy(EmpAttr.No); // 增加排序规则,Order OrderByDesc, addOrderByDesc addOrderByRandom.
//		num = qo.DoQuery(); // 返回查询的个数.
//		this.Response.Write("查询出来(" + num + ")个，部门编号=01的人员。");
//		for (Emp emp : myEmps.ToJavaList())
//		{
//			this.Response.Write("<hr>人员名称:" + emp.getName());
//			this.Response.Write("<br>人员编号:" + emp.getNo());
//			this.Response.Write("<br>部门编号:" + emp.FK_Dept);
//			this.Response.Write("<br>部门名称:" + emp.FK_DeptText);
//		}
//
//	 //   DataTable mydt = qo.DoQueryToTable();  // 查询出来的数据转入到datatable里。.
//
//		Emps myEmp1s = new Emps();
//		myEmp1s.Retrieve(EmpAttr.FK_Dept, "01");
//		for (Emp item : myEmp1s.ToJavaList())
//		{
//			this.Response.Write("<hr>人员名称:" + item.getName());
//			this.Response.Write("<br>人员编号:" + item.No);
//			this.Response.Write("<br>部门编号:" + item.FK_Dept);
//			this.Response.Write("<br>部门名称:" + item.FK_DeptText);
//		}
//
//		// 多个条件查询。
//		myEmps = new Emps();
//		qo = new QueryObject(myEmps);
//		qo.AddWhere(EmpAttr.FK_Dept, "01");
//		qo.addAnd();
//		qo.AddWhere(EmpAttr.No, "guobaogen");
//		num = qo.DoQuery(); // 返回查询的个数.
//		this.Response.Write("查询出来(" + num + ")个，部门编号=01并且编号=guobaogen的人员。");
//		for (Emp emp : myEmps.ToJavaList())
//		{
//			this.Response.Write("<hr>人员名称:" + emp.getName());
//			this.Response.Write("<br>人员编号:" + emp.getNo());
//			this.Response.Write("<br>部门编号:" + emp.FK_Dept);
//			this.Response.Write("<br>部门名称:" + emp.FK_DeptText);
//		}
//		// 具有括号表达式的查询。
//		myEmps = new Emps();
//		qo = new QueryObject(myEmps);
//		qo.addLeftBracket(); // 加上左括号.
//		qo.AddWhere(EmpAttr.FK_Dept, "01");
//		qo.addAnd();
//		qo.AddWhere(EmpAttr.No, "guobaogen");
//		qo.addRightBracket(); // 加上右括号.
//		num = qo.DoQuery(); // 返回查询的个数.
//		this.Response.Write("查询出来(" + num + ")个，部门编号=01并且编号=guobaogen的人员。");
//		for (Emp emp : myEmps.ToJavaList())
//		{
//			this.Response.Write("<hr>人员名称:" + emp.getName());
//			this.Response.Write("<br>人员编号:" + emp.getNo());
//			this.Response.Write("<br>部门编号:" + emp.FK_Dept);
//			this.Response.Write("<br>部门名称:" + emp.FK_DeptText);
//		}
//		// 具有where in 方式的查询。
//		myEmps = new Emps();
//		qo = new QueryObject(myEmps);
//		qo.AddWhereInSQL(EmpAttr.No, "SELECT No FROM Port_Emp WHERE FK_Dept='02'");
//		num = qo.DoQuery(); // 返回查询的个数.
//		this.Response.Write("查询出来(" + num + ")个，WHERE IN (SELECT No FROM Port_Emp WHERE FK_Dept='02')人员。");
//		for (Emp emp : myEmps.ToJavaList())
//		{
//			this.Response.Write("<hr>人员名称:" + emp.getName());
//			this.Response.Write("<br>人员编号:" + emp.getNo());
//			this.Response.Write("<br>部门编号:" + emp.FK_Dept);
//			this.Response.Write("<br>部门名称:" + emp.FK_DeptText);
//		}
//
//		// 具有LIKE 方式的查询。
//		myEmps = new Emps();
//		qo = new QueryObject(myEmps);
//		qo.AddWhere(EmpAttr.No, " LIKE ", "guo");
//		num = qo.DoQuery(); // 返回查询的个数.
//		this.Response.Write("查询出来(" + num + ")个，人员编号包含guo的人员。");
//		for (Emp emp : myEmps.ToJavaList())
//		{
//			this.Response.Write("<hr>人员名称:" + emp.getName());
//			this.Response.Write("<br>人员编号:" + emp.getNo());
//			this.Response.Write("<br>部门编号:" + emp.FK_Dept);
//			this.Response.Write("<br>部门名称:" + emp.FK_DeptText);
//		}
//
//		///#endregion 按条件查询
//
//
//		///#region 集合业务处理.
//		myEmps = new Emps();
//		myEmps.RetrieveAll(); // 查询全部出来.
//		// 遍历集合是常用的处理方法。
//		for (Emp emp : myEmps.ToJavaList())
//		{
//			this.Response.Write("<hr>人员名称:" + emp.getName());
//			this.Response.Write("<br>人员编号:" + emp.getNo());
//			this.Response.Write("<br>部门编号:" + emp.FK_Dept);
//			this.Response.Write("<br>部门名称:" + emp.FK_DeptText);
//		}
//		// 判断是否包含是指定的主键值.
//		boolean isHave = myEmps.Contains("Name", "郭宝庚"); //判断是否集合里面包含Name=郭宝庚的实体.
//		boolean isHave1 = myEmps.Contains("guobaogeng"); //判断是否集合里面主键No=guobaogeng的实体.
//
//		// 获取Name=郭宝庚的实体，如果没有就返回空。
//		Object tempVar = myEmps.GetEntityByKey("Name", "郭宝庚");
//		Emp empFind = (Emp)((tempVar instanceof Emp) ? tempVar : null);
//		if (empFind == null)
//		{
//			this.Response.Write("<br>没有找到: Name =郭宝庚 的实体.");
//		}
//		else
//		{
//			this.Response.Write("<br>已经找到了: Name =郭宝庚 的实体. 他的部门编号="+empFind.FK_Dept+"，部门名称="+empFind.FK_DeptText);
//		}
//		// 批量更新实体。
//		myEmps.Update(); // 等同于下一个循环。
//		for (Emp emp : myEmps.ToJavaList())
//		{
//			emp.Update();
//		}
//		// 删除实体.
//		myEmps.Delete(); // 等同于下一个循环。
//		for (Emp emp : myEmps.ToJavaList())
//		{
//			emp.Delete();
//		}
//		// 执行数据库删除，类于执行 DELETE Port_Emp WHERE FK_Dept='01' 的sql.
//		myEmps.Delete("FK_Dept", "01");
//
//		///#endregion
//	}
//	/** 
//	 展示EnttiyNo自动编号
//	 
//	*/
//	public final void EnttiyNo()
//	{
//		// 创建一个空的实体.
//		BP.Demo.Student en = new BP.Demo.Student();
//		// 给各个属性赋值，但是不要给编号赋值.
//		en.setName("张三");
//		en.FJ_BanJi = "001";
//		en.Age = 19;
//		en.XB = 1;
//		en.Tel = "0531-82374939";
//		en.Addr = "山东.济南.高新区";
//		en.Insert(); //这里会自动给该学生编号，从0001开始，编号规则打开该类的Map.
//
//		String xuehao = en.No;
//		this.Response.Write("信息已经加入,该学生的学好为:" + xuehao);
//
//		//查询出来该实体。
//		BP.Demo.Student myen = new BP.Demo.Student(xuehao);
//		this.Response.Write("学生姓名:" + myen.getName());
//		this.Response.Write("地址:" + myen.Addr);
//	}
//	/** 
//	 实体OID
//	 
//	*/
//	public final void EnttiyOID()
//	{
//		// 创建一个空的简历实体.
//		BP.Demo.Resume dtl = new BP.Demo.Resume();
//		dtl.FK_Emp = "zhangsan"; //给关联的主键赋值.
//		dtl.NianYue = "2014年4月";
//		dtl.GongZuoDanWei = "济南驰骋公司"; // 工作单位.
//		dtl.ZhengMingRen = "李四"; //证明人,李四.
//		dtl.Insert(); //这里会自动给该实体主键OID赋值， 他是一个自动增长的列.
//		this.Response.Write("信息已经加入,OID:" + dtl.OID);
//
//		//初始化该实体，并把它显示出来.
//		BP.Demo.Resume mydtl = new BP.Demo.Resume(dtl.OID);
//		this.Response.Write("工作单位:" + dtl.GongZuoDanWei + "证明人:" + dtl.ZhengMingRen);
//	}
//	/** 
//	 具有MyPK类型的实体，该类实体的主键是MyPK.
//	 它的主键是本表的2个或者3个以上的字段组合得来的.
//	 
//	*/
//	public final void EnttiyMyPK()
//	{
//		// 创建一个员工考核实体.
//		BP.Demo.EmpCent en = new BP.Demo.EmpCent();
//		en.FK_Emp = "zhangsan";
//		en.FK_NY = "2003-01";
//		en.MyPK = en.FK_NY + "_" + en.FK_Emp;
//		en.Cent = 100;
//		en.Insert(); // 插入到数据库里.
//		this.Response.Write("信息已经加入,Cent:" + en.Cent);
//
//		BP.Demo.EmpCent myen = new BP.Demo.EmpCent(en.MyPK);
//		this.Response.Write("人员:" + myen.FK_Emp + ",月份:" + myen.FK_NY+", 得分:"+myen.Cent);
//	}
//	/** 
//	 树的实体包含了No,Name,ParentNo,Idx 必须的属性(字段),它是树结构的描述.
//	 
//	*/
//	public final void EnttiyTree()
//	{
//		//创建父节点, 父节点的编号必须为1 ,父节点的ParentNo 必须是 0.
//		BP.WF.FlowSort en = new BP.WF.FlowSort("1");
//		en.setName("根目录");
//
//		//创建子目录节点.
//		BP.WF.FlowSort subEn = (BP.WF.FlowSort)en.DoCreateSubNode();
//		subEn.setName("行政类");
//		subEn.Update();
//
//		//创建子目录的评级节点.
//		BP.WF.FlowSort sameLevelSubEn = (BP.WF.FlowSort)subEn.DoCreateSameLevelNode();
//		sameLevelSubEn.setName("业务类");
//		sameLevelSubEn.Update();
//
//		//创建子目录的下一级节点1.
//		BP.WF.FlowSort sameLevelSubSubEn = (BP.WF.FlowSort)subEn.DoCreateSameLevelNode();
//		sameLevelSubSubEn.setName("日常办公");
//		sameLevelSubSubEn.Update();
//
//		//创建子目录的下一级节点1.
//		BP.WF.FlowSort sameLevelSubSubEn2 = (BP.WF.FlowSort)subEn.DoCreateSameLevelNode();
//		sameLevelSubSubEn2.setName("人力资源");
//		sameLevelSubSubEn2.Update();
////        *
////         *   根目录
////         *     行政类
////         *        日常办公
////         *        人力资源
////         *     业务类
////         * 
////         * 
////         
//	}
//	/** 
//	 多对多的关系,这种实体就有两个列(属性)
//	 这俩个列都是外键,并且也是该表的主键.
//	 
//	*/
//	public final void EnttiyMM()
//	{
//		BP.Port.EmpStation en = new BP.Port.EmpStation();
//		en.FK_Emp = "zhangsan";
//		en.FK_Station = "01";
//		en.Insert();
//	}