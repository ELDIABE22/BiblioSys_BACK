package com.example.bibliosys.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bibliosys.Models.request.loan.LoanRequest;
import com.example.bibliosys.Models.response.ApiResponse;
import com.example.bibliosys.Models.response.loan.LoanResponse;
import com.example.bibliosys.Models.response.loan.loanFetchResponse;
import com.example.bibliosys.Models.response.loan.loanOverdueResponse;
import com.example.bibliosys.Services.impl.LoanServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
public class LoanController {
    @Autowired
    private LoanServiceImpl loanServiceImpl;

    @GetMapping("/loan")
    public ResponseEntity<List<loanFetchResponse>> getAllLoansController() {
        List<loanFetchResponse> loans = loanServiceImpl.fetchAllLoansService();
        return new ResponseEntity<>(loans, HttpStatus.OK);
    }

    @GetMapping("/loan/overdue")
    public ResponseEntity<List<loanOverdueResponse>> fetchLoansOverdueController() {
        List<loanOverdueResponse> loansOverdue = loanServiceImpl.fetchLoansOverdueService();
        return new ResponseEntity<>(loansOverdue, HttpStatus.OK);
    }

    @PostMapping("/loan/new")
    public ResponseEntity<ApiResponse<LoanResponse>> newLoanController(@RequestBody LoanRequest loanRequest) {
        ApiResponse<LoanResponse> apiResponse = loanServiceImpl.newLoanService(loanRequest);

        HttpStatus status = "Préstamo registrado.".equals(apiResponse.getMessage())
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(apiResponse, status);
    }

    @PostMapping("/loan/overdue")
    public ResponseEntity<ApiResponse<Void>> sendMailLoanOverdueController(@RequestBody Map<String, String> request) {
        String correo = request.get("correo");
        String message = request.get("message");

        ApiResponse<Void> apiResponse = loanServiceImpl.sendMailLoanOverdueService(correo, message);

        HttpStatus status = "Correo electrónico enviado.".equals(apiResponse.getMessage())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(apiResponse, status);
    }

    @PutMapping("/loan/update")
    public ResponseEntity<ApiResponse<LoanResponse>> updateLoanController(@RequestBody LoanRequest loanRequest) {
        ApiResponse<LoanResponse> apiResponse = loanServiceImpl.updateLoanService(loanRequest);

        HttpStatus status = "Préstamo actualizado.".equals(apiResponse.getMessage())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(apiResponse, status);
    }

    @DeleteMapping("/loan/{loanId}")
    public ResponseEntity<ApiResponse<Void>> deleteLoanController(@PathVariable Integer loanId) {
        ApiResponse<Void> apiResponse = loanServiceImpl.deleteLoanService(loanId);

        HttpStatus status = "Préstamo eliminado.".equals(apiResponse.getMessage())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(apiResponse, status);
    }
}
