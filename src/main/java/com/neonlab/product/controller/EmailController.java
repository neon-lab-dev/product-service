package com.neonlab.product.controller;
import com.neonlab.common.dto.EmailSendDto;
import com.neonlab.product.apis.SendEmailApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/email")
public class EmailController {
    @Autowired
    private SendEmailApi sendEmailApi;

    @PostMapping("/send")

    public String sendEmail(@RequestBody EmailSendDto emailSendDto){
        return sendEmailApi.send(emailSendDto);
    }
}
