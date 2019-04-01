package com.infor.client.axiom;

import org.apache.axiom.om.*;

public class ReservationStayID {

    public static OMElement fromStandard(OMNamespace baseNamespace){
        OMFactory factory = OMAbstractFactory.getSOAP11Factory();
        OMElement reservationStayID = factory.createOMElement("ReservationStayID",baseNamespace);
        OMElement pmsConfirmationNumber = factory.createOMElement("PmsConfirmationNumber",baseNamespace);
        OMText text = factory.createOMText("25381186-1");
        pmsConfirmationNumber.addChild(text);
        reservationStayID.addChild(pmsConfirmationNumber);
        return reservationStayID;
    }

}
