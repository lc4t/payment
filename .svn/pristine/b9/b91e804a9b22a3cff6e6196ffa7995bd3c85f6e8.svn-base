package noumena.payment.userverify;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import net.sf.json.JSONObject;
import noumena.payment.userverify.vo.CPPVerifyVO;

public class CPPVerify
{
	public static int MiniObjectSize = 3 * 4;

	public static String verify(int model, ChannelInfoVO vo)
	{
//		String id = getIdFrom(vo.getToken());
//		return id;
		String ret = "";
		switch (model)
		{
		case 0:
			//正常参数验证，返回合法id
			ret = getIdFrom(vo);
			break;
		case 1:
			//正常参数验证，返回json格式状态
			ret = getIdFrom(vo);
			break;
		case 2:
			//json参数验证，返回合法id
			String info = vo.getExinfo();
			JSONObject json = JSONObject.fromObject(info);
			vo.setToken(json.getString("token"));
			vo.setExinfo("");
			
			ret = getIdFrom(vo);
			break;
		}
		return ret;
	}

	private static String getIdFrom(ChannelInfoVO vo)
	{
		String token = vo.getToken();
		byte[] array = new byte[16];
		for (int i = 0 ; i < 16 ; i++)
		{
			array[i] = (byte) Integer.valueOf(token.substring(i * 2, i * 2 + 2), 16).intValue();
		}
		int length = array.length + 8;
		Socket client = null;
		try
		{
			InetAddress inStr = InetAddress.getByName("passport_i.25pp.com");

			client = new Socket(inStr, 8080);
			client.setReuseAddress(false);

			InputStream in = client.getInputStream();
			OutputStream out = client.getOutputStream();

			ByteBuffer inbf = ByteBuffer.allocate(length);
			inbf.order(ByteOrder.LITTLE_ENDIAN);
			inbf.putInt(length);
			inbf.putInt(0xAA000022);
			inbf.put(array);
			inbf.rewind();
			out.write(inbf.array());
			out.flush();

			byte[] read = readStream(in);
			CPPVerifyVO recvPSData = new CPPVerifyVO();
			ByteBuffer otbf = ByteBuffer.wrap(read);
			otbf.order(ByteOrder.LITTLE_ENDIAN);
			recvPSData.setLen(otbf.getInt());
			recvPSData.setCommand(otbf.getInt());
			recvPSData.setStatus(otbf.getInt());
			ChannelVerify.GenerateLog(String.format("Recv from PP Server data status: %x", recvPSData.getStatus()));
			if (read.length > CPPVerify.MiniObjectSize)
			{
				byte busername[] = new byte[recvPSData.getLen() - (3 * 4 + 8)]; // 取
																				// username
																				// 字节长度为
																				// RecvPSData.getLen()-(3*4+8)
				otbf.get(busername, 0, recvPSData.getLen() - (3 * 4 + 8));
				String username = new String(busername, "UTF-8");
				recvPSData.setUserid(otbf.getLong());
				recvPSData.setUsername(username);

				ChannelVerify.GenerateLog("Recv from PP Server data length: " + recvPSData.getLen());
				ChannelVerify.GenerateLog(String.format("Recv from PP Server data command: %x", recvPSData.getCommand()));
				ChannelVerify.GenerateLog("Recv from PP Server data username: " + recvPSData.getUsername());
				ChannelVerify.GenerateLog("Recv from PP Server data userid: " + recvPSData.getUserid());
				
				return recvPSData.getUserid() + "";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				client.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * 读取流
	 * 
	 * @param inStream
	 * @return 字节数组
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception
	{
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		/* while */if ((len = inStream.read(buffer)) != -1)
		{
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		return outSteam.toByteArray();
	}
}
