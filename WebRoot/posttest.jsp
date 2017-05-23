<%@ page import="noumena.payment.util.Constants" %>
<%@ page import="noumena.payment.coolpad.CoolpadCharge" %>
<%@ page import="noumena.payment.gionee.GioneeCharge" %>
<%@ page import="noumena.payment.lenovo.LenovoCharge" %>
<%@ page import="noumena.payment.vivo.VivoCharge" %>
<%@ page import="noumena.payment.duoku.DuokuCharge" %>
<%@ page import="noumena.payment.meizu.MeizuCharge" %>
<%@ page import="noumena.payment.google.GoogleCharge" %>
<%@ page import="noumena.payment.youxiqun.YouxiqunCharge" %>
<%@ page import="noumena.payment.tongbu.TongbuCharge" %>
<%@ page import="noumena.payment.bean.OrdersBean" %>

<html>

<%
        //Constants.initParams(true);
        //CoolpadCharge.init();
        //GioneeCharge.init();
        //LenovoCharge.init();
        //VivoCharge.init();
        //DuokuCharge.init();
        //MeizuCharge.init();
        //GoogleCharge.init();
        //YouxiqunCharge.init();
        //TongbuCharge.init();

        //OrdersBean bean = new OrdersBean();
				//bean.updateOrderKStatus("8901004", 1);
%>

<form name="submitform" action="verifyGooglePlay.jsp" method="POST">
appId:<input name="appId"><br>
signature:<input name="signature"><br>
signedData:<input name="signedData"><br>

<input type="submit" value="submit">
</form>

</html>


