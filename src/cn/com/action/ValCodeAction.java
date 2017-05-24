package cn.com.action;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * ����������֤���Servlet��
 * 
 * @author sky
 * 
 */

@Controller
@RequestMapping(value = "/valCodeAction")
public class ValCodeAction {

	Random random = new Random();
	String str = "123456789ABCDEFG�Ұ��л����񹲺͹�";

	public ValCodeAction() {

	}

	/**
	 * ��ȡ��������
	 * @return
	 */
	private String getRandomStr() {
		String ranStr = "";
		for (int i = 0; i < 4; i++) {
			int index = random.nextInt(str.length());
			ranStr += str.charAt(index);
		}
		return ranStr;
	}

	/**
	 * ���ɱ�����ɫ
	 * @return
	 */
	public Color getBackGround() {
		int red = random.nextInt(256);
		int green = random.nextInt(256);
		int blue = random.nextInt(256);
		return new Color(red, green, blue);
	}

	/**
	 * ��ȡǰ��ɫ
	 * @param backGround
	 * @return
	 */
	public Color getForeGround(Color backGround) {
		int red = 255 - backGround.getRed();
		int green = 255 - backGround.getGreen();
		int blue = 255 - backGround.getBlue();
		return new Color(red, green, blue);
	}
	
	@RequestMapping("/test")
	public String test(){
		return "index";
	}
	
	@RequestMapping(value="/valCode")
	public void createValCode(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 1.����response ��mime
		// response.setContentType("text/html;charset=utf-8");//��Ӧ���������ַ�����
		response.setContentType("image/JPEG");
		HttpSession session = request.getSession();
		// 2. ��ȡ�����
		String ranStr = getRandomStr();
		// 3.����BufferedImage
		BufferedImage buffImg = new BufferedImage(80, 30,
				BufferedImage.TYPE_INT_RGB);
		// 4.����ͼƬ
		Graphics g = buffImg.getGraphics();
		Color bkColor = getBackGround();
		g.setColor(bkColor);
		g.fillRect(0, 0, 80, 30);
		g.setColor(getForeGround(bkColor));
		g.setFont(new Font("����", Font.BOLD, 20));
		g.drawString(ranStr, 10, 20);
		// 5.�������
		for (int i = 1; i <= 20; i++) {
			int x = random.nextInt(80 - 2);
			int y = random.nextInt(30 - 2);
			g.setColor(Color.white);
			g.fillRect(x, y, 2, 2);
		}
		// 6.����session
		session.setAttribute("code", ranStr);
		// 7.��BufferedImage���浽�����
		ServletOutputStream sos = response.getOutputStream();
		JPEGImageEncoder coder = JPEGCodec.createJPEGEncoder(sos);
		coder.encode(buffImg);
		sos.flush();
		sos.close();
	}
}
