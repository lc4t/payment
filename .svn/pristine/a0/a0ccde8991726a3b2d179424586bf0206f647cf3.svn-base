package noumena.payment.bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import noumena.payment.util.Constants;
import noumena.payment.vo.PostResVO;

public class Post {
	public int gatOrderKStatus(String payId, String sign, String URL, String payTypeId) {
		// 等待添加
		PostResVO vo = doPost(URL, payId, sign, payTypeId);
		if (vo.getStatus() == Constants.URL_ERROR || vo.getRes().equals("")) {
			return Constants.K_CON_ERROR;
		}

		String[] res = resFormat(vo.getRes());
		if (res[0].equals("")||res[0] == null) {
			return Constants.ORDER_ERROR;
		}
		int status = -1;
		try
		{
			status = Integer.valueOf(res[0]);
			if (status == Constants.K_ERROR) {
				status = Constants.K_STSTUS_ERROR;
			} else if (status == Constants.K_DEFAULT) {
				status = Constants.K_STSTUS_DEFAULT;
			} else if (status == Constants.K_SUCCESS) {
				status = Constants.K_STSTUS_SUCCESS;
			} else if (status == -2) {
				System.out.println(payId);
				System.out.println(sign);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}

	public PostResVO doPost(String url, String payId, String sign, String payTypeId) {
		URL u;
		PostResVO vo = new PostResVO();
		try {
			u = new URL(url);
			try {
				HttpURLConnection uc = (HttpURLConnection) u.openConnection();
				uc.setDoOutput(true);
				OutputStreamWriter writer = new OutputStreamWriter(uc
						.getOutputStream());
				writer.write("payId=" + payId + "&sign=" + sign + "&payTypeId=" + payTypeId);
				writer.flush();
				writer.close();

				BufferedReader in = new BufferedReader(new InputStreamReader(uc
						.getInputStream()));
				String res = in.readLine();
				vo.setStatus(Constants.URL_SUCCESS);
				vo.setRes(res);
			} catch (IOException e) {
				vo.setStatus(Constants.URL_ERROR);
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			vo.setStatus(Constants.URL_ERROR);
			e.printStackTrace();
		}
		return vo;
	}

	public String[] resFormat(String res) {
		//{state:'1',returntime:'2012-2-10 11:11:11'}		切换到用户中心平台之前
		//{"state":"-2","returntime":"2012-2-10 11:11:11"}	切换到用户中心平台之后
		//int pos1 = 7, pos2 = 12;		//切换到用户中心平台之前
		int pos1 = 9, pos2 = 14;		//切换到用户中心平台之后
		String[] s = new String[2];
		res = res.substring(1, res.length() - 1);
		String[] tmps = res.split(",");
		for (String tmp : tmps) {
			if (tmp.indexOf("state") != -1) {
				s[0] = tmp.substring(pos1, tmp.length() - 1);
			} else if (tmp.indexOf("returntime") != -1) {
				s[1] = tmp.substring(pos2, tmp.length() - 1);
			}
		}
		return s;
	}
}
