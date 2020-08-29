package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserModel, String> {
    UserModel findByGgAccount(String ggAccount);
    UserModel findByFbAccount(String fbAccount);
    UserModel findByUsername(String username);
    UserModel findByPhoneNumber(String phoneNumber);

    Boolean existsByUsername(String username);
    Boolean existsByPhoneNumber(String phone);
    Boolean existsByGgAccount(String ggAccount);
    Boolean existsByFbAccount(String fbAccount);

    @Query("select new UserModel(u.username, r.id, r.roleName, r.displayName, " +
            "u.fbAccount, u.ggAccount, u.phoneNumber, u.displayName, u.password, " +
            "ll.username, ll.amount, ll.unBanDate, rt.username, rt.gender, rt.career, rt.dob) from UserModel u " +
            "join RoleModel r on u.role.id = r.id " +
            "left outer join LandlordModel ll on u.username = ll.username " +
            "left outer join RenterModel rt on u.username = rt.username " +
            " where (u.username like %:username% or u.displayName like %:username%) " +
            "and (:roleId is null or u.role.id = :roleId)")
    Page<UserModel> searchUser(String username, Long roleId, Pageable pageable);

    @Query(value = "select new UserModel(u.username, r.id, r.roleName, r.displayName, " +
            "u.fbAccount, u.ggAccount, u.phoneNumber, u.displayName, u.password) from UserModel u " +
            "join RoleModel r on r.id = u.role.id " +
            "where (:username is null or u.username = :username)" +
            "and (:fbAccount is null or u.fbAccount = :fbAccount)" +
            "and (:ggAccount is null or u.ggAccount = :ggAccount)")
    UserModel getUserById(String username, String fbAccount, String ggAccount);


}
