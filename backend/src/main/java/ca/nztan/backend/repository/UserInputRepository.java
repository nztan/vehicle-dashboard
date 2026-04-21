package ca.nztan.backend.repository;

import ca.nztan.backend.entity.UserInput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInputRepository extends JpaRepository<UserInput, Integer> {
}
