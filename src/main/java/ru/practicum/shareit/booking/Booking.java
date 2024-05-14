package ru.practicum.shareit.booking;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_time")
    @NotNull
    @FutureOrPresent
    private LocalDateTime start;
    @Column(name = "end_time")
    @NotNull
    @Future
    private LocalDateTime end;
    @Column(name = "item_id")
    private Long itemId;
    @Column(name = "booker_id")
    private Long bookerId;
    @Enumerated(EnumType.ORDINAL)
    private Status status;
}
