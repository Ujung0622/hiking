package project.board;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("boardService")
public class BoardServiceImpl implements BoardService{

    @Resource(name = "boardDAO")
    private BoardDAO boardDAO;

    public List selectBoard(Map map){
        return boardDAO.selectBoard(map);
    }
    public int selectBoardAllCount(String csPostType){
        return boardDAO.selectBoardAllCount(csPostType);
    };
    public List selectBoardDetail(int csPostNum){
        return boardDAO.selectBoardDetail(csPostNum);
    }
}
