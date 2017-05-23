<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=utf-8"%>

<%@ page import="java.lang.reflect.Field"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
	<head>
		<title>List Threads</title>
	</head>
  
<%
  	request.setCharacterEncoding("UTF-8");
%>

	<body>
		
<%
	int threadcount = Thread.activeCount();
	Thread[] threads = new Thread[threadcount*2];
	int ret = Thread.enumerate(threads);
	Vector<Thread> ts = new Vector<Thread>();
	int httpcount = 0;
	for (int m = 0 ; m < threads.length ; m++)
	{
		if (threads[m] == null)
		{
			continue;
		}
		ts.add(threads[m]);
		String name = threads[m].getName();
		if (name.indexOf("http-") >= 0)
		{
			httpcount++;
		}
	}
%>

	Threads: <%=httpcount %> / <%=ts.size() %> / <%=ret %><p>

<%
	for (int i = 0 ; i < ts.size() ; i++)
	{
		String classname = ts.get(i).getClass().getSimpleName();
		String name = ts.get(i).getName();
		Thread.State state = ts.get(i).getState();
		long threadid = ts.get(i).getId();
		
		StackTraceElement[] stackTraceElements = ts.get(i).getStackTrace();
%>

		Thread (<%=threadid %>) name (<%=name %>) classname (<%=classname %>) state (<%=state %>)<br>
		
<%
		for (int j = 0 ; j < stackTraceElements.length ; j++)
		{
%>
		-------> (<%=stackTraceElements[j].getClassName() %>.<%=stackTraceElements[j].getMethodName() %>:<%=stackTraceElements[j].getLineNumber() %> <br>
	
<%
		}
	}
%>
	
	<form action="listthreads.jsp" method="POST">
	</form></body>
</html>