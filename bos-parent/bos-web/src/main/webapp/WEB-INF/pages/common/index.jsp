<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>宅急送BOS主界面</title>
<link rel="stylesheet" type="text/css" id="easyuiTheme"
	href="${pageContext.request.contextPath}/js/easyui/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/easyui/themes/icon.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/js/ztree/zTreeStyle.css"
	type="text/css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery-1.8.3.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript">
	//点击加载中央区域页面
	function zTreeOnClick(event, treeId, treeNode) {
		if(treeNode.page) {//判断是否是一个可选页面
			//判断该tar是否已经存在
			if($('#tars').tabs('exists',treeNode.name)){
				$('#tars').tabs('select',treeNode.name)
			}else {
				var content = "<iframe src=" + treeNode.page + " scrolling=\"auto\" style=\"width:100%;height:100%;border:0;\"></iframe>"
				$('#tars').tabs('add',{ 
					title:treeNode.name, 
					content:content, 
					closable:true, 
					}); 
			}
		}
	};
	
	//更换主题
	changeTheme = function(themeName) {
		$easyuiTheme = $("#easyuiTheme");
		var url = $easyuiTheme.attr('href');
		var href = url.substring(0,url.indexOf('themes')) + "themes/" + themeName + "/easyui.css";
		$easyuiTheme.attr('href', href);
		var $iframe = $('iframe');
		//更换iframe引入页面的主题
		if ($iframe.length > 0) {
			for ( var i = 0; i < $iframe.length; i++) {
				var ifr = $iframe[i];
				$(ifr).contents().find('#easyuiTheme').attr('href', href);
			}
		}
	};
	
	//修改密码
	changePwd = function() {
		$("#editPwdWindow").window('open');
	}
	//联系管理员
	showAbout = function() {
		$.messager.alert("宅急送 v1.0","制作:AirYiSea<br/>管理员邮箱:airyisea@163.com");
	}
	//退出系统
	logoutFun = function() {
		$.messager.confirm("系统提示","你确定要退出本系统吗？",
				function(isConfirm){
					if(isConfirm) {
						location.href = '${pageContext.request.contextPath }/logout.jsp';
					}
		});
	}
	
	
	var setting = {
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				onClick: zTreeOnClick
			}
		};
	
	$(function() {
		//加载基本功能菜单
		$.post("${pageContext.request.contextPath}/json/menu.json",function(data){
			$.fn.zTree.init($("#basicmenu"), setting, data);
		});
		//加载系统管理菜单
		$.post("${pageContext.request.contextPath}/json/admin.json",function(data){
			$.fn.zTree.init($("#sysmenu"), setting, data);
		},"json");
		//页面加载,右下角弹出消息框
		window.setTimeout(function(){
			$.messager.show({  	
			  title:'消息提示',  	
			  msg:'欢迎登录，${loginUser.username}！<a href="javascript:void" onclick="showAbout();">联系管理员</a>',  	
			  timeout:5000,  	
			  showType:'slide'
			});
			},3000);
		
		$("#btnCancel").click(function(){
			$('#editPwdWindow').window('close');
		});
		//修改密码
		$("#btnEp").click(function(){
			
			var newPwd = $("#txtNewPass").val();
			if(newPwd == null || newPwd == "") {
				$.messager.alert("错误","密码不能为空","warning");
				return;
			}
			var reg = /\s+/;
			if(reg.test(newPwd)){
				$.messager.alert("错误","密码不能包含空格","warning");
				return;
			}
			
			if(newPwd.length <3 || newPwd.length > 16) {
				$.messager.alert("错误","密码必须为3-16位","warning");
				return;
			}
			
			if(newPwd != $("#txtRePass").val()){
				$.messager.alert("错误","两次密码不一致","warning");
				return;
			}
			
			$.post("${pageContext.request.contextPath}/user/user_changePassword",{"newPwd":$("#txtNewPass").val(),"rePwd":$("#txtRePass").val()},function(data){
				//1:修改成功,0:服务器异常,-1:密码为必须为3-16位,-2:两次密码不一致,-3:与原密码一致
				switch(data) 
				{
					case -1:{
						$.messager.alert("失败","密码必须为3-16位","error");
						break;
					}
					case -2:{
						$.messager.alert("失败","两次密码不一致","error");
						break;
					}
					case -3:{
						$.messager.alert("失败","新密码与原密码相同","error");
						break;
					}
					case 1:{
						$('#editPwdWindow').window('close');
						$.messager.alert("成功","修改成功","info");
						break;
					}
					default:{
						$.messager.alert("服务器异常,联系管理员","error");
					}
				}
			});
		});

	});
</script>
</head>
<body class="easyui-layout">
	<!-- 顶部 -->
	<div data-options="region:'north',border:false"
		style="height:80px;padding:10px;background:url('${pageContext.request.contextPath}/images/header_bg.png') no-repeat right;">
		<img alt="宅急送"
			src="${pageContext.request.contextPath}/images/logo.png" border="0"/>
		<!-- 当前用户信息 -->
		<div style="position:absolute;right: 20px;top:10px;">
			[<strong>${loginUser.username}</strong>]，欢迎你！您使用[<strong>${pageContext.request.remoteAddr}</strong>]IP登录!
		</div>
		<!-- 更换皮肤和控制面板 -->
		<div style="position: absolute; right: 5px; bottom: 10px; ">
			<a href="javascript:void(0)" class="easyui-menubutton" 
				data-options="menu:'#layout_theme',iconCls:'icon-ok'">更换皮肤</a>
			<a href="javascript:void(0);" class="easyui-menubutton"
				data-options="menu:'#layout_control_panel',iconCls:'icon-help'">控制面板</a>
		</div>
		<div id="layout_theme" style="width: 120px; display: none;">
			<div onclick="changeTheme('default')">default</div>
			<div onclick="changeTheme('gray')">gray</div>
			<div onclick="changeTheme('black')">black</div>
			<div onclick="changeTheme('bootstrap')">bootstrap</div>
			<div onclick="changeTheme('metro')">metro</div>
		</div>
		<div id="layout_control_panel" style="width: 120px; display: none;">
			<div onclick="changePwd();">修改密码</div>
			<div onclick="showAbout();">联系管理员</div>
			<div class="menu-sep"></div>
			<div onclick="logoutFun();">退出系统</div>
		</div>
	</div>

	<!-- 左边导航条 -->
	<div data-options="region:'west',title:'菜单导航',split:true"
		style="width: 200px;">
		<div class="easyui-accordion" data-options="fit:true">
			<div title="基本功能" style="overflow: auto" data-options="iconCls:'icon-mini-add',selected:true">
				<ul id="basicmenu" class="ztree"></ul>
			</div>

			<div title="系统管理" style="overflow: auto" data-options="iconCls:'icon-mini-add'">
				<ul id="sysmenu" class="ztree"></ul>
			</div>
		</div>
	</div>

	<!-- 中央区域 -->
	<div id="tars" class="easyui-tabs" data-options="region:'center'" style=" background: #eee;">
		<div title="消息中心" style="width:100%;height:100%;overflow:hidden">
			<iframe src="page_common_home.action" scrolling="auto" style="width:100%;height:100%;border:0;"></iframe>
		</div> 
	</div>
	<!-- 底部 -->
	<div data-options="region:'south',border:false"
		style="height:50px;padding:10px;background:url('${pageContext.request.contextPath}/images/header_bg.png') no-repeat right;">
		<table style="width: 100%;">
			<tbody>
				<tr>
					<td style="width: 300px;">
						<div style="color: #999; font-size: 8pt;">
							Powered by <a href="#">AirYiSea</a>
						</div>
					</td>
					<td style="width: *;" class="co1"><span id="online"
						style="background: url(${pageContext.request.contextPath }/images/online.png) no-repeat left;padding-left:18px;margin-left:3px;font-size:8pt;color:#005590;">在线人数:1</span>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<!-- 修改密码窗口 -->
	<div id="editPwdWindow" class="easyui-window" title="修改密码" collapsible="false" minimizable="false" modal="true" closed="true" resizable="false"
        maximizable="false" icon="icon-save"  style="width: 300px; height: 160px; padding: 5px; background: #fafafa">
		<div class="easyui-layout" fit="true">
				<div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
                <table cellpadding=3>
                    <tr>
                        <td>新密码：</td>
                        <td><input id="txtNewPass" type="Password" class="txt01" /></td>
                    </tr>
                    <tr>
                        <td>确认密码：</td>
                        <td><input id="txtRePass" type="Password" class="txt01" /></td>
                    </tr>
                </table>
            	</div>
            <div region="south" border="false" style="text-align: right; height: 30px; line-height: 30px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" >确定</a> 
                <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)">取消</a>
            </div>
		</div>
	</div>
</body>
</html>