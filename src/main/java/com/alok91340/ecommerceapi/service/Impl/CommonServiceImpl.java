package com.alok91340.ecommerceapi.service.Impl;

import javax.annotation.Resource;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.alok91340.ecommerceapi.Exception.EcommerceApiException;
import com.alok91340.ecommerceapi.dto.CartItemDto;
import com.alok91340.ecommerceapi.entities.User;
import com.alok91340.ecommerceapi.repository.UserRepository;
import com.alok91340.ecommerceapi.response.CartItemResponse;
import com.alok91340.ecommerceapi.response.CommonResponse;
import com.alok91340.ecommerceapi.service.CommonService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
@Component("commonService")
public class CommonServiceImpl implements CommonService {

    private static Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public CommonResponse getResponseContent(Page page, List dtoList) {

        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setContent(dtoList);
        commonResponse.setPageNo(page.getNumber());
        commonResponse.setPageSize(page.getSize());
        commonResponse.setTotalPages(page.getTotalPages());
        commonResponse.setTotalElements(page.getTotalElements());
        commonResponse.setLast(page.isLast());

        return commonResponse;
    }

    @Override
    public CartItemResponse getResponse(CartItemDto cartItemDto) {

        CartItemResponse cartItemResponse = new CartItemResponse();

        double totalPrice = cartItemDto.getProduct().getPrice() * cartItemDto.getQuantity();
        List<CartItemDto> cartItemDtoList = new ArrayList<>();
        cartItemDtoList.add(cartItemDto);
        cartItemResponse.setContent(cartItemDtoList);
        cartItemResponse.setTotalPrice(totalPrice);
        return cartItemResponse;
    }

    @Override
    public User getCurrentAuthenticatedUser(Authentication authentication) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            throw new EcommerceApiException("User not authenticated", HttpStatus.BAD_REQUEST);
        }
        String currentUserEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User Not found"));

        return currentUser;
    }

    @Override
    public Object mapToEntity(Object type) {
        Object entityObject = modelMapper.map(type, Object.class);
        return entityObject;
    }

    @Override
    public Object mapToDto(Object type) {
        Object dtoObject = modelMapper.map(type, Object.class);
        return dtoObject;
    }
}
