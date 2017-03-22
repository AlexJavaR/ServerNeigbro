package com.prototype.web.payment;

import com.prototype.model.Address;
import com.prototype.model.BankAccount;
import com.prototype.security.AuthorizedUser;
import com.prototype.service.address.AddressService;
import com.prototype.service.payment.PaymentService;
import com.prototype.service.user.UserService;
import com.prototype.to.InfoForWithdrawalMoney;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping(value = ManagerWithdrawalMoneyRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ManagerWithdrawalMoneyRestController {
    static final String REST_URL = "/api/v1/withdrawal";

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Address> receiveMoneyFromAccountByManager(@RequestBody InfoForWithdrawalMoney infoForWithdrawalMoney) {
        BigInteger managerId = AuthorizedUser.id();
        BigInteger addressId = infoForWithdrawalMoney.getAddressId();
        Integer amount = infoForWithdrawalMoney.getAmount();
        BankAccount bankAccount = infoForWithdrawalMoney.getBankAccount();
        if (addressId == null || amount == null || bankAccount == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (addressService.findOne(addressId) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (addressService.findOne(addressId).getAmountForWithdrawal() != null && addressService.findOne(addressId).getAmountForWithdrawal() > 0) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        Address address = paymentService.receiveMoneyFromAccountByManager(managerId, addressId, amount, bankAccount);
        if (address == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<BankAccount> getBankAccountByManager() {
        BigInteger managerId = AuthorizedUser.id();
        BankAccount bankAccount = userService.getBankAccount(managerId);
        if (bankAccount == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bankAccount, HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> confirmWithdrawalMoney(@RequestBody InfoForWithdrawalMoney infoForWithdrawalMoney) {
        BigInteger addressId = infoForWithdrawalMoney.getAddressId();
        if (addressId == null || addressService.findOne(addressId) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Address address = paymentService.confirmWithdrawalMoney(addressId, infoForWithdrawalMoney.getAmount());

        if (address == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
