package brocoeur.example.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;

@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWinLossByGame {
    @PrimaryKeyColumn(name = "game_id", type = PARTITIONED)
    private int gameId;
    @PrimaryKeyColumn(name = "user_id", ordinal = 0)
    private int userId;
    private String gameName;
    private String pseudo;
    private int numberOfWin;
    private int numberOfLoss;
}
