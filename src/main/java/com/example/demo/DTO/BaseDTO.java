package com.example.demo.DTO;


import com.example.demo.Exception.DTOConversionException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class BaseDTO implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDTO.class);

    public <T> T toModel(Class<T> clazz, ModelMapper mapper) throws DTOConversionException {
        try {
            return mapper.map(this, clazz);
        } catch (Exception e) {
            LOGGER.error(null, e);
            throw new DTOConversionException(
                    String.format(
                            "Error converting to class %s, message %s",
                            clazz.getTypeName(),
                            e.getLocalizedMessage()));
        }
    }

    public <T> T toModel(T destObj, ModelMapper mapper) throws DTOConversionException {
        try {
            mapper.map(this, destObj);
            return destObj;
        } catch (Exception e) {
            LOGGER.error(null, e);
            throw new DTOConversionException(
                    String.format(
                            "Error converting to class %s, message %s",
                            destObj.getClass().getTypeName(),
                            e.getLocalizedMessage()));
        }
    }

}
