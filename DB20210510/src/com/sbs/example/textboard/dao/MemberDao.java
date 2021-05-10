package com.sbs.example.textboard.dao;

import java.util.Map;

import com.sbs.example.textboard.Container;
import com.sbs.example.textboard.dto.Member;
import com.sbs.example.textboard.util.DBUtil;
import com.sbs.example.textboard.util.SecSql;
import com.sbs.example.textboard.util.util;

public class MemberDao {


	public boolean isLoginIdDup(String loginId) {

		SecSql sql = new SecSql();

		sql.append("SELECT COUNT(*) > 0");
		sql.append("FROM member");
		sql.append("WHERE loginId = ?", loginId);

		return DBUtil.selectRowBooleanValue(Container.conn, sql); // conn, sql을 리턴 시켜주어야 함
	}

	public int join(String loginId, String loginPw, String name) {

		SecSql sql = new SecSql();

		sql.append("INSERT INTO `member`");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", loginId = ?", loginId);
		sql.append(", loginPw = ?", util.sha256(loginPw));
		sql.append(", `name` = ?", name);

		int id = DBUtil.insert(Container.conn, sql);

		return id;
	}

	public Member getMemberByLoginId(String loginId) {
		SecSql sql = new SecSql();

		sql.append("SELECT *");
		sql.append("FROM member");
		sql.append("WHERE loginId = ?", loginId);
		
		Map<String, Object> memberMap = DBUtil.selectRow(Container.conn, sql);
		
		return new Member(memberMap);
	}

}