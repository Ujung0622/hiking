package project.group;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface GroupService {

    void insertGroup(Map map) throws Exception;
    List<Map> selectMainGroupList();
    List<Map> selectAllGroupList();
    void updateGroup(GroupVO vo);
    void deleteGroup(int groupNum);
    Map selectGroupDetail(int groupNum);
    List<Map> selectGroupDetailImage(int groupNum);
    List<Map> selectGroupsByKeyword(String keyword);

    //
    int joinGroup(Map map);

    int withdrawGroup(Map map);
    List selectWaitingList(Map map);
    int selectWaiting(Map map);
    int userAllowed(Map map);
    int userDisallowed(Map map);

    List<Integer> listApplied(int groupNum);

    int insertFavoriteGroup(Map map);
    int checkFavoriteGroup(Map map);
    int deleteFavoriteGroup(Map map);
}
