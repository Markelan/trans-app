package marik.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionPeriodicity {

    private String trType;

    private Long trAmount;

    private Double trPeriodicity;
}
