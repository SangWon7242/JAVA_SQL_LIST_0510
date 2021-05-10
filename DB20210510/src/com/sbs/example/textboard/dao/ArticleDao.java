package com.sbs.example.textboard.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sbs.example.textboard.Container;
import com.sbs.example.textboard.dto.Article;
import com.sbs.example.textboard.util.DBUtil;
import com.sbs.example.textboard.util.SecSql;

public class ArticleDao {

	public int add(int memberId, String title, String body) {

		SecSql sql = new SecSql();

		sql.append("INSERT INTO article");
		sql.append(" SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", memberId = ?", memberId);
		sql.append(", title = ?", title); // ? -> 치환이 된다.s
		sql.append(", body = ?", body);
		

		int id = DBUtil.insert(Container.conn, sql);

		return id;
	}

	public boolean articleExists(int id) {

		SecSql sql = new SecSql();

		sql.append("SELECT COUNT(*) AS cnt"); // 카운트가 article 카운트로 들어감
		sql.append("FROM article");
		sql.append("WHERE id = ?", id);

		return DBUtil.selectRowBooleanValue(Container.conn, sql);
	}

	public void delete(int id) {

		SecSql sql = new SecSql();

		sql.append("DELETE FROM article");
		sql.append("WHERE id = ?", id);

		DBUtil.delete(Container.conn, sql);

	}

	public Article getArticleById(int id) {

		SecSql sql = new SecSql();

		sql.append("SELECT *"); // all 로 받으면 int로 못받는다. 그러므로 셀렉로우로 받음
		sql.append("FROM article");
		sql.append("WHERE id = ?", id);

		Map<String, Object> articleMap = DBUtil.selectRow(Container.conn, sql);

		if (articleMap.isEmpty()) {
			return null; // 내가 선택한 id가 없으면 반환하면 안됨.
		}

		return new Article(articleMap);
	}

	public void update(int id, String title, String body) {

		SecSql sql = new SecSql();

		sql.append("UPDATE article");
		sql.append(" SET updateDate = NOW()");
		sql.append(", title = ?", title);
		sql.append(", body = ?", body);
		sql.append("WHERE id = ?", id);

		DBUtil.update(Container.conn, sql);

	}

	public List<Article> getArticles() {
		
		

		SecSql sql = new SecSql();

		sql.append("SELECT A.* , M.name As extra__writer");
		sql.append(" FROM article AS A");
		sql.append(" INNER JOIN member AS M");
		sql.append(" ON A.memberId = M.id");
		sql.append(" ORDER BY A.id DESC");
		// select 는 모든 데이터가 List로 들어온다.

		List<Map<String, Object>> articlesListMap = DBUtil.selectRows(Container.conn, sql);
		// Map<key, value> 이런식의 형태
		// 쓰는 이유 : 편하게 쓰려고
		
		List<Article> articles = new ArrayList<>();
		
		for (Map<String, Object> articleMap : articlesListMap) {
			articles.add(new Article(articleMap));
		}
		
		return articles;
	}

}
