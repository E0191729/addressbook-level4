package com.t13g2.forum.logic.commands;

import static com.t13g2.forum.logic.parser.CliSyntax.PREFIX_BLOCK;
import static com.t13g2.forum.logic.parser.CliSyntax.PREFIX_USER_NAME;
import static java.util.Objects.requireNonNull;

import com.t13g2.forum.logic.CommandHistory;
import com.t13g2.forum.logic.commands.exceptions.CommandException;
import com.t13g2.forum.model.Context;
import com.t13g2.forum.model.Model;
import com.t13g2.forum.model.UnitOfWork;
import com.t13g2.forum.model.forum.User;
import com.t13g2.forum.storage.forum.EntityDoesNotExistException;

//@@author xllx1
/**
 * Allow admin to block user from posting a new thread.
 */
public class BlockUserFromCreatingCommand extends Command {

    public static final String COMMAND_WORD = "blockUser";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Blocks a certain user from posting new thread."
        + "\nParameters: "
        + PREFIX_USER_NAME + "USER NAME "
        + PREFIX_BLOCK + "BLOCK OR UNBLOCK"
        + "\nExample: " + COMMAND_WORD + " "
        + PREFIX_USER_NAME + "john "
        + PREFIX_BLOCK + "true";

    public static final String MESSAGE_SUCCESS = "User %1$s successfully: %2$s";
    public static final String MESSAGE_USER_IS_ADMIN = "The user \"%s\" is an admin, no point blocking.";
    public static final String MESSAGE_INVALID_USER = "The user \"%s\" does not exist.";
    public static final String MESSAGE_DUPLICATE_BLOCK = "The user \"%s\" has already been blocked.";
    public static final String MESSAGE_DUPLICATE_UNBLOCK = "The user \"%s\" has not been block.";
    public static final String MESSAGE_BLOCK_CONSTRAINTS = "Invalid parameter: block can only take true or false.";

    private final String userNameToBlock;
    private final boolean block;

    /**
     * Creates an BlockUserFromCreatingCommand to block the specified {@code User}
     */
    public BlockUserFromCreatingCommand(String userName, boolean block) {
        requireNonNull(userName);
        requireNonNull(block);
        userNameToBlock = userName;
        this.block = block;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        User user = null;
        // if user has not login or is not admin, then throw exception
        if (!Context.getInstance().isLoggedIn()) {
            throw new CommandException(User.MESSAGE_NOT_LOGIN);
        } else if (!Context.getInstance().isCurrentUserAdmin()) {
            throw new CommandException(User.MESSAGE_NOT_ADMIN);
        }

        try (UnitOfWork unitOfWork = new UnitOfWork()) {
            user = unitOfWork.getUserRepository().getUserByUsername(userNameToBlock);
            if (user.isAdmin()) {
                throw new CommandException(String.format(MESSAGE_USER_IS_ADMIN, userNameToBlock));
            } else if (block && user.isBlock()) {
                throw new CommandException(String.format(MESSAGE_DUPLICATE_BLOCK, userNameToBlock));
            } else if (!block && !user.isBlock()) {
                throw new CommandException(String.format(MESSAGE_DUPLICATE_UNBLOCK, userNameToBlock));
            } else {
                user.setBlock(block);
                unitOfWork.getUserRepository().updateUser(user);
                unitOfWork.commit();
            }
        } catch (CommandException e) {
            throw e;
        } catch (EntityDoesNotExistException e) {
            throw new CommandException(String.format(MESSAGE_INVALID_USER, userNameToBlock));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, (block ? "blocked" : "unblocked"), userNameToBlock));
    }

}
