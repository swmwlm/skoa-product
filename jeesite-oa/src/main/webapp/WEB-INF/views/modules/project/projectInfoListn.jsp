<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>项目管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			top.$.jBox.tip.mess = null;

			var timeagoInstance = new timeago();
			timeagoInstance.render($('abbr'), 'zh_CN');
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		function updateProgress(id, progress){
            $('#myModal').modal();
            $("#projectInfoId").val(id);
            $("#progressBoxProgress").val(progress).trigger("change");
		}
		function toValid(){
			var projectInfoId=$("#projectInfoId").val();
			var currentProjectProgress=$("#href_"+projectInfoId).attr("current-progress");
			var changeProjectProgress=$("#progressBoxProgress").val();
			if(changeProjectProgress==currentProjectProgress){
				alertx("项目进度无变更!");
				return false;
			}
			if(currentProjectProgress!=""&&currentProjectProgress>changeProjectProgress){
				alertx("项目进度不能后撤");
				return false;
			}
			$('#myModal').modal('hide');
			return true;
		}

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/project/projectInfo/listn">项目列表(仅供查看)</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="projectInfo" action="${ctx}/project/projectInfo/listn" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<%--<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>--%>
		<ul class="ul-form">
			<li><label>归属部门：</label>
				<sys:treeselect id="office" name="office.id" value="${projectInfo.office.id}" labelName="office.name" labelValue="${projectInfo.office.name}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>归属区域：</label>
				<sys:treeselect id="area" name="area.id" value="${projectInfo.area.id}" labelName="area.name" labelValue="${projectInfo.area.name}"
					title="区域" url="/sys/area/treeData" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>项目名称：</label>
				<form:input path="projectName" htmlEscape="false" maxlength="200" class="input-medium"/>
			</li>
			<li><label>项目级别：</label>
				<form:select path="projectGrade" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('projectGrade')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>项目负责人：</label>
				<sys:treeselect id="primaryPerson" name="primaryPerson.id" value="${projectInfo.primaryPerson.id}" labelName="primaryPerson.name" labelValue="${projectInfo.primaryPerson.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>项目发起人：</label>
				<sys:treeselect id="createBy" name="createBy.id" value="${projectInfo.createBy.id}" labelName="createBy.name" labelValue="${projectInfo.createBy.name}"
								title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>行业领域：</label>
				<form:select path="industryDomain" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('industryDomain')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>项目进度：</label>
				<form:select path="projectProgress" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('projectProgress')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>项目阶段：</label>
				<form:select path="projectType" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('projectType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>项目状态：</label>
				<form:select path="projectStatus" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('projectStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>归属部门</th>
				<th>项目名称</th>
				<th>项目主负责人</th>
				<th>行业领域</th>
				<th title="负责人可以变更项目进度">项目进度 <i class="icon-question-sign"></i></th>
				<th>项目阶段</th>
				<th>项目状态</th>
				<%--<th class="sort-column a.update_date">更新时间</th>--%>
				<th title="处于项目发布状态且超过15天未做更新的,&#10;系统将发通知提醒负责人">更新时间 <i class="icon-question-sign"></i></th>
				<th>更新者</th>
				<th>推荐理由</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="projectInfo">
			<tr>
				<td>
					${projectInfo.office.name}
				</td>
				<td>
					<span title="${projectInfo.projectName}">
						${fns:abbr(projectInfo.projectName,30)}
					</span>
					<c:if test="${fns:isProjectInfoPrimaryPerson(projectInfo)}">
						<span class="badge badge-important" title="项目负责人">P</span>
					</c:if>
					<c:if test="${fns:isProjectInfoCreator(projectInfo)}">
						<span class="badge badge-success" title="项目创建者">C</span>
					</c:if>
					<c:if test="${fns:isProjectInfoTeam(projectInfo)}">
						<span class="badge badge-success" title="项目小组成员">T</span>
					</c:if>
				</td>
				<td>
					${projectInfo.primaryPerson.name}
				</td>
				<td>
					${fns:getDictLabel(projectInfo.industryDomain, 'industryDomain', '')}
				</td>
				<td>
					<c:choose>
						<c:when test="${fns:isAllowedUpdateProjectProgress(projectInfo)}">
							<a href="javascript:updateProgress('${projectInfo.id}', '${projectInfo.projectProgress}')" title="设置进度" current-progress="${projectInfo.projectProgress}" id="href_${projectInfo.id}">
								${fns:getDictLabel(projectInfo.projectProgress, 'projectProgress', '暂无进度')}
							</a>
						</c:when>
						<c:otherwise>
							${fns:getDictLabel(projectInfo.projectProgress, 'projectProgress', '暂无进度')}
						</c:otherwise>
					</c:choose>
				</td>
				<td>
					${fns:getDictLabel(projectInfo.projectType, 'projectType', '')}
				</td>
				<td>
					<c:choose>
						<c:when test="${fns:isAllowedUpdateProjectMeeting(projectInfo)}">
							<a href="javascript:updateMeeting('${projectInfo.id}', '${projectInfo.projectStatus}')"
							   title="审批">
									${fns:getDictLabel(projectInfo.projectStatus, 'projectStatus', '')}
							</a>
						</c:when>
						<c:otherwise>${fns:getDictLabel(projectInfo.projectStatus, 'projectStatus', '')}</c:otherwise>
					</c:choose>
				</td>
				<td>
					<fmt:formatDate value="${projectInfo.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					(<abbr datetime='<fmt:formatDate value="${projectInfo.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>'></abbr>)
				</td>
				<td>
					${projectInfo.updateBy.name}
				</td>
				<td title="${projectInfo.remarks}">
					${fns:abbr(projectInfo.remarks,10)}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>