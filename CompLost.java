
import javax.swing.*;

import java.awt.*;

public class CompLost {
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
		JLabel labelLost = new JLabel();
		labelLost.setText("Я потерял");
		labelLost.setFont(font1);
		labelLost.setForeground(Color.BLACK);
		
		JPanel panel2 = new JPanel();
		panel2.setBackground(new Color(250, 232, 184));
		panel2.setPreferredSize(new Dimension(1700, 80));
		
		JPanel panel3 = new JPanel();
		panel3.setBackground(new Color(250, 232, 184));
		panel3.setPreferredSize(new Dimension(1700, 390));
		JPanel panel31 = new JPanel();
		panel31.setBackground(new Color(250, 232, 184));
		panel31.setPreferredSize(new Dimension(430, 390));
		//Кнопка "Вставить фото"
		JButton buttonInsert = new JButton();
		buttonInsert.setText("Вставить фото");
		buttonInsert.setBackground(Color.WHITE);
		buttonInsert.setCursor(cursor);
		buttonInsert.setPreferredSize(new Dimension(390, 370));
		buttonInsert.setFont(font2);
		
		JPanel panel32 = new JPanel();
		panel32.setBackground(new Color(250, 232, 184));
		panel32.setPreferredSize(new Dimension(1250, 390));
		JPanel panel321 = new JPanel();
		panel321.setBackground(new Color(250, 232, 184));
		panel321.setPreferredSize(new Dimension(1250, 70));
		JPanel panel322 = new JPanel();
		panel322.setBackground(new Color(250, 232, 184));
		panel322.setPreferredSize(new Dimension(1250, 70));
		JPanel panel323 = new JPanel();
		panel323.setBackground(new Color(250, 232, 184));
		panel323.setPreferredSize(new Dimension(1250, 70));
		JPanel panel324 = new JPanel();
		panel324.setBackground(new Color(250, 232, 184));
		panel324.setPreferredSize(new Dimension(1250, 70));
		JPanel panel325 = new JPanel();
		panel325.setBackground(new Color(250, 232, 184));
		panel325.setPreferredSize(new Dimension(1250, 70));
		
		JPanel panel3211 = new JPanel();
		panel3211.setBackground(new Color(250, 232, 184));
		panel3211.setPreferredSize(new Dimension(690, 70));
		JPanel panel3212 = new JPanel();
		panel3212.setBackground(new Color(250, 232, 184));
		panel3212.setPreferredSize(new Dimension(540, 70));
		//Надпись "Кличка"
		JLabel labelName = new JLabel();
		labelName.setText("Кличка:");
		labelName.setFont(font2);
		labelName.setForeground(Color.BLACK);
		//Поле ввода рядом с Кличка
		JTextField textFieldName = new JTextField(20);
		textFieldName.setBackground(new Color(250, 232, 184));
		textFieldName.setFont(font5);
		
		JPanel panel3221 = new JPanel();
		panel3221.setBackground(new Color(250, 232, 184));
		panel3221.setPreferredSize(new Dimension(650, 70));
		JPanel panel3222 = new JPanel();
		panel3222.setBackground(new Color(250, 232, 184));
		panel3222.setPreferredSize(new Dimension(580, 70));
		//Надпись "Цвет"
		JLabel labelColor = new JLabel();
		labelColor.setText("Цвет:");
		labelColor.setFont(font2);
		labelColor.setForeground(Color.BLACK);
		//Поле ввода рядом с Цвет
		JTextField textFieldColor = new JTextField(20);
		textFieldColor.setBackground(new Color(250, 232, 184));
		textFieldColor.setFont(font5);
		
		JPanel panel3231 = new JPanel();
		panel3231.setBackground(new Color(250, 232, 184));
		panel3231.setPreferredSize(new Dimension(1186, 70));
		JPanel panel3232 = new JPanel();
		panel3232.setBackground(new Color(250, 232, 184));
		panel3232.setPreferredSize(new Dimension(44, 70));
		//Надпись "Животное"
		JLabel labelAnimal = new JLabel();
		labelAnimal.setText("Животное:");
		labelAnimal.setFont(font2);
		labelAnimal.setForeground(Color.BLACK);
		//Надпись "*"
		JLabel label1 = new JLabel();
		label1.setText("*");
		label1.setFont(font2);
		label1.setForeground(Color.RED);
		//Кнопка "Кот"
		JButton buttonCat = new JButton();
		buttonCat.setText("Кот");
		buttonCat.setBackground(new Color(250, 232, 184));
		buttonCat.setCursor(cursor);
		buttonCat.setPreferredSize(new Dimension(130, 30));
		buttonCat.setFont(font3);
		//Кнопка "Собака"
		JButton buttonDog = new JButton();
		buttonDog.setText("Собака");
		buttonDog.setBackground(new Color(250, 232, 184));
		buttonDog.setCursor(cursor);
		buttonDog.setPreferredSize(new Dimension(130, 30));
		buttonDog.setFont(font3);
		//Кнопка "Другое"
		JButton buttonOther1 = new JButton();
		buttonOther1.setText("Другое");
		buttonOther1.setBackground(new Color(250, 232, 184));
		buttonOther1.setCursor(cursor);
		buttonOther1.setPreferredSize(new Dimension(130, 30));
		buttonOther1.setFont(font3);
		//Поле ввода рядом с животное
		JTextField textFieldAnimal = new JTextField(20);
		textFieldAnimal.setBackground(new Color(250, 232, 184));
		textFieldAnimal.setFont(font5);
		
		JPanel panel3241 = new JPanel();
		panel3241.setBackground(new Color(250, 232, 184));
		panel3241.setPreferredSize(new Dimension(1055, 70));
		JPanel panel3242 = new JPanel();
		panel3242.setBackground(new Color(250, 232, 184));
		panel3242.setPreferredSize(new Dimension(175, 70));
		//Надпись "Пол"
		JLabel labelGender = new JLabel();
		labelGender.setText("Пол:");
		labelGender.setFont(font2);
		labelGender.setForeground(Color.BLACK);
		//Надпись "*"
		JLabel label2 = new JLabel();
		label2.setText("*");
		label2.setFont(font2);
		label2.setForeground(Color.RED);
		//Кнопка "Мальчик"
		JButton buttonBoy = new JButton();
		buttonBoy.setText("Мальчик");
		buttonBoy.setBackground(new Color(250, 232, 184));
		buttonBoy.setCursor(cursor);
		buttonBoy.setPreferredSize(new Dimension(130, 30));
		buttonBoy.setFont(font3);
		//Кнопка "Девочка"
		JButton buttonGirl = new JButton();
		buttonGirl.setText("Девочка");
		buttonGirl.setBackground(new Color(250, 232, 184));
		buttonGirl.setCursor(cursor);
		buttonGirl.setPreferredSize(new Dimension(130, 30));
		buttonGirl.setFont(font3);
		//Кнопка "Другое"
		JButton buttonOther2 = new JButton();
		buttonOther2.setText("Другое");
		buttonOther2.setBackground(new Color(250, 232, 184));
		buttonOther2.setCursor(cursor);
		buttonOther2.setPreferredSize(new Dimension(130, 30));
		buttonOther2.setFont(font3);
		//Поле ввода рядом с Пол
		JTextField textFieldGender = new JTextField(20);
		textFieldGender.setBackground(new Color(250, 232, 184));
		textFieldGender.setFont(font5);
		
		JPanel panel3251 = new JPanel();
		panel3251.setBackground(new Color(250, 232, 184));
		panel3251.setPreferredSize(new Dimension(700, 80));
		JPanel panel3252 = new JPanel();
		panel3252.setBackground(new Color(250, 232, 184));
		panel3252.setPreferredSize(new Dimension(530, 80));
		//Надпись "Порода"
		JLabel labelBreed = new JLabel();
		labelBreed.setText("Порода:");
		labelBreed.setFont(font2);
		labelBreed.setForeground(Color.BLACK);
		//Поле ввода рядом с Порода
		JTextField textFieldBreed = new JTextField(20);
		textFieldBreed.setBackground(new Color(250, 232, 184));
		textFieldBreed.setFont(font5);
	
		JPanel panel4 = new JPanel();
		panel4.setBackground(new Color(250, 232, 184));
		panel4.setPreferredSize(new Dimension(1700, 80));
		JPanel panel41 = new JPanel();
		panel41.setBackground(new Color(250, 232, 184));
		panel41.setPreferredSize(new Dimension(900, 80));
		JPanel panel42 = new JPanel();
		panel42.setBackground(new Color(250, 232, 184));
		panel42.setPreferredSize(new Dimension(750, 80));
		//Надпись "Дата"
		JLabel labelDate = new JLabel();
		labelDate.setText("Дата пропажи:");
		labelDate.setFont(font2);
		labelDate.setForeground(Color.BLACK);
		//Надпись "*"
		JLabel label3 = new JLabel();
		label3.setText("*");
		label3.setFont(font2);
		label3.setForeground(Color.RED);
		//Поле ввода рядом с Дата
		JTextField textFieldDate = new JTextField(20);
		textFieldDate.setBackground(new Color(250, 232, 184));
		textFieldDate.setFont(font5);
		
		JPanel panel5 = new JPanel();
		panel5.setBackground(new Color(250, 232, 184));
		panel5.setPreferredSize(new Dimension(1700, 80));
		JPanel panel51 = new JPanel();
		panel51.setBackground(new Color(250, 232, 184));
		panel51.setPreferredSize(new Dimension(1305, 80));
		JPanel panel52 = new JPanel();
		panel52.setBackground(new Color(250, 232, 184));
		panel52.setPreferredSize(new Dimension(345, 80));
		//Надпись "Наличие аксессуаров"
		JLabel labelAccessory = new JLabel();
		labelAccessory.setText("Наличие аксессуаров:");
		labelAccessory.setFont(font2);
		labelAccessory.setForeground(Color.BLACK);
		//Кнопка "Нет"
		JButton buttonNo = new JButton();
		buttonNo.setText("Нет");
		buttonNo.setBackground(new Color(250, 232, 184));
		buttonNo.setCursor(cursor);
		buttonNo.setPreferredSize(new Dimension(130, 30));
		buttonNo.setFont(font3);
		//Кнопка "Да"
		JButton buttonYes = new JButton();
		buttonYes.setText("Да");
		buttonYes.setBackground(new Color(250, 232, 184));
		buttonYes.setCursor(cursor);
		buttonYes.setPreferredSize(new Dimension(130, 30));
		buttonYes.setFont(font3);
		//Поле ввода рядом с Accessory
		JTextField textFieldAccessory = new JTextField(20);
		textFieldAccessory.setBackground(new Color(250, 232, 184));
		textFieldAccessory.setFont(font5);
		
		JPanel panel6 = new JPanel();
		panel6.setBackground(new Color(250, 232, 184));
		panel6.setPreferredSize(new Dimension(1700, 80));
		JPanel panel61 = new JPanel();
		panel61.setBackground(new Color(250, 232, 184));
		panel61.setPreferredSize(new Dimension(1200, 80));
		JPanel panel62 = new JPanel();
		panel62.setBackground(new Color(250, 232, 184));
		panel62.setPreferredSize(new Dimension(450, 80));
		//Надпись "Дополнительная информация"
		JLabel labelInfo = new JLabel();
		labelInfo.setText("Дополнительная информация:");
		labelInfo.setFont(font2);
		labelInfo.setForeground(Color.BLACK);
		//Поле ввода рядом с Дополнительная информация
		JTextField textFieldInfo = new JTextField(20);
		textFieldInfo.setBackground(new Color(250, 232, 184));
		textFieldInfo.setFont(font5);
		
		JPanel panel7 = new JPanel();
		panel7.setBackground(new Color(250, 232, 184));
		panel7.setPreferredSize(new Dimension(1700, 80));
		JLabel labelIndication = new JLabel();
		labelIndication.setText("Поля со * являются обязательными!");
		labelIndication.setFont(font3);
		labelIndication.setForeground(Color.RED);
		
		JPanel panel8 = new JPanel();
		panel8.setBackground(new Color(250, 232, 184));
		panel8.setPreferredSize(new Dimension(1700, 80));
		JButton buttonSend = new JButton();
		buttonSend.setText("Отправить");
		buttonSend.setBackground(new Color(250, 232, 184));
		buttonSend.setCursor(cursor);
		buttonSend.setSize(40, 10);
		buttonSend.setFont(font4);
		
		
			
		frame.add(panel1);
		panel1.add(labelLost);
		
		frame.add(panel2);
		
		frame.add(panel3);
		panel3.add(panel31);
		panel3.add(panel32);
		panel31.add(buttonInsert);
		
		panel32.add(panel321);
		panel32.add(panel322);
		panel32.add(panel323);
		panel32.add(panel324);
		panel32.add(panel325);
		
		panel321.add(panel3211);
		panel321.add(panel3212);
		panel3211.add(labelName);
		panel3211.add(textFieldName);
		
		panel322.add(panel3221);
		panel322.add(panel3222);
		panel3221.add(labelColor);
		panel3221.add(textFieldColor);
		
		panel323.add(panel3231);
		panel323.add(panel3232);
		panel3231.add(labelAnimal);
		panel3231.add(label1);
		panel3231.add(buttonCat);
		panel3231.add(buttonDog);
		panel3231.add(buttonOther1);
		panel3231.add(textFieldAnimal);
		
		panel324.add(panel3241);
		panel324.add(panel3242);
		panel3241.add(labelGender);
		panel3241.add(label2);
		panel3241.add(buttonBoy);
		panel3241.add(buttonGirl);
		panel3241.add(buttonOther2);
		panel3241.add(textFieldGender);
		
		panel325.add(panel3251);
		panel325.add(panel3252);
		panel3251.add(labelBreed);
		panel3251.add(textFieldBreed);
		
		frame.add(panel4);
		panel4.add(panel41);
		panel4.add(panel42);
		panel41.add(labelDate);
		panel41.add(label3);
		panel41.add(textFieldDate);
		
		frame.add(panel5);
		panel5.add(panel51);
		panel5.add(panel52);
		panel51.add(labelAccessory);
		panel51.add(buttonNo);
		panel51.add(buttonYes);
		panel51.add(textFieldAccessory);
		
		frame.add(panel6);
		panel6.add(panel61);
		panel6.add(panel62);
		panel61.add(labelInfo);
		panel61.add(textFieldInfo);
		
		frame.add(panel7);
		panel7.add(labelIndication);
		
		frame.add(panel8);
		panel8.add(buttonSend);
		
		frame.setVisible(true);
	}
}
