package ru.practicum.shareit.booking;


public enum State {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    static State fromString(String state) {
        for (State bookingState: State.values()) {
            if (bookingState.name().equals(state)) {
                return bookingState;
            }
        }
        throw new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS");
    }
}
