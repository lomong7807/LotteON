package co.kr.lotteon.mapper;

import co.kr.lotteon.dto.member.PointDTO;
import co.kr.lotteon.dto.my.PageRequestMyDTO;
import co.kr.lotteon.dto.my.PageResponseMyDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PointMapperTest {

    @Autowired
    private PointMapper pointMapper;


    @Test
    public void pointListTotal() {
        PageRequestMyDTO pageRequestMyDTO = PageRequestMyDTO.builder().build();

        List<PointDTO> list  = pointMapper.myPointList("admin", "", "", 0);
        int total = pointMapper.myPointCount("admin", "", "");

        PageResponseMyDTO page = PageResponseMyDTO.builder().pageRequestMyDTO(pageRequestMyDTO).list(list).total(total).build();

        System.out.println("===========================================================================================");
        System.out.println("list  : " + list);
        System.out.println("total : " + total);
        System.out.println("start : " + page.getStart());
        System.out.println("end   : " + page.getEnd());
        System.out.println("===========================================================================================");
    }

    public void insertUsedPoint(){

        pointMapper.insertUsedPoint("lomong7807", 380, "포인트 사용" ,-10000);

    }
}

