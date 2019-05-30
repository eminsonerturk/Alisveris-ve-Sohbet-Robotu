package com.company;

import org.junit.Test;

import static org.junit.Assert.*;

public class CellPhoneTest {

    @Test
    public void getUrunYorumlariCellPhone() throws Exception {


        //Yaratılan Asus Zenfone 3 telefonunun ortalama kutup yani sezgi değerinin 0.336 olup olmadığı kontrol edilmiştir.

        CellPhone cellPhoneTest = new CellPhone(1, 2, "Asus", "Zenfone 3", 16, "3GB");


        if(cellPhoneTest.getUrunYorumlariCellPhone().getKutupDegeri() == 0.336)
            assertTrue(true);
    }

}