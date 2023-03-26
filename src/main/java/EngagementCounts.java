import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class EngagementCounts {
    int comments;
    int likes;
    int shares;
}
