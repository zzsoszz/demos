package com.infor.client.axiom;

import org.apache.axiom.om.*;

public class GetReservationStay_001 {

    public static OMElement fromStandard(){
        OMFactory factory = OMAbstractFactory.getSOAP11Factory();
        OMNamespace baseNamespace = factory.createOMNamespace("http://schema.softbrands.com/HMS/base/1","");
        OMElement getReservationStay_001 = factory.createOMElement("GetReservationStay_001", baseNamespace);
        OMAttribute attribute = factory.createOMAttribute("version",null,"1.0 HMS_RELEASE");
        getReservationStay_001.addChild(Get.fromStandard(baseNamespace));
        getReservationStay_001.addChild(ReservationStayID.fromStandard(baseNamespace));
        getReservationStay_001.addAttribute(attribute);
        return getReservationStay_001;
    }

}
