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
						$("#name").val("");
						return;
					}
				}
				
				if(minLength && maxLength) {
					if((maxLength - minLength) <= 0) {
						$("#name").val("");
						return;
					}
				}
				if(minWeight && maxWeight && minLength && maxLength) {
					var name = minWeight + "kg-" + maxWeight + "kg;" + minLength + "cm-" + maxLength + "cm";
					$("#name").val(name);
				}
				
			}
		
			//自定义校验器
			$.extend($.fn.validatebox.defaults.rules, { 
				weight: { 
				validator: function(value, param){ 
				return (value - $("#minWeight").val()) > 0; 
				}, 
				message: '最大重量不能小于最小重量' 
				},
				length: { 
				validator: function(value, param){ 
				return (value - $("#minLength").val()) > 0; 
				}, 
				message: '最大长度不能小于最小长度' 
				},
				uniqueName: { 
				validator: function(value, param){ 
					var flag_name;
					$.ajax({
						url:"${pageContext.request.contextPath}/basic/standard_checkName",
						type : "POST",
						timeout : 6000,
						data : {name:value},
						async : false,
						success : function(data, textStatus, jqXHR){
							flag_name = data;
						}
						
					});
					return flag_name;
				}, 
				message: '已存在相同标准' 
				}
				
			}); 
			
			
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
						$("#name").validatebox("remove");
						//将数据封装入表单
						/* $("#id").val(data[0].id);
						$("#name").val(data[0].name);
						$("#minWeight").val(data[0].minWeight);
						$("#maxWeight").val(data[0].maxWeight);
						$("#minLength").val(data[0].minLength);
						$("#maxLength").val(data[0].maxLength); */
						$("#standardForm").form("load",data[0]);
					}else {
						$.messager.alert("错误","请选择一条数据","warning");
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
		<div id="addStandardWindow" title="" class="easyui-window,easyui-layout"  style="top:20px;left:200px">
			<div data-options="region:'north'" style="height:31px;overflow:hidden;" split="false" border="false" class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="javascript:void(0);" class="easyui-linkbutton" plain="true">保存</a>
			</div>
			<div data-options="region:'center'" style="overflow:auto;padding:5px;" border="false">
				<form id="standardForm" action="${pageContext.request.contextPath}/basic/standard_add" method="post">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">取派标准信息</td>
					</tr>
					<tr>
						<td>取派标准名称</td>
						<td>
						<input type="hidden" name="id" id="id"/>
						<input type="text" name="name" class="easyui-validatebox" data-options="required:true,validType:'uniqueName'" readonly="readonly" id = "name"/>
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
						<input type="text" name="maxWeight" id="maxWeight" class="easyui-numberbox" data-options="min:0,required:true,validType:'weight'" onblur="getName()"/>(kg)
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
						<input type="text" name="maxLength" id="maxLength" class="easyui-numberbox" data-options="min:0,required:true,validType:'length'" onblur="getName()"/>(cm)
						</td>
					</tr>
				</table>
				</form>
				<br/><br/>
				<center><div id="msg"></div></center>
			</div>
		</div>
	</body>

</html>