package Book.MeetingRoom.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import Book.MeetingRoom.Entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository <Booking, Integer>{
    
    // @Query(value = "SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM booking c WHERE c.tanggal = ?1",
    //     countQuery = "SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM booking c WHERE c.tanggal = ?1",
    //     nativeQuery = true)
    // boolean findbookname(Date tanggal);

    // @Query(value = "SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM booking c WHERE c.id_room = ?1",
    //     countQuery = "SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM booking c WHERE c.id_room = ?1",
    //     nativeQuery = true)
    // boolean findbookroom(Integer id);

    @Query(value = "SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM booking c WHERE DATE(c.tanggal) = DATE(?1)",
        countQuery = "SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM booking c WHERE DATE(c.tanggal) = DATE(?1)",
        nativeQuery = true)
    boolean findBookingsByDate(Date tgl);

    @Query(value = "SELECT * FROM booking WHERE id_room IN (?1)",
        countQuery = "SELECT * FROM booking WHERE id_room IN (?1)",
        nativeQuery = true)
    List<Booking> findbooklist(String[] id);

    @Query(value = "SELECT * FROM booking WHERE DATE(tanggal) = DATE(?1)",
        countQuery = "SELECT * FROM booking WHERE DATE(tanggal) = DATE(?1)",
        nativeQuery = true)
    List<Booking> findBookingsByDateList(Date tgl);

    @Query(value = "SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM booking c WHERE (DATE(tanggal) = DATE(?1)) AND (jam_in > ?2 AND jam_in < ?3) AND (jam_out > ?4 AND jam_out < ?5)",
        countQuery = "SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM booking c WHERE (DATE(tanggal) = DATE(?1)) AND (jam_in > ?2 AND jam_in < ?3) AND (jam_out > ?4 AND jam_out < ?5)",
        nativeQuery = true)
    boolean findboooks(Date tgl, String j2 , String j3, String j4, String j5);

    @Query(value = "SELECT c.*, v.id_room as room_id FROM booking c INNER JOIN room v on c.id_room = v.id_room WHERE (CONCAT(c.nama, c.keperluan, c.tanggal, c.jam_in, c.jam_out, c.no_tlp, v.kapasitas, v.name_room) ILIKE %?1%) ORDER BY c.created_at DESC",
           countQuery = "SELECT count(*) FROM booking c INNER JOIN room v on c.id_room = v.id_room WHERE (CONCAT(c.nama, c.keperluan, c.tanggal, c.jam_in, c.jam_out, c.no_tlp, v.kapasitas, v.name_room) ILIKE %?1%) ORDER BY c.created_at DESC",
           nativeQuery = true)
    Page<Booking> findallaa(String search, Pageable pageable);

    @Query(value = "SELECT c.*, v.id_room as room_id FROM booking c INNER JOIN room v on c.id_room = v.id_room WHERE (CONCAT(c.nama, c.keperluan, c.tanggal, c.jam_in, c.jam_out, c.no_tlp, v.kapasitas, v.name_room) ILIKE %?1% AND tanggal = ?2) ORDER BY c.created_at DESC",
           countQuery = "SELECT count(*) FROM booking c INNER JOIN room v on c.id_room = v.id_room WHERE (CONCAT(c.nama, c.keperluan, c.tanggal, c.jam_in, c.jam_out, c.no_tlp, v.kapasitas, v.name_room) ILIKE %?1% AND tanggal = ?2) ORDER BY c.created_at DESC",
           nativeQuery = true)
    Page<Booking> findalltoday(String search, Date tgl, Pageable pageable);

    @Query(value = "SELECT c.*, v.id_room as room_id FROM booking c INNER JOIN room v on c.id_room = v.id_room WHERE (CONCAT(c.nama, c.keperluan, c.tanggal, c.jam_in, c.jam_out, c.no_tlp, v.kapasitas, v.name_room) ILIKE %?1% AND tanggal = ?2) ORDER BY c.created_at DESC",
           countQuery = "SELECT count(*) FROM booking c INNER JOIN room v on c.id_room = v.id_room WHERE (CONCAT(c.nama, c.keperluan, c.tanggal, c.jam_in, c.jam_out, c.no_tlp, v.kapasitas, v.name_room) ILIKE %?1% AND tanggal = ?2) ORDER BY c.created_at DESC",
           nativeQuery = true)
    List<Booking> findalltodays(String search, Date tgl);
    
}
