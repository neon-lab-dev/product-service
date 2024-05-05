package com.neonlab.product.controller;
import com.neonlab.common.dto.AddressDto;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.product.apis.AddAddressApi;
import com.neonlab.product.apis.DeleteAddressApi;
import com.neonlab.product.apis.UpdateAddressApi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddAddressApi addAddressApi;
    private final DeleteAddressApi deleteAddressApi;
    private final UpdateAddressApi updateAddressApi;


    @PostMapping("/create")
    public ApiOutput<?> save(@RequestBody AddressDto addressDto){
        return addAddressApi.save(addressDto);
    }

    @PutMapping("/update")
    public ApiOutput<?> update(@RequestBody AddressDto addressDto){
        return updateAddressApi.update(addressDto);
    }

    @DeleteMapping("/delete")
    public ApiOutput<?> delete(@RequestBody List<String> ids){
        return deleteAddressApi.delete(ids);
    }
}
