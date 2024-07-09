package Book.MeetingRoom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Book.MeetingRoom.Entity.Log;

@Repository
public interface LogRepository extends JpaRepository <Log, Integer>{
    
}
