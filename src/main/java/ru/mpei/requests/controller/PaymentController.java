package ru.mpei.requests.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentController {
    @GetMapping("payment") //Getting the payment page (will be improved)
    public String getPaymentPage() {
        return "payments";
    }
}
