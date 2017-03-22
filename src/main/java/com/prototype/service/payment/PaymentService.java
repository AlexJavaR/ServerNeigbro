package com.prototype.service.payment;

import com.prototype.model.Address;
import com.prototype.model.BankAccount;
import com.prototype.model.event.payment.*;
import com.prototype.to.ApartmentsWithDebt;
import com.prototype.to.SingleManagerPayment;

import java.math.BigInteger;
import java.util.List;

public interface PaymentService {
    ManagerPaymentEvent addSinglePayment(BigInteger userId, SingleManagerPayment singleManagerPayment);

    List<BillEvent> findAllBillsOfApartment(BigInteger userId, BigInteger addressId, String apartment);

    List<BillEvent> findAllBillsOfApartmentByManager(BigInteger managerId, BigInteger addressId, String apartment);

    Integer getAmountDebtOfApartment(BigInteger userId, BigInteger addressId, String apartment);

    Integer getAmountDebtOfApartmentByManager(BigInteger managerId, BigInteger addressId, String apartment);

    BillEvent payPalSingleBillOfApartment(BigInteger userId, BigInteger billId, Integer partAmount);

    HousematePayPalPaymentEvent payPalAllBillsOfApartment(BigInteger userId, BigInteger addressId, String apartment);

    BillEvent payCashSingleBillOfApartment(BigInteger managerId, BigInteger addressId, String apartment, BigInteger billId, Integer partAmount);

    HousemateCashPaymentEvent payCashAllBillsOfApartment(BigInteger managerId, BigInteger addressId, String apartment);

    List<ManagerPaymentEvent> findAllManagerPaymentEvent(BigInteger managerId, BigInteger addressId);

    void createMonthlyBillForAllApartment();

    List<ApartmentsWithDebt> getDebtOfAllApartmentsByManager(BigInteger managerId, BigInteger addressId);

    MonthlyBillEvent createMonthlyBillForAddress(BigInteger addressId, BigInteger managerId);

    BillEvent setStatusBillInProcess(BigInteger userId, BigInteger billId, Integer blockedAmount);

    BillEvent setStatusBillWithoutInProcess(BigInteger userId, BigInteger billId, Integer blockedAmount);

    Boolean getStatusBill(BigInteger userId, BigInteger billId);

    BillEvent changeStatusBill(BigInteger billId);

    Address receiveMoneyFromAccountByManager(BigInteger managerId, BigInteger addressId, Integer amount, BankAccount bankAccount);

    Address confirmWithdrawalMoney(BigInteger addressId, Integer amount);
}
