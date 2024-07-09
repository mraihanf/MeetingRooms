package Book.MeetingRoom.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import Book.MeetingRoom.Entity.Room;

@Repository
public interface RoomRepository extends JpaRepository <Room, Integer>{
    
    @Query(value = "SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM room c WHERE c.name_room = ?1",
        countQuery = "SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM room c WHERE c.name_room = ?1",
        nativeQuery = true)
    boolean findname(String nama);

    @Query(value = "SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM room c WHERE c.name_room = ?1",
        countQuery = "SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM room c WHERE c.name_room = ?1",
        nativeQuery = true)
    boolean jumlahcount(Integer nama);

    @Query(value = "SELECT * FROM room WHERE CAST(kapasitas AS INT) >= ?1",
    countQuery = "SELECT * FROM room WHERE CAST(kapasitas AS INT) >= ?1",
    nativeQuery = true)
    List<Room> countkapasitas(int kapasitas);

    @Query(value = "SELECT * FROM room WHERE id_room in (?1)",
    countQuery = "SELECT * FROM room WHERE id_room in (?1)",
    nativeQuery = true)
    List<Room> getdataroom(ArrayList<Integer> aL);

    @Query(value = "SELECT * FROM room WHERE id_room = ?1",
        countQuery = "SELECT count(*) FROM room WHERE id_room = ?1",
        nativeQuery = true)
    Optional <Room> findByNameroom(Integer id);

}
