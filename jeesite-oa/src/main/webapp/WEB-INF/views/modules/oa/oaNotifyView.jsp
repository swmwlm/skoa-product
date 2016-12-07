<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>通知管理</title>
    <meta name="decorator" content="default"/>
</head>
<body>
<ul class="nav nav-tabs">
    <li><a href="${ctx}/oa/oaNotify/self/">通知列表</a></li>
    <li class="active"><a href="${ctx}/oa/oaNotify/view?id=${oaNotify.id}">通知查看</a></li>
</ul>
<br/>
<form:form id="inputForm" modelAttribute="oaNotify" class="form-horizontal">
    <form:hidden path="id"/>
    <sys:message content="${message}"/>
    <div class="control-group">
        <label class="control-label">类型：</label>

        <div class="controls">
                ${fns:getDictLabel(oaNotify.type, 'oa_notify_type', '')}
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">标题：</label>

        <div class="controls">
            <c:if test="${oaNotify.type==4 && not empty oaNotify.remarks}">
                <a href="${ctx}/project/projectInfo/view?id=${oaNotify.remarks}#divProjectNote" title="项目浏览" style="text-decoration:none">
                    <i class="icon-file-alt icon-large"></i>
                </a>
            </c:if>
            <c:if test="${oaNotify.type==5 && not empty oaNotify.remarks}">
                <a href="${ctx}/project/projectInfo/view?id=${oaNotify.remarks}#divProjectProgress" title="项目浏览" style="text-decoration:none">
                    <i class="icon-file-alt icon-large"></i>
                </a>
            </c:if>
                ${oaNotify.title}
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">内容：</label>

        <div class="controls">
                ${oaNotify.content}
        </div>
    </div>

    <div class="control-group">
        <label class="control-label">附件：</label>

        <div class="controls">
            <form:hidden id="files" path="files" htmlEscape="false" maxlength="255" class="input-xlarge"/>
            <sys:ckfinder input="files" type="files" uploadPath="/oa/notify" selectMultiple="true" readonly="true"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">接受人：</label>

        <div class="controls">
            <table id="contentTable" class="table table-striped table-bordered table-condensed">
                <thead>
                <tr>
                    <th>接受人</th>
                    <th>接受部门</th>
                    <th>阅读状态</th>
                    <th>阅读时间</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${oaNotify.oaNotifyRecordList}" var="oaNotifyRecord">
                    <tr>
                        <td>
                                ${oaNotifyRecord.user.name}
                        </td>
                        <td>
                                ${oaNotifyRecord.user.office.name}
                        </td>
                        <td>
                                ${fns:getDictLabel(oaNotifyRecord.readFlag, 'oa_notify_read', '')}
                        </td>
                        <td>
                            <fmt:formatDate value="${oaNotifyRecord.readDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            已查阅：${oaNotify.readNum} &nbsp; 未查阅：${oaNotify.unReadNum} &nbsp;
            总共：${oaNotify.readNum + oaNotify.unReadNum}
        </div>
    </div>

    <div class="form-actions">
        <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
    </div>
</form:form>
</body>
</html>