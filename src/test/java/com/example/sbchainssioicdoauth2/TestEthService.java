/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.sbchainssioicdoauth2;

import com.example.sbchainssioicdoauth2.model.pojo.Case;
import com.example.sbchainssioicdoauth2.model.pojo.State;
import com.example.sbchainssioicdoauth2.service.EthereumService;
import com.example.sbchainssioicdoauth2.service.impl.EthereumServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author nikos
 */
@Slf4j
public class TestEthService {

    @Test
    public void testGetAllCases() {

        EthereumService ethServ = new EthereumServiceImpl();
        Assertions.assertEquals(ethServ.getAllCaseUUID().size() > 0, true);
//        ethServ.getAllCaseUUID().stream().forEach(uuid -> {
//            System.out.println(uuid);
//            System.out.println("the case stat is:");
//            System.out.println(ethServ.getCaseByUUID(uuid).get().getState().getValue());
//        });
        //fakeuuid
//        Assertions.assertEquals(ethServ.getCaseByUUID("fakeuuid").isPresent(), true);
    }

    @Test
    public void testAddCase() {
        EthereumService ethServ = new EthereumServiceImpl();
        Case theCase = new Case();
        theCase.setUuid("4YLVALU9V5FXMTJS");
        ethServ.addCase(theCase);

        Assertions.assertEquals(true, true);

    }

    @Test
    public void testUpdateCase() {
        EthereumService ethServ = new EthereumServiceImpl();
        Case theCase = new Case();

        theCase.setUuid("2WiYi8");
        theCase.setState(State.PAID);

        // this tests needs to be runned twice, once with calling the contract
        // and once after the transaction has been processed
//        ethServ.updateCase(theCase);
//        Assertions.assertEquals(ethServ.getCaseByUUID("2WiYi8").isPresent(), true);

    }

    @Test
    public void testGetCase() {
        EthereumService ethServ = new EthereumServiceImpl();
        final String uuid = "1SiYd2";

        Assertions.assertEquals(ethServ.getCaseByUUID(uuid).isPresent(), false);

    }

}
