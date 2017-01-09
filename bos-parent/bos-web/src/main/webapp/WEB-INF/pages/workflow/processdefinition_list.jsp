<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程定义列表</title>
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
	var deltag = "${deltag}";

	var has_showModalDialog = !!window.showModalDialog;//定义一个全局变量判定是否有原生showModalDialog方法  
	if(!has_showModalDialog &&!!(window.opener)){         
	    window.onbeforeunload=function(){  
	        window.opener.hasOpenWindow = false;        //弹窗关闭时告诉opener：它子窗口已经关闭  
	    }  
	}  
	//定义window.showModalDialog如果它不存在  
	if(window.showModalDialog == undefined){  
	    window.showModalDialog = function(url,mixedVar,features){  
	        window.hasOpenWindow = true;  
	        if(mixedVar) var mixedVar = mixedVar;  
	        //因window.showmodaldialog 与 window.open 参数不一样，所以封装的时候用正则去格式化一下参数  
	        if(features) var features = features.replace(/(dialog)|(px)/ig,"").replace(/;/g,',').replace(/\:/g,"=");  
	        //window.open("Sample.htm",null,"height=200,width=400,status=yes,toolbar=no,menubar=no");  
	        //window.showModalDialog("modal.htm",obj,"dialogWidth=200px;dialogHeight=100px");   
	        var left = (window.screen.width - parseInt(features.match(/width[\s]*=[\s]*([\d]+)/i)[1]))/2;  
	        var top = (window.screen.height - parseInt(features.match(/height[\s]*=[\s]*([\d]+)/i)[1]))/2;  
	        window.myNewWindow = window.open(url,"_blank",features);  
	    }  
	}  



	$(function(){
		$("#grid").datagrid({
			striped : true,
			rownumbers : true,
			singleSelect : true,
			fitColumns : true,
			toolbar : [
				{
					id : 'deploy',
					text : '发布新流程',
					iconCls : 'icon-add',
					handler : function(){
						location.href = "${pageContext.request.contextPath}/page_workflow_processdefinition_deploy.action";
					}
				}
			]
		});
		if(deltag) {
			$.messager.alert("错误","当前流程正在使用！","error")
		}
		
	});
	
	function viewpng(id) {  
	    url = "${pageContext.request.contextPath}/process/processDefinition_viewpng?id=" + id;  
	    window.showModalDialog(url, "", "dialogWidth:1020px;dialogHeight:500px;help:no;resizable:no;center:yes;scroll:yes;status:no");  
	    //if(!has_showModalDialog) return;//chrome 返回 因为showModalDialog是阻塞的 open不一样;    
	}  
	function del(id) {
		$.messager.confirm("确认","你确定要删除吗？",function(r){
			if(r) {
				location.href = "${pageContext.request.contextPath}/process/processDefinition_delete?id=" + id;
			}
			
		})
	}
</script>	
</head>
<body class="easyui-layout">
  <div region="center" >
  	<table id="grid" class="easyui-datagrid">  
  		<thead>
  			<tr>
  				<th data-options="field:'id'" width="120">流程编号</th>
  				<th data-options="field:'name'" width="200">流程名称</th>
  				<th data-options="field:'key'" width="100">流程key</th>
  				<th data-options="field:'version'" width="80">版本号</th>
  				<th data-options="field:'viewpng'" width="200">查看流程图</th>
  				<th data-options="field:'delete'" width="100">删除</th>
  			</tr>
  		</thead>
  		<tbody>
  				<s:iterator value="list" var="processDefinition">
  					<!-- 在循环过程中 ，将  processDefinition 对象，同时放入 root 和 map 中-->
  				<tr>
  					<td>
  						<s:property value="id"/> <!-- 从root找 -->
  						<s:property value="#processDefinition.id"/> <!-- 从map找 -->
  					</td>
  					<td><s:property value="name"/></td>
  					<td><s:property value="key"/></td>
  					<td><s:property value="version"/></td>
  					<td>
  						<a onclick="viewpng('${id}')"
  							 class="easyui-linkbutton" data-options="iconCls:'icon-search'">
  							 	查看流程图
  						</a>
  						
  					</td>
  					<td>
  						<a onclick="del('${id}')"
  							 class="easyui-linkbutton" data-options="iconCls:'icon-remove'">
  							 	删除
  						</a>
  					</td>
  				</tr>
  				</s:iterator>
  		</tbody>
  	</table>
  	<div id="p"></div> 
  </div>
</body>
</html>