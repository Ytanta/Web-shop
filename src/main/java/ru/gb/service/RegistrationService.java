package ru.gb.service;

import ru.gb.dto.response.MessageResponse;
import ru.gb.dto.request.UserRequest;

public interface RegistrationService {

    MessageResponse registration(String captchaResponse, UserRequest user);

    MessageResponse activateEmailCode(String code);
}
