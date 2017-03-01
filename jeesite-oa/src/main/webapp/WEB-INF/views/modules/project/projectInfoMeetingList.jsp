<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>项目立项审批管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {

        });
        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/project/projectInfoMeeting/">上立项会议审批列表</a></li>
</ul>
<form:form id="searchForm" modelAttribute="projectInfoMeeting" action="${ctx}/project/projectInfoMeeting/" method="post"
           class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <%--<th>编号</th>--%>
        <%--<th>项目id</th>--%>
        <th>项目</th>
        <th>项目状态-更新前</th>
        <th>项目状态-更新后</th>
        <%--<th>状态更新者</th>--%>
        <%--<th>状态更新时间</th>--%>
        <%--<th>状态更新者</th>--%>
        <th>备注</th>
        <th>更新时间</th>
        <%--<shiro:hasPermission name="project:projectInfoMeeting:edit">--%>
        <%--<th>操作</th>--%>
        <%--</shiro:hasPermission>--%>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="projectInfoMeeting">
        <tr>
                <%--<td><a href="${ctx}/project/projectInfoMeeting/form?id=${projectInfoMeeting.id}">--%>
                <%--${projectInfoMeeting.id}--%>
                <%--</a></td>--%>
                <%--<td>--%>
                <%--${projectInfoMeeting.projectInfoId}--%>
                <%--</td>--%>
            <td>
                <a href="${ctx}/project/projectInfo/view?id=${projectInfoMeeting.projectInfoId}"
                   title="${projectInfo.projectName}">
                        ${fns:abbr(projectInfoMeeting.projectInfoName,30)}
                </a>
            </td>
            <td>
                    ${fns:getDictLabel(projectInfoMeeting.statusOrigin, 'projectStatus', '暂无')}
            </td>
            <td>
                    ${fns:getDictLabel(projectInfoMeeting.statusCurrent, 'projectStatus', '暂无')}
            </td>
                <%--<td>--%>
                <%--${projectInfoMeeting.createBy.id}--%>
                <%--</td>--%>
                <%--<td>--%>
                <%--<fmt:formatDate value="${projectInfoMeeting.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>--%>
                <%--</td>--%>
                <%--<td>--%>
                <%--${projectInfoMeeting.updateBy.id}--%>
                <%--</td>--%>
            <td>
                    ${projectInfoMeeting.remarks}
            </td>
            <td>
                <fmt:formatDate value="${projectInfoMeeting.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </td>

                <%--<shiro:hasPermission name="project:projectInfoMeeting:edit"><td>--%>
                <%--<a href="${ctx}/project/projectInfoMeeting/form?id=${projectInfoMeeting.id}">修改</a>--%>
                <%--<a href="${ctx}/project/projectInfoMeeting/delete?id=${projectInfoMeeting.id}" onclick="return confirmx('确认要删除该项目立项审批吗？', this.href)">删除</a>--%>
                <%--</td></shiro:hasPermission>--%>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>