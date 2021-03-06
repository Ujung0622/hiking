package project.user.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.plexus.util.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import project.user.auth.SNSLogin;
import project.user.auth.SnsValue;
import project.user.dto.LoginDTO;
import project.user.service.MailService;
import project.user.service.MypageService;
import project.user.service.UserService;
import project.user.vo.UserVO;

@Controller("userController")
@RequestMapping("/user/*")
public class UserControllerImpl implements UserController {

	public static final Logger logger = LoggerFactory.getLogger(UserControllerImpl.class);
	private static final String LOGIN = "LOGIN";

	@Autowired
	UserService userService;

	@Autowired
	MypageService mypageService;

	@Autowired
	private SnsValue naverSns;

	@Autowired
	private MailService mailService;

//	@Autowired
//	private SnsValue googleSns;

	@Autowired
	public UserControllerImpl(UserService userService) {
		this.userService = userService;
	}

	// 회원가입 완료
	@RequestMapping(value = "/insertUser.do", method = RequestMethod.POST)
	public ModelAndView insertUser(UserVO userVO, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 비밀번호 해싱
		String hashedPw = BCrypt.hashpw(userVO.getPassword(), BCrypt.gensalt(10));
		userVO.setPassword(hashedPw);

		logger.info("insertUser");
		// DB에 기본정보 추가
		userService.insertUser(userVO);

		// 임의의 authKey 생성 & 이메일 발송
		String authKey = mailService.getKey(6);
		request.setCharacterEncoding("utf-8");
		mailService.sendAuthMail(userVO.getEmail(), authKey);
		userVO.setAuthKey(authKey);

		Map<String, String> map = new HashMap<String, String>();
		map.put("id", userVO.getId());
		map.put("email", userVO.getEmail());
		map.put("authKey", userVO.getAuthKey());

		userService.updateAuthKey(map);
		ModelAndView mav = new ModelAndView("/user/signUpMail");
		mav.addObject("map", map);
		return mav;
	}

	// 가입인증메일 다시 보내
	@RequestMapping(value = "/resendMail.do", method = RequestMethod.POST)
	public ModelAndView resendMail(UserVO userVO, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setCharacterEncoding("utf-8");
		mailService.sendAuthMail(userVO.getEmail(), userVO.getAuthKey());

		Map<String, String> map = new HashMap<String, String>();
		map.put("id", userVO.getId());
		map.put("email", userVO.getEmail());
		map.put("authKey", userVO.getAuthKey());

		ModelAndView mav = new ModelAndView("/user/signUpMail");
		mav.addObject("map", map);
		return mav;
	}

	@RequestMapping(value = "/signUpConfirm.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView signUpConfirm(@RequestParam Map<String, String> map, ModelAndView mav) {
		// email, authKey 가 일치할경우 authStatus 업데이트
		userService.updateAuthStatus(map);
		logger.info("authKey update 해드림.");
		mav.setViewName("redirect:/user/logInView.do");
		return mav;
	}

	@RequestMapping(value = "/insertUser2.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String insertUser2(HttpSession httpSession, LoginDTO loginDTO, RedirectAttributes reAttr) throws Exception {
		Map<String, Object> snsUser = (Map<String, Object>) httpSession.getAttribute("snsUser");
		httpSession.removeAttribute("snsUser");
		loginDTO.setId((String) snsUser.get("id"));
		loginDTO.setPassword((String) snsUser.get("password"));
		loginDTO.setName((String) snsUser.get("name"));
		// loginDTO.setNickName((String) snsUser.get("nickName"));
		loginDTO.setSex((int) snsUser.get("sex"));
		String hashedPw = BCrypt.hashpw((String) snsUser.get("password"), BCrypt.gensalt());
		snsUser.put("password", hashedPw);
		logger.info("insertUser2");
		userService.insertUser2(snsUser);
		reAttr.addFlashAttribute("loginDTO", loginDTO);

		return "redirect:/user/logIn.do";
	}

	@RequestMapping(value = "/idCheck.do", method = RequestMethod.GET)
	@ResponseBody
	public String idCheck(@RequestParam("id") String id) throws Exception {
		int rst = userService.idCheck(id);
		String result = "0";
		if (rst != 0) {
			result = "1";
		}
		return result;
	}

	@RequestMapping(value = "/emailCheck.do", method = RequestMethod.GET)
	@ResponseBody
	public String emailCheck(@RequestParam("email") String email) throws Exception {
		int rst = userService.emailCheck(email);
		String result = "0";
		if (rst != 0) {
			result = "1";
		}
		return result;
	}

	@RequestMapping(value = "/nickNameCheck.do", method = RequestMethod.GET)
	@ResponseBody
	public String nickNameCheck(@RequestParam("nickName") String nickName) throws Exception {
		int rst = userService.nickNameCheck(nickName);
		String result = "0";
		if (rst != 0) {
			result = "1";
		}
		return result;
	}

	@Override
	@RequestMapping(value = "/idEmailCheck.do", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String idEmailCheck(@RequestParam("id") String id, @RequestParam("email") String email) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("email", email);
		int rst = userService.idEmailCheck(map);
		String result = "0";
		if (rst != 0) {
			result = "1";
		}
		return result;
	}

	@RequestMapping(value = "/logInView.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String logInView(Model model) {
		SNSLogin naverLogin = new SNSLogin(naverSns);
//			SNSLogin googleLogin = new SNSLogin(googleSns);
		model.addAttribute("naver_url", naverLogin.getNaverAuthURL());
//			model.addAttribute("google_url", googleLogin.getGoogleAuthURL());
		return "/user/logInView";

	}

	@RequestMapping(value = "{snsService}/callback", method = { RequestMethod.GET, RequestMethod.POST })
	public String snsLoginCallBack(@PathVariable String snsService, @RequestParam String code,
			RedirectAttributes reAttr, HttpSession httpSession) throws Exception {
		logger.info("snsLoginCallback: service={}", snsService);
		SnsValue sns = null;
		if (StringUtils.equals("naver", snsService))
			sns = naverSns;
//			else 
//				sns = googleSns;

		SNSLogin snsLogin = new SNSLogin(sns);
		Map<String, Object> snsUser = snsLogin.getUserProfile(code);
		UserVO snsUserInfo = userService.getBySns(snsUser);
		if (snsUserInfo == null) { // 가입하지 않은 회원
			httpSession.setAttribute("snsUser", snsUser);
			return "/user/insertPwdView";
		} else { // 이미 가입한 회원
			LoginDTO loginDTO = new LoginDTO();
			loginDTO.setId(snsUserInfo.getId());
			httpSession.setAttribute("loginDTO", loginDTO);
			return "/user/snsUserPwdCheck";
		}

	}

	@RequestMapping(value = "/insertPwd.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String insertPwd(@RequestParam("password2") String password2, HttpSession httpSession,
			RedirectAttributes reAttr, HttpServletRequest request) throws Exception {
		Map<String, Object> snsUser = (Map<String, Object>) httpSession.getAttribute("snsUser");
		httpSession.removeAttribute("snsUser");
		snsUser.put("password", password2);
		httpSession.setAttribute("snsUser", snsUser);
		request.setAttribute("snsUser", snsUser);
		return "/user/signUpEnd";
	}

	@RequestMapping(value = "/user/snsUserPwdCheck.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String snsUserPwdCheck(@RequestParam("password") String password, HttpSession httpSession,
			RedirectAttributes reAttr) throws Exception {
		LoginDTO loginDTO = (LoginDTO) httpSession.getAttribute("loginDTO");
		loginDTO.setPassword(password);
		reAttr.addFlashAttribute("loginDTO", loginDTO);
		return "redirect:/user/logIn.do";
	}

	@RequestMapping(value = "/logIn.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView logIn(LoginDTO loginDTO, HttpSession httpSession, ModelAndView mav) throws Exception {
		UserVO userVO = userService.logIn(loginDTO);

		if (userVO == null || !BCrypt.checkpw(loginDTO.getPassword(), userVO.getPassword())) {
			mav.setViewName("/user/logInFailed");
			return mav;
		}
		mav.setViewName("/home");
		mav.addObject("userVO", userVO);

		// 로그인 유지를 선택할 경우
		if (loginDTO.isUseCookie()) {
			int amount = 60 * 60 * 24 * 7; // 7일
			Date sessionLimit = new Date(System.currentTimeMillis() + (1000 * amount));// 로그인 유지기간 설정
			userService.keepLogin(userVO.getId(), httpSession.getId(), sessionLimit);
		}
		return mav;
	}

	@RequestMapping(value = "/logOut.do", method = RequestMethod.GET)
	public String logOut(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession)
			throws Exception {
		UserVO userVO = new UserVO();
		userVO.setId((String) httpSession.getAttribute(LOGIN));
		if (userVO != null) {
			httpSession.removeAttribute(LOGIN);
			httpSession.invalidate();
			userService.removeSessionId(httpSession.getId());
			Cookie loginCookie = WebUtils.getCookie(request, "loginCookie");
			if (loginCookie != null) {
				loginCookie.setPath("/");
				loginCookie.setMaxAge(0);
				response.addCookie(loginCookie);
				userService.keepLogin(userVO.getId(), "none", new Date());
			}
		}
		return "/user/logOut";
	}

	@Override
	@RequestMapping(value = "/withdrawalCheck.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView withdrawalCheck(HttpSession httpSession) throws Exception {
		ModelAndView mav = new ModelAndView();
		String id = (String) httpSession.getAttribute(LOGIN);
		Map<String, Integer> mandateC = userService.mandateCheck(id);
		int group1 = Integer.parseInt(String.valueOf(mandateC.get("GROUP1")));
		int group2 = Integer.parseInt(String.valueOf(mandateC.get("GROUP2")));
		if ( group1!= 0 || group2!= 0  ) {
			mav.addObject("mandateC", mandateC);
			mav.setViewName("/user/withdrawFailed");
		} else {
			List<Map> memberC = userService.memberCheck(id);
			if (!memberC.isEmpty()) {
				userService.withdrawUserUpdateStatus(memberC);
			}
			mav.setViewName("/user/withdrawalCheck");
		}
		return mav;
	}

	@Override
	@RequestMapping(value = "/withdrawal.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String withdrawal(UserVO userVO, HttpSession httpSession) throws Exception {
		userVO.setId((String) httpSession.getAttribute(LOGIN));

		userService.withdrawal(userVO);
		httpSession.removeAttribute("LOGIN");

		httpSession.invalidate();
		userService.removeSessionId(httpSession.getId());
		return "/user/withdrawal";
	}

	@RequestMapping(value = "/user/searchId.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchId(@RequestParam("email") String email, HttpServletRequest request) throws Exception {
		UserVO userVO = new UserVO();
		userVO.setEmail(email);
		String id = userService.searchId(email);
		request.setCharacterEncoding("utf-8");
		mailService.sendMail(userVO.getEmail(), "산오름 아이디 찾기 안내 메일",
				"회원님이 산오름에 요청하신 '아이디 찾기' 문의에 대해 안내 해 드립니다. \n" + "회원님의 아이디는:  " + id);
		return "/user/sendMailEnd";
	}

	@RequestMapping(value = "/sendTempPwd.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String sendTempPwd(@RequestParam("id") String id, @RequestParam("email") String email,
			HttpServletRequest request) throws Exception {
		UserVO userVO = new UserVO();
		userVO.setEmail(email);
		// 임의의 authKey 생성 & 이메일 발송
		String tempPwd = mailService.getKey(8);
		request.setCharacterEncoding("utf-8");
		mailService.sendMail(userVO.getEmail(), "산오름 임시 비밀번호 발급", "회원님이 산오름에 요청하신 임시비밀번호를 안내해 드립니다. \n"
				+ "해당 비밀번호는 보안이 취약하니 로그인 후 즉시 마이페이지> 나의 정보 수정> 비밀번호변경을 권장합니다.\n " + "임시 비밀번호:  " + tempPwd);

		String hashedPwd = BCrypt.hashpw(tempPwd, BCrypt.gensalt(10));
		userVO.setPassword(hashedPwd);
		userVO.setId(id);
		mypageService.updatePwd(userVO);
		return "/user/sendMailEnd";
	}

}
