<%@page import="cn.jflow.common.model.S1_TianxieShenqingDanModel"%>
<%@page import="BP.Web.WebUser"%>
<%@page import="BP.WF.Node"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="/WF/head/head1.jsp"%>
<%
	//String path = request.getContextPath();
	//String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	S1_TianxieShenqingDanModel stsd = new S1_TianxieShenqingDanModel(
			request, response);
	stsd.Page_Load();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
	function Btn_Send_Click(){
		$.ajax({
			cache: true,
			type: "POST",
			dataType : 'html',
			url:"<%=basePath%>sdkflowdemo/qingjia/Btn_Send_Click.do",
			data:$('#form1').serialize(),
		    success: function(data) {
		    	//alert(data);
		    	$("#mess").empty();
				
		    	$("#mess").append(data);
		    	var colors='blue';
		    	if(data.indexOf(colors) >= 0){
		    		$("#Btn_Save").attr('disabled',"true");
		    		$("#Btn_Send").attr('disabled',"true");
		    	}
		    }
		});
	}
	
	function Btn_Save_Click(){
		
		$.ajax({
			cache: true,
			type: "POST",
			dataType : 'html',
			url:"<%=basePath%>sdkflowdemo/qingjia/Btn_Save_Click.do",
			data:$('#form1').serialize(),
		    success: function(data) {
		    	window.location.reload();
		    }
		});
	}
	function Btn_Track_Click(){
		$.ajax({
			cache: true,
			type: "POST",
			dataType : 'html',
			url:"<%=basePath%>sdkflowdemo/qingjia/Btn_Track_Click.do",
			data : $('#form1').serialize(),
			success : function(data) {
			   var wd= window.showModalDialog(data,"dialogWidth=500px;dialogHeight=400px")
			}
		});
	}
	
	 //重写执行发送的方法.
    function Send() {
    	Btn_Send_Click();
    }
	 //重写执行发送的方法.
    function Save() {
    	Btn_Save_Click();
    }
</script>
</head>

<body>
	<div id="mess"></div>
	<form id="form1" action="" method="post">
		<input type="hidden" id="WorkID" name="WorkID" value="<%=stsd.getWorkID()%>" /> 
		<input type="hidden" id="FK_Flow" name="FK_Flow" value="<%=stsd.getFK_Flow()%>" /> 
		<input type="hidden" id="FID" name="FID" value="<%=stsd.getFID()%>" />
		<input type="hidden" id="FK_Node" name="FK_Node" value="<%=stsd.getFK_Node()%>" />
		<%
			int nodeid = Integer.parseInt(request.getParameter("FK_Node"));
			Node nd = new Node(nodeid);
		%>

		<h2>
			请假流程 - 当前节点:<%=nd.getName()%></h2>
		<br> 登录者信息:<%=WebUser.getNo()%>,<%=WebUser.getName()%>
		部门编号:<%=WebUser.getFK_Dept()%>,部门名称<%=WebUser.getFK_DeptName()%>
		<div>
			<fieldset>
				<legend>请假基本信息</legend>
				<table border="1" width="90%">
					<tr>
						<th>项目</th>
						<th>数据</th>
						<th>说明</th>
					</tr>

					<tr>
						<td>请假人帐号:</td>
						<td><%=stsd.TB_No%></td>
						<td>(只读)当前登录人登录帐号BP.Web.WebUser.No</td>
					</tr>

					<tr>
						<td>请假人名称:</td>
						<td><%=stsd.TB_Name%></td>
						<td>(只读)当前登录人名称BP.Web.WebUser.Name</td>
					</tr>
					<tr>
						<td>请假人部门编号:</td>
						<td><%=stsd.TB_DeptNo%></td>
						<td>(只读)当前登录人部门编号BP.Web.WebUser.FK_Dept</td>
					</tr>
					<tr>
						<td>请假人部门名称:</td>
						<td><%=stsd.TB_DeptName%></td>
						<td>(只读)当前登录人部门名称BP.Web.WebUser.FK_DeptName</td>
					</tr>
					<tr>
						<td>请假天数:</td>
						<td><%=stsd.TB_QingJiaTianShu%></td>
						<td>请输入一个数字</td>
					</tr>
					<tr>
						<td>请假原因:</td>
						<td><%=stsd.TB_QingJiaYuanYin%></td>
					</tr>
				</table>
			</fieldset>

			<fieldset>
				<legend>功能操作区域</legend>
				<!-- <input type="button" id="Btn_Send" value="发送(发送给部门经理审批)" onclick="Btn_Send_Click()" /> 
				<input type="button" id="Btn_Save"value="保存" onclick="Btn_Save_Click()" /> 
				<input type="button" id="Btn_Track" value="流程图" onclick="Btn_Track_Click()" />
				  -->
				<br/>
				<%@ include file="/WF/SDKComponents/Toolbar.jsp"%>
			</fieldset>

			<fieldset>
				<legend>URL传值</legend>
				<font color='blue' size="2px"> <%=request.getRequestURL() + "?" + request.getQueryString()%>
				</font>
			</fieldset>

		</div>
	</form>
</body>
</html>