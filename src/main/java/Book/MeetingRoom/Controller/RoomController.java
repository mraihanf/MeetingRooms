package Book.MeetingRoom.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.function.EntityResponse;

import Book.MeetingRoom.Entity.Room;
import Book.MeetingRoom.Repository.LogRepository;
import Book.MeetingRoom.Repository.RoomRepository;

@Controller
public class RoomController {
    
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private LogRepository logRepository;

    @GetMapping(value = "/getallroom")
    public ResponseEntity<Map> getallroom(@RequestParam String search, @RequestParam String by,
                                        @RequestParam(defaultValue = "0") Integer start, @RequestParam(defaultValue = "10") Integer length) {
        Map data = new HashMap<>();
        Pageable pageable = PageRequest.of(start, length, Sort.by("updated_at").descending());
        ExampleMatcher caseInsensitiveExampleMatcher = ExampleMatcher.matching().withMatcher(by, GenericPropertyMatchers.contains()).withIgnoreCase();
        Room room = new Room();
        if (by.equalsIgnoreCase("nama_room")) {
            room.setName_room(search);
        }
        Example<Room> exampleAsset = Example.of(room, caseInsensitiveExampleMatcher);
        Page<Room> dataPaging = roomRepository.findAll(exampleAsset, pageable);
        data.put("data", dataPaging);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping(value = "/inputroom")
    public ResponseEntity<Map> inputroom(@RequestParam String nama, @RequestParam String kapasitas){
      Map data = new HashMap<>();
      Date date = new Date();

      if(roomRepository.findname(nama)){
        data.put("icon", "error");
        data.put("message", "Nama Room Sudah Ada");
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
      }

      Room room = new Room();
      room.setName_room(nama);
      room.setKapasitas(kapasitas);
      roomRepository.save(room);

      
      // logRepository
      // .save(new Log(null, null, ));

      data.put("icon", "success");
      data.put("message", "Sukses Insert Room");
      return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PutMapping(value = "/putroom")
    public ResponseEntity<Map> putroom(@RequestParam (required = true) Integer id, @RequestParam String nama, @RequestParam String kapasitas){
      Map data = new HashMap<>();
      if (!roomRepository.existsById(id)) {
        data.put("message", "Data Tidak ada"); 
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
      }

      Room room = roomRepository.getById(id);
      room.setName_room(nama);
      room.setKapasitas(kapasitas);
      roomRepository.save(room);
      
      data.put("icon", "success");
      data.put("message", "Sukses Update Room");
      return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteroom")
    public ResponseEntity<Map> deleteroom(@RequestParam(required = true) Integer id) {
        Map data = new HashMap<>();

        if (!roomRepository.existsById(id)) {
            data.put("message", "Data Tidak ada");
            return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
        }

        roomRepository.deleteById(id);

        data.put("icon", "success");
        data.put("message", "Sukses Delete Room");
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
