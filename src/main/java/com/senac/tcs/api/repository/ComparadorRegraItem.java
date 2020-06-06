package com.senac.tcs.api.repository;

import java.util.Comparator;

import com.senac.tcs.api.domain.RegraItem;

/**
 *
 * @author Christian
 * Classe resposável pela ordenação (Comparação) dos Itens (condições) de uma regra.
 */
public class ComparadorRegraItem implements Comparator<RegraItem> {

    public int compare(RegraItem o1, RegraItem o2) {
        if (o1.getConectivo() < o2.getConectivo()) {
            return -1;
        } else if (o1.getConectivo() > o2.getConectivo()) {
            return +1;
        } else {
            return 0;
        }
    }

}
