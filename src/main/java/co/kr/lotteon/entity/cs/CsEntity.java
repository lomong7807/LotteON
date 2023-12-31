package co.kr.lotteon.entity.cs;

import co.kr.lotteon.dto.cs.CsDTO;
import co.kr.lotteon.dto.product.ProductDTO;
import co.kr.lotteon.entity.member.MemberEntity;
import co.kr.lotteon.entity.product.ProductEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/*@SqlResultSetMapping(
    name = "answerMapping",
    classes = {
        @ConstructorResult(
            targetClass = CsDTO.class,
            columns = {
                @ColumnResult(name = "no",      type = Integer.class),
                @ColumnResult(name = "parent",  type = Integer.class),
                @ColumnResult(name = "comment", type = Integer.class),
                @ColumnResult(name = "group",   type = String.class),
                @ColumnResult(name = "cate1",   type = String.class),
                @ColumnResult(name = "cate2",   type = String.class),
                @ColumnResult(name = "uid",     type = String.class),
                @ColumnResult(name = "title",   type = String.class),
                @ColumnResult(name = "content", type = String.class),
                @ColumnResult(name = "hit",     type = Integer.class),
                @ColumnResult(name = "prodNo",  type = Integer.class),
                @ColumnResult(name = "email",   type = String.class),
                @ColumnResult(name = "rdate",   type = LocalDateTime.class),
                @ColumnResult(name = "answer",  type = String.class),
                @ColumnResult(name = "ansdate", type = LocalDateTime.class)
            }
        )
    }
)*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "km_board")
public class CsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;


    private int parent;
    private int comment;

    /*@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent", referencedColumnName = "no", insertable = false, updatable = false)
    private CsEntity answer;*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`group`")
    private CsGroupEntity group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cate1")
    private CsCate1Entity cate1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cate2")
    private CsCate2Entity cate2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private MemberEntity uid;

    private String title;
    private String content;

    private int hit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prodNo")
    private ProductEntity prodNo;
    private String email;

    @CreationTimestamp
    private LocalDateTime rdate;

    public CsDTO toDTO() {
        CsDTO.CsDTOBuilder builder = CsDTO.builder()
                .no(no)
                .parent(parent)
                .comment(comment)
                .group(group == null ? null : group.toDTO())
                .cate1(cate1 == null ? null : cate1.toDTO())
                .cate2(cate2 == null ? null : cate2.toDTO())
                .prodNo(prodNo == null ? null : prodNo.toDTO())
                .uid(uid == null ? null : uid.toDTO())
                .title(title)
                .content(content)
                .hit(hit)
                .email(email)
                .rdate(rdate);

        return builder.build();
    }
}