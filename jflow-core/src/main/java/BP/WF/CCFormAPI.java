package BP.WF;

import java.util.ArrayList;

import BP.DA.DataSet;
import BP.DA.DataType;
import BP.DA.Log;
import BP.En.FieldTypeS;
import BP.En.QueryObject;
import BP.En.UIContralType;
import BP.Sys.GEEntity;
import BP.Sys.SFTable;
import BP.Sys.SFTables;
import BP.Sys.SysEnum;
import BP.Sys.SysEnumMain;
import BP.Sys.SysEnumMainAttr;
import BP.Sys.SysEnumMains;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.Sys.Frm.AttachmentUploadType;
import BP.Sys.Frm.FrmAttachment;
import BP.Sys.Frm.FrmEle;
import BP.Sys.Frm.FrmImgAth;
import BP.Sys.Frm.FrmRB;
import BP.Sys.Frm.GroupField;
import BP.Sys.Frm.MapAttr;
import BP.Sys.Frm.MapAttrAttr;
import BP.Sys.Frm.MapData;
import BP.Sys.Frm.MapDtl;
import BP.Tools.StringHelper;
import BP.WF.Template.BillFileType;

/** 
 表单引擎api
*/
public class CCFormAPI extends Dev2Interface
{
	/**  
	 生成报表
	 
	 @param templeteFilePath 模版路径
	 @param ds 数据源
	 @return 生成单据的路径
	*/
	public static void Frm_GenerBill(String templeteFullFile, String saveToDir, String saveFileName, BillFileType fileType, DataSet ds, String fk_mapData)
	{

		MapData md = new MapData(fk_mapData);
		GEEntity entity = md.GenerGEEntityByDataSet(ds);

		BP.Pub.RTFEngine rtf = new BP.Pub.RTFEngine();
		rtf.getHisEns().clear();
		rtf.getEnsDataDtls().clear();

		rtf.getHisEns().AddEntity(entity);

		ArrayList dtls = entity.getDtls();


		for (Object item : dtls)
		{
			rtf.getEnsDataDtls().add(item);
		}

		rtf.MakeDoc(templeteFullFile, saveToDir, saveFileName, null, false);
	}
	/**
	 * 保存
	 * @param enumKey
	 * @param enumLab
	 * @param cfg
	 * @param lang
	 * @return
	 */
	
    public static String SaveEnum(String enumKey, String enumLab, String cfg)
    {
    	String lang ="CH";
        SysEnumMain sem = new SysEnumMain();
        sem.setNo(enumKey);
        if (sem.RetrieveFromDBSources() == 0)
        {
            sem.setName(enumLab);
            sem.setCfgVal(cfg);
            sem.setLang(lang);
            sem.Insert();
        }
        else
        {
            sem.setName(enumLab);
            sem.setCfgVal(cfg);
            sem.setLang(lang);
            sem.Update();
        }

        String[] strs = cfg.split("@");
        for(String str : strs)
        {
            if (StringHelper.isNullOrEmpty(str))
                continue;
            String[] kvs = str.split("=");
            SysEnum se = new SysEnum();
            se.setEnumKey(enumKey);
            se.setLang(lang);
            se.setIntKey(Integer.parseInt(kvs[0]));
            se.setLab(kvs[1]);
            se.setMyPK(se.getEnumKey() + "_" + se.getLang() + "_" + se.getIntKey());
            se.Save();
        }
        return "保存成功.";
    }

	
	/**
	 * 创建/修改-多附件
	 * @param fk_mapdata
	 * @param no
	 * @param name
	 * @param x
	 * @param y
	 */
    public static void getCreateOrSaveAthMulti(String fk_mapdata, String no, String name, float x, float y)
    {
        FrmAttachment ath = new FrmAttachment();
        ath.setFK_MapData(fk_mapdata);
        ath.setNoOfObj(no);
        ath.setMyPK(ath.getFK_MapData() + "_" + ath.getNoOfObj());
       int i= ath.RetrieveFromDBSources();
        if (i==0)
        {
            ath.setSaveTo(SystemConfig.getPathOfDataUser() + "/UploadFile/" + fk_mapdata + "/");
        }

        ath.setUploadType(AttachmentUploadType.Multi);
        ath.setName(name);
        ath.setX(x);
        ath.setY(y);
        ath.Save();
    }
    
    /**
     *  获得外键表
     * @param pageNumber 第几页
     * @param pageSize 每页大小
     * @return json
     */
    public static String DB_SFTableList(int pageNumber, int pageSize)
    {
        //获得查询.
        SFTables sftables = new SFTables();
        QueryObject obj = new QueryObject(sftables);
        int RowCount = obj.GetCount();

        //查询
        obj.DoQuery(SysEnumMainAttr.No, pageSize, pageNumber);

        return BP.Tools.Entitis2Json.ConvertEntitis2GridJsonOnlyData(sftables, RowCount);
    }
    
    /**
     * 
     * @param pageNumber 第几页
     * @param pageSize 每页大小
     * @return json
     */
    public static String DB_EnumerationList(int pageNumber, int pageSize)
    {
        SysEnumMains enumMains = new SysEnumMains();
        QueryObject obj = new QueryObject(enumMains);
        int RowCount = obj.GetCount();
        
        //查询
        obj.DoQuery(SysEnumMainAttr.No, pageSize, pageNumber);

        return BP.Tools.Entitis2Json.ConvertEntitis2GridJsonOnlyData(enumMains, RowCount);
    }
    
    /**
     * @param fk_mapdata 表单ID
     * @param ctrlType 控件类型
     * @param no  编号
     * @param name 名称
     * @param x 位置x
     * @param y 位置y
     */
    public static void CreatePublicNoNameCtrl(String fk_mapdata, String ctrlType, String no, String name, float x, float y)
    {
    	if("Dtl".equals(ctrlType))
    	{
    		CreateOrSaveDtl(fk_mapdata, no, name, x, y);
    	}else if("AthMulti".equals(ctrlType))
    	{
    		CreateOrSaveAthMulti(fk_mapdata, no, name, x, y);
    	} else if("AthSingle".equals(ctrlType))
    	{
    		CreateOrSaveAthSingle(fk_mapdata, no, name, x, y);
    	} else if("AthImg".equals(ctrlType))
    	{
    		CreateOrSaveAthImg(fk_mapdata, no, name, x, y);
    	} else if("Fieldset".equals(ctrlType))   //分组.
    	{
    		FrmEle fe = new FrmEle();
            fe.setMyPK(fk_mapdata + "_" + no);
            if (fe.RetrieveFromDBSources() != 0){
            	Log.DebugWriteError("@创建失败，已经有同名元素["+no+"]的控件.");
            }
            fe.setFK_MapData(fk_mapdata);
            fe.setEleType("Fieldset");
            fe.setEleName(name);
            fe.setEleID(no);
            fe.setX(x);
            fe.setY(y);
            fe.Insert();
    	} else 
    	{
    		Log.DebugWriteError("@没有判断的存储控件:"+ctrlType+",存储该控件前,需要做判断.");
    	}
    }
    
    /**
     * 创建/修改一个明细表 
     * @param fk_mapdata 表单ID
     * @param dtlNo 明细表编号
     * @param dtlName 名称
     * @param x 位置x
     * @param y 位置y
     */
    public static void CreateOrSaveDtl(String fk_mapdata, String dtlNo, String dtlName, float x, float y)
    {
        MapDtl dtl = new MapDtl();
        dtl.setNo(dtlNo);
        dtl.RetrieveFromDBSources();

        dtl.setX(x);
        dtl.setY(y);
        dtl.setName(dtlName);
        dtl.setFK_MapData(fk_mapdata);
        dtl.Save();

        //初始化他的map.
        dtl.IntMapAttrs();
    }
    
    /**
     * 创建/修改-多附件
     * @param fk_mapdata 表单ID
     * @param no 明细表编号
     * @param name 名称
     * @param x 位置x
     * @param y 位置y
     */
    public static void CreateOrSaveAthMulti(String fk_mapdata, String no, String name, float x, float y)
    {
        FrmAttachment ath = new FrmAttachment();
        ath.setFK_MapData(fk_mapdata);
        ath.setNoOfObj(no);
        ath.setMyPK(ath.getFK_MapData() + "_" + ath.getNoOfObj());
       int i= ath.RetrieveFromDBSources();
        if (i==0)
        {
            ath.setSaveTo(SystemConfig.getPathOfDataUser() + "UploadFile/" + fk_mapdata + "/");
        }

        ath.setUploadType(AttachmentUploadType.Multi);
        ath.setName(name);
        ath.setX(x);
        ath.setY(y);
        ath.Save();
    }
    
    /**
     * 创建/修改-多附件
     * @param fk_mapdata 表单ID
     * @param no 明细表编号
     * @param name 名称
     * @param x 位置x
     * @param y 位置y
     */
    public static void CreateOrSaveAthSingle(String fk_mapdata, String no, String name, float x, float y)
    {
        FrmAttachment ath = new FrmAttachment();
        ath.setFK_MapData(fk_mapdata);
        ath.setNoOfObj(no);

        ath.setMyPK(ath.getFK_MapData() + "_" + ath.getNoOfObj());
        ath.RetrieveFromDBSources();
        ath.setUploadType(AttachmentUploadType.Single);
        ath.setName(name);
        ath.setX(x);
        ath.setY(y);
        ath.Save();
    }
    
    /**
     * 创建/修改-图片附件
     * @param fk_mapdata 表单ID
     * @param no 明细表编号
     * @param name 名称
     * @param x 位置x
     * @param y 位置y
     */
    public static void CreateOrSaveAthImg(String fk_mapdata, String no, String name, float x, float y)
    {
        FrmImgAth ath = new FrmImgAth();
        ath.setFK_MapData(fk_mapdata);
        ath.setCtrlID(no);
        ath.setMyPK(fk_mapdata + "_" + no);

        ath.setX(x);
        ath.setY(y);
        ath.Insert();
    }
    
  /// <summary>
    /// 
    /// </summary>
    /// <param name="fk_mapdata">表单ID</param>
    /// <param name="fieldName">字段名</param>
    /// <param name="fieldDesc">字段中文名</param>
    /// <param name="fk_SFTable">外键表</param>
    /// <param name="x">位置</param>
    /// <param name="y">位置</param>
    /// <param name="colSpan">跨的列数</param>
    /**
     * 创建一个外部数据字段
     * @param fk_mapdata 表单ID
     * @param fieldName 字段名
     * @param fieldDesc 字段中文名
     * @param fk_SFTable 外键表
     * @param x 位置
     * @param y 位置
     */
    public static void SaveFieldSFTable(String fk_mapdata, String fieldName, String fieldDesc, String fk_SFTable, float x, float y)
    {
        //外键字段表.
        SFTable sf = new SFTable(fk_SFTable);

        MapAttr attr = new MapAttr();
        attr.setMyPK(fk_mapdata + "_" + fieldName);
        attr.RetrieveFromDBSources();

        //基本属性赋值.
        attr.setFK_MapData(fk_mapdata);
        attr.setKeyOfEn(fieldName);
        attr.setName(fieldDesc);
        attr.setMyDataType(BP.DA.DataType.AppString);
        attr.setUIContralType(BP.En.UIContralType.DDL);
        attr.setUIBindKey(fk_SFTable); //绑定信息.
        attr.setX(x);
        attr.setY(y);

        //根据外键表的类型不同，设置它的LGType.
        switch (sf.getSrcType())
        {
            case BPClass:
            case CreateTable:
            case TableOrView:
                attr.setLGType(FieldTypeS.FK);
                break;
            default:
                attr.setLGType(FieldTypeS.Normal);
                break;
        }
        attr.Save();
       
        //如果是普通的字段, 这个属于外部数据类型,或者webservices类型.
        if (attr.getLGType() == FieldTypeS.Normal)
        {
            MapAttr attrH = new MapAttr();
            attrH.Copy(attr);
            attrH.setKeyOfEn(attr.getKeyOfEn() + "T");
            attrH.setName(attr.getName());
            attrH.setUIContralType(BP.En.UIContralType.TB);
            attrH.setMinLen(0);
            attrH.setMaxLen(60);
            attrH.setMyDataType(BP.DA.DataType.AppString);
            attrH.setUIVisible(false);
            attrH.setUIIsEnable(false);
            attrH.setMyPK(attrH.getFK_MapData() + "_" + attrH.getKeyOfEn());
            attrH.Save();
        }
    }
    
    public static void NewEnumField(String fk_mapdata, String field, String fieldDesc, String enumKey, UIContralType ctrlType, float x, float y)
    {

        MapAttr ma = new MapAttr();
        ma.setFK_MapData(fk_mapdata);
        ma.setKeyOfEn(field);
        ma.setName(fieldDesc);
        ma.setMyDataType(DataType.AppInt);
        ma.setX(x);
        ma.setY(y);
        ma.setUIIsEnable(true);
        ma.setLGType(FieldTypeS.Enum);
        ma.setUIContralType(ctrlType);
        ma.setUIBindKey(enumKey);
        ma.Insert();

        if (ma.getUIContralType() != UIContralType.RadioBtn)
        {
        	return;
        }

        //删除可能存在的数据.
        BP.DA.DBAccess.RunSQL("DELETE FROM Sys_FrmRB WHERE KeyOfEn='"+ma.getKeyOfEn()+"' AND FK_MapData='"+ma.getFK_MapData()+"'");

        SysEnums ses = new SysEnums(ma.getUIBindKey());
        int idx = 0;
        for(SysEnum item : ses.ToJavaList())
        {
            idx++;
            FrmRB rb = new FrmRB();
            rb.setFK_MapData(ma.getFK_MapData());
            rb.setKeyOfEn(ma.getKeyOfEn());
            rb.setEnumKey(ma.getUIBindKey());

            rb.setLab(item.getLab());
            rb.setIntKey(item.getIntKey());
            rb.setX(ma.getX());

            //让其变化y值.
            rb.setY(ma.getY() + idx * 30);
            rb.Insert();
         }
     }
    
  /// <summary>
    ///
    /// </summary>
    /// <param name="frmID"></param>
    /// <param name="field"></param>
    /// <param name="fieldDesc"></param>
    /// <param name="mydataType"></param>
    /// <param name="x"></param>
    /// <param name="y"></param>
    /// <param name="colSpan"></param>
    /**
     *  创建字段
     * @param fk_mapdata 表单ID
     * @param field 字段ID
     * @param fieldDesc 字段中文名
     * @param mydataType  
     * @param x 位置
     * @param y 位置
     */
    public static void NewField(String frmID, String field, String fieldDesc,int mydataType, float x, float y)
    {
        MapAttr ma = new MapAttr();
        ma.setFK_MapData(frmID);
        ma.setKeyOfEn(field);
        ma.setName(fieldDesc);
        ma.setMyDataType(mydataType);
        ma.setX(x);
        ma.setY(y);
        ma.Insert();
    }
    
  /// <summary>
    /// 创建字段分组
    /// </summary>
    /// <param name="frmID"></param>
    /// <param name="gKey"></param>
    /// <param name="gName"></param>
    /// <returns></returns>
    public static String NewCheckGroup(String frmID, String gKey, String gName)
    {
        MapAttr attrN = new MapAttr();
        int i = attrN.Retrieve(MapAttrAttr.FK_MapData, frmID, MapAttrAttr.KeyOfEn, gKey + "_Note");
        i += attrN.Retrieve(MapAttrAttr.FK_MapData, frmID, MapAttrAttr.KeyOfEn, gKey + "_Checker");
        i += attrN.Retrieve(MapAttrAttr.FK_MapData, frmID, MapAttrAttr.KeyOfEn, gKey + "_RDT");
        if (i > 0)
        {
        	return "error:前缀已经使用：" + gKey + " ， 请确认您是否增加了这个审核分组或者，请您更换其他的前缀。";
        }

        GroupField gf = new GroupField();
        gf.setLab(gName);
        gf.setEnName(frmID);
        gf.Insert();

        attrN = new MapAttr();
		attrN.setFK_MapData(frmID);
        attrN.setKeyOfEn(gKey + "_Note");
        attrN.setName("审核意见");
        attrN.setMyDataType(DataType.AppString);
        attrN.setUIContralType(UIContralType.TB);
        attrN.setUIIsEnable(true);
        attrN.setUIIsLine(true);
        attrN.setMaxLen(4000);
        attrN.setGroupID((int)gf.getOID());
        attrN.setUIHeight(23 * 3);
        attrN.setIDX(1); 
        attrN.Insert();

        attrN = new MapAttr();
        attrN.setFK_MapData(frmID);
        attrN.setKeyOfEn(gKey + "_Checker");
        attrN.setName("审核人");// "审核人");
        attrN.setMyDataType(DataType.AppString);
        attrN.setUIContralType(UIContralType.TB);
        attrN.setMaxLen(50);
        attrN.setMinLen(0);
        attrN.setUIIsEnable(true);
        attrN.setUIIsLine(false);
        attrN.setDefVal("@WebUser.No");
        attrN.setUIIsEnable(false);
        attrN.setGroupID((int)gf.getOID());
        attrN.setIsSigan(true);
        attrN.setIDX(2);
        attrN.Insert();

        attrN = new MapAttr();
        attrN.setFK_MapData(frmID);
        attrN.setKeyOfEn(gKey + "_RDT");
        attrN.setName("审核日期"); // "审核日期" 
        attrN.setMyDataType(DataType.AppDateTime);
        attrN.setUIContralType(UIContralType.TB);
        attrN.setUIIsEnable(true);
        attrN.setUIIsLine(false);
        attrN.setDefVal("@RDT");
        attrN.setUIIsEnable(false);
        attrN.setGroupID((int)gf.getOID());
        attrN.setIDX(3);
        attrN.Insert();

        /*
         * 判断是否是节点设置的审核分组，如果是就为节点设置焦点字段。
         */
        frmID = frmID.replace("ND", "");
        int nodeid = 0;
        try
        {
            nodeid = Integer.parseInt(frmID);
        }
        catch(Exception e)
        {
            //转化不成功就是不是节点表单字段.
            return "error:只能节点表单才可以使用审核分组组件。";
        }
        return null;
    }
}