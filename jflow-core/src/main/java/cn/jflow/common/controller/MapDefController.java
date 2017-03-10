package cn.jflow.common.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.DA.DBAccess;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Log;
import BP.En.FieldTypeS;
import BP.En.UIContralType;
import BP.Sys.AthShowModel;
import BP.Sys.SFTable;
import BP.Sys.SFTables;
import BP.Sys.SysEnumMain;
import BP.Sys.SysEnumMains;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.Sys.Frm.FrmAttachment;
import BP.Sys.Frm.FrmAttachmentAttr;
import BP.Sys.Frm.FrmAttachments;
import BP.Sys.Frm.GroupField;
import BP.Sys.Frm.GroupFieldAttr;
import BP.Sys.Frm.GroupFields;
import BP.Sys.Frm.MapAttr;
import BP.Sys.Frm.MapAttrAttr;
import BP.Sys.Frm.MapAttrs;
import BP.Sys.Frm.MapData;
import BP.Sys.Frm.MapDataAttr;
import BP.Sys.Frm.MapDtl;
import BP.Sys.Frm.MapFrame;
import BP.Tools.Json;
import BP.Tools.StringHelper;
import BP.Tools.chs2py;
import BP.WF.ExtContral;
//import BP.WF.ExtContral;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/MapDef")
public class MapDefController extends BaseController{
	
	@RequestMapping(value = "/MapDef", method = RequestMethod.POST)
	@ResponseBody
	public void execute()
	{
		String doType = getRequest().getParameter("DoType");
		String msg = "";
		PrintWriter out = null;
		
		if ("Attachment_Init".equals(doType))
		{
			msg = attachment_Init();
		} else if ("Attachment_Save".equals(doType))
		{
			msg = attachment_Save();
		} else if ("Attachment_Delete".equals(doType))
		{
			msg = attachment_Delete();
		} else if("FieldInitGroupAndSysEnum".equals(doType))//转化成json
		{
			msg = this.FieldInitGroupAndSysEnum();
		}else if("Field_InitEnum".equals(doType))
		{
			msg = this.Field_InitEnum();//初始化枚举信息
		}else if(("Field_Delete").equals(doType))
		{
			msg = Field_Delete();//执行删除
		}else if("Field_SaveEnum".equals(doType))
		{
			msg = Field_SaveEnum();//保存枚举值
		}else if(("EditFExtContral_Init").equals(doType))
		{
			msg = EditFExtContral_Init();//扩展控件初始化
		}else if(("EditFExtContral_Save").equals(doType))
		{
			msg = EditFExtContral_Save();//扩展控件保存
		}else if("FieldTypeSelect".equals(doType))
		{
			msg = FieldTypeSelect();
		}else if("SFTable_Delete".equals(doType))//删除
		{
			msg = SFTable_Delete();
		}else if("SFTableList".equals(doType))
		{
			msg = SFTableList();
		}else if("EnumList".equals(doType))//初始化表格
		{
			msg = EnumList();//获得枚举列表.
		}else if("GroupField_Init".equals(doType))
		{
			msg = GroupField_Init();//初始化保存空白
		}else if("GroupField_SaveBlank".equals(doType))
		{
			msg = GroupField_SaveBlank();//保存空白
		}else if("CheckGroup_Save".equals(doType))
		{
			msg = CheckGroup_Save();//审核分组保存
		}else if("GroupField_DeleteCheck".equals(doType))
		{
			msg = GroupField_DeleteCheck();//删除分组
		}else if("HidAttr".equals(doType))
		{
			MapAttrs attrs = new MapAttrs();
            attrs.Retrieve(MapAttrAttr.FK_MapData, this.getFK_MapData(),
                MapAttrAttr.UIVisible, 0);
            msg = Json.ToJson(attrs.ToDataTableField());
		}else if("EditF_FieldInit".equals(doType))
		{
			msg = EditF_FieldInit();//字段属性初始化
		}else if("EditF_Save".equals(doType))
		{
			msg = EditF_Save();//字段属性保存
		}else if("Designer_NewMapDtl".equals(doType))
		{
			msg = this.Designer_NewMapDtl();//增加从表
		}else if("Designer_AthNew".equals(doType))
		{
			msg = this.Designer_AthNew();//新建附件
		}else if("Designer_NewFrame".equals(doType))
		{
			msg = Designer_NewFrame();//新建框架
		}else if("MapFrame_Init".equals(doType))
		{
			msg = MapFrame_Init();//框架初始化
		}else if("MapFrame_Save".equals(doType))
		{
			msg = MapFrame_Save();//框架保存
		}
		else if("MapFrame_Delete".equals(doType))
		{
			msg = MapFrame_Delete();//框架删除
		}
		else if("Dtl_Init".equals(doType))
		{
			msg = Dtl_Init();//初始化明细表
		}else if("Dtl_Save".equals(doType))
		{
			msg = Dtl_Save();//保存明细表
		}else if("DtlAttrs".equals(doType))
		{
			msg = DtlAttrs();//获得从表的列
		}else if("EditTableField_Init".equals(doType))
		{
			msg = EditTableField_Init();//初始化外键表
		}else if("EditTableField_Save".equals(doType))
		{
			msg = EditTableField_Save();//保存外键字段
		}else if("FieldDelete".equals(doType))
		{
			msg = FieldDelete();//执行删除
		}
		try {
			out = getResponse().getWriter();
			out.write(msg);
		} catch (IOException e) {
			Log.DebugWriteError("/WF/MapDef/MapDef.do?DoType="+doType +"err@"+e.getMessage());
		}finally{
			if(null !=out)
			{
				out.close();
			}
		}
	}
	
	/**
	 * 字段属性下拉框初始化
	 */
	public String EditFExtContral_Init()
    {
        ExtContral en = new ExtContral();
        en.setMyPK(this.getFK_MapData() + "_" + this.getKeyOfEn());
        en.RetrieveFromDBSources();
        return en.ToJson();
    }
	/**
	 * 字段属性下拉框保存
	 */
	public String EditFExtContral_Save()
    {
	         ExtContral en = new ExtContral();
	         en.setMyPK(this.getFK_MapData() + "_" + this.getKeyOfEn());
	         en.RetrieveFromDBSources();
	         
	         en.setUIContralType(UIContralType.forValue(Integer.parseInt(getRequest().getParameter("model"))));
          if((UIContralType.AthShow).equals(en.getUIContralType()))
            {
              en.setAthRefObj(this.GetValFromFrmByKey("DDL_Ath"));
             // en.setAthShowModel(Integer.parseInt(this.GetValFromFrmByKey("DDL_AthShowModel")));
              en.setAthShowModel(AthShowModel.forValue(Integer.parseInt(getRequest().getParameter("DDL_AthShowModel"))));

              //让附件不可见.
              FrmAttachment ath = new FrmAttachment(en.getAthRefObj());
              ath.setIsVisable(false);
              ath.Update();
              BP.DA.DBAccess.RunSQL("DELETE FROM Sys_GroupField WHERE EnName='"+this.getFK_MapData()+"' AND CtrlID='"+en.getAthRefObj()+"'");

              FrmAttachments aths = new FrmAttachments(this.getFK_MapData());
              for(FrmAttachment item : aths.ToJavaList())
              {
                  String sql = "SELECT count(*) FROM Sys_MapAttr WHERE AtPara LIKE '%"+item.getMyPK()+"@%' AND FK_MapData='"+this.getFK_MapData()+"'";
                  int num = DBAccess.RunSQLReturnValInt(sql);
                  if (num == 0)
                  {   
                      // 没有被引用.
                      item.setIsVisable(true);
                      item.Update();
                  }
              }	 
            }
          en.Update();

         return "保存成功.";
     }
	/**
	 * 删除
	 */
	public String SFTable_Delete()
    {
		  try{
			  SFTable sf = new SFTable(this.FK_SFTable());
	          sf.Delete();
	          return "删除成功...";
		  }catch(RuntimeException e){
			  return (e.getMessage() + "。您不能删除该表。");
		  }
			
		  
		 
         
      }
	/**
	 * 字典表列表
	 * @return
	 */
    public String SFTableList()
      {
          SFTables ens = new SFTables();
          ens.RetrieveAll();
          return  Json.ToJson(ens.ToDataTableField());   
      }
    /**
     * 枚举值列表
     */
    public String EnumList()
      {
          SysEnumMains ses = new SysEnumMains();
          ses.RetrieveAll();
          
          return Json.ToJson(ses.ToDataTableField());
      }
    
  /**
   * 初始化表
   * @return
   */
    public String EditTableField_Init()
    {
        MapAttr attr = new MapAttr();
        attr.setKeyOfEn(getKeyOfEn());
        attr.setFK_MapData(getFK_MapData());

        if (StringHelper.isNullOrEmpty(this.getMyPK()) == false)
        {
            attr.setMyPK(this.getMyPK());
            attr.RetrieveFromDBSources();
        }
        else
        {
            SFTable sf = new SFTable(FK_SFTable());
            attr.setName(sf.getName());
            attr.setKeyOfEn(sf.getNo());
        }

        //第1次加载.
        attr.setUIContralType(UIContralType.DDL);

        attr.setFK_MapData(getFK_MapData());

        //字体大小.
        int size = attr.getPara_FontSize();
        if (size == 0)
            attr.setPara_FontSize(12);

        //横跨的列数.
        if (attr.getColSpan() == 0)
            attr.setColSpan(1);

        return attr.ToJson();
    }
    
  /**
   * 保存外键表字段.
   * @return
   */
    public String EditTableField_Save()
    {
        try
        {
            //定义变量.
        	if(StringHelper.isNullOrEmpty(FK_SFTable()))
        	{
        		return "err@没有接收到FK_SFTable的值，无法进行保存操作。";
        	}
            //赋值.
            MapAttr attr = new MapAttr();
            attr.setKeyOfEn(this.getKeyOfEn());
            attr.setFK_MapData(this.getFK_MapData());
            if (StringHelper.isNullOrEmpty(this.getMyPK()) == false)
            {
                attr.setMyPK(this.getMyPK());
                attr.RetrieveFromDBSources();
            }
            else
            {
                /*判断字段是否存在？*/
                if (attr.IsExit(MapAttrAttr.KeyOfEn, this.getKeyOfEn(), MapAttrAttr.FK_MapData, this.getFK_MapData()) == true)
                    return "err@字段名:" + this.getKeyOfEn() + "已经存在.";
            }

            attr.setKeyOfEn(getKeyOfEn());
            attr.setFK_MapData(getFK_MapData());
            attr.setLGType(FieldTypeS.FK);
            attr.setUIBindKey(FK_SFTable());
            attr.setMyDataType(DataType.AppString);

            //控件类型.
            attr.setUIContralType(UIContralType.DDL);

            attr.setName(GetValFromFrmByKey("TB_Name"));
            attr.setKeyOfEn(GetValFromFrmByKey("TB_KeyOfEn"));
            attr.setColSpan(this.GetValIntFromFrmByKey("DDL_ColSpan"));
            if (attr.getColSpan() == 0)
                attr.setColSpan(1);

            attr.setUIIsInput(this.GetValBoolenFromFrmByKey("CB_IsInput"));   //是否是必填项.

            attr.setPara_FontSize(this.GetValIntFromFrmByKey("TB_FontSize")); //字体大小.

            //默认值.
            attr.setDefVal(this.GetValFromFrmByKey("TB_DefVal"));

            try
            {
                //分组.
                if (this.GetValIntFromFrmByKey("DDL_GroupID") != 0)
                    attr.setGroupID(this.GetValIntFromFrmByKey("DDL_GroupID")); //在那个分组里？
            }
            catch(Exception e)
            {
            	return "err@"+e.getMessage();
            }

            //是否可用？所有类型的属性，都需要。
            int isEnable = this.GetValIntFromFrmByKey("RB_UIIsEnable");
            if (isEnable == 0)
                attr.setUIIsEnable(false);
            else
                attr.setUIIsEnable(true);

            //是否可见?
            int visable = this.GetValIntFromFrmByKey("RB_UIVisible");
            if (visable == 0)
                attr.setUIVisible(false);
            else
                attr.setUIVisible(true);

            attr.setMyPK(this.getFK_MapData() + "_" + this.getKeyOfEn());
            attr.Save();

            return "保存成功.";
        }
        catch (Exception ex)
        {
            return "err@"+ex.getMessage();
        }
    }
    
  /**
   * 执行删除
   * @return
   */
    public String FieldDelete()
    {
        try
        {
            MapAttr attr = new MapAttr();
            attr.setMyPK(getMyPK());
            attr.RetrieveFromDBSources();
            attr.Delete();
            return "删除成功...";
        }
        catch (Exception ex)
        {
            return "err@" + ex.getMessage();
        }
    }
    
	/**
	 *  字段属性编辑 初始化
	 */
	public final String attachment_Init()
	{
		FrmAttachment ath = new FrmAttachment();
		ath.setFK_MapData(getFK_MapData());
		ath.setNoOfObj(getAth());
		ath.setFK_Node(getFK_Node());
		String MyPk =getRequest().getParameter("MyPK"); 
		if(StringHelper.isNullOrEmpty(MyPk))
		{
			if (getFK_Node() == 0)
			{
				ath.setMyPK(getFK_MapData() + "_" + getAth());
			}
			else
			{
				ath.setMyPK(getFK_MapData() + "_" + getAth() + "_" + getFK_Node());
			}
		}else
		{
			ath.setMyPK(getMyPK());
		}
		

		int i = ath.RetrieveFromDBSources();
		if (i == 0)
		{
			//初始化默认值.
			ath.setNoOfObj("Ath1");
			ath.setName("我的附件");
			ath.setSaveTo(SystemConfig.getPathOfDataUser() + "UploadFile/" + getFK_MapData() + "/");
		}

		if (i == 0 && getFK_Node() != 0)
		{
			//这里处理 独立表单解决方案, 如果有FK_Node 就说明该节点需要单独控制该附件的属性. 
			MapData mapData = new MapData();
			mapData.RetrieveByAttr(MapDataAttr.No, getFK_MapData());
			if ("0".equals(mapData.getAppType()))
			{
				FrmAttachment souceAthMent = new FrmAttachment();
				// 查询出来原来的数据.
				int rowCount = souceAthMent.Retrieve(FrmAttachmentAttr.FK_MapData, getFK_MapData(), FrmAttachmentAttr.NoOfObj, getAth(), FrmAttachmentAttr.FK_Node, "0");
				if (rowCount > 0)
				{
					ath.Copy(souceAthMent);
				}
			}
			if(StringHelper.isNullOrEmpty(MyPk))
			{
				if (getFK_Node() == 0)
				{
					ath.setMyPK(getFK_MapData() + "_" + getAth());
				}
				else
				{
					ath.setMyPK(getFK_MapData() + "_" + getAth() + "_" + getFK_Node());
				}
			}else
			{
				ath.setMyPK(getMyPK());
			}			
			//插入一个新的.
			ath.setFK_Node(getFK_Node());
			ath.setFK_MapData(getFK_MapData());
			ath.setNoOfObj(getAth());
		}

		return ath.ToJson();
	}

	/** 
	 *  保存.
	 * @return 
	 */
	public final String attachment_Save()
	{
		FrmAttachment ath = new FrmAttachment();
		ath.setFK_MapData(getFK_MapData());
		ath.setNoOfObj(getAth());
		ath.setFK_Node(getFK_Node());
		ath.setMyPK(getFK_MapData() + "_" + getAth());

		int i = ath.RetrieveFromDBSources();
		Object tempVar = BP.Sys.PubClass.copyFromRequest(ath, getRequest());
		ath = (FrmAttachment)((tempVar instanceof FrmAttachment) ? tempVar : null);

		if (i == 0)
		{
			ath.Save(); //执行保存.
		}
		else
		{
			ath.Update();
		}
		return "保存成功..";
	}
	
	/**
	 * 删除
	 * @return
	 */
	public final String attachment_Delete()
	{
		FrmAttachment ath = new FrmAttachment();
		ath.setMyPK(getMyPK());
		ath.Delete();
		return "删除成功.." + ath.getMyPK();
	}
	/**
	 * 分组&枚举： 两个数据源
	 * @return
	 */
	public final String FieldInitGroupAndSysEnum()
	{
		GroupFields gfs = new GroupFields(this.getFK_MapData());
        //分组值.
        DataSet ds = new DataSet();
        ds.Tables.add(gfs.ToDataTableField("Sys_GroupField"));
        //枚举值.
        String enumKey = "";
        
        if (StringHelper.isNullOrEmpty(enumKey)) {
        	 MapAttr ma = new MapAttr(this.getMyPK());
        	 
             enumKey = ma.getUIBindKey();
 		}
        
        SysEnums enums = new SysEnums(enumKey);
        ds.Tables.add(enums.ToDataTableField("Sys_Enum"));

        //转化成json输出.
        String json=  BP.Tools.Json.ToJson(ds);
        BP.DA.DataType.WriteFile(SystemConfig.getPathOfDataUser()+"FieldInitGroupAndSysEnum.json", json);
        return json;	
	}
	/**
	 * 初始化枚举
	 * @return
	 */
	public String Field_InitEnum()

     {
         MapAttr attr = new MapAttr();
         attr.setKeyOfEn(getKeyOfEn());
         attr.setFK_MapData(getFK_MapData());

         if (StringHelper.isNullOrEmpty(this.getMyPK()) == false)
         {
        	 System.out.println("this.getMyPK()=============="+this.getMyPK());
             attr.setMyPK(this.getMyPK());
             attr.RetrieveFromDBSources();
         }
         else
         {
             SysEnumMain sem = new SysEnumMain(this.getEnumKey());
             attr.setName(sem.getName());
             attr.setKeyOfEn(sem.getNo());
             attr.setDefVal("0");
         }
         //第1次加载.
         if (attr.getUIContralType() == UIContralType.TB)
         {	
        	 attr.setUIContralType(UIContralType.DDL);
         }
         if(StringHelper.isNullOrEmpty(attr.getFK_MapData()))
         {
        	 attr.setFK_MapData(this.getFK_MapData());
         }

         //字体大小.
         int size = attr.getPara_FontSize();
         if (size == 0)
             attr.setPara_FontSize(12);

         //横跨的列数.
         if (attr.getColSpan() == 0)
             attr.setColSpan(1);

         int model = attr.getRBShowModel();
         attr.setRBShowModel(model);

         return attr.ToJson();
     }
	
	 /**
     * 字段初始化数据
     * @return
     */
    public String EditF_FieldInit()
    {
        MapAttr attr = new MapAttr();
        attr.setKeyOfEn(this.getKeyOfEn());
        attr.setFK_MapData(this.getFK_MapData());

        if (StringHelper.isNullOrEmpty(this.getMyPK()) == false)
        {
            attr.setMyPK(this.getMyPK());
            attr.RetrieveFromDBSources();
        }
        else
        {
        	  SysEnumMain sem = new SysEnumMain("");
              attr.setName(sem.getName());
              attr.setKeyOfEn(sem.getNo());
              attr.setDefVal("0");
            attr.setGroupID(this.GroupField());
        }
        //字体大小.
        int size = attr.getPara_FontSize();
        if (size == 0)
            attr.setPara_FontSize(12);

        String field = attr.getPara_SiganField();
        boolean IsEnableJS = attr.getIsEnableJS();
        boolean IsSupperText = attr.getIsSupperText(); //是否是超大文本？
        boolean isBigDoc = attr.getIsBigDoc();

        //横跨的列数.
        if (attr.getColSpan() == 0)
            attr.setColSpan(1);

        return attr.ToJson();
    }
    /**
   	 * 字段属性保存
     * @return
     */
    public String EditF_Save()
    {
        try
        {
            //定义变量.
            int fType = Integer.parseInt(getRequest().getParameter("FType"));  //字段数据物理类型
            FieldTypeS  lgType = FieldTypeS.forValue(Integer.parseInt(getRequest().getParameter("LGType"))); //逻辑类型.
            String uiBindKey =getRequest().getParameter("UIBindKey");
            //赋值.
            MapAttr attr = new MapAttr();
            attr.setKeyOfEn(this.getKeyOfEn());
            attr.setFK_MapData(this.getFK_MapData());
            attr.setLGType(lgType); //逻辑类型.
            attr.setUIBindKey(uiBindKey); //绑定的枚举或者外键.
            attr.setMyDataType(fType); //物理类型.

            if (StringHelper.isNullOrEmpty(this.getMyPK()) == false)
            {
                attr.setMyPK(this.getMyPK());
                attr.RetrieveFromDBSources();
            }
            attr.setFK_MapData(this.getFK_MapData());
            attr.setMyDataType(fType); //数据类型.
            attr.setName(this.GetValFromFrmByKey("TB_Name"));
            attr.setKeyOfEn(this.GetValFromFrmByKey("TB_KeyOfEn"));
            attr.setColSpan(this.GetValIntFromFrmByKey("DDL_ColSpan"));
            if (attr.getColSpan() == 0)
                attr.setColSpan(1);
            attr.setPara_FontSize(this.GetValIntFromFrmByKey("TB_FontSize")); //字体大小.
            System.out.println("TB_FontSize======"+this.GetValIntFromFrmByKey("TB_FontSize"));
            attr.setPara_Tip(this.GetValFromFrmByKey("TB_Tip")); //操作提示.
            //默认值.
            attr.setDefVal(this.GetValFromFrmByKey("TB_DefVal"));
            //分组.
            if (this.GetValIntFromFrmByKey("DDL_GroupID") != 0)
                attr.setGroupID(this.GetValIntFromFrmByKey("DDL_GroupID")); //在那个分组里？ 
            if (attr.getMyDataType() == BP.DA.DataType.AppString && lgType == FieldTypeS.Normal)
            {
                attr.setUIIsInput(this.GetValBoolenFromFrmByKey("CB_IsInput"));   //是否是必填项.
                attr.setIsRichText(this.GetValBoolenFromFrmByKey("CB_IsRichText")); //是否是富文本？
                attr.setIsSupperText(this.GetValBoolenFromFrmByKey("CB_IsSupperText")); //是否是超大文本？
                //高度.
                attr.setUIHeightInt(this.GetValIntFromFrmByKey("DDL_Rows") * 23);
                //最大最小长度.
                attr.setMaxLen(this.GetValIntFromFrmByKey("TB_MaxLen"));
                attr.setMinLen(this.GetValIntFromFrmByKey("TB_MinLen"));
                System.out.println(this.GetValFloatFromFrmByKey("TB_UIWidth"));
                attr.setUIWidth(this.GetValFloatFromFrmByKey("TB_UIWidth")); //宽度.
            }
            //是否可用？所有类型的属性，都需要。
            int isEnable = this.GetValIntFromFrmByKey("RB_UIIsEnable");
            if (isEnable == 0)
            {
                attr.setUIIsEnable(false);
            }
            else
            {
                attr.setUIIsEnable(true);
            }
            //仅仅对普通类型的字段需要.
            if (lgType == FieldTypeS.Normal)
            {
                //是否可见?
                int visable = this.GetValIntFromFrmByKey("RB_UIVisible");
                if (visable == 0)
                    attr.setUIVisible(false);
                else
                    attr.setUIVisible(true);
            }

            attr.setMyPK(this.getFK_MapData() + "_" + this.getKeyOfEn());
            attr.Save();

            return "保存成功.";
        }
        catch (Exception ex)
        {
            return ex.getMessage();
        }
    }
         
     /**
     * 执行删除
     * @return
     */
    public String Field_Delete()
         {
             try
             {
                 MapAttr attr = new MapAttr();
                 attr.setMyPK(this.getMyPK());
                 attr.RetrieveFromDBSources();
                 attr.Delete();
                 return "删除成功...";
             }
             catch (Exception ex)
             {
                 return "err@" + ex.getMessage();
             }
         }
         
     /**
     * 保存枚举值
     * @return
     */
    public String Field_SaveEnum()
         {
             try
             {
                 //定义变量.
                 if (this.getEnumKey() == null)
                     return "err@没有接收到EnumKey的值，无法进行保存操作。";

                 //赋值.
                 MapAttr attr = new MapAttr();
                 attr.setKeyOfEn(this.getKeyOfEn());
                 attr.setFK_MapData(this.getFK_MapData());
                 if (StringHelper.isNullOrEmpty(this.getMyPK()) == false)
                 {
                     attr.setMyPK(this.getMyPK());
                     attr.RetrieveFromDBSources();
                 }
                 else
                 {
                    // 判断字段是否存在？
                     if (attr.IsExit(MapAttrAttr.KeyOfEn, this.getKeyOfEn(), MapAttrAttr.FK_MapData, this.getFK_MapData()) == true)
                         return "err@字段名:" + this.getKeyOfEn() + "已经存在.";
                 }

                 attr.setKeyOfEn(this.getKeyOfEn());
                 attr.setFK_MapData(this.getFK_MapData());
                 attr.setLGType(FieldTypeS.Enum);
                 attr.setUIBindKey(this.getEnumKey());
                 attr.setMyDataType(DataType.AppInt);

                 //控件类型.
                 attr.setUIContralType(UIContralType.forValue(this.GetValIntFromFrmByKey("RB_CtrlType")));
               //attr.setUIContralType(this.GetValIntFromFrmByKey("RB_CtrlType"));
                 attr.setName(this.GetValFromFrmByKey("TB_Name"));
                 attr.setKeyOfEn(this.GetValFromFrmByKey("TB_KeyOfEn"));
                 attr.setColSpan(this.GetValIntFromFrmByKey("DDL_ColSpan"));
                 if (attr.getColSpan() == 0)
                 {
                	 attr.setColSpan(1);
                 }
                     

                 //显示方式.
                 attr.setRBShowModel(this.GetValIntFromFrmByKey("DDL_RBShowModel"));
               
                 attr.setUIIsInput(this.GetValBoolenFromFrmByKey("CB_IsInput"));   //是否是必填项.

                 attr.setIsEnableJS(this.GetValBoolenFromFrmByKey("CB_IsEnableJS"));   //是否启用js设置？

                 attr.setPara_FontSize(this.GetValIntFromFrmByKey("TB_FontSize")); //字体大小.

                 //默认值.
                 attr.setDefVal(this.GetValFromFrmByKey("TB_DefVal"));

                 //分组.
                 if (this.GetValIntFromFrmByKey("DDL_GroupID") != 0)
                     attr.setGroupID(this.GetValIntFromFrmByKey("DDL_GroupID")); //在那个分组里？

                 //是否可用？所有类型的属性，都需要。
                 int isEnable = this.GetValIntFromFrmByKey("RB_UIIsEnable");
                 if (isEnable == 0)
                     attr.setUIIsEnable(false);
                 else
                     attr.setUIIsEnable(true);

                 //是否可见?
                 int visable = this.GetValIntFromFrmByKey("RB_UIVisible");
                 if (visable == 0)
                     attr.setUIVisible(false);
                 else
                     attr.setUIVisible(true);

                 attr.setMyPK(this.getFK_MapData() + "_" + this.getKeyOfEn());

                 attr.Save();

                 return "保存成功.";
             }
             catch (Exception ex)
             {
                 return "err@"+ex.getMessage();
             }
         }
	
    /**
     * 字段选择
     * @return
     */
         
    public String FieldTypeSelect()
         {
        	 String no = getRequest().getParameter("KeyOfEn");
             String name = getRequest().getParameter("Name");

             int fType = Integer.parseInt(getRequest().getParameter("FType"));

             MapAttrs attrs = new MapAttrs();
             int i = attrs.Retrieve(MapAttrAttr.FK_MapData, this.getFK_MapData(), MapAttrAttr.KeyOfEn, no);
             if (i != 0)
                 return "err@字段名：" + no + "已经存在.";

             //求出选择的字段类型.
             MapAttr attr = new MapAttr();
             attr.setName(name);
             attr.setKeyOfEn(no);
             attr.setFK_MapData(this.getFK_MapData());
             attr.setLGType(FieldTypeS.Normal);
             attr.setMyPK(this.getFK_MapData() + "_" + no);
             attr.setGroupID(this.GroupField());
             attr.setMyDataType(fType);

             int colspan = attr.getColSpan();
             attr.setPara_FontSize(12);
             int rows = attr.getUIRows();

             if (attr.getMyDataType() == DataType.AppString)
             {
                 attr.setUIWidth(100);
                 attr.setUIHeight(23);
                 attr.setUIVisible(true);
                 attr.setUIIsEnable(true);
                 attr.setColSpan(1);
                 attr.setMinLen(0);
                 attr.setMaxLen(50);
                 attr.setMyDataType(DataType.AppString);
                 attr.setUIContralType(UIContralType.TB);
                 attr.Insert();
                 return "url@EditF.htm?MyPK=" + attr.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + no + "&FType=" + attr.getMyDataType() + "&DoType=Edit&GroupField=" + this.GroupField();
             }

             if (attr.getMyDataType() == DataType.AppInt)
             {
            	 attr.setUIWidth(100);
                 attr.setUIHeight(23);
                 attr.setUIVisible(true);
                 attr.setUIIsEnable(true);
                 attr.setColSpan(1);
                 attr.setMinLen(0);
                 attr.setMaxLen(50);
                 attr.setMyDataType(DataType.AppInt);
                 attr.setUIContralType(UIContralType.TB);
                 attr.setDefVal("0");
                 attr.Insert();
                 return "url@EditF.htm?MyPK=" + attr.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + no + "&FType=" + attr.getMyDataType() + "&DoType=Edit&GroupField=" + this.GroupField();
             }

             if (attr.getMyDataType() == DataType.AppMoney)
             {
            	 attr.setUIWidth(100);
                 attr.setUIHeight(23);
                 attr.setUIVisible(true);
                 attr.setUIIsEnable(true);
                 attr.setColSpan(1);
                 attr.setMinLen(0);
                 attr.setMaxLen(50);
                 attr.setMyDataType(DataType.AppMoney);
                 attr.setUIContralType(UIContralType.TB);
                 attr.setDefVal("0");
                 attr.Insert();
                 return "url@EditF.htm?MyPK=" + attr.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + no + "&FType=" + attr.getMyDataType() + "&DoType=Edit&GroupField=" + this.GroupField();
             }

             if (attr.getMyDataType() == DataType.AppFloat)
             {
            	 attr.setUIWidth(100);
                 attr.setUIHeight(23);
                 attr.setUIVisible(true);
                 attr.setUIIsEnable(true);
                 attr.setColSpan(1);
                 attr.setMinLen(0);
                 attr.setMaxLen(50);
                 attr.setMyDataType(DataType.AppFloat);
                 attr.setUIContralType(UIContralType.TB);
                 attr.setDefVal("0");
                 attr.Insert();
                 return "url@EditF.htm?MyPK=" + attr.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + no + "&FType=" + attr.getMyDataType() + "&DoType=Edit&GroupField=" + this.GroupField();
             }

             if (attr.getMyDataType() == DataType.AppDouble)
             {
            	 attr.setUIWidth(100);
                 attr.setUIHeight(23);
                 attr.setUIVisible(true);
                 attr.setUIIsEnable(true);
                 attr.setColSpan(1);
                 attr.setMinLen(0);
                 attr.setMaxLen(50);
                 attr.setMyDataType(DataType.AppDouble);
                 attr.setUIContralType(UIContralType.TB);
                 attr.setDefVal("0");
                 attr.Insert();
                 return "url@EditF.htm?MyPK=" + attr.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + no + "&FType=" + attr.getMyDataType() + "&DoType=Edit&GroupField=" + this.GroupField();
             }

             if (attr.getMyDataType() == DataType.AppDate)
             {
            	 attr.setUIWidth(100);
                 attr.setUIHeight(23);
                 attr.setUIVisible(true);
                 attr.setUIIsEnable(true);
                 attr.setColSpan(1);
                 attr.setMinLen(0);
                 attr.setMaxLen(50);
                 attr.setUIContralType(UIContralType.TB);
                 attr.setMyDataType(DataType.AppDate);
                 attr.Insert();
                 return "url@EditF.htm?MyPK=" + attr.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + no + "&FType=" + DataType.AppDate + "&DoType=Edit&GroupField=" + this.GroupField();
             }

             if (attr.getMyDataType() == DataType.AppDateTime)
             {
            	 attr.setUIWidth(100);
                 attr.setUIHeight(23);
                 attr.setUIVisible(true);
                 attr.setUIIsEnable(true);
                 attr.setColSpan(1);
                 attr.setMinLen(0);
                 attr.setMaxLen(50);
                 attr.setUIContralType(UIContralType.TB);
                 attr.setMyDataType(DataType.AppDateTime);
                 attr.Insert();
                 return "url@EditF.htm?MyPK=" + attr.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + no + "&FType=" + DataType.AppDateTime + "&DoType=Edit&GroupField=" + this.GroupField();
             }

             if (attr.getMyDataType() == DataType.AppBoolean)
             {
            	 attr.setUIWidth(100);
                 attr.setUIHeight(23);
                 attr.setUIVisible(true);
                 attr.setUIIsEnable(true);
                 attr.setColSpan(1);
                 attr.setMinLen(0);
                 attr.setMaxLen(50);
                 attr.setUIContralType(UIContralType.CheckBok);
                 attr.setMyDataType(DataType.AppBoolean);
                 attr.setDefVal("0");
                 attr.Insert();
                 return "url@EditF.htm?MyPK=" + attr.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + no + "&FType=" + DataType.AppBoolean + "&DoType=Edit&GroupField=" + this.GroupField();
             }

             return "err@没有判断的数据类型." + attr.getMyDataTypeStr();
         }
    /**
     * 返回信息
     * @return
     */
    public String GroupField_Init()
    {
      BP.Sys.Frm.GroupField gf = new BP.Sys.Frm.GroupField();
      gf.setOID(this.GetRequestValInt("GroupField"));
      if (gf.getOID() != 0)
          gf.Retrieve();
             return gf.ToJson();
    }
         
    /**
     * 保存空白的分组
     * @return
     */
    public String GroupField_SaveBlank()
    {
       String no = this.GetValFromFrmByKey("TB_Blank_No");
       String name = this.GetValFromFrmByKey("TB_Blank_Name");
       BP.Sys.Frm.GroupField gf = new BP.Sys.Frm.GroupField();
       gf.setOID(this.GetRequestValInt("GroupField"));
       if (gf.getOID() != 0)
           gf.Retrieve();
           gf.setCtrlID(no);
           gf.setEnName(getFK_MapData());
           gf.setLab(name);
           gf.Save();
             return "保存成功.";
    }
    /**
     * 审核分组保存
     * @return
     */
    public String CheckGroup_Save()
     {
        String sta = this.GetValFromFrmByKey("TB_Check_Name");
        if (sta.length() == 0)
         {
           return "审核岗位不能为空";
         }
         String prx = this.GetValFromFrmByKey("TB_Check_No");
         if (prx.length() == 0)
         {
            prx = chs2py.convert(sta);
         }

          MapAttr attr = new MapAttr();
          int i = attr.Retrieve(MapAttrAttr.FK_MapData, this.getFK_MapData(), MapAttrAttr.KeyOfEn, prx + "_Note");
          i += attr.Retrieve(MapAttrAttr.FK_MapData, this.getFK_MapData(), MapAttrAttr.KeyOfEn, prx + "_Checker");
          i += attr.Retrieve(MapAttrAttr.FK_MapData, this.getFK_MapData(), MapAttrAttr.KeyOfEn, prx + "_RDT");
          if (i > 0)
          {
              return  "前缀已经使用：" + prx + " ， 请确认您是否增加了这个审核分组或者，请您更换其他的前缀。";
          }
             return "保存成功";
      }
    /**
     * 删除分组
     * @return
     */
    public String GroupField_DeleteCheck()
    {
       BP.Sys.Frm.GroupField gf = new BP.Sys.Frm.GroupField();
       gf.setOID(this.GetRequestValInt("GroupField"));
       gf.Delete();
       BP.WF.Template.MapFoolForm md = new BP.WF.Template.MapFoolForm(this.getFK_MapData());
       md.DoCheckFixFrmForUpdateVer();
          return "删除成功...";
    }
    /**
     * 增加从表
     * @return
     */
    public String Designer_NewMapDtl()
    {
        MapDtl en = new MapDtl();
        en.setFK_MapData(this.getFK_MapData());
        en.setNo(getRequest().getParameter("DtlNo"));

        if (en.RetrieveFromDBSources() == 1)
        {
            return "err@从表ID:" + en.getNo() + "已经存在.";
        }
        else
        {
            en.setName("我的从表" + en.getNo());
            en.setPTable(en.getNo());
            en.Insert();
            en.IntMapAttrs();
        }
        
        //增加从表控件
        GroupField gf = new GroupField();
		if (!gf.IsExit(GroupFieldAttr.CtrlID, en.getNo()))
		{
			gf.setLab(en.getName());
			gf.setCtrlID(en.getNo());
			gf.setCtrlType("Dtl");
			gf.setEnName(en.getFK_MapData());
			try {
				gf.DirectSave();
			} catch (Exception e) {
				Log.DebugWriteError("从表插入异常："+e.getMessage());
			}
		}
		
        //返回字串.
        return en.getNo();
      }	
    /**
     * 新建附件
     * @return
     */
    public String Designer_AthNew()
    {
        FrmAttachment ath = new FrmAttachment();
        ath.setFK_MapData(this.getFK_MapData());
        ath.setNoOfObj(getRequest().getParameter("AthNo"));
        ath.setMyPK(ath.getFK_MapData() + "_" + ath.getNoOfObj());
        if (ath.RetrieveFromDBSources() == 1){
            return "err@附件ID:"+ath.getNoOfObj()+"已经存在.";
        }else {
        	 //增加附件控件
            GroupField gf = new GroupField();
    		if (!gf.IsExit(GroupFieldAttr.CtrlID, ath.getMyPK()))
    		{
    			gf.setLab(ath.getName());
    			gf.setCtrlID(ath.getMyPK());
    			gf.setCtrlType("Ath");
    			gf.setEnName(ath.getFK_MapData());
    			try {
    				gf.DirectSave();
    			} catch (Exception e) {
    				Log.DebugWriteError("从表插入异常："+e.getMessage());
    			}
    		}
        }
        BP.WF.CCFormAPI.getCreateOrSaveAthMulti(this.getFK_MapData(),ath.getNoOfObj(), "我的附件", 100, 200);
        return ath.getMyPK();
    }
    /**
     * 新建框架
     * @return
     */
    public String Designer_NewFrame()
    {
        MapFrame frm = new MapFrame();
        frm.setFK_MapData(this.getFK_MapData());
        frm.setMyPK(frm.getFK_MapData() + "_" + getRequest().getParameter("FrameNo"));
        if (frm.RetrieveFromDBSources() == 1)
            return "err@框架ID:" + this.getRequest().getParameter("FrameNo") + "已经存在.";
        else
        {
            frm.setURL("http://ccport.org/About.aspx");
            frm.setName("我的框架" + this.getRequest().getParameter("FrameNo"));
            frm.setNoOfObj(getRequest().getParameter("FrameNo"));
            frm.Insert();
            
            //增加框架控件
            GroupField gf = new GroupField();
            if (!gf.IsExit(GroupFieldAttr.CtrlID, frm.getMyPK()))
			{
				gf.setLab(frm.getName());
				gf.setCtrlID(frm.getMyPK());
				gf.setCtrlType("Frame");
				gf.setEnName(frm.getFK_MapData());
				gf.Insert();
			}
        }
        //BP.Sys.CCFormAPI.CreateOrSaveAthMulti(this.FK_MapData, this.GetRequestVal("FrameNo"), "我的附件", 100, 200);
        return frm.getMyPK();
    }
    
    /**
     * 框架信息
     * @return
     */
    public String MapFrame_Init()
    {
        MapFrame mf = new MapFrame();
        mf.setFK_MapData(this.getFK_MapData());
        if (this.getMyPK() == null)
        {
        	
            mf.setURL("http://ccflow.org");
            mf.setW("100%");
            mf.setH("300");
            mf.setName("我的框架.");
            mf.setFK_MapData(this.getFK_MapData());
            mf.setMyPK(BP.DA.DBAccess.GenerGUID());
        }
        else
        {
            mf.setMyPK(this.getMyPK());
            mf.RetrieveFromDBSources();
        }
        return mf.ToJson();
    }
    
  /**
   * 框架保存
   * @return
   */
    public String MapFrame_Save()
    {
        MapFrame mf = new MapFrame();
        mf = (MapFrame) BP.Sys.PubClass.copyFromRequest(mf, getRequest());
        mf.setFK_MapData(this.getFK_MapData());
        mf.Save(); //执行保存.
        return "保存成功..";
    }
    /**
     * 框架删除
     */
    public String MapFrame_Delete()
    {
        MapFrame dtl = new MapFrame();
        dtl.setMyPK(this.getMyPK());  
        dtl.Delete();
        return "操作成功..." + this.getMyPK() ;
    }
    /**
     * 该方法有2处调用
     * 1，修改字段
     * 2，编辑属性
     * @return
     */
 
    public String Dtl_Init()
    {
        MapDtl dtl = new MapDtl();
        dtl.setNo(this.getFK_MapDtl());
        if (dtl.RetrieveFromDBSources() == 0)
        {
            dtl.setFK_MapData(this.getFK_MapData());
            dtl.setName(this.getFK_MapData());
            dtl.Insert();
            dtl.IntMapAttrs();
        }

        if (this.getFK_Node() != 1)
        {
            /* 如果传递来了节点信息, 就是说明了独立表单的节点方案处理, 现在就要做如下判断.
             * 1, 如果已经有了.
             */
            dtl.setNo(this.getFK_MapDtl() + "_" + this.getFK_Node());
            if (dtl.RetrieveFromDBSources() == 0)
            {

                // 开始复制它的属性.
                MapAttrs attrs = new MapAttrs(this.getFK_MapDtl());
                //让其直接保存.
                dtl.setNo(this.getFK_MapDtl() + "_" + this.getFK_Node());
                dtl.setFK_MapData("Temp");
                dtl.DirectInsert(); //生成一个明细表属性的主表.

                //循环保存字段.
                int idx = 0;
                for(MapAttr item : attrs.ToJavaList())
                {
                    item.setFK_MapData(this.getFK_MapDtl() + "_" + this.getFK_Node());
                    item.setMyPK(item.getFK_MapData() + "_" + item.getKeyOfEn());
                    item.Save();
                    idx++;
                    item.setIDX(idx);
                    item.DirectUpdate();
                }

                MapData md = new MapData();
                md.setNo("Temp");
                if (md.getIsExits() == false)
                {
                    md.setName("为权限方案设置的临时的数据");
                    md.Insert();
                }
            }
        }
        DataSet ds = new DataSet();
        DataTable dt = dtl.ToDataTableField("Main");
        
        ds.Tables.add(dt);

        //获得字段列表.
        
        MapAttrs attrsDtl = new MapAttrs(this.getFK_MapDtl());
        DataTable dtAttrs = attrsDtl.ToDataTableField("Ens");
        ds.Tables.add(dtAttrs);

        //返回json配置信息.
        return BP.Tools.Json.ToJson(ds); 
    }
      
    /**
     * 执行保存
     * @return
     */
    public String Dtl_Save()
    {
          try
          {
              //复制.
              MapDtl dtl = new MapDtl(this.getFK_MapDtl());

              //从request对象里复制数据,到entity.
              BP.Sys.PubClass.copyFromRequest(dtl,getRequest());

              dtl.Update();

              return "保存成功...";
          }
          catch(Exception ex)
          {
              return "err@"+ex.getMessage();
          }
      }

  /**
   * 获得从表的列
   * @return
   */
    public String DtlAttrs()
    {
        MapAttrs attrs = new MapAttrs(this.getFK_MapDtl());
        return Json.ToJson(attrs.ToDataTableField()); 
    }
     
    public String getFK_MapData() 
    {
     	String fk_mapdata = getRequest().getParameter("FK_MapData");
     	if (fk_mapdata == null || fk_mapdata.equals("") || fk_mapdata.equals("null"))
     	{
     		fk_mapdata = "ND" + getFK_Node();
    	}
     		return fk_mapdata;
    }
       
    public final String getKeyOfEn()
    {
        String str = getRequest().getParameter("KeyOfEn");
        if(StringHelper.isNullOrEmpty(str))
        {
        	 return null;
        }  
         return str; 
    }
    /**
     * 公共方法获取值
     * @param param
     * @return
     */
    public int GetRequestValInt(String param)
    {
       String str = getRequest().getParameter(param);
       if(StringHelper.isNullOrEmpty(str))
       {
       	 return 0;
       } 
       return Integer.parseInt(str);
     }
         
    /**
     * 公共方法获取值
     * @param param
     * @return
     */
    public String GetRequestVal(String param)
    {
        try {
        	URLEncoder.encode(getRequest().getParameter(param),"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
             return param ;
     }
	
	/**
	 *字段属性编号
	 * @return
	 */
	public final String getAth()
	{
		String str = getRequest().getParameter("Ath");
		if(StringHelper.isNullOrEmpty(str))
        {
        	 return null;
        } 
		return str;
	}

	public String getEnumKey()
     {
             String str = getRequest().getParameter("EnumKey");
             if(StringHelper.isNullOrEmpty(str))
             {
             	 return null;
             } 
             return str;

     }
	/**
	 * 字典表
	 */
    public String FK_SFTable()
     {
            String str =getRequest().getParameter("FK_SFTable");
            if(StringHelper.isNullOrEmpty(str))
            {
            	 return null;
            } 
             return str;         
     }
	
	/**
	 * 获得表单的属性
	 * @param key
	 * @return
	 */
    public String GetValFromFrmByKey(String key)
     {
         String val = getRequest().getParameter(key);
         if (val == null)
             return null;
         val = val.replace("'", "~");
         return val;
     }
    public int GetValIntFromFrmByKey(String key)
     {
         return Integer.parseInt(this.GetValFromFrmByKey(key));
     }
     
    public float GetValFloatFromFrmByKey(String key)
     {
         return Float.parseFloat(this.GetValFromFrmByKey(key));
     }
    public boolean GetValBoolenFromFrmByKey(String key)
     {
         String val = this.GetValFromFrmByKey(key);
         if (val == null || val == "")
             return false;
         return true;
     }
    public int GroupField()
     {
             String str = getRequest().getParameter("GroupField");
             if(StringHelper.isNullOrEmpty(str))
             {
             	 return 0;
             } 
             return Integer.parseInt( str);
         
     }

}
