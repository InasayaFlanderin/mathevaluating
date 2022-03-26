package Matheval;

import Matheval.Config;
import Matheval.Token;
import Matheval.Lexer;
import Matheval.Parser;

import java.util.Scanner;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Mathevali {
	private static void execute(String inputs) {
		String source = inputs.replace(" ", "").replace("π", "pi").replace("∞", "infinity").replace("√", "sqrt").replace("squareroot", "sqrt");
		Lexer lexer = new Lexer(source);
		List<Token> tokens = lexer.scan();

		if(tokens.isEmpty()) {
			return;
		}

		Parser parser = new Parser(tokens);
		Node node = parser.parse();
		System.out.println(node.toString());
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Please enter your input!");

		for(;;) {
			System.out.print("Your input > ");
			String input = scanner.nextLine();

			if(input.equals("version") || input.equals("versions")) {
				System.out.println(Config.getVer());
			} else if(input.equals("exit") || input.equals("exits")) {
				System.exit(0);
			} else {
				execute(input);
			}
		}
	}
}