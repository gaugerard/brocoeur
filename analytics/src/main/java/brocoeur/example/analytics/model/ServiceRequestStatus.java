package brocoeur.example.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequestStatus {
    @PrimaryKey
    private int jobId;
    private String status;
    private int amountBlocked;
    private int userId;
    private String strategy;
    private long insertionTimeMilliSecond;
    private long ackTimeMilliSecond;
}
