package com.neonlab.product.models;
<<<<<<< Updated upstream
=======
import com.neonlab.product.apis.AddProductApi;
import jakarta.persistence.Column;
>>>>>>> Stashed changes
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;


@Data
public class ProductDeleteReq {
    private String code;
    private Integer quantity;
}
