grammar RS;

import RSTOKENS;

@header {
    package rs.vm;
}

command
  : (rotateArea | doLoop | doDraw | shape)*
  ;

doLoop
  : DO (loopDef | loopNDef)? '(' stepToShapeBody* ')'
  ;

loopDef
  : '(' (expr | inlineRnd) ( TIMES | TIME ) ')'
  ;

loopNDef
  : '(' VAR FROM expr TO expr (STEP expr)? ')'
  ;

doDraw
  : DRAW '(' (drawColor) (',' expr)? ')' '(' stepToShapeBody* ')'
  ;

drawColor
  : color
  | VAR
  | inlineRnd
  ;

rotateArea
  : ROTATE AREA '(' (expr | inlineRnd) ')' '(' stepToShapeBody* ')' {$stepToShapeBody.text.contains(VOCABULARY.getDisplayName(RSParser.AREA).replace("'", "")) == false}?
  ;

inlineRnd
  : RANDOM '('rndDefNoVar')'
  ;
  
rnd
  : WITH RANDOMS '(' (rndDefVar | rndDefCoord) (',' (rndDefVar | rndDefCoord))* ')' '(' stepToShapeBody* ')'
  ;

stepToShapeBody
  : doLoop | doDraw | rnd | shape
  ;

rndDefNoVar
  : IN (ofRange | ofSet | doUnion)
  ;

rndDefVar
  : VAR IN (ofRange | ofSet)
  ;

rndDefCoord
  : coordDef IN doUnion
  ;

coordDef
  : '(' VAR ',' VAR ')'
  ;

coord
  : '(' expr ',' expr ')'
  ;

doUnion 
  : UNION '(' shape+ ')' {$shape.text.contains(VOCABULARY.getDisplayName(RSParser.ROTATE).replace("'", "")) == false}?
  ;
  
shape
  : circle
  | oval
  | lines
  | curves
  ;

circle
  : CIRCLES '(' ( '(' shapeCoord (','? RADIUS? shapeSize) ')' )+ ')'
  ;
  
oval
  : OVALS '(' ( '(' shapeCoord (','? SIZE? shapeSize2) (','? sector)? (','? rotateShape)? ')' )+ ')'
  ;

lines
  : LINES '(' (shapeCoord)+ (','? rotateShape)? ')'
  ;

curves
  : CURVES '(' curve+ ')'
  ;

shapeCoord
  : coord | inlineRnd
  ;

shapeSize
  : expr | inlineRnd
  ;

rotateShape
  : ROTATE shapeSize
  ;

sector
  : SECTOR? FROM shapeSize SIZE shapeSize
  ;

curve
  : '(' FROM shapeCoord TO shapeCoord TOWARDS shapeCoord (rotateShape)? ')'
  ;

shapeSize2
  : shapeSize XX shapeSize
  ;

ofSet
  : SET '(' ((color (',' color)*) | (expr (',' expr)*)) ')'
  ;

color
  : (RED | GREEN | BLUE | BLACK | PINK | WHITE | CYAN | GRAY | ORANGE | YELLOW
        | MAGENTA | OLIVE | LIME | NIGHT | SILVER | CLARET | GOLD | VIOLET
        | BRIGHT_PINK | PINK_VIOLET | DARK_BROWN | LIGHT_BROWN | LIGHT_BLUE | DARK_RED
        | CREAM | CORNFLOWER | TARDIS)
  ;

ofRange
  : RANGE '(' expr '..' expr ')'
  ;

expr: expr op=('*'|'/') expr # MulDiv
    | expr op=('+'|'-') expr # AddSub
    | INT                    # Int
    | VAR                    # Var
    | '('expr')'             # Parens
    ;

INT: [0-9]+ ;
MUL: '*' ;
DIV: '/' ;
ADD: '+' ;
SUB: '-' ;
WS : [ \t\r\n]+ -> skip ;
LINE_COMMENT
   : ';' ~[\r\n]* -> skip
   ;