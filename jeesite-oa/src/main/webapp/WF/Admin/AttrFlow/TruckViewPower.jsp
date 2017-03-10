<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="BP.WF.Template.*"%>
<%@page import="BP.WF.*"%>
<%@page import="BP.DA.*"%>
<%@page import="BP.WF.Template.FlowSheet"%>
<%@include file  = "/WF/head/head1.jsp" %>
<%
	String FK_Flow=request.getParameter("FK_Flow");
	boolean CB_FQR = false;
	boolean CB_CYR = false;
	boolean CB_CSR =false;
	boolean CB_BBM = false;
	boolean CB_ZSSJ = false;
	
	boolean CB_SJ=false;
	boolean CB_PJ=false;
	boolean QY_ZDGW=false;
	boolean QY_ZDQXZ=false;
	boolean QY_ZDRY=false;
	boolean QY_ZDBM=false;
	String TB_ZDQXZ="";
	String TB_ZDBM="";
	String TB_ZDGW="";
	String TB_ZDRY="";
	
%>
<body>

<table style=" width:100%; padding-left:19px;" >
<caption>流程轨迹查看权限</caption>

 
<tr>
<td colspan="1"  valign="top" rowspan="9" style="width:25%;">

<fieldset> 
<legend>帮助</legend>
<ol>
<li>该设置为了控制该流程的流程实例是可以被那些范围的人可查询，可见。</li>
<li>该控制在工作流程查看器里校验，就是说在操作员要打开工作查看器的时候检查当前的人员是否有权限查看该流程。</li>
<li>与流程相关的人员是必选项，也就是说，流程相关的人员是可以查看轨迹图的，</li>
</ol>

</fieldset>

</td>

<th colspan="4">必选项 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="CheckBox"  id="selectAll" runat="server"   Enabled="false" name="gjzk" />选择全部 </th>
</tr>

<tr>
<td> 
    <input type="CheckBox"id="CB_FQR" runat="server" name="gjzk" value=""  Enabled="false" checked="checked"  disabled="disabled"/>发起人可见 </td>

	
<td>
    <input type="CheckBox"id="CB_CYR" runat="server" name="gjzk" checked="checked"  Enabled="false" disabled="disabled"/>参与人可见 </td>
<td>
    <input type="CheckBox"id="CB_CSR" runat="server" name="gjzk" checked="checked" Enabled="false" disabled="disabled"/>被炒送人可见 </td>
<td>
     </td>
</tr>

<tr>
<th colspan="4"> 按照部门划分 </th>
</tr>

<tr>
<td> 
    <input type="CheckBox"id="CB_BBM" name="gjzk" runat="server" />本部门人可看 </td>
<td>
     <input type="CheckBox"id="CB_ZSSJ" name="gjzk" runat="server"/>直属上级部门可看(比如:我是)</td>
<td>
     <input type="CheckBox"id="CB_SJ" name="gjzk"  runat="server"/>上级部门可看 
    </td>
<td>
   <input type="CheckBox"id="CB_PJ" name="gjzk" runat="server"/>平级部门可看 
    </td> 
</tr>

<tr>
<td> 
    <input type="CheckBox" id="QY_ZDBM" name="gjzk" runat="server"/>指定的部门可见 </td>
<td colspan="3">
    部门编号&nbsp&nbsp<input type="text"id="TB_ZDBM"name="gjzk"  runat="server"/>
      </td>
</tr>

<tr>
<th colspan="4">按照其他方式指定 </th>
</tr>


<tr>
<td><input type="CheckBox" id="QY_ZDGW" name="gjzk" runat="server"/> 指定的岗位可见 </td>
<td colspan="3">
    岗位编号&nbsp&nbsp<input type="TextBox"id="TB_ZDGW" name="gjzk" runat="server"/>
      </td>
</tr>

<tr>
<td><input type="CheckBox" id="QY_ZDQXZ" name="gjzk" runat="server"/> 指定的权限组可看 </td>
<td colspan="3">
    权限组&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<input type="TextBox"id="TB_ZDQXZ" name="gjzk"runat="server"/>
</tr>

<tr>
<td><input type="CheckBox" id="QY_ZDRY" name="gjzk" runat="server"/> 指定的人员可看 </td>
<td colspan="3">
    指定人员编号&nbsp&nbsp&nbsp<input type="TextBox"id="TB_ZDRY" name="gjzk" runat="server"/>
      </td>
</tr>
</table>
<div style=" text-align:center">
    <input type="button"class="easyui-linkbutton" id="Btn_Save" runat="server" value="保存" onclick="onSave()" />
    </div>
</body>
<script  type="text/javascript">
window.onload=function(){
	$.ajax({
		type : 'post',
		async : false,
		url : '<%=basePath%>WF/TruckViewPower/Init.do',
		data:{FK_Flow:'<%=FK_Flow%>'},
		dataType : 'json',
		error: function(data){},
		success : function(data) {
			for (var i = 0; i < data.length; i++) {
				voluation(data[i]);
			}
		}
	});	
  }
    $('#selectAll').click(function(){
    	var ch=document.getElementsByName("gjzk");
    	if(document.getElementsByName("gjzk")[0].checked==true){
    	for(var i=0;i<ch.length;i++){
    	        ch[i].checked=true;
    	    }
    	   }else{
    	    for(var i=0;i<ch.length;i++){
    	    	if(ch[i].id=="CB_FQR" || ch[i].id=="CB_CYR" || ch[i].id=="CB_CSR"){
    	    		continue;
    	    	   }else{
    	           ch[i].checked=false;	
    	    	}  
    	      }
    	   }
      });
 
    function  voluation(data){	 
		var pmydept = data.pmydept;
		var ppmydept = data.ppmydept;
		var ppdept = data.ppdept;
		var psamedept = data.psamedept;
		var pspecdept = data.pspecdept;		
		var pspecsta = data.pspecsta;	
		var pspecgroup = data.pspecgroup;	
		var pspecemp = data.pspecemp;
		var pspecdeptext = data.pspecdeptext;	
		var pspecstaext = data.pspecstaext;		
		var pspecgroupext = data.pspecgroupext;	
		var pspecempext = data.pspecempext;	
    	 if (pmydept == "1"){
             $("#CB_BBM").attr("checked", true);
    	 }
    	 if (ppmydept == "1"){
             $("#CB_ZSSJ").attr("checked", true);
    	 }
    	 if (ppdept == "1"){
             $("#CB_SJ").attr("checked", true);
    	 }
    	 if (psamedept == "1"){
             $("#CB_PJ").attr("checked", true);
    	 }
    	 if (pspecdept == "1"){
             $("#QY_ZDBM").attr("checked", true);
    	 }
    	 if (pspecsta == "1"){
             $("#QY_ZDGW").attr("checked", true);
    	 }
    	 if (pspecgroup == "1"){
             $("#QY_ZDQXZ").attr("checked", true);
    	 }
    	 if (pspecemp == "1"){
             $("#QY_ZDRY").attr("checked", true);
    	 }
    	 if (pspecdeptext!="" || pspecdeptext!==null){
    		  $("#TB_ZDBM").val(pspecdeptext);
    	 }
    	 if (pspecstaext!="" || pspecstaext!==null){
   		  $("#TB_ZDGW").val(pspecstaext);
   	     }
    	 if (pspecgroupext!="" || pspecgroupext!==null){
      		  $("#TB_ZDQXZ").val(pspecgroupext);
      	 }
    	 if (pspecempext!="" || pspecempext!==null){
      		  $("#TB_ZDRY").val(pspecempext);
      	 }	 
       }
	//ajax 提交
	function onSave(){
		var TB_ZDBM=$("#TB_ZDBM").val();
		var TB_ZDGW=$("#TB_ZDGW").val();
		var TB_ZDQXZ=$("#TB_ZDQXZ").val();
		var TB_ZDRY=$("#TB_ZDRY").val();
		var BBM=$('input[id="CB_BBM"]:checked').attr('id');
		var ZSSJ=$('input[id="CB_ZSSJ"]:checked').attr('id');
		var SJ=$('input[id="CB_SJ"]:checked').attr('id');		
		var PJ=$('input[id="CB_PJ"]:checked').attr('id');
		var ZDBM=$('input[id="QY_ZDBM"]:checked').attr('id');
		var ZDGW=$('input[id="QY_ZDGW"]:checked').attr('id');
		var ZDQXZ=$('input[id="QY_ZDQXZ"]:checked').attr('id');
		var ZDRY=$('input[id="QY_ZDRY"]:checked').attr('id');
		$.ajax({
			url:'<%=basePath%>WF/TruckViewPower/BtnSaveClick.do',
			type:'post', //数据发送方式
			dataType:'json', //接受数据格式
			data:{BBM:BBM,ZSSJ:ZSSJ,SJ:SJ,PJ:PJ,ZDBM:ZDBM,ZDGW:ZDGW,ZDQXZ:ZDQXZ,ZDRY:ZDRY,
				PSpecDeptExt:TB_ZDBM,PSpecStaExt:TB_ZDGW,PSpecGroupExt:TB_ZDQXZ,PSpecEmpExt:TB_ZDRY,FK_Flow:'<%=FK_Flow%>'
				},
			async: false ,
			error: function(data){},
			success: function(data){
			alert("保存成功");
			}
		});
	  }
</script>
</html>