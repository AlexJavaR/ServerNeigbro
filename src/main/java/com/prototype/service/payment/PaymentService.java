package com.prototype.service.payment;

import com.prototype.model.event.ApartmentEvent;
import com.prototype.model.event.payment.BillEvent;
import com.prototype.model.event.payment.HousemateCashPaymentEvent;
import com.prototype.model.event.payment.HousematePayPalPaymentEvent;
import com.prototype.model.event.payment.ManagerPaymentEvent;
import com.prototype.to.ApartmentsWithDebt;
import com.prototype.to.SingleManagerPayment;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface PaymentService {
    ManagerPaymentEvent addSinglePayment(BigInteger userId, SingleManagerPayment singleManagerPayment);

    List<ApartmentEvent> findAllBillsOfApartment(BigInteger userId, BigInteger addressId);

    List<ApartmentEvent> findAllBillsOfApartmentByManager(BigInteger managerId, BigInteger addressId, String apartment);

    Integer getAmountDebtOfApartment(BigInteger userId, BigInteger addressId);

    Integer getAmountDebtOfApartmentByManager(BigInteger managerId, BigInteger addressId, String apartment);

    BillEvent payPalSingleBillOfApartment(BigInteger userId, BigInteger billId, Integer partAmount);

    HousematePayPalPaymentEvent payPalAllBillsOfApartment(BigInteger userId, BigInteger addressId, String apartment);

    BillEvent payCashSingleBillOfApartment(BigInteger managerId, BigInteger addressId, String apartment, BigInteger billId, Integer partAmount);

    HousemateCashPaymentEvent payCashAllBillsOfApartment(BigInteger managerId, BigInteger addressId, String apartment);

    List<ManagerPaymentEvent> findAllManagerPaymentEvent(BigInteger managerId, BigInteger addressId);

    void createMonthlyBillForAllApartment();

    List<ApartmentsWithDebt> getDebtOfAllApartmentsByManager(BigInteger managerId, BigInteger addressId);
}
