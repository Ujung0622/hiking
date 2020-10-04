package project.groupmedia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.common.FileUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service("groupMediaService")
public class GroupMediaServiceImpl implements GroupMediaService {

    @Resource(name = "groupMediaDAO")
    private GroupMediaDAO groupMediaDAO;

    @Resource(name = "fileUtils")
    private FileUtils fileUtils;

    @Transactional(propagation = Propagation.REQUIRED)
    public int insertGroupMedia(int groupNum, List<MultipartFile> files, String path) throws IOException {
        //groupNum이랑 file 경로는 map으로 담아서 dao에 날리고 파일을 여기서 따로 저장
        List list = fileUtils.saveFile(groupNum, files, path);
        return groupMediaDAO.insertGroupMedia(list);
    }

    public byte[] selectGroupMediaOne(int groupNum){
        return groupMediaDAO.selectGroupMediaOne(groupNum);
    }

    public List<byte[]> selectGroupMediaDetail(int groupNum){
        return groupMediaDAO.selectGroupMediaDetail(groupNum);
    }
}
