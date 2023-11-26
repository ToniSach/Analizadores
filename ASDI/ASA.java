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
