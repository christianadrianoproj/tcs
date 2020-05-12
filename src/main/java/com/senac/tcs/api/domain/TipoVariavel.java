package com.senac.tcs.api.domain;

/**
*
* @author Christian
*/

public enum TipoVariavel {
	Numerica(0), Univalorada(1),Multivalorada_(2);
	
    private final Integer tipo;
    
    TipoVariavel(Integer _tipo){
    	tipo = _tipo;
    }
    
    public Integer getTipo(){
        return tipo;
    }
	
}
