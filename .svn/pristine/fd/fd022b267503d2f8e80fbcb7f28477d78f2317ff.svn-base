package noumena.payment.wandoujia;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class WandoujiaOldCharge
{
  public static WandoujiaParams wandoujiaParams = new WandoujiaParams();
  private static boolean testmode = false;
  
  public static boolean isTestmode()
  {
    return testmode;
  }
  
  public static void setTestmode(boolean testmode)
  {
    testmode = testmode;
  }
  
  public static String getTransactionId(Orders order)
  {
    order.setCurrency(Constants.CURRENCY_RMB);
    order.setUnit(Constants.CURRENCY_UNIT_FEN);
    
    OrdersBean bean = new OrdersBean();
    String cburl = order.getCallbackUrl();
    String payId;
    if ((cburl == null) || (cburl.equals("")))
    {
      payId = bean.CreateOrder(order);
    }
    else
    {
      if (cburl.indexOf("?") == -1) {
        cburl = cburl + "?pt=5045";
      } else {
        cburl = cburl + "&pt=5045";
      }
      cburl = cburl + "&currency=" + Constants.CURRENCY_RMB;
      cburl = cburl + "&unit=" + Constants.CURRENCY_UNIT_FEN;
      
      payId = bean.CreateOrder(order, cburl);
    }
    order.setCallbackUrl(cburl);
    String date = DateUtil.formatDate(order.getCreateTime());
    OrderIdVO orderIdVO = new OrderIdVO(payId, date);
    
    JSONObject json = JSONObject.fromObject(orderIdVO);
    return json.toString();
  }
  
  public static String checkOrdersStatus(String payIds)
  {
    String[] orderIds = payIds.split(",");
    
    OrdersBean bean = new OrdersBean();
    List<Orders> orders = bean.qureyOrders(orderIds);
    List<OrderStatusVO> statusret = new ArrayList();
    for (int i = 0; i < orders.size(); i++)
    {
      Orders order = (Orders)orders.get(i);
      int status = order.getKStatus().intValue();
      OrderStatusVO st = new OrderStatusVO();
      st.setPayId(order.getOrderId());
      if ((status == 0) || (status == -3)) {
        st.setStatus(3);
      } else if (status == 1) {
        st.setStatus(1);
      } else {
        st.setStatus(2);
      }
      statusret.add(st);
    }
    JSONArray arr = JSONArray.fromObject(statusret);
    
    return arr.toString();
  }
  
  public static String getCallbackFromWandoujia(String transdata, String sign)
  {
    String ret = "SUCCESS";
    try
    {
      if ((transdata == null) || ("".equalsIgnoreCase(transdata))) {
        return "FAILURE";
      }
      if ((sign == null) || ("".equalsIgnoreCase(sign))) {
        return "FAILURE";
      }
      OrdersBean bean = new OrdersBean();
      JSONObject json = JSONObject.fromObject(transdata);
      WandoujiaOrderVO ordervo = (WandoujiaOrderVO)JSONObject.toBean(json, WandoujiaOrderVO.class);
      Orders order = bean.qureyOrder(ordervo.getOut_trade_no());
      if (order != null)
      {
        boolean isvlid = validMessage(transdata, sign, ordervo.getAppKeyId());
        if (!isvlid) {
          return "FAILURE";
        }
        if (order.getKStatus().intValue() != 1)
        {
          bean.updateOrderAmountPayId(ordervo.getOut_trade_no(), ordervo.getOrderId(), ordervo.getMoney());
          

          bean.updateOrderKStatus(ordervo.getOut_trade_no(), 1);
        }
        else
        {
          System.out.println("wandoujia order (" + order.getOrderId() + ")(" + order.getPayId() + ") had been succeed");
        }
        String path = OSUtil.getRootPath() + "../../logs/wandoujiacb/" + DateUtil.getCurTimeStr().substring(0, 8);
        OSUtil.makeDirs(path);
        String filename = path + "/" + ordervo.getOut_trade_no() + "_" + ordervo.getOrderId();
        
        String res = "";
        res = res + "transdata=" + transdata;
        res = res + "&sign=" + sign;
        OSUtil.saveFile(filename, res);
      }
      else
      {
        ret = "ILLEGAL ORDER";
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      ret = "FAILURE";
    }
    return ret;
  }
  
  private static boolean validMessage(String transdata, String sign, String appid)
    throws Exception
  {
    return WandouRsa.doCheck(transdata, sign);
  }
  
  public static void init()
  {
	  wandoujiaParams.initParams(WandoujiaParams.CHANNEL_ID, new WandoujiaParamsVO());
  }
}
