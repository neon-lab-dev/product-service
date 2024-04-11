package com.neonlab.product.utils;
import org.modelmapper.ModelMapper;


public class MapperUtils {

    private static final ModelMapper mapper = new ModelMapper();

    public static <S, D> D map(S source, Class<D> destinationClass) {
        return mapper.map(source, destinationClass);
    }
}
