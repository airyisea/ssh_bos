<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h3>审核工作单</h3>
	${map }
	<s:form namespace="/process" action="task_checkWorkOrderManage">
		<s:hidden name="id"></s:hidden>
		 审核结果：<select name="check">
		 			<option value="1">通过</option>
		 			<option value="0">不通过</option>
		 		</select>
		 		<input type="submit" value="提交">
	</s:form>
</body>
</html>