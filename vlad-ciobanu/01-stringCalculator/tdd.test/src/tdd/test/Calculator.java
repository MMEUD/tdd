package tdd.test;

public class Calculator {
	
	private static final String DELIMITERS_LINE_START = "//";
	private static final String DELIMITER_END = "]";
	private static final String DELIMITER_START = "[";
	private static final String DEFAULT_DELIMTER_PATTERN = ",|\n";
	
	public int Add(String numbers) throws NegativeNumbersNotAllowedException
	{
		if(numbers.isEmpty())
			return 0;
		
		String[] individualNumbers = splitNumbers(numbers);
		int sum = 0;
		for(String stringNumber : individualNumbers) {
			int number = Integer.parseInt(stringNumber);
			if(number < 0)
				throw new NegativeNumbersNotAllowedException(individualNumbers);
			if(number < 1000)
				sum += number;
		}
		return sum;
	}

	private String[] splitNumbers(String numbers) {
		if(numbers.startsWith(DELIMITERS_LINE_START))
			return splitNumbersWithCustomDelimiter(numbers);
		
		return numbers.split(DEFAULT_DELIMTER_PATTERN);
	}

	private String[] splitNumbersWithCustomDelimiter(String numbers) {
		String delimiter = numbers.substring(2, numbers.indexOf("\n"));
		String regex = parseDelimiters(delimiter);
		return numbers.substring(numbers.indexOf("\n") + 1).split(regex);
	}

	private String parseDelimiters(String delimiter) {
		String regex = "";
		
		while(!delimiter.isEmpty()) {
			String currentDelimiter = delimiter.substring(delimiter.indexOf(DELIMITER_START) + 1, delimiter.indexOf(DELIMITER_END));
			regex += currentDelimiter + "|";
			delimiter = delimiter.substring(delimiter.indexOf(DELIMITER_END) + 1);
		}
		
		return regex.substring(0, regex.length() - 1);
	}

}
