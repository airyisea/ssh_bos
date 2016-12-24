<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<title>取派标准</title>
		<!-- 导入jquery核心类库 -->
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
			$(function(){
				// 先将body隐藏，再显示，不会出现页面刷新效果
				$("body").css({visibility:"visible"});
				
				// 收派标准信息表格
				$('#grid').datagrid( {
					iconCls : 'icon-forward',
					fit : true,
					border : false,
					rownumbers : true,
					striped : true,
					pageSize:5,
					pageList: [1,5,10],
					pagination : true,
					toolbar : toolbar,
					url : "${pageContext.request.contextPath}/basic/standard_queryPage",
					idField : 'id',
					columns : columns
				});
				// 添加取派标准窗口
				$('#addStandardWindow').window({
			        title: '取派标准操作',
			        width: 600,
			        modal: true,
			        shadow: false,
			        closed: true,
			        height: 400,
			        resizable:false,
			        collapsible:false,
			    	minimizable:false,
			    	maximizable:false,
			        onBeforeClose:function(){
			        	  $("#standardForm")[0].reset();
			        	  $("#id").val("");
			        }
			    });
				
				$("#save").click(function(){ 
					var flag = $("#standardForm").form("validate");
					if(flag) {
						$("#standardForm").submit();
					}
					
				});
			});	
			//生成取派标准名称
			getName = function(){
				var minWeight = $("#minWeight").val();
				var maxWeight = $("#maxWeight").val();
				var minLength = $("#minLength").val();
				var maxLength = $("#maxLength").val();
				if(maxWeight && minWeight) {
					if((maxWeight - minWeight) <= 0) {
						$("#msg").html("<font color='red'>最大重量不能小于最小重量</font>");
						$("#name").val("");
						return;
					}
				}
				
				if(minLength && maxLength) {
					if((maxLength - minLength) <= 0) {
						$("#msg").html("<font color='red'>最大长度不能小于最小长度</font>");
						$("#name").val("");
						return;
					}
				}
				if(minWeight && maxWeight && minLength && maxLength) {
					var name = minWeight + "kg-" + maxWeight + "kg;" + minLength + "cm-" + maxLength + "cm";
					$.post("${pageContext.request.contextPath}/basic/standard_checkName",{name:name},function(data){
						if(data){
							$("#name").val(name);
							$("#msg").html("");
							
						}else{
							$("#name").val("");
							$("#msg").html("<font color='red'>已存在相同标准</font>");
						}
					})
				}
				
			}
			
			
			//工具栏
			var toolbar = [ {
				id : 'button-add',
				text : '增加',
				iconCls : 'icon-add',
				handler : function(){
					$('#addStandardWindow').window("open");
				}
			}, {
				id : 'button-edit',
				text : '修改',
				iconCls : 'icon-edit',
				handler : function(){
					//获取选择的第一条数据
					var data = $("#grid").datagrid("getSelections");
					if(data.length == 1) {
						$('#addStandardWindow').window("open");
						//将数据封装入表单
						$("#id").val(data[0].id);
						$("#name").val(data[0].name);
						$("#minWeight").val(data[0].minWeight);
						$("#maxWeight").val(data[0].maxWeight);
						$("#minLength").val(data[0].minLength);
						$("#maxLength").val(data[0].maxLength);
					}else {
						$.messager.alert("错误","请选择一条数据","warring");
					}
				}
			},{
				id : 'button-delete',
				text : '删除',
				iconCls : 'icon-cancel',
				handler : function(){
					var select = $("#grid").datagrid("getSelections");
					if(select != "" && select != null) {
						$.messager.confirm("删除记录","确定要删除这些记录？",function(flag){
							if(flag){
								var idsArr = new Array(); 
								for(var i = 0; i < select.length; i++) {
									idsArr.push(select[i].id);
								}
								$.post("${pageContext.request.contextPath}/basic/standard_deleteBatch",{ids:idsArr.join(",")},function(data){
									if(data){
										$.messager.alert("成功","删除成功！","info");
										$("#grid").datagrid("uncheckAll");
										$("#grid").datagrid("reload");
									}else{
										$.messager.alert("失败","删除失败","error");
									}
								});
							}
						})
					}

				}
			},{
				id : 'button-restore',
				text : '还原',
				iconCls : 'icon-save',
				handler : function(){
					alert('还原');
				}
			}];
			
			// 定义列
			var columns = [ [ {
				field : 'id',
				checkbox : true
			},{
				field : 'name',
				title : '标准名称',
				width : 120,
				align : 'center'
			}, {
				field : 'minWeight',
				title : '最小重量',
				width : 120,
				align : 'center'
			}, {
				field : 'maxWeight',
				title : '最大重量',
				width : 120,
				align : 'center'
			}, {
				field : 'minLength',
				title : '最小长度',
				width : 120,
				align : 'center'
			}, {
				field : 'maxLength',
				title : '最大长度',
				width : 120,
				align : 'center'
			}, {
				field : 'operator',
				title : '操作人',
				width : 120,
				align : 'center'
			}, {
				field : 'operatingTime',
				title : '操作时间',
				width : 120,
				align : 'center'
			}, {
				field : 'operatorStation',
				title : '操作单位',
				width : 120,
				align : 'center'
			} ] ];
		</script>
	</head>

	<body class="easyui-layout" style="visibility:hidden;">
		<div region="center" border="false">
			<table id="grid"></table>
		</div>
		<div id="addStandardWindow" title="" class="easyui-window,easyui-layout"  style="top:20px;left:200px">
			<div data-options="region:'north'" style="height:31px;overflow:hidden;" split="false" border="false" class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="javascript:void(0);" class="easyui-linkbutton" plain="true">保存</a>
			</div>
			<div data-options="region:'center'" style="overflow:auto;padding:5px;" border="false">
				<form id="standardForm" action="${pageContext.request.contextPath}/basic/standard_add">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">取派标准信息</td>
					</tr>
					<tr>
						<td>取派标准名称</td>
						<td>
						<input type="hidden" name="id" id="id"/>
						<input type="text" name="name" class="easyui-validatebox" data-options="required:true" readonly="readonly" id = "name"/>
						</td>
					</tr>
					<tr>
						<td>最小重量</td>
						<td>
						<input type="text" name="minWeight" id="minWeight" class="easyui-numberbox" data-options="min:0,required:true" onblur="getName()"/>(kg)
						</td>
					</tr>
					<tr>
						<td>最大重量</td>
						<td>
						<input type="text" name="maxWeight" id="maxWeight" class="easyui-numberbox" data-options="min:0,required:true" onblur="getName()"/>(kg)
						</td>
					</tr>
					<tr>
						<td>最小长度</td>
						<td>
						<input type="text" name="minLength" id="minLength" class="easyui-numberbox" data-options="min:0,required:true" onblur="getName()"/>(cm)
						</td>
					</tr>
					<tr>
						<td>最大长度</td>
						<td>
						<input type="text" name="maxLength" id="maxLength" class="easyui-numberbox" data-options="min:0,required:true" onblur="getName()"/>(cm)
						</td>
					</tr>
				</table>
				</form>
				<br/><br/>
				<center><div id="msg"></div></center>
			</div>
			
		</div>
		<!-- 添加取派员窗体  -->
	<%-- <div class="easyui-window" title="对收派员进行添加或者修改" id="addStaffWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >保存</a>
			</div>
		</div>
		
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form id="addStaffForm" method="post" action="${pageContext.request.contextPath }/bc/staffAction_save">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">收派员信息</td>
					</tr>
					<tr>
						<td>姓名</td>
						<td>
						<input type="hidden" name="id" id="id"/>
						<input type="text" name="name" class="easyui-validatebox" data-options="required:true"/></td>
					</tr>
					<tr>
						<td>手机</td>
						<td><input id="tel" type="text" name="telephone" class="easyui-validatebox" 
						   data-options="required:true,validType:['telephone','uniquePhone']"/>
						   <span id="tel_sp"></span>
						   </td>
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
							<input type="text" name="standard" class="easyui-validatebox" required="true"/>  
						</td>
					</tr>
					</table>
			</form>
		</div>
	</div> --%>
	</body>

</html>