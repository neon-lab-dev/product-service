package com.neonlab.product.apis;
import com.neonlab.common.dto.EmailSendDto;
import com.neonlab.common.services.EmailService;
import com.neonlab.common.utilities.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class SendEmailApi {
    @Autowired
    EmailService emailService;
    @Autowired
    ValidationUtils validationUtils;

    public String send(EmailSendDto emailSendDto) {
        validationUtils.validate(emailSendDto);
        return emailService.sendEmail(emailSendDto);
    }
}
