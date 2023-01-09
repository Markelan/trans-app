package marik.project.service;

import marik.project.OptionNum;
import org.springframework.stereotype.Service;

@Service
public class Printer {

    public void writeSpecifyFilePath(){
        System.out.println("Введите путь к файлу");
    }
    public void writeOptions() {
        String s = "Выберите опцию и введите её номер: ";
        for (OptionNum option : OptionNum.values()) {
            s = s + "\n" + option.getOptionNumber() + ". " +option.getOptionName();
        }
        System.out.println(s);
    }

    public void writeChooseExcelOrConsole() {
        System.out.println("Введите путь для записи файла");
    }

    public void writeOptionSuccessfully() {
        System.out.println("Опция успешно выполнена!");
    }
}
