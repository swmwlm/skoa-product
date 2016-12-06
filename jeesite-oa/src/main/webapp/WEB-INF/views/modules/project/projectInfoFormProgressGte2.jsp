<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>项目管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					if (CKEDITOR.instances.content.getData()==""){
						top.$.jBox.tip('请填写项目介绍','warning');
						CKEDITOR.instances.content.focus();
					}else{
						loading('正在提交，请稍等...');
						form.submit();
					}
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/project/projectInfo/">项目列表</a></li>
		<li class="active">
			<a href="${ctx}/project/projectInfo/form?id=${projectInfo.id}">
				项目${not empty projectInfo.id?'修改':'添加'}
			</a>
		</li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="projectInfo" action="${ctx}/project/projectInfo/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">归属部门：</label>
			<div class="controls">
				<%--<sys:treeselect id="office" name="office.id" value="${projectInfo.office.id}" labelName="office.name" labelValue="${projectInfo.office.name}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="" allowClear="true" notAllowSelectParent="true"/>--%>
						${projectInfo.office.name}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">归属区域：</label>
			<div class="controls">
				<%--<sys:treeselect id="area" name="area.id" value="${projectInfo.area.id}" labelName="area.name" labelValue="${projectInfo.area.name}"
					title="区域" url="/sys/area/treeData" cssClass="" allowClear="true" notAllowSelectParent="true"/>--%>
						${projectInfo.area.name}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目名称：</label>
			<div class="controls">
				<%--<form:input path="projectName" htmlEscape="false" maxlength="200" class="required measure-input input-xlarge"/>
				<span class="help-inline"><font color="red">*</font> </span>--%>
						${projectInfo.projectName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目级别：</label>
			<div class="controls">
				<%--<form:select path="projectGrade" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('projectGrade')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>--%>
						${fns:getDictLabel(projectInfo.projectGrade, 'projectGrade', '暂无级别')}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目主负责人：</label>
			<div class="controls">
				<%--<sys:treeselect id="primaryPerson" name="primaryPerson.id" value="${projectInfo.primaryPerson.id}" labelName="primaryPerson.name" labelValue="${projectInfo.primaryPerson.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="required" allowClear="true" notAllowSelectParent="true"/>
				<span class="help-inline"><font color="red">*</font> </span>--%>
						${projectInfo.primaryPerson.name}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目副负责人成员：</label>
			<div class="controls">
				<%--<sys:treeselect id="teamMembers" name="teamMembers" value="${projectInfo.teamMembers}" labelName="teamMemberNames" labelValue="${projectInfo.teamMemberNames}"
								title="用户" url="/sys/office/treeData?type=3" cssClass="input-xxlarge" allowClear="true" notAllowSelectParent="true" checked="true"/>--%>
						${projectInfo.teamMemberNames}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">行业领域：</label>
			<div class="controls">
				<%--<form:select path="industryDomain" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('industryDomain')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>--%>
						${fns:getDictLabel(projectInfo.industryDomain, 'industryDomain', '暂无行业领域')}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">主营业务：</label>
			<div class="controls">
				<%--<form:input path="mainBusiness" htmlEscape="false" maxlength="100" class="input-xxlarge "/>
				<span class="help-inline">可输入文本信息 </span>--%>
						${projectInfo.mainBusiness}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目介绍：</label>
			<div class="controls">
				<%--<form:textarea id="content" htmlEscape="true" path="content" rows="4" maxlength="200" class="input-xlarge"/>
				<sys:ckeditor replace="content" uploadPath="/project/projectInfo/content" />--%>
						${projectInfo.content}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">附件路径：</label>
			<div class="controls">
				<form:hidden id="filepath" path="filepath" htmlEscape="false" maxlength="200" class="input-xlarge"/>
				<sys:ckfinder input="filepath" type="files" uploadPath="/project/projectInfo" selectMultiple="true" addonly="true"/>
				<%--<input type="hidden" id="filepath" value="${projectInfo.filepath}" />
				<sys:ckfinder input="filepath" type="files" uploadPath="/project/projectInfo" selectMultiple="true" readonly="true"/>--%>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">年收入：</label>
			<div class="controls">
				<%--<form:input path="annualIncome" htmlEscape="false" maxlength="100" class="input-xlarge "/>
				<span class="help-inline">可输入文本信息 </span>--%>
						${projectInfo.annualIncome}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">年净利润：</label>
			<div class="controls">
				<%--<form:input path="annualNetProfit" htmlEscape="false" maxlength="100" class="input-xlarge "/>
				<span class="help-inline">可输入文本信息 </span>--%>
						${projectInfo.annualNetProfit}
			</div>
		</div>
		<%--<div class="control-group">
			<label class="control-label">项目进度：</label>
			<div class="controls">
				<form:select path="projectProgress" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('projectProgress')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>--%>
		<div class="control-group">
			<label class="control-label">项目的开始时间：</label>
			<div class="controls">
				<%--<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${projectInfo.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>--%>
					<fmt:formatDate value="${projectInfo.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目推荐人：</label>
			<div class="controls">
				<%--<form:input path="recommendedMan" htmlEscape="false" maxlength="50" class="input-xlarge "/>
				<span class="help-inline">可输入文本信息 </span>--%>
						${projectInfo.recommendedMan}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目的推荐时间：</label>
			<div class="controls">
				<%--<input name="recommendedDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${projectInfo.recommendedDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>--%>
					<fmt:formatDate value="${projectInfo.recommendedDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目类型：</label>
			<div class="controls">
				<%--<form:select path="projectType" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('projectType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>--%>
						${fns:getDictLabel(projectInfo.projectType, 'projectType', '暂无类型')}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">拟投金额：</label>
			<div class="controls">
				<%--<form:input path="intendedMoney" htmlEscape="false" class="input-xlarge number"/>
				<span class="help-inline">只能为数值类型 </span>--%>
						${projectInfo.intendedMoney}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目状态：</label>
			<div class="controls">
				<form:select path="projectStatus" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('projectStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
				<span class="help-inline">推介人编辑:此阶段的项目只有创建人可见 </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<c:if test="${!fns:isProjectInfoNew(projectInfo)}">
		<div class="control-group">
			<table class="table table-hover">
				<thead>
				<tr>
					<th>序号</th>
					<th>进度变更</th>
					<th>附件</th>
					<th>备注</th>
					<th>操作者</th>
					<th>日期</th>
				</tr>
				</thead>
				<tbody>
				<c:if test="${not empty projectInfo.projectInfoProgressList}">
				<c:forEach items="${projectInfo.projectInfoProgressList}" var="pp" varStatus="status">
				<tr>
					<td>${status.index+1}</td>
					<td>
						${fns:getDictLabel(pp.statusOrigin, 'projectProgress', '暂无进度')}=>${fns:getDictLabel(pp.statusCurrent, 'projectProgress', '暂无进度')}
					</td>
					<td>
						<input id="filepathProgress_${pp.id}" type="hidden" value="${pp.filepath}"/>
						<sys:ckfinder input="filepathProgress_${pp.id}" type="files" uploadPath="/project/projectInfo" selectMultiple="true" readonly="true"/>
					</td>
					<td>${pp.remarks}</td>
					<td>${pp.createBy.name}</td>
					<td>
						<fmt:formatDate value="${pp.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
				</tr>
				</c:forEach>
				</c:if>
				</tbody>
			</table>
		</div>
		</c:if>
		<div class="form-actions">
			<%--<shiro:hasPermission name="project:projectInfo:edit">--%>
				<c:if test="${fns:isProjectInfoNew(projectInfo) || fns:editableProject(projectInfo)}">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
				</c:if>
			<%--</shiro:hasPermission>--%>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>