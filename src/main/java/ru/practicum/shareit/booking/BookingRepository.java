package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByItemId(Long itemId, Pageable page);

    List<Booking> findAllByItemIdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime start, Pageable page);

    List<Booking> findAllByItemIdAndStartBeforeOrderByEndDesc(Long itemId, LocalDateTime end, Pageable page);

    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId, Pageable page);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long bookerId, Status status, Pageable page);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime end, Pageable page);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime start, Pageable page);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long bookerId, LocalDateTime start,
                                                                             LocalDateTime end, Pageable page);

    @Query("select b from Booking as b " +
            "join b.item as i " +
            "where i.owner = :ownerId " +
            "order by b.start desc")
    List<Booking> findAllByOwnerId(@Param("ownerId") Long ownerId,
                                   Pageable page);

    @Query("select b from Booking as b " +
            "join b.item as i " +
            "where i.owner = :ownerId and b.start > :now " +
            "order by b.start desc")
    List<Booking> findByOwnerIdAndFutureState(@Param("ownerId") Long ownerId,
                                              @Param("now") LocalDateTime now,
                                              Pageable page);

    @Query("select b from Booking as b " +
            "join b.item as i " +
            "where i.owner = :ownerId and b.end < :now " +
            "order by b.start desc")
    List<Booking> findByOwnerIdAndPastState(@Param("ownerId") Long ownerId,
                                            @Param("now") LocalDateTime now,
                                            Pageable page);

    @Query("select b from Booking as b " +
            "join b.item as i " +
            "where i.owner = :ownerId and b.start < :now and b.end >= :now " +
            "order by b.start desc")
    List<Booking> findByOwnerIdAndCurrentState(@Param("ownerId") Long ownerId,
                                               @Param("now") LocalDateTime now,
                                               Pageable page);


    @Query("select b from Booking as b " +
            "join b.item as i " +
            "where i.owner = :ownerId and b.status like :status " +
            "order by b.start desc")
    List<Booking> findByOwnerIdAndStatus(@Param("ownerId") Long ownerId,
                                         @Param("status") Status status,
                                         Pageable page);
}
