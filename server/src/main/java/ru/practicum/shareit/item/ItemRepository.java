package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(" select i " +
            "from Item i " +
            "where (upper(i.name) like upper(concat('%', :text, '%')) " +
            "       or upper(i.description) like upper(concat('%', :text, '%'))) " +
            "and i.available = true")
    List<Item> search(@Param("text") String text);

    List<Item> findByOwnerOrderById(Long ownerId);

    List<Item> findByRequestId(Long requestId);
}
