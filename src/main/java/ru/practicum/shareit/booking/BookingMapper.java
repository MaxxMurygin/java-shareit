package ru.practicum.shareit.booking;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(booking.getBooker())
                .bookerId(booking.getBooker().getId())
                .item(booking.getItem())
                .status(booking.getStatus())
                .build();
    }

    public static Booking fromBookingDto(BookingDto bookingDto) {
        if (bookingDto == null) {
            return null;
        }
        Booking booking = new Booking();

        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setBooker(bookingDto.getBooker());
        booking.setItem(bookingDto.getItem());
        booking.setStatus(bookingDto.getStatus());
        return booking;
    }
}
