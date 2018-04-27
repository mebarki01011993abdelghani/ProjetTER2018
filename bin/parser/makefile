
final: Yylex.java parser.java

Yylex.java: vparser.lex
	cd ../../lib; java JLex.Main ../src/parser/vparser.lex; cd ../src/parser/
	mv vparser.lex.java Yylex.java


parser.java: vparser.cup
	java -jar ../../lib/java-cup-11a.jar vparser.cup


clean: 
	rm Yylex.java parser.java
