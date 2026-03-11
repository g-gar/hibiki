package com.ggar.hibiki.core.identity.service;

import com.ggar.hibiki.core.identity.dto.UpdateProfileRequest;
import com.ggar.hibiki.core.identity.dto.UserDto;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;

public interface UpdateProfileCommandHandler extends CommandHandler<UpdateProfileRequest, UserDto> {}
