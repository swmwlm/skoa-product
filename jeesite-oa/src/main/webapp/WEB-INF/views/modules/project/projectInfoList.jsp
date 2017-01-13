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

		function updateMeeting(id, status) {
			$('#myMeetingModal').modal();
			$("#meetingProjectInfoId").val(id);
			$("#projectBoxStatus").val(status);
		}
		function meetingNo() {
			$("#flag").val("0");
			meetingSubmit();
		}
		function meetingYes() {
			$("#flag").val("1");
			meetingSubmit();
		}
		function meetingSubmit() {
			$('#myMeetingModal').modal('hide');
			$("#meetingForm").submit();
		}

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/project/projectInfo/">项目列表</a></li>
		<%--<shiro:hasPermission name="project:projectInfo:edit">--%>
			<li><a href="${ctx}/project/projectInfo/form">项目添加</a></li>
		<%--</shiro:hasPermission>--%>
	</ul>
	<form:form id="searchForm" modelAttribute="projectInfo" action="${ctx}/project/projectInfo/" method="post" class="breadcrumb form-search">
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
			<li><label>项目类型：</label>
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
				<th>项目类型</th>
				<th>项目状态</th>
				<%--<th class="sort-column a.update_date">更新时间</th>--%>
				<th title="处于项目发布状态且超过15天未做更新的,&#10;系统将发通知提醒负责人">更新时间 <i class="icon-question-sign"></i></th>
				<th>更新者</th>
				<th>推荐理由</th>
				<%--<shiro:hasPermission name="project:projectInfo:edit">--%>
					<th title="两种情况下可以操作:&#10;①项目创建者处于个人编辑时&#10;②项目负责人&#10;注意:项目进度处于立项后不能变更基础资料">操作 <i class="icon-question-sign"></i></th>
				<%--</shiro:hasPermission>--%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="projectInfo">
			<tr>
				<td>
					${projectInfo.office.name}
				</td>
				<td>
					<a href="${ctx}/project/projectInfo/view?id=${projectInfo.id}" title="${projectInfo.projectName}">
						${fns:abbr(projectInfo.projectName,30)}
					</a>
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
				<%--<shiro:hasPermission name="project:projectInfo:edit">--%>
				<td>
					<c:if test="${fns:editableProject(projectInfo)}">
					<a href="${ctx}/project/projectInfo/form?id=${projectInfo.id}">修改</a>
					<a href="${ctx}/project/projectInfo/delete?id=${projectInfo.id}" onclick="return confirmx('确认要删除该项目吗？', this.href)">删除</a>
					</c:if>
				</td>
				<%--</shiro:hasPermission>--%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
    <!-- Modal -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" style="display: none;">
        <div class="modal-dialog">
            <form id="progressForm" action="${ctx}/project/projectInfo/updateProgress" method="post" enctype="multipart/form-data" class="form-search form-horizontal" onsubmit="return toValid();">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only"></span></button>
                        <h4 class="modal-title" id="myModalLabel">项目进度更新</h4>
                    </div>
                    <div class="modal-body">
                        <input id="projectInfoId" type="hidden" name="projectInfoId" value="" />
                        <div class="control-group">
                            <label class="control-label">项目进度：</label>
                            <div class="controls">
                                <select id="progressBoxProgress" name="projectProgress" class="input-medium">
                                    <c:forEach items="${fns:getDictList('projectProgress')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
								<span class="help-inline">提示:项目进度不能后撤,立项后不可变更基础资料. </span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">附件：</label>
                            <div class="controls">
                                <input type="hidden" id="filepath" name="filepath"/>
                                <sys:ckfinder input="filepath" type="files" uploadPath="/project/projectInfoProgress" selectMultiple="true"/><span class="help-inline">提示:附件带有访问权限 </span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">备注：</label>
                            <div class="controls">
                                <textarea name="remarks" rows="4" maxlength="255" class="input-xlarge"></textarea>
                            </div>
                        </div>　
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        <button type="submit" class="btn btn-primary">保存</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

	<!-- meeting-Modal -->
    <div class="modal fade" id="myMeetingModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabelMeeting" style="display: none;">
        <div class="modal-dialog">
            <form id="meetingForm" action="${ctx}/project/projectInfo/updateMeeting" method="post" enctype="multipart/form-data" class="form-search form-horizontal">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only"></span></button>
                        <h4 class="modal-title" id="myModalLabelMeeting">申请上立项会审批</h4>
                    </div>
                    <div class="modal-body">
                        <input id="meetingProjectInfoId" type="hidden" name="meetingProjectInfoId" value="" />
                        <input id="flag" type="hidden" name="flag" value="" />
                        <div class="control-group">
                            <label class="control-label">备注：</label>
                            <div class="controls">
                                <textarea name="remarks" rows="4" maxlength="255" class="input-xlarge"></textarea>
                            </div>
                        </div>　
                    </div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						<button type="button" class="btn btn-warning" onclick="meetingNo()">拒绝</button>
						<button type="button" class="btn btn-success" onclick="meetingYes()">通过</button>
					</div>
                </div>
            </form>
        </div>
    </div>

	<div class="pagination">${page}</div>
</body>
</html>