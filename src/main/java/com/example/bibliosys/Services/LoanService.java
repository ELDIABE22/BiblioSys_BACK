package com.example.bibliosys.Services;

import java.util.List;

import com.example.bibliosys.Models.request.loan.LoanRequest;
import com.example.bibliosys.Models.response.ApiResponse;
import com.example.bibliosys.Models.response.loan.LoanResponse;
import com.example.bibliosys.Models.response.loan.loanFetchResponse;
import com.example.bibliosys.Models.response.loan.loanOverdueResponse;

public interface LoanService {
    List<loanFetchResponse> fetchAllLoansService();

    List<loanOverdueResponse> fetchLoansOverdueService();

    ApiResponse<LoanResponse> newLoanService(LoanRequest loanRequest);

    ApiResponse<Void> sendMailLoanOverdueService(String correo, String message);

    ApiResponse<LoanResponse> updateLoanService(LoanRequest loanRequest);

    ApiResponse<Void> deleteLoanService(Integer loanId);
}
