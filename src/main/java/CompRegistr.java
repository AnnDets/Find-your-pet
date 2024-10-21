
import javax.swing.*;

import java.awt.*;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
public class CompRegistr {
	public static void main(String[] args) throws InterruptedException {
		
		//Фон
		JFrame frame = new JFrame();
		frame.setSize(2400, 1600);
		frame.setTitle("Find your pet");
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		Image logo = Toolkit.getDefaultToolkit().getImage("D:\\рисунка\\logo.png");
		frame.setIconImage(logo);
		//что произлйдет при закрывании приложения (нажатия на крестик)
		//если указано EXIT_On_CLOSE, то программа закроентся, 
		//если этой страчки не будет, то окно закроется, а программа просто продолжит работу
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//означает, где открывается форма, если null, то посередине
		//frame.setLayout(new BorderLayout());
		Container contentPane = frame.getContentPane();
        contentPane.setBackground(new Color(250, 232, 184)); 
        frame.setLocationRelativeTo(null);
		frame.setLayout(new FlowLayout());
		//Курсор
		Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
		
		
		JPanel panel1 = new JPanel();
		panel1.setBackground(new Color(250, 232, 184));
		panel1.setPreferredSize(new Dimension(2400, 80));
		Font font1 = new Font("Arial", Font.BOLD, 50);
		JLabel label1 = new JLabel();
		label1.setText("Регистрация");
		label1.setFont(font1);
		label1.setForeground(Color.BLACK);
		

		JPanel panel2 = new JPanel();
		panel2.setBackground(new Color(250, 232, 184));
		panel2.setPreferredSize(new Dimension(2400, 80));
		
		JPanel panel3 = new JPanel();
		panel3.setBackground(new Color(250, 232, 184));
		panel3.setPreferredSize(new Dimension(2400, 80));
		JPanel panel4 = new JPanel();
		panel4.setBackground(new Color(250, 232, 184));
		panel4.setPreferredSize(new Dimension(700, 80));
		JPanel panel5 = new JPanel();
		panel5.setBackground(new Color(250, 232, 184));
		panel5.setPreferredSize(new Dimension(1000, 80));
		//Надпись "Имя"
		Font font2 = new Font("Arial", Font.TYPE1_FONT, 40);
		JLabel label2 = new JLabel();
		label2.setText("Имя:");
		label2.setFont(font2);
		label2.setForeground(Color.BLACK);
		//Надпись "*"
		JLabel label3 = new JLabel();
		label3.setText("*");
		label3.setFont(font2);
		label3.setForeground(Color.RED);
		//Поле ввода рядом с именем
		JTextField textField1 = new JTextField(56);
		textField1.setBackground(new Color(250, 232, 184));
		
		
		JPanel panel6 = new JPanel();
		panel6.setBackground(new Color(250, 232, 184));
		panel6.setPreferredSize(new Dimension(2400, 80));
		JPanel panel7 = new JPanel();
		panel7.setBackground(new Color(250, 232, 184));
		panel7.setPreferredSize(new Dimension(700, 80));
		JPanel panel8 = new JPanel();
		panel8.setBackground(new Color(250, 232, 184));
		panel8.setPreferredSize(new Dimension(1000, 80));
		//Надпись "Адресс"
		JLabel label4 = new JLabel();
		label4.setText("Адресс:");
		label4.setFont(font2);
		label4.setForeground(Color.BLACK);		
		//Надпись "*"
		JLabel label5 = new JLabel();
		label5.setText("*");
		label5.setFont(font2);
		label5.setForeground(Color.RED);
		//Поле ввода рядом с adress
		JTextField textField2 = new JTextField(50);
		textField2.setBackground(new Color(250, 232, 184));
		
		
		JPanel panel9 = new JPanel();
		panel9.setBackground(new Color(250, 232, 184));
		panel9.setPreferredSize(new Dimension(2400, 80));
		JPanel panel10 = new JPanel();
		panel10.setBackground(new Color(250, 232, 184));
		panel10.setPreferredSize(new Dimension(700, 80));
		JPanel panel11 = new JPanel();
		panel11.setBackground(new Color(250, 232, 184));
		panel11.setPreferredSize(new Dimension(1000, 80));
		//Надпись "Телефон"
		JLabel label6 = new JLabel();
		label6.setText("Телефон:");
		label6.setFont(font2);
		label6.setForeground(Color.BLACK);
		//Надпись "*"
		JLabel label7 = new JLabel();
		label7.setText("*");
		label7.setFont(font2);
		label7.setForeground(Color.RED);
		//Поле ввода рядом с adress
		JTextField textField3 = new JTextField(47);
		textField3.setBackground(new Color(250, 232, 184));

		
		JPanel panel12 = new JPanel();
		panel12.setBackground(new Color(250, 232, 184));
		panel12.setPreferredSize(new Dimension(2400, 80));
		JPanel panel13 = new JPanel();
		panel13.setBackground(new Color(250, 232, 184));
		panel13.setPreferredSize(new Dimension(700, 80));
		JPanel panel14 = new JPanel();
		panel14.setBackground(new Color(250, 232, 184));
		panel14.setPreferredSize(new Dimension(1000, 80));
		//Надпись "Email"
		JLabel label8 = new JLabel();
		label8.setText("Email:");
		label8.setFont(font2);
		label8.setForeground(Color.BLACK);
		//Надпись "*"
		JLabel label9 = new JLabel();
		label9.setText("*");
		label9.setFont(font2);
		label9.setForeground(Color.RED);
		//Поле ввода рядом с adress
		JTextField textField4 = new JTextField(54);
		textField4.setBackground(new Color(250, 232, 184));
				
		
		JPanel panel15 = new JPanel();
		panel15.setBackground(new Color(250, 232, 184));
		panel15.setPreferredSize(new Dimension(2400, 80));
		JPanel panel16 = new JPanel();
		panel16.setBackground(new Color(250, 232, 184));
		panel16.setPreferredSize(new Dimension(700, 80));
		JPanel panel17 = new JPanel();
		panel17.setBackground(new Color(250, 232, 184));
		panel17.setPreferredSize(new Dimension(1000, 80));
		//Надпись "Пароль"
		JLabel label10 = new JLabel();
		label10.setText("Пароль:");
		label10.setFont(font2);
		label10.setForeground(Color.BLACK);
		//Надпись "*"
		JLabel label11 = new JLabel();
		label11.setText("*");
		label11.setFont(font2);
		label11.setForeground(Color.RED);
		//Поле ввода рядом с password
		JPasswordField password1 = new JPasswordField(50);
		password1.setBackground(new Color(250, 232, 184));
		
		
		JPanel panel18 = new JPanel();
		panel18.setBackground(new Color(250, 232, 184));
		panel18.setPreferredSize(new Dimension(2400, 80));
		JPanel panel19 = new JPanel();
		panel19.setBackground(new Color(250, 232, 184));
		panel19.setPreferredSize(new Dimension(700, 80));
		JPanel panel20 = new JPanel();
		panel20.setBackground(new Color(250, 232, 184));
		panel20.setPreferredSize(new Dimension(1000, 80));
		//Надпись "repeat password"
		JLabel label12 = new JLabel();
		label12.setText("Повторите пароль:");
		label12.setFont(font2);
		label12.setForeground(Color.BLACK);
		//Надпись "*"
		JLabel label13 = new JLabel();
		label13.setText("*");
		label13.setFont(font2);
		label13.setForeground(Color.RED);		
		//Поле ввода рядом с adress
		JPasswordField password2 = new JPasswordField(25);
		password2.setBackground(new Color(250, 232, 184));
		
		JPanel panel21 = new JPanel();
		panel21.setBackground(new Color(250, 232, 184));
		panel21.setPreferredSize(new Dimension(2400, 80));
		JPanel panel22 = new JPanel();
		panel22.setBackground(new Color(250, 232, 184));
		panel22.setPreferredSize(new Dimension(700, 80));
		JPanel panel23 = new JPanel();
		panel23.setBackground(new Color(250, 232, 184));
		panel23.setPreferredSize(new Dimension(1000, 80));
		Font font3 = new Font("Arial", Font.TYPE1_FONT, 20);
		JLabel label14 = new JLabel();
		label14.setText("Поля со * являются обязательными!");
		label14.setFont(font3);
		label14.setForeground(Color.RED);

		JPanel panel24 = new JPanel();
		panel24.setBackground(new Color(250, 232, 184));
		panel24.setPreferredSize(new Dimension(2400, 80));
		JPanel panel25 = new JPanel();
		panel25.setBackground(new Color(250, 232, 184));
		panel25.setPreferredSize(new Dimension(700, 80));
		JPanel panel26 = new JPanel();
		panel26.setBackground(new Color(250, 232, 184));
		panel26.setPreferredSize(new Dimension(1000, 80));
		JButton button1 = new JButton();
		button1.setText("Зарегистрироваться");
		button1.setBackground(new Color(250, 232, 184));
		button1.setCursor(cursor);
		button1.setSize(40, 10);
		
		
		
		
		
		frame.add(panel1);
		panel1.add(label1);
		
		frame.add(panel2);
		
		frame.add(panel3);
		panel3.add(panel4);
		panel3.add(panel5);
		panel4.add(label2);
		panel4.add(label3);
		panel4.add(textField1);
		
		frame.add(panel6);
		panel6.add(panel7);
		panel6.add(panel8);
		panel7.add(label4);
		panel7.add(label5);
		panel7.add(textField2);
		
		frame.add(panel9);
		panel9.add(panel10);
		panel9.add(panel11);
		panel10.add(label6);
		panel10.add(label7);
		panel10.add(textField3);
		
		frame.add(panel12);
		panel12.add(panel13);
		panel12.add(panel14);
		panel13.add(label8);
		panel13.add(label9);
		panel13.add(textField4);
		
		frame.add(panel15);
		panel15.add(panel16);
		panel15.add(panel17);
		panel16.add(label10);
		panel16.add(label11);
		panel16.add(password1);

		frame.add(panel18);
		panel18.add(panel19);
		panel18.add(panel20);
		panel19.add(label12);
		panel19.add(label13);
		panel19.add(password2);
		
		frame.add(panel21);
		panel21.add(panel22);
		panel21.add(panel23);
		panel22.add(label14);
		

		frame.add(panel24);
		panel24.add(panel25);
		panel24.add(panel26);
		panel25.add(button1);

		frame.setVisible(true);
		button1.addActionListener(e -> {
			try {
				// Собираем данные из полей ввода
				String name = textField1.getText();
				String address = textField2.getText();
				String phone = textField3.getText();
				String email = textField4.getText();
				String password = new String(password1.getPassword());

				// Создаем JSON-объект
				JSONObject json = new JSONObject();
				json.put("name", name);
				json.put("address", address);
				json.put("phone", phone);
				json.put("email", email);
				json.put("password", password);

				// Устанавливаем URL для отправки запроса
				URL url = new URL("http://localhost:8080/register");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json; utf-8");
				conn.setRequestProperty("Accept", "application/json");
				conn.setDoOutput(true);

				// Отправляем данные
				try (OutputStream os = conn.getOutputStream()) {
					byte[] input = json.toString().getBytes("utf-8");
					os.write(input, 0, input.length);
				}

				// Получаем ответ
				int responseCode = conn.getResponseCode();
				System.out.println("Response Code: " + responseCode);

				InputStream inputStream;
				if (responseCode >= 200 && responseCode < 300) {
					// Если успешный запрос
					inputStream = conn.getInputStream();
				} else {
					// Если произошла ошибка
					inputStream = conn.getErrorStream();
				}

				// Читаем ответ от сервера
				try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"))) {
					StringBuilder response = new StringBuilder();
					String responseLine;
					while ((responseLine = br.readLine()) != null) {
						response.append(responseLine.trim());
					}
					// Выводим полученный JSON в консоль
					System.out.println("Response JSON: " + response.toString());
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

	}
}
