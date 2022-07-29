package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller // 이게 컨트롤러 임을 나타냄
public class BoardController {

    @Autowired
    private BoardService boardService; // service를 controller에서 이용

    @GetMapping("/board/write")
    public String boardWriteForm() {

        return "boardwrite";
    }

    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, Model model, MultipartFile file) throws Exception{ // 파라미터로 String title, String content이런식으로 읽어 들일수 있겠지만
                                               // 나중에 많아지면 복잡해지니 Board객체로 파라미터를 받을 수가 있다
                                               // title과 content는 어디서 받아오는걸까 boardwrite.html form 부분을 보면
                                               // name="title" name="content"부분이 저기 title,content부분에 들어온다
                                               // Board board는 어떻게 받을까 우리가 entity를 만들었잖아 그 entity 를 통째로
                                               // Board board에 받을 수가 있다
        boardService.write(board, file);
        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");
        return "message";
    }

    @GetMapping("/board/list")
    public String boardList(Model model, @PageableDefault(page = 0, size = 10, sort="id", direction = Sort.Direction.DESC) Pageable pageable,
                            String searchKeyword) {

        Page<Board> list = null;

        if(searchKeyword == null) {
            list = boardService.boardList(pageable);
        } else {
            list = boardService.boardSearchList(searchKeyword, pageable);
        }



        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());


        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "boardlist";
    }
// 페이징처리를 위해 아래 코드를 수정해서 위와 같이 코드를 만든다
//    @GetMapping("/board/list")
//    public String boardList(Model model) {
//
//        model.addAttribute("list", boardService.boardList());
//
//        return "boardlist";
//    }


    @GetMapping("/board/view") // localhost:8080/board/view?id=1 여기 id=1이 Integer id에 들어간다
    public String boardView(Model model, Integer id) {


        model.addAttribute("board", boardService.boardView(id));
        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id) {

        boardService.boardDelete(id);
        return "redirect:/board/list";
    }

    // 중요한점은 수정을 할때 수정할 데이터를 넘겨와야 하는게 중요하다
    @GetMapping("/board/modify/{id}") // @PathVariable라는 것은 url이 들어 왔을때 {id}부분이 @PathVariable("id")로 인식이 되서 Integer id에 들어간다
    public String boardModify(@PathVariable("id") Integer id, Model model) { // @PathVariable("id")을 사용한다면 url에서 /뒤에 id=1 이렇게 쿼리 스트링으로 붙는게 아니라 /1 이렇게 깔금하게 작성이 가능하다

        model.addAttribute("board", boardService.boardView(id));

        return "boardmodify";
    }

    // url의 파라미터를 넘길때 방법이 2가지가 존재하는데 기존에 쓰던 쿼리 스트링을 쓰는 방법이 있고(?id=1)
    // PathVariable을 써서 "/board/update/{id}" {id}을 @PathVariable을 사용해서 id를 가져온다
    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board, Model model, MultipartFile file)  throws Exception{

        // 수정하는 법: 기존에 있던 객체를 불러와서 거기에다가 새로 작성한 객체의 내용을 덮어 씌워가지고
        // 저장을 하면 업데이트가 된다
        Board boardTemp = boardService.boardView(id); // 기존에 있던 객체 불러오기
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        boardService.write(boardTemp, file);



        model.addAttribute("message", "글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");
        return "message";
        //return "redirect:/board/list";
    }


}
