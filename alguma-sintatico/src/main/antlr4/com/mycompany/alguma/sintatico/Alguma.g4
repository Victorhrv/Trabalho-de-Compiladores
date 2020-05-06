grammar Alguma;

TIPO_VAR 
	:	'INTEIRO' | 'REAL';

NUMINT
	:	('0'..'9')+
	;

NUMREAL
	:	('0'..'9')+ ('.' ('0'..'9')+)?
	;

OP_BOOL	
	:	'E' | 'OU'
	;
		
VARIAVEL
	:	('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9')*
	;

CADEIA
	:	'\'' ( ESC_SEQ | ~('\''|'\\') )* '\''
	;
	
OP_ARIT1
	:	'+' | '-'
	;

OP_ARIT2
	:	'*' | '/'
	;

OP_REL
	:	'>' | '>=' | '<' | '<=' | '<>' | '='
	;

fragment
ESC_SEQ
	:	'\\\'';

COMENTARIO
	:	'%' ~('\n'|'\r')* '\r'? '\n' {skip();}
	;

WS 	:	( ' ' |'\t' | '\r' | '\n') {skip();}
	;
	
programa
	:     { System.out.println("Começou um programa"); }
		':' 'DECLARACOES' 
		{ System.out.println("  Declarações"); }
		listaDeclaracoes ':' 'ALGORITMO' 
		{ System.out.println("  Algoritmo"); }
		listaComandos EOF
	;
	
listaDeclaracoes
	:	declaracao+
	;
	
declaracao
	:	VARIAVEL ':' TIPO_VAR
                { System.out.println("    Declaração: Var="+$VARIAVEL.text+", Tipo="+$TIPO_VAR.text); }
	;
	
expressaoAritmetica
	:	expressaoAritmetica OP_ARIT1 termoAritmetico
	|	termoAritmetico
	;
	
termoAritmetico
	:	termoAritmetico OP_ARIT2 fatorAritmetico
	|	fatorAritmetico
	;
	
fatorAritmetico
	:	NUMINT
	|	NUMREAL
	|	VARIAVEL
	|	'(' expressaoAritmetica ')'
	;
	
expressaoRelacional
	:	expressaoRelacional OP_BOOL termoRelacional
	|	termoRelacional
	;
	
termoRelacional
	:	expressaoAritmetica OP_REL expressaoAritmetica
	|	'(' expressaoRelacional ')'
	;
	

listaComandos 
	:	(comando
                
                )+
	;
	
comando returns [ String tipoComando ]
	:	comandoAtribuicao { $tipoComando = "Atribuicao"; }
	|	comandoEntrada { $tipoComando = "Entrada"; }
	|	comandoSaida { $tipoComando = "Saida"; }
	|	comandoCondicao { $tipoComando = "Condicao"; }
	|	comandoRepeticao { $tipoComando = "Repeticao"; }
	|	subAlgoritmo{ $tipoComando = "Subalgoritmo"; }
	;
	
comandoAtribuicao
	:	'ATRIBUIR' expressaoAritmetica 'A' VARIAVEL
                
	;
	
comandoEntrada
	:	'LER' VARIAVEL
                
	;
comandoSaida
	:	'IMPRIMIR' (VARIAVEL | CADEIA)
                
	;
	
comandoCondicao
	:	'SE' expressaoRelacional 'ENTAO' comando
	|	'SE' expressaoRelacional 'ENTAO' comando 'SENAO' comando
	;
	
comandoRepeticao
	:	'ENQUANTO' expressaoRelacional comando
	;
subAlgoritmo
	: 'INICIO' listaComandos 'FIM'
	;
