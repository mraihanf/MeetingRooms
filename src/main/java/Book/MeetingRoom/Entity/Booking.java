package Book.MeetingRoom.Entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "booking")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Booking extends DateAudit{
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_booking")
    private Integer id_booking;

    @Column(name = "nama")
    private String nama;

    @Column(name = "keperluan")
    private String keperluan;

    @Column(name = "tanggal")
    private Date tanggal;

    @Column(name = "jam_in")
    private String jam_in;

    @Column(name = "jam_out")
    private String jam_out;

    @Column(name = "no_tlp")
    private String no_tlp;

    @JoinColumn(name = "id_room", referencedColumnName = "id_room", insertable = true, updatable = true)
    @ManyToOne
    private Room Room;

}
