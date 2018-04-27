import java.io.* ;
import java_cup.runtime.* ;
import structure.*;import components.*;
%%

%full
%cup
%ignorecase
%eofval{
  return new Symbol(sym.EOF) ;
%eofval}
%yylexthrow{
  LexicalException
%yylexthrow}
%line

SPACE = " "|\n|\t|\r
NUMBER = [0-9]
INT = "-" ? {NUMBER}+
SSTRING = (["a"-"z", "A"-"Z", "0"-"9", "."])*
ID = (["a"-"z", "A"-"Z"])(["a"-"z", "A"-"Z", "0"-"9"])*
VERSION_ID = (["0"-"9"])(["."]["0"-"9"])*
PUTID = ["."]STRING
%% 

{INT} {return new Symbol(sym.INT, new Integer(yytext()));}
{SSTRING} {return new Symbol(sym.SSTRING, new String(yytext()));}
{PUTID} {return new Symbol(sym.STRING, new String(yytext()));}
"//:" {return new Symbol(sym.DSLASHPP);}
"@" {return new Symbol(sym.AT);}
"{" {return new Symbol(sym.LCBRACK);}
"}" {return new Symbol(sym.RCBRACK);}
"(" {return new Symbol(sym.LBRACK);}
")" {return new Symbol(sym.RBRACK);}
"[" {return new Symbol(sym.LSBRACK);}
"]" {return new Symbol(sym.RSBRACK);}
":" {return new Symbol(sym.PP);}
"," {return new Symbol(sym.COL);}
";" {return new Symbol(sym.SEM);}
"=" {return new Symbol(sym.EQUALS);}
"//: version" {return new Symbol(sym.VERSION) ; } 
"//: property" {return new Symbol(sym.PROPERTY) ; } 
"//: root_module" {return new Symbol(sym.ROOTMODULE) ; }
"//: comment" {return new Symbol(sym.COMMENT) ; }
"module" {return new Symbol(sym.MODULE) ; } 
"//: endmodule" {return new Symbol(sym.ENDMODULE); }
"//: enddecls" {return new Symbol(sym.ENDDECLS); }
"wire" {return new Symbol(sym.WIRE); }
"/sn:" {return new Symbol(sym.SN);}
"/w:" {return new Symbol(sym.W);}
"/dp:" {return new Symbol(sym.DP);}
"/r:" {return new Symbol(sym.R);}
"/type:" {return new Symbol(sym.TYPE);}
"/st:" {return new Symbol(sym.ST);}
"/dr:" {return new Symbol(sym.DR);}
"/dolink:" {return new Symbol(sym.DOLINK);}
"/link:" {return new Symbol(sym.LINK);}
"/end" {return new Symbol(sym.END);}
"useExtBars" {return new Symbol(sym.USEEXTBARS);}
"discardChanges" {return new Symbol(sym.DISCARDCHANGES);}
"//: frame" {return new Symbol(sym.FRAME);}
"/wi:" {return new Symbol(sym.WIDTH);}
"/ht:" {return new Symbol(sym.HEIGHT);}
"/tx:" {return new Symbol(sym.TEXT);}
"//: line:" {return new Symbol(sym.LINE);} 
. {throw new LexicalException("token inconnu : "+yytext()+ " ligne "+yyline);}
