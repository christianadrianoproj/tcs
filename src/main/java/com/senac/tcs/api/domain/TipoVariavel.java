package com.senac.tcs.api.domain;

/**
*
* @author Christian
*/

public enum TipoVariavel {
	Numerica(0), Univalorada(1),Multivalorada_(2);
	
    private final int tipo;
    
    TipoVariavel(int _tipo){
    	tipo = _tipo;
    }
    
    public int getTipo(){
        return tipo;
    }
	
}
