<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>区域设置</title>
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
<script
	src="${pageContext.request.contextPath }/js/oneclickupload/jquery.ocupload-1.1.2.js"
	type="text/javascript"></script>
<script type="text/javascript">
	function doAdd(){
		$('#addRegionWindow').window("open");
	}
	
	function doView(){
		$('#queryRegionWindow').window("open");
	}
	
	
	function doUpdate(index,data) {
		$("#addRegionWindow").window("open");
		$("#_id").validatebox("remove");
		$("#_id").prop("readonly","readonly");
		$("#addRegionForm").form("load",data);
	}
	
	//工具栏
	var toolbar = [ {
		id : 'button-search',	
		text : '查询',
		iconCls : 'icon-search',
		handler : doView
	}, {
		id : 'button-add',
		text : '增加',
		iconCls : 'icon-add',
		handler : doAdd
	}, {
		id : 'button-import',
		text : '导入',
		iconCls : 'icon-redo'
	}];
	// 定义列
	var columns = [ [ {
		field : 'id',
		checkbox : true,
	},{
		field : 'province',
		title : '省',
		width : 120,
		align : 'center'
	}, {
		field : 'city',
		title : '市',
		width : 120,
		align : 'center'
	}, {
		field : 'district',
		title : '区',
		width : 120,
		align : 'center'
	}, {
		field : 'postcode',
		title : '邮编',
		width : 120,
		align : 'center',
		sortable : true
	}, {
		field : 'shortcode',
		title : '简码',
		width : 120,
		align : 'center',
		sortable : true
	}, {
		field : 'citycode',
		title : '城市编码',
		width : 200,
		align : 'center',
		sortable : true
	} ] ];
	
	//自定义校验规则
	$.extend($.fn.validatebox.defaults.rules, { 
		uniqueId: { 
			validator: function(value, param){ 
				var flag;
				$.ajax({
					url:"${pageContext.request.contextPath}/basic/region_checkId",
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
			message: '区域编码已存在'
		} 
		}); 


	
	$(function(){
		// 先将body隐藏，再显示，不会出现页面刷新效果
		$("body").css({visibility:"visible"});
		
		// 收派标准数据表格
		$('#grid').datagrid( {
			iconCls : 'icon-forward',
			fit : true,
			border : false,
			rownumbers : true,
			striped : true,
			pageList: [5,10,15],
			pagination : true,
			toolbar : toolbar,
			url : "${pageContext.request.contextPath}/basic/region_queryPage",
			idField : 'id',
			columns : columns,
			onDblClickRow : doUpdate
		});
		
		// 添加、修改区域窗口
		$('#addRegionWindow').window({
	        title: '添加修改区域',
	        width: 400,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false,
	        onBeforeClose : function(){
	        	$("#addRegionForm")[0].reset();
	        }
	    });
		
		// 查询区域窗口
		$('#queryRegionWindow').window({
	        width: 400,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false,
	        onBeforeClose : function(){
	        	$("#queryRegionForm")[0].reset();
	        }
	    });
		
		$("#button-import").upload({
			action : "${pageContext.request.contextPath}/basic/region_importRegion",
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
				}else {
					$.messager.alert("错误","导入失败,请联系管理员","error");
				}
			}
						
		});
		
		$("#save").click(function(){
			if($("#addRegionForm").form("validate")) {
				$("#addRegionForm").submit();
			}
		});
		$("#query").click(function(){
			$("#grid").datagrid("load",$("#queryRegionForm").serializeJson());
			$('#queryRegionWindow').window("close");
			
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
	<!-- 添加区域窗口 -->
	<div class="easyui-window" title="区域添加修改" id="addRegionWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >保存</a>
			</div>
		</div>
		
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form id="addRegionForm" method="post" action="${pageContext.request.contextPath}/basic/region_add">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">区域信息</td>
					</tr>
					<tr>
						<td>区域编码</td>
						<td><input type="text" name="id" class="easyui-validatebox" 
						required="true" id="_id"  data-options="validType:'uniqueId'"/></td>
					</tr>
					<tr>
						<td>省</td>
						<td><input type="text" name="province" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>市</td>
						<td><input type="text" name="city" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>区</td>
						<td><input type="text" name="district" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>邮编</td>
						<td><input type="text" name="postcode" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>简码</td>
						<td><input type="text" name="shortcode"/></td>
					</tr>
					<tr>
						<td>城市编码</td>
						<td><input type="text" name="citycode"/></td>
					</tr>
					</table>
			</form>
		</div>
	</div>
	<!-- 查询窗口 -->
	<div class="easyui-window" title="区域查询" id="queryRegionWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="query" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >查询</a>
			</div>
		</div>
		
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form id="queryRegionForm" method="post" action="${pageContext.request.contextPath}/basic/region_add">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">区域查询</td>
					</tr>
					<tr>
						<td>区域编码</td>
						<td><input type="text" name="id"/></td>
					</tr>
					<tr>
						<td>省</td>
						<td><input type="text" name="province"/></td>
					</tr>
					<tr>
						<td>市</td>
						<td><input type="text" name="city"/></td>
					</tr>
					<tr>
						<td>区</td>
						<td><input type="text" name="district"/></td>
					</tr>
					<tr>
						<td>简码</td>
						<td><input type="text" name="shortcode"/></td>
					</tr>
					</table>
			</form>
		</div>
	</div>
</body>
</html>