package org.example.fileupload.repo;



import org.example.fileupload.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // Agar telefon raqami bo'lsa qo'shishingiz mumkin (hozircha kommentda)
    // Optional<User> findByPhoneNumber(String phoneNumber);
    // boolean existsByPhoneNumber(String phoneNumber);

//    /**
//     * Userni va uning barcha fayllarini birga yuklab olish (eager fetch)
//     */
//    @Query("""
//    SELECT u FROM User u
//    LEFT JOIN FETCH u.uploadedFiles
//    WHERE u.id = :id
//    """)
//    Optional<User> findByIdWithFiles(@Param("id") Integer id);

    // Qo'shimcha qidiruvlar kerak bo'lsa misollar:
    // List<User> findByRole(Role role);
    // List<User> findByFullNameContainingIgnoreCase(String namePart);
}

