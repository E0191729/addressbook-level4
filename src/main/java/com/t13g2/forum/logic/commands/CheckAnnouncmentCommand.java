package com.t13g2.forum.logic.commands;

import static java.util.Objects.requireNonNull;

import com.t13g2.forum.logic.CommandHistory;
import com.t13g2.forum.logic.commands.exceptions.CommandException;
import com.t13g2.forum.model.Model;
import com.t13g2.forum.model.forum.Announcement;
import com.t13g2.forum.model.forum.User;

//@@xllx1
/**
 * Allow user to check if there is any announcement
 */
public class CheckAnnouncmentCommand extends Command {
    public static final String COMMAND_WORD = "checkAnnounce";

    public static final String MESSAGE_SUCCESS = "Showing new announcement: %1$s";
    public static final String MESSAGE_FAIL = "There is no announcement at the moment";

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        Announcement announcement = null;
        // if user has not login, then throw exception
        if (!model.checkIsLogin()) {
            throw new CommandException(User.MESSAGE_NOT_LOGIN);
        }
        announcement = model.showLatestAnnouncement();
        if (announcement == null) {
            throw new CommandException(MESSAGE_FAIL);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, announcement));
    }
}
