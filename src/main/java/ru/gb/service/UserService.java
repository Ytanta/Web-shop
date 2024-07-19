package ru.gb.service;

import ru.gb.domain.Order;
import ru.gb.domain.User;
import ru.gb.dto.request.ChangePasswordRequest;
import ru.gb.dto.request.EditUserRequest;
import ru.gb.dto.request.SearchRequest;
import ru.gb.dto.response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    User getAuthenticatedUser();

    Page<Order> searchUserOrders(SearchRequest request, Pageable pageable);

    MessageResponse editUserInfo(EditUserRequest request);

    MessageResponse changePassword(ChangePasswordRequest request);
}
