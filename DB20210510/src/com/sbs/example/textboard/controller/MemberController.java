package com.sbs.example.textboard.controller;

import com.sbs.example.textboard.Container;
import com.sbs.example.textboard.dto.Member;
import com.sbs.example.textboard.service.MemberService;
import com.sbs.example.textboard.util.util;

public class MemberController extends Controller {

	private MemberService memberService;

	public MemberController() {
		memberService = Container.memberService;
	}


	public void join(String cmd) {
		String loginId;
		String loginPw;
		String loginPwConfirm;
		String name;

		System.out.println("==회원 가입==");
		// 아이디 입력
		while (true) {
			System.out.printf("로그인 아이디 : ");
			loginId = sc.nextLine().trim();

			if (loginId.length() == 0) {
				System.out.println("아이디를 입력해주세요.");
				continue;
			}

			boolean isLoginIdDup = memberService.isLoginIdDup(loginId);

			if (isLoginIdDup) {
				System.out.printf("%s는 이미 사용중인 아이디입니다. 다시 입력해주세요.\n", loginId);
				continue;
			}

			break;
		}

		// 비밀번호 입력
		while (true) {
			System.out.printf("비밀번호 입력 : ");
			loginPw = sc.nextLine().trim();

			if (loginPw.length() == 0) {
				System.out.println("비밀번호를 입력해주세요.");
				continue;
			}

			boolean loginPwConfirmIsSame = true;

			while (true) {
				System.out.printf("로그인 비밀번호 확인 : ");
				loginPwConfirm = sc.nextLine().trim();

				if (loginPwConfirm.length() == 0) {
					System.out.println("비밀번호 확인을 입력해주세요.");
					continue;
				}

				if (loginPw.equals(loginPwConfirm) == false) {
					System.out.println("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
					loginPwConfirmIsSame = false;
					break;
				}

				break;

			}
			if (loginPwConfirmIsSame) {
				break;
			}
		}
		while (true) {
			System.out.printf("이름 : ");
			name = sc.nextLine().trim();

			if (name.length() == 0) {
				System.out.println("이름을 입력해주세요.");
				continue;
			}
			break;
		}

		int id = memberService.join(loginId, loginPw, name);

		System.out.printf("%s님 환영합니다.\n", name);

	}

	public void login(String cmd) {
		String loginId;
		String loginPw;

		System.out.println("==로그인==");
		
		int loginIdTryMaxCount = 3;
		int loginIdTryCount = 0;
		
		// 아이디 입력
		while (true) {
			if(loginIdTryCount >= loginIdTryMaxCount) {
				System.out.println("아이디를 확인 후 다시 시도해주세요.");
				return;
			}
			
			System.out.printf("로그인 아이디 : ");
			loginId = sc.nextLine().trim();

			if (loginId.length() == 0) {
				loginIdTryCount++;
				System.out.println("아이디를 입력해주세요.");
				continue;
			}

			boolean isLoginIdDup = memberService.isLoginIdDup(loginId);

			if (isLoginIdDup == false) { // 입력된 아이디 확인
				System.out.printf("%s(은)는 존재하지 않는 아이디 입니다.\n", loginId);
				continue;
			}

			break;
		}
		
		Member member = memberService.getMemberByLoginId(loginId);

		int loginPwTryMaxCount = 3;
		int loginPwTryCount = 0;

		// 비밀번호 입력
		while (true) {
			if (loginPwTryCount >= loginPwTryMaxCount) {
				System.out.println("비밀번호 확인 후 다시 시도해주세요.");
				return;
			}

			System.out.printf("비밀번호 입력 : ");
			loginPw = sc.nextLine().trim();

			if (loginPw.length() == 0) {
				System.out.println("비밀번호를 입력해주세요.");
				continue;
			}
			
			if(member.loginPw.equals(util.sha256(loginPw)) == false) {
				loginPwTryCount++;
				System.out.println("비밀번호가 일치하지 않습니다.");
				continue;
			}
			
			System.out.printf("%s님 환영합니다.\n", member.name);
			
			Container.session.login(member);
			
			break;
		}

	}


	public void whoami(String cmd) {
		if(Container.session.isLogined() == false) {
			System.out.println("로그인 상태가 아닙니다.");
		} else {
			System.out.printf("%s님이 로그인중입니다.\n", Container.session.loginedMember.loginId);
		}		
	}


	public void logout(String cmd) {
		Container.session.logut();
		System.out.println("로그아웃 되었습니다.");
	}

}
