﻿/// <reference path="../../../Scripts/EasyUIUtility.js" />
/// <reference path="FuncTrees.js" />

var FLOW_TREE = "flowTree";
var FORM_TREE = "formTree";
var ORG_TREE = "OrgTree";

//设计器加载完毕隐藏等待页面
function DesignerLoaded() {
    $(".mymask").hide();
}

//右键打开流程
function showFlow() {
    var node = $('#flowTree').tree('getSelected');
    if (!node || node.attributes.isparent != '0') return;
    OpenFlowToCanvas(node, node.id, node.text);
}

//重新装载流程图
function RefreshFlowJson() {
    var node = $('#flowTree').tree('getSelected');
    if (!node || node.attributes.isparent != '0') return;
    //首先关闭tab
    closeTab(node.text);
    $.post("../../Admin/CCBPMDesignerController/ProcessRequest.do",{
        action: 'ccbpm_flow_resetversion',
        FK_Flow: node.id
    }, function (jsonData) {
        $(".mymask").show();
        addTab(node.id, node.text, "Designer.jsp?FK_Flow=" + node.id + "&UserNo=" + WebUser.No + "&SID=" + WebUser.SID + "&Flow_V=0", node.iconCls);
        //延时3秒
        setTimeout(DesignerLoaded, 3000);
    });
}


//打开流程到流程图
function OpenFlowToCanvas(node, id, text) {
	//alert(node.attributes.dtype);
    $(".mymask").show();
    if (node.attributes.dtype == "2") {//BPMN模式Flow_V=2 暂时不显示bpmn格式
        addTab(id, text, "Designer.jsp?FK_Flow=" + node.id + "&UserNo=" + WebUser.No + "&SID=" + WebUser.SID + "&Flow_V=1", node.iconCls);
    } else if (node.attributes.dtype == "1") {//CCBPM
        addTab(id, text, "Designer.jsp?FK_Flow=" + node.id + "&UserNo=" + WebUser.No + "&SID=" + WebUser.SID + "&Flow_V=1", node.iconCls);
    } else {
        //if (confirm("此流程版本为V1.0,是否执行升级为V2.0 ?")) {
            var attrs = node.attributes;    //这样写，是为了不将attributes里面原有的属性丢失，edited by liuxc,2015-11-05
            attrs.dtype = "1";
            attrs.Url = "Designer.jsp?FK_Flow=" + node.id + "&UserNo=" + WebUser.No + "&SID=" + WebUser.SID + "&Flow_V=1";
            $('#flowTree').tree('update', {
                target: node.target,
                attributes: attrs
            });
            addTab(id, text, "Designer.jsp?FK_Flow=" + id + "&UserNo=" + WebUser.No + "&SID=" + WebUser.SID + "&Flow_V=0", node.iconCls);
        //} else {
            //addTab(id, text, "DesignerSL.jsp?FK_Flow=" + id + "&UserNo=" + WebUser.No + "&SID=" + WebUser.SID + "&Flow_V=0", node.iconCls);
        //}
    }
    //延时3秒
    setTimeout(DesignerLoaded, 3000);
}

/// <summary>新建流程</summary>
function newFlow() {
    var currSort = $('#flowTree').tree('getSelected');
    var currSortId = "99";
    if (currSort && currSort.attributes["isparent"] != 0) {//edit by qin 2016/2/16
        currSortId = $('#flowTree').tree('getSelected').id; //liuxc,20150323
    }
    if(currSortId == "99"){
    	//currSortId="0";
    }
    var dgId = "iframDg";
    var url = "DialogCtr/NewFlow.htm?sort=" + currSortId + "&s=" + Math.random();
    OpenEasyUiDialog(url, dgId, '新建流程', 600, 394, 'icon-new', true, function () {
        var win = document.getElementById(dgId).contentWindow;
        var newFlowInfo = win.getNewFlowInfo();

        if (newFlowInfo.flowName == null || newFlowInfo.flowName.length == 0 || newFlowInfo.flowSort == null || newFlowInfo.flowSort.length == 0) {
            $.messager.alert('错误', '信息填写不完整', 'error');
            return false;
        }
        //传入参数
        var params = {
            method: "Do",
            doWhat: "NewFlow",
            para1: newFlowInfo.flowSort + ',' + newFlowInfo.flowName + ',' + newFlowInfo.dataStoreModel + ',' + newFlowInfo.pTable + ',' + newFlowInfo.flowCode + ',' + newFlowInfo.FlowVersion
        };
        $(".mymask").show();
        //访问服务
        ajaxService(params, function (data) {
            var jdata = $.parseJSON(data);
            if (jdata.success) {
                //在左侧流程树上增加新建的流程,并选中
                //获取新建流程所属的类别节点
                //todo:此处还有问题，类别id与流程id可能重复，重复就会出问题，解决方案有待进一步确定
                var parentNode = $('#flowTree').tree('find', "F" + newFlowInfo.flowSort);
                var node = $('#flowTree').tree('append', {
                    parent: parentNode.target,
                    data: [{
                        id: jdata.data.no,
                        text: jdata.data.name,
                        attributes: { isparent: '0',ttype:'FLOW', dtype: newFlowInfo.FlowVersion, MenuId: "mFlow", Url: "Designer.jsp?dtype=1&FK_Flow=@@id&UserNo=@@WebUser.No&SID=@@WebUser.SID" },
                        iconCls: 'icon-flow1',
                        checked: false
                    }]
                });
                var nodeData = {
                    id: jdata.data.no,
                    text: jdata.data.name,
                    attributes: { isparent: '0',ttype:'FLOW', dtype: newFlowInfo.FlowVersion, MenuId: "mFlow", Url: "Designer.jsp?dtype=1&FK_Flow=@@id&UserNo=@@WebUser.No&SID=@@WebUser.SID" },
                    iconCls: 'icon-flow1',
                    checked: false
                };
                //展开到指定节点
                $('#flowTree').tree('expandTo', $('#flowTree').tree('find', jdata.data.no).target);
                $('#flowTree').tree('select', $('#flowTree').tree('find', jdata.data.no).target);
                //在右侧流程设计区域打开新建的流程
                OpenFlowToCanvas(nodeData, jdata.data.no, jdata.data.name);
            }
            else {
                $.messager.alert('错误', '新建流程失败：' + jdata.msg, 'error');
            }
        }, this);
    }, null);
    //延时3秒
    setTimeout(DesignerLoaded, 3000);
}

/// <summary>新建流程类别子级</summary>
/// <param name="isSub" type="Boolean">是否是新建子级流程类别</param>
function newFlowSort(isSub) {
    var currSort = $('#flowTree').tree('getSelected');
    if (currSort == null || undefined == typeof(currSort.attributes["isparent"]) ||
    			currSort.attributes["isparent"] != '1' || (currSort.attributes["isroot"] == '1' && isSub == false)) return;
    var propName = (isSub ? '子级' : '同级') + '流程类别';
    OpenEasyUiSampleEditDialog(propName, '新建', null, function (val) {
        if (val == null || val.length == 0) {
            $.messager.alert('错误', '请输入' + propName + '！', 'error');
            return false;
        }

        //传入参数
        var doWhat = isSub ? 'NewSubFlowSort' : 'NewSameLevelFlowSort';
        var params = {
            method: "Do",
            doWhat: doWhat,
            para1: currSort.id + ',' + val
        };
        ajaxService(params, function (data) {
            var jdata = $.parseJSON(data);
            if (jdata.success) {
                var parentNode = isSub ? currSort : $('#flowTree').tree('getParent', currSort.target);

                $('#flowTree').tree('append', {
                    parent: parentNode.target,
                    data: [{
                        id: jdata.data,
                        text: val,
                        attributes: { isparent: '1', MenuId: "mFlowSort" },
                        checked: false,
                        iconCls: 'icon-tree_folder',
                        state: 'open',
                        children: []
                    }]
                });
               window.location.reload();//不加刷新会显示查询不到编号。
               $('#flowTree').tree('select', $('#flowTree').tree('find', jdata.data).target);
            }
            else {
                $.messager.alert('错误', '新建' + propName + '失败：' + jdata.msg, 'error');
            }
        }, this);
    }, null, false, 'icon-new');
}
//新建表单同类级别、子类级别
function newFrmSort(isSub) {
    var node = $('#formTree').tree('getSelected');
//    if (node == null || undefined == typeof(node.attributes["isparent"]) ||
//    		node.attributes["isparent"] != '1' || (node.attributes["isroot"] == '1' && isSub == false)) return;
    var propName = (isSub ? '子级' : '同级') + '流程类别';
    OpenEasyUiSampleEditDialog(propName, '新建', null, function (val) {
        if (val == null || val.length == 0) {
            $.messager.alert('错误', '请输入' + propName + '！', 'error');
            return false;
        }

        //传入参数
        var doWhat = isSub ? 'NewSubLevelFrmSort' : 'NewSameLevelFrmSort';
               
        var params = {
            method: "Do",
            doWhat: doWhat,
            para1: node.id +',' +val
        };
        ajaxService(params, function (data) {
            var jdata = $.parseJSON(data);
            if (jdata.success) {
                var parentNode = isSub ? node : $('#formTree').tree('getParent', node.target);
                
                $('#formTree').tree('append', {
                    parent: parentNode.target,
                    data: [{
                        id: jdata.data,
                        text: val,
                        attributes: { isparent: '1', MenuId: "mFormSort" },
                        checked: false,
                        iconCls: 'icon-tree_folder',
                        state: 'open',
                        children: []
                    }]
                });
                window.location.reload();
                $('#formTree').tree('select', $('#formTree').tree('find', jdata.data).target);
            }
            else {
                $.messager.alert('错误', '新建' + propName + '失败：' + jdata.msg, 'error');
            }
        }, this);
    }, null, false, 'icon-new');
}

//修改流程类别
function editFlowSort() {
    /// <summary>编辑流程类别</summary>
    var currSort = $('#flowTree').tree('getSelected');
    if (currSort == null) return;

    OpenEasyUiSampleEditDialog('流程类别', '编辑', currSort.text, function (val) {
        if (val == null || val.length == 0) {
            $.messager.alert('错误', '请输入流程类别！', 'error');
            return false;
        }
        //传入后台参数
        var params = {
            method: "Do",
            doWhat: "EditFlowSort",
            para1: currSort.id + ',' + val
        };

        ajaxService(params, function (data) {
            var jdata = $.parseJSON(data);
            if (jdata.success) {
                $('#flowTree').tree('update', {
                    target: currSort.target,
                    text: val
                });
            }
            else {
                $.messager.alert('错误', '编辑流程类别失败：' + jdata.msg, 'error');
            }
        });
    }, null, false, 'icon-edit');
}

//修改表单类别
function editFrmSort(){
	/// <summary>编辑流程类别</summary>
    var node = $('#formTree').tree('getSelected');
    //if (node == null) return;
    OpenEasyUiSampleEditDialog('表单类别', '编辑', node.text, function (val) {
        if (val == null || val.length == 0) {
            $.messager.alert('错误', '请输入表单类别！', 'error');
            return false;
        }
        //传入后台参数
        var params = {
            method: "Do",
            doWhat: "EditFrmSort",
            para1: node.id + ',' + val
        };
        ajaxService(params, function (data) {
            var jdata = $.parseJSON(data);
            if (jdata.success) {
                $('#formTree').tree('update', {
                    target: node.target,
                    text: val
                });
            }
            else {
                $.messager.alert('错误', '编辑流程类别失败：' + jdata.msg, 'error');
            }
        });
    }, null, false, 'icon-edit');
}

//删除流程类别
function deleteFlowSort() {
    var currSort = $('#flowTree').tree('getSelected');
    if (currSort == null || currSort.attributes.isparent == undefined) return;

    OpenEasyUiConfirm("你确定要删除名称为“" + currSort.text + "”的流程类别吗？", function () {
        //传入后台参数
        var params = {
            method: "Do",
            doWhat: "DelFlowSort",
            para1: currSort.id
        };
        ajaxService(params, function (data) {
            var jdata = $.parseJSON(data);
            if (jdata.success == true) {
                CloseAllTabs();
                $('#flowTree').tree('remove', currSort.target);
            } else if (jdata.success == false && jdata.reason == "havesubsorts") {
            	
                OpenEasyUiConfirm("所选类别下包含子流程类别，确定强制删除吗？", function () {
                    //传入后台参数
                    var params = {
                        method: "Do",
                        doWhat: "DelFlowSort",
                        force: "true",
                        para1: currSort.id
                    };
                    ajaxService(params, function (data) {
                        var jdata = $.parseJSON(data);
                        if (jdata.success == true) {
                            CloseAllTabs();
                            $('#flowTree').tree('remove', currSort.target);
                        } else {
                        	
                            $.messager.alert('错误', '删除流程类别失败：' + jdata.msg, 'error');
                        }
                    });

                });
            } else if (jdata.success == false && jdata.reason == "haveflows") {
            	
                OpenEasyUiConfirm("所选类别下包含流程，确定强制删除吗？", function () {
                    //传入后台参数
                    var params = {
                        method: "Do",
                        doWhat: "DelFlowSort",
                        force: "true",
                        para1: currSort.id
                    };
                    
                    ajaxService(params, function (data) {
                        var jdata = $.parseJSON(data);
                        if (jdata.success == true) {
                            CloseAllTabs();
                            $('#flowTree').tree('remove', currSort.target);
                        } else {
                        	
                            $.messager.alert('错误', '删除流程类别失败：' + jdata.msg, 'error');
                        }
                    });

                });
            }
            else {
            	
                $.messager.alert('错误', '删除流程类别失败：' + jdata.msg, 'error');
            }
        });
    });
}

//删除表单类别
function DeleteFrmSort(){
    var node = $('#formTree').tree('getSelected');
   // if (node == null || node.attributes.isparent == undefined) return;
    OpenEasyUiConfirm("你确定要删除名称为“" + node.text + "”的表单类别吗？", function () {
        //传入后台参数
        var params = {
            method: "Do",
            doWhat: "DelFrmSort",
            para1: node.id
        };
        ajaxService(params, function (data) {
            var jdata = $.parseJSON(data);
            if (jdata.success == true) {
                CloseAllTabs();
                $('#formTree').tree('remove', node.target);
            } /*else if (jdata.success == false && jdata.reason == "havesubsorts") {
                OpenEasyUiConfirm("所选类别下包含子表单类别，确定强制删除吗？", function () {
                    //传入后台参数
                    var params = {
                        method: "Do",
                        doWhat: "DelFrmSort",
                        force: "true",
                        para1: node.id
                    };
                    ajaxService(params, function (data) {
                        var jdata = $.parseJSON(data);
                        if (jdata.success == true) {
                            CloseAllTabs();
                            $('#formTree').tree('remove', node.target);
                        } else {
                            $.messager.alert('错误', '删除表单类别失败：' + jdata.msg, 'error');
                        }
                    });

                });
            } else if (jdata.success == false && jdata.reason == "haveflows") {
            	
                OpenEasyUiConfirm("所选类别下包含子类，确定强制删除吗？", function () {
                    //传入后台参数
                    var params = {
                        method: "Do",
                        doWhat: "DelFrmSort",
                        force: "true",
                        para1: node.id
                    };
                  
                    ajaxService(params, function (data) {
                        var jdata = $.parseJSON(data);
                        if (jdata.success == true) {
                            CloseAllTabs();
                            $('#formTree').tree('remove', node.target);
                        } else {
                            $.messager.alert('错误', '删除表单类别失败：' + jdata.msg, 'error');
                        }
                    });

                });
            }*/
            else {
            
                $.messager.alert('错误', '删除表单类别失败：此类别中含有表单或者子类别，不能删除。' , 'error');
            }
        });
    });
} 

function CloseAllTabs() {

    $('.tabs-inner span').each(function (i, n) {
        var t = $(n).text();
        if (t != '首页') {
            $('#tabs').tabs('close', t);
        }
    });
}

//查询流程

function SearchFlow() {
    url = "./../CCBPMDesigner/SearchFlow.jsp?Lang=CH";
    addTab("SPO", "查询流程", url);
}

//查询表单
function SearchForm() {
    url = "./../CCFormDesigner/SearchForm.jsp?Lang=CH";
    addTab("SPO", "查询表单", url);
}


//导入流程
function ImpFlow() {
    var currFlow = $('#flowTree').tree('getSelected');
    if (currFlow == null || currFlow.attributes.isparent != '0') {
        alert('没有获得当前的流程编号.');
        return;
    }
    var fk_flow = currFlow.id;
    url = "./../AttrFlow/imp.jsp?FK_Flow=" + fk_flow + "&Lang=CH";
    addTab(fk_flow + "PO", "导入流程模版", url);
}

//导出流程
function ExpFlow() {
    var currFlow = $('#flowTree').tree('getSelected');
    if (currFlow == null || currFlow.attributes.isparent != '0') {
        alert('没有获得当前的流程编号.');
        return;
    }

    var fk_flow = currFlow.id;
    url = "./../AttrFlow/Exp.jsp?FK_Flow=" + fk_flow + "&Lang=CH";
    addTab(fk_flow + "PO", "导出流程模版", url);
}

//导入流程
function ImpFlowBySort() {
    var currFlow = $('#flowTree').tree('getSelected');
    var fk_flow = currFlow.id;
    url = "./../AttrFlow/imp.jsp?FK_FlowSort=" + fk_flow + "&Lang=CH";
    addTab(fk_flow + "PO", "导入流程模版", url);
}

//导出流程
function ExpFlowBySort() {
    var currFlow = $('#flowTree').tree('getSelected');
    if (currFlow == null) {
        alert('没有获得当前的流程编号.');
        return;
    }

    var fk_flow = currFlow.id;
    url = "./../AttrFlow/Exp.jsp?FK_FlowSort=" + fk_flow + "&Lang=CH";
    addTab(fk_flow + "PO", "导出流程模版", url);
}


//删除流程
function DeleteFlow() {
    var currFlow = $('#flowTree').tree('getSelected');
    if (currFlow == null || currFlow.attributes.isparent != '0')
        return;
    OpenEasyUiConfirm("你确定要删除名称为“" + currFlow.text + "”的流程吗？", function () {
        var params = {
            method: "Do",
            doWhat: "DelFlow",
            para1: currFlow.id
        };
        ajaxService(params, function (data) {
            var jdata = $.parseJSON(data);
            if (jdata.success) {
                //如果右侧有打开该流程，则关闭
                var currFlowTab = $('#tabs').tabs('getTab', currFlow.text);
                if (currFlowTab) {
                    //todo:此处因为有关闭前事件，直接这样用会弹出提示关闭框，怎么解决有待进一步确认
                    $('#tabs').tabs('close', currFlow.text);
                }
                $('#flowTree').tree('remove', currFlow.target);
            }
            else {
                $.messager.alert('错误', '删除流程失败：' + jdata.msg, 'error');
            }
        }, this);
    });
}
//删除表单
function DeleteFrm() {
	var node = $('#formTree').tree('getSelected');
    if (!node) {
        return;  
    }
    OpenEasyUiConfirm("你确定要删除名称为“" + node.text + "”的表单吗？？", function () {
    	var params = {
                method: "Do",
                doWhat: "DelFrm",
                para1: node.id
    	}
    	ajaxService(params, function (data) {
            var jdata = $.parseJSON(data);
            if (jdata.success) {
                if (node) {
                    //todo:此处因为有关闭前事件，直接这样用会弹出提示关闭框，怎么解决有待进一步确认
                    $('#tabs').tabs('close', node.text);
                }
                $('#formTree').tree('remove', node.target);
                
            }
            else {
                $.messager.alert('错误', '删除表单失败：' + jdata.msg, 'error');
            }
        }, this);
    });  
}
 //表单属性
function FrmProperty(){
	var node = $('#formTree').tree('getSelected');
	if (!node) {
        return;
    }
    var FK_MapData = node.id;
	url = "../../Comm/RefFunc/UIEn.jsp?EnsName=BP.WF.Template.MapDataExts&PK="+FK_MapData;
	addTab(node, "表单属性", url);
}
//流程属性
function FlowProperty() {
    var currFlow = $('#flowTree').tree('getSelected');
    if (currFlow == null || currFlow.attributes.isparent != '0') return;

    var fk_flow = currFlow.id;
    url = "../../Admin/xap/DoPort.jsp?DoType=En&EnName=BP.WF.Flow&PK=" + fk_flow + "&Lang=CH";
    addTab(currFlow + "PO", "流程属性" + fk_flow, url);
    //WinOpen(url);
}

//新建表单
function newFrm() {
    var node = $('#formTree').tree('getSelected');
    if (!node) {
        return;
    }

    var url = "../CCFormDesigner/NewFrmGuide.jsp?Step=0";
    if (node.attributes) {
        if (node.attributes.TType == "SRC") {
            url += "&Src=" + node.id;
        }
        else if (node.attributes.TType == "FORMTYPE") {
            //在表单类别上单击，则传递表单类别
            var pnode = $('#formTree').tree('getParent', node.target);
            if (pnode != null) {
                url += "&FrmType=" + node.id;

                while (pnode && pnode.attributes) {
                    if (pnode.attributes.TType == "SRC") {
                        url += "&Src=" + pnode.id;
                        break;
                    }

                    pnode = $('#formTree').tree('getParent', pnode.target);
                }
            }
        }
    }

    addTab("NewFrm", "新建表单", url);
}

function designFrm() {
    var node = $('#formTree').tree('getSelected');
    if (!node) {
        return;
    }

    addTab("DesignerFrm" + node.id, "设计表单-" + node.text, "../CCFormDesigner/DesignerFrm.jsp?FK_MapData=" + node.id);
}

//新建数据源，added by liuxc,2015-10-7
function newSrc() {
    //  var url = "../../Comm/RefFunc/UIEn.jsp?EnsName=BP.Sys.SFDBSrcs";
    var url = "../../Comm/Sys/SFDBSrcNewGuide.jsp?EnsName=BP.Sys.SFDBSrcs";
    //OpenEasyUiDialog(url, "euiframeid", '新建数据源', 800, 495, 'icon-new');
    //todo:增加数据源后，在树上增加新结节的逻辑
    addTab("NewSrc", "新建数据源", url);
}

//新建数据源表
function newSrcTable() {
    var url = "../../Comm/Sys/SFGuide.jsp?DoType=New&FromApp=SL";
    //OpenEasyUiDialog(url, "euiframeid", '新建数据源表', 800, 495, 'icon-new');
    //todo:增加数据源表后，在树上增加新结节的逻辑
    addTab("NewSrcTable", "新建数据源表", url);
}

//数据源属性
function srcProperty() {
    var srcNode = $('#formTree').tree('getSelected');
    if (!srcNode || srcNode.attributes.TType != 'SRC') {
        $.messager.alert('错误', '请选择数据源！', 'error');
        return;
    }

    var url = '../../Comm/RefFunc/UIEn.jsp?EnsName=BP.Sys.SFDBSrcs&No=' + srcNode.id + '&t=' + Math.random();
    //OpenEasyUiDialog(url, "euiframeid", srcNode.text + ' 属性', 800, 495, 'icon-edit');
    //todo:数据源属性修改后，在树上的结节信息的相应变更逻辑
    addTab(srcNode.id, srcNode.text, url, srcNode.iconCls);
}

//数据源表属性
function srcTableProperty() {
    var srcTableNode = $('#formTree').tree('getSelected');
    if (!srcTableNode || srcTableNode.attributes.ttype != 'SRCTABLE') {
        $.messager.alert('错误', '请选择数据源表！', 'error');
        return;
    }

    var url = '../../MapDef/Do.jsp?DoType=EditSFTable&RefNo=' + srcTableNode.id + '&t=' + Math.random();
    //OpenEasyUiDialog(url, "euiframeid", srcTableNode.text + ' 属性', 800, 495, 'icon-edit');
    //todo:数据源表属性修改后，在树上的结节信息的相应变更逻辑
    addTab(srcTableNode.id, srcTableNode.text, url, srcTableNode.iconCls);
}

//数据源表数据查看/编辑
function srcTableData() {
    var srcTableNode = $('#formTree').tree('getSelected');
    if (!srcTableNode || srcTableNode.attributes.ttype != 'SRCTABLE') {
        $.messager.alert('错误', '请选择数据源表！', 'error');
        return;
    }

    var url = "../../MapDef/SFTableEditData.jsp?RefNo=" + srcTableNode.id; //todo:此处BP.Pub.Days样式的，页面报错
    //OpenEasyUiDialog(url, "euiframeid", srcTableNode.text + ' 数据编辑', 800, 495, 'icon-edit');
    addTab(srcTableNode.id, srcTableNode.text + ' 数据编辑', url, srcTableNode.iconCls);
}

//打开表单
function openForm(id, text) {
    if (!id || !text) {
        var formNode = $('#formTree').tree('getSelected');
        if (!formNode || formNode.attributes.TType != 'FORM') {
            $.messager.alert('错误', '请选择表单！', 'error');
            return;
        }

        id = formNode.id;
        text = formNode.text;
    }

    addTab(id, text, "../../MapDef/CCForm/Frm.jsp?FK_MapData=" + id + "&UserNo=" + WebUser.No + "&SID=" + WebUser.SID, formNode.iconCls);
}

/*组织结构树操作开始*/
function getSelected(sTreeId, sName, oChecks) {
    var node = $("#" + sTreeId).tree("getSelected");

    if (!node) {
        $.messager.alert("提示", "请选择" + (sName ? sName : "树结点") + "！", "warning");
        return null;
    }

    if (!oChecks) {
        return node;
    }

    var pass = true;
    var exist = false;

    for (var field in oChecks) {
        exist = false;

        if (node[field]) {
            exist = true;

            if (node[field] != oChecks[field]) {
                pass = false;
                break;
            }
        }

        if (!exist && node.attributes && node.attributes[field]) {
            exist = true;

            if (node.attributes[field] != oChecks[field]) {
                pass = false;
                break;
            }
        }

        if (!exist) {
            pass = false;
        }

        if (!pass) {
            break;
        }
    }

    if (!pass) {
        $.messager.alert("提示", "请选择" + (sName ? sName : "树结点") + "！检查规则不通过！", "warning");
        return null;
    }

    return node;
}

function newDept() {
    var node = getSelected(ORG_TREE, "部门", { TType: "DEPT" });
    if (!node) return;

    var pnode = $("#" + ORG_TREE).tree("getParent", node.target);
    addTab("NewDept", "新建同级部门", "../../Comm/RefFunc/UIEn.jsp?EnsName=BP.GPM.Depts&ParentNo=" + node.id, "icon-new");
}

function newSubDept() {

}

function editDept() {

}

function deleteDept() {

}

function newStation() {

}

function editStation() {

}

function deleteStation() {

}

function newEmp() {

}

function editEmp() {

}

function deleteEmp() {

}
/*组织结构树操作结束*/

//打开窗体
function WinOpen(url) {
    var winWidth = 850;
    var winHeight = 680;
    if (screen && screen.availWidth) {
        winWidth = screen.availWidth;
        winHeight = screen.availHeight - 36;
    }
    window.open(url, "_blank", "height=" + winHeight + ",width=" + winWidth + ",top=0,left=0,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no");
}
//用户信息
var WebUser = { No: '', Name: '', FK_Dept: '', SID: '' };
function InitUserInfo() {
    var params = {
        method: "WebUserInfo"
    };
    ajaxService(params, function (data) {
        var jdata = $.parseJSON(data);
        if (jdata.success) {
            WebUser.No = jdata.data.No;
            WebUser.Name = jdata.data.Name;
            WebUser.FK_Dept = jdata.data.FK_Dept;
            WebUser.SID = jdata.data.SID;
        }
        else {
            alert('获取当前登录用户失败：' + jdata.msg);
            window.location.href = "Login.jsp?DoType=Logout";
        }
    }, this);
}

//2017.01.20新增
function GenerStructureTree(parentrootid, pnodeid, treeid) {
    ajaxService({ action: "GetStructureTreeRoot", parentrootid: parentrootid }, function (re) {
        var data = $.parseJSON(re);
        var roottarget;

        if (pnodeid) {
            roottarget = $("#" + treeid).tree("find", pnodeid).target;
        }

        $("#" + treeid).tree("append", {
            parent: roottarget,
            data: [{
                id: "DEPT_" + data[0].NO,
                text: data[0].NAME,
                state: "closed",
                attributes: {TType: "DEPT", IsLoad: false},
                children: [{
                    text: "加载中..."
                }]
            }]
        });

        $("#" + treeid).tree({
            onExpand: function (node) {
                ShowSubDepts(node, treeid);
            }
        });
    });
}
function ShowSubDepts(node, treeid) {
    if (node.attributes.IsLoad) {
        return;
    }
    var isStation = node.attributes.TType == "STATION";
    var data;
    if (isStation) {
        var deptid = node.attributes.DeptId;
        var stid = node.attributes.StationId;
        ajaxService({ action: "GetEmpsByStation", deptid: deptid, stationid: stid }, function (re) {
            data = $.parseJSON(re);
            var children = $("#" + treeid).tree('getChildren', node.target);
            if (children && children.length >= 1) {
                if (children[0].text == "加载中...") {
                    $("#" + treeid).tree("remove", children[0].target);
                }
            }

            $.each(data, function () {
                $("#" + treeid).tree("append", {
                    parent: node.target,
                    data: [{
                        id: this.PARENTNO +  "|" + this.NO,
                        text: this.NAME,
                        iconCls: "icon-user",
                        attributes: { TType: "EMP", StationId: stid, DeptId: deptid }
                    }]
                });
            });

            node.attributes.IsLoad = true;
        });
    }
    else {
        var deptid = node.id.replace(/DEPT_/g, "");
        ajaxService({ action: "GetSubDepts", rootid: deptid }, function (re) {
            data = $.parseJSON(re);

            var children = $("#" + treeid).tree('getChildren', node.target);
            if (children && children.length >= 1) {
                if (children[0].text == "加载中...") {
                    $("#" + treeid).tree("remove", children[0].target);
                }
            }

            $.each(data, function () {
                $("#" + treeid).tree("append", {
                    parent: node.target,
                    data: [{
                        id: this.TTYPE + "_" + this.NO,
                        text: this.NAME,
                        iconCls: this.TTYPE == "STATION" ? "icon-station" : "icon-tree_folder",
                        state: "closed",
                        attributes: {
                            TType: this.TTYPE,
                            IsLoad: false,
                            StationId: this.TTYPE == "STATION" ? this.NO : undefined,
                            DeptId: this.TTYPE == "STATION" ? deptid : undefined 
                        },
                        children: [{
                            text: "加载中..."
                        }]
                    }]
                });
            });

            node.attributes.IsLoad = true;
        });
    }
}



var treesObj;   //保存功能区处理对象

$(function () {
    $(".mymask").show();
    InitUserInfo();
    treesObj = new FuncTrees("menuTab");
    treesObj.loadTrees();
    //定义等待界面的位置
    $(".mymaskContainer").offset({ left: ($(document).innerWidth() - 120) / 2, top: ($(document).innerHeight() - 50) / 2 });
    $(".mymask").hide();
});

