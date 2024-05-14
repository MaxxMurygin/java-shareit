package ru.practicum.shareit.booking;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .bookerId(booking.getBookerId())
                .itemId(booking.getItemId())
                .status(booking.getStatus())
                .build();
    }

    public static Booking fromBookingDto(BookingDto bookingDto) {
        Booking booking = new Booking();

        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setBookerId(bookingDto.getBookerId());
        booking.setItemId(bookingDto.getItemId());
        booking.setStatus(bookingDto.getStatus());
        return booking;
    }
}
