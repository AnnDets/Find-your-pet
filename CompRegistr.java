
import javax.swing.*;

import java.awt.*;

public class CompRegistr {
	public static void main(String[] args) throws InterruptedException {
		
		//Фон
		JFrame frame = new JFrame();
		frame.setSize(1700, 1100);
		frame.setTitle("Find your pet");
		Image logo = Toolkit.getDefaultToolkit().getImage("D:\\рисунка\\logo.png");
		frame.setIconImage(logo);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = frame.getContentPane();
        contentPane.setBackground(new Color(250, 232, 184)); 
        frame.setLocationRelativeTo(null);
		frame.setLayout(new FlowLayout());
		//Курсор
		Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
		
		//Стили
		Font font1 = new Font("Arial", Font.BOLD, 50);//Для заголовка
		Font font2 = new Font("Arial", Font.TYPE1_FONT, 40);//Для названий полей
		Font font3 = new Font("Arial", Font.TYPE1_FONT, 20);//Для приписок внизу
		Font font4 = new Font("Arial", Font.TYPE1_FONT, 30);//Для кнопки "зарегистрироваться"
		Font font5 = new Font("Arial", Font.ITALIC, 30);//Для полей ввода
		
		
		JPanel panel1 = new JPanel();
		panel1.setBackground(new Color(250, 232, 184));
		panel1.setPreferredSize(new Dimension(1700, 80));
		JLabel label1 = new JLabel();
		label1.setText("Регистрация");
		label1.setFont(font1);
		label1.setForeground(Color.BLACK);
		

		JPanel panel2 = new JPanel();
		panel2.setBackground(new Color(250, 232, 184));
		panel2.setPreferredSize(new Dimension(1700, 80));
		
		JPanel panel3 = new JPanel();
		panel3.setBackground(new Color(250, 232, 184));
		panel3.setPreferredSize(new Dimension(1700, 80));
		//Надпись "Имя"
		JLabel labelName = new JLabel();
		labelName.setText("Имя:");
		labelName.setFont(font2);
		labelName.setForeground(Color.BLACK);
		//Надпись "*"
		JLabel label3 = new JLabel();
		label3.setText("*");
		label3.setFont(font2);
		label3.setForeground(Color.RED);
		//Поле ввода рядом с именем
		JTextField textFieldName = new JTextField(20);
		textFieldName.setBackground(new Color(250, 232, 184));
		textFieldName.setFont(font5);
		
		
		JPanel panel4 = new JPanel();
		panel4.setBackground(new Color(250, 232, 184));
		panel4.setPreferredSize(new Dimension(1700, 80));
		//Надпись "Адресс"
		JLabel labelAdress = new JLabel();
		labelAdress.setText("Адресс:");
		labelAdress.setFont(font2);
		labelAdress.setForeground(Color.BLACK);		
		//Надпись "*"
		JLabel label4 = new JLabel();
		label4.setText("*");
		label4.setFont(font2);
		label4.setForeground(Color.RED);
		//Поле ввода рядом с adress
		JTextField textFieldAdress = new JTextField(20);
		textFieldAdress.setBackground(new Color(250, 232, 184));
		textFieldAdress.setFont(font5);
		
		JPanel panel5 = new JPanel();
		panel5.setBackground(new Color(250, 232, 184));
		panel5.setPreferredSize(new Dimension(1700, 80));
		//Надпись "Телефон"
		JLabel labelTel = new JLabel();
		labelTel.setText("Телефон:");
		labelTel.setFont(font2);
		labelTel.setForeground(Color.BLACK);
		//Надпись "*"
		JLabel label5 = new JLabel();
		label5.setText("*");
		label5.setFont(font2);
		label5.setForeground(Color.RED);
		//Поле ввода рядом с telephone
		JTextField textFieldTel = new JTextField(20);
		textFieldTel.setBackground(new Color(250, 232, 184));
		textFieldTel.setFont(font5);
		
		JPanel panel6 = new JPanel();
		panel6.setBackground(new Color(250, 232, 184));
		panel6.setPreferredSize(new Dimension(1700, 80));
		//Надпись "Email"
		JLabel labelEmail = new JLabel();
		labelEmail.setText("Email:");
		labelEmail.setFont(font2);
		labelEmail.setForeground(Color.BLACK);
		//Надпись "*"
		JLabel label6 = new JLabel();
		label6.setText("*");
		label6.setFont(font2);
		label6.setForeground(Color.RED);
		//Поле ввода рядом с email
		JTextField textFieldEmail = new JTextField(20);
		textFieldEmail.setBackground(new Color(250, 232, 184));
		textFieldEmail.setFont(font5);		
		
		JPanel panel7 = new JPanel();
		panel7.setBackground(new Color(250, 232, 184));
		panel7.setPreferredSize(new Dimension(1700, 80));
		//Надпись "Пароль"
		JLabel labelPassword = new JLabel();
		labelPassword.setText("Пароль:");
		labelPassword.setFont(font2);
		labelPassword.setForeground(Color.BLACK);
		//Надпись "*"
		JLabel label7 = new JLabel();
		label7.setText("*");
		label7.setFont(font2);
		label7.setForeground(Color.RED);
		//Поле ввода рядом с password
		JPasswordField password = new JPasswordField(20);
		password.setBackground(new Color(250, 232, 184));
		password.setFont(font5);
		
		JPanel panel8 = new JPanel();
		panel8.setBackground(new Color(250, 232, 184));
		panel8.setPreferredSize(new Dimension(1700, 80));
		//Надпись "repeat password"
		JLabel labelRePassword = new JLabel();
		labelRePassword.setText("Повторите пароль:");
		labelRePassword.setFont(font2);
		labelRePassword.setForeground(Color.BLACK);
		//Надпись "*"
		JLabel label8 = new JLabel();
		label8.setText("*");
		label8.setFont(font2);
		label8.setForeground(Color.RED);		
		//Поле ввода рядом с repeat password
		JPasswordField rePassword = new JPasswordField(20);
		rePassword.setBackground(new Color(250, 232, 184));
		rePassword.setFont(font5);
		
		JPanel panel9 = new JPanel();
		panel9.setBackground(new Color(250, 232, 184));
		panel9.setPreferredSize(new Dimension(1700, 80));
		JLabel labelIndication = new JLabel();
		labelIndication.setText("Поля со * являются обязательными!");
		labelIndication.setFont(font3);
		labelIndication.setForeground(Color.RED);
		
		JPanel panel10 = new JPanel();
		panel10.setBackground(new Color(250, 232, 184));
		panel10.setPreferredSize(new Dimension(1700, 80));
		JCheckBox checkBox = new JCheckBox("Вы соглашаетесь с политикой конфиденциальности");
		checkBox.setBackground(new Color(250, 232, 184));
		checkBox.setFont(font3);
		
		JPanel panel11 = new JPanel();
		panel11.setBackground(new Color(250, 232, 184));
		panel11.setPreferredSize(new Dimension(1700, 80));
		JButton button = new JButton();
		button.setText("Зарегистрироваться");
		button.setBackground(new Color(250, 232, 184));
		button.setCursor(cursor);
		button.setSize(40, 10);
		button.setFont(font4);
		
		frame.add(panel1);
		panel1.add(label1);
		
		frame.add(panel2);
		
		frame.add(panel3);
		panel3.add(labelName);
		panel3.add(label3);
		panel3.add(textFieldName);
		
		frame.add(panel4);
		panel4.add(labelAdress);
		panel4.add(label4);
		panel4.add(textFieldAdress);
		
		frame.add(panel5);
		panel5.add(labelTel);
		panel5.add(label5);
		panel5.add(textFieldTel);
		
		frame.add(panel6);
		panel6.add(labelEmail);
		panel6.add(label6);
		panel6.add(textFieldEmail);
		
		frame.add(panel7);
		panel7.add(labelPassword);
		panel7.add(label7);
		panel7.add(password);

		frame.add(panel8);
		panel8.add(labelRePassword);
		panel8.add(label8);
		panel8.add(rePassword);
		
		frame.add(panel9);
		panel9.add(labelIndication);
		
		frame.add(panel10);
		panel10.add(checkBox);
		
		frame.add(panel11);
		panel11.add(button);
		
		frame.setVisible(true);
	}
}