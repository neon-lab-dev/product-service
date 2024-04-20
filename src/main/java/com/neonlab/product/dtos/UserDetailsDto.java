
package com.neonlab.product.dtos;


import com.neonlab.product.entities.Order;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
public class UserDetailsDto {
    private String name;
    private String email;
    private String primaryPhoneNo;

    /*
    @OneToMany(mappedBy = "UserDetailsDto",fetch = FetchType.LAZY)
    List<Order>orderList;

     */

}

