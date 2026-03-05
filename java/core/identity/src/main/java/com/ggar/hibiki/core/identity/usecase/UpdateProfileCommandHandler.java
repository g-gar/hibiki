package com.ggar.hibiki.core.identity.usecase;

import com.ggar.hibiki.core.identity.model.UpdateProfileRequest;
import com.ggar.hibiki.core.identity.model.User;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;

public interface UpdateProfileCommandHandler extends CommandHandler<UpdateProfileRequest, User> {
}
