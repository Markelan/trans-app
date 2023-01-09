package marik.project;

import lombok.AllArgsConstructor;
import marik.project.service.Printer;
import marik.project.service.FileLoader;
import marik.project.service.option.OptionService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
@AllArgsConstructor
public class ProjectApplication implements CommandLineRunner {  //C:\Users\Marik\Desktop\test.xlsx
																//C:\Users\Marik\Desktop\result.xlsx
	private final FileLoader fileLoader;
	private final Printer consolePrinter;
	private final Scanner scanner = new Scanner(System.in);
	private final List<OptionService> optionServiceList;

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}

	@Override
	public void run(String... args) {
		while (true) {
			Workbook excelWorkbook = getExcelFile();
			Integer optionNumber = getOptionNumber();
			String filePathForRecord = getChooseExcelOrConsole();
			executeOption(optionNumber, filePathForRecord, excelWorkbook);

			consolePrinter.writeOptionSuccessfully();
		}
	}

	private void executeOption(Integer optionNumber, String  filePathForRecord, Workbook excelWorkbook) {

		optionServiceList.forEach(optionService -> {
			if (optionService.getOptionNumber().equals(optionNumber)) {
				optionService.execute(filePathForRecord, excelWorkbook);
				return;
			}
		});
	}

	private Integer getOptionNumber(){
		Integer optionNumber = null;
		while (optionNumber == null){
			consolePrinter.writeOptions();
			optionNumber = Integer.valueOf(scanner.nextLine());
		}
		return optionNumber;
	}

	private Workbook getExcelFile(){
		Workbook excelWorkbook = null;
		while (excelWorkbook == null){
			consolePrinter.writeSpecifyFilePath();
			String filePath = scanner.nextLine();
			excelWorkbook = fileLoader.loadFile(filePath);
		}
		return excelWorkbook;
	}

	private String getChooseExcelOrConsole(){
		consolePrinter.writeChooseExcelOrConsole();
		return scanner.nextLine();
	}
}
