package com.neonlab.product.service;
import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.ApiOutput;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.repositories.UserRepository;
import com.neonlab.common.utilities.AuthorizationUtil;
import com.neonlab.product.apis.DeleteProductApi;
import com.neonlab.product.dtos.BoughtProductDetailsDto;
import com.neonlab.product.dtos.DriverDetailsDto;
import com.neonlab.product.dtos.OrderDto;
import com.neonlab.product.entities.Order;
import com.neonlab.product.entities.Product;
import com.neonlab.product.models.ProductDeleteReq;
import com.neonlab.product.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
@Loggable
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeleteProductApi deleteProductApi;

    @Transactional
    public ApiOutput<?> createOrder(OrderDto orderDto) throws InvalidInputException, ServerException {
        BigDecimal totalItemCost;
        List<BoughtProductDetailsDto>boughtProductDetailsDtoList = new ArrayList<>();
        try {
            for (String code : orderDto.getProductCodeAndQuantity().keySet()) {
                Product product = productService.fetchProductByCode(code);
                Integer qty = orderDto.getProductCodeAndQuantity().get(code);
                int statusCode = reduceProductQuantity(qty, code);
                if (statusCode == 200) {
                    BigDecimal price = product.getPrice();
                    totalItemCost = price.multiply(BigDecimal.valueOf(qty));
                    BoughtProductDetailsDto boughtProductDetailsDto = mapBoughtProductDetails(product, qty, totalItemCost);
                    mapOrder(orderDto, boughtProductDetailsDto);
                    boughtProductDetailsDtoList.add(boughtProductDetailsDto);
                }
            }
            return new ApiOutput<>(HttpStatus.OK.value(), "Product Purchase Successful", boughtProductDetailsDtoList);
        }catch (NullPointerException e){
            throw new NullPointerException("Product code or Quantity should not be null");
        }
    }

    private void mapOrder(OrderDto orderDto, BoughtProductDetailsDto boughtProductDetailsDto) {
        var order = new Order();
        var authUser = AuthorizationUtil.getCurrentUser();
        assert authUser != null;
        var user = userRepository.findById(authUser.getUserId()).orElse(null);
        order.setUser(user);
        order.setAddressId(orderDto.getAddressId());
        order.setPaymentId(orderDto.getPaymentId());
        order.setBoughtProductDetails(boughtProductDetailsDto);
        order.setTotalItemCost(boughtProductDetailsDto.getPrice());
        order.setDeliveryCharges(getDeliveryCharge());
        order.setTotalCost(boughtProductDetailsDto.getPrice().add(getDeliveryCharge()));
        var driverDetailsDto = mapDriverDetails();
        order.setDriverDetails(driverDetailsDto);
        orderRepository.save(order);
    }

    private DriverDetailsDto mapDriverDetails(){
        var driverDetailsDto = new DriverDetailsDto();
        driverDetailsDto.setDriverName("Raju");
        driverDetailsDto.setContactNo("9574413123");
        driverDetailsDto.setVehicleNo("JH1234");
        return driverDetailsDto;
    }

    private BoughtProductDetailsDto mapBoughtProductDetails(Product product, Integer qty, BigDecimal totalItemCost) {
        var boughtProductDetailsDto = new BoughtProductDetailsDto();
        boughtProductDetailsDto.setQuantity(qty);
        boughtProductDetailsDto.setName(product.getName());
        boughtProductDetailsDto.setPrice(totalItemCost);
        boughtProductDetailsDto.setCode(product.getCode());
        return boughtProductDetailsDto;
    }

    private Integer reduceProductQuantity(Integer qty, String code) throws InvalidInputException, ServerException {
        var productDeleteReq = new ProductDeleteReq();
        productDeleteReq.setQuantity(qty);
        productDeleteReq.setCode(code);
        deleteProductApi.validate(productDeleteReq);
        return deleteProductApi.deleteProductApi(productDeleteReq).getStatusCode();
    }

    private BigDecimal getDeliveryCharge(){
        return BigDecimal.valueOf(0);
    }
}
