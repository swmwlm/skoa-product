<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>项目浏览</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/static/atjs/1.5.0/css/jquery.atwho.min.css"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/atjs/jquery.caret.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/atjs/1.5.0/js/jquery.atwho.min.js"></script>

	<script type="text/javascript">
		$(function () {
//			$.fn.atwho.debug = true
            $('#editable').atwho({
				at: "@",
				data: ${userList},
				headerTpl: '<div class="atwho-header">选择用户</div>',
				insertTpl: "@<span class='atwho-loginName'>$"+"{id}</span>($"+"{name})",
				displayTpl: "<li>$" + "{id}($"+"{name})</li>",
				searchKey: "searchKey",
				limit: 10,
				callbacks:{
					beforeReposition:function (offset) {
						var value=$("#openClose",self.parent.document).length;
						if(value>0){
							var $left=$("#left",self.parent.document);
							var leftWidth = $left.width() < 0 ? 0 : -$left.width();
							offset.left+=leftWidth;
							var $header = $("#header",self.parent.document);
							offset.top+=(-$header.height());
						}
					}
				}
			});
			
			$("#progressTable ol li a").each(function (index,domEle) {
				var progressId=$(domEle).parent().parent().prev().attr("data-id");
				$(domEle).attr('href',$(domEle).attr("href")+"?id="+progressId);
			});

		});

		function saveProjectNote() {
			if ($('#editable').text() == "") {
				alertx("请输入要发布的内容！");
				return;
			}
//			confirmx("确定发布吗？", doSaveProjectNote);
			doSaveProjectNote();
		}

		var lock = false;
		function doSaveProjectNote() {

			if (lock) {return;}

			$('#content').val($('#editable').text());
			var atUserids = "";
			$.each($('.atwho-loginName'), function (n, value) {
				atUserids = atUserids + "," + $(value).text();
			});
			$("#atUserids").val(atUserids);

			$.ajax({
				url: "${ctx}/project/projectNote/saveProjectNote",
				type: "POST",
				data: $('#projectNoteForm').serialize(),
				beforeSend: function(XMLHttpRequest){
					lock=true;
				},
				success: function (data, textStatus, jqXHR) {
					$('#editable').text("");
					$('#content').val("");
					$("#atUserids").val("");

					var ahtml = [];
					ahtml.push('<div>');
					ahtml.push('<hr/>');
					ahtml.push('<div style="float: left;width: 10%;">');
					ahtml.push(data.data.name);
					ahtml.push('：</div>');
					ahtml.push('<div style="float: left;width: 68%;">');
					ahtml.push(data.data.content);
					ahtml.push('</div>');
					ahtml.push('<div style="float: right;width: 18%;">');
					ahtml.push(data.data.createDate);
					ahtml.push('</div>');
					ahtml.push('<div style="clear:both;"></div>');
					ahtml.push('</div>');
					$("#divProjectNote").html(ahtml.join("") + $("#divProjectNote").html());

					showTip("发布成功!",'',1000,0);
				},
				error: function (jqXHR, textStatus, errorMsg) {
					showTip("发布失败!",'',1000,0);
				},
				complete:function(XMLHttpRequest, textStatus){
					lock = false;
				}
			});
		}

	</script>
	<%--<link href='http://fonts.googleapis.com/css?family=PT+Sans:400,700' rel='stylesheet' type='text/css'>--%>

	<style type="text/css">
		.inputor {
			height: 60px;
			width: 90%;
			border: 1px solid #dadada;
			border-radius: 4px;
			padding: 5px 8px;
			outline: 0 none;
			margin: 10px 0;
			background: white;
			font-size: inherit;
			overflow-y: scroll;
		}
		[contentEditable=true]:empty:not(:focus):before{
			content:attr(data-text);
			color:#aaa;
		}
	</style>

</head>

<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/project/projectInfo/">项目列表</a></li>
		<li class="active">
			<a href="${ctx}/project/projectInfo/view?id=${projectInfo.id}">
				项目浏览
			</a>
		</li>
	</ul><br/>


	<div class="form-horizontal">
		<div class="control-group">
			<label class="control-label">归属部门：</label>
			<div class="controls">
				${projectInfo.office.name}
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">归属区域：</label>
			<div class="controls">
				${projectInfo.area.name}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目名称：</label>
			<div class="controls">
				${projectInfo.projectName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目级别：</label>
			<div class="controls">
				${fns:getDictLabel(projectInfo.projectGrade, 'projectGrade', '暂无级别')}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目主负责人：</label>
			<div class="controls">
				${projectInfo.primaryPerson.name}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目副负责人成员：</label>
			<div class="controls">
				${projectInfo.teamMemberNames}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目小组成员：</label>
			<div class="controls">
				${projectInfo.projectTeamMemberNames}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">行业领域：</label>
			<div class="controls">
				${fns:getDictLabel(projectInfo.industryDomain, 'industryDomain', '暂无行业领域')}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">主营业务：</label>
			<div class="controls">
				${projectInfo.mainBusiness}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目介绍：</label>
			<div class="controls">
				${projectInfo.content}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">附件路径：</label>
			<div class="controls">
				<input type="hidden" id="filepath" value="${projectInfo.filepath}" />
				<sys:ckfinder input="filepath" type="files" uploadPath="/project/projectInfo" selectMultiple="true" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">年收入：</label>
			<div class="controls">
				${projectInfo.annualIncome}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">年净利润：</label>
			<div class="controls">
				${projectInfo.annualNetProfit}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目进度：</label>
			<div class="controls">
				${fns:getDictLabel(projectInfo.projectProgress, 'projectProgress', '暂无进度')}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目的开始时间：</label>
			<div class="controls">
				<fmt:formatDate value="${projectInfo.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目推荐人：</label>
			<div class="controls">
				${projectInfo.recommendedMan}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目的推荐时间：</label>
			<div class="controls">
				<fmt:formatDate value="${projectInfo.recommendedDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目类型：</label>
			<div class="controls">
				${fns:getDictLabel(projectInfo.projectType, 'projectType', '暂无类型')}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">拟投金额：</label>
			<div class="controls">
				${projectInfo.intendedMoney}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目状态：</label>
			<div class="controls">
				${fns:getDictLabel(projectInfo.projectStatus, 'projectStatus', '暂无状态')}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">推荐理由：</label>
			<div class="controls">
				${projectInfo.remarks}
			</div>
		</div>
		<c:if test="${!fns:isProjectInfoNew(projectInfo)}">
			<c:if test="${not empty projectInfo.projectInfoMeetingList}">
			<div class="control-group" id="divProjectMeeting">
				<table class="table table-hover" id="meetingTable">
					<thead>
					<tr>
						<th>序号</th>
						<th>申请上立项会审批记录</th>
						<th>备注</th>
						<th>操作者</th>
						<th>日期</th>
					</tr>
					</thead>
					<tbody>
						<c:forEach items="${projectInfo.projectInfoMeetingList}" var="pp" varStatus="status">
							<tr>
								<td>${status.index+1}</td>
								<td>
										${fns:getDictLabel(pp.statusOrigin, 'projectStatus', '暂无')}=>${fns:getDictLabel(pp.statusCurrent, 'projectStatus', '暂无')}
								</td>
								<td>${pp.remarks}</td>
								<td>${pp.createBy.name}</td>
								<td>
									<fmt:formatDate value="${pp.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			</c:if>
		</c:if>
		<c:if test="${!fns:isProjectInfoNew(projectInfo)}">
			<div class="control-group" id="divProjectProgress">
				<table class="table table-hover" id="progressTable">
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
									<input id="filepathProgress_${pp.id}" type="hidden" value="${pp.filepath}" data-id="${pp.id}"/>
									<sys:ckfinder input="filepathProgress_${pp.id}" type="files" uploadPath="/project/projectInfoProgress" selectMultiple="true" readonly="true"/>
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


		<div class="control-group">
			<label class="control-label">项目动态：</label>
			<div class="controls">
				<form id="projectNoteForm">
					<input type="hidden" id="projectId" name="projectId" value="${projectInfo.id}"/>
					<input type="hidden" id="atUserids" name="atUserids" value=""/>
					<input type="hidden" id="content" name="content" value=""/>
					<div id="editable" class="inputor" contentEditable="true" data-text="@关键字,他就能在[我的通告]收到;可使用员工中文姓名,全拼,简拼及登录名."></div>
					<%--<div id="editable" class="inputor" contentEditable="true"></div>--%>
					<input type="button" id="btnSend" class="btn btn-primary" onclick="javascript:saveProjectNote();"
						   value="发 布"/>
					<%--<span class="help-inline">友情提醒:@关键字,他就能在[我的通告]收到;可使用员工中文姓名,全拼,简拼及登录名.</span>--%>
				</form>
			</div>

			<label class="control-label">&nbsp;&nbsp;</label>
			<div class="controls" id="divProjectNote">
				<c:forEach var="projectNote" items="${projectNoteList}">
					<div>
						<hr/>
						<div style="float: left;width: 10%;">${projectNote.createBy.name}：</div>
						<div style="float: left;width: 68%;">${projectNote.content}</div>
						<div style="float: right;width: 18%;"><fmt:formatDate value="${projectNote.createDate}"
																			  pattern="yyyy-MM-dd HH:mm:ss"/></div>
						<div style="clear:both;"></div>
					</div>
				</c:forEach>
			</div>

		</div>


		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</div>
</body>
</html>