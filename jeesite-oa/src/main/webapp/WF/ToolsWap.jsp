<%@page import="java.math.BigDecimal"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="BP.Port.*"%>
<%@page import="BP.WF.Port.AuthorWay"%>
<%@page import="BP.WF.Port.AlertWay"%>
<%@page import="BP.DA.*"%>
<%@page import="BP.WF.Template.*"%>
<%@page import="BP.WF.Template.AccepterRole.*"%>
<%@page import="BP.WF.Template.CC.*"%>
<%@page import="BP.WF.Template.*"%>
<%@page import="BP.WF.Node"%>
<%@page import="BP.WF.Nodes"%>
<%@page import="BP.WF.Flow"%>
<%@page import="BP.WF.Flows"%>
<%@page import="BP.WF.Template.FindWorker.*"%>
<%@page import="BP.WF.DTS.*"%>
<%@page import="BP.Web.WebUser"%>
<%@page import="BP.Sys.*"%>
<%@page import="BP.En.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path;
	String DoType = request.getParameter("DoType");
	String RefNo = request.getParameter("RefNo");
	String PageID="ToolsWap";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="<%=basePath%>/WF/Comm/JS/Calendar/WdatePicker.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="<%=basePath%>/WF/Comm/JS/Calendar/jquery-1.3.1.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/WF/Comm/JS/Calendar/jquery.bitmapcutter.js"></script>
<link rel="Stylesheet" type="text/css"
	href="<%=basePath%>/WF/Comm/CSS/jquery.bitmapcutter.css" />
<link href='<%=basePath%>/WF/Comm/Style/Table0.css' rel='stylesheet'
	type='text/css' />
<script src="<%=basePath%>/WF/Comm/JS/Calendar/jquery.Jcrop.js"></script>
<link rel="stylesheet" href="<%=basePath%>/WF/Comm/CSS/demos.css" type="text/css" />
<link rel="stylesheet" href="<%=basePath%>/WF/Comm/CSS/jquery.Jcrop.css" type="text/css" />
<!-- 抽离css -->
<link rel="stylesheet" href="<%=basePath%>/WF/Style/ToolsWap.css" type="text/css" />
<script language="JavaScript" src="<%=basePath%>/WF/Comm/JScript.js"></script>
<!-- 抽离js -->
<script language="JavaScript" src="<%=basePath%>/WF/Comm/JS/ToolsWap.js"></script>
<script type="text/javascript">
var url_img = '<%=basePath%>'+"/DataUser/UserIcon/DefaultX.png";
//$('#target').attr("src",url_img);
//$("#img").attr("src",url_img);
/* 定义JCROP */
jQuery(function($){
    // The variable jcrop_api will hold a reference to the
    // Jcrop API once Jcrop is instantiated.
    var g = document.getElementById("target");
    // 具有较好的通用性
    //var real= document.getElementById(id).height || document.getElementById(id).style.height || document.getElementById(id).offsetHeight;
     var ImgObj = new Image(); //判断图片是否存在  
     ImgObj.src = g.src;  
    if (ImgObj.fileSize > 0 || (ImgObj.width > 0 && ImgObj.height > 0)) {  
    	//alert("存在"); 
	} else {  
		$('#target').attr("src",url_img);
	    $("#img").attr("src",url_img);
	}
    var jcrop_api,
	    boundx,
        boundy,
		$preview = $('#preview-pane'),
        $pcnt = $('#preview-pane .preview-container'),
        $pimg = $('#preview-pane .preview-container img'),
		xsize = $pcnt.width(),
        ysize = $pcnt.height();
    // In this example, since Jcrop may be attached or detached
    // at the whim of the user, I've wrapped the call into a function
    initJcrop();
	
	// Grab some information about the preview pane
        
    
    // The function is pretty simple
    function initJcrop()//{{{
    {
    	//setTimeout("",4000);
      // Hide any interface elements that require Jcrop
      // (This is for the local user interface portion.)
      //$('.requiresjcrop').hide();

      // Invoke Jcrop in typical fashion
      $('#target').Jcrop({
        //onRelease: releaseCheck,
		onChange: updatePreview,
        onSelect: updatePreview,
        aspectRatio: 1
      },function(){

        /*jcrop_api = this;
        jcrop_api.animateTo([100,100,400,300]);

        // Setup and dipslay the interface for "enabled"
        $('#can_click,#can_move,#can_size').attr('checked','checked');
        $('#ar_lock,#size_lock,#bg_swap').attr('checked',false);
        $('.requiresjcrop').show();*/
		// Use the API to get the real image size
		  var bounds = this.getBounds();
		  boundx = bounds[0];
		  boundy = bounds[1];
		  // Store the API in the jcrop_api variable
		  jcrop_api = this;

		  // Move the preview into the jcrop container for css positioning
		  $preview.appendTo(jcrop_api.ui.holder);

      });
	function updatePreview(c)
    {
      if (parseInt(c.w) > 0)
      {
        var rx = xsize / c.w;
        var ry = ysize / c.h;
		console.log(boundx);
        $pimg.css({
          width: Math.round(rx * boundx) + 'px',
          height: Math.round(ry * boundy) + 'px',
          marginLeft: '-' + Math.round(rx * c.x) + 'px',
          marginTop: '-' + Math.round(ry * c.y) + 'px'
        });
		$("#x").val(c.x);
		$("#y").val(c.y);
		$("#w").val(c.w);
		$("#h").val(c.h);
      }
    };
    };
    /* 图片切换 */
    $('#btnImg').click(function(){
		//$(this).addClass('active').closest('.btn-group')
        //.find('button.active').not(this).removeClass('active');
	    $("#img").attr("src",$('#imgx').val());
		$("#target").attr("src",$('#imgx').val());
		$("#img").attr("style","");
		$preview = $('#preview-pane');
	    $pcnt = $('#preview-pane .preview-container');
	    $pimg = $('#preview-pane .preview-container img');
		xsize = $pcnt.width();
	    ysize = $pcnt.height();
		jcrop_api.destroy();
		jcrop_api.setImage($('#imgx').val());//sago
		initJcrop();
	    return false;
	});
  });
</script>

<script type="text/javascript">
/* $('#element_id').Jcrop();
jQuery(function($){

    // I did JSON.stringify(jcrop_api.tellSelect()) on a crop I liked:
    var c = {"x":13,"y":7,"x2":487,"y2":107,"w":474,"h":100};

    $('#target').Jcrop({
      bgFade: true,
      setSelect: [c.x,c.y,c.x2,c.y2]
    });

  }); */
	function SetSelected(cb,ids) {
		var arrmp = ids.split(',');
		var arrObj = document.all;
		var isCheck = false;
		if (cb.checked)
			isCheck = true;
		else
			isCheck = false;
		for (var i = 0; i < arrObj.length; i++) {
			if (typeof arrObj[i].type != "undefined"
					&& arrObj[i].type == 'checkbox') {
				for (var idx = 0; idx <= arrmp.length; idx++) {
					if (arrmp[idx] == '')
						continue;
					var cid = arrObj[i].name + ',';
					var ctmp = arrmp[idx] + ',';
					if (cid.indexOf(ctmp) > 1) {
						arrObj[i].checked = isCheck;
						//                    alert(arrObj[i].name + ' is checked ');
						//                    alert(cid + ctmp);
					}
				}
			}
		}
	}
	/* $(function() {
		try {
			var url = '';
			var width = document
					.getElementById("ContentPlaceHolder1_Tools1_ToolsWap1_WSize").value;
			var height = document
					.getElementById("ContentPlaceHolder1_Tools1_ToolsWap1_HSize").value;
			var cwidth = document
					.getElementById("ContentPlaceHolder1_Tools1_ToolsWap1_Chg").value;
			var name = document
					.getElementById("ContentPlaceHolder1_Tools1_ToolsWap1_ImageName").value;
			if (name) {
				url = '/DataUser/UserIcon/' + name;
				var cw = width / 2;
				var ch = width / 2;
				$.fn.bitmapCutter({
					src : url,
					renderTo : '#Container',
					holderSize : {
						width : width,
						height : height
					},
					cutterSize : {
						width : 200,
						height : 200
					},
					onGenerated : function(src) {
						alert(src);
					},
					rotateAngle : 90,
					lang : {
						clockwise : '顺时针旋转{0}度.'
					}
				});
			}
		} catch (e) {

		}
	}); */
</script>
</head>
<body>
	<form method="post" action="" id="form1">
		<%
			if (WebUser.getIsWap() && RefNo == null) {
				BP.WF.XML.Tools tools = new BP.WF.XML.Tools();
				tools.RetrieveAll();
		%>
		<fieldset>
			<legend>
				&nbsp;<a href='Home.jsp'><img src='../Img/Home.gif' border=0 />Home</a>&nbsp;
			</legend>
			<ul>
				<%
					for (BP.WF.XML.Tool tool : tools.ToJavaList()) {
				%>
				<li><a href="<%=PageID%>.jsp?RefNo=<%=tool.getNo()%>"
					target="_self"><%=tool.getName()%></a></li>
				<%
					}
				%>
			</ul>
		</fieldset>
		<%
			return;
			}
			if ("AthFlows".equals(RefNo)) {
				FlowSorts sorts = new FlowSorts();
				sorts.RetrieveAll();
				Flows fls = new Flows();
				fls.RetrieveAll();
				BP.WF.Port.WFEmp emp = new BP.WF.Port.WFEmp(WebUser.getNo());
		%>
		<table class='Table' cellpadding='2' cellspacing='2'>
			<caption>授权流程范围</caption>
			<tr>
				<th>IDX</th>
				<th>类别</th>
				<th>流程</th>
			</tr>
			<%
				int i = 0;
						for (FlowSort sort : sorts.ToJavaListFs()) {
							String ctlIDs = "";
							i++;
			%>
			<tr class="TRSum">
				<td class="Idx" nowrap><%=i%></td>
				<td nowrap="nowrap"><b><%=sort.getName()%></b></td>
				<td><input type="checkbox" name="CB_d<%=sort.getNo()%>"
					id="typeId" onclick="SetSelected(this,'<%=ctlIDs%>')">选择类别下全部</td>
			</tr>
			<%
				for (Flow fl : fls.ToJavaList()) {
								if (!fl.getFK_FlowSort().equals(sort.getNo()))
									continue;
								i++;
			%>
			<tr>
				<td class="Idx" nowrap><%=i%></td>
				<td nowrap>&nbsp;</td>
				<%
					ctlIDs = "CB_" + fl.getNo() + ",";
										if (emp.getAuthorFlows().contains(fl.getNo())) {
				%>
				<td><input type="checkbox" checked="checked"
					name="CB_<%=fl.getNo()%>" id="CB_<%=fl.getNo()%>"><%=fl.getName()%></td>
				<%
					} else {
				%>
				<td><input type="checkbox" name="CB_<%=fl.getNo()%>"
					id="CB_<%=fl.getNo()%>"><%=fl.getName()%></td>
				<%
					}
				%>
				<%
					//System.out.println("CB_" + fl.getNo());
				%>
			</tr>
			<%
				}
						}
			%>

			<tr>
				<th>&nbsp;</th>
				<td colspan="2"><input type="button" value="Save" id="Btn_Save"
					class="Btn" onclick="btnSaveAthFlows_Click()"></td>
			</tr>
		</table>
		<%
			} else if ("Skin".equals(RefNo)) {
				String pageID = PageID;
				String setNo = request.getParameter("SetNo");
				if (setNo != null) {
					BP.WF.Port.WFEmp em = new BP.WF.Port.WFEmp(WebUser.getNo());
					em.setStyle(setNo);
					em.Update();
					WebUser.setStyle(setNo);
					//response.sendedirect(pageID + ".jsp?RefNo=Skin");
					return;
				}
		%>
		<fieldset width='100%'>
			<legend>&nbsp;风格设置&nbsp;</legend>
			<%
				BP.WF.XML.Skins sks = new BP.WF.XML.Skins();
						sks.RetrieveAll();
			%>
			<ul>
				<%
					for (BP.WF.XML.Skin item : sks.ToJavaList()) {
				%>
				<li><a
					href="<%=pageID%>.jsp?RefNo=Skin&SetNo=<%=item.getNo()%>"><%=item.getName()%>&nbsp;&nbsp;<span
						style='background:<%=item.getCSS()%>'><i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</i></span></a></li>
				<%
					}
				%>
			</ul>
			<input type="button" class="Btn" value="Save" id="Btn_Save">
		</fieldset>
		<%
			} else if ("MyWorks".equals(RefNo)) {
				Flows fls = new Flows();
				fls.RetrieveAll();
				Nodes nds = new Nodes();
				nds.RetrieveAll();
		%>
		<table class='Table' cellpadding='2' cellspacing='2'>
			<tr>
				<th>IDX</th>
				<th>流程</th>
				<th>节点</th>
				<th>查询</th>
			</tr>
			<%
				int idx = 0;
						for (Flow fl : fls.ToJavaList()) {
							boolean isHave = false;
							Nodes mynds = new Nodes();
							for (Node nd : nds.ToJavaList()) {
								if (nd.getFK_Flow() != fl.getNo())
									continue;
								if (nd.getNodeEmps().Contains(WebUser.getNo()))
									mynds.AddEntity(nd);
							}
							if (mynds.size() == 0)
								continue;
							boolean isFirst = true;
							for (Node mynd : mynds.ToJavaList()) {
								if (isFirst) {
			%>
			<tr class='TRSum'>
				<%
					} else {
				%>
			
			<tr>
				<%
					}
				%>
				<td class='Idx' nowrap><%=idx%></td>
				<%
					if (isFirst) {
				%>
				<td nowrap><%=mynd.getFlowName()%></td>
				<%
					} else {
				%>
				<td nowrap>&nbsp;</td>
				<%
					}
				%>
				<td nowrap><%=mynd.getName()%></td>
				<td nowrap><a
					href="javascript:WinOpen('FlowSearchSmallSingle.jsp?FK_Node=<%=mynd.getNodeID()%>');">工作查询</a></td>
			</tr>
			<%
				idx++;
								isFirst = false;
							}
						}
			%>
		</table>

		<%
			} else if ("Siganture".equals(RefNo)) {
				String path1 = BP.Sys.SystemConfig.getPathOfDataUser()
						+ "\\Siganture\\T.jpg";
				File file = new File(path1);
				if (DoType != null || !file.exists()) {
					String pathMe = BP.Sys.SystemConfig.getPathOfDataUser()
							+ "\\Siganture\\" + WebUser.getNo() + ".jpg";
					GenerSiganture.fileChannelCopy(
							new File(SystemConfig.getPathOfDataUser()
									+ "\\Siganture\\Templete.jpg"), new File(
									path1));
					String fontName = "宋体";
					if (DoType.equals("ST")) {
						fontName = "宋体";
					} else if (DoType.equals("LS")) {
						fontName = "隶书";
					}

					GenerSiganture.writeImageLocal(pathMe, GenerSiganture
							.modifyImage(GenerSiganture.loadImageLocal(path1),
									WebUser.getName(), 90, 90));
					GenerSiganture.fileChannelCopy(new File(pathMe), new File(
							SystemConfig.getPathOfDataUser() + "\\Siganture\\"
									+ WebUser.getName() + ".jpg"));
				}
				if (WebUser.getIsWap()) {
		%>
		<fieldset width='100%'>
			<legend>
				&nbsp;<a href="Home.jsp"><img src='../Img/Home.gif' border="0">主页</a>-<a
					href='<%=PageID%>.jsp'>设置</a>-电子签名设置<%-- <%=WebUser.getAuth()%> --%>
				&nbsp;
			</legend>
			<%
				} else {
			%>
			<fieldset width='100%'>
				<legend>电子签名设置
				</legend>
				<%
					}
				%>
				<p align=center>
					<img
						src='<%=basePath%>/DataUser/Siganture/<%=WebUser.getNo()%>.jpg'
						border=1
						onerror="this.src='<%=basePath%>/DataUser/Siganture/UnName.jpg'" />
				</p>
				上传<input type="file" id="F" name="file"
					onchange="checkImgType(this)"> <input type="button"
					value="确定" onclick="btn_Siganture_Click('<%=basePath%>')">
				<hr>
				<b>利用扫描仪设置步骤:</b>
				<ul>
					<li>在白纸上写下您的签名</li>
					<li>送入扫描仪扫描，并得到jpg文件。</li>
					<li>利用图片处理工具把他们处理缩小到 90*30像素大小。</li>
				</ul>
				<b>手写设置:</b>
				<ul>
					<li>启动画板程序，写下您的签名。</li>
					<li>保存成.jpg文件，设置文件为90*30像素大小。</li>
				</ul>
				<b>让系统自动为您创建（请选择字体）:</b>
				<ul>
					<li><a href='<%=PageID%>.jsp?RefNo=Siganture&DoType=ST'>宋体</a></li>
					<li><a href='<%=PageID%>.jsp?RefNo=Siganture&DoType=LS'>隶书</a></li>
				</ul>
			</fieldset>
			<%
				}
					if ("AdminSet".equals(RefNo)) {
			%>
			<fieldset width='100%'>
				<legend>&nbsp;系统设置&nbsp;</legend>
				<table class='Table' cellpadding='2' cellspacing='2'>
					<tr>
						<th>项目</th>
						<th>项目值</th>
						<th>描述</th>
					</tr>
					<tr>
						<td nowrap>标题图片</td>
						<td nowrap><input type="file"></td>
						<td nowrap>系统顶部的标题图片</td>
					</tr>
					<tr>
						<td nowrap>ftp URL</td>
						<td><input type="text" width="200" id="TB_FtpUrl"></td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td nowrap>&nbsp;</td>
						<td><input type="button" value="OK"
							onclick="btn_AdminSet_Click('<%=basePath%>')"></td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td nowrap><a
							href="javascript:WinOpen('../Comm/Sys/EditWebConfig.jsp')">System
								Setting</a>-<a href="javascript:WinOpen('../OA/FtpSet.jsp')">FTP
								Services</a>-<a
							href="javascript:WinOpen('/WF/Comm/Ens.jsp?EnsName=BP.OA.Links')">Link</a></td>
						<td nowrap>&nbsp;</td>
					</tr>
				</table>
			</fieldset>
			<%
				} else if ("AutoLog".equals(RefNo)) {
						String sql = "";

						switch (SystemConfig.getAppCenterDBType()) {
						case Oracle:
							sql = "SELECT a.No || a.Name as Empstr,AuthorDate, a.No,AuthorToDate FROM WF_Emp a WHERE Author='"
									+ WebUser.getNo() + "' AND AuthorWay >= 1";
							break;
						default:
							sql = "SELECT a.No + a.Name as Empstr,AuthorDate, a.No ,AuthorToDate FROM WF_Emp a WHERE Author='"
									+ WebUser.getNo() + "' AND AuthorWay >= 1";
							break;
						}

						DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
						if (dt.Rows.size() == 0) {
							if (WebUser.getIsWap()) {
			%>
			<fieldset width='100%'>
				<legend>
					&nbsp;<a href="Home.jsp"><img src='/WF/Img/Home.gif' border="0">主页</a>-<a
						href='<%=PageID%>.jsp'>设置</a>-密码修改&nbsp;
				</legend>
				<br>
				<fieldset width='100%'>
					<legend>&nbsp;提示&nbsp;</legend>
					<div style="margin-left:30px;margin-top:20px;font-size:14px;"><img src="<%=basePath %>/WF/Img/info.png" align="middle">没有同事授权给您，您不能使用授权方式登陆。</div>。
				</fieldset>
			</fieldset>
			<%
				} else {
			%>
			<fieldset width='100%'>
				<legend>&nbsp;提示&nbsp;</legend>
				<div style="margin-left:30px;margin-top:20px;font-size:14px;"><img src="<%=basePath %>/WF/Img/info.png" align="middle">没有同事授权给您，您不能使用授权方式登陆。</div>
			</fieldset>
			<%
				}
							return;
						}

						if (WebUser.getIsWap()) {
			%>
			<fieldset width='100%'>
				<legend>
					&nbsp;<a href="Home.jsp"><img src='/WF/Img/Home.gif' border="0">主页</a>-<a
						href='<%=PageID%>.jsp'>设置</a>-下列同事授权给您&nbsp;
				</legend>
				<%
					} else {
				%>
				<fieldset width='100%'>
					<legend>&nbsp;下列同事授权给您&nbsp;</legend>
					<%
						}
					%>
					<ul>
						<%
							for (DataRow dr : dt.Rows) {
						%>
						<li><a href="javascript:LogAs('<%=dr.getValue(2)%>')">授权人:<%=dr.getValue("No")%></a>
							- 授权日期:<%=dr.getValue("AuthorDate")%>，有效日期：<%=dr.getValue("AuthorToDate")%></li>
						<%
							}
						%>
					</ul>
				</fieldset>
				<%
					} else if ("Password".equals(RefNo)) {
								if (WebUser.getIsWap()) {
				%>
				<fieldset width='100%'>
					<legend>
						&nbsp;<a href="Home.jsp"><img src='/WF/Img/Home.gif'
							border="0">主页</a>-<a href='<%=PageID%>.jsp'>设置</a>-密码修改&nbsp;
					</legend>
					<%
						} else {
					%>
					<fieldset width='100%'>
						<legend>&nbsp;密码修改&nbsp;</legend>
						<%
							}
						%>
						<br>
						<table border=0 width=80% align=center>
							<tr>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
							</tr>
							<tr>
								<td nowrap>旧密码：</td>
								<td><input type="password" id="TB_Pass1"></td>
							</tr>
							<tr>
								<td nowrap>新密码：</td>
								<td><input type="password" id="TB_Pass2"></td>
							</tr>
							<tr>
								<td nowrap>重输新密码：</td>
								<td><input type="password" id="TB_Pass3"></td>
							</tr>
							<tr>
								<td nowrap>&nbsp;</td>
								<td><input type="button" onclick="btn_Click('<%=basePath %>')" value="确定"></td>
							</tr>
						</table>
						<br>
					</fieldset>
					<%
						} else if ("Profile".equals(RefNo)) {
										BP.WF.Port.WFEmp emp = new BP.WF.Port.WFEmp(WebUser.getNo());
										if (WebUser.getIsWap()) {
					%>
					<fieldset width='100%'>
						<legend>
							&nbsp;<a href="Home.jsp"><img src='/WF/Img/Home.gif'
								border="0">主页</a>-<a href='<%=PageID%>.jsp'>设置</a>-基本信息
						</legend>
						<%
							} else {
						%>
						<fieldset width='100%'>
							<legend>
								&nbsp;基本信息
							</legend>
							<%
								}
							%>
							<br>
							<table border=0 width='80%' align=center>
								<tr>
									<td nowrap>手机</td>
									<td><input type="text" id="TB_Tel"
										value="<%=emp.getTel()%>"></td>
								</tr>
								<tr>
									<td nowrap>Email</td>
									<td><input type="text" id="TB_Email"
										value="<%=emp.getEmail()%>"></td>
								</tr>
								<tr>
									<td nowrap>QQ/RTX/MSN</td>
									<td><input type="text" id="TB_TM"
										value="<%=emp.getEmail()%>"></td>
								</tr>
								<tr>
									<td nowrap>信息接收方式</td>
									<td><select id="DDL_Way">
											<%
												Paras ps = new Paras();
																						String sql = "";
																						sql = "select Lab,IntKey from sys_enum where EnumKey='AlertWay'";
																						ps.SQL = sql;
																						DataTable dt = DBAccess.RunSQLReturnTable(ps);
																						for (DataRow dr : dt.Rows) {
																							String sel = "";
																							if (emp.getHisAlertWayT().equals(
																									dr.getValue("Lab").toString())) {
																								sel = "selected=\"selected\"";
																							}
											%>
											<option value="<%=dr.getValue("IntKey").toString()%>"
												<%=sel%>><%=dr.getValue("Lab").toString()%></option>
											<%
												}
											%>
									</select></td>
								</tr>
								<tr>
									<td nowrap colspan=2 align=center><input type="button"
										onclick="btn_Profile_Click('<%=basePath%>')" value="保存"></td>
								</tr>
							</table>
							<br>
						</fieldset>
						<%
							} else if ("Auto".equals(RefNo)) {
												String sql = "SELECT a.No,a.Name,b.Name as DeptName FROM Port_Emp a, Port_Dept b WHERE a.FK_Dept=b.No AND a.FK_Dept LIKE '"
														+ WebUser.getFK_Dept() + "%' ORDER  BY a.FK_Dept ";
												DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
												if (WebUser.getIsWap()) {
						%>
						<fieldset width='100%'>
							<legend>
								&nbsp;<a href="Home.jsp"><img src='/WF/Img/Home.gif'
									border="0">Home</a>-<a href='<%=PageID%>.jsp'>设置</a>-请选择您要授权的人员&nbsp;
							</legend>
							<%
								} else {
							%>
							<fieldset width='100%'>
								<legend>&nbsp;请选择您要授权的人员&nbsp;</legend>
								<%
									}
																String deptName = null;
								%>
								<br>
								<table width='80%' align=center border=1>
									<tr>
										<th>编号</th>
										<th>部门</th>
										<th>要执行授权的人员</th>
									</tr>
									<%
										int idx = 0;
																		for (DataRow dr : dt.Rows) {
																			String fk_emp = dr.getValue("No").toString();
																			if (fk_emp.equals("admin")
																					|| fk_emp.equals(WebUser.getNo()))
																				continue;
																			idx++;
																			if (!dr.getValue("DeptName").toString().equals(deptName)) {
																				deptName = dr.getValue("DeptName").toString();
									%>
									<tr class='TRSum'>
										<td class='Idx' nowrap><%=idx%></td>
										<td nowrap><%=deptName%></td>
										<%
											} else {
										%>
									
									<tr>
										<td class='Idx' nowrap><%=idx%></td>
										<td>&nbsp;</td>
										<%
											}
																					String str = BP.WF.Glo.DealUserInfoShowModel(fk_emp, dr
																							.getValue("Name").toString());
										%>
										<td nowrap><a
											href="<%=PageID%>.jsp?RefNo=AutoDtl&FK_Emp=<%=fk_emp%>"><%=str%></a></td>
									</tr>
									<%
										}
									%>
								</table>
								<br>
							</fieldset>
							<%
								} else if ("AutoDtl".equals(RefNo)) {
														if (WebUser.getIsWap()) {
							%>
							<fieldset width='100%'>
								<legend>
									&nbsp;<a href="Home.jsp"><img src='/WF/Img/Home.gif'
										border="0">Home</a>-<a href='<%=PageID%>.jsp'>设置</a>-授权详细信息&nbsp;
								</legend>
								<%
									} else {
								%>
								<fieldset width='100%'>
									<legend>&nbsp;授权详细信息&nbsp;</legend>
									<%
										}
																		BP.WF.Port.WFEmp emp = new BP.WF.Port.WFEmp(WebUser.getNo());
																		BP.WF.Port.WFEmp empAu = new BP.WF.Port.WFEmp(
																				request.getParameter("FK_Emp"));
									%>
									<br>
									<table class='Table' cellpadding='2' cellspacing='2'>
										<tr>
											<th>项目</th>
											<th>内容</th>
										</tr>
										<tr>
											<td nowrap>授权给:</td>
											<td nowrap><%=empAu.getNo()%> <%=empAu.getName()%></td>
										</tr>
										<tr>
											<td nowrap="nowrap">收回授权日期:</td>
											<%
												Calendar c = Calendar.getInstance();
																						c.setTime(new Date()); //设置当前日期
																						c.add(Calendar.DATE, 14); //日期加1
																						Date dtNow = c.getTime();
											%>
											<td><input type="text" id="TB_DT"
												value="<%=DataType.dateToStr(dtNow, "yyyy-MM-dd HH:mm")%>"
												onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});"></td>
										</tr>
										<tr>
											<td nowrap="nowrap">授权方式</td>
											<td><select id="DDL_AuthorWay">
													<%
														Paras ps = new Paras();
																										String sql = "";
																										sql = "select Lab,IntKey from sys_enum where EnumKey='AuthorWay'";
																										ps.SQL = sql;
																										DataTable dt = DBAccess.RunSQLReturnTable(ps);
																										for (DataRow dr : dt.Rows) {
																											String sel = "";
																											if (emp.getAuthorWay() == Integer.parseInt(dr.getValue(
																													"IntKey").toString())) {
																												sel = "selected=\"selected\"";
																											}
													%>
													<option value="<%=dr.getValue("IntKey").toString()%>"
														<%=sel%>><%=dr.getValue("Lab").toString()%></option>
													<%
														}
													%>
											</select></td>
										</tr>
										<tr>
											<td colspan="1"><b><a
													href="javascript:WinShowModalDialog('ToolsWap.jsp?RefNo=AthFlows&d=<%=new Date()%>')">设置要授权的流程范围</a></b></td>
											<td nowrap="nowrap" colspan="1"><input type="button"
												class="Btn" id="Btn_Save" value="保存"
												onclick="btnSaveIt_Click()"></td>
										</tr>
										<tr>
											<TD class='BigDoc' colspan="2" valign="top">说明:在您确定了收回授权日期后，被授权人不能再以您的身份登录，<br>如果未到指定的日期您可以取回授权。
											</TD>
										</tr>
									</table>
									<br>
								</fieldset>
								<%
									} else if ("Times".equals(RefNo)) {
																if (request.getParameter("FK_Node").toString() != null) {
																	int nodeid = Integer.parseInt(request
																			.getParameter("FK_Node"));
																	Node nd = new Node(nodeid);
								%>
								<table class='Table' cellpadding='2' cellspacing='2'>
									<Caption>
										<a
											href='<%=PageID%>.jsp?DoType=Times&FK_Flow=<%=nd.getFK_Flow()%>'><%=nd.getFlowName()%></a>
										=><%=nd.getName()%></Caption>
									<tr>
										<th>IDX</th>
										<th>人员</th>
										<th>Average time</th>
										<th>Participation times</th>
									</tr>
								</table>
								<%
									return;
																}
																if (request.getParameter("FK_Flow").toString() != null) {
																	// this.BindTimesFlow();
																	return;
																}

																FlowSorts sorts = new FlowSorts();
																sorts.RetrieveAll();

																Flows fls = new Flows();
																fls.RetrieveAll();

																Nodes nds = new Nodes();
																nds.RetrieveAll();
								%>
								<table class='Table' cellpadding='2' cellspacing='2'>
									<%
										for (FlowSort sort : sorts.ToJavaListFs()) {
									%>
									<tr class='TRSum'>
										<td nowrap="nowrap"><b><%=sort.getName()%></b></td>
										<td nowrap>&nbsp;</td>
										<td nowrap>&nbsp;</td>
										<td nowrap>&nbsp;</td>
										<td nowrap>&nbsp;</td>
										<td nowrap>&nbsp;</td>
									</tr>
									<%
										for (Flow fl : fls.ToJavaList()) {
																				if (!sort.getNo().equals(fl.getFK_FlowSort()))
																					continue;
									%>
									<tr class='TRSum'>
										<td nowrap>&nbsp;</td>
										<td nowrap="nowrap"><b><%=fl.getName()%></b></td>
										<td nowrap>工作数</td>
										<td nowrap>平均天<%=fl.getAvgDay()%></td>
										<td nowrap>我参与的工作数</td>
										<td nowrap>工作总数</td>
									</tr>
									<%
										BigDecimal avgDay = new BigDecimal(0);
																				for (Node nd : nds.ToJavaList()) {
																					if (!nd.getFK_Flow().equals(fl.getNo()))
																						continue;
									%>
									<tr>
										<td nowrap>&nbsp;</td>
										<td nowrap><%=nd.getName()%></td>
										<%
											String sql = "";
																							sql = "SELECT  COUNT(*) FROM ND" + nd.getNodeID();
																							int num = DBAccess.RunSQLReturnValInt(sql);
										%>
										<td nowrap><%=num%></td>
										<%
											sql = "SELECT AVG( DateDiff(d, cast(RDT as datetime),  cast(CDT as datetime) ) ) FROM ND"
																									+ nd.getNodeID();
																							BigDecimal day = DBAccess.RunSQLReturnValDecimal(
																									sql, new BigDecimal(0), 2);
																							avgDay.add(day);
										%>
										<td nowrap><%=day.setScale(2, BigDecimal.ROUND_HALF_UP)%></td>
										<td nowrap>无效</td>
									</tr>
									<%
										}
																				if (avgDay.intValue() != fl.getAvgDay()) {
																					fl.setAvgDay(avgDay);
																					fl.Update();
																				}
																			}
																		}
									%>
								</table>
								<%
									} else if ("FtpSet".equals(RefNo)) {
								%>
								<fieldset width='100%'>
									<legend>&nbsp;ftp setting&nbsp;</legend>
									<Table class='Table' cellpadding='2' cellspacing='2'>
										<tr>
											<th>&nbsp;</th>
											<th>&nbsp;</th>
											<th>&nbsp;</th>
										</tr>
										<tr>
											<td nowrap>用户名</td>
											<td nowrap><input type="text" id="TB_UserNo"></td>
											<td nowrap>&nbsp;</td>
										</tr>
										<tr>
											<td nowrap>密码</td>
											<td nowrap><input type="text" id="TB_Pass1"></td>
											<td nowrap>&nbsp;</td>
										</tr>
										<tr>
											<td nowrap>&nbsp;</td>
											<td nowrap><input type="button" id="TB_Pass1" value="确定"
												onclick="btn_Click('<%=basePath %>')"></td>
											<td nowrap>&nbsp;</td>
										</tr>
									</Table>
								</fieldset>
								<%
									} else if ("PerPng".equals(RefNo)) {
																if (WebUser.getIsWap()) {
								%>
								<fieldset width='100%'>
									<legend>
										&nbsp;<a href="Home.jsp"><img src='../Img/Home.gif'
											border="0">主页</a>-<a href='<%=PageID%>.jsp'>设置</a>-图标信息&nbsp;
									</legend>
									<%
										} else {
									%>
									<fieldset width='100%'>
										<legend>&nbsp;图标信息&nbsp;</legend>
										<%
											}
																				String picfile = SystemConfig.getPathOfWebApp()
																						+ "DataUser\\UserIcon";
										%>
										<hr>
										<br> <b>Icon设置：<a
											href='<%=PageID%>.jsp?RefNo=BitmapCutter'>设置/图标设置</a></b>
										<hr>
										<br>
										<%
											boolean isNo = false;

																				// 							            System.Drawing.Image myImage = null;
																				// 							            int phWidth = 0;
																				// 							            int phHeight = 0;
																				// 							            DirectoryInfo di = new DirectoryInfo(picfile);
																				// 							            if (di.Exists)
																				// 							            {
																				// 							                string[] subpic = Directory.GetFiles(picfile);
																				// 							                if (subpic.Length > 0)
																				// 							                {
																				// 							                    for (String PicPath : subpic)
																				// 							                    {
																				// 							                        string[] tempFilePath = PicPath.Split('\\');

																				// 							                        if (tempFilePath[tempFilePath.Length - 1].StartsWith(WebUser.No + "BigerCon"))
																				// 							                        {
																				// 							                            myImage = System.Drawing.Image.FromFile(picfile + "/" + WebUser.No + "BigerCon.png");
																				// 							                            phWidth = myImage.Width;
																				// 							                            phHeight = myImage.Height;

																				// 							                            if (phWidth > 510)
																				// 							                            {
																				// 							                                isNo = false;
																				// 							                            }
																				// 							                            else
																				// 							                            {
																				// 							                                isNo = true;
																				// 							                            }
																				// 							                            myImage.Dispose();
																				// 							                            break;
																				// 							                        }
																				// 							                    }
																				// 							                }
																				// 							            }
																				if (!isNo) {
										%>
										<div id='Container'>
											<div id='Content'>
												<div id='Content-Left'>
													<img
														src='<%=basePath%>/DataUser/UserIcon/<%=WebUser.getNo()%>BigerCon.png'
														onerror="src='<%=basePath%>/DataUser/UserIcon/Default.png'"
														 />
														<!-- 添加默认显示图片，个别浏览器插件加载正常化-->
												</div>
												<div id='Content-Main2'>
													<img
														src='<%=basePath%>/DataUser/UserIcon/<%=WebUser.getNo()%>Smaller.png'
														onerror="src='<%=basePath%>/DataUser/UserIcon/Default.png'"
														width='40px' height='40px' />
												</div>
												<div id='Content-Main3'>32*32</div>
												<div id='Content-Main'>
													<img
														src='<%=basePath%>/DataUser/UserIcon/<%=WebUser.getNo()%>.png'
														onerror="src='<%=basePath%>/DataUser/UserIcon/Default.png'"
														width='60px' height='60px' />
												</div>
												<div id='Content-Main3'>60*60</div>
												<div id='Content-Main1'>
													<img
														src='<%=basePath%>/DataUser/UserIcon/<%=WebUser.getNo()%>Biger.png'
														onerror="src='<%=basePath%>/DataUser/UserIcon/Default.png'"
														width='100px' height='100px' />
												</div>
												<div id='Content-Main3'>100*100</div>
											</div>
										</div>
										<%
											} else {
										%>
										<div id='Container'>
											<div id='Content'>
												<div id='Content-Left'
													style='border: solid 1px #7d9edb; padding: 1px;'></div>
												<div id='Content-Main2'>
													<img src='<%=basePath%>/DataUser/UserIcon/Default.png'
														border=1 width='32px' height='32px' />
												</div>
												<div id='Content-Main3'>32*32</div>
												<div id='Content-Main'>
													<img src='<%=basePath%>/DataUser/UserIcon/Default.png'
														border=1 width='60px' height='60px' />
												</div>
												<div id='Content-Main3'>60*60</div>
												<div id='Content-Main1'>
													<img src='<%=basePath%>/DataUser/UserIcon/Default.png'
														border=1 width='100px' height='100px' />
												</div>
												<div id='Content-Main3'>100*100</div>
											</div>
										</div>
										<%
											}
										%>
										<br>
									</fieldset>
									<%
										} else if ("BitmapCutter".equals(RefNo)) {
																		if (WebUser.getIsWap()) {
									%>
									<fieldset width='100%'>
										<legend>
											&nbsp;<a href="Home.jsp"><img src='../Img/Home.gif'
												border="0">主页</a>-<a href='<%=PageID%>.jsp'>设置</a>-设置图标&nbsp;
										</legend>
										<%
											} else {
										%>
										<fieldset width='100%'>
											<legend>&nbsp;设置图标&nbsp;</legend>
											<div style="width: 800;height: 550">
												<div style="margin-left: 20px;margin-top: 20px">
														<input type="file" id="file1" name="UpFile" size="46" onchange="document.getElementById('imgx').value=getFullPath(this);"/><input type="button" id="btnImg" value="确定" name="btnOk""/>
												</div>
													<div class="container">
														<div class="row">
															<div class="span12">
																<div class="jc-demo-box">
																	<input id="imgx" type="hidden"/>
																	<input type="hidden" id="x" name="x" />
															        <input type="hidden" id="y" name="y" />
															        <input type="hidden" id="w" name="w" />
															        <input type="hidden" id="h" name="h" />
															        <input type="hidden" id="height" name="height" />
															        <input type="hidden" id="width" name="width" />
															        
																	<img src="<%=basePath%>/DataUser/UserIcon/<%=WebUser.getNo()%>BigerCon.png" id="target"  onerror="load(1);" alt="[Jcrop Example]" />
												
																	<div id="preview-pane">
																		<div class="preview-container">
																			<img id="img" src="<%=basePath%>/DataUser/UserIcon/<%=WebUser.getNo()%>BigerCon.png" class="jcrop-preview"
																				alt="Preview" name="imgNew" />
																		</div>
																		<div><input type="button" onclick="uploadImg('<%=basePath %>')" value="生成图标" name="btnSave"/></div>
																	</div>
																	
																	<div id="preview-pane">
																</div>
																
															</div>
														</div>
													</div>
											</div> 
											<%
												}
													// 											 string picfile = BP.SystemConfig.PathOfWebApp + "DataUser\\UserIcon";
													boolean isNo = false;

											%>
										</fieldset>
										<%
											} else if ("Per".equals(RefNo) || "".equals(RefNo)) {
												if (WebUser.getAuth() != null) {
										%>
										<fieldset width='100%'>
											<legend>&nbsp;提示&nbsp;</legend>
											<br> 您的登录是授权模式，您不能查看个人信息。
											<ul>
												<li><a
													href="javascript:ExitAuth('<%=WebUser.getAuth()%>')">退出授权模式</a></li>
												<li><a href="Tools.jsp">设置</a></li>
												<%
													if (WebUser.getIsWap()) {
												%>
												<li><a href="Home.jsp">返回主页</a></li>
												<%
													}
												%>
											</ul>
										</fieldset>
										<%
											return;
												}

												if (WebUser.getIsWap()) {
										%>
										<fieldset width='100%'>
											<legend>
												&nbsp;<a href="Home.jsp"><img src='../Img/Home.gif'
													border="0">主页</a>-<a href='<%=PageID%>.jsp'>设置</a>-基本信息<%=WebUser.getAuth()%>&nbsp;
											</legend>
											<%
												} else {
														if (WebUser.getAuth() == null) {
											%>
											<fieldset width='100%'>
												<legend> &nbsp;基本信息</legend>
												<%
													} else {
												%>
												<fieldset width='100%'>
													<legend>
														&nbsp;基本信息
													</legend>
													<%
														}
															}
													%>

													<p class="BigDoc">
														用户帐号:&nbsp;&nbsp;<font color="green"><%=WebUser.getNo()%></font>&nbsp;&nbsp;
														<br>用户名:&nbsp;&nbsp;<font color="green"><%=WebUser.getName()%></font>&nbsp;&nbsp;

													
													<hr>
													<b>电子签字:<img
														src='<%=basePath%>/DataUser/Siganture/<%=WebUser.getNo()%>.jpg'
														border="1"
														onerror="this.src='<%=basePath%>/DataUser/Siganture/UnName.jpg'" />
														，<a href='<%=PageID%>.jsp?RefNo=Siganture'>设置/修改</a>。
													</b> <br>
													<hr>
													主部门 : <font color="green"><%=WebUser.getFK_DeptName()%></font>
													<br> <br>
													<%
														BP.WF.Port.WFEmp au = new BP.WF.Port.WFEmp(WebUser.getNo());
														//au.setBasePath(basePath);
															if (!au.getAuthorIsOK()) {
													%>
													授权情况：未授权 - <a href='<%=PageID%>.jsp?RefNo=Auto'>执行授权</a>。
													<%
														} else {
																String way = "";
																if (au.getAuthorWay() == 1)
																	way = "全部授权";
																else
																	way = "指定流程范围授权";
													%>
													授权情况：授权给：<font color="green"><%=au.getAuthor()%></font>，授权日期:
													<font color="green"><%=au.getAuthorDate()%></font>，收回授权日期:<font
														color="green"><%=au.getAuthorToDate()%></font>。<br>我要:<a
														href="javascript:TakeBack('<%=au.getAuthor()%>')">取消授权</a>；授权方式：<font
														color="green"><%=way%></font>，<a
														href="<%=PageID%>.jsp?RefNo=AutoDtl&FK_Emp=<%=au.getAuthor()%>">我要修改授权信息</a>。
													<%
														}
													%>
													&nbsp;我要:<a href='<%=PageID%>.jsp?RefNo=Password'>修改密码</a> <br>
													<hr>
													<b>信息提示：</b><a href='<%=PageID%>.jsp?RefNo=Profile'>设置/修改</a>
													<br> <br>接受短消息提醒手机号 : <font color="green"><%=au.getTelHtml()%></font>
													<br> <br>接受E-mail提醒 : <font color="green"><%=au.getEmailHtml()%></font>
													<hr>
													<%
														Stations sts = WebUser.getHisStations();
													%>
													<b>岗位/部门-权限</b> <br> <br>岗位权限
													<%
 	for (Station st : Stations.convertStations(sts)) {
 %>
													- <font color="green"><%=st.getName()%></font>
													<%
														}
															Depts depts = WebUser.getHisDepts();
													%>
													<br> <br> 部门权限
													<%
 	for (Dept st : Depts.convertDepts(depts)) {
 %>
													- <font color="green"><%=st.getName()%></font>
													<%
														}
													%>
													</p>
												</fieldset>
												<%
													}
												%>
											
	</form>
</body>
<script>
function btnSaveIt_Click(){
	var FK_Emp='<%=request.getParameter("FK_Emp")%>';
	var TB_DT=document.getElementById("TB_DT").value;
	var DDL_AuthorWay=document.getElementById("DDL_AuthorWay").value;
	var url = "<%=basePath%>/WF/btnSaveIt_Click.do?FK_Emp=" + FK_Emp+ "&TB_DT=" + TB_DT + "&DDL_AuthorWay=" + DDL_AuthorWay;
	var url2 = "<%=basePath%>/WF/Tools.jsp";
	/*$("#form1").attr("action", url);
	$("#form1").submit();*/
	$.ajax({
		cache: true,
		type: "POST",
		url:url,
		data:$('#form1').serialize(),
	    success: function(data) {
	    	alert(data);
	    	window.location.href=url2;
	    	
	    }
	});
}
function btnSaveAthFlows_Click(){
	var url = "<%=basePath%>/WF/btnSaveAthFlows_Click.do?";
	$("#form1").attr("action", url);
	$("#form1").submit();
	alert("保存成功.");
}
</script>
</html>