<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>投票</title>
</head>
<body>
	<h1><font color="blue">请选择喜欢的运动项目</font></h1>
	<form action="${pageContext.request.contextPath}/demo/jfreechart_hobby" method="get">
		<input type="checkbox" name="hobby" value="football"/>足球
		<input type="checkbox" name="hobby" value="basketball"/>篮球
		<input type="checkbox" name="hobby" value="volleyball"/>排球
		<input type="checkbox" name="hobby" value="tennis"/>网球
		<input type="submit" value="投票"/>
	</form>
	<a href="${pageContext.request.contextPath}/demo/jfreechart_getBar">获取柱状图</a>
	<a href="${pageContext.request.contextPath}/demo/jfreechart_getPie">获取饼状图</a>
</body>
</html>