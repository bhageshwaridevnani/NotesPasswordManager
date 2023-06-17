package com.example.demo.Model;

import com.example.demo.Exception.BaseConversionException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.Constructor;

public abstract class BaseModel implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseModel.class);

    /**
     * convert into DTO object
     *
     * @param clazz  extends BaseDTO
//     * @param mapper
     * @return
     * @throws BaseConversionException
     * @author Parita
     */

    public <S> S toDTO(Class<S> clazz, ModelMapper mapper) throws BaseConversionException {
        try {
            return mapper.map(this, clazz);
        } catch (SecurityException e) {
            LOGGER.error("BaseConversionException", e);
            throw new BaseConversionException(e);
        } catch (Exception e) {
            LOGGER.error(null, e);
            throw new BaseConversionException(e);
        }
    }

    public <S> S toDTO(Class<S> clazz) throws BaseConversionException {

        try {
            Constructor<S> constructor = clazz.getConstructor();
            Object objDTO = constructor.newInstance();

            BeanUtils.copyProperties(this, objDTO);
            return clazz.cast(objDTO);

        } catch (SecurityException e) {
            LOGGER.error("BaseConversionException", e);
            throw new BaseConversionException(e);
        } catch (Exception e) {
            LOGGER.error(null, e);
            throw new BaseConversionException(e);
        }
    }
}
