package com.azat4dev.booking.usersms.generated.server.api;

import com.azat4dev.booking.usersms.generated.server.model.GenerateUploadUserPhotoUrlRequestBodyDTO;
import com.azat4dev.booking.usersms.generated.server.model.GenerateUploadUserPhotoUrlResponseBodyDTO;
import com.azat4dev.booking.usersms.generated.server.model.UpdateUserPhoto200ResponseDTO;
import com.azat4dev.booking.usersms.generated.server.model.UpdateUserPhotoRequestBodyDTO;


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
public class CommandsUpdateUserPhotoApiController implements CommandsUpdateUserPhotoApi {

    private final CommandsUpdateUserPhotoApiDelegate delegate;

    public CommandsUpdateUserPhotoApiController(@Autowired(required = false) CommandsUpdateUserPhotoApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new CommandsUpdateUserPhotoApiDelegate() {});
    }

    @Override
    public CommandsUpdateUserPhotoApiDelegate getDelegate() {
        return delegate;
    }

}
