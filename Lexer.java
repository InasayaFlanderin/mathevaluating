package Matheval;

import Matheval.Token;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Lexer {
	private String source;
	private int position;
	private char currentChar;
	private static final List<Token> empty = new ArrayList<>();
	private static final Map<String, Token> map;

	public Lexer(String input) {
		this.source = input;
		this.position = -1;
		this.currentChar = '\0';
	}

	public List<Token> scan() {
		List<Token> tok = scanf();
		List<Token> rtok = new ArrayList<>();

		if(tok.isEmpty()) {
			return tok;
		}

		for(int i = 0; i < tok.size() - 1; i++) {
			Token current = (Token) tok.get(i);
			Token next = (Token) tok.get(i + 1);

			rtok.add(current);

			if((current.getType().equals("Constant") && next.getType().equals("Constant")) ||
				(current.getType().equals("Variable") && next.getType().equals("Variable")) ||
				(current.getType().equals("Constant") && next.getType().equals("Variable")) ||
				(current.getType().equals("Variable") && next.getType().equals("Constant")) ||
				(current.getType().equals("Constant") && next.toString().equals("(")) ||
				(next.getType().equals("Constant") && current.toString().equals(")")) ||
				(current.getType().equals("Variable") && next.toString().equals("(")) ||
				(next.getType().equals("Variable") && current.toString().equals(")"))) {
				rtok.add(new Operator('*', 2));
			}
		}

		rtok.add(((Token) tok.get(tok.size() - 1)));
		List<Token> ftok = fixToken(rtok);

		return ftok;
	}

	private List<Token> fixToken(List<Token> tokens) {
		boolean hasEqual = false;

		List<Token> result = new ArrayList<>();

		for(int i = 0; i < tokens.size(); i++) {
			if(((Token) tokens.get(i)).toString().equals("=")) {
				hasEqual = true;
			}
		}

		if(hasEqual) {
			List<Token> lhs = new ArrayList<>();
			List<Token> rhs = new ArrayList<>();
			boolean ae = false;

			for(int i = 0; i < tokens.size(); i++) {
				if(((Token) tokens.get(i)).toString().equals("=")) {
					ae = true;
				} else if(!ae) {
					lhs.add((Token) tokens.get(i));
				} else {
					rhs.add((Token) tokens.get(i));
				}
			}

			List<Token> flhs = fix(lhs);
			List<Token> frhs = fix(rhs);

			for(int i = 0; i < flhs.size(); i++) {
				result.add((Token) flhs.get(i));
			}

			result.add(new Operator('=', 0));

			for(int i = 0; i < frhs.size(); i++) {
				result.add((Token) frhs.get(i));
			}
		} else {
			result = fix(tokens);
		}

		return result;
	}

	private List<Token> fix(List<Token> tokens) {
		int lparent = 0;
		int rparent = 0;

		for(int i = 0; i < tokens.size(); i++) {
			if(((Token) tokens.get(i)).toString().equals("(")) {
				lparent++;
			}

			if(((Token) tokens.get(i)).toString().equals(")")) {
				rparent++;
			}
		}

		if(lparent > rparent) {
			for(int i = 0; i < lparent - rparent; i++) {
				tokens.add(new Operator(')', 0));
			}
		}

		if(rparent > lparent) {
			List<Token> f = new ArrayList<>();

			for(int i = 0; i < rparent - lparent; i++) {
				f.add(new Operator('(', 0));
			}

			for(int i = 0; i < tokens.size(); i++) {
				f.add((Token) tokens.get(i));
			}

			return f;
		}

		return tokens;
	}

	private List<Token> scanf() {
		List<Token> tokens = new ArrayList<>();

		advanced();
		while(this.currentChar != '\0') {
			switch(this.currentChar) {
				case '(':
				case ')':
				case '=':
					tokens.add(new Operator(this.currentChar, 0));
					advanced();
					break;
				case '+':
				case '-':
					tokens.add(new Operator(this.currentChar, 1));
					advanced();
					break;
				case '*':
				case '/':
				case '%':
					tokens.add(new Operator(this.currentChar, 2));
					advanced();
					break;
				case '^':
					tokens.add(new Operator(this.currentChar, 3));
					advanced();
					break;
				default:
					if(this.currentChar >= '0' && this.currentChar <= '9') {
						String numbers = "";
						int dotn = 0;

						while(this.currentChar >= '0' && this.currentChar <= '9') {
							numbers += this.currentChar;
							advanced();

							if(this.currentChar == '.') {
								if(dotn == 1) {
									System.out.println("Error Inputs");
									return empty;
								}

								dotn++;
								numbers += this.currentChar;
								advanced();
							}
						}

						if(this.currentChar == Config.getImaginarySymbol()) {
							tokens.add(new Complex(0, Double.parseDouble(numbers)));
							advanced();
						} else {
							tokens.add(new Complex(Double.parseDouble(numbers), 0));
						}
					} else if((this.currentChar >= 'a' && this.currentChar <= 'z') || (this.currentChar >= 'A' && this.currentChar <= 'Z') || this.currentChar =='_') {
						int pos = this.position;

						while((this.currentChar >= 'a' && this.currentChar <= 'z') || (this.currentChar >= 'A' && this.currentChar <= 'Z') || this.currentChar =='_') {
							advanced();
						}

						String text = this.source.substring(pos, this.position);

						if(text.equals("e")) {
							tokens.add(new Complex(Math.E, 0));
						} else if(text.equals("i")) {
							tokens.add(new Complex(0, 1));
						} else if(text.equals("pi")) {
							tokens.add(new Complex(Math.PI, 0));
						} else if(text.equals("ei")) {
							tokens.add(new Complex(Math.E, 0));
							tokens.add(new Complex(0, 1));
						} else if(text.equals("ipi")) {
							tokens.add(new Complex(0,1));
							tokens.add(new Complex(Math.PI, 0));
						} else if(text.equals("epi")) {
							tokens.add(new Complex(Math.E, 0));
							tokens.add(new Complex(Math.PI, 0));
						} else {
							Token type = map.get(text);

							if(type == null) {
								for(int i = 0; i < text.length(); i++) {
									tokens.add(new Symbol(text.charAt(i)));
								}
							} else {
								tokens.add(type);
							}
						}
					} else {
						System.out.println("Unknown Character");
						return empty;
					}

					break;
			}
		}

		return tokens;
	}

	private void advanced() {
		this.position++;

		if(position < source.length()) {
			this.currentChar = source.charAt(position);
		} else {
			this.currentChar = '\0';
		}
	}

	static {
		map = new HashMap<>();
		map.put("cos", new Method("cos"));
		map.put("sin", new Method("sin"));
	}
}