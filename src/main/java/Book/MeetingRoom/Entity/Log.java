package Book.MeetingRoom.Entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "log")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Log extends DateAudit{
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_log")
    private Integer id_log;

    @Column(name = "id_booking")
    private Integer id_booking;

    @Column(name = "nama")
    private String nama;

    @Column(name = "tanggal")
    private Date tanggal;

    @Column(name = "jam_in")
    private String jam_in;

    @Column(name = "jam_out")
    private String jam_out;

    @Column(name = "action")
    private String action;
    
}
