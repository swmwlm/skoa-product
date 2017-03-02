<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>我的审批</title>
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
        function remarkDetail(id) {
            $("#remarkDiv").html($("#remarkHidden_" + id).val());
            $('#remarkModal').modal();
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/project/projectInfoMeeting/">上立项会审批记录</a></li>
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
        <th>项目</th>
        <th>项目状态-更新前</th>
        <th>项目状态-更新后</th>
        <th>备注</th>
        <th>更新时间</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="projectInfoMeeting">
        <tr>

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
            <td>
                <c:choose>
                    <c:when test="${fn:length(projectInfoMeeting.remarks)>25}">
                        <a href="javascript:remarkDetail('${projectInfoMeeting.id}')" title="查看">
                                ${fns:abbr(projectInfoMeeting.remarks,50)}
                        </a>
                        <input id="remarkHidden_${projectInfoMeeting.id}" type="hidden"
                               name="remarkHidden_${projectInfoMeeting.id}" value="${projectInfoMeeting.remarks}"/>
                    </c:when>
                    <c:otherwise>${projectInfoMeeting.remarks}</c:otherwise>
                </c:choose>
            </td>
            <td>
                <fmt:formatDate value="${projectInfoMeeting.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<!-- Modal -->
<div class="modal fade" id="remarkModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabelMeeting"
     style="display: none;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                        class="sr-only"></span></button>
                <h4 class="modal-title" id="myModalLabelMeeting">备注</h4>
            </div>
            <div class="modal-body">
                <div class="control-group">
                    <div class="controls" id="remarkDiv"></div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>

<div class="pagination">${page}</div>
</body>
</html>