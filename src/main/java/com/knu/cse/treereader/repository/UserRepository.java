package com.knu.cse.treereader.repository;

import com.knu.cse.treereader.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
//기본적으로 제공해주는거에 더해서 service에서 사용할 함수를 정의해주고
// 동시에 db랑 연결되는애그함수들이 쿼리문으로 바꿔준다
// Repository는 정해진 규칙대로 메소드 이름만 적으면 insert, select, delete 같은걸 자동으로 해줌
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserId(String userId);
    User findByUserIdAndPassword(String userId, String password);
}

//JpaRepository<어떤 entity에 대해, pk타입>
