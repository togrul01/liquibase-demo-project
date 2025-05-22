package org.company.springliquibase.constants;

public final class LogConstants {
    private LogConstants() {
        throw new IllegalStateException("Constants class");
    }

    // Action Log Messages
    public static final class Action {
        private Action() {
            throw new IllegalStateException("Constants class");
        }

        public static final String GET_CARD_START = "ActionLog.getCard.start with card number: {}";
        public static final String GET_CARD_END = "ActionLog.getCard.end with card number: {}";
        public static final String GET_CARD_BY_ID_START = "ActionLog.getCard.start with id: {}";
        public static final String DELETE_CARD_START = "ActionLog.deleteCard.start with id: {}";
        public static final String UPDATE_CARD_START = "ActionLog.updateCard.start with id: {}";
        public static final String GET_USER_BY_ID_START = "Getting user by id: {}";
        public static final String GET_USER_START = "Getting user by username: {}";
        public static final String GET_USERS_START = "Getting users with criteria: {}, {}";
        public static final String GET_USERS_END = "Got users with criteria: {}, {}";
        public static final String FIND_ALL_START = "Finding all users with name: {}";
        public static final String DELETE_USER_START = "Deleting user with id: {}";
        public static final String UPDATE_USER_START = "Updating user with id: {}";
    }

    // Error Log Messages
    public static final class Error {
        private Error() {
            throw new IllegalStateException("Constants class");
        }

        public static final String CARD_NOT_FOUND_WITH_ID = "Card not found with id: {}";
        public static final String CARD_NOT_FOUND_WITH_NUMBER = "Card not found with number: {}";
        public static final String GETTING_CARD = "Error occurred while getting card: {}";
        public static final String DELETING_CARD = "Error occurred while deleting card: {}";
        public static final String UPDATING_CARD = "Error occurred while updating card: {}";
        public static final String CREATING_CARD = "Error occurred while creating card: {}";
        public static final String FINDING_CARDS = "Error occurred while finding cards: {}";
        public static final String UPDATING_BALANCE = "Error updating balance for card {}: {}";
        public static final String USER_NOT_FOUND_WITH_ID = "User not found with id: {}";
        public static final String USER_NOT_FOUND_WITH_USERNAME = "User not found with username: {}";
        public static final String GETTING_USER = "Error getting user: {}";
        public static final String FINDING_USERS = "Error finding users: {}";
        public static final String DELETING_USER = "Error deleting user: {}";
        public static final String UPDATING_USER = "Error updating user: {}";
        public static final String CREATING_USER = "Error creating user: {}";
        public static final String ERROR_GETTING_USER = "Error getting user: {}";
        public static final String ERROR_FINDING_USERS = "Error finding users: {}";
        public static final String ERROR_DELETING_USER = "Error deleting user: {}";
        public static final String ERROR_UPDATING_USER = "Error updating user: {}";
        public static final String ERROR_CREATING_USER = "Error creating user: {}";

    }

    // Validation Log Messages
    public static final class Validation {
        private Validation() {
            throw new IllegalStateException("Constants class");
        }

        public static final String NEGATIVE_BALANCE = "Negative balance provided: {}";
        public static final String INVALID_CARD_TYPE = "Invalid card type provided: {}";
        public static final String VALIDATION_FAILED = "Validation failed: {}";
    }

    // Balance Log Messages
    public static final class Balance {
        private Balance() {
            throw new IllegalStateException("Constants class");
        }

        public static final String EXCEED_MAX = "Balance would exceed maximum allowed value for card: {}";
    }


}