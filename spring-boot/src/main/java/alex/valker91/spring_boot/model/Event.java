package alex.valker91.spring_boot.model;

import alex.valker91.spring_boot.model.impl.EventImpl;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;

/**
 * Created by maksym_govorischev.
 */
@JsonDeserialize(as = EventImpl.class)
public interface Event {
    /**
     * Event id. UNIQUE.
     * @return Event Id
     */
    long getId();
    void setId(long id);
    String getTitle();
    void setTitle(String title);

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    Date getDate();
    void setDate(Date date);
    int getTicketPrice();
    void setTicketPrice(int ticketPrice);
}
