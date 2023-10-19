package co.kr.lotteon.controller.cs;

import co.kr.lotteon.dto.cs.*;
import co.kr.lotteon.entity.cs.CsEntity;
import co.kr.lotteon.entity.cs.CsGroupEntity;
import co.kr.lotteon.service.CsService;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Log4j2
@Controller
public class CsController {

    @Autowired
    private CsService csService;

    /////////////////////////////////////////////
    // 인덱스
    /////////////////////////////////////////////
    @GetMapping(value = {"/cs", "/cs/index"})
    public String index(Model model, PageRequestDTO pageRequestDTO, Pageable pageable) {
        csService.loginStatus();
        List<CsCate1DTO> faqCate1List = csService.faqCate1List();
        log.info("faqCate1List : " + faqCate1List);

        // index에선 list를 5개씩만 출력하기 위함.
        pageRequestDTO.setSize(5);

        pageRequestDTO.setGroup("notice");
        PageResponseDTO notice = csService.indexList(pageRequestDTO);
        log.info("index notice list : " + notice);
        log.info("notice size() : " + notice.getCsList().size());

        pageRequestDTO.setGroup("qna");
        PageResponseDTO qna = csService.indexList(pageRequestDTO);
        log.info("index qna list : " + qna);
        log.info("qna size() : " + notice.getCsList().size());

        model.addAttribute("ntc", notice);
        model.addAttribute("cate1List", faqCate1List);
        model.addAttribute("qna", qna);

        return "/cs/index";
    }



    /////////////////////////////////////////////
    // 자주 묻는 질문
    /////////////////////////////////////////////
    @GetMapping("/cs/faq/list")
    public String faqList(HttpServletRequest request, Model model, PageRequestDTO pageRequestDTO) {
        csService.layout(request, model, pageRequestDTO);
        csService.boardList(model, pageRequestDTO);

        return "/cs/faq/list";
    }

    @GetMapping("/cs/faq/view")
    public String faqView(HttpServletRequest request, Model model, PageRequestDTO pageRequestDTO) {
        csService.layout(request, model, pageRequestDTO);
        if (pageRequestDTO.getNo() == 0) {
            // 비정상적 접근( no == 0 일 때,)
            return "redirect:/cs/faq/list";
        }
        CsDTO dto = csService.view(pageRequestDTO.getNo(), model);

        return "/cs/faq/view";
    }



    /////////////////////////////////////////////
    // 문의사항
    /////////////////////////////////////////////
    @GetMapping("/cs/qna/list")
    public String qnaList(HttpServletRequest request, Model model, PageRequestDTO pageRequestDTO) {
        csService.layout(request, model, pageRequestDTO);
        csService.boardList(model, pageRequestDTO);

        return "/cs/qna/list";
    }

    @GetMapping("/cs/qna/view")
    public String qnaView(HttpServletRequest request, Model model, PageRequestDTO pageRequestDTO) {
        csService.layout(request, model, pageRequestDTO);
        if (pageRequestDTO.getNo() == 0) {
            // 비정상적 접근( no == 0 일 때,)
            return "redirect:/cs/qna/list";
        }
        CsDTO dto = csService.view(pageRequestDTO.getNo(), model);
        if(dto.getParent() < 0) {
            CsDTO answer = csService.findByParent(dto.getNo());
            model.addAttribute("answer", answer);
            log.info("answer : " + answer);
        }else {
            log.info("answer none");
        }

        return "/cs/qna/view";
    }

    // 문의사항 게시글 작성 폼
    @GetMapping("/cs/qna/write")
    public String qnaWrite(HttpServletRequest request, Model model, PageRequestDTO pageRequestDTO) {
        csService.layout(request, model, pageRequestDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);
        log.info("group : " + pageRequestDTO.getGroup());
        log.info("cate1 : " + pageRequestDTO.getCate1());
        log.info("cate2 : " + pageRequestDTO.getCate2());
        log.info("pg : " + pageRequestDTO.getPg());

        // cate1_name 출력용 (경로... cate1.cate1_name)
        List<CsCate1DTO> cate1 = csService.findByCate(pageRequestDTO.getGroup());
        model.addAttribute("cate1", cate1);
        log.info("cate1 result : " + cate1);

        // cate2_name 출력용 (경로... cate2.cate2_name)
        List<CsCate2DTO> cate2 = csService.findByCate1(pageRequestDTO.getCate1());
        model.addAttribute("cate2", cate2);
        log.info("cate2 result : " + cate2);

        // json url을 위한 contextPath
        String path = request.getContextPath();
        String url = path + "/cs/cate2";
        model.addAttribute("url", url);
        log.info("path : " + path);
        log.info("url : " + url);

        log.info(" ========================================= ");
        log.info("group   : " + pageRequestDTO.getGroup());
        log.info("cate1   : " + pageRequestDTO.getCate1());
        log.info("cate2   : " + pageRequestDTO.getCate2());
        log.info("uid     : " + pageRequestDTO.getUid());
        log.info("title   : " + pageRequestDTO.getTitle());
        log.info("content : " + pageRequestDTO.getContent());
        log.info(" ========================================= ");

        return "/cs/qna/write";
    }

    // 문의사항 게시글 작성 후 전송
    @PostMapping("/cs/qna/write")
    public String qnaWrite(PageRequestDTO pageRequestDTO) {

        log.info(" ========================================= ");
        log.info("group   : " + pageRequestDTO.getGroup());
        log.info("cate1   : " + pageRequestDTO.getCate1());
        log.info("cate2   : " + pageRequestDTO.getCate2());
        log.info("uid     : " + pageRequestDTO.getUid());
        log.info("title   : " + pageRequestDTO.getTitle());
        log.info("content : " + pageRequestDTO.getContent());
        log.info(" ========================================= ");

        int result = csService.insertQna(pageRequestDTO);
        log.info("success : " + result);

        return "redirect:/cs/qna/list?cate1=" + pageRequestDTO.getCate1()
                                    + "&success=" + result;
    }

    // 문의사항 게시글 작성시, cate loading
    @ResponseBody
    @RequestMapping(value = "/cs/cate2", method = RequestMethod.POST)
    public HashMap<String, Object> cateJson(@RequestBody HashMap<String, Object> selectCate) {
        Object selectCate1 = selectCate.get("selectCate1");
        log.info("selectCate1-Object : " + selectCate1);
        log.info("selectCate1-String : " + selectCate1.toString());

        List<CsCate2DTO> category = csService.findByCate1(selectCate1.toString());
        log.info("category : " + category);

        selectCate.put("returnCate", category);
        return selectCate;
    }

    // 문의사항 게시글 삭제
    @ResponseBody
    @RequestMapping(value = "/cs/delete", method = RequestMethod.DELETE)
    public HashMap<String, Object> qnaDelete(@RequestBody HashMap<String, Object> delete) {
        int no = Integer.parseInt(delete.get("no").toString());
        String cate1 = delete.get("cate1").toString();
        log.info("no    : " + no);
        log.info("cate1 : " + cate1);

        String result     = csService.deleteBoard(no, cate1);
        String successUrl = "/cs/qna/list?cate1=" + cate1;
        log.info("result : " + result);
        delete.put("result", result);
        delete.put("url", successUrl);

        return delete;
    }


    /////////////////////////////////////////////
    // 공지사항
    /////////////////////////////////////////////
    @GetMapping("/cs/notice/list")
    public String noticeList(HttpServletRequest request, Model model, PageRequestDTO pageRequestDTO) {
        csService.layout(request, model, pageRequestDTO);
        csService.boardList(model, pageRequestDTO);

        return "/cs/notice/list";
    }

    @GetMapping("/cs/notice/view")
    public String noticeView(HttpServletRequest request, Model model, PageRequestDTO pageRequestDTO) {
        csService.layout(request, model, pageRequestDTO);
        if (pageRequestDTO.getNo() == 0) {
            // 비정상적 접근( no == 0 일 때,)
            return "redirect:/cs/notice/list";
        }
        CsDTO dto = csService.view(pageRequestDTO.getNo(), model);

        return "/cs/notice/view";
    }




}