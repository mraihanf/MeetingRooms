package Book.MeetingRoom.Controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import Book.MeetingRoom.Entity.Booking;
import Book.MeetingRoom.Entity.Room;
import Book.MeetingRoom.Repository.BookingRepository;
import Book.MeetingRoom.Repository.RoomRepository;

@Controller
public class BookController {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping(value = "/getallbooking")
    public ResponseEntity<Map> getallbooking(@RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") Integer start, @RequestParam(defaultValue = "10") Integer length) {
        Map data = new HashMap<>();
        Pageable pageable = PageRequest.of(start, length);
        Page<Booking> dataPaging = bookingRepository.findallaa(search, pageable);
        data.put("data", dataPaging);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping(value = "/getbookingtoday")
    public ResponseEntity<Map> getbookingtoday(@RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") Integer start, @RequestParam(defaultValue = "10") Integer length) throws ParseException {
        Map data = new HashMap<>();
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = formatter.format(currentDate);
        Date parsedDate = formatter.parse(formattedDate);
        Pageable pageable = PageRequest.of(start, length); 
        Page<Booking> dataPaging = bookingRepository.findalltoday(search, parsedDate, pageable);
        data.put("data", dataPaging);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping(value = "/getbookroom")
    @ResponseBody
    public ResponseEntity<Map> getbookroom(@RequestParam int durasi, @RequestParam String jam, @RequestParam int jumlah, 
                                @RequestParam String keperluan, @RequestParam String tanggal, Model model){

        Map data = new HashMap<>();

        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        List<Room> listroom = roomRepository.countkapasitas(jumlah);
        ArrayList<Integer> id_rooms = new ArrayList<>();
        for (Room room : listroom) {
            id_rooms.add(room.getId_room());
        }

        LocalDate a = LocalDate.parse(tanggal, formatterDate);
        Date tggl = java.sql.Date.valueOf(a);

        ArrayList<Integer> id_book = new ArrayList<>();

        if (bookingRepository.findBookingsByDate(tggl)) {
            List<Booking> bookingsWithSameDate = bookingRepository.findBookingsByDateList(tggl);
            for(Booking books : bookingsWithSameDate){
                String existingJamIn = books.getJam_in();
                String existingJamOut = books.getJam_out();
                String[] parts = jam.split(":");
                String waktua = jam.replace(":", ".");
                int jamAwal = Integer.parseInt(parts[0]);
                int menitAwal = Integer.parseInt(parts[1]);
                int totalMenitAwal = (jamAwal * 60) + menitAwal;
                int totalMenitAkhir = (totalMenitAwal + durasi) % (24 * 60);
                int jamAkhir = totalMenitAkhir / 60;
                int menitAkhir = totalMenitAkhir % 60;
                String waktu = String.format("%02d.%02d", jamAkhir, menitAkhir);
                if(!bookingRepository.findboooks(tggl, existingJamIn, existingJamOut, waktua, waktu)){
                    id_book.add(books.getRoom().getId_room());
                }
            }
        }

        ArrayList<Integer> AL = new ArrayList<>();      
        for (Integer i = 0; i < id_rooms.size(); i++) {
            boolean found = false;
            for (Integer j = 0; j < id_book.size(); j++) {
                if (id_rooms.get(i).equals(id_book.get(j))) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                AL.add(id_rooms.get(i));
            }
        }


        List<Room> rooms = roomRepository.getdataroom(AL);

        data.put("data", rooms);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping(value = "/inputbooking")
    @ResponseBody
    public ResponseEntity<Map> inputbooking(@RequestParam String tanggal, @RequestParam String jam, @RequestParam Integer durasi, 
                                            @RequestParam String keperluan, @RequestParam Integer jumlah, 
                                            @RequestParam Integer room_id, @RequestParam String nama, @RequestParam String tlp) {
        Map data = new HashMap<>();
        Date date = new Date();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH.mm");
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate a = LocalDate.parse(tanggal, formatterDate);
        Date tggl = java.sql.Date.valueOf(a);
        
        String[] parts = jam.split(":");
        int jamAwal = Integer.parseInt(parts[0]);
        int menitAwal = Integer.parseInt(parts[1]);
        int totalMenitAwal = (jamAwal * 60) + menitAwal;
        int totalMenitAkhir = (totalMenitAwal + durasi) % (24 * 60);
        int jamAkhir = totalMenitAkhir / 60;
        int menitAkhir = totalMenitAkhir % 60;
        String waktua = jam.replace(":", ".");
        String waktu = String.format("%02d.%02d", jamAkhir, menitAkhir);

        if (bookingRepository.findBookingsByDate(tggl)) {
            List<Booking> bookingsWithSameDate = bookingRepository.findBookingsByDateList(tggl);
            for (Booking booking : bookingsWithSameDate) {
                if (booking.getRoom().getId_room() == room_id) {
                    String existingJamIn = booking.getJam_in();
                    String existingJamOut = booking.getJam_out();
                    LocalTime inputJamIn = LocalTime.parse(waktua, formatter);
                    LocalTime inputJamOut = LocalTime.parse(waktu, formatter);
                    LocalTime existingJamInParsed = LocalTime.parse(existingJamIn, formatter);
                    LocalTime existingJamOutParsed = LocalTime.parse(existingJamOut, formatter);

                    if ((inputJamIn == existingJamInParsed) || (inputJamOut == existingJamOutParsed) ||
                            (inputJamIn.isAfter(existingJamInParsed) && inputJamIn.isBefore(existingJamOutParsed)) ||
                            (inputJamOut.isAfter(existingJamInParsed) && inputJamOut.isBefore(existingJamOutParsed)) ||
                            (inputJamIn.isBefore(existingJamInParsed) && inputJamOut.isAfter(existingJamOutParsed))) {
                        data.put("icon", "error");
                        data.put("message", "Waktu sudah dipesan pada interval yang sama");
                        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
                    }
                }
            }
        }

        Booking book = new Booking();
        book.setRoom(roomRepository.findByNameroom(room_id).get());
        book.setJam_in(waktua);
        book.setJam_out(waktu);
        book.setNama(nama);
        book.setNo_tlp(tlp);
        book.setKeperluan(keperluan);
        book.setTanggal(tggl);
        bookingRepository.save(book);

        data.put("icon", "success");
        data.put("message", "Sukses Insert Booking");
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PutMapping(value = "/putbooking")
    public ResponseEntity<Map> putbooking(@RequestParam(required = true) Integer id, @RequestParam Integer id_room, @RequestParam String nama,
    @RequestParam String jamin, @RequestParam String jamout, @RequestParam Date tgl) {
        Map data = new HashMap<>();
        if (!roomRepository.existsById(id)) {
            data.put("message", "Data Tidak ada");
            return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
        }

        Booking book = bookingRepository.getById(id);
        book.setRoom(roomRepository.findByNameroom(id).get());
        book.setJam_in(jamin);
        book.setJam_out(jamout);
        book.setNama(nama);
        book.setTanggal(tgl);
        bookingRepository.save(book);

        data.put("icon", "success");
        data.put("message", "Sukses Update Room");
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deletebook")
    public ResponseEntity<Map> deletebook(@RequestParam(required = true) Integer id) {
        Map data = new HashMap<>();

        if (!bookingRepository.existsById(id)) {
            data.put("message", "Data Tidak ada");
            return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
        }

        bookingRepository.deleteById(id);

        data.put("icon", "success");
        data.put("message", "Sukses Delete Booking");
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
