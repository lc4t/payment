<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" pageEncoding="UTF-8"%>

<%@ page import="noumena.payment.bean.PayItemBean" %>
<%@ page import="noumena.payment.model.PayItems" %>
<%@ page import="noumena.payment.util.DateUtil" %>
<%@ page import="java.util.List" %>

<%
request.setCharacterEncoding("utf-8");
response.setCharacterEncoding("utf-8");

String model = request.getParameter("model");
String id = request.getParameter("id");
String gameid = request.getParameter("gameid");
String serverid = request.getParameter("serverid");
String itemid = request.getParameter("itemid");
String itemprice = request.getParameter("itemprice");
String itemdesc = request.getParameter("itemdesc");

PayItemBean bean = new PayItemBean();
if (model == null || model.equals("") || model.equals("insert"))
{
	PayItems vo = new PayItems();
	vo.setGameid(gameid);
	vo.setServerid(serverid);
	vo.setItemid(itemid);
	if (itemprice == null || itemprice.equals(""))
	{
		itemprice = "0";
	}
	vo.setItemdesc(itemdesc);
	vo.setItemprice(Integer.parseInt(itemprice));
	vo.setCreatetime(DateUtil.getCurrentTime());
	bean.save(vo);
}
else if (model.equals("delete"))
{
	PayItems payitem = bean.selectById(Integer.parseInt(id));
	if (payitem != null)
	{
		PayItems vo = payitem;
		vo.setGameid(gameid);
		vo.setServerid(serverid);
		vo.setItemid(itemid);
		if (itemprice == null || itemprice.equals(""))
		{
			itemprice = "0";
		}
		vo.setItemdesc(itemdesc);
		vo.setItemprice(Integer.parseInt(itemprice));
		bean.delete(vo);
	}
}
else
{
	PayItems payitem = bean.selectById(Integer.parseInt(id));
	if (payitem != null)
	{
		PayItems vo = payitem;
		vo.setGameid(gameid);
		vo.setServerid(serverid);
		vo.setItemid(itemid);
		if (itemprice == null || itemprice.equals(""))
		{
			itemprice = "0";
		}
		vo.setItemdesc(itemdesc);
		vo.setItemprice(Integer.parseInt(itemprice));
		vo.setUpdatetime(DateUtil.getCurrentTime());
		bean.update(vo);
	}
}

response.sendRedirect("items.jsp?gameid=" + gameid);
%>
