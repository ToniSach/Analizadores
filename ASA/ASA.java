import java.util.List;

public class ASA implements Parser{

    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;


    public ASA(List<Token> tokens){
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
    }

    @Override
    public boolean parse() {
        Q();

        if(preanalisis.tipo == TipoToken.EOF && !hayErrores){
            System.out.println("Consulta correcta");
            return  true;
        }else {
            System.out.println("Se encontraron errores");
        }
        return false;
    }
   
    //Q -> select D from T
    private void Q(){
        if(preanalisis.tipo == TipoToken.SELECT){
            match(TipoToken.SELECT);
            D();
            match(TipoToken.FROM);
            T();
        }
        
    }
  // D -> distinct P | P
  private void D(){
    if(hayErrores)
        return;

    if(preanalisis.tipo == TipoToken.DISTINCT){
        match(TipoToken.DISTINCT);
        P();
    }
    else if (preanalisis.tipo == TipoToken.ASTERISCO
            || preanalisis.tipo == TipoToken.IDENTIFICADOR) {
        P();
    }
    else{
        hayErrores = true;
        System.out.println("Se esperaba 'distinct' or '*' or 'identificador'");
    }
}

    // P -> * | A
    private void P(){
        if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.ASTERISCO){
            match(TipoToken.ASTERISCO);
        }
        else if (preanalisis.tipo == TipoToken.IDENTIFICADOR) {
            A();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '*' or 'identificador'");
        }
    }

    //A -> A,A1|A1
    private void A(){
        if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
            A1();
            if(preanalisis.tipo == TipoToken.COMA){
                match(TipoToken.COMA);
                A();
            }
        }
    }

    //A1 -> idA2
    private void A1(){
        if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
            match(TipoToken.IDENTIFICADOR);
            A2();
        }
    }

    //A2 ->.id|epsilon
    private void A2(){
        if(preanalisis.tipo == TipoToken.PUNTO){
            match(TipoToken.PUNTO);
            match(TipoToken.IDENTIFICADOR);
        }
    }

    //T -> T,T1|T1
    private void T(){
        if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
            T1();
            if(preanalisis.tipo == TipoToken.COMA){
                match(TipoToken.COMA);
                T();
            }
        }

    } 

    //T1 -> idT2
    private void T1(){
        if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
            match(TipoToken.IDENTIFICADOR);
            T2();
        }
    }
    //T2 -> id|epsilon
    private void T2(){
        if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
            match(TipoToken.IDENTIFICADOR);
        }
    }

    private void match(TipoToken tt){
        if(preanalisis.tipo == tt){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            hayErrores = true;
            System.out.println("Error encontrado");
        }

    }

}
