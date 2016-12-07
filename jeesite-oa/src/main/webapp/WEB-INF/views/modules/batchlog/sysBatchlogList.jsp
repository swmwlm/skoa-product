<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>任务日志管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/batchlog/sysBatchlog/">任务日志列表</a></li>
		<shiro:hasPermission name="batchlog:sysBatchlog:edit"><li><a href="${ctx}/batchlog/sysBatchlog/form">任务日志添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="sysBatchlog" action="${ctx}/batchlog/sysBatchlog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>任务名称：</label>
				<form:input path="jobname" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>执行时间：</label>
				<input name="lastexecutetime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${sysBatchlog.lastexecutetime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>任务名称</th>
				<th>执行时间</th>
				<th>添加时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="batchlog:sysBatchlog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sysBatchlog">
			<tr>
				<td><a href="${ctx}/batchlog/sysBatchlog/form?id=${sysBatchlog.id}">
					${sysBatchlog.jobname}
				</a></td>
				<td>
					<fmt:formatDate value="${sysBatchlog.lastexecutetime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${sysBatchlog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${sysBatchlog.remarks}
				</td>
				<shiro:hasPermission name="batchlog:sysBatchlog:edit"><td>
    				<a href="${ctx}/batchlog/sysBatchlog/form?id=${sysBatchlog.id}">修改</a>
					<a href="${ctx}/batchlog/sysBatchlog/delete?id=${sysBatchlog.id}" onclick="return confirmx('确认要删除该任务日志吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>