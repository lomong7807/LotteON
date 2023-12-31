package co.kr.lotteon.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointDTO {

    private     int              pointNo;
    private     String           uid;
    private     int              ordNo;
    private     int              point;
    private     String           note;
    private     LocalDateTime    pointDate;
    // 추가 필드
    private     LocalDateTime    expiredDate;
}
