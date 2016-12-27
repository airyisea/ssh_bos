<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理分区</title>
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
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/oneclickupload/jquery.ocupload-1.1.2.js"></script>
<script type="text/javascript">
	var $query;
	function doAdd(){
		$("#tr_id").css("display","");
		$('#addSubareaWindow').window("open");
	}
	
	function doEdit(index,data){
		$("#_id").validatebox("remove");
		$("#addSubareaForm").form("load",data);
		$("#_region").combobox("select",data.region.id);
		$("#tr_id").css("display","none");
		$('#addSubareaWindow').window("open");
	}
	
	/* function doDelete(){
		alert("删除...");
	}
	 */
	function doSearch(){
		$('#searchWindow').window("open");
	}
	
	function doExport(){
		$("#queryForm").form("load",$query);
		$("#queryForm").submit();
		$("#queryForm")[0].reset();
	}
	
	//工具栏
	var toolbar = [ {
		id : 'button-search',	
		text : '查询',
		iconCls : 'icon-search',
		handler : doSearch
	}, {
		id : 'button-add',
		text : '增加',
		iconCls : 'icon-add',
		handler : doAdd
	}/*, {
		id : 'button-edit',	
		text : '修改',
		iconCls : 'icon-edit',
		handler : doEdit
	} ,{
		id : 'button-delete',
		text : '删除',
		iconCls : 'icon-cancel',
		handler : doDelete
	} */,{
		id : 'button-import',
		text : '导入',
		iconCls : 'icon-redo',
	},{
		id : 'button-export',
		text : '导出',
		iconCls : 'icon-undo',
		handler : doExport
	}];
	// 定义列
	var columns = [ [ {
		field : 'id',
		checkbox : true,
	}, {
		field : 'showid',
		title : '分区编号',
		width : 120,
		align : 'center',
		formatter : function(data,row ,index){
			return row.id;
		}
	},{
		field : 'province',
		title : '省',
		width : 120,
		align : 'center',
		formatter : function(data,row ,index){
			return row.region.province;
		}
	}, {
		field : 'city',
		title : '市',
		width : 120,
		align : 'center',
		formatter : function(data,row,index){
			return row.region.city;
		}
	}, {
		field : 'district',
		title : '区',
		width : 120,
		align : 'center',
		formatter : function(data,row ,index){
			return row.region.district;
		}
	}, {
		field : 'addresskey',
		title : '关键字',
		width : 120,
		align : 'center'
	}, {
		field : 'startnum',
		title : '起始号',
		width : 100,
		align : 'center'
	}, {
		field : 'endnum',
		title : '终止号',
		width : 100,
		align : 'center'
	} , {
		field : 'single',
		title : '单双号',
		width : 100,
		align : 'center',
		formatter : function(data,row ,index){
			switch(data) {
			case '0':return "单双号";
			case '1':return "单号";
			case '2':return "双号";
			}
		}
	} , {
		field : 'position',
		title : '位置',
		width : 200,
		align : 'center'
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
			pageList: [30,50,100],
			pagination : true,
			toolbar : toolbar,
			url : "${pageContext.request.contextPath}/basic/subarea_queryPage",
			idField : 'id',
			columns : columns,
			onDblClickRow : doEdit
		});
		
		// 添加、修改分区
		$('#addSubareaWindow').window({
	        title: '添加修改分区',
	        width: 600,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false,
	        onBeforeClose: function(){
	        	//$("#_id").validatebox("remove");
	        	$('#addSubareaForm')[0].reset();
	        }
	    });
		
		// 查询分区
		$('#searchWindow').window({
	        title: '查询分区',
	        width: 400,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false,
	        onBeforeClose: function(){
	        	$('#queryForm')[0].reset();
	        }
	    });
		
		$("#button-import").upload({
			action : "${pageContext.request.contextPath}/basic/subarea_importData",
			name : "upload",
			enctype : "multipart/form-data",
			onSelect : function(){
				this.autoSubmit = false;
				var reg = /^.+\.xls[x]?$/;
				if(reg.test(this.filename())) {
					this.submit();
				}else {
					$.messager.alert("错误","只能接收xls或xlsx文件","warning");
				}
			},
			onComplete : function(data) {
				if(eval("(" +data+ ")")) {
					$.messager.alert("成功","导入完成","info");
					$("#grid").datagrid('load');
				}else {
					$.messager.alert("错误","导入失败,请联系管理员","error");
				}
			}
						
		});
		
		$("#save").click(function(){
			if($("#addSubareaForm").form("validate")) {
				$("#addSubareaForm").submit();
			}
		});
		$("#btn").click(function(){
			$query = $("#queryForm").serializeJson();
			$("#grid").datagrid('load',$query);
			$('#searchWindow').window("close");
		});
		
	});

	
	//自定义校验规则
	$.extend($.fn.validatebox.defaults.rules, { 
		uniqueId: { 
			validator: function(value, param){ 
				var flag;
				$.ajax({
					url:"${pageContext.request.contextPath}/basic/subarea_checkId",
					type : "POST",
					timeout : 6000,
					data : {id:value},
					async : false,
					success : function(data, textStatus, jqXHR){
						flag = data;
					}
				});
				return flag;
				}, 
			message: '分区编码已存在'
		}
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
	<!-- 添加 修改分区 -->
	<div class="easyui-window" title="分区添加修改" id="addSubareaWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >保存</a>
			</div>
		</div>
		
		<div style="overflow:auto;padding:5px;" border="false">
			<form id="addSubareaForm" action="${pageContext.request.contextPath}/basic/subarea_add" method="post">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">分区信息</td>
					</tr>
					<tr id="tr_id">
						<td>分区编码</td>
						<td><input type="text" name="id" id="_id" class="easyui-validatebox" required="true" validType="uniqueId"/></td>
					</tr>
					<tr>
						<td>选择区域</td>
						<td>
							<input class="easyui-combobox" name="region.id" id="_region" 
    							data-options="mode:'remote',valueField:'id',textField:'name',url:'${pageContext.request.contextPath}/basic/region_list'" />  
						</td>
					</tr>
					<tr>
						<td>关键字</td>
						<td><input type="text" name="addresskey" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>起始号</td>
						<td><input type="text" name="startnum" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>终止号</td>
						<td><input type="text" name="endnum" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>单双号</td>
						<td>
							<select class="easyui-combobox" name="single" style="width:150px;">  
							    <option value="0">单双号</option>  
							    <option value="1">单号</option>  
							    <option value="2">双号</option>  
							</select> 
						</td>
					</tr>
					<tr>
						<td>位置信息</td>
						<td><input type="text" name="position" class="easyui-validatebox" required="true" style="width:250px;"/></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<!-- 查询分区 -->
	<div class="easyui-window" title="查询分区窗口" id="searchWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div style="overflow:auto;padding:5px;" border="false">
			<form id="queryForm" method="post" action="${pageContext.request.contextPath}/basic/subarea_exportData">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">查询条件</td>
					</tr>
					<tr>
						<td>省</td>
						<td><input type="text" name="region.province"/></td>
					</tr>
					<tr>
						<td>市</td>
						<td><input type="text" name="region.city"/></td>
					</tr>
					<tr>
						<td>区（县）</td>
						<td><input type="text" name="region.district"/></td>
					</tr>
					<tr>
						<td>定区编码</td>
						<td><input type="text" name="decidedZone.id"/></td>
					</tr>
					<tr>
						<td>关键字</td>
						<td><input type="text" name="addresskey"/></td>
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