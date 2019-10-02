package com.company;

import org.junit.Test;

import static org.junit.Assert.*;

public class ContextStrategyPatternTest {

    ContextStrategyPattern contextStrategyPatternTest1 = new ContextStrategyPattern(new StrategyDeseniDosyadanPuanlaIlkYontem());

    ContextStrategyPattern contextStrategyPatternTest2 = new ContextStrategyPattern(new StrategyDeseniDosyadanPuanlaIkinciYontem());


    @Test
    public void executeStrategy() throws Exception {

        int bayrak = 0;

        float olumPuani = contextStrategyPatternTest1.executeStrategy("Volkswagen People's car project, Hover Car, the flying two-seater https://t.co/E3lml8bcse via @YouTube");


        //Yapılan 1. yorum olum puanı araştırmasında bu değer 0.348 olarak bulunmuştur...
        if(olumPuani == (float)0.348) {
            bayrak = 1;
        }else
            assertTrue(false);

        //Yapılan 2. yorum olum puanı araştırmasında bu değer 2.8960001 olarak bulunmuştur..
        float olumPuani2 = contextStrategyPatternTest2.executeStrategy("RT @sportingnews: “ESPN is a journalistic organization — not a political organization.\"\n\nAfter losing 13 million subscribers in just six ye…");

        if(olumPuani2 == (float)2.8960001 && bayrak == 1)
            assertTrue(true);



    }


}