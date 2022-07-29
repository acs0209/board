package com.study.board.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// -> @Entity 라고 하면 JPA가 클래스 Board를 읽어들여서 처리를 해준다
@Entity // -> table을 의미한다 자바의 영속성 jpa에서 @Entity은 클래스가 db에 있는 table을 의미한다는걸 의미한다
@Getter @Setter
public class Board {

    @Id // -> 이 어노테이션은 프라이머리 key를 의미한다
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 게시글 번호
    private String title; // 제목
    private String content; // 내용
    // 위 id, title, content는 뭐를 의미할까 바로 board스키마 안에 있는 Tables중에 board의 구조를 보면
    // 저희가 게시글 번호로 지정한 id 제목 title 내용 content가 있다 즉 db에 있는 board 형식에 맞게 위에
    // id, title, content를 만든거다

    private String filename;
    private String filepath;

}
