package com.azat4dev.booking.usersms.generated.server.api;

import com.azat4dev.booking.usersms.generated.server.model.LoginByEmailRequestBodyDTO;
import com.azat4dev.booking.usersms.generated.server.model.LoginByEmailResponseBodyDTO;
import com.azat4dev.booking.usersms.generated.server.model.VerifyEmail400ResponseDTO;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.6.0")
@Controller
@RequestMapping("${openapi.users.base-path:}")
public class CommandsLoginApiController implements CommandsLoginApi {

    private final CommandsLoginApiDelegate delegate;

    public CommandsLoginApiController(@Autowired(required = false) CommandsLoginApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new CommandsLoginApiDelegate() {});
    }

    @Override
    public CommandsLoginApiDelegate getDelegate() {
        return delegate;
    }

}
