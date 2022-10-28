package hellospring2.hellospring2.repository;

import hellospring2.hellospring2.domain.Member;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMemberRepository implements MemberRepository {

    //db에 붙이려면 이 datasource라는게 필요함 이녀석을 스프링한테 주입 받아야함
    // datasource.getConnection()하면 실제로 db랑 커넥팅이 되는 것임
    private final DataSource dataSource;

    public JdbcMemberRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Member save(Member member) {
        String sql =  "insert into member(name) values(?)";//파라미터 바인딩 위해 물음표

       Connection conn = null;
       PreparedStatement pstmt = null;//sql을 넣는것
       ResultSet rs = null;//결과를 받는것
       try {
           conn = getConnection();
           pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
           pstmt.setString(1, member.getName());
           pstmt.executeUpdate();
           rs =pstmt.getGeneratedKeys();

           if(rs.next()) {
               member.setId(rs.getLong(1));
           }else {
               throw new SQLException("id 조회 실패");
           }
           return member;

       } catch (Exception e) {
           throw new IllegalStateException(e);

       } finally {
           close(conn,pstmt, rs);
           //쓰고 나면 리소스 꼭 반환해야지 아니면 데이터커넥션 계속 쌓임
       }
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "select * from member where id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;//sql을 넣는것
        ResultSet rs = null;//결과를 받는것
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();//조회는 executequery

            if(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);

            }else {
                return Optional.empty();
            }

        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn,pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findByName(String name) {
        String sql = "select * from member where name = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;//sql을 넣는것
        ResultSet rs = null;//결과를 받는것
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();//조회는 executequery

            if(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);

            }
            return Optional.empty();


        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn,pstmt, rs);
        }
    }

    @Override
    public List<Member> findAll() {

        String sql = "select * from member";
        Connection conn = null;
        PreparedStatement pstmt = null;//sql을 넣는것
        ResultSet rs = null;//결과를 받는것
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();//조회는 executequery
            List<Member> members = new ArrayList<>();
            while(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                members.add(member);
            }
            return members;

        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn,pstmt, rs);
        }
    }
    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }

    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstmt !=null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn!=null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
