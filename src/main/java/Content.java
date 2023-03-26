import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class Content {
    private int comments;
    private int likes;
    private int shares;
    private OffsetDateTime timestamp;
    private String text;

    @Override
    public String toString() {
        return "[" +  comments + ", " + likes + ", " + shares + ", " + text + "]";
    }
}
