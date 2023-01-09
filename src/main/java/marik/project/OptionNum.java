package marik.project;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OptionNum {

    FIRST_OPTION(1,"Подсчет количества и частоты встречаемости транзакций по их типу"),
    SECOND_OPTION(2,"Вывод 5 самых часто встречающихся транзакций в консоль");

    private final Integer optionNumber;
    private final String optionName;
}
