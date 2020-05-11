package com.senac.tcs.api.domain;

public enum TipoConectivo {
	_IF_(0),_AND_(1), _OR_(2);
	
    private final int valor;
    
    TipoConectivo(int opcao){
        valor = opcao;
    }
    
    public int getOpcao(){
        return valor;
    }
	
}
