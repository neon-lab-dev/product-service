package com.neonlab.product.apis;
import com.neonlab.common.dto.AddressDto;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.services.AddressService;
import com.neonlab.common.utilities.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;



@Service
public class AddAddressApi {

    @Autowired
    private AddressService addressService;
    @Autowired
    private ValidationUtils validationUtils;

    public ApiOutput<?> save(AddressDto addressDto)  {
        try {
            validationUtils.validate(addressDto);
            addressService.addressExists(addressDto);
            var address = addressService.addAddress(addressDto);
            return new ApiOutput<>(HttpStatus.OK.value(), "Address Save Successfully", address);
        }catch (InvalidInputException | ServerException e){
            return new ApiOutput<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}