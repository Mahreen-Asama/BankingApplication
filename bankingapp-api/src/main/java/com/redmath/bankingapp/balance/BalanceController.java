package com.redmath.bankingapp.balance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/balances")
public class BalanceController {
    @Autowired
    private BalanceService balanceService;
    private final Logger logger= LoggerFactory.getLogger(getClass());

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/latest")
    public ResponseEntity<Map<String, List<Balance>>> getLatestBalances() {

        List<Balance> balances = balanceService.getLatestBalances();
        if (balances == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("content", balances));
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<Map<String, Balance>> getLatestBalanceByAccountId(@RequestParam(name = "accountId") Long accountId, Authentication auth) {

        Balance balance = balanceService.getLatestBalanceByAccountId(accountId, auth);
        if (balance == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();              //return this in any case (its also best practice -> miss-guide the user as well)
        }
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("content", balance));
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @GetMapping("/balance-history")
    public ResponseEntity<Map<String, List<Balance>>> getBalanceHistoryByAccountId(@RequestParam(name = "accountId") Long accountId, Authentication auth){

        List<Balance> balances = balanceService.getBalanceHistoryByAccountId(accountId, auth);
        if (balances == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("content", balances));
    }
}
