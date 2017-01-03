<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>业务通知单</title>
<!-- 导入jquery核心类库 -->
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
<!-- 导入easyui类库 -->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/ext/portal.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/default.css">	
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/ext/jquery.portal.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/ext/jquery.cookie.js"></script>
<script
	src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"
	type="text/javascript"></script>
<script type="text/javascript">
	
	function doRepeat(){
		var select = $('#grid').datagrid('getSelections');
		var ids = new Array();
		if(select.length > 0) {
			for(var i = 0; i < select.length; i++) {
				if(select[i].type == '销') {
					$.messager.alert("错误",select[i].id +"已销单","warning");
					return;
				}
				ids.push(select[i].id);
			}
			$.post("${pageContext.request.contextPath}/qp/workbill_repeat",{"ids" : ids.join(",")},function(data){
				if(data) {
					$.messager.alert("成功","追单成功","info");
					$('#grid').datagrid('reload');
					$('#grid').datagrid('clearSelections');
				}else {
					$.messager.alert("失败","追单失败","error");
				}
			});
		}
	}
	
	function doCancel(){
		var select = $('#grid').datagrid('getSelections');
		var ids = new Array();
		if(select.length > 0) {
			for(var i = 0; i < select.length; i++) {
				ids.push(select[i].id);
			}
			$.post("${pageContext.request.contextPath}/qp/workbill_cancel",{"ids" : ids.join(",")},function(data){
				if(data) {
					$.messager.alert("成功","销单成功","info");
					$('#grid').datagrid('reload');
					$('#grid').datagrid('clearSelections');
				}else {
					$.messager.alert("失败","销单失败","error");
				}
			});
		}
	}
	
	function doSearch(){
		$('#searchWindow').window("open");
	}
	
	function doAdd(){
		location.href="${pageContext.request.contextPath }/page_qupai_noticebill_add.action"
	}
	
	//工具栏
	var toolbar = [ {
		id : 'button-search',	
		text : '查询',
		iconCls : 'icon-search',
		handler : doSearch
	}, {
		id : 'button-add',
		text : '添加新单',
		iconCls : 'icon-save',
		handler : doAdd
	}, {
		id : 'button-repeat',
		text : '追单',
		iconCls : 'icon-redo',
		handler : doRepeat
	}, {
		id : 'button-cancel',	
		text : '销单',
		iconCls : 'icon-cancel',
		handler : doCancel
	}];
	// 定义列
	var columns = [ [ {
		field : 'id',
		checkbox : true,
	}, {
		field : 'noticebill_id',
		title : '通知单号',
		width : 210,
		align : 'center',
		formatter : function(value,data,index) {
			return data.noticeBill.id;
		}
	},{
		field : 'type',
		title : '工单类型',
		width : 120,
		align : 'center',
		styler: function(value,row,index){
			if(value=='销') {
				return 'color:red';
			}
		}
	}, {
		field : 'pickstate',
		title : '取件状态',
		width : 120,
		align : 'center',
		styler: function(value,row,index){
			if(value=='已注销') {
				return 'color:red';
			}
		}
	}, {
		field : 'buildtime',
		title : '工单生成时间',
		width : 120,
		align : 'center',
		formatter : function(data, row, index) {
			return data.replace("T", " ");
		}
	}, {
		field : 'attachbilltimes',
		title : '追单次数',
		width : 120,
		align : 'center'
	}, {
		field : 'staff.name',
		title : '取派员',
		width : 100,
		align : 'center',
		formatter : function(value,data,index) {
			return data.staff.name;
		}
	}, {
		field : 'staff.telephone',
		title : '联系方式',
		width : 100,
		align : 'center',
		formatter : function(value,data,index) {
			return data.noticeBill.telephone;
		}
	} ] ];
	
	$(function(){
		// 先将body隐藏，再显示，不会出现页面刷新效果
		$("body").css({visibility:"visible"});
		
		// 收派标准数据表格
		$('#grid').datagrid( {
			iconCls : 'icon-forward',
			fit : true,
			border : true,
			rownumbers : true,
			striped : true,
			pageList: [3,5,10],
			pagination : true,
			toolbar : toolbar,
			url :  "${pageContext.request.contextPath}/qp/workbill_queryPage",
			idField : 'id',
			columns : columns,
			onDblClickRow : doDblClickRow
		});
		
		// 查询分区
		$('#searchWindow').window({
	        title: '查询分区',
	        width: 400,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false
	    });
		$("#btn").click(function(){
			var param = $("#searchForm").serializeJson();
			$("#grid").datagrid('load',param);
			$("#searchForm").get(0).reset();// 重置查询表单
			$("#searchWindow").window("close"); // 关闭窗口
			
		});
	});

	function doDblClickRow(){
		alert("双击表格数据...");
	}
	
	//去除样式js 封装函数
	$.extend($.fn.validatebox.methods, {
		//  去除validatebox 非法校验样式
	    remove: function (jq, newposition) {
              return $(jq).removeClass("validatebox-text validatebox-invalid");
	    },
    	//样式还原
	    reduce: function (jq, newposition) {
	        return jq.each(function () {
	            var opt = $(this).data().validatebox.options;
	            $(this).addClass("validatebox-text").validatebox(opt);
	        });
	    }
	});
	
	$.fn.serializeJson=function(){  
        var serializeObj={};  
        var array=this.serializeArray();  
        var str=this.serialize();  
        $(array).each(function(){  
            if(serializeObj[this.name]){  
                if($.isArray(serializeObj[this.name])){  
                    serializeObj[this.name].push(this.value);  
                }else{  
                    serializeObj[this.name]=[serializeObj[this.name],this.value];  
                }  
            }else{  
                serializeObj[this.name]=this.value;   
            }  
        });  
        return serializeObj;  
    }; 
</script>	
</head>
<body class="easyui-layout" style="visibility:hidden;">
	<div region="center" border="false">
    	<table id="grid"></table>
	</div>
	
	<!-- 查询分区 -->
	<div class="easyui-window" title="查询窗口" id="searchWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div style="overflow:auto;padding:5px;" border="false">
			<form id="searchForm">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">查询条件</td>
					</tr>
					<tr>
						<td>客户电话</td>
						<td><input type="text" class="easyui-validatebox" required="true" name="noticeBill.telephone"/></td>
					</tr>
					<tr>
						<td>取派员</td>
						<td><input type="text" class="easyui-validatebox" required="true" name="staff.name"/></td>
					</tr>
					<tr>
						<td>受理时间</td>
						<td><input type="text" class="easyui-datebox" required="true" name="buildtime"/></td>
					</tr>
					<tr>
						<td colspan="2"><a id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a> </td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>