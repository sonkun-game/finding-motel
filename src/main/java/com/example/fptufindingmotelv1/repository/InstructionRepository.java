package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.InstructionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InstructionRepository extends JpaRepository<InstructionModel, Long> {
    @Query(value = "select new InstructionModel(ins.id, ins.title, ins.content, " +
            "r.id, r.roleName) from InstructionModel ins " +
            "join RoleModel r on ins.roleModel.id = r.id")
    List<InstructionModel> getAllInstruction();
}
