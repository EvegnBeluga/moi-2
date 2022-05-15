
import java.util.Scanner;
import java.util.Map;
import java.util.TreeMap;

public class Main {


    public static String[] calc(String input) {

        try {
            String[] input_value = input.split(" ");
            if (input_value.length != 3) throw new Exception("Что-то пошло не так, попробуйте еще раз");
            Number firstNumber = NumberService.parseAndValidate(input_value[0]);
            Number secondNumber = NumberService.parseAndValidate(input_value[2], firstNumber.type());
            String result = ActionService.calc(firstNumber, secondNumber, input_value[1]);
            System.out.println("Output: \n" + result);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            exitCalc();

        }

        return new String[0];
    }



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        startCalc();

        while (true) {
            System.out.println("Input: ");
            String input = scanner.nextLine();
            String[] calc_input = calc(input);
          //  input = calc_input[Integer.parseInt(input)];
            
                if (input.equals("exit")) {
                    exitCalc();
                    break;
                }
            }

            scanner.close();

        }




    private static void startCalc() {
        System.out.println("Добро пожаловать в Калькулятор запущен");
        System.out.println("Если Вы хотите покинуть программу, введите 'exit'");
    }

    private static void exitCalc() {

        System.out.println("До скорых встреч!");

    }

    }
 class ActionService {

    public static String calc(Number first, Number second, String action) throws Exception {

        int result = switch (action) {
            case "+" -> first.value() + second.value();
            case "-" -> first.value() - second.value();
            case "*" -> first.value() * second.value();
            case "/" -> first.value() / second.value();
            default -> throw new Exception("Не правильно введен символ операции, используйте только +, -, *, /");
        };

        if (first.type() == NumberType.ROMAN) {
            return NumberService.toRomanNumber(result);
        } else return String.valueOf(result);
    }
}




class NumberService {

    private final static TreeMap < Integer, String > romanString = new TreeMap<>();

    static {
        romanString.put(1, "I");
        romanString.put(4, "IV");
        romanString.put(5, "V");
        romanString.put(9, "IX");
        romanString.put(10, "X");
        romanString.put(40, "XL");
        romanString.put(50, "L");
        romanString.put(90, "XC");
        romanString.put(100, "C");
    }

    static Number parseAndValidate(String symbol) throws Exception {

        int value;
        NumberType type;

        try {
            value = Integer.parseInt(symbol);
            type = NumberType.ARABIC;
        }catch (NumberFormatException e) {
            value = toArabicNumber(symbol);
            type = NumberType.ROMAN;
        }

        if (value < 1 || value > 10) {
            throw new Exception("Неподходящее значение числа(ел), используйте числа от 1 до 10 включительно");
        }

        return new Number(value, type);
    }

    static Number parseAndValidate(String action, NumberType type) throws Exception {

        Number number = parseAndValidate(action);
        if (number.type() != type) {
            throw new Exception("Числа разных типов, используйте один тип вводных значений");
        }

        return number;
    }

    private static int letterToNumber(char letter) {

        int result = -1;

        for (Map.Entry < Integer, String > entry: romanString.entrySet()) {
            if (entry.getValue().equals(String.valueOf(letter))) result = entry.getKey();
        }
        return result;
    }

    static String toRomanNumber(int number) {

        int i = romanString.floorKey(number);

        if (number == i) {
            return romanString.get(number);
        }
        return romanString.get(i) + toRomanNumber(number - i);
    }

    static int toArabicNumber(String roman) throws Exception {
        int result = 0;

        int i = 0;
        while (i < roman.length()) {
            char letter = roman.charAt(i);
            int num = letterToNumber(letter);

            if (num < 0) throw new Exception("Неверный римский символ");

            i++;
            if (i == roman.length()) {
                result += num;
            }else {
                int nextNum = letterToNumber(roman.charAt(i));
                if(nextNum > num) {
                    result += (nextNum - num);
                    i++;
                }
                else result += num;
            }
        }
        return result;
    }
}

record Number(int value, NumberType type) {

}
