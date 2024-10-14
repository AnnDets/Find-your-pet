package utils;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class PhoneAndEmailValidator {
    static Map<String, String> countryMap = new HashMap<>();
    public static PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
    public static Set<String> regions = phoneUtil.getSupportedRegions();
    static Logger logger_;
    private void setupLogger() throws IOException {
        FileHandler fileHandler = new FileHandler("DBlog.log", true); // true для добавления, а не перезаписи
        fileHandler.setFormatter(new SimpleFormatter()); // Простой формат логов
        logger_.addHandler(fileHandler);
        logger_.setLevel(Level.ALL);
    }
    public PhoneAndEmailValidator() {
        setupMap();
        try {
            setupLogger();
        } catch (IOException e){
            System.err.println("Ошибка настройки логгера: " + e.getMessage());
        }

    }
    private void setupMap(){
        countryMap.put("Беларусь", "BY");
        countryMap.put("США", "US");
        countryMap.put("Россия", "RU");
        countryMap.put("Канада", "CA");
        countryMap.put("Германия", "DE");
        countryMap.put("Великобритания", "GB");
    }

    public static boolean isValidNumber(String number, String country){
        String regionCode = countryMap.get(country);
        return true;
        /*if (regionCode != null) {
            try {
                // Парсинг номера телефона
                PhoneNumber phoneNumber = phoneUtil.parse(number, regionCode);

                // Проверка валидности номера
                boolean isValid = phoneUtil.isValidNumber(phoneNumber);

                if (isValid) {
                   logger_.info("Номер валидный: " + number);
                } else {
                    logger_.info("Номер невалидный: " + number);

                }
                return isValid;
            } catch (NumberParseException e) {
                logger_.log(Level.SEVERE, "Ошибка парсинга номера: " + e.toString());
            }

        } else {
            throw new NullPointerException("Регион не поддерживается");

        }*/
        //return false;
    }
    public static boolean isEmailValid(String email){
        return true;
        /*try {
            new InternetAddress(email).validate();
            return true;
        } catch (Exception ex) {
            return false;
        }*/
    }
}
