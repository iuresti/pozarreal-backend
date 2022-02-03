package org.uresti.pozarreal.tools;

import org.uresti.pozarreal.config.FeeConfig;
import org.uresti.pozarreal.config.PozarrealConfig;
import org.uresti.pozarreal.dto.House;
import org.uresti.pozarreal.dto.PaymentByConcept;
import org.uresti.pozarreal.model.Payment;

import java.util.ArrayList;
import java.util.List;

import static org.uresti.pozarreal.model.PaymentSubConcept.*;

public class PaymentTools {
    public static void setYearPayments(House house, List<Payment> payments) {
        String[] twoMonthsPaymentIds = {
                MAINTENANCE_BIM_1,
                MAINTENANCE_BIM_2,
                MAINTENANCE_BIM_3,
                MAINTENANCE_BIM_4,
                MAINTENANCE_BIM_5,
                MAINTENANCE_BIM_6
        };

        ArrayList<PaymentByConcept> paymentInfo = new ArrayList<>();

        paymentInfo.add(new PaymentByConcept());
        paymentInfo.add(new PaymentByConcept());
        paymentInfo.add(new PaymentByConcept());
        paymentInfo.add(new PaymentByConcept());
        paymentInfo.add(new PaymentByConcept());
        paymentInfo.add(new PaymentByConcept());

        house.setTwoMonthsPayments(paymentInfo);

        PozarrealConfig pozarrealConfig = new PozarrealConfig();

        FeeConfig feeConfig = new FeeConfig();

        feeConfig.setYearlyMaintenanceFee(4560.0);
        feeConfig.setBiMonthlyMaintenanceFee(800.0);
        feeConfig.setParkingPenFee(1500.0);

        pozarrealConfig.setFees(feeConfig);

        for (Payment payment : payments) {
            if (MAINTENANCE_ANNUITY.equals(payment.getPaymentSubConceptId())) {
                for (PaymentByConcept paymentByConcept : paymentInfo) {
                    paymentByConcept.setId(payment.getId());
                    paymentByConcept.setComplete(true);
                    paymentByConcept.setAmount(payment.getAmount());
                    paymentByConcept.setValidated(payment.isValidated());
                    paymentByConcept.setConflict(payment.isConflict());
                }
                break;
            } else {
                for (int i = 0; i < twoMonthsPaymentIds.length; i++) {
                    if (twoMonthsPaymentIds[i].equals(payment.getPaymentSubConceptId())) {
                        paymentInfo.get(i).setId(payment.getId());
                        paymentInfo.get(i).setAmount(paymentInfo.get(i).getAmount() + payment.getAmount());
                        paymentInfo.get(i).setComplete(paymentInfo.get(i).getAmount() >= feeConfig.getBiMonthlyMaintenanceFee());
                        paymentInfo.get(i).setValidated(payment.isValidated());
                        paymentInfo.get(i).setConflict(payment.isConflict());
                        break;
                    }
                }
            }
        }
    }

}
