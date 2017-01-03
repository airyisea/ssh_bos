<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
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
	function doAdd(){
		$('#addStaffWindow').window("open");
	}
	
	function doView(){
		$('#queryStaffWindow').window("open");
	}
	
	function doDelete(){
		var select = $('#grid').datagrid('getSelections');
		if(select != null && select != "") {
			var ids = new Array();
			for(var i = 0 ; i < select.length; i++) {
				if(select[i].deltag == '1') {
					$.messager.alert("错误","收派员:" + select[i].name + "已经作废!","warning");
					return;
				}
				ids.push(select[i].id);
			}
			$.post("${pageContext.request.contextPath}/basic/staff_deleteBatch",{ids : ids.join(",")},function(data){
				if(data){
					$.messager.alert("成功","作废成功！","info");
					$("#grid").datagrid("uncheckAll");
					$("#grid").datagrid("reload");
				}else{
					$.messager.alert("失败","作废失败","error");
				}
			});
		}else {
			$.messager.alert("错误","请至少选择一行","warning");
		}
	}
	
	function doRestore(){
		var select = $('#grid').datagrid('getSelections');
		if(select != null && select != "") {
			var ids = new Array();
			for(var i = 0 ; i < select.length; i++) {
				if(select[i].deltag == '0') {
					$.messager.alert("错误","收派员:" + select[i].name + "没有作废!","warning");
					return;
				}
				ids.push(select[i].id);
			}
			$.post("${pageContext.request.contextPath}/basic/staff_restoreBatch",{ids : ids.join(",")},function(data){
				if(data){
					$.messager.alert("成功","还原成功！","info");
					$("#grid").datagrid("uncheckAll");
					$("#grid").datagrid("reload");
				}else{
					$.messager.alert("失败","还原失败","error");
				}
			});
		}else {
			$.messager.alert("错误","请至少选择一行","warning");
		}
	}
	
	function doUpate(index, data){
		$('#addStaffWindow').window("open");
		$("#tel").validatebox("remove");
		$('#staffForm').form('load',data);
	}	
	
	//工具栏
	var toolbar = [ {
		id : 'button-view',	
		text : '查询',
		iconCls : 'icon-search',
		handler : doView
	}, {
		id : 'button-add',
		text : '增加',
		iconCls : 'icon-add',
		handler : doAdd
	}, {
		id : 'button-delete',
		text : '作废',
		iconCls : 'icon-cancel',
		handler : doDelete
	},{
		id : 'button-save',
		text : '还原',
		iconCls : 'icon-save',
		handler : doRestore
	}];
	// 定义列
	var columns = [ [ {
		field : 'id',
		checkbox : true,
	},{
		field : 'name',
		title : '姓名',
		width : 120,
		align : 'center',
		sortable : true		
	}, {
		field : 'telephone',
		title : '手机号',
		width : 120,
		align : 'center',
		sortable : true
	}, {
		field : 'haspda',
		title : '是否有PDA',
		width : 120,
		align : 'center',
		formatter : function(data,row, index){
			if(data=="1"){
				return "有";
			}else{
				return "无";
			}
		},
		styler : function(data,row, index) {
			if(data=="1") {
				return "color:blue;";
			}else {
				return "color:red;";
			}
		}
	}, {
		field : 'deltag',
		title : '是否作废',
		width : 120,
		align : 'center',
		formatter : function(data,row, index){
			if(data=="0"){
				return "正常使用"
			}else{
				return "已作废";
			}
		},
		styler : function(data,row, index) {
			if(data=="1") {
				return "color:red;";
			}
		}
	}, {
		field : 'standard',
		title : '取派标准',
		width : 120,
		align : 'center',
		sortable : true
	}, {
		field : 'station',
		title : '所属单位',
		width : 200,
		align : 'center',
		sortable : true
	} ] ];
	
	//自定义校验器
	$.extend($.fn.validatebox.defaults.rules, { 
		telephone: { 
		validator: function(value, param){
			var reg = /^1[3|4|5|7|8]\d{9}$/;
			return reg.test(value); 
		}, 
		message: '请输入正确的手机号码' 
		},
		uniquePhone: {
			validator: function(value, param){ 
			var flag;
			$.ajax({
				url:"${pageContext.request.contextPath}/basic/staff_checkPhone",
				type : "POST",
				timeout : 6000,
				data : {telephone:value},
				async : false,
				success : function(data, textStatus, jqXHR){
					flag = data;
				}
			});
			return flag;
			}, 
			message: '手机号已存在'
		}
		
	}); 


	
	$(function(){
		// 先将body隐藏，再显示，不会出现页面刷新效果
		$("body").css({visibility:"visible"});
		
		// 取派员信息表格
		$('#grid').datagrid( {
			iconCls : 'icon-forward',
			fit : true,
			border : false,
			rownumbers : true,
			striped : true,
			pageList: [2,4,10],
			pageSize : 2,
			pagination : true,
			toolbar : toolbar,
			url : "${pageContext.request.contextPath}/basic/staff_queryPage",
			idField : 'id',
			columns : columns,
			onDblClickRow : doUpate
		});
		
		// 添加取派员窗口
		$('#addStaffWindow').window({
	        width: 400,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false,
	        onBeforeClose : function() {
	        	$("#staffForm")[0].reset();
	        	$("#_id").val("");
	        }
	    });
		
		$("#save").click(function(){
			if($("#staffForm").form("validate")) {
				$("#staffForm").submit();
			}
		});
		// 查询取派员窗口
		$('#queryStaffWindow').window({
	        width: 300,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 250,
	        resizable:false,
	        onBeforeClose : function() {
	        	$("#queryStaffForm")[0].reset();
	        	$('#qstandard').combobox('unselect');
	        }
	    });
		
		$("#save").click(function(){
			if($("#staffForm").form("validate")) {
				$("#staffForm").submit();
			}
		});
		
		$("#query").click(function(){
			$('#grid').datagrid('load',{name:$("#qname").val(),telephone:$("#qtelephone").val(),station:$("#qstation").val(),standard:$("#qstandard").combobox('getValue')});
			$('#queryStaffWindow').window("close");
		});
		
	});
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
	
</script>	
</head>
<body class="easyui-layout" style="visibility:hidden;">
	<div region="center" border="false">
    	<table id="grid"></table>
	</div>
	<div class="easyui-window" title="取派员操作" id="addStaffWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="javascript:void(0);" class="easyui-linkbutton" plain="true" >保存</a>
			</div>
		</div>
		
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form action="${pageContext.request.contextPath}/basic/staff_add" id="staffForm" method="post">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">取派员信息</td>
					</tr>
					<tr>
						<td>姓名</td>
						<td>
						<input type="hidden" name="id" id="_id"/>
						<input type="text" name="name" class="easyui-validatebox" required="true"/>
						</td>
					</tr>
					<tr>
						<td>手机</td>
						<td><input type="text" name="telephone" id="tel" class="easyui-validatebox" required="true" data-options="validType:['telephone','uniquePhone']"/></td>
					</tr>
					<tr>
						<td>单位</td>
						<td><input type="text" name="station" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td colspan="2">
						<input type="checkbox" name="haspda" value="1" />
						是否有PDA</td>
					</tr>
					<tr>
						<td>取派标准</td>
						<td>
							<input type="text" name="standard" class="easyui-combobox" required="true" 
							data-options="valueField:'name',textField:'name',url:'${pageContext.request.contextPath}/basic/standard_nameList'"/>  
						</td>
					</tr>
					</table>
			</form>
		</div>
	</div>
	<div class="easyui-window" title="取派员查询" id="queryStaffWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="query" icon="icon-search" href="javascript:void(0);" class="easyui-linkbutton" plain="true" >查询</a>
			</div>
		</div>
		
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form action="${pageContext.request.contextPath}/basic/staff_add" id="queryStaffForm" method="post">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">查询取派员</td>
					</tr>
					<tr>
						<td>姓名</td>
						<td>
						<input type="text" name="name" id="qname"/>
						</td>
					</tr>
					<tr>
						<td>手机</td>
						<td><input type="text" name="telephone" id="qtelephone"/></td>
					</tr>
					<tr>
						<td>单位</td>
						<td><input type="text" name="station" id="qstation"/></td>
					</tr>
					<tr>
						<td>取派标准</td>
						<td>
							<input type="text" name="standard" class="easyui-combobox" id="qstandard"
							data-options="valueField:'name',textField:'name',url:'${pageContext.request.contextPath}/basic/standard_nameList'"/>  
						</td>
					</tr>
					</table>
			</form>
		</div>
	</div>
</body>
</html>	